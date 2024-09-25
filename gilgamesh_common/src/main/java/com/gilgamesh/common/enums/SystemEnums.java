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
        }

        @Override
        public String getValue() {
            return this.value;
        }

        @Override
        public String getLabel() {
            return this.label;
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
        }

        @Override
        public String getValue() {
            return this.value;
        }

        @Override
        public String getLabel() {
            return this.label;
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
        }

        @Override
        public String getValue() {
            return this.value;
        }

        @Override
        public String getLabel() {
            return this.label;
        }
    }
}
