package com.gilgamesh.common.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 枚举名称的枚举接口
 * @createDate 2024/5/2 15:35
 * @since 1.0.0
 */
public interface EnumValueLabel<T> extends EnumValue<T> {

    default void addEnumValueLabel(T value, String label) {
        EnumValueLabel<T> self = self();
        self.addEnumValue(value);
        EnumValueLabelState.enumLabelMap.putIfAbsent(self, label);
        Map<Object, EnumValueLabel<?>> valueLabelEnums = EnumValueLabelState.value2EnumMap.computeIfAbsent(self.getClass(),
                key -> new HashMap<>());
        valueLabelEnums.put(value, self);
    }

    /**
     * 获取枚举label
     *
     * @return 枚举label
     */
    default String getLabel() {
        return EnumValueLabelState.enumLabelMap.get(self());
    }

    @Override
    default EnumValueLabel<T> self() {
        return this;
    }
}

class EnumValueLabelState {
    static final ConcurrentHashMap<EnumValue<?>, String> enumLabelMap = new ConcurrentHashMap<>();
    static final ConcurrentHashMap<Class<?>, Map<Object, EnumValueLabel<?>>> value2EnumMap = new ConcurrentHashMap<>();

    private EnumValueLabelState() {
    }
}
