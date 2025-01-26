package com.gilgamesh.persistence.entity.gilgamesh.sys;

import com.gilgamesh.persistence.entity.base.GenericBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serial;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 部门信息
 * @createDate 2025/1/26 14:12
 * @since 1.0.0
 */
@Entity
@Table(schema = "gilgamesh", name = "sys_dept")
public class SysDept extends GenericBaseEntity {
    @Serial
    private static final long serialVersionUID = -7973539614940317930L;

    /**
     * 部门中文名称
     */
    @Column(name = "dept_cn_name", unique = true)
    private String deptCnName;

    /**
     * 部门英文名称
     */
    @Column(name = "dept_en_name", unique = true)
    private String deptEnName;

    /**
     * 父级部门ID
     */
    @Column(name = "parent_dept_id", nullable = true)
    private Long parentDeptId;

    /**
     * 部门描述
     */
    @Column(name = "dept_description", nullable = true)
    private String deptDescription;

    public String getDeptCnName() {
        return deptCnName;
    }

    public void setDeptCnName(String deptCnName) {
        this.deptCnName = deptCnName;
    }

    public String getDeptEnName() {
        return deptEnName;
    }

    public void setDeptEnName(String deptEnName) {
        this.deptEnName = deptEnName;
    }

    public Long getParentDeptId() {
        return parentDeptId;
    }

    public void setParentDeptId(Long parentDeptId) {
        this.parentDeptId = parentDeptId;
    }

    public String getDeptDescription() {
        return deptDescription;
    }

    public void setDeptDescription(String deptDescription) {
        this.deptDescription = deptDescription;
    }

    @Override
    public String toString() {
        return "SysDept{" +
                "super=" + super.toString() +
                ", deptCnName='" + deptCnName + '\'' +
                ", deptEnName='" + deptEnName + '\'' +
                ", parentDeptId=" + parentDeptId +
                ", deptDescription='" + deptDescription + '\'' +
                '}';
    }
}