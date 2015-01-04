package me.kafeitu.activiti.chapter10;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.activiti.spring.impl.test.SpringActivitiTestCase;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * 购买办公用品流程测试
 *
 * @author henryyan
 */
@ContextConfiguration("classpath:applicationContext-test-chapter10.xml")
public class PurchaseCallActivityTest extends SpringActivitiTestCase {

    /**
     * 全部通过
     */
    @Test
    @Deployment(resources = {"diagrams/chapter10/purchase-callactivity.bpmn", "diagrams/chapter10/payment.bpmn"})
    public void testAllApproved() throws Exception {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("dueDate", "2013-03-11");
        String listing = "1. MacBook Pro一台\n";
        listing += "2. 27寸显示器一台";
        properties.put("listing", listing);
        properties.put("amountMoney", "22000");

        identityService.setAuthenticatedUserId("henryyan");
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("purchase-callactivity").singleResult();
        ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), properties);
        assertNotNull(processInstance);

        // 部门领导
        Task task = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
        taskService.claim(task.getId(), "kermit");
        properties = new HashMap<String, String>();
        properties.put("deptLeaderApproved", "true");
        formService.submitTaskFormData(task.getId(), properties);

        // 联系供货方
        task = taskService.createTaskQuery().taskCandidateGroup("supportCrew").singleResult();
        taskService.claim(task.getId(), "kermit");
        properties = new HashMap<String, String>();
        properties.put("supplier", "苹果公司");
        properties.put("bankName", "中国工商银行");
        properties.put("bankAccount", "203840240274247293");
        properties.put("planDate", "2013-03-20");
        identityService.setAuthenticatedUserId("kermit");
        formService.submitTaskFormData(task.getId(), properties);

        // 验证是否启动子流程
        Execution subExecution = runtimeService.createExecutionQuery().processDefinitionKey("payment").singleResult();
        assertNotNull(subExecution);
        assertEquals(listing, runtimeService.getVariable(subExecution.getId(), "usage"));
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(subExecution.getProcessInstanceId()).singleResult();
        assertEquals("kermit", historicProcessInstance.getStartUserId());

        // 子流程--财务审批
        task = taskService.createTaskQuery().taskCandidateGroup("treasurer").singleResult();
        taskService.claim(task.getId(), "kermit");
        properties = new HashMap<String, String>();
        properties.put("treasurerApproved", "true");
        formService.submitTaskFormData(task.getId(), properties);

        // 子流程--总经理审批
        task = taskService.createTaskQuery().taskCandidateGroup("generalManager").singleResult();
        taskService.claim(task.getId(), "kermit");
        properties = new HashMap<String, String>();
        properties.put("generalManagerApproved", "true");
        formService.submitTaskFormData(task.getId(), properties);

        // 子流程--出纳付款
        task = taskService.createTaskQuery().taskCandidateGroup("cashier").singleResult();
        taskService.claim(task.getId(), "kermit");
        properties = new HashMap<String, String>();
        properties.put("generalManagerApproved", "true");
        formService.submitTaskFormData(task.getId(), properties);

        // 收货确认
        task = taskService.createTaskQuery().taskAssignee("henryyan").singleResult();
        assertEquals("收货确认", task.getName());
        taskService.complete(task.getId());

        long count = historyService.createHistoricProcessInstanceQuery().finished().count();
        assertEquals(2, count);
    }

    /**
     * 财务拒绝
     */
    @Test
    @Deployment(resources = {"diagrams/chapter10/purchase-callactivity.bpmn", "diagrams/chapter10/payment.bpmn"})
    public void testRejectOnTreasurer() throws Exception {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("dueDate", "2013-03-11");
        String listing = "1. MacBook Pro一台\n";
        listing += "2. 27寸显示器一台";
        properties.put("listing", listing);
        properties.put("amountMoney", "22000");

        identityService.setAuthenticatedUserId("henryyan");
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("purchase-callactivity").singleResult();
        ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), properties);
        assertNotNull(processInstance);

        // 部门领导
        Task task = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
        taskService.claim(task.getId(), "kermit");
        properties = new HashMap<String, String>();
        properties.put("deptLeaderApproved", "true");
        formService.submitTaskFormData(task.getId(), properties);

        // 联系供货方
        task = taskService.createTaskQuery().taskCandidateGroup("supportCrew").singleResult();
        taskService.claim(task.getId(), "kermit");
        properties = new HashMap<String, String>();
        properties.put("supplier", "苹果公司");
        properties.put("bankName", "中国工商银行");
        properties.put("bankAccount", "203840240274247293");
        properties.put("planDate", "2013-03-20");
        formService.submitTaskFormData(task.getId(), properties);

        // 验证是否启动子流程
        Execution subExecution = runtimeService.createExecutionQuery().processDefinitionKey("payment").singleResult();
        assertNotNull(subExecution);
        assertEquals(listing, runtimeService.getVariable(subExecution.getId(), "usage"));

        // 子流程--财务审批
        task = taskService.createTaskQuery().taskCandidateGroup("treasurer").singleResult();
        taskService.claim(task.getId(), "kermit");
        properties = new HashMap<String, String>();
        properties.put("treasurerApproved", "false");
        // TODO 完成任务失败！！！在Web执行可以。
        formService.submitTaskFormData(task.getId(), properties);

        // 任务流转至调整申请
        task = taskService.createTaskQuery().taskAssignee("henryyan").singleResult();
        assertNotNull(task);
    }

}
