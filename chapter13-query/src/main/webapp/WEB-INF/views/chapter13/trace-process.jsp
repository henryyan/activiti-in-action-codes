<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/common/global.jsp" %>
    <%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/include-base-styles.jsp" %>
    <title>流程跟踪--${historicProcessInstance.processInstanceId}</title>
    <style type="text/css">
        #pinfo th {background: #f7f7f9;}
    </style>

    <script src="${ctx }/js/common/jquery.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctx }/js/common/bootstrap.min.js"></script>
    <script type="text/javascript">
        var processInstanceId = '${historicProcessInstance.processInstanceId}';
        var executionId = '${executionId}';
    </script>
    <script type="text/javascript" src="${ctx }/js/modules/chapter13/trace-process.js"></script>
</head>
<body>${activeActivities}
    <%-- 先读取图片再通过Javascript定位 --%>
    <div>
        <img id="processDiagram" src="${ctx }/chapter5/read-resource?pdid=${historicProcessInstance.processDefinitionId}&resourceName=${processDefinition.diagramResourceName}" />
    </div>

    <%-- 通过引擎自动生成图片并用红色边框标注
    <div>
        <img id="processDiagramAuto" src="${ctx }/chapter13/process/trace/data/auto/${historicProcessInstance.processInstanceId}" />
    </div>--%>
    <hr>
    <fieldset>
        <legend>流程综合信息-【${processDefinition.name}】<button id="changeToAutoDiagram" class="btn btn-info">坐标错位请点击这里</button></legend>
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