package service;

import entity.Admin;
import java.util.List;

public interface AdminService {
    // 添加管理员
    boolean addAdmin(Admin admin);

    // 修改管理员信息
    boolean updateAdmin(Admin admin);

    // 根据ID查询管理员
    Admin getAdminById(int adminId);

    // 根据用户名查询管理员
    Admin getAdminByUsername(String username);

    // 查询所有管理员
    List<Admin> getAllAdmins();

    // 删除管理员
    boolean deleteAdmin(int adminId);
}
