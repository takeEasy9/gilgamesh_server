package com.gilgamesh.persistence.entity.gilgamesh.sys;

import com.gilgamesh.persistence.entity.base.VersionControlGenericBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serial;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 系统约束
 * @createDate 2025/1/26 14:10
 * @since 1.0.0
 */
@Entity
@Table(schema = "gilgamesh", name = "sys_constraint")
public class SysConstraint extends VersionControlGenericBaseEntity {
    @Serial
    private static final long serialVersionUID = -6941436567484810790L;

    /**
     * 约束编码
     */
    @Column(name = "constraint_code", unique = true)
    private String constraintCode;

    /**
     * 约束中文名称
     */
    @Column(name = "constraint_cn_name", unique = true)
    private String constraintCnName;

    /**
     * 约束英文文名称
     */
    @Column(name = "constraint_en_name", unique = true)
    private String constraintEnName;

    /**
     * 约束排序
     */
    @Column(name = "constraint_order")
    private Integer constraintOrder;

    /**
     * 约束描述
     */
    @Column(name = "constraint_description")
    private String constraintDescription;

    /**
     * 约束父级编码
     */
    @Column(name = "constraint_parent_code", nullable = true)
    private String constraintParentCode;

    /**
     * 约束类型, 1-NULL, 2-字符串, 3-整数, 4-浮点数, 5-集合(集合)
     */
    @Column(name = "constraint_type")
    private String constraintType;

    public String getConstraintCode() {
        return constraintCode;
    }

    public void setConstraintCode(String constraintCode) {
        this.constraintCode = constraintCode;
    }

    public String getConstraintCnName() {
        return constraintCnName;
    }

    public void setConstraintCnName(String constraintCnName) {
        this.constraintCnName = constraintCnName;
    }

    public String getConstraintEnName() {
        return constraintEnName;
    }

    public void setConstraintEnName(String constraintEnName) {
        this.constraintEnName = constraintEnName;
    }

    public Integer getConstraintOrder() {
        return constraintOrder;
    }

    public void setConstraintOrder(Integer constraintOrder) {
        this.constraintOrder = constraintOrder;
    }

    public String getConstraintDescription() {
        return constraintDescription;
    }

    public void setConstraintDescription(String constraintDescription) {
        this.constraintDescription = constraintDescription;
    }

    public String getConstraintParentCode() {
        return constraintParentCode;
    }

    public void setConstraintParentCode(String constraintParentCode) {
        this.constraintParentCode = constraintParentCode;
    }

    public String getConstraintType() {
        return constraintType;
    }

    public void setConstraintType(String constraintType) {
        this.constraintType = constraintType;
    }

    @Override
    public String toString() {
        return "SysConstraint{" +
                "super=" + super.toString() +
                ", constraintCode='" + constraintCode + '\'' +
                ", constraintCnName='" + constraintCnName + '\'' +
                ", constraintEnName='" + constraintEnName + '\'' +
                ", constraintOrder=" + constraintOrder +
                ", constraintDescription='" + constraintDescription + '\'' +
                ", constraintParentCode='" + constraintParentCode + '\'' +
                ", constraintType='" + constraintType + '\'' +
                '}';
    }
}