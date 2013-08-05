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

<script type="text/javascript">
var pre = "";
function showPanel(id){
	if(pre == id){
		document.getElementById("panel_"+pre).style.display="none";
		pre = "";
	}else{
		if(pre != ""){
			document.getElementById("panel_"+pre).style.display="none";
		}
		document.getElementById("panel_"+id).style.display="block";
		pre = id;
	}
}

function goToTairDetail(){
	
	var searchDate = $("#datepicker").val();
	var appName = $("#appNameSelect").val();
	location.href="tair_show_old.jsp?collectDay="+searchDate+"&selectAppId="+appName;
}

$(function(){
	$("#tabs").tabs();
});
</script>
</head>
<body>
<div id="main">
	<div id="banner"><jsp:include page="../top.jsp"></jsp:include></div>
	<%
	request.setCharacterEncoding("gbk");
	List<AppInfoPo> appList = AppInfoAo.get().findAllAppInfo();
	// 应用
	String selectAppId = request.getParameter("selectAppId");
	if (selectAppId == null) {
		selectAppId = "1";
	}
	AppInfoPo appInfopo = null;
	appInfopo = AppCache.get().getDayAppId(Integer.parseInt(selectAppId));

	// 时间
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String collectDay = request.getParameter("collectDay");
	Date collect = null;
	if(collectDay == null){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH,-1);
		collectDay = sdf.format(cal.getTime());
	}
	collect =sdf.parse(collectDay) ;
	
	Map<String, Map<String, AllTairData>> tairMap = MonitorTairAo.getInstance().findAppTairList(appInfopo.getOpsName(), collectDay);
	Map<String, Map<String, AllTairData>> exceptionTairMap = new HashMap<String, Map<String, AllTairData>>();
	
	%>
    <div id="selectform">
   	<select id="parentGroupSelect" onChange="groupChange(this)">	
	</select>
	<select id="appNameSelect" name="selectAppId">	
	</select>
	日期: <input type="text" id="datepicker" value="<%=collectDay%>"/>
	<button onClick="goToTairDetail()">查看详细信息</button>
    </div>
    <dir id="content">
   	<div id="tabs">
	<ul>
		<li><a href="#tabs-1">&nbsp;&nbsp;按类型显示&nbsp;&nbsp;</a></li>
		<li><a href="#tabs-3">&nbsp;&nbsp;异常情况显示&nbsp;&nbsp;</a></li>
	</ul>
	<div id="tabs-1">
	<%
	
	// map<actionTyep, map<groupName, AllTairData>>
	for (Map.Entry<String, Map<String, AllTairData>> entry : tairMap.entrySet()) {
		
		String actionType = entry.getKey();
		Map<String, AllTairData> groupTairDataMap = entry.getValue();
		
		// 对actionType下得所有数据的汇总
		long data1 = 0;
		long data2 = 0;
		for (Map.Entry<String, AllTairData> entry1 : groupTairDataMap.entrySet()) {
			data1 += entry1.getValue().getSumData1();
			data2 += entry1.getValue().getSumData2();
		}
		
		if (actionType.indexOf("exception") > -1 || actionType.indexOf("error") > -1){
			exceptionTairMap.put(actionType, groupTairDataMap);
			continue;
		}
	%>
	<table width="100%" border="1">
		<tr>
			<td width="200"><h3><a style="color:#3366CC" href="javascript:showPanel('<%=actionType %>')">&nbsp;&nbsp;<%=getActionType(actionType) %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;次数:[<%=Utlitites.fromatLong(data1+"") %>]&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;均值:[<%=getAveData(actionType, data1, data2) %>]</a></h3></td>
		</tr>
		<tr id="panel_<%=actionType %>" style="display:none">
			<td>
				<table width="890" border="1">
					
				<%
				for (Map.Entry<String, AllTairData> entry1 : groupTairDataMap.entrySet()) {
					String groupName = entry1.getKey();
					AllTairData groupTairData = entry1.getValue();
					
					// map<siteName, 每行数据>
					Map<String, List<SingleTairData>> siteTairDataMap = groupTairData.getSiteListMap();
					
					// groupName下得机房名列表
					List<String> siteNameList = groupTairData.getSiteNameList();
					
					// groupName下得每个机房的数据 map<siteName, 每个机房的数据统计>
					Map<String, SingleTairData> siteDataMap = groupTairData.getSiteDataMap();
					
				%>
				
					<!-- 每个分组的第一行-->
					<tr align="center">  
						<td rowspan="<%=groupTairData.getMaxRow()+2 %>" colspan="2"><%=groupName.equals("")?"-":groupName %></td>
					<%
					for (String str : siteNameList) {
					%>
						<td colspan="3"><%=str.equals("")?"-":str %><br>机房总次数：<%=Utlitites.fromatLong(siteDataMap.get(str).getData1()+"") %><br>机房平均值：<%=getAveData(actionType,siteDataMap.get(str).getData1(),siteDataMap.get(str).getData2()) %></td>
					<%
					}
					%>
						<td>分组总次数</td>
						<td>分组均值<br><%=getUnit(actionType) %></td>
					</tr>
					<tr>
					<%
					for (String str : siteNameList) {
					%>
						<td>命名空间</td>
						<td>次数</td>
						<td>均值<%=getUnit(actionType) %></td>
					<%
					}
					%>
						<td rowspan="<%=groupTairData.getMaxRow()+1 %>"><%=Utlitites.fromatLong(groupTairData.getSumData1()+"") %></td>
						<td rowspan="<%=groupTairData.getMaxRow()+1 %>"><%=getAveData(actionType,groupTairData.getSumData1(),groupTairData.getSumData2()) %></td>
					</tr>
					
					<!-- 具体信息的行-->
					<%					
					for (int j = 0 ; j < groupTairData.getMaxRow(); j++) {
					%>
					<tr>
					<%
						for(String siteName : siteNameList) {
							try {
								SingleTairData singleData = siteTairDataMap.get(siteName).get(j);
							%>
								<td width="" height="25"><a style="text-decoration: underline;" target="_blank" href="tair_detail.jsp?appId=<%=selectAppId %>&groupName=<%=groupName %>&namespace=<%=singleData.getNamespace() %>&collectTime=<%=collectDay %>"><%=singleData.getNamespace() %></a></td>
								<td width=""><%=Utlitites.fromatLong(singleData.getData1() + "") %></td>
								<td width=""><%=getAveData(actionType, singleData.getData1(), singleData.getData2()) %></td>
							<%
							} catch (IndexOutOfBoundsException e) {
							%>
								<td>-</td>
								<td>-</td>
								<td>-</td>
							<%
							}
						}
					%>
					</tr>
					<%
					}
				}
				%>
			</table>			</td>
		</tr>
	</table>
	<%} %>
	</div>
	<div id="tabs-3">
	
		<table border="1" width="100%">
			<tr>
			<td width="150" align="center">异常类型</td>
			<td>总量</td>
		</tr>
		<%
		for (Map.Entry<String, Map<String, AllTairData>> entry : exceptionTairMap.entrySet()) {
			String actionType = entry.getKey();
			Map<String, AllTairData> exceGroupMap = entry.getValue();
			long data = 0;
		%>
		<tr>
			<td align="center"><%=actionType %></td>
		<%
			for (Map.Entry<String, AllTairData> entry1 : exceGroupMap.entrySet()){
				data += entry1.getValue().getSumData2();
			}
		%>
			<td><%=Utlitites.fromatLong(data + "") %></td>
		</tr>
	
	<%} %>	
	</table>
	</div>
	</div>
    </dir>
