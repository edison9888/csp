<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.po.KeyValuePo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>性能图</title>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>

</head>
<body>
<%



String appId = request.getParameter("appId");
String keyId = request.getParameter("keyId");
String collectTime = request.getParameter("collectTime");

if(appId!=null&&keyId!=null&&collectTime!=null){

	String reslut = "";
	if("c_pv".equals(keyId)) {
		
		List<KeyValuePo> keyvalueList = MonitorDayAo.get().findCappPvDetailByTime(Integer.parseInt(appId),collectTime);

		reslut = AmLineFlash.createCharXml(keyvalueList);
	} else if("c_rt".equals(keyId)){
		
		List<KeyValuePo> keyvalueList = MonitorDayAo.get().findCappRtDetailByTime(Integer.parseInt(appId),collectTime);

		reslut = AmLineFlash.createCharXml(keyvalueList);

	} else {
		List<KeyValuePo> keyvalueList = MonitorDayAo.get().findMonitorDataListByTime(Integer.parseInt(appId),Integer.parseInt(keyId),collectTime);
		
		reslut = AmLineFlash.createCharXml(keyvalueList);
	}

%>

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
<%}else{ %>
参数不正确！
<%} %>
</html>