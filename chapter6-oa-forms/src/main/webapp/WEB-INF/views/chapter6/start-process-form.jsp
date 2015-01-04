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
				<c:if test="${fp.type.name == 'string' || fp.type.name == 'long'}">
					<label class="control-label" for="${fp.id}">${fp.name}:</label>
					<div class="controls">
						<input type="text" id="${fp.id}" name="${fp.id}" data-type="${fp.type.name}" value="" />
					</div>
				</c:if>

				<%-- 日期 --%>
				<c:if test="${fp.type.name == 'date'}">
					<label class="control-label" for="${fp.id}">${fp.name}:</label>
					<div class="controls">
						<input type="text" id="${fp.id}" name="${fp.id}" class="datepicker"  data-type="${fp.type.name}" data-date-format="${fn:toLowerCase(datePattern)}" />
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
				<button type="submit" class="btn"><i class="icon-play"></i>启动流程</button>
			</div>
		</div>
	</form>
</body>
</html>