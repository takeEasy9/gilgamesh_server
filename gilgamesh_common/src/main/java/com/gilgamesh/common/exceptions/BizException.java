package com.gilgamesh.common.exceptions;

import com.gilgamesh.common.enums.CodeMsg;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 自定义业务异常
 * @createDate 2024/5/2 16:25
 * @since 1.0.0
 */
public class BizException extends RuntimeException implements Serializable {
    @Serial
    private static final long serialVersionUID = 4015687047944726552L;

    /**
     * 消息编码
     */
    private final String code;

    /**
     * 消息编码含义
     */
    private final String msg;

    public BizException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BizException(CodeMsg codeMsg) {
        this.code = codeMsg.getCode();
        this.msg = codeMsg.getMsg();
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
