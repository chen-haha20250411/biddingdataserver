-- =====================================================
-- 数据权限管理系统 - 完整表结构
-- 生成时间: 2026-02-27
-- =====================================================

-- =====================================================
-- 1. 用户表 (operator)
-- =====================================================
-- 如果表不存在则创建，如果存在则添加新字段
-- 注意：以下为表结构说明，实际执行时请根据现有表结构调整

/*
用户表 (operator) - 核心字段
字段名              类型           说明
----------------------------------------------------------
operator_id        INT           主键，用户ID
real_name          VARCHAR(50)   真实姓名
login_name         VARCHAR(50)   登录名
login_pwd          VARCHAR(100)  登录密码(MD5加密)
phone_tel          VARCHAR(20)   电话
email              VARCHAR(100)  邮箱
roleinfo_id        INT           功能角色ID (关联 admin_roleinfo)
oper_code          VARCHAR(50)   商户编码
last_time          VARCHAR(50)   最后登录时间
fail_times         VARCHAR(10)   失败次数
company_name       VARCHAR(200)  企业名称
unified_code       VARCHAR(50)   信用代码
corporate          VARCHAR(50)   法人姓名
phone_no           VARCHAR(20)   法人手机号
jbrxm              VARCHAR(50)   经办人姓名
jbrphone           VARCHAR(20)   经办人联系方式
dept_id            INT           部门ID (关联 department_info)
sug_org_id         INT           分支机构ID (关联 branch_info)
*/

-- 添加部门ID和分支机构ID字段（如果不存在）
IF NOT EXISTS (SELECT * FROM syscolumns WHERE id=OBJECT_ID('operator') AND name='dept_id')
BEGIN
    ALTER TABLE operator ADD dept_id INT NULL;
END
GO

IF NOT EXISTS (SELECT * FROM syscolumns WHERE id=OBJECT_ID('operator') AND name='sug_org_id')
BEGIN
    ALTER TABLE operator ADD sug_org_id INT NULL;
END
GO

-- =====================================================
-- 2. 功能角色表 (admin_roleinfo)
-- =====================================================
/*
功能角色表 (admin_roleinfo) - 用于控制用户的功能权限
字段名              类型           说明
----------------------------------------------------------
role_info_id       INT           主键，角色ID
role_name          VARCHAR(50)   角色名称
operator_id        INT           操作员ID
login_name         VARCHAR(50)   操作员名称
remark             VARCHAR(200)  备注
*/

-- =====================================================
-- 3. 数据角色表 (data_role)
-- =====================================================
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='data_role' AND xtype='U')
BEGIN
    CREATE TABLE data_role (
        id              INT IDENTITY(1,1) PRIMARY KEY,
        role_name       NVARCHAR(50) NOT NULL,
        role_code       VARCHAR(50) NOT NULL,
        description     NVARCHAR(200),
        status          INT DEFAULT 1,
        created_at      DATETIME DEFAULT GETDATE(),
        updated_at      DATETIME DEFAULT GETDATE()
    );
    
    -- 创建唯一索引
    CREATE UNIQUE INDEX idx_data_role_code ON data_role(role_code);
    
    PRINT '表 data_role 创建成功';
END
GO

-- =====================================================
-- 4. 数据权限表 (data_permission)
-- =====================================================
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='data_permission' AND xtype='U')
BEGIN
    CREATE TABLE data_permission (
        id                  INT IDENTITY(1,1) PRIMARY KEY,
        role_id             INT NOT NULL,
        permission_type     VARCHAR(50) NOT NULL,
        permission_value    NVARCHAR(MAX),
        created_at          DATETIME DEFAULT GETDATE(),
        CONSTRAINT fk_data_permission_role FOREIGN KEY (role_id) REFERENCES data_role(id)
    );
    
    -- 创建索引
    CREATE INDEX idx_data_permission_role ON data_permission(role_id);
    CREATE INDEX idx_data_permission_type ON data_permission(permission_type);
    
    PRINT '表 data_permission 创建成功';
END
GO

