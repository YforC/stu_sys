package service;

import dao.TeacherDao;
import dao.TeacherDaoImpl;
import entity.Teacher;
import java.util.List;

public class TeacherServiceImpl implements TeacherService {
    private final TeacherDao teacherDao = new TeacherDaoImpl();

    @Override
    public int addTeacher(Teacher teacher) {
        // 业务校验：工号、姓名、性别、职称、学院、专业不能为空
        if (teacher == null || teacher.getId() == null || teacher.getId().isEmpty()
                || teacher.getName() == null || teacher.getName().isEmpty()
                || teacher.getGender() == null || teacher.getGender().isEmpty()
                || teacher.getTitle() == null || teacher.getTitle().isEmpty()
                || teacher.getCollege() == null || teacher.getCollege().isEmpty()
                || teacher.getMajor() == null || teacher.getMajor().isEmpty()) {
            throw new IllegalArgumentException("工号、姓名、性别、职称、学院、专业不能为空");
        }
        return teacherDao.addTeacher(teacher);
    }

    @Override
    public int updateTeacher(Teacher teacher) {
        // 业务校验同添加
        if (teacher == null || teacher.getId() == null || teacher.getId().isEmpty()
                || teacher.getName() == null || teacher.getName().isEmpty()
                || teacher.getGender() == null || teacher.getGender().isEmpty()
                || teacher.getTitle() == null || teacher.getTitle().isEmpty()
                || teacher.getCollege() == null || teacher.getCollege().isEmpty()
                || teacher.getMajor() == null || teacher.getMajor().isEmpty()) {
            throw new IllegalArgumentException("工号、姓名、性别、职称、学院、专业不能为空");
        }
        return teacherDao.updateTeacher(teacher);
    }

    @Override
    public Teacher getTeacherById(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("工号不能为空");
        }
        Teacher teacher = teacherDao.getTeacherById(id);
        // 这里可以添加对密码字段的处理逻辑
        return teacher;
    }

    @Override
    public List<Teacher> queryTeachers(String name, String college, String major, String status) {
        return teacherDao.queryTeachers(name, college, major, status);
    }

    @Override
    public List<Teacher> queryTeachersByPage(int offset, int limit) {
        if (offset < 0 || limit <= 0) {
            throw new IllegalArgumentException("分页参数不合法");
        }
        return teacherDao.queryTeachersByPage(offset, limit);
    }

    @Override
    public int deleteTeacher(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("工号不能为空");
        }
        return teacherDao.deleteTeacher(id);
    }
}
