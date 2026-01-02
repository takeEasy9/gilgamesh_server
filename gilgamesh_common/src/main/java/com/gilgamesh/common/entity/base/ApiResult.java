package com.gilgamesh.common.entity.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gilgamesh.common.enums.CodeMsg;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 接口统一返回结构
 * @createDate 2024/10/2 22:02
 * @since 1.0.0
 */
@Schema(name = "BaseResult", description = "接口统一返回结构实体类")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResult<T> implements Serializable {
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
    /**
     * 响应的具体数据
     */
    @Schema(name = "data", description = "数据")
    private T data;

    public ApiResult(CodeMsg codeMsg) {
        this(codeMsg.getCode(), codeMsg.getMsg());
    }

    public ApiResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ApiResult(CodeMsg codeMsg, T data) {
        this(codeMsg.getCode(), codeMsg.getMsg(), data);
    }

    public ApiResult(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ApiResult<T> success(CodeMsg codeMsg, T data) {
        return new ApiResult<>(codeMsg, data);
    }

    public static <T> ApiResult<T> success(String code, String msg, T data) {
        return new ApiResult<>(code, msg, data);
    }

    public static <T> ApiResult<T> error(String code, String msg) {
        return new ApiResult<>(code, msg);
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "APIResult{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
