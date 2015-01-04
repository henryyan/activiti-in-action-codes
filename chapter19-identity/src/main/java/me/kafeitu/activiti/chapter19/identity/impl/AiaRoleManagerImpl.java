package me.kafeitu.activiti.chapter19.identity.impl;

import me.kafeitu.activiti.chapter19.identity.AiaRoleDao;
import me.kafeitu.activiti.chapter19.identity.AiaRoleManager;
import me.kafeitu.activiti.chapter19.identity.entity.AiaRole;

import java.util.List;

/**
 * @author: Henry Yan
 */
public class AiaRoleManagerImpl implements AiaRoleManager {

    AiaRoleDao dao;

    public void setDao(AiaRoleDao dao) {
        this.dao = dao;
    }

    @Override
    public AiaRole get(Long id) {
        return null;
    }

    @Override
    public List<AiaRole> findByUserId(Long userId) {
        return dao.findByUserId(userId);
    }

    @Override
    public AiaRole save(AiaRole role) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
