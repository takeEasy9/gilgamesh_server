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
        /**
         * 待激活：新注册用户未激活
         * 异常：UserAccountNotActivatedException
         */
        USER_STATUS_PENDING_ACTIVATION("1", "待激活"),
        /**
         * 正常：正常使用的账户
         * 异常：无
         */
        USER_STATUS_NORMAL("2", "正常"),
        /**
         * 锁定（临时）：登录失败次数超限临时锁定
         * 异常：LockedException（1小时后重试）
         */
        USER_STATUS_TEMP_LOCKED("3", "锁定(临时)"),
        /**
         * 锁定（永久）：管理员手动锁定
         * 异常：LockedException（联系客服）
         */
        USER_STATUS_PERM_LOCKED("4", "锁定(永久)"),
        /**
         * 密码过期：密码超有效期需重置
         * 异常：PasswordExpiredException（密码重置）
         */
        USER_STATUS_PASSWORD_EXPIRED("5", "密码过期"),
        /**
         * 待审核：企业用户注册后待审核
         * 异常：UserAccountPendingApprovalException
         */
        USER_STATUS_PENDING_APPROVAL("6", "待审核"),
        /**
         * 账户过期：会员/临时账户有效期到期
         * 异常：AccountExpiredException
         */
        USER_STATUS_ACCOUNT_EXPIRED("7", "账户过期"),
        /**
         * 冻结（长期未用）：长期未登录自动冻结
         * 异常：UserAccountFrozenException
         */
        USER_STATUS_FROZEN("8", "冻结(长期未用)"),
        /**
         * 注销：用户主动注销/管理员注销
         * 异常：DisabledException
         */
        USER_STATUS_DEACTIVATED("9", "注销");

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
