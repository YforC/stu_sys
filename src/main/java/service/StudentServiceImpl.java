package service;

import dao.StudentDao;
import dao.StudentDaoImpl;
import entity.Student;
import java.util.List;

public class StudentServiceImpl implements StudentService {
    private final StudentDao studentDao = new StudentDaoImpl();

    @Override
    public int addStudent(Student student) {
        // 业务校验：学号、姓名、性别、入学年份、班级、专业、学院不能为空
        if (student == null || student.getId() == null || student.getId().isEmpty()
                || student.getName() == null || student.getName().isEmpty()
                || student.getGender() == null || student.getGender().isEmpty()
                || student.getEnrollmentYear() == null
                || student.getClassName() == null || student.getClassName().isEmpty()
                || student.getMajor() == null || student.getMajor().isEmpty()
                || student.getCollege() == null || student.getCollege().isEmpty()) {
            throw new IllegalArgumentException("学号、姓名、性别、入学年份、班级、专业、学院不能为空");
        }
        return studentDao.addStudent(student);
    }

    @Override
    public int updateStudent(Student student) {
        // 业务校验同添加
        if (student == null || student.getId() == null || student.getId().isEmpty()
                || student.getName() == null || student.getName().isEmpty()
                || student.getGender() == null || student.getGender().isEmpty()
                || student.getEnrollmentYear() == null
                || student.getClassName() == null || student.getClassName().isEmpty()
                || student.getMajor() == null || student.getMajor().isEmpty()
                || student.getCollege() == null || student.getCollege().isEmpty()) {
            throw new IllegalArgumentException("学号、姓名、性别、入学年份、班级、专业、学院不能为空");
        }
        return studentDao.updateStudent(student);
    }

    @Override
    public Student getStudentById(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("学号不能为空");
        }
        return studentDao.getStudentById(id);
    }

    @Override
    public List<Student> queryStudents(String name, String className, String major, String college, String status) {
        return studentDao.queryStudents(name, className, major, college, status);
    }

    @Override
    public List<Student> queryStudentsByPage(int offset, int limit) {
        if (offset < 0 || limit <= 0) {
            throw new IllegalArgumentException("分页参数不合法");
        }
        return studentDao.queryStudentsByPage(offset, limit);
    }

    @Override
    public int deleteStudent(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("学号不能为空");
        }
        return studentDao.deleteStudent(id);
    }
}

