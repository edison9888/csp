<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorTairAo"%>
<%@page import="javax.xml.crypto.Data"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.web.tair.AllTairData"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.math.RoundingMode"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>UIC调用Tair统计</title>
</head>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<style type="text/css">
div {
	font-size: 12px;
}
#content {
	margin-top: 10px;
	margin-right: 10px;
	margin-bottom: 10px;
	margin-left: 10px;
	width:1300px
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
}
</style>
<body>
<%
request.setCharacterEncoding("gbk");

String actionType = request.getParameter("actionType");
if (actionType == null) {
	actionType = "get";
}

String groupName = request.getParameter("groupName");
if (groupName == null) {
	groupName = "group3";
}

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
String collectTime = request.getParameter("collectTime");
if (collectTime == null) {
	
	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.DAY_OF_MONTH,-1);
	collectTime = sdf.format(cal.getTime());
}

List<AllTairData> appTairList = MonitorTairAo.getInstance().findTairInfoByAction(actionType, groupName, collectTime);

%>
<div id="main">
	<jsp:include page="../head.jsp"></jsp:include>
	<div id="content">
		<table width="100%" align="center">
			<tr>
			   <td align="center"><font style="color:red" size="5">【UIC调用TOP10】</font></td>
			</tr>
			<tr>
				<td align="center"><font size="3"><%=collectTime %></font></td>
			</tr>
		</table>
		
		<table width="100%" align="center"  border="1"  class="ui-widget ui-widget-content">
			<tr align="center" class="ui-widget-header ">
				<td>应用名</td>
				<td>访问总量</td>
				<td>均值<%=getUnit(actionType) %></td>
				<td>类型</td>
				<td>分组名</td>
				<td>时间</td>
			</tr>
		<% 
			for (AllTairData tairData : appTairList) {
			%>
				<tr>
					<td height="25"><a target="_blank" style="text-decoration: underline;" href="tair_show.jsp?selectAppId=<%=tairData.getStr() %>"><%=tairData.getStr() %></a></td>
					<td><%=Utlitites.fromatLong(tairData.getSumData1()+"") %></td>
					<td><%=getAveData(actionType, tairData.getSumData1(), tairData.getSumData2()) %></td>
					<td><%=actionType %></td>
					<td><%=groupName %></td>
					<td><%=collectTime %></td>
				</tr>
			<%
			}
		%>
			<tr>
				
			</tr>
			
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