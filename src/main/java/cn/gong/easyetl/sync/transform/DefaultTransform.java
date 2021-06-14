package cn.gong.easyetl.sync.transform;

import cn.gong.easyetl.common.configuration.Configuration;
import cn.gong.easyetl.sync.entity.Column;
import cn.gong.easyetl.sync.entity.Record;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * 未来可以根据字段类型做具体的转换
 * @author gongxin
 * @date 2020/9/15
 */
@Slf4j
public class DefaultTransform {

    public final static String OBJECT_SOURCE = "object_source";
    public final static String CONTROL_STATE = "control_state";

    /**
     *
     * 简单的转换
     * @author gongxin
     * @date 2020/9/15
     */
    public static Record transform(ResultSet resultSet, Configuration configuration) {
        Record record = new Record();
        List<Column> columnList = new ArrayList<>();
        List<String> sinkFields = new ArrayList<>(configuration.getMapping().keySet());
        Collections.sort(sinkFields);
        Map<String, String> mapping = configuration.getMapping();
        try {
            for (String sinkField : sinkFields) {
                Object result = resultSet.getObject(mapping.get(sinkField));
                String data = result == null ? "" : result.toString();
                data = regConvert(data);
                Column column = new Column(sinkField, data);
                columnList.add(column);
            }
            if (StringUtils.isNotEmpty(configuration.getSourceData().getSyncField())) {
                Object syncValueSource = resultSet.getObject(configuration.getSourceData().getSyncField());
                String syncValue;
                if (configuration.getSyncValue() == null) {
                    syncValue = syncValueSource.toString();
                } else {
                    if (syncValueSource instanceof Timestamp) {
                        long nowTime = ((Timestamp) syncValueSource).getTime();
                        long oldTime = Timestamp.valueOf(configuration.getSyncValue()).getTime();
                        syncValue = nowTime > oldTime ? syncValueSource.toString() : configuration.getSyncValue();
                    } else {
                        syncValue = syncValueSource.toString();
                    }
                }
                configuration.setSyncValue(syncValue);
            }
            record.setColumnList(columnList);
            return record;
        } catch (SQLException e) {
            log.error("transform时sql异常[{}]", e.getCause());
        }
        return null;
    }

    /**
     *
     * 简单的正则替换
     * @author gongxin
     * @date 2020/10/16
     */
    private static String regConvert(String data) {
        return data.replace("\\", "").replace("'", "");
    }

}
