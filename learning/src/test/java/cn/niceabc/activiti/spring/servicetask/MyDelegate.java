package cn.niceabc.activiti.spring.servicetask;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyDelegate implements JavaDelegate{
    private static Logger log = LoggerFactory.getLogger(MyDelegate.class);
    public void execute(DelegateExecution execution) throws Exception {
        log.debug("in MyDeletgate");
    }
}
