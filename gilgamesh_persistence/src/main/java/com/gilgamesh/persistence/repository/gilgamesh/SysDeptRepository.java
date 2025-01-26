package com.gilgamesh.persistence.repository.gilgamesh;


import com.gilgamesh.persistence.entity.gilgamesh.sys.SysDept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 部门信息
 * @createDate 2025/1/26 14:12
 * @since 1.0.0
 */
@Repository
public interface SysDeptRepository extends JpaRepository<SysDept, Long> {

    /**
     * 通过[物理编码, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param id            Long 物理编码
     * @param deletedStatus String 逻辑删除状态,1-未删除,2-已删除
     * @return SysDept
     */
    SysDept findByIdAndDeletedStatus(Long id, String deletedStatus);

    /**
     * 通过[物理编码集合, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param ids           Collection<Long> 物理编码集合
     * @param deletedStatus String 逻辑删除状态,1-未删除,2-已删除
     * @return List<SysDept>
     */
    List<SysDept> findByIdInAndDeletedStatus(Collection<Long> ids, String deletedStatus);

    /**
     * 通过[部门中文名称, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param deptCnName    String 部门中文名称
     * @param deletedStatus String 逻辑删除状态,1-未删除,2-已删除
     * @return SysDept
     */
    SysDept findByDeptCnNameAndDeletedStatus(String deptCnName, String deletedStatus);

    /**
     * 通过[部门中文名称集合, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param deptCnNames   Collection<String> 部门中文名称集合
     * @param deletedStatus String 逻辑删除状态,1-未删除,2-已删除
     * @return List<SysDept>
     */
    List<SysDept> findByDeptCnNameInAndDeletedStatus(Collection<String> deptCnNames, String deletedStatus);

    /**
     * 通过[部门英文名称, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param deptEnName    String 部门英文名称
     * @param deletedStatus String 逻辑删除状态,1-未删除,2-已删除
     * @return SysDept
     */
    SysDept findByDeptEnNameAndDeletedStatus(String deptEnName, String deletedStatus);

    /**
     * 通过[部门英文名称集合, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param deptEnNames   Collection<String> 部门英文名称集合
     * @param deletedStatus String 逻辑删除状态,1-未删除,2-已删除
     * @return List<SysDept>
     */
    List<SysDept> findByDeptEnNameInAndDeletedStatus(Collection<String> deptEnNames, String deletedStatus);
}