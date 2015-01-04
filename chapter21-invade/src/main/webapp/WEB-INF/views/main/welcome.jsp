<%@ page import="org.activiti.engine.ManagementService" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.Properties" %>
<%@ page import="java.util.Enumeration" %>
<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<%@ include file="/common/global.jsp"%>
	<%@ include file="/common/meta.jsp"%>
	<%@ include file="/common/include-base-styles.jsp" %>
</head>
<body>
<div class="container">
    <div class="row">
        <h2 class="page-header">本章主要讲解：</h2>
        <ol>
            <li>在Activiti中集成JPA</li>
            <li>实例分析（leae-jpa）如何在流程中更新实体对象</li>
        </ol>
    </div>

    <div class="row">
        <h2 class="page-header">系统参数：</h2>
        <div class="col-xs-6">
            <%
                WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
                ManagementService managementService = wac.getBean(ManagementService.class);
                Map<String,String> engineProperties = managementService.getProperties();
                pageContext.setAttribute("engineProperties", engineProperties);
            %>
            <table class="table table-bordered table-hover table-condensed">
                <tr>
                    <th class="text-info">属性名称</th>
                    <th class="text-info">属性值</th>
                </tr>
                <c:forEach items="${engineProperties}" var="prop">
                    <tr class="success">
                        <th>${prop.key}</th>
                        <td>${prop.value}</td>
                    </tr>
                </c:forEach>

                <%
                    Properties properties = System.getProperties();
                    Enumeration<?> enumeration = properties.propertyNames();
                    pageContext.setAttribute("knames", enumeration);
                    pageContext.setAttribute("properties", enumeration);
                %>
                <c:forEach items="${knames}" var="key">
                <tr>
                    <th>${key}</th>
                    <td><%=properties.get(pageContext.getAttribute("key")) %></td>
                </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>
</body>
</html>