package com.gilgamesh.rest.sys;

import com.gilgamesh.biz.entity.dto.CaptchaDTO;
import com.gilgamesh.biz.entity.dto.LoginDTO;
import com.gilgamesh.biz.entity.vo.LoginVO;
import com.gilgamesh.biz.service.sys.UserIdentifyService;
import com.gilgamesh.common.annotations.ApiCodeMsg;
import com.gilgamesh.common.annotations.ApiVersion;
import com.gilgamesh.common.annotations.NotWrapApiResult;
import com.gilgamesh.common.entity.base.ApiResult;
import com.gilgamesh.common.enums.BizCodeMsg;
import com.gilgamesh.common.enums.SystemEnums;
import com.gilgamesh.common.exceptions.BusinessException;
import com.gilgamesh.common.utils.ConstantUtil;
import com.gilgamesh.common.utils.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.*;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 用户认证 controller
 * @createDate 2024/10/5 17:27
 * @since 1.0.0
 */
@ApiVersion
@RestController
@RequestMapping("/api/gilgamesh/{version}")
@Tag(name = "用户认证")
public class UserAuthResource {
    private final Logger logger = LoggerFactory.getLogger(UserAuthResource.class);

    /**
     * 用户认证服务
     */
    private final UserIdentifyService userIdentifyService;

    public UserAuthResource(UserIdentifyService userIdentifyService) {
        this.userIdentifyService = userIdentifyService;
    }

    /**
     * 生成图片验证码
     *
     * @return CaptchaDTO
     */
    @ApiCodeMsg(BizCodeMsg.VALIDATION_CODE_GENERATE_SUCCESS)
    @GetMapping("/image/captcha")
    @Operation(method = ConstantUtil.HTTP_METHOD_GET, summary = "生成图片验证码",
            responses = {
                    @ApiResponse(description = "请求成功",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = CaptchaDTO.class)),
                            responseCode = "200"),})
    public ResponseEntity<ApiResult<CaptchaDTO>> genImageCaptcha(@Min(value = 10, message = "图片宽度不能小于 10")
                                                                 @Parameter(description = "图片宽度, 单位: px, 默认200px")
                                                                 @RequestParam(value = "width", required = false, defaultValue = "200") Integer width,
                                                                 @Min(value = 10, message = "图片高度不能小于 10")
                                                                 @Parameter(description = "图片高度, 单位: px, 默认100px") @RequestParam(value = "height", required = false, defaultValue = "100") Integer height) {
        logger.info("开始生成图片验证码...");
        return userIdentifyService.genImageCaptcha(width, height);
    }

    /**
     * 用户登录
     *
     * @param loginVO 登录参数
     * @return APIResult<TokenDTO>
     */
    @Operation(method = ConstantUtil.HTTP_METHOD_GET, summary = "用户登录",
            responses = {
                    @ApiResponse(description = "请求成功",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = Void.class)),
                            responseCode = "200"),})
    @PostMapping("/login")
    @NotWrapApiResult
    public ResponseEntity<ApiResult<LoginDTO>> userLogin(@RequestBody LoginVO loginVO, HttpServletRequest request) {
        logger.info("REST 用户 {} 尝试登录", loginVO);
        if (loginVO == null ||
                StringUtil.isEmpty(loginVO.getUsername()) ||
                StringUtil.isEmpty(loginVO.getPassword())) {
            logger.error("用户名或密码为空,请重试");
            throw new BusinessException(BizCodeMsg.ACCESS_PARAM_INVALID);
        }
        String clientType = ServletRequestUtils.getStringParameter(request, ConstantUtil.HTTP_HEADER_CLIENT_TYPE, SystemEnums.ClientType.CLIENT_TYPE_WEB.getValue());
        logger.info("用户 {} 登录客户端类型: {}", loginVO.getUsername(), clientType);
        loginVO.setClientType(clientType);
        return userIdentifyService.login(loginVO);
    }
}
