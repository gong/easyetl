package cn.gong.easyetl.websystem.dto;

import java.util.Map;
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
public class TransDictDto {
    private Map<String, String> dict;
    private String transformField;
}
