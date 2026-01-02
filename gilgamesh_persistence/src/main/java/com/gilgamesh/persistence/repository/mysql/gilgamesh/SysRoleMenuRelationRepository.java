package com.gilgamesh.persistence.repository.mysql.gilgamesh;


import com.gilgamesh.persistence.entity.gilgamesh.sys.SysRoleMenuRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 角色菜单关联关系
 * @createDate 2025/1/26 14:19
 * @since 1.0.0
 */
@Repository
public interface SysRoleMenuRelationRepository extends JpaRepository<SysRoleMenuRelation, Long> {

    /**
     * 通过[物理编码, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param id            Long 物理编码
     * @param deletedStatus String 逻辑删除状态, 1-未删除,2-已删除
     * @return SysRoleMenuRelation
     */
    SysRoleMenuRelation findByIdAndDeletedStatus(Long id, String deletedStatus);

    /**
     * 通过[物理编码集合, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param ids           Collection<Long> 物理编码集合
     * @param deletedStatus String 逻辑删除状态, 1-未删除,2-已删除
     * @return List<SysRoleMenuRelation>
     */
    List<SysRoleMenuRelation> findByIdInAndDeletedStatus(Collection<Long> ids, String deletedStatus);
}