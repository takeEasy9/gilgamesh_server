package com.gilgamesh.persistence.repository.gilgamesh;

import com.gilgamesh.persistence.entity.gilgamesh.sys.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author takeEasy9
 * @version 1.0.0
 * @description 用户信息
 * @createDate 2025/1/24 14:56
 * @since 1.0.0
 */
public interface SysUserRepository extends JpaRepository<SysUser, Long> {
}
