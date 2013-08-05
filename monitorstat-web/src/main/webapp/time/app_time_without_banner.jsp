<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>

<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>

<%@page import="com.taobao.monitor.web.vo.OtherKeyValueVo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorTimeAo"%>
<%@page import="com.taobao.monitor.web.vo.OtherHaBoLogRecord"%>
<%@page import="com.taobao.monitor.web.ao.MonitorRatingAo"%>
<%@page import="com.taobao.monitor.web.rating.RatingIndicatorValue"%>
<%@page import="com.taobao.monitor.web.rating.RatingCompute"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.baseline.db.po.KeyValueBaseLinePo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorBaseLineAo"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.common.ao.center.KeyAo"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>监控</title>
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
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/statics/fancybox/jquery.fancybox-1.3.4.css" media="screen" />

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

<%
	SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd HH:mm");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Date current = new Date();
	String collectTime1 = sdf.format(current); 
	//Date collectTime1Date = parseLogFormatDate.parse("2010-04-30 00:00:00") ; 
	//String collectTime1 = sdf.format(collectTime1Date) ; 
	String startDate = sdf.format(current)+" 00:00:00";
	String endDate = sdf.format(current)+" 23:59:59";
	
	Calendar cal = Calendar.getInstance();
	cal.setTime(current);
	cal.add(Calendar.DAY_OF_MONTH,-7);
	
	String startDate2 = sdf.format(cal.getTime())+" 00:00:00";
	String endDate2 = sdf.format(cal.getTime())+" 23:59:59";
	
	String opsName = request.getParameter("opsName");
	
	AppInfoPo appInfopo =AppInfoAo.get().getAppInfoByOpsName(opsName);
	String appName = appInfopo.getAppName();
%>
<%

List<AppInfoPo> appList = AppInfoAo.get().findAllEffectiveAppInfo();

%>
<script type="text/javascript">
	$(function(){
		//IN_HSF-ProviderDetail  OUT_PageCache  OUT_HSF-Consumer  OUT_forest  OUT_TairClient EXCEPTION
		$("#Other_Monitor tr td, #IN_HSF-ProviderDetail tr td,#OUT_PageCache tr td,#OUT_HSF-Consumer tr td,#OUT_forest tr td,#OUT_TairClient tr td,#EXCEPTION tr td").mouseover(function(){
			$(this).parent().children("td").addClass("report_on");
		})
		$("#Other_Monitor tr td, #IN_HSF-ProviderDetail tr td,#OUT_PageCache tr td,#OUT_HSF-Consumer tr td,#OUT_forest tr td,#OUT_TairClient tr td,#EXCEPTION tr td").mouseout(function(){
			$(this).parent().children("td").removeClass("report_on");
		})
	})
	function openTime(keyId){
		//$("#iframe_report").attr("src","<%=request.getContextPath () %>/time/key_time.jsp?action=time&keyId="+keyId+"&appName=<%=appName%>&collectTime1=<%=collectTime1%>");
		//$("#dialog_report").dialog("open");
		window.open("./key_detail_time.jsp?keyId="+keyId+"&appName=<%=appName%>");
	
	}
	
	$(function() {
		$("#dialog_report").dialog({
			bgiframe: true,
			height: 500,
			width:820,
			modal: true,
			draggable:true,
			resizable:false,
			autoOpen:false
		});
	});
		function reinitIframe(down){
			var pTar = null;
			if (document.getElementById){
			pTar = document.getElementById(down);
			}
			else{
			eval('pTar = ' + down + ';');
			}
			if (pTar && !window.opera){
			//begin resizing iframe
			pTar.style.display="block"
			if (pTar.contentDocument && pTar.contentDocument.body.offsetHeight){
			//ns6 syntax
			pTar.height = pTar.contentDocument.body.offsetHeight +20;
			pTar.width = pTar.contentDocument.body.scrollWidth+20;
			}
			else if (pTar.Document && pTar.Document.body.scrollHeight){
			//ie5+ syntax
			pTar.height = pTar.Document.body.scrollHeight;
			pTar.width = pTar.Document.body.scrollWidth;
			}
			} 
		}
