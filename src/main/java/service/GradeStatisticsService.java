package service;

import entity.GradeStatistics;
import java.util.List;

public interface GradeStatisticsService {
    int addGradeStatistics(GradeStatistics statistics);
    GradeStatistics getStatisticsById(Integer id);
    List<GradeStatistics> queryStatistics(String objectId, String type, String academicYear, String semester);
    List<GradeStatistics> queryStatisticsByPage(int offset, int limit);
}

