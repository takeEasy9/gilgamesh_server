package com.gilgamesh.common.utils;

import com.gilgamesh.common.enums.CodeMsg;
import com.gilgamesh.common.enums.SystemEnums;

import java.util.HashMap;
import java.util.Map;

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
    public static Map<String, Object> success(CodeMsg codeMsg, Object data) {
        Map<String, Object> resultMap = new HashMap<>(4);
        resultMap.put(SystemEnums.ApiRes.CODE.getValue(), codeMsg.getCode());
        resultMap.put(SystemEnums.ApiRes.MESSAGE.getValue(), codeMsg.getMsg());
        resultMap.put(SystemEnums.ApiRes.DATA.getValue(), data);
        return resultMap;
    }

    /**
     * 接口异常返回结果封装方法
     *
     * @param codeMsg api消息编码与消息枚举值
     * @return Mono<Map < String, Object>>
     */
    public static Map<String, Object> failed(CodeMsg codeMsg) {
        return failed(codeMsg.getCode(), codeMsg.getMsg());
    }

    /**
     * 接口异常返回结果封装方法
     *
     * @param code 消息编码
     * @param msg  消息
     * @return Mono<Map < String, Object>>
     */
    public static Map<String, Object> failed(String code, String msg) {
        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put(SystemEnums.ApiRes.CODE.getValue(), code);
        resultMap.put(SystemEnums.ApiRes.MESSAGE.getValue(), msg);
        return resultMap;
    }
}
