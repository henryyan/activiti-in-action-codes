package me.kafeitu.activiti.chapter10;

import org.activiti.engine.history.HistoricVariableInstance;
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
 * 购买办公用品流程测试--包含事件子流程--异常事件
 *
 * @author henryyan
 */
@ContextConfiguration("classpath:applicationContext-test-chapter10.xml")
public class PurchaseErrorEventSubProcessTest extends SpringActivitiTestCase {

    /**
     * 财务拒绝
     */
    @Test
    @Deployment(resources = {"diagrams/chapter10/purchase-error-event-subprocess.bpmn"})
    public void testRejectOnTreasurer() throws Exception {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put("dueDate", "2013-03-11");
        String listing = "1. MacBook Pro一台\n";
        listing += "2. 27寸显示器一台";
        properties.put("listing", listing);
        properties.put("amountMoney", "22000");

        identityService.setAuthenticatedUserId("henryyan");
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("purchase-error-event-subprocess").singleResult();
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
        Execution subExecution = runtimeService.createExecutionQuery().processInstanceId(processInstance.getId()).activityId("treasurerAudit").singleResult();
        assertNotNull(subExecution);
        assertEquals(listing, runtimeService.getVariable(processInstance.getId(), "usage"));

        // 子流程--财务审批
        task = taskService.createTaskQuery().taskCandidateGroup("treasurer").singleResult();
        taskService.claim(task.getId(), "kermit");
        properties = new HashMap<String, String>();
        properties.put("treasurerApproved", "false");
        formService.submitTaskFormData(task.getId(), properties);

        // 验证执行了事件子流程
        HistoricVariableInstance ERROR_INFO = historyService.createHistoricVariableInstanceQuery().variableName("ERROR_INFO").singleResult();
        assertEquals("财务审批未通过", ERROR_INFO.getValue());
    }

}
