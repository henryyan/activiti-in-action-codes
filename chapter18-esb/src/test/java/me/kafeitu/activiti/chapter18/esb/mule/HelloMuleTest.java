package me.kafeitu.activiti.chapter18.esb.mule;

import org.junit.Test;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleContext;
import org.mule.api.MuleMessage;
import org.mule.api.client.MuleClient;
import org.mule.client.DefaultLocalMuleClient;
import org.mule.context.DefaultMuleContextFactory;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * @author: Henry Yan
 */
public class HelloMuleTest {

    @Test
    public void testMule() throws Exception {
        MuleContext muleContext = new DefaultMuleContextFactory().createMuleContext("mule/mule-standlone-config.xml");
        muleContext.start();
        MuleClient muleClient = new DefaultLocalMuleClient(muleContext);
        Integer days = 5;
        MuleMessage message = muleClient.send("vm://helloMule", new DefaultMuleMessage(days, muleContext));
        assertNotNull(message.getPayload());
        Boolean result = (Boolean) message.getPayload();
        assertTrue(result);
    }

}
