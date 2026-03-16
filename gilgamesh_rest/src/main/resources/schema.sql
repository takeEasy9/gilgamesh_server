CREATE TABLE IF NOT EXISTS `sys_api_auth` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 接口权限ID',
  `api_auth_cn_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接口权限中文名称',
  `api_auth_en_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接口权限英文文名称',
  `api_auth_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接口权限描述',
  `api_method` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接口请求方法, 如GET, POST',
  `api_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '接口路径',
  `deleted_status` char(1) NOT NULL DEFAULT '1' COMMENT '逻辑删除状态, 1-未删除, 2-已删除',
  `version_id` bigint NOT NULL DEFAULT '1' COMMENT '版本号, 用于控制并发',
  `created_by` varchar(64) NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sys_api_auth_cn_name_ux` (`api_auth_cn_name`),
  UNIQUE KEY `sys_api_auth_en_name_ux` (`api_auth_en_name`),
  UNIQUE KEY `sys_api_auth_api_method_path_ux` (`api_method`,`api_path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='接口权限信息';

CREATE TABLE IF NOT EXISTS `sys_constraint` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 约束ID',
  `constraint_code` char(9) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '约束编码',
  `constraint_cn_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '约束中文名称',
  `constraint_en_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '约束英文文名称',
  `constraint_order` int NOT NULL DEFAULT '1' COMMENT '约束排序',
  `constraint_description` varchar(127) NOT NULL COMMENT '约束描述',
  `constraint_parent_code` char(6) DEFAULT NULL COMMENT '约束父级编码',
  `constraint_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '约束类型, 1-NULL, 2-字符串, 3-整数, 4-浮点数, 5-集合(集合)',
  `deleted_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '逻辑删除状态, 1-未删除,2-已删除',
  `version_id` bigint NOT NULL DEFAULT '1' COMMENT '版本号, 用于控制并发',
  `created_by` varchar(64) NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sys_constraint_code_ux` (`constraint_code`),
  UNIQUE KEY `sys_constraint_cn_name` (`constraint_cn_name`),
  UNIQUE KEY `sys_constraint_en_name` (`constraint_en_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统约束';

