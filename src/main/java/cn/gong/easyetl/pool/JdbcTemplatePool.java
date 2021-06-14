package cn.gong.easyetl.pool;

import cn.gong.easyetl.common.constant.DatabaseTypeEnum;
import cn.gong.easyetl.websystem.po.ExternalDatabase;
import cn.gong.easyetl.websystem.service.ExternalDatabaseService;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * 连接管理对象
 * @author gongxin
 * @date 2021/5/28
 */
@Component
public class JdbcTemplatePool {

    private Map<Integer, JdbcTemplate> nameMapJdbcTemplate = new ConcurrentHashMap<>(16);

    @Autowired
    ExternalDatabaseService externalDatabaseService;

    public JdbcTemplate getJdbcTemplate(Integer externalDatabaseId) {
        JdbcTemplate jdbcTemplate = nameMapJdbcTemplate.get(externalDatabaseId);
        if (jdbcTemplate == null) {
            ExternalDatabase externalDatabase = externalDatabaseService.getById(externalDatabaseId);
            DruidDataSource druidDataSource = DruidDataSourceBuilder.create().build();
            druidDataSource.setUrl(externalDatabase.getUrl());
            druidDataSource.setUsername(externalDatabase.getUsername());
            druidDataSource.setPassword(externalDatabase.getPassword());
            druidDataSource.setDriverClassName(DatabaseTypeEnum.getDriverClassNameByName(externalDatabase.getDbTypeName()));
            jdbcTemplate = new JdbcTemplate(druidDataSource);
            nameMapJdbcTemplate.put(externalDatabaseId, jdbcTemplate);
        }
        return jdbcTemplate;
    }
}
