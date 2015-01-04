package me.kafeitu.activiti.chapter19.identity.session;

import me.kafeitu.activiti.chapter19.identity.AiaUserManager;
import me.kafeitu.activiti.chapter19.identity.entity.AiaUser;
import org.activiti.engine.impl.persistence.entity.UserEntityManager;

/**
 * 自定义的用户实体管理类
 * @author: Henry Yan
 */
public class AiaUserEntityManager extends UserEntityManager {

    private AiaUserManager aiaUserManager;

    @Override
    public Boolean checkPassword(String userId, String password) {
        AiaUser aiaUser = aiaUserManager.get(new Long(userId));
        return aiaUser.getPassword().equals(password);
    }

    public void setAiaUserManager(AiaUserManager aiaUserManager) {
        this.aiaUserManager = aiaUserManager;
    }
}
