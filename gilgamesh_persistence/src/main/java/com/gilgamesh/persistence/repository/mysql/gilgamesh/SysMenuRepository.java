package com.gilgamesh.persistence.repository.mysql.gilgamesh;


import com.gilgamesh.persistence.entity.gilgamesh.sys.SysMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;


/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 菜单信息
 * @createDate 2025/1/26 14:15
 * @since 1.0.0
 */
@Repository
public interface SysMenuRepository extends JpaRepository<SysMenu, Long> {

    /**
     * 通过[物理编码, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param id            Long 物理编码
     * @param deletedStatus String 逻辑删除状态, 1-未删除, 2-已删除
     * @return SysMenu
     */
    SysMenu findByIdAndDeletedStatus(Long id, String deletedStatus);

    /**
     * 通过[物理编码集合, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param ids           Collection<Long> 物理编码集合
     * @param deletedStatus String 逻辑删除状态, 1-未删除, 2-已删除
     * @return List<SysMenu>
     */
    List<SysMenu> findByIdInAndDeletedStatus(Collection<Long> ids, String deletedStatus);

    /**
     * 通过[菜单中文名称, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param menuCnName    String 菜单中文名称
     * @param deletedStatus String 逻辑删除状态, 1-未删除, 2-已删除
     * @return SysMenu
     */
    SysMenu findByMenuCnNameAndDeletedStatus(String menuCnName, String deletedStatus);

    /**
     * 通过[菜单中文名称集合, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param menuCnNames   Collection<String> 菜单中文名称集合
     * @param deletedStatus String 逻辑删除状态, 1-未删除, 2-已删除
     * @return List<SysMenu>
     */
    List<SysMenu> findByMenuCnNameInAndDeletedStatus(Collection<String> menuCnNames, String deletedStatus);

    /**
     * 通过[菜单英文名称, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param menuEnName    String 菜单英文名称
     * @param deletedStatus String 逻辑删除状态, 1-未删除, 2-已删除
     * @return SysMenu
     */
    SysMenu findByMenuEnNameAndDeletedStatus(String menuEnName, String deletedStatus);

    /**
     * 通过[菜单英文名称集合, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param menuEnNames   Collection<String> 菜单英文名称集合
     * @param deletedStatus String 逻辑删除状态, 1-未删除, 2-已删除
     * @return List<SysMenu>
     */
    List<SysMenu> findByMenuEnNameInAndDeletedStatus(Collection<String> menuEnNames, String deletedStatus);

    /**
     * 通过[菜单导航路径, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param menuPath      String 菜单导航路径
     * @param deletedStatus String 逻辑删除状态, 1-未删除, 2-已删除
     * @return SysMenu
     */
    SysMenu findByMenuPathAndDeletedStatus(String menuPath, String deletedStatus);

    /**
     * 通过[菜单导航路径集合, 逻辑删除状态, 逻辑删除状态作为查询条件]查询数据
     *
     * @param menuPaths     Collection<String> 菜单导航路径集合
     * @param deletedStatus String 逻辑删除状态, 1-未删除, 2-已删除
     * @return List<SysMenu>
     */
    List<SysMenu> findByMenuPathInAndDeletedStatus(Collection<String> menuPaths, String deletedStatus);
}