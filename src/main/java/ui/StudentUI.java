package ui;

import service.CourseService;
import service.CourseServiceImpl;
import service.EnrollmentService;
import service.EnrollmentServiceImpl;
import service.GradeService;
import service.GradeServiceImpl;
import entity.Course;
import entity.Enrollment;
import entity.Grade;
import entity.TeachingTask;
import service.TeachingTaskService;
import service.TeachingTaskServiceImpl;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StudentUI {
    private JFrame frame;
    private String studentId;
    private CourseService courseService = new CourseServiceImpl();
    private EnrollmentService enrollmentService = new EnrollmentServiceImpl();
    private GradeService gradeService = new GradeServiceImpl();
    private TeachingTaskService teachingTaskService = new TeachingTaskServiceImpl();

    public StudentUI(String studentId) {
        this.studentId = studentId;
        frame = new JFrame("学生成绩管理系统 - 学生界面");
        frame.setSize(900, 650);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255));

        JLabel titleLabel = new JLabel("欢迎使用学生成绩管理系统 - 学生界面", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        titleLabel.setForeground(new Color(0, 102, 204));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // 查询条件面板
        JPanel queryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        queryPanel.setOpaque(false);
        JLabel searchLabel = new JLabel("查询条件:");
        searchLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        queryPanel.add(searchLabel);
        String[] searchOptions = {"课程编号", "课程名称"};
        JComboBox<String> searchComboBox = new JComboBox<>(searchOptions);
        searchComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        queryPanel.add(searchComboBox);
        JTextField searchField = new JTextField(18);
        searchField.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        queryPanel.add(searchField);
        mainPanel.add(queryPanel, BorderLayout.BEFORE_FIRST_LINE);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(new Dimension(900, 50));

        JButton viewCoursesButton = new JButton("查询可选课程");
        viewCoursesButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        viewCoursesButton.setPreferredSize(new Dimension(140, 35));
        viewCoursesButton.setBackground(new Color(173, 216, 230));
        viewCoursesButton.setFocusPainted(false);
        viewCoursesButton.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        buttonPanel.add(viewCoursesButton);

        JButton viewGradesButton = new JButton("查询课程成绩");
        viewGradesButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        viewGradesButton.setPreferredSize(new Dimension(140, 35));
        viewGradesButton.setBackground(new Color(173, 216, 230));
        viewGradesButton.setFocusPainted(false);
        viewGradesButton.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        buttonPanel.add(viewGradesButton);

        JButton logoutButton = new JButton("退出登录");
        logoutButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        logoutButton.setPreferredSize(new Dimension(140, 35));
        logoutButton.setBackground(new Color(173, 216, 230));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        buttonPanel.add(logoutButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // JTable显示区
        JTable table = new JTable();
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0, 102, 204)), "信息显示", 0, 0, new Font("微软雅黑", Font.BOLD, 14), new Color(0, 102, 204)));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // 查询课程按钮事件
        viewCoursesButton.addActionListener(e -> {
            String searchType = (String) searchComboBox.getSelectedItem();
            String searchValue = searchField.getText().trim();
            List<Course> courses;
            if (searchValue.isEmpty()) {
                courses = courseService.queryCourses(null, null, null, null);
            } else if ("课程编号".equals(searchType)) {
                Course c = courseService.getCourseById(searchValue);
                courses = c != null ? java.util.Collections.singletonList(c) : java.util.Collections.emptyList();
            } else {
                courses = courseService.queryCourses(searchValue, null, null, null);
            }
            String[] colNames = {"课程编号", "课程名称", "学分", "类型", "学期", "学时", "描述"};
            Object[][] data = new Object[courses.size()][colNames.length];
            for (int i = 0; i < courses.size(); i++) {
                Course c = courses.get(i);
                data[i][0] = c.getId();
                data[i][1] = c.getName();
                data[i][2] = c.getCredits();
                data[i][3] = c.getType();
                data[i][4] = c.getSemester();
                data[i][5] = c.getHours();
                data[i][6] = c.getDescription();
            }
            table.setModel(new javax.swing.table.DefaultTableModel(data, colNames));
        });

        // 查询成绩按钮事件
        viewGradesButton.addActionListener(e -> {
            String searchType = (String) searchComboBox.getSelectedItem();
            String searchValue = searchField.getText().trim();
            List<Grade> grades = gradeService.queryGrades(studentId, null, null, null);
            List<Grade> filteredGrades;
            if (searchValue.isEmpty()) {
                filteredGrades = grades;
            } else {
                filteredGrades = new java.util.ArrayList<>();
                for (Grade g : grades) {
                    TeachingTask task = teachingTaskService.getTeachingTaskById(g.getTeachingTaskId());
                    if (task != null) {
                        Course c = courseService.getCourseById(task.getCourseId());
                        if (c != null) {
                            if ("课程编号".equals(searchType) && c.getId().equals(searchValue)) {
                                filteredGrades.add(g);
                            } else if ("课程名称".equals(searchType) && c.getName().equals(searchValue)) {
                                filteredGrades.add(g);
                            }
                        }
                    }
                }
            }
            String[] colNames = {"课程编号", "课程名称", "学分", "类型", "学期", "平时成绩", "期中成绩", "期末成绩", "总评成绩", "成绩状态"};
            Object[][] data = new Object[filteredGrades.size()][colNames.length];
            for (int i = 0; i < filteredGrades.size(); i++) {
                Grade g = filteredGrades.get(i);
                String courseId = null;
                String courseName = "";
                Double credits = null;
                String type = "";
                String semester = "";
                TeachingTask task = teachingTaskService.getTeachingTaskById(g.getTeachingTaskId());
                if (task != null) {
                    courseId = task.getCourseId();
                    semester = task.getSemester();
                    Course c = courseService.getCourseById(courseId);
                    if (c != null) {
                        courseName = c.getName();
                        credits = c.getCredits();
                        type = c.getType();
                    }
                }
                data[i][0] = courseId != null ? courseId : "";
                data[i][1] = courseName;
                data[i][2] = credits != null ? credits : "";
                data[i][3] = type;
                data[i][4] = semester;
                data[i][5] = g.getRegularScore() != null ? g.getRegularScore() : "";
                data[i][6] = g.getMidtermScore() != null ? g.getMidtermScore() : "";
                data[i][7] = g.getFinalScore() != null ? g.getFinalScore() : "";
                data[i][8] = g.getTotalScore() != null ? g.getTotalScore() : "";
                data[i][9] = g.getStatus() != null ? g.getStatus() : "";
            }
            table.setModel(new javax.swing.table.DefaultTableModel(data, colNames));
        });

        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "确定要退出登录吗？", "确认", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                frame.dispose();
            }
        });

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }
}
