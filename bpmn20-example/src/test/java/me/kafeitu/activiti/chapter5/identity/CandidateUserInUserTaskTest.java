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

package me.kafeitu.activiti.chapter5.identity;

import me.kafeitu.activiti.AbstractTest;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.Deployment;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * 候选人在用户任务的使用
 *
 * @author henryyan
 */
public class CandidateUserInUserTaskTest extends AbstractTest {

    /**
     * 学习一个用户任务有多个候选人
     */
    @Test
    @Deployment(resources = {"chapter5/candidateUserInUserTask.bpmn"})
    public void testMultiCadiateUserInUserTask() throws Exception {

        // 添加用户jackchen
        User userJackChen = identityService.newUser("jackchen");
        userJackChen.setFirstName("Jack");
        userJackChen.setLastName("Chen");
        userJackChen.setEmail("jackchen@gmail.com");
        identityService.saveUser(userJackChen);

        // 添加用户henryyan
        User userHenryyan = identityService.newUser("henryyan");
        userHenryyan.setFirstName("Henry");
        userHenryyan.setLastName("Yan");
        userHenryyan.setEmail("yanhonglei@gmail.com");
        identityService.saveUser(userHenryyan);

        // 启动流程
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("candidateUserInUserTask");
        assertNotNull(processInstance);

        // jackchen作为候选人的任务
        Task jackchenTask = taskService.createTaskQuery().taskCandidateUser("jackchen").singleResult();
        assertNotNull(jackchenTask);

        // henryyan作为候选人的任务
        Task henryyanTask = taskService.createTaskQuery().taskCandidateUser("henryyan").singleResult();
        assertNotNull(henryyanTask);

        // jackchen签收任务
        taskService.claim(jackchenTask.getId(), "jackchen");

        // 再次查询用户henryyan是否拥有刚刚的候选任务
        henryyanTask = taskService.createTaskQuery().taskCandidateUser("henryyan").singleResult();
        assertNull(henryyanTask);
    }

}
