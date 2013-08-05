<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.*"%>
<%@ page import="java.lang.Integer"%>

<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorTddlAo"%>
<%@page import="com.taobao.monitor.web.tddl.TddlPo"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.web.tddl.DBTddlData"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>TDDL 流量监控</title>
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

function goToTddl(){
	
	var searchDate = $("#datepicker").val();
	var appName = $("#appNameSelect").val();
	location.href="tddl_show.jsp?collectDay="+searchDate+"&selectAppId="+appName;
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
		selectAppId = "8";
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
	
	
	Map<String, Map<String, DBTddlData>> tddlTypeMap = MonitorTddlAo.getInstence().findTypeMapByApp(appInfopo.getOpsName(), collectDay);
	Map<String, DBTddlData> successDBTddlMap = new HashMap<String, DBTddlData>();
	Map<String, DBTddlData> timeoutDBTddlMap = new HashMap<String, DBTddlData>();
	
	%>
    <div id="selectform">
   	<select id="parentGroupSelect" onChange="groupChange(this)">	
	</select>
	<select id="appNameSelect" name="selectAppId">	
	</select>
	日期: <input type="text" id="datepicker" value="<%=collectDay%>"/>
	<button onClick="goToTddl()">查看详细信息</button>
    </div>
    <dir id="content">
    <div id="tabs">
        <ul>
            <li><a href="#tabs-1">&nbsp;&nbsp;执行成功记录&nbsp;&nbsp;</a></li>
            <li><a href="#tabs-2">&nbsp;&nbsp;执行超时记录&nbsp;&nbsp;</a></li>
        </ul>
        
        <div id="tabs-1">
        
        <%
        if (tddlTypeMap.size() > 0) {
        	 successDBTddlMap = tddlTypeMap.get("EXECUTE_A_SQL_SUCCESS");
        }
       
        for (Map.Entry<String, DBTddlData> entry : successDBTddlMap.entrySet()) {
        	String dbName = entry.getKey();
        	DBTddlData dBTddlData = entry.getValue();
        	List<TddlPo> tddlList = dBTddlData.getTopTddlList();
        	%>
        	<table width="100%" border="1">
        		<tr>
					<td width="100%">
						<h3><a style="color:#3366CC" href="javascript:showPanel('<%=dbName %>')">&nbsp;&nbsp;<%=dbName %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[总计成功：<%=Utlitites.fromatLong(dBTddlData.getSumExecuteTimes()+"") %>次 ]</a></h3>
					</td>
				</tr>
				<tr id="panel_<%=dbName %>" style="display:none">
					<td>
					<table width="100%" border="1">
						<tr align="center">
							<td width="800"> SQL </td>
							<td width="50">执行次数</td>
							<td width="50">响应时间</td>
						</tr>
					<%
					for (TddlPo po : tddlList) {
					%>
					<tr align="left">
						<%
						if (po.getSqlText().length() > 100) {
							%>
							<td height='35' title='<%=po.getSqlText() %>'><%=po.getSqlText().substring(0,100)+"..." %></td>
							<%
						} else {
							%>
							<td height='35' title='<%=po.getSqlText() %>'><%=po.getSqlText() %></td>
							<%	
						}
						%>
						<td height='35' width="70"><%=Utlitites.fromatLong(po.getExecuteSum()+"") %></td>
						<td height='35' width="40"><%=po.getTimeAverage() %></td>
					</tr>
					<%
					}
					%>
					<tr align="center">
						<td height="30" colspan="5"><a target="_blank" href="tddl_detail.jsp?opsName=<%=appInfopo.getOpsName() %>&dbName=<%=dbName %>&type=EXECUTE_A_SQL_SUCCESS&collectTime=<%=collectDay %>">显示更多...	</a></td>
					</tr>
					</table>
					</td>
				</tr>
        	</table>
        	<%
        }
        %>
        </div>
        
        <div id="tabs-2">

        <%
        if (tddlTypeMap.size() > 0) {
        	timeoutDBTddlMap = tddlTypeMap.get("EXECUTE_A_SQL_TIMEOUT");
        }
        
        for (Map.Entry<String, DBTddlData> entry : timeoutDBTddlMap.entrySet()) {
        	String dbName = entry.getKey();
        	DBTddlData dBTddlData = entry.getValue();
        	List<TddlPo> tddlList = dBTddlData.getTopTddlList();
        	%>
        	<table width="100%" border="1">
        		<tr>
					<td width="200"><h3><a style="color:#3366CC" href="javascript:showPanel('<%="_"+dbName %>')">&nbsp;&nbsp;<%=dbName %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[总计失败：<%=Utlitites.fromatLong(dBTddlData.getSumExecuteTimes()+"") %>次]</a></h3></td>
				</tr>
				<tr id="panel_<%="_"+dbName %>" style="display:none">
					<td>
					<table width="100%" border="1">
						<tr align="center">
							<td width="900px"> SQL </td>
							<td width="100px">失败次数</td>
						</tr>
					<%
					for (TddlPo po : tddlList) {
					%>
						<tr align="left">
							<%
							if (po.getSqlText().length() > 100) {
								%>
								<td height='35' title='<%=po.getSqlText() %>'><%=po.getSqlText().substring(0,100)+"..." %></td>
								<%
							} else {
								%>
								<td height='35' title='<%=po.getSqlText() %>'><%=po.getSqlText() %></td>
								<%	
							}
							%>
							
							<td  height='35'><%=Utlitites.fromatLong(po.getExecuteSum()+"") %></td>
						</tr>
					<%
					}
					%>
					<tr align="center">
						<td height="30" colspan="5"><a target="_blank" href="tddl_detail.jsp?opsName=<%=appInfopo.getOpsName() %>&dbName=<%=dbName %>&type=EXECUTE_A_SQL_TIMEOUT&collectTime=<%=collectDay %>">显示更多...	</a></td>
					</tr>
					</table>
					</td>
				</tr>
        	</table>
        	<%
        }
        %>
                
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

</body>
</html>