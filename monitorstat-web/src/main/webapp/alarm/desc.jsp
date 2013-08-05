<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.monitor.common.po.KeyPo"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.vo.AlarmDataPo"%>
<%@page import="com.taobao.monitor.web.vo.AlarmDescPo"%>


<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="com.taobao.monitor.web.ao.MonitorAlarmAo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>应用监控系统-告警信息描述</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>

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

</style>
<script type="text/javascript">

function selectAlarmCause(obj){
	var v = obj.options[obj.selectedIndex].value;
	if(v == "-1"){
		$("#otherCauseId").show();
	}else{
		$("#otherCauseId").hide();
	}	
}


function quickButton(){
	

	
}

function checkLen(obj) 
{ 
    var maxChars = 512;//最多字符数 
    if (obj.value.length > maxChars){
        alert("您输入的字符过多，系统将截取前512个字符!");
    	obj.value = obj.value.substring(0,maxChars);
    } 
    var curr = maxChars - obj.value.length; 
    document.getElementById("count").innerHTML = curr.toString(); 
} 

</script>
</head>

<body>
<jsp:include page="../head.jsp"></jsp:include>
<jsp:include page="../left.jsp"></jsp:include>

<%
request.setCharacterEncoding("gbk");
	 
String action = request.getParameter("action");
String alarmId = request.getParameter("alarmId");

String alarm_cause = request.getParameter("alarm_cause");

String otherCauseId = request.getParameter("otherCauseId");
String alarm_desc = request.getParameter("alarm_desc");
String fillinName = request.getParameter("fillinName");
String  appId = request.getParameter("appId");
 
if("add".equals(action)){
	boolean result1 = false;
	boolean result2 = false;
	String[] ids = alarmId.split(",");
	List<Long> idList = new ArrayList<Long>();
	for(String id:ids)
		idList.add(Long.parseLong(id));
	AlarmDescPo po = new AlarmDescPo();
	po.setAppId(Integer.parseInt(appId));
	po.setAlarmReason(alarm_cause);
	po.setAlarmDesc(alarm_desc);
	po.setAlarmIdList(idList);
	result1 = MonitorAlarmAo.get().addRecordAlarmDesc(po);
	result2 = MonitorAlarmAo.get().addRecordAlarmDescRelation(po);
	if(result1==true && result2 == true ){
		out.print("添加成功!页面将在两秒钟后跳转......");
		%>
			 <script type="text/javascript">window.setTimeout('location.href="alarm_record_detail.jsp";',2000);</script>
		<% 
	}
	
	else {
		out.print("对不起，添加失败!");
	%>
	<a href="javascript:history.go(-1)" >返回</a>
	<% }
	
}else{
	
	String startTime = request.getParameter("startTime");
	String endTime = request.getParameter("endTime");
	
	String otheralarm =  request.getParameter("o");
	StringBuilder alarmNames = new StringBuilder();
	StringBuilder alarmIds = new StringBuilder();
	List<String> idlist = new ArrayList<String>();
	Set<String> alarmNameList = new HashSet<String>();
	
	if(otheralarm != null){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		
		appId = request.getParameter("aId");
		String id =  request.getParameter("id");
		//String name =  request.getParameter("name");
		startTime = sdf.format(new Date());
		endTime = sdf.format(new Date());
		alarmNames.append("告警");
		alarmIds.append(id);
	}else{
		String[] picked = request.getParameterValues("alarm");
		for(int i=0;i<picked.length;i++){	
			String[] als = picked[i].split("&");
			if(als!=null&&als.length==2)
			{
				idlist.add(als[0]);
				alarmNameList.add(als[1]);		 
			}
		}
	
		for(String str:alarmNameList){
			alarmNames.append(str);
			alarmNames.append(",");
		}
		for(String str:idlist){
			alarmIds.append(str);
			alarmIds.append(",");
		}
	}
%>

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">添加告警原因</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table width="100%" border="1" class="ui-widget ui-widget-content">
	<tr>
		 <td>应用名称:<% 
		            List<AppInfoPo> listApp = AppInfoAo.get().findAllEffectiveAppInfo();
		 			for(AppInfoPo app:listApp){
		 				if(app.getAppId()==Integer.parseInt(appId)){out.print(app.getAppName());}
		 			}
		 		%>
		 </td>
	</tr>
	<tr>
		 <td>告警时间段:<%=startTime%>~<%=endTime%></td>
	</tr>
	<tr>
		 <td>告警名称:<% 
		       out.print(alarmNames.toString()); 
		 %>
		 </td>
	</tr>
</table>


<form action="./desc.jsp" method="post">

<table width="300" border="1" class="ui-widget ui-widget-content">
	<tr>
		<td width="80">造成告警原因:
		</td>
		<td><select name="alarm_cause" onChange="selectAlarmCause(this)">
			<option value="普通告警">普通告警</option>
			<option value="系统发布">系统发布</option>
			<option value="外部接口">外部接口</option>
			<option value="系统负载">系统负载</option>
			<option value="-1">其它</option>
		</select>
		<input id="otherCauseId" type="text" name="other" value="" style="display:none"> 
		</td>
	</tr>
	<tr>
		<td valign="top">
			详细描述:
		</td>
		<td valign="top">
			<textarea rows="5" cols="50" name="alarm_desc" onKeyUp="checkLen(this)"></textarea>
			<div>您还可以输入 <span id="count" style="color: red;">512</span> 个文字</div> 	
		</td>
	</tr>	
	<tr>
		<td  colspan="2" >
		<input type="hidden" name="appId" value="<%=appId %>">
		<input type="hidden" name="alarmId" value="<%=alarmIds.toString() %>">
		<input type="hidden" name="action" value="add">
			<input type="submit" value="确认提交">
		</td>
	</tr>
</table>
</form>
</div>
</div>
<%} %>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>