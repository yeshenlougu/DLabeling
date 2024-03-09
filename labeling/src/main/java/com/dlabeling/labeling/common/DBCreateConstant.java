package com.dlabeling.labeling.common;

import com.dlabeling.common.utils.StringUtils;

/**
 * @Description: 数据集库 建库语句
 * @Auther YYS
 * @Email 3223905473@qq.com
 * @Since 2024/1/24
 */
public class DBCreateConstant {


    public static final String DROP_DATABASE = "DROP DATABASE IF EXISTS `{}`;";

    public static final String CREATE_DATABASE = "CREATE DATABASE `{}`";

    public static final String USE_DATABASE = "USE DATABASE `{}`";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS `{}`;";

    public static final String CREATE_TABLE = "CREATE TABLE `{}`";

    public static final String ID_FIELD = "`id` INT NOT NULL  AUTO_INCREMENT PRIMARY KEY COMMENT '数据编号',";

    public static final String DATA_PATH_FIELD = "`data_path` VARCHAR(255) NOT NULL COMMENT '数据路径',";

    public static final String LABEL_PATH_FIELD = "`label_path` VARCHAR(255) NOT NULL COMMENT '标注路径',";

    public static final String LABEL_FIELD = "`{}` VARCHAR(255)  COMMENT '标注的值',\n" +
            "`{}` VARCHAR(255)   COMMENT '标注坐标',\n" +
            "`{}` VARCHAR(255)   COMMENT '标注的值(模型)',\n" +
            "`{}` VARCHAR(255)   COMMENT '标注的值(测试)'";

    public static final String SELECT_DATAS = "";

}
