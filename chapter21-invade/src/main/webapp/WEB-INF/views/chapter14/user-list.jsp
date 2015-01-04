<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/common/global.jsp"%>
    <%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/include-base-styles.jsp" %>

    <%@ include file="/common/include-base-scripts.jsp" %>
    <title>用户列表--chapter14</title>
    <script type="text/javascript">
        $(function() {
            $('.edit-user').click(function() {
                var $tr = $('#' + $(this).data('id'));
                $('#userId').val($tr.find('.prop-id').text());
                $('#firstName').val($tr.find('.prop-firstName').text());
                $('#lastName').val($tr.find('.prop-lastName').text());
                $('#email').val($tr.find('.prop-email').text());
            });

            // 组提示
            $('.groups').tooltip();

            // 点击按钮设置用户的组
            $('.set-group').click(function() {
                $('#setGroupsModal input[name=userId]').val($(this).data('userid'));
                $('#setGroupsModal input[name=group]').attr('checked', false);
                var groupIds = $(this).data('groupids').split(',');
                $(groupIds).each(function() {
                    $('#setGroupsModal input[name=group][value=' + this + ']').attr('checked', true);
                });
                $('#setGroupsModal').modal();
            });
        });
    </script>
</head>
<body>
<div class="container">
<ul class="nav nav-pills">
    <li class="active"><a href="${ctx}/chapter14/identity/user/list"><i class="glyphicon glyphicon-user"></i>用户管理</a></li>
    <li><a href="${ctx}/chapter14/identity/group/list"><i class="glyphicon glyphicon-list"></i>组管理</a></li>
</ul>
<hr>
<c:if test="${not empty message}">
    <div id="message" class="alert alert-success">${message}</div>
    <!-- 自动隐藏提示信息 -->
    <script type="text/javascript">
        setTimeout(function() {
            $('#message').hide('slow');
        }, 5000);
    </script>
</c:if>
<div class="row">
    <div class="col-md-8">
        <fieldset>
            <legend><small>用户列表</small></legend>
            <table width="100%" class="table table-bordered table-hover table-condensed">
                <thead>
                <tr>
                    <th>用户ID</th>
                    <th>姓名</th>
                    <th>Email</th>
                    <th>所属组</th>
                    <th width="130">操作</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${page.result }" var="user">
                    <tr id="${user.id}">
                        <td class="prop-id">${user.id}</td>
                        <td>
                                ${user.firstName} ${user.lastName}
                            <span class="prop-firstName" style="display: none">${user.firstName}</span>
                            <span class="prop-lastName" style="display: none">${user.lastName}</span>
                        </td>
                        <td class="prop-email">${user.email}</td>
                        <c:set var="groupNames" value="${''}" />
                        <c:set var="groupIds" value="${''}" />
                        <c:forEach items="${groupOfUserMap[user.id]}" var="group" varStatus="row">
                            <c:set var="groupNames" value="${groupNames}${group.name}" />
                            <c:set var="groupIds" value="${groupIds}${group.id}" />
                            <c:if test="${row.index + 1 < fn:length(groupOfUserMap[user.id])}">
                                <c:set var="groupNames" value="${groupNames}${','}" />
                                <c:set var="groupIds" value="${groupIds}${','}" />
                            </c:if>
                        </c:forEach>
                        <c:if test="${empty groupNames}">
                            <c:set var="groupNames" value="${'还未设置所属组，请单击设置'}" />
                        </c:if>
                        <td>
                            <a href="#" class="set-group groups" title="${groupNames}" data-groupids="${groupIds}" data-userid="${user.id}" data-toggle="modal">共${fn:length(groupOfUserMap[user.id])}个组</a><br>
                        </td>
                        <td>
                            <a class="btn btn-danger btn-xs" href="${ctx}/chapter14/identity/user/delete/${user.id}"><i class="glyphicon glyphicon-remove"></i>删除</a>
                            <a class="btn btn-info btn-xs edit-user" data-id="${user.id}" href="#"><i class="glyphicon glyphicon-pencil"></i>编辑</a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <tags:pagination page="${page}" paginationSize="${page.pageSize}"/>
        </fieldset>
    </div>

    <!-- 新增、编辑用户的Model -->
    <div class="col-md-4">
        <div class="row">
        <form action="${ctx }/chapter14/identity/user/save" class="form-horizontal" method="post">
            <fieldset>
                <legend><small>新增/编辑用户</small></legend>
                <div id="messageBox" class="alert alert-error input-large controls" style="display:none">输入有误，请先更正。</div>
                <div class="form-group">
                    <label for="userId" class="col-sm-3 control-label">用户ID:</label>
                    <div class="col-sm-6">
                        <input type="text" id="userId" name="userId" class="form-control required" />
                    </div>
                </div>
                <div class="form-group">
                    <label for="lastName" class="col-sm-3 control-label">姓:</label>
                    <div class="col-sm-6">
                        <input type="text" id="lastName" name="lastName" class="form-control required" />
                    </div>
                </div>
                <div class="form-group">
                    <label for="firstName" class="col-sm-3 control-label">名:</label>
                    <div class="col-sm-6">
                        <input type="text" id="firstName" name="firstName" class="form-control required" />
                    </div>
                </div>
                <div class="form-group">
                    <label for="email" class="col-sm-3 control-label">Email:</label>
                    <div class="col-sm-6">
                        <input type="text" id="email" name="email" class="form-control required" />
                    </div>
                </div>
                <div class="form-group">
                    <label for="password" class="col-sm-3 control-label">密码:</label>
                    <div class="col-sm-6">
                        <input type="password" id="password" name="password" class="form-control required" />
                    </div>
                </div>
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-primary"><i class="glyphicon glyphicon-ok-sign"></i>保存</button>
                    <button type="reset" class="btn"><i class="glyphicon glyphicon-remove"></i>重置</button>
                </div>
            </fieldset>
        </form>
        </div>
    </div>
</div><!-- /.container -->

    <div id="setGroupsModal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="setGroupsModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                    <h3 id="setGroupsModalLabel">设置用户的所属组</h3>
                </div>
                <div class="modal-body">
                    <form action="${ctx}/chapter14/identity/group/set" method="POST">
                        <input type="hidden" name="userId" />
                        <div class="row">
                            <div class="span3">
                                <c:forEach items="${allGroup}" var="group">
                                    <label class="checkbox"><input type="checkbox" name="group" value="${group.id}" />${group.name}</label>
                                </c:forEach>
                            </div>
                            <div class="span2">
                                <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
                                <button type="submit" class="btn btn-primary">确定添加</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>