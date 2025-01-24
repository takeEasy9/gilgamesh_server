package com.gilgamesh.persistence.entity.base;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 重复软删除基础实体类
 * @createDate 2025/1/23 16:33
 * @since 1.0.0
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class DeletedAtGenericBaseEntity extends AuditingEntity implements Serializable, LogicallyDeleted {
    @Serial
    private static final long serialVersionUID = 6146274678016331531L;

    /**
     * 物理编码, 自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 逻辑删除时间, UTC UNIX时间戳, 单位毫秒
     */
    @Column(name = "deleted_at", nullable = false)
    private Long deletedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Long deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public String toString() {
        return "RepeatedSoftDeletedBaseEntity{" +
                "super=" + super.toString() +
                "id=" + id +
                ", deletedAt=" + deletedAt +
                '}';
    }

    @Override
    public boolean isDeleted() {
        assert this.deletedAt != null : "deletedAt is null";
        return deletedAt > 0;
    }
}
