package cn.gong.easyetl.websystem.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gongxin
 * @date 2020/9/15
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DataSyncRecord {

    private Integer id;
    private String tableName;
    private String syncField;
    private String syncValue;
}
