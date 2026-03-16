package com.gilgamesh.common.enums;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 系统级别枚举值
 * @createDate 2024/5/2 15:57
 * @since 1.0.0
 */
public class SystemEnums {

    /**
     * api接口返回格式定义
     */
    public enum ApiRes implements EnumValueLabel<String> {
        // api消息编码
        CODE("code", "api消息编码"),
        // api消息编码含义
        MESSAGE("msg", "api消息编码含义"),
        // api结果
        DATA("data", "api结果");

        private final String value;
        private final String label;

        ApiRes(String value, String label) {
            this.value = value;
            this.label = label;
            addEnumValueLabel(value, label);
        }

        @Override
        public ApiRes self() {
            return this;
        }
    }

    /**
     * 时区 id 枚举
     */
    public enum ZoneIdType implements EnumValueLabel<String> {
        // UTC
        ZONE_ID_TYPE_UTC("UTC", "UTC"),
        ZONE_ID_TYPE_ACT("ACT", "Australia/Darwin"),
        ZONE_ID_TYPE_AET("AET", "Australia/Sydney"),
        ZONE_ID_TYPE_AGT("AGT", "America/Argentina/Buenos_Aires"),
        ZONE_ID_TYPE_ART("ART", "Africa/Cairo"),
        ZONE_ID_TYPE_AST("AST", "America/Anchorage"),
        ZONE_ID_TYPE_BET("BET", "America/Sao_Paulo"),
        ZONE_ID_TYPE_BST("BST", "Asia/Dhaka"),
        ZONE_ID_TYPE_CAT("CAT", "Africa/Harare"),
        ZONE_ID_TYPE_CNT("CNT", "America/St_Johns"),
        ZONE_ID_TYPE_CST("CST", "America/Chicago"),
        ZONE_ID_TYPE_CTT("CTT", "Asia/Shanghai"),
        ZONE_ID_TYPE_EAT("EAT", "Africa/Addis_Ababa"),
        ZONE_ID_TYPE_ECT("ECT", "Europe/Paris"),
        ZONE_ID_TYPE_IET("IET", "America/Indiana/Indianapolis"),
        ZONE_ID_TYPE_IST("IST", "Asia/Kolkata"),
        ZONE_ID_TYPE_JST("JST", "Asia/Tokyo"),
        ZONE_ID_TYPE_MIT("MIT", "Pacific/Apia"),
        ZONE_ID_TYPE_NET("NET", "Asia/Yerevan"),
        ZONE_ID_TYPE_NST("NST", "Pacific/Auckland"),
        ZONE_ID_TYPE_PLT("PLT", "Asia/Karachi"),
        ZONE_ID_TYPE_PNT("PNT", "America/Phoenix"),
        ZONE_ID_TYPE_PRT("PRT", "America/Puerto_Rico"),
        ZONE_ID_TYPE_PST("PST", "America/Los_Angeles"),
        ZONE_ID_TYPE_SST("SST", "Pacific/Guadalcanal"),
        ZONE_ID_TYPE_VST("VST", "Asia/Ho_Chi_Minh"),
        ZONE_ID_TYPE_EST("EST", "-05:00"),
        ZONE_ID_TYPE_MST("MST", "-07:00"),
        ZONE_ID_TYPE_HST("HST", "-10:00");

        private final String value;
        private final String label;

        ZoneIdType(String value, String label) {
            this.value = value;
            this.label = label;
            addEnumValueLabel(value, label);
        }

        @Override
        public ZoneIdType self() {
            return this;
        }
    }

    /**
     * 环境
     */
    public enum Profile implements EnumValueLabel<String> {
        // dev-开发环境
        DEV("dev", "开发环境"),
        // test-测试环境
        TEST("test", "测试环境"),
        // product-生产环境
        PRODUCT("prod", "生产环境");
        private final String value;
        private final String label;

        Profile(String value, String label) {
            this.value = value;
            this.label = label;
            addEnumValueLabel(value, label);
        }

        @Override
        public Profile self() {
            return this;
        }
    }

    /**
     * 逻辑删除状态
     */
    public enum DeletedStatus implements EnumValueLabel<String> {
        // 未删除
        NOT_DELETED("1", "未删除"),
        // 已删除
        DELETED("2", "已删除");

        private final String value;
        private final String label;

        DeletedStatus(String value, String label) {
            this.value = value;
            this.label = label;
            addEnumValueLabel(value, label);
        }

        @Override
        public DeletedStatus self() {
            return this;
        }
    }

    /**
     * JWT claim keys
     */
    public enum JWTClaimKey implements EnumValueLabel<String> {
        // 用户ID
        USER_ID("userId", "用户ID"),
        // 用户名
        USER_NAME("userName", "用户名"),
        // 用户角色
        USER_ROLES("userRoles", "用户角色"),
        // token 业务用途, 用于区分 access token 与 refresh token
        TOKEN_BIZ_TYPE("bizType", "token 业务用途"),

        TOKEN_CLIENT_TYPE("cliType", "客户端类型"),

