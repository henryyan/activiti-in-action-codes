package me.kafeitu.activiti.chapter10;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.activiti.spring.impl.test.SpringActivitiTestCase;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购买办公用品流程测试
 *
 * @author henryyan
 */
@ContextConfiguration("classpath:applicationContext-test-chapter10.xml")
public class MultiInstanceForSubprocessTest extends SpringActivitiTestCase {

    @Test
    @Deployment(resources = {"diagrams/chapter10/multiinstance-for-subprocess.bpmn"})
    public void testOne() throws Exception {

        identityService.setAuthenticatedUserId("henryyan");

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("multiinstance-for-subprocess");

        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskCandidateGroup("deptLeader").singleResult();
        taskService.claim(task.getId(), "bill");

        Map<String, Object> variables = new HashMap<String, Object>();
        List<String> users = Arrays.asList("user1", "user2", "user3");
        variables.put("users", users);
        taskService.complete(task.getId(), variables);

        for (String user : users) {
            long count = taskService.createTaskQuery().taskAssignee(user).count();
            assertEquals(1, count);
        }
    }
}
