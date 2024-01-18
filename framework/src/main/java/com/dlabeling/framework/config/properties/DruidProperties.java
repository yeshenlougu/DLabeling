package com.dlabeling.framework.config.properties;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: Druid连接池配置属性文件
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2023/12/6
 */

@Configuration
public class DruidProperties {

    /**
     * 初始连接数
     */
    @Value("${spring.datasource.druid.initialSize}")
    private int initialSize;

    @Value("${spring.datasource.druid.minIdle}")
    private int minIdle;

    @Value("${spring.datasource.druid.maxActive}")
    private int maxActivate;

    @Value("${spring.datasource.druid.maxWait}")
    private int maxWait;

    @Value("${spring.datasource.druid.socketTimeout}")
    private int socketTimeout;

    @Value("${spring.datasource.druid.connectTimeout}")
    private int connectTimeout;

    @Value("${spring.datasource.druid.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.druid.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;

    @Value("${spring.datasource.druid.maxEvictableIdleTimeMillis}")
    private int maxEvictableIdleTimeMillis;

    @Value("${spring.datasource.druid.validationQuery}")
    private String validationQuery;

    @Value("${spring.datasource.druid.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.druid.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.druid.testOnReturn}")
    private boolean testOnReturn;

    public DruidDataSource dataSource(DruidDataSource dataSource){

        /**
         * 配置初始化大小、最小、最大
         */
        dataSource.setInitialSize(initialSize);

        /**
         * 配置驱动连接超时时间，检查数据库连接超时时间，单位毫秒
         */
        dataSource.setMaxWait(maxWait);



        /**
         * 配置间隔多久才进行一次检测，检查需要关闭的空闲连接， 单位毫秒
         */
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);

        /**
         * 配置一个连接在池中最大、小生存的时间，单位毫秒
         */
        dataSource.setMaxEvictableIdleTimeMillis(maxEvictableIdleTimeMillis);
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);

        /**
         * 原来检测连接是否有效的sql，要求是一个查询语句，常用select 'x'。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle不起作用
         */
        dataSource.setValidationQuery(validationQuery);
        /**
         * 建议配置为True，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效
         */
        dataSource.setTestWhileIdle(testWhileIdle);
        /**
         *  申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
         */
        dataSource.setTestOnBorrow(testOnBorrow);
        /**
         * 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能
         */
        dataSource.setTestOnReturn(testOnReturn);
        /**
         * 配置网络超时时间，等待数据库操作完成的网络超时时间，单位是毫秒
         */
        dataSource.setSocketTimeout(socketTimeout);
        dataSource.setConnectTimeout(connectTimeout);

        dataSource.setMinIdle(minIdle);

        dataSource.setMaxActive(maxActivate);

        return dataSource;
    }

}
