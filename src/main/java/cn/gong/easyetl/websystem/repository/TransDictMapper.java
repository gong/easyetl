package cn.gong.easyetl.websystem.repository;

import cn.gong.easyetl.websystem.po.TransDict;
import java.util.List;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 *
 * @author gongxin
 * @date 2020/9/23
 */
@Repository
public interface TransDictMapper {

    @Select("select * from trans_dict where is_delete = 0 and source_data_id = #{param1}")
    List<TransDict> findAllTransDict(Integer sourceDataId);
}
