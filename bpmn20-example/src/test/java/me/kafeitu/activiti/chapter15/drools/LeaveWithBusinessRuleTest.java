package me.kafeitu.activiti.chapter15.drools;

import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请假流程测试--包含业务规则Drools调用
 * @author: Henry Yan
 */
public class LeaveWithBusinessRuleTest extends PluggableActivitiTestCase {

    @Test
    @Deployment(resources = {"chapter15/leave-drools.bpmn", "chapter15/leave.drl"})
    public void testWithWebservice() throws Exception {
        // 验证是否部署成功
        long count = repositoryService.createProcessDefinitionQuery().count();
        assertEquals(1, count);

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("leave-drools").singleResult();

        // 设置当前用户
        String currentUserId = "henryyan";
        identityService.setAuthenticatedUserId(currentUserId);

        Leave leave = new Leave();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Map<String, String> variables = new HashMap<String, String>();
        Calendar ca = Calendar.getInstance();
        leave.setStartTime(ca.getTime());
        String startDate = sdf.format(ca.getTime());
        ca.add(Calendar.DAY_OF_MONTH, 4); // 当前日期加4天
        leave.setEndTime(ca.getTime());
        String endDate = sdf.format(ca.getTime());

        // 启动流程
        variables.put("startDate", startDate);
        variables.put("endDate", endDate);
        variables.put("reason", "公休");
        ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), variables);
        assertNotNull(processInstance);
        runtimeService.setVariable(processInstance.getId(), "leave", leave);

        // 部门领导审批通过
        Task deptLeaderTask = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
        variables = new HashMap<String, String>();
        variables.put("deptLeaderApproved", "true");
        formService.submitTaskFormData(deptLeaderTask.getId(), variables);

        // 人事审批通过
        Task hrTask = taskService.createTaskQuery().taskCandidateGroup("hr").singleResult();
        variables = new HashMap<String, String>();
        variables.put("hrApproved", "true");
        formService.submitTaskFormData(hrTask.getId(), variables);

        // 判断drools执行结果
        Leave leaveOutput = (Leave) runtimeService.getVariable(processInstance.getId(), "leave");
        assertTrue(leaveOutput.isGeneralManagerAudit());

        // 返回的结果集
        // 如果没有设置activiti:resultVariable属性默认的名称为：org.activiti.engine.rules.OUTPUT
        Collection<Object> ruleOutputList = (Collection<Object>)
                runtimeService.getVariable(processInstance.getId(), "rulesOutput");
        assertNotNull(ruleOutputList);
        assertEquals(1, ruleOutputList.size());
        leave = (Leave) ruleOutputList.iterator().next();
        assertTrue(leave.isGeneralManagerAudit());
    }

    @Test
    @Deployment(resources = {"chapter15/leave-drools-multi-variable.bpmn", "chapter15/leave-multi-variable.drl"})
    public void testWithWebserviceMultiVariables() throws Exception {
        // 验证是否部署成功
        long count = repositoryService.createProcessDefinitionQuery().count();
        assertEquals(1, count);

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("leave-drools-multi-variable").singleResult();

        // 设置当前用户
        String currentUserId = "henryyan";
        identityService.setAuthenticatedUserId(currentUserId);

        Leave leave = new Leave();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Map<String, String> variables = new HashMap<String, String>();
        Calendar ca = Calendar.getInstance();
        leave.setStartTime(ca.getTime());
        String startDate = sdf.format(ca.getTime());
        ca.add(Calendar.DAY_OF_MONTH, 4); // 当前日期加4天
        leave.setEndTime(ca.getTime());
        String endDate = sdf.format(ca.getTime());

        // 启动流程
        variables.put("startDate", startDate);
        variables.put("endDate", endDate);
        variables.put("reason", "公休");
        ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), variables);
        assertNotNull(processInstance);
        runtimeService.setVariable(processInstance.getId(), "inputVars", Collections.singletonMap("leave", leave));

        // 部门领导审批通过
        Task deptLeaderTask = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
        variables = new HashMap<String, String>();
        variables.put("deptLeaderApproved", "true");
        formService.submitTaskFormData(deptLeaderTask.getId(), variables);

        // 人事审批通过
        Task hrTask = taskService.createTaskQuery().taskCandidateGroup("hr").singleResult();
        variables = new HashMap<String, String>();
        variables.put("hrApproved", "true");
        formService.submitTaskFormData(hrTask.getId(), variables);

        // 判断drools执行结果
        Map inputVars = (Map) runtimeService.getVariable(processInstance.getId(), "inputVars");
        assertTrue(((Leave) inputVars.get("leave")).isGeneralManagerAudit());

        // 返回的结果集
        // 如果没有设置activiti:resultVariable属性默认的名称为：org.activiti.engine.rules.OUTPUT
        Collection<Object> ruleOutputList = (Collection<Object>)
                runtimeService.getVariable(processInstance.getId(), "rulesOutput");
        assertNotNull(ruleOutputList);
        assertEquals(1, ruleOutputList.size());
        Map rulesOutput = (Map) ruleOutputList.iterator().next();
        leave = (Leave)rulesOutput.get("leave");
        assertTrue(leave.isGeneralManagerAudit());
    }

    /**
     * 读取历史变量并封装到Map中
     */
    private Map<String, Object> packageVariables(ProcessInstance processInstance) {
        Map<String, Object> historyVariables = new HashMap<String, Object>();
        List<HistoricVariableInstance> list = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstance.getId()).list();
        for (HistoricVariableInstance variable : list) {
            historyVariables.put(variable.getVariableName(), variable.getValue());
            System.out.println("variable: " + variable.getVariableName() + " = " + variable.getValue());
        }
        return historyVariables;
    }

}
