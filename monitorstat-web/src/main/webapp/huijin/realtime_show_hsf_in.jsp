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
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.po.KeyValuePo"%><html>
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
	
	String appName = request.getParameter("appName");
	if(appName==null){
		appName = "list";
	}
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
		//parent.openTime(keyId);
		window.open("../time/key_detail_time.jsp?keyId="+keyId+"&appName=" + "<%=appName%>");
		
	}
	
</script>
</head>
<body>

<%
AppInfoPo appInfopo = AppCache.get().getKey(appName);

//if(appInfopo!=null&&appInfopo.getFeature()!=null&&appInfopo.getFeature().indexOf("in")>-1){
if(true){
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000"> = IN = </font></div>
<div class="ui-dialog-content ui-widget-content">

<%	
{
Map<String,List<KeyValuePo>> outHsfKeyMap = MonitorTimeAo.get().findLikeKeyAverageByLimit5("IN_HSF-ProviderDetail",appName);
if(outHsfKeyMap.size()>0){ 
%>
<table id="IN_HSF-ProviderDetail" width="100%" border="1" class="ui-widget ui-widget-content">
	<tr class="headcon">
		<td align="left" colspan="8" width="100%"><font color="#000000" size="2">[ HSF ]</font>&nbsp;&nbsp;</td>
	</tr>	
	
	<%
	MonitorVo vo = new MonitorVo();
	
	for(Map.Entry<String,List<KeyValuePo>> entry:outHsfKeyMap.entrySet()){		
		String keyName = entry.getKey();
				
		String[] keys = keyName.split("_");									
		String hsfClassName = keys[2]+"_"+keys[3];
		List<KeyValuePo> list = entry.getValue();
		String dataValue = list.get(0).getValueStr();
		int keyId = list.get(0).getKeyId();
		//String collectTime = list.get(0).getCollectTimeStr();
		
		
		HsfPo hsf = vo.getInHsfMap().get(hsfClassName);
		if(hsf==null){
			hsf = new HsfPo();
			vo.getInHsfMap().put(hsfClassName, hsf);
			vo.getInHsfList().add(hsf);
		}
		
		String _cn = keys[2];
		if(_cn.indexOf(":")>-1){
			_cn = _cn.substring(0,_cn.lastIndexOf(":"));
			_cn = _cn.substring(_cn.lastIndexOf(".")+1,_cn.length())+keys[2].substring(keys[2].lastIndexOf(":"),keys[2].length());
		}else{
			_cn = _cn.substring(_cn.lastIndexOf(".")+1,_cn.length());
		}
		hsf.setClassName(_cn);
		hsf.setName(keys[1]);
		hsf.setMethodName(keys[3]);									
		
		if(keys[1].equals("HSF-ProviderDetail-BizException")){
			if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>-1){											
				hsf.getBizExecptionNumMap().put(keys[2], Utlitites.getLong(dataValue));
				hsf.setBizExecptionNum(Utlitites.fromatLong(dataValue));
				
			}
			
		}else if(keys[1].equals("HSF-ProviderDetail-Exception")){
			if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>-1){				
				hsf.getExecptionNumMap().put(keys[2], Utlitites.getLong(dataValue));				
				hsf.setExecptionNum(Utlitites.fromatLong(dataValue));
			}
			
		}else if(keys[1].equals("HSF-ProviderDetail")){			
			String collectTime = list.get(0).getCollectTimeStr();
			hsf.setCollectTime(collectTime);
			if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>-1){				
				hsf.getExeCountNumMap().put(keys[2], Utlitites.getLong(dataValue));				
				hsf.setExeCount(Utlitites.fromatLong(dataValue));											
				hsf.setExeCountNum(Utlitites.getLong(dataValue));
				hsf.setCountkeyId(keyId);
			}
			if(keyName.indexOf(Constants.AVERAGE_USERTIMES_FLAG)>-1){
				hsf.getAverageExeMap().put(keys[2],Utlitites.getDouble(dataValue));											
				hsf.setAverageExe(Utlitites.formatDotTwo(dataValue));
				hsf.setAverageKeyId(keyId);
			}										
			
		}
		
	}	
	%>
	<tr class="ui-widget-header ">	 
	  <td align="center" >类名</td>
	  <td align="center" >方法名</td>
	  <td  align="center">时间</td>
	  <td  align="center">执行次数</td>
	  <td  align="center">平均执行时间(ms)</td>
	  <td  align="center">Exception总数</td>
	  <td  align="center">BizException总数</td>
  	</tr>	
	<%
	Map<String, List<HsfClass>> sortOutMap = vo.getSortInHsfMap1();		
	Iterator<Map.Entry<String,List<HsfClass>>> sortOutMapit = sortOutMap.entrySet().iterator();
	while(sortOutMapit.hasNext()){
		Map.Entry<String,List<HsfClass>> aimEntry = sortOutMapit.next();
		String aim = aimEntry.getKey();
		List<HsfClass> hsfClassList = aimEntry.getValue();				
		Collections.sort(hsfClassList);
		boolean aimShow = true;
		for(HsfClass hc:hsfClassList){
			List<HsfPo> list = hc.getHsfPo();
			Collections.sort(list);					
			boolean classShow = true;					
			for(HsfPo po:list){
				String key = aim+"_"+po.getClassName()+"_"+po.getMethodName();
			%>					
			<tr>
				<td align="left" style="font-size:12px" width="200"><%=classShow?po.getClassName():" - " %></td>
				<td align="left" style="font-size:12px" width="200"><%=po.getMethodName() %></td>
				<td align="left"  style="font-size:10px" width="60"><%=po.getCollectTime() %></td>
				<td align="left"  style="font-size:10px" width="60"><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openTime(<%=po.getCountkeyId() %>)"/><%=Utlitites.fromatLong(po.getExeCount()) %>&nbsp;<a href='<%=request.getContextPath() %>/alarm/add_alarm_key.jsp?keyId=<%=po.getCountkeyId() %>&appName=<%=appName %>' title='将此key加入告警' target="_blank">加</a></td>					
				<td align="left"  style="font-size:10px" width="60"><img src="<%=request.getContextPath () %>/statics/images/report.png" onclick="openTime(<%=po.getAverageKeyId() %>)"/><%=Utlitites.formatDotTwo(po.getAverageExe()) %>&nbsp;<a href='<%=request.getContextPath() %>/alarm/add_alarm_key.jsp?keyId=<%=po.getAverageKeyId() %>&appName=<%=appName %>' title='将此key加入告警' target="_blank">加</a></td>				
				<td align="left"  style="font-size:10px" width="60"><%=po.getExecptionNum()==null?" - ":po.getExecptionNum() %></td>				
				<td align="left"  style="font-size:10px" width="60"><%=po.getBizExecptionNum()==null?" - ":po.getBizExecptionNum() %></td>
					
			</tr>					
			<%
			aimShow = false;
			classShow = false;	
			}
		classShow = true;
		}
		aimShow = true;
	}
	
	
	%>
	
</table>
<%}} %>
</div>
</div>
<%} %>

<%
	String currentHost = request.getParameter("currentHost");
%>
<iframe id="iframeC" name="iframeC" src="" width="0" height="0" style="display:none;" ></iframe>
<input id = "currentHost" type="hidden" value="<%=currentHost%>"/>
<script type="text/javascript"> 
function sethash(){
    hashH = document.documentElement.scrollHeight;
	urlC = document.getElementById("currentHost").value;
    document.getElementById("iframeC").src=urlC+"#"+hashH; 
}
window.onload=sethash;
</script>

</body>

</html>