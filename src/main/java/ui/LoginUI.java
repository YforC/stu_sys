package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import entity.Admin;
import entity.Student;
import entity.Teacher;
import service.AdminService;
import service.AdminServiceImpl;
import service.StudentService;
import service.StudentServiceImpl;
import service.TeacherService;
import service.TeacherServiceImpl;

public class LoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton loginButton;
    private JButton resetButton;

    public LoginUI() {
        // 设置窗口标题和大小
        setTitle("学生成绩管理系统");
        setSize(450, 350);
        setLocationRelativeTo(null); // 窗口居中
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建主面板，使用BoxLayout布局
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        mainPanel.setBackground(new Color(240, 248, 255)); // 设置背景颜色

        // 添加标题标签
        JLabel titleLabel = new JLabel("欢迎登录学生成绩管理系统", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(0, 102, 204));
        mainPanel.add(titleLabel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // 添加间距

        // 创建表单面板
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setOpaque(false);

        // 添加用户名输入框
        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        formPanel.add(usernameLabel);

        usernameField = new JTextField(20);
        formPanel.add(usernameField);

        // 添加密码输入框
        JLabel passwordLabel = new JLabel("密码:");
        passwordLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        formPanel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        formPanel.add(passwordField);

        // 添加角色选择下拉框
        JLabel roleLabel = new JLabel("登录身份:");
        roleLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        formPanel.add(roleLabel);

        String[] roles = {"学生", "教师", "教务管理员"};
        roleComboBox = new JComboBox<>(roles);
        formPanel.add(roleComboBox);

        mainPanel.add(formPanel);

        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // 添加间距

        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        loginButton = new JButton("登录");
        loginButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        loginButton.setPreferredSize(new Dimension(100, 30));
        buttonPanel.add(loginButton);

        resetButton = new JButton("重置");
        resetButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        resetButton.setPreferredSize(new Dimension(100, 30));
        buttonPanel.add(resetButton);

        mainPanel.add(buttonPanel);

        // 设置按钮事件监听器
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();

            if (role.equals("学生")) {
                Student student = getLoginStudent(username, password);
                if (student != null) {
                    new StudentUI(student.getId());
                    dispose();
                    return;
                }
            } else if (validateCredentials(username, password, role)) {
                switch (role) {
                    case "教师":
                        new TeacherUI(username); // 传递教师id
                        break;
                    case "教务管理员":
                        new AdminUI();
                        break;
                    default:
                        JOptionPane.showMessageDialog(this, "未知角色！", "错误", JOptionPane.ERROR_MESSAGE);
                }
                dispose();
                return;
            }
            JOptionPane.showMessageDialog(this, "用户名或密码错误！", "登录失败", JOptionPane.ERROR_MESSAGE);
        });

        resetButton.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
            roleComboBox.setSelectedIndex(0);
        });

        // 设置窗口内容并显示
        setContentPane(mainPanel);
        setVisible(true);
    }

    private Student getLoginStudent(String username, String password) {
        StudentService studentService = new StudentServiceImpl();
        Student student = studentService.getStudentById(username);
        if (student != null && student.getPassword().equals(password)) {
            return student;
        }
        return null;
    }

    private boolean validateCredentials(String username, String password, String role) {
        if (role.equals("教务管理员")) {
            AdminService adminService = new AdminServiceImpl();
            Admin admin = adminService.getAdminByUsername(username);
            return admin != null && admin.getPassword().equals(password);
        }
        if (role.equals("教师")) {
            TeacherService teacherService = new TeacherServiceImpl();
            Teacher teacher = teacherService.getTeacherById(username);
            return teacher != null && password.equals(teacher.getPassword());
        }
        return false;
    }

    public static void main(String[] args) {
        // 设置界面外观
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 启动登录界面
        SwingUtilities.invokeLater(() -> new LoginUI());
    }
}
