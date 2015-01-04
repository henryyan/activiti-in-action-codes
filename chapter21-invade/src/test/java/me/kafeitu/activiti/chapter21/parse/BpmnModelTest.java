package me.kafeitu.activiti.chapter21.parse;

import me.kafeitu.activiti.base.AbstractTest;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.test.Deployment;
import org.junit.Test;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * BpmnModel相关测试
 * @author: Henry Yan
 */
public class BpmnModelTest extends AbstractTest {

    /**
     * 把XML转换成BpmnModel对象
     * @throws Exception
     */
    @Test
    @Deployment(resources = "chapter6/dynamic-form/leave.bpmn")
    public void testXmlToBpmnModel() throws Exception {

        // 验证是否部署成功
        long count = repositoryService.createProcessDefinitionQuery().count();
        assertEquals(1, count);

        // 根据流程定义获取XML资源文件流对象
        /*ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("leave").singleResult();
        String resourceName = processDefinition.getResourceName();
        InputStream inputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);*/

        // 从classpath中获取
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("chapter6/dynamic-form/leave.bpmn");

        // 创建转换对象
        BpmnXMLConverter converter = new BpmnXMLConverter();

        // 创建XMLStreamReader读取XML资源
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(inputStream);

        // 把XML转换成BpmnModel对象
        BpmnModel bpmnModel = converter.convertToBpmnModel(reader);

        // 验证BpmnModel对象不为空
        assertNotNull(bpmnModel);
        Process process = bpmnModel.getMainProcess();
        assertEquals("leave", process.getId());
    }

    /**
     * 把BpmnModel转换为XML对象
     * @throws Exception
     */
    @Test
    @Deployment(resources = "chapter6/dynamic-form/leave.bpmn")
    public void testBpmnModelToXml() throws Exception {

        // 验证是否部署成功
        long count = repositoryService.createProcessDefinitionQuery().count();
        assertEquals(1, count);

        // 查询流程定义对象
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("leave").singleResult();

        // 获取BpmnModel对象
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());

        // 创建转换对象
        BpmnXMLConverter converter = new BpmnXMLConverter();

        // 把BpmnModel对象转换成字符（也可以输出到文件中）
        byte[] bytes = converter.convertToXML(bpmnModel);
        String xmlContent = new String(bytes);
        System.out.println(xmlContent);
    }

}
