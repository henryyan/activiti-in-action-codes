package me.kafeitu.activiti.chapter9.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

/**
 * 请假会签任务监听器，当会签任务完成时统计同意的数量
 *
 * @author henryyan
 */
@Component
public class LeaveCounterSignCompleteListener implements TaskListener {

    private static final long serialVersionUID = 1L;

    @Override
    public void notify(DelegateTask delegateTask) {
        String approved = (String) delegateTask.getVariable("approved");
        if (approved.equals("true")) {
            Long agreeCounter = (Long) delegateTask.getVariable("approvedCounter");
            delegateTask.setVariable("approvedCounter", agreeCounter + 1);
        }
    }

}