</script>
</head>
<body>
<div >
<%
	double d = 0;
	List<RatingIndicatorValue> list = MonitorRatingAo.get().getRecentHealthIndexByAppId(appInfopo.getAppId());
	for(RatingIndicatorValue riv : list){
		d += RatingCompute.compute(riv);
	}

%>
<table width="100%">
	<tr>
		<td align="center">
			<font size="+1" color="#FF0000" face="宋体"><%=appName %>    </font> <%if(d!=0){ %><font size="+2" color="#00FF33" face="宋体">(<%=d %>分)</font><a href="<%=request.getContextPath () %>/health/health_idx_history.jsp?appId=<%=appInfopo.getAppId() %>" target="_blank">详细</a><%} %>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a id="checkIframe" target="_blank" href="<%=request.getContextPath () %>/center/rel_app_time_noAction.jsp?appId=<%=appInfopo.getAppId() %>&appName=<%=appInfopo.getAppName() %>" STYLE="text-decoration:underline"><font color='red'>-->查看应用配置</font></a> &nbsp;
		</td>
	</tr>
</table>

</div>
<%if(appInfopo!=null&&appInfopo.getFeature()!=null&&appInfopo.getFeature().indexOf("pv")>-1){  %>
<iframe src="flash_show_pv.jsp?appId=<%=appInfopo.getAppId()%>" frameborder="0" height="370" width="100%" marginheight="0" marginwidth="0" scrolling="no"></iframe>
<%}else{
	if(appInfopo.getAppName().equals("ic")||appInfopo.getAppName().equals("shopcenter")||appInfopo.getAppName().equals("tbuic")||appInfopo.getAppName().equals("tc")){
%>	
	<iframe src="flash_show_pv.jsp?appId=<%=appInfopo.getAppId()%>" frameborder="0" height="370" width="100%" marginheight="0" marginwidth="0" scrolling="no"></iframe>
<%		
	}
} %>
<iframe src="flash_show_load.jsp?appId=<%=appInfopo.getAppId()%>" frameborder="0" height="340" width="100%" marginheight="0" marginwidth="0" scrolling="no"></iframe>

<iframe id="showthread" onload="reinitIframe('showthread')" src="show_mbean.jsp?appId=<%=appInfopo.getAppId() %>" frameborder="0" height="0" width="100%" marginheight="0" marginwidth="0" scrolling="no"></iframe>
<iframe id="showexceptioniframe" onload="reinitIframe('showexceptioniframe')" src="show_exception.jsp?appId=<%=appInfopo.getAppId() %>" frameborder="0" height="0"  width="100%" marginheight="0" marginwidth="0" scrolling="no"></iframe>
<iframe id="showout" onload="reinitIframe('showout')" src="show_out.jsp?appId=<%=appInfopo.getAppId() %>" frameborder="0" height="0" width="100%" marginheight="0" marginwidth="0" scrolling="no"></iframe>
<iframe id="showin" onload="reinitIframe('showin')" src="show_in.jsp?appId=<%=appInfopo.getAppId() %>" frameborder="0" height="0" width="100%" marginheight="0" marginwidth="0" scrolling="no"></iframe>



<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000"> = 其它 = </font></div>
<div class="ui-dialog-content ui-widget-content">
<%
Map<String, OtherKeyValueVo> otherMap = MonitorTimeAo.get().findKeyValueByLikeOtherFromLimitTable(appName);

