package com.xiao.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@DependsOn("sqlServerDataSource")
@MapperScan(basePackages = "com.xiao.core.sqlserver.mapper", 
             sqlSessionFactoryRef = "sqlServerSqlSessionFactory")
public class SqlServerMybatisConfig {

    @Bean(name = "sqlServerSqlSessionFactory")
    public SqlSessionFactory sqlServerSqlSessionFactory(@Qualifier("sqlServerDataSource") DataSource dataSource) throws Exception {
        // 检查数据源是否为null（配置不完整时）
        if (dataSource == null) {
            System.out.println("⚠️  SQL Server数据源为空，跳过SqlSessionFactory创建");
            return null;
        }
        
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:mapper/sqlserver/*Mapper.xml"));
        bean.setTypeAliasesPackage("com.xiao.core.sqlserver.domain");
        return bean.getObject();
    }

    @Bean(name = "sqlServerSqlSessionTemplate")
    public SqlSessionTemplate sqlServerSqlSessionTemplate(@Qualifier("sqlServerSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        if (sqlSessionFactory == null) {
            return null;
        }
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "sqlServerTransactionManager")
    public PlatformTransactionManager sqlServerTransactionManager(@Qualifier("sqlServerDataSource") DataSource dataSource) {
        if (dataSource == null) {
            return null;
        }
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }
}
