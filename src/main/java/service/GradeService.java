package service;

import entity.Grade;
import java.util.List;

public interface GradeService {
    int addGrade(Grade grade);
    int updateGrade(Grade grade, String modifiedBy, String reason);
    Grade getGradeById(Integer id);
    List<Grade> queryGrades(String studentId, String teachingTaskId, String academicYear, String semester);
    List<Grade> queryGradesByPage(int offset, int limit);
    int deleteGrade(Integer id);
    List<Grade> getGradeHistory(Integer id);
}

