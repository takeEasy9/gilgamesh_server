package com.gilgamesh.common.entity.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description
 * @createDate 2025/1/26 11:31
 * @since 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "web.security.policy")
public class WebSecurityPolicy {
    /**
     * 内容安全策略
     */
    private String contentSecurityPolicy;

    /**
     * 特征策略
     */
    private String webFeaturePolicy;

    public String getContentSecurityPolicy() {
        return contentSecurityPolicy;
    }

    public void setContentSecurityPolicy(String contentSecurityPolicy) {
        this.contentSecurityPolicy = contentSecurityPolicy;
    }

    public String getWebFeaturePolicy() {
        return webFeaturePolicy;
    }

    public void setWebFeaturePolicy(String webFeaturePolicy) {
        this.webFeaturePolicy = webFeaturePolicy;
    }

    @Override
    public String toString() {
        return "WebSecurityPolicy{" +
                "contentSecurityPolicy='" + contentSecurityPolicy + '\'' +
                ", webFeaturePolicy='" + webFeaturePolicy + '\'' +
                '}';
    }
}
