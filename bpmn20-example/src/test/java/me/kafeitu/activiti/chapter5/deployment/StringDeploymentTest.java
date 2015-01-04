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
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


/**
 * 用文本方式部署
 *
 * @author henryyan
 */
public class StringDeploymentTest extends AbstractTest {

    // XML字符串
    private String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<definitions xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:activiti=\"http://activiti.org/bpmn\" xmlns:bpmndi=\"http://www.omg.org/spec/BPMN/20100524/DI\" xmlns:omgdc=\"http://www.omg.org/spec/DD/20100524/DC\" xmlns:omgdi=\"http://www.omg.org/spec/DD/20100524/DI\" typeLanguage=\"http://www.w3.org/2001/XMLSchema\" expressionLanguage=\"http://www.w3.org/1999/XPath\" targetNamespace=\"http://www.kafeitu.me/activiti-in-action\">"
            + "  <process id=\"candidateUserInUserTask\" name=\"candidateUserInUserTask\">"
            + "    <startEvent id=\"startevent1\" name=\"Start\"></startEvent>"
            + "    <userTask id=\"usertask1\" name=\"用户任务包含多个直接候选人\" activiti:candidateUsers=\"jackchen, henryyan\"></userTask>"
            + "    <sequenceFlow id=\"flow1\" name=\"\" sourceRef=\"startevent1\" targetRef=\"usertask1\"></sequenceFlow>"
            + "    <endEvent id=\"endevent1\" name=\"End\"></endEvent>"
            + "    <sequenceFlow id=\"flow2\" name=\"\" sourceRef=\"usertask1\" targetRef=\"endevent1\"></sequenceFlow>"
            + "  </process>"
            + "</definitions>";

    @Test
    public void testCharsDeployment() {
        // 以candidateUserInUserTask.bpmn为资源名称，以text的内容为资源内容部署到引擎
        repositoryService.createDeployment().addString("candidateUserInUserTask.bpmn", text).deploy();

        // 验证流程定义是否部署成功
        ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
        long count = processDefinitionQuery.processDefinitionKey("candidateUserInUserTask").count();
        assertEquals(1, count);
    }

}
