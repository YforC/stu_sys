package service;

import dao.GradeDao;
import dao.GradeDaoImpl;
import entity.Grade;
import java.util.List;
import java.util.Map;

public class GradeServiceImpl implements GradeService {
    private final GradeDao gradeDao = new GradeDaoImpl();

    @Override
    public int addGrade(Grade grade) {
        // 业务规则：成绩有效范围0-100，必填项校验
        if (grade == null || grade.getStudentId() == null || grade.getTeachingTaskId() == null) {
            throw new IllegalArgumentException("学生ID和教学任务ID不能为空");
        }
        validateScore(grade);
        return gradeDao.addGrade(grade);
    }

    @Override
    public int updateGrade(Grade grade, String modifiedBy, String reason) {
        if (grade == null || grade.getId() == null) {
            throw new IllegalArgumentException("成绩ID不能为空");
        }
        validateScore(grade);
        if (modifiedBy == null || modifiedBy.isEmpty()) {
            throw new IllegalArgumentException("修改人不能为空");
        }
        if (reason == null || reason.isEmpty()) {
            throw new IllegalArgumentException("修改原因不能为空");
        }
        return gradeDao.updateGrade(grade, modifiedBy, reason);
    }

    @Override
    public Grade getGradeById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("成绩ID不能为空");
        }
        return gradeDao.getGradeById(id);
    }

    @Override
    public List<Grade> queryGrades(String studentId, String teachingTaskId, String academicYear, String semester) {
        return gradeDao.queryGrades(studentId, teachingTaskId, academicYear, semester);
    }

    @Override
    public List<Grade> queryGradesByPage(int offset, int limit) {
        if (offset < 0 || limit <= 0) {
            throw new IllegalArgumentException("分页参数不合法");
        }
        return gradeDao.queryGradesByPage(offset, limit);
    }

    @Override
    public int deleteGrade(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("成绩ID不能为空");
        }
        return gradeDao.deleteGrade(id);
    }

    @Override
    public List<Grade> getGradeHistory(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("成绩ID不能为空");
        }
        return gradeDao.getGradeHistory(id);
    }
    
    @Override
    public List<Map<String, Object>> getGradesWithCourseInfo(String studentId) {
        if (studentId == null || studentId.isEmpty()) {
            throw new IllegalArgumentException("学生ID不能为空");
        }
        return gradeDao.getGradesWithCourseInfo(studentId);
    }
    
    @Override
    public List<Map<String, Object>> getClassGradeSummary(String className, String academicYear, String semester) {
        if (className == null || className.trim().isEmpty()) {
            throw new IllegalArgumentException("班级名称不能为空");
        }
        return gradeDao.getClassGradeSummary(className.trim(), academicYear, semester);
    }
    
    @Override
    public List<Map<String, Object>> getCourseGradeSummary(String courseId, String academicYear, String semester) {
        return gradeDao.getCourseGradeSummary(courseId, academicYear, semester);
    }
    
    @Override
    public Map<String, Object> getCourseGradeStatistics(String courseId, String academicYear, String semester) {
        return gradeDao.getCourseGradeStatistics(courseId, academicYear, semester);
    }
    
    @Override
    public Map<String, Object> getClassGradeStatistics(String className, String academicYear, String semester) {
        return gradeDao.getClassGradeStatistics(className, academicYear, semester);
    }
    
    @Override
    public List<Map<String, Object>> getClassGradeRanking(String className, String academicYear, String semester, String rankType, String courseId) {
        if (className == null || className.trim().isEmpty()) {
            throw new IllegalArgumentException("班级名称不能为空");
        }
        if (rankType == null || rankType.trim().isEmpty()) {
            throw new IllegalArgumentException("排名类型不能为空");
        }
        if ("course".equals(rankType) && (courseId == null || courseId.trim().isEmpty())) {
            throw new IllegalArgumentException("按单科成绩排名时课程ID不能为空");
        }
        if (!"total".equals(rankType) && !"average".equals(rankType) && !"course".equals(rankType)) {
            throw new IllegalArgumentException("排名类型只能是total、average或course");
        }
        return gradeDao.getClassGradeRanking(className.trim(), academicYear, semester, rankType, courseId);
    }
    
    @Override
    public List<Map<String, Object>> getSchoolGradeRanking(String academicYear, String semester, String rankType, String courseId, Integer limit) {
        if (rankType == null || rankType.trim().isEmpty()) {
            throw new IllegalArgumentException("排名类型不能为空");
        }
        if ("course".equals(rankType) && (courseId == null || courseId.trim().isEmpty())) {
            throw new IllegalArgumentException("按单科成绩排名时课程ID不能为空");
        }
        if (!"total".equals(rankType) && !"average".equals(rankType) && !"course".equals(rankType)) {
            throw new IllegalArgumentException("排名类型只能是total、average或course");
        }
        if (limit != null && limit <= 0) {
            throw new IllegalArgumentException("限制条数必须大于0");
        }
        return gradeDao.getSchoolGradeRanking(academicYear, semester, rankType, courseId, limit);
    }

    @Override
    public List<Map<String, Object>> getFailingStudents(String className, String academicYear, String semester, String courseId) {
        // 参数校验
        if (className != null && className.trim().isEmpty()) {
            className = null;
        }
        if (academicYear != null && academicYear.trim().isEmpty()) {
            academicYear = null;
        }
        if (semester != null && semester.trim().isEmpty()) {
            semester = null;
        }
        if (courseId != null && courseId.trim().isEmpty()) {
            courseId = null;
        }
        
        return gradeDao.getFailingStudents(className, academicYear, semester, courseId);
    }

    private void validateScore(Grade grade) {
        if (grade.getRegularScore() != null && (grade.getRegularScore() < 0 || grade.getRegularScore() > 100)) {
            throw new IllegalArgumentException("平时成绩应在0-100之间");
        }
        if (grade.getMidtermScore() != null && (grade.getMidtermScore() < 0 || grade.getMidtermScore() > 100)) {
            throw new IllegalArgumentException("期中成绩应在0-100之间");
        }
        if (grade.getFinalScore() != null && (grade.getFinalScore() < 0 || grade.getFinalScore() > 100)) {
            throw new IllegalArgumentException("期末成绩应在0-100之间");
        }
        if (grade.getTotalScore() != null && (grade.getTotalScore() < 0 || grade.getTotalScore() > 100)) {
            throw new IllegalArgumentException("总评成绩应在0-100之间");
        }
    }
    
    @Override
    public List<String> getAllClassNames() {
        return gradeDao.getAllClassNames();
    }
    
    @Override
    public List<String> getAllAcademicYears() {
        return gradeDao.getAllAcademicYears();
    }
    
    @Override
    public List<Map<String, Object>> getAllCourses() {
        return gradeDao.getAllCourses();
    }
    
    @Override
    public List<Map<String, Object>> getExcellentStudents(String className, String academicYear, String semester, String courseId) {
        return gradeDao.getExcellentStudents(className, academicYear, semester, courseId);
    }
    
    @Override
    public List<Map<String, Object>> getGradeTrendAnalysis(String analysisType, String targetId) {
        if (analysisType == null || analysisType.trim().isEmpty()) {
            throw new IllegalArgumentException("分析类型不能为空");
        }
        if (!"class".equals(analysisType) && !"course".equals(analysisType) && !"student".equals(analysisType)) {
            throw new IllegalArgumentException("分析类型只能是class、course或student");
        }
        if (targetId == null || targetId.trim().isEmpty()) {
            throw new IllegalArgumentException("目标ID不能为空");
        }
        return gradeDao.getGradeTrendAnalysis(analysisType, targetId.trim());
    }
}

