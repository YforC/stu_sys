package service;

import entity.GradeStatistics;
import java.util.List;

public interface GradeStatisticsService {
    int addGradeStatistics(GradeStatistics statistics);
    GradeStatistics getStatisticsById(Integer id);
    List<GradeStatistics> queryStatistics(String objectId, String type, String academicYear, String semester);
    List<GradeStatistics> queryStatisticsByPage(int offset, int limit);
    
    /**
     * 生成全部成绩统计数据
     * 为所有班级和课程生成统计数据并保存到grade_statistics表
     * 
     * @return 生成的统计记录数量
     */
    int generateAllStatistics();
    
    /**
     * 生成指定条件的成绩统计数据
     * 
     * @param type 统计类型："class"(班级)、"course"(课程)
     * @param academicYear 学年（可选）
     * @param semester 学期（可选）
     * @return 生成的统计记录数量
     */
    int generateSelectedStatistics(String type, String academicYear, String semester);
}

