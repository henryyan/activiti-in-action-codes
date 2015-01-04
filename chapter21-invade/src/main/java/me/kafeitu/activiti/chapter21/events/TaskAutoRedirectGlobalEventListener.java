package me.kafeitu.activiti.chapter21.events;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 使用全局事件实现任务自动转办
 * @author: Henry Yan
 */
public class TaskAutoRedirectGlobalEventListener implements ActivitiEventListener {

    private static Map<String, String> userMap = new HashMap<String, String>();

    static {
        userMap.put("henryyan", "thomas");
    }

    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;
        Object entity = entityEvent.getEntity();
        if (entity instanceof TaskEntity) {
            TaskEntity task = (TaskEntity) entity;
            String originUserId = task.getAssignee();
            String newUserId = userMap.get(originUserId);
            if (StringUtils.isNotBlank(newUserId)) {
                task.setAssignee(newUserId);
                TaskService taskService = event.getEngineServices().getTaskService();
                String message = getClass().getName() + "-> 任务[" + task.getName() + "]的办理人[" +
                        originUserId + "]自动转办给了用户[" + newUserId + "]";
                taskService.addComment(task.getId(), task.getProcessInstanceId(), "redirect", message);
            }
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
