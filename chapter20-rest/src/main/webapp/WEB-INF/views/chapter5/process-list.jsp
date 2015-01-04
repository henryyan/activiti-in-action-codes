<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/common/global.jsp" %>
    <%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/include-base-styles.jsp" %>
    <title>已部署流程定义列表--chapter5</title>
    <link rel="stylesheet" href="${ctx }/static/js/common/plugins/datetimepicker/bootstrap-datetimepicker.min.css">

    <%@ include file="/common/include-base-scripts.jsp" %>
    <script type="text/javascript"
            src="${ctx }/static/js/common/plugins/datetimepicker/bootstrap-datetimepicker.min.js"></script>
    <script type="text/javascript">
        var processDefinitionId;
        $(function () {
            $('#datetimepicker').datetimepicker();
            $('.resource-name').tooltip();
            $('input[name=execTime]').change(function () {
                if ($(this).val() == 'now') {
                    $('#changeStateForm input[name=effectiveDate]').val('');
                }
            });

            // 监听挂起、激活的click事件设置表单的processDefintionId值
            $('.change-state').click(function () {
                var state = $(this).data('state');
                var processDefinitionId = $(this).data('pid');
                $('#changeStateForm input[name=processDefinitionId]').val(processDefinitionId);
                $('.state').text(state == 'active' ? '激活' : '挂起');
                $('#changeStateForm').attr('action', ctx + '/chapter14/process/' + state);
            });

            // 触发设置候选属性
            $('.set-startable').click(function () {
                var pid = $(this).data('pid');
                processDefinitionId = pid;
                var rowIndex = $(this).parents('tr').attr('index');
                if ($('#startable-' + rowIndex).length == 1) {
                    var users = $('#startable-' + rowIndex).find('.users li');
                    var groups = $('#startable-' + rowIndex).find('.groups li');

                }

                // 打开对话框的时候先从后台读取已有配置
                $('input[name=user],input[name=group]').attr('checked', false);
                $.getJSON(ctx + '/chapter14/process/startable/read/' + pid, function(result) {
                    $(result.users).each(function() {
                       $('input[name=user][value=' + this + ']').attr('checked', true);
                    });
                    $(result.groups).each(function() {
                        $('input[name=group][value=' + this + ']').attr('checked', true);
                    });
                    $('#addStartableModal').modal();
                });
            });

            // 全选用户、组
            $('#selectAllUser').change(function() {
               $('input[name=user]').attr('checked', $(this).attr('checked') || false);
            });
            $('#selectAllGroup').change(function() {
                $('input[name=group]').attr('checked', $(this).attr('checked') || false);
            });

            // 设置候选属性
            $('#addStartableAttr').click(function () {
                var users = new Array();
                var groups = new Array();
                $('input[name=user]:checked').each(function() {
                    users.push($(this).val());
                });

                $('input[name=group]:checked').each(function() {
                    groups.push($(this).val());
                });

                // 发送请求
                $.post(ctx + "/chapter14/process/startable/set/" + processDefinitionId, {
                    users: users,
                    groups: groups
                }, function (resp) {
                    if (resp == 'true') {
                        location.reload();
                    } else {
                        alert('设置失败！');
                    }
                });
            });

            // 显示/隐藏候选属性
            $('.toggle-startable').click(function() {
                var index = $(this).data('index');
                $('#startable-' + index).fadeToggle();
            });

        });
    </script>
</head>
<body>
<c:if test="${not empty message}">
    <div id="message" class="alert alert-success">${message}</div>
    <!-- 自动隐藏提示信息 -->
    <script type="text/javascript">
        setTimeout(function () {
            $('#message').hide('slow');
        }, 5000);
    </script>
