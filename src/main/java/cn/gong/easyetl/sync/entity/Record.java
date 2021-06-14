package cn.gong.easyetl.sync.entity;

import java.util.List;

/**
 * @author gongxin
 * @date 2020/9/15
 */
public class Record {

    public List<Column> columnList;

    public List<Column> getColumnList() {
        return columnList;
    }

    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }
}
