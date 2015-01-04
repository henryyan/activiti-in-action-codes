package me.kafeitu.activiti.chapter18.esb.camel;

import me.kafeitu.activiti.chapter18.esb.camel.standlone.CamelLeaveBean;
import me.kafeitu.activiti.chapter18.esb.camel.standlone.CamelLeaveRoute;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.util.jndi.JndiContext;
import org.junit.Test;

import java.util.Collections;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Camel简单入门示例
 *
 * @author: Henry Yan
 */
public class CamelTest {

    /**
     * Camel简单入门
     * @throws Exception
     */
    @Test
    public void testCamelForBean() throws Exception {
        JndiContext context = new JndiContext();
        CamelLeaveBean leave = new CamelLeaveBean();
        context.bind("leave", leave);

        CamelContext camelContext = new DefaultCamelContext(context);

        camelContext.addRoutes(new CamelLeaveRoute());
        camelContext.start();
        ProducerTemplate tpl = camelContext.createProducerTemplate();

        // true
        tpl.sendBody("direct:start", Collections.singletonMap("days", 2));
        assertFalse(leave.getResult());

        // false
        tpl.sendBody("direct:start", Collections.singletonMap("days", 5));
        assertTrue(leave.getResult());

        // Set<LeaveBean> byType = camelContext.getRegistry().findByType(LeaveBean.class);
        // System.out.println("aa=" + ((LeaveBean)byType.iterator().next()).getResult());

        /*tpl.sendBody("direct:forProperty", Collections.singletonMap("days", 2));
        String result = System.getProperty("ok");
        assertEquals("false", result);

        tpl.sendBody("direct:forProperty", Collections.singletonMap("days", 5));
        result = System.getProperty("ok");
        assertEquals("true", result);*/

        camelContext.stop();
    }
}
