package me.kafeitu.activiti.chapter21.command;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;

/**
 * 自定义命令实现节点跳转
 */
public class JumpActivityCmd implements Command<ExecutionEntity> {

    private String activityId;
    private String processInstanceId;
    private String jumpOrigin;

    public JumpActivityCmd(String processInstanceId, String activityId) {
        this(processInstanceId, activityId, "jump");
    }

    public JumpActivityCmd(String processInstanceId, String activityId, String jumpOrigin) {
        this.activityId = activityId;
        this.processInstanceId = processInstanceId;
        this.jumpOrigin = jumpOrigin;
    }

    public ExecutionEntity execute(CommandContext commandContext) {

        // 查询活动的分支
        ExecutionEntity executionEntity = commandContext.getExecutionEntityManager().findExecutionById(processInstanceId);

        // 销毁当前分支
        executionEntity.destroyScope(jumpOrigin);

        // 从流程定义中查询目标Activity
        ProcessDefinitionImpl processDefinition = executionEntity.getProcessDefinition();
        ActivityImpl activity = processDefinition.findActivity(activityId);

        // 节点跳转到目标活动
        executionEntity.executeActivity(activity);

        return executionEntity;
    }
}