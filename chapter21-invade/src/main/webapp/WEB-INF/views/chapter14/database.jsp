<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/common/global.jsp"%>
    <%@ include file="/common/meta.jsp" %>
    <title>数据库表查询</title>
    <%@ include file="/common/include-base-styles.jsp" %>
    <script src="${ctx }/js/common/jquery.js" type="text/javascript"></script>
</head>
<body>
<div class='page-title ui-corner-all'>引擎数据库</div>
    <div class="row">
        <div class="col-xs-3" style="border-right: 1px solid #000000;max-width: 270px;">
            <div class='page-title ui-corner-all'>表名</div>
            <ul class="unstyled">
                <c:forEach items="${tableCount}" var="table">
                    <li><a href="?tableName=${table.key}">${table.key}（${table.value}）</a></li>
                </c:forEach>
            </ul>
        </div>
        <div class="col-xs-9">
            <c:if test="${not empty tableMetaData}">
            <div class='page-title ui-corner-all'>记录（${tableMetaData.tableName}）</div>
            <table width="100%" class="table table-bordered table-hover table-condensed">
                <thead>
                    <tr>
                        <th>#</th>
                        <c:forEach items="${tableMetaData.columnNames}" var="data" varStatus="row">
                            <th title="字段类型：${tableMetaData.columnTypes[row.index]}">${data}</th>
                        </c:forEach>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="counter" value="${((page.pageNo - 1) * page.pageSize) + 1}" />
                    <c:forEach items="${page.result}" var="data">
                    <tr>
                        <td style="padding: 0 10px 0 10px">${counter}</td>
                        <c:set var="counter" value="${counter+1}" />
                        <c:forEach items="${tableMetaData.columnNames}" var="column">
                            <td>${data[column]}</td>
                        </c:forEach>
                    </tr>
                    </c:forEach>
                </tbody>
            </table>
            <c:set var="page_params" scope="session" value="tableName=${tableMetaData.tableName}" />
            <c:if test="${not empty page}">
                <tags:pagination page="${page}" paginationSize="${page.pageSize}"/>
            </c:if>
            </c:if>

            <c:if test="${empty tableMetaData}">
                <p class="text-info text-center" style="margin-top: 50%;font-size: 20px;">单击左边的表名可以查看记录！</p>
            </c:if>
        </div>
    </div>
</div>
</body>
</html>