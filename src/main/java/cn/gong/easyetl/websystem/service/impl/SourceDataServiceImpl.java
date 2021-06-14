package cn.gong.easyetl.websystem.service.impl;

import cn.gong.easyetl.websystem.po.SourceData;
import cn.gong.easyetl.websystem.repository.SourceDataMapper;
import cn.gong.easyetl.websystem.service.SourceDataService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author gongxin
 * @date 2021/6/14
 */
@Service
public class SourceDataServiceImpl extends ServiceImpl<SourceDataMapper, SourceData> implements
    SourceDataService {

}
