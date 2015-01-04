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
import org.activiti.engine.repository.ProcessDefinition;
import org.junit.Test;

import java.io.InputStream;

/**
 * 学习如何读取XML资源文件
 *
 * @author henryyan
 */
public class ReadXmlResourceTest extends AbstractTest {

    @Test
    public void testClasspathDeployment() throws Exception {

        // 定义classpath
        String bpmnClasspath = "chapter5/candidateUserInUserTask.bpmn";

        // 添加资源
        repositoryService.createDeployment().addClasspathResource(bpmnClasspath).deploy();

        // 获取流程定义对象
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().singleResult();
        String resourceName = pd.getResourceName();
        System.out.println(String.format("资源名称：%s", resourceName));

        // 读取资源字节流
        InputStream resourceAsStream = repositoryService.getResourceAsStream(pd.getDeploymentId(), resourceName);

        // 输出流的内容
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = resourceAsStream.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        System.out.println(out);
    }
}
