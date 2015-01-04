package me.kafeitu.activiti.chapter15;

import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.Deployment;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试请假Webservice在流程中的使用
 * 请先运行：LeaveWebserviceUtil#main发布Webservice
 * @author: Henry Yan
 */
public class WebserviceLeaveTest extends PluggableActivitiTestCase {

    @Deployment(resources = "chapter15/webservice.bpmn")
    public void testOne() {
        Map<String, Object> variableMap = new HashMap<String, Object>();
        variableMap.put("startDate", "2013-01-01 09:00");
        variableMap.put("endDate", "2013-01-05 09:00");
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("webservice", variableMap);
        Object responseValue = runtimeService.getVariable(processInstance.getProcessInstanceId(), "webserviceResponse");
        assertEquals(true, responseValue);
    }

}
