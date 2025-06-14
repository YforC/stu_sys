package ui;

import entity.Student;
import entity.Teacher;
import entity.Course;
import java.awt.Font;
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
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Date;

public class AdminUI {
    private JFrame frame;
    private StudentService studentService = new StudentServiceImpl();
    private GradeService gradeService = new GradeServiceImpl();
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
            "成绩排名报表",
            "不及格名单报表",
            "优秀学生名单报表",
            "成绩趋势分析报表"
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
            } else if (name.equals("班级成绩汇总报表")) {
                btn.addActionListener(e -> showClassGradeSummaryDialog(dialog));
            } else if (name.equals("课程成绩分布报表")) {
                btn.addActionListener(e -> showCourseGradeSummaryDialog(dialog));
            } else if (name.equals("成绩排名报表")) {
                btn.addActionListener(e -> showGradeRankingDialog(dialog));
            } else if (name.equals("不及格名单报表")) {
                btn.addActionListener(e -> showFailingStudentsDialog(dialog));
            } else if (name.equals("优秀学生名单报表")) {
                btn.addActionListener(e -> showExcellentStudentsDialog(dialog));
            } else if (name.equals("成绩趋势分析报表")) {
                btn.addActionListener(e -> showGradeTrendAnalysisDialog());
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

            // 获取包含课程信息的成绩数据
            List<Map<String, Object>> gradesWithInfo = gradeService.getGradesWithCourseInfo(student.getId());
            
            // 填充成绩数据
            for (Map<String, Object> gradeInfo : gradesWithInfo) {
                Grade g = (Grade) gradeInfo.get("grade");
                String courseId = (String) gradeInfo.get("courseId");
                String courseName = (String) gradeInfo.get("courseName");
                Double credits = (Double) gradeInfo.get("credits");
                
                Row r = sheet.createRow(rowIdx++);
                r.createCell(0).setCellValue(courseId != null ? courseId : "");
                r.createCell(1).setCellValue(courseName != null ? courseName : "");
                r.createCell(2).setCellValue(credits != null ? credits : 0);
                r.createCell(3).setCellValue(g.getTotalScore() != null ? g.getTotalScore() : 0);
            }

            // 自动调整列宽
            for (int i = 0; i < 4; i++) {
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
    private void showClassGradeSummaryDialog(JDialog parent) {
        JDialog dialog = new JDialog(parent, "班级成绩汇总报表", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());
        
        // 创建输入面板
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("查询条件"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // 班级名称输入
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("班级名称:"), gbc);
        JTextField classNameField = new JTextField(15);
        gbc.gridx = 1;
        inputPanel.add(classNameField, gbc);
        
        // 学年输入
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("学年(可选):"), gbc);
        JTextField academicYearField = new JTextField(15);
        academicYearField.setToolTipText("例如: 2023-2024，留空查询所有学年");
        gbc.gridx = 1;
        inputPanel.add(academicYearField, gbc);
        
        // 学期输入
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("学期(可选):"), gbc);
        JComboBox<String> semesterCombo = new JComboBox<>(new String[]{"", "春", "秋"});
        gbc.gridx = 1;
        inputPanel.add(semesterCombo, gbc);
        
        dialog.add(inputPanel, BorderLayout.CENTER);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton exportBtn = new JButton("导出报表");
        exportBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        exportBtn.setBackground(new Color(0, 123, 255));
        exportBtn.setForeground(Color.WHITE);
        exportBtn.setFocusPainted(false);
        
        exportBtn.addActionListener(e -> {
            String className = classNameField.getText().trim();
            if (className.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "请输入班级名称！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String academicYear = academicYearField.getText().trim();
            if (academicYear.isEmpty()) academicYear = null;
            
            String semester = (String) semesterCombo.getSelectedItem();
            if (semester.isEmpty()) semester = null;
            
            exportClassGradeSummary(className, academicYear, semester);
            dialog.dispose();
        });
        
        JButton cancelBtn = new JButton("取消");
        cancelBtn.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(exportBtn);
        buttonPanel.add(cancelBtn);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void exportClassGradeSummary(String className, String academicYear, String semester) {
        try {
            List<Map<String, Object>> summaryData = gradeService.getClassGradeSummary(className, academicYear, semester);
            
            if (summaryData.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "未找到该班级的成绩记录！", "提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // 创建Excel工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(className + "成绩汇总");
            
            // 创建标题行样式
            CellStyle titleStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            titleStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            
            // 创建表头样式
            CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            
            // 创建标题
            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            String title = className + "成绩汇总报表";
            if (academicYear != null) title += "（" + academicYear;
            if (semester != null) title += " " + semester;
            if (academicYear != null) title += "）";
            titleCell.setCellValue(title);
            titleCell.setCellStyle(titleStyle);
            
            // 合并标题单元格
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 6));
            
            // 创建表头
            Row headerRow = sheet.createRow(2);
            String[] headers = {"学号", "姓名", "专业", "课程编号", "课程名称", "学分", "总评成绩"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // 填充数据
            int rowIndex = 3;
            for (Map<String, Object> record : summaryData) {
                Row row = sheet.createRow(rowIndex++);
                
                row.createCell(0).setCellValue((String) record.get("studentId"));
                row.createCell(1).setCellValue((String) record.get("studentName"));
                row.createCell(2).setCellValue((String) record.get("major"));
                row.createCell(3).setCellValue((String) record.get("courseId"));
                row.createCell(4).setCellValue((String) record.get("courseName"));
                
                Double credits = (Double) record.get("credits");
                if (credits != null) {
                    row.createCell(5).setCellValue(credits);
                } else {
                    row.createCell(5).setCellValue("");
                }
                
                Double totalScore = (Double) record.get("totalScore");
                if (totalScore != null) {
                    row.createCell(6).setCellValue(totalScore);
                } else {
                    row.createCell(6).setCellValue("未录入");
                }
            }
            
            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // 保存文件
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("保存班级成绩汇总报表");
            fileChooser.setSelectedFile(new File(className + "成绩汇总.xlsx"));
            fileChooser.setFileFilter(new FileNameExtensionFilter("Excel文件 (*.xlsx)", "xlsx"));
            
            if (fileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                if (!file.getName().toLowerCase().endsWith(".xlsx")) {
                    file = new File(file.getAbsolutePath() + ".xlsx");
                }
                
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    workbook.write(fos);
                    JOptionPane.showMessageDialog(frame, "班级成绩汇总报表导出成功！\n文件保存至: " + file.getAbsolutePath(), 
                                                "导出成功", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            
            workbook.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "导出失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // 1. 学生信息的增删改查（已实现）
    // 2. 支持按学号、姓名、班级、专业、学院搜索（已实现）
    // 3. 添加、修改、删除均有弹窗提示，数据实时刷新（已实现）
    // 4. 表格支持操作按钮（已实现）
    // 5. 可扩展导入导出、批量操作等功能（如需请告知）
    // 如需进一步完善（如导入/导出、批量删除、分页、Excel导出等），请说明具体需求。
    
    private void showCourseGradeSummaryDialog(JDialog parent) {
        JDialog dialog = new JDialog(parent, "课程成绩分布报表", true);
        dialog.setSize(450, 300);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());
        
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        // 课程ID输入
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("课程ID:"), gbc);
        gbc.gridx = 1;
        JTextField courseIdField = new JTextField(15);
        inputPanel.add(courseIdField, gbc);
        
        // 学年输入
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("学年:"), gbc);
        gbc.gridx = 1;
        JTextField academicYearField = new JTextField(15);
        academicYearField.setText("2023-2024"); // 默认值
        inputPanel.add(academicYearField, gbc);
        
        // 学期输入
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(new JLabel("学期:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> semesterCombo = new JComboBox<>(new String[]{"春", "秋"});
        inputPanel.add(semesterCombo, gbc);
        
        dialog.add(inputPanel, BorderLayout.CENTER);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton exportButton = new JButton("导出报表");
        exportButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        exportButton.setBackground(new Color(173, 216, 230));
        exportButton.setFocusPainted(false);
        exportButton.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        
        JButton cancelButton = new JButton("取消");
        cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(exportButton);
        buttonPanel.add(cancelButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        exportButton.addActionListener(e -> {
            String courseId = courseIdField.getText().trim();
            String academicYear = academicYearField.getText().trim();
            String semester = (String) semesterCombo.getSelectedItem();
            
            if (courseId.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "请输入课程ID！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            exportCourseGradeSummary(courseId, academicYear, semester, dialog);
        });
        
        dialog.setVisible(true);
    }
    
    private void exportCourseGradeSummary(String courseId, String academicYear, String semester, JDialog parent) {
        try {
            // 获取课程成绩数据
            List<Map<String, Object>> gradeData = gradeService.getCourseGradeSummary(courseId, academicYear, semester);
            Map<String, Object> statistics = gradeService.getCourseGradeStatistics(courseId, academicYear, semester);
            
            if (gradeData.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "未找到符合条件的成绩数据！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // 创建Excel工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("课程成绩分布报表");
            
            // 创建标题样式
            CellStyle titleStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 16);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);
            
            // 创建表头样式
            CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);
            
            // 创建数据样式
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            
            int rowNum = 0;
            
            // 添加标题
            Row titleRow = sheet.createRow(rowNum++);
            Cell titleCell = titleRow.createCell(0);
            String courseName = statistics.get("courseName") != null ? statistics.get("courseName").toString() : "课程";
            titleCell.setCellValue(courseName + "成绩分布报表");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
            
            // 添加统计信息
            rowNum++; // 空行
            Row infoRow1 = sheet.createRow(rowNum++);
            infoRow1.createCell(0).setCellValue("课程ID: " + courseId);
            infoRow1.createCell(2).setCellValue("学年: " + academicYear);
            infoRow1.createCell(4).setCellValue("学期: " + semester);
            
            Row infoRow2 = sheet.createRow(rowNum++);
            infoRow2.createCell(0).setCellValue("总人数: " + statistics.get("totalCount"));
            infoRow2.createCell(2).setCellValue("平均分: " + statistics.get("averageScore"));
            infoRow2.createCell(4).setCellValue("及格率: " + statistics.get("passRate") + "%");
            
            Row infoRow3 = sheet.createRow(rowNum++);
            infoRow3.createCell(0).setCellValue("最高分: " + statistics.get("highestScore"));
            infoRow3.createCell(2).setCellValue("最低分: " + statistics.get("lowestScore"));
            infoRow3.createCell(4).setCellValue("优秀率: " + statistics.get("excellentRate") + "%");
            
            rowNum++; // 空行
            
            // 添加表头
            Row headerRow = sheet.createRow(rowNum++);
            String[] headers = {"学号", "姓名", "班级", "专业", "平时成绩", "期中成绩", "期末成绩", "总成绩"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // 添加数据
            for (Map<String, Object> record : gradeData) {
                Row dataRow = sheet.createRow(rowNum++);
                
                Cell cell0 = dataRow.createCell(0);
                cell0.setCellValue(record.get("studentId") != null ? record.get("studentId").toString() : "");
                cell0.setCellStyle(dataStyle);
                
                Cell cell1 = dataRow.createCell(1);
                cell1.setCellValue(record.get("studentName") != null ? record.get("studentName").toString() : "");
                cell1.setCellStyle(dataStyle);
                
                Cell cell2 = dataRow.createCell(2);
                cell2.setCellValue(record.get("className") != null ? record.get("className").toString() : "");
                cell2.setCellStyle(dataStyle);
                
                Cell cell3 = dataRow.createCell(3);
                cell3.setCellValue(record.get("major") != null ? record.get("major").toString() : "");
                cell3.setCellStyle(dataStyle);
                
                Cell cell4 = dataRow.createCell(4);
                cell4.setCellValue(record.get("regularScore") != null ? record.get("regularScore").toString() : "");
                cell4.setCellStyle(dataStyle);
                
                Cell cell5 = dataRow.createCell(5);
                cell5.setCellValue(record.get("midtermScore") != null ? record.get("midtermScore").toString() : "");
                cell5.setCellStyle(dataStyle);
                
                Cell cell6 = dataRow.createCell(6);
                cell6.setCellValue(record.get("finalScore") != null ? record.get("finalScore").toString() : "");
                cell6.setCellStyle(dataStyle);
                
                Cell cell7 = dataRow.createCell(7);
                cell7.setCellValue(record.get("totalScore") != null ? record.get("totalScore").toString() : "");
                cell7.setCellStyle(dataStyle);
            }
            
            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // 保存文件
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("保存课程成绩分布报表");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Excel文件 (*.xlsx)", "xlsx"));
            fileChooser.setSelectedFile(new File(courseName + "_成绩分布报表_" + academicYear + "_" + semester + ".xlsx"));
            
            int userSelection = fileChooser.showSaveDialog(parent);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getName().toLowerCase().endsWith(".xlsx")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".xlsx");
                }
                
                try (FileOutputStream outputStream = new FileOutputStream(fileToSave)) {
                    workbook.write(outputStream);
                    JOptionPane.showMessageDialog(parent, "课程成绩分布报表导出成功！\n文件保存位置: " + fileToSave.getAbsolutePath(), "成功", JOptionPane.INFORMATION_MESSAGE);
                    parent.dispose();
                }
            }
            
            workbook.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "导出失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void showGradeRankingDialog(JDialog parent) {
        JDialog dialog = new JDialog(parent, "成绩排名报表", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());
        
        // 创建主面板
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // 排名范围选择
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("排名范围:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> scopeCombo = new JComboBox<>(new String[]{"班级排名", "全校排名"});
        mainPanel.add(scopeCombo, gbc);
        
        // 班级名称输入（仅班级排名时显示）
        gbc.gridx = 0; gbc.gridy = 1;
        JLabel classLabel = new JLabel("班级名称:");
        mainPanel.add(classLabel, gbc);
        gbc.gridx = 1;
        JTextField classField = new JTextField(15);
        mainPanel.add(classField, gbc);
        
        // 学年输入
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("学年:"), gbc);
        gbc.gridx = 1;
        JTextField yearField = new JTextField(15);
        yearField.setText("2023-2024");
        mainPanel.add(yearField, gbc);
        
        // 学期选择
        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("学期:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> semesterCombo = new JComboBox<>(new String[]{"春", "秋"});
        mainPanel.add(semesterCombo, gbc);
        
        // 排名类型选择
        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("排名类型:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> rankTypeCombo = new JComboBox<>(new String[]{"总分排名", "平均分排名", "单科成绩排名"});
        mainPanel.add(rankTypeCombo, gbc);
        
        // 课程ID输入（仅单科成绩排名时显示）
        gbc.gridx = 0; gbc.gridy = 5;
        JLabel courseLabel = new JLabel("课程ID:");
        mainPanel.add(courseLabel, gbc);
        gbc.gridx = 1;
        JTextField courseField = new JTextField(15);
        mainPanel.add(courseField, gbc);
        
        // 限制条数输入（仅全校排名时显示）
        gbc.gridx = 0; gbc.gridy = 6;
        JLabel limitLabel = new JLabel("显示前N名:");
        mainPanel.add(limitLabel, gbc);
        gbc.gridx = 1;
        JTextField limitField = new JTextField(15);
        limitField.setText("50");
        mainPanel.add(limitField, gbc);
        
        // 初始状态设置
        courseLabel.setVisible(false);
        courseField.setVisible(false);
        limitLabel.setVisible(false);
        limitField.setVisible(false);
        
        // 监听器设置
        scopeCombo.addActionListener(e -> {
            boolean isClass = "班级排名".equals(scopeCombo.getSelectedItem());
            classLabel.setVisible(isClass);
            classField.setVisible(isClass);
            limitLabel.setVisible(!isClass);
            limitField.setVisible(!isClass);
            dialog.revalidate();
        });
        
        rankTypeCombo.addActionListener(e -> {
            boolean isCourse = "单科成绩排名".equals(rankTypeCombo.getSelectedItem());
            courseLabel.setVisible(isCourse);
            courseField.setVisible(isCourse);
            dialog.revalidate();
        });
        
        dialog.add(mainPanel, BorderLayout.CENTER);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton generateBtn = new JButton("生成报表");
        JButton cancelBtn = new JButton("取消");
        
        generateBtn.addActionListener(e -> {
            try {
                String scope = (String) scopeCombo.getSelectedItem();
                String className = classField.getText().trim();
                String academicYear = yearField.getText().trim();
                String semester = (String) semesterCombo.getSelectedItem();
                String rankTypeStr = (String) rankTypeCombo.getSelectedItem();
                String courseId = courseField.getText().trim();
                String limitStr = limitField.getText().trim();
                
                // 参数验证
                if ("班级排名".equals(scope) && className.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "请输入班级名称！", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if ("单科成绩排名".equals(rankTypeStr) && courseId.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "请输入课程ID！", "提示", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // 转换排名类型
                String rankType;
                switch (rankTypeStr) {
                    case "总分排名": rankType = "total"; break;
                    case "平均分排名": rankType = "average"; break;
                    case "单科成绩排名": rankType = "course"; break;
                    default: rankType = "total";
                }
                
                Integer limit = null;
                if ("全校排名".equals(scope) && !limitStr.isEmpty()) {
                    try {
                        limit = Integer.parseInt(limitStr);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "显示条数必须是数字！", "提示", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                }
                
                exportGradeRanking(scope, className, academicYear, semester, rankType, courseId, limit, dialog);
                
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "生成报表失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(generateBtn);
        buttonPanel.add(cancelBtn);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void exportGradeRanking(String scope, String className, String academicYear, String semester, 
                                   String rankType, String courseId, Integer limit, JDialog parent) {
        try {
            GradeService gradeService = new GradeServiceImpl();
            List<Map<String, Object>> rankings;
            
            // 获取排名数据
            if ("班级排名".equals(scope)) {
                rankings = gradeService.getClassGradeRanking(className, academicYear, semester, rankType, courseId);
            } else {
                rankings = gradeService.getSchoolGradeRanking(academicYear, semester, rankType, courseId, limit);
            }
            
            if (rankings.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "未找到符合条件的排名数据！", "提示", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // 创建Excel工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("成绩排名报表");
            
            // 创建标题行
            Row titleRow = sheet.createRow(0);
            String title = scope + "_" + getRankTypeDisplayName(rankType) + "_" + academicYear + "_" + semester;
            if ("course".equals(rankType)) {
                title += "_课程" + courseId;
            }
            titleRow.createCell(0).setCellValue(title);
            
            // 创建表头
            Row headerRow = sheet.createRow(2);
            String[] headers;
            if ("course".equals(rankType)) {
                headers = new String[]{"排名", "学号", "姓名", "班级", "课程名称", "总评成绩", "平时成绩", "期中成绩", "期末成绩"};
                if ("全校排名".equals(scope)) {
                    headers = new String[]{"排名", "学号", "姓名", "班级", "专业", "课程名称", "总评成绩", "平时成绩", "期中成绩", "期末成绩"};
                }
            } else {
                headers = new String[]{"排名", "学号", "姓名", "班级", "总分", "课程数", "平均分"};
                if ("全校排名".equals(scope)) {
                    headers = new String[]{"排名", "学号", "姓名", "班级", "专业", "总分", "课程数", "平均分"};
                }
            }
            
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }
            
            // 填充数据
            int rowIdx = 3;
            for (Map<String, Object> ranking : rankings) {
                Row row = sheet.createRow(rowIdx++);
                int colIdx = 0;
                
                row.createCell(colIdx++).setCellValue((Integer) ranking.get("rank"));
                row.createCell(colIdx++).setCellValue((String) ranking.get("studentId"));
                row.createCell(colIdx++).setCellValue((String) ranking.get("studentName"));
                row.createCell(colIdx++).setCellValue((String) ranking.get("className"));
                
                if ("全校排名".equals(scope)) {
                    row.createCell(colIdx++).setCellValue((String) ranking.get("major"));
                }
                
                if ("course".equals(rankType)) {
                    row.createCell(colIdx++).setCellValue((String) ranking.get("courseName"));
                    row.createCell(colIdx++).setCellValue(ranking.get("totalScore") != null ? (Double) ranking.get("totalScore") : 0.0);
                    row.createCell(colIdx++).setCellValue(ranking.get("regularScore") != null ? ranking.get("regularScore").toString() : "");
                    row.createCell(colIdx++).setCellValue(ranking.get("midtermScore") != null ? ranking.get("midtermScore").toString() : "");
                    row.createCell(colIdx++).setCellValue(ranking.get("finalScore") != null ? ranking.get("finalScore").toString() : "");
                } else {
                    row.createCell(colIdx++).setCellValue((Double) ranking.get("totalScore"));
                    row.createCell(colIdx++).setCellValue((Integer) ranking.get("courseCount"));
                    row.createCell(colIdx++).setCellValue((Double) ranking.get("averageScore"));
                }
            }
            
            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // 保存文件
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("保存成绩排名报表");
            fileChooser.setFileFilter(new FileNameExtensionFilter("Excel文件 (*.xlsx)", "xlsx"));
            fileChooser.setSelectedFile(new File(title + ".xlsx"));
            
            int userSelection = fileChooser.showSaveDialog(parent);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getName().toLowerCase().endsWith(".xlsx")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".xlsx");
                }
                
                try (FileOutputStream outputStream = new FileOutputStream(fileToSave)) {
                    workbook.write(outputStream);
                    JOptionPane.showMessageDialog(parent, "成绩排名报表导出成功！\n文件保存位置: " + fileToSave.getAbsolutePath(), "成功", JOptionPane.INFORMATION_MESSAGE);
                    parent.dispose();
                }
            }
            
            workbook.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent, "导出失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String getRankTypeDisplayName(String rankType) {
        switch (rankType) {
            case "total": return "总分排名";
            case "average": return "平均分排名";
            case "course": return "单科成绩排名";
            default: return "排名";
        }
    }
    
    private void showFailingStudentsDialog(JDialog parent) {
        JDialog dialog = new JDialog(parent, "不及格名单报表", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());
        
        // 查询条件面板
        JPanel queryPanel = new JPanel(new GridBagLayout());
        queryPanel.setBorder(BorderFactory.createTitledBorder("查询条件"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // 班级选择 - 从数据库动态加载
        gbc.gridx = 0; gbc.gridy = 0;
        queryPanel.add(new JLabel("班级："), gbc);
        gbc.gridx = 1;
        JComboBox<String> classCombo = new JComboBox<>();
        classCombo.addItem(""); // 添加空选项
        try {
            List<String> classNames = gradeService.getAllClassNames();
            for (String className : classNames) {
                classCombo.addItem(className);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialog, "加载班级数据失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
        queryPanel.add(classCombo, gbc);
        
        // 学年选择 - 从数据库动态加载
        gbc.gridx = 0; gbc.gridy = 1;
        queryPanel.add(new JLabel("学年："), gbc);
        gbc.gridx = 1;
        JComboBox<String> yearCombo = new JComboBox<>();
        yearCombo.addItem(""); // 添加空选项
        try {
            List<String> academicYears = gradeService.getAllAcademicYears();
            for (String year : academicYears) {
                yearCombo.addItem(year);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialog, "加载学年数据失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
        queryPanel.add(yearCombo, gbc);
        
        // 学期选择
        gbc.gridx = 0; gbc.gridy = 2;
        queryPanel.add(new JLabel("学期："), gbc);
        gbc.gridx = 1;
        JComboBox<String> semesterCombo = new JComboBox<>(new String[]{"", "春", "秋"});
        queryPanel.add(semesterCombo, gbc);
        
        // 课程选择 - 从数据库动态加载
        gbc.gridx = 0; gbc.gridy = 3;
        queryPanel.add(new JLabel("课程："), gbc);
        gbc.gridx = 1;
        JComboBox<String> courseCombo = new JComboBox<>();
        courseCombo.addItem(""); // 添加空选项
        try {
            List<Map<String, Object>> courses = gradeService.getAllCourses();
            for (Map<String, Object> course : courses) {
                String courseId = (String) course.get("id");
                String courseName = (String) course.get("name");
                courseCombo.addItem(courseId + "-" + courseName);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(dialog, "加载课程数据失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
        queryPanel.add(courseCombo, gbc);
        
        dialog.add(queryPanel, BorderLayout.NORTH);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton queryBtn = new JButton("查询");
        JButton exportBtn = new JButton("导出Excel");
        JButton closeBtn = new JButton("关闭");
        
        buttonPanel.add(queryBtn);
        buttonPanel.add(exportBtn);
        buttonPanel.add(closeBtn);
        
        // 结果显示面板
        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBorder(BorderFactory.createTitledBorder("查询结果"));
        
        String[] columnNames = {"学号", "姓名", "班级", "专业", "课程", "总分", "平时成绩", "期中成绩", "期末成绩", "学年", "学期"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable resultTable = new JTable(tableModel);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setPreferredSize(new Dimension(450, 200));
        resultPanel.add(scrollPane, BorderLayout.CENTER);
        
        dialog.add(resultPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // 查询按钮事件
        queryBtn.addActionListener(e -> {
            String className = (String) classCombo.getSelectedItem();
            String academicYear = (String) yearCombo.getSelectedItem();
            String semester = (String) semesterCombo.getSelectedItem();
            String courseSelection = (String) courseCombo.getSelectedItem();
            String courseId = null;
            
            if (courseSelection != null && !courseSelection.isEmpty() && courseSelection.contains("-")) {
                courseId = courseSelection.split("-")[0];
            }
            
            try {
                List<Map<String, Object>> failingStudents = gradeService.getFailingStudents(
                    className.isEmpty() ? null : className,
                    academicYear.isEmpty() ? null : academicYear,
                    semester.isEmpty() ? null : semester,
                    courseId
                );
                
                // 清空表格
                tableModel.setRowCount(0);
                
                // 填充数据
                for (Map<String, Object> student : failingStudents) {
                    Object[] row = {
                        student.get("studentId"),
                        student.get("studentName"),
                        student.get("className"),
                        student.get("major"),
                        student.get("courseName"),
                        student.get("totalScore"),
                        student.get("regularScore"),
                        student.get("midtermScore"),
                        student.get("finalScore"),
                        student.get("academicYear"),
                        student.get("semester")
                    };
                    tableModel.addRow(row);
                }
                
                if (failingStudents.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "未找到符合条件的不及格学生记录", "查询结果", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "查询失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        
        // 导出按钮事件
        exportBtn.addActionListener(e -> {
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(dialog, "没有数据可导出，请先查询", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            exportFailingStudents(tableModel, dialog);
        });
        
        // 关闭按钮事件
        closeBtn.addActionListener(e -> dialog.dispose());
        
        dialog.setVisible(true);
    }
    
    private void exportFailingStudents(DefaultTableModel tableModel, JDialog parent) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("保存不及格名单报表");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel文件 (*.xlsx)", "xlsx"));
        fileChooser.setSelectedFile(new File("不及格名单报表_" + 
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xlsx"));
        
        if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".xlsx")) {
                file = new File(file.getAbsolutePath() + ".xlsx");
            }
            
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("不及格名单");
                
                // 创建标题行
                Row headerRow = sheet.createRow(0);
                String[] headers = {"学号", "姓名", "班级", "专业", "课程", "总分", "平时成绩", "期中成绩", "期末成绩", "学年", "学期"};
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                    
                    // 设置标题样式
                    CellStyle headerStyle = workbook.createCellStyle();
                    org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
                    headerFont.setBold(true);
                    headerStyle.setFont(headerFont);
                    headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
                    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    cell.setCellStyle(headerStyle);
                }
                
                // 填充数据
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        Cell cell = row.createCell(j);
                        Object value = tableModel.getValueAt(i, j);
                        if (value != null) {
                            if (value instanceof Number) {
                                cell.setCellValue(((Number) value).doubleValue());
                            } else {
                                cell.setCellValue(value.toString());
                            }
                        }
                    }
                }
                
                // 自动调整列宽
                for (int i = 0; i < headers.length; i++) {
                    sheet.autoSizeColumn(i);
                }
                
                // 保存文件
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    workbook.write(fos);
                }
                
                JOptionPane.showMessageDialog(parent, "不及格名单报表导出成功！\n文件路径：" + file.getAbsolutePath(), 
                    "导出成功", JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "导出失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    private void showExcellentStudentsDialog(JDialog parent) {
        JDialog dialog = new JDialog(parent, "优秀学生名单报表", true);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());
        
        // 标题
        JLabel title = new JLabel("优秀学生名单报表 (总分≥85分)", SwingConstants.CENTER);
        title.setFont(new Font("微软雅黑", Font.BOLD, 18));
        title.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        dialog.add(title, BorderLayout.NORTH);
        
        // 查询条件面板
        JPanel queryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        queryPanel.setBorder(BorderFactory.createTitledBorder("查询条件"));
        
        queryPanel.add(new JLabel("班级："));
        JComboBox<String> classComboBox = new JComboBox<>();
        classComboBox.addItem(""); // 空选项表示全部
        try {
            List<String> classNames = gradeService.getAllClassNames();
            for (String className : classNames) {
                classComboBox.addItem(className);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        queryPanel.add(classComboBox);
        
        queryPanel.add(new JLabel("学年："));
        JComboBox<String> yearComboBox = new JComboBox<>();
        yearComboBox.addItem(""); // 空选项表示全部
        try {
            List<String> academicYears = gradeService.getAllAcademicYears();
            for (String year : academicYears) {
                yearComboBox.addItem(year);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        queryPanel.add(yearComboBox);
        
        queryPanel.add(new JLabel("学期："));
        JComboBox<String> semesterComboBox = new JComboBox<>();
        semesterComboBox.addItem(""); // 空选项表示全部
        semesterComboBox.addItem("春");
        semesterComboBox.addItem("秋");
        queryPanel.add(semesterComboBox);
        
        queryPanel.add(new JLabel("课程："));
        JComboBox<String> courseComboBox = new JComboBox<>();
        courseComboBox.addItem(""); // 空选项表示全部
        try {
            List<Map<String, Object>> courses = gradeService.getAllCourses();
            for (Map<String, Object> course : courses) {
                courseComboBox.addItem(course.get("id") + " - " + course.get("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        queryPanel.add(courseComboBox);
        
        JButton queryBtn = new JButton("查询");
        queryBtn.setBackground(new Color(70, 130, 180));
        queryBtn.setForeground(Color.WHITE);
        queryPanel.add(queryBtn);
        
        dialog.add(queryPanel, BorderLayout.NORTH);
        
        // 结果表格
        String[] columnNames = {"学号", "姓名", "班级", "专业", "课程", "学分", "学年", "学期", "平时成绩", "期中成绩", "期末成绩", "总分"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        
        // 设置表格列宽
        table.getColumnModel().getColumn(0).setPreferredWidth(80);  // 学号
        table.getColumnModel().getColumn(1).setPreferredWidth(80);  // 姓名
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // 班级
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // 专业
        table.getColumnModel().getColumn(4).setPreferredWidth(120); // 课程
        table.getColumnModel().getColumn(5).setPreferredWidth(60);  // 学分
        table.getColumnModel().getColumn(6).setPreferredWidth(80);  // 学年
        table.getColumnModel().getColumn(7).setPreferredWidth(80);  // 学期
        table.getColumnModel().getColumn(8).setPreferredWidth(80);  // 平时成绩
        table.getColumnModel().getColumn(9).setPreferredWidth(80);  // 期中成绩
        table.getColumnModel().getColumn(10).setPreferredWidth(80); // 期末成绩
        table.getColumnModel().getColumn(11).setPreferredWidth(60); // 总分
        
        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane, BorderLayout.CENTER);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout());
        
        JButton exportBtn = new JButton("导出Excel");
        exportBtn.setBackground(new Color(34, 139, 34));
        exportBtn.setForeground(Color.WHITE);
        
        JButton closeBtn = new JButton("关闭");
        closeBtn.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(exportBtn);
        buttonPanel.add(closeBtn);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // 查询按钮事件
        queryBtn.addActionListener(e -> {
            try {
                String selectedClass = (String) classComboBox.getSelectedItem();
                String selectedYear = (String) yearComboBox.getSelectedItem();
                String selectedSemester = (String) semesterComboBox.getSelectedItem();
                String selectedCourse = (String) courseComboBox.getSelectedItem();
                
                // 处理课程选择（提取课程ID）
                String courseId = null;
                if (selectedCourse != null && !selectedCourse.trim().isEmpty()) {
                    String[] parts = selectedCourse.split(" - ");
                    if (parts.length > 0) {
                        courseId = parts[0];
                    }
                }
                
                // 清空表格
                tableModel.setRowCount(0);
                
                // 查询优秀学生数据
                List<Map<String, Object>> excellentStudents = gradeService.getExcellentStudents(
                    selectedClass != null && !selectedClass.trim().isEmpty() ? selectedClass : null,
                    selectedYear != null && !selectedYear.trim().isEmpty() ? selectedYear : null,
                    selectedSemester != null && !selectedSemester.trim().isEmpty() ? selectedSemester : null,
                    courseId
                );
                
                // 填充表格数据
                for (Map<String, Object> student : excellentStudents) {
                    Object[] rowData = {
                        student.get("studentId"),
                        student.get("studentName"),
                        student.get("className"),
                        student.get("major"),
                        student.get("courseName"),
                        student.get("credits"),
                        student.get("academicYear"),
                        student.get("semester"),
                        student.get("regularScore"),
                        student.get("midtermScore"),
                        student.get("finalScore"),
                        student.get("totalScore")
                    };
                    tableModel.addRow(rowData);
                }
                
                if (excellentStudents.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "未找到符合条件的优秀学生记录！", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "查询失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        
        // 导出Excel按钮事件
        exportBtn.addActionListener(e -> exportExcellentStudentsToExcel(dialog, tableModel));
        
        dialog.setVisible(true);
    }
    
    private void exportExcellentStudentsToExcel(JDialog parent, DefaultTableModel tableModel) {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(parent, "没有数据可以导出！", "提示", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("保存优秀学生名单报表");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel文件 (*.xlsx)", "xlsx"));
        fileChooser.setSelectedFile(new File("优秀学生名单报表_" + 
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xlsx"));
        
        if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".xlsx")) {
                file = new File(file.getAbsolutePath() + ".xlsx");
            }
            
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("优秀学生名单");
                
                // 创建标题行
                Row headerRow = sheet.createRow(0);
                String[] headers = {"学号", "姓名", "班级", "专业", "课程", "学分", "学年", "学期", "平时成绩", "期中成绩", "期末成绩", "总分"};
                
                // 设置标题样式
                CellStyle headerStyle = workbook.createCellStyle();
                headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                headerStyle.setBorderBottom(BorderStyle.THIN);
                headerStyle.setBorderTop(BorderStyle.THIN);
                headerStyle.setBorderRight(BorderStyle.THIN);
                headerStyle.setBorderLeft(BorderStyle.THIN);
                headerStyle.setAlignment(HorizontalAlignment.CENTER);
                
                org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);
                
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                    cell.setCellStyle(headerStyle);
                }
                
                // 创建数据行
                CellStyle dataStyle = workbook.createCellStyle();
                dataStyle.setBorderBottom(BorderStyle.THIN);
                dataStyle.setBorderTop(BorderStyle.THIN);
                dataStyle.setBorderRight(BorderStyle.THIN);
                dataStyle.setBorderLeft(BorderStyle.THIN);
                dataStyle.setAlignment(HorizontalAlignment.CENTER);
                
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        Cell cell = row.createCell(j);
                        Object value = tableModel.getValueAt(i, j);
                        if (value != null) {
                            cell.setCellValue(value.toString());
                        }
                        cell.setCellStyle(dataStyle);
                    }
                }
                
                // 自动调整列宽
                for (int i = 0; i < headers.length; i++) {
                    sheet.autoSizeColumn(i);
                }
                
                // 写入文件
                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                }
                
                JOptionPane.showMessageDialog(parent, "优秀学生名单报表导出成功！\n文件路径：" + file.getAbsolutePath(), 
                    "导出成功", JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(parent, "导出失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * 显示成绩趋势分析报表对话框
     */
    private void showGradeTrendAnalysisDialog() {
        JDialog dialog = new JDialog(frame, "成绩趋势分析报表", true);
        dialog.setSize(1000, 700);
        dialog.setLocationRelativeTo(frame);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // 查询条件面板
        JPanel queryPanel = new JPanel(new GridBagLayout());
        queryPanel.setBorder(BorderFactory.createTitledBorder("查询条件"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // 分析类型
        gbc.gridx = 0; gbc.gridy = 0;
        queryPanel.add(new JLabel("分析类型:"), gbc);
        gbc.gridx = 1;
        JComboBox<String> analysisTypeCombo = new JComboBox<>(new String[]{"班级", "课程", "学生"});
        queryPanel.add(analysisTypeCombo, gbc);
        
        // 目标选择
        gbc.gridx = 2; gbc.gridy = 0;
        queryPanel.add(new JLabel("选择目标:"), gbc);
        gbc.gridx = 3;
        JComboBox<String> targetCombo = new JComboBox<>();
        queryPanel.add(targetCombo, gbc);
        
        // 查询按钮
        gbc.gridx = 4; gbc.gridy = 0;
        JButton queryButton = new JButton("查询");
        queryPanel.add(queryButton, gbc);
        
        // 导出图表按钮
        gbc.gridx = 5; gbc.gridy = 0;
        JButton exportButton = new JButton("导出图表");
        exportButton.setEnabled(false);
        queryPanel.add(exportButton, gbc);
        
        mainPanel.add(queryPanel, BorderLayout.NORTH);
        
        // 图表显示面板
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBorder(BorderFactory.createTitledBorder("趋势分析图表"));
        JLabel noDataLabel = new JLabel("请选择查询条件并点击查询按钮", JLabel.CENTER);
        noDataLabel.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        noDataLabel.setForeground(Color.GRAY);
        chartPanel.add(noDataLabel, BorderLayout.CENTER);
        mainPanel.add(chartPanel, BorderLayout.CENTER);
        
        // 存储当前图表对象
        final org.jfree.chart.JFreeChart[] currentChart = {null};
        
        // 分析类型变化事件
        analysisTypeCombo.addActionListener(e -> {
            String selectedType = (String) analysisTypeCombo.getSelectedItem();
            targetCombo.removeAllItems();
            
            try {
                if ("班级".equals(selectedType)) {
                    List<String> classNames = gradeService.getAllClassNames();
                    for (String className : classNames) {
                        targetCombo.addItem(className);
                    }
                } else if ("课程".equals(selectedType)) {
                    List<Map<String, Object>> courses = gradeService.getAllCourses();
                    for (Map<String, Object> course : courses) {
                        targetCombo.addItem(course.get("id") + " - " + course.get("name"));
                    }
                } else if ("学生".equals(selectedType)) {
                    // 这里可以添加学生选择逻辑，暂时使用输入框
                    targetCombo.setEditable(true);
                    targetCombo.addItem("请输入学生ID");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "加载数据失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // 初始化班级数据
        analysisTypeCombo.setSelectedIndex(0);
        analysisTypeCombo.getActionListeners()[0].actionPerformed(null);
        
        // 查询按钮事件
        queryButton.addActionListener(e -> {
            String analysisType = (String) analysisTypeCombo.getSelectedItem();
            String selectedTarget = (String) targetCombo.getSelectedItem();
            
            if (selectedTarget == null || selectedTarget.trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "请选择查询目标", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                // 转换分析类型
                String typeKey = "";
                String targetId = "";
                String targetName = "";
                
                switch (analysisType) {
                    case "班级":
                        typeKey = "class";
                        targetId = selectedTarget;
                        targetName = selectedTarget;
                        break;
                    case "课程":
                        typeKey = "course";
                        String[] parts = selectedTarget.split(" - ");
                        targetId = parts[0];
                        targetName = selectedTarget;
                        break;
                    case "学生":
                        typeKey = "student";
                        targetId = selectedTarget;
                        targetName = selectedTarget;
                        break;
                }
                
                // 查询趋势数据
                List<Map<String, Object>> trendData = gradeService.getGradeTrendAnalysis(typeKey, targetId);
                
                if (trendData.isEmpty()) {
                    chartPanel.removeAll();
                    JLabel noDataLabel2 = new JLabel("未找到相关数据", JLabel.CENTER);
                    noDataLabel2.setFont(new Font("微软雅黑", Font.PLAIN, 16));
                    noDataLabel2.setForeground(Color.GRAY);
                    chartPanel.add(noDataLabel2, BorderLayout.CENTER);
                    chartPanel.revalidate();
                    chartPanel.repaint();
                    exportButton.setEnabled(false);
                    currentChart[0] = null;
                    return;
                }
                
                // 创建图表
                org.jfree.chart.JFreeChart chart = utils.ChartUtils.createTrendChart(
                    trendData, "成绩趋势分析", typeKey, targetName);
                
                // 创建图表面板
                org.jfree.chart.ChartPanel jfreeChartPanel = utils.ChartUtils.createChartPanel(chart);
                
                // 更新显示
                chartPanel.removeAll();
                chartPanel.add(jfreeChartPanel, BorderLayout.CENTER);
                chartPanel.revalidate();
                chartPanel.repaint();
                
                // 启用导出按钮
                exportButton.setEnabled(true);
                currentChart[0] = chart;
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "查询失败: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
        
        // 导出图表按钮事件
        exportButton.addActionListener(e -> {
            if (currentChart[0] != null) {
                utils.ChartUtils.showSaveDialog(dialog, currentChart[0]);
            }
        });
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminUI());
    }
}
