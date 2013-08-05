<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>
<%@page import="com.taobao.monitor.web.rating.RatingApp"%>
<%@page import="com.taobao.monitor.web.rating.RatingIndicatorValue"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>

<%@page import="com.taobao.monitor.web.rating.RatingCompute"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>评分应用列表</title>
<style>
.report_on{background:#bce774;}
</style>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/ui/ui.tabs.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery.bgiframe.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/statics/js/swfobject.js"></script>


<style type="text/css">

<!--
.thumbnail{position:relative;z-index:0;}
.thumbnail:hover{background-color:transparent;z-index:50;}
.thumbnail span{position:absolute;background-color:#FFFFE0;left:-1000px;border:1px dashed gray;visibility:hidden;color:#000;text-decoration:none;padding:5px;}
.thumbnail span img{border-width:0;padding:2px;}
.thumbnail:hover span{visibility:visible;top:-30px;left:60px;}
p{margin-top:200px}
-->
</style>
<script type="text/javascript">
	$(function(){
		$("#ratingTableId tr td,othertableId tr td").mouseover(function(){
			$(this).parent().children("td").addClass("report_on");
		})
		$("#ratingTableId tr td,othertableId tr td").mouseout(function(){
			$(this).parent().children("td").removeClass("report_on");
		})
	})
	function openTime(keyId){
		parent.openTime(keyId);
	}
	
</script>
</head>
<body>

		<jsp:include page="../left.jsp"></jsp:include>
<%
	String action = request.getParameter("action");

	if ("delete".equals(action)) {
		MonitorRatingAo.get().deleteRatingApp(
				Integer.parseInt(request.getParameter("id")));
	}

	List<AppInfoPo> appList = AppInfoAo.get().findAllEffectiveAppInfo();
	Map<Integer, AppInfoPo> map = new HashMap<Integer, AppInfoPo>();
	for (AppInfoPo po : appList) {
		map.put(po.getAppId(), po);
	}

	List<RatingApp> listRatingAppList = MonitorRatingAo.get()
			.findAllRatingApp();
	Collections.sort(listRatingAppList, new Comparator() {
		public int compare(Object o1, Object o2) {
			return ((RatingApp) o1).getAppName().compareToIgnoreCase(
					((RatingApp) o2).getAppName());
		}
	});
%>
<center>
<table width="1000" align="center" cellspacing="0" cellpadding="0" border="0"  style="margin:0 auto;">
	<tr>
		<jsp:include page="../top.jsp"></jsp:include>	
	</tr>
	<tr>
       		<td  align="center"><h1>淘宝网性能健康指数</h1></td>
	</tr>
	<tr>
		<td height="20" colspan="3">
			&nbsp;
		</td>
	</tr>
	
 	<tr><td><table class="datalist" width="1000">
		<tr class="ui-widget-header ">
			    <td width="20%" align="center"><b>应用名</b></td>
				 <td width="20%" align="center"><b></b><a class="thumbnail" ><u>得分</u><span><img src="<%=request.getContextPath()%>/statics/images/formula1.png" /></span></a></td>
				 <td  align="center"><b>健康状况</b></td>
				 <td  align="center"><b>优化等级</b></td>
				 <td  align="center"><b>得分历史</b></td>
		</tr>
			  <%
			  	for (RatingApp app : listRatingAppList) {
			  		AppInfoPo po = map.get(app.getAppId());
			  		if(po== null){
			  			continue;
			  		}
			  		double d = 0;
			  		List<RatingIndicatorValue> list = MonitorRatingAo.get()
			  				.getRecentHealthIndexByAppId(app.getAppId());
			  		String s = "";
			  		for (RatingIndicatorValue riv : list) {
			  			d += RatingCompute.compute(riv);
			  			int region = RatingCompute.region(riv);
			  			String key = riv.getIndicatorKey()+"("+riv.getKeyUnit()+")";
			  			s += key
			  					+ "#"
			  					+ riv.getIndicatorValue()
			  					+ "#"
			  					+ RatingCompute.compute(riv)
			  					+ "#"
			  					+ riv.getIndicatorThresholdValue()
			  					+ "#"
			  					+ region
			  					+ "#"
			  					+ riv.getIndicatorWeight()
			  					+ "#"
			  					+ riv.getCollectDay()
			  					+ "#"
			  					+ RatingCompute.getThresholdValue(riv
			  							.getIndicatorThresholdValue()) + ""			  							
			  					+ "#"
					  			+ riv.getKeyId()
			  					+ "#"
			  					+ po.getAppId() + ";"; 
			  		}
			  %>    
			 
			   <tr >
			    <td  align="center"><%=po.getAppName()%></td>
				 <td  align="center"><a href="javascript:showDetail('<%=po.getAppName()%>')"><u><%=d%></u></a><input type="hidden" value="<%=s%>" id="<%=po.getAppName()%>_detail"/></td>
				 <td  align="center">
				 <%
				 	if (d >= 85) {
				 			out.print("<a href=\"javascript:showStatusDetail('"
				 					+ po.getAppName() + "','" + po.getAppId()
				 					+ "')\" style='color:#00FF00'><u>健康</u></a>");
				 		} else if (d > 60) {
				 			out.print("<a href=\"javascript:showStatusDetail('"
				 					+ po.getAppName() + "','" + po.getAppId()
				 					+ "')\" style='color:#FF00FF'><u>亚健康</u></a>");
				 		} else if (d > 0) {
				 			out.print("<a href=\"javascript:showStatusDetail('"
				 					+ po.getAppName() + "','" + po.getAppId()
				 					+ "')\" style='color:#FF0000'><u>危险</u></a>");
				 		} else {
				 			out.print(" 无  ");
				 		}
				 %>
				 </td>
				 <td  align="center">
				 <%
				 	if (d >= 85) {
				 			out.print("<a href='manage_optimize_record.jsp?appid="
				 					+ po.getAppId()
				 					+ "' style='color:#00FF00'><u>优化记录</u></a>");
				 		} else if (d > 60) {
				 			out.print("<a href='manage_optimize_record.jsp?appid="
				 					+ po.getAppId()
				 					+ "' style='color:#FF00FF'><u>需优化</u></a>");
				 		} else if (d > 0) {
				 			out.print("<a href='manage_optimize_record.jsp?appid="
				 					+ po.getAppId()
				 					+ "' style='color:#FF0000'><u>急需优化</u></a>");
				 		} else {
				 			out.print(" 无  ");
				 		}
				 %>				 
				 </td>
				 <td  align="center"><a href="health_idx_history.jsp?appId=<%=po.getAppId()%>"><u>进入</u></a></td>
			  </tr>
			  <%
			  	}
			  %>
			</table>
		</td>
	</tr>
	<tr>
      <td><img  src="<%=request.getContextPath () %>/statics/images/line.png"  width="1000" height="1"></td>
    </tr>
    <tr><td><jsp:include page="../bottom.jsp"></jsp:include></td></tr>
</table>

</center>
<div id="otherMessagePanel" title="">
	<table width="100%" height="100%" border="1" id="othertableId">
		<tr>
			<td align='center'>指标名称</td>			
			<td align="center">指标值</td>
			<td align="center">分数</td>
			<td align="center">权重</td>
			<td align="center">区间</td>
		</tr>
		<tbody id="otherMessageTable">		
		</tbody>
	</table>
</div>
<div id="statusMessagePanel" title="">
	<table width="100%" height="100%" border="1" >
		<tr>
			<td align='center'>指标名称</td>	
			<td align="center">指标值</td>
			<td align="center">健康阀值</td>
			<td align="center">分数</td>
			<td align="center">状态</td>
		</tr>
		<tbody id="statusMessageTable">		
		</tbody>
	</table>
</div>
<div id="dialog_report" title="Basic modal dialog">
	<iframe id="iframe_report" src="key_detail.jsp" frameborder="0" height="450" width="800" marginheight="0" marginwidth="0" scrolling="no"></iframe>
</div>
<script type="text/javascript">
$("#dialog_report").dialog({bgiframe: true,height: 500,	width:820,modal: true,draggable:true,resizable:false,autoOpen:false});
$("#otherMessagePanel").dialog({autoOpen: false, resizable: true ,zIndex:13001,width:600});
$("#statusMessagePanel").dialog({autoOpen: false, resizable: true ,zIndex:13001,width:600});
function showDetail(appname){
	var detail = $("#"+appname+"_detail").val();
	var keys = detail.split(";");
	$("#otherMessageTable").empty();
	for(var i=0;i<keys.length;i++ ){
		var key = keys[i];
		if(key != ""){			
			var k = key.split("#");
			if(k[4] == 1){
				 var tr = "<tr style='background-color:#00FF00'><td align='center'>"+k[0]+"<img src='<%=request.getContextPath () %>/statics/images/report.png' onclick='window.open(\"./indicator_history.jsp?appId="+k[9]+"&indicator="+k[8]+"\")'/></td><td align='center'>"+k[1]+"</td><td align='center'>"+k[2]+"</td><td align='center'>"+k[5]+"</td><td >"+k[3]+"</td></tr>";
				    $("#otherMessageTable").append(tr);
			}else if(k[4] == 2){
				 var tr = "<tr style='background-color:#FFFF00'><td align='center'>"+k[0]+"<img src='<%=request.getContextPath () %>/statics/images/report.png' onclick='window.open(\"./indicator_history.jsp?appId="+k[9]+"&indicator="+k[8]+"\")'/></td><td align='center'>"+k[1]+"</td><td align='center'>"+k[2]+"</td><td align='center'>"+k[5]+"</td><td >"+k[3]+"</td></tr>";
				    $("#otherMessageTable").append(tr);
			}else if(k[4] == 3){
				 var tr = "<tr style='background-color:#FF0000'><td align='center'>"+k[0]+"<img src='<%=request.getContextPath () %>/statics/images/report.png' onclick='window.open(\"./indicator_history.jsp?appId="+k[9]+"&indicator="+k[8]+"\")'/></td><td align='center'>"+k[1]+"</td><td align='center'>"+k[2]+"</td><td align='center'>"+k[5]+"</td><td >"+k[3]+"</td></tr>";
				    $("#otherMessageTable").append(tr);
			}else{
				var tr = "<tr ><td >"+k[0]+"<img src='<%=request.getContextPath () %>/statics/images/report.png' onclick='window.open(\"./indicator_history.jsp?appId="+k[9]+"&indicator="+k[8]+"\")'/></td><td >"+k[1]+"</td><td >"+k[2]+"</td><td align='center'>"+k[5]+"</td><td >"+k[3]+"</td></tr>";
			    $("#otherMessageTable").append(tr);
			}
		}
	}
	 $("#otherMessagePanel" ).dialog("option","title",(appname+"评分详细 &nbsp;&nbsp;&nbsp;&nbsp;<a target='_blank' href='help.html'>说明</a>"));
     $("#otherMessagePanel" ).dialog( 'open' );
}


function showStatusDetail(appname,appId){
	var detail = $("#"+appname+"_detail").val();
	var keys = detail.split(";");
	$("#statusMessageTable").empty();
	for(var i=0;i<keys.length;i++ ){
		var key = keys[i];
		if(key != ""){			
			var k = key.split("#");
			var h="<img src='<%=request.getContextPath () %>/statics/images/report.png' onclick='window.open(\"./indicator_history.jsp?appId="+k[9]+"&indicator="+k[8]+"\")'/>";
			if(k[4] == 1){
				 var tr = "<tr ><td align='center'>"+k[0]+h+"</td><td align='center'>"+k[1]+"</td><td align='center'>"+k[7]+"</td><td align='center'>"+k[2]+"</td><td align='center'><a target='_blank' href='app_detail.jsp?appId="+appId+"&key=" + k[8] + "&collectTime1="+ k[6] +"'><font style='color:#00008B'><u>健康</u></font></a></td></tr>";
				    $("#statusMessageTable").append(tr);
			}else if(k[4] == 2){
				var tr = "<tr ><td align='center'>"+k[0]+h+"</td><td align='center'>"+k[1]+"</td><td align='center'>"+k[7]+"</td><td align='center'>"+k[2]+"</td><td align='center'><a target='_blank' href='app_detail.jsp?appId="+appId+"&key=" + k[8] + "&collectTime1="+ k[6] +"'><font style='color:#FF00FF'><u>亚健康</u></font></a></td></tr>";
				    $("#statusMessageTable").append(tr);
			}else if(k[4] == 3){
				var tr = "<tr ><td align='center'>"+k[0]+h+"</td><td align='center'>"+k[1]+"</td><td align='center'>"+k[7]+"</td><td align='center'>"+k[2]+"</td><td align='center'><a target='_blank' href='app_detail.jsp?appId="+appId+"&key=" + k[8] + "&collectTime1="+ k[6] +"'><font style='color:#FF0000'><u>危险</u></font></a></td></tr>";
				    $("#statusMessageTable").append(tr);
			}else{
				var tr = "<tr ><td align='center'>"+k[0]+h+"</td><td align='center'>"+k[1]+"</td><td align='center'>"+k[7]+"</td><td align='center'>"+k[2]+"</td><td align='center'><a target='_blank' href='app_detail.jsp?appId="+appId+"&key=" + k[8] + "&collectTime1="+ k[6] +"'><font style='color:#00FF00'><u>健康</u></font></a></td></tr>";
			    $("#statusMessageTable").append(tr);
			}
		}
	}
	 $("#statusMessagePanel" ).dialog("option","title",(appname+"状态&nbsp;&nbsp;&nbsp;&nbsp;<a target='_blank' href='help.html'>说明</a>"));
     $("#statusMessagePanel" ).dialog( 'open' );
}

function openAppDistinct(appid){
	$("#iframe_report").attr("src","<%=request.getContextPath()%>/distinct/index_distinct.jsp?appId="+appid);
	$("#dialog_report").dialog("open");

}

</script>
</body>
</html>