-- =====================================================
-- 5. 用户数据角色关联表 (user_data_role)
-- =====================================================
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='user_data_role' AND xtype='U')
BEGIN
    CREATE TABLE user_data_role (
        id          INT IDENTITY(1,1) PRIMARY KEY,
        user_id     INT NOT NULL,
        role_id     INT NOT NULL,
        created_at  DATETIME DEFAULT GETDATE(),
        CONSTRAINT fk_user_data_role_user FOREIGN KEY (user_id) REFERENCES operator(operator_id),
        CONSTRAINT fk_user_data_role_role FOREIGN KEY (role_id) REFERENCES data_role(id)
    );
    
    -- 创建索引
    CREATE INDEX idx_user_data_role_user ON user_data_role(user_id);
    CREATE INDEX idx_user_data_role_role ON user_data_role(role_id);
    
    -- 创建唯一约束，防止重复分配
    CREATE UNIQUE INDEX idx_user_data_role_unique ON user_data_role(user_id, role_id);
    
    PRINT '表 user_data_role 创建成功';
END
GO

-- =====================================================
-- 6. 部门信息表 (department_info)
-- =====================================================
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='department_info' AND xtype='U')
BEGIN
    CREATE TABLE department_info (
        id              INT IDENTITY(1,1) PRIMARY KEY,
        dept_name       NVARCHAR(100) NOT NULL,
        dept_code       VARCHAR(50),
        parent_id       INT DEFAULT 0,
        dept_level      INT DEFAULT 1,
        status          INT DEFAULT 1,
        created_at      DATETIME DEFAULT GETDATE(),
        updated_at      DATETIME DEFAULT GETDATE()
    );
    
    -- 创建索引
    CREATE INDEX idx_dept_parent ON department_info(parent_id);
    CREATE INDEX idx_dept_status ON department_info(status);
    CREATE UNIQUE INDEX idx_dept_code ON department_info(dept_code);
    
    PRINT '表 department_info 创建成功';
END
GO

-- =====================================================
-- 7. 分支机构信息表 (branch_info)
-- =====================================================
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='branch_info' AND xtype='U')
BEGIN
    CREATE TABLE branch_info (
        id              INT IDENTITY(1,1) PRIMARY KEY,
        branch_name     NVARCHAR(100) NOT NULL,
        branch_code     VARCHAR(50),
        parent_id       INT DEFAULT 0,
        branch_level    INT DEFAULT 1,
        status          INT DEFAULT 1,
        created_at      DATETIME DEFAULT GETDATE(),
        updated_at      DATETIME DEFAULT GETDATE()
    );
    
    -- 创建索引
    CREATE INDEX idx_branch_parent ON branch_info(parent_id);
    CREATE INDEX idx_branch_status ON branch_info(status);
    CREATE UNIQUE INDEX idx_branch_code ON branch_info(branch_code);
    
    PRINT '表 branch_info 创建成功';
END
GO

-- =====================================================
-- 8. 权限类型说明表 (permission_type) - 可选
-- =====================================================
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='permission_type' AND xtype='U')
BEGIN
    CREATE TABLE permission_type (
        id                      INT IDENTITY(1,1) PRIMARY KEY,
        type_code               VARCHAR(50) NOT NULL,
        type_name               NVARCHAR(50) NOT NULL,
        description             NVARCHAR(200),
        sort_order              INT DEFAULT 0,
        status                  INT DEFAULT 1,
        created_at              DATETIME DEFAULT GETDATE()
    );
    
    -- 插入权限类型数据
    INSERT INTO permission_type (type_code, type_name, description, sort_order) VALUES
    ('CUSTOMER', '客户', '客户数据权限', 1),
    ('INDUSTRY', '行业', '行业数据权限', 2),
    ('PRODUCT_TYPE', '产品类型', '产品类型数据权限', 3),
    ('PRODUCT_LINE', '产品线', '产品线数据权限', 4),
    ('WAREHOUSE', '仓库', '仓库数据权限', 5),
    ('BRANCH', '分支机构', '分支机构数据权限', 6),
    ('DEPARTMENT', '部门', '部门数据权限', 7),
    ('EMPLOYEE', '员工', '员工数据权限', 8),
    ('ALL', '全部数据', '可访问全部数据', 9),
    ('CURRENT_USER', '仅当前用户', '仅可访问当前用户数据', 10);
    
    PRINT '表 permission_type 创建成功';
