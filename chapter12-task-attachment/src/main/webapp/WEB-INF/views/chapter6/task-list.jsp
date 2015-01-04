<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/common/global.jsp"%>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/include-base-styles.jsp" %>
	<title>待办任务列表--chapter6</title>
	<style type="text/css">
		div.datepicker {z-index: 10000;}
	</style>

	<script src="${ctx }/js/common/jquery.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctx }/js/common/bootstrap.min.js"></script>
	<script type="text/javascript" src="${ctx }/js/common/bootstrap-datepicker.js"></script>
	<script type="text/javascript">
	$(function() {
		$('.datepicker').datepicker();
	});
	</script>
</head>
<body>
	<c:if test="${not empty message}">
		<div id="message" class="alert alert-success">${message}</div>
		<!-- 自动隐藏提示信息 -->
		<script type="text/javascript">
		setTimeout(function() {
			$('#message').hide('slow');
		}, 5000);
		</script>
	</c:if>
	<c:if test="${not empty error}">
		<div id="error" class="alert alert-error">${error}</div>
		<!-- 自动隐藏提示信息 -->
		<script type="text/javascript">
		setTimeout(function() {
			$('#error').hide('slow');
		}, 5000);
		</script>
	</c:if>
	<table width="100%" class="table table-bordered table-hover table-condensed">
		<thead>
			<tr>
				<th>任务ID</th>
				<th>任务名称</th>
				<th>流程实例ID</th>
				<th>流程定义ID</th>
				<th>父任务ID</th>
                <th>任务委派状态</th>
				<th>任务创建时间</th>
				<th>办理人</th>
				<th>操作
					<a href="#newTaskModal" data-toggle="modal" class="btn btn-primary btn-small" style="float:right;">
						<i class="icon-plus"></i>新任务</a>
				</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${tasks }" var="task">
				<tr>
					<td>${task.id }</td>
					<td>${task.name }</td>
					<td>${task.processInstanceId }</td>
					<td>${task.processDefinitionId }</td>
					<td style="text-align:center;">
						<c:if test="${empty task.parentTaskId}">无</c:if>
						<c:if test="${not empty task.parentTaskId}">
							<a href="getform/${task.parentTaskId}">${task.parentTaskId}</a>
						</c:if>
					</td>
                    <td style="text-align: center">
                        <c:if test="${task.delegationState == 'PENDING' }">
                            <i class="icon-ok"></i>被委派
                        </c:if>
                        <c:if test="${task.delegationState == 'RESOLVED' }">
                            <i class="icon-ok"></i>任务已处理完成
                        </c:if>
                    </td>
					<td>
						<fmt:formatDate value="${task.createTime }" pattern="yyyy-MM-dd hh:mm:ss"/>
					</td>
					<td>${task.assignee }</td>
					<td>
						<c:if test="${empty task.assignee }">
							<a class="btn" href="getform/${task.id}"><i class="icon-eye-open"></i>查看</a>
							<a class="btn" href="claim/${task.id}"><i class="icon-ok"></i>签收</a>
						</c:if>
						<c:if test="${not empty task.assignee }">
							<a class="btn" href="getform/${task.id}"><i class="icon-user"></i>办理</a>
							<a class="btn" href="unclaim/${task.id}"><i class="icon-remove"></i>反签收</a>
						</c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<!-- 添加子任务对话框 -->
	<div id="newTaskModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="newTaskModalLabel" aria-hidden="true">
	  <div class="modal-header">
	    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
	    <h3 id="newTaskModalLabel">新任务</h3>
	  </div>
	  <div class="modal-body">
	    <form id="newTaskForm" action="${ctx}/chapter6/task/new" class="form-horizontal" method="post">
	    	<div class="control-group">
		    	<label class="control-label" for="subTaskName">任务名称:</label>
				<div class="controls">
					<input type="text" name="taskName" class="required" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="description">任务描述:</label>
				<div class="controls">
					<textarea name="description" class="required"></textarea>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="priority">优先级:</label>
				<div class="controls">
					<select name="priority">
						<option value="0">低</option>
						<option value="50">中</option>
						<option value="100">高</option>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="dueDate">到期日:</label>
				<div class="controls">
					<input type="text" name="dueDate" data-date-format="yyyy-mm-dd" class="datepicker required" />
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
			    	<button type="submit" class="btn btn-primary">创建</button>
			    </div>
		    </div>
	    </form>
	  </div>
	</div>
</body>
</html>