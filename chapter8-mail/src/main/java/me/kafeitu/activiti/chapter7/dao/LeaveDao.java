package me.kafeitu.activiti.chapter7.dao;

import me.kafeitu.activiti.chapter7.entity.Leave;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author henryyan
 */
@Repository
public class LeaveDao {

    @Autowired
    private SessionFactory sessionFactory;

    /**
     * 保存实体
     */
    public void save(Leave entity) {
        getSession().saveOrUpdate(entity);
    }

    public void delete(Long id) {
        getSession().delete(get(id));
    }

    public Leave get(Long id) {
        return (Leave) getSession().get(Leave.class, id);
    }

    private Session getSession() {
        return sessionFactory.getCurrentSession();
    }

}
