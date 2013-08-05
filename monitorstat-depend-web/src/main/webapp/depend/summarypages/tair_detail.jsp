<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.ao.center.MonitorTairAo"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.TairInfoPo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.math.RoundingMode"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/userjs/mainpage.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/statics/datePicket/WdatePicker.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/statics/js/swfobject.js"></script>
<title>Tair流量详细</title>
</head>
<body>
	<%@ include file="../header.jsp"%>
<%
String groupName = (String)request.getAttribute("groupName");
String namespace = (String)request.getAttribute("namespace");
String collectTime = (String)request.getAttribute("collectTime");
String appName = (String)request.getAttribute("appName");
String errorMsg = (String)request.getAttribute("errorMsg");
AppInfoPo appInfopo = AppInfoAo.get().getAppInfoByAppName(appName);
if (errorMsg != null && !errorMsg.trim().equals("")) {
	out.print(errorMsg);	//打印异常信息
}
Map<String, List<TairInfoPo>> ipTairMap = (Map<String, List<TairInfoPo>>)request.getAttribute("ipTairMap");
%>

<div id="main">
	<div id="content">
		<table width="100%" align="center" class="table-bordered table-striped table-condensed" >
			<tr>
			   <td align="center"><font style="color:red" size="5">应用：<%=appInfopo.getOpsName()%>，时间：<%=collectTime%></font></td>
			   <td align="center" colspan="3"><font size="5">groupName:<%=groupName%>&nbsp;&nbsp;
			   namespace:<%=namespace%>&nbsp;&nbsp;</font></td>
			</tr>
		</table>
		
		<table width="100%" align="center" class="table table-bordered" >
			<tr align="center" class="ui-widget-header ">
				<td>Tair主机IP</td>
				<td>类型</td>
				<td>次数</td>
				<td>均值</td>
				<td>分组</td>
				<td>命名空间</td>
				<td>机房名</td>

			</tr>
		<%
			for (Map.Entry<String, List<TairInfoPo>> entry : ipTairMap.entrySet()) {
				String hostIp = entry.getKey();
				List<TairInfoPo> ipTairList = entry.getValue();
				boolean flag = true;
				for (TairInfoPo tairPo : ipTairList) {
				%>
				<tr>
				<%
					if (flag) {
					%>
						<td rowspan="<%=ipTairList.size() %>"><%=hostIp %></td>
					<%
						flag = false;
					}
				%>
					<td><%=getActionType(tairPo.getActionType()) %></td>
					<td><%=Utlitites.fromatLong(tairPo.getData1()+"") %></td>
					<td><%=getAveData(tairPo.getActionType(),tairPo.getData1(),tairPo.getData2()) %>&nbsp;<%=getUnit(tairPo.getActionType()) %></td>
					<td><%=tairPo.getGroupName() %></td>
					<td><%=tairPo.getNameSpace() %></td>
					<td><%=tairPo.getSiteName() %></td>
				</tr>
				<%
				}
			}
		%>
		</table>
	</div>
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
			aveData = decimal.format((float) data2 / (float)data1 * 100) + "%";
		} else {
			aveData = decimal.format((float)data2 / (float) data1);
		}
	}
	return aveData;
}

%>
</body>
</html>