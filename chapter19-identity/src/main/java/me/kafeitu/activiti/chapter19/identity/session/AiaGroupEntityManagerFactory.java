package me.kafeitu.activiti.chapter19.identity.session;

import org.activiti.engine.impl.interceptor.Session;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.engine.impl.persistence.entity.GroupIdentityManager;

/**
 * @author: Henry Yan
 */
public class AiaGroupEntityManagerFactory implements SessionFactory {

    private AiaGroupEntityManager aiaGroupEntityManager;

    public void setAiaGroupEntityManager(AiaGroupEntityManager aiaGroupEntityManager) {
        this.aiaGroupEntityManager = aiaGroupEntityManager;
    }

    @Override
    public Class<?> getSessionType() {
        return GroupIdentityManager.class;
    }

    @Override
    public Session openSession() {
        return aiaGroupEntityManager;
    }
}
