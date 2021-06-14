package cn.gong.easyetl.websystem.repository;

import cn.gong.easyetl.websystem.po.DataSyncRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author gongxin
 * @date 2020/9/15
 */
@Repository
public interface DataSyncRecordMapper {

    /**
     *
     * 根据表名找到同步记录
     * @author gongxin
     * @date 2020/9/15
     */
    DataSyncRecord find(@Param("tableName") String tableName);

    /**
     *
     * 更新同步记录
     * @author gongxin
     * @date 2020/9/15
     */
    void updateSync(DataSyncRecord dataSyncRecord);

    /**
     *
     * 插入同步记录
     * @author gongxin
     * @date 2020/9/15
     */
    void insert(DataSyncRecord dataSyncRecord);
}
