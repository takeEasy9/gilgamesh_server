package com.gilgamesh.common.enums;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description id 固定前缀
 * @createDate 2025/12/6 10:28
 * @since 1.0.0
 */
public enum IdPrefixEnum implements EnumValueLabel<String> {
    // 用户ID前缀
    USER_ID_PREFIX("uid_", "用户ID前缀"),
    ;
    final String value;
    final String label;

    IdPrefixEnum(String value, String label) {
        this.value = value;
        this.label = label;
        addEnumValueLabel(value, label);
    }

    @Override
    public IdPrefixEnum self() {
        return this;
    }
}
