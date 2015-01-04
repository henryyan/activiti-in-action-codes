package me.kafeitu.activiti.chapter13;

import java.util.HashMap;
import java.util.Map;

/**
 * User: henryyan
 */
public class ActivityUtil {

    public static Map<String, String> ACTIVITY_TYPE = new HashMap<String, String>();

    static {
        ACTIVITY_TYPE.put("userTask", "用户任务");
        ACTIVITY_TYPE.put("serviceTask", "系统任务");
        ACTIVITY_TYPE.put("startEvent", "开始节点");
        ACTIVITY_TYPE.put("endEvent", "结束节点");
        ACTIVITY_TYPE.put("exclusiveGateway", "条件判断节点(系统自动根据条件处理)");
        ACTIVITY_TYPE.put("inclusiveGateway", "并行处理任务");
        ACTIVITY_TYPE.put("callActivity", "调用活动");
        ACTIVITY_TYPE.put("subProcess", "子流程");
    }

    /**
     * 根据英文获取中文类型
     *
     * @param type
     * @return
     */
    public static String getZhActivityType(String type) {
        return ACTIVITY_TYPE.get(type) == null ? type : ACTIVITY_TYPE.get(type);
    }

}
