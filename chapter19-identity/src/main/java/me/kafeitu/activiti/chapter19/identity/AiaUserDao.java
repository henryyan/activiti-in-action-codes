package me.kafeitu.activiti.chapter19.identity;

import me.kafeitu.activiti.chapter19.identity.entity.AiaUser;

/**
 * @author: Henry Yan
 */
public interface AiaUserDao {

    AiaUser save(AiaUser user);

    void delete(Long id);

    AiaUser get(Long id);

    AiaUser findByLoginName(String loginName);
}
