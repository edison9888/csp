<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>信息提示</title>
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet"/>
</head>
<body>
<table class="table table-striped table-bordered table-condensed">
<% 
boolean needBack = true;
Object backUrl = request.getAttribute("backurl");
   if (backUrl == null) {
   		needBack = false;
   }
%>
	<tr>
		<td>${ErrorMsg}</td><td><% if(needBack) {%><a href="${pageContext.request.contextPath}${backurl}">返回</a><% } %></td>
	</tr>
</table>
</body>
</html>