<%@page import="com.taobao.monitor.common.util.CspCacheTBHostInfos"%>
<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.taobao.monitor.web.weekreport.InterfaceWaveManage"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.web.ao.MonitorDayAo"%>
<%@page import="com.taobao.monitor.common.po.KeyValuePo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorLoadRunAo"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Collections"%>
<%@page import="com.taobao.monitor.common.util.Arith"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
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
border:0;
}
.body_summary{margin:5px 0;padding:2px;border-top:2px solid #ccc;border-bottom:1px solid #ccc;}
</style>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/My97DatePicker/WdatePicker.js"></script>
<title>Insert title here</title>
</head>
<body>
<%
String groupName = request.getParameter("groupName");
Date dateTime = null;

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
String current = request.getParameter("dateTime");
Calendar cal = Calendar.getInstance();
cal.add(Calendar.DAY_OF_MONTH,-1);
if(current!=null){
	dateTime = sdf.parse(current);
	cal.setTime(dateTime);
}else{		
	dateTime =  sdf.parse(sdf.format(cal.getTime()));
}

List<AppInfoPo> appList = AppInfoAo.get().findAllDayApp();

if(groupName == null){
	groupName="交易相关";
}

Set<String> groupList = new HashSet<String>();
List<AppInfoPo> needReportApp = new ArrayList<AppInfoPo>();
for(AppInfoPo po:appList){
	if(po.getGroupName().equals(groupName)){
		needReportApp.add(po);
	}
	groupList.add(po.getGroupName());
}
%>
<form action="pe.jsp">	
	<select name="groupName">
	<%for(String g:groupList){ %>
		<option value="<%=g %>" <%if(g.equals(groupName)){out.print("selected");} %>><%=g %></option>
	<%} %>	
	</select>
	<input type="text" name="dateTime" value="<%=sdf.format(dateTime)%>" class="Wdate" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})"/> <input type="submit" value="查看当天情况"/>
</form>
<table width="100%" border="1" width="100%" border="1" class="ui-widget ui-widget-content">
  <tr class="ui-widget-header ">
    <td colspan="15" align="center">当前应用水位情况[<%=sdf.format(dateTime)%>]</td>
  </tr> 
  <tr class="ui-widget-header ">
    <td>应用</td>
    <td>应用描述</td>
    <td>机器数</td>
    <td>当前pv</td>
    <td>当前水位(QPS)</td>
    <td>最高水位</td>
    <td>归一化水位</td>
  </tr>
  <%
     for(AppInfoPo appPo:needReportApp){
    	 
    	 double maxQps = MonitorLoadRunAo.get().findRecentlyAppLoadRunQps(appPo.getAppId());
    	 
    	 int[] machineNum = CspCacheTBHostInfos.get().getHostType(appPo.getOpsName());
    	 
    	 
    	 int qpsKeyId = 985;
    	 int pvkeyId = 982;
    	 int restKeyId = 3067;
    	 
    	 
    	 
    	// 16931 | PV_QPS_AVERAGEUSERTIMES  | NULL          | NULL    |
    	 //|  16930 | PV_REST_AVERAGEUSERTIMES | NULL          | NULL    |
    	// |  16929 | PV_VISIT_COUNTTIMES      | NULL 
    	
    	//27733 | IN_HSF_AllInterfacePV_COUNTTIMES         | NULL          | NULL    |
        //27734 | IN_HSF_AllInterfaceQps_AVERAGEUSERTIMES  | NULL          | NULL    |
         //27735 | IN_HSF_AllInterfaceRest_AVERAGEUSERTIMES | NULL        
    	 
    	 
    	 KeyValuePo qpsKeyValue = MonitorDayAo.get().findKeyValueFromCountByDate(qpsKeyId,appPo.getAppDayId(),sdf.format(cal.getTime()));
    	 KeyValuePo pvKeyValue = MonitorDayAo.get().findKeyValueFromCountByDate(pvkeyId,appPo.getAppDayId(),sdf.format(cal.getTime()));
    	
    	 if(Double.parseDouble(qpsKeyValue.getValueStr())<1){
    		 qpsKeyId = 16931;
    		 pvkeyId = 16929;
    		 qpsKeyValue = MonitorDayAo.get().findKeyValueFromCountByDate(qpsKeyId,appPo.getAppDayId(),sdf.format(cal.getTime()));
    		 pvKeyValue = MonitorDayAo.get().findKeyValueFromCountByDate(pvkeyId,appPo.getAppDayId(),sdf.format(cal.getTime()));
    		 
    		 if(Double.parseDouble(qpsKeyValue.getValueStr())<1){
    			 qpsKeyId = 27734;
        		 pvkeyId = 27733;
        		 qpsKeyValue = MonitorDayAo.get().findKeyValueFromCountByDate(qpsKeyId,appPo.getAppDayId(),sdf.format(cal.getTime()));
        		 pvKeyValue = MonitorDayAo.get().findKeyValueFromCountByDate(pvkeyId,appPo.getAppDayId(),sdf.format(cal.getTime()));
    		 }
    		 
    	 }
    	double num = -1;
    	double pvNum = -1;
    	try{
    		num = Double.parseDouble(qpsKeyValue.getValueStr());
    	}catch(Exception e){}
    	try{
    		pvNum = Double.parseDouble(pvKeyValue.getValueStr());
    	}catch(Exception e){}
   %>
   <tr>
    <td align="center"><a target="_blank" href="http://cm.taobao.net:9999/monitorstat/app_detail.jsp?selectAppId=<%=appPo.getAppDayId() %>"><%=appPo.getAppName()%></a></td>
   <td> - </td>
    <td><%=machineNum[0] %>/<%=machineNum[1] %></td>
    <td><%=Utlitites.fromatLong((long)pvNum+"") %></td>
    <td><%=num %></td>
    <td> <%=maxQps>0?maxQps:" - " %> </td>
    <td> <%=maxQps>0?(long)((num/maxQps)*100)+"%":" - " %> </td>
  </tr>
  <%
  	}
  %> 
  
 </table>
</body>
</html>