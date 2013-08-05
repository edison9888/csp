<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.baseline.db.po.KeyValueBaseLinePo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.common.ao.center.HostAo"%>
<%@page import="com.taobao.monitor.common.po.HostPo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="cache, must-revalidate">
<title>监控</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>


<style type="text/css">
div{
font-size: 12px;

}

table {
	margin: 1em 0;
	border-collapse: collapse;
	width: 100%;
	font-size: 12px;
	margin: 0px 0;
}

table td {
font-size: 12px;
}

.ui-button {
	outline: 0;
	margin: 0;
	padding: .4em 1em .5em;
	text-decoration: none; ! important;
	cursor: pointer;
	position: relative;
	text-align: center;
}

.ui-dialog .ui-state-highlight,.ui-dialog .ui-state-error {
	padding: .3em;
}

.headcon {
	font-family: "宋体";
	font-size: 12px;
	font-weight: bold;
	text-indent: 3px;
	border-color: #316cb2;
	/*text-transform: uppercase;*/
	background: url(<%=request.getContextPath () %>/statics/images/4_17.gif);
}
img {
cursor:pointer;
}
</style>
</head>
<body>

<jsp:include page="../head.jsp"></jsp:include>
<%

String action = request.getParameter("action");
String name = request.getParameter("name");
String appId = request.getParameter("appId");
List<HostPo> hostList = HostAo.get().findTimeAppHost(Integer.parseInt(appId));

Map<Integer,Set<String>> cancelHost = null;
Set<String> ipset = cancelHost.get(Integer.parseInt(appId));
List<AppInfoPo> appList = AppInfoAo.get().findAllEffectiveAppInfo();
AppInfoPo appInfopo = AppCache.get().getKey(Integer.parseInt(appId));
%>
<form action="alarm_filter_host.jsp">
<select id="parentGroupSelect" onchange="groupChange(this)">	
</select>
<select id="appNameSelect" name="appId">	
</select>
<input type="submit" value="查看">
</form>
<table>
	<tr>
		<td>机器</td>
	</tr>
	<%
		for(HostPo host:hostList){
			String hostName = host.getHostSite()+"_"+host.getHostIp();
			hostName = hostName.toLowerCase();
	%>
	<tr>
		<td>
		<%=host.getHostIp() %>
		<%if(ipset!=null&&ipset.contains(hostName)){ %>
			<a href="alarm_filter_host.jsp?action=recover&name=<%=hostName %>&appId=<%=appId %>">恢复</a>
			<%}else{ %>
			<a href="alarm_filter_host.jsp?action=cancel&name=<%=hostName %>&appId=<%=appId %>">取消</a>
			<%} %>
		</td>
	</tr>
	<%} %>
</table>

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
	<%
	Map<String,List<AppInfoPo>> appInfoGroupMap = new HashMap<String,List<AppInfoPo>>();
	for(AppInfoPo app:appList){
	%>
	addAppGroup("<%=app.getGroupName()%>","<%=app.getAppName()%>","<%=app.getAppId()%>")
	<%				
	}
	%>
	 initParentSelect("<%=appInfopo.getGroupName()%>","<%=appInfopo.getAppName()%>");
</script>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>