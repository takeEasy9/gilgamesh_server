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

    // 用户登录异常
    VALIDATION_CODE_GENERATE_SUCCESS(BizCode.VALIDATION_CODE_GENERATE_SUCCESS, "验证码生成成功"),
    VALIDATION_CODE_GENERATE_FAILED(BizCode.VALIDATION_CODE_GENERATE_FAILED, "验证码生成失败"),
    VALIDATION_CODE_VERIFY_FAILED(BizCode.VALIDATION_CODE_GENERATE_FAILED, "验证码错误或验证码已过期"),
    USER_LOGIN_FAILED(BizCode.USER_LOGIN_FAILED, "您此次登录出现异常,请重试或联系管理员"),
    USER_LOGIN_ACCOUNT_NOT_EXISTS(BizCode.USER_LOGIN_ACCOUNT_NOT_EXISTS, "用户账号不存在"),
    USER_LOGIN_ACCOUNT_FORBIDDEN(BizCode.USER_LOGIN_ACCOUNT_FORBIDDEN, "您的账号已被锁定,请解锁或联系管理员"),
    USER_LOGIN_ACCOUNT_DESTROY(BizCode.USER_LOGIN_ACCOUNT_DESTROY, "您的账号已被注销,如需找回请联系管理员"),
    USER_LOGIN_ACCOUNT_INACTIVE(BizCode.USER_LOGIN_ACCOUNT_INACTIVE, "您的账号尚未激活,请先激活"),
    USER_LOGIN_PASSWORD_EXPIRED(BizCode.USER_LOGIN_PASSWORD_EXPIRED, "您的账号密码已过期,请重置密码"),

    USER_LOGIN_PASSWORD_CHECK_FAILED(BizCode.USER_LOGIN_PASSWORD_CHECK_FAILED, "用户密码错误"),
    USER_LOGIN_PASSWORD_ENTRY_LIMITED(BizCode.USER_LOGIN_PASSWORD_ENTRY_LIMITED, "用户输入密码次数超限"),
    USER_LOGIN_FAILED_WITH_NAME_PASSWORD(BizCode.USER_LOGIN_FAILED_WITH_NAME_PASSWORD, "账号或密码错误,请重试"),
    USER_LOGIN_SUCCESS(BizCode.USER_LOGIN_SUCCESS, "用户登录成功!"),

    USER_LOGOUT_FAILED(BizCode.USER_LOGOUT_FAILED, "用户注销失败"),
    USER_LOGOUT_SUCCESS(BizCode.USER_LOGOUT_SUCCESS, "用户注销成功"),

    // Token 异常
    AUTH_NOT_LOGIN(BizCode.AUTH_NOT_LOGIN, "用户未登录,无法访问"),
    AUTH_USER_INVALID(BizCode.AUTH_USER_INVALID, "用户无效"),
    AUTH_ACCESS_TOKEN_EXPIRED(BizCode.AUTH_TOKEN_EXPIRED, "Access Token 已过期"),
    AUTH_REFRESH_TOKEN_EXPIRED(BizCode.AUTH_TOKEN_EXPIRED, "Refresh Token 已过期"),
    // Access Token 无效(未提供、格式错误、jwt信息无效)
    AUTH_ACCESS_TOKEN_INVALID(BizCode.AUTH_TOKEN_EXPIRED, "Access Token 无效"),
    // Refresh Token 无效(未提供、格式错误、jwt信息无效)
    AUTH_REFRESH_TOKEN_INVALID(BizCode.AUTH_TOKEN_EXPIRED, "Refresh Token 无效"),
    AUTH_ACCESS_TOKEN_EXPIRED_RENEW_FAILED(BizCode.AUTH_TOKEN_EXPIRED_RENEW_FAILED, "Token 续期失败，请重新登录"),
    AUTH_NO_PERMISSION(BizCode.AUTH_NO_PERMISSION, "用户无权限访问"),
    AUTH_TOKEN_REVOKED(BizCode.AUTH_TOKEN_REVOKED, "Token 已注销，请重新重新登录"),
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
