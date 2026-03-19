-- 创建用户数据权限表
CREATE TABLE IF NOT EXISTS `user_data_permission` (
  `id` INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `source_user_id` INT(11) NOT NULL COMMENT '授权用户ID',
  `target_user_id` INT(11) NOT NULL COMMENT '被访问用户ID',
  `permission_type` VARCHAR(50) NOT NULL COMMENT '权限类型',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_source_target_permission` (`source_user_id`, `target_user_id`, `permission_type`),
  INDEX `idx_source_user` (`source_user_id`),
  INDEX `idx_target_user` (`target_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户数据访问权限表';