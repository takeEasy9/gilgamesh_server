package com.gilgamesh.biz.service.security;

import com.gilgamesh.biz.entity.dto.TokenDTO;
import com.gilgamesh.common.entity.property.JwtProperty;
import com.gilgamesh.common.entity.security.GilgameshUserDetail;
import com.gilgamesh.common.enums.BizCodeMsg;
import com.gilgamesh.common.enums.EnumValue;
import com.gilgamesh.common.enums.SystemEnums;
import com.gilgamesh.common.exceptions.BusinessException;
import com.gilgamesh.common.redis.RedisService;
import com.gilgamesh.common.utils.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestUtils;

import java.security.KeyPair;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description JWT服务类，用于处理JSON Web Token相关的操作
 * @createDate 2025/7/20 15:49
 * @since 1.0.0
 */
@Component
public class JwtService {
    private final Logger logger = LoggerFactory.getLogger(JwtService.class);

    /**
     * JWT配置属性
     */
    private final JwtProperty jwtProperty;

    /**
     * JWT 签名 RSA 算法密钥对
     */
    private final KeyPair jwtSignKeyPair;

    /**
     * Redis实现服务
     */
    private final RedisService redisService;

    /**
     * 用户详情服务
     */
    private final UserDetailsService userDetailsService;

    /**
     * JWT解析器
     */
    private final JwtParser jwtParser;

    /**
     * 维护「用户ID→可重入锁」，仅锁定同一用户的续期请求
     * ConcurrentHashMap保证线程安全，ReentrantLock支持可重入（避免死锁）
     */
    private final Map<String, ReentrantLock> userLockMap = new ConcurrentHashMap<>();


    @Autowired
    public JwtService(JwtProperty jwtProperty,
                      @Qualifier("jwtSignEcdsaKeyPair") KeyPair jwtSignKeyPair,
                      RedisService redisService,
                      @Qualifier("userDetailsService") UserDetailsService userDetailsService,
                      JwtParser jwtParser) {
        this.jwtProperty = jwtProperty;
        this.jwtSignKeyPair = jwtSignKeyPair;
        this.redisService = redisService;
        this.userDetailsService = userDetailsService;
        this.jwtParser = jwtParser;
    }

    /**
     * 生成 token
     *
     * @param refreshJti          refresh token唯一标识 uuid
     * @param clientType          客户端类型
     * @param gilgameshUserDetail GilgameshUserDetail
     * @return TokenDTO
     */
    public TokenDTO generateToken(String refreshJti, String clientType, GilgameshUserDetail gilgameshUserDetail) {
        if (StringUtil.isEmpty(refreshJti) || gilgameshUserDetail == null) {
            throw new BusinessException(BizCodeMsg.ACCESS_PARAM_INVALID);
        }
        // 生成 JWT refresh token
        String refreshToken = this.generateRefreshToken(refreshJti, clientType, gilgameshUserDetail);
        // 生成 JWT access token
        String accessJti = UUIDUtil.genUuidV7WithoutHyphen();
        String accessToken = this.generateAccessToken(refreshJti, accessJti, clientType, gilgameshUserDetail);
        // 保存Token到Redis
        this.saveTokenToRedis(refreshJti, gilgameshUserDetail.getUserId(), clientType, accessJti, accessToken);
        TokenDTO tokenDTO = new TokenDTO(accessToken, accessJti, refreshToken, refreshJti);
        tokenDTO.setAtExpiresIn(jwtProperty.getAccessTokenExpireMs());
        tokenDTO.setRtExpiresIn(jwtProperty.getRefreshTokenExpireMs());
        return tokenDTO;
    }

