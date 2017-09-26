package cn.niceabc.activiti.spring.servicetask;

import cn.niceabc.activiti.spring.MyApp;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MyApp.class)
public class ServicetaskTest {

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
                .processDefinitionKey("servicetask")
                .singleResult();
        Assert.assertNotNull(processDefinition);
        Assert.assertEquals("servicetask", processDefinition.getKey());



        //start a process instance
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("servicetask");
        Assert.assertNotNull(processInstance);





        //check that the task has been completed.
        ProcessInstance processInstance1_completed = runtimeService
                .createProcessInstanceQuery()
                .processInstanceId(processInstance.getId())
                .singleResult();
        // if the processInstance is null then the instance was completed.
        Assert.assertNull(processInstance1_completed);
        //这里为什么没有结束呢
        //cn.niceabc.activiti.spring.ScriptTest.test()却结束了

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

}
