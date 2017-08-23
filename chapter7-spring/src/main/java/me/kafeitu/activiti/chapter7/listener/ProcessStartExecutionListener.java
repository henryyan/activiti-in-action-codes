package me.kafeitu.activiti.chapter7.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 流程启动监听器
 *
 * @author henryyan
 */
public class ProcessStartExecutionListener implements ExecutionListener {
    private static Logger log = LoggerFactory.getLogger(ProcessStartExecutionListener.class);

    private static final long serialVersionUID = 1L;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        execution.setVariable("setInStartListener", true);
        //System.out.println(this.getClass().getName() + ", " + execution.getEventName());
        log.debug("event name: {}", execution.getEventName());
    }

}
