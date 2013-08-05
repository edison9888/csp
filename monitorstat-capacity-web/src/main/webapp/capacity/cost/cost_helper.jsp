<%@page import="com.taobao.csp.capacity.NumberUtil"%>
<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="com.taobao.csp.capacity.po.CapacityRankingPo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.csp.capacity.NumberUtil"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>成本计算公式</title>

</head>
<body style="padding-top:45px" class="span20">
<%@ include file="../../top.jsp" %>

<br>

<strong><font size="3">一、机器与成本值的关系</font></strong>
<br/>
<br/>
一台机器 = 10的9次方个成本，反过来1个成本 = 10的负9次方台机器<br/>
如果detail对itemcenter的依赖成本为2亿,说明itemcenter有两台机器是被detail所消耗<br/><br/><br/>

<strong><font size="3">二、单次调用成本</font></strong>
<br/>
<br/>
1、直接成本（针对自身的机器）<br/>
2、依赖成本（针对依赖应用的机器）
<br/><br/><br/>

<strong><font size="3">三、依赖模式</font></strong>
<br/>
<br/>
1、直接依赖（detail->itemcenter,itemcenter为detail直接依赖）<br/>
2、间接依赖（detail->itemcenter-tair1,tair1为detail的间接依赖）
&nbsp;间接依赖中不包含hsf类型依赖,比如detail->itemcenter-uic,uic不被统计到detail的间接依赖中
<br/><br/><br/>

目前成本中心的数据覆盖量还不够全，持续完善中，感谢大家的支持！！

</body>
</html>