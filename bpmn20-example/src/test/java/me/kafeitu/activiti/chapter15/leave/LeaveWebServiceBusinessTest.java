package me.kafeitu.activiti.chapter15.leave;

import me.kafeitu.activiti.chapter15.leave.ws.LeaveWebService;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * 测试请假流程的Webservice基础功能
 * @author: Henry Yan
 */
public class LeaveWebServiceBusinessTest {

    /**
     * 发布并启动WebService
     */
    @Before
    public void before() {
        LeaveWebserviceUtil.startServer();
    }

    /**
     * 需要总经理审批
     * @throws ParseException
     */
    @Test
    public void testTrue() throws ParseException, MalformedURLException {
        /*
        // CXF方式
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.getInInterceptors().add(new LoggingInInterceptor());
        factory.getOutInterceptors().add(new LoggingOutInterceptor());
        factory.setServiceClass(LeaveWebService.class);
        factory.setAddress(LeaveWebserviceUtil.WEBSERVICE_URL);
        LeaveWebService leaveWebService = (LeaveWebService) factory.create();*/

        // 标准方式
        URL url = new URL(LeaveWebserviceUtil.WEBSERVICE_WSDL_URL);
        QName qname = new QName(LeaveWebserviceUtil.WEBSERVICE_URI, "LeaveWebService");
        Service service = Service.create(url, qname);
        LeaveWebService leaveWebService = service.getPort(LeaveWebService.class);
        boolean audit = leaveWebService.generalManagerAudit("2013-01-01 09:00", "2013-01-05 17:30");
        assertTrue(audit);
    }

    /**
     * 不需要总经理审批
     * @throws ParseException
     */
    @Test
    public void testFalse() throws ParseException {
        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.getInInterceptors().add(new LoggingInInterceptor());
        factory.getOutInterceptors().add(new LoggingOutInterceptor());
        factory.setServiceClass(LeaveWebService.class);
        factory.setAddress(LeaveWebserviceUtil.WEBSERVICE_URL);
        LeaveWebService leaveWebService = (LeaveWebService) factory.create();
        boolean audit = leaveWebService.generalManagerAudit("2013-01-01 09:00", "2013-01-04 17:30");
        assertFalse(audit);
    }

    @After
    public void after() {
        LeaveWebserviceUtil.stopServer();
    }
}
