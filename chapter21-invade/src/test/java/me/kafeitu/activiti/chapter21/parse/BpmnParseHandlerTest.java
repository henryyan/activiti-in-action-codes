package me.kafeitu.activiti.chapter21.parse;

import me.kafeitu.activiti.base.AbstractTest;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.ReadOnlyProcessDefinition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author: Henry Yan
 */
public class BpmnParseHandlerTest  extends AbstractTest {

    public BpmnParseHandlerTest() {
        super.activitiRule = new ActivitiRule("chapter21/activiti.cfg.chapter21.parse.xml");
    }

    @Test
    @Deployment(resources = "chapter6/dynamic-form/leave.bpmn")
    public void testParseHandler() throws Exception {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        assertEquals("请假流程-动态表单-被PRE解析器修改", processDefinition.getName());
        assertEquals("leave-modified-by-post-parse-handler", processDefinition.getKey());

        RepositoryServiceImpl repositoryServiceImpl = (RepositoryServiceImpl) repositoryService;
        ReadOnlyProcessDefinition deployedProcessDefinition = repositoryServiceImpl
                .getDeployedProcessDefinition(processDefinition.getId());

        ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) deployedProcessDefinition;
        List<ActivityImpl> activitiList = processDefinitionEntity.getActivities();//获得当前任务的所有节点
        for (ActivityImpl activity : activitiList) {
            System.out.println("Activity Name: " + activity.getProperty("name") + ", async=" + activity.isAsync());
        }

        InputStream processBpmn = activitiRule.getRepositoryService()
                .getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getResourceName());
        String userHomeDir = getClass().getResource("/").getFile();
        FileUtils.copyInputStreamToFile(processBpmn,
                new File(userHomeDir + "/leave.bpmn20.xml"));
    }
}
