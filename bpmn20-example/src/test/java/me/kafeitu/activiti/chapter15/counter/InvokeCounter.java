package me.kafeitu.activiti.chapter15.counter;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

/**
 * @author: Henry Yan
 */
public class InvokeCounter {
    public static void main(String[] args) {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.getInInterceptors().add(new LoggingInInterceptor());
        factory.getOutInterceptors().add(new LoggingOutInterceptor());
        factory.setServiceClass(Counter.class);
        factory.setAddress("http://localhost:12345/counter");
        Counter counter = (Counter) factory.create();
        counter.inc();
        counter.inc();
        counter.inc();
        counter.inc();
        counter.inc();

        System.out.println(counter.getCount());
    }
}
