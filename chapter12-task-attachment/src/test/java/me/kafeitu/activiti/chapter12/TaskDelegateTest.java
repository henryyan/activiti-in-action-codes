package me.kafeitu.activiti.chapter12;

import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: henryyan
 */
public class TaskDelegateTest extends PluggableActivitiTestCase {

    /**
     * 学习任务委派的过程，任务分配给bill，然后bill把任务委派给henryyan，henryyan处理完成后任务回归到bill
     */
    @Deployment(resources = "diagrams/chapter12/taskDelegate.bpmn")
    public void testTaskDelegate() throws Exception {
        Map<String, Object> variables = new HashMap<String, Object>();
        String userId = "bill";
        variables.put("userId", userId);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("taskDelegateProcess", variables);
        Task task = taskService.createTaskQuery().taskAssignee(userId).singleResult();
        assertNotNull(task);
        assertNull(task.getOwner());

        // 委派任务给其他人
        String delegatedUserId = "henryyan";
        taskService.delegateTask(task.getId(), delegatedUserId);

        // 查看数据状态
        task = taskService.createTaskQuery().taskAssignee(delegatedUserId).taskDelegationState(DelegationState.PENDING).singleResult();
        assertEquals(task.getOwner(), userId);
        assertEquals(task.getAssignee(), delegatedUserId);

        // 被委派人处理完成任务
        taskService.resolveTask(task.getId());

        // 任务回归到委派人
        task = taskService.createTaskQuery().taskAssignee(userId).taskDelegationState(DelegationState.RESOLVED).singleResult();
        assertEquals(task.getOwner(), userId);
        assertEquals(task.getAssignee(), userId);

        // 委派人完成任务
        taskService.complete(task.getId());
        long count = historyService.createHistoricProcessInstanceQuery().finished().count();
        assertEquals(1, count);
    }
}
