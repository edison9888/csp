<%@ page language="java" contentType="text/html; charset=GB18030"  isELIgnored="false" 
    pageEncoding="GB18030"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.taobao.csp.depend.po.report.ConsumeHSFReport"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.regex.Pattern"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.csp.depend.util.ConstantParameters"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk" >
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>HSF Center 调用与异常总量</title>
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet"/>
<style type="text/css">

*{
	font-size:13px;
}
html,body{
	margin:0;
	padding:0;
}
body{
    font-family:Arial,Helvetica,"Nimbus Sans L",sans-serif;
	background:#fafafa;
	text-align:center;
}
h3{
	font-size:20px;
	font-weight:bold;
	margin:12px 0;
}
h4{
	font-size:16px;
	font-weight:bold;
	color:#CC0000;
	margin:10px 0;
}
h5{
	font-size:14px;
	font-weight:bold;
	color:#990000;
	margin:5px 0;
}

.mian_body {
		width:100%;
		float: left;
		background-color: #E7EEFE;
		border: 1px solid #ccc;
		border-radius: 5px;
		-moz-border-radius: 5px;
		-webkit-border-radius: 5px;
	}
	
	.table_comm {
		width:100%;
		float: left;
		background-color: #E7EEFE;
		border: 1px solid #ccc;
		border-radius: 5px;
		-moz-border-radius: 5px;
		-webkit-border-radius: 5px;
	}
	
	.table_comm  td {
	border:1px solid #99BBE8;
	background: #fff;
	color: #4f6b72;
}

div {
	font-size: 12px;
}

table {
	margin: 1em 0;
	border-collapse: collapse;
	width: 100%;
	font-size: 12px;
	margin: 0px 0;
}

table td {
font-size: 12px;
}

h2{
	padding-left: 50px;
}

thead{
	background: #fff;
	color: #4f6b72;
}

.ui-button {
	outline: 0;
	margin: 0;
	padding: .4em 1em .5em;
	text-decoration: none; ! important;
	cursor: pointer;
	position: relative;
	text-align: center;
}

.ui-dialog .ui-state-highlight,.ui-dialog .ui-state-error {
	padding: .3em;
}

.headcon {
	font-family: "宋体";
	font-size: 12px;
	font-weight: bold;
	text-indent: 3px;
	border-color: #316cb2;
	/*text-transform: uppercase;*/
	background: url(<%=request.getContextPath () %>/statics/images/4_17.gif);
}
img {
cursor:pointer;
}
.report_on{background:#bce774;}

</style>

<%
	List<ConsumeHSFReport> polist = (ArrayList<ConsumeHSFReport>)request.getAttribute("list");
	Map<String, ConsumeHSFReport> oldMap = (Map<String, ConsumeHSFReport>)request.getAttribute("oldMap");
%>
</head>
<body>
<h2 align="center">HSF Center 调用与异常总量</h2>
<h2 align="left">&nbsp;查询日期：${selectDate}</h2>
<h2 align="left">&nbsp;对比日期：${predate}</h2>
<table class="table table-striped table-bordered table-condensed"  width="100%" style="margin-bottom: 20px">
	<tr>
		<td class="firstTitleTd">应用名</td>
		<td class="firstTitleTd">调用总量</td>
		<td class="firstTitleTd">BizException</td>
		<td class="firstTitleTd">Exception</td>
		<td class="firstTitleTd">详细</td>
	</tr>
	<%
		for(ConsumeHSFReport po: polist) {
			String appName = po.getAppName();
			
			String oldCallNum = "";
			String oldBiz = "";
			String oldException = "";
			
			ConsumeHSFReport oldPo = oldMap.get(appName);
			if(oldMap.get(appName) != null) {
				oldCallNum = oldPo.getCallNumber() + "";
				oldBiz = oldPo.getBizExceptionNum() + "";
				oldException = oldPo.getExceptionNum() + "";
			}
		%>
			<tr>
				<td><%=appName%></td>
				<td><%=Utlitites.fromatLong(po.getCallNumber()+ "")%>&nbsp;<%=Utlitites.scale((po.getCallNumber() + ""), (oldCallNum))%></td>	
				<td><%=Utlitites.formatDotTwo((double)po.getBizExceptionNum(),2)%>&nbsp;<%=Utlitites.scale((po.getBizExceptionNum() + ""), (oldBiz))%></td>
				<td><%=Utlitites.formatDotTwo((double)po.getExceptionNum(),2)%>&nbsp;<%=Utlitites.scale((po.getExceptionNum() + ""), (oldException))%></td>
				<td><a href="http://110.75.2.75:9999/depend/show/hsfprovide.do?method=showAppCenterHsfInfo&opsName=<%=appName%>&selectDate=${selectDate}" target="_blank">查看详细</a></td>
			</tr>	
		<%
		}
	%>
</table>
</body>
</html>