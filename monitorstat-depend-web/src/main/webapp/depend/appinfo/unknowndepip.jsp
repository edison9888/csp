<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet"/><title>Insert title here</title>
<link href="<%=request.getContextPath() %>/statics/css/style.css" rel="stylesheet" type="text/css">
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.tabs.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/jquery.ui.widget.js"></script>
</head>
<body>

<div>
<table width="1000" class="table table-striped table-bordered table-condensed">
	<tr class="ui-widget-header ">
		<td>本地IP</td>
		<td>本地端口</td>
		<td>未知依赖IP</td>
		<td>未知ip使用端口</td>
	</tr>
	<c:forEach items="${unkowdepips}" var="ip">
	<tr>
		<td >${ip.localIp}</td>
		<td>${ip.localPort}</td>
		<td>${ip.foreignIp}</td>
		<td>${ip.foreignPort}</td>
	</tr>
	</c:forEach>
</table>
</div>

</body>
</html>