package me.kafeitu.activiti.chapter7.engine;

import org.activiti.engine.RepositoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;


/**
 * 利用Spring自动部署流程定义
 *
 * @author henryyan
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-autodeployment.xml")
public class AutoDeploymentUseSpring {

    @Autowired
    RepositoryService repositoryService;

    @Test
    public void testAutoDeployment() {
        long count = repositoryService.createProcessDefinitionQuery().count();
        assertEquals(1, count);
    }

}
