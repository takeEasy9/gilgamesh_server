package com.gilgamesh.biz.service.sys;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.gilgamesh.biz.entity.dto.CaptchaDTO;
import com.gilgamesh.biz.entity.dto.LoginDTO;
import com.gilgamesh.biz.entity.dto.TokenDTO;
import com.gilgamesh.biz.entity.vo.LoginVO;
import com.gilgamesh.biz.service.security.JwtService;
import com.gilgamesh.common.entity.base.ApiResult;
import com.gilgamesh.common.entity.property.JwtProperty;
import com.gilgamesh.common.entity.security.GilgameshUserDetail;
import com.gilgamesh.common.enums.BizCodeMsg;
import com.gilgamesh.common.exceptions.BusinessException;
import com.gilgamesh.common.redis.RedisService;
import com.gilgamesh.common.utils.ConstantUtil;
import com.gilgamesh.common.utils.ResponseUtil;
import com.gilgamesh.common.utils.StringUtil;
import com.gilgamesh.common.utils.UUIDUtil;
import com.gilgamesh.persistence.repository.mysql.gilgamesh.SysUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.Instant;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 用户认证服务
 * @createDate 2024/10/5 15:18
 * @since 1.0.0
 */
@Service
public class UserIdentifyService {
    private final Logger logger = LoggerFactory.getLogger(UserIdentifyService.class);

    /**
     * redis 服务
     */
    private final RedisService redisService;

    /**
     * 认证管理器
     */
    private final AuthenticationManager authenticationManager;

    /**
     * JWT 服务
     */
    private final JwtService jwtService;

    /**
     * SysUserRepository
     */
    private final SysUserRepository sysUserRepository;

    /**
     * JWT 属性
     */
    private final JwtProperty jwtProperty;

    public UserIdentifyService(RedisService redisService, AuthenticationManager authenticationManager,
                               JwtService jwtService, SysUserRepository sysUserRepository, JwtProperty jwtProperty) {
        this.redisService = redisService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.sysUserRepository = sysUserRepository;
        this.jwtProperty = jwtProperty;
    }

    /**
     * 生成图片验证码
     *
     * @return CaptchaDTO
     */
    public ResponseEntity<ApiResult<CaptchaDTO>> genImageCaptcha(Integer width, Integer height) {
        // 使用uuid作为captchaKey
        String captchaKey = UUIDUtil.genUuidV7WithoutHyphen();
//        StringUtils.isEmpty(captchaKey)
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(width, height, 4, 20);
        String captchaCode = captcha.getCode();
        if (StringUtil.isNotEmpty(captchaCode) && redisService.setValue(captchaKey, captcha.getCode(), 300L)) {
            CaptchaDTO captchaDTO = new CaptchaDTO(captchaKey, captcha.getImageBase64());
            return ResponseEntity.ok(ApiResult.success(BizCodeMsg.VALIDATION_CODE_GENERATE_SUCCESS, captchaDTO));
        } else {
            logger.error("生成图片验证码失败: 验证码:{}为空或验证码key写入缓存失败", captchaCode);
            throw new BusinessException(BizCodeMsg.VALIDATION_CODE_GENERATE_FAILED);
        }
    }

    /**
     * 用户登录
     *
     * @param loginVO LoginVO
     * @return TokenDTO
     */
    @Transactional(transactionManager = "mySqlGilgameshTransactionManager", rollbackFor = Exception.class)
    public ResponseEntity<ApiResult<LoginDTO>> login(LoginVO loginVO) {
        this.logger.info("用户 <{}> 开始登录", loginVO);
        try {
            // 构造认证令牌（用户名+明文密码）
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(loginVO.getUsername(), loginVO.getPassword());
            // 自动比对：DelegatingPasswordEncoder 识别密码前缀，调用对应算法
            Authentication authentication = authenticationManager.authenticate(authToken);
            GilgameshUserDetail userDetail = (GilgameshUserDetail) authentication.getPrincipal();
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 认证通过，生成 JWT access token
            String jti = UUIDUtil.genUuidV7WithoutHyphen();
            // 生成 JWT token
            TokenDTO tokenDTO = jwtService.generateToken(jti, loginVO.getClientType(), userDetail);
            int count = this.sysUserRepository.updateLastLoginAtByUsername(userDetail.getUsername(), Instant.now());
            if (count == ConstantUtil.DATA_COUNT) {
                this.logger.info("成功更新该用户 <{}> 的最后登录时间", loginVO.getUsername());
            } else {
                this.logger.error("更新该用户 <{}> 的最后登录时间失败", loginVO.getUsername());
            }
            HttpHeaders headers = new HttpHeaders();
            // 设置 Authorization 响应头：Bearer + Access Token
            headers.set(HttpHeaders.AUTHORIZATION, jwtProperty.getTokenHead() + tokenDTO.getAccessToken());
            // 推荐 HttpOnly Cookie 存储 refresh token
            ResponseCookie refreshCookie = ResponseUtil.buildHttpOnlyCookie(ConstantUtil.JWT_REFRESH_TOKEN_KEY, tokenDTO.getRefreshToken(), jwtProperty.getRefreshTokenExpireMs()); // 7天
            headers.add(HttpHeaders.SET_COOKIE, refreshCookie.toString());
            LoginDTO loginDTO = new LoginDTO(userDetail.getUserId(), userDetail.getUsername(), userDetail.getUserAlias());
            // token 放在响应头中，不放在响应体中，防止 XSS 攻击
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(ApiResult.success(BizCodeMsg.USER_LOGIN_SUCCESS, loginDTO));
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            this.logger.error(String.valueOf(e));
            throw new BusinessException(BizCodeMsg.USER_LOGIN_FAILED_WITH_NAME_PASSWORD);
        } catch (DisabledException e) {
            this.logger.error("用户 <{}> 账号尚未激活,请先激活,原因:", loginVO.getUsername(), e);
            throw new BusinessException(BizCodeMsg.USER_LOGIN_ACCOUNT_INACTIVE);
        } catch (LockedException e) {
            this.logger.error("用户 <{}> 账号已被锁定,请先解锁,原因:", loginVO.getUsername(), e);
            throw new BusinessException(BizCodeMsg.USER_LOGIN_ACCOUNT_FORBIDDEN);
        } catch (CredentialsExpiredException e) {
            this.logger.error("用户 <{}> 账号密码过期,请重置密码, 原因:", loginVO.getUsername(), e);
            throw new BusinessException(BizCodeMsg.USER_LOGIN_PASSWORD_EXPIRED);
        } catch (AccountExpiredException e) {
            this.logger.error("用户 <{}> 账号已注销,请联系管理员解决、原因:", loginVO.getUsername(), e);
            throw new BusinessException(BizCodeMsg.USER_LOGIN_ACCOUNT_DESTROY);
        } catch (BusinessException e) {
            this.logger.error("获取用户 <{}> 构建 JWT 所需的信息失败,原因:", loginVO.getUsername(), e);
            throw new BusinessException(BizCodeMsg.USER_LOGIN_ACCOUNT_INACTIVE);
        } catch (Exception e) {
            this.logger.error("用户 <{}> 登录失败,原因是:", loginVO.getUsername(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BusinessException(BizCodeMsg.USER_LOGIN_ACCOUNT_INACTIVE);
        }
    }
}
