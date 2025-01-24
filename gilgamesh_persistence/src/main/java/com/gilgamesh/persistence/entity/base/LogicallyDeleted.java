package com.gilgamesh.persistence.entity.base;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 逻辑删除标记接口
 * @createDate 2025/1/24 14:36
 * @since 1.0.0
 */
public interface LogicallyDeleted {
    /**
     * 是否已删除
     *
     * @return true-已删除, false-未删除
     */
    boolean isDeleted();
}
