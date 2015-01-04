<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/common/global.jsp"%>
  <script>
    var notLogon = ${empty user};
    if (notLogon) {
      location.href = '${ctx}/login.jsp?timeout=true';
    }
  </script>
	<%@ include file="/common/meta.jsp" %>
    <title>Activiti Explorer[第6章-任务表单]</title>
    <%@ include file="/common/include-base-styles.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctx }/css/menu.css" />
  	<style type="text/css">
      iframe{
      	margin-top: .5em;
      }
  	</style>

    <script src="${ctx }/js/common/jquery.js" type="text/javascript"></script>
    <script src="${ctx }/js/common/bootstrap.min.js" type="text/javascript"></script>
    <script src="${ctx }/js/modules/main/main.js" type="text/javascript"></script>
</head>
<body>

  <div class="navbar navbar-fixed-top">
  <div class="navbar-inner">
      <div class="container-fluid">
        <a data-target=".nav-collapse" data-toggle="collapse" class="btn btn-navbar">
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
          <span class="icon-bar"></span>
        </a>
        <a href="#" class="brand">Activiti Explorer</a>
        <div class="nav-collapse">
          <ul class="nav">
            <li class="active"><a href="#" rel="main/welcome.action"><i class="icon-home icon-black"></i>首页</a></li>
            <li><a href="#" rel="chapter6/task/list"><i class="icon-th-list icon-black"></i>任务列表</a></li>
            <li class="dropdown">
              <a data-toggle="dropdown" class="dropdown-toggle" href="#"><i class="icon-th-large icon-black"></i>管理<b class="caret"></b></a>
              <ul class="dropdown-menu">
                <li><a href="#" rel='chapter5/process-list'>流程定义</a></li>
              </ul>
            </li>
          </ul>

          <ul class="nav pull-right">
            <li class="dropdown">
            	<a data-toggle="dropdown" class="dropdown-toggle" href="#" title="角色：${groupNames}">
            		<i class="icon-user icon-black" style="margin-right: .3em"></i>${user.firstName }&nbsp;${user.lastName }/${user.id }<b class="caret"></b>
            	</a>
            	<ul class="dropdown-menu">
					<li><a id="changePwd" href="#"><i class="icon-wrench icon-black"></i>修改密码</a></li>
					<li><a id="loginOut" href="#"><i class="icon-eject icon-black"></i>安全退出</a></li>
				</ul>
            </li>
          </ul>
        </div>
      </div>
  </div>
<div class="container">
	<iframe id="mainIframe" name="mainIframe" src="welcome" class="module-iframe" scrolling="auto" frameborder="0" style="width:100%;"></iframe>
</div>
</body>
</html>