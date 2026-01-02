package com.gilgamesh.common.entity.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Set;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 封装JWT所需的用户信息
 * @createDate 2025/7/20 9:54
 * @since 1.0.0
 */
public class GilgameshUserDetail implements UserDetails {
    @Serial
    private static final long serialVersionUID = -8666785786665870087L;
    /**
     * 用户编码
     */
    private String userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码(明文)
     */
    private String password;

    /**
     * 用户昵称
     */
    private String userAlias;

    /**
     * 账号可用 标识
     */
    private boolean enabled;

    /**
     * 账号未过期 标识
     */
    private boolean accountNonExpired;

    /**
     * 账号未锁定 标识
     */
    private boolean accountNonLocked;

    /**
     * 密码未过期 标识
     */
    private boolean credentialsNonExpired;

    private Set<GrantedAuthority> authorities;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    /**
     * 账号是否可用
     *
     * @return boolean True-可用 False-不可用
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * 账号是否过期
     *
     * @return boolean True-未过期 False-已过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    /**
     * 账号是否锁定
     *
     * @return boolean True-未锁定 False-已锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    /**
     * 密码是否过期,默认为不过期
     *
     * @return boolean True-未过期 False-已过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String toString() {
        return "JwtUserDetail{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", userAlias='" + userAlias + '\'' +
                ", enabled=" + enabled +
                ", accountNonExpired=" + accountNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", authorities=" + authorities +
                '}';
    }
}
