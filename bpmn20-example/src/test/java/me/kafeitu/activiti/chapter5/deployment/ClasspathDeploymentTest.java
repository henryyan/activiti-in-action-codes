/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.kafeitu.activiti.chapter5.deployment;

import me.kafeitu.activiti.AbstractTest;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * classpath方式部署流程定义
 *
 * @author henryyan
 */
public class ClasspathDeploymentTest extends AbstractTest {

    @Test
    public void testClasspathDeployment() throws Exception {

        // 定义classpath
        String bpmnClasspath = "chapter5/candidateUserInUserTask.bpmn";
        String pngClasspath = "chapter5/candidateUserInUserTask.png";

        // 创建部署构建器
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();

        // 添加资源
        deploymentBuilder.addClasspathResource(bpmnClasspath);
        deploymentBuilder.addClasspathResource(pngClasspath);

        // 执行部署
        deploymentBuilder.deploy();

        // 验证流程定义是否部署成功
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        long count = processDefinitionQuery.processDefinitionKey("candidateUserInUserTask").count();
        assertEquals(1, count);

        // 读取图片文件
        ProcessDefinition processDefinition = processDefinitionQuery.singleResult();
        String diagramResourceName = processDefinition.getDiagramResourceName();
        assertEquals(pngClasspath, diagramResourceName);

        Map<String, Object> vars = new HashMap<String, Object>();
        ArrayList<Date> objs = new ArrayList<Date>();
        objs.add(new Date());
        vars.put("list", objs);
//        vars.put("aaa", "333");
        runtimeService.startProcessInstanceByKey("candidateUserInUserTask", vars);
        List<Task> list = taskService.createTaskQuery().includeProcessVariables().list();
        System.out.println(list);
        Task task = taskService.createTaskQuery().taskId(list.get(0).getId())
                .includeProcessVariables().includeTaskLocalVariables().singleResult();
        CommandContext commandContext = Context.getCommandContext();
        System.out.println(task);
        System.out.println(commandContext);

//        ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl) ProcessEngines.getDefaultProcessEngine();
//        Context.setProcessEngineConfiguration(defaultProcessEngine.getProcessEngineConfiguration());
//        Context.setCommandContext(defaultProcessEngine.getProcessEngineConfiguration().getCommandContextFactory().createCommandContext(null));
        System.out.println(Context.getCommandContext());
        System.out.println(task.getProcessVariables());
    }

    @Test
    public void testClasspathDeployment111() throws Exception {

        // 定义classpath
        String bpmnClasspath = "chapter5/candidateUserInUserTask.bpmn";

        // 创建部署构建器
        DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();

        // 添加资源
        deploymentBuilder.addClasspathResource(bpmnClasspath);

        // 执行部署
        deploymentBuilder.deploy();

        // 验证流程定义是否部署成功
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        long count = processDefinitionQuery.processDefinitionKey("candidateUserInUserTask").count();
        assertEquals(1, count);

        Map<String, Object> vars = new HashMap<String, Object>();
        ArrayList<Date> objs = new ArrayList<Date>();
        objs.add(new Date());
        vars.put("list", objs);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("candidateUserInUserTask", vars);
        Task task = taskService.createTaskQuery().includeProcessVariables().singleResult();
        assertNotNull(task.getProcessVariables());
    }

}
