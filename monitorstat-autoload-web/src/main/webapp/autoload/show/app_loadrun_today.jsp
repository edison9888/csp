<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title>CSPѹ��ϵͳ�ڲ��ձ�</title>

</head>
<body>

<table  border="1" id="myTable"  style="width:1200;align:center">
	<tr>
		<font color="#972630" size="6">
		<td colspan="5" align="center">CSPѹ��ϵͳ�ڲ��ձ�(${date})</td>
		</font>
	</tr>	
	
	<tr>
		<font size="4">
		<td align="center">Ӧ����</td>
		<td align="center">Ŀ�����</td>
		<td align="center">ѹ�ⷽʽ</td>
		<td align="center">ѹ���ڼ���Сqps</td>
		<td align="center">ѹ���ڼ����qps</td>
		</font>
	</tr>	
	
	<c:forEach items="${max}" var="entry">
	<tr>
		<td align="center">${entry.key}</td>
		<td align="center">${entry.value.targetIp}</td>
		<td align="center">${entry.value.loadrunType}</td>
		<td align="center">${min[entry.key].value}</td>
		<td align="center">${entry.value.value}</td>
	</tr>	
	</c:forEach>
	
</table>

</body>
</html>