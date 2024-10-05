package com.gilgamesh.common.entity.base;

import com.gilgamesh.common.enums.CodeMsg;

import java.io.Serial;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 接口统一返回结构
 * @createDate 2024/10/2 22:02
 * @since 1.0.0
 */
public class ApiResult<T> extends BaseResult {
    @Serial
    private static final long serialVersionUID = -5044426311088001904L;
    /**
     * 响应的具体数据
     */
    private T data;

    public ApiResult(CodeMsg codeMsg, T data) {
        super(codeMsg);
        this.data = data;
    }

    public ApiResult(String code, String msg, T data) {
        super(code, msg);
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "super=" + super.toString() +
                "data=" + data +
                '}';
    }
}
