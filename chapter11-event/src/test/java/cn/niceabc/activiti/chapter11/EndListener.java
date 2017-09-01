package cn.niceabc.activiti.chapter11;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndListener implements ExecutionListener {

    private static Logger log = LoggerFactory.getLogger(EndListener.class);

    public void notify(DelegateExecution execution) throws Exception {

        log.debug("end.");
    }
}
