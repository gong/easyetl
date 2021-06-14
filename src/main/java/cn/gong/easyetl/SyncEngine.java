package cn.gong.easyetl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

/**
 * @author gongxin
 * @date 2021/6/13
 */
@Slf4j
@MapperScan("cn.gong.easyetl.websystem.repository")
@SpringBootApplication
public class SyncEngine {

    public static void main(String[] args) throws UnknownHostException {
        ConfigurableApplicationContext application = SpringApplication.run(SyncEngine.class);
        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");
        log.info("\n----------------------------------------------------------\n\t" +
            "Application easy etl is running! Access URLs:\n\t" +
            "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
            "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
            "Swagger-ui: \thttp://" + ip + ":" + port + path + "/swagger-ui.html\n\t" +
            "Doc文档: \thttp://" + ip + ":" + port + path + "/doc.html\n" +
            "----------------------------------------------------------");
    }

}
