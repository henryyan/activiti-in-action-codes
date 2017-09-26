package cn.niceabc.activiti.spring.servicetask;

import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyActivityBehavior implements ActivityBehavior {
    private static Logger log = LoggerFactory.getLogger(MyActivityBehavior.class);
    public void execute(ActivityExecution execution) throws Exception {
        log.debug("in MyActivityBehavior");
    }
}
