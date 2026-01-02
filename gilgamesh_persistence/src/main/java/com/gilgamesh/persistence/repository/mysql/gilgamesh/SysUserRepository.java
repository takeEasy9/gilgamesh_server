package com.gilgamesh.persistence.repository.mysql.gilgamesh;

import com.gilgamesh.persistence.entity.gilgamesh.sys.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 用户信息
 * @createDate 2025/1/24 14:56
 * @since 1.0.0
 */
@Repository
public interface SysUserRepository extends JpaRepository<SysUser, Long> {
    /**
     * 根据用户名或用户手机或邮箱查询用户信息
     *
     * @param username 用户名
     * @return SysUser 用户信息
     */
    @Query("SELECT u FROM SysUser u WHERE u.userName = :username OR u.userPhone = :username OR u.userEmail = :username")
    SysUser findByUserNameOrUserPhoneOrUserEmail(@Param("username") String username);

    /**
     * 自定义更新：仅更新最后登录时间（JPQL 写法，跨数据库兼容）
     *
     * @param username        用户名（条件）
     * @param userLastLoginAt 新的最后登录时间
     * @return 受影响的行数（1=更新成功，0=用户不存在）
     */
    @Modifying
    @Query("UPDATE SysUser u SET u.userLastLoginAt = :userLastLoginAt WHERE u.userName = :userName")
    int updateLastLoginAtByUsername(@Param("userName") String username, @Param("userLastLoginAt") Instant userLastLoginAt);
}
