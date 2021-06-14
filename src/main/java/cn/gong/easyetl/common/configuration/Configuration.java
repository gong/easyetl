package cn.gong.easyetl.common.configuration;


import cn.gong.easyetl.common.constant.DataBaseType;
import cn.gong.easyetl.websystem.dto.TransDictDto;
import cn.gong.easyetl.websystem.po.SourceData;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * @author gongxin
 * @date 2020/9/15
 */
@Data
public class Configuration {

    private Integer batchSize;
    private Integer capacity;
    private Map<String, String> mapping;
    private DataBaseType dataBaseType;
    private List<TransDictDto> transDictDtoList;
    private SourceData sourceData;
    private String syncValue;

    public Configuration(Integer batchSize, Integer capacity, DataBaseType dataBaseType, List<TransDictDto> transDictDtoList, SourceData sourceData) {
        this.batchSize = batchSize;
        this.capacity = capacity;
        this.dataBaseType = dataBaseType;
        this.transDictDtoList = transDictDtoList;
        this.sourceData = sourceData;
    }

    public Configuration() {
    }
}
