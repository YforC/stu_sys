package service;

import dao.TeachingTaskDao;
import dao.TeachingTaskDaoImpl;
import entity.TeachingTask;
import java.util.List;

public class TeachingTaskServiceImpl implements TeachingTaskService {
    private final TeachingTaskDao teachingTaskDao = new TeachingTaskDaoImpl();

    @Override
    public int addTeachingTask(TeachingTask task) {
        // 业务校验：任务编号、课程编号、教师编号、学年、学期、班级不能为空
        if (task == null || task.getId() == null || task.getId().isEmpty()
                || task.getCourseId() == null || task.getCourseId().isEmpty()
                || task.getTeacherId() == null || task.getTeacherId().isEmpty()
                || task.getAcademicYear() == null || task.getAcademicYear().isEmpty()
                || task.getSemester() == null || task.getSemester().isEmpty()
                || task.getClassName() == null || task.getClassName().isEmpty()) {
            throw new IllegalArgumentException("任务编号、课程编号、教师编号、学年、学期、班级不能为空");
        }
        return teachingTaskDao.addTeachingTask(task);
    }

    @Override
    public int updateTeachingTask(TeachingTask task) {
        // 业务校验同添加
        if (task == null || task.getId() == null || task.getId().isEmpty()
                || task.getCourseId() == null || task.getCourseId().isEmpty()
                || task.getTeacherId() == null || task.getTeacherId().isEmpty()
                || task.getAcademicYear() == null || task.getAcademicYear().isEmpty()
                || task.getSemester() == null || task.getSemester().isEmpty()
                || task.getClassName() == null || task.getClassName().isEmpty()) {
            throw new IllegalArgumentException("任务编号、课程编号、教师编号、学年、学期、班级不能为空");
        }
        return teachingTaskDao.updateTeachingTask(task);
    }

    @Override
    public TeachingTask getTeachingTaskById(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("任务编号不能为空");
        }
        return teachingTaskDao.getTeachingTaskById(id);
    }

    @Override
    public List<TeachingTask> queryTeachingTasks(String courseId, String teacherId, String academicYear, String semester, String className) {
        return teachingTaskDao.queryTeachingTasks(courseId, teacherId, academicYear, semester, className);
    }

    @Override
    public List<TeachingTask> queryTeachingTasksByPage(int offset, int limit) {
        if (offset < 0 || limit <= 0) {
            throw new IllegalArgumentException("分页参数不合法");
        }
        return teachingTaskDao.queryTeachingTasksByPage(offset, limit);
    }

    @Override
    public int deleteTeachingTask(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("任务编号不能为空");
        }
        return teachingTaskDao.deleteTeachingTask(id);
    }
}

