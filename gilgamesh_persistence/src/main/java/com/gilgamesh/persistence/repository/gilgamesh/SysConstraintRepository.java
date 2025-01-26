package com.gilgamesh.persistence.repository.gilgamesh;


import com.gilgamesh.persistence.entity.gilgamesh.sys.SysConstraint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 系统约束
 * @createDate 2025/1/26 14:10
 * @since 1.0.0
 */
@Repository
public interface SysConstraintRepository extends JpaRepository<SysConstraint, Long> {

    /**
     * 通过[物理编码, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param id            Long 物理编码
     * @param deletedStatus String 逻辑删除状态, 1-未删除,2-已删除
     * @return SysConstraint
     */
    SysConstraint findByIdAndDeletedStatus(Long id, String deletedStatus);

    /**
     * 通过[物理编码集合, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param ids           Collection<Long> 物理编码集合
     * @param deletedStatus String 逻辑删除状态, 1-未删除,2-已删除
     * @return List<SysConstraint>
     */
    List<SysConstraint> findByIdInAndDeletedStatus(Collection<Long> ids, String deletedStatus);

    /**
     * 通过[约束编码, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param constraintCode String 约束编码
     * @param deletedStatus  String 逻辑删除状态, 1-未删除,2-已删除
     * @return SysConstraint
     */
    SysConstraint findByConstraintCodeAndDeletedStatus(String constraintCode, String deletedStatus);

    /**
     * 通过[约束编码集合, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param constraintCodes Collection<String> 约束编码集合
     * @param deletedStatus   String 逻辑删除状态, 1-未删除,2-已删除
     * @return List<SysConstraint>
     */
    List<SysConstraint> findByConstraintCodeInAndDeletedStatus(Collection<String> constraintCodes, String deletedStatus);

    /**
     * 通过[约束中文名称, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param constraintCnName String 约束中文名称
     * @param deletedStatus    String 逻辑删除状态, 1-未删除,2-已删除
     * @return SysConstraint
     */
    SysConstraint findByConstraintCnNameAndDeletedStatus(String constraintCnName, String deletedStatus);

    /**
     * 通过[约束中文名称集合, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param constraintCnNames Collection<String> 约束中文名称集合
     * @param deletedStatus     String 逻辑删除状态, 1-未删除,2-已删除
     * @return List<SysConstraint>
     */
    List<SysConstraint> findByConstraintCnNameInAndDeletedStatus(Collection<String> constraintCnNames, String deletedStatus);

    /**
     * 通过[约束英文文名称, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param constraintEnName String 约束英文文名称
     * @param deletedStatus    String 逻辑删除状态, 1-未删除,2-已删除
     * @return SysConstraint
     */
    SysConstraint findByConstraintEnNameAndDeletedStatus(String constraintEnName, String deletedStatus);

    /**
     * 通过[约束英文文名称集合, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param constraintEnNames Collection<String> 约束英文文名称集合
     * @param deletedStatus     String 逻辑删除状态, 1-未删除,2-已删除
     * @return List<SysConstraint>
     */
    List<SysConstraint> findByConstraintEnNameInAndDeletedStatus(Collection<String> constraintEnNames, String deletedStatus);
}