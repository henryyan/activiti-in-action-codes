package me.kafeitu.activiti.chapter7.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import java.io.Serializable;

/**
 * 流程结束监听器
 *
 * @author henryyan
 */
public class ProcessEndExecutionListener implements ExecutionListener, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        execution.setVariable("setInEndListener", true);
        System.out.println(this.getClass().getSimpleName() + ", " + execution.getEventName());
    }

}
