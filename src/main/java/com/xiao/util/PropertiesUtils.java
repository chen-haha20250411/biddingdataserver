package com.xiao.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtils {
    private final static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

    private void PropertiesUtils(){

    }

    public static Properties getProperties(String locationPath){
        Properties props=null;
        try {
            logger.info("加载资源[{}]",locationPath);
            props= PropertiesLoaderUtils.loadProperties(new EncodedResource(new ClassPathResource(locationPath),"UTF-8"));
        } catch (IOException e) {
            logger.error("加载资源失败[{}]",locationPath);
            e.printStackTrace();
        }
        return props;
    }
}
