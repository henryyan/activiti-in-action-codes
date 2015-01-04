<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/common/global.jsp"%>
    <%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/include-base-styles.jsp" %>
    <title>事件日志--chapter21</title>

    <%@ include file="/common/include-base-scripts.jsp" %>
    <style>
    </style>
    <script>
        var logDatas = {};
        <c:forEach items="${datas}" var="d">
            logDatas[${d.key}] = ${d.value};
        </c:forEach>
        var currentLogBtn = null;
        $(function() {
            $('.view-log').click(function() {
                currentLogBtn = this;
                var logNumber = $(this).data('lognumber');
                var data = logDatas[logNumber];
                data['logId'] = $('#dataTable tr').eq($('.view-log').index(currentLogBtn) + 1).find('td:eq(0)').text();
                var dl = $('#viewLogDataModal').find('dl').html('');
                $.each(data, function(k, v) {
                    $('<dt/>').html(k).appendTo(dl);
                    var value = v;
                    if (k == 'variables') {
                        value = '';
                        $.each(v, function(sk, sv) {
                            if (value != '') {
                                value += "<br/>";
                            }
                            value += "<strong>" + sk + "</strong>: " + sv;
                        });
                    }
                    $('<dd/>').html(value || '&nbsp;').appendTo(dl);
                });
                $('#viewLogDataModal').modal('show');
            });

            // 上一条、下一条切换
            $('.log-prev').click(function() {
                var index = $('.view-log').index(currentLogBtn);
                if (index == 0) {
                    currentLogBtn = $('.view-log').eq($('.view-log').length - 1);
                    currentLogBtn.trigger('click');
                } else {
                    $('.view-log').eq(index - 1).trigger('click');
                }
            });
            $('.log-next').click(function() {
                var index = $('.view-log').index(currentLogBtn);
                if (index + 1 == $('.view-log').length) {
                    currentLogBtn = $('.view-log').eq(0);
                    currentLogBtn.trigger('click');
                } else {
                    $('.view-log').eq(index + 1).trigger('click');
                }
            });
        });
    </script>
</head>
<body>
<table id="dataTable" width="100%" class="table table-bordered table-hover table-condensed">
    <thead>
    <tr>
        <th>日志ID</th>
        <th>流程实例ID</th>
        <th>执行ID</th>
        <th>流程定义ID</th>
        <th>任务ID</th>
        <th>发生时间</th>
        <th>类型</th>
        <th>操作人</th>
        <th>日志内容</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.result }" var="e">
        <tr>
            <td>${e.id}</td>
            <td>${e.processInstanceId}</td>
            <td>${e.executionId}</td>
            <td>${e.processDefinitionId}</td>
            <td>${e.taskId}</td>
            <td>${e.timeStamp}</td>
            <td>${e.type}</td>
            <td>${e.userId}</td>
            <td>
                <button type="button" class="view-log btn btn-sm btn-info" data-lognumber="${e.logNumber}">查看</button>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<tags:pagination page="${page}" paginationSize="${page.pageSize}"/>

<!-- 查看日志详细对话框 -->
<div id="viewLogDataModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="viewLogDataModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" style="padding-bottom: 2em;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3 id="viewLogDataModalLabel">
                    查看日志详细
                    <span style="padding-left: 5em;">
                        <button type="button" class="btn btn-sm btn-primary log-prev"> <i class="glyphicon glyphicon-chevron-left"></i> 上一条</button>
                        <button type="button" class="btn btn-sm btn-primary log-next"> <i class="glyphicon glyphicon-chevron-right"></i> 下一条</button>
                    </span>
                </h3>
            </div>
            <div class="modal-body">
                <dl class="dl-horizontal"></dl>
            </div>
        </div>
    </div>
</div>
</body>
</html>