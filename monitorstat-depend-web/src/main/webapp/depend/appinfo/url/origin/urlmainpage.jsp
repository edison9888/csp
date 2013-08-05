<%@page import="com.taobao.csp.depend.util.MethodUtil"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.csp.depend.po.url.UrlOriginSummary"%>
<%@page import="com.taobao.csp.depend.po.url.RequestUrlSummary"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=GB18030"
	isELIgnored="false" pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/userjs/mainpage.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=request.getContextPath()%>/statics/datePicket/WdatePicker.js"></script>
<script language="JavaScript"
	src="<%=request.getContextPath()%>/statics/js/swfobject.js"></script>
<title>我调用的URL信息</title>
<%
String opsName = (String)request.getAttribute("opsName");
String selectDate = (String)request.getAttribute("selectDate");

ArrayList<UrlOriginSummary> urlOriginList = (ArrayList<UrlOriginSummary>)request.getAttribute("urlOriginList");
ArrayList<RequestUrlSummary> urlRequestList = (ArrayList<RequestUrlSummary>)request.getAttribute("urlRequestList");

String originTotal = (String)request.getAttribute("originTotal");		
String requestTotal = (String)request.getAttribute("requestTotal");
String originPreTotal = (String)request.getAttribute("originPreTotal");		
String requestPreTotal = (String)request.getAttribute("requestPreTotal");
%>

</head>
<body>
	<%@ include file="../../../header.jsp"%>
	<form id="mainForm"
		action="<%=request.getContextPath()%>/show/urlorigin.do" method="get">
		<input type="hidden" value="showUrlOriginMain" name="method">
		<div style="text-align: center">
			<div id="page_nav"></div>
		</div>
		<script>
		$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {appName: '${param.opsName}', selectDate:'<%=selectDate%>'});
		</script>
		<div class="container">
			<div class="row">
				<div class="span12">
					<span style="font-weight: bold">&nbsp;&nbsp;<%=opsName%>的URL信息
					</span>
				</div>
			</div>
			<div class="row">
				<div class="span12">
					<table width="100%"
						class="table table-striped table-bordered table-condensed">
						<tr>
							<td width="100">总请求量：</td>
							<td><%=Utlitites.fromatLong(originTotal)%>&nbsp;&nbsp;<%=Utlitites.scale(originTotal, originPreTotal)%></td>
							<td width="100">总跳转量：</td>
							<td><%=Utlitites.fromatLong(requestTotal)%>&nbsp;&nbsp;<%=Utlitites.scale(requestTotal, requestPreTotal)%></td>
						</tr>
					</table>
				</div>
			</div>
			<div class="row">
				<div class="span12">
					<table width="100%"
						class="table table-striped table-bordered table-condensed">
						<tr>
							<td colspan="4" align="center">来源URL前10名 &nbsp;&nbsp; <a
								href="<%=request.getContextPath()%>/show/urlorigin.do?method=showOriginUrlAll&opsName=<%= opsName%>&selectDate=<%= selectDate%>">查看全部</a></td>
						</tr>
						<tr>
							<td align="center" width="30%">跳转URL</td>
							<td align="center" width="30%">调用总计</td>
							<td align="left" width="15%">百分比</td>
							<td align="center" width="15%">历史访问</td>
						</tr>
						<%
						for(int i=0; i < urlOriginList.size()&&i<10;i++) {
						  UrlOriginSummary is = urlOriginList.get(i);
						%>
						<tr>
							<td align="left"><%=is.getOriginUrl()%></td>
							<td align="center"><%=Utlitites.fromatLong(is.getOriginUrlNum()+"")%><%=Utlitites.scale((is.getOriginUrlNum() + ""), (is.getPreOriginUrlNum() + ""))%></td>
							<td><%=Arith.mul(Arith.div(is.getOriginUrlNum(),Double.parseDouble(originTotal),4), 100)%>%</td>
							<td align="center" width="120px">
							<a href="javascript:openWin('<%=request.getContextPath()%>/show/urlorigin.do?method=showUrlHistoryGraph&url=<%=is.getOriginUrl()%>&opsName=<%=opsName%>&endDate=<%= selectDate%>&type=origin',700,1100)">查看历史走势</a>
							</td>
						</tr>
						<% }%>
					</table>
				</div>
			</div>			
			<div class="row">
				<div class="span12">
					<table width="100%"
						class="table table-striped table-bordered table-condensed">
						<tr>
							<td colspan="4" align="center">实际跳转URL前10名 &nbsp;&nbsp; <a
								href="<%=request.getContextPath()%>/show/urlorigin.do?method=showRequestUrlAll&opsName=<%= opsName%>&selectDate=<%= selectDate%>">查看全部</a></td>
						</tr>
						<tr>
							<td align="center" width="30%">跳转URL</td>
							<td align="center" width="30%">调用总计</td>
							<td align="left" width="15%">百分比</td>
							<td align="center" width="15%">历史访问</td>
						</tr>
						<%
						for(int i=0; i < urlRequestList.size()&&i<10;i++) {
							RequestUrlSummary is = urlRequestList.get(i);
						%>
						<tr>
							<td align="left" title="<%=is.getRequestUrl()%>"><%=is.getRequestUrl()%>
							&nbsp;&nbsp;<a href='<%=request.getContextPath()%>/rate/callrate.do?method=gotoTopoPage&sourceKey=<%=is.getRequestUrl()%>&appName=<%=opsName%>&type=child_key&collectTime=<%=selectDate%>' target="_blank">查看调用路径 &nbsp;<img src="<%=request.getContextPath()%>/statics/css/images/newhotcool.gif" /></a>
							</td>
							<td align="center"><%=Utlitites.fromatLong(is.getRequestNum()+"")%><%=Utlitites.scale((is.getRequestNum() + ""), (is.getPreRequestUrlNum() + ""))%></td>
							<td><%=Arith.mul(Arith.div(is.getRequestNum(),Double.parseDouble(requestTotal),4), 100)%>%</td>
							<td align="center" width="120px">
							<a href="javascript:openWin('<%=request.getContextPath()%>/show/urlorigin.do?method=showUrlHistoryGraph&url=<%=is.getRequestUrl()%>&opsName=<%=opsName%>&endDate=<%= selectDate%>&type=request',700,1100)">查看历史走势</a>							
							</td>
						</tr>
						<% }%>
					</table>
				</div>
			</div>
		</div>
	</form>
</body>
</html>