package service;

import dao.GradeStatisticsDao;
import dao.GradeStatisticsDaoImpl;
import entity.GradeStatistics;
import service.GradeService;
import service.GradeServiceImpl;
import service.StudentService;
import service.StudentServiceImpl;
import service.CourseService;
import service.CourseServiceImpl;
import entity.Student;
import entity.Course;
import java.util.List;
import java.util.Map;
import java.util.Date;

public class GradeStatisticsServiceImpl implements GradeStatisticsService {
    private final GradeStatisticsDao statisticsDao = new GradeStatisticsDaoImpl();
    private final GradeService gradeService = new GradeServiceImpl();
    private final StudentService studentService = new StudentServiceImpl();
    private final CourseService courseService = new CourseServiceImpl();

    @Override
    public int addGradeStatistics(GradeStatistics statistics) {
        // 业务校验：统计对象、类型、学年、学期不能为空，分数与比例范围校验
        if (statistics == null || statistics.getObjectId() == null || statistics.getObjectId().isEmpty()
                || statistics.getType() == null || statistics.getType().isEmpty()
                || statistics.getAcademicYear() == null || statistics.getAcademicYear().isEmpty()
                || statistics.getSemester() == null || statistics.getSemester().isEmpty()) {
            throw new IllegalArgumentException("统计对象、类型、学年、学期不能为空");
        }
        validateStatistics(statistics);
        return statisticsDao.addGradeStatistics(statistics);
    }

