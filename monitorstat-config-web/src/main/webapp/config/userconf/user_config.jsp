<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.TimeConfTmpPo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.csp.config.po.LoginUserPo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>用户管理</title>

<link type="text/css"  href="<%=request.getContextPath() %>/common_res/css/bootstrap.css" rel="stylesheet">
<style type="text/css">
	body {
	  padding-top: 60px;
	}
</style>
<script type="text/javascript" src="<%=request.getContextPath() %>/common_res/js/jquery.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/common_res/js/jquery.tablesorter.js"></script>
<%
List<LoginUserPo> filterList = (ArrayList<LoginUserPo>)request.getAttribute("filterList");
List<AppInfoPo> appList = (ArrayList<AppInfoPo>)request.getAttribute("appList");
String searchUserName = (String)request.getAttribute("searchUserName");
String selectGroupName = (String)request.getAttribute("selectGroupName");
String selectAppName = (String)request.getAttribute("selectAppName");
%>
<script type="text/javascript">
$(function() {
	$("#sortTable").tablesorter( { sortList: [[ 0, 0 ]] } );

	$(".deleteAction").click(function(){
	   return confirm("想清楚要删除了么?");
	  });
});

function gotoAddUser() {
	//alert("aaaaaaaaaa");
	location.href = "<%=request.getContextPath()%>/show/UserConf.do?method=gotoAddUser";
}
</script>
</head>
<body>
<jsp:include page="../../header.jsp"></jsp:include>

<div class="container-fluid">
	<jsp:include page="../../leftmenu.jsp"></jsp:include>
	
	<div class="content">
		<div class="page-header">
			<h2>
				用户配置管理
			</h2>
		</div>
		<form action="<%=request.getContextPath() %>/show/UserConf.do?method=gotoUserConf" method="post">
		<div class="clearfix">
			组名:<select class="medium" id="parentGroupSelect" name="parentGroupSelect" onchange="groupChange(this)" >
			</select>&nbsp;&nbsp;
			应用名:<select class="medium" id="appNameSelect" name="appNameSelect"></select>&nbsp;&nbsp;
			
			用户名:<input class="medium" id="searchUserName" name="searchUserName" size="30" type="text" value="<%=(searchUserName==null) ? "" : searchUserName %>">
			&nbsp;&nbsp;&nbsp;&nbsp;
			<input class="btn primary" type="submit" value="搜索" />
			<input class="btn primary pull-right" type="button" onclick="gotoAddUser()" value="增加用户" >
        </div>
        </form>
		
		<br>
		<table class="zebra-striped condensed-table bordered-table" id="sortTable">
			<thead>
				<tr>
					<th class="blue">用户名</th>
					<th class="blue">旺旺</th>
					<th class="blue">手机</th>
					<th class="blue">邮箱</th>
					<th class="blue">操作</th>
				</tr>
			</thead>
			<tbody>
				<%for(LoginUserPo po:filterList){ %>
				<tr>
					<td><%=po.getName() %></td>
					<td><%=po.getWangwang() %></td>
					<td><%=po.getPhone() %></td>
					<td><%=po.getMail() %></td>
					<td><a href="<%=request.getContextPath() %>/show/UserConf.do?method=gotoUserUpdate&userId=<%=po.getId() %>">修改</a>&nbsp;&nbsp;
					<a id="deleteAction" href="<%=request.getContextPath() %>/show/UserConf.do?method=deleteUser&userId=<%=po.getId() %>">删除</a> &nbsp;&nbsp;
					<a href="<%=request.getContextPath() %>/show/UserConf.do?method=gotoUserCheck&userId=<%=po.getId() %>">查看</a></td>
				</tr>
				<%} %>
			</tbody>
		</table>
		<footer>
		  <p>&copy; TaoBao 2011</p>
		</footer>
	</div>
</div>
<script type="text/javascript">

var groupMap ={}

function addAppGroup(groupName,appName,appId){
	
	if(!groupMap[groupName]){
		groupMap[groupName]={};
	}
	if(!groupMap[groupName][appName]){
		groupMap[groupName][appName]=appId;
	}			
}

function groupChange(selectObj){
	var groupName = selectObj.options[selectObj.selectedIndex].value;
	var group = groupMap[groupName];
	if(group){
		clearSubSelect();
		fillSubSelect(groupName);
	}
}

function clearSubSelect(){
	 document.getElementById("appNameSelect").options.length=0;		
	
}
function fillSubSelect(groupName,value){
	var group = groupMap[groupName];

	var ops = document.getElementById("appNameSelect").options;
	var len = ops.length;
	for (name in group){
		document.getElementById("appNameSelect").options[len++]=new Option(name,group[name]);
		if(name == value){
			document.getElementById("appNameSelect").options[len-1].selected=true;
		}
	}
}
	
function initParentSelect(gname,gvalue){
	clearSubSelect();
	var len = document.getElementById("parentGroupSelect").options.length;
	for (name in groupMap){
		document.getElementById("parentGroupSelect").options[len++]=new Option(name,name);
		if(name == gname){
			document.getElementById("parentGroupSelect").options[len-1].selected=true;
		}
	}
			
	if(gname&&gvalue){
		fillSubSelect(gname,gvalue);
	}else{
		groupChange(document.getElementById("parentGroupSelect"));
	}

}
addAppGroup("","","")
<%
for(AppInfoPo app:appList){
%>
addAppGroup("<%=app.getGroupName()%>","<%=app.getAppName()%>","<%=app.getAppId()%>")
<%				
}
%>
initParentSelect("<%=selectGroupName%>","<%=selectAppName%>");
</script>
</body>
</html>