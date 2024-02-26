# images-manage

#### 介绍
云盘java后端

#### 软件架构
软件架构说明
架构：k8s+nginx
后端：springboot+mysql5.7+mybatis+redis
#### 安装教程

1. 创建数据库表
``` shell
CREATE DATABASE image_manage
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
  
USE image_manage;

CREATE TABLE user (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    uuid VARCHAR(36) NOT NULL,
    level INT NOT NULL,
    thumbnail_avatar longblob
);
CREATE INDEX idx_username ON user (username);
INSERT INTO user (username, password, uuid, level)
VALUES ('admin', 'admin', 'admin', 9);


-- 创建文件夹表
CREATE TABLE folder (
    id INT AUTO_INCREMENT PRIMARY KEY,
    uuid CHAR(36) NOT NULL,
    folder_name VARCHAR(255) NOT NULL,
    path VARCHAR(255) NOT NULL,
    parent_folder_uuid CHAR(36),
    user_uuid CHAR(36) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE UNIQUE INDEX idx_useruuid_path ON folder(user_uuid,path)
CREATE INDEX idx_uuid ON folder (uuid);
-- 创建图片表
CREATE TABLE image (
    id INT AUTO_INCREMENT PRIMARY KEY,
    image_name VARCHAR(255) NOT NULL,
    folder_uuid CHAR(36) NOT NULL,
    user_uuid CHAR(36) NOT NULL,
    image_size VARCHAR(255) NOT NULL,
    thumbnail_data LONGBLOB NOT NULL,
    md5 varchar(255) NOT NULL,
    upload_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
-- 创建路径索引和id索引


CREATE INDEX idx_md5 ON image (md5);
CREATE INDEX idx_user_uuid_md5 ON image (user_uuid,md5);


-- 创建图片数据表
CREATE TABLE image_metadata (
    id INT AUTO_INCREMENT PRIMARY KEY,
    md5 VARCHAR(32) NOT NULL,
    aliyun_url VARCHAR(255),
    local_url VARCHAR(255)
    version INT NOT NULL DEFAULT 0

);
CREATE INDEX idx_md5 ON image_metadata (md5);
-- 创建配置表
CREATE TABLE configuration (
    id INT AUTO_INCREMENT PRIMARY KEY,
    config_key VARCHAR(255) NOT NULL,
    config_value VARCHAR(255) NOT NULL
);
-- 创建首页数据统计表
CREATE TABLE user_login_log (
    id INT AUTO_INCREMENT PRIMARY KEY,
    last_login_time TIMESTAMP NOT NULL,
    last_login_ip VARCHAR(15) NOT NULL,  -- 假设使用IPv4地址
    last_login_device VARCHAR(255),
    user_uuid VARCHAR(255) NOT NULL
);
CREATE INDEX idx_user_id ON user_login_log (user_uuid);

CREATE TABLE file_statistics (
    id INT AUTO_INCREMENT PRIMARY KEY,
    file_id INT NOT NULL,  
    reference_count INT NOT NULL,
    user_uuid VARCHAR(255) NOT NULL,
    date DATE NOT NULL
);
CREATE  INDEX idx_user_id ON file_statistics (user_uuid);
CREATE UNIQUE INDEX unique_file_id_user_uuid_data
ON file_statistics (file_id,user_uuid, date);

CREATE TABLE total_stats (
    id INT AUTO_INCREMENT PRIMARY KEY,
    total_upload INT NOT NULL,
    total_download INT NOT NULL,
    total_delete INT NOT NULL,
    user_uuid VARCHAR(255) NOT NULL,
    date DATE NOT NULL
);
CREATE  INDEX idx_user_id ON total_stats (user_uuid);
CREATE UNIQUE INDEX unique_user_uuid_data
ON total_stats (user_uuid, date);

CREATE TABLE device_statistics (
    id INT AUTO_INCREMENT PRIMARY KEY,
    windows INT DEFAULT 0,
    mac INT DEFAULT 0,
    linux INT DEFAULT 0,
    ios INT DEFAULT 0,
    android INT DEFAULT 0,
    other INT DEFAULT 0,
    user_uuid VARCHAR(255)
);
CREATE UNIQUE INDEX idx_user_id ON device_statistics (user_uuid);

CREATE TABLE direct_link_tokens (
    id INT PRIMARY KEY AUTO_INCREMENT,
    link_token VARCHAR(255) NOT NULL,
    user_uuid VARCHAR(255) NOT NULL,
    version INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE UNIQUE INDEX idx_unique_user_id ON direct_link_tokens (user_id);

-- 直链表
CREATE TABLE direct_link_tokens (
    id INT PRIMARY KEY AUTO_INCREMENT,
    link_token VARCHAR(255) NOT NULL,
	store_path VARCHAR(255) NOT NULL DEFAULT '/',  
    user_uuid VARCHAR(255) NOT NULL,
    version INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE UNIQUE INDEX idx_unique_link_user ON direct_link_tokens (link_token, user_uuid);

CREATE TABLE `logging_event`  (
  `timestmp` bigint(20) NOT NULL,
  `formatted_message` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `logger_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `level_string` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `thread_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `caller_filename` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `caller_class` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `caller_method` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `caller_line` char(4) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `event_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pod_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
PRIMARY KEY (`event_id`) USING BTREE

) ENGINE = InnoDB AUTO_INCREMENT = 1794 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;










```
2. xxxx
3. xxxx

#### 使用说明

1.  xxxx
2.  xxxx
3.  xxxx

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
