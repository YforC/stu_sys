package entity;

public class TeachingTask {
    private String id; // 教学任务ID
    private String courseId; // 课程ID
    private String teacherId; // 教师ID
    private String academicYear; // 学年
    private String semester; // 学期
    private String className; // 班级名称
    private String classTime; // 上课时间
    private String location; // 上课地点
    private String status; // 任务状态


    public TeachingTask() {
        // 默认构造函数
    }
    public TeachingTask(String id, String courseId, String teacherId, String academicYear, String semester,
                        String className, String classTime, String location, String status) {
        this.id = id;
        this.courseId = courseId;
        this.teacherId = teacherId;
        this.academicYear = academicYear;
        this.semester = semester;
        this.className = className;
        this.classTime = classTime;
        this.location = location;
        this.status = status;
    }
    // getter和setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
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

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TeachingTask{" +
                "id='" + id + '\'' +
                ", courseId='" + courseId + '\'' +
                ", teacherId='" + teacherId + '\'' +
                ", academicYear='" + academicYear + '\'' +
                ", semester='" + semester + '\'' +
                ", className='" + className + '\'' +
                ", classTime='" + classTime + '\'' +
                ", location='" + location + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}