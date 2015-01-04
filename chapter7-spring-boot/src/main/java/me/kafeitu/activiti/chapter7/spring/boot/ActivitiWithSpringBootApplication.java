package me.kafeitu.activiti.chapter7.spring.boot;

import org.activiti.engine.RuntimeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

/**
 * Hello Activiti by Spring Boot
 */
public class ActivitiWithSpringBootApplication {

    @Configuration
    @ComponentScan
    @EnableAutoConfiguration
    public static class SpringBootConfiguration {

    }

    public static void main(String[] args) throws Exception {
        ApplicationContext ctx = SpringApplication.run(SpringBootConfiguration.class, args);

        assertNotNull(ctx.getBean(RuntimeService.class));

        System.out.println("通过Spring Boot启动了Http Server，以下是Spring Boot扫描的Bean列表：");
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            // System.out.println("found bean -> " + beanName);
        }
    }
}
