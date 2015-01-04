package me.kafeitu.activiti.chapter14;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsynchronousExecutor implements JavaDelegate {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        logger.info(execution.getCurrentActivityName() + "---开始执行任务");
        Long sleepSeconds = (Long) execution.getVariable("sleepSeconds");
        Thread.sleep(sleepSeconds * 1000l);
        logger.info(execution.getCurrentActivityName() + "---任务完成");
    }

}
