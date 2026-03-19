-- 测试存储过程调用
-- 1. 测试2024年数据
EXEC saturn_p_GetSalesProfitReport18_condition '2024-01-01 00:00:00', '2024-03-03 23:59:59', '翁小萍, 玄林洁', '', '', '', '';

-- 2. 测试2025年数据
EXEC saturn_p_GetSalesProfitReport18_condition '2025-01-01 00:00:00', '2025-03-03 23:59:59', '翁小萍, 玄林洁', '', '', '', '';

-- 3. 测试2026年数据
EXEC saturn_p_GetSalesProfitReport18_condition '2026-01-01 00:00:00', '2026-03-03 23:59:59', '翁小萍, 玄林洁', '', '', '', '';

-- 4. 测试2026年单个员工
EXEC saturn_p_GetSalesProfitReport18_condition '2026-01-01 00:00:00', '2026-03-03 23:59:59', '翁小萍', '', '', '', '';

-- 5. 测试2026年销售额目标
EXEC sp_GetSalesData_2026_v5 2026, 'PERSON', '翁小萍, 玄林洁';

-- 6. 检查相关表中是否存在2026年的数据
SELECT TOP 10 * FROM saturn_porfit WHERE _year = 2026;
SELECT TOP 10 * FROM ERP_DelegationFootedUp WHERE YEAR(DELEGATION_FOOTED_UP_DATE) = 2026;
SELECT TOP 10 * FROM saturn_V_PurchaseDetailZhekou WHERE DOCUMENT_YEAR = 2026;