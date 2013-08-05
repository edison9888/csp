<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>CSP系统-应用压测列表</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/jquery/themes/base/ui.all.css" rel="stylesheet" />
<link type="text/css" href="<%=request.getContextPath() %>/statics/commons.css" rel="stylesheet" />
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/My97DatePicker/WdatePicker.js"></script>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/swfobject.js"></script>

</head>
<body>
<form action="<%=request.getContextPath() %>/loadrun/show.do?method=show" method="post">
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000"> 压测应用列表</font></div>
<div class="ui-dialog-content ui-widget-content">

<c:forEach items="${loadrunMap}" var="entry">

<table cellpadding="0" cellspacing="0" border="1" id="myTable" class="tablesorter" style="width:100%;align:center">
	<tr>
		<td align="center">应用:${entry.value['loadconfig'].appName} - ${entry.value['collectTime']}<input type="hidden" value="${entry.value['loadconfig'].appId}" name="appId"></td>
	</tr>	
</table>
<c:forEach items="${entry.value['loadrunMap']}" var="loadrun">
<table cellpadding="0" cellspacing="0" border="1" id="myTable" class="tablesorter" style="width:100%;align:center">
	
	<tr class="ui-widget-header ">
		<td align="center">顺序</td>
		<td align="center">${entry.value['loadmessage']}</td>
	<c:forEach items="${entry.value['keys']}" var="key">
		<td width="100" align="center">${key.name }</td>		
	</c:forEach>
	</tr>
	
	<c:forEach items="${entry.value['sortList']}" var="sort">
	<tr>
		<td width="100" align="center">${sort}</td>
		<td width="100" align="center">${entry.value['loadControlMap'][loadrun.key][sort]}</td>
		<c:forEach items="${entry.value['keys']}" var="key">
			<td align="center">${loadrun.value[sort][key].value}</td>			
		</c:forEach>
	</tr>		
	</c:forEach>
	
		
</table>
</c:forEach>

</c:forEach>
</div>
</div>

</form>
</body>
</html>