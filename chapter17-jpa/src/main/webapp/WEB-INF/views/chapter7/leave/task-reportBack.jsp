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
		$('input[name=p_DT_realityStartTime]').val($('#realityStartDate').val() + ' ' + $('#realityStartTime').val());
		$('input[name=p_DT_realityEndTime]').val($('#realityEndDate').val() + ' ' + $('#realityEndTime').val());
	}
	</script>
</head>
<body>
	<form action="${ctx }/chapter7/leave/task/complete/${task.id}" class="form-horizontal" method="post" onsubmit="beforeSend()">
		<input type="hidden" name="id" value="${leave.id}" />
		<input type="hidden" name="p_DT_realityStartTime" />
		<input type="hidden" name="p_DT_realityEndTime" />
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
				<label for="name" class="control-label">实际开始时间:</label>
				<div class="controls">
					<input type="text" id="realityStartDate" value="${leave.stringStartDate}" class="datepicker input-small" data-date-format="yyyy-mm-dd" />
					<input type="text" id="realityStartTime" value="${leave.stringStartTime}" class="time input-small" />
				</div>
			</div>
			<div class="control-group">
				<label for="plainPassword" class="control-label">实际结束时间:</label>
				<div class="controls">
					<input type="text" id="realityEndDate" value="${leave.stringEndDate}" class="datepicker input-small" data-date-format="yyyy-mm-dd" />
					<input type="text" id="realityEndTime" value="${leave.stringEndTime}" class="time input-small" />
				</div>
			</div>
			<div class="form-actions">
				<button type="submit" class="btn"><i class="glyphicon glyphicon-ok"></i>完成任务</button>
			</div>
		</fieldset>
	</form>
</body>
</html>