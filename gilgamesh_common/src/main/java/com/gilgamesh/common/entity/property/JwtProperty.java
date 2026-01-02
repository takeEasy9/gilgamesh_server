package com.gilgamesh.common.entity.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description jwt 配置信息
 * @createDate 2025/1/26 10:20
 * @since 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "web.security.jwt")
public class JwtProperty {
    /**
     * 认证请求头名称
     */
    private String header;

    /**
     * Token前缀（包含末尾空格）
     */
    private String tokenHead;

    /**
     * RSA签名公钥（Base64编码，无PEM头/尾）
     */
    private String signRsaPublicKey;

    /**
     * RSA签名私钥（Base64编码，无PEM头/尾）
     */
    private String signRsaPrivateKey;

    /**
     * ECDSA签名公钥（Base64编码，无PEM头/尾）
     */
    private String signEcdsaPublicKey;

    /**
     * ECDSA签名私钥（Base64编码，无PEM头/尾）
     */
    private String signEcdsaPrivateKey;

    /**
     * Access Token有效期（毫秒），默认30分钟
     */
    private long accessTokenExpireMs;

    /**
     * refresh token 续期次数
     */
    private int maxRtExchangeTimes;

    /**
     * Refresh Token有效期（毫秒），默认7天
     */
    private long refreshTokenExpireMs;

    /**
     * Access Token续期阈值（毫秒），默认1分钟
     */
    private long atExpireThresholdMs;

    /**
     * Refresh Token有效期（毫秒），默认3分钟
     */
    private long rtExpireThresholdMs;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getTokenHead() {
        return tokenHead;
    }

    public void setTokenHead(String tokenHead) {
        this.tokenHead = tokenHead;
    }

    public String getSignRsaPublicKey() {
        return signRsaPublicKey;
    }

    public void setSignRsaPublicKey(String signRsaPublicKey) {
        this.signRsaPublicKey = signRsaPublicKey;
    }

    public String getSignRsaPrivateKey() {
        return signRsaPrivateKey;
    }

    public void setSignRsaPrivateKey(String signRsaPrivateKey) {
        this.signRsaPrivateKey = signRsaPrivateKey;
    }

    public String getSignEcdsaPublicKey() {
        return signEcdsaPublicKey;
    }

    public void setSignEcdsaPublicKey(String signEcdsaPublicKey) {
        this.signEcdsaPublicKey = signEcdsaPublicKey;
    }

    public String getSignEcdsaPrivateKey() {
        return signEcdsaPrivateKey;
    }

    public void setSignEcdsaPrivateKey(String signEcdsaPrivateKey) {
        this.signEcdsaPrivateKey = signEcdsaPrivateKey;
    }

    public int getMaxRtExchangeTimes() {
        return maxRtExchangeTimes;
    }

    public void setMaxRtExchangeTimes(int maxRtExchangeTimes) {
        this.maxRtExchangeTimes = maxRtExchangeTimes;
    }

    public long getAccessTokenExpireMs() {
        return accessTokenExpireMs;
    }

    public void setAccessTokenExpireMs(long accessTokenExpireMs) {
        this.accessTokenExpireMs = accessTokenExpireMs;
    }

    public long getRefreshTokenExpireMs() {
        return refreshTokenExpireMs;
    }

    public void setRefreshTokenExpireMs(long refreshTokenExpireMs) {
        this.refreshTokenExpireMs = refreshTokenExpireMs;
    }

    public long getAtExpireThresholdMs() {
        return atExpireThresholdMs;
    }

    public void setAtExpireThresholdMs(long atExpireThresholdMs) {
        this.atExpireThresholdMs = atExpireThresholdMs;
    }

    public long getRtExpireThresholdMs() {
        return rtExpireThresholdMs;
    }

    public void setRtExpireThresholdMs(long rtExpireThresholdMs) {
        this.rtExpireThresholdMs = rtExpireThresholdMs;
    }

    @Override
    public String toString() {
        return "JWTProperty{" +
                "header='" + header + '\'' +
                ", tokenHead='" + tokenHead + '\'' +
                ", signRsaPublicKey='" + signRsaPublicKey + '\'' +
                ", signRsaPrivateKey='" + signRsaPrivateKey + '\'' +
                ", signEcdsaPublicKey='" + signEcdsaPublicKey + '\'' +
                ", signEcdsaPrivateKey='" + signEcdsaPrivateKey + '\'' +
                ", accessTokenExpirationMs=" + accessTokenExpireMs +
                ", refreshTokenExpirationMs=" + refreshTokenExpireMs +
                ", atExpireThresholdMs=" + atExpireThresholdMs +
                ", rtExpireThresholdMs=" + rtExpireThresholdMs +
                '}';
    }
}
