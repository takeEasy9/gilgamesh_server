package com.gilgamesh.common.enums;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 消息编码与消息内容接口
 * @createDate 2024/5/2 15:38
 * @since 1.0.0
 */
public interface CodeMsg extends Code {

    /**
     * 获取消息内容
     *
     * @return String
     */
    String getMsg();
}
