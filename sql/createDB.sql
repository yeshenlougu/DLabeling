-- 创建数据库，定义字符
DROP DATABASE IF EXISTS DLabeling;
CREATE DATABASE DLabeling CHARACTER SET utf8 COLLATE utf8_general_ci;

-- 选择数据库
USE DLabeling;

-- 创建用户表
DROP TABLE IF EXISTS user;
CREATE TABLE `user`(
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户唯一编号',
    `username` VARCHAR(20) NOT NULL COMMENT '用户名',
    `email` VARCHAR(20) NOT NULL COMMENT '用户邮箱',
    `phone` VARCHAR(20) NOT NULL COMMENT '用户手机号',
    `password` VARCHAR(40) NOT NULL COMMENT '用户密码',
    `create_time` DATETIME NOT NULL COMMENT '账号创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '密码修改时间',
    `destroy_time` DATETIME DEFAULT NULL COMMENT '账号删除时间'
);

DROP TABLE IF EXISTS user_info;
CREATE TABLE `user_info`(
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '用户唯一编号',
    `username` VARCHAR(20) NOT NULL COMMENT '用户名',
    `email` VARCHAR(20) NOT NULL COMMENT '用户邮箱',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '用户手机号',
    `privilege` INT DEFAULT 0 COMMENT '用户权限 0标注员,1训练员,2管理员',
    `gender` INT DEFAULT 0 COMMENT '用户性别',
    `address` VARCHAR(40) DEFAULT NULL COMMENT '用户住址',
    `create_time` DATETIME NOT NULL COMMENT '用户信息创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '用户信息修改时间',
    `destroy_time` DATETIME DEFAULT NULL COMMENT '用户信息删除时间'
);

-- 所有数据集的表
DROP TABLE IF EXISTS datasets;
CREATE TABLE `datasets`(
    `id` INT AUTO_INCREMENT PRIMARY KEY COMMENT '数据集唯一标识符',
    `name` VARCHAR(20) NOT NULL UNIQUE COMMENT 'DATASET名称',
    `description` VARCHAR(40) DEFAULT NULL COMMENT '数据集描述',
    `type` INT NOT NULL COMMENT '数据集类型   图片、文本、音频等   0图片, 1文本, 2音频...',
    `data_root_dir` VARCHAR(40) DEFAULT NULL COMMENT '数据存储的根目录  数据目录结构   -根目录 --子目录 ---data',
    `visible` TINYINT(1) DEFAULT 1 NOT NULL COMMENT '数据集可见性， 标注师在不可见时不可见',
    `create_time` DATETIME NOT NULL COMMENT '用户信息创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '用户信息修改时间',
    `destroy_time` DATETIME DEFAULT NULL COMMENT '用户信息删除时间'
);

-- 数据集各类接口地址
DROP TABLE IF EXISTS interface_address;
CREATE TABLE `interface_address`(
	`id` INT AUTO_INCREMENT  PRIMARY KEY COMMENT 'id标识',
    `dataset_id` VARCHAR(40) NOT NULL COMMENT '数据集标识符',
    `dataset_name` VARCHAR(20) NOT NULL COMMENT 'DATASET名称',
    `interface_type` INT NOT NULL COMMENT '接口所属类型，(0:自动标注,1:识别,2:查验)',
    `interface_name` VARCHAR(20) NOT NULL COMMENT '接口别名',
    `interface_address` VARCHAR(40) NOT NULL COMMENT '接口地址',
    `create_time` DATETIME NOT NULL COMMENT '用户信息创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '用户信息修改时间',
    `destroy_time` DATETIME DEFAULT NULL COMMENT '用户信息删除时间'
);

-- 用户权限申请表
DROP TABLE IF EXISTS level_apply;
CREATE TABLE `level_apply`(
	`id` INT AUTO_INCREMENT  PRIMARY KEY COMMENT 'id标识',
    `applyer` VARCHAR(40) NOT NULL COMMENT '申请者id',
    `status` CHAR(1) NOT NULL COMMENT '申请状态  A:申请中, B:已批准, C:已拒绝',
    `judger` VARCHAR(40) DEFAULT NULL COMMENT '审批人id',
    `create_time` DATETIME NOT NULL COMMENT '用户信息创建时间',
    `update_time` DATETIME DEFAULT NULL COMMENT '用户信息修改时间',
    `destroy_time` DATETIME DEFAULT NULL COMMENT '用户信息删除时间',
		UNIQUE INDEX idx_status(`status`, `applyer`) USING BTREE
);

-- 用户标注记录
DROP TABLE IF EXISTS label_history;
CREATE TABLE `label_history`(
	`id` INT AUTO_INCREMENT  PRIMARY KEY COMMENT 'id标识',
    `user_id` VARCHAR(40) NOT NULL COMMENT '操作者id',
    `dataset_id` VARCHAR(40) NOT NULL COMMENT '数据集id',
    `data_id` VARCHAR(40) NOT NULL COMMENT '被标注数据id',
    `action` VARCHAR(500) NOT NULL COMMENT '数据操作',
    `create_time` DATETIME NOT NULL COMMENT '标注时间',
    `destroy` TINYINT DEFAULT 0,
    UNIQUE INDEX idx_label_history (`user_id`,`dataset_id`,`data_id`, `create_time`) USING BTREE
);
