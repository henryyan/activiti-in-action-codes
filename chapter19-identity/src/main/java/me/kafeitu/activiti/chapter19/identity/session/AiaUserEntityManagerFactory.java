package me.kafeitu.activiti.chapter19.identity.session;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.UserIdentityManager;

/**
 * 自定义的用户实体管理类工厂
 * @author: Henry Yan
 */
public class AiaUserEntityManagerFactory implements SessionFactory {

    private AiaUserEntityManager aiaUserEntityManager;

    public void setAiaUserEntityManager(AiaUserEntityManager aiaUserEntityManager) {
        this.aiaUserEntityManager = aiaUserEntityManager;
    }

    @Override
    public Class<?> getSessionType() {
        return UserIdentityManager.class;
    }

    @Override
    public Session openSession() {
        return aiaUserEntityManager;
    }
}