    /**
     * 保存Token到Redis
     *
     * @param refreshJti refresh token唯一标识 uuid
     * @param userId     用户ID
     * @param clientType 客户端类型
     * @param accessJti  access token唯一标识 uuid
     */
    private void saveTokenToRedis(String refreshJti, String userId, String clientType, String accessJti, String accessToken) {
        if (StringUtil.isEmpty(refreshJti) || StringUtil.isEmpty(userId) || StringUtil.isEmpty(clientType) || StringUtil.isEmpty(accessJti)) {
            throw new BusinessException(BizCodeMsg.ACCESS_PARAM_INVALID);
        }
        try {
            // redis key
            String redisHashKey = String.format(ConstantUtil.JWT_TOKEN_REDIS_KEY_FORMAT, refreshJti);
            // 2. 批量写入Hash + 设置过期时间
            List<String> keys = Collections.singletonList(redisHashKey);
            String expireSeconds = String.valueOf(jwtProperty.getRefreshTokenExpireMs() / 1000);
            // ARGV[1] → 过期时间（秒）,ARGV[2...] → 哈希字段-值对（如 field1, value1, field2, value2...）
            Object[] args = new Object[]{expireSeconds,
                    ConstantUtil.TOKEN_HASH_FIELD_ACCESS_TOKEN, accessToken,
                    ConstantUtil.TOKEN_HASH_FIELD_UID, userId,
                    ConstantUtil.TOKEN_HASH_FIELD_STATUS, SystemEnums.JwtTokenStatus.JWT_TOKEN_STATUS_VALID.getValue(),
                    ConstantUtil.TOKEN_HASH_FIELD_RT_AT_ROTATE, "0",
            };
            // 3. 执行Lua脚本（原子操作：HMSET + EXPIRE）
            Long saveTokenResult = redisService.executeBySha1(ConstantUtil.REDIS_SCRIPT_KEY_SAVE_TOKEN, keys, args);
            if (saveTokenResult == null || saveTokenResult == 0) {
                logger.error("保存Token失败,refreshJti={},userId={}", refreshJti, userId);
            } else {
                logger.info("保存Token成功,refreshJti={},userId={}", refreshJti, userId);
            }
            // 3. 维护userId → JTI列表
            String userJtisKey = String.format(ConstantUtil.JWT_USER_JTI_REDIS_KEY_FORMAT, userId, clientType);
            Long userJtiResult = redisService.executeBySha1(ConstantUtil.REDIS_SCRIPT_KEY_USER_JTI_LIST,
                    Collections.singletonList(userJtisKey),
                    refreshJti, expireSeconds, String.valueOf(ConstantUtil.USER_JTI_LIST_MAX_SIZE),
                    SystemEnums.JwtTokenStatus.JWT_TOKEN_STATUS_REVOKED.getValue());
            if (userJtiResult == null || userJtiResult == 0) {
                logger.error("保存用户 {} jti列表失败,refreshJti={}", userId, refreshJti);
            } else {
                logger.info("保存用户 {} jti列表成功,refreshJti={}", userId, refreshJti);
            }
        } catch (Exception e) {
            logger.error("保存用户 {} token到redis失败,refreshJti={},原因:", userId, refreshJti, e);
        }
    }

