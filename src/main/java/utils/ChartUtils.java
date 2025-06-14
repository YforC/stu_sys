package utils;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * 图表工具类
 * 用于生成成绩趋势分析图表
 */
public class ChartUtils {
    
    /**
     * 创建成绩趋势分析图表
     * 
     * @param trendData 趋势数据
     * @param title 图表标题
     * @param analysisType 分析类型
     * @param targetName 目标名称
     * @return JFreeChart图表对象
     */
    public static JFreeChart createTrendChart(List<Map<String, Object>> trendData, 
                                            String title, String analysisType, String targetName) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        
        // 添加数据到数据集
        for (Map<String, Object> data : trendData) {
            String period = data.get("academicYear") + "-" + data.get("semester");
            Double avgScore = (Double) data.get("avgScore");
            Double passRate = (Double) data.get("passRate");
            Double excellentRate = (Double) data.get("excellentRate");
            
            if (avgScore != null) {
                dataset.addValue(avgScore, "平均分", period);
            }
            if (passRate != null) {
                dataset.addValue(passRate, "及格率(%)", period);
            }
            if (excellentRate != null) {
                dataset.addValue(excellentRate, "优秀率(%)", period);
            }
        }
        
        // 创建折线图
        JFreeChart chart = ChartFactory.createLineChart(
            title + " - " + getAnalysisTypeName(analysisType) + ": " + targetName,
            "学年学期",
            "数值",
            dataset,
            PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        
        // 设置图表样式
        customizeChart(chart);
        
        return chart;
    }
    
    /**
     * 自定义图表样式
     */
    private static void customizeChart(JFreeChart chart) {
        // 设置背景色
        chart.setBackgroundPaint(Color.WHITE);
        
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        
        // 设置渲染器
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesShapesVisible(2, true);
        
        // 设置线条颜色
        renderer.setSeriesPaint(0, new Color(255, 102, 102)); // 平均分 - 红色
        renderer.setSeriesPaint(1, new Color(102, 178, 255)); // 及格率 - 蓝色
        renderer.setSeriesPaint(2, new Color(102, 255, 102)); // 优秀率 - 绿色
        
        plot.setRenderer(renderer);
        
        // 设置字体
        Font font = new Font("微软雅黑", Font.PLAIN, 12);
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setLabelFont(font);
        domainAxis.setTickLabelFont(font);
        
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setLabelFont(font);
        rangeAxis.setTickLabelFont(font);
        
        chart.getTitle().setFont(new Font("微软雅黑", Font.BOLD, 16));
        chart.getLegend().setItemFont(font);
    }
    
    /**
     * 获取分析类型的中文名称
     */
    private static String getAnalysisTypeName(String analysisType) {
        switch (analysisType) {
            case "class": return "班级";
            case "course": return "课程";
            case "student": return "学生";
            default: return "未知";
        }
    }
    
    /**
     * 创建图表面板
     */
    public static ChartPanel createChartPanel(JFreeChart chart) {
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 600));
        chartPanel.setMouseWheelEnabled(true);
        return chartPanel;
    }
    
    /**
     * 将图表保存为PNG文件
     */
    public static void saveChartAsPNG(JFreeChart chart, String filePath, int width, int height) throws IOException {
        BufferedImage image = chart.createBufferedImage(width, height);
        File file = new File(filePath);
        ImageIO.write(image, "PNG", file);
    }
    
    /**
     * 将图表保存为JPEG文件
     */
    public static void saveChartAsJPEG(JFreeChart chart, String filePath, int width, int height) throws IOException {
        BufferedImage image = chart.createBufferedImage(width, height);
        File file = new File(filePath);
        ImageIO.write(image, "JPEG", file);
    }
    
    /**
     * 显示图表保存对话框
     */
    public static void showSaveDialog(Component parent, JFreeChart chart) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("保存图表");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        // 添加文件过滤器
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".png");
            }
            
            @Override
            public String getDescription() {
                return "PNG图片文件 (*.png)";
            }
        });
        
        fileChooser.addChoosableFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".jpg") || f.getName().toLowerCase().endsWith(".jpeg");
            }
            
            @Override
            public String getDescription() {
                return "JPEG图片文件 (*.jpg, *.jpeg)";
            }
        });
        
        int result = fileChooser.showSaveDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String filePath = selectedFile.getAbsolutePath();
            
            try {
                if (filePath.toLowerCase().endsWith(".png")) {
                    saveChartAsPNG(chart, filePath, 1200, 800);
                } else if (filePath.toLowerCase().endsWith(".jpg") || filePath.toLowerCase().endsWith(".jpeg")) {
                    saveChartAsJPEG(chart, filePath, 1200, 800);
                } else {
                    // 默认保存为PNG
                    saveChartAsPNG(chart, filePath + ".png", 1200, 800);
                }
                
                JOptionPane.showMessageDialog(parent, "图表已成功保存到: " + filePath, "保存成功", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "保存图表时发生错误: " + e.getMessage(), "保存失败", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}