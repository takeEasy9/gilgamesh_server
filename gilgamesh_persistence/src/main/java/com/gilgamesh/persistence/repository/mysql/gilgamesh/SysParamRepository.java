package com.gilgamesh.persistence.repository.mysql.gilgamesh;


import com.gilgamesh.persistence.entity.gilgamesh.sys.SysParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 系统参数
 * @createDate 2025/1/26 14:16
 * @since 1.0.0
 */
@Repository
public interface SysParamRepository extends JpaRepository<SysParam, Long> {

    /**
     * 通过[物理编码集合]查询数据
     *
     * @param ids Collection<Long> 物理编码集合
     * @return List<SysParam>
     */
    List<SysParam> findByIdIn(Collection<Long> ids);
}