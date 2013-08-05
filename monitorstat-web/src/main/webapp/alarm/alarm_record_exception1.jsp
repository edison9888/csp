<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.vo.AlarmRecordPo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<%@page import="com.taobao.monitor.web.ao.MonitorAlarmAo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.regex.Pattern"%>
<%@page import="java.util.regex.Matcher"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>监控-告警接收管理页面</title>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
<table width="1000" align="center" cellspacing="0" cellpadding="0" border="0"  style="margin:0 auto;">
	<tr>
	  <td> <jsp:include page="../top.jsp"></jsp:include></td>
  	</tr>
<jsp:include page="../left.jsp"></jsp:include>
<%
String startTime = request.getParameter("startTime");
String endTime = request.getParameter("endTime");
String appId = request.getParameter("appId");
String exception = request.getParameter("exception");
String ip = request.getParameter("ip");
Calendar cal = Calendar.getInstance();
cal.add(Calendar.DAY_OF_MONTH,-1);
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");


if(startTime==null){
	startTime = sdf.format(cal.getTime());
};
cal.add(Calendar.DAY_OF_MONTH,1);
if(endTime==null){
	endTime = sdf.format(cal.getTime());
}



List<AppInfoPo> listApp = AppInfoAo.get().findAllEffectiveAppInfo();
if(appId==null){
	appId = listApp.get(0).getAppId()+"";
}

List<AlarmRecordPo> list = MonitorAlarmAo.get().findAllExceptionMonitorDataDesc(Integer.parseInt(appId),sdf.parse(startTime), sdf.parse(endTime));
if(list ==null){
	list = new ArrayList<AlarmRecordPo>();
}
List<AlarmRecordPo> filterList = list;
if(exception != null&&!"".equals(exception.trim())){
	 filterList = MonitorAlarmAo.get().findMatcherException(list, exception);
}


if(ip != null&&!"".equals(exception.trim())){
	List<AlarmRecordPo> filterListTmp = new ArrayList<AlarmRecordPo>();
	
	Pattern pattern = Pattern.compile(".*"+ ip+ ".*",Pattern.CASE_INSENSITIVE);
	Matcher matcher = null;
	for(AlarmRecordPo po : list) {
		matcher = pattern.matcher(po.getSiteName());
		if(matcher.matches()) {
			filterListTmp.add(po);
		}
	}
	filterList = filterListTmp;
}


Collections.sort(filterList,new Comparator<AlarmRecordPo>(){
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
<tr><td align="center">
<form action="alarm_record_exception1.jsp">
<table width="1000"  >
  <tr>
    <td colspan="4" align="center">
    	开始时间:<input type="text" name="startTime" value="<%=startTime %>" class="Wdate" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm'})"/>
    	结束时间:<input type="text" name="endTime" value="<%=endTime %>" class="Wdate" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm'})"/>
		应用名称:<select name="appId">
			<%for(AppInfoPo app:listApp){%> 
				<option value="<%=app.getAppId() %>" <%if(app.getAppId()==Integer.parseInt(appId)){out.print("selected");} %>><%=app.getAppName() %></option>
			<%} %>
			</select>
			异常匹配符:<input type="text" name="exception"  id="exception" value="<%=exception == null ? "" : exception %>" />
			机器IP:<input type="text" name="ip"  id="ip" value="<%=ip == null ? "" : ip %>" />
		<input type="submit" value="查看" />
	</td>
  </tr>  
</table>
</form>
</td></tr>
<%
Map<String,Integer> exceptionMap = new HashMap<String,Integer>();
for(AlarmRecordPo po:filterList){
	Integer num = exceptionMap.get(po.getAlarmKeyName());
	if(num == null){
		try{
			exceptionMap.put(po.getAlarmKeyName(),Integer.parseInt(po.getAlarmValue()));
		}catch(Exception e){
			
		}
	}else{
		exceptionMap.put(po.getAlarmKeyName(),Integer.parseInt(po.getAlarmValue())+num);
	}
}

%>
	<tr><td><table class="datalist"  width="1000">
    <tr class="ui-widget-header ">
		<td align="center">异常名称</td>
		<td align="center">次数</td>
	</tr>
	<%for(Map.Entry<String,Integer> po:exceptionMap.entrySet()){
	%>
	<tr>
		<td align="center"><%=po.getKey() %></td>
		<td align="center"><%=po.getValue() %></td>
	</tr>
	<%} %>
</table></td></tr>
<tr height="10"></tr>
	<tr><td><table class="datalist"  width="1000">
    <tr class="ui-widget-header ">
		<td align="center">异常名称</td>
		<td align="center">机器</td>
		<td align="center">时间</td>
		<td align="center">操作</td>
	</tr>
	<%for(AlarmRecordPo po:filterList){
	%>
	<tr>
		<td align="center"><%=po.getAlarmKeyName() %></td>
		<td align="center"><%=po.getSiteName() %></td>
		<td align="center"><%=sdf1.format(po.getCollectTime()) %></td>
		<td align="center"> <a href="exception_info1.jsp?appId=<%=appId%>&keyId=<%=po.getAlarmkeyId() %>&collectTime=<%=sdf3.format(po.getCollectTime()) %>">查看详细</a></td>
	</tr>
	<%} %>
</table></td></tr>
<tr>
	  <td><img  src="<%=request.getContextPath () %>/statics/images/line.png"  width="1000" height="1"></td>
	</tr>
	<tr><td><jsp:include page="../bottom.jsp"></jsp:include></td></tr>
</table>
</body>
</html>