package cn.gong.easyetl.websystem.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gongxin
 * @date 2020/9/23
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransDict {
    private Integer id;
    private String querySql;
    private String mapping;
    private String transformField;
    private Integer sourceDataId;
}
