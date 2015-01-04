<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <%@ include file="/common/global.jsp" %>
    <script>
        var notLogon = ${empty user};
        if (notLogon) {
            location.href = '${ctx}/login.jsp?timeout=true';
        }
    </script>
    <%@ include file="/common/meta.jsp" %>
    <title>Activiti Explorer第21章例子</title>
    <%@ include file="/common/include-base-styles.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctx }/css/menu.css"/>
    <style type="text/css">
        iframe {
            margin-top: .5em;
        }
    </style>

    <%@ include file="/common/include-base-scripts.jsp" %>
    <script src="${ctx }/js/modules/main/main.js" type="text/javascript"></script>
</head>
<body>
<div class="container">
    <nav id="navbar-example" class="navbar navbar-default navbar-static" role="navigation">
        <div class="container-fluid">
            <div class="navbar-header">
                <button class="navbar-toggle" type="button" data-toggle="collapse" data-target=".bs-example-js-navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="glyphicon glyphicon-bar"></span>
                    <span class="glyphicon glyphicon-bar"></span>
                    <span class="glyphicon glyphicon-bar"></span>
                </button>
                <a class="navbar-brand" href="#">第21章-入侵Activiti</a>
            </div>
            <div class="collapse navbar-collapse bs-example-js-navbar-collapse">
                <ul class="nav navbar-nav">
                    <li class="active"><a href="#" rel="main/welcome.action"><i class="glyphicon glyphicon-home glyphicon glyphicon-black"></i>首页</a>
                    </li>
                    <li class="dropdown">
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#"><i class="glyphicon glyphicon-th-large glyphicon glyphicon-black"></i>请假（普通表单）<b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li role="presentation"><a href="#" rel='chapter7/leave/apply'>请假申请</a></li>
                            <li role="presentation"><a href="#" rel='chapter7/leave/task/list'>待办任务</a></li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#"><i
                                class="glyphicon glyphicon-th-large glyphicon glyphicon-black"></i>运行中<b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li role="presentation"><a href="#" rel="chapter6/task/list"><i class="glyphicon glyphicon-th-list glyphicon glyphicon-black"></i>待办任务</a></li>
                            <li role="presentation"><a href="#" rel="chapter13/execution/list"><i class="glyphicon glyphicon-th-list glyphicon glyphicon-black"></i>参与的流程</a></li>
                        </ul>
                    </li>
                    <li><a href="#" rel="chapter13/history/process/finished/list"><i class="glyphicon glyphicon-lock glyphicon glyphicon-black"></i>已归档</a>
                    <li><a href="#" rel='chapter5/process-list-view'><i class="glyphicon glyphicon-th-list glyphicon glyphicon-black"></i>流程列表</a></li>
                    </li>
                    <li class="dropdown">
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#"><i
                                class="glyphicon glyphicon-th-large glyphicon glyphicon-black"></i>管理<b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li role="presentation"><a href="#" rel='chapter5/process-list'>流程定义管理</a></li>
                            <li class="divider"></li>
                            <li role="presentation"><a href="#" rel='chapter14/processinstance/list'>运行中流程</a></li>
                            <li role="presentation"><a href="#" rel='chapter13/history/process/finished/manager'>已归档流程</a></li>
                            <li class="divider"></li>
                            <li role="presentation"><a href="#" rel='chapter14/engine'>引擎属性</a></li>
                            <li role="presentation"><a href="#" rel='chapter14/database'>引擎数据库</a></li>
                            <li role="presentation"><a href="#" rel='chapter14/job/list'>作业管理</a></li>
                            <li class="divider"></li>
                            <li role="presentation"><a href="#" rel='chapter14/identity/user/list'>用户与组</a></li>
                            <li class="divider"></li>
                            <li role="presentation"><a href="#" rel='chapter21/event/log'>事件日志</a></li>
                        </ul>
                    </li>
                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li id="fat-menu" class="dropdown">
                        <a href="#" id="drop3" role="button" class="dropdown-toggle" data-toggle="dropdown" title="角色：${groupNames}">
                            ${user.firstName }&nbsp;${user.lastName }/${user.id }<b class="caret"></b></a>
                        <ul class="dropdown-menu" role="menu" aria-labelledby="drop3">
                            <li><a id="changePwd" href="#"><i class="glyphicon glyphicon-wrench glyphicon glyphicon-black"></i>修改密码</a></li>
                            <li><a id="loginOut" href="#"><i class="glyphicon glyphicon-eject glyphicon glyphicon-black"></i>安全退出</a></li>
                        </ul>
                    </li>
                </ul>
            </div><!-- /.nav-collapse -->
        </div><!-- /.container-fluid -->
    </nav>

    <iframe id="mainIframe" name="mainIframe" src="welcome" class="module-iframe" scrolling="auto" frameborder="0"
            style="width:100%;"></iframe>
</div><!-- /.container -->
</body>
</html>