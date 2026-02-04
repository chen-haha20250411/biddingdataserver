package com.xiao.core.sqlserver.mapper;

import com.xiao.core.sqlserver.domain.SqlServerExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SqlServerExampleMapper {

    int insert(SqlServerExample entity);

    int deleteById(String id);

    int update(SqlServerExample entity);

    SqlServerExample queryById(String id);

    List<SqlServerExample> queryByMap(Map<String, Object> columnMap);

    List<SqlServerExample> queryByIds(List<String> idList);

    int queryByCount(Map<String, Object> columnMap);
}
