-- ================================================
-- SQL Server 2008 数据库配置初始化脚本
-- 将以下配置插入到MySQL的sys_dict表中
-- 注意：dict_id是自增长列，不需要插入值
-- ================================================

-- 1. 插入SQL Server配置项（根据实际表结构调整）

-- 配置项1: SQL Server连接URL
INSERT INTO sys_dict (
    dict_name, dict_type, param_name, dict_stat, dict_order
) VALUES (
    'jdbc:sqlserver://10.1.1.10:1433;databaseName=kpm68099;encrypt=false;sslProtocol=TLSv1;selectMethod=cursor;loginTimeout=30;socketTimeout=60000',
    '1',
    'sqlserver.url',
    '1',
    '1'
);

-- 配置项2: SQL Server用户名
INSERT INTO sys_dict (
    dict_name, dict_type, param_name, dict_stat, dict_order
) VALUES (
    'kpis12354521',
    '1',
    'sqlserver.username',
    '1',
    '2'
);

-- 配置项3: SQL Server密码
INSERT INTO sys_dict (
    dict_name, dict_type, param_name, dict_stat, dict_order
) VALUES (
    'k89874admin',
    '1',
    'sqlserver.password',
    '1',
    '3'
);

-- 2. 验证配置是否插入成功
SELECT 
    param_name AS '配置项',
    dict_name AS '配置值',
    CASE dict_stat 
        WHEN '1' THEN '启用' 
        ELSE '禁用' 
    END AS '状态',
    dict_type AS '配置类型',
    dict_id AS '自动生成的ID'
FROM sys_dict 
WHERE dict_type = '1' 
    AND param_name LIKE 'sqlserver.%'
ORDER BY dict_order;

-- ================================================
-- 配置说明：
-- 1. dict_type='1' 表示系统配置类型
-- 2. param_name 是配置项的唯一标识
-- 3. dict_name 是配置值
-- 4. dict_stat='1' 表示配置启用
-- 5. dict_id 是自增长列，数据库自动生成
-- ================================================
