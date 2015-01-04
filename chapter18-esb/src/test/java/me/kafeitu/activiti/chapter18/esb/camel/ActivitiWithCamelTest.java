package me.kafeitu.activiti.chapter18.esb.camel;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.activiti.spring.impl.test.SpringActivitiTestCase;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Camel与Activiti流程整合示例
 *
 * @author: Henry Yan
 */
@ContextConfiguration("classpath:camel/applicationContext.xml")
public class ActivitiWithCamelTest extends SpringActivitiTestCase {

    @Deployment(resources = {"camel/leaveWithCamel.bpmn"})
    public void testCamel() throws Exception {
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("days", 5);

        // 设置camelBody
        vars.put("camelBody", Collections.singletonMap("days", 5));

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leaveWithCamel", vars);
        assertNotNull(processInstance);

        Task task = taskService.createTaskQuery().singleResult();
        assertEquals("部门经理审批", task.getName());
    }

    @Deployment(resources = {"camel/leaveWithCamel.bpmn"})
    public void testCamelLessThree() throws Exception {
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("days", 2);

        // 设置camelBody
        vars.put("camelBody", Collections.singletonMap("days", 2));

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leaveWithCamel", vars);
        assertNotNull(processInstance);

        Task task = taskService.createTaskQuery().singleResult();
        assertEquals("直接领导审批", task.getName());
    }

    /**
     * 通过Camel启动流程
     * @throws Exception
     */
    @Deployment(resources = {"camel/masterProcess.bpmn", "camel/camelSubProcess.bpmn"})
    public void testStartProcessByCamel() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("masterProcess");
        assertNotNull(processInstance);

        // 检查子流程是否已启动
        ProcessInstance subProcess = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("camelSubProcess").singleResult();
        assertNotNull(subProcess);
    }

}
