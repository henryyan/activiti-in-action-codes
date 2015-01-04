package me.kafeitu.activiti.chapter11.listener;

import org.activiti.engine.delegate.BpmnError;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * 代码方式抛出异常
 *
 * @author henryyan
 */
public class ThrowErrorManaualService implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        if (execution.getVariable("pass") == null) {
            throw new BpmnError("AIA_ERROR_99");
        }
    }

}
