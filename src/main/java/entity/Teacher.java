package entity;

import java.util.Date;

public class Teacher {
    private String id; // 教师ID
    private String name; // 教师姓名
    private String gender; // 性别
    private Date birthDate; // 出生日期
    private String title; // 职称
    private String college; // 学院
    private String major; // 专业
    private String contactInfo; // 联系方式
    private String status; // 教师状态
    private String password; // 登录密码

    public Teacher() {
        // 默认构造函数
    }
    public Teacher(String id, String name, String gender, Date birthDate, String title, String college, String major, String contactInfo, String status, String password) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.title = title;
        this.college = college;
        this.major = major;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
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
        return "Teacher{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", birthDate=" + birthDate +
                ", title='" + title + '\'' +
                ", college='" + college + '\'' +
                ", major='" + major + '\'' +
                ", contactInfo='" + contactInfo + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}