package com.gilgamesh.persistence.entity.gilgamesh.sys;


import com.gilgamesh.persistence.entity.base.GenericBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serial;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 角色菜单关联关系
 * @createDate 2025/1/26 14:19
 * @since 1.0.0
 */
@Entity
@Table(schema = "gilgamesh", name = "sys_role_menu_relation")
public class SysRoleMenuRelation extends GenericBaseEntity {
    @Serial
    private static final long serialVersionUID = -4789548250854056099L;
    /**
     * 角色ID
     */
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 菜单ID
     */
    @Column(name = "menu_id")
    private Long menuId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    @Override
    public String toString() {
        return "SysRoleMenuRelation{" +
                "super=" + super.toString() +
                ", roleId=" + roleId +
                ", menuId=" + menuId +
                '}';
    }

}