package com.gilgamesh.persistence.entity.gilgamesh.sys;

import com.gilgamesh.persistence.entity.base.GenericBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serial;
import java.time.Instant;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 菜单信息
 * @createDate 2025/1/26 14:15
 * @since 1.0.0
 */
@Entity
@Table(schema = "gilgamesh", name = "sys_menu")
public class SysMenu extends GenericBaseEntity {

    @Serial
    private static final long serialVersionUID = -5324233528648933388L;
    /**
     * 菜单中文名称
     */
    @Column(name = "menu_cn_name", unique = true)
    private String menuCnName;

    /**
     * 菜单英文名称
     */
    @Column(name = "menu_en_name", unique = true)
    private String menuEnName;

    /**
     * 菜单导航路径
     */
    @Column(name = "menu_path", unique = true)
    private String menuPath;

    /**
     * 菜单图标
     */
    @Column(name = "menu_icon")
    private String menuIcon;

    /**
     * 父级菜单ID
     */
    @Column(name = "parent_menu_id", nullable = true)
    private Long parentMenuId;

    /**
     * 菜单显示顺序
     */
    @Column(name = "menu_order")
    private Integer menuOrder;

    /**
     * 菜单描述
     */
    @Column(name = "menu_description")
    private String menuDescription;

    /**
     * 逻辑删除状态, 1-未删除, 2-已删除
     */
    @Column(name = "deleted_status")
    private String deletedStatus;

    /**
     * 版本号, 用于控制并发
     */
    @Column(name = "version_id")
    private Long versionId;

    /**
     * 创建时间, UTC时间
     */
    @Column(name = "created_at")
    private Instant createdAt;

    /**
     * 最后更新者
     */
    @Column(name = "last_updated_by", nullable = true)
    private String lastUpdatedBy;

    /**
     * 最后更新时间, UTC时间
     */
    @Column(name = "last_updated_at", nullable = true)
    private Instant lastUpdatedAt;

    public String getMenuCnName() {
        return menuCnName;
    }

    public void setMenuCnName(String menuCnName) {
        this.menuCnName = menuCnName;
    }

    public String getMenuEnName() {
        return menuEnName;
    }

    public void setMenuEnName(String menuEnName) {
        this.menuEnName = menuEnName;
    }

    public String getMenuPath() {
        return menuPath;
    }

    public void setMenuPath(String menuPath) {
        this.menuPath = menuPath;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public Long getParentMenuId() {
        return parentMenuId;
    }

    public void setParentMenuId(Long parentMenuId) {
        this.parentMenuId = parentMenuId;
    }

    public Integer getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(Integer menuOrder) {
        this.menuOrder = menuOrder;
    }

    public String getMenuDescription() {
        return menuDescription;
    }

    public void setMenuDescription(String menuDescription) {
        this.menuDescription = menuDescription;
    }

    public String getDeletedStatus() {
        return deletedStatus;
    }

    public void setDeletedStatus(String deletedStatus) {
        this.deletedStatus = deletedStatus;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Instant getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public void setLastUpdatedAt(Instant lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    @Override
    public String toString() {
        return "SysMenu{" +
                "super=" + super.toString() +
                ", menuCnName='" + menuCnName + '\'' +
                ", menuEnName='" + menuEnName + '\'' +
                ", menuPath='" + menuPath + '\'' +
                ", menuIcon='" + menuIcon + '\'' +
                ", parentMenuId=" + parentMenuId +
                ", menuOrder=" + menuOrder +
                ", menuDescription='" + menuDescription + '\'' +
                ", deletedStatus='" + deletedStatus + '\'' +
                ", versionId=" + versionId +
                ", createdAt=" + createdAt +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdatedAt=" + lastUpdatedAt +
                '}';
    }
}