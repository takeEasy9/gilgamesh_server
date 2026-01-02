package com.gilgamesh.persistence.entity.gilgamesh.sys;

import com.gilgamesh.persistence.entity.base.VersionControlGenericBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serial;
import java.time.Instant;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 系统用户 entity
 * @createDate 2025/1/23 16:45
 * @since 1.0.0
 */
@Entity
@Table(schema = "gilgamesh", name = "sys_user")
@DynamicInsert
@DynamicUpdate
public class SysUser extends VersionControlGenericBaseEntity {
    @Serial
    private static final long serialVersionUID = -1865544633199071594L;

    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false)
    private String userId;

    /**
     * 用户登录名
     */
    @Column(name = "user_name", nullable = false)
    private String userName;

    /**
     * 用户昵称
     */
    @Column(name = "user_alias", nullable = false)
    private String userAlias;

    /**
     * 用户邮箱
     */
    @Column(name = "user_email")
    private String userEmail;

    /**
     * 用户手机号
     */
    @Column(name = "user_phone")
    private String userPhone;

    /**
     * 用户性别,1-男,2-女,3-保密
     */
    @Column(name = "user_gender", nullable = false)
    private String userGender;

    /**
     * 用户头像路径
     */
    @Column(name = "user_avatar")
    private String userAvatar;

    /**
     * 用户密码哈希
     */
    @Column(name = "user_password")
    private String userPassword;

    /**
     * 用户最后登录时间
     */
    @Column(name = "user_last_login_at", nullable = false)
    private Instant userLastLoginAt;

    /**
     * 用户最后登录的IP
     */
    @Column(name = "user_lats_login_ip")
    private String userLastLoginIp;

    /**
     * 用户账户激活key
     */
    @Column(name = "user_activate_key")
    private String userActivateKey;

    /**
     * 用户密码重置密码key
     */
    @Column(name = "user_password_reset_key")
    private String userPasswordResetKey;

    /**
     * 用户密码重置时间
     */
    @Column(name = "user_password_reset_at")
    private String userPasswordResetAt;

    /**
     * 用户状态,1-待激活,2-正常,3-锁定,4-密码过期,5-注销
     */
    @Column(name = "user_status", nullable = false)
    private String userStatus;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Instant getUserLastLoginAt() {
        return userLastLoginAt;
    }

    public void setUserLastLoginAt(Instant userLastLoginAt) {
        this.userLastLoginAt = userLastLoginAt;
    }

    public String getUserLastLoginIp() {
        return userLastLoginIp;
    }

    public void setUserLastLoginIp(String userLastLoginIp) {
        this.userLastLoginIp = userLastLoginIp;
    }

    public String getUserActivateKey() {
        return userActivateKey;
    }

    public void setUserActivateKey(String userActivateKey) {
        this.userActivateKey = userActivateKey;
    }

    public String getUserPasswordResetKey() {
        return userPasswordResetKey;
    }

    public void setUserPasswordResetKey(String userPasswordResetKey) {
        this.userPasswordResetKey = userPasswordResetKey;
    }

    public String getUserPasswordResetAt() {
        return userPasswordResetAt;
    }

    public void setUserPasswordResetAt(String userPasswordResetAt) {
        this.userPasswordResetAt = userPasswordResetAt;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    @Override
    public String toString() {
        return "SysUser{" +
                "super=" + super.toString() +
                "userId='" + userId + '\'' +
                "userName='" + userName + '\'' +
                ", userAlias='" + userAlias + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPhone='" + userPhone + '\'' +
                ", userGender='" + userGender + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userLastLoginAt=" + userLastLoginAt +
                ", userLastLoginIp='" + userLastLoginIp + '\'' +
                ", userActivateKey='" + userActivateKey + '\'' +
                ", userPasswordResetKey='" + userPasswordResetKey + '\'' +
                ", userPasswordResetAt='" + userPasswordResetAt + '\'' +
                ", userStatus='" + userStatus + '\'' +
                '}';
    }
}
