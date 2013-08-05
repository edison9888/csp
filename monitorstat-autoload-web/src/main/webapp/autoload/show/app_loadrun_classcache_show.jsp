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

</head>
<body>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000"> Slave 缓存class列表</font></div>
<div class="ui-dialog-content ui-widget-content">


<table cellpadding="0" cellspacing="0" border="1" id="myTable" class="tablesorter" style="width:100%;align:center">
	
	<tr class="ui-widget-header ">
		<td align="center">className</td>
		<td align="center">构建时间</td>
		<td width="100" align="center">大小</td>
	</tr>
	
	<c:forEach items="${classEntryList}" var="classEntry">
	<tr>
		<td width="100" align="center">${classEntry[0]}</td>
		<td width="100" align="center">${classEntry[1]}</td>
		<td align="center">${classEntry[2]}</td>			
	</tr>		
	</c:forEach>
		
</table>
</div>
</div>


</body>
</html>