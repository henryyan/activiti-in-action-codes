<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/common/global.jsp"%>
	<%@ include file="/common/meta.jsp" %>
	<%@ include file="/common/include-base-styles.jsp" %>
	<title>已部署流程定义列表--chapter5</title>
</head>
<body>
	<fieldset id="deployFieldset">
		<legend>部署流程资源</legend>
		<span class="alert alert-info"><b>支持文件格式：</b>zip、bar、bpmn、bpmn20.xml</span>
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
				<th>版本号</th>
				<th>XML资源名称</th>
				<th>图片资源名称</th>
				<th width="80">操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${processDefinitionList }" var="pd">
				<tr>
					<td>${pd.id }</td>
					<td>${pd.deploymentId }</td>
					<td>${pd.name }</td>
					<td>${pd.key }</td>
					<td>${pd.version }</td>
					<td><a target="_blank" href='${ctx }/chapter5/read-resource?pdid=${pd.id }&resourceName=${pd.resourceName }'>${pd.resourceName }</a></td>
					<td><a target="_blank" href='${ctx }/chapter5/read-resource?pdid=${pd.id }&resourceName=${pd.diagramResourceName }'>${pd.diagramResourceName }</a></td>
					<td><a target="_blank" href='${ctx }/chapter5/delete-deployment?deploymentId=${pd.deploymentId }'>删除</a></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</body>
</html>