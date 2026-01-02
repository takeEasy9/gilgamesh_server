package com.gilgamesh.common.utils;

import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.regex.Pattern;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 常量工具类
 * @createDate 2024/5/2 15:52
 * @since 1.0.0
 */
public class ConstantUtil {
    private ConstantUtil() {
    }

    /**
     * ********************************  常用字符  *************************************
     */
    public static final String SPECIAL_CHARACTER_EMPTY = "";
    public static final String SPECIAL_CHARACTER_HYPHEN = "-";
    public static final String SPECIAL_CHARACTER_COMMA = ",";
    public static final String SPECIAL_CHARACTER_SPACE = " ";


    /**
     * ********************************  常用时间格式处理  *************************************
     */
    public static final String DATE_TIME_FORMAT_GENERAL = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_TIME_FORMAT_FULL_NORMAL_WITHOUT_HYPHEN = "yyyyMMdd HH:mm:ss";
    public static final String DATE_FORMAT_GENERAL = "yyyy-MM-dd";
    public static final String TIME_FORMAT_GENERAL = "HH:mm:ss";
    public static final String SPECIAL_CHARACTER_INSTANT_T = "T";
    public static final String SPECIAL_CHARACTER_INSTANT_Z = "Z";

    // 空字符串
    public static final String STRING_EMPTY = "";

    public static final String DEFAULT_API_VERSION = "v1";

    public static final String API_PREFIX = "/api/gilgamesh/";

    public static final String API_VERSION_PLACEHOLDER = "{version}";

    public static final Pattern API_VERSION_PREFIX_PATTERN = Pattern.compile(API_PREFIX + "(v\\d)");

    /**
     * 常用的http请求方法
     */
    public static final String HTTP_METHOD_GET = "GET";
    public static final String HTTP_METHOD_POST = "POST";
    public static final String HTTP_METHOD_PUT = "PUT";
    public static final String HTTP_METHOD_DELETE = "DELETE";
    public static final String HTTP_METHOD_PATCH = "PATCH";

    public static final String TOKEN_HASH_FIELD_RT_JTI = "rtJti";    // refreshToken
    public static final String TOKEN_HASH_FIELD_AT_JTI = "atJti";    // accessToken
    public static final String TOKEN_HASH_FIELD_STATUS = "status";  // refreshToken状态（VALID/INVALID/EXPIRED/REVOKED）
    public static final String TOKEN_HASH_FIELD_ACCESS_TOKEN = "atoken";        // 新jti
    public static final String TOKEN_HASH_FIELD_UID = "uid";
    public static final String TOKEN_HASH_FIELD_RT_AT_ROTATE = "rotate";

    public static final String JWT_REFRESH_TOKEN_KEY = "refresh_token";

    public static final String JWT_TOKEN_REDIS_KEY_FORMAT = "jwt:token:%s";

    public static final String JWT_USER_JTI_REDIS_KEY_FORMAT = "jwt:user:%s_%s:rtJti";

    /**
     * lua 脚本
     */
    // 1. 校验旧Token状态 → 2. 吊销旧Token → 3. 生成新Token并写入Redis
    public static final String REDIS_SCRIPT_RENEW_TOKEN_CONTENT = """
            local tokenKey = KEYS[1]
            local newAToken = ARGV[1]
            -- 1. 校验Token是否有效（仅有效状态可刷新）
            local status = redis.call('HGET', tokenKey, '%s')
            if rtStatus ~= '%s' then
                return 0 -- Token无效（已吊销/过期/无效），返回失败
            end
            -- 2. 原子更新atoken字段 + rtCnt自增（rtCnt不存在则初始化为1）
            redis.call('HSET', tokenKey, '%s', newAToken)
            redis.call('HINCRBY', tokenKey, '%s', 1)
            return 1 -- 执行成功
            """;


    /**
     * 用户JTI列表最大长度
     */
    public static final int USER_JTI_LIST_MAX_SIZE = 100;

    /**
     * 1小时秒数（用于Token哈希过期时间重置）
     */
    public static final int ONE_HOUR_SECONDS = 60 * 60;

