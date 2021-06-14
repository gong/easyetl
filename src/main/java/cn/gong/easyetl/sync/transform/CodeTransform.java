package cn.gong.easyetl.sync.transform;

import cn.gong.easyetl.common.configuration.Configuration;
import cn.gong.easyetl.sync.entity.Column;
import cn.gong.easyetl.sync.entity.Record;
import cn.gong.easyetl.websystem.dto.TransDictDto;
import java.sql.ResultSet;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @author gongxin
 * @date 2020/9/23
 */
@Slf4j
public class CodeTransform extends DefaultTransform{

    public static Record codeTransform(ResultSet resultSet, Configuration configuration) {
        Record record = transform(resultSet, configuration);
        List<TransDictDto> transDictDtoList = configuration.getTransDictDtoList();
        if (transDictDtoList == null || transDictDtoList.isEmpty()) {
            log.debug("code transform规则是空的！");
            return record;
        }
        for (TransDictDto transDictDto : transDictDtoList) {
            for (Column column : record.getColumnList()) {
                if (transDictDto.getTransformField().equalsIgnoreCase(column.getName())) {
                    column.setValue(transDictDto.getDict().get(column.getValue().toString()));
                }
            }
        }
        return record;
    }
}
