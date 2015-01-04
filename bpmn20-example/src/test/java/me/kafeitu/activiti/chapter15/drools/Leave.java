package me.kafeitu.activiti.chapter15.drools;

import java.io.Serializable;
import java.util.Date;

/**
 * 请假Bean
 * @author: Henry Yan
 */
public class Leave implements Serializable {

    private String processInstanceId;
    private String userId;
    private java.util.Date startTime;
    private java.util.Date endTime;
    private java.util.Date realityStartTime;
    private java.util.Date realityEndTime;
    private Date applyTime;
    private String leaveType;
    private String reason;

    private boolean generalManagerAudit;

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getRealityStartTime() {
        return realityStartTime;
    }

    public void setRealityStartTime(Date realityStartTime) {
        this.realityStartTime = realityStartTime;
    }

    public Date getRealityEndTime() {
        return realityEndTime;
    }

    public void setRealityEndTime(Date realityEndTime) {
        this.realityEndTime = realityEndTime;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isGeneralManagerAudit() {
        return generalManagerAudit;
    }

    public void setGeneralManagerAudit(boolean generalManagerAudit) {
        this.generalManagerAudit = generalManagerAudit;
    }
}
