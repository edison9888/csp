<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*"%>
<%@ page import="java.lang.Integer"%>

<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorTairAo"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.web.tair.SingleTairData"%>
<%@page import="com.taobao.monitor.web.tair.AllTairData"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>

<%@page import="java.text.DecimalFormat"%>
<%@page import="java.math.RoundingMode"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>Tair 流量监控</title>
<link href="../statics/css/tair/tair.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.tabs.js"></script>

</head>
<body>
<%
Calendar cal = Calendar.getInstance();
cal.add(Calendar.DAY_OF_MONTH, -1);
String opsName = request.getParameter("opsName");
if(opsName!=null){
	opsName = "itemcenter";
}
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
response.sendRedirect("http://110.75.2.75:9999/depend/show/tairconsume.do?method=showTairConsumeMain&opsName=itemcenter&showType=consume&selectDate="+sdf.format(cal.getTime())) ;
%>
</body>
</html>