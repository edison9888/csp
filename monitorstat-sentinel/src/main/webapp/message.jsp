<%@ page language="java" contentType="text/html; charset="UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>信息提示</title>
</head>
<body>
<table>
<% 
boolean needBack = true;
Object backUrl = request.getAttribute("backurl");
   if (backUrl == null) {
   		needBack = false;
   }
%>
	<tr>
		<td>${message}</td><td><% if(needBack) {%><a href="${pageContext.request.contextPath}${backurl}">返回</a><% } %></td>
	</tr>
</table>
</body>
</html>