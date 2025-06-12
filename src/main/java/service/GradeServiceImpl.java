package service;

import dao.GradeDao;
import dao.GradeDaoImpl;
import entity.Grade;
import java.util.List;

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
}

