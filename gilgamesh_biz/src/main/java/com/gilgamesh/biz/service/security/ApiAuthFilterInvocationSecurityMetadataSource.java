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
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Spring Security 5.7 使用 FilterInvocationSecurityMetadataSource 动态加载权限方式已被废弃
 * 推荐自定义 AuthorizationManager 实现原有功能
 *
 * @author takeEasy9
 * @version 1.0.0
 * @description 动态提供 API-权限映射规则
 * @createDate 2025/11/22 18:12
 * @since 1.0.0
 */
@Deprecated
@Component
public class ApiAuthFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    private final Logger logger = LoggerFactory.getLogger(ApiAuthFilterInvocationSecurityMetadataSource.class);
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

    public void setRoleApiAuthMap(Map<Long, Set<SysApiAuthSimpleDTO>> roleApiAuthMap) {
        this.roleApiAuthMap = roleApiAuthMap;
    }

    /**
     * 角色接口权限映射
     */
    private volatile Map<Long, Set<SysApiAuthSimpleDTO>> roleApiAuthMap = new ConcurrentHashMap<>();

    public ApiAuthFilterInvocationSecurityMetadataSource(SysRoleRepository sysRoleRepository, SysRoleApiAuthRelationRepository sysRoleApiAuthRelationRepository, SysApiAuthRepository sysApiAuthRepository) {
        this.sysRoleRepository = sysRoleRepository;
        this.sysRoleApiAuthRelationRepository = sysRoleApiAuthRelationRepository;
        this.sysApiAuthRepository = sysApiAuthRepository;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        if (!(object instanceof FilterInvocation fi)) {
            logger.error("非法参数：{}，必须是 FilterInvocation 类型", object.getClass().getName());
            throw new IllegalArgumentException("参数必须是 FilterInvocation 类型");
        }
        // 获取当前用户已拥有的权限
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            logger.error("error message: 当前用户未登录，无访问权限");
            // 返回 null → Spring Security 会拒绝访问(403 或重定向登录页)
            return null;
        }

        Collection<? extends GrantedAuthority> userAuthorities = authentication.getAuthorities();
        if (CollectionUtil.isEmpty(userAuthorities)) {
            logger.error("当前用户已登录，但无任何资源访问权限");
            // 无权限，拒绝访问
            return null;
        }
        Set<String> userApiAuths = userAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        HttpServletRequest request = fi.getHttpRequest();
        String requestUri = request.getRequestURI();
        String httpMethod = request.getMethod().toUpperCase();
        Set<String> requiredRoleIds = this.getRequiredRoles(request);
        if (CollectionUtil.isEmpty(requiredRoleIds)) {
            logger.debug("当前请求 <{}:{}> 未匹配接口权限, 尝试从DB重新加载角色接口权限信息", request.getMethod(), request.getRequestURI());
            synchronized (this) {
                getRoleApiAuthMappingFromDB();
                // 尝试再次匹配
                requiredRoleIds = this.getRequiredRoles(request);
                if (CollectionUtil.isEmpty(requiredRoleIds)) {
                    logger.error("当前系统匹配不到接口 {} {} 所需的权限,拒绝访问", request.getMethod(), request.getRequestURI());
                    return null;
                }
            }
        }
        String userName = authentication.getName();
        if (requiredRoleIds.stream().noneMatch(userApiAuths::contains)) {
            logger.error("当前用户 {} 权限 {}，无访问 {}:{} 所需的权限 {}，拒绝访问", userName, userApiAuths, httpMethod, requestUri, requiredRoleIds);
            // 无匹配权限，拒绝访问
            return null;
        }
        // 返回接口访问所需权限
        return requiredRoleIds.stream().map(SecurityConfig::new).collect(Collectors.toList());
    }

    /**
     * 从数据库加载 角色与接口映射关系
     */
    private void getRoleApiAuthMappingFromDB() {
        try {
            // 查询所有有效角色
            List<SysRole> sysRoles = sysRoleRepository.findByDeletedStatus(SystemEnums.DeletedStatus.NOT_DELETED.getValue());
            if (CollectionUtil.isEmpty(sysRoles)) {
                this.logger.error("当前数据库中未初始化任何有效的角色信息!");
                return;
            }
            Set<Long> roleIds = sysRoles.stream().map(SysRole::getId).collect(Collectors.toSet());
            // 通过角色ID集合查询有效的角色与接口权限关联关系
            List<SysRoleApiAuthRelation> sysRoleApiAuthRelations = sysRoleApiAuthRelationRepository.findByRoleIdInAndDeletedStatus(roleIds, SystemEnums.DeletedStatus.NOT_DELETED.getValue());
            if (CollectionUtil.isEmpty(sysRoleApiAuthRelations)) {
                this.logger.error("当前数据库中不存在有效的角色与接口权限关联关系!");
                return;
            }
            Set<Long> apiAuthId = sysRoleApiAuthRelations.stream().map(SysRoleApiAuthRelation::getApiAuthId).collect(Collectors.toSet());
            List<SysApiAuth> sysApiAuths = sysApiAuthRepository.findByIdInAndDeletedStatus(apiAuthId, SystemEnums.DeletedStatus.NOT_DELETED.getValue());
            if (CollectionUtil.isEmpty(sysRoleApiAuthRelations)) {
                this.logger.error("当前数据库中未初始化任何有效的接口权限信息!");
                return;
            }
            Map<Long, SysRole> roleIdMap = sysRoles.stream().collect(Collectors.toMap(SysRole::getId, Function.identity()));
            Map<Long, SysApiAuth> apiAuthIdMap = sysApiAuths.stream().collect(Collectors.toMap(SysApiAuth::getId, Function.identity()));
            Map<Long, Set<SysApiAuthSimpleDTO>> newRoleApiAuthMap = new ConcurrentHashMap<>();
            for (SysRoleApiAuthRelation sysRoleApiAuthRelation : sysRoleApiAuthRelations) {
                if (roleIdMap.containsKey(sysRoleApiAuthRelation.getRoleId()) && apiAuthIdMap.containsKey(sysRoleApiAuthRelation.getApiAuthId())) {
                    SysApiAuth sysApiAuth = apiAuthIdMap.get(sysRoleApiAuthRelation.getApiAuthId());
                    SysApiAuthSimpleDTO sysApiAuthSimpleDTO = new SysApiAuthSimpleDTO(sysApiAuth);
                    if (newRoleApiAuthMap.containsKey(sysRoleApiAuthRelation.getRoleId())) {
                        Set<SysApiAuthSimpleDTO> sysApiAuthSimpleDTOS = newRoleApiAuthMap.get(sysRoleApiAuthRelation.getRoleId());
                        sysApiAuthSimpleDTOS.add(sysApiAuthSimpleDTO);
                    } else {
                        Set<SysApiAuthSimpleDTO> sysApiAuthSimpleDTOS = new HashSet<>();
                        sysApiAuthSimpleDTOS.add(sysApiAuthSimpleDTO);
                        newRoleApiAuthMap.put(sysRoleApiAuthRelation.getRoleId(), sysApiAuthSimpleDTOS);
                    }
                }
            }
            this.setRoleApiAuthMap(newRoleApiAuthMap);
        } catch (Exception e) {
            logger.error("加载权限规则失败, 原因是:{}", e.getMessage(), e);
        }
    }

    private Set<String> getRequiredRoles(HttpServletRequest request) {
        Set<String> requiredRoleIds = new HashSet<>();
        roleApiAuthMap.forEach((roleId, apiAuths) -> {
            apiAuths.forEach(apiAuth -> {
                AntPathRequestMatcher matcher = new AntPathRequestMatcher(apiAuth.getApiPath(), apiAuth.getApiMethod());
                if (matcher.matches(request)) {
                    requiredRoleIds.add(String.valueOf(roleId));
                }
            });
        });
        return requiredRoleIds;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        this.getRoleApiAuthMappingFromDB();
        return List.of();
    }

    @Override
    public boolean supports(Class<?> clazz) {
        // 只支持 FilterInvocation 类型的参数(当前请求对象)
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
