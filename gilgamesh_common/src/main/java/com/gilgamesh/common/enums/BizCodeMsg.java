package com.gilgamesh.common.enums;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description business code message enum
 * @createDate 2024/5/2 15:40
 * @since 1.0.0
 */
public enum BizCodeMsg implements CodeMsg {
    /**
     * GUI运行正常
     */
    GUI_SUCCESS(BizCode.BIZ_SUCCESS, "GUI运行正常"),
    /**
     * 用户端运行异常
     */
    GUI_FAILED(BizCode.BIZ_FAILED, "GUI运行异常"),

    // 参数异常
    ACCESS_PARAM_INVALID(BizCode.ACCESS_PARAMETER_INVALID, "入参无效，请检查"),

    DATE_CONVERT_FAILED(BizCode.DATE_CONVERT_FAILED, "日期转换失败"),

    VALIDATION_CODE_GENERATE_SUCCESS(BizCode.VALIDATION_CODE_GENERATE_SUCCESS, "验证码生成成功"),

    VALIDATION_CODE_GENERATE_FAILED(BizCode.VALIDATION_CODE_GENERATE_FAILED, "验证码生成失败"),
    ;


    /**
     * 消息编码
     */
    private final String code;

    /**
     * 消息编码含义
     */
    private final String msg;

    BizCodeMsg(Code code, String msg) {
        this.code = code.getCode();
        this.msg = msg;
    }

    ;


    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
