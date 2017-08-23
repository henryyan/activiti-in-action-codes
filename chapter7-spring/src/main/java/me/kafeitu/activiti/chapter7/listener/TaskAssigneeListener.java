package me.kafeitu.activiti.chapter7.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class TaskAssigneeListener implements TaskListener, Serializable {
    private static Logger log = LoggerFactory.getLogger(TaskAssigneeListener.class);

    private static final long serialVersionUID = 1L;

    @Override
    public void notify(DelegateTask delegateTask) {

        log.debug("event name: {}, assignee is: {}", delegateTask.getEventName(), delegateTask.getAssignee());
        //System.out.println(delegateTask.getEventName() + "，任务分配给：" + delegateTask.getAssignee());
    }

}
