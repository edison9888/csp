<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>重插入pv数据</title>
</head>
<body>
<%
	boolean reinserted = false;
	
	boolean success = false;
    if (request.getAttribute("success") != null) {
    	success = ((Boolean)request.getAttribute("success")).booleanValue();
    	reinserted = true;
    }
    
    String message = "日期格式错误或者重新插入失败";
    if (request.getAttribute("message") != null && success) {
    	message = (String)request.getAttribute("message");
    } 
%>
<form action="./manage.do?method=reinsertCapacityPv" method="post">
<table width="100%">
<tr width="100%">
<td align="center">
日期:<input type="text" name="date" value="" style="width: 100;"> &nbsp;&nbsp;
<input type="submit" value="重新插入"> &nbsp;

<% if (reinserted) { %>
<font color="red">
<%= message%>
</font>
<%}  %>
</td>
</tr>
</table>
</form>
</body>
</html>