<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title>CSP系统-应用压测列表</title>

</head>
<body>


<c:forEach items="${loadrunMap}" var="entry">

<table  border="1" id="myTable"  style="width:1200;align:center">
	<tr>
		<font color="#972630" size="6">
		<td align="center">应用:${entry.value['loadconfig'].appName} - ${entry.value['collectTime']}<input type="hidden" value="${entry.value['loadconfig'].appId}" name="appId"></td>
		</font>
	</tr>	
</table>
<c:forEach items="${entry.value['loadrunMap']}" var="loadrun">
<table  border="1" id="myTable"  style="width:1200;align:center">
	
	<font color="#154190">
	<tr">
		<td align="center">顺序</td>
		<td align="center">${entry.value['loadmessage']}</td>
	<c:forEach items="${entry.value['keys']}" var="key">
		<td width="100" align="center">${key.name }</td>		
	</c:forEach>
	</tr>
	</font>
	
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

</body>
</html>