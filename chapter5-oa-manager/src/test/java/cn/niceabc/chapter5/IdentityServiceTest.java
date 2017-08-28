package cn.niceabc.chapter5;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;

public class IdentityServiceTest {

    String configurationResource = "activiti.cfg.xml";

    @Rule
    // 默认加载activiti.cfg.xml
    public ActivitiRule activitiRule = new ActivitiRule(configurationResource);

    @Test
    public void testUser() {
        IdentityService identityService = activitiRule.getIdentityService();

        identityService.deleteUser("tom");

        User user = identityService.newUser("tom");
        user.setFirstName("tom");
        user.setLastName("cruise");
        user.setEmail("tom.cruise@email.com");
        identityService.saveUser(user);

        User tom = identityService.createUserQuery().userId("tom").singleResult();
        Assert.assertNotNull(tom);
        Assert.assertEquals("cruise", tom.getLastName());


        identityService.deleteUser("tom");

        User tom_null = identityService.createUserQuery().userId("tom").singleResult();
        Assert.assertNull(tom_null);

    }

    @Test
    public void testGroup() {
        IdentityService identityService = activitiRule.getIdentityService();

        identityService.deleteGroup("development");

        Group group  =identityService.newGroup("development");
        group.setName("development");
        group.setType("assignment");
        identityService.saveGroup(group);

        Group group1 = identityService.createGroupQuery().groupId("development").singleResult();
        Assert.assertNotNull(group1);
        Assert.assertEquals("development", group1.getName());
        Assert.assertEquals("assignment", group1.getType());

        identityService.deleteGroup("development");

        Group group_null = identityService.createGroupQuery().groupId("development").singleResult();
        Assert.assertNull(group_null);

    }

    @Test
    public void testMembership() {
        IdentityService identityService = activitiRule.getIdentityService();

        identityService.deleteMembership("tom", "development");
        identityService.deleteGroup("development");
        identityService.deleteUser("tom");

        //group

        Group group  =identityService.newGroup("development");
        group.setName("development");
        group.setType("assignment");
        identityService.saveGroup(group);

        Group group1 = identityService.createGroupQuery().groupId("development").singleResult();
        Assert.assertNotNull(group1);
        Assert.assertEquals("development", group1.getName());
        Assert.assertEquals("assignment", group1.getType());

        //user

        User user = identityService.newUser("tom");
        user.setFirstName("tom");
        user.setLastName("cruise");
        user.setEmail("tom.cruise@email.com");
        identityService.saveUser(user);

        User tom = identityService.createUserQuery().userId("tom").singleResult();
        Assert.assertNotNull(tom);
        Assert.assertEquals("cruise", tom.getLastName());


        // membership
        identityService.createMembership("tom", "development");
        User tomInGroup = identityService
                .createUserQuery()
                .memberOfGroup("development")
                .singleResult();
        Assert.assertNotNull(tomInGroup);
        Assert.assertEquals("tom", tomInGroup.getId());

        Group groupContainsTom = identityService
              .createGroupQuery()
                .groupMember("tom")
                .singleResult();
        Assert.assertNotNull(groupContainsTom);
        Assert.assertEquals("development", groupContainsTom.getId());

        // clean

        identityService.deleteMembership("tom", "development");

        identityService.deleteUser("tom");
        User tom_null = identityService.createUserQuery().userId("tom").singleResult();
        Assert.assertNull(tom_null);

        identityService.deleteGroup("development");
        Group group_null = identityService.createGroupQuery().groupId("development").singleResult();
        Assert.assertNull(group_null);

    }
}
