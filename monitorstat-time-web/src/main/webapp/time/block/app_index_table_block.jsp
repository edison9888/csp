<%@page import="com.taobao.csp.time.web.po.IndexPvTable"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.csp.time.web.po.IndexHSfTable"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"   src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
<script type="text/javascript"		src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>
<%
Map<String,Map<String,IndexPvTable>> siteMap =(Map<String,Map<String,IndexPvTable>>)request.getAttribute("siteMap");
List<String> timeList  = (List<String>)request.getAttribute("timeList");

List<String> siteList = new ArrayList<String>();
siteList.addAll(siteMap.keySet());
Collections.sort(siteList);

%>
	<div class="row-fluid" >
			TMD_Block收集的阻挡信息包括:punish,checkcode,hourglass三种拦截模式,TMD三种拦截模式说明如下：<br/>
			<table class="table table-striped table-bordered table-condensed"  width="100%">
				<tr>
					<td>名称</td>
					<td>说明</td>
					<td>拦截结果</td>
				</tr>			
				<tr>
					<td>punish</td>
					<td>超出BaseQPS,被地域限流被拦截;被hotpatch规则拦截</td>
					<td>跳转到拒绝页面</td>
				</tr>
				<tr>
					<td>checkcode</td>
					<td>被黑名单拦截</td>
					<td>跳转到验证码页面</td>
				</tr>
				<tr>
					<td>hourglass</td>
					<td>超出最大QPS限流被拦截</td>
					<td>跳转到等待页面</td>
				</tr>								
			</table>
			<hr>	
		<span>${appInfo.opsName}</span>的Block信息包括TMD_Block和SS_Monitor两种阻挡，如下面表格(Tdod Block&nbsp;/&nbsp;SS Block)&nbsp;&nbsp;
		<a href="<%=request.getContextPath()%>/index.do?method=showBlockHost&appId=${appInfo.appId}" target="_blank;">机器信息</a>&nbsp;&nbsp;					
		<a href="<%=request.getContextPath()%>/app/detail/history.do?appName=${appInfo.appName}&keyName=PV-Block&method=showHistory" target="_blank;">查看阻挡历史</a>
			<hr>
		<table width="100%"   class="table table-striped table-bordered table-condensed">
			<tr>
				<td>&nbsp;</td>
				<%for(String site:siteList){ %>
				<td colspan="6" align="center"><%=site%></td>
				<%} %>
			</tr>
			<tr>
				<td>时间</td>
				<%for(String site:siteList){ %>
				<td>单台平均pv</td>
				<td>QPS</td>
				<td>机房PV</td>
				<td>Tdod block</td>
				<td>SS block</td>
				<%} %>
			</tr>
			<%
			for(String time:timeList){
			%>
			<tr>
				<td><%=time %></td>
				<%
				for(String siteName:siteList){
				Map<String,IndexPvTable> site = siteMap.get(siteName);
				if(site!= null){
					IndexPvTable hsf = site.get(time);
					if(hsf != null){
				%>
				<td><%=hsf.getPv() %></td>
				<td><%=hsf.getQps()%></td>
				<td><%=hsf.getPvForSite()%></td>
				<td><%=hsf.getTdodBlock()%></td>
				<td><%=hsf.getSsBlock()%></td>
				<%		
					}else{
						%>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>						
						<td></td>
						<%				
					}
				}
				}
				%>
			</tr>
			<%} %>
		</table>
	</div>