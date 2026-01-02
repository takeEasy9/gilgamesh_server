package com.gilgamesh.persistence.entity.gilgamesh.sys;

import com.gilgamesh.persistence.entity.base.GenericBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serial;


/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 角色与接口权限关联关系
 * @createDate 2025/2/16 16:57
 * @since 1.0.0
 */
@Entity
@Table(schema = "gilgamesh", name = "sys_role_api_auth_relation")
public class SysRoleApiAuthRelation extends GenericBaseEntity {

    @Serial
    private static final long serialVersionUID = -8490069069640561293L;
    /**
     * 角色ID
     */
    @Column(name = "role_id")
    private Long roleId;

    /**
     * 接口权限ID
     */
    @Column(name = "api_auth_id")
    private Long apiAuthId;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getApiAuthId() {
        return apiAuthId;
    }

    public void setApiAuthId(Long apiAuthId) {
        this.apiAuthId = apiAuthId;
    }

    @Override
    public String toString() {
        return "SysRoleApiAuthRelation{" +
                "super=" + super.toString() +
                ", roleId=" + roleId +
                ", apiAuthId=" + apiAuthId +
                '}';
    }
}