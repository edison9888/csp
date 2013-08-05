<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.moinitor.matrix.dataobject.*" %>
<%@ page import="com.taobao.moinitor.matrix.enumtype.*" %>
<%@ page import="com.taobao.moinitor.matrix.util.*" %>
<%@ page import="com.taobao.moinitor.matrix.*" %>
<%@ page import="com.taobao.moinitor.matrix.parser.MatrixAppParser" %>
<%@ page import="com.taobao.monitor.applog.query.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Hsf详细信息图</title>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.datepicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/i18n/ui.datepicker-zh-CN.js"></script>

</head>
<body>
	<%
		String appId = request.getParameter("appId");
		String childAppId = request.getParameter("childAppId");
		Date queryDate = Utils.toDate(request.getParameter("date"), "yyyy-MM-dd", new Date());
		String level1 = request.getParameter("level1");
		String level2 = request.getParameter("level2");
		String level3 = request.getParameter("level3");
		
		HsfDetailQuery query = new HsfDetailQuery();
		query.setAppId(Integer.parseInt(appId));
		query.setChildAppId(Integer.parseInt(childAppId));
		query.setQueryDate(queryDate);
		query.setLevel1(Integer.parseInt(level1));
		query.setLevel2(Integer.parseInt(level2));
		query.setLevel3(Integer.parseInt(level3));
		
		String result = MatrixAO.instance.showDayLogDetail(query);
	%>
	
	<div id="chartdiv">
	</div>
</body>


<script type="text/javascript">
	var so = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline", "800", "400", "8", "#FFFFFF");
	so.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
	so.addVariable("chart_id", "amline");   
	so.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings.xml");
	so.addVariable("chart_data", encodeURIComponent("<%=result%>"));
	so.write("chartdiv");			

</script>
</html>