<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.po.KeyValuePo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>性能图</title>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>

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
</style>
<%

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String appId = request.getParameter("appId");
String keyId = request.getParameter("keyId");
String start = request.getParameter("start");
String end = request.getParameter("end");


if(start==null){
	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.DAY_OF_MONTH,-7);	
	start = sdf.format(cal.getTime());
}

if(end==null){	
	end = sdf.format(new Date());
}


if(appId!=null&&keyId!=null&&start!=null&&end!=null){

	Map<String,List<KeyValuePo>> keyvalueMap = MonitorAlarmAo.get().getKeyAlarmCountBySite(Integer.parseInt(appId),Integer.parseInt(keyId),sdf1.parse(start+" 00:00:00"),sdf1.parse(end+" 23:59:59"));
	String reslut = AmLineFlash.createCharDateXml(keyvalueMap);



%>

</head>
<body>
<jsp:include page="../head.jsp"></jsp:include>
<jsp:include page="../left.jsp"></jsp:include>
<form action="alarm_record_flash.jsp">
<input type="hidden" value="<%=appId %>" name="appId"/>
<input type="hidden" value="<%=keyId %>" name="keyId">
<div class="ui-widget-content ui-corner-all" style=" font-size: 1.2em; margin: .6em 0;">
<table width="100%">
<tr>
	<td width="100%" align="center">
		开始日期:<input type="text" name="start" value="<%=start %>"/> - 结束日期:<input type="text" name="end" value="<%=end %>"/><input type="submit" value="查看">
	</td>
</tr>
</table>

</div>
<div id="chartdiv" align="center">


</div>
</form>
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
<%}else{ %>
参数不正确！
<%} %>
</html>