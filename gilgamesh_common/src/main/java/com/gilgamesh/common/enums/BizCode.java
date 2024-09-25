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
    BIZ_SUCCESS("B0000"),
    /**
     * GUI失败
     */
    BIZ_FAILED("B0001"),

    // 参数无效
    ACCESS_PARAMETER_INVALID("B0100"),
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
