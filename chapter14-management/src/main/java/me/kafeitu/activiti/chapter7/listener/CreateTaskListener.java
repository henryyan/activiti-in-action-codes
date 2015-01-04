package me.kafeitu.activiti.chapter7.listener;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.TaskListener;

/**
 * 创建用户任务监听器
 *
 * @author henryyan
 */
public class CreateTaskListener implements TaskListener {

    private static final long serialVersionUID = 1L;

    private Expression content;

    private Expression task;

    @Override
    public void notify(DelegateTask delegateTask) {
        System.out.println(task.getValue(delegateTask));
        delegateTask.setVariable("setInTaskCreate", delegateTask.getEventName() + ", " + content.getValue(delegateTask));
        System.out.println(delegateTask.getEventName() + "，任务分配给：" + delegateTask.getAssignee());
        delegateTask.setAssignee("jenny");
    }

  /*
   * 这里的setter可以省略，引擎会使用反射设置注入字段 
   * public void setContent(Expression content) {
   * this.content = content; }
   * 
   * public void setTask(Expression task) { this.task = task; }
   */

}
