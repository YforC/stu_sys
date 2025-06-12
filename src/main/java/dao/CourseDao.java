package dao;

import entity.Course;
import java.util.List;

public interface CourseDao {
    // 添加课程
    int addCourse(Course course);
    // 修改课程
    int updateCourse(Course course);
    // 查询课程（可按条件）
    Course getCourseById(String id);
    List<Course> queryCourses(String name, String type, String semester, String teacherId);
    // 分页查询
    List<Course> queryCoursesByPage(int offset, int limit);
    // 逻辑删除课程
    int deleteCourse(String id);
}
