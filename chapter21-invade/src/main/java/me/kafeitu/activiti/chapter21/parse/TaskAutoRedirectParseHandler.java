package me.kafeitu.activiti.chapter21.parse;

import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.UserTaskParseHandler;

/**
 * 利用BPMN解析拦截器为任务动态添加任务自动转办监听器
 * @author: Henry Yan
 */
public class TaskAutoRedirectParseHandler extends UserTaskParseHandler {

    protected void executeParse(BpmnParse bpmnParse, UserTask userTask) {
        super.executeParse(bpmnParse, userTask);

        // 实验后不能添加
        ActivitiListener listener = new ActivitiListener();
        listener.setEvent("create");
        listener.setImplementationType("class");
        listener.setImplementation("me.kafeitu.activiti.chapter21.listeners.TaskAutoRedirectListener");
        userTask.getTaskListeners().add(listener);
    }
}
