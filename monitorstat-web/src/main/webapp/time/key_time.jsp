<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>

<%
//�����һ��Ϊ��λ��ʾ���Ʊ仯
String action = request.getParameter("action");

String collectTime1 = request.getParameter("collectTime1");
String start1 = collectTime1+" 00:00:00";
String end1 = collectTime1+" 23:59:59";


String collectTime2 = request.getParameter("collectTime2");
String start2 = collectTime2+" 00:00:00";
String end2 = collectTime2+" 23:59:59";

SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String reslut = "";
String type = request.getParameter("type");

if("trade".equals(action)){
	
	Map<String, List<KeyValuePo>> map1 = MonitorTradeAo.get().findTradeByCollectTime(Integer.valueOf(type),parseLogFormatDate.parse(start1),parseLogFormatDate.parse(end1));
	
	if(collectTime2!=null&&"".equals(collectTime2)){
		Map<String, List<KeyValuePo>> map2 = MonitorTradeAo.get().findTradeByCollectTime(Integer.valueOf(type),parseLogFormatDate.parse(start2),parseLogFormatDate.parse(end2));	
		map1.putAll(map2);
	}	
	reslut = AmLineFlash.createCharXml(map1);	
}

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.common.po.KeyValuePo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>����ͼ</title>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>



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
	font-family: "����";
	font-size: 12px;
	font-weight: bold;
	text-indent: 3px;
	border-color: #316cb2;
	/*text-transform: uppercase;*/
	background: url(<%=request.getContextPath () %>/statics/images/4_17.gif);
}
</style>
<script type="text/javascript">

function goToDetailMonitor(){
	var collectTime2 = $('#datepicker').val();	
	location.href="./key_time.jsp?action=<%=action%>&type=<%=type%>&collectTime1=<%=collectTime1%>&collectTime2="+collectTime2;	
} 



</script>
</head>
<body>
<div class="ui-widget-content ui-corner-all" style=" font-size: 1.2em; margin: .6em 0;">
<table width="100%">
<tr>
	<td width="100%" align="center">
		����: <input type="text" id="datepicker" value="<%=collectTime2==null?collectTime1:collectTime2 %>"/><button  onclick="goToDetailMonitor()">�Ա�</button>
	</td>
</tr>
</table>
</div>


<div id="chartdiv" align="center">


</div>
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