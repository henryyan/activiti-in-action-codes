package me.kafeitu.activiti.chapter21.events;

import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.activiti.engine.impl.persistence.entity.TaskEntity;

/**
 * 全局监听器（对应配置文件的eventListeners）
 *
 * @author: Henry Yan
 */
public class EventExceptionListener implements ActivitiEventListener {

    private boolean isFailOnException = false;

    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiEventType eventType = event.getType();
        switch (eventType) {
            case ENTITY_CREATED:
                ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;
                System.out.println("创建了实体：" + entityEvent.getEntity());
                break;
            case ENTITY_DELETED:
                entityEvent = (ActivitiEntityEvent) event;
                if (entityEvent.getEntity() instanceof TaskEntity) {
                    isFailOnException = true;
                    throw new RuntimeException("不允许删除TaskEntity");
                }
                System.out.println("实体已被删除：" + entityEvent.getEntity().getClass());
                break;
        }
    }

    @Override
    public boolean isFailOnException() {
        return isFailOnException;
    }
}