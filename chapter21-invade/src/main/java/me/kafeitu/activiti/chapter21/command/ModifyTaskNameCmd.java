package me.kafeitu.activiti.chapter21.command;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntityManager;
import org.activiti.engine.task.Task;

/**
 * 修改任务名称命令
 * @author: Henry Yan
 */
public class ModifyTaskNameCmd implements Command<Task> {

    protected String taskId;
    protected String taskName;

    public ModifyTaskNameCmd(String taskId, String taskName) {
        this.taskId = taskId;
        this.taskName = taskName;
    }

    @Override
    public Task execute(CommandContext commandContext) {
        TaskEntityManager taskEntityManager = commandContext.getTaskEntityManager();
        TaskEntity task = taskEntityManager.findTaskById(taskId);
        task.setName(taskName);
        task.update();
        return task;
    }
}
