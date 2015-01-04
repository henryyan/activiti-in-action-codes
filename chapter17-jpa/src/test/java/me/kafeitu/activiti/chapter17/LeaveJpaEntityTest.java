package me.kafeitu.activiti.chapter17;

import me.kafeitu.activiti.chapter17.entity.LeaveJpaEntity;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.test.AbstractActivitiTestCase;
import org.activiti.engine.impl.variable.EntityManagerSession;
import org.activiti.engine.impl.variable.EntityManagerSessionFactory;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Activiti的JPA实体映射测试
 *
 * @author: Henry Yan
 */
public class LeaveJpaEntityTest extends AbstractActivitiTestCase {

    protected static ProcessEngine cachedProcessEngine;
    private static EntityManagerFactory entityManagerFactory;

    protected void initializeProcessEngine() {
        if (cachedProcessEngine == null) {
            ProcessEngineConfigurationImpl processEngineConfiguration = (ProcessEngineConfigurationImpl) ProcessEngineConfiguration
                    .createProcessEngineConfigurationFromResource("activiti.jpa.cfg.xml");

            cachedProcessEngine = processEngineConfiguration.buildProcessEngine();

            EntityManagerSessionFactory entityManagerSessionFactory = (EntityManagerSessionFactory) processEngineConfiguration
                    .getSessionFactories()
                    .get(EntityManagerSession.class);

            entityManagerFactory = entityManagerSessionFactory.getEntityManagerFactory();
        }
        processEngine = cachedProcessEngine;
    }

    @Deployment(resources = "chapter17/leave-jpa.bpmn")
    public void testSaveEntity() {
        LeaveJpaEntity leave = new LeaveJpaEntity();
        leave.setReason("测试");
        EntityManager manager = entityManagerFactory.createEntityManager();
        manager.getTransaction().begin();

        manager.persist(leave);

        manager.flush();
        manager.getTransaction().commit();
        manager.close();
    }

    @Deployment(resources = "chapter17/leave-jpa.bpmn")
    public void testAll() {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("leave-jpa").singleResult();

        // 设置当前用户
        String currentUserId = "henryyan";
        identityService.setAuthenticatedUserId(currentUserId);

        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceById(processDefinition.getId());
        assertNotNull(processInstance);

        Object varLeave = runtimeService.getVariable(processInstance.getId(), "leave");
        assertNotNull(varLeave);

        // 部门领导审批通过
        Task deptLeaderTask = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
        Map<String, String> variablesString = new HashMap<String, String>();
        variablesString.put("deptLeaderApproved", "true");
        formService.submitTaskFormData(deptLeaderTask.getId(), variablesString);

        // 人事审批通过
        Task hrTask = taskService.createTaskQuery().taskCandidateGroup("hr").singleResult();
        variablesString = new HashMap<String, String>();
        variablesString.put("hrApproved", "true");
        formService.submitTaskFormData(hrTask.getId(), variablesString);

        // 销假（根据申请人的用户ID读取）
        Task reportBackTask = taskService.createTaskQuery().taskAssignee(currentUserId).singleResult();
        variablesString = new HashMap<String, String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        variablesString.put("reportBackDate", sdf.format(ca.getTime()));
        formService.submitTaskFormData(reportBackTask.getId(), variablesString);
    }

}
