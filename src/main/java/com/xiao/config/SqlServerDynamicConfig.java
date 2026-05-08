package com.xiao.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.xiao.core.basic.sys_dict.service.SysConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;

/**
 * SQL Server动态配置类
 * 从MySQL数据库的sys_dict表读取配置
 */
@Configuration
@DependsOn({"primaryDataSource", "sysConfigService"})
public class SqlServerDynamicConfig {
    private static final Logger log = LoggerFactory.getLogger(SqlServerDynamicConfig.class);

    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 动态创建SQL Server数据源
     * 只有当数据库中存在完整配置时才创建
     */
    @Bean(name = "sqlServerDataSource")
    public DataSource sqlServerDataSource() {
        // 检查SQL Server配置是否完整
        if (!sysConfigService.isSqlServerConfigured()) {
            log.warn("SQL Server配置不完整，跳过数据源创建");
            log.warn("请在sys_dict表中添加以下配置项: sqlserver.url, sqlserver.username, sqlserver.password");
            return null;
        }

        // 获取SQL Server配置
        Map<String, String> config = sysConfigService.getSqlServerConfig();
        String url = config.get("url");
        String username = config.get("username");
        String password = config.get("password");

        log.info("从数据库加载SQL Server配置, URL: {}, 用户名: {}", maskUrl(url), username);

        // 创建Druid数据源
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        
        // 连接池配置
        dataSource.setInitialSize(5);
        dataSource.setMinIdle(5);
        dataSource.setMaxActive(20);
        dataSource.setMaxWait(60000);
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);

        // 启用 Druid StatFilter 进行 SQL 监控
        try {
            dataSource.setFilters("stat");
        } catch (SQLException e) {
            log.error("Failed to set Druid filters", e);
        }

        // 添加TLS 1.0相关连接属性
        dataSource.setConnectionProperties("encrypt=false;sslProtocol=TLSv1;loginTimeout=30;socketTimeout=60000");

        return dataSource;
    }

    /**
     * 隐藏URL中的敏感信息
     */
    private String maskUrl(String url) {
        if (url == null) return null;
        // 隐藏密码信息
        return url.replaceAll("password=[^;]*", "password=******");
    }
}