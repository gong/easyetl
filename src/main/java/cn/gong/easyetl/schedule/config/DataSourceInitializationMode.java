package cn.gong.easyetl.schedule.config;

/**
 * Supported {@link javax.sql.DataSource} initialization modes.
 * @author gongxin
 * @date 2020/09/09
 */
public enum DataSourceInitializationMode {

	/**
	 * Always initialize the datasource.
	 */
	ALWAYS,

	/**
	 * Only initialize an embedded datasource.
	 */
	EMBEDDED,

	/**
	 * Do not initialize the datasource.
	 */
	NEVER

}
