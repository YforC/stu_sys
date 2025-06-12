package dao;

import entity.Course;
import utils.db_connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDaoImpl implements CourseDao {
    @Override
    public int addCourse(Course course) {
        String sql = "INSERT INTO course (id, name, credits, type, semester, hours, description) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, course.getId());
            ps.setString(2, course.getName());
            ps.setDouble(3, course.getCredits());
            ps.setString(4, course.getType());
            ps.setString(5, course.getSemester());
            ps.setInt(6, course.getHours());
            ps.setString(7, course.getDescription());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int updateCourse(Course course) {
        String sql = "UPDATE course SET name=?, credits=?, type=?, semester=?, hours=?, description=? WHERE id=?";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, course.getName());
            ps.setDouble(2, course.getCredits());
            ps.setString(3, course.getType());
            ps.setString(4, course.getSemester());
            ps.setInt(5, course.getHours());
            ps.setString(6, course.getDescription());
            ps.setString(7, course.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Course getCourseById(String id) {
        String sql = "SELECT * FROM course WHERE id=?";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractCourse(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Course> queryCourses(String name, String type, String semester, String teacherId) {
        StringBuilder sql = new StringBuilder("SELECT * FROM course WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            sql.append(" AND name LIKE ?");
            params.add("%" + name + "%");
        }
        if (type != null && !type.isEmpty()) {
            sql.append(" AND type=?");
            params.add(type);
        }
        if (semester != null && !semester.isEmpty()) {
            sql.append(" AND semester=?");
            params.add(semester);
        }
        // teacherId条件可根据实际表结构调整
        List<Course> list = new ArrayList<>();
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
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
        String sql = "SELECT * FROM course LIMIT ?, ?";
        List<Course> list = new ArrayList<>();
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
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
    public int deleteCourse(String id) {
        String sql = "UPDATE course SET deleted=1 WHERE id=?"; // 逻辑删除，需有deleted字段
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }


    private Course extractCourse(ResultSet rs) throws SQLException {
        return new Course(
                rs.getString("id"),
                rs.getString("name"),
                rs.getDouble("credits"),
                rs.getString("type"),
                rs.getString("semester"),
                rs.getInt("hours"),
                rs.getString("description")
        );
    }
    public static void main(String[] args) {
        CourseDao dao = new CourseDaoImpl();
        // 添加课程测试
        Course c = new Course("C003", "高等数学", 4.0, "必修", "2024春", 64, "基础数学课程");
        int addResult = dao.addCourse(c);
        System.out.println("添加课程结果: " + addResult);

        // 查询课程
        Course c2 = dao.getCourseById("C001");
        System.out.println("查询课程: " + c2);

        // 修改课程
        c2.setName("高等数学A");
        int updateResult = dao.updateCourse(c2);
        System.out.println("修改课程结果: " + updateResult);

        // 分页查询
        System.out.println("分页查询:");
        for (Course course : dao.queryCoursesByPage(0, 10)) {
            System.out.println(course);
        }

        // 条件查询
        System.out.println("条件查询:");
        for (Course course : dao.queryCourses("高等", null, null, null)) {
            System.out.println(course);
        }

//        // 逻辑删除
//        int delResult = dao.deleteCourse("C001");
//        System.out.println("逻辑删除结果: " + delResult);
    }
}
