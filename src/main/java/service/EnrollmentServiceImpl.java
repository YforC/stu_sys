package service;

import dao.EnrollmentDao;
import dao.EnrollmentDaoImpl;
import entity.Enrollment;
import java.util.List;

public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentDao enrollmentDao = new EnrollmentDaoImpl();

    @Override
    public List<Enrollment> getEnrollmentsByStudentId(String studentId) {
        if (studentId == null || studentId.isEmpty()) {
            throw new IllegalArgumentException("学生ID不能为空");
        }
        return enrollmentDao.getEnrollmentsByStudentId(studentId);
    }

    @Override
    public List<Enrollment> getEnrollmentsByCourseId(String courseId) {
        if (courseId == null || courseId.isEmpty()) {
            throw new IllegalArgumentException("课程ID不能为空");
        }
        return enrollmentDao.getEnrollmentsByCourseId(courseId);
    }
}