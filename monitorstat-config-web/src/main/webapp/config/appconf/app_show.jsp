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
<html>
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
List<AppInfoPo>  groupAppList = (ArrayList<AppInfoPo>)request.getAttribute("groupAppList");

Set<String> groupNameSet = (HashSet<String>)request.getAttribute("groupNameSet");

String groupName = (String)request.getAttribute("groupName");

%>

<script type="text/javascript">

	function goToGroupDetail(){	
		var groupName = $("#normalSelect").val();
		var coderName = encodeURI(encodeURI(groupName));
		location.href="appconfig.do?method=showAppInfo&groupName=" + coderName;
	}

	function goToAllAppPage(){	
		location.href="appconfig.do?method=showAppInfo&groupName=AllApp";
	}

	function getHostList(opsName) {
		location.href="appconfig.do?method=showAppHost&opsName=" + opsName;
	}

	function gotoAddAppPage() {
		location.href="appconfig.do?method=addAppPage";
	}

	function deleteApp(appId) {
		var i =window.confirm( "要删除喽~~~亲可要想清楚~~~"); 
		if (i == 1) {
			var coderName = encodeURI(encodeURI('<%=groupName%>'));
			location.href="appconfig.do?method=deleteApp&groupName="+coderName+"&appId="+appId;
		}
	}

	function cancelDeleteApp(appId) {
		var coderName = encodeURI(encodeURI('<%=groupName%>'));
		location.href="appconfig.do?method=cancelDeleteApp&groupName="+coderName+"&appId="+appId;
	}

	$(function() {

		$("#sortTable").tablesorter( { sortList: [[ 0, 0 ]] } );
		
	});

</script>

</head>

<body>

<jsp:include page="../../header.jsp"></jsp:include>

<div class="container-fluid">
	<jsp:include page="../../leftmenu.jsp"></jsp:include>
	
	<div class="content">
		<div class="clearfix">
			
			<div class="input">
				<select id="normalSelect" name="normalSelect">
					<option>AllApp</option>
			<%
			for (String str : groupNameSet) {
				if (!str.equals(groupName)) {
				%>
					<option><%=str %></option>
				<%
				} else {
				%>
					<option selected="selected"><%=str %></option>
				<%	
				}
			}
			%>
				</select>
				&nbsp;&nbsp;&nbsp;
				<button class="btn primary" onclick="goToGroupDetail()">显示分组</button>
				&nbsp;&nbsp;&nbsp;
				<button class="btn primary" onclick="goToAllAppPage()">显示全部</button>
				<button class="btn primary pull-right" onclick="gotoAddAppPage()">增加应用</button>
				
			</div>
		</div>
		<br>
		<br>
		<table class="bordered-table zebra-striped condensed-table" id="sortTable">
			<thead>
				<tr>
					<th class="blue">应用ID</th>
					<th class="blue">应用名</th>
					<th class="blue">Feature</th>
					<th class="blue">ops名称</th>
					<th class="blue">分组名称</th>
					<th class="blue">状态</th>
					<th class="blue">操作</th>
				</tr>
			</thead>
			<tbody>
				<%
				for (int i = 0; i < groupAppList.size(); i++) {
					AppInfoPo po = groupAppList.get(i);
				%>
				<tr>
					<td align="center"><%=po.getAppId()%></td>
					<td align="center"><%=po.getAppName()%></td>
					<td align="center"><%=po.getFeature() %></td>
					<td align="center"><%=po.getOpsName()%></td>
					<td align="center"><%=po.getGroupName()%></td>
					<td align="center"><%=po.getAppStatus() == 0 ? "正常" : "删除"%></td>
					<%
						if(po.getAppStatus() == 0) {
					%>
					<td align="center">
						<a href="<%=request.getContextPath()%>/show/appconfig.do?method=showAppDetail&appId=<%=po.getAppId() %>">修改</a>&nbsp;&nbsp;
						<a id="deleteAction" class="deleteAction" href="javascript:deleteApp('<%=po.getAppId() %>')">删除</a> &nbsp;&nbsp;
						<a target='_blank' href="<%=request.getContextPath()%>/show/appconfig.do?method=checkAppDetail&appId=<%=po.getAppId() %>">查看</a> &nbsp;&nbsp;
						<a>日报</a>&nbsp;&nbsp;
						<a target='_blank' href="<%=request.getContextPath()%>/show/appconfig.do?method=getTimeConfig&appId=<%=po.getAppId() %>">实时</a>&nbsp;&nbsp;
					</td>
					
					<%
						} else {
					%>
					<td align="center">
						<a href="javascript:cancelDeleteApp('<%=po.getAppId() %>')">取消删除</a> &nbsp;&nbsp;
					</td>
					
					<%
						}
					%>
				</tr>
				<%
				}
				%>
			</tbody>
	    </table>
		<footer>
		  <p>&copy; TaoBao 2011</p>
		</footer>
		
	</div>
</div>

</body>
</html>