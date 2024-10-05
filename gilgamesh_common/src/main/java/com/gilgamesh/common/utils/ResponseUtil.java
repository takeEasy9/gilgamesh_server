package com.gilgamesh.common.utils;

import com.gilgamesh.common.entity.base.ApiResult;
import com.gilgamesh.common.entity.base.BaseResult;
import com.gilgamesh.common.enums.CodeMsg;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 响应工具类
 * @createDate 2024/5/2 15:52
 * @since 1.0.0
 */
public class ResponseUtil {
    private ResponseUtil() {

    }

    /**
     * 接口正常返回结果封装方法
     *
     * @param codeMsg codeMsg api消息编码与消息枚举值
     * @param data    Object 接口待返回数据
     * @return Mono<Map < String, Object>>
     */
    public static <T> ApiResult<T> success(CodeMsg codeMsg, T data) {
        return new ApiResult<>(codeMsg, data);
    }

    /**
     * 接口异常返回结果封装方法
     *
     * @param codeMsg api消息编码与消息枚举值
     * @return Mono<Map < String, Object>>
     */
    public static BaseResult failed(CodeMsg codeMsg) {
        return failed(codeMsg.getCode(), codeMsg.getMsg());
    }

    /**
     * 接口异常返回结果封装方法
     *
     * @param code 消息编码
     * @param msg  消息
     * @return Mono<Map < String, Object>>
     */
    public static BaseResult failed(String code, String msg) {
        return new BaseResult(code, msg);
    }
}
