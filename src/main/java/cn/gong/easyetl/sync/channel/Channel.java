package cn.gong.easyetl.sync.channel;

import java.util.Collection;

/**
 *
 * 中间缓冲
 * @author gongxin
 * @date 2020/9/15
 */
public interface Channel<T> {

    /**
     * push单条记录
     * @author gongxin
     * @date 2020/9/15
     */
    void push(T t);

    /**
     *
     * push多条记录
     * @author gongxin
     * @date 2020/9/15
     */
    void pushAll(Collection<T> records);

    /**
     *
     * pull单条记录
     * @author gongxin
     * @date 2020/9/15
     */
    T pull();

    /**
     *
     * pull多条记录
     * @author gongxin
     * @date 2020/9/15
     */
    void pullAll(Collection<T> records);
}
