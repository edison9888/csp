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

<div>
<c:if test="${injectMessage == 'success'}">Btrace 注入成功,等待连接。。。
<script type="text/javascript">
function sendRedirect(ip){
	$.getJSON("<%=request.getContextPath() %>/btrace/control.do?method=waitInject&ip="+ip, {}, function(json){
		if(json["message"] == 'ok'){
			location.href="<%=request.getContextPath() %>/btrace/control.do?method=intoShowJstack&ip="+ip;
		}
	}); 
	
	window.setTimeout("sendRedirect('"+ip+"')",30000); 	
}
sendRedirect("${ip}");
</script>
</c:if>

<c:if test="${injectMessage != 'success'}">
Btrace 注入失败!
${injectMessage}
</c:if>

</div>
</body>
</html>