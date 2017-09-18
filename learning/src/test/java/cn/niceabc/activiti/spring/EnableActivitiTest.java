package cn.niceabc.activiti.spring;

import org.activiti.engine.RuntimeService;
import org.activiti.spring.annotations.EnableActiviti;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class EnableActivitiTest {

    @Autowired
    private RuntimeService runtimeService;

    @Test
    public void test() {
        Assert.assertNotNull(runtimeService);
    }

    @EnableActiviti
    @Configuration
    public static class simplestConf {

    }
}
