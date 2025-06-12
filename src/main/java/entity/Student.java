package entity;

import java.util.Date;

public class Student {
    private String id; // 学生ID
    private String name; // 学生姓名
    private String gender; // 性别
    private Date birthDate; // 出生日期
    private Integer enrollmentYear; // 入学年份
    private String className; // 班级名称
    private String major; // 专业
    private String college; // 学院
    private String contactInfo; // 联系方式
    private String status; // 学生状态
    private String password; // 登录密码

    public Student() {
        // 默认构造函数
    }
    public Student(String id, String name, String gender, Date birthDate, Integer enrollmentYear, String className, String major, String college, String contactInfo, String status, String password) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.enrollmentYear = enrollmentYear;
        this.className = className;
        this.major = major;
        this.college = college;
        this.contactInfo = contactInfo;
        this.status = status;
        this.password = password;
    }

    // getter和setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getEnrollmentYear() {
        return enrollmentYear;
    }

    public void setEnrollmentYear(Integer enrollmentYear) {
        this.enrollmentYear = enrollmentYear;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", birthDate=" + birthDate +
                ", enrollmentYear=" + enrollmentYear +
                ", className='" + className + '\'' +
                ", major='" + major + '\'' +
                ", college='" + college + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                ", status='" + status + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}