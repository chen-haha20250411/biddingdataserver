package com.xiao.core.sqlserver.service;

import com.xiao.core.sqlserver.domain.SqlServerExample;

import java.util.List;
import java.util.Map;

public interface SqlServerExampleService {

    int insert(SqlServerExample entity);

    int deleteById(String id);

    int update(SqlServerExample entity);

    SqlServerExample queryById(String id);

    List<SqlServerExample> queryByMap(Map<String, Object> columnMap);

    int queryByCount(Map<String, Object> columnMap);
}