CREATE TABLE IF NOT EXISTS `sys_dept` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 系统用户ID',
  `dept_cn_name` varchar(32) NOT NULL COMMENT '部门中文名称',
  `dept_en_name` varchar(32) NOT NULL COMMENT '部门英文名称',
  `parent_dept_id` bigint DEFAULT NULL COMMENT '父级部门ID',
  `dept_description` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '部门描述',
  `deleted_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '逻辑删除状态,1-未删除,2-已删除',
  `version_id` bigint NOT NULL DEFAULT '1' COMMENT '版本号, 用于控制并发',
  `created_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `dept_cn_name_ux` (`dept_cn_name`),
  UNIQUE KEY `dept_en_name_ux` (`dept_en_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门表';

CREATE TABLE IF NOT EXISTS `sys_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 菜单ID',
  `menu_cn_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单中文名称',
  `menu_en_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单英文名称',
  `menu_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单导航路径',
  `menu_icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单图标',
  `parent_menu_id` bigint DEFAULT NULL COMMENT '父级菜单ID',
  `menu_order` int NOT NULL DEFAULT '1' COMMENT '菜单显示顺序',
  `menu_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单描述',
  `deleted_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '逻辑删除状态, 1-未删除, 2-已删除',
  `version_id` bigint NOT NULL DEFAULT '1' COMMENT '版本号, 用于控制并发',
  `created_by` varchar(64) NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sys_menu_cn_name_ux` (`menu_cn_name`),
  UNIQUE KEY `sys_menu_en_name_ux` (`menu_en_name`),
  UNIQUE KEY `sys_menu_path_ux` (`menu_path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单信息';

CREATE TABLE IF NOT EXISTS `sys_param` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 系统参数ID',
  `param_cn_name` varchar(64) NOT NULL COMMENT '系统参数中文名称',
  `param_en_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '系统参数英文名称',
  `param_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '参数键名一旦录入不允许修改',
  `param_value` varchar(64) NOT NULL COMMENT '系统参数值',
  `param_type` char(1) NOT NULL DEFAULT '1' COMMENT '系统参数类型, 1-字符串, 2-整数, 3-浮点数',
  `param_description` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '参数描述',
  `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT '参数删除UTC时间(UNIX时间戳, 单位毫秒)',
  `version_id` bigint NOT NULL DEFAULT '1' COMMENT '版本号, 用于控制并发',
  `created_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sys_param_cn_name_ux` (`param_cn_name`,`deleted_at`),
  UNIQUE KEY `sys_param_en_name_ux` (`param_en_name`,`deleted_at`),
  UNIQUE KEY `sys_param_key_ux` (`param_key`,`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统参数表';

CREATE TABLE IF NOT EXISTS `sys_param_constraint_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 系统参数与约束关联关系ID',
  `param_id` bigint NOT NULL COMMENT '系统参数ID, 取自表sys_param表id',
  `constraint_code` char(9) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `constraint_value` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '约束值',
  `deleted_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '逻辑删除状态, 1-未删除,2-已删除',
  `created_by` varchar(64) NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sys_param_id_constraint_code_ux` (`param_id`,`constraint_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统参数与约束关联关系';

CREATE TABLE IF NOT EXISTS `sys_post` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 系统用户ID',
  `post_cn_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位中文名称',
  `post_en_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '岗位英文名称',
  `post_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '岗位描述',
  `deleted_status` char(1) NOT NULL DEFAULT '1' COMMENT '逻辑删除状态,1-未删除,2-已删除',
  `version_id` bigint NOT NULL DEFAULT '1' COMMENT '版本号, 用于控制并发',
  `created_by` varchar(64) NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间,UTC时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='岗位信息表';

CREATE TABLE IF NOT EXISTS `sys_role` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 角色ID',
  `role_cn_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色中文名称',
  `role_en_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色英文名称',
  `role_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色描述',
  `version_id` bigint NOT NULL DEFAULT '1' COMMENT '版本号, 用于控制并发',
  `deleted_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '逻辑删除状态, 1-未删除,2-已删除',
  `created_by` varchar(64) NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色信息';

CREATE TABLE IF NOT EXISTS `sys_role_menu_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 系统角色与菜单那关联关系ID',
  `deleted_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '逻辑删除状态, 1-未删除,2-已删除',
  `created_by` varchar(64) NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统角色与菜单关联关系';

CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 系统用户ID',
  `user_name` varchar(32) NOT NULL COMMENT '用户登录名',
  `user_alias` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `user_email` varchar(64) DEFAULT NULL COMMENT '用户邮箱',
  `user_phone` varchar(32) DEFAULT NULL COMMENT '用户手机号',
  `user_gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '3' COMMENT '用户性别,1-男,2-女,3-保密',
  `user_avatar` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户头像路径',
  `user_password` char(60) DEFAULT NULL COMMENT '用户密码哈希',
  `user_last_login_at` timestamp NOT NULL COMMENT '用户最后登录时间',
  `user_lats_login_ip` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户最后登录的IP',
  `user_activate_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户账户激活key',
  `user_password_reset_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户密码重置密码key',
  `user_password_reset_at` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户密码重置时间',
  `user_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户状态,1-待激活,2-正常,3-锁定,4-密码过期,5-注销',
  `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT '用户注销UTC时间(UNIX时间戳, 单位毫秒)',
  `version_id` bigint NOT NULL DEFAULT '1' COMMENT '版本号, 用于控制并发',
  `created_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sys_user_name_ux` (`user_name`,`deleted_at`),
  UNIQUE KEY `sys_user_alias_ux` (`user_alias`,`deleted_at`),
  UNIQUE KEY `sys_user_email_ux` (`user_email`,`deleted_at`),
  UNIQUE KEY `sys_user_phone_ux` (`user_phone`,`deleted_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

CREATE TABLE IF NOT EXISTS `sys_user_dept_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 系统用户与部门关联关系ID',
  `deleted_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '逻辑删除状态, 1-未删除,2-已删除',
  `created_by` varchar(64) NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统用户与部门关联关系';

CREATE TABLE IF NOT EXISTS `sys_user_post_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 系统用户与岗位关联关系ID',
  `deleted_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '逻辑删除状态, 1-未删除,2-已删除',
  `created_by` varchar(64) NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统用户与岗位关联关系';

CREATE TABLE IF NOT EXISTS `sys_user_role_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 系统用户与角色关联关系ID',
  `deleted_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '逻辑删除状态, 1-未删除,2-已删除',
  `created_by` varchar(64) NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统参数与约束关联关系';

CREATE TABLE IF NOT EXISTS `table_meta_basic` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 数据库表元数据基础信息ID',
  `deleted_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '逻辑删除状态, 1-未删除,2-已删除',
  `created_by` varchar(64) NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据库表元数据基础信息';

####################### 以下是新的用户权限体系表设计 #######################
# 用户表
CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 系统用户ID',
  `user_id` varchar(64) NOT NULL COMMENT '用户ID',
  `user_name` varchar(32) NOT NULL COMMENT '用户登录名',
  `user_alias` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `user_email` varchar(64) DEFAULT NULL COMMENT '用户邮箱',
  `user_phone` varchar(32) DEFAULT NULL COMMENT '用户手机号',
  `user_account_expired_at` timestamp NOT NULL COMMENT '用户账户过期时间,UTC时间',
  `user_gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '3' COMMENT '用户性别,1-男,2-女,3-保密',
  `user_avatar` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户头像路径',
  `user_password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户密码哈希',
  `user_pwd_expired_at` timestamp NOT NULL COMMENT '用户密码过期时间,默认90天有效期',
  `user_activate_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户账户激活key',
  `user_activate_key_expire_at` timestamp NULL DEFAULT NULL COMMENT '激活码过期时间,默认24小时有效期',
  `user_activated_at` timestamp NULL DEFAULT NULL COMMENT '激活完成时间',
  `user_pwd_reset_key` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户密码重置密码key',
  `user_pwd_reset_at` timestamp NULL DEFAULT NULL COMMENT '用户密码重置时间',
  `user_pwd_reset_key_expire_at` timestamp NULL DEFAULT NULL COMMENT '用户密码重置密码key过期时间',
  `login_failed_count` int NOT NULL DEFAULT '0' COMMENT '连续登录失败次数(防暴力破解)',
  `login_lock_at` timestamp NULL DEFAULT NULL COMMENT '账号锁定时间(失败次数超限后)，解锁前禁止登录',
  `inviter_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邀请人 ID(推广场景)',
  `user_register_channel` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '1' COMMENT '注册渠道(APP/小程序/ 网页), 复用客户端类型枚举',
  `user_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户状态：1-待激活 2-正常 3-锁定（临时） 4-锁定（永久） 5-密码过期 6-待审核 7-账户过期 8-冻结（长期未使用） 9-注销',
  `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT '用户注销UTC时间(UNIX时间戳, 单位毫秒)',
  `deleted_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '逻辑删除状态, 1-未删除,2-已删除',
  `version_id` bigint NOT NULL DEFAULT '0' COMMENT '版本号, 用于控制并发',
  `created_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sys_user_name_ux` (`user_name`,`deleted_at`),
  UNIQUE KEY `sys_user_alias_ux` (`user_alias`,`deleted_at`),
  UNIQUE KEY `uk_sys_user_user_id` (`user_id`),
  UNIQUE KEY `sys_user_email_ux` (`user_email`,`deleted_at`),
  UNIQUE KEY `sys_user_phone_ux` (`user_phone`,`deleted_at`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';

#用户登录日志表
-- gilgamesh.sys_user_login_log definition
CREATE TABLE IF NOT EXISTS `sys_user_login_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '登录日志ID',
  `user_id` varchar(64) NOT NULL COMMENT '关联sys_user.user_id',
  `login_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间(UTC)',
  `login_ip` varchar(64) NOT NULL COMMENT '登录IP',
  `login_device` varchar(128) DEFAULT NULL COMMENT '登录设备（如Chrome 120/iPhone 15/Windows 11）',
  `login_client_type` varchar(2) NOT NULL COMMENT '客户端类型：1-web端,2-小程序,3-IOS,4-Android,5-桌面',
  `login_client` varchar(128) DEFAULT NULL COMMENT '登录客户端（如微信小程序/APP/浏览器）',
  `login_location` varchar(128) DEFAULT NULL COMMENT 'IP归属地（如北京市朝阳区/阿里云杭州节点）',
  `login_status` char(1) NOT NULL COMMENT '登录结果：1-成功,2-失败（密码错误）,3-失败（账户锁定）,4-失败（密码过期）',
  `fail_reason` varchar(256) DEFAULT NULL COMMENT '登录失败原因（如：密码错误3次/账户已锁定）',
  `created_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  PRIMARY KEY (`id`,`login_at`),
  KEY `idx_user_id` (`user_id`,`login_at`),
  KEY `idx_login_status` (`login_status`,`login_at`),
  KEY `idx_login_client_type` (`login_client_type`,`login_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户登录日志表(按月分区)'
/*!50100 PARTITION BY RANGE (unix_timestamp(`login_at`))
(PARTITION p202601 VALUES LESS THAN (1769904000) ENGINE = InnoDB,
 PARTITION p202602 VALUES LESS THAN (1772323200) ENGINE = InnoDB,
 PARTITION p202603 VALUES LESS THAN (1775001600) ENGINE = InnoDB,
 PARTITION p202604 VALUES LESS THAN (1777593600) ENGINE = InnoDB,
 PARTITION p202605 VALUES LESS THAN (1780272000) ENGINE = InnoDB,
 PARTITION p202606 VALUES LESS THAN (1782864000) ENGINE = InnoDB,
 PARTITION p202607 VALUES LESS THAN (1785542400) ENGINE = InnoDB,
 PARTITION p202608 VALUES LESS THAN (1788220800) ENGINE = InnoDB,
 PARTITION p202609 VALUES LESS THAN (1790812800) ENGINE = InnoDB,
 PARTITION p202610 VALUES LESS THAN (1793491200) ENGINE = InnoDB,
 PARTITION p202611 VALUES LESS THAN (1796083200) ENGINE = InnoDB,
 PARTITION p202612 VALUES LESS THAN (1798761600) ENGINE = InnoDB,
 PARTITION p_default VALUES LESS THAN (2147483647) ENGINE = InnoDB) */;

#员工-组织/岗位关联表
CREATE TABLE IF NOT EXISTS `org_user_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 员工组织关联ID',
  `user_id` varchar(64) NOT NULL COMMENT '员工ID（关联sys_user.user_id）',
  `org_code` varchar(64) NOT NULL COMMENT '关联组织主表编码（sys_org_main.org_code）',
  `org_post_code` varchar(64) DEFAULT NULL COMMENT '岗位编码（org_main.org_code，岗位类型组织）',
  `main_org_flag` tinyint NOT NULL DEFAULT '1' COMMENT '是否主组织：1-是 0-否',
  `start_at` timestamp NOT NULL DEFAULT '1970-01-01 00:00:01' COMMENT '生效时间, 默认1970-01-01表示立即生效',
  `end_at` timestamp NOT NULL DEFAULT '2038-01-19 03:14:07' COMMENT '失效时间, 默认2038-01-19表示永久有效',
  `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT '逻辑删除时间, UTC UNIX时间戳, 单位毫秒',
  `deleted_status` char(1) NOT NULL DEFAULT '1' COMMENT '逻辑删除状态,1-未删除,2-已删除',
  `version_id` bigint NOT NULL DEFAULT '1' COMMENT '版本号, 用于控制并发',
  `created_by` varchar(64) NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_org_post` (`user_id`,`org_code`,`org_post_code`,`deleted_at`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_org_id` (`org_code`),
  KEY `idx_post_id` (`org_post_code`),
  KEY `idx_main_org_flag` (`main_org_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='员工-组织/岗位关联表';

#系统权限(RBAC/ABAC共用)
CREATE TABLE IF NOT EXISTS `sys_permission` (
   `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码，自增主键',
   `perm_code` varchar(64) NOT NULL COMMENT '权限编码(全局唯一，如menu:order, menu:order:query, api:order)',
   `perm_cn_name` varchar(64) NOT NULL COMMENT '权限名称(如查看文档、审批订单)',
   `perm_en_name` varchar(64) NOT NULL COMMENT '权限名称(如查看文档、审批订单)',
   `perm_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '权限类型：1-RBAC 2-ABAC',
   `perm_sub_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限业务类型：11-菜单权限 12-接口权限 13-操作权限 21-abac环境规则',
   `perm_rules` json DEFAULT NULL COMMENT '权限规则, abac环境规则使用',
   `perm_group` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'RBAC/ABAC权限分组(如DOC_GROUP、ORDER_GROUP)',
   `resource_type` varchar(64) NOT NULL COMMENT '关联资源类型(如document、order、user)',
   `perm_sort` int NOT NULL DEFAULT '1' COMMENT '排序优先级',
   `perm_description` varchar(256) DEFAULT NULL COMMENT '权限描述',
   `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT '用户注销UTC时间(UNIX时间戳, 单位毫秒)',
   `deleted_status` char(1) NOT NULL DEFAULT '1' COMMENT '逻辑删除：1-未删 2-已删',
   `version_id` bigint NOT NULL DEFAULT '0' COMMENT '乐观锁版本号',
   `created_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'admin' COMMENT '创建者',
   `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间(UTC)',
   `last_updated_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '最后更新者',
   `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '更新时间(UTC)',
   PRIMARY KEY (`id`),
   UNIQUE KEY `uk_perm_code` (`perm_code`),
   KEY `idx_perm_type` (`perm_type`,`perm_sub_type`),
   KEY `idx_resource_type` (`resource_type`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统权限(RBAC/ABAC共用)';

 #角色信息
CREATE TABLE IF NOT EXISTS `sys_role` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 角色ID',
    `role_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '角色编码',
    `role_cn_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色中文名称',
    `role_en_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色英文名称',
    `role_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色描述',
    `version_id` bigint NOT NULL DEFAULT '0' COMMENT '版本号, 用于控制并发',
    `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT '逻辑删除时间, UTC UNIX时间戳, 单位毫秒',
    `deleted_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '逻辑删除状态, 1-未删除,2-已删除',
    `created_by` varchar(64) NOT NULL DEFAULT 'admin' COMMENT '创建者',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
    `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
    `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sys_role_role_code` (`role_code`)
  ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色信息';

 #角色信息
CREATE TABLE IF NOT EXISTS `sys_role_api_relation` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 角色与接口权限关联关系ID',
    `role_code` varchar(64) NOT NULL COMMENT '角色编码',
    `perm_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接口权限编码,取自表sys_api.perm_code',
    `perm_start_at` timestamp NOT NULL DEFAULT '1970-01-01 00:00:01' COMMENT '接口权限生效时间, 默认1970-01-01表示立即生效',
    `perm_end_at` timestamp NOT NULL DEFAULT '2038-01-19 03:14:07' COMMENT '权限失效时间, 默认2038-01-19表示永久有效',
    `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT '逻辑删除时间, UTC UNIX时间戳, 单位毫秒',
    `deleted_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '逻辑删除状态, 1-未删除,2-已删除',
    `version_id` bigint NOT NULL DEFAULT '0' COMMENT '版本号, 用户控制并发',
    `created_by` varchar(64) NOT NULL DEFAULT 'admin' COMMENT '创建者',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
    `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
    `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code_perm_code_deleted_at` (`role_code`,`perm_code`,`deleted_at`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='角色与接口权限关联关系';

 #系统角色与菜单关联关系
CREATE TABLE IF NOT EXISTS `sys_role_menu_relation` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 系统角色与菜单那关联关系ID',
    `role_code` varchar(64) NOT NULL COMMENT '角色编码',
    `menu_perm_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单权限编码',
    `handle_perm_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作权限编码',
    `perm_start_at` timestamp NULL DEFAULT '1970-01-01 00:00:01' COMMENT '接口权限生效时间, 默认1970-01-01表示立即生效',
    `perm_end_at` timestamp NULL DEFAULT '2038-01-19 03:14:07' COMMENT '权限失效时间, 默认2038-01-19表示永久有效',
    `version_id` bigint NOT NULL DEFAULT '0' COMMENT '版本号, 用户控制并发',
    `deleted_at` bigint DEFAULT NULL COMMENT '逻辑删除时间, UTC UNIX时间戳, 单位毫秒',
    `deleted_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '逻辑删除状态, 1-未删除,2-已删除',
    `created_by` varchar(64) NOT NULL DEFAULT 'admin' COMMENT '创建者',
    `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
    `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
    `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_code_menu_code_handle_code_deleted_at` (`role_code`,`menu_perm_code`,`handle_perm_code`,`deleted_at`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统角色与菜单关联关系';

 #菜单信息
CREATE TABLE IF NOT EXISTS `sys_menu` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 菜单ID',
    `perm_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '菜单权限编码, 取自sys_permission.perm_code',
    `menu_cn_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单中文名称',
    `menu_en_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单英文名称',
    `menu_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单导航路径',
    `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '前端组件路径(仅菜单有效，如sys/user/index)',
    `component_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '前端组件名(路由命名用)',
    `menu_redirect` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '路由重定向路径(仅目录有效)',
    `menu_icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单图标',
    `parent_menu_id` bigint DEFAULT NULL COMMENT '父级菜单ID',
    `menu_sort` int NOT NULL DEFAULT '1' COMMENT '菜单显示顺序',
    `menu_level` int NOT NULL COMMENT '层级深度(根节点=1，子节点逐级+1)',
    `menu_path` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT '/' COMMENT '组织路径(如/1001/1002/1003，用/分隔)',
    `menu_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单描述',
    `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT '逻辑删除时间, UTC UNIX时间戳, 单位毫秒',
    `deleted_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '逻辑删除状态, 1-未删除, 2-已删除',
    `version_id` bigint NOT NULL DEFAULT '1' COMMENT '版本号, 用于控制并发',
    `created_by` varchar(64) NOT NULL DEFAULT 'admin' COMMENT '创建者',
    `created_at` timestamp NOT NULL COMMENT '创建时间, UTC时间',
    `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
    `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `sys_menu_cn_name_ux` (`menu_cn_name`),
    UNIQUE KEY `sys_menu_en_name_ux` (`menu_en_name`),
    UNIQUE KEY `sys_menu_path_ux` (`menu_url`),
    UNIQUE KEY `uk_sys_menu_menu_code` (`perm_code`)
  ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='菜单信息';

 #菜单按钮信息
CREATE TABLE IF NOT EXISTS `sys_menu_handle` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 角色ID',
  `menu_perm_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单权限编码, 取自sys_permission.perm_code',
  `handle_perm_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作权编码, 取自sys_permission.perm_code',
  `handle_cn_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作中文名',
  `handle_en_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作英文名',
  `handle_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作类型：1-按钮 2-输入框 3-下拉框',
  `handle_sort` int NOT NULL DEFAULT '1' COMMENT '同菜单下排序',
  `display_strategy` char(1) NOT NULL COMMENT '无权限展示策略：1-隐藏 2-展示但禁用',
  `handle_attribute` json DEFAULT NULL COMMENT '操作属性配置，如按钮 type 设置为 primary',
  `handle_tips` varchar(128) DEFAULT NULL COMMENT '禁用提示语(如需申请权限，请联系管理员)',
  `handle_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '操作描述',
  `version_id` bigint NOT NULL DEFAULT '1' COMMENT '版本号, 用于控制并发',
  `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT '逻辑删除时间, UTC UNIX时间戳, 单位毫秒',
  `deleted_status` char(1) NOT NULL DEFAULT '1' COMMENT '逻辑删除状态, 1-未删除,2-已删除',
  `created_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

#接口权限信息
CREATE TABLE IF NOT EXISTS `sys_api` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 接口权限ID',
  `perm_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接口权限编码, 取自sys_permission.perm_code',
  `api_cn_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接口中文名称',
  `api_en_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接口英文文名称',
  `api_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接口描述',
  `api_method` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '接口请求方法, 如GET, POST',
  `api_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '接口路径',
  `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT '逻辑删除时间, UTC UNIX时间戳, 单位毫秒',
  `deleted_status` char(1) NOT NULL DEFAULT '1' COMMENT '逻辑删除状态, 1-未删除, 2-已删除',
  `version_id` bigint NOT NULL DEFAULT '1' COMMENT '版本号, 用于控制并发',
  `created_by` varchar(64) NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `sys_api_auth_cn_name_ux` (`api_cn_name`),
  UNIQUE KEY `sys_api_auth_en_name_ux` (`api_en_name`),
  UNIQUE KEY `uk_sys_api_api_code` (`perm_code`),
  UNIQUE KEY `sys_api_auth_api_method_path_ux` (`api_method`,`api_path`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='接口权限信息';

#系统通用字典表
CREATE TABLE IF NOT EXISTS `sys_dict` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长',
  `dict_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典业务主键(唯一语义标识，如 PROVINCE_BJ、SYS_CONFIG_HTTP_PORT)',
  `dict_category` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典分类 BIZ_COMMON：业务通用字典 SYS_CONFIG：系统基础配置 ABAC_SUBJECT_RULE：ABAC主体规则 ABAC_OBJECT_RULE：ABAC客体规则 ABAC_ENV_RULE：ABAC环境规则',
  `dict_sub_category` varchar(64) DEFAULT NULL COMMENT '字典子分类（可选，如：ABAC_SUBJECT_ATTR下的leader_type/role_type）',
  `dict_cn_name` varchar(64) NOT NULL COMMENT '字典中文名称',
  `dict_en_name` varchar(64) NOT NULL COMMENT '字典英文名称',
  `dict_key` varchar(64) NOT NULL COMMENT '字典键名(ABAC规则=属性键/运算符)',
  `dict_value` text NOT NULL COMMENT '字典值(支持长文本/JSON,ABAC规则=规则模板/可选值)',
  `dict_value_type` char(1) NOT NULL DEFAULT '1' COMMENT '值类型：1-字符串 2-整数 3-浮点数 4-JSON 5-枚举',
  `dict_path` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '/' COMMENT '节点路径(基于业务主键，如 /PROVINCE_BJ/CITY_BJ_CHAOYANG/)',
  `dict_level` int NOT NULL DEFAULT '1' COMMENT '节点层级，根节点为1，子节点依次递增',
  `dict_parent_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '父节点业务主键，空字符串表示根节点',
  `dict_sort` int NOT NULL DEFAULT '1' COMMENT '排序号(值越小越靠前)',
  `valid_start_at` timestamp NULL DEFAULT '1970-01-01 00:00:01' COMMENT '生效时间(UTC时间), 默认1970-01-01表示立即生效',
  `valid_end_at` timestamp NOT NULL DEFAULT '2038-01-19 03:14:07' COMMENT '失效时间(UTC时间), 默认2038-01-19表示永久有效',
  `deleted_status` char(1) NOT NULL DEFAULT '1' COMMENT '逻辑删除：1-未删 2-已删',
  `dict_description` varchar(255) NOT NULL COMMENT '字典描述',
  `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT '删除UTC时间(UNIX时间戳, 单位毫秒)',
  `version_id` bigint NOT NULL DEFAULT '1' COMMENT '版本号, 用于控制并发',
  `created_by` varchar(64) NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dict_code_deleted_status` (`dict_code`,`deleted_status`),
  UNIQUE KEY `uk_dict_category_sub_key_deleted` (`dict_category`,`dict_sub_category`,`dict_key`,`deleted_at`),
  KEY `idx_dict_key` (`dict_key`),
  KEY `idx_dict_category_parent_code` (`dict_category`,`dict_parent_code`,`deleted_status`) USING BTREE,
  KEY `idx_dict_path` (`dict_path`(512),`deleted_status`) USING BTREE,
  KEY `idx_dict_level` (`dict_level`,`deleted_status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='系统通用字典表';

#ABAC策略主表
CREATE TABLE IF NOT EXISTS `sys_abac_policy` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码',
  `policy_code` varchar(64) NOT NULL COMMENT '策略编码（如DEPT_LEADER_EDIT_DOC）',
  `policy_cn_name` varchar(64) NOT NULL COMMENT '策略名称(如部门负责人编辑本部门文档)',
  `policy_en_name` varchar(64) NOT NULL COMMENT '策略名称(如部门负责人编辑本部门文档)',
  `subject_attr` json NOT NULL COMMENT '主体属性规则：{"leader_type":1,"org_level":{"operator":"<=","value":3}}',
  `object_attr` json NOT NULL COMMENT '客体属性规则：{"resource_type":"document","org_path":{"operator":"LIKE","value":"0/1001%"}}',
  `action_codes` varchar(512) NOT NULL COMMENT '关联操作编码(多个用逗号分隔，如DOC_VIEW,DOC_EDIT),取自sys_permission.perm_code',
  `env_rule_codes` varchar(512) DEFAULT NULL COMMENT '关联环境规则编码(多个用逗号分隔，如IP_INTERNAL,TIME_WORKDAY),取自sys_permission.perm_code',
  `policy_effect` tinyint NOT NULL COMMENT '策略效果：1-允许 2-拒绝',
  `policy_priority` int NOT NULL DEFAULT '1' COMMENT '策略优先级(值越小越优先)',
  `policy_start_at` timestamp NULL DEFAULT NULL COMMENT '生效开始时间',
  `policy_end_at` timestamp NULL DEFAULT NULL COMMENT '生效结束时间',
  `policy_description` varchar(512) DEFAULT NULL COMMENT '策略描述',
  `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT '逻辑删除时间, UTC UNIX时间戳, 单位毫秒',
  `deleted_status` char(1) NOT NULL DEFAULT '1',
  `version_id` bigint NOT NULL DEFAULT '1',
  `created_by` varchar(64) NOT NULL DEFAULT 'admin',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_updated_by` varchar(64) DEFAULT NULL,
  `last_updated_at` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_policy_code` (`policy_code`,`deleted_at`),
  KEY `idx_priority` (`policy_priority`),
  KEY `idx_effect` (`policy_effect`),
  KEY `idx_policy_start_end` (`policy_start_at`,`policy_end_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='ABAC策略主表';

#组织主表(所有层级通用)
CREATE TABLE IF NOT EXISTS `sys_org_main` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 系统用户ID',
  `org_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '组织编码(全局唯一，如GROUP001/COMP001/DEPT001)',
  `org_cn_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '组织中文名称',
  `org_en_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '组织英文名称',
  `org_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '组织类型：1-集团 2-公司 3-部门 4-小组 5-岗位',
  `dept_description` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '部门描述',
  `parent_org_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '父级组织编码',
  `org_path` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '/' COMMENT '组织路径(如/1001/1002/1003，用/分隔)',
  `org_level` int NOT NULL COMMENT '层级深度(根节点=1，子节点逐级+1)',
  `org_sort` int NOT NULL DEFAULT '0' COMMENT '同层级排序(值越小越靠前)',
  `deleted_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '逻辑删除状态,1-未删除,2-已删除',
  `version_id` bigint NOT NULL DEFAULT '0' COMMENT '版本号, 用于控制并发',
  `created_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT '逻辑删除时间, UTC UNIX时间戳, 单位毫秒',
  PRIMARY KEY (`id`),
  UNIQUE KEY `dept_cn_name_ux` (`org_cn_name`),
  UNIQUE KEY `dept_en_name_ux` (`org_en_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='组织主表(所有层级通用)';

#公司扩展表
CREATE TABLE IF NOT EXISTS `sys_org_company` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 公司扩展表ID',
  `org_code` varchar(64) NOT NULL COMMENT '关联组织主表编码（sys_org_main.org_code）',
  `company_cn_name` varchar(64) DEFAULT NULL COMMENT '公司中文名',
  `company_en_name` varchar(64) DEFAULT NULL COMMENT '公司英文名',
  `company_cn_short_name` varchar(32) DEFAULT NULL COMMENT '公司中文简称',
  `company_en_short_name` varchar(32) DEFAULT NULL COMMENT '公司英文文简称',
  `company_type` tinyint DEFAULT NULL COMMENT '公司类型：1-国企 2-民企 3-外企 4-合资',
  `tax_no` varchar(32) DEFAULT NULL COMMENT '税务登记号',
  `business_license_no` varchar(64) DEFAULT NULL COMMENT '营业执照号',
  `industry` varchar(32) DEFAULT NULL COMMENT '所属行业',
  `company_address` varchar(256) DEFAULT NULL COMMENT '公司地址',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `deleted_status` char(1) NOT NULL DEFAULT '1' COMMENT '逻辑删除状态,1-未删除,2-已删除',
  `version_id` bigint NOT NULL DEFAULT '0' COMMENT '版本号, 用于控制并发',
  `created_by` varchar(64) NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_org_code` (`org_code`),
  UNIQUE KEY `uk_tax_no` (`tax_no`),
  UNIQUE KEY `uk_business_license_no` (`business_license_no`),
  KEY `idx_company_type` (`company_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='公司扩展表';

#部门扩展表
CREATE TABLE IF NOT EXISTS `sys_org_dept` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 部门扩展表ID',
  `org_code` varchar(64) NOT NULL COMMENT '关联组织主表编码（sys_org_main.org_code）',
  `dept_cn_name` varchar(64) DEFAULT NULL COMMENT '部门中文名',
  `dept_en_name` varchar(64) DEFAULT NULL COMMENT '部门英文名',
  `dept_type` tinyint DEFAULT NULL COMMENT '部门类型：1-研发 2-销售 3-财务 4-人事 5-行政',
  `cost_center` varchar(32) DEFAULT NULL COMMENT '成本中心编码',
  `biz_line` varchar(32) DEFAULT NULL COMMENT '所属业务线',
  `office_address` varchar(256) DEFAULT NULL COMMENT '办公地址',
  `deleted_status` char(1) NOT NULL DEFAULT '1' COMMENT '逻辑删除状态,1-未删除,2-已删除',
  `version_id` bigint NOT NULL DEFAULT '0' COMMENT '版本号, 用于控制并发',
  `created_by` varchar(64) NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT '逻辑删除时间, UTC UNIX时间戳, 单位毫秒',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_org_id` (`org_code`),
  KEY `idx_dept_type` (`dept_type`),
  KEY `idx_biz_line` (`biz_line`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='部门扩展表';

#集团扩展表
CREATE TABLE IF NOT EXISTS `sys_org_group` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 集团扩展表ID',
  `org_code` varchar(64) NOT NULL COMMENT '关联组织主表ID（sys_org_main.org_code）',
  `group_cn_name` varchar(64) DEFAULT NULL COMMENT '集团中文名',
  `group_en_name` varchar(64) DEFAULT NULL COMMENT '集团英文名',
  `group_cn_short_name` varchar(32) DEFAULT NULL COMMENT '集团中文简称',
  `group_en_short_name` varchar(32) DEFAULT NULL COMMENT '集团英文文简称',
  `business_scope` varchar(256) DEFAULT NULL COMMENT '经营范围',
  `legal_person` varchar(32) DEFAULT NULL COMMENT '法人',
  `register_capital` decimal(20,2) DEFAULT NULL COMMENT '注册资本（万元）',
  `register_address` varchar(256) DEFAULT NULL COMMENT '注册地址',
  `deleted_status` char(1) NOT NULL DEFAULT '1' COMMENT '逻辑删除状态,1-未删除,2-已删除',
  `version_id` bigint NOT NULL DEFAULT '0' COMMENT '版本号, 用于控制并发',
  `created_by` varchar(64) NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT '逻辑删除时间, UTC UNIX时间戳, 单位毫秒',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_org_code` (`org_code`),
  KEY `idx_legal_person` (`legal_person`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='集团扩展表';

#岗位扩展表
CREATE TABLE IF NOT EXISTS `sys_org_post` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 岗位扩展表ID',
  `org_code` varchar(64) NOT NULL COMMENT '关联组织主表编码（sys_org_main.org_code）',
  `post_cn_name` varchar(64) DEFAULT NULL COMMENT '公司中文名',
  `post_en_name` varchar(64) DEFAULT NULL COMMENT '公司英文名',
  `post_cn_short_name` varchar(32) DEFAULT NULL COMMENT '公司中文简称',
  `post_en_short_name` varchar(32) DEFAULT NULL COMMENT '公司英文文简称',
  `post_grade` varchar(32) DEFAULT NULL COMMENT '职级（如P6/M3）',
  `salary_range` varchar(64) DEFAULT NULL COMMENT '薪资范围（脱敏，如15k-20k）',
  `responsibility` varchar(512) DEFAULT NULL COMMENT '岗位职责',
  `requirement` varchar(512) DEFAULT NULL COMMENT '岗位要求',
  `deleted_status` char(1) NOT NULL DEFAULT '1' COMMENT '逻辑删除状态,1-未删除,2-已删除',
  `version_id` bigint NOT NULL DEFAULT '0' COMMENT '版本号, 用于控制并发',
  `created_by` varchar(64) NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT '逻辑删除时间, UTC UNIX时间戳, 单位毫秒',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_org_code` (`org_code`),
  KEY `idx_post_grade` (`post_grade`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='岗位扩展表';

#组织-负责人关联表
CREATE TABLE IF NOT EXISTS  `org_leader_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 组织负责人关联ID',
  `org_code` varchar(64) NOT NULL COMMENT '关联组织主表编码（sys_org_main.org_code）',
  `leader_user_id` varchar(64) NOT NULL COMMENT '负责人ID（关联sys_user.user_id）',
  `leader_type` tinyint NOT NULL COMMENT '负责人类型：1-主要负责人 2-分管领导 3-临时负责人',
  `start_at` timestamp NOT NULL DEFAULT '1970-01-01 00:00:01' COMMENT '生效时间, 默认1970-01-01表示立即生效',
  `end_time` timestamp NOT NULL DEFAULT '2038-01-19 03:14:07' COMMENT '失效时间, 默认2038-01-19表示永久有效',
  `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT '逻辑删除时间, UTC UNIX时间戳, 单位毫秒',
  `deleted_status` char(1) NOT NULL DEFAULT '1' COMMENT '逻辑删除状态,1-未删除,2-已删除',
  `version_id` bigint NOT NULL DEFAULT '1' COMMENT '版本号, 用于控制并发',
  `created_by` varchar(64) NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_org_leader_type` (`org_code`,`leader_user_id`,`leader_type`,`deleted_at`),
  KEY `idx_org_code` (`org_code`),
  KEY `idx_leader_user_id` (`leader_user_id`),
  KEY `idx_leader_type` (`leader_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='组织-负责人关联表';

#字典基础信息
CREATE TABLE IF NOT EXISTS  `sys_enum_dict_basic` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 字典ID',
  `dict_cn_name` varchar(64) NOT NULL COMMENT '字典中文名称',
  `dict_en_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典英文名称, 用作字典类型编码，如sex_dict, 在代码中获取字典ID然后再通过字典ID查询字典数据',
  `dict_description` varchar(127) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典描述',
  `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT '逻辑删除时间, UTC UNIX时间戳, 单位毫秒',
  `deleted_status` char(1) NOT NULL DEFAULT '1' COMMENT '逻辑删除状态, 1-未删除, 2-已删除',
  `created_by` varchar(64) NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) NOT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_enum_dict_basic_dict_cn_name` (`dict_cn_name`),
  UNIQUE KEY `uk_sys_enum_dict_basic_dict_en_name` (`dict_en_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='字典基础信息';

#字典基础信息
CREATE TABLE IF NOT EXISTS  `sys_enum_dict_detail` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '物理编码, 自增长, 字典数据ID',
  `dict_id` bigint NOT NULL COMMENT '字典ID, 取自sys_dict_basic表id',
  `dict_value` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典数据,如1',
  `dict_cn_label` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典数据中标签，如是',
  `dict_en_label` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典数据英文标签，如YES',
  `dict_sort` int NOT NULL DEFAULT '1' COMMENT '字典数据顺序',
  `dict_default_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '2' COMMENT '是否是默认值, 1-是, 2-否',
  `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT '逻辑删除时间, UTC UNIX时间戳, 单位毫秒',
  `deleted_status` char(1) NOT NULL DEFAULT '1' COMMENT '逻辑删除状态, 1-未删除, 2-已删除',
  `created_by` varchar(64) NOT NULL DEFAULT 'admin' COMMENT '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) NOT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `dict_id_value_ux` (`dict_id`,`dict_value`),
  UNIQUE KEY `dict_id_cn_label` (`dict_id`,`dict_cn_label`),
  UNIQUE KEY `dict_id_en_label` (`dict_id`,`dict_en_label`),
  UNIQUE KEY `uk_sys_enum_dict_detail_dict_id_dict_value` (`dict_id`,`dict_value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

#
CREATE TABLE IF NOT EXISTS   `sys_enum_dict_detail_group_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键,自增长',
  `dict_id` bigint NOT NULL COMMENT '枚举字典字典ID',
  `dict_detail_id` bigint NOT NULL COMMENT '枚举详细数据ID,取自sys_enum_dict_detail表',
  `dict_group_id` bigint NOT NULL COMMENT '字典分组ID,取自sys_enum_dict_group表id',
  `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT '逻辑删除时间, UTC UNIX时间戳, 单位毫秒',
  `deleted_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '逻辑删除状态,1-未删除,2-已删除',
  `created_by` varchar(64) NOT NULL COMMENT '创建者',
  `created_at` timestamp NOT NULL COMMENT '创建时间, UTC时间',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新者',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dict_id_dict_detail_id_dict_group_id` (`dict_id`,`dict_detail_id`,`dict_group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

#sys_enum_dict_group
CREATE TABLE IF NOT EXISTS   `sys_enum_dict_group` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键,自增长',
  `group_cn_name` varchar(64) NOT NULL COMMENT '分组中文名称',
  `group_en_name` varchar(64) NOT NULL COMMENT '分组英文名称',
  `group_description` varchar(128) DEFAULT NULL COMMENT '分组描述',
  `deleted_at` bigint NOT NULL DEFAULT '0' COMMENT '逻辑删除时间, UTC UNIX时间戳, 单位毫秒',
  `deleted_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '1' COMMENT '逻辑删除状态,1-未删除,2-已删除',
  `created_by` varchar(64) NOT NULL COMMENT '创建者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间, UTC时间',
  `last_updated_by` varchar(64) DEFAULT NULL COMMENT '最后更新者',
  `last_updated_at` timestamp NULL DEFAULT NULL COMMENT '最后更新时间, UTC时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_enum_dict_group_group_cn_name` (`group_cn_name`),
  UNIQUE KEY `uk_sys_enum_dict_group_group_en_name` (`group_en_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;