<script type="text/javascript">
	var groupMap ={}

	function addAppGroup(groupName,appName,appId){
			
			if(!groupMap[groupName]){
				groupMap[groupName]={};
			}
			if(!groupMap[groupName][appName]){
				groupMap[groupName][appName]=appId;
			}			
	}
	
	function groupChange(selectObj){
		var groupName = selectObj.options[selectObj.selectedIndex].value;
		var group = groupMap[groupName];
		if(group){
			clearSubSelect();
			fillSubSelect(groupName);
		}
	}
	
	function clearSubSelect(){
		 document.getElementById("appNameSelect").options.length=0;		
		
	}
	function fillSubSelect(groupName,value){
		var group = groupMap[groupName];
	
		var ops = document.getElementById("appNameSelect").options;
		var len = ops.length;
		for (name in group){
			document.getElementById("appNameSelect").options[len++]=new Option(name,group[name]);
			if(name == value){
				document.getElementById("appNameSelect").options[len-1].selected=true;
			}
		}
	}
	
	function initParentSelect(gname,gvalue){
		clearSubSelect();
		var len = document.getElementById("parentGroupSelect").options.length;
		for (name in groupMap){
			document.getElementById("parentGroupSelect").options[len++]=new Option(name,name);
			if(name == gname){
				document.getElementById("parentGroupSelect").options[len-1].selected=true;
			}
		}
				
		if(gname&&gvalue){
			fillSubSelect(gname,gvalue);
		}else{
			groupChange(document.getElementById("parentGroupSelect"));
		}
	
	}
	<%
	Map<String,List<AppInfoPo>> appInfoGroupMap = new HashMap<String,List<AppInfoPo>>();
	for(AppInfoPo app:appList){
	%>
	addAppGroup("<%=app.getGroupName()%>","<%=app.getOpsName()%>","<%=app.getAppDayId()%>")
	<%				
	}
	%>
	 initParentSelect("<%=appInfopo.getGroupName()%>","<%=appInfopo.getOpsName()%>");
</script>
    <div id="buttom"><jsp:include page="../buttom.jsp"></jsp:include></div>
</div>
<%!
private String getActionType(String str) {
	
	if (str.indexOf("hit") > -1) {
		return str.replaceAll("hit","命中率");
	} else if (str.indexOf("len") > -1) {
		return str.replaceAll("len","长度");
	} else {
		return str;
	}
}

private String getUnit(String str) {
	String unit = "";
	if (str.indexOf("hit") > -1) {
		unit = "（命中率）";
	} else if (str.indexOf("len") > -1) {
		unit = "（字节/次数）";
	} else {
		unit = "（毫秒/次数）";
	}
	return unit;
}


private String getAveData(String actionType, long data1, long data2) {
	String aveData = "";
	DecimalFormat decimal = new DecimalFormat("0.00");
	decimal.setRoundingMode(RoundingMode.HALF_UP);
	if (actionType != null) {
		if (actionType.indexOf("hit") > -1) {
			aveData = decimal.format((float)data2 / (float) data1 * 100) + "%";
		} else {
			aveData = decimal.format((float)data2 / (float) data1);
		}
	}
	return aveData;
}

%>
</body>
</html>