</c:if>
<fieldset id="deployFieldset">
    <legend>部署流程资源</legend>
    <form action="${ctx }/chapter5/deploy" method="post" enctype="multipart/form-data" style="margin-top:1em;">
        <input type="file" name="file"/>
        <input type="submit" value="Submit" class="btn btn-primary"/>
    </form>
    <hr class="soften"/>
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
        <th>XML</th>
        <th>流程图</th>
        <th width="40">状态</th>
        <th>候选启动</th>
        <th width="70">操作</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.result }" var="pd" varStatus="row">
        <tr index="${row.index}">
            <td>${pd.id }</td>
            <td>${pd.deploymentId }</td>
            <td>${pd.name }</td>
            <td>${pd.key }</td>
            <td>${pd.description}</td>
            <td style="text-align: center">${pd.version }</td>
            <td><a target="_blank" title="资源名称：${pd.resourceName }"
                   href='${ctx }/chapter5/read-resource?pdid=${pd.id }&resourceName=${pd.resourceName }'>查看</a>
            </td>
            <td><a target="_blank" title="资源名称：${pd.resourceName }，通过Diagram Viewer访问"
                   href='${ctx }/diagram-viewer/index.html?processDefinitionId=${pd.id}'>查看</a>
            </td>
            <td style="text-align: center">${pd.suspended ? '挂起' : '正常'}</td>
            <td>
                <c:if test="${not empty linksMap[pd.id]['user'] || not empty linksMap[pd.id]['group']}">
                    <a href="#" class="toggle-startable" data-index="${row.index}">
                        人[${fn:length(linksMap[pd.id]['user'])}],
                        组[${fn:length(linksMap[pd.id]['group'])}]
                    </a>
                </c:if>
            </td>
            <td>
                <div class="btn-group">
                    <a class="btn btn-small btn-danger dropdown-toggle" data-toggle="dropdown" href="#">操作
                        <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu" style="min-width: 100px;margin-left: -50px;">
                        <li><a href='${ctx }/chapter5/delete-deployment?deploymentId=${pd.deploymentId }'><i
                                class="glyphicon glyphicon-trash"></i>删除</a></li>
                        <li><a class="set-startable" data-pid="${pd.id}" title="设置候选启动人|组"><i class="glyphicon glyphicon-user"></i>候选启动</a></li>
                        <c:if test="${!pd.suspended}">
                            <li><a href="#changeStateModal" class="change-state" data-state="suspend"
                                   data-pid="${pd.id}" data-toggle="modal"><i class="glyphicon glyphicon-lock"></i>挂起</a></li>
                        </c:if>
                        <c:if test="${pd.suspended}">
                            <li><a href="#changeStateModal" class="change-state" data-state="active" data-pid="${pd.id}"
                                   data-toggle="modal"><i class="glyphicon glyphicon-ok"></i>激活</a></li>
                        </c:if>
                        <li><a href='${ctx }/chapter5/process/convert-to-model/${pd.id}'><i class="glyphicon glyphicon-edit"></i>转换为模型</a></li>
                    </ul>
                </div>
            </td>
        </tr>

    </c:forEach>
    </tbody>
</table>
<tags:pagination page="${page}" paginationSize="${page.pageSize}"/>

<!-- 流程定义挂起 -->
<div id="changeStateModal" class="modal fade" tabindex="-1" role="dialog"
     aria-labelledby="changeStateModalModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3 id="changeStateModalLabel"><span class="state"></span>流程定义</h3>
            </div>
            <div class="modal-body">
                <form id="changeStateForm" class="form-horizontal" method="post">
                    <input type="hidden" name="processDefinitionId"/>

                    <div class="control-group">
                        <label class="control-label">何时执行:</label>

                        <div class="controls">
                            <label class="radio">
                                <input type="radio" name="execTime" id="optionsRadios1" value="now" checked>
                                现在
                            </label>
                            <label class="radio">
                                <input type="radio" name="execTime" id="optionsRadios2" value="timer">
                                定时：
                                <div id="datetimepicker" class="input-append date">
                                    <input data-format="yyyy-MM-dd hh:mm:ss" name="effectiveDate" class="input-medium"
                                           type="text"/>
                                        <span class="add-on">
                                            <i data-time-icon="glyphicon glyphicon-time" data-date-icon="glyphicon glyphicon-calendar"></i>
                                        </span>
                                </div>
                            </label>
                        </div>
                    </div>
                    <div class="control-group">
                        <div class="controls">
                            <label class="checkbox">
                                <input type="checkbox" name="cascade" checked="checked"/>同时挂起所有与流程定义相关的流程实例？
                            </label>
                        </div>
                    </div>
                    <div class="control-group">
                        <div class="controls">
                            <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
                            <button type="submit" class="btn btn-primary">确定<span class="state"></span></button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- 流程定义挂起 -->
<div id="addStartableModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="addStartableModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3 id="addStartableModalLabel"><span class="state"></span>设置候选启动人|组</h3>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-xs-5">
                        <label class="checkbox">
                            <input type="checkbox" id="selectAllUser" />候选启动（人）
                        </label>
                        <hr>
                        <c:forEach items="${users}" var="user">
                            <label class="checkbox"><input type="checkbox" value="${user.id}" name="user" />${user.firstName} ${user.lastName}</label>
                        </c:forEach>
                    </div>
                    <div class="col-xs-5">
                        <label class="checkbox">
                            <input type="checkbox" id="selectAllGroup" />候选启动（组）
                        </label>
                        <hr>
                        <c:forEach items="${groups}" var="group">
                            <label class="checkbox"><input type="checkbox" value="${group.id}" name="group" />${group.name}</label>
                        </c:forEach>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
                <button type="button" id="addStartableAttr" class="btn btn-primary">确定<span class="state"></span></button>
            </div>
        </div>
    </div>
</div>
</body>
</html>