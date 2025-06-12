package dao;

import entity.TeachingTask;
import utils.db_connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TeachingTaskDaoImpl implements TeachingTaskDao {
    @Override
    public int addTeachingTask(TeachingTask task) {
        String sql = "INSERT INTO teaching_task (id, course_id, teacher_id, academic_year, semester, class_name, class_time, location, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, task.getId());
            ps.setString(2, task.getCourseId());
            ps.setString(3, task.getTeacherId());
            ps.setString(4, task.getAcademicYear());
            ps.setString(5, task.getSemester());
            ps.setString(6, task.getClassName());
            ps.setString(7, task.getClassTime());
            ps.setString(8, task.getLocation());
            ps.setString(9, task.getStatus());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int updateTeachingTask(TeachingTask task) {
        String sql = "UPDATE teaching_task SET course_id=?, teacher_id=?, academic_year=?, semester=?, class_name=?, class_time=?, location=?, status=? WHERE id=?";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, task.getCourseId());
            ps.setString(2, task.getTeacherId());
            ps.setString(3, task.getAcademicYear());
            ps.setString(4, task.getSemester());
            ps.setString(5, task.getClassName());
            ps.setString(6, task.getClassTime());
            ps.setString(7, task.getLocation());
            ps.setString(8, task.getStatus());
            ps.setString(9, task.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public TeachingTask getTeachingTaskById(String id) {
        String sql = "SELECT * FROM teaching_task WHERE id=?";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractTeachingTask(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<TeachingTask> queryTeachingTasks(String courseId, String teacherId, String academicYear, String semester, String className) {
        StringBuilder sql = new StringBuilder("SELECT * FROM teaching_task WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (courseId != null && !courseId.isEmpty()) {
            sql.append(" AND course_id=?");
            params.add(courseId);
        }
        if (teacherId != null && !teacherId.isEmpty()) {
            sql.append(" AND teacher_id=?");
            params.add(teacherId);
        }
        if (academicYear != null && !academicYear.isEmpty()) {
            sql.append(" AND academic_year=?");
            params.add(academicYear);
        }
        if (semester != null && !semester.isEmpty()) {
            sql.append(" AND semester=?");
            params.add(semester);
        }
        if (className != null && !className.isEmpty()) {
            sql.append(" AND class_name=?");
            params.add(className);
        }
        List<TeachingTask> list = new ArrayList<>();
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractTeachingTask(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<TeachingTask> queryTeachingTasksByPage(int offset, int limit) {
        String sql = "SELECT * FROM teaching_task LIMIT ?, ?";
        List<TeachingTask> list = new ArrayList<>();
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractTeachingTask(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int deleteTeachingTask(String id) {
        String sql = "UPDATE teaching_task SET status='已删除' WHERE id=?";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private TeachingTask extractTeachingTask(ResultSet rs) throws SQLException {
        TeachingTask t = new TeachingTask();
        t.setId(rs.getString("id"));
        t.setCourseId(rs.getString("course_id"));
        t.setTeacherId(rs.getString("teacher_id"));
        t.setAcademicYear(rs.getString("academic_year"));
        t.setSemester(rs.getString("semester"));
        t.setClassName(rs.getString("class_name"));
        t.setClassTime(rs.getString("class_time"));
        t.setLocation(rs.getString("location"));
        t.setStatus(rs.getString("status"));
        return t;
    }

    public static void main(String[] args) {
        TeachingTaskDao dao = new TeachingTaskDaoImpl();
        // 添加教学任务测试
        TeachingTask t = new TeachingTask();
        t.setId("TT002");
        t.setCourseId("C001");
        t.setTeacherId("T001");
        t.setAcademicYear("2024-2025");
        t.setSemester("秋");
        t.setClassName("计科2班");
        t.setClassTime("周三3-4节");
        t.setLocation("A102");
        t.setStatus("未开始");
        int addResult = dao.addTeachingTask(t);
        System.out.println("添加教学任务结果: " + addResult);

        // 查询教学任务
        TeachingTask t2 = dao.getTeachingTaskById("TT002");
        System.out.println("查询教学任务: " + t2);

        // 修改教学任务
        t2.setStatus("进行中");
        int updateResult = dao.updateTeachingTask(t2);
        System.out.println("修改教学任务结果: " + updateResult);

        // 分页查询
        System.out.println("分页查询:");
        for (TeachingTask task : dao.queryTeachingTasksByPage(0, 10)) {
            System.out.println(task);
        }

        // 条件查询
        System.out.println("条件查询:");
        for (TeachingTask task : dao.queryTeachingTasks("C001", null, null, null, null)) {
            System.out.println(task);
        }

//        // 逻辑删除
//        int delResult = dao.deleteTeachingTask("TT002");
//        System.out.println("逻辑删除结果: " + delResult);
    }
}

