package com.gilgamesh.biz.service.sys;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import com.gilgamesh.biz.entity.dto.CaptchaDTO;
import com.gilgamesh.common.enums.BizCodeMsg;
import com.gilgamesh.common.exceptions.BizException;
import com.gilgamesh.common.redis.RedisService;
import com.gilgamesh.common.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 用户认证服务
 * @createDate 2024/10/5 15:18
 * @since 1.0.0
 */
@Service
public class UserIdentifyService {
    private final Logger logger = LoggerFactory.getLogger(UserIdentifyService.class);

    /**
     * redis 服务
     */
    private final RedisService redisService;

    public UserIdentifyService(RedisService redisService) {
        this.redisService = redisService;
    }

    /**
     * 生成图片验证码
     *
     * @return CaptchaDTO
     */
    public CaptchaDTO genImageCaptcha() {
        // 使用uuid作为captchaKey
        String captchaKey = UUID.randomUUID().toString();
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 20);
        String captchaCode = captcha.getCode();
        if (StringUtil.isNotEmpty(captchaCode) && redisService.setValue(captchaKey, captcha.getCode(), 5 * 60)) {
            return new CaptchaDTO(captchaKey, captcha.getImageBase64());
        } else {
            logger.error("生成图片验证码失败: 验证码:{}为空或验证码key写入缓存失败", captchaCode);
            throw new BizException(BizCodeMsg.VALIDATION_CODE_GENERATE_FAILED);
        }
    }
}
