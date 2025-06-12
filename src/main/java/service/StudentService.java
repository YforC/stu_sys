package service;

import entity.Student;
import java.util.List;

public interface StudentService {
    int addStudent(Student student);
    int updateStudent(Student student);
    Student getStudentById(String id);
    List<Student> queryStudents(String name, String className, String major, String college, String status);
    List<Student> queryStudentsByPage(int offset, int limit);
    int deleteStudent(String id);
}

