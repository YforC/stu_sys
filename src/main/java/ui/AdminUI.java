package ui;

import entity.Student;
import entity.Teacher;
import entity.Course;
import service.StudentService;
import service.StudentServiceImpl;
import service.TeacherService;
import service.TeacherServiceImpl;
import service.CourseService;
import service.CourseServiceImpl;
import service.GradeService;
import service.GradeServiceImpl;
import entity.Grade;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
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
        manageTeachersButton.addActionListener(e -> showTeacherManagementPanel());
        manageCoursesButton.addActionListener(e -> showCourseManagementPanel());
        generateReportsButton.addActionListener(e -> showReportEntryPanel());
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

    private void showTeacherManagementPanel() {
        JDialog dialog = new JDialog(frame, "教师档案管理", true);
        dialog.setSize(900, 550);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.add(new JLabel("搜索条件:"));
        String[] searchTypes = {"工号", "姓名", "学院"};
        JComboBox<String> searchTypeCombo = new JComboBox<>(searchTypes);
        searchPanel.add(searchTypeCombo);
        JTextField searchField = new JTextField(15);
        searchPanel.add(searchField);
        JButton searchButton = new JButton("搜索");
        searchPanel.add(searchButton);
        JButton addButton = new JButton("添加教师");
        searchPanel.add(addButton);
        dialog.add(searchPanel, BorderLayout.NORTH);

        String[] columns = {"工号", "姓名", "性别", "出生日期", "学院", "联系方式", "状态", "操作"};
        DefaultTableModel teacherTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == columns.length - 1;
            }
        };
        JTable teacherTable = new JTable(teacherTableModel);
        teacherTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        teacherTable.getColumnModel().getColumn(columns.length - 1).setCellRenderer(new ButtonRenderer());
        teacherTable.getColumnModel().getColumn(columns.length - 1).setCellEditor(new TeacherButtonEditor(new JTextField(), teacherTableModel, teacherTable));
        JScrollPane scrollPane = new JScrollPane(teacherTable);
        dialog.add(scrollPane, BorderLayout.CENTER);

        TeacherService teacherService = new TeacherServiceImpl();

        Runnable loadAllTeachers = () -> {
            List<Teacher> teachers = teacherService.queryTeachers(null, null, null, null);
            teacherTableModel.setRowCount(0);
            for (Teacher t : teachers) {
                Object[] rowData = {
                    t.getId(), t.getName(), t.getGender(), t.getBirthDate(), t.getCollege(), t.getContactInfo(), t.getStatus(), "修改 删除"
                };
                teacherTableModel.addRow(rowData);
            }
        };
        loadAllTeachers.run();

        searchButton.addActionListener(e -> {
            String type = (String) searchTypeCombo.getSelectedItem();
            String value = searchField.getText().trim();
            List<Teacher> teachers;
            switch (type) {
                case "工号":
                    Teacher t = teacherService.getTeacherById(value);
                    teachers = t != null ? Arrays.asList(t) : new ArrayList<>();
                    break;
                case "姓名":
                    teachers = teacherService.queryTeachers(value, null, null, null);
                    break;
                case "学院":
                    teachers = teacherService.queryTeachers(null, value, null, null);
                    break;
                default:
                    teachers = teacherService.queryTeachers(null, null, null, null);
            }
            teacherTableModel.setRowCount(0);
            for (Teacher teacher : teachers) {
                Object[] rowData = {
                    teacher.getId(), teacher.getName(), teacher.getGender(), teacher.getBirthDate(), teacher.getCollege(), teacher.getContactInfo(), teacher.getStatus(), "修改 删除"
                };
                teacherTableModel.addRow(rowData);
            }
        });

        addButton.addActionListener(e -> showAddTeacherDialog(teacherService, teacherTableModel, loadAllTeachers));

        dialog.setVisible(true);
    }

    private void showAddTeacherDialog(TeacherService teacherService, DefaultTableModel teacherTableModel, Runnable reload) {
        JDialog addDialog = new JDialog(frame, "添加教师", true);
        addDialog.setSize(350, 400);
        addDialog.setLocationRelativeTo(null);
        addDialog.setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"男", "女"});
        JTextField birthDateField = new JTextField("YYYY-MM-DD");
        JTextField collegeField = new JTextField();
        JTextField contactField = new JTextField();
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"在职", "离职"});
        formPanel.add(new JLabel("工号:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("姓名:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("性别:"));
        formPanel.add(genderCombo);
        formPanel.add(new JLabel("出生日期:"));
        formPanel.add(birthDateField);
        formPanel.add(new JLabel("学院:"));
        formPanel.add(collegeField);
        formPanel.add(new JLabel("联系方式:"));
        formPanel.add(contactField);
        formPanel.add(new JLabel("状态:"));
        formPanel.add(statusCombo);
        addDialog.add(formPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        JButton confirmButton = new JButton("确认");
        confirmButton.addActionListener(e -> {
            try {
                Teacher teacher = new Teacher();
                teacher.setId(idField.getText());
                teacher.setName(nameField.getText());
                teacher.setGender((String) genderCombo.getSelectedItem());
                teacher.setBirthDate(java.sql.Date.valueOf(birthDateField.getText()));
                teacher.setCollege(collegeField.getText());
                teacher.setContactInfo(contactField.getText());
                teacher.setStatus((String) statusCombo.getSelectedItem());
                teacherService.addTeacher(teacher);
                JOptionPane.showMessageDialog(addDialog, "添加成功！");
                addDialog.dispose();
                reload.run();
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

    class TeacherButtonEditor extends DefaultCellEditor {
        private JButton modifyButton;
        private JButton deleteButton;
        private JPanel panel;
        private String teacherId;
        private DefaultTableModel model;
        private JTable table;
        public TeacherButtonEditor(JTextField textField, DefaultTableModel model, JTable table) {
            super(textField);
            this.model = model;
            this.table = table;
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            modifyButton = new JButton("修改");
            modifyButton.addActionListener(e -> {
                teacherId = (String) table.getValueAt(table.getSelectedRow(), 0);
                showModifyTeacherDialog(teacherId, model, table);
                fireEditingStopped();
            });
            deleteButton = new JButton("删除");
            deleteButton.addActionListener(e -> {
                teacherId = (String) table.getValueAt(table.getSelectedRow(), 0);
                int confirm = JOptionPane.showConfirmDialog(null,
                        "确认删除工号为 " + teacherId + " 的教师吗？", "确认删除",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    TeacherService teacherService = new TeacherServiceImpl();
                    teacherService.deleteTeacher(teacherId);
                    model.removeRow(table.getSelectedRow());
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
            return teacherId;
        }
    }

    private void showModifyTeacherDialog(String teacherId, DefaultTableModel model, JTable table) {
        TeacherService teacherService = new TeacherServiceImpl();
        Teacher teacher = teacherService.getTeacherById(teacherId);
        if (teacher == null) {
            JOptionPane.showMessageDialog(frame, "未找到该教师信息！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JDialog modifyDialog = new JDialog(frame, "修改教师信息", true);
        modifyDialog.setSize(350, 400);
        modifyDialog.setLocationRelativeTo(null);
        modifyDialog.setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JTextField nameField = new JTextField(teacher.getName());
        JComboBox<String> genderCombo = new JComboBox<>(new String[]{"男", "女"});
        genderCombo.setSelectedItem(teacher.getGender());
        JTextField birthDateField = new JTextField(teacher.getBirthDate().toString());
        JTextField collegeField = new JTextField(teacher.getCollege());
        JTextField contactField = new JTextField(teacher.getContactInfo());
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"在职", "离职"});
        statusCombo.setSelectedItem(teacher.getStatus());
        formPanel.add(new JLabel("工号:"));
        formPanel.add(new JLabel(teacher.getId()));
        formPanel.add(new JLabel("姓名:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("性别:"));
        formPanel.add(genderCombo);
        formPanel.add(new JLabel("出生日期:"));
        formPanel.add(birthDateField);
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
                teacher.setName(nameField.getText());
                teacher.setGender((String) genderCombo.getSelectedItem());
                teacher.setBirthDate(java.sql.Date.valueOf(birthDateField.getText()));
                teacher.setCollege(collegeField.getText());
                teacher.setContactInfo(contactField.getText());
                teacher.setStatus((String) statusCombo.getSelectedItem());
                teacherService.updateTeacher(teacher);
                JOptionPane.showMessageDialog(modifyDialog, "修改成功！");
                modifyDialog.dispose();
                // 刷新表格
                int row = table.getSelectedRow();
                table.setValueAt(teacher.getName(), row, 1);
                table.setValueAt(teacher.getGender(), row, 2);
                table.setValueAt(teacher.getBirthDate(), row, 3);
                table.setValueAt(teacher.getCollege(), row, 4);
                table.setValueAt(teacher.getContactInfo(), row, 5);
                table.setValueAt(teacher.getStatus(), row, 6);
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

    private void showCourseManagementPanel() {
        JDialog dialog = new JDialog(frame, "课程信息管理", true);
        dialog.setSize(1000, 600);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.add(new JLabel("搜索条件:"));
        String[] searchTypes = {"课程编号", "课程名称", "课程类型", "开课学期", "教师工号"};
        JComboBox<String> searchTypeCombo = new JComboBox<>(searchTypes);
        searchPanel.add(searchTypeCombo);
        JTextField searchField = new JTextField(15);
        searchPanel.add(searchField);
        JButton searchButton = new JButton("搜索");
        searchPanel.add(searchButton);
        JButton addButton = new JButton("添加课程");
        searchPanel.add(addButton);
        JButton batchDeleteButton = new JButton("批量删除");
        searchPanel.add(batchDeleteButton);
        dialog.add(searchPanel, BorderLayout.NORTH);

        String[] columns = {"选择", "课程编号", "课程名称", "学分", "类型", "学期", "学时", "描述", "操作"};
        DefaultTableModel courseTableModel = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0 || column == columns.length - 1;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Boolean.class;
                return super.getColumnClass(columnIndex);
            }
        };
        JTable courseTable = new JTable(courseTableModel);
        courseTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        courseTable.getColumnModel().getColumn(columns.length - 1).setCellRenderer(new ButtonRenderer());
        courseTable.getColumnModel().getColumn(columns.length - 1).setCellEditor(new CourseButtonEditor(new JTextField(), courseTableModel, courseTable));
        JScrollPane scrollPane = new JScrollPane(courseTable);
        dialog.add(scrollPane, BorderLayout.CENTER);

        JPanel pagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton prevPage = new JButton("上一页");
        JButton nextPage = new JButton("下一页");
        JLabel pageInfo = new JLabel();
        pagePanel.add(prevPage);
        pagePanel.add(pageInfo);
        pagePanel.add(nextPage);
        dialog.add(pagePanel, BorderLayout.SOUTH);

        CourseService courseService = new CourseServiceImpl();
        final int[] page = {0};
        final int pageSize = 20;

        Runnable loadPage = () -> {
            List<Course> courses = courseService.queryCoursesByPage(page[0] * pageSize, pageSize);
            courseTableModel.setRowCount(0);
            for (Course c : courses) {
                Object[] rowData = {false, c.getId(), c.getName(), c.getCredits(), c.getType(), c.getSemester(), c.getHours(), c.getDescription(), "修改 删除"};
                courseTableModel.addRow(rowData);
            }
            pageInfo.setText("第" + (page[0] + 1) + "页");
        };
        loadPage.run();

        prevPage.addActionListener(e -> {
            if (page[0] > 0) {
                page[0]--;
                loadPage.run();
            }
        });
        nextPage.addActionListener(e -> {
            page[0]++;
            loadPage.run();
        });

        searchButton.addActionListener(e -> {
            String type = (String) searchTypeCombo.getSelectedItem();
            String value = searchField.getText().trim();
            List<Course> courses;
            switch (type) {
                case "课程编号":
                    Course c = courseService.getCourseById(value);
                    courses = c != null ? Arrays.asList(c) : new ArrayList<>();
                    break;
                case "课程名称":
                    courses = courseService.queryCourses(value, null, null, null);
                    break;
                case "课程类型":
                    courses = courseService.queryCourses(null, value, null, null);
                    break;
                case "开课学期":
                    courses = courseService.queryCourses(null, null, value, null);
                    break;
                case "教师工号":
                    courses = courseService.queryCourses(null, null, null, value);
                    break;
                default:
                    courses = courseService.queryCoursesByPage(0, pageSize);
            }
            courseTableModel.setRowCount(0);
            for (Course course : courses) {
                Object[] rowData = {false, course.getId(), course.getName(), course.getCredits(), course.getType(), course.getSemester(), course.getHours(), course.getDescription(), "修改 删除"};
                courseTableModel.addRow(rowData);
            }
            page[0] = 0;
            pageInfo.setText("第1页");
        });

        addButton.addActionListener(e -> showAddCourseDialog(courseService, courseTableModel, loadPage));
        batchDeleteButton.addActionListener(e -> {
            int count = 0;
            for (int i = courseTableModel.getRowCount() - 1; i >= 0; i--) {
                Boolean checked = (Boolean) courseTableModel.getValueAt(i, 0);
                if (checked != null && checked) {
                    String id = (String) courseTableModel.getValueAt(i, 1);
                    courseService.deleteCourse(id);
                    courseTableModel.removeRow(i);
                    count++;
                }
            }
            JOptionPane.showMessageDialog(dialog, "已删除" + count + "门课程");
            loadPage.run();
        });

        dialog.setVisible(true);
    }

    private void showAddCourseDialog(CourseService courseService, DefaultTableModel courseTableModel, Runnable reload) {
        JDialog addDialog = new JDialog(frame, "添加课程", true);
        addDialog.setSize(400, 400);
        addDialog.setLocationRelativeTo(null);
        addDialog.setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField creditsField = new JTextField();
        JTextField typeField = new JTextField();
        JTextField semesterField = new JTextField();
        JTextField hoursField = new JTextField();
        JTextField descField = new JTextField();
        formPanel.add(new JLabel("课程编号:"));
        formPanel.add(idField);
        formPanel.add(new JLabel("课程名称:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("学分:"));
        formPanel.add(creditsField);
        formPanel.add(new JLabel("类型:"));
        formPanel.add(typeField);
        formPanel.add(new JLabel("学期:"));
        formPanel.add(semesterField);
        formPanel.add(new JLabel("学时:"));
        formPanel.add(hoursField);
        formPanel.add(new JLabel("描述:"));
        formPanel.add(descField);
        addDialog.add(formPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        JButton confirmButton = new JButton("确认");
        confirmButton.addActionListener(e -> {
            try {
                Course course = new Course();
                course.setId(idField.getText());
                course.setName(nameField.getText());
                course.setCredits(Double.valueOf(creditsField.getText()));
                course.setType(typeField.getText());
                course.setSemester(semesterField.getText());
                course.setHours(Integer.valueOf(hoursField.getText()));
                course.setDescription(descField.getText());
                courseService.addCourse(course);
                JOptionPane.showMessageDialog(addDialog, "添加成功！");
                addDialog.dispose();
                reload.run();
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

    class CourseButtonEditor extends DefaultCellEditor {
        private JButton modifyButton;
        private JButton deleteButton;
        private JPanel panel;
        private String courseId;
        private DefaultTableModel model;
        private JTable table;
        public CourseButtonEditor(JTextField textField, DefaultTableModel model, JTable table) {
            super(textField);
            this.model = model;
            this.table = table;
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            modifyButton = new JButton("修改");
            modifyButton.addActionListener(e -> {
                courseId = (String) table.getValueAt(table.getSelectedRow(), 1);
                showModifyCourseDialog(courseId, model, table);
                fireEditingStopped();
            });
            deleteButton = new JButton("删除");
            deleteButton.addActionListener(e -> {
                courseId = (String) table.getValueAt(table.getSelectedRow(), 1);
                int confirm = JOptionPane.showConfirmDialog(null,
                        "确认删除课程编号为 " + courseId + " 的课程吗？", "确认删除",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    CourseService courseService = new CourseServiceImpl();
                    courseService.deleteCourse(courseId);
                    model.removeRow(table.getSelectedRow());
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
            return courseId;
        }
    }

    private void showModifyCourseDialog(String courseId, DefaultTableModel model, JTable table) {
        CourseService courseService = new CourseServiceImpl();
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            JOptionPane.showMessageDialog(frame, "未找到该课程信息！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JDialog modifyDialog = new JDialog(frame, "修改课程信息", true);
        modifyDialog.setSize(400, 400);
        modifyDialog.setLocationRelativeTo(null);
        modifyDialog.setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JTextField nameField = new JTextField(course.getName());
        JTextField creditsField = new JTextField(course.getCredits() + "");
        JTextField typeField = new JTextField(course.getType());
        JTextField semesterField = new JTextField(course.getSemester());
        JTextField hoursField = new JTextField(course.getHours() + "");
        JTextField descField = new JTextField(course.getDescription());
        formPanel.add(new JLabel("课程编号:"));
        formPanel.add(new JLabel(course.getId()));
        formPanel.add(new JLabel("课程名称:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("学分:"));
        formPanel.add(creditsField);
        formPanel.add(new JLabel("类型:"));
        formPanel.add(typeField);
        formPanel.add(new JLabel("学期:"));
        formPanel.add(semesterField);
        formPanel.add(new JLabel("学时:"));
        formPanel.add(hoursField);
        formPanel.add(new JLabel("描述:"));
        formPanel.add(descField);
        modifyDialog.add(formPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        JButton confirmButton = new JButton("确认");
        confirmButton.addActionListener(e -> {
            try {
                course.setName(nameField.getText());
                course.setCredits(Double.valueOf(creditsField.getText()));
                course.setType(typeField.getText());
                course.setSemester(semesterField.getText());
                course.setHours(Integer.valueOf(hoursField.getText()));
                course.setDescription(descField.getText());
                courseService.updateCourse(course);
                JOptionPane.showMessageDialog(modifyDialog, "修改成功！");
                modifyDialog.dispose();
                int row = table.getSelectedRow();
                table.setValueAt(course.getName(), row, 2);
                table.setValueAt(course.getCredits(), row, 3);
                table.setValueAt(course.getType(), row, 4);
                table.setValueAt(course.getSemester(), row, 5);
                table.setValueAt(course.getHours(), row, 6);
                table.setValueAt(course.getDescription(), row, 7);
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

    private void showReportEntryPanel() {
        JDialog dialog = new JDialog(frame, "成绩报表中心", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());
        JLabel title = new JLabel("成绩报表中心", SwingConstants.CENTER);
        title.setFont(new Font("微软雅黑", Font.BOLD, 22));
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        dialog.add(title, BorderLayout.NORTH);
        JPanel btnPanel = new JPanel(new GridLayout(10, 1, 15, 15));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        String[] btnNames = {
            "学生成绩单导出",
            "班级成绩汇总报表",
            "课程成绩分布报表",
            "学期成绩统计报表",
            "教师所授课程成绩报表",
            "成绩排名报表",
            "不及格名单报表",
            "优秀学生名单报表",
            "成绩趋势分析报表",
            "自定义条件报表"
        };
        for (String name : btnNames) {
            JButton btn = new JButton(name);
            btn.setFont(new Font("微软雅黑", Font.PLAIN, 16));
            btn.setBackground(new Color(173, 216, 230));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
            btnPanel.add(btn);

            if (name.equals("学生成绩单导出")) {
                btn.addActionListener(e -> showStudentTranscriptExportDialog(dialog));
            }
            // 其他按钮的事件处理后续实现
        }
        dialog.add(btnPanel, BorderLayout.CENTER);
        JButton closeBtn = new JButton("关闭");
        closeBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        closeBtn.addActionListener(e -> dialog.dispose());
        JPanel closePanel = new JPanel();
        closePanel.add(closeBtn);
        dialog.add(closePanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showStudentTranscriptExportDialog(JDialog parent) {
        JDialog dialog = new JDialog(parent, "学生成绩单导出", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("请输入学生学号："));
        JTextField idField = new JTextField(15);
        panel.add(idField);

        JButton exportBtn = new JButton("导出Excel");
        panel.add(exportBtn);

        dialog.add(panel, BorderLayout.CENTER);

        exportBtn.addActionListener(e -> {
            String studentId = idField.getText().trim();
            if (studentId.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "请输入学号！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            exportStudentTranscript(studentId, dialog);
        });

        dialog.setVisible(true);
    }

    private void exportStudentTranscript(String studentId, JDialog parent) {
        GradeService gradeService = new GradeServiceImpl();
        StudentService studentService = new StudentServiceImpl();

        List<Grade> grades = gradeService.queryGrades(studentId, null, null, null);
        Student student = studentService.getStudentById(studentId);

        if (student == null) {
            JOptionPane.showMessageDialog(parent, "未找到该学生信息！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (grades == null || grades.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "该学生无成绩记录！", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("保存成绩单");
        fileChooser.setSelectedFile(new java.io.File(student.getName() + "-成绩单.xlsx"));

        int userSelection = fileChooser.showSaveDialog(parent);
        if (userSelection != JFileChooser.APPROVE_OPTION) return;

        String filePath = fileChooser.getSelectedFile().getAbsolutePath();
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("成绩单");

            // 创建学生信息行
            int rowIdx = 0;
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("学号");
            row.createCell(1).setCellValue(student.getId());
            row.createCell(2).setCellValue("姓名");
            row.createCell(3).setCellValue(student.getName());

            row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("班级");
            row.createCell(1).setCellValue(student.getClassName());
            row.createCell(2).setCellValue("专业");
            row.createCell(3).setCellValue(student.getMajor());

            // 创建成绩表头
            row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue("课程编号");
            row.createCell(1).setCellValue("课程名称");
            row.createCell(2).setCellValue("学分");
            row.createCell(3).setCellValue("成绩");
            row.createCell(4).setCellValue("学年");
            row.createCell(5).setCellValue("学期");

            // 填充成绩数据
            for (Grade g : grades) {
                Row r = sheet.createRow(rowIdx++);
                r.createCell(0).setCellValue(g.getCourseId() != null ? g.getCourseId() : "");
                r.createCell(1).setCellValue(g.getCourseName() != null ? g.getCourseName() : "");
                r.createCell(2).setCellValue(g.getCredits() != null ? g.getCredits() : 0);
                r.createCell(3).setCellValue(g.getScore() != null ? g.getScore() : 0);
                r.createCell(4).setCellValue(g.getAcademicYear() != null ? g.getAcademicYear() : "");
                r.createCell(5).setCellValue(g.getSemester() != null ? g.getSemester() : "");
            }

            // 自动调整列宽
            for (int i = 0; i < 6; i++) {
                sheet.autoSizeColumn(i);
            }

            // 保存文件
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }

            JOptionPane.showMessageDialog(parent, "导出成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(parent, "导出失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 学生档案管理功能已完善，包括：
    // 1. 学生信息的增删改查（已实现）
    // 2. 支持按学号、姓名、班级、专业、学院搜索（已实现）
    // 3. 添加、修改、删除均有弹窗提示，数据实时刷新（已实现）
    // 4. 表格支持操作按钮（已实现）
    // 5. 可扩展导入导出、批量操作等功能（如需请告知）
    // 如需进一步完善（如导入/导出、批量删除、分页、Excel导出等），请说明具体需求。

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminUI());
    }
}
