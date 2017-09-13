package me.kafeitu.activiti.chapter13;

import me.kafeitu.activiti.base.AbstractTest;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.Deployment;
import org.activiti.spring.impl.test.SpringActivitiTestCase;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

//@ContextConfiguration("classpath:applicationContext.xml")
public class UserServiceTest extends AbstractTest {

    private static Logger log = LoggerFactory.getLogger(UserServiceTest.class);

    @Test
    public void testUserQuery() throws InterruptedException {

        List<User> users = super.identityService.createUserQuery().list();
        if (users.size() == 0) log.debug("there is no user.");
        users.forEach(user -> {
            log.debug("user, {}, {}", user.getFirstName(), user.getLastName());
        });
    }
}
