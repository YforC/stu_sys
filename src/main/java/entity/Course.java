package entity;

public class Course {
    private String id; // 课程ID
    private String name; // 课程名称
    private Double credits; // 学分
    private String type; // 课程类型
    private String semester; // 开课学期
    private Integer hours; // 学时
    private String description; // 课程描述

    public Course() {
        // 默认构造函数
    }

    public Course(String id, String name, Double credits, String type, String semester, Integer hours, String description) {
        this.id = id;
        this.name = name;
        this.credits = credits;
        this.type = type;
        this.semester = semester;
        this.hours = hours;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getCredits() {
        return credits;
    }

    public void setCredits(Double credits) {
        this.credits = credits;
    }

    public Integer getHours() {
        return hours;
    }

    public void setHours(Integer hours) {
        this.hours = hours;
    }


    @Override
    public String toString() {
        return "Course{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", credits=" + credits +
                ", type='" + type + '\'' +
                ", semester='" + semester + '\'' +
                ", hours=" + hours +
                ", description='" + description + '\'' +
                '}';
    }
}