package cn.niceabc.activiti.chapter11;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.Deployment;
import org.activiti.spring.impl.test.SpringActivitiTestCase;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;

@ContextConfiguration("classpath:applicationContext.xml")
public class EventTest extends SpringActivitiTestCase {

    private static Logger log = LoggerFactory.getLogger(EventTest.class);

    @Test
    @Deployment(resources = {"event.bpmn20.xml"})
    public void test() {

        identityService.setAuthenticatedUserId("henryyan");

        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("start-event");

        log.debug("over.");
    }

    @Test
    @Deployment(resources = {"timer-start.bpmn20.xml"})
    public void testTimerStart() throws InterruptedException {

        identityService.setAuthenticatedUserId("henryyan");

        waitForJobExecutorToProcessAllJobs(70*1000, 30*1000);
        log.debug("over.");
    }
}
