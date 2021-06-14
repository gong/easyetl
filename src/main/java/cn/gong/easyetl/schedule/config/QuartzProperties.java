package cn.gong.easyetl.schedule.config;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for the Quartz Scheduler integration.
 * @author gongxin
 * @date 2020/09/09
 */
@Component
@ConfigurationProperties("spring.quartz")
public class QuartzProperties {

	/**
	 * Quartz job store type.
	 */
	private JobStoreType jobStoreType = JobStoreType.MEMORY;

	/**
	 * Name of the scheduler.
	 */
	private String schedulerName;

	/**
	 * Whether to automatically start the scheduler after initialization.
	 */
	private boolean autoStartup = true;

	/**
	 * Delay after which the scheduler is started once initialization completes. Setting
	 * this property makes sense if no jobs should be run before the entire application
	 * has started up.
	 */
	private Duration startupDelay = Duration.ofSeconds(0);

	/**
	 * Whether to wait for running jobs to complete on shutdown.
	 */
	private boolean waitForJobsToCompleteOnShutdown = false;

	/**
	 * Whether configured jobs should overwrite existing job definitions.
	 */
	private boolean overwriteExistingJobs = false;

	/**
	 * Additional Quartz Scheduler properties.
	 */
	private final Map<String, String> properties = new HashMap<>();

	private final Jdbc jdbc = new Jdbc();

	public JobStoreType getJobStoreType() {
		return this.jobStoreType;
	}

	public void setJobStoreType(JobStoreType jobStoreType) {
		this.jobStoreType = jobStoreType;
	}

	public String getSchedulerName() {
		return this.schedulerName;
	}

	public void setSchedulerName(String schedulerName) {
		this.schedulerName = schedulerName;
	}

	public boolean isAutoStartup() {
		return this.autoStartup;
	}

	public void setAutoStartup(boolean autoStartup) {
		this.autoStartup = autoStartup;
	}

	public Duration getStartupDelay() {
		return this.startupDelay;
	}

	public void setStartupDelay(Duration startupDelay) {
		this.startupDelay = startupDelay;
	}

	public boolean isWaitForJobsToCompleteOnShutdown() {
		return this.waitForJobsToCompleteOnShutdown;
	}

	public void setWaitForJobsToCompleteOnShutdown(
			boolean waitForJobsToCompleteOnShutdown) {
		this.waitForJobsToCompleteOnShutdown = waitForJobsToCompleteOnShutdown;
	}

	public boolean isOverwriteExistingJobs() {
		return this.overwriteExistingJobs;
	}

	public void setOverwriteExistingJobs(boolean overwriteExistingJobs) {
		this.overwriteExistingJobs = overwriteExistingJobs;
	}

	public Map<String, String> getProperties() {
		return this.properties;
	}

	public Jdbc getJdbc() {
		return this.jdbc;
	}

	public static class Jdbc {

		private static final String DEFAULT_SCHEMA_LOCATION = "classpath:org/quartz/impl/"
				+ "jdbcjobstore/tables_@@platform@@.sql";

		/**
		 * Path to the SQL file to use to initialize the database schema.
		 */
		private String schema = DEFAULT_SCHEMA_LOCATION;

		/**
		 * Database schema initialization mode.
		 */
		private DataSourceInitializationMode initializeSchema = DataSourceInitializationMode.EMBEDDED;

		/**
		 * Prefix for single-line comments in SQL initialization scripts.
		 */
		private String commentPrefix = "--";

		public String getSchema() {
			return this.schema;
		}

		public void setSchema(String schema) {
			this.schema = schema;
		}

		public DataSourceInitializationMode getInitializeSchema() {
			return this.initializeSchema;
		}

		public void setInitializeSchema(DataSourceInitializationMode initializeSchema) {
			this.initializeSchema = initializeSchema;
		}

		public String getCommentPrefix() {
			return this.commentPrefix;
		}

		public void setCommentPrefix(String commentPrefix) {
			this.commentPrefix = commentPrefix;
		}

	}

}
