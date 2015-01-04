package me.kafeitu.activiti.chapter18.esb.mule;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.client.MuleClient;
import org.mule.api.registry.MuleRegistry;
import org.mule.api.transport.PropertyScope;
import org.mule.client.DefaultLocalMuleClient;
import org.mule.context.DefaultMuleContextFactory;
import org.mule.module.activiti.action.model.ProcessInstance;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * @author: Henry Yan
 */
public class ActivitiWithMuleTest {

    @Test
    public void testMuleMaster() throws Exception {
        MuleContext muleContext = new DefaultMuleContextFactory().createMuleContext("mule/mule-master/mule-master.xml");
        muleContext.start();

        MuleRegistry registry = muleContext.getRegistry();
        RepositoryService repositoryService = (RepositoryService) registry.get("repositoryService");
        repositoryService.createDeployment().addClasspathResource("mule/leaveWithMule.bpmn").deploy();

        MuleClient muleClient = new DefaultLocalMuleClient(muleContext);
        DefaultMuleMessage message = new DefaultMuleMessage("", muleContext);
        Map<String, Object> variableMap = new HashMap<String, Object>();
        variableMap.put("days", 5);
        variableMap.put("processDefinitionKey", "leaveWithMule");
        message.setProperty("createProcessParameters", variableMap , PropertyScope.OUTBOUND);
        MuleMessage responseMessage = muleClient.send("vm://startLeaveProcess", message);
        ProcessInstance processInstance = (ProcessInstance) responseMessage.getPayload();
        assertFalse(processInstance.isEnded());

        TaskService taskService = registry.get("taskService");
        Task task = taskService.createTaskQuery().singleResult();
        assertNotNull(task);
        assertEquals("部门经理审批", task.getName());

        muleContext.stop();
        muleContext.dispose();
    }

    @Test
    public void testSpringMaster() throws Exception {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("mule/spring-master/spring-master.xml");

        MuleContext mc = (MuleContext) ctx.getBean("_muleContext");
        mc.start();

        RepositoryService repositoryService = (RepositoryService) ctx.getBean("repositoryService");
        repositoryService.createDeployment().addClasspathResource("mule/leaveWithMule.bpmn").deploy();

        RuntimeService runtimeService = (RuntimeService) ctx.getBean("runtimeService");

        Map<String, Object> variableMap = new HashMap<String, Object>();
        variableMap.put("days", 5);
        runtimeService.startProcessInstanceByKey("leaveWithMule", variableMap);

        TaskService taskService = (TaskService) ctx.getBean("taskService");
        Task task = taskService.createTaskQuery().singleResult();
        assertNotNull(task);
        assertEquals("部门经理审批", task.getName());

        mc.stop();
    }

}
