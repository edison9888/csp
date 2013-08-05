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
	<title>Ӧ������</title>
	
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
		var i =window.confirm( "Ҫɾ���~~~�׿�Ҫ�����~~~"); 
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
				<button class="btn primary" onclick="goToGroupDetail()">��ʾ����</button>
				&nbsp;&nbsp;&nbsp;
				<button class="btn primary" onclick="goToAllAppPage()">��ʾȫ��</button>
				<button class="btn primary pull-right" onclick="gotoAddAppPage()">����Ӧ��</button>
				
			</div>
		</div>
		<br>
		<br>
		<table class="bordered-table zebra-striped condensed-table" id="sortTable">
			<thead>
				<tr>
					<th class="blue">Ӧ��ID</th>
					<th class="blue">Ӧ����</th>
					<th class="blue">Feature</th>
					<th class="blue">ops����</th>
					<th class="blue">��������</th>
					<th class="blue">״̬</th>
					<th class="blue">����</th>
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
					<td align="center"><%=po.getAppStatus() == 0 ? "����" : "ɾ��"%></td>
					<%
						if(po.getAppStatus() == 0) {
					%>
					<td align="center">
						<a href="<%=request.getContextPath()%>/show/appconfig.do?method=showAppDetail&appId=<%=po.getAppId() %>">�޸�</a>&nbsp;&nbsp;
						<a id="deleteAction" class="deleteAction" href="javascript:deleteApp('<%=po.getAppId() %>')">ɾ��</a> &nbsp;&nbsp;
						<a target='_blank' href="<%=request.getContextPath()%>/show/appconfig.do?method=checkAppDetail&appId=<%=po.getAppId() %>">�鿴</a> &nbsp;&nbsp;
						<a>�ձ�</a>&nbsp;&nbsp;
						<a target='_blank' href="<%=request.getContextPath()%>/show/appconfig.do?method=getTimeConfig&appId=<%=po.getAppId() %>">ʵʱ</a>&nbsp;&nbsp;
					</td>
					
					<%
						} else {
					%>
					<td align="center">
						<a href="javascript:cancelDeleteApp('<%=po.getAppId() %>')">ȡ��ɾ��</a> &nbsp;&nbsp;
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