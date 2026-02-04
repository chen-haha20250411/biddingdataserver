package com.xiao.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.X509Certificate;

/**
 * 全局SSL配置，解决JDK 11禁用TLS 1.0的问题
 * 适用于SQL Server 2008等旧系统
 */
public class GlobalSslConfig {

    private static final Logger logger = LoggerFactory.getLogger(GlobalSslConfig.class);
    
    private static boolean initialized = false;

    static {
        init();
    }

    /**
     * 初始化全局SSL配置
     */
    public static synchronized void init() {
        if (initialized) {
            return;
        }
        
        try {
            logger.info("正在配置全局SSL/TLS协议以支持SQL Server 2008...");
            
            // 方法1: 设置系统属性（可能不够）
            System.setProperty("jdk.tls.client.protocols", "TLSv1,TLSv1.1,TLSv1.2");
            System.setProperty("jdk.tls.server.protocols", "TLSv1,TLSv1.1,TLSv1.2");
            
            // 临时禁用TLS算法限制（生产环境需谨慎）
            String disabledAlgorithms = Security.getProperty("jdk.tls.disabledAlgorithms");
            if (disabledAlgorithms != null && disabledAlgorithms.contains("TLSv1")) {
                logger.warn("当前JDK禁用了TLSv1: {}", disabledAlgorithms);
                // 注意：修改此设置可能影响安全性
                // 仅在连接SQL Server 2008等旧系统时使用
                String newDisabled = disabledAlgorithms
                        .replace("TLSv1,", "")
                        .replace("TLSv1.1,", "")
                        .replace(",TLSv1", "")
                        .replace(",TLSv1.1", "")
                        .replace("TLSv1", "")
                        .replace("TLSv1.1", "");
                Security.setProperty("jdk.tls.disabledAlgorithms", newDisabled);
                logger.info("已临时修改jdk.tls.disabledAlgorithms: {}", newDisabled);
            }
            
            // 方法2: 创建信任所有证书的SSLContext（仅测试环境）
            // createTrustAllSslContext();
            
            // 方法3: 使用特定的SSLContext
            enableTlsProtocols();
            
            initialized = true;
            logger.info("全局SSL配置完成");
            
        } catch (Exception e) {
            logger.error("全局SSL配置失败: {}", e.getMessage(), e);
        }
    }

    /**
     * 启用TLS协议
     */
    private static void enableTlsProtocols() throws NoSuchAlgorithmException, KeyManagementException {
        // 获取默认的SSLContext
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, null, new SecureRandom());
        
        // 设置默认的SSLContext（影响所有SSL连接）
        SSLContext.setDefault(sslContext);
        
        // 设置HttpsURLConnection使用的SSLContext
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        
        logger.info("已设置默认SSLContext，支持协议: {}", 
                String.join(", ", sslContext.getSupportedSSLParameters().getProtocols()));
    }

    /**
     * 创建信任所有证书的SSLContext（仅用于测试环境）
     * 警告：这会降低安全性，仅用于测试旧系统
     */
    @SuppressWarnings("unused")
    private static void createTrustAllSslContext() throws NoSuchAlgorithmException, KeyManagementException {
        // 创建信任所有证书的TrustManager
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        // 创建SSLContext
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());
        
        // 设置默认的SSLContext
        SSLContext.setDefault(sc);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        
        // 设置主机名验证器为接受所有
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        
        logger.warn("⚠️ 已启用信任所有证书的SSLContext（仅测试环境使用）");
    }

    /**
     * 获取当前SSL配置状态
     */
    public static String getSslConfigStatus() {
        try {
            SSLContext context = SSLContext.getDefault();
            SSLParameters params = context.getSupportedSSLParameters();
            return String.format("SSL协议: %s, 密码套件: %s",
                    String.join(", ", params.getProtocols()),
                    params.getCipherSuites().length);
        } catch (NoSuchAlgorithmException e) {
            return "无法获取SSL配置状态: " + e.getMessage();
        }
    }
}
