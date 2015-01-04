<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/common/global.jsp"%>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/include-base-styles.jsp" %>
	<link rel="stylesheet" href="${ctx}/js/common/plugins/timepicker.css">
	<title>请假申请--${task.name}</title>
	<script type="text/javascript" src="${ctx }/js/common/jquery.js"></script>
	<script type="text/javascript" src="${ctx }/js/common/bootstrap.min.js"></script>
	<script type="text/javascript" src="${ctx }/js/common/bootstrap-datepicker.js"></script>
	<script type="text/javascript" src="${ctx }/js/common/plugins/bootstrap-timepicker.js"></script>
	<script type="text/javascript">
	$(function() {
		$('.datepicker').datepicker();
		$('.time').timepicker({
			minuteStep: 10,
            showMeridian: false
		});
	});

	function beforeSend() {
		$('input[name=startTime]').val($('#startDate').val() + ' ' + $('#startTime').val());
		$('input[name=endTime]').val($('#endDate').val() + ' ' + $('#endTime').val());
	}
	</script>
</head>
<body>
	<form action="${ctx }/chapter7/leave/task/complete/${task.id}" class="form-horizontal" method="post" onsubmit="beforeSend()">
		<input type="hidden" name="id" value="${leave.id}" />
		<input type="hidden" name="startTime" />
		<input type="hidden" name="endTime" />
		<input type="hidden" name="saveEntity" value="true" />
		<fieldset>
			<legend><small>${task.name}</small></legend>
			<div class="control-group">
				<label for="loginName" class="control-label">请假类型:</label>
				<div class="controls">
					<select id="leaveType" name="leaveType" class="required">
						<option>公休</option>
						<option>病假</option>
						<option>调休</option>
						<option>事假</option>
						<option>婚假</option>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label for="name" class="control-label">开始时间:</label>
				<div class="controls">
					<input type="text" id="startDate" value="${leave.stringStartDate}" class="datepicker input-small" data-date-format="yyyy-mm-dd" />
					<input type="text" id="startTime" value="${leave.stringStartTime}" class="time input-small" />
				</div>
			</div>
			<div class="control-group">
				<label for="plainPassword" class="control-label">结束时间:</label>
				<div class="controls">
					<input type="text" id="endDate" value="${leave.stringEndDate}" class="datepicker input-small" data-date-format="yyyy-mm-dd" />
					<input type="text" id="endTime" value="${leave.stringEndTime}" class="time input-small" />
				</div>
			</div>
			<div class="control-group">
				<label for="groupList" class="control-label">请假原因:</label>
				<div class="controls">
					<textarea name="reason">${leave.reason}</textarea>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="reApply">是否继续申请：</label>
				<div class="controls">
					<select id="reApply" name="p_B_reApply">
						<option value="true">重新申请</option>
						<option value="false">结束流程</option>
					</select>
				</div>
			</div>
			<div class="form-actions">
				<button type="submit" class="btn"><i class="icon-ok"></i>完成任务</button>
			</div>
		</fieldset>
	</form>
</body>
</html>