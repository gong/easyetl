package cn.gong.easyetl.websystem.service.impl;

import cn.gong.easyetl.websystem.po.ExternalDatabase;
import cn.gong.easyetl.websystem.repository.ExternalDatabaseMapper;
import cn.gong.easyetl.websystem.service.ExternalDatabaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author gongxin
 * @date 2021/6/14
 */
@Service
public class ExternalDatabaseServiceImpl extends ServiceImpl<ExternalDatabaseMapper, ExternalDatabase> implements
    ExternalDatabaseService {

}
