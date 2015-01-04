package me.kafeitu.activiti.chapter7.service;

import me.kafeitu.activiti.chapter7.entity.Leave;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 请假流程Service
 *
 * @author henryyan
 */
@Service
@Transactional
public class LeaveWorkflowService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    LeaveManager leaveManager;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 保存请假实体并启动流程
     */
    public ProcessInstance startWorkflow(Leave entity, String userId, Map<String, Object> variables) {
        if (entity.getId() == null) {
            entity.setApplyTime(new Date());
            entity.setUserId(userId);
        }
        leaveManager.save(entity);
        String businessKey = entity.getId().toString();

        // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
        identityService.setAuthenticatedUserId(userId);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave", businessKey, variables);
        String processInstanceId = processInstance.getId();
        entity.setProcessInstanceId(processInstanceId);
        logger.debug("start process of {key={}, bkey={}, pid={}, variables={}}", new Object[]{"leave", businessKey, processInstanceId, variables});
        leaveManager.save(entity);
        return processInstance;
    }

    /**
     * 查询待办任务
     */
    @Transactional(readOnly = true)
    public List<Leave> findTodoTasks(String userId) {
        List<Leave> results = new ArrayList<Leave>();
        List<Task> tasks = new ArrayList<Task>();

        // 根据当前人的ID查询
        List<Task> todoList = taskService.createTaskQuery().processDefinitionKey("leave").taskAssignee(userId).list();

        // 根据当前人未签收的任务
        List<Task> unsignedTasks = taskService.createTaskQuery().processDefinitionKey("leave").taskCandidateUser(userId).list();

        // 合并
        tasks.addAll(todoList);
        tasks.addAll(unsignedTasks);

        // 根据流程的业务ID查询实体并关联
        for (Task task : tasks) {
            String processInstanceId = task.getProcessInstanceId();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            String businessKey = processInstance.getBusinessKey();
            Leave leave = leaveManager.get(new Long(businessKey));
            leave.setTask(task);
            leave.setProcessInstance(processInstance);
            String processDefinitionId = processInstance.getProcessDefinitionId();
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
            leave.setProcessDefinition(processDefinition);
            results.add(leave);
        }
        return results;
    }

    public void complete(Leave leave, Boolean saveEntity, String taskId, Map<String, Object> variables) {
        if (saveEntity) {
            leaveManager.save(leave);
        }
        taskService.complete(taskId, variables);
    }

}
