package com.gilgamesh.persistence.entity.gilgamesh.sys;


import com.gilgamesh.persistence.entity.base.GenericBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serial;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 用户角色关联关系
 * @createDate 2025/1/26 14:20
 * @since 1.0.0
 */
@Entity
@Table(schema = "gilgamesh", name = "sys_user_role_relation")
public class SysUserRoleRelation extends GenericBaseEntity {
    @Serial
    private static final long serialVersionUID = 7974405534611568798L;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 角色ID
     */
    @Column(name = "role_id")
    private Long roleId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "SysUserRoleRelation{" +
                "super=" + super.toString() +
                ", userId=" + userId +
                ", roleId=" + roleId +
                '}';
    }
}