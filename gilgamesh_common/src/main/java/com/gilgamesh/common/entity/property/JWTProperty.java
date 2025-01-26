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
public class JWTProperty {
    /**
     * JWT 请求头
     */
    private String header;

    /**
     * JWT 刷新 token
     */
    private String refreshTokenHeader;

    /**
     * base64 加密密钥
     */
    private String base64Secret;

    /**
     * token 有效时间, 默认30分钟
     */
    private Long tokenExpireInSeconds;

    /**
     * 记住我, token 有效时间
     */
    private String tokenExpireInSecondsForRememberMe;

    /**
     * 缓存延期时间,默认15分钟
     */
    private Long cacheExtendInSeconds;

    /**
     * 缓存过期前的 n 分钟内用户仍在操作,目前 n 默认取值为 3 分钟 = 180s
     */
    private Long tokenCacheRemainInSeconds;

    /**
     * token 前缀
     */
    private String tokenHead;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getRefreshTokenHeader() {
        return refreshTokenHeader;
    }

    public void setRefreshTokenHeader(String refreshTokenHeader) {
        this.refreshTokenHeader = refreshTokenHeader;
    }

    public String getBase64Secret() {
        return base64Secret;
    }

    public void setBase64Secret(String base64Secret) {
        this.base64Secret = base64Secret;
    }

    public Long getTokenExpireInSeconds() {
        return tokenExpireInSeconds;
    }

    public void setTokenExpireInSeconds(Long tokenValidityInSeconds) {
        this.tokenExpireInSeconds = tokenValidityInSeconds;
    }

    public String getTokenExpireInSecondsForRememberMe() {
        return tokenExpireInSecondsForRememberMe;
    }

    public void setTokenExpireInSecondsForRememberMe(String tokenValidityInSecondsForRememberMe) {
        this.tokenExpireInSecondsForRememberMe = tokenValidityInSecondsForRememberMe;
    }

    public Long getCacheExtendInSeconds() {
        return cacheExtendInSeconds;
    }

    public void setCacheExtendInSeconds(Long cacheExtendInSeconds) {
        this.cacheExtendInSeconds = cacheExtendInSeconds;
    }

    public Long getTokenCacheRemainInSeconds() {
        return tokenCacheRemainInSeconds;
    }

    public void setTokenCacheRemainInSeconds(Long tokenCacheRemainInSeconds) {
        this.tokenCacheRemainInSeconds = tokenCacheRemainInSeconds;
    }

    public String getTokenHead() {
        return tokenHead;
    }

    public void setTokenHead(String tokenHead) {
        this.tokenHead = tokenHead;
    }

    @Override
    public String toString() {
        return "JWTProperty{" +
                "header='" + header + '\'' +
                ", refreshTokenHeader='" + refreshTokenHeader + '\'' +
                ", base64Secret='" + base64Secret + '\'' +
                ", tokenValidityInSeconds=" + tokenExpireInSeconds +
                ", tokenValidityInSecondsForRememberMe='" + tokenExpireInSecondsForRememberMe + '\'' +
                ", cacheExtendInSeconds=" + cacheExtendInSeconds +
                ", tokenCacheRemainInSeconds=" + tokenCacheRemainInSeconds +
                ", tokenHead='" + tokenHead + '\'' +
                '}';
    }
}
