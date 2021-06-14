package cn.gong.easyetl.sync.source;

/**
 *
 * @author gongxin
 * @date 2020/9/15
 */
public interface Source {

    /**
     *
     * 准备阶段，构造sql
     * @author gongxin
     * @date 2020/9/15
     */
    String prep();
    /**
     *
     * 读取外部的数据
     * @author gongxin
     * @date 2020/9/15
     */
    void read();

}
