package me.kafeitu.activiti.chapter17;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import javax.persistence.PersistenceContext;

/**
 * @author henryyan
 */
public class StartLeaveJpaListener implements ExecutionListener {

    @PersistenceContext(name = "persistence/SKO_EM")

	@Override
	public void notify(DelegateExecution execution) throws Exception {
	}

}
