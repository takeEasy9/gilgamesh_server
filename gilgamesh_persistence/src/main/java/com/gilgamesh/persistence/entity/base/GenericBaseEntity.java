package com.gilgamesh.persistence.entity.base;

import com.gilgamesh.common.enums.SystemEnums;
import com.gilgamesh.common.utils.StringUtil;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description GenericBaseEntity
 * @createDate 2025/1/23 16:20
 * @since 1.0.0
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class GenericBaseEntity extends AuditingEntity implements Serializable, LogicallyDeleted {
    @Serial
    private static final long serialVersionUID = -601344038396753516L;

    /**
     * 物理编码, 自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 逻辑删除状态, 1-未删除,2-已删除
     */
    @Column(name = "deleted_status", nullable = false)
    private String deletedStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeletedStatus() {
        return deletedStatus;
    }

    public void setDeletedStatus(String deletedStatus) {
        this.deletedStatus = deletedStatus;
    }

    @Override
    public String toString() {
        return "GenericBaseEntity{" +
                "super=" + super.toString() +
                "id=" + id +
                ", deletedStatus='" + deletedStatus + '\'' +
                '}';
    }

    @Override
    public boolean isDeleted() {
        assert StringUtil.isNotEmpty(this.deletedStatus) : "deletedStatus is empty";
        return SystemEnums.DeletedStatus.DELETED.getLabel().equals(this.deletedStatus);
    }
}
