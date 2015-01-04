package me.kafeitu.activiti.chapter2;

import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SayHelloToLeaveTest {

    @Test
    public void testStartProcess() throws Exception {
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .buildProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        String bpmnFileName = "me/kafeitu/activiti/helloworld/SayHelloToLeave.bpmn";
        repositoryService
                .createDeployment()
                .addInputStream(
                        "SayHelloToLeave.bpmn",
                        this.getClass().getClassLoader()
                                .getResourceAsStream(bpmnFileName)).deploy();

        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery().singleResult();
        assertEquals("SayHelloToLeave", processDefinition.getKey());

        RuntimeService runtimeService = processEngine.getRuntimeService();

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("applyUser", "employee1");
        variables.put("days", 3);

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                "SayHelloToLeave", variables);
        assertNotNull(processInstance);
        System.out.println("pid=" + processInstance.getId() + ", pdid="
                + processInstance.getProcessDefinitionId());

        TaskService taskService = processEngine.getTaskService();
        Task taskOfDeptLeader = taskService.createTaskQuery()
                .taskCandidateGroup("deptLeader").singleResult();
        assertNotNull(taskOfDeptLeader);
        assertEquals("领导审批", taskOfDeptLeader.getName());

        taskService.claim(taskOfDeptLeader.getId(), "leaderUser");
        variables = new HashMap<String, Object>();
        variables.put("approved", true);
        taskService.complete(taskOfDeptLeader.getId(), variables);

        taskOfDeptLeader = taskService.createTaskQuery()
                .taskCandidateGroup("deptLeader").singleResult();
        assertNull(taskOfDeptLeader);

        HistoryService historyService = processEngine.getHistoryService();
        long count = historyService.createHistoricProcessInstanceQuery().finished()
                .count();
        assertEquals(1, count);
    }
}