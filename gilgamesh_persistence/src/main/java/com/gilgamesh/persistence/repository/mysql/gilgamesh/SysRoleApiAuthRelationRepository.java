package com.gilgamesh.persistence.repository.mysql.gilgamesh;

import com.gilgamesh.persistence.entity.gilgamesh.sys.SysRoleApiAuthRelation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 角色与接口权限关联关系
 * @createDate 2025/2/16 16:57
 * @since 1.0.0
 */
@Repository
public interface SysRoleApiAuthRelationRepository extends CrudRepository<SysRoleApiAuthRelation, Long> {

    /**
     * 通过[物理编码, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param id            Long 物理编码
     * @param deletedStatus String 逻辑删除状态, 1-未删除,2-已删除
     * @return SysRoleApiAuthRelation
     */
    SysRoleApiAuthRelation findByIdAndDeletedStatus(Long id, String deletedStatus);

    /**
     * 通过[物理编码集合, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param ids           Collection<Long> 物理编码集合
     * @param deletedStatus String 逻辑删除状态, 1-未删除,2-已删除
     * @return List<SysRoleApiAuthRelation>
     */
    List<SysRoleApiAuthRelation> findByIdInAndDeletedStatus(Collection<Long> ids, String deletedStatus);

    /**
     * 通过[角色编码集合, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param roleIds       Collection<Long>
     * @param deletedStatus String
     * @return List<SysRoleApiAuthRelation>
     */
    List<SysRoleApiAuthRelation> findByRoleIdInAndDeletedStatus(Collection<Long> roleIds, String deletedStatus);
}