package cn.niceabc.chapter5;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserAndGroupInUserTaskTest {
    private static Logger log = LoggerFactory.getLogger(UserAndGroupInUserTaskTest.class);

    @Rule
    // 默认加载activiti.cfg.xml
    public ActivitiRule activitiRule = new ActivitiRule();
    
    private IdentityService identityService;
    private RuntimeService runtimeService;
    private TaskService taskService;
    private RepositoryService repositoryService;

    @Test
    @Deployment(resources = {"chapter5/userAndGroupInUserTask.bpmn"})
    public void testInUserTask() {

        ProcessDefinition definition_1 = repositoryService.createProcessDefinitionQuery().singleResult();
        Assert.assertNotNull(definition_1);
        log.debug("definition id: {}", definition_1.getId());

        org.activiti.engine.repository.Deployment deployment_1 = repositoryService.createDeploymentQuery().singleResult();
        Assert.assertNotNull(deployment_1);
        log.debug("deployment id: {}", deployment_1.getId());

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("userAndGroupInUserTask");
        Assert.assertNotNull(processInstance);

        Task task = taskService.createTaskQuery().taskCandidateUser("tom").singleResult();

        log.debug("task.id: {}", task.getId());

        taskService.claim(task.getId(), "tom");
        taskService.complete(task.getId());

    }
    
    @Before
    public void before() {
        identityService = activitiRule.getIdentityService();
        runtimeService = activitiRule.getRuntimeService();
        taskService = activitiRule.getTaskService();
        repositoryService = activitiRule.getRepositoryService();

        /*identityService.deleteMembership("tom", "deptLeader");
        identityService.deleteGroup("deptLeader");
        identityService.deleteUser("tom");*/

        //group

        Group group =identityService.newGroup("deptLeader");
        group.setName("deptLeader");
        group.setType("assignment");
        identityService.saveGroup(group);

        Group group1 = identityService.createGroupQuery().groupId("deptLeader").singleResult();
        Assert.assertNotNull(group1);
        Assert.assertEquals("deptLeader", group1.getName());
        Assert.assertEquals("assignment", group1.getType());

        //user

        User user = identityService.newUser("tom");
        user.setFirstName("tom");
        user.setLastName("cruise");
        user.setEmail("tom.cruise@email.com");
        identityService.saveUser(user);

        User tom = identityService.createUserQuery().userId("tom").singleResult();
        Assert.assertNotNull(tom);
        Assert.assertEquals("cruise", tom.getLastName());


        // membership
        identityService.createMembership("tom", "deptLeader");

        User tomInGroup = identityService
                .createUserQuery()
                .memberOfGroup("deptLeader")
                .singleResult();
        Assert.assertNotNull(tomInGroup);
        Assert.assertEquals("tom", tomInGroup.getId());

        Group groupContainsTom = identityService
                .createGroupQuery()
                .groupMember("tom")
                .singleResult();
        Assert.assertNotNull(groupContainsTom);
        Assert.assertEquals("deptLeader", groupContainsTom.getId());
        
    }

    @After
    public void after() {
        identityService.deleteMembership("tom", "deptLeader");
        identityService.deleteGroup("deptLeader");
        identityService.deleteUser("tom");

    }
}
