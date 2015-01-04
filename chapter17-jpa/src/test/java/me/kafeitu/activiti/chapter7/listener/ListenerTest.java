package me.kafeitu.activiti.chapter7.listener;

import me.kafeitu.activiti.base.AbstractTest;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * 监听器测试，不使用Spring
 *
 * @author henryyan
 */
public class ListenerTest extends AbstractTest {

    @Test
    @Deployment(resources = {"diagrams/chapter7/listener/listener.bpmn"})
    public void testListener() {
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("endListener", new ProcessEndExecutionListener());
        variables.put("assignmentDelegate", new TaskAssigneeListener());
        variables.put("name", "Henry Yan");

        identityService.setAuthenticatedUserId("henryyan");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("listener", variables);

        // 校验是否执行了启动监听
        String processInstanceId = processInstance.getId();
        assertTrue((Boolean) runtimeService.getVariable(processInstanceId, "setInStartListener"));

        Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).taskAssignee("jenny").singleResult();
        String setInTaskCreate = (String) taskService.getVariable(task.getId(), "setInTaskCreate");
        assertEquals("create, Hello, Henry Yan", setInTaskCreate);
        taskService.complete(task.getId());

        // 流程结束后查询变量
        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
        boolean hasVariableOfEndListener = false;
        for (HistoricVariableInstance variableInstance : list) {
            if (variableInstance.getVariableName().equals("setInEndListener")) {
                hasVariableOfEndListener = true;
            }
        }
        assertTrue(hasVariableOfEndListener);
    }

}
