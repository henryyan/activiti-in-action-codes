package me.kafeitu.activiti.chapter21.pvm;

import org.activiti.engine.impl.pvm.ProcessDefinitionBuilder;
import org.activiti.engine.impl.pvm.PvmProcessDefinition;
import org.activiti.engine.impl.pvm.PvmProcessInstance;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.junit.Test;

import java.util.List;

/**
 * PVM学习辅助测试类
 * @author: Henry Yan
 */
public class PvmTest {
    @Test
    public void helloPvm() {
        // 创建开始活动
        ActivityBehavior startBehavior = new ActivityBehavior() {
            @Override
            public void execute(ActivityExecution execution) throws Exception {
                System.out.println("处理开始节点");
                List<PvmTransition> transitions = execution.getActivity().getOutgoingTransitions();
                execution.take(transitions.get(0));
            }
        };
        // 创建用户任务活动
        ActivityBehavior userTaskBehavior = new ActivityBehavior() {
            @Override
            public void execute(ActivityExecution execution) throws Exception {
                System.out.println("处理用户任务");
                List<PvmTransition> transitions = execution.getActivity().getOutgoingTransitions();
                execution.take(transitions.get(0));
            }
        };
        // 创建结束活动
        ActivityBehavior endBehavior = new ActivityBehavior() {
            @Override
            public void execute(ActivityExecution execution) throws Exception {
                System.out.println("处理结束节点");
            }
        };
        // 创建流程定义构造器
        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilder();
        builder.createActivity("start") // 设置活动的名称
                .initial() // 标记为起始节点
                .behavior(startBehavior) // 活动处理器
                .transition("userTask") // 下一个执行的活动
                .endActivity(); // 结束当前活动

        // 创建用户任务
        builder.createActivity("userTask").behavior(userTaskBehavior)
                .transition("end").endActivity();
        // 创建结束节点
        builder.createActivity("end").behavior(endBehavior).endActivity();

        // 构建PVM流程定义
        PvmProcessDefinition pvmProcessDefinition = builder.buildProcessDefinition();
        // 创建PVM流程实例
        PvmProcessInstance processInstance = pvmProcessDefinition.createProcessInstance();
        // 启动流程
        processInstance.start();
    }

    @Test
    public void pvmSignalTask() {
        // 创建流程定义构造器
        ProcessDefinitionBuilder builder = new ProcessDefinitionBuilder();
        builder.createActivity("start") // 设置活动的名称
                .initial() // 标记为起始节点
                .behavior(new Automatic()) // 活动处理器
                .transition("userTask") // 下一个执行的活动
                .endActivity(); // 结束当前活动

        // 创建用户任务
        builder.createActivity("userTask").behavior(new WaitState())
                .transition("end").endActivity();
        // 创建结束节点
        builder.createActivity("end").behavior(new Automatic()).endActivity();

        // 构建PVM流程定义
        PvmProcessDefinition pvmProcessDefinition = builder.buildProcessDefinition();
        // 创建PVM流程实例
        PvmProcessInstance processInstance = pvmProcessDefinition.createProcessInstance();
        // 启动流程
        processInstance.start();

        // 触发等待的活动(userTask)
        //processInstance.signal("userTask", null);
    }

}