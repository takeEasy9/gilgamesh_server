package com.gilgamesh.biz.filters;

import com.gilgamesh.persistence.repository.gilgamesh.SysApiAuthRepository;
import com.gilgamesh.persistence.repository.gilgamesh.SysRoleApiAuthRelationRepository;
import com.gilgamesh.persistence.repository.gilgamesh.SysRoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 安全元数据源用于初始化基于数据库的权限数据源
 * @createDate 2025/1/26 16:12
 * @since 1.0.0
 */
@Component
public class GilgameshFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    private final Logger logger = LoggerFactory.getLogger(GilgameshFilterInvocationSecurityMetadataSource.class);

    /**
     * 角色数据访问层
     */
    private final SysRoleRepository sysRoleRepository;

    /**
     * API权限数据访问层
     */
    private final SysApiAuthRepository sysApiAuthRepository;

    /**
     * 角色与API权限关联关系数据访问层
     */
    private final SysRoleApiAuthRelationRepository sysRoleApiAuthRelationRepository;

    /**
     * 存储资源路径与权限的映射关系 key:资源路径 value:权限集合
     */
    private Map<String, Collection<ConfigAttribute>> collectionMap = new HashMap<>();

    public GilgameshFilterInvocationSecurityMetadataSource(SysRoleRepository sysRoleRepository,
                                                           SysApiAuthRepository sysApiAuthRepository,
                                                           SysRoleApiAuthRelationRepository sysRoleApiAuthRelationRepository) {
        this.sysRoleRepository = sysRoleRepository;
        this.sysApiAuthRepository = sysApiAuthRepository;
        this.sysRoleApiAuthRelationRepository = sysRoleApiAuthRelationRepository;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        return List.of();
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return List.of();
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }
}
