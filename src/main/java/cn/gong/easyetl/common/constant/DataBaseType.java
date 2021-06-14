package cn.gong.easyetl.common.constant;

/**
 * @author gongxin
 * @date 2020/9/16
 */
public enum DataBaseType {

    /**
     *
     * clickhouse使用反单引号表示字段
     * @author gongxin
     * @date 2020/9/16
     */
    CLICKHOUSE("`"),
    /**
     *
     * mysql使用反单引号表示字段
     * @author gongxin
     * @date 2020/9/16
     */
    MYSQL("`"),
    /**
     *
     * postgresql使用双引号表示字段
     * @author gongxin
     * @date 2020/9/16
     */
    POSTGRESQL("\"");

    private String symbol;

    DataBaseType(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

}
