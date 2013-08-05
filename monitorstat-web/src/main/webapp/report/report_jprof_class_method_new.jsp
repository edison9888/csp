<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<%@ page import="com.taobao.monitor.web.cache.AppCache" %>
<%@ page import="com.taobao.monitor.common.po.AppInfoPo" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>

<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.web.core.po.JprofHost"%>
<%@page import="com.taobao.monitor.web.core.po.JprofClassMethod"%>
<%@page import="com.taobao.monitor.common.util.Arith"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>监控</title>
<style>
.report_on{background:#bce774;}
</style>
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
	background: url(http://cm.taobao.net:9999/monitorstat/statics/images/4_17.gif);
}
img {
cursor:pointer;
border:0;
}
.body_summary{margin:5px 0;padding:2px;border-top:2px solid #ccc;border-bottom:1px solid #ccc;}
</style>  
  
  
  

</head>
<jsp:include page="report_head.jsp"></jsp:include>
<%
String orderType = request.getParameter("orderType");

if(orderType==null){
	orderType = "1";
}
String appIds = request.getParameter("appIds");

String collectDay = request.getParameter("collectDay");
String classNamelike = request.getParameter("classNamelike");
if(classNamelike==null){
	classNamelike="com/";
}
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

if(collectDay==null){
	java.util.Calendar cal = java.util.Calendar.getInstance();
	cal.add(java.util.Calendar.DAY_OF_MONTH, -1);
	collectDay = sdf.format(cal.getTime());
}
String[] _appNames = null;
if(appIds != null && !appIds.equals("") && !appIds.equals(",")){
	String[] appIdArray = appIds.split(",");
	_appNames = new String[appIdArray.length];
	for(int i=0;i<appIdArray.length;i++){
		AppInfoPo appInfo = AppCache.get().getKey(Integer.valueOf(appIdArray[i]));
		if(appInfo!=null)_appNames[i] = appInfo.getAppName();
	}
}else{
	_appNames = new String[]{};
}
%>
<body class="example">
  <%    
  for(String appName :_appNames){
  if(appName != null&&collectDay != null){  
  List<JprofClassMethod> listClass = MonitorJprofAo.get().findJprofClassMethod(appName,collectDay);
  
  if("1".equals(orderType)){	  
	  Collections.sort(listClass,new Comparator<JprofClassMethod>(){
		  public int compare(JprofClassMethod o1, JprofClassMethod o2){
			  long e1 = o1.getExcuteNum();
			  double t1 = o1.getUseTime();
			  
			  long e2 = o2.getExcuteNum();
			  double t2 = o2.getUseTime();
			  
			  if(t1*e1 >t2*e2){
				  return -1;
			  }else if(t1*e1 == t2*e2){
				  return 0;
			  }			  
			  return 1;
		  }
	  });	  
  }
  
  %>


<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" ><%=appName %>执行时间前30名  <a href="http://cm.taobao.net:9999/monitorstat/jprof/manage_jprof_class_method.jsp?appName=<%=appName %>&collectDay=<%=collectDay %>" target="_blank">查看详细</a></font>&nbsp;&nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table width="100%" border="1" class="ui-widget ui-widget-content">	
	<tr class="headcon ">
		<td >类信息</td>	
		<td >平均时间</td>		
		<td >总次数</td>
		<td >总时间</td>
		
	</tr>
	<%
	
	for(int i=0;i<listClass.size()&&i<30;i++){
		JprofClassMethod m = listClass.get(i);
		%>
		<tr >
			<td ><%=m.getClassName()+":"+m.getMethodName()+":"+m.getLineNum() %></td>
			<td ><%=Arith.div(m.getUseTime(),m.getExcuteNum(),2) %></td>
			<td ><%=m.getExcuteNum() %></td>
			<td ><%=m.getUseTime() %></td>			
		</tr>
		<%	
		} 
	%>
</table>
</div>
</div>  
  <%}} %>
<jsp:include page="report_buttom.jsp"></jsp:include>
</body>


</html>