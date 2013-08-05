<%@ page language="java" contentType="text/html; charset=GB18030"
	pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.vo.UserAcceptInfo"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.web.ao.MonitorAlarmAo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.core.dao.impl.*"%>
<%@page import="com.taobao.monitor.web.distrib.DistribFlowPo"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.taobao.monitor.web.util.AmLineFlash"%>
<%@page import="com.taobao.monitor.web.vo.UserAcceptCount"%>
<%@page import="java.util.Comparator"%>

<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.alarm.po.AlarmSendPo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>核心监控</title>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=request.getContextPath() %>/statics/js/ui/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script language="JavaScript"
	src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/statics/js/ui/ui.tabs.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/statics/js/ui/jquery.ui.widget.js"></script>

</head>
<body>
<table width="1000" align="center" cellspacing="0" cellpadding="0" border="0"  style="margin:0 auto;">
	<tr>
	  <td> <jsp:include page="../top.jsp"></jsp:include></td>
  	</tr>
	<jsp:include page="../left.jsp"></jsp:include>
<%
request.setCharacterEncoding("gbk");

MonitorAlarmAo alarmAo = MonitorAlarmAo.get();
MonitorUserAo userAo = MonitorUserAo.get();
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
Calendar cal = Calendar.getInstance();
String currentDate = sdf.format(cal.getTime());		//今天
cal.add(Calendar.DAY_OF_MONTH, -7);			
String lastDate = sdf.format(cal.getTime());		//相对于今天的之前第七天


List<UserAcceptInfo> allList = alarmAo.findAllUserAcceptMsg(lastDate, currentDate);

List<AlarmSendPo> alarmSendList = alarmAo.findAllAlarmSend(lastDate, currentDate);

Map<String, UserAcceptCount> wwMap = new HashMap<String, UserAcceptCount>();		//key是用户的旺旺，value是总和
int wwAll = 0;
int phoneAll = 0;

List<LoginUserPo> userlist = userAo.findAllUser();

Map<String,String> phoneMap = new HashMap<String,String>();

for(LoginUserPo po:userlist){
	phoneMap.put(po.getPhone(),po.getWangwang());
}


Map<Integer,LoginUserPo> mapUser = new HashMap<Integer,LoginUserPo>();
for(LoginUserPo po:userlist){
	mapUser.put(po.getId(),po);
}

for(UserAcceptInfo u : allList) {	
	String userWwName = "";
	if(mapUser.get(u.getUserId())==null){
		userWwName = "已删除用户";
		continue;
	}else{
		userWwName = mapUser.get(u.getUserId()).getWangwang();
	}
	UserAcceptCount usercount = wwMap.get(userWwName);
	if(usercount == null){
		usercount = new UserAcceptCount();
		usercount.setWwName(userWwName);
		wwMap.put(userWwName,usercount);
	}
	
	if("wangwang".equals(u.getAlarmType())) {
		usercount.setWwAlarmNum(usercount.getWwAlarmNum()+1);		
		wwAll++;
	}
	
	if("phone".equals(u.getAlarmType())) {
		usercount.setPhoneAlarmNum(usercount.getPhoneAlarmNum()+1);	
		phoneAll++;
	}
	usercount.setAllAlarmNum(usercount.getAllAlarmNum()+1);
	//统计应用
	usercount.getAppSet().add(u.getAppId());
}

for(AlarmSendPo send:alarmSendList){
	
	if("wangwang".equals(send.getAlarmType())) {
		UserAcceptCount usercount = wwMap.get(send.getTargetAim());
		if(usercount!=null){
			usercount.setWwSendNum(usercount.getWwSendNum()+1);
		}
	}
	
	if("phone".equals(send.getAlarmType())) {
		String ww = phoneMap.get(send.getTargetAim());
		
		if(ww != null){
			UserAcceptCount usercount = wwMap.get(ww);
			if(usercount!=null){
				usercount.setPhoneSendNum(usercount.getPhoneSendNum()+1);
			}
		}
		
	}
	
	
	
}



List<UserAcceptCount> listCount = new ArrayList<UserAcceptCount>();
listCount.addAll(wwMap.values());
Collections.sort(listCount);

//饼形图的显示
StringBuilder sbww = new StringBuilder();
StringBuilder sbphone = new StringBuilder();
sbww.append("<pie>");
sbphone.append("<pie>");
for(UserAcceptCount entry : listCount) {
	
	sbphone.append("<slice title='"+entry.getWwName()+"'>"+entry.getPhoneAlarmNum()+"</slice>");			
	sbww.append("<slice title='"+entry.getWwName()+"'>"+entry.getWwAlarmNum()+"</slice>");			
}
sbww.append("</pie>");
sbphone.append("</pie>");



%>


<tr><td>
<div id="tabs">
<ul>
		<li><a href="#tabs-1">按照用户</a></li>
		<li><a href="#tabs-2">按照应用</a></li>
