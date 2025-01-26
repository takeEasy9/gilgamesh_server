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
 * @description 字典基础信息
 * @createDate 2025/1/26 14:13
 * @since 1.0.0
 */
@Entity
@Table(schema = "gilgamesh", name = "sys_dict_basic")
public class SysDictBasic extends GenericBaseEntity {

    @Serial
    private static final long serialVersionUID = 7675730310727418232L;
    /**
     * 字典中文名称
     */
    @Column(name = "dict_cn_name")
    private String dictCnName;

    /**
     * 字典英文名称, 用作字典类型编码，如sex_dict, 在代码中获取字典ID然后再通过字典ID查询字典数据
     */
    @Column(name = "dict_en_name")
    private String dictEnName;

    /**
     * 字典描述
     */
    @Column(name = "dict_description")
    private String dictDescription;

    /**
     * 逻辑删除状态, 1-未删除, 2-已删除
     */
    @Column(name = "deleted_status")
    private String deletedStatus;

    /**
     * 创建时间, UTC时间
     */
    @Column(name = "created_at")
    private Instant createdAt;

    /**
     * 最后更新者
     */
    @Column(name = "last_updated_by")
    private String lastUpdatedBy;

    /**
     * 最后更新时间, UTC时间
     */
    @Column(name = "last_updated_at", nullable = true)
    private Instant lastUpdatedAt;

    public String getDictCnName() {
        return dictCnName;
    }

    public void setDictCnName(String dictCnName) {
        this.dictCnName = dictCnName;
    }

    public String getDictEnName() {
        return dictEnName;
    }

    public void setDictEnName(String dictEnName) {
        this.dictEnName = dictEnName;
    }

    public String getDictDescription() {
        return dictDescription;
    }

    public void setDictDescription(String dictDescription) {
        this.dictDescription = dictDescription;
    }

    public String getDeletedStatus() {
        return deletedStatus;
    }

    public void setDeletedStatus(String deletedStatus) {
        this.deletedStatus = deletedStatus;
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
        return "SysDictBasic{" +
                "super=" + super.toString() +
                ", dictCnName='" + dictCnName + '\'' +
                ", dictEnName='" + dictEnName + '\'' +
                ", dictDescription='" + dictDescription + '\'' +
                ", deletedStatus='" + deletedStatus + '\'' +
                ", createdAt=" + createdAt +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdatedAt=" + lastUpdatedAt +
                '}';
    }
}