for(Map.Entry<String,OtherKeyValueVo> entry:otherMap.entrySet()){
	String typeName	 = entry.getKey();  	
	OtherKeyValueVo otherVo = entry.getValue();
%>
<table id="Other_Monitor" width="100%" border="1" class="ui-widget ui-widget-content">
	<tr class="headcon">
		<td align="left" colspan="6" width="100%"><font color="#000000" size="2">[ <%=typeName %> ]</font>&nbsp;&nbsp;</td>
	</tr>	
	<tr class="ui-widget-header ">
	  <td align="center" width="100">方法名</td>
	  <td  align="center">时间</td>
	  <td  align="center">执行次数</td>
	  <td  align="center">基线值</td>
	  <td  align="center">平均执行时间(ms)</td>
	  <td  align="center">基线值</td>	  
  	</tr>
  	<%
  	Map<String,OtherHaBoLogRecord> keyMap = otherVo.getKeyMap();
  	List<OtherHaBoLogRecord> listOther = new ArrayList<OtherHaBoLogRecord>();
  	listOther.addAll(keyMap.values());
  	Collections.sort(listOther);
  	for(OtherHaBoLogRecord record:listOther){
  		// 获取当前key当前时间的基线数据
  		Date date = record.getCollectTime();
  		String countKeyFullName ="OTHER_"+record.getTypeName()+"_"+record.getKeyName()+"_"+Constants.COUNT_TIMES_FLAG;
  		String aveKeyFullName ="OTHER_"+record.getTypeName()+"_"+record.getKeyName()+"_"+Constants.AVERAGE_USERTIMES_FLAG;
  		int countKeyId = KeyAo.get().getKeyByName(countKeyFullName)==null?0:KeyAo.get().getKeyByName(countKeyFullName).getKeyId();
  		int aveKeyId = KeyAo.get().getKeyByName(aveKeyFullName)==null?0:KeyAo.get().getKeyByName(aveKeyFullName).getKeyId();
		KeyValueBaseLinePo countKeyBaseLinePo = MonitorBaseLineAo.get().findKeyBaseValueByDate(appInfopo.getAppId(), countKeyId, date);
		KeyValueBaseLinePo aveKeyBaseLinePo = MonitorBaseLineAo.get().findKeyBaseValueByDate(appInfopo.getAppId(), aveKeyId, date);
		String countKeyBaseLineValue;
		if(countKeyBaseLinePo != null) {
			countKeyBaseLineValue = String.valueOf(countKeyBaseLinePo.getBaseLineValue());
		} else {
			countKeyBaseLineValue = "0";
		}
		String aveKeyBaseLineValue;
		if(aveKeyBaseLinePo != null) {
			aveKeyBaseLineValue = String.valueOf(aveKeyBaseLinePo.getBaseLineValue());
		} else {
			aveKeyBaseLineValue = "0";
		}
  	%>
  	<tr>
  	  <td  align="left" ><%=record.getKeyName() %></td>	 
	  <td  align="center"><%=sdf2.format(record.getCollectTime())%></td>
	  <td  align="center"><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openTime(<%=record.getExeCount()==null?" - ":record.getExeCount().getKeyId() %>)"/><%=record.getExeCount()==null?" - ":record.getExeCount().getValueStr()%>&nbsp;<a href='<%=request.getContextPath() %>/alarm/add_alarm_key.jsp?keyName=<%=record.getExeCount()==null?" - ":UrlCode.encode(record.getExeCount().getKeyName()) %>&keyId=<%=record.getExeCount()==null?" - ":record.getExeCount().getKeyId() %>&appName=<%=appName %>' title='将此key加入告警' target="_blank">加</a></td>
	  <td  align="center"><%=countKeyBaseLineValue %>&nbsp&nbsp<%=Utlitites.scale(record.getExeCount()==null?null:record.getExeCount().getValueStr(), countKeyBaseLineValue) %></td>
	  <td  align="center"><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openTime(<%=record.getAverageExeTime()==null?" - ":record.getAverageExeTime().getKeyId() %>)"/><%=record.getAverageExeTime()==null?" - ":record.getAverageExeTime().getValueStr()%>&nbsp;<a href='<%=request.getContextPath() %>/alarm/add_alarm_key.jsp?keyName=<%=record.getAverageExeTime()==null?" - ":UrlCode.encode(record.getAverageExeTime().getKeyName()) %>&keyId=<%=record.getAverageExeTime()==null?" - ":record.getAverageExeTime().getKeyId() %>&appName=<%=appName %>' title='将此key加入告警' target="_blank">加</a></td>	
  	  <td  align="center"><%=aveKeyBaseLineValue %>&nbsp&nbsp<%=Utlitites.scale(record.getAverageExeTime()==null?null:record.getAverageExeTime().getValueStr(), aveKeyBaseLineValue) %></td>
  	</tr>
  	<%} %>
</table>
<%} %>


</div>
</div>

<div id="dialog_report" title="Basic modal dialog">
	<iframe id="iframe_report" src="key_detail.jsp" frameborder="0" height="450" width="800" marginheight="0" marginwidth="0" scrolling="no"></iframe>
</div>

</body>

</html>