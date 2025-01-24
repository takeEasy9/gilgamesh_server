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