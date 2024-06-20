/*
 Navicat Premium Data Transfer

 Source Server         : 群晖dockermysql5.7
 Source Server Type    : MySQL
 Source Server Version : 50743
 Source Host           : 192.168.0.254:3306
 Source Schema         : image_manage

 Target Server Type    : MySQL
 Target Server Version : 50743
 File Encoding         : 65001

 Date: 20/06/2024 17:24:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for configuration
-- ----------------------------
DROP TABLE IF EXISTS `configuration`;
CREATE TABLE `configuration`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `config_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `config_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of configuration
-- ----------------------------

-- ----------------------------
-- Table structure for device_statistics
-- ----------------------------
DROP TABLE IF EXISTS `device_statistics`;
CREATE TABLE `device_statistics`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `windows` int(11) NULL DEFAULT 0,
  `mac` int(11) NULL DEFAULT 0,
  `linux` int(11) NULL DEFAULT 0,
  `ios` int(11) NULL DEFAULT 0,
  `android` int(11) NULL DEFAULT 0,
  `other` int(11) NULL DEFAULT 0,
  `user_uuid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_user_id`(`user_uuid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of device_statistics
-- ----------------------------
INSERT INTO `device_statistics` VALUES (2, 1, 0, 0, 0, 0, 0, 'admin');

-- ----------------------------
-- Table structure for direct_link_tokens
-- ----------------------------
DROP TABLE IF EXISTS `direct_link_tokens`;
CREATE TABLE `direct_link_tokens`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `link_token` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `store_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '/',
  `user_uuid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `version` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_unique_link_user`(`link_token`, `user_uuid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of direct_link_tokens
-- ----------------------------
INSERT INTO `direct_link_tokens` VALUES (8, '3c8895e0-8439-45d5-8847-9efb5cb1fc13', '/', 'admin', 0, '2024-06-20 08:44:22', '2024-06-20 08:44:22');

-- ----------------------------
-- Table structure for file_statistics
-- ----------------------------
DROP TABLE IF EXISTS `file_statistics`;
CREATE TABLE `file_statistics`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_id` int(11) NOT NULL,
  `reference_count` int(11) NOT NULL,
  `user_uuid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_file_id_user_uuid_data`(`file_id`, `user_uuid`, `date`) USING BTREE,
  INDEX `idx_user_id`(`user_uuid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of file_statistics
-- ----------------------------

-- ----------------------------
-- Table structure for folder
-- ----------------------------
DROP TABLE IF EXISTS `folder`;
CREATE TABLE `folder`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `folder_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `parent_folder_uuid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `user_uuid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_useruuid_path`(`user_uuid`, `path`) USING BTREE,
  INDEX `idx_uuid`(`uuid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of folder
-- ----------------------------

-- ----------------------------
-- Table structure for image
-- ----------------------------
DROP TABLE IF EXISTS `image`;
CREATE TABLE `image`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `image_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `folder_uuid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_uuid` char(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `image_size` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `thumbnail_data` longblob NOT NULL,
  `md5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `upload_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `last_modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_user_uuid_md5`(`user_uuid`, `md5`) USING BTREE,
  INDEX `idx_md5`(`md5`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 96 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of image
-- ----------------------------
INSERT INTO `image` VALUES (95, '微信截图_20240620164427.png', '/', 'admin', '155.43 KB', 0xFFD8FFE000104A46494600010200000100010000FFDB004300080606070605080707070909080A0C140D0C0B0B0C1912130F141D1A1F1E1D1A1C1C20242E2720222C231C1C2837292C30313434341F27393D38323C2E333432FFDB0043010909090C0B0C180D0D1832211C213232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232323232FFC00011080064006403012200021101031101FFC4001F0000010501010101010100000000000000000102030405060708090A0BFFC400B5100002010303020403050504040000017D01020300041105122131410613516107227114328191A1082342B1C11552D1F02433627282090A161718191A25262728292A3435363738393A434445464748494A535455565758595A636465666768696A737475767778797A838485868788898A92939495969798999AA2A3A4A5A6A7A8A9AAB2B3B4B5B6B7B8B9BAC2C3C4C5C6C7C8C9CAD2D3D4D5D6D7D8D9DAE1E2E3E4E5E6E7E8E9EAF1F2F3F4F5F6F7F8F9FAFFC4001F0100030101010101010101010000000000000102030405060708090A0BFFC400B51100020102040403040705040400010277000102031104052131061241510761711322328108144291A1B1C109233352F0156272D10A162434E125F11718191A262728292A35363738393A434445464748494A535455565758595A636465666768696A737475767778797A82838485868788898A92939495969798999AA2A3A4A5A6A7A8A9AAB2B3B4B5B6B7B8B9BAC2C3C4C5C6C7C8C9CAD2D3D4D5D6D7D8D9DAE2E3E4E5E6E7E8E9EAF2F3F4F5F6F7F8F9FAFFDA000C03010002110311003F00F3E37D3E38BCB9CE3FE7A1F4FAD5DFB5DA6D3FF134D4B7638E3BFF00DF55F49DEC17515DB25AE876924200DAE634E78E7B8A7E996D34F34AB7FA2DA411A81B19514EE3DF8C9AD5D27CBCD75F799AAAB9B96CFEE3E58FB7DE7FCFDCFF00F7F0D1F6FBCFF9FB9FFEFE1AFAEBFB36C7FE7CADBFEFD2FF00851FD9B63FF3E56DFF007E97FC2B2343E45FB7DE7FCFDCFF00F7F0D1F6FBCFF9FB9FFEFE1AFAEBFB36C7FE7CADBFEFD2FF00851FD9B63FF3E56DFF007E97FC2803E45FB7DE7FCFDCFF00F7F0D1F6FBCFF9FB9FFEFE1AFAEBFB36C7FE7CADBFEFD2FF00851FD9B63FF3E56DFF007E97FC2803E45FB7DE7FCFDCFF00F7F0D1F6FBCFF9FB9FFEFE1AFAEBFB36C7FE7CADBFEFD2FF00851FD9B63FF3E56DFF007E97FC2803E45FB7DE7FCFDCFF00F7F0D1F6FBCFF9FB9FFEFE1AFAEBFB36C7FE7CADBFEFD2FF00851FD9B63FF3E56DFF007E97FC2803E45FB7DE7FCFDCFF00F7F0D15F5D7F66D8FF00CF95B7FDFA5FF0A2802C7273CFE94B86F5FD285EFF005A5A004C37AFE94CF35319F353192339EE289A4F2A167CA023A1738151DB4C1C63F72B9C9DB1BEEFC69DB4B8AFAD893CD4FF009EC9F98A3CD4FF009EC9F98A928A4323F353FE7B27E628F353FE7B27E62A4A86E6EA2B588BCB2C71E7852ED804D00352EE092592359D4BC7F7C6318A93CD4FF9EC9F98AC8B4D4BFD23322E9B11908DCF15C6E67186ED8073C0FCCFA56CC7224B1AC91B064619041E08A006F9A9FF003D93F31479A9FF003D93F3152514011F9A9FF3D93F314549450020EFF5A5A45EFF005A5A004233D71400074007E15997767AA4B7DE65BEAE2DEDF03F73F67563D0FF0011F7C7E5562D62BD8A4CDCDF473A608C2C3B0E78E73B8FBFE7401728A320F7A2800A6B22B8C32861EE334EA2801BE5A631B171F4A5030303005191EA29720F7A0028A28A0028A28A0045EFF5A5A45EFF005A5A00CAD626B54458EE6FDED0BFDD647DAC70474E3E9F9D57B5D355BCBB88B55BF9632430CCC0AB01F87B56D3C51C9C3A061E86811200005C01D853B815E631881FCD9CC09DDC36DC7E354DEE2C94E1B57932C3AACA0FE5C7B1AD508A3B5218A32725149F5C52033A436BB8AB6A7382BC1C498C73F4F6AB515F5ACC3CB8AE15C85CF5E718EB5604683A2A8FC2904518C611463DA8031A49EC659032EACE9B864049060FE9EE3F4AD5118961F965215C021D0F38EB5208A30721173F414FA00A6B61B411F6BBA39F593FFAD4EFB11C63ED573D739F33FF00AD56A8A00A4FA7976CFDB2EC7B0931FD28ABB450020EFF005A5A6820672475A5DC3D4500078A68624E3691C67269DB87A8A370F51401CC6BDE28B8D275236B1DA248A230FB989E49EDC54FE1EF10CFACDCCF14B6E9188D0306527927B735D06E1EA28DC3D4574BAB4BD9F2A86BDEE73AA553DA7373E9DAC2D149B87A8A370F515CC740B4526E1EA28DC3D45002D149B87A8A370F51400B4526E1EA28A005C5181E9451400607A5181E9451400607A5181E9451400607A5181E9451400607A5181E9451400607A5181E9451400607A5145140051451400514514005145140051451400514514005145140051451401FFFD9, '2b2e5c8d74041ed552e537f726ff5d8d', '2024-06-20 08:44:44', '2024-06-20 08:44:44');

-- ----------------------------
-- Table structure for image_metadata
-- ----------------------------
DROP TABLE IF EXISTS `image_metadata`;
CREATE TABLE `image_metadata`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `md5` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `aliyun_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `local_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `version` int(11) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_md5`(`md5`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of image_metadata
-- ----------------------------
INSERT INTO `image_metadata` VALUES (30, '2b2e5c8d74041ed552e537f726ff5d8d', NULL, 'web-0', 0);

-- ----------------------------
-- Table structure for logging_event
-- ----------------------------
DROP TABLE IF EXISTS `logging_event`;
CREATE TABLE `logging_event`  (
  `timestamp` bigint(20) NOT NULL,
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
) ENGINE = InnoDB AUTO_INCREMENT = 8830 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of logging_event
-- ----------------------------
INSERT INTO `logging_event` VALUES (1718873006837, '通过接口防刷拦截器', 'com.chen.behindimagesmanage.Interceptor.AccessLimitInterceptor', 'INFO', 'http-nio-8080-exec-9', 'AccessLimitInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.AccessLimitInterceptor', 'preHandle', '94', 8723, 'web-0');
INSERT INTO `logging_event` VALUES (1718873007165, 'Controller method LoginController.login is invoked.', 'com.chen.behindimagesmanage.aop.ControllerLoggerAspect', 'INFO', 'http-nio-8080-exec-9', 'ControllerLoggerAspect.java', 'com.chen.behindimagesmanage.aop.ControllerLoggerAspect', 'logController', '22', 8724, 'web-0');
INSERT INTO `logging_event` VALUES (1718873007513, '登录接口被请求，用户：User(id=0, username=admin, password=admin, uuid=null, level=0, thumbnailAvatar=null)', 'com.chen.behindimagesmanage.controller.LoginController', 'INFO', 'http-nio-8080-exec-9', 'LoginController.java', 'com.chen.behindimagesmanage.controller.LoginController', 'login', '29', 8725, 'web-0');
INSERT INTO `logging_event` VALUES (1718873007959, '当前登录用户UserInformation(token=Bearer 170e3f22-d7a8-4387-be19-cf35cfd07720)', 'com.chen.behindimagesmanage.service.LoginService', 'INFO', 'http-nio-8080-exec-9', 'LoginService.java', 'com.chen.behindimagesmanage.service.LoginService', 'checkPass', '35', 8726, 'web-0');
INSERT INTO `logging_event` VALUES (1718873008521, '成功返回:UserInformation(token=Bearer 170e3f22-d7a8-4387-be19-cf35cfd07720)', 'com.chen.behindimagesmanage.util.ApiResponse', 'INFO', 'http-nio-8080-exec-9', 'ApiResponse.java', 'com.chen.behindimagesmanage.util.ApiResponse', 'success', '54', 8727, 'web-0');
INSERT INTO `logging_event` VALUES (1718873008968, '通过接口防刷拦截器', 'com.chen.behindimagesmanage.Interceptor.AccessLimitInterceptor', 'INFO', 'http-nio-8080-exec-2', 'AccessLimitInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.AccessLimitInterceptor', 'preHandle', '94', 8728, 'web-0');
INSERT INTO `logging_event` VALUES (1718873008968, '通过接口防刷拦截器', 'com.chen.behindimagesmanage.Interceptor.AccessLimitInterceptor', 'INFO', 'http-nio-8080-exec-4', 'AccessLimitInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.AccessLimitInterceptor', 'preHandle', '94', 8729, 'web-0');
INSERT INTO `logging_event` VALUES (1718873009291, 'Bearer 170e3f22-d7a8-4387-be19-cf35cfd07720', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'INFO', 'http-nio-8080-exec-2', 'TokenValidationInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'isValidToken', '50', 8730, 'web-0');
INSERT INTO `logging_event` VALUES (1718873009389, 'Bearer 170e3f22-d7a8-4387-be19-cf35cfd07720', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'INFO', 'http-nio-8080-exec-4', 'TokenValidationInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'isValidToken', '50', 8731, 'web-0');
INSERT INTO `logging_event` VALUES (1718873009757, 'Token验证通过', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'INFO', 'http-nio-8080-exec-2', 'TokenValidationInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'preHandle', '34', 8732, 'web-0');
INSERT INTO `logging_event` VALUES (1718873009869, 'Token验证通过', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'INFO', 'http-nio-8080-exec-4', 'TokenValidationInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'preHandle', '34', 8733, 'web-0');
INSERT INTO `logging_event` VALUES (1718873010185, 'Controller method AdminController.isClusterModeEnabled is invoked.', 'com.chen.behindimagesmanage.aop.ControllerLoggerAspect', 'INFO', 'http-nio-8080-exec-2', 'ControllerLoggerAspect.java', 'com.chen.behindimagesmanage.aop.ControllerLoggerAspect', 'logController', '22', 8734, 'web-0');
INSERT INTO `logging_event` VALUES (1718873010297, 'Controller method UserController.getHomeData is invoked.', 'com.chen.behindimagesmanage.aop.ControllerLoggerAspect', 'INFO', 'http-nio-8080-exec-4', 'ControllerLoggerAspect.java', 'com.chen.behindimagesmanage.aop.ControllerLoggerAspect', 'logController', '22', 8735, 'web-0');
INSERT INTO `logging_event` VALUES (1718873010755, '成功返回:{isClusterModeEnabled=false}', 'com.chen.behindimagesmanage.util.ApiResponse', 'INFO', 'http-nio-8080-exec-2', 'ApiResponse.java', 'com.chen.behindimagesmanage.util.ApiResponse', 'success', '54', 8736, 'web-0');
INSERT INTO `logging_event` VALUES (1718873011008, '成功返回:{totalStats=null, sevenTotalStats=[], deviceStatistics=DeviceStatistics(id=2, windows=1, mac=0, linux=0, ios=0, android=0, other=0, userUuid=admin), newFileStatistics=[], userLoginLog=UserLoginLog(id=2, lastLoginTime=Thu Jun 20 08:43:28 CST 2024, lastLoginIp=0:0:0:0:0:0:0:1, lastLoginDevice=windows,', 'com.chen.behindimagesmanage.util.ApiResponse', 'INFO', 'http-nio-8080-exec-4', 'ApiResponse.java', 'com.chen.behindimagesmanage.util.ApiResponse', 'success', '54', 8737, 'web-0');
INSERT INTO `logging_event` VALUES (1718873020025, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8738, 'web-0');
INSERT INTO `logging_event` VALUES (1718873036218, '通过接口防刷拦截器', 'com.chen.behindimagesmanage.Interceptor.AccessLimitInterceptor', 'INFO', 'http-nio-8080-exec-7', 'AccessLimitInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.AccessLimitInterceptor', 'preHandle', '94', 8739, 'web-0');
INSERT INTO `logging_event` VALUES (1718873036218, '通过接口防刷拦截器', 'com.chen.behindimagesmanage.Interceptor.AccessLimitInterceptor', 'INFO', 'http-nio-8080-exec-6', 'AccessLimitInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.AccessLimitInterceptor', 'preHandle', '94', 8740, 'web-0');
INSERT INTO `logging_event` VALUES (1718873036551, 'Bearer 170e3f22-d7a8-4387-be19-cf35cfd07720', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'INFO', 'http-nio-8080-exec-7', 'TokenValidationInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'isValidToken', '50', 8741, 'web-0');
INSERT INTO `logging_event` VALUES (1718873036611, 'Bearer 170e3f22-d7a8-4387-be19-cf35cfd07720', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'INFO', 'http-nio-8080-exec-6', 'TokenValidationInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'isValidToken', '50', 8742, 'web-0');
INSERT INTO `logging_event` VALUES (1718873037034, 'Token验证通过', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'INFO', 'http-nio-8080-exec-7', 'TokenValidationInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'preHandle', '34', 8743, 'web-0');
INSERT INTO `logging_event` VALUES (1718873037109, 'Token验证通过', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'INFO', 'http-nio-8080-exec-6', 'TokenValidationInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'preHandle', '34', 8744, 'web-0');
INSERT INTO `logging_event` VALUES (1718873037452, 'Controller method AdminController.isClusterModeEnabled is invoked.', 'com.chen.behindimagesmanage.aop.ControllerLoggerAspect', 'INFO', 'http-nio-8080-exec-7', 'ControllerLoggerAspect.java', 'com.chen.behindimagesmanage.aop.ControllerLoggerAspect', 'logController', '22', 8745, 'web-0');
INSERT INTO `logging_event` VALUES (1718873037534, 'Controller method UserController.getHomeData is invoked.', 'com.chen.behindimagesmanage.aop.ControllerLoggerAspect', 'INFO', 'http-nio-8080-exec-6', 'ControllerLoggerAspect.java', 'com.chen.behindimagesmanage.aop.ControllerLoggerAspect', 'logController', '22', 8746, 'web-0');
INSERT INTO `logging_event` VALUES (1718873037774, '成功返回:{isClusterModeEnabled=false}', 'com.chen.behindimagesmanage.util.ApiResponse', 'INFO', 'http-nio-8080-exec-7', 'ApiResponse.java', 'com.chen.behindimagesmanage.util.ApiResponse', 'success', '54', 8747, 'web-0');
INSERT INTO `logging_event` VALUES (1718873038014, '成功返回:{totalStats=null, sevenTotalStats=[], deviceStatistics=DeviceStatistics(id=2, windows=1, mac=0, linux=0, ios=0, android=0, other=0, userUuid=admin), newFileStatistics=[], userLoginLog=UserLoginLog(id=2, lastLoginTime=Thu Jun 20 08:43:28 CST 2024, lastLoginIp=0:0:0:0:0:0:0:1, lastLoginDevice=windows,', 'com.chen.behindimagesmanage.util.ApiResponse', 'INFO', 'http-nio-8080-exec-6', 'ApiResponse.java', 'com.chen.behindimagesmanage.util.ApiResponse', 'success', '54', 8748, 'web-0');
INSERT INTO `logging_event` VALUES (1718873056259, '通过接口防刷拦截器', 'com.chen.behindimagesmanage.Interceptor.AccessLimitInterceptor', 'INFO', 'http-nio-8080-exec-9', 'AccessLimitInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.AccessLimitInterceptor', 'preHandle', '94', 8749, 'web-0');
INSERT INTO `logging_event` VALUES (1718873056559, 'Bearer 170e3f22-d7a8-4387-be19-cf35cfd07720', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'INFO', 'http-nio-8080-exec-9', 'TokenValidationInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'isValidToken', '50', 8750, 'web-0');
INSERT INTO `logging_event` VALUES (1718873056947, 'Token验证通过', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'INFO', 'http-nio-8080-exec-9', 'TokenValidationInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'preHandle', '34', 8751, 'web-0');
INSERT INTO `logging_event` VALUES (1718873057285, 'Controller method UserController.getPageFiles is invoked.', 'com.chen.behindimagesmanage.aop.ControllerLoggerAspect', 'INFO', 'http-nio-8080-exec-9', 'ControllerLoggerAspect.java', 'com.chen.behindimagesmanage.aop.ControllerLoggerAspect', 'logController', '22', 8752, 'web-0');
INSERT INTO `logging_event` VALUES (1718873057586, 'admin请求获取/文件', 'com.chen.behindimagesmanage.controller.UserController', 'INFO', 'http-nio-8080-exec-9', 'UserController.java', 'com.chen.behindimagesmanage.controller.UserController', 'getPageFiles', '54', 8753, 'web-0');
INSERT INTO `logging_event` VALUES (1718873058025, '成功返回:PageFiles(folderList=[], imageList=[])', 'com.chen.behindimagesmanage.util.ApiResponse', 'INFO', 'http-nio-8080-exec-9', 'ApiResponse.java', 'com.chen.behindimagesmanage.util.ApiResponse', 'success', '54', 8754, 'web-0');
INSERT INTO `logging_event` VALUES (1718873060631, '通过接口防刷拦截器', 'com.chen.behindimagesmanage.Interceptor.AccessLimitInterceptor', 'INFO', 'http-nio-8080-exec-1', 'AccessLimitInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.AccessLimitInterceptor', 'preHandle', '94', 8755, 'web-0');
INSERT INTO `logging_event` VALUES (1718873060919, 'Bearer 170e3f22-d7a8-4387-be19-cf35cfd07720', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'INFO', 'http-nio-8080-exec-1', 'TokenValidationInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'isValidToken', '50', 8756, 'web-0');
INSERT INTO `logging_event` VALUES (1718873061255, 'Token验证通过', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'INFO', 'http-nio-8080-exec-1', 'TokenValidationInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'preHandle', '34', 8757, 'web-0');
INSERT INTO `logging_event` VALUES (1718873061871, 'Controller method UserController.getPicgoToken is invoked.', 'com.chen.behindimagesmanage.aop.ControllerLoggerAspect', 'INFO', 'http-nio-8080-exec-1', 'ControllerLoggerAspect.java', 'com.chen.behindimagesmanage.aop.ControllerLoggerAspect', 'logController', '22', 8758, 'web-0');
INSERT INTO `logging_event` VALUES (1718873062478, '成功返回:DirectLinkToken(id=8, linkToken=3c8895e0-8439-45d5-8847-9efb5cb1fc13, storePath=/, userUuid=admin, version=0, createdAt=2024-06-20 08:44:22.0, updatedAt=2024-06-20 08:44:22.0)', 'com.chen.behindimagesmanage.util.ApiResponse', 'INFO', 'http-nio-8080-exec-1', 'ApiResponse.java', 'com.chen.behindimagesmanage.util.ApiResponse', 'success', '54', 8759, 'web-0');
INSERT INTO `logging_event` VALUES (1718873069768, '通过接口防刷拦截器', 'com.chen.behindimagesmanage.Interceptor.AccessLimitInterceptor', 'INFO', 'http-nio-8080-exec-4', 'AccessLimitInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.AccessLimitInterceptor', 'preHandle', '94', 8760, 'web-0');
INSERT INTO `logging_event` VALUES (1718873070068, 'Bearer 170e3f22-d7a8-4387-be19-cf35cfd07720', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'INFO', 'http-nio-8080-exec-4', 'TokenValidationInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'isValidToken', '50', 8761, 'web-0');
INSERT INTO `logging_event` VALUES (1718873070428, 'Token验证通过', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'INFO', 'http-nio-8080-exec-4', 'TokenValidationInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'preHandle', '34', 8762, 'web-0');
INSERT INTO `logging_event` VALUES (1718873070779, 'Controller method UserController.getPageFiles is invoked.', 'com.chen.behindimagesmanage.aop.ControllerLoggerAspect', 'INFO', 'http-nio-8080-exec-4', 'ControllerLoggerAspect.java', 'com.chen.behindimagesmanage.aop.ControllerLoggerAspect', 'logController', '22', 8763, 'web-0');
INSERT INTO `logging_event` VALUES (1718873071108, 'admin请求获取/文件', 'com.chen.behindimagesmanage.controller.UserController', 'INFO', 'http-nio-8080-exec-4', 'UserController.java', 'com.chen.behindimagesmanage.controller.UserController', 'getPageFiles', '54', 8764, 'web-0');
INSERT INTO `logging_event` VALUES (1718873071666, '成功返回:PageFiles(folderList=[], imageList=[])', 'com.chen.behindimagesmanage.util.ApiResponse', 'INFO', 'http-nio-8080-exec-4', 'ApiResponse.java', 'com.chen.behindimagesmanage.util.ApiResponse', 'success', '54', 8765, 'web-0');
INSERT INTO `logging_event` VALUES (1718873080007, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8766, 'web-0');
INSERT INTO `logging_event` VALUES (1718873082336, '通过接口防刷拦截器', 'com.chen.behindimagesmanage.Interceptor.AccessLimitInterceptor', 'INFO', 'http-nio-8080-exec-5', 'AccessLimitInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.AccessLimitInterceptor', 'preHandle', '94', 8767, 'web-0');
INSERT INTO `logging_event` VALUES (1718873082649, 'Bearer 170e3f22-d7a8-4387-be19-cf35cfd07720', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'INFO', 'http-nio-8080-exec-5', 'TokenValidationInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'isValidToken', '50', 8768, 'web-0');
INSERT INTO `logging_event` VALUES (1718873083097, 'Token验证通过', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'INFO', 'http-nio-8080-exec-5', 'TokenValidationInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'preHandle', '34', 8769, 'web-0');
INSERT INTO `logging_event` VALUES (1718873083495, 'Controller method UserController.uploadImg is invoked.', 'com.chen.behindimagesmanage.aop.ControllerLoggerAspect', 'INFO', 'http-nio-8080-exec-5', 'ControllerLoggerAspect.java', 'com.chen.behindimagesmanage.aop.ControllerLoggerAspect', 'logController', '22', 8770, 'web-0');
INSERT INTO `logging_event` VALUES (1718873083877, 'admin用户发送了文件路径：/文件:=>org.springframework.web.multipart.support.StandardMultipartHttpServletRequest$StandardMultipartFile@16337ed6', 'com.chen.behindimagesmanage.controller.UserController', 'INFO', 'http-nio-8080-exec-5', 'UserController.java', 'com.chen.behindimagesmanage.controller.UserController', 'uploadImg', '44', 8771, 'web-0');
INSERT INTO `logging_event` VALUES (1718873084280, '删除adminpath/true', 'com.chen.behindimagesmanage.service.RedisService', 'INFO', 'http-nio-8080-exec-5', 'RedisService.java', 'com.chen.behindimagesmanage.service.RedisService', 'clearPageFiles', '82', 8772, 'web-0');
INSERT INTO `logging_event` VALUES (1718873085192, '成功返回:上传成功', 'com.chen.behindimagesmanage.util.ApiResponse', 'INFO', 'http-nio-8080-exec-5', 'ApiResponse.java', 'com.chen.behindimagesmanage.util.ApiResponse', 'success', '54', 8773, 'web-0');
INSERT INTO `logging_event` VALUES (1718873085620, '通过接口防刷拦截器', 'com.chen.behindimagesmanage.Interceptor.AccessLimitInterceptor', 'INFO', 'http-nio-8080-exec-6', 'AccessLimitInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.AccessLimitInterceptor', 'preHandle', '94', 8774, 'web-0');
INSERT INTO `logging_event` VALUES (1718873085955, 'Bearer 170e3f22-d7a8-4387-be19-cf35cfd07720', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'INFO', 'http-nio-8080-exec-6', 'TokenValidationInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'isValidToken', '50', 8775, 'web-0');
INSERT INTO `logging_event` VALUES (1718873086570, 'Token验证通过', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'INFO', 'http-nio-8080-exec-6', 'TokenValidationInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'preHandle', '34', 8776, 'web-0');
INSERT INTO `logging_event` VALUES (1718873087025, 'Controller method UserController.getPageFiles is invoked.', 'com.chen.behindimagesmanage.aop.ControllerLoggerAspect', 'INFO', 'http-nio-8080-exec-6', 'ControllerLoggerAspect.java', 'com.chen.behindimagesmanage.aop.ControllerLoggerAspect', 'logController', '22', 8777, 'web-0');
INSERT INTO `logging_event` VALUES (1718873087379, 'admin请求获取/文件', 'com.chen.behindimagesmanage.controller.UserController', 'INFO', 'http-nio-8080-exec-6', 'UserController.java', 'com.chen.behindimagesmanage.controller.UserController', 'getPageFiles', '54', 8778, 'web-0');
INSERT INTO `logging_event` VALUES (1718873088171, '成功返回:PageFiles(folderList=[], imageList=[Image{id=95, imageName=\'微信截图_20240620164427.png\', folderUuid=\'/\', userUuid=\'admin\', imageSize=\'155.43 KB\', md5=\'2b2e5c8d74041ed552e537f726ff5d8d\', uploadTime=2024-06-20 08:44:44.0, lastModifiedAt=2024-06-20 08:44:44.0}])', 'com.chen.behindimagesmanage.util.ApiResponse', 'INFO', 'http-nio-8080-exec-6', 'ApiResponse.java', 'com.chen.behindimagesmanage.util.ApiResponse', 'success', '54', 8779, 'web-0');
INSERT INTO `logging_event` VALUES (1718873098765, '通过接口防刷拦截器', 'com.chen.behindimagesmanage.Interceptor.AccessLimitInterceptor', 'INFO', 'http-nio-8080-exec-9', 'AccessLimitInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.AccessLimitInterceptor', 'preHandle', '94', 8780, 'web-0');
INSERT INTO `logging_event` VALUES (1718873099125, 'Bearer 170e3f22-d7a8-4387-be19-cf35cfd07720', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'INFO', 'http-nio-8080-exec-9', 'TokenValidationInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'isValidToken', '50', 8781, 'web-0');
INSERT INTO `logging_event` VALUES (1718873099753, 'Token验证通过', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'INFO', 'http-nio-8080-exec-9', 'TokenValidationInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'preHandle', '34', 8782, 'web-0');
INSERT INTO `logging_event` VALUES (1718873100136, 'Controller method UserController.getImg is invoked.', 'com.chen.behindimagesmanage.aop.ControllerLoggerAspect', 'INFO', 'http-nio-8080-exec-9', 'ControllerLoggerAspect.java', 'com.chen.behindimagesmanage.aop.ControllerLoggerAspect', 'logController', '22', 8783, 'web-0');
INSERT INTO `logging_event` VALUES (1718873100489, 'admin请求获取2b2e5c8d74041ed552e537f726ff5d8d.png文件', 'com.chen.behindimagesmanage.controller.UserController', 'INFO', 'http-nio-8080-exec-9', 'UserController.java', 'com.chen.behindimagesmanage.controller.UserController', 'getImg', '59', 8784, 'web-0');
INSERT INTO `logging_event` VALUES (1718873113194, '通过接口防刷拦截器', 'com.chen.behindimagesmanage.Interceptor.AccessLimitInterceptor', 'INFO', 'http-nio-8080-exec-1', 'AccessLimitInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.AccessLimitInterceptor', 'preHandle', '94', 8785, 'web-0');
INSERT INTO `logging_event` VALUES (1718873113515, 'Bearer 170e3f22-d7a8-4387-be19-cf35cfd07720', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'INFO', 'http-nio-8080-exec-1', 'TokenValidationInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'isValidToken', '50', 8786, 'web-0');
INSERT INTO `logging_event` VALUES (1718873113862, 'Token验证通过', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'INFO', 'http-nio-8080-exec-1', 'TokenValidationInterceptor.java', 'com.chen.behindimagesmanage.Interceptor.TokenValidationInterceptor', 'preHandle', '34', 8787, 'web-0');
INSERT INTO `logging_event` VALUES (1718873114336, 'Controller method AdminController.getAllUsers is invoked.', 'com.chen.behindimagesmanage.aop.ControllerLoggerAspect', 'INFO', 'http-nio-8080-exec-1', 'ControllerLoggerAspect.java', 'com.chen.behindimagesmanage.aop.ControllerLoggerAspect', 'logController', '22', 8788, 'web-0');
INSERT INTO `logging_event` VALUES (1718873114738, '成功返回:[SimplyUser(id=17, uuid=admin, username=admin, level=9, thumbnailAvatar=null, lastLoginTime=Thu Jun 20 08:43:28 CST 2024)]', 'com.chen.behindimagesmanage.util.ApiResponse', 'INFO', 'http-nio-8080-exec-1', 'ApiResponse.java', 'com.chen.behindimagesmanage.util.ApiResponse', 'success', '54', 8789, 'web-0');
INSERT INTO `logging_event` VALUES (1718873140005, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8790, 'web-0');
INSERT INTO `logging_event` VALUES (1718873200080, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8791, 'web-0');
INSERT INTO `logging_event` VALUES (1718873260016, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8792, 'web-0');
INSERT INTO `logging_event` VALUES (1718873320073, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8793, 'web-0');
INSERT INTO `logging_event` VALUES (1718873380023, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8794, 'web-0');
INSERT INTO `logging_event` VALUES (1718873440017, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8795, 'web-0');
INSERT INTO `logging_event` VALUES (1718873500021, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8796, 'web-0');
INSERT INTO `logging_event` VALUES (1718873560028, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8797, 'web-0');
INSERT INTO `logging_event` VALUES (1718873620022, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8798, 'web-0');
INSERT INTO `logging_event` VALUES (1718873680082, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8799, 'web-0');
INSERT INTO `logging_event` VALUES (1718873740025, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8800, 'web-0');
INSERT INTO `logging_event` VALUES (1718873800066, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8801, 'web-0');
INSERT INTO `logging_event` VALUES (1718873860008, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8802, 'web-0');
INSERT INTO `logging_event` VALUES (1718873920087, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8803, 'web-0');
INSERT INTO `logging_event` VALUES (1718873980010, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8804, 'web-0');
INSERT INTO `logging_event` VALUES (1718874040060, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8805, 'web-0');
INSERT INTO `logging_event` VALUES (1718874100021, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8806, 'web-0');
INSERT INTO `logging_event` VALUES (1718874160067, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8807, 'web-0');
INSERT INTO `logging_event` VALUES (1718874220013, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8808, 'web-0');
INSERT INTO `logging_event` VALUES (1718874280069, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8809, 'web-0');
INSERT INTO `logging_event` VALUES (1718874340007, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8810, 'web-0');
INSERT INTO `logging_event` VALUES (1718874400019, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8811, 'web-0');
INSERT INTO `logging_event` VALUES (1718874460011, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8812, 'web-0');
INSERT INTO `logging_event` VALUES (1718874520021, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8813, 'web-0');
INSERT INTO `logging_event` VALUES (1718874580014, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8814, 'web-0');
INSERT INTO `logging_event` VALUES (1718874640106, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8815, 'web-0');
INSERT INTO `logging_event` VALUES (1718874700025, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8816, 'web-0');
INSERT INTO `logging_event` VALUES (1718874760024, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8817, 'web-0');
INSERT INTO `logging_event` VALUES (1718874820015, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8818, 'web-0');
INSERT INTO `logging_event` VALUES (1718874880107, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8819, 'web-0');
INSERT INTO `logging_event` VALUES (1718874940025, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8820, 'web-0');
INSERT INTO `logging_event` VALUES (1718875000012, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8821, 'web-0');
INSERT INTO `logging_event` VALUES (1718875060012, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8822, 'web-0');
INSERT INTO `logging_event` VALUES (1718875120023, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8823, 'web-0');
INSERT INTO `logging_event` VALUES (1718875180023, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8824, 'web-0');
INSERT INTO `logging_event` VALUES (1718875240015, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8825, 'web-0');
INSERT INTO `logging_event` VALUES (1718875300027, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8826, 'web-0');
INSERT INTO `logging_event` VALUES (1718875360012, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8827, 'web-0');
INSERT INTO `logging_event` VALUES (1718875420017, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8828, 'web-0');
INSERT INTO `logging_event` VALUES (1718875480105, 'Unexpected error occurred in scheduled task', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'ERROR', 'ThreadPoolTaskScheduler-1', 'TaskUtils.java', 'org.springframework.scheduling.support.TaskUtils$LoggingErrorHandler', 'handleError', '95', 8829, 'web-0');

-- ----------------------------
-- Table structure for pods_log
-- ----------------------------
DROP TABLE IF EXISTS `pods_log`;
CREATE TABLE `pods_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pod_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `log_level` enum('DEBUG','INFO','WARNING','ERROR','CRITICAL') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `log_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `log_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pods_log
-- ----------------------------

-- ----------------------------
-- Table structure for total_stats
-- ----------------------------
DROP TABLE IF EXISTS `total_stats`;
CREATE TABLE `total_stats`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `total_upload` int(11) NOT NULL,
  `total_download` int(11) NOT NULL,
  `total_delete` int(11) NOT NULL,
  `user_uuid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `date` date NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_date`(`date`) USING BTREE,
  UNIQUE INDEX `unique_user_uuid_data`(`user_uuid`, `date`) USING BTREE,
  INDEX `idx_user_id`(`user_uuid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of total_stats
-- ----------------------------
INSERT INTO `total_stats` VALUES (37, 1, 0, 0, 'admin', '2024-06-20');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `uuid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `level` int(11) NOT NULL,
  `thumbnail_avatar` longblob NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (17, 'admin', 'admin', 'admin', 9, NULL);

-- ----------------------------
-- Table structure for user_login_log
-- ----------------------------
DROP TABLE IF EXISTS `user_login_log`;
CREATE TABLE `user_login_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `last_login_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `last_login_ip` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_login_device` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `user_uuid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_login_log
-- ----------------------------
INSERT INTO `user_login_log` VALUES (2, '2024-06-20 08:43:28', '0:0:0:0:0:0:0:1', 'windows', 'admin');

SET FOREIGN_KEY_CHECKS = 1;