    /**
     * Redis Lua脚本：维护用户JTI列表（List结构）+ 智能吊销旧Token
     * 脚本参数说明：
     * KEYS[1] → 用户JTI列表Key（如 jwt:user:10086:ios:valid_jtis）
     * ARGV[1] → 新JTI值（refreshJti）
     * ARGV[2] → 列表过期时间（秒）
     * ARGV[3] → JTI列表最大长度（如100）
     * 执行逻辑：
     * 1. 获取List最后一个元素（上一个JTI）
     * 2. 若存在上一个JTI，将其对应的Token哈希中rtStatus设为4（已吊销）；
     * - 若Token哈希过期时间>12小时/永不过期 → 重置为12小时
     * - 若Token哈希过期时间≤12小时/不存在 → 不处理
     * 3. 若List长度≥最大值，移除最前面的元素（最旧JTI）
     * 4. 将新JTI添加到List尾部
     * 5. 重置List Key的过期时间
     * 6. 返回操作结果（1=成功，0=失败）
     */
    public static final String REDIS_SCRIPT_USER_JTI_LIST_CONTENT =
            """
                    local jtiListKey = KEYS[1]
                    local newJti = ARGV[1]
                    local expireSeconds = tonumber(ARGV[2])
                    local maxListSize = tonumber(ARGV[3])
                    local rtStatusField = "%s"
                    local revokedStatus = ARGV[4]
                    local tokenHashKeyFormat = "%s"
                    -- 1. 获取List最后一个元素（上一个JTI）
                    local lastJti = redis.call('LRANGE', jtiListKey, -1, -1)[1]
                    -- 2. 吊销上一个JTI对应的Token（若存在）
                    if lastJti and lastJti ~= '' then
                        local tokenHashKey = string.format(tokenHashKeyFormat, lastJti)
                        -- 设置吊销状态
                        redis.call('HSET', tokenHashKey, rtStatusField, revokedStatus)
                    end
                    
                    -- 3. 检查List长度，超出则移除最旧元素
                    local currentSize = redis.call('LLEN', jtiListKey)
                    if currentSize >= maxListSize then
                        redis.call('LPOP', jtiListKey)
                    end
                    
                    -- 4. 添加新JTI到List尾部
                    redis.call('RPUSH', jtiListKey, newJti)
                    
                    -- 5. 重置List Key的过期时间
                    redis.call('EXPIRE', jtiListKey, expireSeconds)
                    
                    -- 6. 返回成功标识
                    return 1
                    """;

    public static final RedisScript<Long> REDIS_SCRIPT_USER_JTI_LIST = new DefaultRedisScript<>() {{
        setScriptText(REDIS_SCRIPT_USER_JTI_LIST_CONTENT);
        setResultType(Long.class);
    }};

    /**
     * Lua脚本内容：批量设置Hash字段，并为Key设置过期时间（原子操作）
     * 脚本参数说明：
     * KEYS[1] → Redis Hash Key
     * ARGV[1] → 过期时间（秒）
     * ARGV[2...] → 哈希字段-值对（如 field1, value1, field2, value2...）
     * 执行逻辑：
     * 1. HMSET 批量设置Hash字段
     * 2. EXPIRE 设置Key过期时间
     * 3. 返回 1 表示执行成功（异常时Redis会抛错）
     */
    public static final String REDIS_SCRIPT_SAVE_TOKEN_CONTENT =
            """
                    local hashKey = KEYS[1]
                    local expireSeconds = tonumber(ARGV[1])
                    local hashData = {}
                    for i = 2, #ARGV do
                        table.insert(hashData, ARGV[i])
                    end
                    redis.call('HMSET', hashKey, unpack(hashData))
                    redis.call('EXPIRE', hashKey, expireSeconds)
                    return 1""";

    //
    public static final RedisScript<Long> REDIS_SCRIPT_RENEW_TOKEN;

    /**
     * 预定义 RedisScript 常量（复用，无需每次new）
     * 返回值类型：Long（固定返回1，异常时抛RedisScriptException）
     */
    public static final RedisScript<Long> REDIS_SCRIPT_SAVE_TOKEN;


    // 静态代码块初始化（避免匿名内部类，更优雅）
    static {
        DefaultRedisScript<Long> renewTokenScript = new DefaultRedisScript<>();
        renewTokenScript.setScriptText(REDIS_SCRIPT_RENEW_TOKEN_CONTENT);
        renewTokenScript.setResultType(Long.class);
        REDIS_SCRIPT_RENEW_TOKEN = renewTokenScript;

        DefaultRedisScript<Long> tokenHmSetScript = new DefaultRedisScript<>();
        tokenHmSetScript.setScriptText(REDIS_SCRIPT_SAVE_TOKEN_CONTENT);
        tokenHmSetScript.setResultType(Long.class); // 必须指定返回值类型，否则返回null
        REDIS_SCRIPT_SAVE_TOKEN = tokenHmSetScript;
    }

    /**
     * Redis Lua脚本Key
     */
    public static final String REDIS_SCRIPT_KEY_SAVE_TOKEN = "saveToken";
    public static final String REDIS_SCRIPT_KEY_USER_JTI_LIST = "userJtiList";
    public static final String REDIS_SCRIPT_KEY_RENEW_TOKEN = "renewToken";


    public static final String HTTP_HEADER_CLIENT_TYPE = "X-Client-Type";

    public static final String HTTP_HEADER_TOKEN_TYPE_BEARER = "Bearer";


}
