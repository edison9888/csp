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
<%@page import="com.taobao.monitor.web.core.po.LoadRunHost"%>
<%@page import="com.taobao.csp.loadrun.core.LoadrunResult"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>应用监控系统-查询应用key</title>
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
$(function(){
	//IN_HSF-ProviderDetail  OUT_PageCache  OUT_HSF-Consumer  OUT_forest  OUT_TairClient EXCEPTION
	$("#keyBodyId tr td").mouseover(function(){
		$(this).parent().children("td").addClass("report_on");
	})
	$("#keyBodyId tr td").mouseout(function(){
		$(this).parent().children("td").removeClass("report_on");
	})
})

</script>
</head>
<%
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
Calendar cal = Calendar.getInstance();
String end = sdf.format(cal.getTime());
cal.add(Calendar.DAY_OF_MONTH,-30);
String  start= sdf.format(cal.getTime());

List<LoadRunHost> listLoadHost = MonitorLoadRunAo.get().findAllLoadRunHost();

for(LoadRunHost host:listLoadHost){

	if(host.getLoadrunType()!=1){
		continue;
	}

int appId= host.getAppId();
String hostip = host.getHostIp();
String collectTime = sdf.format(new Date());;

List<LoadrunResult> loadList = MonitorLoadRunAo.get().findAppLoadRunResult(appId,1,collectTime);

AppInfoPo appPo = AppCache.get().getKey(appId);




double maxQps = 0;
double currentqps = 0;
%>
<body>


<%

int[] machineNum = CspCacheTBHostInfos.get().getHostType(appPo.getOpsName());
double need = ((machineNum[0]+machineNum[1])*currentqps)/maxQps;
%>
<table width="100%" border="1" class="ui-widget ui-widget-content">
	<tr class="ui-widget-header ">
		<td>当前机器数</td><td>理论可支撑机器数</td><td>安全机器数</td>
	</tr>
	<tr>
		<td>实体机:<%=machineNum[0] %>;虚拟机:<%=machineNum[1] %></td><td><%=(int)need %></td><td><%=(int)(need*3.2) %></td>
	</tr>
</table>

<%} %>

说明:线上自动压测采用分流apache jk 实现,<br>
  qps，rest，pagesize 获取是来自 tomcat access <br>
GC信息来自于gc.log<br>
平均每次请求消耗 ：每秒PSYoungGen回收/qps <br>
 <br>


</body>
</html>