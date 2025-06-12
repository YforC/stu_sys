package dao;

import entity.Student;
import utils.db_connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentDaoImpl implements StudentDao {
    @Override
    public int addStudent(Student student) {
        String sql = "INSERT INTO student (id, name, gender, birth_date, enrollment_year, class_name, major, college, contact_info, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getId());
            ps.setString(2, student.getName());
            ps.setString(3, student.getGender());
            ps.setDate(4, student.getBirthDate() == null ? null : new java.sql.Date(student.getBirthDate().getTime()));
            ps.setInt(5, student.getEnrollmentYear());
            ps.setString(6, student.getClassName());
            ps.setString(7, student.getMajor());
            ps.setString(8, student.getCollege());
            ps.setString(9, student.getContactInfo());
            ps.setString(10, student.getStatus());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int updateStudent(Student student) {
        String sql = "UPDATE student SET name=?, gender=?, birth_date=?, enrollment_year=?, class_name=?, major=?, college=?, contact_info=?, status=? WHERE id=?";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, student.getName());
            ps.setString(2, student.getGender());
            ps.setDate(3, student.getBirthDate() == null ? null : new java.sql.Date(student.getBirthDate().getTime()));
            ps.setInt(4, student.getEnrollmentYear());
            ps.setString(5, student.getClassName());
            ps.setString(6, student.getMajor());
            ps.setString(7, student.getCollege());
            ps.setString(8, student.getContactInfo());
            ps.setString(9, student.getStatus());
            ps.setString(10, student.getId());
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Student getStudentById(String id) {
        String sql = "SELECT * FROM student WHERE id = ?";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Student student = new Student();
                    student.setId(rs.getString("id"));
                    student.setName(rs.getString("name"));
                    student.setGender(rs.getString("gender"));
                    student.setBirthDate(rs.getDate("birth_date"));
                    student.setEnrollmentYear(rs.getInt("enrollment_year"));
                    student.setClassName(rs.getString("class_name"));
                    student.setMajor(rs.getString("major"));
                    student.setCollege(rs.getString("college"));
                    student.setContactInfo(rs.getString("contact_info"));
                    student.setStatus(rs.getString("status"));
                    student.setPassword(rs.getString("password"));
                    return student;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Student> queryStudents(String name, String className, String major, String college, String status) {
        StringBuilder sql = new StringBuilder("SELECT * FROM student WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            sql.append(" AND name LIKE ?");
            params.add("%" + name + "%");
        }
        if (className != null && !className.isEmpty()) {
            sql.append(" AND class_name=?");
            params.add(className);
        }
        if (major != null && !major.isEmpty()) {
            sql.append(" AND major=?");
            params.add(major);
        }
        if (college != null && !college.isEmpty()) {
            sql.append(" AND college=?");
            params.add(college);
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND status=?");
            params.add(status);
        }
        List<Student> list = new ArrayList<>();
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Student> queryStudentsByPage(int offset, int limit) {
        String sql = "SELECT * FROM student LIMIT ?, ?";
        List<Student> list = new ArrayList<>();
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, offset);
            ps.setInt(2, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public int deleteStudent(String id) {
        String sql = "UPDATE student SET status='已删除' WHERE id=?";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private Student extractStudent(ResultSet rs) throws SQLException {
        Student s = new Student();
        s.setId(rs.getString("id"));
        s.setName(rs.getString("name"));
        s.setGender(rs.getString("gender"));
        s.setBirthDate(rs.getDate("birth_date"));
        s.setEnrollmentYear(rs.getInt("enrollment_year"));
        s.setClassName(rs.getString("class_name"));
        s.setMajor(rs.getString("major"));
        s.setCollege(rs.getString("college"));
        s.setContactInfo(rs.getString("contact_info"));
        s.setStatus(rs.getString("status"));
        return s;
    }
    public static void main(String[] args) {
        StudentDao dao = new StudentDaoImpl();
        // 添加学生测试
        Student s = new Student();
        s.setId("S002");
        s.setName("李四");
        s.setGender("女");
        s.setBirthDate(new Date());
        s.setEnrollmentYear(2021);
        s.setClassName("计科2班");
        s.setMajor("软件工程");
        s.setCollege("计算机学院");
        s.setContactInfo("12345678902");
        s.setStatus("在读");
        int addResult = dao.addStudent(s);
        System.out.println("添加学生结果: " + addResult);

        // 查询学生
        Student s2 = dao.getStudentById("S002");
        System.out.println("查询学生: " + s2);

        // 修改学生
        s2.setName("李四四");
        int updateResult = dao.updateStudent(s2);
        System.out.println("修改学生结果: " + updateResult);

        // 分页查询
        System.out.println("分页查询:");
        for (Student stu : dao.queryStudentsByPage(0, 10)) {
            System.out.println(stu);
        }

        // 条件查询
        System.out.println("条件查询:");
        for (Student stu : dao.queryStudents("李", null, null, null, null)) {
            System.out.println(stu);
        }

        // 逻辑删除
        int delResult = dao.deleteStudent("S002");
        System.out.println("逻辑删除结果: " + delResult);
    }
}
