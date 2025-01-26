package com.gilgamesh.persistence.entity.gilgamesh.sys;


import com.gilgamesh.persistence.entity.base.VersionControlGenericBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serial;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 角色信息
 * @createDate 2025/1/26 14:18
 * @since 1.0.0
 */
@Entity
@Table(schema = "gilgamesh", name = "sys_role")
public class SysRole extends VersionControlGenericBaseEntity {

    @Serial
    private static final long serialVersionUID = -2112493164890900557L;
    /**
     * 角色中文名称
     */
    @Column(name = "role_cn_name")
    private String roleCnName;

    /**
     * 角色英文名称
     */
    @Column(name = "role_en_name")
    private String roleEnName;

    /**
     * 角色描述
     */
    @Column(name = "role_description")
    private String roleDescription;

    public String getRoleCnName() {
        return roleCnName;
    }

    public void setRoleCnName(String roleCnName) {
        this.roleCnName = roleCnName;
    }

    public String getRoleEnName() {
        return roleEnName;
    }

    public void setRoleEnName(String roleEnName) {
        this.roleEnName = roleEnName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    @Override
    public String toString() {
        return "SysRole{" +
                "super=" + super.toString() +
                ", roleCnName='" + roleCnName + '\'' +
                ", roleEnName='" + roleEnName + '\'' +
                ", roleDescription='" + roleDescription + '\'' +
                '}';
    }
}