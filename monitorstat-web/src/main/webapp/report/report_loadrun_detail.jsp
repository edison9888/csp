<%@page import="com.taobao.monitor.common.util.CspCacheTBHostInfos"%>
<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.KeyPo"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.vo.AlarmDataPo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorLoadRunAo"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>Ӧ�ü��ϵͳ-��ѯӦ��key</title>

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
	font-family: "����";
	font-size: 12px;
	font-weight: bold;
	text-indent: 3px;
	border-color: #316cb2;
	/*text-transform: uppercase;*/
	background: url(http://cm.taobao.net:9999/monitorstat/statics/images/4_17.gif);
}
img {
cursor:pointer;
}
.report_on{background:#bce774;}

</style>
</head>

<body>
<jsp:include page="report_head.jsp"></jsp:include>
˵��:�����Զ�ѹ�����http_load������ʵ��,ѹ��ʱ��Ϊ60�룬����5���û����ϵ���30��Ϊֹ��<br>
��־����ֱ����Ŀ�������apache��־�л�ȡ10000����http statusΪ200��ѹ��˿�Ϊ7001ҳ���С��ѹ����<br>
GC��Ϣ������gc.log<br>
ƽ��ÿ���������� ��ÿ��PSYoungGen����/qps <br>
ѹ��ʱ��Ϊ:����7�� <br>

<%

String appIds = request.getParameter("appIds");
String[] appIdArry = null;
if(appIds != null && !appIds.equals("")
		&& !appIds.equals("null")){
	appIdArry = appIds.split(",");
}
if(appIdArry == null || appIdArry.length <= 0)return;
Map<String,String> appIdsMap = new HashMap<String,String>();
for(String appId:appIdArry){
	appIdsMap.put(appId, appId);
}


List<LoadRunHost> listLoadHost = MonitorLoadRunAo.get().findAllLoadRunHost();

for(LoadRunHost host:listLoadHost){
	try{
	if(host.getLoadrunType()!=0){
		continue;
	}
	if(appIdsMap.get(host.getAppId()+"") == null){
		continue;
	}
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
String appId= host.getAppId()+"";
String collectTime = sdf.format(new Date());

List<LoadRunResult> loadList = MonitorLoadRunAo.get().findAppLoadRunResult(Integer.parseInt(appId),0,collectTime);
AppInfoPo appPo = AppCache.get().getKey(Integer.parseInt(appId));
//List<AppInfoPo> appList = AppInfoAo.get().findAllAppInfo();

//Map<Integer,String> mapApp = new HashMap<Integer,String>();
//for(AppInfoPo po:appList){
//	mapApp.put(po.getAppId(),po.getAppName());
//}


Collections.sort(loadList,new Comparator<LoadRunResult>(){
	public  int compare(LoadRunResult o1, LoadRunResult o2){
		
		String s1 = o1.getShell();
		String s2 = o2.getShell();
		
		String[] tmp1 = s1.split(" ");
		String[] tmp2 = s2.split(" ");
		int parallel1 = Integer.parseInt(tmp1[2]);
		int parallel2 = Integer.parseInt(tmp2[2]);;
		
		if(parallel1>parallel2){
			return 1;
		}else if(parallel2>parallel1){
			return -1;
		}
		return 0;
	}
});

double maxQps = 0;
double currentqps = 0;
%>

<table width="100%" border="1">
	 <tr>
	 	<td colspan="4" align="center"><font size="5"><%=appPo.getAppName() %>[<%=host.getHostIp()%>]</font><a href="http://cm.taobao.net:9999/monitorstat/load/app_loadrun_detail.jsp?appId=<%=appId %>&hostip=<%=host.getHostIp()%>" target="_blank">�鿴��ϸ</a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="http://cm.taobao.net:9999/monitorstat/load/upload/apache_<%=host.getHostIp()%>_<%=collectTime%>" target="_blank"/>ѹ���ļ�����</a></td>
  	</tr>
  	 <%
  	 String cpu = "";
  	 String memory = "";
	  for(LoadRunResult load:loadList){
		  cpu = AnalyseLoadLog.analyseHostCpuInfo(load.getHostDesc());
		  memory = AnalyseLoadLog.analyseHostMemoryInfo(load.getHostDesc());
	  } 
	 %>	
	<tr>
		<td width="100" align="center">Cpu:</td><td width="400"><%=cpu%></td><td width="100" align="center">�ڴ�:</td><td><%=memory%></td>
	</tr>	
</table>
<table width="100%" border="1" class="ui-widget ui-widget-content"> 
  <tr class="ui-widget-header ">
    <td width="100" align="center"></td>
    <td width="200" align="center">5�û�</td>
   <td width="200" align="center">10�û�</td>
   <td width="200" align="center">15�û�</td>
   <td width="200" align="center">20�û�</td>
   <td width="200" align="center">25�û�</td>
   <td width="200" align="center">30�û�</td>
  </tr>
 
  <tr>
    <td align="center">QPS(p/s)</td>
     <%
	  for(LoadRunResult load:loadList){
		  currentqps = load.getMaxPv();
		 double qps =  AnalyseLoadLog.analyseAverageQps(load.getLoadRunDesc());
		  if(maxQps < qps){
			  maxQps = qps;
		  }
		  
	 %>
    <td><%=qps %></td>  
     <%} %>
  </tr>
 
  <tr>
    <td align="center">CPU(%)</td>
    <%
	  for(LoadRunResult load:loadList){
	%>
    <td><%=AnalyseLoadLog.analyseAverageCpu(load.getCpuDesc()) %></td>
    <%} %>   
  </tr>
    <tr>
    <td align="center">LOAD</td>
    <%
	  for(LoadRunResult load:loadList){
	%>
    <td><%=AnalyseLoadLog.analyseTopDesc(load.getTopDesc()) %></td>
    <%} %>   
  </tr>
  <tr>
    <td align="center">ƽ����Ӧ(ms)</td>
     <%
	  for(LoadRunResult load:loadList){
	 %>
    <td><%=AnalyseLoadLog.analyseAverageResT(load.getLoadRunDesc()) %></td>  
     <%} %>
  </tr>
  <tr>
    <td align="center">ƽ��ҳ��(kb)</td>
     <%
	  for(LoadRunResult load:loadList){
	 %>
    <td><%=AnalyseLoadLog.analyseAveragePageSize(load.getLoadRunDesc()) %></td>  
     <%} %>
  </tr>
   <tr>
    <td align="center">GC��Ϣ</td>
     <%
	  for(LoadRunResult load:loadList){
	 %>
    <td>
    <%   JvmMessage jvm = AnalyseLoadLog.analyseAverageGc(load.getGcDesc()); %>
    	ƽ��ÿ��:<%=jvm.getGcNum() %>��<br/>
    	ƽ��ÿ��:<%=jvm.getGcAverageTime() %>ms
    </td>  
     <%} %>
  </tr>
  <tr>
    <td align="center">FGC��Ϣ</td>
     <%
	  for(LoadRunResult load:loadList){
	 %>
    <td><%   JvmMessage jvm = AnalyseLoadLog.analyseAverageGc(load.getGcDesc()); %>
    	�ܼ�:<%=jvm.getFgcNum() %>��<br/>
    	ƽ��ÿ��:<%=jvm.getFgcAverageTime() %>ms</td>  
     <%} %>
  </tr>
  <tr>
    <td align="center">ƽ��ÿ�����������ڴ�</td>
     <%
	  for(LoadRunResult load:loadList){
	 %>
    <td><%
    double qps= AnalyseLoadLog.analyseAverageQps(load.getLoadRunDesc());
    JvmMessage jvm =  AnalyseLoadLog.analyseAverageGc(load.getGcDesc()) ;    
    %>
   	 ƽ��ÿ����������:<%=Arith.div(jvm.getGcUseMemory(),qps,2)%>k
    </td>  
     <%} %>
  </tr>
</table>
<%

int[] machineNum = CspCacheTBHostInfos.get().getHostType(appPo.getOpsName());
double need = ((machineNum[0]+machineNum[1])*currentqps)/maxQps;
%>
<table width="100%" border="1" class="ui-widget ui-widget-content">
	<tr class="ui-widget-header ">
		<td>��ǰ������</td><td>���ۿ�֧�Ż�����</td><td>��ȫ������</td>
	</tr>
	<tr>
		<td>ʵ���:<%=machineNum[0] %>;�����:<%=machineNum[1] %></td><td><%=(int)need %></td><td><%=(int)(need*3.2) %></td>
	</tr>
</table>
<%}catch(Exception e){}} %>

<jsp:include page="report_buttom.jsp"></jsp:include>
</body>
</html>