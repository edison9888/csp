<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk" isELIgnored="false" import="com.taobao.csp.btrace.core.FieldProfilerInfo"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<meta http-equiv="expires" content="0">
<title>Field属性监控页面</title>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/jquery/jquery-1.6.js"></script>
<script type="text/javascript">
	//调试的时候，自动刷新去掉
	setTimeout("self.location.reload();",1000);		//按照当前的URL自动刷新页面 
</script>
</head>
<body>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-content ui-widget-content">
	<c:if test="${findClass == 'failure'}">
		查找的类${tarClassname}时异常，错误信息：
		<br/>
		<div style="color:red">
			${erroirCode}
		</div>
		
	</c:if>
	<c:if test="${findClass == 'success'}">
		<script type="text/javascript">
			function sendInfoRedirect(ip, tarClassname, tarFieldname, tarMethodname){
				var actionUrl = "<%=request.getContextPath() %>/btrace/control.do?method=showFieldInfo";
				actionUrl += "&tarMethodname=" + tarMethodname;
				actionUrl += "&tarClassname=" + tarClassname + "&tarFieldname=" + tarFieldname + "&ip=" + ip;
				location.href = actionUrl;				
				window.setTimeout("sendRedirect('" + ip + "','" + tarClassname + "','" 
						+ tarFieldname + "','" + tarMethodname + "')",30000); 	
			}
			//sendInfoRedirect("${ip}","${tarClassname}","${tarFieldname}");			
		</script>
	</c:if>
	<table border="1" class="tablesorter" style="width:99%;align:center" >
		<tr class="ui-widget-header ">
			<td>类名称：${tarClassname}</td>
			<td>方法名:${tarMethodname}</td>
			<td>变量名:${tarFieldname}</td>
			</tr>
		<tr>
			<td>线程号</td>
			<td>执行方法前值</td>
			<td>执行方法后值</td>
		</tr>
		<c:forEach items="${fieldList}" var="fieldInfo">
			<tr>
				<td align="left">${fieldInfo.threadId}</td>
				<td align="left">${fieldInfo.startValue}</td>
				<td align="left">${fieldInfo.endValue}</td>
			</tr>
		</c:forEach>
	</table>	
</div>
</div>
</body>
</html>