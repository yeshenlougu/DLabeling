package com.dlabeling.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @Description:
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/14
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class }, scanBasePackages = {"com.dlabeling.*"})
public class DLabelingApplication {
    public static void main(String[] args) {
        SpringApplication.run(DLabelingApplication.class, args);
        StringBuilder sb = new StringBuilder();
        sb.append("\r\n 欢迎使用DLabeling标注工具 \r\n");
        System.out.println(sb.toString());
    }
}
