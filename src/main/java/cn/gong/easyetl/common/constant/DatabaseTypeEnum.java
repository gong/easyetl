package cn.gong.easyetl.common.constant;


/**
 * @author gongxin
 * @date 2021/5/28
 */
public enum DatabaseTypeEnum {

    /**
     *
     * @author gongxin
     * @date 2021/5/28
     */
    MYSQL("mysql", "com.mysql.cj.jdbc.Driver"),
    /**
     *
     * @author gongxin
     * @date 2021/5/28
     */
    POSTGRESQL("postgresql", "org.postgresql.Driver"),
    /**
     *
     * @author gongxin
     * @date 2021/5/28
     */
    HIVE("hive",  "org.apache.hive.jdbc.HiveDriver"),

    /**
     *
     * @author gongxin
     * @date 2021/6/1
     */
    ORACLE("oracle", "oracle.jdbc.OracleDriver");

    DatabaseTypeEnum(String name, String driverClassName) {
        this.name = name;
        this.driverClassName = driverClassName;
    }

    private String name;
    private String driverClassName;

    /**
     *
     * 获取驱动类名
     * @author gongxin
     * @date 2021/5/28
     */
    public static String getDriverClassNameByName(String name) {
        if (MYSQL.name.equalsIgnoreCase(name)) {
            return MYSQL.driverClassName;
        }
        if (POSTGRESQL.name.equalsIgnoreCase(name)) {
            return POSTGRESQL.driverClassName;
        }
        if (HIVE.name.equalsIgnoreCase(name)) {
            return HIVE.driverClassName;
        }
        if (ORACLE.name.equalsIgnoreCase(name)) {
            return ORACLE.driverClassName;
        }
        throw new RuntimeException("不支持的数据库类型");
    }

    public String getName() {
        return name;
    }
}
