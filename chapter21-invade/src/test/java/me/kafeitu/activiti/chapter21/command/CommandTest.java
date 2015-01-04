package me.kafeitu.activiti.chapter21.command;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author: Henry Yan
 */
public class CommandTest {

    protected ProcessEngine processEngine;
    protected RepositoryService repositoryService;
    protected RuntimeService runtimeService;
    protected TaskService taskService;
    protected HistoryService historyService;
    protected IdentityService identityService;
    protected ManagementService managementService;
    protected FormService formService;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public void initProcessEngine(String file) throws Exception {
        processEngine = ProcessEngineConfiguration.createProcessEngineConfigurationFromResource(file).buildProcessEngine();
        repositoryService = processEngine.getRepositoryService();
        runtimeService = processEngine.getRuntimeService();
        taskService = processEngine.getTaskService();
        historyService = processEngine.getHistoryService();
        identityService = processEngine.getIdentityService();
        managementService = processEngine.getManagementService();
        formService = processEngine.getFormService();
    }

    /**
     * 自定义命令：更改任务名称
     * @throws Exception
     */
    @Test
    public void testCustomCommand() throws Exception {
        initProcessEngine("chapter21/activiti.cfg.chapter21.command.xml");
        deploy("chapter21/leave.bpmn");
        ProcessInstance processInstance = startProcess();

        // 验证任务名称
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        assertEquals("部门领导审批", task.getName());

        // 执行自定义的命令
        managementService.executeCommand(new ModifyTaskNameCmd(task.getId(), task.getName() + "-Modified"));
        task = taskService.createTaskQuery().singleResult();
        assertEquals("部门领导审批-Modified", task.getName());
    }

    /**
     * 自定义命令：节点跳转
     * @throws Exception
     */
    @Test
    public void testCustomCommandJump() throws Exception {
        initProcessEngine("chapter21/activiti.cfg.chapter21.command.xml");
        deploy("chapter21/leave.bpmn");
        ProcessInstance processInstance = startProcess();

        // 验证任务名称
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        assertEquals("部门领导审批", task.getName());

        // 执行自定义的命令
        managementService.executeCommand(new JumpActivityCmd(processInstance.getId(), "reportBack"));
        task = taskService.createTaskQuery().singleResult();
        assertEquals("销假", task.getName());
    }

    /**
     * Pre类型的Command拦截器
     */
    @Test
    public void testPreCommandInterceptor() throws Exception {
        initProcessEngine("chapter21/activiti.cfg.chapter21.command.xml");
        deploy("chapter21/leave.bpmn");
        execute();
    }

    /**
     * 部署流程文件
     * @param bpmnFile
     */
    private void deploy(String bpmnFile) {
        repositoryService.createDeployment().addClasspathResource(bpmnFile).deploy();
    }

    /**
     * 启动流程
     * @return
     */
    private ProcessInstance startProcess() {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("leave").singleResult();

        Map<String, String> variables = new HashMap<String, String>();
        Calendar ca = Calendar.getInstance();
        String startDate = sdf.format(ca.getTime());
        ca.add(Calendar.DAY_OF_MONTH, 2); // 当前日期加2天
        String endDate = sdf.format(ca.getTime());

        // 启动流程
        variables.put("startDate", startDate);
        variables.put("endDate", endDate);
        variables.put("reason", "公休");
        ProcessInstance processInstance = formService.submitStartFormData(processDefinition.getId(), variables);
        assertNotNull(processInstance);

        return processInstance;
    }

    /**
     * 执行流程
     */
    private void execute() {
        // 验证是否部署成功
        long count = repositoryService.createProcessDefinitionQuery().count();
        assertEquals(1, count);

        Map<String, String> variables = new HashMap<String, String>();

        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DAY_OF_MONTH, 2); // 当前日期加2天

        // 设置当前用户
        String currentUserId = "henryyan";
        identityService.setAuthenticatedUserId(currentUserId);

        // 启动流程
        ProcessInstance processInstance = startProcess();

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

        // 销假（根据申请人的用户ID读取）
        Task reportBackTask = taskService.createTaskQuery().singleResult();
        List<Comment> taskComments = taskService.getTaskComments(reportBackTask.getId(), "delegate");
        for (Comment taskComment : taskComments) {
            System.out.println("任务相关事件：" + taskComment.getFullMessage());
        }
        assertEquals(currentUserId, reportBackTask.getAssignee());
        variables = new HashMap<String, String>();
        variables.put("reportBackDate", sdf.format(ca.getTime()));
        formService.submitTaskFormData(reportBackTask.getId(), variables);

        // 验证流程是否已经结束
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().finished().singleResult();
        assertNotNull(historicProcessInstance);

        // 读取历史变量
        Map<String, Object> historyVariables = packageVariables(processInstance.getId());

        // 验证执行结果
        assertEquals("ok", historyVariables.get("result"));
    }

    /**
     * 读取历史变量并封装到Map中
     */
    private Map<String, Object> packageVariables(String processInstanceId) {
        List<HistoricVariableInstance> variableInstances = historyService.createHistoricVariableInstanceQuery().processInstanceId(processInstanceId).list();
        Map<String, Object> historyVariables = new HashMap<String, Object>();
        for (HistoricVariableInstance variableInstance : variableInstances) {
            historyVariables.put(variableInstance.getVariableName(), variableInstance.getValue());
        }

        return historyVariables;
    }

}
