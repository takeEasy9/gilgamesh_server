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
 * @description 系统参数
 * @createDate 2025/1/26 14:16
 * @since 1.0.0
 */
@Entity
@Table(schema = "gilgamesh", name = "sys_param")
public class SysParam extends GenericBaseEntity {

    @Serial
    private static final long serialVersionUID = -2848932416079423618L;
    /**
     * 系统参数中文名称
     */
    @Column(name = "param_cn_name")
    private String paramCnName;

    /**
     * 系统参数英文名称
     */
    @Column(name = "param_en_name")
    private String paramEnName;

    /**
     * 参数键名一旦录入不允许修改
     */
    @Column(name = "param_key")
    private String paramKey;

    /**
     * 系统参数值
     */
    @Column(name = "param_value")
    private String paramValue;

    /**
     * 系统参数类型, 1-字符串, 2-整数, 3-浮点数
     */
    @Column(name = "param_type")
    private String paramType;

    /**
     * 参数描述
     */
    @Column(name = "param_description")
    private String paramDescription;

    /**
     * 参数删除UTC时间(UNIX时间戳, 单位毫秒)
     */
    @Column(name = "deleted_at")
    private Long deletedAt;

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

    public String getParamCnName() {
        return paramCnName;
    }

    public void setParamCnName(String paramCnName) {
        this.paramCnName = paramCnName;
    }

    public String getParamEnName() {
        return paramEnName;
    }

    public void setParamEnName(String paramEnName) {
        this.paramEnName = paramEnName;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getParamDescription() {
        return paramDescription;
    }

    public void setParamDescription(String paramDescription) {
        this.paramDescription = paramDescription;
    }

    public Long getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Long deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    @Override
    public String toString() {
        return "SysParam{" +
                "super=" + super.toString() +
                ", paramCnName='" + paramCnName + '\'' +
                ", paramEnName='" + paramEnName + '\'' +
                ", paramKey='" + paramKey + '\'' +
                ", paramValue='" + paramValue + '\'' +
                ", paramType='" + paramType + '\'' +
                ", paramDescription='" + paramDescription + '\'' +
                ", deletedAt=" + deletedAt +
                ", versionId=" + versionId +
                ", createdAt=" + createdAt +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                ", lastUpdatedAt=" + lastUpdatedAt +
                '}';
    }
}