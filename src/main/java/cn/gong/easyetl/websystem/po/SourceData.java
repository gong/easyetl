package cn.gong.easyetl.websystem.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gongxin
 * @date 2020/9/17
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SourceData {

    private Integer id;
    private String tableName;
    private String queryField;
    private String fieldMapping;
    private String syncField;
    private String corn;
    private Integer isDelete;
    private String objectSource;
    private String stateField;
    private String sinkTableName;
    private String uniqueField;
    private Integer sourceDatabaseId;
    private Integer targetDatabaseId;
}
