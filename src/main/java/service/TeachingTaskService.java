package service;

import entity.TeachingTask;
import java.util.List;

public interface TeachingTaskService {
    int addTeachingTask(TeachingTask task);
    int updateTeachingTask(TeachingTask task);
    TeachingTask getTeachingTaskById(String id);
    List<TeachingTask> queryTeachingTasks(String courseId, String teacherId, String academicYear, String semester, String className);
    List<TeachingTask> queryTeachingTasksByPage(int offset, int limit);
    int deleteTeachingTask(String id);
}

