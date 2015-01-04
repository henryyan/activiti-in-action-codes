package me.kafeitu.activiti.chapter11;

import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;

import java.util.HashMap;
import java.util.Map;

/**
 * 手动抛出异常
 *
 * @author henryyan
 */
public class ThrowErrorManualTest extends PluggableActivitiTestCase {

    @Deployment(resources = "chapter11/boundaryEvent/throwErrorManual.bpmn")
    public void testThrowErrorManual() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("throwErrorManual");
        assertNotNull(processInstance);

        // 流转至用户任务
        Task task = taskService.createTaskQuery().singleResult();
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("pass", true);
        taskService.complete(task.getId(), variables);

        // 流程执行完成
        long count = historyService.createHistoricProcessInstanceQuery().finished().count();
        assertEquals(1, count);
    }

}
