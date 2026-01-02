package com.gilgamesh.common.enums;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description Gilgamesh 系统枚举类
 * @createDate 2025/7/20 10:39
 * @since 1.0.0
 */
public class GilgameshEnums {

    /**
     * 用户状态枚举, 1-待激活,2-正常,3-锁定,4-密码过期,5-注销
     */
    public enum UserStatus implements EnumValueLabel<String> {
        // 1-待激活
        USER_STATUS_PENDING_ACTIVATION("1", "待激活"),
        // 2-正常
        USER_STATUS_NORMAL("2", "正常"),
        // 3-锁定
        USER_STATUS_LOCKED("3", "锁定"),
        // 4-密码过期
        USER_STATUS_PASSWORD_EXPIRED("4", "密码过期"),
        // 5-注销
        USER_STATUS_DEACTIVATED("5", "注销");

        private final String value;
        private final String label;

        UserStatus(String value, String label) {
            this.value = value;
            this.label = label;
            addEnumValueLabel(value, label);
        }

        @Override
        public UserStatus self() {
            return this;
        }
    }
}
