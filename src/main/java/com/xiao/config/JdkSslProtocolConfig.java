package com.xiao.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;

/**
 * @deprecated 请使用 {@link GlobalSslConfig} 替代
 */
@Deprecated
@Configuration
@ConditionalOnProperty(name = "spring.datasource.sqlserver.url")
public class JdkSslProtocolConfig {

    private static final Logger logger = LoggerFactory.getLogger(JdkSslProtocolConfig.class);

    @PostConstruct
    public void init() {
        logger.warn("JdkSslProtocolConfig 已弃用，请使用 GlobalSslConfig");
    }
}
