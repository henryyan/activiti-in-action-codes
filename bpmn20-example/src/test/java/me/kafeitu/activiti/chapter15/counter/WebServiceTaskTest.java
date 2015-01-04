package me.kafeitu.activiti.chapter15.counter;

import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.test.Deployment;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

/**
 * @author
 */
public class WebServiceTaskTest extends PluggableActivitiTestCase {

	protected Counter counter;
	private Server server;

	@Override
	protected void initializeProcessEngine() {
		super.initializeProcessEngine();

		counter = new CounterImpl();
		JaxWsServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
		svrFactory.setServiceClass(Counter.class);
		svrFactory.setAddress("http://localhost:12345/counter");
		svrFactory.setServiceBean(counter);
		svrFactory.getInInterceptors().add(new LoggingInInterceptor());
		svrFactory.getOutInterceptors().add(new LoggingOutInterceptor());
		server = svrFactory.create();
		server.start();
	}

	@Deployment(resources = "chapter15/webservice.bpmn")
	public void testWebServiceInvocation() throws Exception {
		assertEquals(-1, counter.getCount());

		processEngine.getRuntimeService().startProcessInstanceByKey("webservice");
		waitForJobExecutorToProcessAllJobs(10000L, 250L);

		assertEquals(0, counter.getCount());
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		server.stop();
		server.destroy();
	}

	/**
	 * 独立启动WebService服务
	 */
	public static void main(String[] args) {
		Counter counter = new CounterImpl();
		JaxWsServerFactoryBean svrFactory = new JaxWsServerFactoryBean();
		svrFactory.setServiceClass(Counter.class);
		svrFactory.setAddress("http://localhost:12345/counter");
		svrFactory.setServiceBean(counter);
		svrFactory.getInInterceptors().add(new LoggingInInterceptor());
		svrFactory.getOutInterceptors().add(new LoggingOutInterceptor());
		Server server = svrFactory.create();
		server.start();
	}
}