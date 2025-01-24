package com.gilgamesh.persistence.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.MappedSuperclass;

import java.io.Serial;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 版本控制泛型基础实体类
 * @createDate 2025/1/23 16:55
 * @since 1.0.0
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class VersionControlGenericBaseEntity extends GenericBaseEntity {
    @Serial
    private static final long serialVersionUID = 2244481482793303492L;
    /**
     * 版本号, 用于控制并发
     */
    @Column(name = "version_id", nullable = false)
    private Long versionId;

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    @Override
    public String toString() {
        return "VersionControlGenericBaseEntity{" +
                "super=" + super.toString() +
                "versionId=" + versionId +
                '}';
    }
}
