package cn.gong.easyetl.websystem.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 *
 * 外部对接的数据源连接信息
 * @author gongxin
 * @date 2021/5/28
 */
@Data
@TableName("external_database")
public class ExternalDatabase {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String dbTypeName;

    private String url;

    private String username;

    private String password;

    private String principal;

    private String keytab;

    private String krb5Conf;

    private Integer isView;

    private String name;

    private String displayName;

}
