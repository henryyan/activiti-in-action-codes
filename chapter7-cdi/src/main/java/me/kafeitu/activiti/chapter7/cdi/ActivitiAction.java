package me.kafeitu.activiti.chapter7.cdi;

import me.kafeitu.activiti.chapter7.cdi.bean.SystemUser;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.Map;

/**
 * @author: Henry Yan
 */
public class ActivitiAction {

    @Inject
    private ManagementService managementService;

    @Inject
    private RepositoryService repositoryService;

    @Inject
    private TaskService taskService;

    @Inject
    private RuntimeService runtimeService;

    @Inject
    private HistoryService historyService;

    @Inject
    private SystemUser systemUser;

    /**
     * 读取流程引擎属性列表
     * @return
     */
    @Produces
    @Named("engineProperties")
    public Map<String, String> getEngineProperties() {
        return managementService.getProperties();
    }

    /**
     * 读取流程定义
     * @return
     */
    @Produces
    @Named("processDefinitionList")
    public List<ProcessDefinition> getProcessDefinitionList() {
        return repositoryService.createProcessDefinitionQuery().list();
    }

    /**
     * 读取待办任务
     */
    @Produces
    @Named("todoTaskList")
    public List<Task> getPersonalTaskList() {
        return taskService.createTaskQuery()
                .taskCandidateOrAssigned(systemUser.getUserId()).list();
    }

    @Produces
    @Named("vars")
    public List<HistoricVariableInstance> getVariables() {
        return historyService.createHistoricVariableInstanceQuery().list();
    }

}
