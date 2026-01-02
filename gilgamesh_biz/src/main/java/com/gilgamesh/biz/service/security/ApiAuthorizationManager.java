package com.gilgamesh.biz.service.security;

import com.gilgamesh.biz.entity.dto.SysApiAuthSimpleDTO;
import com.gilgamesh.common.enums.SystemEnums;
import com.gilgamesh.common.utils.CollectionUtil;
import com.gilgamesh.persistence.entity.gilgamesh.sys.SysApiAuth;
import com.gilgamesh.persistence.entity.gilgamesh.sys.SysRole;
import com.gilgamesh.persistence.entity.gilgamesh.sys.SysRoleApiAuthRelation;
import com.gilgamesh.persistence.repository.mysql.gilgamesh.SysApiAuthRepository;
import com.gilgamesh.persistence.repository.mysql.gilgamesh.SysRoleApiAuthRelationRepository;
import com.gilgamesh.persistence.repository.mysql.gilgamesh.SysRoleRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 定义接口权限授权管理器(替代原 FilterInvocationSecurityMetadataSource)
 * @createDate 2025/11/23 11:51
 * @since 1.0.0
 */
@Component
public class ApiAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
    private final Logger logger = LoggerFactory.getLogger(ApiAuthorizationManager.class);

    /**
     * 数据库-角色信息访问接口
     */
    private final SysRoleRepository sysRoleRepository;

    /**
     * 数据库-角色接口权限信息访问接口
     */
    private final SysRoleApiAuthRelationRepository sysRoleApiAuthRelationRepository;

    /**
     * 数据库-接口权限信息访问接口
     */
    private final SysApiAuthRepository sysApiAuthRepository;

    /**
     * 接口路径匹配工具类
     */
    private final AntPathMatcher antPathMatcher;

    /**
     * 角色接口权限映射（key：角色ID，value：该角色拥有的接口权限集合）
     */
    private volatile Map<Long, Set<SysApiAuthSimpleDTO>> roleApiAuthMap = new ConcurrentHashMap<>();

    @Autowired
    public ApiAuthorizationManager(SysRoleRepository sysRoleRepository,
                                   SysRoleApiAuthRelationRepository sysRoleApiAuthRelationRepository,
                                   SysApiAuthRepository sysApiAuthRepository,
                                   AntPathMatcher antPathMatcher) {
        this.sysRoleRepository = sysRoleRepository;
        this.sysRoleApiAuthRelationRepository = sysRoleApiAuthRelationRepository;
        this.sysApiAuthRepository = sysApiAuthRepository;
        this.antPathMatcher = antPathMatcher;
    }

    /**
     * 核心授权方法：校验用户是否有权限访问当前请求
     * 替代原 FilterInvocationSecurityMetadataSource 的 getAttributes 逻辑
     */
    @Override
    public AuthorizationDecision check(Supplier<Authentication> authenticationSupplier,
                                       RequestAuthorizationContext context) {
        // 1. 获取当前认证信息（优先从 supplier 获取，其次从 SecurityContext  fallback）
        Authentication authentication = authenticationSupplier.get();
        if (authentication == null) {
            authentication = SecurityContextHolder.getContext().getAuthentication();
        }

        // 2. 未登录/匿名用户直接拒绝
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            logger.error("error message: 当前用户未登录，无访问权限");
            return new AuthorizationDecision(false);
        }

        // 3. 获取用户拥有的权限（角色ID集合，与原逻辑一致）
        Collection<? extends GrantedAuthority> userAuthorities = authentication.getAuthorities();
        if (CollectionUtil.isEmpty(userAuthorities)) {
            logger.error("当前用户已登录，但无任何资源访问权限");
            return new AuthorizationDecision(false);
        }
        Set<String> userRoleIds = userAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(StringUtils::hasText)
                .collect(Collectors.toSet());

        // 4. 获取当前请求信息
        HttpServletRequest request = context.getRequest();
        String httpMethod = request.getMethod().toUpperCase();
        String requestUri = request.getRequestURI();

        // 5. 匹配当前请求所需的角色ID集合
        Set<String> requiredRoleIds = getRequiredRoles(httpMethod, requestUri);
        if (CollectionUtils.isEmpty(requiredRoleIds)) {
            logger.debug("当前请求 <{}:{}> 未匹配接口权限, 尝试从DB重新加载角色接口权限信息",
                    httpMethod, requestUri);
            // 6. 双重检查锁定：重新从数据库加载权限映射
            synchronized (this) {
                loadRoleApiAuthMappingFromDB();
                requiredRoleIds = getRequiredRoles(httpMethod, requestUri);
                // 重新加载后仍无匹配权限，拒绝访问
                if (CollectionUtils.isEmpty(requiredRoleIds)) {
                    logger.error("当前系统匹配不到接口 {} {} 所需的权限,拒绝访问",
                            httpMethod, requestUri);
                    return new AuthorizationDecision(false);
                }
            }
        }

        // 7. 校验用户角色是否包含所需角色(核心权限校验逻辑)
        String userName = authentication.getName();
        boolean hasPermission = requiredRoleIds.stream().anyMatch(userRoleIds::contains);
        if (!hasPermission) {
            logger.error("当前用户 {} 拥有角色 {}，无访问 {}:{} 所需的角色 {}，拒绝访问",
                    userName, userRoleIds, httpMethod, requestUri, requiredRoleIds);
            return new AuthorizationDecision(false);
        }

        // 8. 权限通过
        logger.debug("当前用户 {} 拥有角色 {}，已具备访问 {}:{} 所需角色 {}，允许访问",
                userName, userRoleIds, httpMethod, requestUri, requiredRoleIds);
        return new AuthorizationDecision(true);
    }

    /**
     * 从数据库加载 角色与接口映射关系（复用原 getRoleApiAuthMappingFromDB 逻辑）
     */
    private void loadRoleApiAuthMappingFromDB() {
        try {
            // 1. 查询所有有效角色（未删除）
            List<SysRole> sysRoles = sysRoleRepository.findByDeletedStatus(SystemEnums.DeletedStatus.NOT_DELETED.getValue());
            if (CollectionUtils.isEmpty(sysRoles)) {
                logger.error("当前数据库中未初始化任何有效的角色信息!");
                return;
            }
            Set<Long> roleIds = sysRoles.stream().map(SysRole::getId).collect(Collectors.toSet());

            // 2. 查询角色与接口权限的有效关联关系
            List<SysRoleApiAuthRelation> roleApiAuthRelations = sysRoleApiAuthRelationRepository
                    .findByRoleIdInAndDeletedStatus(roleIds, SystemEnums.DeletedStatus.NOT_DELETED.getValue());
            if (CollectionUtils.isEmpty(roleApiAuthRelations)) {
                logger.error("当前数据库中不存在有效的角色与接口权限关联关系!");
                return;
            }

            // 3. 查询关联的有效接口权限
            Set<Long> apiAuthIds = roleApiAuthRelations.stream()
                    .map(SysRoleApiAuthRelation::getApiAuthId)
                    .collect(Collectors.toSet());
            List<SysApiAuth> sysApiAuths = sysApiAuthRepository
                    .findByIdInAndDeletedStatus(apiAuthIds, SystemEnums.DeletedStatus.NOT_DELETED.getValue());
            if (CollectionUtils.isEmpty(sysApiAuths)) {
                logger.error("当前数据库中未初始化任何有效的接口权限信息!");
                return;
            }

            // 4. 构建映射（角色ID→角色对象、接口ID→接口对象）
            Map<Long, SysRole> roleIdMap = sysRoles.stream()
                    .collect(Collectors.toMap(SysRole::getId, Function.identity()));
            Map<Long, SysApiAuth> apiAuthIdMap = sysApiAuths.stream()
                    .collect(Collectors.toMap(SysApiAuth::getId, Function.identity()));

            // 5. 构建角色-接口权限映射
            Map<Long, Set<SysApiAuthSimpleDTO>> newRoleApiAuthMap = new ConcurrentHashMap<>();
            for (SysRoleApiAuthRelation relation : roleApiAuthRelations) {
                Long roleId = relation.getRoleId();
                Long apiAuthId = relation.getApiAuthId();
                // 校验角色和接口权限是否有效
                if (roleIdMap.containsKey(roleId) && apiAuthIdMap.containsKey(apiAuthId)) {
                    SysApiAuth apiAuth = apiAuthIdMap.get(apiAuthId);
                    SysApiAuthSimpleDTO apiAuthDTO = new SysApiAuthSimpleDTO(apiAuth);
                    // 放入映射集合
                    newRoleApiAuthMap.computeIfAbsent(roleId, k -> new HashSet<>())
                            .add(apiAuthDTO);
                }
            }

            // 6. 更新 volatile 变量（保证线程可见性）
            this.setRoleApiAuthMap(newRoleApiAuthMap);
            logger.info("成功从数据库加载角色-接口权限映射，共加载 {} 个角色的权限配置", newRoleApiAuthMap.size());
        } catch (Exception e) {
            logger.error("加载角色-接口权限映射失败, 原因是:{}", e.getMessage(), e);
        }
    }


    /**
     * 获取接口请求所需的角色ID
     *
     * @param httpMethod String
     * @param requestUri String
     * @return Set<String>
     */
    private Set<String> getRequiredRoles(String httpMethod, String requestUri) {
        Set<String> requiredRoleIds = new HashSet<>();
        // 遍历角色-接口权限映射，匹配当前请求
        roleApiAuthMap.forEach((roleId, apiAuths) -> apiAuths.forEach(apiAuth -> {
            // 匹配接口
            if (Objects.equals(httpMethod, apiAuth.getApiMethod()) && antPathMatcher.match(requestUri, apiAuth.getApiPath())) {
                requiredRoleIds.add(String.valueOf(roleId));
            }
        }));
        return requiredRoleIds;
    }

    /**
     * 可选：重写 verify 方法，无权访问时抛出自定义异常(增强可读性)
     */
    @Override
    public void verify(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext context) {
        AuthorizationDecision decision = check(authenticationSupplier, context);
        if (decision != null && !decision.isGranted()) {
            HttpServletRequest request = context.getRequest();
            throw new AccessDeniedException(
                    String.format("无权访问接口：%s %s", request.getMethod(), request.getRequestURI())
            );
        }
    }


    public void setRoleApiAuthMap(Map<Long, Set<SysApiAuthSimpleDTO>> roleApiAuthMap) {
        if (roleApiAuthMap != null) {
            this.roleApiAuthMap = new ConcurrentHashMap<>(roleApiAuthMap);
        }
    }

    public Map<Long, Set<SysApiAuthSimpleDTO>> getRoleApiAuthMap() {
        return Collections.unmodifiableMap(roleApiAuthMap);
    }
}
