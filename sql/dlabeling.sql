/*
 Navicat Premium Data Transfer

 Source Server         : DLabeling
 Source Server Type    : MySQL
 Source Server Version : 80200
 Source Host           : 127.0.0.1:3306
 Source Schema         : dlabeling

 Target Server Type    : MySQL
 Target Server Version : 80200
 File Encoding         : 65001

 Date: 08/04/2024 11:24:50
*/
DROP DATABASE IF EXISTS dlabeling;
CREATE DATABASE dlabeling;
USE dlabeling;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
-- ----------------------------
-- Table structure for data_split
-- ----------------------------
DROP TABLE IF EXISTS `data_split`;
CREATE TABLE `data_split`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID标识',
  `dataset_id` int NOT NULL COMMENT '数据集id',
  `data_id` int NOT NULL COMMENT '数据id',
  `split_id` int NOT NULL COMMENT '训练集、测试集id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ids`(`dataset_id` ASC, `data_id` ASC, `split_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for dataset_permission
-- ----------------------------
DROP TABLE IF EXISTS `dataset_permission`;
CREATE TABLE `dataset_permission`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id编号',
  `user_id` int NOT NULL COMMENT '用户编号',
  `dataset_id` int NOT NULL COMMENT '数据集编号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for datasets
-- ----------------------------
DROP TABLE IF EXISTS `datasets`;
CREATE TABLE `datasets`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '数据集唯一标识符',
  `name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT 'DATASET名称',
  `description` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '数据集描述',
  `type` int NOT NULL COMMENT '数据集类型   图片、文本、音频等   0图片, 1文本, 2音频...',
  `data_root_dir` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '数据存储的根目录  数据目录结构   -根目录 --子目录 ---data',
  `creator` int NOT NULL COMMENT '创建者ID',
  `create_time` datetime NOT NULL COMMENT '数据集创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '数据集修改时间',
  `destroy_time` datetime NULL DEFAULT NULL COMMENT '数据集删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name`(`name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for interface_address
-- ----------------------------
DROP TABLE IF EXISTS `interface_address`;
CREATE TABLE `interface_address`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id标识',
  `dataset_id` int NOT NULL COMMENT '数据集标识符',
  `interface_type` int NOT NULL COMMENT '接口所属类型，(0:自动标注,1:测试,2:查验)',
  `interface_name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '接口别名',
  `interface_address` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '接口地址',
  `create_time` datetime NOT NULL COMMENT '接口创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '接口修改时间',
  `destroy_time` datetime NULL DEFAULT NULL COMMENT '接口删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for interface_history
-- ----------------------------
DROP TABLE IF EXISTS `interface_history`;
CREATE TABLE `interface_history`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL,
  `interfaece_id` int NOT NULL,
  `dataset_id` int NOT NULL,
  `split_id` int NULL DEFAULT NULL,
  `type` int NOT NULL,
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `name_unique`(`name` ASC, `interfaece_id` ASC, `dataset_id` ASC, `split_id` ASC, `type` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for label_conf
-- ----------------------------
DROP TABLE IF EXISTS `label_conf`;
CREATE TABLE `label_conf`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id标识',
  `dataset_id` int NOT NULL,
  `label_name` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `label_type` int NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for label_history
-- ----------------------------
DROP TABLE IF EXISTS `label_history`;
CREATE TABLE `label_history`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id标识',
  `user_id` int NOT NULL COMMENT '操作者id',
  `dataset_id` int NOT NULL COMMENT '数据集id',
  `data_id` int NOT NULL COMMENT '被标注数据id',
  `action_type` int NOT NULL COMMENT 'action类型',
  `before_action` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `after_action` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '数据操作',
  `create_time` datetime NOT NULL COMMENT '标注时间',
  `destroy` tinyint NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_label_history`(`user_id` ASC, `dataset_id` ASC, `data_id` ASC, `create_time` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for level_apply
-- ----------------------------
DROP TABLE IF EXISTS `level_apply`;
CREATE TABLE `level_apply`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id标识',
  `applyer` int NOT NULL COMMENT '申请者id',
  `privilege` int NOT NULL COMMENT '申请权限',
  `type` int NOT NULL COMMENT '申请 0：用户权限 1：数据集权限',
  `status` int NOT NULL COMMENT '申请状态  A:申请中, B:已批准, C:已拒绝',
  `judger` int NULL DEFAULT NULL COMMENT '审批人id',
  `create_time` datetime NOT NULL COMMENT '用户信息创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '用户信息修改时间',
  `destroy_time` datetime NULL DEFAULT NULL COMMENT '用户信息删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_status`(`status` ASC, `applyer` ASC, `type` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for split
-- ----------------------------
DROP TABLE IF EXISTS `split`;
CREATE TABLE `split`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID标识',
  `dataset_id` int NOT NULL COMMENT '数据集id',
  `type` tinyint NOT NULL COMMENT '数据集分类类型',
  `name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '数据集分类名称',
  `create_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户唯一编号',
  `username` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `email` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户手机号',
  `password` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户密码',
  `create_time` datetime NOT NULL COMMENT '账号创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '密码修改时间',
  `destroy_time` datetime NULL DEFAULT NULL COMMENT '账号删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL COMMENT '用户唯一编号',
  `username` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户名',
  `email` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户手机号',
  `privilege` int NULL DEFAULT 0 COMMENT '用户权限 0管理员，1标注员,2训练员',
  `gender` int NULL DEFAULT 0 COMMENT '用户性别',
  `address` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户住址',
  `create_time` datetime NOT NULL COMMENT '用户信息创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '用户信息修改时间',
  `destroy_time` datetime NULL DEFAULT NULL COMMENT '用户信息删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci ROW_FORMAT = Dynamic;
INSERT INTO user(username,password, create_time) VALUE ("admin", "$2a$10$jPoxQHGIpCLbC20/7WILa.Zqt10MlX5Em6L3ipITe.pVASOy1WZ/C", CURRENT_TIMESTAMP);
INSERT INTO user_info(user_id, username, privilege, create_time) VALUE ((SELECT id FROM user WHERE user.username="admin"), "admin",0,CURRENT_TIMESTAMP);
# SET FOREIGN_KEY_CHECKS = 1;
