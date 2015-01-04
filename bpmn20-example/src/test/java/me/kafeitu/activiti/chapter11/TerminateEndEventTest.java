package me.kafeitu.activiti.chapter11;

import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;

/**
 * classpath方式部署流程定义
 *
 * @author henryyan
 */
public class TerminateEndEventTest extends PluggableActivitiTestCase {

    /**
     * 先完成【任务A】，流程未结束
     */
    @Deployment(resources = "chapter11/terminateEndEvent/terminateEndEvent.bpmn")
    public void testOne() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("terminateEndEvent");
        assertNotNull(processInstance);

        // 完成第一个
        Task task1 = taskService.createTaskQuery().taskDefinitionKey("usertask1").singleResult();
        taskService.complete(task1.getId());

        // 流程还在运行
        long count = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).count();
        assertEquals(1, count);

        Task task2 = taskService.createTaskQuery().taskDefinitionKey("usertask2").singleResult();
        taskService.complete(task2.getId());

        // 流程已结束
        count = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).count();
        assertEquals(0, count);
    }

    /**
     * 直接完成【任务B】整个流程实例结束
     */
    @Deployment(resources = "chapter11/terminateEndEvent/terminateEndEvent.bpmn")
    public void testSecond() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("terminateEndEvent");
        assertNotNull(processInstance);

        // 完成第一个
        Task task1 = taskService.createTaskQuery().taskDefinitionKey("usertask2").singleResult();
        taskService.complete(task1.getId());

        // 流程还在运行
        long count = runtimeService.createProcessInstanceQuery().processInstanceId(processInstance.getId()).count();
        assertEquals(0, count);
    }

}
