package cn.gong.easyetl.sync.core;

import cn.gong.easyetl.common.configuration.Configuration;
import cn.gong.easyetl.common.constant.DataBaseType;
import cn.gong.easyetl.pool.JdbcTemplatePool;
import cn.gong.easyetl.sync.channel.MemoryChannel;
import cn.gong.easyetl.sync.sink.DefaultSink;
import cn.gong.easyetl.sync.source.DefaultSource;
import cn.gong.easyetl.websystem.po.SourceData;
import cn.gong.easyetl.websystem.dto.TransDictDto;
import cn.gong.easyetl.websystem.po.TransDict;
import cn.gong.easyetl.websystem.repository.TransDictMapper;
import cn.gong.easyetl.websystem.service.SourceDataService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * 数据同步任务核心入口，包含source sink两个模块，
 * 使用数组阻塞队列作为缓冲空间
 * @author gongxin
 * @date 2020/9/17
 */

@Component
@Data
public class DataSyncCore {

    @Autowired
    private SourceDataService sourceDataService;

    @Autowired
    private TransDictMapper transDictMapper;

    @Value("${datasync.channel.batchSize}")
    private Integer batchSize;

    @Value("${datasync.channel.capacity}")
    private Integer capacity;

    @Autowired
    private JdbcTemplatePool jdbcTemplatePool;

    /**
     *
     * 增量同步数据
     * @author gongxin
     * @date 2020/9/17
     */
    public void run(Integer dataSyncId) {
        SourceData sourceData = sourceDataService.getById(dataSyncId);
        JdbcTemplate sourceJdbcTemplate = jdbcTemplatePool.getJdbcTemplate(sourceData.getSourceDatabaseId());
        JdbcTemplate targetJdbcTemplate = jdbcTemplatePool.getJdbcTemplate(sourceData.getTargetDatabaseId());
        Configuration configuration = init(dataSyncId, sourceJdbcTemplate, sourceData);
        MemoryChannel memoryChannel = new MemoryChannel(configuration);
        Thread reader = new Thread(new Runnable() {
            @Override
            public void run() {
                DefaultSource defaultSource = new DefaultSource(sourceJdbcTemplate, memoryChannel, configuration);
                defaultSource.read();
            }
        });
        reader.start();
        Thread writer = new Thread(new Runnable() {
            @Override
            public void run() {
                DefaultSink defaultSink = new DefaultSink(targetJdbcTemplate, memoryChannel, configuration);
                defaultSink.write();
            }
        });
        writer.start();
    }

    /**
     *
     * 配置初始化
     * @author gongxin
     * @date 2020/9/23
     */
    private Configuration init(Integer dataSyncId, JdbcTemplate sourceJdbcTemplate, SourceData sourceData) {
        List<TransDict> transDictList = transDictMapper.findAllTransDict(dataSyncId);
        List<TransDictDto> transDictDtoList = new ArrayList<>();
        for (TransDict transDict : transDictList) {
            List<Map<String, Object>> data = sourceJdbcTemplate.queryForList(transDict.getQuerySql());
            String[] mapping = transDict.getMapping().split("&");
            if (!data.isEmpty()) {
                Map<String, String> dict;
                dict = data.stream().collect(Collectors.toMap(key -> key.get(mapping[0].trim()).toString(), value -> value.get(mapping[1].trim()).toString()));
                TransDictDto transDictDto = new TransDictDto(dict, transDict.getTransformField());
                transDictDtoList.add(transDictDto);
            }
        }
        return new Configuration(batchSize, capacity, DataBaseType.POSTGRESQL, transDictDtoList, sourceData);
    }
}
