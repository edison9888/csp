<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<HTML xmlns:v="urn:schemas-microsoft-com:vml" xmlns:o="urn:schemas-microsoft-com:office:office">
<HEAD>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<meta http-equiv="imagetoolbar" content="no">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<title>应用监控系统-实时首页</title>
<link href="<%=request.getContextPath() %>/statics/css/axurerppage.css" type="text/css" rel="stylesheet">
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.accordion.js"></script>

<STYLE>
v\:* { behavior: url(#default#VML);} 
o\:* { behavior: url(#default#VML);}
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
.code {
background-color:#DDEDFB;
border:1px solid #0099CC;
color:#000000;
display:block;
font-size:13px;
position:absolute; 
z-index:100;  
left:10px; 
width:130px; 
height:20px;
text-align:left;
font-family:Arial; 
text-align:left; 
word-wrap:break-word;
}


.main_panel {
background-color:#DDEDFB;
border:1px solid #0099CC;
color:#000000;
display:block;
font-size:13px;
text-align:left;
font-family:Arial; 
text-align:left; 
word-wrap:break-word;
position:relative;
left:50px; 
top:20px;
width:1140px; 
height:600px;
}


.alarm_panel {
background-color:#DDEDFB;
border:1px solid #0099CC;
color:#000000;
display:block;
font-size:13px;
text-align:left;
font-family:Arial; 
text-align:left; 
word-wrap:break-word;
position:relative;
left:50px; 
top:20px;
width:1140px; 
height:100px;
}


.valueSpan {
color:green;
font-family:'Arial'; 
font-size:18px;

}

</STYLE>
</HEAD>
<BODY>
<table width="100%" border="1" class="ui-widget ui-widget-content">	
<tr>
		<td align="center">
			最近告警记录
		</td>
	<tr>
		<td>
			<table width="500" border="1" class="ui-widget ui-widget-content">	
				<tr class="ui-widget-header ">
					<td align="center" width="50">应用</td>
					<td align="center" width="50">时间</td>
					<td align="center" width="400">信息</td>
				</tr>
				<tbody id="alarm_table_1">
					<tr >
					<td align="center" width="50">&nbsp;</td>
					<td align="center" width="50"> &nbsp;</td>
					<td align="center" width="400">&nbsp;</td>
				</tr>
				</tbody>
			</table>
		</td>
		<td>
			<table width="500" border="1" class="ui-widget ui-widget-content">	
				<tr class="ui-widget-header ">
					<td align="center" width="50">应用</td>
					<td align="center" width="50"> 时间</td>
					<td align="center" width="400">信息</td>
				</tr>
				<tbody id="alarm_table_2">
					<tr >
					<td align="center" width="50">&nbsp;</td>
					<td align="center" width="50"> &nbsp;</td>
					<td align="center" width="400">&nbsp;</td>
				</tr>
				</tbody>
			</table>
		</td>
	</tr>
</table>





<div id="main_panel" class="main_panel">
<DIV id="inter_panel" style="position:absolute; left:250px; top:0px; width:600px; height:40px;; overflow:visible; "><!-- start panel-->
<span style="position:absolute; left:0px; top:0px; width:600px; height:40px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u2.png',sizingMethod='scale');
"><img style="width:600px; height:40px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u2.png" border="0" alt=""></span>

<div style="position:absolute; left:250px; top:5px; width:200px; height:40px;; overflow:visible;z-index:9001;">
互联网 -用户<font id="time_panel" style="font-size:20px;"></font></div>

</DIV><!-- end panel-->



<DIV id="list_panel" style="position:absolute; left:20px; top:150px; width:150px; height:150px;; overflow:visible; background-color:green;"><!-- start panel-->
<span style="position:absolute; left:0px; top:0px; width:150px; height:150px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u2.png',sizingMethod='scale');
"><img style="width:150px; height:150px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u2.png" border="0" alt=""></span>

<DIV id="list_title_panel" style="position:absolute;z-index:10; left:0px; top:0px; width:150px; height:29px;">
<span style="position:absolute; left:0px; top:0px; width:150px; height:29px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u4.png',sizingMethod='scale');
"><img style="width:150px; height:29px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u4.png" border="0" alt=""></span>
</DIV>
<DIV id="list_text" style="position:absolute; z-index:100;  left:60px; top:3px; width:80px; height:20px;text-align:left;font-family:Arial; text-align:left; word-wrap:break-word;" >
<DIV id="list_text_value"><span style=" font-family:'Arial'; color:#000000; font-size:13px;"><a href="<%=request.getContextPath() %>/time/app_time.jsp?appName=list" target="_blank">list</a></span></DIV></DIV>

<DIV id="list_CPU_text" class="code" style="top:30px;" >
<DIV >CPU:<span id="list_CPU_need_alarm" class="valueSpan"></span>%</DIV></DIV>
<DIV id="list_load_text" class="code" style="top:50px;" >
<DIV >LOAD:<span id="list_LOAD_need_alarm" class="valueSpan"></span></DIV></DIV>

<div id="list_pv_panel" class="annwindow"  style="z-index:8001;width:250px;height:20px;left:00px; top:-40px;visibility:visible;position:absolute;">
pv:<span id="list_PV_VISIT_COUNTTIMES_need_alarm" class="valueSpan"></span>rt:<span id="list_PV_REST_AVERAGEUSERTIMES_need_alarm" class="valueSpan"></span>
</div>

<div id="list_other_alarm_msg" style="position:absolute;display:none;z-index:9001;">
	<img width="20" height="20" src="<%=request.getContextPath() %>/statics/images/alarm.gif" title="点击查看详细告警信息" />
</div>

</DIV><!-- end panel-->



<DIV id="shopsystem_panel" style="position:absolute; left:210px; top:150px; width:150px; height:150px;; overflow:visible;background-color:green; "><!-- start panel-->
<span style="position:absolute; left:0px; top:0px; width:150px; height:150px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u2.png',sizingMethod='scale');
"><img style="width:150px; height:150px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u2.png" border="0" alt=""></span>

<DIV id="shopsystem_title_panel" style="position:absolute;z-index:10; left:0px; top:0px; width:150px; height:29px;">
<span style="position:absolute; left:0px; top:0px; width:150px; height:29px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u4.png',sizingMethod='scale');
"><img style="width:150px; height:29px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u4.png" border="0" alt=""></span>
</DIV>

<DIV id="shopsystem_text" style="position:absolute; z-index:100;  left:60px; top:3px; width:120px; height:20px;text-align:left;font-family:Arial; text-align:left; word-wrap:break-word;" >
<DIV ><span id="shopsystem_text_value" style=" font-family:'Arial'; color:#000000; font-size:13px;"><a href="<%=request.getContextPath() %>/time/app_time.jsp?appName=shopsystem" target="_blank">shopsystem</a></span></DIV></DIV>

<DIV id="shopsystem_CPU_text" class="code" style="top:30px;" >
<DIV >CPU:<span id="shopsystem_CPU_need_alarm" class="valueSpan"></span>%</DIV></DIV>

<DIV id="shopsystem_LOAD_text" class="code" style="top:50px;" >
<DIV >LOAD:<span id="shopsystem_LOAD_need_alarm" class="valueSpan"></span></DIV></DIV>

<div id="shopsystem_pv_panel" class="annwindow"  style="z-index:8001;width:250px;height:20px;left:20px; top:-40px;visibility:visible;position:absolute;">
pv:<span id="shopsystem_PV_VISIT_COUNTTIMES_need_alarm" class="valueSpan"></span>rt:<span id="shopsystem_PV_REST_AVERAGEUSERTIMES_need_alarm" class="valueSpan"></span>
</div>


<div id="shopsystem_other_alarm_msg" style="position:absolute;display:none;z-index:9001;">
	<img width="20" height="20" src="<%=request.getContextPath() %>/statics/images/alarm.gif" title="点击查看详细告警信息" />
</div>
</DIV><!-- end panel-->






<DIV id="item_panel" style="position:absolute; left:400px; top:150px; width:150px; height:150px;; overflow:visible;background-color:green; "><!-- start panel-->
<span style="position:absolute; left:0px; top:0px; width:150px; height:150px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u2.png',sizingMethod='scale');
"><img style="width:150px; height:150px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u2.png" border="0" alt=""></span>

<DIV id="item_title_panel" style="position:absolute;z-index:10; left:0px; top:0px; width:150px; height:29px;">
<span style="position:absolute; left:0px; top:0px; width:150px; height:29px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u4.png',sizingMethod='scale');
"><img style="width:150px; height:29px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u4.png" border="0" alt=""></span>
</DIV>
<DIV id="item_text" style="position:absolute; z-index:100;  left:60px; top:3px; width:40px; height:20px;text-align:left;font-family:Arial; text-align:left; word-wrap:break-word;" >
<DIV id="item_text_value"><span style=" font-family:'Arial'; color:#000000; font-size:13px;"><a href="<%=request.getContextPath() %>/time/app_time.jsp?appName=item" target="_blank">item</a></span></DIV></DIV>

<DIV id="item_CPU_text" class="code" style="top:30px;" >
<DIV >CPU:<span id="item_CPU_need_alarm" class="valueSpan"></span>%</DIV></DIV>

<DIV id="item_LOAD_text" class="code" style="top:50px;" >
<DIV >LOAD:<span id="item_LOAD_need_alarm" class="valueSpan"></span></DIV></DIV>


<div id="item_pv_panel" class="annwindow"  style="z-index:8001;width:250px;height:20px;left:40px; top:-30px;visibility:visible;position:absolute;">
pv:<span id="item_PV_VISIT_COUNTTIMES_need_alarm" class="valueSpan"></span>rt:<span id="item_PV_REST_AVERAGEUSERTIMES_need_alarm" class="valueSpan"></span>
</div>


<div id="item_other_alarm_msg" style="position:absolute;display:none;z-index:9001;">
	<img width="20" height="20" src="<%=request.getContextPath() %>/statics/images/alarm.gif" title="点击查看详细告警信息" />
</div>
</DIV><!-- end panel-->




<DIV id="trademgr_panel" style="position:absolute; left:590px; top:150px; width:150px; height:150px;; overflow:visible;background-color:green; "><!-- start panel-->
<span style="position:absolute; left:0px; top:0px; width:150px; height:150px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u2.png',sizingMethod='scale');
"><img style="width:150px; height:150px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u2.png" border="0" alt=""></span>

<DIV id="trademgr_title_panel" style="position:absolute;z-index:10; left:0px; top:0px; width:150px; height:29px;">
<span style="position:absolute; left:0px; top:0px; width:150px; height:29px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u4.png',sizingMethod='scale');
"><img style="width:150px; height:29px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u4.png" border="0" alt=""></span>
</DIV>
<DIV id="trademgr_text" style="position:absolute; z-index:100;  left:60px; top:3px; width:80px; height:20px;text-align:left;font-family:Arial; text-align:left; word-wrap:break-word;" >
<DIV id="trademgr_text_value"><span style=" font-family:'Arial'; color:#000000; font-size:13px;"><a href="<%=request.getContextPath() %>/time/app_time.jsp?appName=trademgr" target="_blank">trademgr</a></span></DIV></DIV>


<DIV id="trademgr_CPU_text" class="code" style="top:30px;" >
<DIV >CPU:<span id="trademgr_CPU_need_alarm" class="valueSpan"></span>%</DIV></DIV>

<DIV id="trademgr_LOAD_text" class="code" style="top:50px;" >
<DIV >LOAD:<span id="trademgr_LOAD_need_alarm" class="valueSpan"></span></DIV></DIV>


<div id="trademgr_pv_panel" class="annwindow"  style="z-index:8001;width:250px;height:20px;left:40px; top:-30px;visibility:visible;position:absolute;">
pv:<span id="trademgr_PV_VISIT_COUNTTIMES_need_alarm" class="valueSpan"></span>rt:<span id="trademgr_PV_REST_AVERAGEUSERTIMES_need_alarm" class="valueSpan"></span>
</div>


<div id="trademgr_other_alarm_msg" style="position:absolute;display:none;z-index:9001;">
	<img width="20" height="20" src="<%=request.getContextPath() %>/statics/images/alarm.gif" title="点击查看详细告警信息" />
</div>
</DIV><!-- end panel-->






<DIV id="buy_panel" style="position:absolute; left:780px; top:150px; width:150px; height:150px;; overflow:visible;background-color:green; "><!-- start panel-->
<span style="position:absolute; left:0px; top:0px; width:150px; height:150px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u2.png',sizingMethod='scale');
"><img style="width:150px; height:150px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u2.png" border="0" alt=""></span>

<DIV id="buy_title_panel" style="position:absolute;z-index:10; left:0px; top:0px; width:150px; height:29px;">
<span style="position:absolute; left:0px; top:0px; width:150px; height:29px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u4.png',sizingMethod='scale');
"><img style="width:150px; height:29px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u4.png" border="0" alt=""></span>
</DIV>
<DIV id="buy_text" style="position:absolute; z-index:100;  left:60px; top:3px; width:40px; height:20px;text-align:left;font-family:Arial; text-align:left; word-wrap:break-word;" >
<DIV id="buy_text_value"><span style=" font-family:'Arial'; color:#000000; font-size:13px;"><a href="<%=request.getContextPath() %>/time/app_time.jsp?appName=buy" target="_blank">buy</a></span></DIV></DIV>


<DIV id="buy_CPU_text" class="code" style="top:30px;" >
<DIV >CPU:<span id="buy_CPU_need_alarm" class="valueSpan"></span>%</DIV></DIV>

<DIV id="buy_LOAD_text" class="code" style="top:50px;" >
<DIV >LOAD:<span id="buy_LOAD_need_alarm" class="valueSpan"></span></DIV></DIV>

<div id="buy_pv_panel" class="annwindow"  style="z-index:8001;width:250px;height:20px;left:40px; top:-20px;visibility:visible;position:absolute;">
pv:<span id="buy_PV_VISIT_COUNTTIMES_need_alarm" class="valueSpan"></span>rt:<span id="buy_PV_REST_AVERAGEUSERTIMES_need_alarm" class="valueSpan"></span>
</div>


<div id="buy_other_alarm_msg" style="position:absolute;display:none;z-index:9001;">
	<img width="20" height="20" src="<%=request.getContextPath() %>/statics/images/alarm.gif" title="点击查看详细告警信息" />
</div>
</DIV><!-- end panel-->






<DIV id="login_panel" style="position:absolute; left:970px; top:150px; width:150px; height:150px;; overflow:visible;background-color:green; "><!-- start panel-->
<span style="position:absolute; left:0px; top:0px; width:150px; height:150px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u2.png',sizingMethod='scale');
"><img style="width:150px; height:150px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u2.png" border="0" alt=""></span>

<DIV id="login_title_panel" style="position:absolute;z-index:10; left:0px; top:0px; width:150px; height:29px;">
<span style="position:absolute; left:0px; top:0px; width:150px; height:29px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u4.png',sizingMethod='scale');
"><img style="width:150px; height:29px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u4.png" border="0" alt=""></span>
</DIV>
<DIV id="login_text" style="position:absolute; z-index:100;  left:60px; top:3px; width:40px; height:20px;text-align:left;font-family:Arial; text-align:left; word-wrap:break-word;" >
<DIV id="login_text_value"><span style=" font-family:'Arial'; color:#000000; font-size:13px;"><a href="<%=request.getContextPath() %>/time/app_time.jsp?appName=login" target="_blank">login</a></span></DIV></DIV>


<DIV id="login_CPU_text" class="code" style="top:30px;" >
<DIV >CPU:<span id="login_CPU_need_alarm" class="valueSpan"></span>%</DIV></DIV>

<DIV id="login_LOAD_text" class="code" style="top:50px;" >
<DIV >LOAD:<span id="login_LOAD_need_alarm" class="valueSpan"></span></DIV></DIV>

<div id="login_pv_panel" class="annwindow"  style="z-index:8001;width:250px;height:20px;left:40px; top:-30px;visibility:visible;position:absolute;">
pv:<span id="login_PV_VISIT_COUNTTIMES_need_alarm" class="valueSpan"></span>rt:<span id="login_PV_REST_AVERAGEUSERTIMES_need_alarm" class="valueSpan"></span>
</div>


<div id="login_other_alarm_msg" style="position:absolute;display:none;z-index:9001;">
	<img width="20" height="20" src="<%=request.getContextPath() %>/statics/images/alarm.gif" title="点击查看详细告警信息" />
</div>
</DIV><!-- end panel-->





<DIV id="ic_panel" style="position:absolute; left:170px; top:450px; width:150px; height:150px;; overflow:visible;background-color:green; "><!-- start panel-->
<span style="position:absolute; left:0px; top:0px; width:150px; height:150px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u2.png',sizingMethod='scale');
"><img style="width:150px; height:150px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u2.png" border="0" alt=""></span>

<DIV id="ic_title_panel" style="position:absolute;z-index:10; left:0px; top:0px; width:150px; height:29px;">
<span style="position:absolute; left:0px; top:0px; width:150px; height:29px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u4.png',sizingMethod='scale');
"><img style="width:150px; height:29px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u4.png" border="0" alt=""></span>
</DIV>
<DIV id="ic_text" style="position:absolute; z-index:100;  left:60px; top:3px; width:40px; height:20px;text-align:left;font-family:Arial; text-align:left; word-wrap:break-word;" >
<DIV id="ic_text_value"><span style=" font-family:'Arial'; color:#000000; font-size:13px;"><a href="<%=request.getContextPath() %>/time/app_time.jsp?appName=ic" target="_blank">ic</a></span></DIV></DIV>

<DIV id="ic_CPU_text" class="code" style="top:30px;" >
<DIV >CPU:<span id="ic_CPU_need_alarm" class="valueSpan"></span>%</DIV></DIV>
<DIV id="ic_LOAD_text" class="code" style="top:50px;" >
<DIV >LOAD:<span id="ic_LOAD_need_alarm" class="valueSpan"></span></DIV></DIV>



<DIV id="ic_pv_text" class="code" style="top:70px;" >
<DIV title="ItemQueryService:1.0.0-L0_queryItemById的每分钟调用量">接口pv:<span id="ic_PV_VISIT_COUNTTIMES_need_alarm" class="valueSpan"></span></DIV></DIV>
<DIV id="ic_Rt_text" class="code" style="top:90px;" >
<DIV title="ItemQueryService:1.0.0-L0_queryItemById的平均响应时间">接口Rt:<span id="ic_PV_REST_AVERAGEUSERTIMES_need_alarm" class="valueSpan"></span></DIV></DIV>

<DIV class="code" style="top:110px;" >
<DIV > <button id="ic_ProviderDetail">All Provider</button> </DIV></DIV>

<div id="ic_other_alarm_msg" style="position:absolute;display:none;z-index:9001;">
	<img width="20" height="20" src="<%=request.getContextPath() %>/statics/images/alarm.gif" title="点击查看详细告警信息" />
</div>
</DIV><!-- end panel-->







<DIV id="tc_panel" style="position:absolute; left:370px; top:450px; width:150px; height:150px;; overflow:visible;background-color:green; "><!-- start panel-->
<span style="position:absolute; left:0px; top:0px; width:150px; height:150px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u2.png',sizingMethod='scale');
"><img style="width:150px; height:150px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u2.png" border="0" alt=""></span>

<DIV id="tc_title_panel" style="position:absolute;z-index:10; left:0px; top:0px; width:150px; height:29px;">
<span style="position:absolute; left:0px; top:0px; width:150px; height:29px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u4.png',sizingMethod='scale');
"><img style="width:150px; height:29px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u4.png" border="0" alt=""></span>
</DIV>
<DIV id="tc_text" style="position:absolute; z-index:100;  left:60px; top:3px; width:40px; height:20px;text-align:left;font-family:Arial; text-align:left; word-wrap:break-word;" >
<DIV id="tc_text_value"><span style=" font-family:'Arial'; color:#000000; font-size:13px;"><a href="<%=request.getContextPath() %>/time/app_time.jsp?appName=tc" target="_blank">tc</a></span></DIV></DIV>

<DIV id="tc_CPU_text" class="code" style="top:30px;" >
<DIV >CPU:<span id="tc_CPU_need_alarm" class="valueSpan"></span>%</DIV></DIV>
<DIV id="tc_LOAD_text" class="code" style="top:50px;" >
<DIV >LOAD:<span id="tc_LOAD_need_alarm" class="valueSpan"></span></DIV></DIV>


<DIV id="tc_pv_text" class="code" style="top:70px;" >
<DIV title="TcBaseService:1.0.0_queryMainAndDetail的每分钟调用量">接口pv:<span id="tc_PV_VISIT_COUNTTIMES_need_alarm" class="valueSpan"></span></DIV></DIV>
<DIV id="tc_Rt_text" class="code" style="top:90px;" >
<DIV title="TcBaseService:1.0.0_queryMainAndDetail的平均响应时间">接口Rt:<span id="tc_PV_REST_AVERAGEUSERTIMES_need_alarm" class="valueSpan"></span></DIV></DIV>

<DIV class="code" style="top:110px;" >
<DIV > <button id="tc_ProviderDetail">All Provider</button> </DIV></DIV>

<div id="tc_other_alarm_msg" style="position:absolute;display:none;z-index:9001;">
	<img width="20" height="20" src="<%=request.getContextPath() %>/statics/images/alarm.gif" title="点击查看详细告警信息" />
</div>
</DIV><!-- end panel-->









<DIV id="tbuic_panel" style="position:absolute; left:570px; top:450px; width:150px; height:150px;; overflow:visible;background-color:green; "><!-- start panel-->
<span style="position:absolute; left:0px; top:0px; width:150px; height:150px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u2.png',sizingMethod='scale');
"><img style="width:150px; height:150px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u2.png" border="0" alt=""></span>

<DIV id="tbuic_title_panel" style="position:absolute;z-index:10; left:0px; top:0px; width:150px; height:29px;">
<span style="position:absolute;left:0px;top:0px;width:150px;height:29px;filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u4.png',sizingMethod='scale');">
<img style="width:150px; height:29px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" src="<%=request.getContextPath() %>/statics/images/u4.png" border="0" alt=""></span>
</DIV>
<DIV id="tbuic_text" style="position:absolute; z-index:100;  left:60px; top:3px; width:40px; height:20px;text-align:left;font-family:Arial; text-align:left; word-wrap:break-word;" >
<DIV id="tbuic_text_value"><span style=" font-family:'Arial'; color:#000000; font-size:13px;"><a href="<%=request.getContextPath() %>/time/app_time.jsp?appName=tbuic" target="_blank">tbuic</a></span></DIV></DIV>

<DIV id="tbuic_CPU_text" class="code" style="top:30px;" >
<DIV >CPU:<span id="tbuic_CPU_need_alarm" class="valueSpan"></span>%</DIV></DIV>
<DIV id="tbuic_LOAD_text" class="code" style="top:50px;" >
<DIV >LOAD:<span id="tbuic_LOAD_need_alarm" class="valueSpan"></span></DIV></DIV>


<DIV id="tbuic_pv_text" class="code" style="top:70px;" >
<DIV title="UserReadService:1.0.0_getUserAndUserExtraById的每分钟调用量">接口pv:<span id="tbuic_PV_VISIT_COUNTTIMES_need_alarm" class="valueSpan"></span></DIV></DIV>
<DIV id="tbuic_Rt_text" class="code" style="top:90px;" >
<DIV title="UserReadService:1.0.0_getUserAndUserExtraById的平均响应时间">接口Rt:<span id="tbuic_PV_REST_AVERAGEUSERTIMES_need_alarm" class="valueSpan"></span></DIV></DIV>

<DIV class="code" style="top:110px;" >
<DIV > <button id="tbuic_ProviderDetail">All Provider</button> </DIV></DIV>

<div id="tbuic_other_alarm_msg" style="position:absolute;display:none;z-index:9001;">
	<img width="20" height="20" src="<%=request.getContextPath() %>/statics/images/alarm.gif" title="点击查看详细告警信息" />
</div>
</DIV><!-- end panel-->




<DIV id="shopcenter_panel" style="position:absolute; left:770px; top:450px; width:150px; height:150px;; overflow:visible;background-color:green; "><!-- start panel-->
<span style="position:absolute; left:0px; top:0px; width:150px; height:150px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u2.png',sizingMethod='scale');
"><img style="width:150px; height:150px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u2.png" border="0" alt=""></span>

<DIV id="shopcenter_title_panel" style="position:absolute;z-index:10; left:0px; top:0px; width:150px; height:29px;">
<span style="position:absolute; left:0px; top:0px; width:150px; height:29px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u4.png',sizingMethod='scale');
"><img style="width:150px; height:29px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u4.png" border="0" alt=""></span>
</DIV>
<DIV id="shopcenter_text" style="position:absolute; z-index:100;  left:60px; top:3px; width:120px; height:20px;text-align:left;font-family:Arial; text-align:left; word-wrap:break-word;" >
<DIV id="shopcenter_text_value"><span style=" font-family:'Arial'; color:#000000; font-size:13px;"><a href="<%=request.getContextPath() %>/time/app_time.jsp?appName=shopcenter" target="_blank">shopcenter</a></span></DIV></DIV>

<DIV id="shopcenter_CPU_text" class="code" style="top:30px;" >
<DIV >CPU:<span id="shopcenter_CPU_need_alarm" class="valueSpan"></span>%</DIV></DIV>
<DIV id="shopcenter_LOAD_text" class="code" style="top:50px;" >
<DIV >LOAD:<span id="shopcenter_LOAD_need_alarm" class="valueSpan"></span></DIV></DIV>

<DIV id="shopcenter_pv_text" class="code" style="top:70px;" >
<DIV title="ShopReadService:1.0.0_queryShop的每分钟调用量">接口pv:<span id="shopcenter_PV_VISIT_COUNTTIMES_need_alarm" class="valueSpan"></span></DIV></DIV>
<DIV id="shopcenter_Rt_text" class="code" style="top:90px;" >
<DIV title="ShopReadService:1.0.0_queryShop的平均响应时间">接口Rt:<span id="shopcenter_PV_REST_AVERAGEUSERTIMES_need_alarm" class="valueSpan"></span></DIV></DIV>

<DIV class="code" style="top:110px;" >
<DIV > <button id="shopcenter_ProviderDetail">All Provider</button> </DIV></DIV>


<div id="shopcenter_other_alarm_msg" style="position:absolute;display:none;z-index:9001;">
	<img width="20" height="20" src="<%=request.getContextPath() %>/statics/images/alarm.gif" title="点击查看详细告警信息" />
</div>
</DIV><!-- end panel-->


<DIV id="payway_panel" style="position:absolute; left:450px; top:650px; width:150px; height:75px;; overflow:visible;background-color:green; "><!-- start panel-->
<span style="position:absolute; left:0px; top:0px; width:150px; height:75px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u2.png',sizingMethod='scale');
"><img style="width:150px; height:75px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u2.png" border="0" alt=""></span>

<DIV id="payway_title_panel" style="position:absolute;z-index:10; left:0px; top:0px; width:150px; height:29px;">
<span style="position:absolute; left:0px; top:0px; width:150px; height:29px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u4.png',sizingMethod='scale');
"><img style="width:150px; height:29px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u4.png" border="0" alt=""></span>
</DIV>
<DIV id="payway_text" style="position:absolute; z-index:100;  left:60px; top:3px; width:120px; height:20px;text-align:left;font-family:Arial; text-align:left; word-wrap:break-word;" >
<DIV id="payway_text_value"><span style=" font-family:'Arial'; color:#000000; font-size:13px;">支付宝</span></DIV></DIV>
<div id="payway_other_alarm_msg" style="position:absolute;display:none;z-index:9001;">
	<img width="20" height="20" src="<%=request.getContextPath() %>/statics/images/alarm.gif" title="点击查看详细告警信息" />
</div>
</DIV><!-- end panel-->



<!-- start panel-->
<DIV id="searchengine_panel" style="position:absolute; left:40px; top:350px; width:50px; height:50px;; overflow:visible;background-color:green; ">
<span style="position:absolute; left:0px; top:0px; width:50px; height:50px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u2.png',sizingMethod='scale');
"><img style="width:50px; height:50px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u2.png" border="0" alt=""></span>

<DIV id="searchengine_title_panel" style="position:absolute;z-index:10; left:0px; top:0px; width:150px; height:29px;">
<span style="position:absolute; left:0px; top:0px; width:50px; height:29px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u4.png',sizingMethod='scale');
"><img style="width:50px; height:29px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u4.png" border="0" alt=""></span>
</DIV>
<DIV id="searchengine_text" style="position:absolute; z-index:100;  left:0px; top:3px; width:120px; height:20px;text-align:left;font-family:Arial; text-align:left; word-wrap:break-word;" >
<DIV id="searchengine_text_value"><span style=" font-family:'Arial'; color:#000000; font-size:13px;">Search</span></DIV></DIV>
</DIV>
<!-- end panel-->


<!-- start panel-->
<!--
<DIV id="tair_panel" style="position:absolute; left:130px; top:350px; width:50px; height:50px;; overflow:visible;background-color:green; ">
<span style="position:absolute; left:0px; top:0px; width:50px; height:50px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u2.png',sizingMethod='scale');
"><img style="width:50px; height:50px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u2.png" border="0" alt=""></span>

<DIV id="tair_title_panel" style="position:absolute;z-index:10; left:0px; top:0px; width:150px; height:29px;">
<span style="position:absolute; left:0px; top:0px; width:50px; height:29px; ;
filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='<%=request.getContextPath() %>/statics/images/u4.png',sizingMethod='scale');
"><img style="width:50px; height:29px; filter:progid:DXImageTransform.Microsoft.Alpha(opacity=0);" 
src="<%=request.getContextPath() %>/statics/images/u4.png" border="0" alt=""></span>
</DIV>
<DIV id="tair_text" style="position:absolute; z-index:100;  left:0px; top:3px; width:120px; height:20px;text-align:left;font-family:Arial; text-align:left; word-wrap:break-word;" >
<DIV id="tair_text_value"><span style=" font-family:'Arial'; color:#000000; font-size:13px;">Tair</span></DIV></DIV>
</DIV> 
end panel-->



<div id="lineContain" style="z-index:1000"></div>

</div>

<!---------------------- 下面是放各种弹出信息 -->

<!-- 前端连线 鼠标移到上面显示 -->
<div id="visitMessagePanel" title="">
	<table width="100%" height="100%" border="1" >
		<tr>
			<td >流量</td>
			<td >qps</td>
			<td >响应时间</td>
		</tr>
		<tbody id="visitMessageTable">		
		
		</tbody>		
	</table>
</div>


<div id="toCMessagePanel" title="">
	<table width="100%" height="100%" border="1" >
		<tr>
			<td >接口名称</td>
			<td >执行次数</td>
			<td >平均时间</td>
		</tr>
		<tbody id="toCMessageTable">		
		</tbody>
	</table>
</div>

<div id="otherMessagePanel" title="">
	<table width="100%" height="100%" border="1" >
		<tr>
			<td >告警key名称</td>			
			<td >告警值</td>
		</tr>
		<tbody id="otherMessageTable">		
		</tbody>
	</table>
</div>

<div id="paywayMessagePanel" title="">
	<table width="100%" height="100%" border="1" >
		<tr>
			<td >监控点</td>			
			<td >失败率</td>
		</tr>
		<tbody id="paywayMessageTable">		
		</tbody>
	</table>
</div>

<div id="dialog_report" title="详细信息">
	<iframe id="iframe_report" src="key_detail.jsp" frameborder="0" height="450" width="800" marginheight="0" marginwidth="0" scrolling="no"></iframe>
</div>



<script type="text/javascript">

/********************************************************
 *所有告警控件的ID 命名规则为appName_keyName_need_alarm   
 *
 ********************************************************/

var suffix = "need_alarm";

/**
* 创建div 之间的连线
* @author xiaodu
* 
*/
function linkPanel(lineName,panel1,panel2){
	createLine(lineName,computePoint(panel1,computePosition(panel1,panel2)),computePoint(panel2,computePosition(panel2,panel1)));	
}
/**
* 创建线条
* @author xiaodu
* 
*/
function createLine(name,pos1,pos2){		
	var line = "<v:line id='"+name+"' strokeColor='green' strokeWeight='2pt' from='"+pos1.left+","+pos1.top+"' to='"+pos2.left+","+pos2.top+"' style='position:absolute;z-index:1000;bgcolor:red'><v:stroke dashstyle='Solid' endarrow='classic'/></v:line>";
	document.getElementById("lineContain").innerHTML+=line;
	
}
/**
* 计算 两个panel 的上下位置
* @author xiaodu
* 
*/
function computePosition(panel1,panel2){
	var position1 = $(panel1).position();
	var top1 = position1.top;
	var position2 = $(panel2).position();
	var top2 = position2.top;
	if(top1<top2){
		return true;
	}else{
		return false;
	}	
}
/**
* 计算 线条的点的坐标
* @author xiaodu
* 
*/
function computePoint(panel1,pos){
	var position = $(panel1).position();
	var left = position.left;
	var top = position.top;
	var height = $(panel1).height();
	
	var width = $(panel1).width();
	
	if(pos){//true 表示这个panel 是在上面
		var returnTop = top+height;
		var returnLeft = left+(width/2);
		return {top:returnTop,left:returnLeft};		
	}else{
		var returnTop = top;
		var returnLeft = left+(width/2);
		return {top:returnTop,left:returnLeft};		
	}	
}
linkPanel("inter_list_line_need_alarm","#inter_panel","#list_panel");
linkPanel("inter_shopsystem_line_need_alarm","#inter_panel","#shopsystem_panel");
linkPanel("inter_item_line_need_alarm","#inter_panel","#item_panel");
linkPanel("inter_trademgr_line_need_alarm","#inter_panel","#trademgr_panel");
linkPanel("inter_buy_line_need_alarm","#inter_panel","#buy_panel");
linkPanel("inter_login_line_need_alarm","#inter_panel","#login_panel");

linkPanel("shopsystem_shopcenter_line_need_alarm","#shopsystem_panel","#shopcenter_panel");
linkPanel("shopsystem_ic_line_need_alarm","#shopsystem_panel","#ic_panel");
linkPanel("shopsystem_tbuic_line_need_alarm","#shopsystem_panel","#tbuic_panel");



linkPanel("item_tbuic_line_need_alarm","#item_panel","#tbuic_panel");
linkPanel("item_ic_line_need_alarm","#item_panel","#ic_panel");
linkPanel("item_tc_line_need_alarm","#item_panel","#tc_panel");
linkPanel("item_shopcenter_line_need_alarm","#item_panel","#shopcenter_panel");


linkPanel("trademgr_tbuic_line_need_alarm","#trademgr_panel","#tbuic_panel");
linkPanel("trademgr_ic_line_need_alarm","#trademgr_panel","#ic_panel");
linkPanel("trademgr_shopcenter_line_need_alarm","#trademgr_panel","#shopcenter_panel");
linkPanel("trademgr_tc_line_need_alarm","#trademgr_panel","#tc_panel");




linkPanel("buy_tbuic_line_need_alarm","#buy_panel","#tbuic_panel");
linkPanel("buy_ic_line_need_alarm","#buy_panel","#ic_panel");
linkPanel("buy_shopcenter_line_need_alarm","#buy_panel","#shopcenter_panel");
linkPanel("buy_tc_line_need_alarm","#buy_panel","#tc_panel");


linkPanel("login_tbuic_line_need_alarm","#login_panel","#tbuic_panel");
linkPanel("login_shopcenter_line_need_alarm","#login_panel","#shopcenter_panel");


linkPanel("list_searchengine_line_need_alarm","#list_panel","#searchengine_panel");
linkPanel("ic_searchengine_line_need_alarm","#ic_panel","#searchengine_panel");
linkPanel("shopsystem_searchengine_line_need_alarm","#shopsystem_panel","#searchengine_panel");

linkPanel("tc_payway_line_need_alarm","#tc_panel","#payway_panel");
//linkPanel("tc_tair_line_need_alarm","#tc_panel","#tair_panel");
//linkPanel("shopcenter_tair_line_need_alarm","#shopcenter_panel","#tair_panel");
//linkPanel("tbuic_tair_line_need_alarm","#tbuic_panel","#tair_panel");
//linkPanel("ic_tair_line_need_alarm","#ic_panel","#tair_panel");
//linkPanel("buy_tair_line_need_alarm","#buy_panel","#tair_panel");
//linkPanel("login_tair_line_need_alarm","#login_panel","#tair_panel");
/**
* 创建在pv 连接线上点击事件，弹出详细信息
* @author xiaodu
* 
*/
function createVisitOnclick(appName){
	var id = "#inter_"+appName+"_line_need_alarm";
	$(id).click(function(e){
			var mouseX = e.clientX;
       		var mouseY = e.clientY;
			showVisitMessage(appName,mouseX,mouseY);
		}
	);
	$(id).mouseover(function(){
		$(id).attr("strokeColor","#000000")
	});
	$(id).mouseout(function(){
		$(id).attr("strokeColor","green")
	});
}
/**
* 创建在前段和c 连接线上点击事件，弹出详细信息
* @author xiaodu
* 
*/
function createToCOnclick(appName,toC){
	var id = "#"+appName+"_"+toC+"_line_need_alarm";
	$(id).click(function(e){
			var mouseX = e.clientX;
       		var mouseY = e.clientY;
       		showToCMessage(appName,toC,mouseX,mouseY);
		}
	);

	$(id).mouseover(function(){
		$(id).attr("strokeColor","#000000")
	});
	$(id).mouseout(function(){
		$(id).attr("strokeColor","green")
	});
	
}

function createOtherOnclick(appName){
	var id = "#"+appName+"_other_alarm_msg";
	$(id).click(function(e){
			var mouseX = e.clientX;
   			var mouseY = e.clientY;
   			showOtherAlarmPanle(appName,mouseX,mouseY);
		}
	);
}

function createPaywayOnclick(appName){
	var id = "#tc_payway_line_need_alarm";
	$(id).click(function(e){
			var mouseX = e.clientX;
   			var mouseY = e.clientY;
   			showPaywayAlarmPanle(appName,mouseX,mouseY);
		}
	);
	$(id).mouseover(function(){
		$(id).attr("strokeColor","#000000")
	});
	$(id).mouseout(function(){
		$(id).attr("strokeColor","green")
	});
}


function showPaywayAlarmPanle(appName,mouseX,mouseY){
	var pvUrl = "<%=request.getContextPath() %>/ajax/monitor.json?action=payway&appName="+appName+"&time="+Math.random();	
	$.getJSON(pvUrl,function(json){
		$("#paywayMessageTable").empty();
		for(keyname in json){
			$("#paywayMessageTable").append("<tr><td >"+keyname+"</td><td>"+json[keyname]+"</td></tr>");	       
		}
	 	$("#paywayMessagePanel" ).dialog("option","title",(appName+"支付宝信息"));
        $("#paywayMessagePanel" ).dialog("option", "position", [mouseX,mouseY]);
        $("#paywayMessagePanel" ).dialog( 'open' );
	});	
}

function showOtherAlarmPanle(appName,mouseX,mouseY){	
	var pvUrl = "<%=request.getContextPath() %>/ajax/monitor.json?action=other&appName="+appName+"&time="+Math.random();	
	$.getJSON(pvUrl,function(json){
		$("#otherMessageTable").empty();
		for(keyname in json){
			var eleName = appName+"_"+keyname+"_need_alarm";
			if(!document.getElementById(eleName)){
				$("#otherMessageTable").append("<tr><td >"+keyname+"</td><td style='color:red;'>"+json[keyname]+"</td></tr>");
			}				
	       
		}
		 $("#otherMessagePanel" ).dialog("option","title",(appName+"其它告警信息"));
	     $("#otherMessagePanel" ).dialog("option", "position", [mouseX,mouseY]);
	     $("#otherMessagePanel" ).dialog( 'open' );
	});			
}
/**
 * 接口中的名称和应用名称的对照转换
 */
function getAppName(c){
	var toC = c;
	if(c=="item"){
		toC = "ic";
	}
	if(c=="shopservice"){
		toC = "shopcenter";
	}
	if(c=="uic"){
		toC = "tbuic";
	}
	if(c=="tc"){
		toC = "tc";
	}
	return toC;
}
/**
* 接口中的名称和应用名称的对照转换
*/
function getInterfaceName(c){
	var toC = c;
	if(c=="ic"){
		toC = "item";
	}
	if(c=="shopcenter"){
		toC = "shopservice";
	}
	if(c=="tbuic"){
		toC = "uic";
	}
	if(c=="tc"){
		toC = "tc";
	}
	return toC;
}

/**
* 显示连接线上的前段和C的接口详细信息
* @author xiaodu
* 
*/
function showToCMessage(appName,toC,mouseX,mouseY){
	var tocAppName = toC;
	toC = getInterfaceName(toC);
	var pvUrl = "<%=request.getContextPath() %>/ajax/monitor.json?action=toC&appName="+appName+"&toC="+toC+"&time="+Math.random();
		$.getJSON(pvUrl,function(json){		
			$("#toCMessageTable").empty();
			for(keyName in json){
				var name1 = parseKeyName(json[keyName].name1);
				var name2 = parseKeyName(json[keyName].name2);
				//alert(appName+"_"+name2+"_need_alarm");
				var m = "<tr><td >"+keyName+"</td><td id='"+appName+"_"+name1+"_need_alarm' style='color:green;'>"+json[keyName].value1+"&nbsp;<a href='<%=request.getContextPath() %>/alarm/add_alarm_key.jsp?keyName="+json[keyName].name1+"&keyId="+json[keyName].keyId1+"&appName="+appName+"' title='将此key加入告警'>加</a></td><td id='"+appName+"_"+name2+"_need_alarm' style='color:green;'>"+json[keyName].value2+"&nbsp;<a href='<%=request.getContextPath() %>/alarm/add_alarm_key.jsp?keyName="+json[keyName].name2+"&keyId="+json[keyName].keyId2+"&appName="+appName+"' title='将此key加入告警'>加</a></td></tr>";
				$("#toCMessageTable").append(m);
			}
			$("#toCMessagePanel" ).dialog("option","title",(appName+"调用"+tocAppName));
	       // $("#toCMessagePanel" ).dialog("option", "position", [mouseX,mouseY]);
	        $("#toCMessagePanel" ).dialog( 'open' );

		})
}

/**
* 显示连接线上的搜索详细信息
* @author xiaodu
* 
*/
function showSearchMessage(appName,mouseX,mouseY){
	var pvUrl = "<%=request.getContextPath() %>/ajax/monitor.json?action=search&appName="+appName+"&time="+Math.random();
		$.getJSON(pvUrl,function(json){		
			$("#toCMessageTable").empty();
			for(keyName in json){
				var name1 = (json[keyName].keyId1);
				var name2 = (json[keyName].keyId2);
				//alert(appName+"_"+name2+"_need_alarm");
				var m = "<tr><td >"+keyName+"</td><td id='"+appName+"_"+name1+"_need_alarm' style='color:green;'>"+json[keyName].value1+"&nbsp;<a href='<%=request.getContextPath() %>/alarm/add_alarm_key.jsp?keyName="+json[keyName].name1+"&keyId="+name1+"&appName="+appName+"' title='将此key加入告警'>加</a></td><td id='"+appName+"_"+name2+"_need_alarm' style='color:green;'>"+json[keyName].value2+"&nbsp;<a href='<%=request.getContextPath() %>/alarm/add_alarm_key.jsp?keyName="+json[keyName].name2+"&keyId="+name2+"&appName="+appName+"' title='将此key加入告警'>加</a></td></tr>";
				$("#toCMessageTable").append(m);
			}
			$("#toCMessagePanel" ).dialog("option","title",(appName+"调用搜索引擎信息"));
	        //$("#toCMessagePanel" ).dialog("option", "position", [mouseX,mouseY]);
	        $("#toCMessagePanel" ).dialog( 'open' );

		})
}


function createProviderOnclick(appName){
	var id = "#"+appName+"_ProviderDetail";
	$(id).click(function(e){
			var mouseX = e.clientX;
   			var mouseY = e.clientY;
   			showProviderMessage(appName,mouseX,mouseY);
		}
	);
}

/**
* 显示连接线上的C的所有接口信息
* @author xiaodu
* 
*/
function showProviderMessage(appName,mouseX,mouseY){
	var pvUrl = "<%=request.getContextPath() %>/ajax/monitor.json?action=Provider&appName="+appName+"&time="+Math.random();
		$.getJSON(pvUrl,function(json){		
			$("#toCMessageTable").empty();
			for(keyName in json){
				var name1 = (json[keyName].keyId1);
				var name2 = (json[keyName].keyId2);
				//alert(appName+"_"+name2+"_need_alarm");
				var m = "<tr><td >"+keyName+"</td><td id='"+appName+"_"+name1+"_need_alarm' style='color:green;'>"+json[keyName].value1+"&nbsp;<a href='<%=request.getContextPath() %>/alarm/add_alarm_key.jsp?keyName="+json[keyName].name1+"&keyId="+name1+"&appName="+appName+"' title='将此key加入告警'>加</a></td><td id='"+appName+"_"+name2+"_need_alarm' style='color:green;'>"+json[keyName].value2+"&nbsp;<a href='<%=request.getContextPath() %>/alarm/add_alarm_key.jsp?keyName="+json[keyName].name2+"&keyId="+name2+"&appName="+appName+"' title='将此key加入告警'>加</a></td></tr>";
				$("#toCMessageTable").append(m);
			}
			$("#toCMessagePanel" ).dialog("option","title",(appName+"接口信息"));
	        //$("#toCMessagePanel" ).dialog("option", "position", [mouseX,mouseY]);
	        $("#toCMessagePanel" ).dialog( 'open' );

		})
}
/**
* 创建在前段和c 连接线上点击事件，弹出详细信息
* @author xiaodu
* 
*/
function createSearchOnclick(appName){
	var id = "#"+appName+"_searchengine_line_need_alarm";
	$(id).click(function(e){
			var mouseX = e.clientX;
       		var mouseY = e.clientY;
       		showSearchMessage(appName,mouseX,mouseY);
		}
	);

	$(id).mouseover(function(){
		$(id).attr("strokeColor","#000000")
	});
	$(id).mouseout(function(){
		$(id).attr("strokeColor","green")
	});
	
}
/**
* 创建在 应用panel 上的信息点击 弹出详细信息
* @author xiaodu
* 
*/
function createKeyDetailOnclick(){

	var apps = ["list","shopsystem","item","trademgr","buy","login","ic","tc","tbuic","shopcenter"];
	$.each(apps,function(i,appName){		
		$("#"+appName+"_CPU_text").click(function(){getKeyDetail(appName,"3113","CPU")});
		$("#"+appName+"_LOAD_text").click(function(){getKeyDetail(appName,"944","LOAD")});
		$("#"+appName+"_PV_VISIT_COUNTTIMES_text").click(function(){getKeyDetail(appName,"1220","PV_VISIT_COUNTTIMES")});
		$("#"+appName+"_PV_REST_AVERAGEUSERTIMES_text").click(function(){getKeyDetail(appName,"1186","PV_REST_AVERAGEUSERTIMES")});
		});

}
/**
* 显示连接线上的pv 信息
* @author xiaodu
* 
*/
function showVisitMessage(appName,mouseX,mouseY){
	var pvUrl = "<%=request.getContextPath() %>/ajax/monitor.json?action=pv&appName="+appName+"&time="+Math.random();
		$.getJSON(pvUrl,function(json){		
			var pvVisit = json["PV_VISIT_COUNTTIMES"];
			var pvRest = json["PV_REST_AVERAGEUSERTIMES"];
			var qps = json["qps"];
			$("#visitMessageTable").empty();
			$("#visitMessageTable").append("<tr><td id='"+appName+"_PV_VISIT_COUNTTIMES_need_alarm_1'>"+pvVisit+"</td><td id='"+appName+"_qps_need_alarm' style='color:green;'>"+qps+"</td><td id='"+appName+"_PV_REST_AVERAGEUSERTIMES_need_alarm' style='color:green;'>"+pvRest+"</td></tr>");
							
	        $("#visitMessagePanel" ).dialog("option","title",(appName+"流量信息"));
	        $("#visitMessagePanel" ).dialog("option", "position", [mouseX,mouseY]);
	        $("#visitMessagePanel" ).dialog( 'open' );

		})
}


function initMessagePanel(){
	$.ui.dialog.defaults.bgiframe = true;		
	$("#visitMessagePanel").dialog({autoOpen: false, resizable: false ,zIndex:13001});
	$("#toCMessagePanel").dialog({autoOpen: false, resizable: true ,zIndex:13001,width:800,close: function(event, ui){$("#toCMessageTable").empty();}});
	$("#otherMessagePanel").dialog({autoOpen: false, resizable: true ,zIndex:13001,width:800});
	$("#paywayMessagePanel").dialog({autoOpen: false, resizable: true ,zIndex:13001,width:300});
	$("#dialog_report").dialog({
		bgiframe: true,
		height: 500,
		width:840,
		modal: true,
		draggable:true,
		resizable:false,
		autoOpen:false
	});


	

	createKeyDetailOnclick();
	
	createVisitOnclick("list");
	createVisitOnclick("shopsystem");
	createVisitOnclick("item");
	createVisitOnclick("trademgr");
	createVisitOnclick("buy");
	createVisitOnclick("login");
	
	createToCOnclick("buy","tbuic");
	createToCOnclick("buy","ic");
	createToCOnclick("buy","shopcenter");
	createToCOnclick("buy","tc");
	createToCOnclick("trademgr","tbuic");
	createToCOnclick("trademgr","ic");
	createToCOnclick("trademgr","shopcenter");
	createToCOnclick("trademgr","tc");
	createToCOnclick("shopsystem","tbuic");
	createToCOnclick("shopsystem","ic");
	createToCOnclick("shopsystem","shopcenter");	
	createToCOnclick("item","tbuic");
	createToCOnclick("item","ic");
	createToCOnclick("item","tc");
	createToCOnclick("item","shopcenter");
	createToCOnclick("login","shopcenter");
	createToCOnclick("login","tbuic");	


	createOtherOnclick("list");
	createOtherOnclick("shopsystem");
	createOtherOnclick("item");
	createOtherOnclick("trademgr");
	createOtherOnclick("buy");
	createOtherOnclick("login");
	createOtherOnclick("tbuic");
	createOtherOnclick("ic");
	createOtherOnclick("shopcenter");
	createOtherOnclick("tc");
	

	createSearchOnclick("list");
	createSearchOnclick("ic");
	createSearchOnclick("shopsystem");

	createProviderOnclick("ic");
	createProviderOnclick("tc");
	createProviderOnclick("tbuic");
	createProviderOnclick("shopcenter");

	createPaywayOnclick("tc");
}

function intervalShowVisitMessage(appName){		
	var from = $("#inter_"+appName+"_line").attr("from")+"";
	var tmp = from.split(",");
	var fromx = tmp[0].replace("pt","");
	var fromy = tmp[1].replace("pt","");		
	var to = $("#inter_"+appName+"_line").attr("to")+"";
	tmp = to.split(",");
	var tox = tmp[0].replace("pt","");
	var toy = tmp[1].replace("pt","");
	var x = Math.abs(fromx-tox)/2+160+Math.min(fromx,tox);
	var y = Math.abs(fromy-toy)/2+20+Math.min(fromy,toy);
	showVisitMessage(appName,x,y);
}



/**
* 查看key 的详细信息
* @author xiaodu
* 
*/
function getKeyDetail(appName,keyId,desc){
	var detailUrl = "<%=request.getContextPath() %>/time/key_detail_time.jsp?keyId="+keyId+"&appName="+appName+"&aimName="+desc+"&time="+Math.random();
	$("#iframe_report").attr("src",detailUrl);
	$("#dialog_report").dialog("open")
}


var alarmMap = {};
var OtherAlarmMap = {};
function showOtherAlarm(){
	for(appName in OtherAlarmMap){
		if(OtherAlarmMap[appName] == 1){
			$("#"+appName+"_other_alarm_msg").css("display","block");
			var width = $("#"+appName+"_panel").width();
			$("#"+appName+"_other_alarm_msg").css("left",width);
			$("#"+appName+"_other_alarm_msg").css("top",0);

			alarmPanel(appName+"_panel","div","#000000")
		}else{
			$("#"+appName+"_other_alarm_msg").css("display","none");
			alarmPanel(appName+"_panel","div","green")
		}

	}
}

function clearOtherAlarmMap(){

	for(appName in OtherAlarmMap){
		OtherAlarmMap[appName] = 0;
	}

	
}


function changeColor(id,type,color){	
	if(document.getElementById(id)){
		if(type == "line"){			
			$("#"+id).attr("strokeColor",color);
		}else if(type == "div"){
			$("#"+id).css("backgroundColor",color);
		}else if(type == "span"){	
			$("#"+id).css("color",color);
		}else {
			$("#"+id).css("color",color);		
		}
	}
		
}




function alarmPanel(eleId,eletype,color){
	if(color != "green"){		
		//window.setTimeout("changeColor('"+eleId+"','"+eletype+"','red')",1000);	
		//window.setTimeout("changeColor('"+eleId+"','"+eletype+"','#000000')",2000);	
		changeColor(eleId,eletype,'red');
		
		if(eleId.indexOf("PV_VISIT_COUNTTIMES")>=0){
			//window.setTimeout("changeColor('"+eleId+"_1','"+eletype+"','red')",1000);	
			//window.setTimeout("changeColor('"+eleId+"_1','"+eletype+"','#000000')",2000);	
			changeColor(eleId+'_1',eletype,'red');
		}
				
	}else{
		changeColor(eleId,eletype,"green");
		if(eleId.indexOf("PV_VISIT_COUNTTIMES")>=0){//PV_VISIT_COUNTTIMES 有两个空间需要显示
			changeColor(eleId+"_1",eletype,"green");
		}
	}	
}
/**
 * 将hsfkey的名称缩小
 */
function parseKeyName(name){	
	var newName = name.replace(/[\.\:\?]/g,"");	
	return 	newName;	
}
/**
* 这个function 的作用为：在前段调用C应用的接口是否告警，
* 如果存在告警，就需要将连接线标记成红色
*/
function checkLineNeedAlarm(appName,keyName,alarmFlag){
	var tmp = keyName.split("_");
	if(tmp.length>1){
		if("HSF-Consumer" == tmp[1]){
			var className = tmp[2];	
			var _pack = className.split("\.");
			var toC = _pack[2];
			var toAppName = getAppName(toC);
			var lineId = appName+"_"+toAppName+"_line_need_alarm";	
			alarmMap[lineId] = alarmFlag;
			return true;
		}
		if("SearchEngine" == tmp[1]){			
			var lineId = appName+"_searchengine_line_need_alarm";	
			alarmMap[lineId] = alarmFlag;
			return true;
		}
		
		if("NAGIOSMAIDIAN_Payway" == keyName){			
			var lineId = "tc_payway_line_need_alarm";	
			alarmMap[lineId] = alarmFlag;
			return true;
		}
	}
	return false;	

}

function checkPvLineAlarm(appName,keyName,alarmFlag){
	if("PV_VISIT_COUNTTIMES" == keyName){			
		var lineId = "inter_"+appName+"_line_need_alarm";		
		alarmMap[lineId] = alarmFlag;
	}
}


/**
 * 将所有需要显示的数据查询出来并将这个值赋给控件
 */
function showAllValue(){
	var pvUrl = "<%=request.getContextPath() %>/ajax/monitor.json?action=all&time="+Math.random();
	$.getJSON(pvUrl,function(json){
		for( app in json){
			var appName = app;
			var appMap = json[app];
			for(key in appMap){
				var value = appMap[key];
				var id = appName+"_"+parseKeyName(key)+"_need_alarm";
				var _tmp_id = appName+"_"+value.keyId+"_need_alarm";
				alarmMap[id] = value.alarmFlag;
				alarmMap[_tmp_id] = value.alarmFlag;
				if(!document.getElementById(id)){
					if(!checkLineNeedAlarm(appName,key,value.alarmFlag)){
						OtherAlarmMap[appName] = value.alarmFlag;
					}
				}
				checkPvLineAlarm(appName,key,value.alarmFlag);
				$("#"+id).text(value.value);
				$("#"+_tmp_id).text(value.value);												
			}
		}
		_intervalCall();
	});
}




/**
 * 所有需要告警的控件查询并标记告警
 */
function findAllNeedAlarmEle(){
	for(id in alarmMap){
		if(document.getElementById(id)){
			if(alarmMap[id] == 1){
				alarmPanel(id,document.getElementById(id).tagName.toLowerCase(),"#000000");
			}else{
				alarmPanel(id,document.getElementById(id).tagName.toLowerCase(),"green");
			}
		}
	}	
}


function findCurrentAlarm(){	
	var pvUrl = "<%=request.getContextPath() %>/ajax/monitor.json?action=getCurrentAlarm&time="+Math.random();
	$.getJSON(pvUrl,function(json){
		$("#alarm_table_1").empty();
		$("#alarm_table_2").empty();
		for(var i=0;i<json.length&&i<5;i++){
			var simpleAlarm = json[i];
			$("#alarm_table_1").append("<tr ><td align=\"center\">"+simpleAlarm.appName+"</td><td align=\"center\">"+simpleAlarm.time+"</td><td align=\"center\">"+simpleAlarm.alarmMsg+"</td></tr>");		
		}
		for(var i=5;i<json.length;i++){
			var simpleAlarm = json[i];
			$("#alarm_table_2").append("<tr ><td align=\"center\">"+simpleAlarm.appName+"</td><td align=\"center\">"+simpleAlarm.time+"</td><td align=\"center\">"+simpleAlarm.alarmMsg+"</td></tr>");		
		}
	});
}


function findPvNUm(){	
	var pvUrl = "<%=request.getContextPath() %>/ajax/monitor.json?action=pvNum&time="+Math.random();
	$.getJSON(pvUrl,function(json){
		for(app in json){
			var k = Math.round(json[app]/500);
			if(k == 0){
				k=1;
			}
			if(document.getElementById(app+"_line_need_alarm")){				
				document.getElementById(app+"_line_need_alarm").strokeWeight = k;	
			}	
		}
				
	});
}


function _intervalCall(){	
	findAllNeedAlarmEle();
	showOtherAlarm();
	clearOtherAlarmMap();
	findCurrentAlarm();
	findPvNUm();
	window.setTimeout("showAllValue()",3000);
}






initMessagePanel();

showAllValue();

function showTime(){
	var dateObj = new Date()
	var day = dateObj.getDay();
	var hours = dateObj.getHours();
	
	var minutes = dateObj.getMinutes();
	var seconds = dateObj.getSeconds();
	$("#time_panel").text("["+(hours<10?("0"+hours):hours)+":"+(minutes<10?("0"+minutes):minutes)+":"+(seconds<10?("0"+seconds):seconds)+"]");
	window.setTimeout("showTime()",1000);
}

showTime();

</script>


<jsp:include page="left.jsp"></jsp:include>

</BODY>
</HTML>