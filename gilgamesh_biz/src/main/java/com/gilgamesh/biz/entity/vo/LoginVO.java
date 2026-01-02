package com.gilgamesh.biz.entity.vo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 用户登录VO
 * @createDate 2025/7/19 10:06
 * @since 1.0.0
 */
public class LoginVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 5382241637408972813L;
    /**
     * 用户名
     */
    @NotNull
    @Size(min = 1, max = 32)
    private String username;

    /**
     * 密码(明文)
     */
    @NotNull
    @Size(min = 8, max = 20)
    private String password;


    /**
     * 验证码
     */
    @NotNull
    @Size(min = 4, max = 4)
    private String verifyCode;

    /**
     * 验证码key
     */
    @NotNull
    @Size(min = 32, max = 32)
    private String verifyKey;

    /**
     * 客户端类型
     */
    private String clientType;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getVerifyKey() {
        return verifyKey;
    }

    public void setVerifyKey(String verifyKey) {
        this.verifyKey = verifyKey;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    @Override
    public String toString() {
        return "LoginVO{" +
                "username='" + username + '\'' +
                ", verifyCode='" + verifyCode + '\'' +
                ", verifyKey='" + verifyKey + '\'' +
                ", clientType='" + clientType + '\'' +
                '}';
    }
}
