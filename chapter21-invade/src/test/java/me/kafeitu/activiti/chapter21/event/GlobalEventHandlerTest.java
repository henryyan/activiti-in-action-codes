package me.kafeitu.activiti.chapter21.event;

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
 * 全局事件处理器
 * @author: Henry Yan
 */
public class GlobalEventHandlerTest {

    protected ProcessEngine processEngine;
    protected RepositoryService repositoryService;
    protected RuntimeService runtimeService;
    protected TaskService taskService;
    protected HistoryService historyService;
    protected IdentityService identityService;
    protected ManagementService managementService;
    protected FormService formService;

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
     * 处理全部事件
     */
    @Test
    public void testEventListener() throws Exception {
        initProcessEngine("chapter21/activiti.cfg.chapter21.event.eventListener.xml");
        deploy("chapter21/leave.bpmn");
        execute();
    }

    /**
     * 类型映射
     * @throws Exception
     */
    @Test
    public void testTypedEventListener() throws Exception {
        initProcessEngine("chapter21/activiti.cfg.chapter21.event.typedEventListener.xml");
        deploy("chapter21/leave.bpmn");
        execute();
    }

    /**
     * 在流程文件中定义事件处理器
     * @throws Exception
     */
    @Test
    public void testEventInProcess() throws Exception {
        initProcessEngine("chapter21/activiti.cfg.chapter21.event.default.xml");
        deploy("chapter21/leave-event.bpmn");
        execute();
    }

    /**
     * 单独处理实体类型的事件
     * @throws Exception
     */
    @Test
    public void testEntityEvent() throws Exception {
        initProcessEngine("chapter21/activiti.cfg.chapter21.event.entity.xml");
        deploy("chapter21/leave.bpmn");
        execute();
    }

    /**
     * 事件处理器的异常处理
     * @throws Exception
     */
    @Test
    public void testEventException() throws Exception {
        initProcessEngine("chapter21/activiti.cfg.chapter21.event.exception.xml");
        deploy("chapter21/leave.bpmn");
        execute();
    }

    /**
     * 任务自动代办：原本销假节点由用户henryyan处理，通过任务监听器把任务办理人更改为thomas
     * 该方法会执行失败，说明任务自动委派处理成功
     * @throws Exception
     */
    @Test
    public void testAutoRedirectUseTaskListener() throws Exception {
        initProcessEngine("chapter21/activiti.cfg.chapter21.event.default.xml");
        deploy("chapter21/leave-auto-redirect.bpmn");
        execute();
    }

    /**
     * 任务自动代办：原本销假节点由用户henryyan处理，通过TASK_CREATED事件处理器把任务办理人更改为thomas
     * 该方法会执行失败，说明任务自动委派处理成功
     * @throws Exception
     */
    @Test
    public void testAutoRedirectUseGlobalEvent() throws Exception {
        initProcessEngine("chapter21/activiti.cfg.chapter21.event.auto-redirect.xml");
        deploy("chapter21/leave.bpmn");
        execute();
    }

    @Test
    public void testAutoRedirectUseParseHandler() throws Exception {
        initProcessEngine("chapter21/activiti.cfg.chapter21.parse-auto-redirect.xml");
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
     * 执行流程
     */
    private void execute() {
        // 验证是否部署成功
        long count = repositoryService.createProcessDefinitionQuery().count();
        assertEquals(1, count);

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("leave").singleResult();

        // 设置当前用户
        String currentUserId = "henryyan";
        identityService.setAuthenticatedUserId(currentUserId);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
