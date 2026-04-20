/*
 Navicat Premium Data Transfer

 Source Server         : a1101
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : localhost:3306
 Source Schema         : course_arrange

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 14/04/2026 16:16:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for classes
-- ----------------------------
DROP TABLE IF EXISTS `classes`;
CREATE TABLE `classes`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `size` int(11) NOT NULL,
  `c_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_classes_name`(`c_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of classes
-- ----------------------------
INSERT INTO `classes` VALUES (1, 40, '软件工程1班', '2022级');
INSERT INTO `classes` VALUES (2, 38, '软件工程2班', '2022级');
INSERT INTO `classes` VALUES (3, 45, '计算机科学1班', '2022级');

-- ----------------------------
-- Table structure for classes_course
-- ----------------------------
DROP TABLE IF EXISTS `classes_course`;
CREATE TABLE `classes_course`  (
  `cc_id` int(11) NOT NULL AUTO_INCREMENT,
  `cc_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `course_id` int(11) NOT NULL,
  `cc_size` int(11) NOT NULL,
  `cc_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`cc_id`) USING BTREE,
  UNIQUE INDEX `uk_classes_course_name`(`cc_name`, `course_id`) USING BTREE,
  INDEX `idx_classes_course_course`(`course_id`) USING BTREE,
  CONSTRAINT `fk_classes_course_course` FOREIGN KEY (`course_id`) REFERENCES `course` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of classes_course
-- ----------------------------
INSERT INTO `classes_course` VALUES (1, '软件工程1班', 1, 40, '高数教学班');
INSERT INTO `classes_course` VALUES (2, '软件工程2班', 2, 38, '英语教学班');
INSERT INTO `classes_course` VALUES (3, '计算机科学1班', 3, 45, '程序设计教学班');

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `c_id` int(11) NOT NULL AUTO_INCREMENT,
  `c_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `c_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `c_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`c_id`) USING BTREE,
  UNIQUE INDEX `uk_course_code`(`c_code`) USING BTREE,
  UNIQUE INDEX `uk_course_name`(`c_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES (1, 'MATH101', '高等数学', '基础课');
INSERT INTO `course` VALUES (2, 'ENG101', '大学英语', '公共课');
INSERT INTO `course` VALUES (3, 'CS101', '程序设计基础', '专业基础课');

-- ----------------------------
-- Table structure for course_table
-- ----------------------------
DROP TABLE IF EXISTS `course_table`;
CREATE TABLE `course_table`  (
  `task_id` int(11) NOT NULL,
  `room_id` int(11) NOT NULL,
  `timeslot_id` int(11) NOT NULL,
  PRIMARY KEY (`task_id`) USING BTREE,
  UNIQUE INDEX `uk_room_timeslot`(`room_id`, `timeslot_id`) USING BTREE,
  INDEX `idx_course_table_timeslot`(`timeslot_id`) USING BTREE,
  CONSTRAINT `fk_course_table_room` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_course_table_task` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_course_table_timeslot` FOREIGN KEY (`timeslot_id`) REFERENCES `timeslot` (`time_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_table
-- ----------------------------
INSERT INTO `course_table` VALUES (1, 1, 1);
INSERT INTO `course_table` VALUES (2, 2, 2);
INSERT INTO `course_table` VALUES (3, 3, 3);

-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `r_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `capacity` int(11) NOT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_room_name`(`r_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room
-- ----------------------------
INSERT INTO `room` VALUES (1, 'A101', 50, '普通教室');
INSERT INTO `room` VALUES (2, 'A102', 50, '普通教室');
INSERT INTO `room` VALUES (3, '机房1', 45, '计算机机房');

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cc_id` int(11) NOT NULL,
  `teacher_id` int(11) NOT NULL,
  `start_week` int(11) NOT NULL,
  `end_week` int(11) NOT NULL,
  `week_node` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_task_unique`(`cc_id`, `teacher_id`, `week_node`, `start_week`, `end_week`) USING BTREE,
  INDEX `idx_task_teacher`(`teacher_id`) USING BTREE,
  CONSTRAINT `fk_task_cclasses` FOREIGN KEY (`cc_id`) REFERENCES `classes_course` (`cc_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_task_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES (1, 1, 2022000001, 1, 16, 1);
INSERT INTO `task` VALUES (2, 2, 2022000002, 1, 16, 2);
INSERT INTO `task` VALUES (3, 3, 2022000003, 1, 16, 3);

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `id` int(11) NOT NULL,
  `t_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_teacher_name`(`t_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES (2022000001, '张老师', 'a123456', '数学教师');
INSERT INTO `teacher` VALUES (2022000002, '李老师', 'a123456', '英语教师');
INSERT INTO `teacher` VALUES (2022000003, '王老师', 'a123456', '计算机教师');

-- ----------------------------
-- Table structure for teacher_course
-- ----------------------------
DROP TABLE IF EXISTS `teacher_course`;
CREATE TABLE `teacher_course`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `course_id` int(11) NOT NULL,
  `teacher_id` int(11) NOT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_teacher_course`(`course_id`, `teacher_id`) USING BTREE,
  INDEX `idx_teacher_course_teacher`(`teacher_id`) USING BTREE,
  CONSTRAINT `fk_teacher_course_course` FOREIGN KEY (`course_id`) REFERENCES `course` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_teacher_course_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of teacher_course
-- ----------------------------
INSERT INTO `teacher_course` VALUES (1, 1, 2022000001, '主讲');
INSERT INTO `teacher_course` VALUES (2, 2, 2022000002, '主讲');
INSERT INTO `teacher_course` VALUES (3, 3, 2022000003, '主讲');

-- ----------------------------
-- Table structure for timeslot
-- ----------------------------
DROP TABLE IF EXISTS `timeslot`;
CREATE TABLE `timeslot`  (
  `time_id` int(11) NOT NULL,
  `timeslot` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`time_id`) USING BTREE,
  UNIQUE INDEX `uk_timeslot_name`(`timeslot`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of timeslot
-- ----------------------------
INSERT INTO `timeslot` VALUES (1, '周一第1-2节');
INSERT INTO `timeslot` VALUES (2, '周一第3-4节');
INSERT INTO `timeslot` VALUES (5, '周三第1-2节');
INSERT INTO `timeslot` VALUES (6, '周三第3-4节');
INSERT INTO `timeslot` VALUES (3, '周二第1-2节');
INSERT INTO `timeslot` VALUES (4, '周二第3-4节');
INSERT INTO `timeslot` VALUES (9, '周五第1-2节');
INSERT INTO `timeslot` VALUES (10, '周五第3-4节');
INSERT INTO `timeslot` VALUES (7, '周四第1-2节');
INSERT INTO `timeslot` VALUES (8, '周四第3-4节');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `u_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_name`(`u_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'a123456', 1);

SET FOREIGN_KEY_CHECKS = 1;
