package me.kafeitu.activiti.chapter9;

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
 * 请假会签测试
 *
 * @author henryyan
 */
@ContextConfiguration("classpath:applicationContext-test-chapter9.xml")
public class LeaveCoutersignTest extends SpringActivitiTestCase {

    /**
     * 全部通过
     */
    @Test
    @Deployment(resources = {"diagrams/chapter9/leave-countersign.bpmn"})
    public void testAllApproved() throws Exception {
        Map<String, Object> variables = new HashMap<String, Object>();
        List<String> users = Arrays.asList("groupLeader", "deptLeader", "hr");
        variables.put("users", users);
        identityService.setAuthenticatedUserId("henryyan");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave-countersign", variables);
        for (String user : users) {
            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee(user).singleResult();
            Map<String, Object> taskVariables = new HashMap<String, Object>();
            taskVariables.put("approved", "true");
            taskService.complete(task.getId(), taskVariables);
        }

        Task task = taskService.createTaskQuery().taskAssignee("henryyan").singleResult();
        assertNotNull(task);
        assertEquals("销假", task.getName());
    }

    /**
     * 部分通过
     */
    @Test
    @Deployment(resources = {"diagrams/chapter9/leave-countersign.bpmn"})
    public void testNotAllApproved() throws Exception {
        Map<String, Object> variables = new HashMap<String, Object>();
        List<String> users = Arrays.asList("groupLeader", "deptLeader", "hr");
        variables.put("users", users);
        identityService.setAuthenticatedUserId("henryyan");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave-countersign", variables);
        for (String user : users) {
            Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).taskAssignee(user).singleResult();
            Map<String, Object> taskVariables = new HashMap<String, Object>();
            taskVariables.put("approved", "false");
            taskService.complete(task.getId(), taskVariables);
        }

        Task task = taskService.createTaskQuery().taskAssignee("henryyan").singleResult();
        assertNotNull(task);
        assertEquals("调整申请", task.getName());
    }

}
