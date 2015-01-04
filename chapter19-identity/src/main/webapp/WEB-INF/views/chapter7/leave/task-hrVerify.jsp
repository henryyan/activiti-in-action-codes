<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/common/global.jsp"%>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/include-base-styles.jsp" %>
	<link rel="stylesheet" href="${ctx}/js/common/plugins/timepicker.css">
	<title>请假--${task.name}</title>
</head>
<body>
	<form action="${ctx }/chapter7/leave/task/complete/${task.id}" class="form-horizontal" method="post" onsubmit="beforeSend()">
		<input type="hidden" name="id" value="${leave.id}" />
		<fieldset>
			<legend><small>${task.name}</small></legend>
			<div class="control-group">
				<label for="loginName" class="control-label">申请人:</label>
				<div class="controls">${leave.userId}</div>
			</div>
			<div class="control-group">
				<label for="loginName" class="control-label">申请时间:</label>
				<div class="controls">${leave.applyTime}</div>
			</div>
			<div class="control-group">
				<label for="loginName" class="control-label">请假类型:</label>
				<div class="controls">${leave.leaveType}</div>
			</div>
			<div class="control-group">
				<label for="name" class="control-label">开始时间:</label>
				<div class="controls">${leave.startTime}</div>
			</div>
			<div class="control-group">
				<label for="plainPassword" class="control-label">结束时间:</label>
				<div class="controls">${leave.endTime}</div>
			</div>
			<div class="control-group">
				<label for="groupList" class="control-label">请假原因:</label>
				<div class="controls">${leave.reason}</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="hrApproved">是否继续申请：</label>
				<div class="controls">
					<select id="hrApproved" name="p_B_hrApproved">
						<option value="true">同意</option>
						<option value="false">拒绝</option>
					</select>
				</div>
			</div>
			<div class="form-actions">
				<button type="submit" class="btn"><i class="glyphicon glyphicon-ok"></i>完成任务</button>
			</div>
		</fieldset>
	</form>
</body>
</html>