<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="org.activiti.engine.form.*, org.apache.commons.lang3.*" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/common/global.jsp"%>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/include-base-styles.jsp" %>
	<title>启动流程</title>
	<script type="text/javascript" src="${ctx }/js/common/jquery.js"></script>
	<script type="text/javascript" src="${ctx }/js/common/bootstrap.min.js"></script>
	<script type="text/javascript" src="${ctx }/js/common/bootstrap-datepicker.js"></script>
	<script type="text/javascript">
	$(function() {
		$('.datepicker').datepicker();

		// 选择人对话框
		$('#userModal').on('show', function() {
			$('#userModal select').html('');
			$.getJSON(ctx + '/user/list', function(datas) {
				$.each(datas, function(k, v) {
					var $opg = $('<optgroup/>', {
						label: k
					}).appendTo('#userModal select');
					$.each(v, function() {
						$('<option/>', {
							'value': this.id,
							'text': this.firstName + ' ' + this.lastName + '（' + this.id + '）'
						}).appendTo($opg);
					});
				});
			});
		});

		// 单击打开选择人对话框
		$('.users').live('click', function() {
			$('body').data('usersEle', this);
			$('#userModal').modal('show');
		});

		$('#userModal .ok').click(function() {
			var ele = $('body').data('usersEle');
			var users = new Array();
			$('#userModal select option:selected').each(function() {
				users.push($(this).val());
			});
			$(ele).val(users);
			$('#userModal').modal('hide');
		});
	});
	</script>
</head>
<body>
	<h3>启动流程—
		<c:if test="${hasStartFormKey}">
			[${processDefinition.name}]，版本号：${processDefinition.version}
		</c:if>
		<c:if test="${!hasStartFormKey}">
			[${startFormData.processDefinition.name}]，版本号：${startFormData.processDefinition.version}
		</c:if>
	</h3>
	<hr/>
	<form action="${ctx }/chapter6/process-instance/start/${processDefinitionId}" class="form-horizontal" method="post">
		<c:if test="${hasStartFormKey}">
		${startFormData}
		</c:if>

		<c:if test="${!hasStartFormKey}">
			<c:forEach items="${startFormData.formProperties}" var="fp">
				<c:set var="fpo" value="${fp}"/>
				<c:set var="required" value="${fp.required ? 'required' : ''}" />
				<%
					// 把需要获取的属性读取并设置到pageContext域
					FormType type = ((FormProperty)pageContext.getAttribute("fpo")).getType();
					String[] keys = {"datePattern"};
					for (String key: keys) {
						pageContext.setAttribute(key, ObjectUtils.toString(type.getInformation(key)));
					}
				%>
				<div class="control-group">
				<%-- 文本或者数字类型 --%>
				<c:if test="${fp.type.name == 'string' || fp.type.name == 'double' || fp.type.name == 'long'}">
					<label class="control-label" for="${fp.id}">${fp.name}:</label>
					<div class="controls">
						<input type="text" id="${fp.id}" name="${fp.id}" data-type="${fp.type.name}" ${required}/>
					</div>
				</c:if>

				<%-- 大文本 --%>
				<c:if test="${fp.type.name == 'bigtext'}">
					<label class="control-label" for="${fp.id}">${fp.name}:</label>
					<div class="controls">
						<textarea id="${fp.id}" name="${fp.id}" data-type="${fp.type.name}" ${required}></textarea>
					</div>
				</c:if>

				<%-- 日期 --%>
				<c:if test="${fp.type.name == 'date'}">
					<label class="control-label" for="${fp.id}">${fp.name}:</label>
					<div class="controls">
						<input type="text" id="${fp.id}" name="${fp.id}" class="datepicker" data-type="${fp.type.name}" data-date-format="${fn:toLowerCase(datePattern)}" ${required} />
					</div>
				</c:if>

				<%-- Javascript --%>
				<c:if test="${fp.type.name == 'javascript'}">
					<script type="text/javascript">${fp.value};</script>
				</c:if>

				<%-- 选择人员 --%>
				<c:if test="${fp.type.name == 'users'}">
					<label class="control-label" for="${fp.id}">${fp.name}:</label>
					<div class="controls">
						<input type="text" id="${fp.id}" name="${fp.id}" data-type="${fp.type.name}" class="users"readonly />
					</div>
				</c:if>
				</div>
			</c:forEach>
		</c:if>

		<%-- 按钮区域 --%>
		<div class="control-group">
			<div class="controls">
				<a href="javascript:history.back();" class="btn"><i class="icon-backward"></i>返回列表</a>
				<button type="submit" class="btn"><i class="icon-play"></i>启动流程</button>
			</div>
		</div>
	</form>

	<div id="userModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="userModalLabel" aria-hidden="true" style="width: 260px;">
	  <div class="modal-header">
	    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
	    <h3 id="userModalLabel">选择人员</h3>
	  </div>
	  <div class="modal-body">
	  	<select multiple="multiple" style="height: 200px;"></select>
	  </div>
	  <div class="modal-footer">
	    <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
	    <button class="btn btn-primary ok">确定</button>
	  </div>
	</div>
</body>
</html>