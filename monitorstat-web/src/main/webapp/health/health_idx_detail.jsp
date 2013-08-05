<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.web.rating.RatingIndicator"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>查看评分应用设置</title>
<style>
.report_on{background:#bce774;}
</style>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.tabs.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
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
border:0;
}
.body_summary{margin:5px 0;padding:2px;border-top:2px solid #ccc;border-bottom:1px solid #ccc;}
</style>
<script type="text/javascript">


</script>
</head>
<body>
		<jsp:include page="../head.jsp"></jsp:include>	
		<jsp:include page="../left.jsp"></jsp:include>
<%

String appid = request.getParameter("appId");
List<RatingIndicator> ratingIndicatorList = MonitorRatingAo.get().getRatingIndicatorByAppId(Integer.parseInt(appid));
Map<String,RatingIndicator> map = new HashMap<String,RatingIndicator>();
for (RatingIndicator ri : ratingIndicatorList){
	map.put(ri.getIndicatorKeyName(), ri);
}
%>
<form>
<input type="button" value="返回应用评分管理" onclick="location.href='./manage_rating_app.jsp'">
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" >查看评分应用设置</font>&nbsp;&nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table width="100%" border="1" class="ui-widget ui-widget-content">
 <tr >
    <td  colspan="4">应用:
    </td>	
  </tr>
  

  <tr class="headcon ">
    <td width="13%" align="center">指标名称</td>
	 <td width="13%" align="center">权重</td>
	 <td width="54%" align="center">健康区间</td>
	 <td width="20%" align="center">采样时间</td>
  </tr>
  
  
<% 
for(Map.Entry<String,RatingIndicator> entry : map.entrySet()){ 
%>
  <tr>
    <td align="center"><%=entry.getKey() %></td>
	<td align="center"><%=entry.getValue().getIndicatorWeight() %> % </td>
	<td align="center"><%=entry.getValue().getIndicatorThresholdValue().replaceAll("<","&lt;") %></td>
	<td align="center"><%=entry.getValue().getRushHour_start() %> ~ <%=entry.getValue().getRushHour_end() %></td>
  </tr>
<% 
} 
%>

  <tr>
    <td align="center" colspan="4"><input type="button" value="返回应用评分管理" onclick="location.href='./manage_rating_app.jsp'" /></td>
  </tr>
</table>
</div>
</div>
</form>

</body>

</html>