package service;

import entity.Enrollment;
import java.util.List;

public interface EnrollmentService {
    List<Enrollment> getEnrollmentsByStudentId(String studentId);

    // 新增：按课程ID查找所有选课学生
    List<Enrollment> getEnrollmentsByCourseId(String courseId);
}