END
GO

-- =====================================================
-- 表结构说明
-- =====================================================

/*
=====================================================
表关系说明
=====================================================

1. operator (用户表)
   - roleinfo_id -> admin_roleinfo.role_info_id (功能角色)
   - dept_id -> department_info.id (部门)
   - sug_org_id -> branch_info.id (分支机构)

2. admin_roleinfo (功能角色表)
   - 用于控制用户可以访问哪些功能菜单

3. data_role (数据角色表)
   - 定义数据访问角色，如"销售经理"、"区域经理"等

4. data_permission (数据权限表)
   - role_id -> data_role.id
   - 定义每个数据角色可以访问的数据范围
   - permission_type: 权限类型(CUSTOMER/INDUSTRY/PRODUCT_TYPE等)
   - permission_value: 权限值(具体的ID列表或ALL/CURRENT_USER)

5. user_data_role (用户数据角色关联表)
   - user_id -> operator.operator_id
   - role_id -> data_role.id
   - 一个用户可以有多个数据角色

6. department_info (部门信息表)
   - 树形结构，通过parent_id关联父部门

7. branch_info (分支机构信息表)
   - 树形结构，通过parent_id关联父机构

8. permission_type (权限类型表)
   - 定义系统支持的权限类型

=====================================================
权限类型说明
=====================================================

| 类型代码      | 类型名称   | 说明                    |
|--------------|-----------|-------------------------|
| CUSTOMER     | 客户       | 可访问的客户数据         |
| INDUSTRY     | 行业       | 可访问的行业数据         |
| PRODUCT_TYPE | 产品类型   | 可访问的产品类型数据     |
| PRODUCT_LINE | 产品线     | 可访问的产品线数据       |
| WAREHOUSE    | 仓库       | 可访问的仓库数据         |
| BRANCH       | 分支机构   | 可访问的分支机构数据     |
| DEPARTMENT   | 部门       | 可访问的部门数据         |
| EMPLOYEE     | 员工       | 可访问的员工数据         |
| ALL          | 全部数据   | 可访问全部数据           |
| CURRENT_USER | 仅当前用户 | 仅可访问当前用户数据     |

=====================================================
API接口说明
=====================================================

1. 用户列表（含详情）
   GET/POST /admin/oper/listWithDetails
   返回：用户基本信息 + 部门名称 + 分支机构名称 + 数据角色列表

2. 用户完整详情
   GET /admin/oper/{operatorId}/detail
   返回：用户完整信息 + 部门详情 + 分支机构详情 + 功能角色 + 数据角色(含权限详情)

3. 部门管理
   GET /api/departmentInfo/list - 部门列表
   POST /api/departmentInfo - 创建部门
   PUT /api/departmentInfo - 更新部门
   DELETE /api/departmentInfo/{id} - 删除部门

4. 分支机构管理
   GET /api/branchInfo/list - 分支机构列表
   POST /api/branchInfo - 创建分支机构
   PUT /api/branchInfo - 更新分支机构
   DELETE /api/branchInfo/{id} - 删除分支机构

5. 数据角色管理
   GET /api/dataRole/list - 数据角色列表
   POST /api/dataRole - 创建数据角色
   PUT /api/dataRole - 更新数据角色
   DELETE /api/dataRole/{id} - 删除数据角色

6. 数据权限管理
   GET /api/dataPermission/role/{roleId} - 获取角色的权限列表
   POST /api/dataPermission - 分配权限
   DELETE /api/dataPermission/{id} - 删除权限

7. 用户数据角色管理
   GET /api/userDataRole/user/{userId} - 获取用户的数据角色
   POST /api/userDataRole/assign - 为用户分配数据角色
   DELETE /api/userDataRole/revoke - 撤销用户的数据角色

*/

PRINT '数据权限管理系统表结构创建完成';
GO
