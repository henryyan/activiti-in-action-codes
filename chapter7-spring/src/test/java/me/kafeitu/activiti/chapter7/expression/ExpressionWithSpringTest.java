package me.kafeitu.activiti.chapter7.expression;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.activiti.spring.impl.test.SpringActivitiTestCase;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 不使用Spring的表达式测试
 *
 * @author henryyan
 */
@ContextConfiguration("classpath:applicationContext-expression.xml")
public class ExpressionWithSpringTest extends SpringActivitiTestCase {

    @Deployment(resources = "diagrams/chapter7/expression.bpmn")
    public void testExpression() {
        Map<String, Object> variables = new HashMap<String, Object>();
        String name = "Henry Yan";
        variables.put("name", name);

        // 运行期表达式
        identityService.setAuthenticatedUserId("henryyan");
        String businessKey = "9999"; // 业务ID
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("expression", businessKey, variables);
        assertEquals("henryyan", runtimeService.getVariable(processInstance.getId(), "authenticatedUserIdForTest"));
        assertEquals("Henry Yan, added by print(String name)", runtimeService.getVariable(processInstance.getId(), "returnValue"));
        assertEquals(businessKey, runtimeService.getVariable(processInstance.getId(), "businessKey"));

        // 显示数据库状态
        List<Map<String, Object>> rows = managementService.createTablePageQuery().tableName("ACT_HI_DETAIL").listPage(0, 100).getRows();
        for (Map<String, Object> map : rows) {
            Set<Entry<String, Object>> entrySet = map.entrySet();
            for (Entry<String, Object> entry : entrySet) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }
            System.out.println("-------------------------");
        }

        System.out.println("==============字节流数据==============");
        rows = managementService.createTablePageQuery().tableName("ACT_GE_BYTEARRAY").listPage(0, 100).getRows();
        for (Map<String, Object> map : rows) {
            Set<Entry<String, Object>> entrySet = map.entrySet();
            for (Entry<String, Object> entry : entrySet) {
                System.out.println(entry.getKey() + " = " + entry.getValue());
            }
            System.out.println("-------------------------");
        }

        // DelegateTask 设置的变量
        Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();
        String setByTask = (String) taskService.getVariable(task.getId(), "setByTask");
        assertEquals("I'm setted by DelegateTask, " + name, setByTask);
    }

}
