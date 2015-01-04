package me.kafeitu.activiti;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.test.Deployment;
import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

/**
 * 自定义表单引擎
 *
 * @author henryyan
 */
public class MyFormEngineTest extends AbstractTest {

    @Test
    @Deployment(resources = {"chapter6/leave-formkey/leave-formkey.bpmn", "chapter6/leave-formkey/leave-start.form"})
    public void allPass() throws Exception {

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();

        // 读取启动表单
        Object renderedStartForm = formService.getRenderedStartForm(processDefinition.getId(), "myformengine");

        // 验证表单对象
        assertNotNull(renderedStartForm);
        assertTrue(renderedStartForm instanceof javax.swing.JButton);
        javax.swing.JButton button = (JButton) renderedStartForm;
        assertEquals("My Start Form Button", button.getName());

    }
}
