<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/common/global.jsp"%>
    <%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/include-base-styles.jsp" %>
    <title>所有正在运行的任务--chapter13</title>

    <script src="${ctx }/js/common/jquery.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctx }/js/common/bootstrap.min.js"></script>
</head>
<body>
<div>
    <form action="${ctx}/chapter13/query/task/running">
        流程名称：
        <select name="processKey">
            <option value="">全部</option>
            <c:forEach items="${processes}" var="process">
                <option value="${process.key}" ${process.key == param.processKey ? 'selected' : ''}>${process.name}</option>
            </c:forEach>
        </select>
        <button class="btn">查询</button>
    </form>
</div>
<table width="100%" class="table table-bordered table-hover table-condensed">
    <thead>
    <tr>
        <th>任务ID</th>
        <th>任务名称</th>
        <th>办理人</th>
        <th>流程实例ID</th>
        <th>流程名称</th>
        <th>流程定义ID</th>
        <th>创建时间</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${tasks }" var="task">
        <tr>
            <td>${task.id}</td>
            <td>${task.name}</td>
            <td>${task.assignee}</td>
            <td>${task.processInstanceId}</td>
            <td>${task.processName}</td>
            <td>${task.processDefinitionId}</td>
            <td>${task.createTime}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</div>
</body>
</html>