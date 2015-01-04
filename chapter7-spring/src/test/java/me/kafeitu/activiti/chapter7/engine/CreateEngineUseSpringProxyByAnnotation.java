package me.kafeitu.activiti.chapter7.engine;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

/**
 * 使用注解方式测试引擎
 *
 * @author henryyan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-test.xml")
public class CreateEngineUseSpringProxyByAnnotation {

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    ProcessEngineFactoryBean factoryBean;

    @Test
    public void testService() throws Exception {
        assertNotNull(runtimeService);

        ProcessEngine processEngine = factoryBean.getObject();
        assertNotNull(processEngine.getRuntimeService());
    }

}
