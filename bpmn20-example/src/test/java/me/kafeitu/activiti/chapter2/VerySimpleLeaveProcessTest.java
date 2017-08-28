package me.kafeitu.activiti.chapter2;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class VerySimpleLeaveProcessTest {

    private static Logger log = LoggerFactory.getLogger(VerySimpleLeaveProcessTest.class);

    @Test
    public void testStartProcess() throws Exception {
        // 创建流程引擎，使用内存数据库
        ProcessEngine processEngine = ProcessEngineConfiguration
                .createStandaloneInMemProcessEngineConfiguration()
                .buildProcessEngine();
        log.debug("engine name: {}", processEngine.getName());
        Assert.assertEquals("default", processEngine.getName());

        ProcessEngine engine = ProcessEngineConfiguration
                .createProcessEngineConfigurationFromResourceDefault()
                .setProcessEngineName("engineDefault")
                .buildProcessEngine();
        log.debug("engine name: {}", engine.getName());
        Assert.assertEquals("engineDefault", engine.getName());

        // 部署流程定义文件
        RepositoryService repositoryService = processEngine.getRepositoryService();
        String processFileName = "me/kafeitu/activiti/helloworld/sayhelloleave.bpmn";
        repositoryService.createDeployment().addClasspathResource(processFileName)
                .deploy();

        // 验证已部署流程定义
        ProcessDefinition processDefinition = repositoryService
                .createProcessDefinitionQuery().singleResult();
        assertEquals("leavesayhello", processDefinition.getKey());

        // 启动流程并返回流程实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("leavesayhello");
        assertNotNull(processInstance);
        System.out.println("pid=" + processInstance.getId() + ", pdid="
                + processInstance.getProcessDefinitionId());
    }
}