package me.kafeitu.activiti.chapter7.cdi.event;

import org.activiti.cdi.BusinessProcessEvent;
import org.activiti.cdi.annotation.event.CreateTask;

import javax.enterprise.event.Observes;

/**
 * CDI模块的事件监听处理器
 * @author: Henry Yan
 */
public class ActivitiCdiEventListener {

    public void onStartActivityService1(@Observes @CreateTask("deptLeaderAudit") BusinessProcessEvent businessProcessEvent) {
        System.out.println("捕获到【部门领导审批】的事件：" + businessProcessEvent);
    }

}
