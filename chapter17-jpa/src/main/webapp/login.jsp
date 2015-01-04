<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>《Activiti实战》示例第17章-登录系统</title>
	<%@ include file="/common/global.jsp"%>
	<%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/include-base-styles.jsp" %>
    <style type="text/css">
        body {
            padding-top: 40px;
            padding-bottom: 40px;
            background-color: #eee;
        }

        .form-signin {
            max-width: 330px;
            padding: 15px;
            margin: 0 auto;
        }
        .form-signin .form-signin-heading,
        .form-signin .checkbox {
            margin-bottom: 10px;
        }
        .form-signin .checkbox {
            font-weight: normal;
        }
        .form-signin .form-control {
            position: relative;
            height: auto;
            -webkit-box-sizing: border-box;
            -moz-box-sizing: border-box;
            box-sizing: border-box;
            padding: 10px;
            font-size: 16px;
        }
        .form-signin .form-control:focus {
            z-index: 2;
        }
        .form-signin input[type="email"] {
            margin-bottom: -1px;
            border-bottom-right-radius: 0;
            border-bottom-left-radius: 0;
        }
        .form-signin input[type="password"] {
            margin-bottom: 10px;
            border-top-left-radius: 0;
            border-top-right-radius: 0;
        }

    </style>

    <script src="${ctx }/js/common/jquery.js" type="text/javascript"></script>
    <script type="text/javascript">
        $(function() {
            $('#username').focus();
        });
    </script>
</head>

<body>
<div class="container">

    <div class="row">
        <div class="col-xs-6 col-xs-offset-4">
            <h2>第17章—《集成JPA》配套示例</h2>
        </div>
    </div>

    <div class="row">
        <div class="col-xs-4 col-xs-offset-4">
            <form action="${ctx }/user/logon" class="form-signin" role="form">
                <input type="text" id="username" name="username" class="form-control" placeholder="用户名" required autofocus>
                <input type="password" id="password" name="password" class="form-control" placeholder="密码" required>
                <button class="btn btn-lg btn-primary btn-block" type="submit">登录</button>
            </form>
        </div>
    </div>

    <hr/>
    <div class="row">
        <div class="col-xs-6 col-xs-offset-4">
            <p class="text-info">
                如果按照书上的用户名、密码登录失败请用终端进入 <strong>chapter17-jpa</strong> 项目目录执行：
                <br/><code>mvn antrun:run -Pinit-db</code>
            </p>
        </div>
        <hr>
    </div>

    <div class="row">
        <div class="col-xs-6 col-xs-offset-4">
            <h4 class="text-info">用户与角色列表（密码：000000）</h4>
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>#</th>
                    <th>用户名</th>
                    <th>角色</th>
                    <th>部门</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>1</td>
                    <td>henry</td>
                    <td>系统管理员</td>
                    <th>IT部</th>
                </tr>
                <tr>
                    <td>2</td>
                    <td>bill</td>
                    <td>总经理</td>
                    <td>总经理室</td>
                </tr>
                <tr>
                    <td>3</td>
                    <td>jenny</td>
                    <td>人事经理</td>
                    <td>人事部</td>
                </tr>
                <tr class="success">
                    <td>4</td>
                    <td>kermit</td>
                    <td>部门经理</td>
                    <td>市场部</td>
                </tr>
                <tr class="success">
                    <td>5</td>
                    <td>eric</td>
                    <td>普通职员</td>
                    <td>市场部</td>
                </tr>
                <tr class="success">
                    <td>6</td>
                    <td>tom</td>
                    <td>普通职员</td>
                    <td>市场部</td>
                </tr>
                <tr class="info">
                    <td>7</td>
                    <td>andy</td>
                    <td>普通职员</td>
                    <td>业务部</td>
                </tr>
                <tr class="info">
                    <td>8</td>
                    <td>amy</td>
                    <td>普通职员</td>
                    <td>业务部</td>
                </tr>
                <tr class="danger">
                    <td>9</td>
                    <td>tony</td>
                    <td>财务人员</td>
                    <td>财务部</td>
                </tr>
                <tr class="danger">
                    <td>10</td>
                    <td>lily</td>
                    <td>出纳员</td>
                    <td>财务部</td>
                </tr>
                <tr>
                    <td>11</td>
                    <td>thomas</td>
                    <td>后勤人员</td>
                    <td>后勤部</td>
                </tr>
                </tbody>
            </table>
        </div>
        <hr>
    </div>
</div> <!-- /container -->
</body>
</html>
