package com.gilgamesh.persistence.repository.gilgamesh;


import com.gilgamesh.persistence.entity.gilgamesh.sys.SysDictDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 字典详细信息
 * @createDate 2025/1/26 14:14
 * @since 1.0.0
 */
@Repository
public interface SysDictDetailRepository extends JpaRepository<SysDictDetail, Long> {

    /**
     * 通过[物理编码, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param id            Long 物理编码
     * @param deletedStatus String 逻辑删除状态, 1-未删除, 2-已删除
     * @return SysDictDetail
     */
    SysDictDetail findByIdAndDeletedStatus(Long id, String deletedStatus);

    /**
     * 通过[物理编码集合, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param ids           Collection<Long> 物理编码集合
     * @param deletedStatus String 逻辑删除状态, 1-未删除, 2-已删除
     * @return List<SysDictDetail>
     */
    List<SysDictDetail> findByIdInAndDeletedStatus(Collection<Long> ids, String deletedStatus);
}