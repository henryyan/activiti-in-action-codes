package me.kafeitu.activiti.chapter11;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;

import java.util.List;

/**
 * classpath方式部署流程定义
 *
 * @author henryyan
 */
public class TerminateEndEventWithSubprocess extends PluggableActivitiTestCase {

    /**
     * 先完成子流程，流程未结束
     */
    @Deployment(resources = "chapter11/terminateEndEvent/terminateEndEventWithSubprocess.bpmn")
    public void testOne() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("terminateEndEventWithSubprocess");
        assertNotNull(processInstance);

        // 查询子任务的
        Task task = taskService.createTaskQuery().taskDefinitionKey("subTask").singleResult();
        taskService.complete(task.getId());

        // 完成主流程用户任务
        task = taskService.createTaskQuery().taskDefinitionKey("masterTask").singleResult();
        taskService.complete(task.getId());

        checkFinished(processInstance);
    }

    /**
     * 直接触发“Receive Task”，流程结束
     */
    @Deployment(resources = "chapter11/terminateEndEvent/terminateEndEventWithSubprocess.bpmn")
    public void testSecond() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("terminateEndEventWithSubprocess");
        assertNotNull(processInstance);

        // 完成主流程用户任务
        Task task = taskService.createTaskQuery().taskDefinitionKey("masterTask").singleResult();
        taskService.complete(task.getId());

        checkFinished(processInstance);
    }

    private void checkFinished(ProcessInstance processInstance) {
        // 验证流程已结束
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstance.getProcessInstanceId()).singleResult();
        assertNotNull(historicProcessInstance.getEndTime());

        // 查询历史任务
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().list();
        for (HistoricTaskInstance hti : list) {
            System.out.println(hti.getName() + "  " + hti.getDeleteReason());
        }

        // 流程结束后校验监听器设置的变量
        HistoricVariableInstance variableInstance = historyService.createHistoricVariableInstanceQuery().variableName("settedOnEnd").singleResult();
        assertEquals(true, variableInstance.getValue());
    }

}
