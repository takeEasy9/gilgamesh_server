package com.gilgamesh.common.enums;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 业务消息编码
 * @createDate 2024/5/2 15:44
 * @since 1.0.0
 */
public enum BizCode implements Code {
    /**
     * GUI成功
     */
    BIZ_SUCCESS("B2000"),
    /**
     * GUI失败
     */
    BIZ_FAILED("B2001"),

    // 参数无效
    ACCESS_PARAMETER_INVALID("B2002"),
    DATE_CONVERT_FAILED("B2003"),


    /**
     * 用户登录失败
     */
    USER_LOGIN_SUCCESS("A1000"),
    USER_LOGIN_FAILED("A1001"),

    // 用户登录 & 注销异常
    USER_LOGIN_ACCOUNT_NOT_EXISTS("A1002"),
    USER_LOGIN_ACCOUNT_FORBIDDEN("A1003"),
    USER_LOGIN_ACCOUNT_DESTROY("A1004"),
    USER_LOGIN_ACCOUNT_INACTIVE("A1005"),
    USER_LOGIN_PASSWORD_EXPIRED("A1006"),

    USER_LOGIN_PASSWORD_CHECK_FAILED("A1007"),
    USER_LOGIN_PASSWORD_ENTRY_LIMITED("A1008"),
    USER_LOGIN_FAILED_WITH_NAME_PASSWORD("A1009"),

    USER_LOGOUT_FAILED("A1010"),
    USER_LOGOUT_SUCCESS("A1011"),
    VALIDATION_CODE_GENERATE_SUCCESS("A1012"),
    VALIDATION_CODE_GENERATE_FAILED("A1013"),
    VALIDATION_CODE_VERIFY_FAILED("A1014"),
    AUTH_NOT_LOGIN("A1015"),
    AUTH_USER_INVALID("A1015"),
    AUTH_TOKEN_EXPIRED("A1016"),
    AUTH_TOKEN_INVALID("A1016"),
    AUTH_TOKEN_EXPIRED_RENEW_FAILED("A1017"),
    AUTH_NO_PERMISSION("A1018"),
    AUTH_TOKEN_REVOKED("A1019"),
    ;
    /**
     * 消息编码
     */
    private final String code;

    BizCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
