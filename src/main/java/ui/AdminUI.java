package ui;

import entity.Student;
import service.StudentService;
import service.StudentServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AdminUI {
    private JFrame frame;
    private StudentService studentService = new StudentServiceImpl();
    private JTable studentTable;
    private DefaultTableModel studentTableModel;
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;

    public AdminUI() {
        // 创建主窗口
        frame = new JFrame("学生成绩管理系统 - 管理员界面");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // 设置主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255)); // 浅蓝色背景


        // 添加功能按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20)); // 使用流式布局
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 调整功能按钮
        JButton manageStudentsButton = new JButton("管理学生档案");
        manageStudentsButton.setFont(new Font("微软雅黑", Font.PLAIN, 10)); // 调整字体大小
        manageStudentsButton.setPreferredSize(new Dimension(100, 30)); // 进一步缩小按钮尺寸
        manageStudentsButton.setBackground(new Color(173, 216, 230));
        manageStudentsButton.setFocusPainted(false);
        manageStudentsButton.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        buttonPanel.add(manageStudentsButton);

        JButton manageTeachersButton = new JButton("管理教师档案");
        manageTeachersButton.setFont(new Font("微软雅黑", Font.PLAIN, 10));
        manageTeachersButton.setPreferredSize(new Dimension(100, 30));
        manageTeachersButton.setBackground(new Color(173, 216, 230));
        manageTeachersButton.setFocusPainted(false);
        manageTeachersButton.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        buttonPanel.add(manageTeachersButton);

        JButton manageCoursesButton = new JButton("管理课程信息");
        manageCoursesButton.setFont(new Font("微软雅黑", Font.PLAIN, 10));
        manageCoursesButton.setPreferredSize(new Dimension(100, 30));
        manageCoursesButton.setBackground(new Color(173, 216, 230));
        manageCoursesButton.setFocusPainted(false);
        manageCoursesButton.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        buttonPanel.add(manageCoursesButton);

        JButton generateReportsButton = new JButton("生成成绩报表");
        generateReportsButton.setFont(new Font("微软雅黑", Font.PLAIN, 10));
        generateReportsButton.setPreferredSize(new Dimension(100, 30));
        generateReportsButton.setBackground(new Color(173, 216, 230));
        generateReportsButton.setFocusPainted(false);
        generateReportsButton.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        buttonPanel.add(generateReportsButton);

        JButton analyzeGradesButton = new JButton("统计分析成绩");
        analyzeGradesButton.setFont(new Font("微软雅黑", Font.PLAIN, 10));
        analyzeGradesButton.setPreferredSize(new Dimension(100, 30));
        analyzeGradesButton.setBackground(new Color(173, 216, 230));
        analyzeGradesButton.setFocusPainted(false);
        analyzeGradesButton.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        buttonPanel.add(analyzeGradesButton);

        JButton logoutButton = new JButton("退出登录");
        logoutButton.setFont(new Font("微软雅黑", Font.PLAIN, 10));
        logoutButton.setPreferredSize(new Dimension(100, 30));
        logoutButton.setBackground(new Color(173, 216, 230));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        buttonPanel.add(logoutButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH); // 将按钮面板放置在顶部

        // 添加信息显示区域
        JTextArea displayArea = new JTextArea();
        displayArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        displayArea.setEditable(false);
        displayArea.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0, 102, 204)), "信息显示", 0, 0, new Font("微软雅黑", Font.BOLD, 14), new Color(0, 102, 204)));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 设置按钮事件
        manageStudentsButton.addActionListener(e -> showStudentManagementPanel());
        manageTeachersButton.addActionListener(e -> displayArea.setText("管理教师档案功能尚未实现"));
        manageCoursesButton.addActionListener(e -> displayArea.setText("管理课程信息功能尚未实现"));
        generateReportsButton.addActionListener(e -> displayArea.setText("生成成绩报表功能尚未实现"));
        analyzeGradesButton.addActionListener(e -> displayArea.setText("统计分析成绩功能尚未实现"));
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "确定要退出登录吗？", "确认", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                frame.dispose();
            }
        });

        // 显示窗口
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    private void showStudentManagementPanel() {
        // 创建新窗口
        JDialog dialog = new JDialog(frame, "学生档案管理", true);
        dialog.setSize(1000, 600);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        // 顶部搜索面板
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.add(new JLabel("搜索条件:"));

        String[] searchTypes = {"学号", "姓名", "班级", "专业", "学院"};
        searchTypeCombo = new JComboBox<>(searchTypes);
        searchPanel.add(searchTypeCombo);

        searchField = new JTextField(15);
        searchPanel.add(searchField);

        JButton searchButton = new JButton("搜索");
        searchButton.addActionListener(e -> searchStudents());
        searchPanel.add(searchButton);

        // 添加新学生按钮
        JButton addButton = new JButton("添加学生");
        addButton.addActionListener(e -> showAddStudentDialog());
        searchPanel.add(addButton);

        dialog.add(searchPanel, BorderLayout.NORTH);

        // 创建表格
        String[] columns = {"学号", "姓名", "性别", "出生日期", "入学年份", "班级", "专业", "学院", "联系方式", "状态", "操作"};
        studentTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == columns.length - 1; // 只有操作列可编辑
            }
        };
        studentTable = new JTable(studentTableModel);
        studentTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // 设置操作列的渲染器和编辑器
        studentTable.getColumnModel().getColumn(columns.length - 1).setCellRenderer(new ButtonRenderer());
        studentTable.getColumnModel().getColumn(columns.length - 1).setCellEditor(new ButtonEditor(new JTextField()));

        JScrollPane scrollPane = new JScrollPane(studentTable);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // 初始加载所有学生数据
        loadAllStudents();

        dialog.setVisible(true);
    }

    private void searchStudents() {
        String searchType = (String) searchTypeCombo.getSelectedItem();
        String searchValue = searchField.getText().trim();

        List<Student> students;
        switch (searchType) {
            case "学号":
                Student student = studentService.getStudentById(searchValue);
                students = student != null ? Arrays.asList(student) : new ArrayList<>();
                break;
            case "姓名":
                students = studentService.queryStudents(searchValue, null, null, null, null);
                break;
            case "班级":
                students = studentService.queryStudents(null, searchValue, null, null, null);
                break;
            case "专业":
                students = studentService.queryStudents(null, null, searchValue, null, null);
                break;
            case "学院":
                students = studentService.queryStudents(null, null, null, searchValue, null);
                break;
            default:
                students = studentService.queryStudents(null, null, null, null, null);
        }

        updateStudentTable(students);
    }

    private void loadAllStudents() {
        List<Student> students = studentService.queryStudents(null, null, null, null, null);
        updateStudentTable(students);
    }

    private void updateStudentTable(List<Student> students) {
        studentTableModel.setRowCount(0);
        for (Student student : students) {
            Object[] rowData = {
                    student.getId(),
                    student.getName(),
                    student.getGender(),
                    student.getBirthDate(),
                    student.getEnrollmentYear(),
                    student.getClassName(),
                    student.getMajor(),
                    student.getCollege(),
                    student.getContactInfo(),
                    student.getStatus(),
                    "修改 删除"
            };
            studentTableModel.addRow(rowData);
        }
    }

    private void showAddStudentDialog() {
        JDialog addDialog = new JDialog(frame, "添加学生", true);
        addDialog.setSize(400, 500);
        addDialog.setLocationRelativeTo(null);
        addDialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(10, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 创建输入字段
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"男", "女"});
        JTextField birthDateField = new JTextField("YYYY-MM-DD");
        JTextField enrollmentYearField = new JTextField();
        JTextField classNameField = new JTextField();
        JTextField majorField = new JTextField();
        JTextField collegeField = new JTextField();
        JTextField contactField = new JTextField();
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"在读", "休学", "毕业"});

        // 添加标签和输入字段
        formPanel.add(new JLabel("学号:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("姓名:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("性别:"));
        formPanel.add(genderCombo);
        formPanel.add(new JLabel("出生日期:"));
        formPanel.add(birthDateField);
        formPanel.add(new JLabel("入学年份:"));
        formPanel.add(enrollmentYearField);
        formPanel.add(new JLabel("班级:"));
        formPanel.add(classNameField);
        formPanel.add(new JLabel("专业:"));
        formPanel.add(majorField);
        formPanel.add(new JLabel("学院:"));
        formPanel.add(collegeField);
        formPanel.add(new JLabel("联系方式:"));
        formPanel.add(contactField);
        formPanel.add(new JLabel("状态:"));
        formPanel.add(statusCombo);

        addDialog.add(formPanel, BorderLayout.CENTER);

        // 添加确认和取消按钮
        JPanel buttonPanel = new JPanel();
        JButton confirmButton = new JButton("确认");
        confirmButton.addActionListener(e -> {
            try {
                Student student = new Student();
                student.setId(idField.getText());
                student.setName(nameField.getText());
                student.setGender((String) genderCombo.getSelectedItem());
                student.setBirthDate(java.sql.Date.valueOf(birthDateField.getText()));
                student.setEnrollmentYear(Integer.parseInt(enrollmentYearField.getText()));
                student.setClassName(classNameField.getText());
                student.setMajor(majorField.getText());
                student.setCollege(collegeField.getText());
                student.setContactInfo(contactField.getText());
                student.setStatus((String) statusCombo.getSelectedItem());

                studentService.addStudent(student);
                JOptionPane.showMessageDialog(addDialog, "添加成功！");
                addDialog.dispose();
                loadAllStudents();  // 刷新表格
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(addDialog, "添加失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> addDialog.dispose());

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        addDialog.add(buttonPanel, BorderLayout.SOUTH);

        addDialog.setVisible(true);
    }

    // 表格按钮渲染器
    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton modifyButton = new JButton("修改");
        private JButton deleteButton = new JButton("删除");

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            add(modifyButton);
            add(deleteButton);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    // 表格按钮编辑器
    class ButtonEditor extends DefaultCellEditor {
        private JButton modifyButton;
        private JButton deleteButton;
        private JPanel panel;
        private String studentId;

        public ButtonEditor(JTextField textField) {
            super(textField);
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));

            modifyButton = new JButton("修改");
            modifyButton.addActionListener(e -> {
                studentId = (String) studentTable.getValueAt(studentTable.getSelectedRow(), 0);
                showModifyStudentDialog(studentId);
                fireEditingStopped();
            });

            deleteButton = new JButton("删除");
            deleteButton.addActionListener(e -> {
                studentId = (String) studentTable.getValueAt(studentTable.getSelectedRow(), 0);
                int confirm = JOptionPane.showConfirmDialog(null,
                        "确认删除学号为 " + studentId + " 的学生吗？", "确认删除",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    studentService.deleteStudent(studentId);
                    loadAllStudents();
                }
                fireEditingStopped();
            });

            panel.add(modifyButton);
            panel.add(deleteButton);
        }

        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            return panel;
        }

        public Object getCellEditorValue() {
            return studentId;
        }
    }

    private void showModifyStudentDialog(String studentId) {
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            JOptionPane.showMessageDialog(frame, "未找到该学生信息！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog modifyDialog = new JDialog(frame, "修改学生信息", true);
        modifyDialog.setSize(400, 500);
        modifyDialog.setLocationRelativeTo(null);
        modifyDialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(10, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JTextField nameField = new JTextField(student.getName());
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"男", "女"});
        genderCombo.setSelectedItem(student.getGender());
        JTextField birthDateField = new JTextField(student.getBirthDate().toString());
        JTextField enrollmentYearField = new JTextField(student.getEnrollmentYear().toString());
        JTextField classNameField = new JTextField(student.getClassName());
        JTextField majorField = new JTextField(student.getMajor());
        JTextField collegeField = new JTextField(student.getCollege());
        JTextField contactField = new JTextField(student.getContactInfo());
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"在读", "休学", "毕业"});
        statusCombo.setSelectedItem(student.getStatus());

        formPanel.add(new JLabel("学号:"));
        formPanel.add(new JLabel(student.getId()));
        formPanel.add(new JLabel("姓名:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("性别:"));
        formPanel.add(genderCombo);
        formPanel.add(new JLabel("出生日期:"));
        formPanel.add(birthDateField);
        formPanel.add(new JLabel("入学年份:"));
        formPanel.add(enrollmentYearField);
        formPanel.add(new JLabel("班级:"));
        formPanel.add(classNameField);
        formPanel.add(new JLabel("专业:"));
        formPanel.add(majorField);
        formPanel.add(new JLabel("学院:"));
        formPanel.add(collegeField);
        formPanel.add(new JLabel("联系方式:"));
        formPanel.add(contactField);
        formPanel.add(new JLabel("状态:"));
        formPanel.add(statusCombo);

        modifyDialog.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton confirmButton = new JButton("确认");
        confirmButton.addActionListener(e -> {
            try {
                student.setName(nameField.getText());
                student.setGender((String) genderCombo.getSelectedItem());
                student.setBirthDate(java.sql.Date.valueOf(birthDateField.getText()));
                student.setEnrollmentYear(Integer.parseInt(enrollmentYearField.getText()));
                student.setClassName(classNameField.getText());
                student.setMajor(majorField.getText());
                student.setCollege(collegeField.getText());
                student.setContactInfo(contactField.getText());
                student.setStatus((String) statusCombo.getSelectedItem());

                studentService.updateStudent(student);
                JOptionPane.showMessageDialog(modifyDialog, "修改成功！");
                modifyDialog.dispose();
                loadAllStudents();  // 刷新表格
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(modifyDialog, "修改失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton cancelButton = new JButton("取消");
        cancelButton.addActionListener(e -> modifyDialog.dispose());

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        modifyDialog.add(buttonPanel, BorderLayout.SOUTH);

        modifyDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminUI());
    }
}
