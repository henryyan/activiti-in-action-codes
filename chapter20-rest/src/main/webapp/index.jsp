<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <%@ include file="/common/global.jsp" %>
    <%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/include-base-styles.jsp" %>
    <title>Activiti实战第20章《REST服务》示例</title>

    <%@ include file="/common/include-base-scripts.jsp" %>
    <script type="text/javascript"></script>
    <title>Ajax REST Example</title>
    <script src="${ctx}/static/js/base64.min.js"></script>
    <script type="text/javascript">
        $(function() {
            $('#normal').click(sameDomain);
            $('#jsonp').click(jsonp);
        });

        /**
         * 跨域访问
         */
        function jsonp() {
            alert('jsonp不支持Basic Auth认证所以会请求失败。');
            $.ajax({
                type: "get",
                url: "http://kermit:000000@henryyan:8080/chapter20-rest/service/management/properties",
                dataType: "jsonp",
                jsonpCallback: 'jsonpResp'
            });
        }

        /**
         * JSONP回调Function
         */
        function jsonpResp(resp) {
            $('#resp').empty();
            $.each(resp, function(k, v) {
                $('<li/>', {
                    html: k + ": " + v
                }).appendTo('#resp');
            });
        }

        /**
         * 相同域访问
         */
        function sameDomain() {
            $.ajax({
                type: "get",
                url: "${ctx}/service/management/properties",
                dataType: "json",
                beforeSend: function(xhr) {
                    var tok = 'kermit:000000';
                    var hash = Base64.encode(tok);
                    var baseAuth = "Basic " + hash;
                    xhr.setRequestHeader('Authorization', baseAuth);
                },
                contentType: "application/json",
                success: function(json) {
                    $('#resp').empty();
                    $.each(json, function(k, v) {
                        $('<li/>', {
                            html: k + ": " + v
                        }).appendTo('#resp');
                    });
                }
            });
        }

    </script>
</head>
<body>
    <div class="container">
        <div class="row">
            <h2 class="text-center">第20章《REST服务》示例</h2>
        </div>
        <div class="row">
            <div class="col-xs-6 col-sm-offset-6">
                <span>如果没有初始化用户数据请在项目目录执行命令：</span>
                <code>mvn antrun:run -Pinit-db</code>
            </div>
        </div>
        <hr/>
        <div class="row">
            <div class="col-xs-6">
                <p>通过Ajax访问Activiti REST服务</p>
                <button id="normal" class="btn btn-primary">Ajax访问</button>
                <hr/>
                <ul id="resp"></ul>
            </div>
            <div class="col-xs-6">
                <ul>
                    <li><a target="_blank" title="不支持IE" href="chapter20/model/list">模型列表（在线设计流程）</a></li>
                    <li><a target="_blank" title="查看已经部署的流程定义" href="chapter5/processes">流程定义列表</a></li>
                </ul>
            </div>
        </div>
    </div>
</body>
</html>