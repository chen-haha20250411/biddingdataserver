package com.xiao.config;

import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Properties;

/**
 * Created by guozp on 2017/8/28.
 *
 */
//@Component
@Configuration
public class TransactionAdviceConfig {
  @Autowired
  private DataSourceTransactionManager transactionManager;
  // 创建事务通知
  @Bean(name = "txAdvice")
  public TransactionInterceptor getAdvisor() throws Exception {
    Properties properties = new Properties();
    properties.setProperty("query*", "PROPAGATION_REQUIRED,-Exception,readOnly");
    properties.setProperty("select*", "PROPAGATION_REQUIRED,-Exception,readOnly");
    properties.setProperty("find*", "PROPAGATION_REQUIRED,-Exception,readOnly");
    properties.setProperty("excel*", "PROPAGATION_REQUIRED,-Exception,readOnly");
    properties.setProperty("get*", "PROPAGATION_REQUIRED,-Exception,readOnly");

    properties.setProperty("send*", "PROPAGATION_REQUIRED,-Exception");
    properties.setProperty("insert*", "PROPAGATION_REQUIRED,-Exception");
    properties.setProperty("update*", "PROPAGATION_REQUIRED,-Exception");
    properties.setProperty("delete*", "PROPAGATION_REQUIRED,-Exception");
    properties.setProperty("create*", "PROPAGATION_REQUIRED,-Exception");

    TransactionInterceptor tsi = new TransactionInterceptor(transactionManager,properties);
    return tsi;
  }
  @Bean
  public BeanNameAutoProxyCreator txProxy() {
    BeanNameAutoProxyCreator creator = new BeanNameAutoProxyCreator();
    creator.setInterceptorNames("txAdvice");
    creator.setBeanNames("*Service", "*ServiceImpl");
    creator.setProxyTargetClass(true);
    return creator;
  }
}