    /**
     * 生成用户 accessToken
     *
     * @param refreshJti          String refresh Token 唯一标识 uuid
     * @param accessJti           String access Token 唯一标识 uuid
     * @param clientType          String 客户端类型
     * @param gilgameshUserDetail JwtUserDetail 用户信息
     * @return String jwtToken
     */
    private String generateAccessToken(String refreshJti, String accessJti, String clientType, GilgameshUserDetail gilgameshUserDetail) {
        // 构建 jwt 负载信息
        Map<String, Object> claimMap = new HashMap<>(4);
        claimMap.put(SystemEnums.JWTClaimKey.TOKEN_BIZ_TYPE.getValue(), SystemEnums.TokenBizType.TOKEN_BIZ_TYPE_ACCESS.getValue());
        claimMap.put(SystemEnums.JWTClaimKey.TOKEN_CLIENT_TYPE.getValue(), clientType);
        claimMap.put(SystemEnums.JWTClaimKey.TOKEN_RT_JTI.getValue(), refreshJti);
        String userId = String.valueOf(gilgameshUserDetail.getUserId());
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + jwtProperty.getAccessTokenExpireMs());
        return Jwts.builder()
                .claims(claimMap)
                .subject(userId)
                .id(accessJti)
                .issuedAt(now)
                .expiration(expireDate)
                .compressWith(Jwts.ZIP.DEF)
                .signWith(jwtSignKeyPair.getPrivate(), Jwts.SIG.ES256)
                .compact();
    }

    /**
     * 生成用户 refreshToken
     *
     * @param refreshJti          String refreshToken 唯一标识 uuid 用于吊销、防重复
     * @param clientType          String 客户端类型
     * @param gilgameshUserDetail JwtUserDetail 用户信息
     * @return String jwtToken
     */
    private String generateRefreshToken(String refreshJti, String clientType, GilgameshUserDetail gilgameshUserDetail) {
        String userId = gilgameshUserDetail.getUserId();
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + jwtProperty.getRefreshTokenExpireMs());
        // 构建 JWT 载荷(Payload)：仅存必要信息，不包含敏感数据
        Map<String, Object> claimMap = new HashMap<>();
        // 标记为 Refresh Token(避免与 Access Token 混淆)
        claimMap.put(SystemEnums.JWTClaimKey.TOKEN_BIZ_TYPE.getValue(), SystemEnums.TokenBizType.TOKEN_BIZ_TYPE_REFRESH.getValue());
        claimMap.put(SystemEnums.JWTClaimKey.TOKEN_CLIENT_TYPE.getValue(), clientType);
        return Jwts.builder()
                .claims(claimMap)
                .subject(userId)
                .id(refreshJti)
                .issuedAt(now)
                .expiration(expireDate)
                .compressWith(Jwts.ZIP.DEF)
                .signWith(jwtSignKeyPair.getPrivate(), Jwts.SIG.ES256)
                .compact();
    }

    /**
     * 提取并验证 Token 格式（优化：单独抽离，提高可读性）
     */
    public String getAccessTokenFromHttpRequest(HttpServletRequest request) {
        String tokenHeader = jwtProperty.getHeader();
        String tokenHead = jwtProperty.getTokenHead();
        String jwtToken = request.getHeader(tokenHeader);

        // 校验：Token 非空 + 以 TokenHead（如 Bearer ）开头
        if (StringUtil.isEmpty(jwtToken) || !jwtToken.startsWith(tokenHead)) {
            SecurityContextHolder.clearContext();
            return null;
        }
        // 截取 Token 前缀（避免空格问题：trim() 处理前缀后可能的空格）
        return jwtToken.substring(tokenHead.length()).trim();
    }

    /**
     * 检测 accessToken 的状态
     *
     * @param accessToken String 待检测的 accessToken
     * @return AnalysisEnumEntity.JwtTokenStatus
     * JWT_TOKEN_STATUS_VALID:有效,可正常使用
     * JWT_TOKEN_STATUS_EXPIRED:已过期
     * JWT_TOKEN_STATUS_INVALID:无效
     */
    public SystemEnums.JwtTokenStatus checkAccessToken(String accessToken) {
        if (StringUtil.isEmpty(accessToken)) {
            logger.error("error message: 该 accessToken 为空,无法解析");
            return SystemEnums.JwtTokenStatus.JWT_TOKEN_STATUS_INVALID;
        }
        try {
            // 解析 JWT
            Claims claims = getClaimsFromJwtToken(accessToken);
            String jti = claims.getId();
            Date expiration = claims.getExpiration();
            if (StringUtil.isEmpty(jti) || expiration == null) {
                return SystemEnums.JwtTokenStatus.JWT_TOKEN_STATUS_INVALID;
            }
            // access token 续期阈值(毫秒) 默认分钟, 获取当前时间减去 1 分钟的日期
            Date now = TimeUtil.localDateTimeDate(LocalDateTime.now().minus(jwtProperty.getAtExpireThresholdMs(), ChronoUnit.MILLIS));
            if (expiration.before(now)) {
                return SystemEnums.JwtTokenStatus.JWT_TOKEN_STATUS_EXPIRED;
            }
            // 获取 redis 中该 jti 的 token 信息, 读取到再利用缓存中的信息验证, 即使 redis 服务不可用时, 未过期的的 accessToken 也是可用的
            Map<Object, Object> tokenFromRedis = this.getTokenFromRedis(jti);
            if (CollectionUtil.isNotEmpty(tokenFromRedis)) {
                String curAccessToken = (String) tokenFromRedis.get(ConstantUtil.TOKEN_HASH_FIELD_ACCESS_TOKEN);
                String userId = (String) tokenFromRedis.get(ConstantUtil.TOKEN_HASH_FIELD_UID);
                String status = (String) tokenFromRedis.get(ConstantUtil.TOKEN_HASH_FIELD_STATUS);
                if (!Objects.equals(claims.getSubject(), userId)
                        || !Objects.equals(curAccessToken, accessToken)
                        || !SystemEnums.JwtTokenStatus.JWT_TOKEN_STATUS_VALID.getValue().equals(status)
                        || !Objects.equals(claims.get(SystemEnums.JWTClaimKey.TOKEN_BIZ_TYPE.getValue()), SystemEnums.TokenBizType.TOKEN_BIZ_TYPE_ACCESS.getValue())) {
                    return SystemEnums.JwtTokenStatus.JWT_TOKEN_STATUS_INVALID;
                }
            }
            return SystemEnums.JwtTokenStatus.JWT_TOKEN_STATUS_VALID;
        } catch (ExpiredJwtException e) {
            logger.error("error message: 该 JwtToken 已过期,具体过期时间为 <{}>", e.getMessage(), e);
            return SystemEnums.JwtTokenStatus.JWT_TOKEN_STATUS_EXPIRED;
        } catch (Exception e) {
            logger.error("error message: 从 JwtToken 中获取负载信息失败,原因是 <{}>", e.getMessage(), e);
            return SystemEnums.JwtTokenStatus.JWT_TOKEN_STATUS_INVALID;
        }
    }

    private Map<Object, Object> getTokenFromRedis(String jti) {
        if (StringUtil.isEmpty(jti)) {
            throw new BusinessException(BizCodeMsg.ACCESS_PARAM_INVALID);
        }
        String redisHashKey = String.format(ConstantUtil.JWT_TOKEN_REDIS_KEY_FORMAT, jti);
        try {
            return redisService.hgetAll(redisHashKey);
        } catch (Exception e) {
            logger.error("error message: 从 Redis 中获取 {} Token 信息失败,原因:", jti, e);
            return new HashMap<>();
        }
    }

    /**
     * 从 jwtToken 中获取负载信息
     *
     * @param jwtToken String
     * @return Claims
     */
    private Claims getClaimsFromJwtToken(String jwtToken) {
        return jwtParser
                .parseSignedClaims(jwtToken) // 解析 JWS（带签名的 JWT）
                .getPayload(); // 获取 Payload（claims）
    }

    /**
     * 从 jwtToken 中解析负载信息并构建用户jwt信息
     *
     * @param jwtToken String
     * @return JwtUserDetail
     */
    public UserDetails getUserDetailFromAccessToken(String jwtToken) {
        // 从 jwtToken 中获取负载信息
        Claims claims = getClaimsFromJwtToken(jwtToken);
        if (CollectionUtil.isEmpty(claims)) {
            logger.error("无法从该 JwtToken <{}> 中获取任何有效的负载信息", jwtToken);
            return null;
        }
        try {
            String userId = claims.getSubject();
            GilgameshUserDetail jwtUserDetail = new GilgameshUserDetail();
            jwtUserDetail.setUserId(userId);
            jwtUserDetail.setUsername(userId);
            return jwtUserDetail;
        } catch (Exception e) {
            logger.error("accessToken解析中发生了错误,原因:", e);
            return null;
        }
    }

    /**
     * 获取用户专属锁（不存在则创建）
     */
    private ReentrantLock getUserLock(String userId) {
        Assert.notNull(userId, "userId不能为空");
        // computeIfAbsent：线程安全的创建锁，避免重复创建
        return userLockMap.computeIfAbsent(userId, k -> new ReentrantLock());
    }

    /**
     * token 自动续期（单节点并发优化版）
     * 核心优化：细粒度锁 + Redis原子操作 + 二次校验
     */
    public boolean renewJwtToken(HttpServletRequest request, HttpServletResponse response, String accessToken) {
        String refreshToken;
        String userId = null;
        ReentrantLock userLock = null;
        try {
            String clientType = ServletRequestUtils.getStringParameter(request, ConstantUtil.HTTP_HEADER_CLIENT_TYPE, ConstantUtil.STRING_EMPTY);
            if (EnumValue.isNotExist(SystemEnums.ClientType.class, clientType)) {
                logger.warn("{}-{}请求头中未指定客户端类型, 使用默认客户端类型(web)", request.getMethod(), request.getRequestURI());
                clientType = SystemEnums.ClientType.CLIENT_TYPE_WEB.getValue();
            }
            // 1. 提取Refresh Token并解析核心信息（前置操作，不涉及锁）
            refreshToken = ResponseUtil.getCookieValue(request, ConstantUtil.JWT_REFRESH_TOKEN_KEY);
            if (StringUtil.isEmpty(refreshToken)) {
                logger.error("续期失败：Refresh Token为空");
                ResponseUtil.setResponse(response, HttpStatus.UNAUTHORIZED.value(), BizCodeMsg.AUTH_REFRESH_TOKEN_INVALID);
                return false;
            }

            // 解析Refresh Token获取用户ID（用于后续加锁）
            Claims refreshTokenClaims = getClaimsFromJwtToken(refreshToken);
            userId = refreshTokenClaims.getSubject();
            if (StringUtil.isEmpty(userId)) {
                logger.error("续期失败：Refresh Token中未解析出userId");
                ResponseUtil.setResponse(response, HttpStatus.UNAUTHORIZED.value(), BizCodeMsg.AUTH_REFRESH_TOKEN_INVALID);
                return false;
            }

            // 核心：获取用户专属锁 仅锁定当前用户，不阻塞其他用户
            userLock = getUserLock(userId);
            // 加锁（可重入，避免同一线程重复加锁死锁）
            userLock.lock();
            // 加锁后二次校验：避免加锁期间Token状态变化
            Map<Object, Object> tokenHash = new HashMap<>();
            SystemEnums.JwtTokenStatus refreshTokenStatus = this.checkRefreshToken(accessToken, refreshToken, refreshTokenClaims, tokenHash);
            String curAccessToken = (String) tokenHash.get(ConstantUtil.TOKEN_HASH_FIELD_ACCESS_TOKEN);
            // 校验当前最新的 accessToken 与 缓存中的 accessToken 是否一致, 避免并发情况下重复获取 accessToken
            if (!Objects.equals(curAccessToken, accessToken)) {
                response.setHeader(HttpHeaders.AUTHORIZATION, jwtProperty.getTokenHead() + curAccessToken);
                return true;
            }
            switch (refreshTokenStatus) {
                case JWT_TOKEN_STATUS_VALID:
                    // 正常续期流程，继续执行
                    break;
                case JWT_TOKEN_STATUS_EXPIRED:
                    logger.error("Token 自动续期失败：Refresh Token {} 已过期", refreshToken);
                    ResponseUtil.setResponse(response, HttpStatus.UNAUTHORIZED.value(), BizCodeMsg.AUTH_REFRESH_TOKEN_EXPIRED);
                    return false;
                case JWT_TOKEN_STATUS_INVALID:
                    logger.error("Token 自动续期失败：Refresh Token {} 无效", refreshToken);
                    ResponseUtil.setResponse(response, HttpStatus.UNAUTHORIZED.value(), BizCodeMsg.AUTH_REFRESH_TOKEN_INVALID);
                    return false;
                case JWT_TOKEN_STATUS_REVOKED:
                    logger.error("Token 自动续期失败：Refresh Token {} 已被吊销", refreshToken);
                    ResponseUtil.setResponse(response, HttpStatus.UNAUTHORIZED.value(), BizCodeMsg.AUTH_TOKEN_REVOKED);
                    return false;
                default:
                    ResponseUtil.setResponse(response, HttpStatus.UNAUTHORIZED.value(), BizCodeMsg.AUTH_REFRESH_TOKEN_INVALID);
                    return false;
            }
            // 2. 加载用户信息（加锁后执行，避免用户状态变化）
            GilgameshUserDetail userDetails = (GilgameshUserDetail) userDetailsService.loadUserByUsername(userId);
            if (userDetails == null) {
                logger.error("续期失败：用户{}不存在", userId);
                ResponseUtil.setResponse(response, HttpStatus.UNAUTHORIZED.value(), BizCodeMsg.AUTH_USER_INVALID);
                return false;
            }
            // 3. 原子执行：吊销旧Token + 生成新Token（Lua脚本保证单节点Redis操作原子性）
            String refreshJti = refreshTokenClaims.getId();
            String newAccessJti = UUIDUtil.genUuidV7WithoutHyphen();
            TokenDTO tokenDTO = new TokenDTO();
            boolean renewSuccess = renewTokenAtomically(refreshJti, newAccessJti, clientType, userDetails, tokenDTO);
            if (!renewSuccess) {
                logger.error("续期失败：原子操作执行失败,oldJti={},userId={}", newAccessJti, userId);
                ResponseUtil.setResponse(response, HttpStatus.UNAUTHORIZED.value(), BizCodeMsg.AUTH_REFRESH_TOKEN_INVALID);
                return false;
            }
            // 4. 设置响应（新Token）
            response.setHeader(HttpHeaders.AUTHORIZATION, jwtProperty.getTokenHead() + tokenDTO.getAccessToken());
            logger.info("用户{} Access Token续期成功,旧jti={},新jti={}", userId, newAccessJti, newAccessJti);
            return true;
        } catch (ExpiredJwtException e) {
            logger.error("续期失败：Refresh Token 已过期,userId={}", userId, e);
            ResponseUtil.setResponse(response, HttpStatus.UNAUTHORIZED.value(), BizCodeMsg.AUTH_REFRESH_TOKEN_EXPIRED);
            return false;
        } catch (Exception e) {
            logger.error("Access Token 续期失败,userId={}", userId, e);
            ResponseUtil.setResponse(response, HttpStatus.UNAUTHORIZED.value(), BizCodeMsg.AUTH_REFRESH_TOKEN_INVALID);
            return false;
        } finally {
            // ========== 核心：释放用户专属锁（必须在finally中，避免死锁） ==========
            if (userLock != null && userLock.isHeldByCurrentThread()) {
                userLock.unlock();
                cleanUnusedLock(userId);
            }
        }
    }

    /**
     * 单节点内Redis原子操作：吊销旧Token + 生成新Token
     * 避免多步Redis操作的竞态问题
     *
     * @param refreshJti   refresh jti
     * @param newAccessJti 新 access token jti
     * @param clientType   客户端类型
     * @param userDetails  用户信息
     * @return 是否成功
     */
    private boolean renewTokenAtomically(String refreshJti, String newAccessJti, String clientType, GilgameshUserDetail userDetails, TokenDTO tokenDTO) {
        // 提前生成新 access Token
        String accessToken = this.generateAccessToken(refreshJti, newAccessJti, clientType, userDetails);
        tokenDTO.setAccessToken(accessToken);
        String tokenKey = String.format(ConstantUtil.JWT_TOKEN_REDIS_KEY_FORMAT, refreshJti);
        // KEYS 仅放 Redis 键名，参数放 ARGV
        List<String> keys = List.of(tokenKey);
        // ARGV 顺序：newAccessToken
        Object[] args = new Object[]{accessToken};
        // 执行Lua脚本
        Long result = redisService.executeBySha1(ConstantUtil.REDIS_SCRIPT_KEY_RENEW_TOKEN, keys, args);
        return result != null && result == 1;
    }

    /**
     * 清理长时间未使用的用户锁（如用户登出后）
     * 可结合定时任务定期清理，或在解锁时判断
     */
    private void cleanUnusedLock(String userId) {
        ReentrantLock lock = userLockMap.get(userId);
        if (lock != null && !lock.isLocked() && lock.getQueueLength() == 0) {
            userLockMap.remove(userId);
        }
    }

    /**
     * 验证 Refresh Token
     *
     * @param accessToken        String
     * @param refreshToken       String
     * @param refreshTokenClaims Claims
     * @return SystemEnums.JwtTokenStatus
     */
    public SystemEnums.JwtTokenStatus checkRefreshToken(String accessToken, String refreshToken, Claims refreshTokenClaims, Map<Object, Object> tokenHash) {
        if (StringUtil.isEmpty(accessToken) || StringUtil.isEmpty(refreshToken)) {
            logger.error("该 accessToken {} 或 refreshToken {} 为空,无法解析", accessToken, refreshToken);
            return SystemEnums.JwtTokenStatus.JWT_TOKEN_STATUS_INVALID;
        }
        try {
            // 1. 校验 Refresh Token 签名和格式（防篡改）
            String refreshJti = refreshTokenClaims.getId();
            String userIdFromRefreshToken = refreshTokenClaims.getSubject();
            String tokenBizType = (String) refreshTokenClaims.get(SystemEnums.JWTClaimKey.TOKEN_BIZ_TYPE.getValue());
            Date expiration = refreshTokenClaims.getExpiration();
            if (StringUtil.isEmpty(refreshJti) || StringUtil.isEmpty(userIdFromRefreshToken) || !SystemEnums.TokenBizType.TOKEN_BIZ_TYPE_REFRESH.getValue().equals(tokenBizType) || expiration == null) {
                logger.error("Refresh Token缺失核心信息,jti={},userId={},bizType={}", refreshJti, userIdFromRefreshToken, tokenBizType);
                return SystemEnums.JwtTokenStatus.JWT_TOKEN_STATUS_INVALID;
            }
            // 2. 校验 Refresh Token 是否已过期, refresh token 续期阈值(毫秒) 默认分钟, 获取当前时间减去 1 分钟的日期
            Date now = TimeUtil.localDateTimeDate(LocalDateTime.now().minus(jwtProperty.getRtExpireThresholdMs(), ChronoUnit.MILLIS));
            if (expiration.before(now)) {
                logger.error("Refresh Token已过期,jti={},过期时间={}", refreshJti, expiration);
                return SystemEnums.JwtTokenStatus.JWT_TOKEN_STATUS_EXPIRED;
            }
            // 3. 校验过期 Access Token 与 Refresh Token 用户名一致（防跨用户续期）
            Claims expiredAccessTokenClaims;
            try {
                expiredAccessTokenClaims = getClaimsFromJwtToken(accessToken);
            } catch (ExpiredJwtException e) {
                // Access Token 过期是正常续期场景，继续执行
                expiredAccessTokenClaims = e.getClaims();
            }
            String accessRJti = (String) expiredAccessTokenClaims.get(SystemEnums.JWTClaimKey.TOKEN_RT_JTI.getValue());
            if (!refreshJti.equals(accessRJti) || !userIdFromRefreshToken.equals(expiredAccessTokenClaims.getSubject())) {
                logger.error("Refresh Token与Access Token归属不一致,refreshJti={},accessJti={}", refreshJti, accessRJti);
                return SystemEnums.JwtTokenStatus.JWT_TOKEN_STATUS_INVALID;
            }
            // 3. 校验 Refresh Token 是否在 Redis 中存在(防注销后滥用)
            Map<Object, Object> tokenFromRedis = getTokenFromRedis(refreshJti);
            if (CollectionUtil.isEmpty(tokenFromRedis)) {
                logger.error("Refresh Token未存储在Redis,refreshJti={}", refreshJti);
                return SystemEnums.JwtTokenStatus.JWT_TOKEN_STATUS_INVALID;
            }
            String refreshJtiFromRedis = (String) tokenFromRedis.get(ConstantUtil.TOKEN_HASH_FIELD_RT_JTI);
            String userIdFromRedisByRefresh = (String) tokenFromRedis.get(ConstantUtil.TOKEN_HASH_FIELD_UID);
            if (StringUtil.isEmpty(userIdFromRedisByRefresh) || !userIdFromRedisByRefresh.equals(userIdFromRefreshToken) || !Objects.equals(refreshJtiFromRedis, refreshToken)) {
                logger.error("续期失败：Refresh Token已注销或未存储, 或所属用户 {} 与 redis 缓存用户 {} 不一致", userIdFromRefreshToken, userIdFromRefreshToken);
                return SystemEnums.JwtTokenStatus.JWT_TOKEN_STATUS_INVALID;
            }
            // 校验 Refresh Token 换取 access token 次数
            String accessTokenCount = (String) tokenFromRedis.getOrDefault(ConstantUtil.TOKEN_HASH_FIELD_RT_AT_ROTATE, jwtProperty.getMaxRtExchangeTimes());
            if (Integer.parseInt(accessTokenCount) >= jwtProperty.getMaxRtExchangeTimes()) {
                logger.error("Refresh Token已超过最大续期次数,refreshJti={},当前次数={}", refreshJti, accessTokenCount);
                return SystemEnums.JwtTokenStatus.JWT_TOKEN_STATUS_INVALID;
            }
            String refreshTokenStatus = (String) tokenFromRedis.get(ConstantUtil.TOKEN_HASH_FIELD_STATUS);
            SystemEnums.JwtTokenStatus refreshTokenStatusEnum = EnumValue.getEnumByValue(SystemEnums.JwtTokenStatus.class, refreshTokenStatus);
            if (refreshTokenStatusEnum == null) {
                logger.error("Refresh Token 非有效状态,refreshJti={},当前状态={}", refreshJti, refreshTokenStatus);
                return SystemEnums.JwtTokenStatus.JWT_TOKEN_STATUS_INVALID;
            }
            tokenHash.putAll(tokenFromRedis);
            return refreshTokenStatusEnum;
        } catch (ExpiredJwtException e) {
            logger.error("Refresh Token已过期,原因:", e);
            return SystemEnums.JwtTokenStatus.JWT_TOKEN_STATUS_EXPIRED;
        } catch (Exception e) {
            logger.error("校验Refresh Token失败,原因:", e);
            return SystemEnums.JwtTokenStatus.JWT_TOKEN_STATUS_INVALID;
        }
    }

    /**
     * 吊销单个 Token（核心方法）
     *
     * @param refreshJti 待吊销的 Token 唯一标识
     * @param userId     所属用户ID（用于日志/校验）
     * @param clientType 客户端类型
     * @return 是否吊销成功
     */
    public boolean revokeToken(String refreshJti, String userId, String clientType) {
        if (StringUtil.isEmpty(refreshJti) || StringUtil.isEmpty(userId)) {
            throw new BusinessException(BizCodeMsg.ACCESS_PARAM_INVALID);
        }
        try {
            String redisHashKey = String.format(ConstantUtil.JWT_TOKEN_REDIS_KEY_FORMAT, refreshJti);
            // 1. 校验 Token 归属（防止跨用户吊销）
            String userIdFromRedis = (String) redisService.hget(redisHashKey, ConstantUtil.TOKEN_HASH_FIELD_UID);
            if (StringUtil.isEmpty(userIdFromRedis)) {
                logger.warn("吊销失败：Token不存在,refreshJti={},userId={}", refreshJti, userId);
                return false;
            }
            if (!userId.equals(userIdFromRedis)) {
                logger.error("吊销失败：Token归属不一致,refreshJti={},请求userId={},实际userId={}", refreshJti, userId, userIdFromRedis);
                return false;
            }
            // 2. 更新状态为已吊销（核心操作）
            boolean updateSuccess = redisService.hset(redisHashKey, ConstantUtil.TOKEN_HASH_FIELD_STATUS,
                    SystemEnums.JwtTokenStatus.JWT_TOKEN_STATUS_REVOKED.getValue());
            logger.info("Token吊销成功,jti={},userId={}, clientType={}", refreshJti, userId, clientType);
            return updateSuccess;
        } catch (Exception e) {
            logger.error("Token吊销失败,jti={},userId={}, clientType={}, 原因:", refreshJti, userId, clientType, e);
            return false;
        }
    }

    /**
     * 吊销用户所有未过期 Token（全设备登出/密码修改场景）
     *
     * @param userId 用户ID
     * @return 吊销成功的 Token 数量
     */
    public int revokeAllTokenByUserId(String userId, String clientType) {
        if (StringUtil.isEmpty(userId)) {
            throw new BusinessException(BizCodeMsg.ACCESS_PARAM_INVALID);
        }
        int revokeCount = 0;
        try {
            String userJtisKey = String.format(ConstantUtil.JWT_USER_JTI_REDIS_KEY_FORMAT, userId, clientType);
            List<Object> jtiSet = redisService.getAllList(userJtisKey);
            if (CollectionUtil.isEmpty(jtiSet)) {
                logger.info("用户无未过期Token,无需吊销,userId={}", userId);
                return 0;
            }
            // 批量吊销每个 jti
            for (Object jti : jtiSet) {
                if (revokeToken(jti.toString(), userId, clientType)) {
                    revokeCount++;
                }
            }
            // 清空 userJtis 集合(避免后续遍历无效 jti)
            redisService.deleteKey(userJtisKey);
            logger.info("用户全设备Token吊销完成,userId={},吊销数量={}", userId, revokeCount);
            return revokeCount;
        } catch (Exception e) {
            logger.error("用户全设备Token吊销失败,userId={},原因:", userId, e);
            return revokeCount;
        }
    }
}
