<%@ page language="java" contentType="text/html; charset=GB18030"
	pageEncoding="GB18030" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="com.taobao.csp.depend.po.AutoCheckDependResult"%>
<%@ page import="com.taobao.csp.depend.checkup.job.IsolationType"%>
<%@ page import="com.taobao.csp.depend.util.ConstantParameters"%>
<%@ page import="com.taobao.csp.depend.util.StartUpParamWraper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>检测过程描述页面</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link href="<%=request.getContextPath()%>/statics/css/bootstrap.css"
	rel="stylesheet">
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">

<script
	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/statics/js/bootstrap.js"
	type="text/javascript"></script>
<script language="javascript" type="text/javascript"
	src="<%=request.getContextPath()%>/statics/datePicket/WdatePicker.js"></script>

<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
	
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/statics/css/jquery.fancybox.css" media="screen" />
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.fancybox.js"></script>	
<script src="<%=request.getContextPath()%>/statics/userjs/fancyboxwraper.js" type="text/javascript"></script>	

<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/jquery.galleryview-3.0-dev.css" />
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet"/><script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.easing.1.3.js"></script>
</head>
<body>
<%@ include file="../header.jsp"%>
	<div class="container">
	<h4 style="color: red;">
		应用的检查过程不存在，可能原因<br>	
		1.应用非淘宝网核心系统，强弱依赖检查暂未覆盖到。<br>	
		2.应用为淘宝网核心系统，但是没有配置强弱依赖检查。<br>	
	</h4>
	<br>	
	<h4>
目前定义的淘宝网核心系统包括：detail,hesper,login,cart,shopsystem,shopcenter,tradeplatform,tradeplatform,tf_buy,tf_tm,uicfinal,itemcenter,ump
<br/>
关于强弱依赖检查原理，请参看<a href="http://baike.corp.taobao.com/index.php/File:%E5%BC%BA%E5%BC%B1%E4%BE%9D%E8%B5%96%E6%A3%80%E6%B5%8B%E5%8E%9F%E7%90%86%E8%AF%B4%E6%98%8E.pdf" target="_blank">强弱依赖检测的原理和检测流程的介绍(pdf下载)</a>	
	</h4>
	</div>
</body>
</html>