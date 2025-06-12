package service;

import dao.AdminDao;
import dao.AdminDaoImpl;
import entity.Admin;

import java.util.List;

public class AdminServiceImpl implements AdminService {

    private final AdminDao adminDao = new AdminDaoImpl();

    @Override
    public boolean addAdmin(Admin admin) {
        return adminDao.addAdmin(admin) > 0;
    }

    @Override
    public boolean updateAdmin(Admin admin) {
        return adminDao.updateAdmin(admin) > 0;
    }

    @Override
    public Admin getAdminById(int adminId) {
        return adminDao.getAdminById(adminId);
    }

    @Override
    public Admin getAdminByUsername(String username) {
        return adminDao.getAdminByUsername(username);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminDao.getAllAdmins();
    }

    @Override
    public boolean deleteAdmin(int adminId) {
        return adminDao.deleteAdmin(adminId) > 0;
    }
}
