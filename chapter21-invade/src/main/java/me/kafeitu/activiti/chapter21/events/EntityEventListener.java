package me.kafeitu.activiti.chapter21.events;

import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.BaseEntityEventListener;

/**
 * 实体相关事件处理器
 * @author: Henry Yan
 */
public class EntityEventListener extends BaseEntityEventListener {

    @Override
    protected void onCreate(ActivitiEvent event) {
        ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;
        System.out.println("创建了实体：" + entityEvent);
    }

    @Override
    protected void onInitialized(ActivitiEvent event) {
        super.onInitialized(event);
    }

    @Override
    protected void onDelete(ActivitiEvent event) {
        super.onDelete(event);
    }

    @Override
    protected void onUpdate(ActivitiEvent event) {
        super.onUpdate(event);
    }

    @Override
    protected void onEntityEvent(ActivitiEvent event) {
        super.onEntityEvent(event);
    }
}
