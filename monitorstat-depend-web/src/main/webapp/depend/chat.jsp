<%@ page language="java" contentType="text/html; charset=GB18030"  isELIgnored="false" 
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0">
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/dracular/raphael-min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/dracular/dracula_graffle.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/dracular/dracula_graph.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/dracular/dracula_algorithms.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/userjs/showchat.js"></script>
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet"/>
<title>图形显示依赖</title>
<%
	String opsName = (String) request.getParameter("opsName"); //应用名称
	String selectDate = (String) request.getParameter("selectDate"); //时间
	String dependAppType = (String) request
			.getParameter("dependAppType"); //应用的类别
	String showType = (String) request.getParameter("showType"); //显示方式
	String optType = (String) request.getParameter("optType"); //对比依赖

	opsName = opsName != null ? opsName : "";
	selectDate = selectDate != null ? selectDate : "";
	dependAppType = dependAppType != null ? dependAppType : "all";
	showType = showType != null ? showType : "list";
	optType = optType != null ? optType : "same";
%>

<script type="text/javascript">
	//全局变量,showchat.js中有使用
	var contextPath = '<%=request.getContextPath()%>'; 
	var opsName = '<%=opsName%>';
	var selectDate = '<%=selectDate%>';
	var optType = '<%=optType%>';
</script>
</head>
<body>
	<!-- style="border:5px solid red;width: 600px;height: 400px" -->
	<div id="canvas" style="width: 100%;height: 100%"></div>
</body>
</html>