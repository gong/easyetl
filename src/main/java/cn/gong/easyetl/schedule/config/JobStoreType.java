package cn.gong.easyetl.schedule.config;

/**
 * Define the supported Quartz {@code JobStore}.
 * @author gongxin
 * @date 2020/09/09
 */
public enum JobStoreType {

	/**
	 * Store jobs in memory.
	 */
	MEMORY,

	/**
	 * Store jobs in the database.
	 */
	JDBC

}
