<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.activiti.engine.form.*, org.apache.commons.lang3.*" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/common/global.jsp"%>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/include-base-styles.jsp" %>
	<title>已归档任务办理</title>
	<style type="text/css">
	.task-infos {margin-left: 20px;}
	.task-infos span {margin-right: 20px;}
	#commentList span {padding: 0 5px 0 5px;}
	#commentList ol {margin-left: 3em;}
	</style>

	<script type="text/javascript" src="${ctx }/js/common/jquery.js"></script>
	<script type="text/javascript" src="${ctx }/js/common/bootstrap.min.js"></script>
	<script type="text/javascript" src="${ctx }/js/common/bootstrap-datepicker.js"></script>
	<script type="text/javascript">
		var taskId = '${task.id}';
		var processInstanceId = '${task.processInstanceId}';
	</script>
	<script type="text/javascript" src="${ctx }/js/modules/chapter12/events.js"></script>
	<script type="text/javascript">
	$(function() {
		$('.attachment-item').tooltip();
		// 读取事件列表
		readComments();
	});
	</script>
</head>
<body>
	<h3>任务—[${task.name}]
		<c:if test="${not empty task.processDefinitionId}">
			，流程定义ID：[${task.processDefinitionId}]
		</c:if>
	</h3>
	<!-- 任务属性 -->
	<p class="task-infos text-left">
		<span><i class="glyphicon glyphicon-calendar"></i>到期日：
			<span class='due-date'>
				<c:if test="${empty task.dueDate}">无到期日</c:if>
				<c:if test="${not empty task.dueDate}">
					<fmt:formatDate value="${task.dueDate}" pattern="yyyy-MM-dd"/>
				</c:if>
			</span>
		</span>
		<span><i class="glyphicon glyphicon-flag"></i>优先级：
			<span class='priority'>
				<c:if test="${empty task.priority}">无到期日</c:if>
				<c:if test="${not empty task.priority}">
					<c:if test="${task.priority == 0}">低</c:if>
					<c:if test="${task.priority == 50}">中</c:if>
					<c:if test="${task.priority == 100}">高</c:if>
				</c:if>
			</span>
		</span>
		<span><i class="glyphicon glyphicon-calendar"></i>创建日期：
			<fmt:formatDate value="${task.startTime}" pattern="yyyy-MM-dd hh:mm:ss"/>
		</span>
		<span><i class="glyphicon glyphicon-calendar"></i>完成日期：
			<fmt:formatDate value="${task.endTime}" pattern="yyyy-MM-dd hh:mm:ss"/>
		</span>
		<c:if test="${not empty parentTask}">
			<hr/>
			<%-- 运行中任务 --%>
			<c:if test="${empty parentTask.endTime}">
				<div>
					该任务属于：<a href="${ctx}/chapter6/task/getform/${parentTask.id}">${parentTask.name}</a>
				</div>
			</c:if>

			<%-- 已结束任务 --%>
			<c:if test="${not empty parentTask.endTime}">
				<div>
					该任务属于：<a href="${ctx}/chapter6/task/archived/${parentTask.id}">${parentTask.name}</a>
				</div>
			</c:if>
		</c:if>
	</p>
	<hr/>
	<%-- 任务描述 --%>
	<c:if test="${empty task.description}">
		<p class="text-muted">该任务无描述。</p>
	</c:if>
	<c:if test="${not empty task.description}">
		<p class="text-infos">${task.description}</p>
	</c:if>

	<hr/>
	<p class="task-infos text-left">
		<h4 style="display:inline;">任务人员</h4>
		<div class="row">
			<div class="span4">
				<span><i class="glyphicon glyphicon-user"></i>拥有人：<span id="owner">${empty task.owner ? '无' : task.owner}</span></span>
			</div>
			<div class="span4">
				<span><i class="glyphicon glyphicon-user"></i>办理人：<span id="assignee">${empty task.assignee ? '无' : task.assignee}</span></span>
			</div>
		</div>
	</p>
	<hr/>

	<!-- 子任务 -->
	<p class="text-left">
		<h4 style="display:inline;">子任务</h4>
		<ul class="unstyled">
			<c:forEach items="${subTasks}" var="subTask">
			<li>
				<%-- 根据任务完成状态显示不同的图标 --%>
				<i class="glyphicon glyphicon-${empty subTask.endTime ? 'tasks' : 'ok'}"></i>

				<c:if test="${empty subTask.endTime}">
					<a href="${ctx}/chapter6/task/getform/${subTask.id}" class="task-name">${subTask.name}</a>
				</c:if>

				<c:if test="${not empty subTask.endTime}">
					<a href="${ctx}/chapter6/task/archived/${subTask.id}" class="task-name">
					${subTask.name}（完成时间：<fmt:formatDate value="${subTask.endTime}" pattern="yyyy-MM-dd hh:mm:ss"/>）</a>
				</c:if>
			</li>
		</c:forEach>
		</ul>
	</p>
	<hr/>

	<!-- 附件 -->
	<div>
		<h4 style="display:inline;">相关附件</h4>
		<ul class="unstyled">
			<c:forEach items="${attachments}" var="attachment">
			<li>
				<%-- url类型的文件 --%>
				<c:if test="${attachment.type == 'url'}">
					<a href="${attachment.url}" target="_blank" data-toggle="tooltip" title="地址：${attachment.url}<br/>描述：${attachment.description}" data-placement="right" class="attachment-item"><i class="glyphicon glyphicon-share"></i>${attachment.name}</a>
				</c:if>

				<%-- 文件类型的文件 --%>
				<c:if test="${attachment.type != 'url'}">
					<a href="${ctx}/chapter12/attachment/download/${attachment.id}" target="_blank" data-toggle="tooltip" title="${attachment.description}" data-placement="right" class="attachment-item"><i class="glyphicon glyphicon-file"></i>${attachment.name}</a>
				</c:if>
			</li>
		</c:forEach>
		</ul>
	</div>
	<hr/>
	<%-- 事件区域，参考events.js --%>
	<fieldset id="commentList">
		<legend>事件列表</legend>
		<%-- 列表是通过js发送ajax请求后动态生成的 --%>
		<ol></ol>
	</fieldset>
</body>
</html>