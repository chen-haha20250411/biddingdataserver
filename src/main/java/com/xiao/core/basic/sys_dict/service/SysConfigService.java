package com.xiao.core.basic.sys_dict.service;

import com.xiao.core.basic.sys_dict.domain.Sys_dict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SysConfigService {

    @Autowired
    private Sys_dictService sysDictService;

    // 配置缓存
    private final Map<String, String> configCache = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        // 初始化时加载所有配置
        refreshConfig();
    }

    /**
     * 刷新配置缓存
     */
    public void refreshConfig() {
        try {
            // 使用queryByMap查询所有系统配置（dict_type='1'表示系统配置）
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("dict_type", "1");
            queryParams.put("dict_stat", "1"); // 只查询启用状态的配置
            
            List<Sys_dict> configList = sysDictService.queryByMap(queryParams);
            configCache.clear();
            
            for (Sys_dict config : configList) {
                if (config.getParam_name() != null && config.getDict_name() != null) {
                    configCache.put(config.getParam_name(), config.getDict_name());
                }
            }
            
            System.out.println("✅ 系统配置加载完成，共加载 " + configCache.size() + " 个配置项");
            if (!configCache.isEmpty()) {
                System.out.println("📋 配置项: " + String.join(", ", configCache.keySet()));
            }
        } catch (Exception e) {
            System.err.println("❌ 加载系统配置失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取配置值
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public String getConfig(String key, String defaultValue) {
        String value = configCache.get(key);
        return value != null ? value : defaultValue;
    }

    /**
     * 获取配置值
     * @param key 配置键
     * @return 配置值，不存在则返回null
     */
    public String getConfig(String key) {
        return configCache.get(key);
    }

    /**
     * 获取SQL Server相关配置
     */
    public Map<String, String> getSqlServerConfig() {
        Map<String, String> sqlServerConfig = new HashMap<>();
        
        // 从缓存中获取SQL Server配置
        String url = getConfig("sqlserver.url");
        String username = getConfig("sqlserver.username");
        String password = getConfig("sqlserver.password");
        
        sqlServerConfig.put("url", url);
        sqlServerConfig.put("username", username);
        sqlServerConfig.put("password", password);
        
        return sqlServerConfig;
    }

    /**
     * 检查SQL Server配置是否完整
     */
    public boolean isSqlServerConfigured() {
        Map<String, String> config = getSqlServerConfig();
        return config.get("url") != null && 
               config.get("username") != null && 
               config.get("password") != null;
    }

    /**
     * 获取所有配置
     */
    public Map<String, String> getAllConfig() {
        return new HashMap<>(configCache);
    }
}