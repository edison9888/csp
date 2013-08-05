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
<%@page import="java.math.RoundingMode"%>
<%@page import="com.taobao.monitor.web.tair.TairSumData"%>
<%@page import="com.taobao.monitor.web.tair.TairNamespacePo"%>
<%@page import="com.taobao.monitor.web.tair.TairNamespacePo.SiteInfo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>Tair 流量排行</title>
<link href="../statics/css/tair/tair.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.tabs.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>

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

function goToTopDetail(){
	
	var searchDate = $("#datepicker").val();
	var groupName = $("#groupNameSelect").val();
	location.href="tair_top_new.jsp?collectTime="+searchDate+"&groupName="+groupName;
}

$(function(){
	$("#tabs").tabs();
});

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

function openKeyDetail(appName,groupName,end){
	$("#iframe_report").attr("src","tair_date_chart.jsp?appName="+appName+"&groupName="+groupName+"&endCollectTime="+end);
	$("#dialog_report").dialog("open")
}
</script>
</head>
<body>
<div id="main">
	<div id="banner"><jsp:include page="../top.jsp"></jsp:include></div>
	<%
	request.setCharacterEncoding("gbk");
	
	String top = request.getParameter("top");
	if (top == null) {
		top = "20";
	}

	// 分组名
	String groupName = request.getParameter("groupName");
	
	if (groupName == null) {
		groupName = "group3";
	}
	
	// 时间
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	String collectDay = request.getParameter("collectTime");
	Date collect = null;
	if(collectDay == null){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH,-1);
		collectDay = sdf.format(cal.getTime());
	}
	collect =sdf.parse(collectDay) ;
	
	List<String> groupList = MonitorTairAo.getInstance().findGroupList(collectDay);
	
	int flag = 0;
	for (String str : groupList) {
		if (groupName.equals(str)) {
			flag = 1;
			break;
		}
	}
	if (flag == 0) {
		out.print("页面请求出错");
		return;
	}
	
	// 此处查询出的结果并没有排序
	Map<String, TairSumData> appTairDataMap = MonitorTairAo.getInstance().findTairProviderTopApp(groupName, collectDay);
	
	// 此处查询这个group被总共调用了多少次
	long sumCountInTairGroup = 0l; 
	for(Map.Entry<String, TairSumData> entry : appTairDataMap.entrySet()) {
		sumCountInTairGroup += entry.getValue().getAppCallSum();
	}
	
	DecimalFormat df= new DecimalFormat("0.000");
	%>
    <div id="selectform2">
	   	<select id="groupNameSelect" >	
		</select>
		日期: <input type="text" id="datepicker" value="<%=collectDay%>"/>
		<button onClick="goToTopDetail()">查看  Top 应用</button>
    </div>
    <dir id="content">
   	<div id="tabs">
   	
	<ul>
		<li><a href="#tabs-1">&nbsp;&nbsp;排名前<%=top %>应用&nbsp;&nbsp;&nbsp;&nbsp;（<%=groupName %>分组总调用次数：<%=Utlitites.fromatLong(sumCountInTairGroup+"") %>）&nbsp;&nbsp;</a></li>
	</ul>
	
	<div id="tabs-1">
	
	<%
	List<Map.Entry<String, TairSumData>> appTairDataMapList = new ArrayList<Map.Entry<String, TairSumData>>(appTairDataMap.entrySet());
	Comparator<Map.Entry<String, TairSumData>> compare = new Comparator<Map.Entry<String, TairSumData>>(){
		public int compare(Map.Entry<String, TairSumData> o1, Map.Entry<String, TairSumData> o2) {
			if (o2.getValue().getAppCallSum() > o1.getValue().getAppCallSum()) {
				return 1;
			} else if (o2.getValue().getAppCallSum() == o1.getValue().getAppCallSum()) {
				return 0;
			} else {
				return -1;
			}
		}
	};
	
	Collections.sort(appTairDataMapList, compare);
	
	int limit ;
	if (appTairDataMapList.size() < Integer.parseInt(top)) {
		limit = appTairDataMapList.size();
	} else {
		limit = Integer.parseInt(top);
	}
	for (int i = 0; i < limit; i++) {
		Map.Entry<String, TairSumData> siteTairDataEntry = appTairDataMapList.get(i);
		String appName = siteTairDataEntry.getKey();
		TairSumData siteTairData = siteTairDataEntry.getValue();
		
		List<TairNamespacePo> tairNamespaceList = siteTairData.getTairNamespaceList();
	%>
	<table width="100%" border="1">
		<tr>
			<td width="200"><h3><a style="color:#3366CC" href="javascript:showPanel('<%=appName %>')">&nbsp;&nbsp;&nbsp;&nbsp;<%=appName %>&nbsp;&nbsp;总次数：<img src="<%=request.getContextPath () %>/statics/images/report.png" onClick="openKeyDetail('<%=appName %>','<%=groupName %>','<%=collectDay %>')"></img><%=Utlitites.fromatLong(siteTairData.getAppCallSum() + "") %>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;高峰QPS：<%=df.format(siteTairData.getRushQps()) %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;高峰RT：<%=df.format(siteTairData.getRushRt()) %></a></h3></td>
		</tr>
		<tr id="panel_<%=appName %>" style="display:none">
			<td>
			<table width="890" border="1">
				<tr>
					<td>命名空间</td>
					<td>机房</td>
					<td>机房总次数</td>
					<td>机房高峰QPS</td>
					<td>机房高峰RT</td>
					<td>命名空间总次数</td>
					<td>命名空间高峰QPS</td>
					<td>命名空间高峰RT</td>
				</tr>
			<%
			for (TairNamespacePo po : tairNamespaceList) {
				int flag1 = 0;
				for (Map.Entry<String, TairNamespacePo.SiteInfo> entry1 : po.getSiteDataInfoMap().entrySet()) {
			%>
				<tr>
				<%
				if (flag1 == 0) {
				%>
					<td rowspan='<%=po.getSiteDataInfoMap().size() %>'><a style="text-decoration: underline;" target="_blank" href="tair_detail.jsp?appName=<%=appName %>&groupName=<%=groupName %>&namespace=<%=po.getNamespace() %>&collectTime=<%=collectDay %>"><%=po.getNamespace() %></a></td>
				<%
				}
				%>
					<td height='25'><%=entry1.getKey() %></td>
					<td><%=Utlitites.fromatLong(entry1.getValue().siteCallNum + "") %></td>
					<td><%=entry1.getValue().siteRushQps %></td>
					<td><%=entry1.getValue().siteRushRt %></td>
				<%
				if (flag1 == 0) {
					flag1 = 1;
				%>
					<td rowspan='<%=po.getSiteDataInfoMap().size() %>'><%=Utlitites.fromatLong(po.getCallSumNum() + "") %></td>
					<td rowspan='<%=po.getSiteDataInfoMap().size() %>'><%=po.getRushQps() %></td>
					<td rowspan='<%=po.getSiteDataInfoMap().size() %>'><%=po.getRushRt() %></td>
				<%
				}
				%>
				</tr>
			<%
				}
			}
			%>
			<tr>
				<td colspan="8" height="35" align="center">
					<a style="text-decoration: underline;" target="_blank"  href="http://110.75.2.75:9999/depend/show/tairconsume.do?method=showTairConsumeMain&opsName=<%=appName %>&showType=consume&selectDate=<%=collectDay %>">应用访问Tair详细信息</a>
				</td>
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
    
<div id="dialog_report" title="Basic modal dialog">
	<iframe id="iframe_report" src="" frameborder="0" height="450" width="800" marginheight="0" marginwidth="0" scrolling="no"></iframe>
</div>  
<script type="text/javascript">
	var groupMap ={}
	
	function addGroupName(groupStr){
		if(!groupMap[groupStr]){
			groupMap[groupStr]=groupStr;
		}
	}

	function clearGroupSelect(){
		 document.getElementById("groupNameSelect").options.length=0;		
		
	}
	
	function initGroupSelect(gname){
		clearGroupSelect();
		var len = document.getElementById("groupNameSelect").options.length;
		for (name in groupMap){
			document.getElementById("groupNameSelect").options[len++]=new Option(name);
			if(name == gname){
				document.getElementById("groupNameSelect").options[len-1].selected=true;
			}
		}
	}

	<%
	for(String groupStr : groupList){
	%>
		addGroupName("<%=groupStr%>");
	<%				
	}
	%>
	
	initGroupSelect("<%=groupName%>");
</script>
    <div id="buttom"><jsp:include page="../buttom.jsp"></jsp:include></div>
</div>
</body>
</html>