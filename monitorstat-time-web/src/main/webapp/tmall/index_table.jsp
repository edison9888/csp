<%@page import="com.taobao.csp.time.cache.TimeCache"%>
<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<%@ include file="/time/common/base.jsp"%>
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"   src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
<script type="text/javascript"		src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>

<title>实时监控</title>
</head>
<body>
<%@ include file="/header.jsp"%>
<div class="row-fluid"  style="text-align: center">
	<div class="span12" id="page_nav"></div>
<script>
	$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {urlPrefix:'<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=',appId: '${appInfo.appId}'});
</script>	

</div>
<table width="100%"  style="margin-top: 5px;">
	<%for(String id:new String[]{"3","16","286","323","369","379","590","688","705"}){%>
	<tr>
		<td id="tdId_<%=id%>">
			<script type="text/javascript">
			$("#tdId_<%=id%>").load("<%=request.getContextPath()%>/index.do?method=queryIndexTableForPv&appId=<%=id%>");
			</script>
		</td>
	</tr>
	<%} %>
	<%for(String id:new String[]{"4","8","21","80","322","338","390","460","546","585"}){%>
	<tr>
		<td id="tdId_<%=id%>">
			<script type="text/javascript">
			$("#tdId_<%=id%>").load("<%=request.getContextPath()%>/index.do?method=queryIndexTableForHSf&appId=<%=id%>");
			</script>
		</td>
	</tr>
	<%} %>
</table>
</body>
</html>