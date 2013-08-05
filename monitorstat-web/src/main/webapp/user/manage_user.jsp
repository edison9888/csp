<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@ page import="java.util.*"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>核心监控-用户管理</title>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/datatable/js/jquery.dataTables.js"></script>
<script type="text/javascript">
var oTable;
$(document).ready(function() {

	//要想选择一行会变色，必须在table中添加class="display"属性
	$("#userInfoTableId tbody").click(function(event) {
		$(oTable.dataTable().fnSettings().aoData).each(function (){
			$(this.nTr).removeClass('row_selected');
		});
		$(event.target.parentNode).addClass('row_selected');
	});
	/* Init the table */
	oTable = $('#userInfoTableId').dataTable( );
});

/* Get the rows which are currently selected */
function fnGetSelected( oTableLocal )
{
	var aReturn = new Array();
	var aTrs = oTableLocal.fnGetNodes();
	
	for ( var i=0 ; i<aTrs.length ; i++ )
	{
		if ( $(aTrs[i]).hasClass('row_selected') )
		{
			aReturn.push( aTrs[i] );
		}
	}
	return aReturn;
}

$(function(){
	 $(".deleteAction").click(function(){
	   return confirm("想清楚要删除了没有啊?");
	  });
	});


</script>
</head>
<body>
<table width="1000" align="center" cellspacing="0" cellpadding="0" border="0"  style="margin:0 auto;">
	<tr>
	  <td><jsp:include page="../top.jsp"></jsp:include></td>
  	</tr>

<%
String id = request.getParameter("id");
String action = request.getParameter("action");
String searchUserName = request.getParameter("searchUserName");
String appNameSelect = request.getParameter("appNameSelect");
String parentGroupSelect = request.getParameter("parentGroupSelect");



if("delete".equals(action)){
	MonitorUserAo.get().deleteLoginUserPo(Integer.parseInt(id));
}

//初始化应用分组时候用到
List<AppInfoPo> appList = AppInfoAo.get().findAllEffectiveAppInfo();
String selectAppName = "";
String selectGroupName  = "";
if(appNameSelect != null && !"".equals(appNameSelect)) {
	selectAppName = AppCache.get().getKey(Integer.parseInt(appNameSelect)).getAppName(); 
	selectGroupName = AppCache.get().getKey(Integer.parseInt(appNameSelect)).getGroupName();
}

%>
<tr><td align="center"><form action="">
<table >
	<tr >
		<td align="center">
			用户名:<input id="searchUserName" name="searchUserName" type="text" value="<%=(searchUserName==null) ? "" : searchUserName %>"></input>&nbsp;&nbsp;
			应用组名:<select id="parentGroupSelect" name="parentGroupSelect" onchange="groupChange(this)" >
			</select>&nbsp;&nbsp;
			应用名:<select id="appNameSelect" name="appNameSelect"></select>&nbsp;&nbsp;
			<input type="submit" value="搜索" />
		</td>
	</tr>
</table>
</form>
</td></tr>

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

<% 
List<LoginUserPo> listUser = MonitorUserAo.get().findAllUser();
List<LoginUserPo> filterList = listUser;

if(!"".equals(searchUserName) && null != searchUserName) {
	
	filterList = MonitorUserAo.get().findMatcherUser(filterList, searchUserName);
}

if(!"".equals(appNameSelect) && !"".equals(parentGroupSelect) && null != appNameSelect && null != parentGroupSelect) {
	
	filterList = MonitorUserAo.get().findMatcherAppName(filterList, appNameSelect);
}
%>


<tr><td algin="right">
<input type="button" value="添加新用户" onclick="location.href='./user_info_add2.jsp'">
</td></tr>

<tr class="headcon">
        <td colspan="12" align="center">检索后的用户列表</td>
</tr>

<tr><td>
<table width="1000"  id="userInfoTableId" class="datalist"  >
	 
	
	<tr class="ui-widget-header ">
		<td>用户名</td>
		<td>旺旺</td>
		<td>手机</td>
		<td>邮箱</td>
		<td>操作</td>
	</tr>

	<tbody>
	<%for(LoginUserPo po:filterList){ %>
	<tr>
		<td><%=po.getName() %></td>
		<td><%=po.getWangwang() %></td>
		<td><%=po.getPhone() %></td>
		<td><%=po.getMail() %></td>
		<td><a href="./user_info_update2.jsp?id=<%=po.getId() %>">修改</a>&nbsp;&nbsp;
		<a id="deleteAction" class="deleteAction" href="./manage_user.jsp?id=<%=po.getId() %>&action=delete">删除</a> &nbsp;&nbsp;
		<a href="./user_info2.jsp?id=<%=po.getId() %>">查看</a></td>
	</tr>
	<%} %>
	
	</tbody>
</table></td>
</tr>

<tr>
	  <td><img  src="<%=request.getContextPath () %>/statics/images/line.png"  width="1000" height="1"></td>
	</tr>
<tr><td>
<jsp:include page="../bottom.jsp"></jsp:include>
</td></tr>
</table>
</body>
</html>