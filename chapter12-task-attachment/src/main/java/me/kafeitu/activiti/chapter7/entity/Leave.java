package me.kafeitu.activiti.chapter7.entity;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Entity: Leave
 *
 * @author HenryYan
 */
@Entity
@Table(name = "LEAVE")
public class Leave implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String processInstanceId;
    private String userId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date endTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date realityStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date realityEndTime;

    private Date applyTime;
    private String leaveType;
    private String reason;

    // 流程任务
    private Task task;

    private Map<String, Object> variables;

    // 运行中的流程实例
    private ProcessInstance processInstance;

    // 流程定义
    private ProcessDefinition processDefinition;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column
    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    @Column
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_TIME")
    public Date getStartTime() {
        return startTime;
    }

    @Transient
    public String getStringStartDate() {
        if (startTime == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(startTime);
    }

    @Transient
    public String getStringStartTime() {
        if (startTime == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(startTime);
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_TIME")
    public Date getEndTime() {
        return endTime;
    }

    @Transient
    public String getStringEndDate() {
        if (endTime == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(endTime);
    }

    @Transient
    public String getStringEndTime() {
        if (endTime == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(endTime);
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    @Column
    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    @Column
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REALITY_START_TIME")
    public Date getRealityStartTime() {
        return realityStartTime;
    }

    public void setRealityStartTime(Date realityStartTime) {
        this.realityStartTime = realityStartTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "REALITY_END_TIME")
    public Date getRealityEndTime() {
        return realityEndTime;
    }

    public void setRealityEndTime(Date realityEndTime) {
        this.realityEndTime = realityEndTime;
    }

    @Transient
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    @Transient
    public Map<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    @Transient
    public ProcessInstance getProcessInstance() {
        return processInstance;
    }

    public void setProcessInstance(ProcessInstance processInstance) {
        this.processInstance = processInstance;
    }

    @Transient
    public ProcessDefinition getProcessDefinition() {
        return processDefinition;
    }

    public void setProcessDefinition(ProcessDefinition processDefinition) {
        this.processDefinition = processDefinition;
    }

}