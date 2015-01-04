package me.kafeitu.activiti.chapter18.esb.camel.activiti;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author: Henry Yan
 */
public class StartProcessByCamelRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("activiti://masterProcess:startSubProcess").to("activiti:camelSubProcess");
    }
}
