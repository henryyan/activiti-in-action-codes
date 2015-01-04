<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/common/global.jsp"%>
    <%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/include-base-styles.jsp" %>
    <title>已结束流程实例列表--chapter13</title>
    <style type="text/css">
        div.datepicker {z-index: 10000;}
    </style>

    <script src="${ctx }/js/common/jquery.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctx }/js/common/bootstrap.min.js"></script>
    <script type="text/javascript" src="${ctx }/js/common/bootstrap-datepicker.js"></script>
</head>
<body>
<div class='page-title ui-corner-all'>已归档流程实例</div>
<table width="100%" class="table table-bordered table-hover table-condensed">
    <thead>
    <tr>
        <th>流程实例ID</th>
        <th>所属流程</th>
        <th>流程定义ID</th>
        <th>启动时间</th>
        <th>流程启动人</th>
        <th>结束时间</th>
        <th>父流程ID</th>
        <th>结束原因</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.result }" var="hp">
        <tr>
            <td><a href="${ctx}/chapter13/history/process/finished/view/${hp.processInstanceId}">${hp.processInstanceId}</a></td>
            <td>${definitions[hp.processDefinitionId].name}</td>
            <td>${hp.processDefinitionId}</td>
            <td><fmt:formatDate value="${hp.startTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
            <td>${hp.startUserId}</td>
            <td><fmt:formatDate value="${hp.endTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
            <td><a href="${ctx}/chapter13/history/process/finished/view/${hp.superProcessInstanceId}">${hp.superProcessInstanceId}</a></td>
            <td>${hp.deleteReason}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<tags:pagination page="${page}" paginationSize="${page.pageSize}"/>
</div>
</body>
</html>