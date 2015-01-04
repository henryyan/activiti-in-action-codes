package me.kafeitu.activiti.chapter7.annotation;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.spring.annotations.EnableActiviti;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;
import java.sql.Driver;

import static org.junit.Assert.assertNotNull;

/**
 * 全Spring注解初始化引擎并获取Service接口
 */
public class InitProcessEngineBySpringAnnotation {

    @Configuration
    @EnableActiviti
    public static class SpringAnnotationConfiguration {

        /**
         * 定义数据源
         * @return
         * @throws ClassNotFoundException
         */
        @Bean
        public DataSource dataSource() throws ClassNotFoundException {
            SimpleDriverDataSource simpleDriverDataSource = new SimpleDriverDataSource();
            simpleDriverDataSource.setDriverClass((Class<? extends Driver>) Class.forName("org.h2.Driver"));
            simpleDriverDataSource.setUrl("jdbc:h2:mem:aia-chapter7;DB_CLOSE_DELAY=1000");
            simpleDriverDataSource.setUsername("sa");
            simpleDriverDataSource.setPassword("");
            return simpleDriverDataSource;
        }

    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext();
        ctx.register(SpringAnnotationConfiguration.class);
        ctx.refresh();

        assertNotNull(ctx.getBean(ProcessEngine.class));
        assertNotNull(ctx.getBean(RuntimeService.class));
        TaskService bean = ctx.getBean(TaskService.class);
        assertNotNull(bean);
        assertNotNull(ctx.getBean(HistoryService.class));
        assertNotNull(ctx.getBean(RepositoryService.class));
        assertNotNull(ctx.getBean(ManagementService.class));
        assertNotNull(ctx.getBean(FormService.class));
        Task task = bean.newTask();
        task.setName("哈哈");
        bean.saveTask(task);
    }
}