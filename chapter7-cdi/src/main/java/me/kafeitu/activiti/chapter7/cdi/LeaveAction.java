package me.kafeitu.activiti.chapter7.cdi;

import org.activiti.engine.TaskService;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * 传统用法
 * @author: Henry Yan
 */
@Named
public class LeaveAction {

    @Inject
    TaskService taskService;

    /**
     * 完成任务
     * @return
     */
    public String completeTask(String taskId) {
        taskService.complete(taskId);
        return "/taskList.xhtml";
    }

}
