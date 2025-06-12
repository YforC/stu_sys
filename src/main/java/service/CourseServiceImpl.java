package service;

import dao.CourseDao;
import dao.CourseDaoImpl;
import entity.Course;
import utils.db_connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CourseServiceImpl implements CourseService {
    private final CourseDao courseDao = new CourseDaoImpl();
    
    // 从ResultSet中提取Course对象的私有方法
    private Course extractCourse(ResultSet rs) throws SQLException {
        Course course = new Course();
        course.setId(rs.getString("id"));
        course.setName(rs.getString("name"));
        course.setType(rs.getString("type"));
        course.setSemester(rs.getString("semester"));
        Integer creditValue = rs.getObject("credits", Integer.class);
        course.setCredits(creditValue != null ? creditValue.doubleValue() : null);
        return course;
    }
    
    @Override
    public int addCourse(Course course) {
        // 业务规则：课程编号、名称不能为空，学分范围校验
        if (course == null || course.getId() == null || course.getId().isEmpty() || course.getName() == null || course.getName().isEmpty()) {
            throw new IllegalArgumentException("课程编号和名称不能为空");
        }
        if (course.getCredits() != null && (course.getCredits() < 0 || course.getCredits() > 20)) {
            throw new IllegalArgumentException("学分范围应为0-20");
        }
        return courseDao.addCourse(course);
    }

    @Override
    public int updateCourse(Course course) {
        // 业务规则：课程编号、名称不能为空，学分范围校验
        if (course == null || course.getId() == null || course.getId().isEmpty() || course.getName() == null || course.getName().isEmpty()) {
            throw new IllegalArgumentException("课程编号和名称不能为空");
        }
        if (course.getCredits() != null && (course.getCredits() < 0 || course.getCredits() > 20)) {
            throw new IllegalArgumentException("学分范围应为0-20");
        }
        return courseDao.updateCourse(course);
    }

    @Override
    public Course getCourseById(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("课程编号不能为空");
        }
        return courseDao.getCourseById(id);
    }

    @Override
    public List<Course> queryCourses(String name, String type, String semester, String teacherId) {
        return courseDao.queryCourses(name, type, semester, teacherId);
    }

    @Override
    public List<Course> queryCoursesByIds(List<String> courseIds) {
        if (courseIds == null || courseIds.isEmpty()) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder("SELECT * FROM course WHERE id IN (");
        for (int i = 0; i < courseIds.size(); i++) {
            sql.append("?");
            if (i < courseIds.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(")");
        List<Course> list = new ArrayList<>();
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < courseIds.size(); i++) {
                ps.setString(i + 1, courseIds.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractCourse(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Course> queryCoursesByPage(int offset, int limit) {
        if (offset < 0 || limit <= 0) {
            throw new IllegalArgumentException("分页参数不合法");
        }
        return courseDao.queryCoursesByPage(offset, limit);
    }

    @Override
    public int deleteCourse(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("课程编号不能为空");
        }
        return courseDao.deleteCourse(id);
    }
}

