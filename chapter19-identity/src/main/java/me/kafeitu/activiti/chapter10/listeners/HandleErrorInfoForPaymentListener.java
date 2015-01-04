package me.kafeitu.activiti.chapter10.listeners;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

/**
 * @author henryyan
 */
public class HandleErrorInfoForPaymentListener implements ExecutionListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String currentActivityId = execution.getCurrentActivityId();
        if ("exclusivegateway-treasurerAudit".equals(currentActivityId)) {
            execution.setVariable("message", "财务审批未通过");
        } else if ("exclusivegateway-generalManagerAudit".equals(currentActivityId)) {
            execution.setVariable("message", "总经理审批未通过");
        }
    }

}
