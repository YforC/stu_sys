package ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import entity.*;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import service.*;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class TeacherUI {
    private JFrame frame;
    private String teacherId;

    public TeacherUI(String teacherId) {
        this.teacherId = teacherId;
        // 创建主窗口
        frame = new JFrame("学生成绩管理系统 - 教师界面");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // 设置主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 248, 255)); // 浅蓝色背景

        // 添加标题
        JLabel titleLabel = new JLabel("欢迎使用学生成绩管理系统 - 教师界面", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        titleLabel.setForeground(new Color(0, 102, 204));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // 添加功能按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(new Dimension(800, 50));

        JButton inputGradesButton = new JButton("录入/修改学生成绩");
        inputGradesButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        inputGradesButton.setPreferredSize(new Dimension(160, 35));
        inputGradesButton.setBackground(new Color(173, 216, 230));
        inputGradesButton.setFocusPainted(false);
        inputGradesButton.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        buttonPanel.add(inputGradesButton);

        JButton viewStatisticsButton = new JButton("查询课程成绩统计");
        viewStatisticsButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        viewStatisticsButton.setPreferredSize(new Dimension(160, 35));
        viewStatisticsButton.setBackground(new Color(173, 216, 230));
        viewStatisticsButton.setFocusPainted(false);
        viewStatisticsButton.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        buttonPanel.add(viewStatisticsButton);

        JButton logoutButton = new JButton("退出登录");
        logoutButton.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        logoutButton.setPreferredSize(new Dimension(160, 35));
        logoutButton.setBackground(new Color(173, 216, 230));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        buttonPanel.add(logoutButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        // 添加信息显示区域
        JTextArea displayArea = new JTextArea();
        displayArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        displayArea.setEditable(false);
        displayArea.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(0, 102, 204)), "信息显示", 0, 0, new Font("微软雅黑", Font.BOLD, 14), new Color(0, 102, 204)));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 设置按钮事件
        inputGradesButton.addActionListener(e -> {
            JDialog dialog = new JDialog(frame, "录入/修改学生成绩", true);
            dialog.setSize(700, 500);
            dialog.setLocationRelativeTo(frame);
            dialog.setLayout(new BorderLayout());

            // 顶部：选择课程
            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topPanel.add(new JLabel("选择课程:"));
            TeachingTaskService teachingTaskService = new TeachingTaskServiceImpl();
            CourseService courseService = new CourseServiceImpl();
            // 直接用teacherId
            List<TeachingTask> tasks = teachingTaskService.queryTeachingTasks(null, teacherId, null, null, null);
            List<Course> courses = new ArrayList<>();
            for (TeachingTask t : tasks) {
                Course c = courseService.getCourseById(t.getCourseId());
                if (c != null) courses.add(c);
            }
            JComboBox<String> courseCombo = new JComboBox<>();
            for (Course c : courses) {
                courseCombo.addItem(c.getId() + "-" + c.getName());
            }
            topPanel.add(courseCombo);
            dialog.add(topPanel, BorderLayout.NORTH);

            // 中部：学生成绩表
            String[] colNames = {"学号", "姓名", "平时成绩(20%)", "期中成绩(30%)", "期末成绩(50%)", "总评成绩"};
            Object[][] data = new Object[0][colNames.length];
            DefaultTableModel tableModel = new DefaultTableModel(data, colNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return column >= 2 && column <= 4; // 只允许编辑平时、期中、期末成绩
                }
            };
            JTable table = new JTable(tableModel);
            JScrollPane tablePane = new JScrollPane(table);

            // 添加成绩输入提示
            JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            infoPanel.add(new JLabel("<html><font color='blue'>成绩计算规则：总评 = 期末×50% + 期中×30% + 平时×20%</font></html>"));

            JPanel centerPanel = new JPanel(new BorderLayout());
            centerPanel.add(infoPanel, BorderLayout.NORTH);
            centerPanel.add(tablePane, BorderLayout.CENTER);
            dialog.add(centerPanel, BorderLayout.CENTER);

            // 底部：按钮
            JPanel bottomPanel = new JPanel();
            JButton loadBtn = new JButton("加载学生");
            JButton saveBtn = new JButton("保存成绩");
            bottomPanel.add(loadBtn);
            bottomPanel.add(saveBtn);
            dialog.add(bottomPanel, BorderLayout.SOUTH);

            // 加载学生按钮事件
            loadBtn.addActionListener(ev -> {
                int idx = courseCombo.getSelectedIndex();
                if (idx < 0) return;
                final TeachingTask task = tasks.get(idx);
                EnrollmentService enrollmentService = new EnrollmentServiceImpl();
                List<Enrollment> enrollments = enrollmentService.getEnrollmentsByCourseId(task.getCourseId());
                final List<Enrollment> finalEnrollments = enrollments;
                StudentService studentService = new StudentServiceImpl();
                GradeService gradeService = new GradeServiceImpl();
                // 查询已有成绩
                List<Grade> gradeList = gradeService.queryGrades(null, task.getId(), null, null);
                final Object[][] dataArr = new Object[finalEnrollments.size()][colNames.length];
                for (int i = 0; i < finalEnrollments.size(); i++) {
                    Enrollment en = finalEnrollments.get(i);
                    dataArr[i][0] = en.getStudentId();
                    Student stu = studentService.getStudentById(en.getStudentId());
                    dataArr[i][1] = stu != null ? stu.getName() : "";
                    // 查找该学生是否已有成绩
                    Grade g = null;
                    for (Grade gg : gradeList) {
                        if (gg.getStudentId().equals(en.getStudentId())) {
                            g = gg; break;
                        }
                    }
                    dataArr[i][2] = g != null && g.getRegularScore() != null ? g.getRegularScore() : "";
                    dataArr[i][3] = g != null && g.getMidtermScore() != null ? g.getMidtermScore() : "";
                    dataArr[i][4] = g != null && g.getFinalScore() != null ? g.getFinalScore() : "";
                    dataArr[i][5] = g != null && g.getTotalScore() != null ? g.getTotalScore() : "";
                }
                table.setModel(new DefaultTableModel(dataArr, colNames));
            });

            // 保存成绩按钮事件
            saveBtn.addActionListener(ev -> {
                int idx = courseCombo.getSelectedIndex();
                if (idx < 0) return;
                final TeachingTask task = tasks.get(idx);
                GradeService gradeService = new GradeServiceImpl();
                for (int i = 0; i < table.getRowCount(); i++) {
                    String studentId = (String) table.getValueAt(i, 0);
                    Double regular = parseDouble(table.getValueAt(i, 2));
                    Double mid = parseDouble(table.getValueAt(i, 3));
                    Double fin = parseDouble(table.getValueAt(i, 4));
                    Double total = parseDouble(table.getValueAt(i, 5));
                    if (!isValidScore(regular) || !isValidScore(mid) || !isValidScore(fin) || !isValidScore(total)) {
                        JOptionPane.showMessageDialog(dialog, "成绩必须在0-100之间！", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    // 判断是否已有成绩，已有则update，无则add
                    List<Grade> existGrades = gradeService.queryGrades(studentId, task.getId(), null, null);
                    if (existGrades != null && !existGrades.isEmpty()) {
                        Grade g = existGrades.get(0);
                        g.setRegularScore(regular);
                        g.setMidtermScore(mid);
                        g.setFinalScore(fin);
                        g.setStatus("公示中");
                        gradeService.updateGrade(g, teacherId, "教师录入/修改成绩");
                    } else {
                        Grade g = new Grade();
                        g.setStudentId(studentId);
                        g.setTeachingTaskId(task.getId());
                        g.setRegularScore(regular);
                        g.setMidtermScore(mid);
                        g.setFinalScore(fin);
                        g.setEntryTime(new Date());
                        g.setEntryBy(teacherId);
                        g.setStatus("公示中");
                        gradeService.addGrade(g);
                    }
                }
                // 保存成功后只显示提示信息，不关闭窗口
                JOptionPane.showMessageDialog(dialog, "成绩保存成功！可继续录入其他学生成绩，或手动关闭窗口。", "成功", JOptionPane.INFORMATION_MESSAGE);
            });

            dialog.setVisible(true);
        });
        viewStatisticsButton.addActionListener(e -> {
            JDialog dialog = new JDialog(frame, "课程成绩统计", true);
            dialog.setSize(900, 700);
            dialog.setLocationRelativeTo(frame);
            dialog.setLayout(new BorderLayout());

            // 顶部：选择课程
            JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            topPanel.add(new JLabel("选择课程:"));
            TeachingTaskService teachingTaskService = new TeachingTaskServiceImpl();
            CourseService courseService = new CourseServiceImpl();
            List<TeachingTask> tasks = teachingTaskService.queryTeachingTasks(null, teacherId, null, null, null);
            List<Course> courses = new ArrayList<>();
            for (TeachingTask t : tasks) {
                Course c = courseService.getCourseById(t.getCourseId());
                if (c != null) courses.add(c);
            }
            JComboBox<String> courseCombo = new JComboBox<>();
            for (Course c : courses) {
                courseCombo.addItem(c.getId() + "-" + c.getName());
            }
            topPanel.add(courseCombo);
            dialog.add(topPanel, BorderLayout.NORTH);

            // 中部面板：分为左右两部分
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            splitPane.setDividerLocation(450);

            // 左侧：统计信息表格
            JPanel leftPanel = new JPanel(new BorderLayout());
            leftPanel.setBorder(BorderFactory.createTitledBorder("成绩统计信息"));
            String[] colNames = {"统计指标", "数值"};
            Object[][] data = new Object[0][2];
            final JTable table = new JTable(data, colNames);
            leftPanel.add(new JScrollPane(table), BorderLayout.CENTER);
            splitPane.setLeftComponent(leftPanel);

            // 右侧：图表面板
            JPanel rightPanel = new JPanel(new GridLayout(2, 1));
            rightPanel.setBorder(BorderFactory.createTitledBorder("统计图表"));

            // 用于存放图表的面板
            final JPanel chartPanel1 = new JPanel(new BorderLayout());
            final JPanel chartPanel2 = new JPanel(new BorderLayout());
            rightPanel.add(chartPanel1);
            rightPanel.add(chartPanel2);
            splitPane.setRightComponent(rightPanel);

            dialog.add(splitPane, BorderLayout.CENTER);

            // 添加查询按钮
            JButton queryBtn = new JButton("查看统计");
            JPanel bottomPanel = new JPanel();
            bottomPanel.add(queryBtn);
            dialog.add(bottomPanel, BorderLayout.SOUTH);

            // 查询按钮事件
            queryBtn.addActionListener(ev -> {
                int idx = courseCombo.getSelectedIndex();
                if (idx < 0) return;
                TeachingTask task = tasks.get(idx);
                GradeService gradeService = new GradeServiceImpl();
                List<Grade> grades = gradeService.queryGrades(null, task.getId(), null, null);

                // 计算统计数据
                double avgScore = grades.stream()
                    .filter(g -> g.getTotalScore() != null)
                    .mapToDouble(Grade::getTotalScore)
                    .average()
                    .orElse(0.0);
                double maxScore = grades.stream()
                    .filter(g -> g.getTotalScore() != null)
                    .mapToDouble(Grade::getTotalScore)
                    .max()
                    .orElse(0.0);
                double minScore = grades.stream()
                    .filter(g -> g.getTotalScore() != null)
                    .mapToDouble(Grade::getTotalScore)
                    .min()
                    .orElse(0.0);
                long totalStudents = grades.size();
                long passCount = grades.stream()
                    .filter(g -> g.getTotalScore() != null && g.getTotalScore() >= 60)
                    .count();
                long excellentCount = grades.stream()
                    .filter(g -> g.getTotalScore() != null && g.getTotalScore() >= 90)
                    .count();
                double passRate = totalStudents > 0 ? (double)passCount / totalStudents * 100 : 0;
                double excellentRate = totalStudents > 0 ? (double)excellentCount / totalStudents * 100 : 0;

                // 更新表格
                Object[][] newData = {
                    {"平均分", String.format("%.2f", avgScore)},
                    {"最高分", String.format("%.2f", maxScore)},
                    {"最低分", String.format("%.2f", minScore)},
                    {"总人数", totalStudents},
                    {"及格人数", passCount},
                    {"及格率", String.format("%.2f%%", passRate)},
                    {"优秀人数", excellentCount},
                    {"优秀率", String.format("%.2f%%", excellentRate)}
                };
                table.setModel(new DefaultTableModel(newData, colNames));

                // 创建成绩分布饼图
                DefaultPieDataset pieDataset = new DefaultPieDataset();
                long failCount = totalStudents - passCount;
                long goodCount = passCount - excellentCount;
                pieDataset.setValue("不及格(< 60分)", failCount);
                pieDataset.setValue("及格(60-89分)", goodCount);
                pieDataset.setValue("优秀(>= 90分)", excellentCount);

                JFreeChart pieChart = ChartFactory.createPieChart(
                    "成绩分布",  // 图表标题
                    pieDataset,
                    true,  // 是否显示图例
                    true,
                    false
                );
                // 设置中文字体
                pieChart.getTitle().setFont(new Font("微软雅黑", Font.BOLD, 18));
                pieChart.getLegend().setItemFont(new Font("微软雅黑", Font.PLAIN, 14));
                // 设置饼图样式
                PiePlot plot = (PiePlot) pieChart.getPlot();
                plot.setLabelFont(new Font("微软雅黑", Font.PLAIN, 12));
                plot.setLabelGap(0.02);
                plot.setOutlinePaint(null);
                plot.setBackgroundPaint(Color.WHITE);

                chartPanel1.removeAll();
                chartPanel1.add(new ChartPanel(pieChart));

                // 创建成绩分段柱状图
                DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
                long count0_59 = grades.stream()
                    .filter(g -> g.getTotalScore() != null && g.getTotalScore() < 60)
                    .count();
                long count60_69 = grades.stream()
                    .filter(g -> g.getTotalScore() != null && g.getTotalScore() >= 60 && g.getTotalScore() < 70)
                    .count();
                long count70_79 = grades.stream()
                    .filter(g -> g.getTotalScore() != null && g.getTotalScore() >= 70 && g.getTotalScore() < 80)
                    .count();
                long count80_89 = grades.stream()
                    .filter(g -> g.getTotalScore() != null && g.getTotalScore() >= 80 && g.getTotalScore() < 90)
                    .count();
                long count90_100 = grades.stream()
                    .filter(g -> g.getTotalScore() != null && g.getTotalScore() >= 90)
                    .count();

                // 添加数据，使用中文标签
                barDataset.addValue(count0_59, "人数", "0-59分");
                barDataset.addValue(count60_69, "人数", "60-69分");
                barDataset.addValue(count70_79, "人数", "70-79分");
                barDataset.addValue(count80_89, "人数", "80-89分");
                barDataset.addValue(count90_100, "人数", "90-100分");

                JFreeChart barChart = ChartFactory.createBarChart(
                    "成绩分段统计",     // 图表标题
                    "分数段",           // X轴标签
                    "人数",            // Y轴标签
                    barDataset,
                    PlotOrientation.VERTICAL,
                    true,
                    true,
                    false
                );

                // 设置中文字体
                barChart.getTitle().setFont(new Font("微软雅黑", Font.BOLD, 18));
                barChart.getLegend().setItemFont(new Font("微软雅黑", Font.PLAIN, 14));

                // 设置柱状图样式
                CategoryPlot categoryPlot = barChart.getCategoryPlot();
                categoryPlot.setBackgroundPaint(Color.WHITE);
                categoryPlot.setDomainGridlinePaint(Color.LIGHT_GRAY);
                categoryPlot.setRangeGridlinePaint(Color.LIGHT_GRAY);

                // 设置X轴
                CategoryAxis domainAxis = categoryPlot.getDomainAxis();
                domainAxis.setTickLabelFont(new Font("微软雅黑", Font.PLAIN, 12));
                domainAxis.setLabelFont(new Font("微软雅黑", Font.BOLD, 14));

                // 设置Y轴
                NumberAxis rangeAxis = (NumberAxis) categoryPlot.getRangeAxis();
                rangeAxis.setTickLabelFont(new Font("微软雅黑", Font.PLAIN, 12));
                rangeAxis.setLabelFont(new Font("微软雅黑", Font.BOLD, 14));
                rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

                // 设置柱子样式
                BarRenderer renderer = (BarRenderer) categoryPlot.getRenderer();
                renderer.setMaximumBarWidth(0.1);
                renderer.setSeriesPaint(0, new Color(79, 129, 189));

                chartPanel2.removeAll();
                chartPanel2.add(new ChartPanel(barChart));

                // 刷新图表显示
                chartPanel1.revalidate();
                chartPanel2.revalidate();
            });

            dialog.setVisible(true);
        });
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

    public TeacherUI() {
        this(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TeacherUI());
    }

    // 工具方法
    private boolean isValidScore(Double d) {
        return d == null || (d >= 0 && d <= 100);
    }
    private Double parseDouble(Object o) {
        if (o == null || o.toString().trim().isEmpty()) return null;
        try { return Double.parseDouble(o.toString().trim()); } catch (Exception e) { return null; }
    }
}
