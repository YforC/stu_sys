package service;

import entity.Teacher;
import java.util.List;

public interface TeacherService {
    int addTeacher(Teacher teacher);
    int updateTeacher(Teacher teacher);
    Teacher getTeacherById(String id); // 根据工号查询教师（包含密码）
    List<Teacher> queryTeachers(String name, String college, String major, String status);
    List<Teacher> queryTeachersByPage(int offset, int limit);
    int deleteTeacher(String id);
}
