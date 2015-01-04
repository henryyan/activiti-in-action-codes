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

package me.kafeitu.activiti.chapter11;

import org.activiti.engine.impl.EventSubscriptionQueryImpl;
import org.activiti.engine.impl.persistence.entity.EventSubscriptionEntity;
import org.activiti.engine.impl.test.PluggableActivitiTestCase;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.Deployment;

/**
 * classpath方式部署流程定义
 *
 * @author henryyan
 */
public class MessageStartEventTest extends PluggableActivitiTestCase {

    /**
     * 通过消息名称启动
     */
    @Deployment(resources = "chapter11/messageEvent/messageStartEvent.bpmn")
    public void testStartMessageEvent() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByMessage("启动XXX流程");
        assertNotNull(processInstance);
    }

    /**
     * 通过流程ID启动
     */
    @Deployment(resources = "chapter11/messageEvent/messageStartEvent.bpmn")
    public void testStartMessageEventByKey() throws Exception {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("messageStartEvent");
        assertNotNull(processInstance);
    }

    /**
     * 启动前检查对应的消息是否已注册到引擎
     */
    @Deployment(resources = "chapter11/messageEvent/messageStartEvent.bpmn")
    public void testMessageSubcription() throws Exception {
        EventSubscriptionQueryImpl eventSubscriptionQuery = new EventSubscriptionQueryImpl(processEngineConfiguration.getCommandExecutor());
        EventSubscriptionEntity subscriptionEntity = eventSubscriptionQuery.eventName("启动XXX流程").singleResult();
        assertNotNull(subscriptionEntity);
    }

}
