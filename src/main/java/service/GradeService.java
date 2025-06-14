package service;

import entity.Grade;
import java.util.List;
import java.util.Map;

public interface GradeService {
    int addGrade(Grade grade);
    int updateGrade(Grade grade, String modifiedBy, String reason);
    Grade getGradeById(Integer id);
    List<Grade> queryGrades(String studentId, String teachingTaskId, String academicYear, String semester);
    List<Grade> queryGradesByPage(int offset, int limit);
    int deleteGrade(Integer id);
    List<Grade> getGradeHistory(Integer id);
    
    /**
     * 获取包含课程信息的成绩数据
     * 将Grade表与TeachingTask表和Course表关联，返回包含课程名称、学分、学年、学期等完整信息的成绩数据
     * 
     * @param studentId 学生ID
     * @return 包含完整课程信息的成绩数据列表，每个元素是一个Map，包含Grade对象及相关的课程信息
     */
    List<Map<String, Object>> getGradesWithCourseInfo(String studentId);
    
    /**
     * 获取班级成绩汇总数据
     * 通过班级名称查询该班级所有学生的成绩信息，包含学生基本信息和课程成绩
     * 
     * @param className 班级名称
     * @param academicYear 学年（可选，为null时查询所有学年）
     * @param semester 学期（可选，为null时查询所有学期）
     * @return 班级成绩汇总列表，每个元素包含学生信息、成绩信息和课程信息
     */
    List<Map<String, Object>> getClassGradeSummary(String className, String academicYear, String semester);
    
    /**
     * 获取课程成绩汇总数据
     * 通过课程ID查询该课程所有学生的成绩信息，包含学生基本信息和成绩详情
     * 
     * @param courseId 课程ID
     * @param academicYear 学年（可选，为null时查询所有学年）
     * @param semester 学期（可选，为null时查询所有学期）
     * @return 课程成绩汇总列表，每个元素包含学生信息、成绩信息和课程信息
     */
    List<Map<String, Object>> getCourseGradeSummary(String courseId, String academicYear, String semester);
    
    /**
     * 获取课程成绩统计数据
     * 计算指定课程的平均分、最高分、最低分、及格率、优秀率等统计信息
     * 
     * @param courseId 课程ID
     * @param academicYear 学年（可选，为null时查询所有学年）
     * @param semester 学期（可选，为null时查询所有学期）
     * @return 课程成绩统计信息Map
     */
    Map<String, Object> getCourseGradeStatistics(String courseId, String academicYear, String semester);
    
    /**
     * 获取班级成绩排名数据
     * 按照指定条件查询班级学生成绩排名，支持按总分、平均分、单科成绩等排序
     * 
     * @param className 班级名称
     * @param academicYear 学年（可选，为null时查询所有学年）
     * @param semester 学期（可选，为null时查询所有学期）
     * @param rankType 排名类型："total"(总分)、"average"(平均分)、"course"(单科成绩)
     * @param courseId 课程ID（当rankType为"course"时必填）
     * @return 成绩排名列表，包含学生信息、成绩信息和排名
     */
    List<Map<String, Object>> getClassGradeRanking(String className, String academicYear, String semester, String rankType, String courseId);
    
    /**
     * 获取全校成绩排名数据
     * 按照指定条件查询全校学生成绩排名
     * 
     * @param academicYear 学年（可选，为null时查询所有学年）
     * @param semester 学期（可选，为null时查询所有学期）
     * @param rankType 排名类型："total"(总分)、"average"(平均分)、"course"(单科成绩)
     * @param courseId 课程ID（当rankType为"course"时必填）
     * @param limit 限制返回条数（可选，为null时返回所有）
     * @return 成绩排名列表，包含学生信息、成绩信息和排名
     */
    List<Map<String, Object>> getSchoolGradeRanking(String academicYear, String semester, String rankType, String courseId, Integer limit);
    
    /**
     * 获取不及格学生名单
     * 查询指定条件下总分低于60分的学生信息
     * 
     * @param className 班级名称（可选，为null时查询所有班级）
     * @param academicYear 学年（可选，为null时查询所有学年）
     * @param semester 学期（可选，为null时查询所有学期）
     * @param courseId 课程ID（可选，为null时查询所有课程）
     * @return 不及格学生列表，包含学生信息、课程信息和成绩信息
     */
    List<Map<String, Object>> getFailingStudents(String className, String academicYear, String semester, String courseId);
    
    /**
     * 获取所有不同的班级名称
     * 
     * @return 班级名称列表
     */
    List<String> getAllClassNames();
    
    /**
     * 获取所有不同的学年
     * 
     * @return 学年列表
     */
    List<String> getAllAcademicYears();
    
/**
     * 获取所有课程信息
     * 从课程表中查询所有课程的ID和名称
     * 
     * @return 课程信息列表，每个元素包含课程ID和名称
     */
    List<Map<String, Object>> getAllCourses();
    
    /**
     * 获取优秀学生名单
     * 查询指定条件下总分>=85分的学生信息
     * 
     * @param className 班级名称（可选，为null时查询所有班级）
     * @param academicYear 学年（可选，为null时查询所有学年）
     * @param semester 学期（可选，为null时查询所有学期）
     * @param courseId 课程ID（可选，为null时查询所有课程）
     * @return 优秀学生列表，包含学生信息、课程信息和成绩信息
     */
    List<Map<String, Object>> getExcellentStudents(String className, String academicYear, String semester, String courseId);
    
    /**
     * 获取成绩趋势分析数据
     * 按学年学期统计平均分、及格率、优秀率等指标的变化趋势
     * 
     * @param analysisType 分析类型："class"(班级)、"course"(课程)、"student"(学生)
     * @param targetId 目标ID（班级名称、课程ID或学生ID）
     * @return 趋势分析数据列表，包含学年学期、平均分、及格率、优秀率等统计信息
     */
    List<Map<String, Object>> getGradeTrendAnalysis(String analysisType, String targetId);
}

