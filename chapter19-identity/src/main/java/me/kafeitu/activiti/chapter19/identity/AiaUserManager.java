package me.kafeitu.activiti.chapter19.identity;

import me.kafeitu.activiti.chapter19.identity.entity.AiaUser;

/**
 * 用户管理
 * @author: Henry Yan
 */
public interface AiaUserManager {

    // 根据ID查询用户
    AiaUser get(Long id);

    // 根据登录名查询用户
    AiaUser findByLoginName(String loginName);

    // 保存用户
    AiaUser save(AiaUser user);

    // 删除用户
    void delete(Long id);
}
