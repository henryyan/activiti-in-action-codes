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

import java.io.FileInputStream;

import static org.junit.Assert.assertEquals;

/**
 * InputStream方式部署资源
 *
 * @author henryyan
 */
public class InputStreamDeploymentTest extends AbstractTest {

    /**
     * 从具体的文件中读取输入流部署
     */
    @Test
    public void testInputStreamFromAbsoluteFilePath() throws Exception {

        // 注意：读者根据自己的实际项目路径更改后验证
        String filePath = "/Users/henryyan/work/books/activiti-in-action-codes/bpmn20-example/src/test/resources/chapter5/userAndGroupInUserTask.bpmn";

        // 读取classpath的资源为一个输入流
        FileInputStream fileInputStream = new FileInputStream(filePath);
        repositoryService.createDeployment().addInputStream("userAndGroupInUserTask.bpmn", fileInputStream).deploy();

        // 验证是否部署成功
        ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
        long count = pdq.processDefinitionKey("userAndGroupInUserTask").count();
        assertEquals(1, count);
    }

}
