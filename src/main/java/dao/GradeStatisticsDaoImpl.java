package dao;

import entity.GradeStatistics;
import utils.db_connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GradeStatisticsDaoImpl implements GradeStatisticsDao {
    @Override
    public int addGradeStatistics(GradeStatistics statistics) {
        String sql = "INSERT INTO grade_statistics (object_id, type, academic_year, semester, average_score, highest_score, lowest_score, pass_count, pass_rate, excellent_count, excellent_rate, statistics_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, statistics.getObjectId());
            ps.setString(2, statistics.getType());
            ps.setString(3, statistics.getAcademicYear());
            ps.setString(4, statistics.getSemester());
            ps.setDouble(5, statistics.getAverageScore());
            ps.setDouble(6, statistics.getHighestScore());
            ps.setDouble(7, statistics.getLowestScore());
            ps.setInt(8, statistics.getPassCount());
            ps.setDouble(9, statistics.getPassRate());
            ps.setInt(10, statistics.getExcellentCount());
            ps.setDouble(11, statistics.getExcellentRate());
            ps.setTimestamp(12, new Timestamp(statistics.getStatisticsTime().getTime()));
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public GradeStatistics getStatisticsById(Integer id) {
        String sql = "SELECT * FROM grade_statistics WHERE id=?";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractStatistics(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<GradeStatistics> queryStatistics(String objectId, String type, String academicYear, String semester) {
        StringBuilder sql = new StringBuilder("SELECT * FROM grade_statistics WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (objectId != null && !objectId.isEmpty()) {
            sql.append(" AND object_id=?");
            params.add(objectId);
        }
        if (type != null && !type.isEmpty()) {
            sql.append(" AND type=?");
            params.add(type);
        }
        if (academicYear != null && !academicYear.isEmpty()) {
            sql.append(" AND academic_year=?");
            params.add(academicYear);
        }
        if (semester != null && !semester.isEmpty()) {
            sql.append(" AND semester=?");
            params.add(semester);
        }
        List<GradeStatistics> list = new ArrayList<>();
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractStatistics(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<GradeStatistics> queryStatisticsByPage(int offset, int limit) {
        String sql = "SELECT * FROM grade_statistics LIMIT ?, ?";
        List<GradeStatistics> list = new ArrayList<>();
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractStatistics(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private GradeStatistics extractStatistics(ResultSet rs) throws SQLException {
        GradeStatistics s = new GradeStatistics();
        s.setId(rs.getInt("id"));
        s.setObjectId(rs.getString("object_id"));
        s.setType(rs.getString("type"));
        s.setAcademicYear(rs.getString("academic_year"));
        s.setSemester(rs.getString("semester"));
        s.setAverageScore(rs.getDouble("average_score"));
        s.setHighestScore(rs.getDouble("highest_score"));
        s.setLowestScore(rs.getDouble("lowest_score"));
        s.setPassCount(rs.getInt("pass_count"));
        s.setPassRate(rs.getDouble("pass_rate"));
        s.setExcellentCount(rs.getInt("excellent_count"));
        s.setExcellentRate(rs.getDouble("excellent_rate"));
        s.setStatisticsTime(rs.getTimestamp("statistics_time"));
        return s;
    }
    public static void main(String[] args) {
        GradeStatisticsDao dao = new GradeStatisticsDaoImpl();
        // 添加统计记录测试
        GradeStatistics s = new GradeStatistics();
        s.setObjectId("C001");
        s.setType("课程统计");
        s.setAcademicYear("2023-2024");
        s.setSemester("春");
        s.setAverageScore(88.0);
        s.setHighestScore(95.0);
        s.setLowestScore(70.0);
        s.setPassCount(30);
        s.setPassRate(96.7);
        s.setExcellentCount(10);
        s.setExcellentRate(32.3);
        s.setStatisticsTime(new Date());
        int addResult = dao.addGradeStatistics(s);
        System.out.println("添加统计记录结果: " + addResult);

        // 查询统计记录
        List<GradeStatistics> stats = dao.queryStatistics("C001", "课程统计", "2023-2024", "春");
        for (GradeStatistics stat : stats) {
            System.out.println("查询统计记录: " + stat);
        }

        // 分页查询
        System.out.println("分页查询:");
        for (GradeStatistics stat : dao.queryStatisticsByPage(0, 10)) {
            System.out.println(stat);
        }
    }
}

