<%@page import="com.taobao.monitor.common.util.CspCacheTBHostInfos"%>
<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="com.taobao.monitor.common.util.TBProductCache"%>
<%@page import="com.taobao.monitor.common.po.ProductLine"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>

<%@page import="java.util.ArrayList"%>

<%@page import="java.util.Collections"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.weekreport.WeekReportDataProvider"%>
<%@page import="java.text.SimpleDateFormat"%>

<%@page import="com.taobao.monitor.common.po.ReportBasicDataPo"%>
<%@page import="com.taobao.monitor.common.po.ReportInvokeDataPo"%>
<%@page import="com.taobao.monitor.common.util.CommonUtil"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK" />
<title>本周系统报告</title>
<style type="text/css">
body {
	font-size: 62.5%;
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
	background: url(<%=request.getContextPath ()%>/statics/images/4_17.gif);
}
img {
cursor:pointer;
border:0;
}
.body_summary{margin:5px 0;padding:2px;border-top:2px solid #ccc;border-bottom:1px solid #ccc;}
</style>
</head>

<body>
<%
	Date dateTime = null;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String current = request.getParameter("dateTime");
	Calendar cal = Calendar.getInstance();
	cal.set(Calendar.HOUR_OF_DAY, 0);
	cal.set(Calendar.MINUTE, 0);
	cal.set(Calendar.SECOND, 0);
	cal.set(Calendar.MILLISECOND, 0);
	if(current!=null){
		dateTime = sdf.parse(current);
		cal.setTime(dateTime);
	}else{		
		dateTime = cal.getTime();
	}
	cal.add(Calendar.DAY_OF_MONTH,-1);
	
	dateTime = cal.getTime();
	
	
	
	List<Date> dayList = new ArrayList<Date>();
	
	int week = cal.get(Calendar.DAY_OF_WEEK);
	int offest = (week-1)==0?7:(week-1);		
	for(int i=0;i<offest;i++){
		dayList.add(cal.getTime());	
		cal.add(Calendar.DAY_OF_MONTH, -1);
	}	
	
	Collections.sort(dayList);
	Date fristDate = dayList.get(0);
%>
<%
request.setCharacterEncoding("GBK");
String[] groupNameArray = null;
String groupNames = request.getParameter("groupName");

if(groupNames == null || groupNames.equals("#") || groupNames.equals("")){
	groupNames = "交易核心";
}

groupNameArray = groupNames.split("#");

List<AppInfoPo> appList = AppInfoAo.get().findAllDayApp();
for(String groupName:groupNameArray){
	List<AppInfoPo> needReportApp = new ArrayList<AppInfoPo>();
	for(AppInfoPo po:appList){
		ProductLine p = TBProductCache.getProductLineByAppName(po.getOpsName());
	
		if(groupName.equals(p.getDevelopGroup())){
			needReportApp.add(po);
		}
	}
  
  	WeekReportDataProvider dataProvider = new WeekReportDataProvider(fristDate, dateTime, needReportApp);
  
%>
<table width="100%" border="1" width="100%" border="1" bordercolor="#b0c4de">
<tr><td align="center" style="height:30px;font-weight:900;color:blue;"><%=groupName%></td></tr>
<tr>
 <td>
<table width="100%" border="1" width="100%" border="1" class="ui-widget ui-widget-content">
  <tr class="headcon">
    <td colspan="15" align="center">当前应用水位情况[<%=sdf.format(dateTime)%>]</td>
  </tr> 
  <tr class="headcon">
    <td>应用</td>
    <td>机器数</td>
    <td>当前pv</td>
    <td>当前水位(QPS)</td>
    <td>归一化水位</td>
    <td>单次调用成本</td>
  </tr>
  <%
     for(AppInfoPo appPo:needReportApp){
    	String appName = appPo.getOpsName();
    	ReportBasicDataPo po = dataProvider.latestBasicDataPo(appName);
   %>
   <tr>
   	<td align="center"><a target="_blank" href="http://capacity.taobao.net:9999/capacity/show.do?method=showCapacityMore"><%=appPo.getAppName()%></a></td>
   	<td align="center">
    	<%=po.getMachineInfo()%>
    </td>
    <td align="center">
    	<%=Utlitites.fromatLong(po.getPv() + "")%>
    </td>
    <td align="center">
    	<%=po.getCapacityLevel()%>
    </td>
    <td align="center">
    	<%=CommonUtil.getCapacityStandard(appName)%>%
    </td>
    <td align="center">
    	<%=po.getSingleCost()%>
    </td>
  
  <%
  	}
  %>
  
 </table>
 <br></br>
<table width="100%" border="1" width="100%" border="1" class="ui-widget ui-widget-content">
  <tr class="headcon ">
    <td colspan="15" align="center">本周应用情况[<%=sdf.format(fristDate)+"-"+sdf.format(dateTime)%>]</td>
  </tr> 
  <tr class="headcon ">
    <td>应用</td>
    <td colspan="2" align="center">PV</td>
    <td colspan="2" align="center">UV</td>
    <td colspan="2" align="center">QPS</td>
    <td colspan="2" align="center">RT</td>
    <td colspan="2" align="center">LOAD</td>
    <td colspan="2" align="center">FullGC</td>
    <td colspan="2" align="center">小GC次数</td>
  </tr>
  <tr>
   	<td>&nbsp;</td>
    <td align="center">最大值</td>    
    <td align="center">趋势</td>
    <td align="center">最大值</td>   
    <td align="center">趋势</td>
    <td align="center">最大值</td>   
    <td align="center">趋势</td>
    <td align="center">最大值</td>    
    <td align="center">趋势</td>
    <td align="center">最大值</td>   
    <td align="center">趋势</td>
     <td align="center">次数</td>   
    <td align="center">时间</td>
    <td align="center">最大值</td>   
    <td align="center">趋势</td>
  </tr> 
  <%
     for(AppInfoPo appPo:needReportApp){ 		
   %>
  
  <tr>
    <td align="center"><a target="_blank" href="http://cm.taobao.net:9999/monitorstat/app_detail.jsp?selectAppId=<%=appPo.getAppDayId() %>"><%=appPo.getAppName()%></a></td>
    <%
    	int appDayId = appPo.getAppDayId();
    	String appName = appPo.getOpsName();
	%>   
    
    <td align="center">
    	<%=Utlitites.fromatLong(dataProvider.maxKeyValue(appName, "pv"))%>
    </td>
    <td align="center">    
    	<%=dataProvider.keyRisingTrendStr(appName, "pv")%>    
    </td>
    
    <td align="center">
    	<%=Utlitites.fromatLong(dataProvider.maxKeyValue(appName, "uv"))%>
    </td>
    <td align="center">    
    	<%=dataProvider.keyRisingTrendStr(appName, "uv")%>    
    </td>
    
    <td align="center">
    	<%=dataProvider.maxKeyValue(appName, "qps")%>
    </td>
    <td align="center">    
    	<%=dataProvider.keyRisingTrendStr(appName, "qps")%>    
    </td>
    
    <td align="center">
    	<%=dataProvider.maxKeyValue(appName, "rt")%>
    </td>
    <td align="center">    
    	<%=dataProvider.keyRisingTrendStr(appName, "rt")%>    
    </td>
    
    <td align="center">
    	<%=dataProvider.maxKeyValue(appName, "load")%>
    </td>
    <td align="center">    
    	<%=dataProvider.keyRisingTrendStr(appName, "load")%>    
    </td>
    
    <td align="center">
    	<%=dataProvider.maxKeyValue(appName, "fGcCount")%>
    </td>
    <td align="center">
    	<%=dataProvider.maxKeyValue(appName, "fGcTime")%>
    </td>
    
    <td align="center">
    	<%=dataProvider.maxKeyValue(appName, "yGcCount")%>
    </td>
    <td align="center">    
    	<%=dataProvider.keyRisingTrendStr(appName, "yGcCount")%>    
    </td>
  
  <%
  	}
  %>
</table>
 </td>
</tr>
</table>
<%}%>

<table border="1" width="100%">
	<tr>
		<td>本周系统概况与问题</td>
	</tr>
	<tr>
		<td>
		1、	aaa
		2、	bbb
		3、	ccc
		</td>
	</tr>
</table>
<table border="1" width="100%">
	<tr>
		<td>本周主要工作</td>
	</tr>
	<tr>
		<td>
		1、	aaa
		2、	bbb
		3、	ccc
		</td>
	</tr>
</table>
<table border="1" width="100%">
	<tr>
		<td>下周主要工作计划</td>
	</tr>
	<tr>
		<td>
		1、	aaa
		2、	bbb
		3、	ccc
		</td>
	</tr>
</table>
</body>
</html>