    @Override
    public GradeStatistics getStatisticsById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("统计编号不能为空");
        }
        return statisticsDao.getStatisticsById(id);
    }

    @Override
    public List<GradeStatistics> queryStatistics(String objectId, String type, String academicYear, String semester) {
        return statisticsDao.queryStatistics(objectId, type, academicYear, semester);
    }

    @Override
    public List<GradeStatistics> queryStatisticsByPage(int offset, int limit) {
        if (offset < 0 || limit <= 0) {
            throw new IllegalArgumentException("分页参数不合法");
        }
        return statisticsDao.queryStatisticsByPage(offset, limit);
    }

    private void validateStatistics(GradeStatistics s) {
        if (s.getAverageScore() != null && (s.getAverageScore() < 0 || s.getAverageScore() > 100)) {
            throw new IllegalArgumentException("平均分应在0-100之间");
        }
        if (s.getHighestScore() != null && (s.getHighestScore() < 0 || s.getHighestScore() > 100)) {
            throw new IllegalArgumentException("最高分应在0-100之间");
        }
        if (s.getLowestScore() != null && (s.getLowestScore() < 0 || s.getLowestScore() > 100)) {
            throw new IllegalArgumentException("最低分应在0-100之间");
        }
        if (s.getPassRate() != null && (s.getPassRate() < 0 || s.getPassRate() > 100)) {
            throw new IllegalArgumentException("及格率应在0-100之间");
        }
        if (s.getExcellentRate() != null && (s.getExcellentRate() < 0 || s.getExcellentRate() > 100)) {
            throw new IllegalArgumentException("优秀率应在0-100之间");
        }
    }
    
    @Override
    public int generateAllStatistics() {
        int count = 0;
        
        // 生成所有班级的统计数据
        List<Student> allStudents = studentService.queryStudentsByPage(0, Integer.MAX_VALUE);
        java.util.Set<String> classNames = new java.util.HashSet<>();
        for (Student student : allStudents) {
            if (student.getClassName() != null && !student.getClassName().isEmpty()) {
                classNames.add(student.getClassName());
            }
        }
        
        for (String className : classNames) {
            Map<String, Object> stats = gradeService.getClassGradeStatistics(className, null, null);
            if (stats != null && stats.get("totalCount") != null && (Integer) stats.get("totalCount") > 0) {
                GradeStatistics gradeStats = createGradeStatisticsFromMap(stats, className, "class");
                statisticsDao.addGradeStatistics(gradeStats);
                count++;
            }
        }
        
        // 生成所有课程的统计数据
        List<Course> allCourses = courseService.queryAllCourses();
        for (Course course : allCourses) {
            Map<String, Object> stats = gradeService.getCourseGradeStatistics(course.getId(), null, null);
            if (stats != null && stats.get("totalCount") != null && (Integer) stats.get("totalCount") > 0) {
                GradeStatistics gradeStats = createGradeStatisticsFromMap(stats, course.getId(), "course");
                statisticsDao.addGradeStatistics(gradeStats);
                count++;
            }
        }
        
        return count;
    }
    
    @Override
    public int generateSelectedStatistics(String type, String academicYear, String semester) {
        int count = 0;
        
        if ("class".equals(type)) {
            // 生成班级统计数据
            List<Student> allStudents = studentService.queryStudentsByPage(0, Integer.MAX_VALUE);
            java.util.Set<String> classNames = new java.util.HashSet<>();
            for (Student student : allStudents) {
                if (student.getClassName() != null && !student.getClassName().isEmpty()) {
                    classNames.add(student.getClassName());
                }
            }
            
            for (String className : classNames) {
                Map<String, Object> stats = gradeService.getClassGradeStatistics(className, academicYear, semester);
                if (stats != null && stats.get("totalCount") != null && (Integer) stats.get("totalCount") > 0) {
                    GradeStatistics gradeStats = createGradeStatisticsFromMap(stats, className, "class");
                    gradeStats.setAcademicYear(academicYear);
                    gradeStats.setSemester(semester);
                    statisticsDao.addGradeStatistics(gradeStats);
                    count++;
                }
            }
        } else if ("course".equals(type)) {
            // 生成课程统计数据
            List<Course> allCourses = courseService.queryAllCourses();
            for (Course course : allCourses) {
                Map<String, Object> stats = gradeService.getCourseGradeStatistics(course.getId(), academicYear, semester);
                if (stats != null && stats.get("totalCount") != null && (Integer) stats.get("totalCount") > 0) {
                    GradeStatistics gradeStats = createGradeStatisticsFromMap(stats, course.getId(), "course");
                    gradeStats.setAcademicYear(academicYear);
                    gradeStats.setSemester(semester);
                    statisticsDao.addGradeStatistics(gradeStats);
                    count++;
                }
            }
        }
        
        return count;
    }
    
    private GradeStatistics createGradeStatisticsFromMap(Map<String, Object> stats, String objectId, String type) {
        GradeStatistics gradeStats = new GradeStatistics();
        gradeStats.setObjectId(objectId);
        gradeStats.setType(type);
        gradeStats.setAcademicYear((String) stats.get("academicYear"));
        gradeStats.setSemester((String) stats.get("semester"));
        
        // 安全的类型转换，处理可能的BigDecimal类型
        Object avgScore = stats.get("averageScore");
        gradeStats.setAverageScore(avgScore != null ? ((Number) avgScore).doubleValue() : null);
        
        Object highScore = stats.get("highestScore");
        gradeStats.setHighestScore(highScore != null ? ((Number) highScore).doubleValue() : null);
        
        Object lowScore = stats.get("lowestScore");
        gradeStats.setLowestScore(lowScore != null ? ((Number) lowScore).doubleValue() : null);
        
        gradeStats.setPassCount((Integer) stats.get("passCount"));
        
        Object passRateObj = stats.get("passRate");
        gradeStats.setPassRate(passRateObj != null ? ((Number) passRateObj).doubleValue() : null);
        
        gradeStats.setExcellentCount((Integer) stats.get("excellentCount"));
        
        Object excellentRateObj = stats.get("excellentRate");
        gradeStats.setExcellentRate(excellentRateObj != null ? ((Number) excellentRateObj).doubleValue() : null);
        
        gradeStats.setStatisticsTime(new Date());
        return gradeStats;
    }
}

