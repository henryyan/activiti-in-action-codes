package me.kafeitu.activiti.web.base;

import me.kafeitu.activiti.chapter5.util.ActivitiUtils;
import org.activiti.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象Controller，提供一些基础的方法、属性
 *
 * @author henryyan
 */
public abstract class AbstractController {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected ProcessEngine processEngine = null;
    protected RepositoryService repositoryService;
    protected RuntimeService runtimeService;
    protected TaskService taskService;
    protected HistoryService historyService;
    protected IdentityService identityService;
    protected ManagementService managementService;
    protected FormService formService;

    public AbstractController() {
        super();
        processEngine = ActivitiUtils.getProcessEngine();
        repositoryService = processEngine.getRepositoryService();
        runtimeService = processEngine.getRuntimeService();
        taskService = processEngine.getTaskService();
        historyService = processEngine.getHistoryService();
        identityService = processEngine.getIdentityService();
        managementService = processEngine.getManagementService();
        formService = processEngine.getFormService();
    }

}
