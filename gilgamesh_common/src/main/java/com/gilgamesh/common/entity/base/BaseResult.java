package com.gilgamesh.common.entity.base;

import com.gilgamesh.common.enums.CodeMsg;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description api base result
 * @createDate 2024/10/2 21:58
 * @since 1.0.0
 */
@Schema(name = "BaseResult", description = "接口消息编码与消息内容")
public class BaseResult implements Serializable {
    @Serial
    private static final long serialVersionUID = -3238055636120225578L;
    /**
     * 消息编码
     */
    @Schema(name = "code", description = "消息编码")
    private String code;
    /**
     * 消息内容
     */
    @Schema(name = "msg", description = "消息内容")
    private String msg;

    public BaseResult(CodeMsg codeMsg) {
        this.code = codeMsg.getCode();
        this.msg = codeMsg.getMsg();
    }

    public BaseResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
