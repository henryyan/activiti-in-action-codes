package me.kafeitu.activiti.chapter17.service;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * 请假流程结束监听器
 *
 * @author: Henry Yan
 */
@Service
@Transactional
public class LeaveProcessEndListener implements ExecutionListener {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void notify(DelegateExecution execution) throws Exception {
        String processInstanceId = execution.getProcessInstanceId();

        String sql = "delete from act_hi_detail where proc_inst_id_ = ? and type_ = ?";
        int i = entityManager.createNativeQuery(sql)
                .setParameter(1, processInstanceId)
                .setParameter(2, "FormProperty")
                .executeUpdate();
        logger.debug("清理了 {} 条历史表单数据", i);
    }
}
