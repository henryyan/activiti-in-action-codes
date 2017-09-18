package cn.niceabc.activiti.spring;

import org.activiti.engine.RuntimeService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SpringBootTest.MyApp.class)
public class SpringBootTest {

    @Autowired
    private RuntimeService runtimeService;

    @Test
    public void test() {
        Assert.assertNotNull(runtimeService);
    }

    @Configuration
    @ComponentScan
    @EnableAutoConfiguration
    public static class MyApp {

    }
}
