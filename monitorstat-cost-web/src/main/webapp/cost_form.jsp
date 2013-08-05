<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/statics/css/style.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/jquery-1.4.4.js"></script>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<title>成本中心</title>

</head>
<body style="padding-top:45px" class="span20">
<%@ include file="../../top.jsp" %>

成本计算公式：<br/>
1.基础依赖系统只算硬件成本以及运维成本(tair/db等)<br/>
2.业务系统以及各个C除了算硬件成本、运维成本还要算上依赖成本<br/>


<img src="<%=request.getContextPath() %>/statics/images/cost_all.JPG" style="width:450px;hight:60px;"/><br/>
其中：<br/>
<img src="<%=request.getContextPath() %>/statics/images/cost_hard.jpg" style="width:390px;hight:55px;"/><br/>
<img src="<%=request.getContextPath() %>/statics/images/cost_ops.jpg" style="width:200px;hight:25px;"/><br/><br/>

<hr/>

</body>
</html>