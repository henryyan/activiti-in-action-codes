<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/common/global.jsp"%>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/include-base-styles.jsp" %>
	<title>已部署流程定义列表--chapter5</title>

	<script src="${ctx }/js/common/jquery.js" type="text/javascript"></script>
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
	<fieldset id="deployFieldset">
		<legend>部署流程资源</legend>
		<form action="${ctx }/chapter5/deploy" method="post" enctype="multipart/form-data" style="margin-top:1em;">
			<input type="file" name="file" />
			<input type="submit" value="Submit" class="btn" />
		</form>
		<hr class="soften" />
	</fieldset>
	<table width="100%" class="table table-bordered table-hover table-condensed">
		<thead>
			<tr>
				<th>流程定义ID</th>
				<th>部署ID</th>
				<th>流程定义名称</th>
				<th>流程定义KEY</th>
				<th>流程描述</th>
				<th>版本号</th>
				<th>XML资源名称</th>
				<th>图片资源名称</th>
				<th width="120">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${processDefinitionList }" var="pd">
				<tr>
					<td>${pd.id }</td>
					<td>${pd.deploymentId }</td>
					<td>${pd.name }</td>
					<td>${pd.key }</td>
					<td>${pd.description}</td>
					<td>${pd.version }</td>
					<td><a target="_blank" href='${ctx }/chapter5/read-resource?pdid=${pd.id }&resourceName=${pd.resourceName }'>${pd.resourceName }</a></td>
					<td><a target="_blank" href='${ctx }/chapter5/read-resource?pdid=${pd.id }&resourceName=${pd.diagramResourceName }'>${pd.diagramResourceName }</a></td>
					<td>
						<a class="btn btn-small" href='${ctx }/chapter5/delete-deployment?deploymentId=${pd.deploymentId }'><i class="icon-trash"></i>删除</a>
						<a class="btn btn-small" href='${ctx }/chapter6/getform/start/${pd.id }'><i class="icon-play"></i>启动</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>