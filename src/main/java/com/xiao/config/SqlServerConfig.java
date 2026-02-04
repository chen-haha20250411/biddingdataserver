package com.xiao.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

// @Configuration - 已弃用，使用SqlServerDynamicConfig从数据库读取配置
// @ConditionalOnProperty(name = "spring.datasource.sqlserver.url")
@Deprecated
public class SqlServerConfig {

    @Value("${spring.datasource.sqlserver.url}")
    private String url;

    @Value("${spring.datasource.sqlserver.username}")
    private String username;

    @Value("${spring.datasource.sqlserver.password}")
    private String password;

    @Value("${spring.datasource.sqlserver.driverClassName:com.microsoft.sqlserver.jdbc.SQLServerDriver}")
    private String driverClassName;

    @Value("${spring.datasource.sqlserver.initialSize:5}")
    private int initialSize;

    @Value("${spring.datasource.sqlserver.minIdle:5}")
    private int minIdle;

    @Value("${spring.datasource.sqlserver.maxActive:20}")
    private int maxActive;

    @Value("${spring.datasource.sqlserver.maxWait:60000}")
    private long maxWait;

    @Value("${spring.datasource.sqlserver.timeBetweenEvictionRunsMillis:60000}")
    private long timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.sqlserver.minEvictableIdleTimeMillis:300000}")
    private long minEvictableIdleTimeMillis;

    @Bean(name = "sqlServerDataSource")
    public DataSource sqlServerDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        dataSource.setInitialSize(initialSize);
        dataSource.setMinIdle(minIdle);
        dataSource.setMaxActive(maxActive);
        dataSource.setMaxWait(maxWait);
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        return dataSource;
    }
}
