package cn.gong.easyetl.sync.source;

import cn.gong.easyetl.sync.channel.MemoryChannel;
import cn.gong.easyetl.common.configuration.Configuration;
import cn.gong.easyetl.sync.entity.Record;
import cn.gong.easyetl.sync.entity.TerminateRecord;
import cn.gong.easyetl.sync.transform.CodeTransform;
import cn.gong.easyetl.websystem.po.DataSyncRecord;
import cn.gong.easyetl.websystem.repository.DataSyncRecordMapper;
import cn.gong.easyetl.websystem.util.SpringBeanUtil;
import cn.gong.easyetl.websystem.util.SqlQueryBuilder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

/**
 * @author gongxin
 * @date 2020/9/15
 */
@Slf4j
public class DefaultSource implements Source{

    private JdbcTemplate jdbcTemplate;

    private MemoryChannel memoryChannel;

    private Configuration configuration;

    public DefaultSource(JdbcTemplate jdbcTemplate, MemoryChannel memoryChannel, Configuration configuration) {
        this.jdbcTemplate = jdbcTemplate;
        this.memoryChannel = memoryChannel;
        this.configuration = configuration;
    }

    @Override
    public String prep() {
        DataSyncRecordMapper dataSyncRecordMapper = SpringBeanUtil.getBean(DataSyncRecordMapper.class);
        DataSyncRecord dataSyncRecord = dataSyncRecordMapper.find(configuration.getSourceData().getTableName());
        String fieldMapping = configuration.getSourceData().getFieldMapping();
        List<String> fieldMappingStrList = Arrays.asList(fieldMapping.split(","));
        Map<String, String> mapping = fieldMappingStrList.stream().map(v -> v.split("&")).collect(Collectors.toMap(key -> key[0].trim(), value -> value[1].trim()));
        configuration.setMapping(mapping);
        List<String> queryFieldList = Arrays.asList(configuration.getSourceData().getQueryField().split("&"));
        Set<String> fields = new HashSet<>(queryFieldList);
        if (StringUtils.isNotEmpty(configuration.getSourceData().getSyncField())) {
            fields.add(configuration.getSourceData().getSyncField());
        }
        SqlQueryBuilder sqlQueryBuilder = new SqlQueryBuilder(configuration.getDataBaseType(), configuration.getSourceData().getTableName(), fields);
        if (StringUtils.isNotEmpty(configuration.getSourceData().getSyncField())) {
            SqlQueryBuilder.ConditionBuilder conditionBuilder = sqlQueryBuilder.newConditionBuilder();
            conditionBuilder.gt(configuration.getSourceData().getSyncField(), dataSyncRecord.getSyncValue());
        }
        if (dataSyncRecord == null) {
            dataSyncRecord = new DataSyncRecord();
            dataSyncRecord.setTableName(configuration.getSourceData().getTableName());
            dataSyncRecord.setSyncField(configuration.getSourceData().getSyncField());
            dataSyncRecordMapper.insert(dataSyncRecord);
        }
        return sqlQueryBuilder.get();
    }

    /**
     *
     * 流式读取
     * @author gongxin
     * @date 2020/9/15
     */
    @Override
    public void read() {
        log.info("开始读取数据......");
        List<Record> buffer = new ArrayList<>(configuration.getBatchSize());
        jdbcTemplate.setFetchSize(configuration.getBatchSize());

        String sql = prep();
        if (sql != null) {
            jdbcTemplate.query(sql, new RowCallbackHandler() {
                @Override
                public void processRow(ResultSet resultSet) throws SQLException {
                    Record record = CodeTransform.codeTransform(resultSet, configuration);
                    if (buffer.size() == configuration.getBatchSize()) {
                        memoryChannel.pushAll(new ArrayList<>(buffer));
                        buffer.clear();
                    } else if (record != null){
                        buffer.add(record);
                    }
                }
            });
        }
        if (!buffer.isEmpty()) {
            memoryChannel.pushAll(new ArrayList<>(buffer));
            buffer.clear();
        }
        memoryChannel.push(TerminateRecord.get());
    }


}
