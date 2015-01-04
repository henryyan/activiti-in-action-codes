package me.kafeitu.activiti.chapter7.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

/**
 * 流程启动监听器
 *
 * @author henryyan
 */
public class ProcessStartExecutionListener implements ExecutionListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        execution.setVariable("setInStartListener", true);
        System.out.println(this.getClass().getSimpleName() + ", " + execution.getEventName());
    }

}
