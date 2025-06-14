package dao;

import entity.Grade;
import utils.db_connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    @Override
    public List<Map<String, Object>> getGradesWithCourseInfo(String studentId) {
        String sql = "SELECT g.*, t.academic_year, t.semester, c.id as course_id, c.name as course_name, c.credits " +
                     "FROM grade g " +
                     "JOIN teaching_task t ON g.teaching_task_id = t.id " +
                     "JOIN course c ON t.course_id = c.id " +
                     "WHERE g.student_id = ? AND g.status != '已删除'";
        
        List<Map<String, Object>> resultList = new ArrayList<>();
        
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentId);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> gradeInfo = new HashMap<>();
                
                // 添加Grade对象
                Grade grade = extractGrade(rs);
                gradeInfo.put("grade", grade);
                
                // 添加课程信息
                gradeInfo.put("courseId", rs.getString("course_id"));
                gradeInfo.put("courseName", rs.getString("course_name"));
                gradeInfo.put("credits", rs.getDouble("credits"));
                gradeInfo.put("academicYear", rs.getString("academic_year"));
                gradeInfo.put("semester", rs.getString("semester"));
                
                resultList.add(gradeInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return resultList;
    }
    
    @Override
    public List<Map<String, Object>> getClassGradeSummary(String className, String academicYear, String semester) {
        StringBuilder sql = new StringBuilder(
            "SELECT s.id as student_id, s.name as student_name, s.class_name, s.major, " +
            "g.*, t.academic_year, t.semester, c.id as course_id, c.name as course_name, c.credits " +
            "FROM student s " +
            "LEFT JOIN grade g ON s.id = g.student_id AND g.status != '已删除' " +
            "LEFT JOIN teaching_task t ON g.teaching_task_id = t.id " +
            "LEFT JOIN course c ON t.course_id = c.id " +
            "WHERE s.class_name = ? AND s.status != '已删除'"
        );
        
        List<Object> params = new ArrayList<>();
        params.add(className);
        
        if (academicYear != null && !academicYear.isEmpty()) {
            sql.append(" AND t.academic_year = ?");
            params.add(academicYear);
        }
        
        if (semester != null && !semester.isEmpty()) {
            sql.append(" AND t.semester = ?");
            params.add(semester);
        }
        
        sql.append(" ORDER BY s.id, c.name");
        
        List<Map<String, Object>> resultList = new ArrayList<>();
        
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> record = new HashMap<>();
                
                // 学生信息
                record.put("studentId", rs.getString("student_id"));
                record.put("studentName", rs.getString("student_name"));
                record.put("className", rs.getString("class_name"));
                record.put("major", rs.getString("major"));
                
                // 课程信息
                record.put("courseId", rs.getString("course_id"));
                record.put("courseName", rs.getString("course_name"));
                record.put("credits", rs.getObject("credits", Double.class));
                record.put("academicYear", rs.getString("academic_year"));
                record.put("semester", rs.getString("semester"));
                
                // 成绩信息
                Integer gradeId = rs.getObject("id", Integer.class);
                if (gradeId != null) {
                    Grade grade = new Grade();
                    grade.setId(gradeId);
                    grade.setStudentId(rs.getString("student_id"));
                    grade.setTeachingTaskId(rs.getString("teaching_task_id"));
                    grade.setRegularScore(rs.getObject("regular_score", Double.class));
                    grade.setMidtermScore(rs.getObject("midterm_score", Double.class));
                    grade.setFinalScore(rs.getObject("final_score", Double.class));
                    grade.setTotalScore(rs.getObject("total_score", Double.class));
                    grade.setEntryTime(rs.getTimestamp("entry_time"));
                    grade.setEntryBy(rs.getString("entry_by"));
                    grade.setLastModifiedTime(rs.getTimestamp("last_modified_time"));
                    grade.setLastModifiedBy(rs.getString("last_modified_by"));
                    grade.setModificationReason(rs.getString("modification_reason"));
                    grade.setStatus(rs.getString("status"));
                    record.put("grade", grade);
                    record.put("totalScore", rs.getObject("total_score", Double.class));
                } else {
                    record.put("grade", null);
                    record.put("totalScore", null);
                }
                
                resultList.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return resultList;
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
    
    @Override
    public List<Map<String, Object>> getCourseGradeSummary(String courseId, String academicYear, String semester) {
        StringBuilder sql = new StringBuilder(
            "SELECT s.id as student_id, s.name as student_name, s.class_name, s.major, " +
            "g.*, t.academic_year, t.semester, c.id as course_id, c.name as course_name, c.credits " +
            "FROM grade g " +
            "JOIN teaching_task t ON g.teaching_task_id = t.id " +
            "JOIN course c ON t.course_id = c.id " +
            "JOIN student s ON g.student_id = s.id " +
            "WHERE c.id = ? AND g.status != '已删除' AND s.status != '已删除'"
        );
        
        List<Object> params = new ArrayList<>();
        params.add(courseId);
        
        if (academicYear != null && !academicYear.isEmpty()) {
            sql.append(" AND t.academic_year = ?");
            params.add(academicYear);
        }
        
        if (semester != null && !semester.isEmpty()) {
            sql.append(" AND t.semester = ?");
            params.add(semester);
        }
        
        sql.append(" ORDER BY s.id, t.academic_year, t.semester");
        
        List<Map<String, Object>> result = new ArrayList<>();
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> record = new HashMap<>();
                    record.put("studentId", rs.getString("student_id"));
                    record.put("studentName", rs.getString("student_name"));
                    record.put("className", rs.getString("class_name"));
                    record.put("major", rs.getString("major"));
                    record.put("courseId", rs.getString("course_id"));
                    record.put("courseName", rs.getString("course_name"));
                    record.put("credits", rs.getDouble("credits"));
                    record.put("academicYear", rs.getString("academic_year"));
                    record.put("semester", rs.getString("semester"));
                    record.put("regularScore", rs.getObject("regular_score"));
                    record.put("midtermScore", rs.getObject("midterm_score"));
                    record.put("finalScore", rs.getObject("final_score"));
                    record.put("totalScore", rs.getObject("total_score"));
                    result.add(record);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    @Override
    public Map<String, Object> getCourseGradeStatistics(String courseId, String academicYear, String semester) {
        StringBuilder sql = new StringBuilder(
            "SELECT " +
            "COUNT(*) as total_count, " +
            "AVG(g.total_score) as average_score, " +
            "MAX(g.total_score) as highest_score, " +
            "MIN(g.total_score) as lowest_score, " +
            "SUM(CASE WHEN g.total_score >= 60 THEN 1 ELSE 0 END) as pass_count, " +
            "SUM(CASE WHEN g.total_score >= 85 THEN 1 ELSE 0 END) as excellent_count, " +
            "c.name as course_name " +
            "FROM grade g " +
            "JOIN teaching_task t ON g.teaching_task_id = t.id " +
            "JOIN course c ON t.course_id = c.id " +
            "WHERE c.id = ? AND g.status != '已删除' AND g.total_score IS NOT NULL"
        );
        
        List<Object> params = new ArrayList<>();
        params.add(courseId);
        
        if (academicYear != null && !academicYear.isEmpty()) {
            sql.append(" AND t.academic_year = ?");
            params.add(academicYear);
        }
        
        if (semester != null && !semester.isEmpty()) {
            sql.append(" AND t.semester = ?");
            params.add(semester);
        }
        
        sql.append(" GROUP BY c.id, c.name");
        
        Map<String, Object> statistics = new HashMap<>();
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int totalCount = rs.getInt("total_count");
                    int passCount = rs.getInt("pass_count");
                    int excellentCount = rs.getInt("excellent_count");
                    
                    statistics.put("courseId", courseId);
                    statistics.put("courseName", rs.getString("course_name"));
                    statistics.put("academicYear", academicYear);
                    statistics.put("semester", semester);
                    statistics.put("totalCount", totalCount);
                    statistics.put("averageScore", Math.round(rs.getDouble("average_score") * 100.0) / 100.0);
                    statistics.put("highestScore", rs.getDouble("highest_score"));
                    statistics.put("lowestScore", rs.getDouble("lowest_score"));
                    statistics.put("passCount", passCount);
                    statistics.put("excellentCount", excellentCount);
                    statistics.put("passRate", totalCount > 0 ? Math.round((double) passCount / totalCount * 10000.0) / 100.0 : 0.0);
                    statistics.put("excellentRate", totalCount > 0 ? Math.round((double) excellentCount / totalCount * 10000.0) / 100.0 : 0.0);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statistics;
    }
    
    @Override
    public Map<String, Object> getClassGradeStatistics(String className, String academicYear, String semester) {
        StringBuilder sql = new StringBuilder(
            "SELECT " +
            "COUNT(*) as total_count, " +
            "AVG(g.total_score) as average_score, " +
            "MAX(g.total_score) as highest_score, " +
            "MIN(g.total_score) as lowest_score, " +
            "SUM(CASE WHEN g.total_score >= 60 THEN 1 ELSE 0 END) as pass_count, " +
            "SUM(CASE WHEN g.total_score >= 85 THEN 1 ELSE 0 END) as excellent_count, " +
            "s.class_name " +
            "FROM grade g " +
            "JOIN teaching_task t ON g.teaching_task_id = t.id " +
            "JOIN student s ON g.student_id = s.id " +
            "WHERE s.class_name = ? AND g.status != '已删除' AND g.total_score IS NOT NULL"
        );
        
        List<Object> params = new ArrayList<>();
        params.add(className);
        
        if (academicYear != null && !academicYear.isEmpty()) {
            sql.append(" AND t.academic_year = ?");
            params.add(academicYear);
        }
        
        if (semester != null && !semester.isEmpty()) {
            sql.append(" AND t.semester = ?");
            params.add(semester);
        }
        
        sql.append(" GROUP BY s.class_name");
        
        Map<String, Object> statistics = new HashMap<>();
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int totalCount = rs.getInt("total_count");
                    int passCount = rs.getInt("pass_count");
                    int excellentCount = rs.getInt("excellent_count");
                    
                    statistics.put("className", className);
                    statistics.put("academicYear", academicYear);
                    statistics.put("semester", semester);
                    statistics.put("totalCount", totalCount);
                    statistics.put("averageScore", Math.round(rs.getDouble("average_score") * 100.0) / 100.0);
                    statistics.put("highestScore", rs.getDouble("highest_score"));
                    statistics.put("lowestScore", rs.getDouble("lowest_score"));
                    statistics.put("passCount", passCount);
                    statistics.put("excellentCount", excellentCount);
                    statistics.put("passRate", totalCount > 0 ? Math.round((double) passCount / totalCount * 10000.0) / 100.0 : 0.0);
                    statistics.put("excellentRate", totalCount > 0 ? Math.round((double) excellentCount / totalCount * 10000.0) / 100.0 : 0.0);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return statistics;
    }
    
    @Override
    public List<Map<String, Object>> getClassGradeRanking(String className, String academicYear, String semester, String rankType, String courseId) {
        List<Map<String, Object>> rankings = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();
        
        if ("total".equals(rankType)) {
            // 按总分排名
            sql.append("SELECT s.id as student_id, s.name as student_name, s.class_name, ")
               .append("SUM(g.total_score) as total_score, COUNT(g.id) as course_count, ")
               .append("ROUND(AVG(g.total_score), 2) as average_score ")
               .append("FROM student s ")
               .append("JOIN grade g ON s.id = g.student_id ")
               .append("JOIN teaching_task tt ON g.teaching_task_id = tt.id ")
               .append("WHERE s.class_name = ? AND g.status IN ('公示中', '已归档') ");
            params.add(className);
            
            if (academicYear != null && !academicYear.trim().isEmpty()) {
                sql.append("AND tt.academic_year = ? ");
                params.add(academicYear);
            }
            if (semester != null && !semester.trim().isEmpty()) {
                sql.append("AND tt.semester = ? ");
                params.add(semester);
            }
            
            sql.append("GROUP BY s.id, s.name, s.class_name ")
               .append("ORDER BY total_score DESC, average_score DESC");
               
        } else if ("average".equals(rankType)) {
            // 按平均分排名
            sql.append("SELECT s.id as student_id, s.name as student_name, s.class_name, ")
               .append("SUM(g.total_score) as total_score, COUNT(g.id) as course_count, ")
               .append("ROUND(AVG(g.total_score), 2) as average_score ")
               .append("FROM student s ")
               .append("JOIN grade g ON s.id = g.student_id ")
               .append("JOIN teaching_task tt ON g.teaching_task_id = tt.id ")
               .append("WHERE s.class_name = ? AND g.status IN ('公示中', '已归档') ");
            params.add(className);
            
            if (academicYear != null && !academicYear.trim().isEmpty()) {
                sql.append("AND tt.academic_year = ? ");
                params.add(academicYear);
            }
            if (semester != null && !semester.trim().isEmpty()) {
                sql.append("AND tt.semester = ? ");
                params.add(semester);
            }
            
            sql.append("GROUP BY s.id, s.name, s.class_name ")
               .append("ORDER BY average_score DESC, total_score DESC");
               
        } else if ("course".equals(rankType) && courseId != null) {
            // 按单科成绩排名
            sql.append("SELECT s.id as student_id, s.name as student_name, s.class_name, ")
               .append("c.id as course_id, c.name as course_name, g.total_score, ")
               .append("g.regular_score, g.midterm_score, g.final_score ")
               .append("FROM student s ")
               .append("JOIN grade g ON s.id = g.student_id ")
               .append("JOIN teaching_task tt ON g.teaching_task_id = tt.id ")
               .append("JOIN course c ON tt.course_id = c.id ")
               .append("WHERE s.class_name = ? AND c.id = ? AND g.status IN ('公示中', '已归档') ");
            params.add(className);
            params.add(courseId);
            
            if (academicYear != null && !academicYear.trim().isEmpty()) {
                sql.append("AND tt.academic_year = ? ");
                params.add(academicYear);
            }
            if (semester != null && !semester.trim().isEmpty()) {
                sql.append("AND tt.semester = ? ");
                params.add(semester);
            }
            
            sql.append("ORDER BY g.total_score DESC");
        }
        
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                int rank = 1;
                while (rs.next()) {
                    Map<String, Object> ranking = new HashMap<>();
                    ranking.put("rank", rank++);
                    ranking.put("studentId", rs.getString("student_id"));
                    ranking.put("studentName", rs.getString("student_name"));
                    ranking.put("className", rs.getString("class_name"));
                    
                    if ("course".equals(rankType)) {
                        ranking.put("courseId", rs.getString("course_id"));
                        ranking.put("courseName", rs.getString("course_name"));
                        ranking.put("totalScore", rs.getDouble("total_score"));
                        ranking.put("regularScore", rs.getObject("regular_score"));
                        ranking.put("midtermScore", rs.getObject("midterm_score"));
                        ranking.put("finalScore", rs.getObject("final_score"));
                    } else {
                        ranking.put("totalScore", rs.getDouble("total_score"));
                        ranking.put("courseCount", rs.getInt("course_count"));
                        ranking.put("averageScore", rs.getDouble("average_score"));
                    }
                    
                    rankings.add(ranking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return rankings;
    }
    
    @Override
    public List<Map<String, Object>> getSchoolGradeRanking(String academicYear, String semester, String rankType, String courseId, Integer limit) {
        List<Map<String, Object>> rankings = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();
        
        if ("total".equals(rankType)) {
            // 按总分排名
            sql.append("SELECT s.id as student_id, s.name as student_name, s.class_name, s.major, ")
               .append("SUM(g.total_score) as total_score, COUNT(g.id) as course_count, ")
               .append("ROUND(AVG(g.total_score), 2) as average_score ")
               .append("FROM student s ")
               .append("JOIN grade g ON s.id = g.student_id ")
               .append("JOIN teaching_task tt ON g.teaching_task_id = tt.id ")
               .append("WHERE g.status IN ('公示中', '已归档') ");
            
            if (academicYear != null && !academicYear.trim().isEmpty()) {
                sql.append("AND tt.academic_year = ? ");
                params.add(academicYear);
            }
            if (semester != null && !semester.trim().isEmpty()) {
                sql.append("AND tt.semester = ? ");
                params.add(semester);
            }
            
            sql.append("GROUP BY s.id, s.name, s.class_name, s.major ")
               .append("ORDER BY total_score DESC, average_score DESC");
               
        } else if ("average".equals(rankType)) {
            // 按平均分排名
            sql.append("SELECT s.id as student_id, s.name as student_name, s.class_name, s.major, ")
               .append("SUM(g.total_score) as total_score, COUNT(g.id) as course_count, ")
               .append("ROUND(AVG(g.total_score), 2) as average_score ")
               .append("FROM student s ")
               .append("JOIN grade g ON s.id = g.student_id ")
               .append("JOIN teaching_task tt ON g.teaching_task_id = tt.id ")
               .append("WHERE g.status IN ('公示中', '已归档') ");
            
            if (academicYear != null && !academicYear.trim().isEmpty()) {
                sql.append("AND tt.academic_year = ? ");
                params.add(academicYear);
            }
            if (semester != null && !semester.trim().isEmpty()) {
                sql.append("AND tt.semester = ? ");
                params.add(semester);
            }
            
            sql.append("GROUP BY s.id, s.name, s.class_name, s.major ")
               .append("ORDER BY average_score DESC, total_score DESC");
               
        } else if ("course".equals(rankType) && courseId != null) {
            // 按单科成绩排名
            sql.append("SELECT s.id as student_id, s.name as student_name, s.class_name, s.major, ")
               .append("c.id as course_id, c.name as course_name, g.total_score, ")
               .append("g.regular_score, g.midterm_score, g.final_score ")
               .append("FROM student s ")
               .append("JOIN grade g ON s.id = g.student_id ")
               .append("JOIN teaching_task tt ON g.teaching_task_id = tt.id ")
               .append("JOIN course c ON tt.course_id = c.id ")
               .append("WHERE c.id = ? AND g.status IN ('公示中', '已归档') ");
            params.add(courseId);
            
            if (academicYear != null && !academicYear.trim().isEmpty()) {
                sql.append("AND tt.academic_year = ? ");
                params.add(academicYear);
            }
            if (semester != null && !semester.trim().isEmpty()) {
                sql.append("AND tt.semester = ? ");
                params.add(semester);
            }
            
            sql.append("ORDER BY g.total_score DESC");
        }
        
        if (limit != null && limit > 0) {
            sql.append(" LIMIT ?");
            params.add(limit);
        }
        
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                int rank = 1;
                while (rs.next()) {
                    Map<String, Object> ranking = new HashMap<>();
                    ranking.put("rank", rank++);
                    ranking.put("studentId", rs.getString("student_id"));
                    ranking.put("studentName", rs.getString("student_name"));
                    ranking.put("className", rs.getString("class_name"));
                    ranking.put("major", rs.getString("major"));
                    
                    if ("course".equals(rankType)) {
                        ranking.put("courseId", rs.getString("course_id"));
                        ranking.put("courseName", rs.getString("course_name"));
                        ranking.put("totalScore", rs.getDouble("total_score"));
                        ranking.put("regularScore", rs.getObject("regular_score"));
                        ranking.put("midtermScore", rs.getObject("midterm_score"));
                        ranking.put("finalScore", rs.getObject("final_score"));
                    } else {
                        ranking.put("totalScore", rs.getDouble("total_score"));
                        ranking.put("courseCount", rs.getInt("course_count"));
                        ranking.put("averageScore", rs.getDouble("average_score"));
                    }
                    
                    rankings.add(ranking);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return rankings;
    }
    
    @Override
    public List<Map<String, Object>> getFailingStudents(String className, String academicYear, String semester, String courseId) {
        List<Map<String, Object>> failingStudents = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();
        
        sql.append("SELECT s.id as student_id, s.name as student_name, s.class_name, s.major, ")
           .append("c.id as course_id, c.name as course_name, g.total_score, ")
           .append("g.regular_score, g.midterm_score, g.final_score, ")
           .append("tt.academic_year, tt.semester ")
           .append("FROM student s ")
           .append("JOIN grade g ON s.id = g.student_id ")
           .append("JOIN teaching_task tt ON g.teaching_task_id = tt.id ")
           .append("JOIN course c ON tt.course_id = c.id ")
           .append("WHERE g.total_score < 60 AND g.status IN ('公示中', '已归档') ");
        
        if (className != null && !className.trim().isEmpty()) {
            sql.append("AND s.class_name = ? ");
            params.add(className);
        }
        
        if (academicYear != null && !academicYear.trim().isEmpty()) {
            sql.append("AND tt.academic_year = ? ");
            params.add(academicYear);
        }
        
        if (semester != null && !semester.trim().isEmpty()) {
            sql.append("AND tt.semester = ? ");
            params.add(semester);
        }
        
        if (courseId != null && !courseId.trim().isEmpty()) {
            sql.append("AND c.id = ? ");
            params.add(courseId);
        }
        
        sql.append("ORDER BY s.class_name, s.id, c.name");
        
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> student = new HashMap<>();
                    student.put("studentId", rs.getString("student_id"));
                    student.put("studentName", rs.getString("student_name"));
                    student.put("className", rs.getString("class_name"));
                    student.put("major", rs.getString("major"));
                    student.put("courseId", rs.getString("course_id"));
                    student.put("courseName", rs.getString("course_name"));
                    student.put("totalScore", rs.getDouble("total_score"));
                    student.put("regularScore", rs.getObject("regular_score"));
                    student.put("midtermScore", rs.getObject("midterm_score"));
                    student.put("finalScore", rs.getObject("final_score"));
                    student.put("academicYear", rs.getString("academic_year"));
                    student.put("semester", rs.getString("semester"));
                    
                    failingStudents.add(student);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return failingStudents;
    }
    
    @Override
    public List<String> getAllClassNames() {
        List<String> classNames = new ArrayList<>();
        String sql = "SELECT DISTINCT class_name FROM student WHERE status != '已删除' ORDER BY class_name";
        
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                String className = rs.getString("class_name");
                if (className != null && !className.trim().isEmpty()) {
                    classNames.add(className);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return classNames;
    }
    
    @Override
    public List<String> getAllAcademicYears() {
        List<String> academicYears = new ArrayList<>();
        String sql = "SELECT DISTINCT academic_year FROM teaching_task WHERE academic_year IS NOT NULL ORDER BY academic_year DESC";
        
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                String academicYear = rs.getString("academic_year");
                if (academicYear != null && !academicYear.trim().isEmpty()) {
                    academicYears.add(academicYear);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return academicYears;
    }
    
    @Override
    public List<Map<String, Object>> getAllCourses() {
        List<Map<String, Object>> courses = new ArrayList<>();
        String sql = "SELECT id, name FROM course ORDER BY id";
        
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> course = new HashMap<>();
                course.put("id", rs.getString("id"));
                course.put("name", rs.getString("name"));
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return courses;
    }
    
    @Override
    public List<Map<String, Object>> getExcellentStudents(String className, String academicYear, String semester, String courseId) {
        List<Map<String, Object>> excellentStudents = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder(
            "SELECT DISTINCT " +
            "s.id as student_id, " +
            "s.name as student_name, " +
            "s.class_name, " +
            "s.major, " +
            "c.id as course_id, " +
            "c.name as course_name, " +
            "c.credits, " +
            "t.academic_year, " +
            "t.semester, " +
            "g.regular_score, " +
            "g.midterm_score, " +
            "g.final_score, " +
            "g.total_score " +
            "FROM grade g " +
            "JOIN teaching_task t ON g.teaching_task_id = t.id " +
            "JOIN student s ON g.student_id = s.id " +
            "JOIN course c ON t.course_id = c.id " +
            "WHERE g.status != '已删除' AND g.total_score >= 85 "
        );
        
        List<Object> params = new ArrayList<>();
        
        // 添加查询条件
        if (className != null && !className.trim().isEmpty()) {
            sql.append(" AND s.class_name = ?");
            params.add(className.trim());
        }
        
        if (academicYear != null && !academicYear.trim().isEmpty()) {
            sql.append(" AND t.academic_year = ?");
            params.add(academicYear.trim());
        }
        
        if (semester != null && !semester.trim().isEmpty()) {
            sql.append(" AND t.semester = ?");
            params.add(semester.trim());
        }
        
        if (courseId != null && !courseId.trim().isEmpty()) {
            sql.append(" AND c.id = ?");
            params.add(courseId.trim());
        }
        
        sql.append(" ORDER BY g.total_score DESC, s.class_name, s.name");
        
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            
            // 设置参数
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> student = new HashMap<>();
                    student.put("studentId", rs.getString("student_id"));
                    student.put("studentName", rs.getString("student_name"));
                    student.put("className", rs.getString("class_name"));
                    student.put("major", rs.getString("major"));
                    student.put("courseId", rs.getString("course_id"));
                    student.put("courseName", rs.getString("course_name"));
                    student.put("credits", rs.getDouble("credits"));
                    student.put("academicYear", rs.getString("academic_year"));
                    student.put("semester", rs.getString("semester"));
                    student.put("regularScore", rs.getObject("regular_score"));
                    student.put("midtermScore", rs.getObject("midterm_score"));
                    student.put("finalScore", rs.getObject("final_score"));
                    student.put("totalScore", rs.getObject("total_score"));
                    
                    excellentStudents.add(student);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return excellentStudents;
    }
    
    @Override
    public List<Map<String, Object>> getGradeTrendAnalysis(String analysisType, String targetId) {
        List<Map<String, Object>> trendData = new ArrayList<>();
        
        String sql = "";
        List<Object> params = new ArrayList<>();
        
        switch (analysisType) {
            case "class":
                sql = "SELECT " +
                      "t.academic_year, " +
                      "t.semester, " +
                      "ROUND(AVG(g.total_score), 2) as avg_score, " +
                      "ROUND(COUNT(CASE WHEN g.total_score >= 60 THEN 1 END) * 100.0 / COUNT(*), 2) as pass_rate, " +
                      "ROUND(COUNT(CASE WHEN g.total_score >= 85 THEN 1 END) * 100.0 / COUNT(*), 2) as excellent_rate, " +
                      "COUNT(*) as total_count " +
                      "FROM grade g " +
                      "JOIN teaching_task t ON g.teaching_task_id = t.id " +
                      "JOIN student s ON g.student_id = s.id " +
                      "WHERE g.status != '已删除' AND s.class_name = ? " +
                      "GROUP BY t.academic_year, t.semester " +
                      "ORDER BY t.academic_year, t.semester";
                params.add(targetId);
                break;
                
            case "course":
                sql = "SELECT " +
                      "t.academic_year, " +
                      "t.semester, " +
                      "ROUND(AVG(g.total_score), 2) as avg_score, " +
                      "ROUND(COUNT(CASE WHEN g.total_score >= 60 THEN 1 END) * 100.0 / COUNT(*), 2) as pass_rate, " +
                      "ROUND(COUNT(CASE WHEN g.total_score >= 85 THEN 1 END) * 100.0 / COUNT(*), 2) as excellent_rate, " +
                      "COUNT(*) as total_count " +
                      "FROM grade g " +
                      "JOIN teaching_task t ON g.teaching_task_id = t.id " +
                      "JOIN course c ON t.course_id = c.id " +
                      "WHERE g.status != '已删除' AND c.id = ? " +
                      "GROUP BY t.academic_year, t.semester " +
                      "ORDER BY t.academic_year, t.semester";
                params.add(targetId);
                break;
                
            case "student":
                sql = "SELECT " +
                      "t.academic_year, " +
                      "t.semester, " +
                      "ROUND(AVG(g.total_score), 2) as avg_score, " +
                      "ROUND(COUNT(CASE WHEN g.total_score >= 60 THEN 1 END) * 100.0 / COUNT(*), 2) as pass_rate, " +
                      "ROUND(COUNT(CASE WHEN g.total_score >= 85 THEN 1 END) * 100.0 / COUNT(*), 2) as excellent_rate, " +
                      "COUNT(*) as total_count " +
                      "FROM grade g " +
                      "JOIN teaching_task t ON g.teaching_task_id = t.id " +
                      "WHERE g.status != '已删除' AND g.student_id = ? " +
                      "GROUP BY t.academic_year, t.semester " +
                      "ORDER BY t.academic_year, t.semester";
                params.add(targetId);
                break;
                
            default:
                return trendData; // 返回空列表
        }
        
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // 设置参数
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> trend = new HashMap<>();
                    trend.put("academicYear", rs.getString("academic_year"));
                    trend.put("semester", rs.getString("semester"));
                    trend.put("avgScore", rs.getDouble("avg_score"));
                    trend.put("passRate", rs.getDouble("pass_rate"));
                    trend.put("excellentRate", rs.getDouble("excellent_rate"));
                    trend.put("totalCount", rs.getInt("total_count"));
                    
                    trendData.add(trend);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return trendData;
    }
}
