package com.gilgamesh.biz.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description token DTO
 * @createDate 2025/11/11 22:06
 * @since 1.0.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 6188689043028821526L;

    /**
     * 访问 token, 30 分钟有效
     */
    private String accessToken;

    /**
     * 访问 token jti
     */
    private String atJti;

    /**
     * 访问 token 过期时间
     */
    private long atExpiresIn;

    /**
     * 刷新 token, 7 天有效
     */
    private String refreshToken;

    /**
     * 刷新 token jti
     */
    private String rtJti;

    /**
     * 刷新 token 过期时间
     */
    private long rtExpiresIn;

    public TokenDTO() {
    }

    public TokenDTO(String accessToken, String atJti, String refreshToken, String rtJti) {
        this.accessToken = accessToken;
        this.atJti = atJti;
        this.refreshToken = refreshToken;
        this.rtJti = rtJti;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAtJti() {
        return atJti;
    }

    public void setAtJti(String atJti) {
        this.atJti = atJti;
    }

    public long getAtExpiresIn() {
        return atExpiresIn;
    }

    public void setAtExpiresIn(long atExpiresIn) {
        this.atExpiresIn = atExpiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRtJti() {
        return rtJti;
    }

    public void setRtJti(String rtJti) {
        this.rtJti = rtJti;
    }

    public long getRtExpiresIn() {
        return rtExpiresIn;
    }

    public void setRtExpiresIn(long rtExpiresIn) {
        this.rtExpiresIn = rtExpiresIn;
    }

    @Override
    public String toString() {
        return "TokenDTO{" +
                "accessToken='" + accessToken + '\'' +
                ", atJti='" + atJti + '\'' +
                ", atExpiresIn=" + atExpiresIn +
                ", refreshToken='" + refreshToken + '\'' +
                ", rtJti='" + rtJti + '\'' +
                ", rtExpiresIn=" + rtExpiresIn +
                '}';
    }
}
