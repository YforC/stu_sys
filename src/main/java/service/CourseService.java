package service;

import entity.Course;
import java.util.List;

public interface CourseService {
    int addCourse(Course course);
    int updateCourse(Course course);
    Course getCourseById(String id);
    List<Course> queryCourses(String name, String type, String semester, String teacherId);
    List<Course> queryCoursesByIds(List<String> courseIds);
    List<Course> queryCoursesByPage(int offset, int limit);
    int deleteCourse(String id);
}

