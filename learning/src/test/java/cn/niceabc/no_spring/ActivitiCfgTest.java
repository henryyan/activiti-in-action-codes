package cn.niceabc.no_spring;

import org.activiti.engine.*;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ActivitiCfgTest {

    private ProcessEngine processEngine;
    private IdentityService identityService;
    private RepositoryService repositoryService;
    private RuntimeService runtimeService;
    private TaskService taskService;

    @Test
    public void test() {

        //deploy
        repositoryService.createDeployment()
                .addClasspathResource("leave.bpmn20.xml")
                .deploy();



        //create process definition
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .singleResult();
        Assert.assertNotNull(processDefinition);
        Assert.assertEquals("leave", processDefinition.getKey());



        //start a process instance
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("leave");
        Assert.assertNotNull(processInstance);


        //get a task, complete it.
        //Task task_manager = taskService.createTaskQuery().taskCandidateGroup("manager").singleResult();
        Task task_manager = taskService.createTaskQuery().taskCandidateUser("tom").singleResult();
        Assert.assertNotNull(task_manager);
        Assert.assertEquals("verify-by-manager", task_manager.getName());
        taskService.complete(task_manager.getId());


        //check that the task has been completed.
        ProcessInstance processInstance1_uncompleted = runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();
        // if the processInstance is null then the instance was completed.
        Assert.assertNotNull(processInstance1_uncompleted);


        //get a task, complete it.
        //Task task_hr = taskService.createTaskQuery().taskCandidateGroup("hr").singleResult();
        Task task_hr = taskService.createTaskQuery().taskCandidateUser("john").singleResult();
        Assert.assertNotNull(task_hr);
        Assert.assertEquals("verify-by-hr", task_hr.getName());
        taskService.complete(task_hr.getId());


        //check that the task has been completed.
        ProcessInstance processInstance1_completed = runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();
        // if the processInstance is null then the instance was completed.
        Assert.assertNull(processInstance1_completed);

    }

    @Before
    public void before() {
        // use activiti.cfg.xml
        processEngine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResourceDefault()
                .buildProcessEngine();
        identityService = processEngine.getIdentityService();
        repositoryService = processEngine.getRepositoryService();
        runtimeService = processEngine.getRuntimeService();
        taskService = processEngine.getTaskService();

        //group

        Group group_manager =identityService.newGroup("manager");
        group_manager.setName("manager");
        identityService.saveGroup(group_manager);

        Group group_hr =identityService.newGroup("hr");
        group_hr.setName("hr");
        identityService.saveGroup(group_hr);


        //user

        User user = identityService.newUser("tom");
        user.setFirstName("tom");
        user.setLastName("cruise");
        user.setEmail("tom.cruise@email.com");
        identityService.saveUser(user);

        User user_john = identityService.newUser("john");
        user_john.setFirstName("john");
        user_john.setLastName("snow");
        user_john.setEmail("john.snow@email.com");
        identityService.saveUser(user_john);

        User user_max = identityService.newUser("max");
        user_max.setFirstName("max");
        user_max.setLastName("ma");
        user_max.setEmail("max.ma@email.com");
        identityService.saveUser(user_max);




        // membership
        identityService.createMembership("tom", "manager");
        identityService.createMembership("john", "hr");

    }

    @After
    public void after() {
        /*identityService.deleteMembership("tom", "manager");
        identityService.deleteGroup("manager");
        identityService.deleteUser("tom");*/

    }
}
