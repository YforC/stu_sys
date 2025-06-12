package dao;

import entity.Grade;
import utils.db_connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GradeDaoImpl implements GradeDao {
    @Override
    public int addGrade(Grade grade) {
        // 自动计算总评成绩：50%期末+30%期中+20%平时
        if (grade.getFinalScore() != null && grade.getMidtermScore() != null && grade.getRegularScore() != null) {
            double totalScore = grade.getFinalScore() * 0.5 +
                    grade.getMidtermScore() * 0.3 +
                    grade.getRegularScore() * 0.2;
            grade.setTotalScore(Math.round(totalScore * 100.0) / 100.0); // 保留两位小数
        }

        String sql = "INSERT INTO grade (student_id, teaching_task_id, regular_score, midterm_score, final_score, total_score, entry_time, entry_by, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, grade.getStudentId());
            ps.setString(2, grade.getTeachingTaskId());
            ps.setObject(3, grade.getRegularScore());
            ps.setObject(4, grade.getMidtermScore());
            ps.setObject(5, grade.getFinalScore());
            ps.setObject(6, grade.getTotalScore());
            ps.setTimestamp(7, new Timestamp(grade.getEntryTime().getTime()));
            ps.setString(8, grade.getEntryBy());
            ps.setString(9, grade.getStatus());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int updateGrade(Grade grade, String modifiedBy, String reason) {
        // 自动计算总评成绩：50%期末+30%期中+20%平时
        if (grade.getFinalScore() != null && grade.getMidtermScore() != null && grade.getRegularScore() != null) {
            double totalScore = grade.getFinalScore() * 0.5 +
                    grade.getMidtermScore() * 0.3 +
                    grade.getRegularScore() * 0.2;
            grade.setTotalScore(Math.round(totalScore * 100.0) / 100.0); // 保留两位小数
        }

        String sql = "UPDATE grade SET regular_score=?, midterm_score=?, final_score=?, total_score=?, last_modified_time=?, last_modified_by=?, modification_reason=?, status=? WHERE id=?";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, grade.getRegularScore());
            ps.setDouble(2, grade.getMidtermScore());
            ps.setDouble(3, grade.getFinalScore());
            ps.setDouble(4, grade.getTotalScore());
            ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            ps.setString(6, modifiedBy);
            ps.setString(7, reason);
            ps.setString(8, grade.getStatus());
            ps.setInt(9, grade.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Grade getGradeById(Integer id) {
        String sql = "SELECT * FROM grade WHERE id=?";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractGrade(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Grade> queryGrades(String studentId, String teachingTaskId, String academicYear, String semester) {
        StringBuilder sql = new StringBuilder("SELECT g.* FROM grade g LEFT JOIN teaching_task t ON g.teaching_task_id = t.id WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (studentId != null && !studentId.isEmpty()) {
            sql.append(" AND g.student_id=?");
            params.add(studentId);
        }
        if (teachingTaskId != null && !teachingTaskId.isEmpty()) {
            sql.append(" AND g.teaching_task_id=?");
            params.add(teachingTaskId);
        }
        if (academicYear != null && !academicYear.isEmpty()) {
            sql.append(" AND t.academic_year=?");
            params.add(academicYear);
        }
        if (semester != null && !semester.isEmpty()) {
            sql.append(" AND t.semester=?");
            params.add(semester);
        }
        List<Grade> list = new ArrayList<>();
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractGrade(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Grade> queryGradesByPage(int offset, int limit) {
        String sql = "SELECT * FROM grade LIMIT ?, ?";
        List<Grade> list = new ArrayList<>();
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractGrade(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int deleteGrade(Integer id) {
        String sql = "UPDATE grade SET status='已删除' WHERE id=?";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public List<Grade> getGradeHistory(Integer id) {
        // 假设有grade_history表，若无则可返回单条修改记录
        // 这里只返回当前grade表的修改信息
        List<Grade> history = new ArrayList<>();
        Grade g = getGradeById(id);
        if (g != null) {
            history.add(g);
        }
        return history;
    }

    private Grade extractGrade(ResultSet rs) throws SQLException {
        Grade g = new Grade();
        g.setId(rs.getInt("id"));
        g.setStudentId(rs.getString("student_id"));
        g.setTeachingTaskId(rs.getString("teaching_task_id"));
        g.setRegularScore(rs.getDouble("regular_score"));
        g.setMidtermScore(rs.getDouble("midterm_score"));
        g.setFinalScore(rs.getDouble("final_score"));
        g.setTotalScore(rs.getDouble("total_score"));
        g.setEntryTime(rs.getTimestamp("entry_time"));
        g.setEntryBy(rs.getString("entry_by"));
        g.setLastModifiedTime(rs.getTimestamp("last_modified_time"));
        g.setLastModifiedBy(rs.getString("last_modified_by"));
        g.setModificationReason(rs.getString("modification_reason"));
        g.setStatus(rs.getString("status"));
        return g;
    }

    public static void main(String[] args) {
        GradeDao dao = new GradeDaoImpl();
        // 添加成绩测试
        Grade g = new Grade();
        g.setStudentId("S001");
        g.setTeachingTaskId("TT001");
        g.setRegularScore(90.0);
        g.setMidtermScore(88.0);
        g.setFinalScore(92.0);
        g.setTotalScore(90.0);
        g.setEntryTime(new Date());
        g.setEntryBy("T001");
        g.setStatus("公示中");
        int addResult = dao.addGrade(g);
        System.out.println("添加成绩结果: " + addResult);

        // 查询成绩
        List<Grade> grades = dao.queryGrades("S001", null, null, null);
        for (Grade grade : grades) {
            System.out.println("查询成绩: " + grade);
        }

        // 修改成绩
        if (!grades.isEmpty()) {
            Grade toUpdate = grades.get(0);
            toUpdate.setFinalScore(95.0);
            toUpdate.setTotalScore(91.0);
            int updateResult = dao.updateGrade(toUpdate, "T001", "修正期末成绩");
            System.out.println("修改成绩结果: " + updateResult);
        }

        // 分页查询
        System.out.println("分页查询:");
        for (Grade grade : dao.queryGradesByPage(0, 10)) {
            System.out.println(grade);
        }

        // 查询成绩历史
        if (!grades.isEmpty()) {
            System.out.println("成绩历史:");
            for (Grade h : dao.getGradeHistory(grades.get(0).getId())) {
                System.out.println(h);
            }
        }

        // 逻辑删除
//        if (!grades.isEmpty()) {
//            int delResult = dao.deleteGrade(grades.get(0).getId());
//            System.out.println("逻辑删除结果: " + delResult);
//        }
    }
}