        TOKEN_RT_JTI("rtJti", "refresh token jti"),
        ;

        private final String value;
        private final String label;

        JWTClaimKey(String value, String label) {
            this.value = value;
            this.label = label;
            addEnumValueLabel(value, label);
        }

        @Override
        public JWTClaimKey self() {
            return this;
        }
    }

    /**
     * 客户端类型
     */
    public enum ClientType implements EnumValueLabel<String> {
        // web 端
        CLIENT_TYPE_WEB("1", "web端"),
        // 小程序
        CLIENT_TYPE_MINI_APP("2", "小程序"),
        // ios
        CLIENT_TYPE_IOS("3", "IOS"),
        // Android
        CLIENT_TYPE_ANDROID("4", "Android"),
        // 桌面
        CLIENT_TYPE_DESKTOP("5", "桌面"),
        // 桌面
        CLIENT_TYPE_UNKNOWN("6", "未知"),
        ;

        private final String value;
        private final String label;

        ClientType(String value, String label) {
            this.value = value;
            this.label = label;
            addEnumValueLabel(value, label);
        }

        @Override
        public ClientType self() {
            return this;
        }
    }

    /**
     * token 业务用途
     */
    public enum TokenBizType implements EnumValueLabel<String> {
        // access token
        TOKEN_BIZ_TYPE_ACCESS("1", "访问Token"),
        // refresh token
        TOKEN_BIZ_TYPE_REFRESH("2", "刷新Token"),
        ;

        private final String value;
        private final String label;

        TokenBizType(String value, String label) {
            this.value = value;
            this.label = label;
            addEnumValueLabel(value, label);
        }

        @Override
        public TokenBizType self() {
            return this;
        }
    }

    /**
     * Token 传输类型枚举（对应 HTTP Authorization 头前缀，区分 Bearer/ApiKey 等）
     */
    public enum TokenTransportType implements EnumValueLabel<String> {
        /**
         * Bearer 传输（最常用，JWT/ OAuth2.0 标准）
         * 格式：Authorization: Bearer <token>
         */
        BEARER("Bearer", "Bearer 传输（JWT/OAuth2.0 标准）"),

        /**
         * ApiKey 传输（第三方对接/服务间调用）
         * 格式：Authorization: ApiKey <api-key>
         */
        API_KEY("ApiKey", "ApiKey 传输（第三方接口/服务间调用）"),

        /**
         * Basic 传输（基础认证，低安全）
         * 格式：Authorization: Basic <base64-username:password>
         */
        BASIC("Basic", "Basic 传输（基础认证，适用于内部系统）"),

        /**
         * HMAC 传输（高安全签名传输）
         * 格式：Authorization: HMAC <signature>
         */
        HMAC("HMAC", "HMAC 签名传输（金融/支付等高安全场景）");

        private final String value;
        private final String label;

        TokenTransportType(String value, String label) {
            this.value = value;
            this.label = label;
            addEnumValueLabel(value, label);
        }

        @Override
        public TokenTransportType self() {
            return this;
        }
    }

    /**
     * 用户密码加密算法
     */
    public enum EncryptAlgorithm implements EnumValueLabel<String> {
        //
        ENCRYPT_ALGORITHM_BCRYPT("bcrypt", "BCrypt 加密"),
        ENCRYPT_ALGORITHM_PBKDF2_V5_8("pbkdf2@SpringSecurity_v5_8", "pbkdf2 加密"),
        ENCRYPT_ALGORITHM_SCRYPT_V5_8("scrypt@SpringSecurity_v5_8", "scrypt 加密"),
        ENCRYPT_ALGORITHM_ARGON2_V5_8("argon2@SpringSecurity_v5_8", "argon2 加密"),
        ;

        private final String value;
        private final String label;

        EncryptAlgorithm(String value, String label) {
            this.value = value;
            this.label = label;
            addEnumValueLabel(value, label);
        }

        @Override
        public EncryptAlgorithm self() {
            return this;
        }
    }

    /**
     * JwtToken 状态
     */
    public enum JwtTokenStatus implements EnumValueLabel<String> {
        // 1-jwtToken 正常,可以使用
        JWT_TOKEN_STATUS_VALID("1", "有效"),
        // 2-jwtToken 已过期
        JWT_TOKEN_STATUS_EXPIRED("2", "已过期"),
        // 3-jwtToken 无效,如空、篡改等场景
        JWT_TOKEN_STATUS_INVALID("3", "无效"),
        // 4-jwtToken 已吊销,如 用户主动登出、密码修改
        JWT_TOKEN_STATUS_REVOKED("4", "已吊销"),
        // 5-jwtToken 续期
        JWT_TOKEN_STATUS_RENEW("5", "已续期"),
        ;

        final String value;
        final String label;

        JwtTokenStatus(String value, String label) {
            this.value = value;
            this.label = label;
            addEnumValueLabel(value, label);
        }

        @Override
        public JwtTokenStatus self() {
            return this;
        }
    }

}
