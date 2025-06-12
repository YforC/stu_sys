package dao;

import entity.Teacher;
import java.util.List;

public interface TeacherDao {
    // 添加教师档案
    int addTeacher(Teacher teacher);
    // 修改教师档案
    int updateTeacher(Teacher teacher);
    // 查询教师档案（可按条件）
    Teacher getTeacherById(String id);
    // 根据工号查询教师（包含密码）
    List<Teacher> queryTeachers(String name, String college, String major, String status);
    // 分页查询
    List<Teacher> queryTeachersByPage(int offset, int limit);
    // 逻辑删除教师档案
    int deleteTeacher(String id);
}
