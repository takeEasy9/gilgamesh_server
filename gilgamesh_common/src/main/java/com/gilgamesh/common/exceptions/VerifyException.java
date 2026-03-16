package com.gilgamesh.common.exceptions;

import com.gilgamesh.common.enums.BizCodeMsg;
import org.springframework.security.core.AuthenticationException;

import java.io.Serial;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 自定义验证码异常
 * @createDate 2026/1/3 8:47
 * @since 1.0.0
 */
public class VerifyException extends AuthenticationException {

    @Serial
    private static final long serialVersionUID = -55899263545830529L;
    /**
     * 错误码
     */
    private final String code;
    /**
     * 错误信息
     */
    private final String msg;

    public VerifyException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }


    public VerifyException(BizCodeMsg codeMsg) {
        super(codeMsg.getMsg());
        this.code = codeMsg.getCode();
        this.msg = codeMsg.getMsg();
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String toString() {
        return "{\"code\":\"" + this.code + "\", \"msg\":\"" + this.msg + "\"}";
    }
}
