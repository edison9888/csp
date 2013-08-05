<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk" isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>CSP系统-应用压测列表</title>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/jquery/jquery.js"></script>
<link type="text/css" href="<%=request.getContextPath() %>/statics/jquery/themes/base/ui.all.css" rel="stylesheet" />
<link type="text/css" href="<%=request.getContextPath() %>/statics/commons.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="screen" href="<%=request.getContextPath() %>/statics/jquery/validation/css/screen.css" />
<script type="text/javascript">

</script>

</head>

<body>
<form action="<%=request.getContextPath() %>/btrace/control.do?method=injectTrace" method="post">
<input type="hidden" name="ip" value="${ip}">
<input type="hidden" name="userName" value="${userName}">
<input type="hidden" name="password" value="${password}">
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000"> ${ip} - 机器进程列表</font></div>
<div class="ui-dialog-content ui-widget-content">
<table cellpadding="0" cellspacing="0" border="1" id="myTable" class="tablesorter" style="width:99%;align:center" >
	<tr class="ui-widget-header ">
		<td align="center" width="20"></td>
		<td align="center" width="100">Pid</td>
		<td align="center" width="180">所属用户</td>
		<td align="center" width="180">描述</td>	
	</tr>
	<c:forEach items="${infoList}" var="pInfo">
	<tr>
		<td align="center" width="20"><input type="radio" name="pid" value="${pInfo.processId}"></td>
		<td align="center">${pInfo.processId}</td>
		<td align="center">${pInfo.processUserName}</td>
		<td align="center"><textarea rows="2" cols="150">${pInfo.processDesc}</textarea></td>
	</tr>
	</c:forEach>
	<tr >
		<td align="center" width="20" colspan="4"><input type="submit" value="提交"/></td>		
	</tr>
</table>
</div>
</div>
</form>
<font>${message}</font>
</body>
</html>