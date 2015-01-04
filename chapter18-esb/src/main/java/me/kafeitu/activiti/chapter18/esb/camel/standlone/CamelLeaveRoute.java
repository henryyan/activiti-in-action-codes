package me.kafeitu.activiti.chapter18.esb.camel.standlone;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

/**
 * @author: Henry Yan
 */
public class CamelLeaveRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:start")
                .log(LoggingLevel.INFO, "接收到消息：${in.body}")
                .choice()
                    .when(simple("${in.body[days]} > 3"))
                    .log("用户${body[userId]} 请假天数超过 3 天")
                    .beanRef("leave", "setResult(true)")
                .endChoice();

        /*from("direct:forProperty")
                .log(LoggingLevel.INFO, "接收到消息-forProperty：${in.body}")
                .choice()
                    .when(simple("${body[days]} > 3"))
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {
                                System.setProperty("ok", "true");
                            }
                        })
                    .otherwise()
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {
                                System.setProperty("ok", "false");
                            }
                        });*/
    }
}
