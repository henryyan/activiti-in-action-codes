<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/common/global.jsp" %>
    <%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/include-base-styles.jsp" %>
    <title>历史流程--${historicProcessInstance.processInstanceId}</title>
    <style type="text/css">
        .title {font-size: 20px; font-weight: bold;}
    </style>
</head>
<body>
<p class="title text-center text-info">
    已归档流程实例信息
    <a class="btn" style="float:right;" href="javascript:history.back();"><i class="glyphicon glyphicon-circle-arrow-left"></i>返回列表</a>
</p>
<fieldset>
    <legend>流程综合信息-【${processDefinition.name}】-${historicProcessInstance.processInstanceId}</legend>
    <table id="pinfo" class="table table-bordered table-hover table-condensed">
        <tr>
            <th width="100">流程ID</th>
            <td>
                ${historicProcessInstance.id}
                <c:if test="${not empty parentProcessInstance.id}"><a href="${parentProcessInstance.id}" style="margin-left: 2em;">父流程：${parentProcessInstance.id}</a></c:if>
            </td>
            <th width="100">流程定义ID</th>
            <td>${historicProcessInstance.processDefinitionId}</td>
            <th width="100">业务KEY</th>
            <td>${historicProcessInstance.businessKey}</td>
        </tr>
        <tr>
            <th width="100">流程启动时间</th>
            <td><fmt:formatDate value="${historicProcessInstance.startTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
            <th width="100">流程结束时间</th>
            <td><fmt:formatDate value="${historicProcessInstance.endTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
            <th width="100">流程状态</th>
            <td>${empty historicProcessInstance.endTime ? '未结束': '已结束'}</td>
        </tr>
    </table>
</fieldset>
<fieldset>
    <legend>活动记录</legend>
    <table width="100%" class="table table-bordered table-hover table-condensed">
        <thead>
        <tr>
            <th>活动ID</th>
            <th>活动名称</th>
            <th>活动类型</th>
            <th>任务ID</th>
            <th>办理人</th>
            <th>活动开始时间</th>
            <td>活动结束时间</td>
            <td>活动耗时（秒）</td>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${activities}" var="activity">
            <tr>
                <td>${activity.id}</td>
                <td>${activity.activityName}</td>
                <td>${activity.activityType}</td>
                <td>${activity.taskId}</td>
                <td>${activity.assignee}</td>
                <td><fmt:formatDate value="${activity.startTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
                <td><fmt:formatDate value="${activity.endTime}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
                <td>${activity.durationInMillis / 1000}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</fieldset>
<fieldset>
    <legend>表单属性</legend>
    <table width="100%" class="table table-bordered table-hover table-condensed">
        <thead>
        <tr>
            <th>属性名称</th>
            <th>属性值</th>
            <th>任务ID</th>
            <th>设置时间</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${formProperties}" var="prop">
            <tr>
                <td>${prop.propertyId}</td>
                <td>${prop.propertyValue}</td>
                <td>${prop.taskId}</td>
                <td><fmt:formatDate value="${prop.time}" pattern="yyyy-MM-dd hh:mm:ss"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</fieldset>
<fieldset>
    <legend>相关变量</legend>
    <table width="100%" class="table table-bordered table-hover table-condensed">
        <thead>
        <tr>
            <th>变量名称</th>
            <th>变量类型</th>
            <th>值</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${variableInstances}" var="var">
            <tr>
                <td>${var.variableName}</td>
                <td>${var.variableType.typeName}</td>
                <td>${var.value}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</fieldset>
</body>
</html>