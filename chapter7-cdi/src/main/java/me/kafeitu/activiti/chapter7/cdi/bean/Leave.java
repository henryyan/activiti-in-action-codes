package me.kafeitu.activiti.chapter7.cdi.bean;

import org.activiti.cdi.annotation.BusinessProcessScoped;

import javax.inject.Named;
import java.io.Serializable;

/**
 * @author: Henry Yan
 */
@Named
@BusinessProcessScoped
public class Leave implements Serializable {

    private String startTime;
    private String endTime;
    private String reason;
    private String reallyStartTime;
    private String reallyEndTime;

    private boolean reapply;
    private boolean deptLeaderApproved;
    private boolean hrApproved;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isDeptLeaderApproved() {
        return deptLeaderApproved;
    }

    public void setDeptLeaderApproved(boolean deptLeaderApproved) {
        this.deptLeaderApproved = deptLeaderApproved;
    }

    public boolean isHrApproved() {
        return hrApproved;
    }

    public void setHrApproved(boolean hrApproved) {
        this.hrApproved = hrApproved;
    }

    public String getReallyStartTime() {
        return reallyStartTime;
    }

    public void setReallyStartTime(String reallyStartTime) {
        this.reallyStartTime = reallyStartTime;
    }

    public String getReallyEndTime() {
        return reallyEndTime;
    }

    public void setReallyEndTime(String reallyEndTime) {
        this.reallyEndTime = reallyEndTime;
    }

    public boolean isReapply() {
        return reapply;
    }

    public void setReapply(boolean reapply) {
        this.reapply = reapply;
    }
}
