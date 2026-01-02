package com.gilgamesh.biz.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 登录 DTO
 * @createDate 2026/1/1 18:05
 * @since 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 6188689043028821526L;
    /**
     * 用户ID
     */
    private String uid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户昵称
     */
    private String userAlias;

    public LoginDTO() {
    }

    public LoginDTO(String uid, String username, String userAlias) {
        this.uid = uid;
        this.username = username;
        this.userAlias = userAlias;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserAlias() {
        return userAlias;
    }

    public void setUserAlias(String userAlias) {
        this.userAlias = userAlias;
    }

    @Override
    public String toString() {
        return "LoginDTO{" +
                "uid='" + uid + '\'' +
                ", username='" + username + '\'' +
                ", userAlias='" + userAlias + '\'' +
                '}';
    }
}
