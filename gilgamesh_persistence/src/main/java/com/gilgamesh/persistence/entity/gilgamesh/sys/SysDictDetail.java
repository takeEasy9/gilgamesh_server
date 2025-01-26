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
 * @description 字典详细信息
 * @createDate 2025/1/26 14:14
 * @since 1.0.0
 */
@Entity
@Table(schema = "gilgamesh", name = "sys_dict_detail")
public class SysDictDetail extends GenericBaseEntity {

    @Serial
    private static final long serialVersionUID = 3254249813597263231L;
    /**
     * 字典ID, 取自sys_dict_basic表id
     */
    @Column(name = "dict_id")
    private Long dictId;

    /**
     * 字典数据,如1
     */
    @Column(name = "dict_value")
    private String dictValue;

    /**
     * 字典数据中标签，如是
     */
    @Column(name = "dict_cn_label")
    private String dictCnLabel;

    /**
     * 字典数据英文标签，如YES
     */
    @Column(name = "dict_en_label")
    private String dictEnLabel;

    /**
     * 字典数据顺序
     */
    @Column(name = "dict_sort")
    private Integer dictSort;

    /**
     * 是否是默认值, 1-是, 2-否
     */
    @Column(name = "dict_default_flag")
    private String dictDefaultFlag;

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

    public Long getDictId() {
        return dictId;
    }

    public void setDictId(Long dictId) {
        this.dictId = dictId;
    }

    public String getDictValue() {
        return dictValue;
    }

    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }

    public String getDictCnLabel() {
        return dictCnLabel;
    }

    public void setDictCnLabel(String dictCnLabel) {
        this.dictCnLabel = dictCnLabel;
    }

    public String getDictEnLabel() {
        return dictEnLabel;
    }

    public void setDictEnLabel(String dictEnLabel) {
        this.dictEnLabel = dictEnLabel;
    }

    public Integer getDictSort() {
        return dictSort;
    }

    public void setDictSort(Integer dictSort) {
        this.dictSort = dictSort;
    }

    public String getDictDefaultFlag() {
        return dictDefaultFlag;
    }

    public void setDictDefaultFlag(String dictDefaultFlag) {
        this.dictDefaultFlag = dictDefaultFlag;
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
        return "SysDictDetail{" +
                "super=" + super.toString() +
                ", dictId=" + dictId +
                ", dictValue='" + dictValue + '\'' +
                ", dictCnLabel='" + dictCnLabel + '\'' +
                ", dictEnLabel='" + dictEnLabel + '\'' +
                ", dictSort=" + dictSort +
                ", dictDefaultFlag='" + dictDefaultFlag + '\'' +
                ", deletedStatus='" + deletedStatus + '\'' +
                ", createdAt=" + createdAt +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdatedAt=" + lastUpdatedAt +
                '}';
    }
}