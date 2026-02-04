package com.xiao.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

// @Configuration - Disabled due to duplicate bean definition with SqlServerMybatisConfig
// @Deprecated
public class SqlServerTransactionConfig {

    @Autowired
    private DataSource sqlServerDataSource;

    @Bean(name = "sqlServerTransactionManager")
    @ConditionalOnBean(name = "sqlServerDataSource")
    public PlatformTransactionManager sqlServerTransactionManager() {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(sqlServerDataSource);
        return transactionManager;
    }
}
