package dao;

import entity.Admin;
import utils.db_connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDaoImpl implements AdminDao {

    @Override
    public int addAdmin(Admin admin) {
        String sql = "INSERT INTO admin (username, password, name, email, phone) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, admin.getUsername());
            stmt.setString(2, admin.getPassword());
            stmt.setString(3, admin.getName());
            stmt.setString(4, admin.getEmail());
            stmt.setString(5, admin.getPhone());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int updateAdmin(Admin admin) {
        String sql = "UPDATE admin SET username = ?, password = ?, name = ?, email = ?, phone = ? WHERE admin_id = ?";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, admin.getUsername());
            stmt.setString(2, admin.getPassword());
            stmt.setString(3, admin.getName());
            stmt.setString(4, admin.getEmail());
            stmt.setString(5, admin.getPhone());
            stmt.setInt(6, admin.getAdminId());
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Admin getAdminById(int adminId) {
        String sql = "SELECT * FROM admin WHERE admin_id = ?";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, adminId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAdmin(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Admin getAdminByUsername(String username) {
        String sql = "SELECT * FROM admin WHERE username = ?";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAdmin(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Admin> getAllAdmins() {
        List<Admin> admins = new ArrayList<>();
        String sql = "SELECT * FROM admin";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                admins.add(mapResultSetToAdmin(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admins;
    }

    @Override
    public int deleteAdmin(int adminId) {
        String sql = "DELETE FROM admin WHERE admin_id = ?";
        try (Connection conn = db_connection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, adminId);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Admin mapResultSetToAdmin(ResultSet rs) throws SQLException {
        Admin admin = new Admin();
        admin.setAdminId(rs.getInt("admin_id"));
        admin.setUsername(rs.getString("username"));
        admin.setPassword(rs.getString("password"));
        admin.setName(rs.getString("name"));
        admin.setEmail(rs.getString("email"));
        admin.setPhone(rs.getString("phone"));
        admin.setCreatedAt(rs.getTimestamp("created_at"));
        admin.setUpdatedAt(rs.getTimestamp("updated_at"));
        return admin;
    }
    public static void main(String[] args) {
        AdminDao adminDao = new AdminDaoImpl();

        // 测试添加管理员
        Admin newAdmin = new Admin();
        newAdmin.setUsername("testAdmin");
        newAdmin.setPassword("test123");
        newAdmin.setName("测试管理员");
        newAdmin.setEmail("testadmin@example.com");
        newAdmin.setPhone("9876543210");
        int addResult = adminDao.addAdmin(newAdmin);
        System.out.println("添加管理员结果: " + (addResult > 0 ? "成功" : "失败"));

        // 测试查询所有管理员
        List<Admin> admins = adminDao.getAllAdmins();
        System.out.println("所有管理员:");
        for (Admin admin : admins) {
            System.out.println(admin);
        }

        // 测试根据用户名查询管理员
        Admin fetchedAdmin = adminDao.getAdminByUsername("testAdmin");
        System.out.println("根据用户名查询管理员: " + fetchedAdmin);

        // 测试更新管理员
        if (fetchedAdmin != null) {
            fetchedAdmin.setEmail("updatedemail@example.com");
            int updateResult = adminDao.updateAdmin(fetchedAdmin);
            System.out.println("更新管理员结果: " + (updateResult > 0 ? "成功" : "失败"));
        }

//        // 测试删除管理员
//        if (fetchedAdmin != null) {
//            int deleteResult = adminDao.deleteAdmin(fetchedAdmin.getAdminId());
//            System.out.println("删除管理员结果: " + (deleteResult > 0 ? "成功" : "失败"));
//        }
    }
}
