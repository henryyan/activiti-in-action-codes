package cn.niceabc.activiti.chapter11;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StartListener implements ExecutionListener {

    private static Logger log = LoggerFactory.getLogger(StartListener.class);

    public void notify(DelegateExecution execution) throws Exception {

        log.debug("start");
    }
}
