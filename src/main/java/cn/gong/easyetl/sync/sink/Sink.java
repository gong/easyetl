package cn.gong.easyetl.sync.sink;


import cn.gong.easyetl.common.configuration.Configuration;
import cn.gong.easyetl.sync.entity.Record;
import java.util.List;

/**
 * @author gongxin
 * @date 2020/9/15
 */
public interface Sink {

    /**
     * 转换的结果写入相应的库
     * @author gongxin
     * @date 2020/9/15
     */
    void write();

    /**
     * 构造sql
     * @param records
     * @param configuration
     * @author gongxin
     * @date 2020/9/15
     */
    String constructSql(List<Record> records, Configuration configuration);
}
