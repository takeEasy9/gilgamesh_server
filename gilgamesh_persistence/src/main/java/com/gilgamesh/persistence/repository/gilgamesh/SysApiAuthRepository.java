package com.gilgamesh.persistence.repository.gilgamesh;

import com.gilgamesh.persistence.entity.gilgamesh.sys.SysApiAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 接口权限信息
 * @createDate 2025/1/26 14:04
 * @since 1.0.0
 */
@Repository
public interface SysApiAuthRepository extends JpaRepository<SysApiAuth, Long> {

    /**
     * 通过[物理编码, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param id            Long 物理编码
     * @param deletedStatus String 逻辑删除状态, 1-未删除, 2-已删除
     * @return SysApiAuth
     */
    SysApiAuth findByIdAndDeletedStatus(Long id, String deletedStatus);

    /**
     * 通过[物理编码集合, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param ids           Collection<Long> 物理编码集合
     * @param deletedStatus String 逻辑删除状态, 1-未删除, 2-已删除
     * @return List<SysApiAuth>
     */
    List<SysApiAuth> findByIdInAndDeletedStatus(Collection<Long> ids, String deletedStatus);

    /**
     * 通过[接口权限中文名称, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param apiAuthCnName String 接口权限中文名称
     * @param deletedStatus String 逻辑删除状态, 1-未删除, 2-已删除
     * @return SysApiAuth
     */
    SysApiAuth findByApiAuthCnNameAndDeletedStatus(String apiAuthCnName, String deletedStatus);

    /**
     * 通过[接口权限中文名称集合, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param apiAuthCnNames Collection<String> 接口权限中文名称集合
     * @param deletedStatus  String 逻辑删除状态, 1-未删除, 2-已删除
     * @return List<SysApiAuth>
     */
    List<SysApiAuth> findByApiAuthCnNameInAndDeletedStatus(Collection<String> apiAuthCnNames, String deletedStatus);

    /**
     * 通过[接口权限英文文名称, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param apiAuthEnName String 接口权限英文文名称
     * @param deletedStatus String 逻辑删除状态, 1-未删除, 2-已删除
     * @return SysApiAuth
     */
    SysApiAuth findByApiAuthEnNameAndDeletedStatus(String apiAuthEnName, String deletedStatus);

    /**
     * 通过[接口权限英文文名称集合, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param apiAuthEnNames Collection<String> 接口权限英文文名称集合
     * @param deletedStatus  String 逻辑删除状态, 1-未删除, 2-已删除
     * @return List<SysApiAuth>
     */
    List<SysApiAuth> findByApiAuthEnNameInAndDeletedStatus(Collection<String> apiAuthEnNames, String deletedStatus);
}