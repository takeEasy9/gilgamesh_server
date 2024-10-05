package com.gilgamesh.biz.entity.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 验证码DTO
 * @createDate 2024/10/5 17:07
 * @since 1.0.0
 */
@Schema(name = "CaptchaDTO", description = "图片验证码接口返回结果")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CaptchaDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 923781351014961601L;
    /**
     * 验证码Key
     */
    @Schema(name = "captchaKey", description = "验证码Key")
    private String captchaKey;

    /**
     * 验证码
     */
    @Schema(name = "captcha", description = "图片base64字符串")
    private String captcha;

    public CaptchaDTO() {

    }

    public CaptchaDTO(String captchaKey) {
        this.captchaKey = captchaKey;
    }

    public CaptchaDTO(String captchaKey, String captcha) {
        this.captchaKey = captchaKey;
        this.captcha = captcha;
    }

    public String getCaptchaKey() {
        return captchaKey;
    }

    public void setCaptchaKey(String captchaKey) {
        this.captchaKey = captchaKey;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    @Override
    public String toString() {
        return "CaptchaDTO{" + "captchaKey='" + captchaKey + '\'' + ", captcha='" + captcha + '\'' + '}';
    }
}
