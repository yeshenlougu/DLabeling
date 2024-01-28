# 根据变量数量，创建 相应字段数据表
DROP TABLE IF EXISTS `generate`;
CREATE TABLE `generate`(
    `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT 'id编号',
    `database_type` INT NOT NULL COMMENT '数据库类型',
    `drop_table` VARCHAR(255) NOT NULL COMMENT '删库语句',
    `create_table` VARCHAR(255) NOT NULL COMMENT '建库语句',
    `create_field` VARCHAR(255) NOT NULL COMMENT '创建字段'
);

INSERT INTO `generate`(database_type, drop_table, create_table, fields) VALUE (1, "DROP TABLE IF EXISTS `?`", "CREATE TABLE `?`()");

CREATE TABLE `?`(
    `id` INT NOT NULL  AUTO_INCREMENT PRIMARY KEY COMMENT '数据编号',
    `data_path` VARCHAR(255) NOT NULL COMMENT '数据路径',
    `label_path` VARCHAR(255) NOT NULL COMMENT '标注路径',
    `?` VARCHAR(255)  COMMENT '标注的值',
    `?2` VARCHAR(255)   COMMENT '标注坐标',
    `?3` VARCHAR(255)   COMMENT '标注的值(模型)',
    `?4` VARCHAR(255)   COMMENT '标注的值(测试)'
);