package cn.gong.easyetl.sync.entity;

/**
 * @author gongxin
 * @date 2020/9/15
 */
public class Column {

    public String name;
    public Object value;

    public Column(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public Column() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
