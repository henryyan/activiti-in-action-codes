package org.activiti.designer.test;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import java.io.FileInputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

public class ProcessTestExecutionListenerInTask {

	private String filename = "/Users/henryyan/work/books/activiti-in-action-codes/chapter17-jpa/src/main/resources/executionListenerInTask.bpmn";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void startProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		repositoryService.createDeployment().addInputStream("executionListenerInTask.bpmn20.xml",
				new FileInputStream(filename)).deploy();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("name", "Activiti");
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("executionListenerInTask", variableMap);
		assertNotNull(processInstance.getId());
		System.out.println("id " + processInstance.getId() + " "
				+ processInstance.getProcessDefinitionId());
	}
}