package me.kafeitu.activiti.chapter21.listeners;

import org.activiti.engine.EngineServices;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 任务自动转办
 * @author: Henry Yan
 */
public class TaskAutoRedirectListener implements TaskListener {

    private static Map<String, String> userMap = new HashMap<String, String>();

    static {
        userMap.put("henryyan", "thomas");
    }

    @Override
    public void notify(DelegateTask delegateTask) {
        String originAssginee = delegateTask.getAssignee();
        String newUser = userMap.get(originAssginee);
        if (StringUtils.isNotBlank(newUser)) {
            delegateTask.setAssignee(newUser);
            EngineServices engineServices = delegateTask.getExecution().getEngineServices();
            TaskService taskService = engineServices.getTaskService();
            String message = getClass().getName() + "-> 任务[" + delegateTask.getName() + "]的办理人[" + originAssginee + "]自动转办给了用户[" + newUser + "]";
            taskService.addComment(delegateTask.getId(), delegateTask.getProcessInstanceId(), "delegate", message);
        } else {
            System.out.println("任务[" + delegateTask.getName() + "]没有预设的代办人");
        }
    }
}
