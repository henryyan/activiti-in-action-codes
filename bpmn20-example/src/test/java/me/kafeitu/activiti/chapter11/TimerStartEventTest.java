package me.kafeitu.activiti.chapter11;

import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.JobQuery;
import org.activiti.engine.test.Deployment;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * classpath方式部署流程定义
 *
 * @author henryyan
 */
public class TimerStartEventTest extends PluggableActivitiTestCase {

    @Deployment(resources = "chapter11/timerEvent/timerStartEvent.bpmn")
    public void testTriggerAutomatic() throws Exception {
        // 部署之后引擎会自动创建一个定时启动事件的Job
        JobQuery jobQuery = managementService.createJobQuery();
        assertEquals(1, jobQuery.count());

        // 模拟时间5分钟之后
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        System.out.println(".... start ...." + sdf.format(new Date()));
        Context.getProcessEngineConfiguration().getClock().setCurrentTime(new Date(System.currentTimeMillis() + ((50 * 60 * 1000) + 5000)));
        waitForJobExecutorToProcessAllJobs(5000L, 1L);
        System.out.println(".... end ...." + sdf.format(new Date()));

        assertEquals(0, jobQuery.count());

        // 检查是否启动了流程实例
        long count = runtimeService.createProcessInstanceQuery().processDefinitionKey("timerStartEvent").count();
        assertEquals(1, count);
    }

    @Deployment(resources = "chapter11/timerEvent/timerStartEvent.bpmn")
    public void testTriggerManual() throws Exception {
        // 部署之后引擎会自动创建一个定时启动事件的Job
        JobQuery jobQuery = managementService.createJobQuery();
        assertEquals(1, jobQuery.count());

        // 手动触发作业的执行
        Job job = jobQuery.singleResult();
        managementService.executeJob(job.getId());

        assertEquals(0, jobQuery.count());

        // 检查是否启动了流程实例
        long count = runtimeService.createProcessInstanceQuery().processDefinitionKey("timerStartEvent").count();
        assertEquals(1, count);
    }

}
