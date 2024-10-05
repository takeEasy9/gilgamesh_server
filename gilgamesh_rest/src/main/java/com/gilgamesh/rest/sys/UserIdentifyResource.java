package com.gilgamesh.rest.sys;

import com.gilgamesh.biz.entity.dto.CaptchaDTO;
import com.gilgamesh.biz.service.sys.UserIdentifyService;
import com.gilgamesh.common.annotations.ApiCodeMsg;
import com.gilgamesh.common.annotations.ApiVersion;
import com.gilgamesh.common.enums.BizCodeMsg;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 用户认证 controller
 * @createDate 2024/10/5 17:27
 * @since 1.0.0
 */
@ApiVersion
@RestController
@RequestMapping("/api/gilgamesh/{version}/identify")
@Tag(name = "用户认证")
public class UserIdentifyResource {
    private final Logger logger = LoggerFactory.getLogger(UserIdentifyResource.class);

    /**
     * 用户认证服务
     */
    private final UserIdentifyService userIdentifyService;

    public UserIdentifyResource(UserIdentifyService userIdentifyService) {
        this.userIdentifyService = userIdentifyService;
    }

    /**
     * 生成图片验证码
     *
     * @return CaptchaDTO
     */
    @ApiCodeMsg(BizCodeMsg.VALIDATION_CODE_GENERATE_SUCCESS)
    @GetMapping("/imageCaptcha")
    @Operation(method = "GET", summary = "生成图片验证码",
            responses = {
                    @ApiResponse(description = "请求成功",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CaptchaDTO.class)),
                            responseCode = "200"),})
    public CaptchaDTO genImageCaptcha() {
        logger.info("开始生成图片验证码...");
        return userIdentifyService.genImageCaptcha();
    }
}
