package com.gilgamesh.common.utils;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.json.JsonParseException;

import java.util.concurrent.Callable;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description JSON 工具类
 * @createDate 2024/10/5 10:37
 * @since 1.0.0
 */
public class JSONUtil {
    private JSONUtil() {
    }

    // 静态代码块单例
    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    /**
     * 捕获 ObjectMapper api 可能抛出的异常
     * 调用示例: User user = JacksonUtil.tryParse(() -> JacksonUtil.getObjectMapper().readValue("{json}", User.class));
     *
     * @param parser Callable<T> JSON解析器
     * @return T
     */
    public static <T> T tryParse(Callable<T> parser) {
        return tryParse(parser, JacksonException.class);
    }

    /**
     * 捕获 ObjectMapper api 可能抛出的异常
     * List<User> users = JacksonUtil.tryParse(() -> JacksonUtil.getObjectMapper().readValue("{json}", new TypeReference<List<User>>() {}));
     *
     * @param parser JSON解析器
     * @param check  Class<? extends Exception> 指定Json解析抛出的异常
     * @return T
     */
    public static <T> T tryParse(Callable<T> parser, Class<? extends Exception> check) {
        try {
            return parser.call();
        } catch (Exception e) {
            if (check.isAssignableFrom(e.getClass())) {
                throw new JsonParseException(e);
            }
            throw new IllegalStateException(e);
        }
    }

}
