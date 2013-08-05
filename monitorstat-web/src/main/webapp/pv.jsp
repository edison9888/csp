<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>

<%

SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

Calendar cal = Calendar.getInstance();
int start = Integer.parseInt(sdf.format(cal.getTime()));
cal.add(Calendar.DAY_OF_MONTH,-30);
int end = Integer.parseInt(sdf.format(cal.getTime()));
//这个是一天为单位显示走势变化
List<Pv> pvList = MonitorPvAo.get().findPv(end,start);

StringBuilder sb = new StringBuilder();
sb.append("<chart>");
sb.append("<series>");
for(int i=0;i<pvList.size();i++){
	Pv pv = pvList.get(i);
	sb.append("<value xid='"+i+"'>"+pv.getCollectDay()+"</value>");
}
sb.append("</series>");
	
sb.append("<graphs>");
sb.append("<graph gid='0' title='PV'>>");
for(int i=0;i<pvList.size();i++){
	Pv pv = pvList.get(i);
	sb.append("<value xid='"+i+"'>"+pv.getPv()+"</value>");
}
sb.append("</graph>");
sb.append("<graph gid='1' title='UV'>");
for(int i=0;i<pvList.size();i++){
	Pv pv = pvList.get(i);
	sb.append("<value xid='"+i+"'>"+pv.getUv()+"</value>");
}
sb.append("</graph>");
sb.append("</graphs>");
sb.append("</chart>");

String reslut = sb.toString(); 

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>性能图</title>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>




<script type="text/javascript">


</script>
</head>
<body>
<div id="chartdiv" align="center">


</div>
</body>


<script type="text/javascript">
var so = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline", "1000", "500", "8", "#FFFFFF");
so.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so.addVariable("chart_id", "amline");   
so.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings.xml");
so.addVariable("chart_data", encodeURIComponent("<%=reslut%>"));
so.write("chartdiv");			

</script>

</html>