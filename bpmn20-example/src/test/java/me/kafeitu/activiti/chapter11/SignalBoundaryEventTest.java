package me.kafeitu.activiti.chapter11;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;

import java.util.List;

/**
 * 手动触发消息
 *
 * @author henryyan
 */
public class SignalBoundaryEventTest extends PluggableActivitiTestCase {

    /**
     * 在审核文件节点抛出消息事件触发消息边界事件--cancelActivity='true'
     */
    @Deployment(resources = "chapter11/boundaryEvent/signalBoundaryEvent.bpmn")
    public void testReceiveMessageManual() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("signalBoundaryEvent");
        assertNotNull(processInstance);

        // 审核文件任务
        Task task = taskService.createTaskQuery().taskName("审核文件").singleResult();
        assertNotNull(task);
        ExecutionQuery executionQuery = runtimeService.createExecutionQuery().signalEventSubscriptionName("S_协助处理");
        Execution execution = executionQuery.singleResult();
        runtimeService.signalEventReceived("S_协助处理", execution.getId());

        // 任务到达“协助处理节点”
        task = taskService.createTaskQuery().taskName("协助处理").singleResult();
        assertNotNull(task);
        taskService.complete(task.getId());

        // 任务流转至审核文件节点
        task = taskService.createTaskQuery().taskName("审核文件").singleResult();
        assertNotNull(task);

        // 验证流程实例运行结束
        taskService.complete(task.getId());
        assertEquals(1, historyService.createHistoricProcessInstanceQuery().finished().count());
    }

    /**
     * 不触发消息边界事件，直接完成任务--cancelActivity='true'
     */
    @Deployment(resources = "chapter11/boundaryEvent/signalBoundaryEvent.bpmn")
    public void testCompleteDirectly() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("signalBoundaryEvent");
        assertNotNull(processInstance);

        // 审核文件任务
        Task task = taskService.createTaskQuery().taskName("审核文件").singleResult();
        assertNotNull(task);
        ExecutionQuery executionQuery = runtimeService.createExecutionQuery();
        Execution execution = executionQuery.signalEventSubscriptionName("S_协助处理").singleResult();
        assertNotNull(execution);

        taskService.complete(task.getId());

    }

    /**
     * 触发多次消息事件--cancelActivity='false'
     */
    @Deployment(resources = "chapter11/boundaryEvent/signalBoundaryEventNoCancelActivity.bpmn")
    public void testMultiInstance() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("signalBoundaryEventNoCancelActivity");
        assertNotNull(processInstance);

        // 审核文件任务
        Task task = taskService.createTaskQuery().taskName("审核文件").singleResult();
        assertNotNull(task);
        ExecutionQuery executionQuery = runtimeService.createExecutionQuery().signalEventSubscriptionName("S_协助处理");
        Execution execution = executionQuery.singleResult();

        // 触发两次
        runtimeService.signalEventReceived("S_协助处理", execution.getId());
        runtimeService.signalEventReceived("S_协助处理", execution.getId());

        List<Execution> list2 = runtimeService.createExecutionQuery().list();
        for (Execution executionTemp : list2) {
            ExecutionEntity ee = (ExecutionEntity) executionTemp;
            System.out.println("execution: id=" + ee.getId() + ", pid=" + ee.getProcessInstanceId() + ", activityId=" + ee.getActivityId() + ", active="
                    + ee.isActive());
        }

        // 任务到达“协助处理节点”
        assertEquals(2, taskService.createTaskQuery().taskName("协助处理").count());

        List<Task> list = taskService.createTaskQuery().taskName("协助处理").list();
        for (Task task2 : list) {
            System.out.println("task: id=" + task2.getId() + ", executionId=" + task2.getExecutionId() + ", tkey=" + task2.getTaskDefinitionKey() + ", name="
                    + task2.getName());
        }

    }

    /**
     * 在审核文件节点抛出消息事件触发消息边界事件--cancelActivity='false'
     */
    @Deployment(resources = "chapter11/boundaryEvent/signalBoundaryEventNoCancelActivity.bpmn")
    public void testReceiveMessageManualNoCancelActivity() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("signalBoundaryEventNoCancelActivity");
        assertNotNull(processInstance);

        // 审核文件任务
        Task task = taskService.createTaskQuery().taskName("审核文件").singleResult();
        assertNotNull(task);
        ExecutionQuery executionQuery = runtimeService.createExecutionQuery().signalEventSubscriptionName("S_协助处理");
        Execution execution = executionQuery.singleResult();
        runtimeService.signalEventReceived("S_协助处理", execution.getId());

        // 任务到达“协助处理节点”
        task = taskService.createTaskQuery().taskName("协助处理").singleResult();
        assertNotNull(task);
        taskService.complete(task.getId());

        // 任务流转至审核文件节点
        List<Task> tasks = taskService.createTaskQuery().taskName("审核文件").active().list();
        assertEquals(2, tasks.size());

        // 2个注册了消息事件的Execution
        List<Execution> list = executionQuery.list();
        for (Execution executionTemp : list) {
            ExecutionEntity ee = (ExecutionEntity) executionTemp;
            System.out.println(ee.getActivityId());
        }
        assertEquals(2, executionQuery.count());

        // 完成2个任务流程结束
        taskService.complete(tasks.get(1).getId());
    }

}
