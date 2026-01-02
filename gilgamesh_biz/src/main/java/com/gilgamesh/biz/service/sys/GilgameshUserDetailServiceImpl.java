package com.gilgamesh.biz.service.sys;

import com.gilgamesh.common.entity.security.GilgameshUserDetail;
import com.gilgamesh.common.enums.BizCodeMsg;
import com.gilgamesh.common.enums.EnumValue;
import com.gilgamesh.common.enums.GilgameshEnums;
import com.gilgamesh.common.enums.SystemEnums;
import com.gilgamesh.common.exceptions.BusinessException;
import com.gilgamesh.common.utils.CollectionUtil;
import com.gilgamesh.common.utils.StringUtil;
import com.gilgamesh.persistence.entity.gilgamesh.sys.*;
import com.gilgamesh.persistence.repository.mysql.gilgamesh.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 用户详情实现类
 * @createDate 2025/7/20 9:00
 * @since 1.0.0
 */
@Component("userDetailsService")
public class GilgameshUserDetailServiceImpl implements UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(GilgameshUserDetailServiceImpl.class);

    /**
     * 用户数据库访问接口
     */
    private final SysUserRepository sysUserRepository;

    /**
     * 角色数据库访问接口
     */
    private final SysRoleRepository sysRoleRepository;

    /**
     * API权限数据库访问接口
     */
    private final SysApiAuthRepository sysApiAuthRepository;

    /**
     * 用户角色关联关系数据库访问接口
     */
    private final SysUserRoleRelationRepository sysUserRoleRelationRepository;

    /**
     * 角色API权限关联关系数据库访问接口
     */
    private final SysRoleApiAuthRelationRepository sysRoleApiAuthRelationRepository;


    @Autowired
    public GilgameshUserDetailServiceImpl(SysUserRepository sysUserRepository, SysRoleRepository sysRoleRepository, SysApiAuthRepository sysApiAuthRepository, SysUserRoleRelationRepository sysUserRoleRelationRepository, SysRoleApiAuthRelationRepository sysRoleApiAuthRelationRepository) {
        this.sysUserRepository = sysUserRepository;
        this.sysRoleRepository = sysRoleRepository;
        this.sysApiAuthRepository = sysApiAuthRepository;
        this.sysUserRoleRelationRepository = sysUserRoleRelationRepository;
        this.sysRoleApiAuthRelationRepository = sysRoleApiAuthRelationRepository;
    }

    /**
     * 根据用户名加载用户详情
     *
     * @param username 用户名
     * @return UserDetails 用户详情
     * @throws UsernameNotFoundException 用户名未找到异常
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserRepository.findByUserNameOrUserPhoneOrUserEmail(username);
        if (sysUser == null || sysUser.getId() == null) {
            logger.error("error message: 用户 <{}> 不存在", username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        try {
            // 用户所拥有的角色集合
            Set<GrantedAuthority> grantedAuthoritySet;
            // 查询该用户所拥有的有效角色
            List<SysUserRoleRelation> sysUserRoleRelationList = sysUserRoleRelationRepository.findByUserIdAndDeletedStatus(sysUser.getId(), SystemEnums.DeletedStatus.NOT_DELETED.getValue());
            if (CollectionUtil.isEmpty(sysUserRoleRelationList)) {
                logger.error("error message: 用户 <{}> 没有分配任何角色", sysUser.getUserName());
                grantedAuthoritySet = new HashSet<>();
            } else {
                Set<Long> roleIds = sysUserRoleRelationList.stream()
                        .map(SysUserRoleRelation::getRoleId)
                        .collect(Collectors.toSet());
                grantedAuthoritySet = getUserRoles(sysUser.getUserName(), roleIds);
            }
            // 构造用户jwt基础信息
            GilgameshUserDetail gilgameshUserDetail = new GilgameshUserDetail();
            gilgameshUserDetail.setUserId(sysUser.getUserId());
            gilgameshUserDetail.setUsername(sysUser.getUserName());
            gilgameshUserDetail.setUserAlias(sysUser.getUserAlias());
            gilgameshUserDetail.setPassword(sysUser.getUserPassword());
            gilgameshUserDetail.setAuthorities(Collections.unmodifiableSet(grantedAuthoritySet));
            setUserAccountStatus(sysUser, gilgameshUserDetail);
            return gilgameshUserDetail;
        } catch (Exception e) {
            logger.error("error message: 用户登录出现错误,原因是:", e);
            throw new BusinessException(BizCodeMsg.USER_LOGIN_FAILED);
        }
    }

    /**
     * 获取用户所拥有的角色
     *
     * @param username String
     * @return Set<GrantedAuthority>
     */
    private Set<GrantedAuthority> getUserRoles(String username, Set<Long> roleIds) {
        // 用户所拥有的角色集合
        Set<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
        // 查询角色信息
        List<SysRole> sysRoleList = sysRoleRepository.findByIdInAndDeletedStatus(roleIds, SystemEnums.DeletedStatus.NOT_DELETED.getValue());
        if (CollectionUtil.isEmpty(sysRoleList)) {
            logger.error("error message: 该用户 <{}> 所设置角色均已失效,该用户无系统访问权限", username);
            return grantedAuthoritySet;
        }
        return sysRoleList.stream()
                .map(SysRole::getRoleEnName)
                .map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    /**
     * 设置用户账户状态
     *
     * @param sysUser       SysUser
     * @param jwtUserDetail JwtUserDetail
     */
    private void setUserAccountStatus(SysUser sysUser, GilgameshUserDetail jwtUserDetail) {
        // 账户可用
        boolean enabled = true;
        // 账户未过期
        boolean accountNonExpired = true;
        // 密码未过期
        boolean credentialsNonExpired = true;
        // 账号未锁定
        boolean accountNonLocked = true;
        GilgameshEnums.UserStatus userStatus = EnumValue.getEnumByValue(GilgameshEnums.UserStatus.class, sysUser.getUserStatus());
        // 未知用户状态,默认该账户不可用
        if (null == userStatus) {
            enabled = false;
        }
        // 状态已知
        else {
            switch (userStatus) {
                // 账户正常
                case GilgameshEnums.UserStatus.USER_STATUS_NORMAL:
                    break;
                // 账户锁定
                case GilgameshEnums.UserStatus.USER_STATUS_LOCKED:
                    accountNonLocked = false;
                    break;
                // 密码过期
                case USER_STATUS_PASSWORD_EXPIRED:
                    credentialsNonExpired = false;
                    break;
                // 账户注销(即已过期)
                case GilgameshEnums.UserStatus.USER_STATUS_DEACTIVATED:
                    accountNonExpired = false;
                    break;
                // 账户未激活
                case GilgameshEnums.UserStatus.USER_STATUS_PENDING_ACTIVATION:
                    enabled = false;
                    break;
                // 暂不支持的用户状态,默认该账户不可用
                default:
                    enabled = false;
                    logger.error("error message: 暂不支持的用户状态 <{}> ", userStatus.getLabel());
                    break;
            }
        }
        jwtUserDetail.setEnabled(enabled);
        jwtUserDetail.setAccountNonExpired(accountNonExpired);
        jwtUserDetail.setCredentialsNonExpired(credentialsNonExpired);
        jwtUserDetail.setAccountNonLocked(accountNonLocked);
    }

    /**
     * 获取用户所拥有的API权限
     *
     * @param username String
     * @param roleIds  Set<Long>
     * @return Set<GrantedAuthority>
     */
    private Set<GrantedAuthority> getUserApiAuths(String username, Set<Long> roleIds) {
        // 用户所拥有的API权限集合
        Set<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
        List<SysRoleApiAuthRelation> sysRoleApiAuthRelationList = sysRoleApiAuthRelationRepository.findByRoleIdInAndDeletedStatus(roleIds, SystemEnums.DeletedStatus.NOT_DELETED.getValue());
        if (CollectionUtil.isEmpty(sysRoleApiAuthRelationList)) {
            logger.error("error message: 该用户 <{}> 所关联的角色暂未设置任何权限", username);
            return grantedAuthoritySet;
        }
        // 获取API权限ID集合
        Set<Long> apiAuthIds = sysRoleApiAuthRelationList.stream().map(SysRoleApiAuthRelation::getApiAuthId).collect(Collectors.toSet());
        // 查询API权限信息
        List<SysApiAuth> sysApiAuthList = sysApiAuthRepository.findByIdInAndDeletedStatus(apiAuthIds, SystemEnums.DeletedStatus.NOT_DELETED.getValue());
        for (SysApiAuth sysApiAuth : sysApiAuthList) {
            if (sysApiAuth == null || StringUtil.isEmpty(sysApiAuth.getApiAuthEnName())) {
                logger.error("error message: 该权限 <{}> 信息无效,请检查", sysApiAuth);
                continue;
            }
            // 使用权限英文名进行初始化
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(sysApiAuth.getApiAuthEnName());
            grantedAuthoritySet.add(grantedAuthority);
        }
        return grantedAuthoritySet;
    }

    public static void main(String[] args) {
        System.out.println(LocalDateTime.now());
    }
}
