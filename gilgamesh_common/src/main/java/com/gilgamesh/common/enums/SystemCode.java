package com.gilgamesh.common.enums;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description system code message enum
 * @createDate 2024/5/2 15:43
 * @since 1.0.0
 */
public enum SystemCode implements Code {

    /**
     * 系统正常
     */
    SYSTEM_SUCCESS("S0000"),
    /**
     * 系统异常
     */
    SYSTEM_FAILED("S0001"),

    /**
     * 接口404异常
     */
    SYSTEM_HTTP_API_RESPONSE_EXCEPTION("S0100"),

    /**
     * 接口404异常
     */
    SYSTEM_HTTP_API_NOT_FOUND("S0101"),

    /**
     * 接口返回json处理异常
     */
    SYSTEM_HTTP_API_JSON_PROCESS_EXCEPTION("S0102"),
    ;

    /**
     * 消息编码
     */
    private final String code;

    SystemCode(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return this.code;
    }
}
