<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.activiti.engine.form.*, org.apache.commons.lang3.*" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/common/global.jsp"%>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/include-base-styles.jsp" %>
	<title>任务办理</title>
	<script type="text/javascript" src="${ctx }/js/common/jquery.js"></script>
	<script type="text/javascript" src="${ctx }/js/common/bootstrap.min.js"></script>
	<script type="text/javascript" src="${ctx }/js/common/bootstrap-datepicker.js"></script>
	<script type="text/javascript">
	$(function() {
		$('.datepicker').datepicker();

		readComments();

		// 保存意见
		$('#saveComment').click(function() {
			if (!$('#comment').val()) {
				return false;
			}
			$.post(ctx + '/chapter9/comment/save', {
				taskId: '${hasFormKey ? task.id : taskFormData.task.id}',
				processInstanceId: '${hasFormKey ? task.processInstanceId : taskFormData.task.processInstanceId}',
				message: $('#comment').val()
			}, function(resp) {
				readComments();
			});
		});
	});

	/**
	 * 读取意见列表
	 * @return {[type]} [description]
	 */
	function readComments() {
		$('#commentList ol').html('');
		// 读取意见
		$.getJSON(ctx + '/chapter9/comment/list/${hasFormKey ? task.processInstanceId : taskFormData.task.processInstanceId}', function(datas) {
			$.each(datas.comments, function(i, v) {
				$('<li/>', {
					html: function() {
						var content = v.fullMessage;
						content += "<span style='margin-left:1em;'></span>" + v.userId + "（" + datas.taskNames[v.taskId] + "）";
						content += "<span style='margin-left:1em;'></span>" + new Date(v.time).toLocaleString();
						return content;
					}
				}).appendTo('#commentList ol')
			});
		});
	}
	</script>
</head>
<body>
	<h3>任务办理—[${hasFormKey ? task.name : taskFormData.task.name}]，流程定义ID：[${hasFormKey ? task.processDefinitionId : taskFormData.task.processDefinitionId}]</h3>
	<hr/>
	<form action="${ctx }/chapter6/task/complete/${hasFormKey ? task.id : taskFormData.task.id}" class="form-horizontal" method="post">
		<c:if test="${hasFormKey}">
			${taskFormData}
		</c:if>
		<c:if test="${!hasFormKey}">
		<c:forEach items="${taskFormData.formProperties}" var="fp">
			<c:set var="fpo" value="${fp}"/>
			<c:set var="disabled" value="${fp.writable ? '' : 'disabled'}" />
			<c:set var="readonly" value="${fp.writable ? '' : 'readonly'}" />
			<c:set var="required" value="${fp.required ? 'required' : ''}" />
			<%
				// 把需要获取的属性读取并设置到pageContext域
				FormType type = ((FormProperty)pageContext.getAttribute("fpo")).getType();
				String[] keys = {"datePattern", "values"};
				for (String key: keys) {
					pageContext.setAttribute(key, type.getInformation(key));
				}
			%>
			<div class="control-group">
			<%-- 文本或者数字类型 --%>
			<c:if test="${fp.type.name == 'string' || fp.type.name == 'long' || fp.type.name == 'double'}">
				<label class="control-label" for="${fp.id}">${fp.name}:</label>
				<div class="controls">
					<input type="text" id="${fp.id}" name="${fp.id}" data-type="${fp.type.name}" value="${fp.value}" ${readonly} ${required} />
				</div>
			</c:if>

			<%-- 大文本 --%>
			<c:if test="${fp.type.name == 'bigtext'}">
				<label class="control-label" for="${fp.id}">${fp.name}:</label>
				<div class="controls">
					<textarea id="${fp.id}" name="${fp.id}" data-type="${fp.type.name}" ${readonly} ${required}>${fp.value}</textarea>
				</div>
			</c:if>

			<%-- 日期 --%>
			<c:if test="${fp.type.name == 'date'}">
				<label class="control-label" for="${fp.id}">${fp.name}:</label>
				<div class="controls">
					<input type="text" id="${fp.id}" name="${fp.id}" class="datepicker" value="${fp.value}" data-type="${fp.type.name}" data-date-format="${fn:toLowerCase(datePattern)}" ${readonly} ${required}/>
				</div>
			</c:if>

			<%-- 下拉框 --%>
			<c:if test="${fp.type.name == 'enum'}">
				<label class="control-label" for="${fp.id}">${fp.name}:</label>
				<div class="controls">
					<select name="${fp.id}" id="${fp.id}" ${disabled} ${required}>
						<c:forEach items="${values}" var="item">
							<option value="${item.key}" <c:if test="${item.value == fp.value}">selected</c:if>>${item.value}</option>
						</c:forEach>
					</select>
				</div>
			</c:if>

			<%-- Javascript --%>
			<c:if test="${fp.type.name == 'javascript'}">
				<script type="text/javascript">${fp.value};</script>
			</c:if>

			</div>
		</c:forEach>
		</c:if>

		<%-- 按钮区域 --%>
		<div class="control-group">
			<div class="controls">
				<a href="javascript:history.back();" class="btn"><i class="icon-backward"></i>返回列表</a>
				<button type="submit" class="btn btn-primary"><i class="icon-ok"></i>完成任务</button>
			</div>
		</div>
	</form>

	<div class="container-fluid">
	  <div class="row-fluid">
	    <div class="span2">
	      <!-- 添加意见 -->
			<fieldset id="commentContainer" style="margin-left: 5em;">
				<legend>添加意见</legend>
				<textarea id="comment" rows="3"></textarea>
				<button id="saveComment" type="button" class="btn"><i class="icon-plus"></i>保存意见<i class="icon-comment"></i></button>
			</fieldset>
	    </div>
	    <div class="span10">
	      <fieldset id="commentList" style="margin-left: 12em;">
			<legend>意见列表</legend>
			<ol></ol>		
		  </fieldset>
	    </div>
	  </div>
	</div>
</body>
</html>