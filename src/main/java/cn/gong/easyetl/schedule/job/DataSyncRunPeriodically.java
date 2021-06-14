package cn.gong.easyetl.schedule.job;

import cn.gong.easyetl.pool.JdbcTemplatePool;
import cn.gong.easyetl.sync.core.DataSyncCore;
import cn.gong.easyetl.websystem.po.SourceData;
import cn.gong.easyetl.websystem.service.SourceDataService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * 调度数据同步任务执行
 * @author gongxin
 * @date 2020/9/17
 */
@Slf4j
@Component
@DisallowConcurrentExecution
public class DataSyncRunPeriodically extends QuartzJobBean {

    @Autowired
    DataSyncCore dataSyncCore;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            JobDataMap map = context.getMergedJobDataMap();
            Integer jobId = map.getInt("dataSyncId");
            String jobName = map.getString("dataSyncName");
            log.info("执行数据同步: {}-{}", jobId, jobName);
            dataSyncCore.run(jobId);
        } catch (Exception e) {
            log.error("", e);
            throw new JobExecutionException(e.getCause());
        }
    }
}
