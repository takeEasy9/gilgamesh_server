package com.gilgamesh.common.enums;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description system code message enum
 * @createDate 2024/5/2 15:44
 * @since 1.0.0
 */
public enum SystemCodeMsg implements CodeMsg {

    /**
     * 系统正常
     */
    SYSTEM_SUCCESS(SystemCode.SYSTEM_SUCCESS, "系统正常"),
    /**
     * 系统异常
     */
    SYSTEM_FAILED(SystemCode.SYSTEM_SUCCESS, "系统异常"),

    SYSTEM_HTTP_API_RESPONSE_EXCEPTION(SystemCode.SYSTEM_HTTP_API_RESPONSE_EXCEPTION, "接口响应异常, 请检查接口请求是否正确"),
    SYSTEM_HTTP_API_NOT_FOUND(SystemCode.SYSTEM_HTTP_API_NOT_FOUND, "接口不存在, 请检查url与请求方式是否正确"),
    SYSTEM_HTTP_API_JSON_PROCESS_EXCEPTION(SystemCode.SYSTEM_HTTP_API_JSON_PROCESS_EXCEPTION, "JSON处理异常"),
    ;

    /**
     * 消息编码
     */
    private final String code;

    /**
     * 消息编码含义
     */
    private final String msg;

    SystemCodeMsg(Code code, String msg) {
        this.code = code.getCode();
        this.msg = msg;
    }


    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
