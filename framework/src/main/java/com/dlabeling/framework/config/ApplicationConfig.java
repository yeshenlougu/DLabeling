package com.dlabeling.framework.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.TimeZone;

/**
 * @Description: 程序注解配置
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2023/12/7
 */

@Configuration
// 表示通过aop框架暴露代理对象，AopContext能访问
@EnableAspectJAutoProxy(exposeProxy = true)
// 指定要扫描的Mapper类的包的路径
@MapperScan("com.dlabeling.**.mapper")
public class ApplicationConfig {
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization(){
        return jacksonObjectMapperCustomization -> jacksonObjectMapperCustomization.timeZone(TimeZone.getDefault());
    }
}
