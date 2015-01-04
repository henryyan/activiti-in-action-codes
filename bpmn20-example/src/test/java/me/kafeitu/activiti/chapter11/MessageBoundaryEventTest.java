package me.kafeitu.activiti.chapter11;

import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.management.TablePage;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 手动触发消息
 *
 * @author henryyan
 */
public class MessageBoundaryEventTest extends PluggableActivitiTestCase {

    /**
     * 在审核文件节点抛出消息事件触发消息边界事件--cancelActivity='true'
     */
    @Deployment(resources = "chapter11/boundaryEvent/messageBoundaryEvent.bpmn")
    public void testReceiveMessageManual() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("messageBoundaryEvent");
        assertNotNull(processInstance);

        // System.out.println("11eid=" + processInstance.getId() + ", pid=" +
        // processInstance.getProcessInstanceId());

        // 审核文件任务
        Task task = taskService.createTaskQuery().taskName("审核文件").singleResult();
        assertNotNull(task);
        ExecutionQuery executionQuery = runtimeService.createExecutionQuery().messageEventSubscriptionName("MSG_协助处理");
        Execution execution = executionQuery.singleResult();
        // System.out.println("22eid=" + execution.getId() + ", pid=" +
        // execution.getProcessInstanceId());

    /*
     * TablePage listPage =
     * managementService.createTablePageQuery().tableName("ACT_RU_EVENT_SUBSCR"
     * ).listPage(0, 10); List<Map<String, Object>> rows = listPage.getRows();
     * for (Map<String, Object> map : rows) { Set<Entry<String, Object>>
     * entrySet = map.entrySet(); for (Entry<String, Object> entry : entrySet) {
     * System.out.println(entry.getKey() + " = " + entry.getValue()); } }
     */

        runtimeService.messageEventReceived("MSG_协助处理", execution.getId());

        execution = runtimeService.createExecutionQuery().singleResult();
        // System.out.println("33eid=" + execution.getId() + ", pid=" +
        // execution.getProcessInstanceId());

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
    @Deployment(resources = "chapter11/boundaryEvent/messageBoundaryEvent.bpmn")
    public void testCompleteDirectly() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("messageBoundaryEvent");
        assertNotNull(processInstance);

        // 审核文件任务
        Task task = taskService.createTaskQuery().taskName("审核文件").singleResult();
        assertNotNull(task);
        ExecutionQuery executionQuery = runtimeService.createExecutionQuery();
        Execution execution = executionQuery.messageEventSubscriptionName("MSG_协助处理").singleResult();
        assertNotNull(execution);

        taskService.complete(task.getId());

        execution = executionQuery.messageEventSubscriptionName("MSG_协助处理").singleResult();
        assertNull(execution);

    }

    /**
     * 触发多次消息事件--cancelActivity='false'
     */
    @Deployment(resources = "chapter11/boundaryEvent/messageBoundaryEventNoCancelActivity.bpmn")
    public void testTriggerManyTimes() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("messageBoundaryEventNoCancelActivity");
        assertNotNull(processInstance);

        System.out.println("11eid=" + processInstance.getId() + ", pid=" + processInstance.getProcessInstanceId());

        // 审核文件任务
        Task task = taskService.createTaskQuery().taskName("审核文件").singleResult();
        assertNotNull(task);
        ExecutionQuery executionQuery = runtimeService.createExecutionQuery().messageEventSubscriptionName("MSG_协助处理");
        Execution execution = executionQuery.singleResult();

        // 触发两次
        runtimeService.messageEventReceived("MSG_协助处理", execution.getId());
        runtimeService.messageEventReceived("MSG_协助处理", execution.getId());

        TablePage listPage = managementService.createTablePageQuery().tableName("ACT_RU_EVENT_SUBSCR").listPage(0, 10);
        List<Map<String, Object>> rows = listPage.getRows();
        for (Map<String, Object> map : rows) {
            Set<Entry<String, Object>> entrySet = map.entrySet();
            for (Entry<String, Object> entry : entrySet) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }
        }

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

        // 完成两个协助处理任务
        for (Task task2 : list) {
            taskService.complete(task2.getId());
        }

        // 任务回归到审核文件，此时有3个注册了消息名称为“MSG_协助处理”的事件
        assertEquals(3, executionQuery.count());
        List<Execution> list3 = executionQuery.list();
        for (Execution execution2 : list3) {
            task = taskService.createTaskQuery().executionId(execution2.getId()).singleResult();
            taskService.complete(task.getId());
        }

        // 三个任务都完成了，此时整个流程实例结束
        assertEquals(1, historyService.createHistoricProcessInstanceQuery().finished().count());
    }

}
