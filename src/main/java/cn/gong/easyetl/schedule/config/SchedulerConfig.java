package cn.gong.easyetl.schedule.config;

import cn.gong.easyetl.schedule.component.SchedulerJobFactory;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 *
 * @author gongxin
 * @date 2020/9/9
 */
@Configuration
public class SchedulerConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private QuartzProperties quartzProperties;


    /**
     * create scheduler factory
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerJobFactory jobFactory = new SchedulerJobFactory();
        jobFactory.setApplicationContext(applicationContext);

        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setJobFactory(jobFactory);

        /**
         *
         * 不需要数据库保存工作调度情况，每次重启应用，已经实现启动调度任务
         * @author gongxin
         * @date 2020/9/17
         */
        if (quartzProperties.getJobStoreType().equals(JobStoreType.JDBC)) {
            Properties properties = new Properties();
            properties.putAll(quartzProperties.getProperties());
            factory.setQuartzProperties(properties);

            factory.setDataSource(dataSource);
            factory.setOverwriteExistingJobs(true);
        }

        return factory;
    }

}
