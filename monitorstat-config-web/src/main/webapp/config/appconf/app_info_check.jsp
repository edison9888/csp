<%@ page language="java" contentType="text/html; charset=GBK"
	isELIgnored="false" pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.net.URLEncoder"%>

<%@page import="com.taobao.monitor.common.po.HostPo"%><html>
<head>
	<title>应用配置</title>
	
	<link type="text/css"  href="<%=request.getContextPath() %>/common_res/css/bootstrap.css" rel="stylesheet">
	
	<style type="text/css">
		body {
		  padding-top: 60px;
		}
	</style>
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/common_res/js/jquery.js"></script>
	<script type="text/javascript"	src="<%=request.getContextPath() %>/common_res/js/jquery.tablesorter.js"></script>
	
	<%
	AppInfoPo appInfoPo = (AppInfoPo)request.getAttribute("appInfoPo");
	
	List<HostPo>  hostList = (ArrayList<HostPo>)request.getAttribute("hostList");

	Set<HostPo> monitorHostSet = (HashSet<HostPo>)request.getAttribute("monitorHostSet");
	
	String opsName = appInfoPo.getOpsName();
	%>
	
	<script>
		$(function() {

			$("#close").click(function() {
				window.close();
			});

			$("#sortTable").tablesorter( { sortList: [[ 0, 0 ]] } );
			
		});

	</script>
	
</head>

<body>

<jsp:include page="../../header.jsp"></jsp:include>

<div class="container-fluid">
	<jsp:include page="../../leftmenu.jsp"></jsp:include>
	
	<div class="content">
		<div class="page-header">
			<h2>应用详细信息</h2>
		</div>
		<table class="bordered-table zebra-striped condensed-table">
			<tbody>
				<tr>
					<td class="blue">应用名</td>
					<td><%=appInfoPo.getAppName() %></td>
					<td class="blue">应用类型</td>
					<td><%=appInfoPo.getAppType().equals("pv")?"前端应用":"center应用" %></td>
				</tr>
				<tr>
					<td class="blue">OPS_Name</td>
					<td><%=appInfoPo.getOpsName() %></td>
					<td class="blue">Feature</td>
					<td><%=appInfoPo.getFeature() %></td>
				</tr>
				<tr>
					<td class="blue">Group</td>
					<td><%=appInfoPo.getGroupName() %></td>
					<td class="blue">高峰期时间段</td>
					<td><%=appInfoPo.getAppRushHours() %></td>
				</tr>
				<tr>
					<td class="blue">日报</td>
					<td><%=appInfoPo.getDayDeploy() == 1?"生效":"不生效" %></td>
					<td class="blue">实时监控</td>
					<td><%=appInfoPo.getTimeDeploy() == 1?"生效":"不生效" %></td>
				</tr>
				<tr>
					<td class="blue">登陆用户名</td>
					<td><%=appInfoPo.getLoginName() %></td>
					<td class="blue">状态</td>
					<td><%=appInfoPo.getAppStatus()==0?"正常":"删除" %></td>
				</tr>
				<tr>
					<td class="blue">登陆用户名</td>
					<td><%=appInfoPo.getLoginName() %></td>
					<td class="blue">状态</td>
					<td><%=appInfoPo.getAppStatus()==0?"正常":"删除" %></td>
				</tr>
				<tr>
					<td class="blue">应用主机数</td>
					<td><%=hostList.size() %></td>
					<td class="blue">监控主机数</td>
					<td><%=hostList.size()-monitorHostSet.size() %></td>
				</tr>
			</tbody>
		</table>
		<br>	
		<table class="zebra-striped condensed-table" id="sortTable">
			<thead>
				<tr>
					<th class="blue">主机名</th>
					<th class="blue">IP</th>
					<th class="blue">机房</th>
					<th class="blue">实时监控</th>
					<th class="blue">主机名</th>
					<th class="blue">IP</th>
					<th class="blue">机房</th>
					<th class="blue">实时监控</th>
				</tr>
			</thead>
			<tbody>
			<%
			for (int i = 0; i < hostList.size(); i++) {
				HostPo po = hostList.get(i);
			%>
				<tr>
					<td><%=po.getHostName() %></td>
					<td><%=po.getHostIp() %></td>
					<td><%=po.getHostSite() %></td>
					<td><%=!monitorHostSet.contains(po) %></td>
					<%
					i++;
					if (i == hostList.size()) {
					%>
					<td></td>
					<td></td>
					<td></td>
					<td></td>
					<%
					} else {
						HostPo po1 = hostList.get(i);
					%>
					<td><%=po1.getHostName() %></td>
					<td><%=po1.getHostIp() %></td>
					<td><%=po1.getHostSite() %></td>
					<td><%=!monitorHostSet.contains(po1) %></td>
					<%
					}
					%>
				</tr>
			<%
			}
			%>
			</tbody>
		</table>
		
		<div class="actions">
			<button class="btn primary pull-right" id="close">关闭</button>
			<br>
		</div>
		
		<footer>
		  <p>&copy; TaoBao 2011</p>
		</footer>
		
	</div>
</div>

</body>
</html>