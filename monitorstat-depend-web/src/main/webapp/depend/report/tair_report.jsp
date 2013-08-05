<%@ page language="java" contentType="text/html; charset=GB18030"  isELIgnored="false" 
    pageEncoding="GB18030"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.taobao.csp.depend.po.tair.TairConsumeSummaryPo"%>
<%@page import="com.taobao.csp.depend.po.report.TairException"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
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
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet"/>
<title>Tair  调用与异常总量</title>
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
	List<TairConsumeSummaryPo> polist = (ArrayList<TairConsumeSummaryPo>)request.getAttribute("list");
	Map<String, TairConsumeSummaryPo> oldMap = (Map<String, TairConsumeSummaryPo>)request.getAttribute("oldMap");
	Map<String, TairException> exceptionMap = (Map<String, TairException>)request.getAttribute("exceptionMap");
	
%>
</head>
<body>
<h2 align="center">Tair  调用与异常总量</h2>
<h2 align="left">&nbsp;查询日期：${selectDate}</h2>
<h2 align="left">&nbsp;对比日期：${predate}</h2>
<table class="table table-striped table-bordered table-condensed"  width="100%" style="margin-bottom: 20px">
	<tr>
		<td class="firstTitleTd">TairGroupName</td>
		<td class="firstTitleTd">调用总次数</td>
		<td class="firstTitleTd">异常数</td>
		<td class="firstTitleTd">平均QPS</td>
		<td class="firstTitleTd">平均RT</td>
		<td class="firstTitleTd">详细</td>
	</tr>
	<%
		for(TairConsumeSummaryPo po: polist) {
			String tairGroupName = po.getTair_group_name();
			String tempTairGroupName = "";
			if(tairGroupName.equals(ConstantParameters.EMPTY_TAIR_GROUPNAME))
				tempTairGroupName = "";
			
			String oldCallNum = "";
			String oldQps = "";
			String oldRT = "";
			
			TairConsumeSummaryPo oldPo = oldMap.get(tairGroupName);
			if(oldMap.get(tairGroupName) != null) {
				oldCallNum = oldPo.getInvoking_all_num() + "";
				oldQps = oldPo.getRush_time_qps() + "";
				oldRT = oldPo.getRush_time_rt() + "";
			}
			
			TairException tairException = exceptionMap.get(tairGroupName);
			long curException = 0;
			long oldException = 0;
			if(tairException != null) {
				curException = tairException.getCurExceptionNum();
				oldException = tairException.getOldExceptionNum();
			}
			
			String aGroupName = po.getTair_group_name();
			if(aGroupName != null && "无".equals(aGroupName)) {
			  aGroupName = "";
			}
		%>
			<tr>
				<td><%=tairGroupName%></td>	
				<td><%=Utlitites.fromatLong(po.getInvoking_all_num()+ "")%>&nbsp;<%=Utlitites.scale((po.getInvoking_all_num() + ""), (oldCallNum))%></td>
				<td><%=Utlitites.fromatLong(curException+ "")%>&nbsp;<%=Utlitites.scale((curException + ""), (oldException + ""))%></td>
				<td><%=Utlitites.formatDotTwo((double)po.getRush_time_qps(),2)%>&nbsp;<%=Utlitites.scale((po.getRush_time_qps() + ""), (oldQps))%></td>
				<td><%=Utlitites.formatDotTwo((double)po.getRush_time_rt(),2)%>&nbsp;<%=Utlitites.scale((po.getRush_time_rt() + ""), (oldRT))%></td>
				<td><a href="http://cm.taobao.net:9999/monitorstat/tair/tair_top_new.jsp?collectTime=${selectDate}&groupName=<%=aGroupName%>" target="_blank">查看详细</a></td>
			</tr>	
		<%
		}
	%>
</table>
</body>
</html>