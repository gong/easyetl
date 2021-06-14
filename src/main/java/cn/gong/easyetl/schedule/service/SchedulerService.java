package cn.gong.easyetl.schedule.service;

/**
 * 调度任务管理接口，如果有新的不同功能的调度任务，可以实现此接口
 * @author gongxin
 * @date 2020/9/17
 */
public interface SchedulerService<T> {

    void startAllSchedulers();

    void scheduleNewJob(T t);

    boolean deleteJob(T t);

    boolean resumeJob(T t);

}
