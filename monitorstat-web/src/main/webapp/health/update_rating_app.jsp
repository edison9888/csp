<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.web.core.dao.impl.MonitorRatingDao"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.web.rating.RatingIndicator"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>更新评分应用设置</title>
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

	function checkSubmit(){
		//var obj = document.getElementById("appSelectId");
		//var v = obj.options[obj.selectedIndex].text;

		var arr = $(".quanzhong");
		for(var j = 0; j < arr.length; j++) {
	
			if(arr.get(j).value == "") {
	
				arr.get(j).focus();
				alert('权重不允许为空，请重新输入');  
				return false;
			}
		}
		
		if(window.confirm("你确认修改吗?")){
			return true;
		}
		
		return false;
	}

</script>
</head>
<body>
		<jsp:include page="../head.jsp"></jsp:include>	
		<jsp:include page="../left.jsp"></jsp:include>
<%

String id = request.getParameter("id");
List<RatingIndicator> ratingIndicatorList = MonitorRatingAo.get().getRatingIndicatorByAppId(Integer.parseInt(id));

String action = request.getParameter("action");

if("update".equals(action)){
	
	for(RatingIndicator i:ratingIndicatorList){
		int rushHourStart = 0;
		int rushHourEnd = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
		try {
			
			String rushHourStart1 = request.getParameter(i.getIndicatorKeyName()+"_rushHour_start");
			String rushHourEnd1 = request.getParameter(i.getIndicatorKeyName()+"_rushHour_end");
			if(rushHourStart1.length() != 4 || rushHourEnd1.length() != 4) {
				
				rushHourStart = 2030;
				rushHourEnd = 2230;
			} else {
				rushHourStart = Integer.parseInt(sdf.format(sdf.parse(rushHourStart1)));	//获得rushHour_start
				rushHourEnd = Integer.parseInt(sdf.format(sdf.parse(rushHourEnd1)));		//获得rushHour_end
			}
		}catch (Exception e) {
			
			//如果填写时间出错就填默认值
			rushHourStart = 2030;
			rushHourEnd = 2230;
		}
		
		
		String qps_quanzhong = request.getParameter(i.getIndicatorKeyName()+"_quanzhong");
		String qps_qujian = request.getParameter(i.getIndicatorKeyName()+"_qujian");
		i.setIndicatorWeight(Integer.parseInt(qps_quanzhong));
		i.setIndicatorThresholdValue(qps_qujian);
		
		i.setRushHour_start(rushHourStart);		//设置采样开始时间
		i.setRushHour_end(rushHourEnd);			//设置采样结束时间
		MonitorRatingAo.get().updateRatingApp(i);
	}
	out.print("修改成功^-^");
	out.print("<input type=\"button\" value=\"返回应用评分管理\" onclick=\"location.href='./manage_rating_app.jsp'\">");
}else{
%>
<form action="./update_rating_app.jsp" method="post" onsubmit="return checkSubmit()">
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" >新评分应用设置</font>&nbsp;&nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table width="100%" border="1" class="ui-widget ui-widget-content"> 
  <tr class="headcon ">
     <td width="6%" align="center">指标名称</td>
     <td width="6%" align="center">单位</td>
	 <td width="13%" align="center">权重</td>
	 <td width="37%" align="center">健康区间</td>
	 <td width="37%" align="center">采样时间段</td>
  </tr>
  
  <%
  for(RatingIndicator i:ratingIndicatorList){
  %>
  <tr>
    <td align="center"><%=i.getIndicatorKeyName() %></td>
     <td align="center"><%=i.getKeyUnit() %></td>
	<td align="center"><input type="text" name="<%=i.getIndicatorKeyName() %>_quanzhong" class = "quanzhong" value="<%=i.getIndicatorWeight() %>" size="10"/>
	  % </td>
	<td align="center"><input type="text" name="<%=i.getIndicatorKeyName() %>_qujian" value="<%=i.getIndicatorThresholdValue() %>" size="80"/> </td>
  	<td align="center">开始: <input type="text" name="<%=i.getIndicatorKeyName() %>_rushHour_start" value="<%=i.getRushHour_start() %>" size="10">结束: <input type="text" name="<%=i.getIndicatorKeyName() %>_rushHour_end" value="<%=i.getRushHour_end() %>" size="10"></td>
  </tr>
  
  <%} %>
    <tr>
    <td align="center" colspan="4">
    <input type="hidden" name="id" value="<%=request.getParameter("id") %>" size="40"/>
    <input type="hidden" name="action" value="update"/>
    <input type="submit" value="更新评分应用" />
    <input type="button" value="返回应用评分管理" onclick="location.href='./manage_rating_app.jsp'">
    </td>
  </tr>
</table>
</div>
</div>
</form>
<%} %>
</body>

</html>