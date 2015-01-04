package me.kafeitu.activiti.chapter18.esb.camel.activiti;

import org.activiti.camel.ActivitiProducer;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * 处理请假流程的Camel路由
 * @author: Henry Yan
 */
public class CamelWithActivitiLeaveRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // CamelBehaviorDefaultImpl
        /*from("activiti://leaveWithCamel:invokeCamel?copyVariablesToProperties=true&copyVariablesFromProperties=true")
            .log(LoggingLevel.INFO, "接收到消息：${property.days}")
            .log(LoggingLevel.INFO, "Camel路由中接收到流程ID：${property." + ActivitiProducer.PROCESS_ID_PROPERTY + "}")
            .choice()
                .when(simple("${property.days} > 3"))
                .setProperty("deptLeaderAudit", simple("true", Boolean.class))
                .otherwise()
                .setProperty("deptLeaderAudit", simple("false", Boolean.class));*/

        // CamelBehaviorCamelBodyImpl
        from("activiti://leaveWithCamel:invokeCamel?copyCamelBodyToBody=true&copyVariablesFromProperties=true")
            .log(LoggingLevel.INFO, "接收到消息BODY：${body}")
            .log(LoggingLevel.INFO, "Camel路由中接收到流程ID：${property." + ActivitiProducer.PROCESS_ID_PROPERTY + "}")
            .choice()
                .when(simple("${body[days]} > 3")) // 这里使用的是camelBody对象中的days属性
                .setProperty("deptLeaderAudit", simple("true", Boolean.class))
                .when(simple("${body[days]} <= 3"))
                .setProperty("deptLeaderAudit", simple("false", Boolean.class));

        // CamelBehaviorBodyAsMapImpl
        /*from("activiti://leaveWithCamel:invokeCamel?copyVariablesToBodyAsMap=true&copyVariablesFromProperties=true")
            .log(LoggingLevel.INFO, "接收到消息BODY：${body}")
            .log(LoggingLevel.INFO, "Camel路由中接收到流程ID：${property." + ActivitiProducer.PROCESS_ID_PROPERTY + "}")
            .choice()
                .when(simple("${body[days]} > 3"))
                .setProperty("deptLeaderAudit", simple("true", Boolean.class))
                .otherwise()
                .setProperty("deptLeaderAudit", simple("false", Boolean.class));*/

        // 默认输入输出参数
        /*from("activiti://leaveWithCamel:invokeCamel")
            .log(LoggingLevel.INFO, "接收到消息：${property.days}")
            .log(LoggingLevel.INFO, "Camel路由中接收到流程ID：${property." + ActivitiProducer.PROCESS_ID_PROPERTY + "}")
            .choice()
                .when(simple("${property.days} > 3"))
                    .setProperty("deptLeaderAudit", simple("true", Boolean.class))
                .when(simple("${property.days} <= 3"))
                    .setProperty("deptLeaderAudit", simple("false", Boolean.class))
            .endChoice().setBody().properties();*/

        // copyVariablesFromProperties
        /*from("activiti://leaveWithCamel:invokeCamel?copyVariablesFromProperties=true")
            .log(LoggingLevel.INFO, "接收到消息：${property.days}")
            .log(LoggingLevel.INFO, "Camel路由中接收到流程ID：${property." + ActivitiProducer.PROCESS_ID_PROPERTY + "}")
            .choice()
                .when(simple("${property.days} > 3"))
                .setProperty("deptLeaderAudit", simple("true", Boolean.class))
                .otherwise()
                .setProperty("deptLeaderAudit", simple("false", Boolean.class));*/

        // copyVariablesFromHeader
        /*from("activiti://leaveWithCamel:invokeCamel?copyVariablesFromHeader=true")
            .log(LoggingLevel.INFO, "接收到消息：${property.days}")
            .log(LoggingLevel.INFO, "Camel路由中接收到流程ID：${property." + ActivitiProducer.PROCESS_ID_PROPERTY + "}")
            .choice()
                .when(simple("${property.days} > 3"))
                .setHeader("deptLeaderAudit", simple("true", Boolean.class))
                .otherwise()
                .setHeader("deptLeaderAudit", simple("false", Boolean.class));*/
    }
}
