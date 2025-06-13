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
}

