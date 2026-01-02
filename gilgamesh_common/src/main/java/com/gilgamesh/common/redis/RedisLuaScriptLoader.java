package com.gilgamesh.common.redis;

import com.gilgamesh.common.enums.SystemEnums;
import com.gilgamesh.common.utils.ConstantUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveScriptingCommands;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description RedisLuaScriptLoader
 * @createDate 2025/12/28 15:56
 * @since 1.0.0
 */
@Component
public class RedisLuaScriptLoader implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(RedisLuaScriptLoader.class);

    private final ReactiveStringRedisTemplate reactiveRedisTemplate;
    /**
     * 脚本缓存（SHA1 → 脚本标识）
     */
    private final Map<String, String> scriptSha1Cache = new ConcurrentHashMap<>();

    @Autowired
    public RedisLuaScriptLoader(ReactiveStringRedisTemplate reactiveRedisTemplate) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    @Override
    public void afterPropertiesSet() {
        Map<String, String> scriptMap = new HashMap<>();
        scriptMap.put(ConstantUtil.REDIS_SCRIPT_KEY_SAVE_TOKEN, ConstantUtil.REDIS_SCRIPT_SAVE_TOKEN_CONTENT);
        String userJtiListScript = ConstantUtil.REDIS_SCRIPT_USER_JTI_LIST_CONTENT
                .formatted(ConstantUtil.TOKEN_HASH_FIELD_STATUS,
                        ConstantUtil.JWT_TOKEN_REDIS_KEY_FORMAT);
        scriptMap.put(ConstantUtil.REDIS_SCRIPT_KEY_USER_JTI_LIST, userJtiListScript);
        String renewTokenScript = ConstantUtil.REDIS_SCRIPT_RENEW_TOKEN_CONTENT
                .formatted(ConstantUtil.TOKEN_HASH_FIELD_STATUS,
                        SystemEnums.JwtTokenStatus.JWT_TOKEN_STATUS_VALID.getValue(),
                        ConstantUtil.JWT_TOKEN_REDIS_KEY_FORMAT,
                        ConstantUtil.TOKEN_HASH_FIELD_RT_AT_ROTATE);
        scriptMap.put(ConstantUtil.REDIS_SCRIPT_KEY_RENEW_TOKEN, renewTokenScript);

        // 批量异步加载所有脚本
        Flux.fromIterable(scriptMap.entrySet())
                .flatMap(entry -> loadScriptReactive(entry.getKey(), entry.getValue()))
                .doOnComplete(() -> log.info("所有Lua脚本异步加载完成，共加载 {} 个脚本", scriptSha1Cache.size()))
                .doOnError(e -> log.error("Lua脚本加载失败", e))
                // 等待加载完成（启动阶段阻塞，确保脚本加载完毕）
                .blockLast(Duration.ofSeconds(5));
    }

    /**
     * 修复核心：通过 ReactiveScriptingCommands 调用 scriptLoad
     */
    private Flux<Void> loadScriptReactive(String scriptKey, String scriptContent) {
        // 1. 脚本内容 String → byte[] → ByteBuffer（满足方法入参要求）
        byte[] scriptBytes = scriptContent.getBytes(StandardCharsets.UTF_8);
        ByteBuffer scriptBuffer = ByteBuffer.wrap(scriptBytes);

        return reactiveRedisTemplate.execute(connection -> {
            // 2. 获取脚本命令操作对象
            ReactiveScriptingCommands scriptingCommands = connection.scriptingCommands();

            // 3. 执行 scriptLoad（入参 ByteBuffer，返回值 Mono<ByteBuffer>）
            return scriptingCommands.scriptLoad(scriptBuffer)
                    // 5. 缓存 SHA1 字符串
                    .doOnNext(sha1 -> {
                        scriptSha1Cache.put(scriptKey, sha1);
                        log.info("加载脚本[{}]成功，SHA1={}", scriptKey, sha1);
                    })
                    // 6. 异常处理 + 转为无返回值 Mono<Void>
                    .doOnError(e -> log.error("加载脚本[{}]失败", scriptKey, e))
                    .then();
        }).onErrorMap(e -> new RuntimeException("加载脚本[" + scriptKey + "]失败", e));
    }

    /**
     * 获取脚本 SHA1 摘要（线程安全）
     */
    public String getScriptSha1(String scriptKey) {
        String sha1 = scriptSha1Cache.get(scriptKey);
        if (sha1 == null) {
            throw new IllegalArgumentException("脚本未加载：" + scriptKey + "，请检查脚本内容或Redis连接");
        }
        return sha1;
    }

    /**
     * 根据标识获取脚本内容
     */
    public String getScriptContentByKey(String scriptKey) {
        return switch (scriptKey) {
            case ConstantUtil.REDIS_SCRIPT_KEY_SAVE_TOKEN -> ConstantUtil.REDIS_SCRIPT_SAVE_TOKEN_CONTENT;
            case ConstantUtil.REDIS_SCRIPT_KEY_USER_JTI_LIST -> ConstantUtil.REDIS_SCRIPT_USER_JTI_LIST_CONTENT;
            case ConstantUtil.REDIS_SCRIPT_KEY_RENEW_TOKEN -> ConstantUtil.REDIS_SCRIPT_RENEW_TOKEN_CONTENT;
            default -> throw new IllegalArgumentException("未知脚本标识：" + scriptKey);
        };
    }

}
