package entity;

import java.util.Date;

public class Grade {
    private Integer id; // 成绩记录ID
    private String studentId; // 学生ID
    private String teachingTaskId; // 教学任务ID
    private Double regularScore; // 平时成绩
    private Double midtermScore; // 期中成绩
    private Double finalScore; // 期末成绩
    private Double totalScore; // 总成绩
    private Date entryTime; // 录入时间
    private String entryBy; // 录入人
    private Date lastModifiedTime; // 最后修改时间
    private String lastModifiedBy; // 最后修改人
    private String modificationReason; // 修改原因
    private String status; // 成绩状态


    public Grade() {
        // 默认构造函数
    }
    public Grade(Integer id, String studentId, String teachingTaskId, Double regularScore, Double midtermScore,
                 Double finalScore, Double totalScore, Date entryTime, String entryBy, Date lastModifiedTime,
                 String lastModifiedBy, String modificationReason, String status) {
        this.id = id;
        this.studentId = studentId;
        this.teachingTaskId = teachingTaskId;
        this.regularScore = regularScore;
        this.midtermScore = midtermScore;
        this.finalScore = finalScore;
        this.totalScore = totalScore;
        this.entryTime = entryTime;
        this.entryBy = entryBy;
        this.lastModifiedTime = lastModifiedTime;
        this.lastModifiedBy = lastModifiedBy;
        this.modificationReason = modificationReason;
        this.status = status;
    }
    // getter和setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getTeachingTaskId() {
        return teachingTaskId;
    }

    public void setTeachingTaskId(String teachingTaskId) {
        this.teachingTaskId = teachingTaskId;
    }

    public Double getRegularScore() {
        return regularScore;
    }

    public void setRegularScore(Double regularScore) {
        this.regularScore = regularScore;
    }

    public Double getMidtermScore() {
        return midtermScore;
    }

    public void setMidtermScore(Double midtermScore) {
        this.midtermScore = midtermScore;
    }

    public Double getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(Double finalScore) {
        this.finalScore = finalScore;
    }

    public Double getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Double totalScore) {
        this.totalScore = totalScore;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

    public String getEntryBy() {
        return entryBy;
    }

    public void setEntryBy(String entryBy) {
        this.entryBy = entryBy;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getModificationReason() {
        return modificationReason;
    }

    public void setModificationReason(String modificationReason) {
        this.modificationReason = modificationReason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "id=" + id +
                ", studentId='" + studentId + '\'' +
                ", teachingTaskId='" + teachingTaskId + '\'' +
                ", regularScore=" + regularScore +
                ", midtermScore=" + midtermScore +
                ", finalScore=" + finalScore +
                ", totalScore=" + totalScore +
                ", entryTime=" + entryTime +
                ", entryBy='" + entryBy + '\'' +
                ", lastModifiedTime=" + lastModifiedTime +
                ", lastModifiedBy='" + lastModifiedBy + '\'' +
                ", modificationReason='" + modificationReason + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}