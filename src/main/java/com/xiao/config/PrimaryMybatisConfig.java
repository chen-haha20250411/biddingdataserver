package com.xiao.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = {"com.xiao.core.zhongbiao.mapper",
                             "com.xiao.core.biddingInfo.mapper",
                             "com.xiao.core.basic.sys_dict.mapper",
                             "com.xiao.core.basic.si_zone.mapper",
                             "com.xiao.core.basic.operator_log.mapper",
                             "com.xiao.core.basic.operator.mapper",
                             "com.xiao.core.basic.admin_rolemenu.mapper",
                             "com.xiao.core.basic.admin_roleinfo.mapper",
                             "com.xiao.core.basic.admin_rolebtn.mapper",
                             "com.xiao.core.basic.admin_menu.mapper",
                             "com.xiao.core.basic.admin_btn.mapper"},
             sqlSessionFactoryRef = "primarySqlSessionFactory")
public class PrimaryMybatisConfig {

    @Bean(name = "primarySqlSessionFactory")
    @Primary
    public SqlSessionFactory primarySqlSessionFactory(@Qualifier("primaryDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath:mapper/**/*Mapper.xml"));
        bean.setTypeAliasesPackage("com.xiao.**.domain");
        return bean.getObject();
    }

    @Bean(name = "primarySqlSessionTemplate")
    @Primary
    public SqlSessionTemplate primarySqlSessionTemplate(@Qualifier("primarySqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean(name = "primaryTransactionManager")
    @Primary
    public PlatformTransactionManager primaryTransactionManager(@Qualifier("primaryDataSource") DataSource dataSource) {
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }
}
