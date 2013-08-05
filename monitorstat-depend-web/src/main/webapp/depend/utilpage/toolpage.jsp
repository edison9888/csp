<%@ page language="java" contentType="text/html; charset=GB18030"
	isELIgnored="false" pageEncoding="GB18030"%>
<link href="<%=request.getContextPath()%>/statics/css/bootstrap.css"
	rel="stylesheet" />
<link
	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css"
	rel="stylesheet" />
<script language="JavaScript"
	src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>
	<div class="container">
	<%
	String content = request.getParameter("content");	
	out.println(content);
	%>
	</div>