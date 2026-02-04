package com.xiao;

import com.xiao.config.GlobalSslConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;

@SpringBootApplication
@EnableScheduling
public class AppMainServer extends SpringBootServletInitializer {

    private static final Logger logger = LoggerFactory.getLogger(AppMainServer.class);

    public static void main(String[] args) {
        // 1. 初始化全局SSL配置（支持SQL Server 2008 TLS 1.0）
        logger.info("初始化应用...");
        GlobalSslConfig.init();
        
        // 2. 设置JVM参数（备用）
        setupJvmProperties();
        
        // 3. 启动Spring应用
        SpringApplication app = new SpringApplication(AppMainServer.class);
        app.setAddCommandLineProperties(false);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }

    /**
     * 设置JVM属性以支持TLS 1.0
     */
    private static void setupJvmProperties() {
        try {
            // 设置SSL协议
            System.setProperty("jdk.tls.client.protocols", "TLSv1,TLSv1.1,TLSv1.2");
            System.setProperty("jdk.tls.server.protocols", "TLSv1,TLSv1.1,TLSv1.2");
            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
            
            // 记录当前JVM属性
            logger.info("JVM SSL配置: jdk.tls.client.protocols={}", 
                    System.getProperty("jdk.tls.client.protocols"));
            logger.info("JVM SSL配置: jdk.tls.server.protocols={}", 
                    System.getProperty("jdk.tls.server.protocols"));
        } catch (Exception e) {
            logger.warn("设置JVM属性失败: {}", e.getMessage());
        }
    }

    @PostConstruct
    public void init() {
        logger.info("应用启动完成，SSL配置状态: {}", GlobalSslConfig.getSslConfigStatus());
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(AppMainServer.class);
    }

}
