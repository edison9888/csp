<%@page import="com.taobao.csp.other.changefree.ChangeFree"%>
<%@page import="com.taobao.csp.other.artoo.Artoo"%>
<%@page import="com.taobao.monitor.common.po.CspTimeKeyAlarmRecordPo"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
<head>
<title>ʵʱ���ϵͳ</title>
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
				<td colspan="6"  style="text-align:center"><b>���2��changefreeδ�б����¼!</b></td>
			</tr>
			</thead>
			<%	
			}else{
			%>
			
			<thead>
				<tr>
				<td colspan="6"  style="text-align:center"><b>���2��changefree�ϼ�¼����¼�</b></td>
			</tr>
			</thead>
			<tr>
				<td>�������</td>
				<td>ԭ��</td>
				<td>�������</td>
				<td>������Ա</td>
				<td>��ʼʱ��</td>
				<td>����ʱ��</td>
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
