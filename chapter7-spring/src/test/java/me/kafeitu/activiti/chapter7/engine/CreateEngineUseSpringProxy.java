package me.kafeitu.activiti.chapter7.engine;

import org.activiti.engine.RuntimeService;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertNotNull;

/**
 * 测试用Spring方式创建引擎对象
 *
 * @author henryyan
 */
public class CreateEngineUseSpringProxy {

    @Test
    public void createEngineUseSpring() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-test.xml");
        ProcessEngineFactoryBean factoryBean = context.getBean(ProcessEngineFactoryBean.class);
        assertNotNull(factoryBean);

        RuntimeService bean = context.getBean(RuntimeService.class);
        assertNotNull(bean);
    }

}
