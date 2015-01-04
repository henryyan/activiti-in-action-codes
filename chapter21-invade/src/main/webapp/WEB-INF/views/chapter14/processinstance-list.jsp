<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/common/global.jsp"%>
    <%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/include-base-styles.jsp" %>
    <script src="${ctx }/js/common/jquery.js" type="text/javascript"></script>
    <style type="text/css">
        .state-btn {float: right;}
    </style>
    <title>流程实例列表--chapter14</title>
    <script type="text/javascript">
        $(function() {
           $('.delete-btn').click(function() {
              var reason = prompt("请输入删除原因：");
               $.post(ctx + '/chapter14/processinstance/delete/' + $(this).data('pid'), {
                   deleteReason: reason
               }, function(resp) {
                   if (resp) {
                       location.reload();
                   } else {
                       alert('删除失败！');
                   }
               });
           });
        });
    </script>
</head>
<body>
<div class='page-title'>运行中流程实例</div>
<table width="100%" class="table table-bordered table-hover table-condensed">
    <thead>
    <tr>
        <th>流程实例ID</th>
        <th>流程定义ID</th>
        <th>流程名称</th>
        <th>流程版本</th>
        <th>业务KEY</th>
        <th>状态</th>
        <th>操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.result }" var="pi">
        <tr>
            <td>
                <a href="${ctx}/chapter13/process/trace/view/${pi.id}" target="_blank">${pi.id}</a>
            </td>
            <td>${pi.processDefinitionId}</td>
            <td>${definitions[pi.processDefinitionId].name}</td>
            <td>${definitions[pi.processDefinitionId].version}</td>
            <td>${pi.businessKey}</td>
            <td>
                ${pi.suspended ? '挂起' : '正常'}
                <c:if test="${pi.suspended}">
                    <a class="btn btn-small state-btn" href="${ctx }/chapter14/processinstance/active/${pi.id}"><i class="glyphicon glyphicon-ok"></i>激活</a>
                </c:if>
                <c:if test="${!pi.suspended}">
                    <a class="btn btn-small state-btn" href="${ctx }/chapter14/processinstance/suspend/${pi.id}"><i class="glyphicon glyphicon-lock"></i>挂起</a>
                </c:if>
            </td>
            <td>
                <a class="btn btn-small btn-danger delete-btn" data-pid="${pi.processInstanceId}"><i class="glyphicon glyphicon-remove"></i>删除</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<tags:pagination page="${page}" paginationSize="${page.pageSize}"/>
</div>
</body>
</html>