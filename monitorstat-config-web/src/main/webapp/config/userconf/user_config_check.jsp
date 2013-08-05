<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.TimeConfTmpPo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.csp.config.po.LoginUserPo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.po.ReportInfoPo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>�û���Ϣ�鿴</title>

<link type="text/css"  href="<%=request.getContextPath() %>/common_res/css/bootstrap.css" rel="stylesheet">
<link type="text/css" href="<%=request.getContextPath() %>/common_res/css/demo_table.css" rel="stylesheet" />
<link type="text/css" href="<%=request.getContextPath() %>/common_res/css/demo_page.css" rel="stylesheet" />

<style type="text/css">
	body {
	  padding-top: 60px;
	}
</style>
<script type="text/javascript" src="<%=request.getContextPath() %>/common_res/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common_res/js/bootstrap-modal.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common_res/js/jquery.dataTables.js"></script>
<%
LoginUserPo po = (LoginUserPo)request.getAttribute("loginUserPo");
String[] week = (String[])request.getAttribute("week");
String[] num = (String[])request.getAttribute("num");
String[] week1 = (String[])request.getAttribute("week1");
String[] num1 = (String[])request.getAttribute("num1");
List<AppInfoPo> appList = (ArrayList<AppInfoPo>)request.getAttribute("appList");
String initAppKeyRel = (String)request.getAttribute("initAppKeyRel");
%>
<script type="text/javascript">

function returnConfig() {
	location.href = "<%=request.getContextPath() %>/show/UserConf.do?method=gotoUserConf";
}

function selectedKey(appId, appName) {
	$("#label1").html("<h3>Ӧ��"+appName+"������key</h3>");
	$.post("<%=request.getContextPath()%>/show/UserConf.do?method=getSelectedKey",{selectedAppId:appId, relKey:'<%=initAppKeyRel%>'},
		function callback(json) {
			if (json != "ERROR" && json.isSuccess != "false") {
				//alert(json);
			} else {
				alert("error");
			}
		},
		"json"
	);
}

$(document).ready(function() {
	$('#example').dataTable();
} );
</script>
</head>
<body>
<jsp:include page="../../header.jsp"></jsp:include>

<div class="container-fluid">
	<jsp:include page="../../leftmenu.jsp"></jsp:include>
	
	<div class="content">
		<div class="page-header">
			<h2>
				�û�������Ϣ
				<button class="btn primary pull-right" onclick="returnConfig()">����</button>
			</h2>
			
		</div>
		<table class="zebra-striped condensed-table bordered-table">
			<tr>
				<th class="blue">�û���</th>
				<th class="blue">������</th>
				<th class="blue">�ֻ���</th>
				<th class="blue">����</th>
			</tr>
			<tr>
				<th><%=po.getName() %></th>
				<th><%=po.getWangwang() %></th>
				<th><%=po.getPhone() %></th>
				<th><%=po.getMail() %></th>
			</tr>	
		</table>
		
		<table class="zebra-striped condensed-table bordered-table">
		<tr>
			<th class="blue" colspan="4">Ȩ��˵��</th>
		</tr>
		<tr>
			<td colspan="4"><%=po.getPermissionDesc() %></td>
		</tr>
		</table>
		<div class="page-header">
			<h4>
				�澯������Ϣ��ʽ
			</h4>
		</div>
		<table class="zebra-striped condensed-table bordered-table">
			<tr>
				<th class="blue">�ֻ�����</th>
				<th class="blue">��һ</th>
				<th class="blue">�ܶ�</th>
				<th class="blue">����</th>
				<th class="blue">����</th>
				<th class="blue">����</th>
				<th class="blue">����</th>
				<th class="blue">����</th>
			</tr>
			<tr>
				<td class="blue">����ʱ�䷶Χ</td>
				<td><%=week[1]==null?"":week[1] %></td>
				<td><%=week[1]==null?"":week[2] %></td>
				<td><%=week[1]==null?"":week[3] %></td>
				<td><%=week[1]==null?"":week[4] %></td>
				<td><%=week[1]==null?"":week[5] %></td>
				<td><%=week[1]==null?"":week[6] %></td>
				<td><%=week[1]==null?"":week[0] %></td>
			</tr>
			<tr>
				<td class="blue">�������κ���</td>
				<td><%=num[1]==null?"":num[1] %></td>
				<td><%=num[1]==null?"":num[2] %></td>
				<td><%=num[1]==null?"":num[3] %></td>
				<td><%=num[1]==null?"":num[4] %></td>
				<td><%=num[1]==null?"":num[5] %></td>
				<td><%=num[1]==null?"":num[6] %></td>
				<td><%=num[1]==null?"":num[0] %></td>
			</tr>
		</table>
		<table class="zebra-striped condensed-table bordered-table">
		<thead>
			<tr>
				<th class="blue">��������</th>
				<th class="blue">��һ</th>
				<th class="blue">�ܶ�</th>
				<th class="blue">����</th>
				<th class="blue">����</th>
				<th class="blue">����</th>
				<th class="blue">����</th>
				<th class="blue">����</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td class="blue">����ʱ�䷶Χ</td>
				<td><%=week1[1]==null?"":week1[1] %></td>
				<td><%=week1[1]==null?"":week1[2] %></td>
				<td><%=week1[1]==null?"":week1[3] %></td>
				<td><%=week1[1]==null?"":week1[4] %></td>
				<td><%=week1[1]==null?"":week1[5] %></td>
				<td><%=week1[1]==null?"":week1[6] %></td>
				<td><%=week1[1]==null?"":week1[0] %></td>
			</tr>
			<tr>
				<td class="blue">�������κ���</td>
				<td><%=num1[1]==null?"":num1[1] %></td>
				<td><%=num1[1]==null?"":num1[2] %></td>
				<td><%=num1[1]==null?"":num1[3] %></td>
				<td><%=num1[1]==null?"":num1[4] %></td>
				<td><%=num1[1]==null?"":num1[5] %></td>
				<td><%=num1[1]==null?"":num1[6] %></td>
				<td><%=num1[1]==null?"":num1[0] %></td>
			</tr>
		</tbody>
		</table>
		
		<div class="page-header">
			<h4>
				ѡ����Ҫ�澯Ӧ��
			</h4>
		</div>
		<table class="zebra-striped condensed-table bordered-table">
			<thead>
				<tr>
					<th class="blue">Ӧ����</th>
				</tr>
			</thead>
			<tbody>
				<%
				for(AppInfoPo app : appList) {
					%>
					<tr id="<%=app.getAppId() %>">
						<td align='center'><%=app.getAppName() %></td>
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