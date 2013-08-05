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
	Map<Integer, String> map2 = new HashMap<Integer, String>();
	for (RatingIndicatorValue riv : list) {
		int region  = RatingCompute.region(riv);
		if (map.containsKey(riv.getCollectDay())) {
			map.put(riv.getCollectDay(), map.get(riv.getCollectDay())
					+ RatingCompute.compute(riv));
			map2.put(riv.getCollectDay(), map2.get(riv.getCollectDay()) + riv.getIndicatorKey()+"#"+riv.getIndicatorValue()+"#"+ RatingCompute.compute(riv)+"#"+riv.getIndicatorThresholdValue()+"#"+region+";");		
		} else {
			map.put(riv.getCollectDay(), RatingCompute.compute(riv));
			map2.put(riv.getCollectDay(), riv.getIndicatorKey()+"#"+riv.getIndicatorValue()+"#"+ RatingCompute.compute(riv)+"#"+riv.getIndicatorThresholdValue()+"#"+region+";");
		}
	}

%>


<form>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table width="100%" border="1" class="ui-widget ui-widget-content">
 <tr >
    <td  colspan="3">应用名:
	</td>	
  </tr>
  <tr class="headcon ">
	 <td width="20%" align="center">收集日</td>
	 <td width="80%" align="center">分数</td>
  </tr>
  
  
<%
    	for (Map.Entry<Integer, Double> entry : map.entrySet()) {
    %>
  <tr>
	 <td width="20%" align="center"><%=entry.getKey()%></td>
	 <td width="80%" align="center"><a href="#" onclick="showDetail('<%=entry.getKey() %>')"><%=entry.getValue() %></a><input type="hidden" value="<%=map2.get(entry.getKey()) %>" id="<%=entry.getKey() %>_detail"/></td>
  </tr>
<%
	}
%>
  
    <tr>
    <td align="center" colspan="3"><input type="button" value="返回首页" onclick="location.href='./index.jsp'" /></td>
  </tr>
</table>

</div>
</div>
</form>


<div id="otherMessagePanel" title="">
	<table width="100%" height="100%" border="1" >
		<tr>
			<td align='center'>指标名称</td>			
			<td align="center">指标值</td>
			<td align="center">分数</td>
			<td align="center">区间</td>
		</tr>
		<tbody id="otherMessageTable">		
		</tbody>
	</table>
</div>
<script type="text/javascript">

$("#otherMessagePanel").dialog({autoOpen: false, resizable: true ,zIndex:13001,width:500});

function showDetail(collect_day){
	var detail = $("#"+collect_day+"_detail").val();
	var keys = detail.split(";");
	$("#otherMessageTable").empty();
	for(var i=0;i<keys.length;i++ ){
		var key = keys[i];
		if(key != ""){			
			var k = key.split("#");
			if(k[4] == 1){
				 var tr = "<tr style='background-color:#00FF00'><td align='center'>"+k[0]+"</td><td align='center'>"+k[1]+"</td><td align='center'>"+k[2]+"</td><td >"+k[3]+"</td></tr>";
				    $("#otherMessageTable").append(tr);
			}else if(k[4] == 2){
				 var tr = "<tr style='background-color:#FFFF00'><td align='center'>"+k[0]+"</td><td align='center'>"+k[1]+"</td><td align='center'>"+k[2]+"</td><td >"+k[3]+"</td></tr>";
				    $("#otherMessageTable").append(tr);
			}else if(k[4] == 3){
				 var tr = "<tr style='background-color:#FF0000'><td align='center'>"+k[0]+"</td><td align='center'>"+k[1]+"</td><td align='center'>"+k[2]+"</td><td >"+k[3]+"</td></tr>";
				    $("#otherMessageTable").append(tr);
			}else{
				var tr = "<tr ><td >"+k[0]+"</td><td >"+k[1]+"</td><td >"+k[2]+"</td><td >"+k[3]+"</td></tr>";
			    $("#otherMessageTable").append(tr);
			}
		}
	}
	 $("#otherMessagePanel" ).dialog("option","title","评分详细");
     $("#otherMessagePanel" ).dialog( 'open' );
}

</script>
</body>
</html>