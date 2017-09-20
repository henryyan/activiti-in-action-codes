package cn.niceabc.activiti.spring;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ScriptTest.MyApp.class)
public class ScriptTest {

    @Autowired
    private IdentityService identityService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @Test
    public void test() {

        //create process definition
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery()
                .processDefinitionKey("script")
                .singleResult();
        Assert.assertNotNull(processDefinition);
        Assert.assertEquals("script", processDefinition.getKey());



        //start a process instance
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("script");
        Assert.assertNotNull(processInstance);





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

    @Configuration
    @ComponentScan
    @EnableAutoConfiguration
    public static class MyApp {

    }
}
