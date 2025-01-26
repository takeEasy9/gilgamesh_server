package com.gilgamesh.common.entity.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 图片验证码配置
 * @createDate 2025/1/26 10:32
 * @since 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "web.security.captcha")
public class CaptchaProperty {
    /**
     * 验证码过期时间, 单位: 秒
     */
    private Long expireInSeconds;

    public Long getExpireInSeconds() {
        return expireInSeconds;
    }

    public void setExpireInSeconds(Long expireInSeconds) {
        this.expireInSeconds = expireInSeconds;
    }

    @Override
    public String toString() {
        return "CaptchaProperty{" +
                "expireInSeconds=" + expireInSeconds +
                '}';
    }
}
