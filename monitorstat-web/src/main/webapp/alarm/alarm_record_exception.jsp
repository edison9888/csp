<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.vo.AlarmRecordPo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<%@page import="com.taobao.monitor.web.ao.MonitorAlarmAo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>监控-告警接收管理页面</title>
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
</head>
<body>
<jsp:include page="../head.jsp"></jsp:include>
<jsp:include page="../left.jsp"></jsp:include>
<%
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String currentDay = request.getParameter("currentDay");
if(currentDay==null){
	currentDay = sdf.format(new Date());
}

String appId = request.getParameter("appId");
List<AppInfoPo> listApp = AppInfoAo.get().findAllEffectiveAppInfo();
if(appId==null){
	appId = listApp.get(0).getAppId()+"";
}

List<AlarmRecordPo> list = MonitorAlarmAo.get().findExceptionMonitorDataDesc(Integer.parseInt(appId),sdf.parse(currentDay));


Collections.sort(list,new Comparator<AlarmRecordPo>(){
	public int compare(AlarmRecordPo o1, AlarmRecordPo o2) {
		if(o1.getCollectTime().before(o2.getCollectTime())){
			return 1;
		}else if(o1.getCollectTime().equals(o2.getCollectTime())){
			return 0;
		}else if(o1.getCollectTime().after(o2.getCollectTime())){
			return -1;
		}
		return 0;
	}});
%>
<form action="alarm_record_exception.jsp">
<table width="100%" border="1">
  <tr>
    <td colspan="4" align="center">
    	日期:<input type="text" name="currentDay" value="<%=currentDay %>"/>
		应用名称:<select name="appId">
			<%for(AppInfoPo app:listApp){%> 
				<option value="<%=app.getAppId() %>" <%if(app.getAppId()==Integer.parseInt(appId)){out.print("selected");} %>><%=app.getAppName() %></option>
			<%} %>
			</select>
		<input type="submit" value="查看" />
	</td>
  </tr>  
</table>
<table width="100%" border="1">
	<tr>
		<td align="center">异常名称</td>
		<td align="center">机器</td>
		<td align="center">时间</td>
		<td align="center">操作</td>
	</tr>
	<%for(AlarmRecordPo po:list){ %>
	<tr>
		<td align="center"><%=po.getAlarmKeyName() %></td>
		<td align="center"><%=po.getSiteName() %></td>
		<td align="center"><%=sdf1.format(po.getCollectTime()) %></td>
		<td align="center"> <a href="exception_info.jsp?appId=<%=appId%>&keyId=<%=po.getAlarmkeyId() %>&collectTime=<%=currentDay %>">查看详细</a></td>
	</tr>
	<%} %>
</table>
<jsp:include page="../buttom.jsp"></jsp:include>
</form>
</body>
</html>