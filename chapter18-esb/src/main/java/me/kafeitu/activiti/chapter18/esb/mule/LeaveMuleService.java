package me.kafeitu.activiti.chapter18.esb.mule;

/**
 * 请假天数逻辑判断Mule Service
 * @author: Henry Yan
 */
public class LeaveMuleService {

    public boolean isDeptLeaderApproved(Integer days) {
        return days > 3;
    }

    public boolean isDeptLeaderApproved(String days) {
        return isDeptLeaderApproved(Integer.valueOf(days));
    }

}
