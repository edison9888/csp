<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<%@ page import="java.util.regex.*" %>
<%@ page import="java.text.ParseException" %>
<%@ page import="com.taobao.monitor.web.cache.KeyCache" %>
<%@ page import="com.taobao.monitor.common.util.Arith" %>

<%

String appId = request.getParameter("appId");
List<KeyPo> keyList = KeyAo.get().findAllAppKey(Integer.parseInt(appId));
String collectTime1 = request.getParameter("collectTime1");
String collectTime2 = request.getParameter("collectTime2");
String keyId1   = request.getParameter("keyId1");
String keyId2   = request.getParameter("keyId2");

//这个是一天为单位显示走势变化

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

if(collectTime1 == null){
	 collectTime1 = sdf.format(new Date());
}
if(collectTime2 == null){
	 collectTime2 = sdf.format(new Date());
}

String start1 = collectTime1+" 00:00:00";
String end1 = collectTime1+" 23:59:59";

String start2 = collectTime2+" 00:00:00";
String end2 = collectTime2+" 23:59:59";

SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


List<KeyValuePo> keyValueList1 = MonitorTimeAo.get().findKeyValueByRangeDate(Integer.parseInt(appId),Integer.parseInt(keyId1), parseLogFormatDate.parse(start1), parseLogFormatDate.parse(end1)) ;
List<KeyValuePo> keyValueList2 = MonitorTimeAo.get().findKeyValueByRangeDate(Integer.parseInt(appId),Integer.parseInt(keyId2), parseLogFormatDate.parse(start2), parseLogFormatDate.parse(end2)) ;
Map<String,List<KeyValuePo>> map2 = new HashMap<String,List<KeyValuePo>>();



%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.ao.center.KeyAo"%>
<%@page import="com.taobao.monitor.common.po.KeyPo"%>
<%@page import="com.taobao.monitor.common.po.KeyValuePo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>性能图</title>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>


<style type="text/css">

</style>
<script type="text/javascript">

function goToComparedDetailMonitor(){
	var collectTime2 = $('#datepicker').val();	
	location.href="./key_detail_time_comparison.jsp?keyId1=<%=keyId1%>&keyId2=<%=keyId2%>&appId=<%=appId%>&collectTime2=<%=collectTime2%>&collectTime1=<%=collectTime1%>";
}
</script>
</head>
<body>
<jsp:include page="../head.jsp"></jsp:include>
<jsp:include page="../left.jsp"></jsp:include>
<div class="ui-widget-content ui-corner-all" style=" font-size: 1.2em; margin: .6em 0;">
<form action="./key_detail_time_comparison.jsp" method="get">
	<table width="100%">
		<tr>
		   <td align="center">
		   	key1:<select name="keyId1">
		   			<%
		   			for(KeyPo po:keyList){
		   				if(po.getKeyId()<23416){
		   					continue;
		   				}
		   			%>
		   			<option value="<%=po.getKeyId() %>" <%if(po.getKeyId()==Integer.parseInt(keyId1)){out.print("selected");} %>><%=po.getKeyName() %></option>
		   			<%} %>
		   		</select>		   
		  	日期1: <input type="text" name="collectTime1" value="<%=collectTime1==null?"":collectTime1 %>"/>			
		   </td>
		 </tr>	
		 <tr>
		   <td align="center">		   
	  		key2:<select name="keyId2">
	   			<%
	   			for(KeyPo po:keyList){
	   				if(po.getKeyId()<23416){
	   					continue;
	   				}
	   			%>
	   			<option value="<%=po.getKeyId() %>" <%if(po.getKeyId()==Integer.parseInt(keyId2)){out.print("selected");} %>><%=po.getKeyName() %></option>
	   			<%} %>
	   		</select>		
			日期2: <input type="text" name="collectTime2" value="<%=collectTime2==null?"":collectTime2 %>"/>
			
			<input type="hidden" name="appId" value="<%=appId %>"/>
			<input type="submit" value="对比"  />			
		   </td>
		 </tr>	 
	</table>
</form>
</div>
<%

	Map<String,List<KeyValuePo>> map = new HashMap<String,List<KeyValuePo>>();
	if(keyValueList1!=null&&keyValueList1.size()>0){
		map.put(KeyCache.get().getKey(Integer.parseInt(keyId1)).getAliasName()+"["+collectTime1+"]",keyValueList1);
	}
	if(keyValueList2!=null&&keyValueList2.size()>0){
		map.put(KeyCache.get().getKey(Integer.parseInt(keyId2)).getAliasName()+"["+collectTime2+"]",keyValueList2);
	}
	
	
	String reslut = AmLineFlash.createCharXml1(map,true) ; 
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"> </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<div id="chartdiv" align="center">


</div>
</div>
</div>

<jsp:include page="../buttom.jsp"></jsp:include>
</body>


<script type="text/javascript">
var so = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline", "800", "400", "8", "#FFFFFF");
so.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so.addVariable("chart_id", "amline");   
so.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings.xml");
so.addVariable("chart_data", encodeURIComponent("<%=reslut%>"));
so.write("chartdiv");

</script>

</html>