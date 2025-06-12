package dao;

import entity.Teacher;
import utils.db_connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TeacherDaoImpl implements TeacherDao {
    @Override
    public int addTeacher(Teacher teacher) {
        String sql = "INSERT INTO teacher (id, name, gender, birth_date, title, college, major, contact_info, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, teacher.getId());
            ps.setString(2, teacher.getName());
            ps.setString(3, teacher.getGender());
            ps.setDate(4, teacher.getBirthDate() == null ? null : new java.sql.Date(teacher.getBirthDate().getTime()));
            ps.setString(5, teacher.getTitle());
            ps.setString(6, teacher.getCollege());
            ps.setString(7, teacher.getMajor());
            ps.setString(8, teacher.getContactInfo());
            ps.setString(9, teacher.getStatus());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int updateTeacher(Teacher teacher) {
        String sql = "UPDATE teacher SET name=?, gender=?, birth_date=?, title=?, college=?, major=?, contact_info=?, status=? WHERE id=?";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, teacher.getName());
            ps.setString(2, teacher.getGender());
            ps.setDate(3, teacher.getBirthDate() == null ? null : new java.sql.Date(teacher.getBirthDate().getTime()));
            ps.setString(4, teacher.getTitle());
            ps.setString(5, teacher.getCollege());
            ps.setString(6, teacher.getMajor());
            ps.setString(7, teacher.getContactInfo());
            ps.setString(8, teacher.getStatus());
            ps.setString(9, teacher.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Teacher getTeacherById(String id) {
        String sql = "SELECT * FROM teacher WHERE id = ?";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Teacher teacher = new Teacher();
                    teacher.setId(rs.getString("id"));
                    teacher.setName(rs.getString("name"));
                    teacher.setGender(rs.getString("gender"));
                    teacher.setBirthDate(rs.getDate("birth_date"));
                    teacher.setTitle(rs.getString("title"));
                    teacher.setCollege(rs.getString("college"));
                    teacher.setMajor(rs.getString("major"));
                    teacher.setContactInfo(rs.getString("contact_info"));
                    teacher.setStatus(rs.getString("status"));
                    teacher.setPassword(rs.getString("password"));
                    return teacher;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Teacher> queryTeachers(String name, String college, String major, String status) {
        StringBuilder sql = new StringBuilder("SELECT * FROM teacher WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            sql.append(" AND name LIKE ?");
            params.add("%" + name + "%");
        }
        if (college != null && !college.isEmpty()) {
            sql.append(" AND college=?");
            params.add(college);
        }
        if (major != null && !major.isEmpty()) {
            sql.append(" AND major=?");
            params.add(major);
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND status=?");
            params.add(status);
        }
        List<Teacher> list = new ArrayList<>();
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractTeacher(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Teacher> queryTeachersByPage(int offset, int limit) {
        String sql = "SELECT * FROM teacher LIMIT ?, ?";
        List<Teacher> list = new ArrayList<>();
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractTeacher(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int deleteTeacher(String id) {
        String sql = "UPDATE teacher SET status='已删除' WHERE id=?";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private Teacher extractTeacher(ResultSet rs) throws SQLException {
        Teacher t = new Teacher();
        t.setId(rs.getString("id"));
        t.setName(rs.getString("name"));
        t.setGender(rs.getString("gender"));
        t.setBirthDate(rs.getDate("birth_date"));
        t.setTitle(rs.getString("title"));
        t.setCollege(rs.getString("college"));
        t.setMajor(rs.getString("major"));
        t.setContactInfo(rs.getString("contact_info"));
        t.setStatus(rs.getString("status"));
        return t;
    }
    public static void main(String[] args) {
        TeacherDao dao = new TeacherDaoImpl();
        // 添加教师测试
        Teacher t = new Teacher();
        t.setId("T002");
        t.setName("王老师");
        t.setGender("男");
        t.setBirthDate(new Date());
        t.setTitle("讲师");
        t.setCollege("计算机学院");
        t.setMajor("软件工程");
        t.setContactInfo("98765432102");
        t.setStatus("在职");
        int addResult = dao.addTeacher(t);
        System.out.println("添加教师结果: " + addResult);

        // 查询教师
        Teacher t2 = dao.getTeacherById("T002");
        System.out.println("查询教师: " + t2);

        // 修改教师
        t2.setName("王小明");
        int updateResult = dao.updateTeacher(t2);
        System.out.println("修改教师结果: " + updateResult);

        // 分页查询
        System.out.println("分页查询:");
        for (Teacher teacher : dao.queryTeachersByPage(0, 10)) {
            System.out.println(teacher);
        }

        // 条件查询
        System.out.println("条件查询:");
        for (Teacher teacher : dao.queryTeachers("王", null, null, null)) {
            System.out.println(teacher);
        }

        // 逻辑删除
        int delResult = dao.deleteTeacher("T002");
        System.out.println("逻辑删除结果: " + delResult);
    }
}
