package dao;

import entity.Student;
import java.util.List;

public interface StudentDao {
    // 添加学生档案
    int addStudent(Student student);
    // 修改学生档案
    int updateStudent(Student student);
    // 查询学生档案（可按条件）
    Student getStudentById(String id);
    // 根据学号查询学生（包含密码）
    List<Student> queryStudents(String name, String className, String major, String college, String status);
    // 分页查询
    List<Student> queryStudentsByPage(int offset, int limit);
    // 逻辑删除学生档案
    int deleteStudent(String id);
}
