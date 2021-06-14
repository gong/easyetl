package cn.gong.easyetl.websystem.repository;

import cn.gong.easyetl.websystem.po.SourceData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 *
 * @author gongxin
 * @date 2020/9/17
 */
@Repository
public interface SourceDataMapper extends BaseMapper<SourceData> {

    @Select("select * from source_data where is_delete = 0")
    List<SourceData> findAllSource();

    @Select("select * from source_data where id = #{param1}")
    SourceData find(String dataSyncId);
}
