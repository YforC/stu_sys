package entity;

import java.util.Date;

public class GradeStatistics {
    private Integer id; // 统计记录ID
    private String objectId; // 统计对象ID（如课程ID、班级ID等）
    private String type; // 统计类型
    private String academicYear; // 学年
    private String semester; // 学期
    private Double averageScore; // 平均分
    private Double highestScore; // 最高分
    private Double lowestScore; // 最低分
    private Integer passCount; // 及格人数
    private Double passRate; // 及格率
    private Integer excellentCount; // 优秀人数
    private Double excellentRate; // 优秀率
    private Date statisticsTime; // 统计时间


    public GradeStatistics() {
        // 默认构造函数
    }
    public GradeStatistics(Integer id, String objectId, String type, String academicYear, String semester,
                           Double averageScore, Double highestScore, Double lowestScore,
                           Integer passCount, Double passRate, Integer excellentCount, Double excellentRate,
                           Date statisticsTime) {
        this.id = id;
        this.objectId = objectId;
        this.type = type;
        this.academicYear = academicYear;
        this.semester = semester;
        this.averageScore = averageScore;
        this.highestScore = highestScore;
        this.lowestScore = lowestScore;
        this.passCount = passCount;
        this.passRate = passRate;
        this.excellentCount = excellentCount;
        this.excellentRate = excellentRate;
        this.statisticsTime = statisticsTime;
    }
    // getter和setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public Double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Double averageScore) {
        this.averageScore = averageScore;
    }

    public Double getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(Double highestScore) {
        this.highestScore = highestScore;
    }

    public Double getLowestScore() {
        return lowestScore;
    }

    public void setLowestScore(Double lowestScore) {
        this.lowestScore = lowestScore;
    }

    public Integer getPassCount() {
        return passCount;
    }

    public void setPassCount(Integer passCount) {
        this.passCount = passCount;
    }

    public Double getPassRate() {
        return passRate;
    }

    public void setPassRate(Double passRate) {
        this.passRate = passRate;
    }

    public Integer getExcellentCount() {
        return excellentCount;
    }

    public void setExcellentCount(Integer excellentCount) {
        this.excellentCount = excellentCount;
    }

    public Double getExcellentRate() {
        return excellentRate;
    }

    public void setExcellentRate(Double excellentRate) {
        this.excellentRate = excellentRate;
    }

    public Date getStatisticsTime() {
        return statisticsTime;
    }

    public void setStatisticsTime(Date statisticsTime) {
        this.statisticsTime = statisticsTime;
    }

    @Override
    public String toString() {
        return "GradeStatistics{" +
                "id=" + id +
                ", objectId='" + objectId + '\'' +
                ", type='" + type + '\'' +
                ", academicYear='" + academicYear + '\'' +
                ", semester='" + semester + '\'' +
                ", averageScore=" + averageScore +
                ", highestScore=" + highestScore +
                ", lowestScore=" + lowestScore +
                ", passCount=" + passCount +
                ", passRate=" + passRate +
                ", excellentCount=" + excellentCount +
                ", excellentRate=" + excellentRate +
                ", statisticsTime=" + statisticsTime +
                '}';
    }
}