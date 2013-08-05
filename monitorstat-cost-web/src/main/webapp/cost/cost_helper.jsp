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

<style type="text/css">
#demo{font-size:13px;color:#333;width:1000px;padding-top:20px;}
#demo img{float:left; margin:0px 10px 10px 0;}
#main{clear:both;}
#appMain{text-align:center;}
</style>

<div id="demo">
	<img src="<%=request.getContextPath() %>/statics/images/cost_down.JPG" style="width:200px;hight:80px;"/>
	网站规模的扩大，系统的成本会成为公司中支出的重要部分，因此成本控制就显得非常重要。之前隐农曾在阿里味上连续发了多个贴出，指出成本浪费的现象
	《<a href="http://www.aliway.com/read.php?fid=38&tid=157787">三淘线上的“水”，一年浪费超过一亿</a> 》
	《<a href="http://www.aliway.com/read.php?fid=38&tid=155433">业务一去不复返，设备干烧600万</a> 》
	《<a href="http://www.aliway.com/read.php?fid=38&tid=153865">奢侈的云梯II，1年浪费2143万！</a> 》；但是线上应用的load一直是比较低的状态，
	因此我们想从其他角度来统计应用的成本。<br/><br/>
	具体请看：<br/>
	<a target="_blank" href="./appCost.do?method=showTop" style="font-size:12px;color:red;">
	公司每月平均成本排行</a>　　组织层面的成本排行<br/>
	<a target="_blank" href="./appCost.do?method=showApp" style="font-size:12px;color:red;">
	应用每周成本明细</a>　　 　　应用层面，详细曾本，包括硬件，hsf依赖，db，tair等 <br/>
	<a target="_blank" href="./appCost.do?method=showPreTop" style="font-size:12px;color:red;">
	每千次调用成本top10</a> 　　这个值比较高的应用，说明利用率不高
	<br/>
	
	<br>
</div>

<hr style="clear:both;"/>

目前业务系统和各个C以及一些基础设施存在着下图的依赖关系:
<div id="appMain">

	<img src="<%=request.getContextPath() %>/statics/images/app_arc.JPG" style="margin:auto;top:auto;width:550px;hight:400px;"/>
</div>

因此我们得出了下面的成本计算方式：<br/>
1.基础依赖系统只算硬件成本以及运维成本(tair/db等)<br/>
2.业务系统以及各个C除了算硬件成本、运维成本还要算上依赖成本<br/>


<img src="<%=request.getContextPath() %>/statics/images/cost_all.JPG" style="width:450px;hight:60px;"/><br/>
其中：<br/>
<img src="<%=request.getContextPath() %>/statics/images/cost_hard.jpg" style="width:390px;hight:55px;"/><br/>
<img src="<%=request.getContextPath() %>/statics/images/cost_ops.jpg" style="width:200px;hight:25px;"/><br/><br/>

<hr/>
<div id="main">
	目前成本中心的数据覆盖量还不够全，持续完善中，感谢大家的支持！！
</div>

</body>
</html>