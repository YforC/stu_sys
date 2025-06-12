package service;

import dao.GradeStatisticsDao;
import dao.GradeStatisticsDaoImpl;
import entity.GradeStatistics;
import java.util.List;

public class GradeStatisticsServiceImpl implements GradeStatisticsService {
    private final GradeStatisticsDao statisticsDao = new GradeStatisticsDaoImpl();

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
}

