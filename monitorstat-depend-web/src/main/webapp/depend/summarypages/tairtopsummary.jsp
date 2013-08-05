<%@ page language="java" contentType="text/html; charset=GB18030"  isELIgnored="false" 
    pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.*"%>
<%@page import="java.lang.Integer"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.common.ao.center.MonitorTairAo"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.common.po.SingleTairData"%>
<%@page import="com.taobao.monitor.common.po.AllTairData"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.math.RoundingMode"%>
<%@page import="com.taobao.monitor.common.po.TairSumData"%>
<%@page import="com.taobao.monitor.common.po.TairNamespacePo"%>
<%@page import="com.taobao.monitor.common.po.TairNamespacePo.SiteInfo"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0">
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/userjs/mainpage.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/statics/datePicket/WdatePicker.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/statics/js/swfobject.js"></script>
<title>Tair流量排行New</title>
<%
List<String> groupList = (List<String>)request.getAttribute("groupList");
if(groupList == null)
  groupList = new ArrayList<String>();
List<Map.Entry<String, TairSumData>> appTairDataMapList = (List<Map.Entry<String, TairSumData>>)request.getAttribute("appTairDataMapList");
String groupName = (String)request.getAttribute("groupName");
String topStr = (String)request.getAttribute("top");
String limitStr = (String)request.getAttribute("limit");
String sumCountInTairGroup = (String)request.getAttribute("sumCountInTairGroup");
String collectTime = (String)request.getAttribute("collectTime");
int limit = Integer.parseInt(limitStr);	
DecimalFormat df = new DecimalFormat("0.000");
%>
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
	//$("#iframe_report").attr("src","<%=request.getContextPath()%>/depend/summarypages/tair_date_chart.jsp?appName="+appName+"&groupName="+groupName+"&endCollectTime="+end);
	//$("#dialog_report").dialog("open")
}
</script>
</head>
<body>
	<%@ include file="../header.jsp"%>
		
	<div class="container">
	<span style="font-weight: bold"><h2 align="center">Tair流量排行</h2></span>
	<form class="well form-inline" action="<%=request.getContextPath()%>/show/tairconsume.do" method="get">
	  <input type="hidden" value="showTopTair" name="method">
	  <label>
	  GroupName:
		<select id="groupName" name="groupName">
			<%
				for(String tmpGroupname : groupList) {
				  String str = "";
				  if(tmpGroupname.equals(groupName))
				    str = "selected=true";
				  out.println("<option value='" + tmpGroupname + "' " + str + ">" + tmpGroupname + "</option>");
				}
			%>
		</select>	  
	  </label>
	  <label>日期: <input type="text" id="collectTime" value="${collectTime}" name="collectTime" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})" class="span2"/></label>
	  <input type="submit" value="查看Top应用" class="btn btn-success" />
	</form>
	
	<h3>
		排名前<%=topStr%>应用&nbsp;&nbsp;&nbsp;&nbsp;（<%=groupName %>分组总调用次数：<%=Utlitites.fromatLong(sumCountInTairGroup+"") %>）
	</h3>
	<%
		for (int i = 0; i < limit; i++) {
		Map.Entry<String, TairSumData> siteTairDataEntry = appTairDataMapList.get(i);
		String appName = siteTairDataEntry.getKey();
		TairSumData siteTairData = siteTairDataEntry.getValue();
		
		List<TairNamespacePo> tairNamespaceList = siteTairData.getTairNamespaceList();
	%>
	<table width="100%" class="table-bordered table-striped table-condensed" >
		<tr>
			<td width="200"><h3><a style="color:#3366CC" href="javascript:showPanel('<%=appName %>')">&nbsp;&nbsp;&nbsp;&nbsp;<%=appName %>&nbsp;&nbsp;总次数：<img src="<%=request.getContextPath () %>/statics/css/images/report.png" onClick="openKeyDetail('<%=appName %>','<%=groupName %>','<%=collectTime %>')"></img><%=Utlitites.fromatLong(siteTairData.getAppCallSum() + "") %>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;高峰QPS：<%=df.format(siteTairData.getRushQps()) %>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;高峰RT：<%=df.format(siteTairData.getRushRt()) %></a></h3></td>
		</tr>
		<tr id="panel_<%=appName %>" style="display:none">
			<td>
			<table width="890" class="table table-bordered">
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
					<td rowspan='<%=po.getSiteDataInfoMap().size() %>'><a style="text-decoration: underline;" target="_blank" href="<%=request.getContextPath()%>/show/tairconsume.do?method=gotoShowTairDetail&appName=<%=appName %>&groupName=<%=groupName %>&namespace=<%=po.getNamespace() %>&collectTime=<%=collectTime %>"><%=po.getNamespace() %></a></td>
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
					<a style="text-decoration: underline;" target="_blank"  href="<%=request.getContextPath()%>/show/tairconsume.do?method=showTairConsumeMain&opsName=<%=appName %>&showType=consume&selectDate=<%=collectTime %>">应用访问Tair详细信息</a>
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
	<!-- container -->
	<div id="dialog_report" title="Basic modal dialog">
		<iframe id="iframe_report" src="" frameborder="0" height="450" width="800" marginheight="0" marginwidth="0" scrolling="no"></iframe>
	</div>  
	
</body>
</html>