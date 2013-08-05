<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.KeyPo"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.vo.AlarmDataPo"%>
<%@page import="com.taobao.monitor.web.util.UrlCode"%>
<%@page import="com.taobao.monitor.web.ao.MonitorAlarmAo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.taobao.monitor.common.ao.center.KeyAo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="org.apache.commons.lang.StringUtils"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>应用监控系统-查询应用key</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script language="javascript" src="<%=request.getContextPath() %>/statics/js/jscript.isselect.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>

<style type="text/css">
div {
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
.report_on{background:#bce774;}
.fc {
display:block;
margin-top:-15px;
overflow:hidden;
position:absolute;
z-index:1000;
}

.fci {
border:1px solid #7589C3;
margin-left:9px;
position:relative;
}

.fcc {
background:none repeat scroll 0 0 #FFFFFF;
border:4px solid #C1CFF3;
overflow-x:hidden;
overflow-y:auto;
padding:10px 15px;
width:auto;
}
 
.jj8 {
background-position:-30px -50px;
display:block;
height:25px;
left:1px;
position:absolute;
top:8px;
width:11px;
z-index:999;
}

 
.jj {
background-image:url("<%=request.getContextPath () %>/statics/images/s.gif");
overflow:hidden;
}
</style>
<script type="text/javascript">
$(function(){
	//IN_HSF-ProviderDetail  OUT_PageCache  OUT_HSF-Consumer  OUT_forest  OUT_TairClient EXCEPTION
	$("#keyBodyId tr td").mouseover(function(){
		$(this).parent().children("td").addClass("report_on");
	})
	$("#keyBodyId tr td").mouseout(function(){
		$(this).parent().children("td").removeClass("report_on");
	})
})


function noshow(obj){
	var el = obj.getElementsByTagName("div")[0];
	el.style.display='none';
}

function show(obj,event){
	var el = obj.getElementsByTagName("div")[0];
	el.style.left = event.clientX+500;
	el.style.top = event.clientY;
	el.style.display='block';
}

</script>
</head>

<body>
<jsp:include page="../head.jsp"></jsp:include>
<jsp:include page="../left.jsp"></jsp:include>
<%
List<AppInfoPo> appList = AppInfoAo.get().findAllEffectiveAppInfo();

String keyName = request.getParameter("keyName");
String appId= request.getParameter("appId");
AppInfoPo appInfopo = null;
if(appId==null){
	appId="2";
	
}
appInfopo = AppCache.get().getKey(Integer.parseInt(appId));
List<KeyPo> keyList = KeyAo.get().findKeyLikeName(keyName,appId==null?null:Integer.valueOf(appId));
List<AlarmDataPo> allalarmKeyList = MonitorAlarmAo.get().findAllAlarmKeyByAimAndLikeName(Integer.parseInt(appId),keyName);

Map<Integer,AlarmDataPo> alarmKeyMap = new HashMap<Integer,AlarmDataPo>();
for(AlarmDataPo po:allalarmKeyList){
	alarmKeyMap.put(Integer.parseInt(po.getKeyId()),po);
}
AppInfoPo appPo = AppCache.get().getKey(Integer.parseInt(appId));

List<AppInfoPo> listApp = AppInfoAo.get().findAllEffectiveAppInfo();
%>
<input type="button" value="告警点管理页面" onclick="location.href='manage_alarm_key.jsp'">
<input type="button" value="key管理页面" onclick="location.href='manage_key.jsp'">
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">查询条件</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<form action="./manage_key.jsp" method="get">
	key名称:<input name="keyName" value="<%=keyName==null?"":keyName %>">&nbsp;&nbsp;
	应用组:
	<select id="parentGroupSelect" onchange="groupChange(this)">	
	</select>
	应用名:
	<select name="appId"  id="appIdId">	
	</select>	
	<input value="查询" type="submit">
</form>
</div>
</div>



<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">key列表</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table id="keyBodyId" width="100%" border="1" class="ui-widget ui-widget-content">
<tr>	
	<td align="center">key名称</td>
	<td align="center" width="100">名称</td>
	<td align="center" width="100">操作</td>
</tr>	
<%
for(KeyPo po:keyList){
	
	if(po.getKeyName() !=null){
		StringBuffer buf = new StringBuffer();
	    char ch = ' ';
	    for( int i=0; i<po.getKeyName().length(); i++ ) {
	        ch = po.getKeyName().charAt(i);
	        if( ch == '<' ) {
	            buf.append( "&lt;" );
	        }
	        else if( ch == '>' ) {
	            buf.append( "&gt;" );
	        }
	        else {
	            buf.append( ch );
	        }
	    }
		
		po.setKeyName(buf.toString());
	}
	
%>
<tr>
	<td align="left"  >			
		<a style="text-decoration: none;" href='../comm/keyInfo_update.jsp?keyId=<%=po.getKeyId()%>' title="<%=po.getKeyName()%>"><%=po.getKeyName().length()>100?po.getKeyName().substring(0,100):po.getKeyName()%></a>
	</td>
	
	<td><a href='../comm/keyInfo_update.jsp?keyId=<%=po.getKeyId()%>'><%=po.getAliasName()==null?"<font color='red'>点击填写</font>":po.getAliasName()%></a></td>
	<td>
	<%
	if(alarmKeyMap.get(po.getKeyId())==null){
	%>
	<a target="_blank" href="add_alarm_key.jsp?keyId=<%=po.getKeyId() %>&appId=<%=appId %>">加入告警</a> 
	<%}else{%>
	<a href="manage_alarm_key.jsp?action=update&id=<%=alarmKeyMap.get(po.getKeyId()).getId() %>&keyName=<%=keyName==null?"":keyName %>&appId=<%=appId==null?"":appId %>">修改告警设置</a>
	<%}%>
	<a target="_blank" href="<%=request.getContextPath() %>/time/key_detail_time.jsp?appId=<%=appInfopo.getAppId() %>&keyId=<%=po.getKeyId() %>&appName=<%=appPo.getAppName() %>&aimName=<%=po.getKeyName()%>">查看</a>
	</td>
</tr>	
<%} %>
</table>

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
		 document.getElementById("appIdId").options.length=0;		
		
	}
	function fillSubSelect(groupName,value){
		var group = groupMap[groupName];
	
		var ops = document.getElementById("appIdId").options;
		var len = ops.length;
		for (name in group){
			document.getElementById("appIdId").options[len++]=new Option(name,group[name]);
			if(name == value){
				document.getElementById("appIdId").options[len-1].selected=true;
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