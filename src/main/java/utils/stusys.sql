/*
SQLyog Ultimate v8.32 
MySQL - 8.0.33 : Database - student_management
*********************************************************************
*/


/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`student_management` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `student_management`;

/*Table structure for table `course` */

DROP TABLE IF EXISTS `course`;

CREATE TABLE `course` (
  `id` varchar(20) NOT NULL COMMENT '课程编号',
  `name` varchar(100) NOT NULL,
  `credits` decimal(3,1) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `semester` varchar(20) DEFAULT NULL,
  `hours` int DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `course` */

insert  into `course`(`id`,`name`,`credits`,`type`,`semester`,`hours`,`description`) values ('C001','数据库原理','3.0','必修','2023春',48,'数据库基础知识');

/*Table structure for table `grade` */

DROP TABLE IF EXISTS `grade`;

CREATE TABLE `grade` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '成绩编号',
  `student_id` varchar(20) DEFAULT NULL,
  `teaching_task_id` varchar(20) DEFAULT NULL,
  `regular_score` decimal(5,2) DEFAULT NULL,
  `midterm_score` decimal(5,2) DEFAULT NULL,
  `final_score` decimal(5,2) DEFAULT NULL,
  `total_score` decimal(5,2) DEFAULT NULL,
  `entry_time` datetime DEFAULT NULL,
  `entry_by` varchar(50) DEFAULT NULL,
  `last_modified_time` datetime DEFAULT NULL,
  `last_modified_by` varchar(50) DEFAULT NULL,
  `modification_reason` varchar(100) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `student_id` (`student_id`),
  KEY `teaching_task_id` (`teaching_task_id`),
  CONSTRAINT `grade_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student` (`id`),
  CONSTRAINT `grade_ibfk_2` FOREIGN KEY (`teaching_task_id`) REFERENCES `teaching_task` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `grade` */

insert  into `grade`(`id`,`student_id`,`teaching_task_id`,`regular_score`,`midterm_score`,`final_score`,`total_score`,`entry_time`,`entry_by`,`last_modified_time`,`last_modified_by`,`modification_reason`,`status`) values (1,'S001','TT001','85.00','88.00','90.00','88.00','2025-06-11 17:25:09','T001',NULL,NULL,NULL,'公示中');

/*Table structure for table `grade_statistics` */

DROP TABLE IF EXISTS `grade_statistics`;

CREATE TABLE `grade_statistics` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '统计编号',
  `object_id` varchar(20) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `academic_year` varchar(20) DEFAULT NULL,
  `semester` varchar(20) DEFAULT NULL,
  `average_score` decimal(5,2) DEFAULT NULL,
  `highest_score` decimal(5,2) DEFAULT NULL,
  `lowest_score` decimal(5,2) DEFAULT NULL,
  `pass_count` int DEFAULT NULL,
  `pass_rate` decimal(5,2) DEFAULT NULL,
  `excellent_count` int DEFAULT NULL,
  `excellent_rate` decimal(5,2) DEFAULT NULL,
  `statistics_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `grade_statistics` */

/*Table structure for table `student` */

DROP TABLE IF EXISTS `student`;

CREATE TABLE `student` (
  `id` varchar(20) NOT NULL COMMENT '学号',
  `name` varchar(50) NOT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `birth_date` date DEFAULT NULL,
  `enrollment_year` int DEFAULT NULL,
  `class_name` varchar(50) DEFAULT NULL,
  `major` varchar(50) DEFAULT NULL,
  `college` varchar(50) DEFAULT NULL,
  `contact_info` varchar(100) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `password` varchar(255) NOT NULL DEFAULT '123456' COMMENT '登录密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `student` */

insert  into `student`(`id`,`name`,`gender`,`birth_date`,`enrollment_year`,`class_name`,`major`,`college`,`contact_info`,`status`) values ('S001','张三','男','2002-05-01',2020,'计科1班','计算机科学与技术','计算机学院','12345678901','在读');

/*Table structure for table `teacher` */

DROP TABLE IF EXISTS `teacher`;

CREATE TABLE `teacher` (
  `id` varchar(20) NOT NULL COMMENT '工号',
  `name` varchar(50) NOT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `birth_date` date DEFAULT NULL,
  `title` varchar(50) DEFAULT NULL,
  `college` varchar(50) DEFAULT NULL,
  `major` varchar(50) DEFAULT NULL,
  `contact_info` varchar(100) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `password` varchar(255) NOT NULL DEFAULT '123456' COMMENT '登录密码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `teacher` */

insert  into `teacher`(`id`,`name`,`gender`,`birth_date`,`title`,`college`,`major`,`contact_info`,`status`) values ('T001','李老师','女','1980-03-15','副教授','计算机学院','计算机科学与技术','98765432101','在职');

/*Table structure for table `teaching_task` */

DROP TABLE IF EXISTS `teaching_task`;

CREATE TABLE `teaching_task` (
  `id` varchar(20) NOT NULL COMMENT '任务编号',
  `course_id` varchar(20) DEFAULT NULL,
  `teacher_id` varchar(20) DEFAULT NULL,
  `academic_year` varchar(20) DEFAULT NULL,
  `semester` varchar(20) DEFAULT NULL,
  `class_name` varchar(50) DEFAULT NULL,
  `class_time` varchar(50) DEFAULT NULL,
  `location` varchar(100) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `course_id` (`course_id`),
  KEY `teacher_id` (`teacher_id`),
  CONSTRAINT `teaching_task_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`),
  CONSTRAINT `teaching_task_ibfk_2` FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `teaching_task` */

insert  into `teaching_task`(`id`,`course_id`,`teacher_id`,`academic_year`,`semester`,`class_name`,`class_time`,`location`,`status`) values ('TT001','C001','T001','2023-2024','春','计科1班','周一1-2节','A101','进行中');

-- 创建管理员表
CREATE TABLE admin (
    admin_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '管理员ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    name VARCHAR(100) NOT NULL COMMENT '姓名',
    email VARCHAR(100) COMMENT '电子邮件',
    phone VARCHAR(20) COMMENT '联系电话',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) COMMENT='管理员表';

/*Data for the table `admin` */

insert into admin (username, password, name, email, phone)
VALUES ('admin', 'admin123', '系统管理员', 'admin@example.com', '12345678901');

CREATE TABLE enrollment (
                            id INT AUTO_INCREMENT PRIMARY KEY COMMENT '选课记录ID',
                            student_id VARCHAR(20) NOT NULL COMMENT '学生学号',
                            course_id VARCHAR(20) NOT NULL COMMENT '课程编号',
                            enrollment_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
                            status VARCHAR(20) DEFAULT '已选' COMMENT '选课状态',
                            FOREIGN KEY (student_id) REFERENCES student(id),
                            FOREIGN KEY (course_id) REFERENCES course(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

 /*Data for the table `enrollment` */
insert into enrollment (student_id, course_id, enrollment_date, status)
VALUES ('S001', 'C001', NOW(), '已选');

-- 添加更多学生数据
INSERT INTO student (id, name, gender, birth_date, enrollment_year, class_name, major, college, contact_info, status) VALUES ('S002', '李四', '男', '2001-08-15', 2021, '软件工程1班', '软件工程', '计算机学院', '13567890123', '在读');
INSERT INTO student (id, name, gender, birth_date, enrollment_year, class_name, major, college, contact_info, status) VALUES ('S003', '王五', '女', '2002-11-20', 2022, '网络工程1班', '网络工程', '计算机学院', '14785236901', '在读');

-- 添加更多教师数据
INSERT INTO teacher (id, name, gender, birth_date, title, college, major, contact_info, status) VALUES ('T002', '王教授', '男', '1975-04-10', '教授', '计算机学院', '人工智能', '13579246801', '在职');
INSERT INTO teacher (id, name, gender, birth_date, title, college, major, contact_info, status) VALUES ('T003', '赵老师', '女', '1985-09-25', '讲师', '计算机学院', '大数据', '12365478901', '在职');

-- 添加更多课程数据
INSERT INTO course (id, name, credits, type, semester, hours, description) VALUES ('C002', '数据结构与算法', '4.0', '必修', '2023秋', 64, '学习基本数据结构和算法设计');
INSERT INTO course (id, name, credits, type, semester, hours, description) VALUES ('C003', '操作系统原理', '3.5', '必修', '2023秋', 56, '操作系统的基本概念和原理');

-- 添加更多教学任务数据
INSERT INTO teaching_task (id, course_id, teacher_id, academic_year, semester, class_name, class_time, location, status) VALUES ('TT002', 'C002', 'T002', '2023-2024', '秋', '软件工程1班', '周二3-4节', 'B201', '进行中');
INSERT INTO teaching_task (id, course_id, teacher_id, academic_year, semester, class_name, class_time, location, status) VALUES ('TT003', 'C003', 'T003', '2023-2024', '秋', '网络工程1班', '周三5-6节', 'C301', '未开始');

-- 添加更多成绩数据
INSERT INTO grade (student_id, teaching_task_id, regular_score, midterm_score, final_score, total_score, entry_time, entry_by, status) VALUES ('S002', 'TT002', '78.50', '82.00', '85.00', '82.50', NOW(), 'T002', '公示中');
INSERT INTO grade (student_id, teaching_task_id, regular_score, midterm_score, final_score, total_score, entry_time, entry_by, status) VALUES ('S003', 'TT003', '90.00', '88.00', '92.00', '90.00', NOW(), 'T003', '已归档');

-- 添加更多选课记录数据
INSERT INTO enrollment (student_id, course_id, enrollment_date, status) VALUES ('S002', 'C002', NOW(), '已选');
INSERT INTO enrollment (student_id, course_id, enrollment_date, status) VALUES ('S003', 'C003', NOW(), '已选');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
