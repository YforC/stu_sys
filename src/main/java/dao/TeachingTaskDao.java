package dao;

import entity.TeachingTask;
import java.util.List;

public interface TeachingTaskDao {
    // 添加教学任务
    int addTeachingTask(TeachingTask task);
    // 修改教学任务
    int updateTeachingTask(TeachingTask task);
    // 查询教学任务（可按条件）
    TeachingTask getTeachingTaskById(String id);
    List<TeachingTask> queryTeachingTasks(String courseId, String teacherId, String academicYear, String semester, String className);
    // 分页查询
    List<TeachingTask> queryTeachingTasksByPage(int offset, int limit);
    // 逻辑删除教学任务
    int deleteTeachingTask(String id);
}

