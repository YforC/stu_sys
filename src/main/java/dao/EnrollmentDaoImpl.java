package dao;

import entity.Enrollment;
import utils.db_connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDaoImpl implements EnrollmentDao {

    @Override
    public List<Enrollment> getEnrollmentsByStudentId(String studentId) {
        String sql = "SELECT * FROM enrollment WHERE student_id=? AND status='已选'";
        List<Enrollment> list = new ArrayList<>();
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractEnrollment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Enrollment> getEnrollmentsByCourseId(String courseId) {
        String sql = "SELECT * FROM enrollment WHERE course_id=? AND status='已选'";
        List<Enrollment> list = new ArrayList<>();
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, courseId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractEnrollment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Enrollment extractEnrollment(ResultSet rs) throws SQLException {
        Enrollment e = new Enrollment();
        e.setId(rs.getInt("id"));
        e.setStudentId(rs.getString("student_id"));
        e.setCourseId(rs.getString("course_id"));
        e.setEnrollmentDate(rs.getTimestamp("enrollment_date"));
        e.setStatus(rs.getString("status"));
        return e;
    }
}