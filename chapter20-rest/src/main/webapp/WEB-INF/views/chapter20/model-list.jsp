<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/common/global.jsp" %>
    <%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/include-base-styles.jsp" %>
    <title>模型列表--chapter20</title>

    <%@ include file="/common/include-base-scripts.jsp" %>
    <script type="text/javascript"></script>
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
<div class="pull-right">
    <a href="#" class="btn" data-toggle="modal" data-target="#newModel">创建新模型</a>
</div>
<table width="100%" class="table table-bordered table-hover table-condensed">
    <thead>
        <tr>
            <th>ID</th>
            <th>KEY</th>
            <th>名称</th>
            <th>版本</th>
            <th>创建时间</th>
            <th>最后更新时间</th>
            <th>元数据</th>
            <th>操作</th>
        </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.result }" var="model">
        <tr>
            <td>${model.id }</td>
            <td>${model.key }</td>
            <td>${model.name}</td>
            <td>${model.version}</td>
            <td>${model.createTime}</td>
            <td>${model.lastUpdateTime}</td>
            <td>${model.metaInfo}</td>
            <td>
                <a href="${ctx}/mservice/editor?id=${model.id}" target="_blank">编辑</a>
                <a href="${ctx}/chapter20/model/deploy/${model.id}">部署</a>
                <a href="${ctx}/chapter20/model/export/${model.id}" target="_blank">导出</a>
                <a href="${ctx}/chapter20/model/delete/${model.id}">删除</a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<tags:pagination page="${page}" paginationSize="${page.pageSize}"/>

<!-- 流程定义挂起 -->
<div id="newModel" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="newModelModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content" style="padding-bottom: 2em;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <h3 id="newModelModalLabel">创建新模型</h3>
            </div>
            <div class="modal-body">
                <form action="${ctx}/chapter20/model/create" class="form-horizontal" method="post">
                    <div class="form-group">
                        <label for="name" class="col-sm-3 control-label">名称:</label>
                        <div class="col-sm-6">
                            <input type="text" id="name" name="name" class="form-control required" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="key" class="col-sm-3 control-label">KEY:</label>
                        <div class="col-sm-6">
                            <input type="text" id="key" name="key" class="form-control required" />
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="description" class="col-sm-3 control-label">描述:</label>
                        <div class="col-sm-6">
                            <input type="text" id="description" name="description" class="form-control" />
                        </div>
                    </div>
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="submit" class="btn btn-primary"><i class="glyphicon glyphicon-ok-sign"></i>创建</button>
                        <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>