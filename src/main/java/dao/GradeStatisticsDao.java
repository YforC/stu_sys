package dao;

import entity.GradeStatistics;
import java.util.List;

public interface GradeStatisticsDao {
    // 添加统计记录
    int addGradeStatistics(GradeStatistics statistics);
    // 查询统计记录（支持按课程、班级、学期等条件）
    GradeStatistics getStatisticsById(Integer id);
    List<GradeStatistics> queryStatistics(String objectId, String type, String academicYear, String semester);
    // 分页查询
    List<GradeStatistics> queryStatisticsByPage(int offset, int limit);
}