</ul>
<div id="tabs-1">
<table align="center" width="600" border="1">
	<tr>
		<td align="center" colspan="8"><span style="font-size: 16px; color: #003300"><strong><%=lastDate %> ~ <%=currentDate %>的汇总信息</strong></span></td>
		
	</tr>
	<tr>
		<td align="center">名称</td>
		<td align="center">应用总数</td>
		<td align="center" colspan="3" >wangwang:<%=wwAll %>条</td>
		<td align="center" colspan="3">phone:<%=phoneAll %>条</td>
	</tr>
		
	<%	
	String uu = "";
		for(UserAcceptCount u : listCount) {
			uu+=u.getWwName()+";";
			
	%>
		
	<tr>
		<td align="center"><%=u.getWwName() %></td>
		<td align="center"><%=u.getAppSet().size() %></td>
		<td align="center">共 <%=u.getWwAlarmNum() %> 条</td>
		<td align="center">共<%=u.getWwSendNum() %> 条被发送</td>
		<td align="center"><%=Arith.mul(Arith.div(u.getWwAlarmNum(),wwAll,2),100)%> %</td>
		<td align="center">共 <%=u.getPhoneAlarmNum() %> 条</td>
		<td align="center">共 <%=u.getPhoneSendNum() %> 条被发送</td>
		<td align="center"><%=Arith.mul(Arith.div(u.getPhoneAlarmNum(),phoneAll,2),100)%> %</td>
	</tr>

	
	<%
	}
	%>
</table>
</div>
<div id="tabs-2">
<table align="center" width="600" border="1">
	<tr>
		<td align="center" colspan="5"><span style="font-size: 16px; color: #003300"><strong><%=lastDate %> ~ <%=currentDate %>的汇总信息</strong></span></td>
		
	</tr>
	<tr>
		<td align="center">应用</td>
		<td align="center"  >wangwang告警数据</td>
		<td align="center"  >wangwang发送数据</td>
		<td align="center" >phone告警数据</td>
		<td align="center" >phone发送数据</td>
	</tr>
		
	<%
	
	Map<Integer,Integer[]> appSendMap = new HashMap<Integer,Integer[]>();
	for(UserAcceptInfo u: allList) {
		Integer[] msg = appSendMap.get(u.getAppId());
		if(msg == null){
			msg = new Integer[]{0,0,0,0};
			appSendMap.put(u.getAppId(),msg);
		}
		if("wangwang".equals(u.getAlarmType())) {
			msg[0]++;
		}
		
		if("phone".equals(u.getAlarmType())) {
			msg[1]++;
		}
	}
	
	for(AlarmSendPo send:alarmSendList){
		Integer[] msg = appSendMap.get(send.getAppId());
		if(msg == null){
			msg = new Integer[]{0,0,0,0};
			appSendMap.put(send.getAppId(),msg);
		}
		if("wangwang".equals(send.getAlarmType())) {
			msg[2]++;
		}
		
		if("phone".equals(send.getAlarmType())) {
			msg[3]++;
		}
	}
	
	%>
	<%
	for(Map.Entry<Integer,Integer[]> entry:appSendMap.entrySet()){ 
		
	%>	
	<tr>
		<td align="center"><%=AppCache.get().getKey(entry.getKey()).getAppName() %></td>
		<td align="center"><%=entry.getValue()[0] %></td>
		<td align="center"><%=entry.getValue()[2] %></td>
		<td align="center"><%=entry.getValue()[1] %></td>
		<td align="center"><%=entry.getValue()[3] %></td>
	</tr>
	<%} %>
	
	
</table>
</div>
</div>
</td></tr>
<tr><td>
<%=uu %>
</td></tr>
<tr height="10"></tr>

<tr><td>
<table width="1000" >
	<tr>
	
		<td width="50%"><div id="chartdivWW" align="center"></div></td>
		<td width="50%"><div id="chartdivPhone" align="center"></div></td>
	</tr>
</table></td></tr>
<tr>
	  <td><img  src="<%=request.getContextPath () %>/statics/images/line.png"  width="1000" height="1"></td>
	</tr>
	<tr><td><jsp:include page="../bottom.jsp"></jsp:include></td></tr>
<script type="text/javascript">
$(function() {
	$("#tabs").tabs();
});
var so1 = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/ampie.swf", "ampie", "500", "380", "8", "#FFFFFF");
so1.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so1.addVariable("chart_id", "amline1");   
so1.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/ampie_settings3.xml");
so1.addVariable("chart_data", encodeURIComponent("<%=sbww.toString()%>"));
so1.addVariable("additional_chart_settings", "<settings><text_size>15</text_size><labels><label><x>250</x><y>20</y><text>wangwang分布情况:</text></label></labels></settings>");

so1.write("chartdivWW");		

var so2 = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/ampie.swf", "ampie", "500", "380", "8", "#FFFFFF");
so2.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so2.addVariable("chart_id", "amline2");   
so2.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/ampie_settings3.xml");
so2.addVariable("chart_data", encodeURIComponent("<%=sbphone.toString()%>"));
so2.addVariable("additional_chart_settings", "<settings><text_size>15</text_size><labels><label><x>250</x><y>20</y><text>Phone分布情况:</text></label></labels></settings>");

so2.write("chartdivPhone");		

</script>

</table>
</body>
</html>