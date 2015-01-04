package me.kafeitu.activiti.web.chapter13;

import me.kafeitu.activiti.chapter13.ActivityUtil;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.SubProcessActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.ReadOnlyProcessDefinition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程跟踪控制器
 * User: henryyan
 */
@Controller
@RequestMapping(value = "/chapter13/process")
public class TraceProcessController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    TaskService taskService;

    @Autowired
    HistoryService historyService;

    @Autowired
    RuntimeService runtimeService;

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    protected IdentityService identityService;

    @Autowired
    ProcessEngineConfiguration processEngineConfiguration;

    /**
     * 读取流程资源
     */
    @RequestMapping(value = "trace/data/auto/{executionId}")
    public void readResource(@PathVariable("executionId") String executionId, HttpServletResponse response)
            throws Exception {
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(executionId).singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService.createProcessDefinitionQuery().processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(executionId);
        ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
        List<String> highLightedFlows = getHighLightedFlows(processDefinition, processInstance.getId());
        InputStream imageStream =diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds, highLightedFlows);

        // 输出资源内容到相应对象
        byte[] b = new byte[1024];
        int len;
        while ((len = imageStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }

    private List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinition, String processInstanceId) {
        List<String> highLightedFlows = new ArrayList<String>();
        List<HistoricActivityInstance> historicActivityInstances = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime().asc().list();

        List<String> historicActivityInstanceList = new ArrayList<String>();
        for (HistoricActivityInstance hai : historicActivityInstances) {
            historicActivityInstanceList.add(hai.getActivityId());
        }

        // add current activities to list
        List<String> highLightedActivities = runtimeService.getActiveActivityIds(processInstanceId);
        historicActivityInstanceList.addAll(highLightedActivities);

        // activities and their sequence-flows
        for (ActivityImpl activity : processDefinition.getActivities()) {
            int index = historicActivityInstanceList.indexOf(activity.getId());

            if (index >= 0 && index + 1 < historicActivityInstanceList.size()) {
                List<PvmTransition> pvmTransitionList = activity
                        .getOutgoingTransitions();
                for (PvmTransition pvmTransition : pvmTransitionList) {
                    String destinationFlowId = pvmTransition.getDestination().getId();
                    if (destinationFlowId.equals(historicActivityInstanceList.get(index + 1))) {
                        highLightedFlows.add(pvmTransition.getId());
                    }
                }
            }
        }
        return highLightedFlows;
    }

    /**
     * 读取历史数据
     *
     * @return
     */
    @RequestMapping(value = "trace/view/{executionId}")
    public ModelAndView historyDatas(@PathVariable("executionId") String executionId) {
        ModelAndView mav = new ModelAndView("chapter13/trace-process");

        // 查询Execution对象
        Execution execution = runtimeService.createExecutionQuery().executionId(executionId).singleResult();

        // 查询所有的历史活动记录
        String processInstanceId = execution.getProcessInstanceId();
        List<HistoricActivityInstance> activityInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId).list();

        // 查询历史流程实例
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId).singleResult();

        // 查询流程有关的变量
        List<HistoricVariableInstance> variableInstances = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId).list();

        List<HistoricDetail> formProperties = historyService.createHistoricDetailQuery().processInstanceId(processInstanceId).formProperties().list();

        // 查询流程定义对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(historicProcessInstance.getProcessDefinitionId()).singleResult();

        // 查询运行时流程实例
        ProcessInstance parentProcessInstance = runtimeService.createProcessInstanceQuery()
                .subProcessInstanceId(execution.getProcessInstanceId()).singleResult();

        mav.addObject("parentProcessInstance", parentProcessInstance);
        mav.addObject("historicProcessInstance", historicProcessInstance);
        mav.addObject("variableInstances", variableInstances);
        mav.addObject("activities", activityInstances);
        mav.addObject("formProperties", formProperties);
        mav.addObject("processDefinition", processDefinition);
        mav.addObject("executionId", executionId);

        return mav;
    }

    /**
     * 读取跟踪数据
     *
     * @param executionId
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "trace/data/{executionId}")
    @ResponseBody
    public List<Map<String, Object>> readActivityDatas(@PathVariable("executionId") String executionId) throws Exception {
        ExecutionEntity executionEntity = (ExecutionEntity) runtimeService.createExecutionQuery().executionId(executionId).singleResult();
        List<String> activeActivityIds = runtimeService.getActiveActivityIds(executionId);

        RepositoryServiceImpl repositoryServiceImpl = (RepositoryServiceImpl) repositoryService;
        ReadOnlyProcessDefinition deployedProcessDefinition = repositoryServiceImpl
                .getDeployedProcessDefinition(executionEntity.getProcessDefinitionId());

        ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) deployedProcessDefinition;
        List<ActivityImpl> activitiList = processDefinition.getActivities();//获得当前任务的所有节点

        List<Map<String, Object>> activityInfos = new ArrayList<Map<String, Object>>();
        for (ActivityImpl activity : activitiList) {

            ActivityBehavior activityBehavior = activity.getActivityBehavior();

            boolean currentActiviti = false;
            // 当前节点
            String activityId = activity.getId();
            if (activeActivityIds.contains(activityId)) {
                currentActiviti = true;
            }
            Map<String, Object> activityImageInfo = packageSingleActivitiInfo(activity, executionEntity.getId(), currentActiviti);
            activityInfos.add(activityImageInfo);

            // 处理子流程
            if (activityBehavior instanceof SubProcessActivityBehavior) {
                List<ActivityImpl> innerActivityList = activity.getActivities();
                for (ActivityImpl innerActivity : innerActivityList) {
                    String innerActivityId = innerActivity.getId();
                    if (activeActivityIds.contains(innerActivityId)) {
                        currentActiviti = true;
                    } else {
                        currentActiviti = false;
                    }
                    activityImageInfo = packageSingleActivitiInfo(innerActivity, executionEntity.getId(), currentActiviti);
                    activityInfos.add(activityImageInfo);
                }
            }

        }

        return activityInfos;
    }

    /**
     * 封装输出信息，包括：当前节点的X、Y坐标、变量信息、任务类型、任务描述
     *
     * @param activity
     * @param currentActiviti
     * @return
     */
    private Map<String, Object> packageSingleActivitiInfo(ActivityImpl activity, String executionId,
                                                          boolean currentActiviti) throws Exception {
        Map<String, Object> activityInfo = new HashMap<String, Object>();
        activityInfo.put("currentActiviti", currentActiviti);

        // 设置图形的XY坐标以及宽度、高度
        setSizeAndPositonInfo(activity, activityInfo);

        Map<String, Object> vars = new HashMap<String, Object>();
        Map<String, Object> properties = activity.getProperties();
        vars.put("任务类型", ActivityUtil.getZhActivityType(properties.get("type").toString()));
        vars.put("任务名称", properties.get("name"));

        // 当前节点的task
        if (currentActiviti) {
            setCurrentTaskInfo(executionId, activity.getId(), vars);
        }

        logger.debug("trace variables: {}", vars);
        activityInfo.put("vars", vars);
        return activityInfo;
    }

    /**
     * 获取当前节点信息
     *
     * @return
     */
    private void setCurrentTaskInfo(String executionId, String activityId, Map<String, Object> vars) {
        Task currentTask = taskService.createTaskQuery().executionId(executionId)
                .taskDefinitionKey(activityId).singleResult();
        logger.debug("current task for processInstance: {}", ToStringBuilder.reflectionToString(currentTask));

        if (currentTask == null) return;

        String assignee = currentTask.getAssignee();
        if (assignee != null) {
            User assigneeUser = identityService.createUserQuery().userId(assignee).singleResult();
            String userInfo = assigneeUser.getFirstName() + " " + assigneeUser.getLastName() + "/" + assigneeUser.getId();
            vars.put("当前处理人", userInfo);
            vars.put("创建时间", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(currentTask.getCreateTime()));
        } else {
            vars.put("任务状态", "未签收");
        }

    }

    /**
     * 设置宽度、高度、坐标属性
     *
     * @param activity
     * @param activityInfo
     */
    private void setSizeAndPositonInfo(ActivityImpl activity, Map<String, Object> activityInfo) {
        activityInfo.put("width", activity.getWidth());
        activityInfo.put("height", activity.getHeight());
        activityInfo.put("x", activity.getX());
        activityInfo.put("y", activity.getY());
    }
}
