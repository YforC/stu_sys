package dao;

import entity.Grade;
import java.util.List;

public interface GradeDao {
    // 添加成绩
    int addGrade(Grade grade);
    // 修改成绩（含历史记录）
    int updateGrade(Grade grade, String modifiedBy, String reason);
    // 查询成绩（支持按学生、课程、学期等条件）
    Grade getGradeById(Integer id);
    List<Grade> queryGrades(String studentId, String teachingTaskId, String academicYear, String semester);
    // 分页查询
    List<Grade> queryGradesByPage(int offset, int limit);
    // 逻辑删除成绩
    int deleteGrade(Integer id);
    // 查询成绩修改历史
    List<Grade> getGradeHistory(Integer id);
}

