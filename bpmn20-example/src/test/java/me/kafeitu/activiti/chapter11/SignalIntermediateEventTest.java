package me.kafeitu.activiti.chapter11;

import org.activiti.engine.impl.EventSubscriptionQueryImpl;
import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;

/**
 * @author henryyan
 */
public class SignalIntermediateEventTest extends PluggableActivitiTestCase {

    /**
     * 中间信号事件（捕获、抛出）、信号边界事件
     */
    @Deployment(resources = {"chapter11/intermediateEvent/signal/catchMultipleSignals.bpmn", "chapter11/intermediateEvent/signal/throwAlertSignal.bpmn",
            "chapter11/intermediateEvent/signal/throwAbortSignal.bpmn"})
    public void testSignalIntermediateEvent() throws Exception {
        runtimeService.startProcessInstanceByKey("catchSignal");

        // 注册了两个信号事件
        assertEquals(2, createEventSubscriptionQuery().count());
        assertEquals(1, runtimeService.createProcessInstanceQuery().count());

        // 启动流程后抛出了abort信号
        runtimeService.startProcessInstanceByKey("throwAbort");

        assertEquals(1, createEventSubscriptionQuery().count());
        assertEquals(1, runtimeService.createProcessInstanceQuery().count());

        Task taskAfterAbort = taskService.createTaskQuery().taskName("被中间信号抛出事件触发").singleResult();
        assertNotNull(taskAfterAbort);
        taskService.complete(taskAfterAbort.getId());

        // 启动流程后抛出了alert信号
        runtimeService.startProcessInstanceByKey("throwAlert");

        // 可以用下面的代码替代: runtimeService.startProcessInstanceByKey("throwAlert");
        // runtimeService.signalEventReceived("alert");

        Task taskAfterAlert = taskService.createTaskQuery().taskName("通过信号边界事件触发").singleResult();
        assertNotNull(taskAfterAlert);
        taskService.complete(taskAfterAlert.getId());

        assertEquals(0, createEventSubscriptionQuery().count());
        assertEquals(0, runtimeService.createProcessInstanceQuery().count());
    }

    /**
     * 创建消息订阅查询对象（非公开API）
     */
    private EventSubscriptionQueryImpl createEventSubscriptionQuery() {
        return new EventSubscriptionQueryImpl(processEngineConfiguration.getCommandExecutor());
    }

    @Deployment(resources = {"chapter11/intermediateEvent/signal/sameSignal.bpmn", "chapter11/intermediateEvent/signal/throwAlertSignal.bpmn"})
    public void testSameSignal() {
        runtimeService.startProcessInstanceByKey("sameSignal");
        assertEquals(2, createEventSubscriptionQuery().count());

        runtimeService.startProcessInstanceByKey("throwAlert");

        assertEquals(2, taskService.createTaskQuery().count());
    }

}
