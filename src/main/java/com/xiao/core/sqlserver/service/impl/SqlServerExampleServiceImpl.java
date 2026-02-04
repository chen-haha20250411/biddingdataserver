package com.xiao.core.sqlserver.service.impl;

import com.xiao.core.sqlserver.domain.SqlServerExample;
import com.xiao.core.sqlserver.mapper.SqlServerExampleMapper;
import com.xiao.core.sqlserver.service.SqlServerExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("sqlServerExampleService")
@Transactional(transactionManager = "sqlServerTransactionManager")
public class SqlServerExampleServiceImpl implements SqlServerExampleService {

    @Autowired
    private SqlServerExampleMapper sqlServerExampleMapper;

    @Override
    public int insert(SqlServerExample entity) {
        return sqlServerExampleMapper.insert(entity);
    }

    @Override
    public int deleteById(String id) {
        return sqlServerExampleMapper.deleteById(id);
    }

    @Override
    public int update(SqlServerExample entity) {
        return sqlServerExampleMapper.update(entity);
    }

    @Override
    public SqlServerExample queryById(String id) {
        return sqlServerExampleMapper.queryById(id);
    }

    @Override
    public List<SqlServerExample> queryByMap(Map<String, Object> columnMap) {
        return sqlServerExampleMapper.queryByMap(columnMap);
    }

    @Override
    public int queryByCount(Map<String, Object> columnMap) {
        return sqlServerExampleMapper.queryByCount(columnMap);
    }
}
