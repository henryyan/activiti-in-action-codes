package me.kafeitu.activiti.chapter7.engine;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

/**
 * @author henryyan
 */
public class ProcessEngineConfigurationTest {

    @Test
    public void userDefaultName() {
        ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration().buildProcessEngine();
        System.out.println(processEngine.hashCode());
        assertEquals("default", processEngine.getName());
    }

    /**
     * 此测试会失败，因为本例没有配置oracle驱动
     */
    @Test
    public void specialName() {
        ProcessEngineConfiguration.createProcessEngineConfigurationFromResourceDefault().setProcessEngineName("oracleEngine").setJdbcDriver("oracle.jdbc.Driver")
                .setJdbcUrl("jdbc:oracle:thin:@localhost:1521:XE").setJdbcUsername("dbusername").setJdbcPassword("password").setDatabaseType("oracle")
                .buildProcessEngine();
        ProcessEngine oracleEngine = ProcessEngines.getProcessEngine("oracleEngine");
        assertNotNull(oracleEngine);
    }

}
