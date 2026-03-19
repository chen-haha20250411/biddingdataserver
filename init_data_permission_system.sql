-- =============================================
-- 数据权限系统数据库表结构
-- 创建日期: 2026-02-27
-- 说明: 包含数据角色、数据权限、用户数据角色关联、部门信息、分支机构信息等表
-- =============================================

-- 1. 修改现有用户表（operator），添加部门ID和分支机构ID字段
ALTER TABLE operator ADD COLUMN dept_id INT(11) COMMENT '部门ID';
ALTER TABLE operator ADD COLUMN sug_org_id INT(11) COMMENT '分支机构ID';

-- 2. 创建数据角色表（data_role）
CREATE TABLE IF NOT EXISTS data_role (
  id INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  role_name VARCHAR(100) NOT NULL COMMENT '角色名称',
  role_code VARCHAR(50) NOT NULL COMMENT '角色编码',
  description VARCHAR(500) COMMENT '角色描述',
  status TINYINT(1) DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_role_code (role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据角色表';

-- 3. 创建数据权限表（data_permission）
CREATE TABLE IF NOT EXISTS data_permission (
  id INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  role_id INT(11) NOT NULL COMMENT '数据角色ID',
  permission_type VARCHAR(50) NOT NULL COMMENT '权限类型：CUSTOMER-客户，INDUSTRY-行业，PRODUCT_TYPE-产品类型，PRODUCT_LINE-产品线，WAREHOUSE-仓库，BRANCH-分支机构，DEPARTMENT-部门，EMPLOYEE-员工',
  permission_value VARCHAR(500) NOT NULL COMMENT '权限值（支持多个值，用逗号分隔）',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  INDEX idx_role_id (role_id),
  INDEX idx_permission_type (permission_type),
  CONSTRAINT fk_data_permission_role FOREIGN KEY (role_id) REFERENCES data_role(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据权限表';

-- 4. 创建用户数据角色关联表（user_data_role）
CREATE TABLE IF NOT EXISTS user_data_role (
  id INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  user_id INT(11) NOT NULL COMMENT '用户ID',
  role_id INT(11) NOT NULL COMMENT '数据角色ID',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_user_role (user_id, role_id),
  INDEX idx_user_id (user_id),
  INDEX idx_role_id (role_id),
  CONSTRAINT fk_user_data_role_user FOREIGN KEY (user_id) REFERENCES operator(operator_id) ON DELETE CASCADE,
  CONSTRAINT fk_user_data_role_role FOREIGN KEY (role_id) REFERENCES data_role(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户数据角色关联表';

-- 5. 创建部门信息表（department_info）
CREATE TABLE IF NOT EXISTS department_info (
  id INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  dept_name VARCHAR(100) NOT NULL COMMENT '部门名称',
  dept_code VARCHAR(50) COMMENT '部门编码',
  parent_id INT(11) DEFAULT 0 COMMENT '父级部门ID',
  dept_level INT(2) DEFAULT 1 COMMENT '部门层级',
  status TINYINT(1) DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  INDEX idx_parent_id (parent_id),
  INDEX idx_dept_code (dept_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门信息表';

-- 6. 创建分支机构信息表（branch_info）
CREATE TABLE IF NOT EXISTS branch_info (
  id INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  branch_name VARCHAR(100) NOT NULL COMMENT '分支机构名称',
  branch_code VARCHAR(50) COMMENT '分支机构编码',
  parent_id INT(11) DEFAULT 0 COMMENT '父级机构ID',
  branch_level INT(2) DEFAULT 1 COMMENT '机构层级',
  status TINYINT(1) DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id),
  INDEX idx_parent_id (parent_id),
  INDEX idx_branch_code (branch_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分支机构信息表';

-- 7. 插入示例数据角色
INSERT INTO data_role (role_name, role_code, description, status) VALUES
('销售经理', 'SALES_MANAGER', '销售部门经理角色，可以查看部门所有员工的数据', 1),
('区域经理', 'REGION_MANAGER', '区域经理角色，可以查看区域内所有分支机构的数据', 1),
('普通员工', 'EMPLOYEE', '普通员工角色，只能查看自己的数据', 1),
('数据管理员', 'DATA_ADMIN', '数据管理员角色，可以查看所有数据', 1);

-- 8. 插入示例数据权限
INSERT INTO data_permission (role_id, permission_type, permission_value) VALUES
(1, 'DEPARTMENT', '1,2,3'),
(2, 'BRANCH', '1,2,3,4'),
(3, 'EMPLOYEE', 'CURRENT_USER'),
(4, 'CUSTOMER', 'ALL');

-- 9. 插入示例部门信息
INSERT INTO department_info (dept_name, dept_code, parent_id, dept_level, status) VALUES
('销售管理部', 'SALES_MGMT', 0, 1, 1),
('销售一部', 'SALES_01', 1, 2, 1),
('销售二部', 'SALES_02', 1, 2, 1),
('销售三部', 'SALES_03', 1, 2, 1);

-- 10. 插入示例分支机构信息
INSERT INTO branch_info (branch_name, branch_code, parent_id, branch_level, status) VALUES
('总公司', 'HQ', 0, 1, 1),
('华东分公司', 'HD', 1, 2, 1),
('华南分公司', 'HN', 1, 2, 1),
('华北分公司', 'HB', 1, 2, 1);

-- 11. 创建权限类型常量表（可选，用于系统内部使用）
CREATE TABLE IF NOT EXISTS permission_type (
  id INT(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  type_code VARCHAR(50) NOT NULL COMMENT '权限类型编码',
  type_name VARCHAR(100) NOT NULL COMMENT '权限类型名称',
  description VARCHAR(500) COMMENT '权限类型描述',
  status TINYINT(1) DEFAULT 1 COMMENT '状态：1-启用，0-禁用',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id),
  UNIQUE KEY uk_type_code (type_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限类型常量表';

-- 12. 插入权限类型常量
INSERT INTO permission_type (type_code, type_name, description, status) VALUES
('CUSTOMER', '客户', '客户数据权限', 1),
('INDUSTRY', '行业', '行业数据权限', 1),
('PRODUCT_TYPE', '产品类型', '产品类型数据权限', 1),
('PRODUCT_LINE', '产品线', '产品线数据权限', 1),
('WAREHOUSE', '仓库', '仓库数据权限', 1),
('BRANCH', '分支机构', '分支机构数据权限', 1),
('DEPARTMENT', '部门', '部门数据权限', 1),
('EMPLOYEE', '员工', '员工数据权限', 1);
