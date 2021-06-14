package cn.gong.easyetl.sync.sink;

import cn.gong.easyetl.common.configuration.Configuration;
import cn.gong.easyetl.sync.channel.MemoryChannel;
import cn.gong.easyetl.sync.entity.Column;
import cn.gong.easyetl.sync.entity.Record;
import cn.gong.easyetl.sync.entity.TerminateRecord;
import cn.gong.easyetl.websystem.po.DataSyncRecord;
import cn.gong.easyetl.websystem.repository.DataSyncRecordMapper;
import cn.gong.easyetl.websystem.util.SpringBeanUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author gongxin
 * @date 2020/9/15
 */
@Slf4j
public class DefaultSink implements Sink {

    private JdbcTemplate jdbcTemplate;

    private MemoryChannel memoryChannel;

    private Integer bufferSize = 0;

    private Integer bufferIndex = 0;

    private Configuration configuration;

    private List<Record> recordList = new ArrayList<>();

    private DataSyncRecordMapper dataSyncRecordMapper = SpringBeanUtil.getBean(DataSyncRecordMapper.class);

    public DefaultSink(JdbcTemplate jdbcTemplate, MemoryChannel memoryChannel, Configuration configuration) {
        this.jdbcTemplate = jdbcTemplate;
        this.memoryChannel = memoryChannel;
        this.configuration = configuration;
    }

    @Override
    public void write() {
        log.info("开始写入数据......");
        Record record;
        List<Record> records = new ArrayList<>(configuration.getBatchSize());
        String sql;
        while ((record = getFromReader()) != null) {
            if (records.size() == configuration.getBatchSize()) {
                sql = constructSql(new ArrayList<>(records), configuration);
                jdbcTemplate.execute(sql);
                records.clear();
            } else {
                records.add(record);
            }
        }
        if (!records.isEmpty()) {
            sql = constructSql(new ArrayList<>(records), configuration);
            if (sql != null) {
                jdbcTemplate.execute(sql);
            }
            records.clear();
        }
        if (StringUtils.isNotEmpty(configuration.getSyncValue())) {
            DataSyncRecord dataSyncRecord = new DataSyncRecord();
            dataSyncRecord.setTableName(configuration.getSourceData().getTableName());
            dataSyncRecord.setSyncValue(configuration.getSyncValue());
            dataSyncRecordMapper.updateSync(dataSyncRecord);
        }
    }

    private Record getFromReader() {
        if (bufferIndex >= bufferSize) {
            memoryChannel.pullAll(recordList);
            bufferSize = recordList.size();
            bufferIndex = 0;
        }
        Record record = recordList.get(bufferIndex++);
        if (record instanceof TerminateRecord) {
            record = null;
        }
        return record;
    }

    @Override
    public String constructSql(List<Record> records, Configuration configuration) {
        if (!records.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder("insert into ");
            stringBuilder.append(configuration.getSourceData().getSinkTableName()).append(" (");
            List<String> columns = new ArrayList<>(configuration.getMapping().keySet());
            Collections.sort(columns);
            for (String field : columns) {
                stringBuilder.append(field).append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
            stringBuilder.append(") values");
            for (Record record : records) {
                stringBuilder.append(constructValue(record));
                stringBuilder.append(",");
            }
            stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
            if (StringUtils.isEmpty(configuration.getSourceData().getSyncField())) {
                stringBuilder.append(" ON DUPLICATE KEY UPDATE ").append(configuration.getSourceData().getUniqueField())
                    .append(" = ").append("values(").append(configuration.getSourceData().getUniqueField()).append(")");
            }
            return stringBuilder.toString();
        }
        return null;
    }

    private String constructValue(Record record) {
        StringBuilder stringBuilder = new StringBuilder("(");
        for (Column column : record.getColumnList()) {
            stringBuilder.append("'").append(column.getValue()).append("'").append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

}
