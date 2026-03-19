-- 测试分割函数的行为
SELECT * FROM dbo.[COM_F_Split]('翁小萍, 玄林洁', ',');
SELECT * FROM dbo.[COM_F_Split]('翁小萍,玄林洁', ',');
SELECT * FROM dbo.[COM_F_Split]('翁小萍', ',');

-- 测试存储过程的员工参数处理
DECLARE @staff_name NVARCHAR(100) = '翁小萍, 玄林洁';

CREATE TABLE #TestStaffNames (StaffName NVARCHAR(50) PRIMARY KEY);
INSERT INTO #TestStaffNames(StaffName)
SELECT DISTINCT ITEM FROM dbo.[COM_F_Split](@staff_name, ',');

SELECT * FROM #TestStaffNames;

-- 检查员工姓名是否存在于表中
SELECT PersonId, PersonName FROM saturn_KHDX WHERE Year = 2026 AND PersonName IN (SELECT StaffName FROM #TestStaffNames);
SELECT PersonId, PersonName FROM saturn_khdx WHERE Year = 2026 AND PersonName IN (SELECT StaffName FROM #TestStaffNames);

-- 测试每个员工单独查询
EXEC saturn_p_GetSalesProfitReport18_condition '2026-01-01', '2026-03-03', '翁小萍', '', '', '', '';
EXEC saturn_p_GetSalesProfitReport18_condition '2026-01-01', '2026-03-03', '玄林洁', '', '', '', '';

-- 测试去除空格的员工列表
EXEC saturn_p_GetSalesProfitReport18_condition '2026-01-01', '2026-03-03', '翁小萍,玄林洁', '', '', '', '';

DROP TABLE #TestStaffNames;