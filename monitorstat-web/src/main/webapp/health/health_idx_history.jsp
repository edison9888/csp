<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>
<%@page import="com.taobao.monitor.web.ao.MonitorTimeAo"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.web.rating.RatingIndicator"%>
<%@page import="com.taobao.monitor.web.rating.RatingCompute"%>
<%@page import="com.taobao.monitor.web.rating.RatingIndicatorValue"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>应用历史评分</title>
<style>
.report_on{background:#bce774;}
</style>
<link type="text/css" href="<%=request.getContextPath()%>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/ui/ui.tabs.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery.bgiframe.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/statics/js/swfobject.js"></script>
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
	background: url(<%=request.getContextPath()%>/statics/images/4_17.gif);
}
img {
cursor:pointer;
border:0;
}
.body_summary{margin:5px 0;padding:2px;border-top:2px solid #ccc;border-bottom:1px solid #ccc;}
</style>

</head>
<body>
		<jsp:include page="../head.jsp"></jsp:include>	
		<jsp:include page="../left.jsp"></jsp:include>
<%
	String appid = request.getParameter("appId");
	List<RatingIndicatorValue> list = MonitorRatingAo.get()
			.getHealthIndexByAppId(Integer.parseInt(appid));
	Map<Integer, Double> map = new TreeMap<Integer, Double>(
			new Comparator() {
				public int compare(Object o1, Object o2) {
					return Integer.parseInt(o2.toString())
							- Integer.parseInt(o1.toString());
				}
			});

	for (RatingIndicatorValue riv : list) {
		int region  = RatingCompute.region(riv);
		if (map.containsKey(riv.getCollectDay())) {
			map.put(riv.getCollectDay(), map.get(riv.getCollectDay())
					+ RatingCompute.compute(riv));
		} else {
			map.put(riv.getCollectDay(), RatingCompute.compute(riv));
		}
	}
	Map<String,Map<String, Double>> map_result = new HashMap<String,Map<String, Double>>();
	Map<String,Double> map_tmp = new HashMap<String,Double>();
	for(Map.Entry<Integer,Double> entry:map.entrySet()){
		map_tmp.put(entry.getKey().toString(),entry.getValue());
	}
	map_result.put("分数",map_tmp);
	String result = AmLineFlash.createCommonCharXml1(map_result);
%>

<div id="chartdiv" align="center">
</div>

<form>
<div style="width: 100%;">
<div id="dialog">
<table width="100%" border="0" >
  
  <tr>
    <td width="50%" align="right" colspan="1"><input type="button" value="查看应用评分历史细节信息" onclick="location.href='./health_idx_history2.jsp?appId=<%=appid%>'" /></td>  
    <td width="50%" align="left" colspan="1"><input type="button" value="返回首页" onclick="location.href='./index.jsp'" /></td>
  </tr>
</table>

</div>
</div>
</form>


<script type="text/javascript">
var so = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline", "800", "400", "8", "#FFFFFF");
so.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so.addVariable("chart_id", "amline");   
so.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings.xml");
so.addVariable("chart_data", encodeURIComponent("<%=result%>"));
so.write("chartdiv");
</script>
	</body>
</html>