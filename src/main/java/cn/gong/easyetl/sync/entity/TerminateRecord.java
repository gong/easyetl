package cn.gong.easyetl.sync.entity;

/**
 * @author gongxin
 * @date 2020/9/15
 */
public class TerminateRecord extends Record{

    private final static TerminateRecord SINGLE = new TerminateRecord();

    private TerminateRecord() {
    }

    public static TerminateRecord get() {
        return SINGLE;
    }
}
