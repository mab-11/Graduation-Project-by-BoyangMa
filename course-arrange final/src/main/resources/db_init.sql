SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE IF NOT EXISTS `user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `u_name` VARCHAR(32) NOT NULL,
  `password` VARCHAR(64) NOT NULL,
  `status` TINYINT NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_name` (`u_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `teacher` (
  `id` INT NOT NULL,
  `t_name` VARCHAR(32) NOT NULL,
  `password` VARCHAR(64) NOT NULL,
  `remark` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_teacher_name` (`t_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `course` (
  `c_id` INT NOT NULL AUTO_INCREMENT,
  `c_code` VARCHAR(32) NOT NULL,
  `c_name` VARCHAR(64) NOT NULL,
  `c_remark` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`c_id`),
  UNIQUE KEY `uk_course_code` (`c_code`),
  UNIQUE KEY `uk_course_name` (`c_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `classes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `size` INT NOT NULL,
  `c_name` VARCHAR(64) NOT NULL,
  `remark` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_classes_name` (`c_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `room` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `r_name` VARCHAR(64) NOT NULL,
  `capacity` INT NOT NULL,
  `remark` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_room_name` (`r_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `teacher_course` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `course_id` INT NOT NULL,
  `teacher_id` INT NOT NULL,
  `remark` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_teacher_course` (`course_id`, `teacher_id`),
  KEY `idx_teacher_course_teacher` (`teacher_id`),
  CONSTRAINT `fk_teacher_course_course` FOREIGN KEY (`course_id`) REFERENCES `course` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_teacher_course_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `classes_course` (
  `cc_id` INT NOT NULL AUTO_INCREMENT,
  `cc_name` VARCHAR(64) NOT NULL,
  `course_id` INT NOT NULL,
  `cc_size` INT NOT NULL,
  `cc_remark` VARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (`cc_id`),
  UNIQUE KEY `uk_classes_course_name` (`cc_name`, `course_id`),
  KEY `idx_classes_course_course` (`course_id`),
  CONSTRAINT `fk_classes_course_course` FOREIGN KEY (`course_id`) REFERENCES `course` (`c_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `task` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `cc_id` INT NOT NULL,
  `teacher_id` INT NOT NULL,
  `start_week` INT NOT NULL,
  `end_week` INT NOT NULL,
  `week_node` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_task_unique` (`cc_id`, `teacher_id`, `week_node`, `start_week`, `end_week`),
  KEY `idx_task_teacher` (`teacher_id`),
  CONSTRAINT `fk_task_cclasses` FOREIGN KEY (`cc_id`) REFERENCES `classes_course` (`cc_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_task_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `timeslot` (
  `time_id` INT NOT NULL,
  `timeslot` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`time_id`),
  UNIQUE KEY `uk_timeslot_name` (`timeslot`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `course_table` (
  `task_id` INT NOT NULL,
  `room_id` INT NOT NULL,
  `timeslot_id` INT NOT NULL,
  PRIMARY KEY (`task_id`),
  UNIQUE KEY `uk_room_timeslot` (`room_id`, `timeslot_id`),
  KEY `idx_course_table_timeslot` (`timeslot_id`),
  CONSTRAINT `fk_course_table_task` FOREIGN KEY (`task_id`) REFERENCES `task` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_course_table_room` FOREIGN KEY (`room_id`) REFERENCES `room` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_course_table_timeslot` FOREIGN KEY (`timeslot_id`) REFERENCES `timeslot` (`time_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `user` (`id`, `u_name`, `password`, `status`) VALUES
  (1, 'admin', 'a123456', 1)
ON DUPLICATE KEY UPDATE `password` = VALUES(`password`), `status` = VALUES(`status`);

INSERT INTO `teacher` (`id`, `t_name`, `password`, `remark`) VALUES
  (2022000001, '张老师', 'a123456', '数学教师'),
  (2022000002, '李老师', 'a123456', '英语教师'),
  (2022000003, '王老师', 'a123456', '计算机教师')
ON DUPLICATE KEY UPDATE `t_name` = VALUES(`t_name`), `password` = VALUES(`password`), `remark` = VALUES(`remark`);

INSERT INTO `course` (`c_id`, `c_code`, `c_name`, `c_remark`) VALUES
  (1, 'MATH101', '高等数学', '基础课'),
  (2, 'ENG101', '大学英语', '公共课'),
  (3, 'CS101', '程序设计基础', '专业基础课')
ON DUPLICATE KEY UPDATE `c_code` = VALUES(`c_code`), `c_name` = VALUES(`c_name`), `c_remark` = VALUES(`c_remark`);

INSERT INTO `classes` (`id`, `size`, `c_name`, `remark`) VALUES
  (1, 40, '软件工程1班', '2022级'),
  (2, 38, '软件工程2班', '2022级'),
  (3, 45, '计算机科学1班', '2022级')
ON DUPLICATE KEY UPDATE `size` = VALUES(`size`), `remark` = VALUES(`remark`);

INSERT INTO `room` (`id`, `r_name`, `capacity`, `remark`) VALUES
  (1, 'A101', 50, '普通教室'),
  (2, 'A102', 50, '普通教室'),
  (3, '机房1', 45, '计算机机房')
ON DUPLICATE KEY UPDATE `capacity` = VALUES(`capacity`), `remark` = VALUES(`remark`);

INSERT INTO `teacher_course` (`id`, `course_id`, `teacher_id`, `remark`) VALUES
  (1, 1, 2022000001, '主讲'),
  (2, 2, 2022000002, '主讲'),
  (3, 3, 2022000003, '主讲')
ON DUPLICATE KEY UPDATE `remark` = VALUES(`remark`);

INSERT INTO `classes_course` (`cc_id`, `cc_name`, `course_id`, `cc_size`, `cc_remark`) VALUES
  (1, '软件工程1班', 1, 40, '高数教学班'),
  (2, '软件工程2班', 2, 38, '英语教学班'),
  (3, '计算机科学1班', 3, 45, '程序设计教学班')
ON DUPLICATE KEY UPDATE `cc_size` = VALUES(`cc_size`), `cc_remark` = VALUES(`cc_remark`);

INSERT INTO `task` (`id`, `cc_id`, `teacher_id`, `start_week`, `end_week`, `week_node`) VALUES
  (1, 1, 2022000001, 1, 16, 1),
  (2, 2, 2022000002, 1, 16, 2),
  (3, 3, 2022000003, 1, 16, 3)
ON DUPLICATE KEY UPDATE `cc_id` = VALUES(`cc_id`), `teacher_id` = VALUES(`teacher_id`), `start_week` = VALUES(`start_week`), `end_week` = VALUES(`end_week`), `week_node` = VALUES(`week_node`);

INSERT INTO `timeslot` (`time_id`, `timeslot`) VALUES
  (1, '周一第1-2节'),
  (2, '周一第3-4节'),
  (3, '周二第1-2节'),
  (4, '周二第3-4节'),
  (5, '周三第1-2节'),
  (6, '周三第3-4节'),
  (7, '周四第1-2节'),
  (8, '周四第3-4节'),
  (9, '周五第1-2节'),
  (10, '周五第3-4节')
ON DUPLICATE KEY UPDATE `timeslot` = VALUES(`timeslot`);

INSERT INTO `course_table` (`task_id`, `room_id`, `timeslot_id`) VALUES
  (1, 1, 1),
  (2, 2, 2),
  (3, 3, 3)
ON DUPLICATE KEY UPDATE `room_id` = VALUES(`room_id`), `timeslot_id` = VALUES(`timeslot_id`);

SET FOREIGN_KEY_CHECKS = 1;
