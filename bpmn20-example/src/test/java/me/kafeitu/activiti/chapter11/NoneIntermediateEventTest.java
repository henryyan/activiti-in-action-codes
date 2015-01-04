package me.kafeitu.activiti.chapter11;

import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.Deployment;

/**
 * @author henryyan
 */
public class NoneIntermediateEventTest extends PluggableActivitiTestCase {

    /**
     * 空中间抛出事件
     */
    @Deployment(resources = "chapter11/intermediateEvent/noneIntermediateEvent.bpmn")
    public void testStartMessageEvent() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("noneIntermediateEvent");
        assertNotNull(processInstance);
    }

}
