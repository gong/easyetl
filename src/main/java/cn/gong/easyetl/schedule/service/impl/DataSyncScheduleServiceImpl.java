package cn.gong.easyetl.schedule.service.impl;

import cn.gong.easyetl.schedule.component.JobScheduleCreator;
import cn.gong.easyetl.schedule.job.DataSyncRunPeriodically;
import cn.gong.easyetl.schedule.service.SchedulerService;
import cn.gong.easyetl.websystem.po.SourceData;
import cn.gong.easyetl.websystem.repository.SourceDataMapper;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 *  同步任务添加和随应用启动调度任务
 * @author gongxin
 * @date 2020/9/17
 */

/**
 * @Service
 * @author gongxin
 * @date 2020/11/29
 */
@Slf4j
public class DataSyncScheduleServiceImpl implements SchedulerService<SourceData>, ApplicationRunner {


    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    private JobScheduleCreator scheduleCreator;

    @Autowired
    private SourceDataMapper sourceDataMapper;

    @Override
    public void startAllSchedulers() {
        List<SourceData> sourceDataList = sourceDataMapper.findAllSource();
        sourceDataList.forEach(this::scheduleNewJob);
    }

    @Override
    public void scheduleNewJob(SourceData sourceData) {
        JobKey jobKey = getJobKey(sourceData);
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobDetail jobDetail = JobBuilder.newJob(DataSyncRunPeriodically.class)
            .withIdentity(jobKey.getName(), jobKey.getGroup())
            .usingJobData("dataSyncId", sourceData.getId())
            .usingJobData("dataSyncName", sourceData.getTableName())
            .build();
        Trigger trigger = scheduleCreator
            .createCronTrigger(getTriggerName(sourceData), new Date(), sourceData.getCorn(), CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW);
        try {
            scheduler.scheduleJob(jobDetail, trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            log.error("数据同步调度任务启动失败！，原因[{}]", e.getLocalizedMessage());
        }
    }

    @Override
    public boolean deleteJob(SourceData sourceData) {
        return false;
    }

    @Override
    public boolean resumeJob(SourceData sourceData) {
        return false;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("数据同步任务启动......");
        startAllSchedulers();
        log.info("数据同步任务启动完成！");
    }

    private JobKey getJobKey(SourceData sourceData) {
        return new JobKey("dataSync" + sourceData.getId());
    }

    private String getTriggerName(SourceData sourceData) {
        return "dataSync" + sourceData.getId();
    }
}
