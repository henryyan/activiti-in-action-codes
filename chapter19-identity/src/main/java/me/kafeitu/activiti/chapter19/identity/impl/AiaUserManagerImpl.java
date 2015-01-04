package me.kafeitu.activiti.chapter19.identity.impl;

import me.kafeitu.activiti.chapter19.identity.AiaUserDao;
import me.kafeitu.activiti.chapter19.identity.AiaUserManager;
import me.kafeitu.activiti.chapter19.identity.entity.AiaUser;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;

/**
 * 用户实体管理实现类
 * @author: Henry Yan
 */
public class AiaUserManagerImpl implements AiaUserManager {

    private AiaUserDao dao;
    private IdentityService identityService;

    public void setDao(AiaUserDao dao) {
        this.dao = dao;
    }

    public void setIdentityService(IdentityService identityService) {
        this.identityService = identityService;
    }

    @Override
    public AiaUser get(Long id) {
        return dao.get(id);
    }

    @Override
    public AiaUser findByLoginName(String loginName) {
        return dao.findByLoginName(loginName);
    }

    @Override
    public AiaUser save(AiaUser user) {
        dao.save(user);
        User activitiUser = null;
        if (user.getId() == null) {
            activitiUser = identityService.newUser(user.getId().toString());
        } else {
            activitiUser = identityService.createUserQuery()
                    .userId(user.getId().toString()).singleResult();
            /** 省略代码 -> 复制user的属性到activitiUser */
            identityService.saveUser(activitiUser);
        }
        return user;
    }

    @Override
    public void delete(Long id) {
        dao.delete(id);
    }
}
