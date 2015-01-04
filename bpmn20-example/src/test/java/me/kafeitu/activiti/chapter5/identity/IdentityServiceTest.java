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
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * 组与用户单元测试
 *
 * @author henryyan
 */
public class IdentityServiceTest extends AbstractTest {

    /**
     * 组管理API演示
     */
    @Test
    public void testGroup() throws Exception {
        // 创建一个组对象
        Group group = identityService.newGroup("deptLeader");
        group.setName("部门领导");
        group.setType("assignment");

        // 保存组
        identityService.saveGroup(group);

        // 验证组是否已保存成功，首先需要创建组查询对象
        List<Group> groupList = identityService.createGroupQuery().groupId("deptLeader").list();
        assertEquals(1, groupList.size());

        // 删除组
        identityService.deleteGroup("deptLeader");

        // 验证是否删除成功
        groupList = identityService.createGroupQuery().groupId("deptLeader").list();
        assertEquals(0, groupList.size());
    }

    /**
     * 用户管理API演示
     */
    @Test
    public void testUser() throws Exception {
        // 创建一个用户
        User user = identityService.newUser("henryyan");
        user.setFirstName("Henry");
        user.setLastName("Yan");
        user.setEmail("yanhonglei@gmail.com");

        // 保存用户到数据库
        identityService.saveUser(user);

        // 验证用户是否保存成功
        User userInDb = identityService.createUserQuery().userId("henryyan").singleResult();
        assertNotNull(userInDb);

        // 删除用户
        identityService.deleteUser("henryyan");

        // 验证是否删除成功
        userInDb = identityService.createUserQuery().userId("henryyan").singleResult();
        assertNull(userInDb);
    }

    /**
     * 组和用户关联关系API演示
     */
    @Test
    public void testUserAndGroupMemership() throws Exception {
        // 创建并保存组对象
        Group group = identityService.newGroup("deptLeader");
        group.setName("部门领导");
        group.setType("assignment");
        identityService.saveGroup(group);

        // 创建并保存用户对象
        User user = identityService.newUser("henryyan");
        user.setFirstName("Henry");
        user.setLastName("Yan");
        user.setEmail("yanhonglei@gmail.com");
        identityService.saveUser(user);

        // 把用户henryyan加入到组deptLeader中
        identityService.createMembership("henryyan", "deptLeader");

        // 查询属于组deptLeader的用户
        User userInGroup = identityService.createUserQuery().memberOfGroup("deptLeader").singleResult();
        assertNotNull(userInGroup);
        assertEquals("henryyan", userInGroup.getId());

        // 查询henryyan所属组
        Group groupContainsHenryyan = identityService.createGroupQuery().groupMember("henryyan").singleResult();
        assertNotNull(groupContainsHenryyan);
        assertEquals("deptLeader", groupContainsHenryyan.getId());
    }
}
