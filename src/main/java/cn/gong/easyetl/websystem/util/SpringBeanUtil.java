package cn.gong.easyetl.websystem.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *
 * @author gongxin
 * @date 2020/9/15
 */
@Component
public class SpringBeanUtil implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    private static final Logger logger = LoggerFactory.getLogger(SpringBeanUtil.class);

    /**
     *
     * @author gongxin
     * @date 2020/9/15
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(SpringBeanUtil.applicationContext == null) {
            SpringBeanUtil.applicationContext = applicationContext;
        }
        logger.info(
            "========ApplicationContext配置成功,在普通类可以通过调用SpringBeanUtil.getApplicationContext()获取applicationContext对象,applicationContext===>{}",
            SpringBeanUtil.getApplicationContext());

    }

    /**
     *
     * @author gongxin
     * @date 2020/9/15
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     *
     * 根据名称获取对象
     * @author gongxin
     * @date 2020/9/15
     */
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    /**
     *
     * 根据class对象获取对象
     * @author gongxin
     * @date 2020/9/15
     */
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    /**
     *
     * 根据名称和class对象获取对象
     * @author gongxin
     * @date 2020/9/15
     */
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }
}
