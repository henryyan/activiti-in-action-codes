package me.kafeitu.activiti.chapter21.events;

import org.activiti.engine.delegate.event.ActivitiEntityEvent;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 全局监听器（对应配置文件的eventListeners）
 *
 * @author: Henry Yan
 */
public class GlobalEventListener implements ActivitiEventListener {

    @Override
    public void onEvent(ActivitiEvent event) {
        ActivitiEventType eventType = event.getType();
        switch (eventType) {
            case ENGINE_CREATED:
                System.out.println("引擎初始化成功！");
                break;
            case ENGINE_CLOSED:
                System.out.println("引擎已关闭！");
                break;
            case ENTITY_CREATED:
                ActivitiEntityEvent entityEvent = (ActivitiEntityEvent) event;
                System.out.println("创建了实体：" + entityEvent.getEntity());
                break;
            case ENTITY_INITIALIZED:
                entityEvent = (ActivitiEntityEvent) event;
                System.out.println("实体初始化完毕：" + entityEvent.getEntity());
                break;

            default:
                System.out.println("捕获到事件[需要处理]：" + eventType.name() + ", type=" + ToStringBuilder.reflectionToString(event));
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}