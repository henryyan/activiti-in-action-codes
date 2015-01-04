package me.kafeitu.activiti.chapter7.cdi;

import me.kafeitu.activiti.chapter7.cdi.bean.SystemUser;
import org.activiti.cdi.annotation.CompleteTask;
import org.activiti.cdi.annotation.ProcessVariable;
import org.activiti.cdi.annotation.StartProcess;
import org.activiti.engine.IdentityService;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * 使用Activiti CDI注解的Producer
 * @author: Henry Yan
 */
@Named
public class ActivitiCdiAction {

    @ProcessVariable String orgId;

    @Inject
    IdentityService identityService;

    @Inject
    private SystemUser systemUser;

    /**
     * 启动请假流程
     */
    @StartProcess(value = "leave")
    public String startLeaveProcess(String orgId) {
        identityService.setAuthenticatedUserId(systemUser.getUserId());

        this.orgId = orgId;
        System.out.println("流程【请假】启动" + orgId);
        return "taskList.xhtml";
    }

    /**
     *
     */
    @CompleteTask(endConversation = true)
    public String completeTask() {
        System.out.println("执行任务完成");
        return "/taskList.xhtml";
    }

}
