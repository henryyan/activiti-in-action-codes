package me.kafeitu.activiti.chapter7.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TaskCompleteListener implements TaskListener{
    private static Logger log = LoggerFactory.getLogger(TaskCompleteListener.class);

    @Override
    public void notify(DelegateTask delegateTask) {
        log.debug("event name: {}", delegateTask.getEventName());
    }
}
