<%@page import="com.taobao.csp.other.changefree.ChangeFree"%>
<%@page import="com.taobao.csp.other.artoo.Artoo"%>
<%@page import="com.taobao.monitor.common.po.CspTimeKeyAlarmRecordPo"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
<head>
<title>实时监控系统</title>
<meta http-equiv="content-type" content="text/html;charset=gbk" />
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
</head>
<%
List<ChangeFree> cfList = (List<ChangeFree> )request.getAttribute("cfList");
%>
<body>
	<div class="container-fluid">
			
	
			<table class="table table-striped table-bordered table-condensed">
			
			<%
			if(cfList==null||cfList.size()==0){
			%>
			<thead>
				<tr>
				<td colspan="6"  style="text-align:center"><b>最近2天changefree未有变更记录!</b></td>
			</tr>
			</thead>
			<%	
			}else{
			%>
			
			<thead>
				<tr>
				<td colspan="6"  style="text-align:center"><b>最近2天changefree上记录变更事件</b></td>
			</tr>
			</thead>
			<tr>
				<td>更变标题</td>
				<td>原因</td>
				<td>变更类型</td>
				<td>操作人员</td>
				<td>开始时间</td>
				<td>结束时间</td>
			</tr>
			
			<%
			for(ChangeFree cf:cfList){
			%>	
				<tr>
				<td width="120"><%=cf.getTitle() %></td>
				<td width="240" style="word-break:break-all;"><%=cf.getChange_reason() %></td>
				<td><%=cf.getChange_type() %></td>
				<td><%=cf.getUsername()%></td>
				<td><%=cf.getStart_time()%></td>
				<td><%=cf.getEnd_time()%></td>
			</tr>
			<%	
			}}
			%>
		</table>
	</div>
</body>
</html>
