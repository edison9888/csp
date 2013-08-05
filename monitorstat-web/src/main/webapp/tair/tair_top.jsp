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
<%@page import="java.math.RoundingMode"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>UIC����Tair Top</title>
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
	font-family: "����";
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

String top = request.getParameter("top");
if (top == null) {
	top = "20";
}
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
String collectTime = request.getParameter("collectTime");
if (collectTime == null) {
	
	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.DAY_OF_MONTH,-1);
	collectTime = sdf.format(cal.getTime());
}

Map<String, Long> topMap = MonitorTairAo.getInstance().findTairInfoTop(actionType, collectTime, Integer.parseInt(top));

Comparator<Map.Entry<String, Long>> compare = new Comparator<Map.Entry<String, Long>>(){
	public int compare(Map.Entry<String, Long> e1,Map.Entry<String, Long> e2){
		if (e2.getValue() - e1.getValue() > 0) {
			return 1;
		} else if (e2.getValue() - e1.getValue() == 0) {
			return 0;
		} else {
			return -1;
		}
	}
};

List<Map.Entry<String, Long>> topList = new ArrayList<Map.Entry<String, Long>>(topMap.entrySet());

Collections.sort(topList, compare);

%>
<div id="main">
	<jsp:include page="../head.jsp"></jsp:include>
	<div id="content">
		<table width="100%" align="center">
			<tr>
			   <td align="center"><font style="color:red" size="5">��UIC����TOP<%=top %>��</font></td>
			</tr>
			<tr>
				<td align="center"><font size="3"><%=collectTime %></font></td>
			</tr>
		</table>
		
		<table width="100%" align="center"  border="1"  class="ui-widget ui-widget-content">
			<tr align="center" class="ui-widget-header ">
				<td>Ӧ����</td>
				<td>��������</td>
				<td>����</td>
				<td>ʱ��</td>
			</tr>
		<% 
			for (Map.Entry<String, Long> entry : topList) {
			%>
				<tr>
					<td height="25"><a target="_blank" style="text-decoration: underline;" href="tair_show.jsp?selectAppId=<%=AppCache.get().getKey(entry.getKey()).getAppId() %>"><%=entry.getKey() %></a></td>
					<td><%=Utlitites.fromatLong(entry.getValue()+"") %></td>
					<td><%=actionType %></td>
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
		return str.replaceAll("hit","������");
	} else if (str.indexOf("len") > -1) {
		return str.replaceAll("len","����");
	} else {
		return str;
	}
}

private String getUnit(String str) {
	String unit = "";
	if (str.indexOf("hit") > -1) {
		unit = "�������ʣ�";
	} else if (str.indexOf("len") > -1) {
		unit = "���ֽ�/������";
	} else {
		unit = "������/������";
	}
	return unit;
}


private String getAveData(String actionType, long data1, long data2) {
	String aveData = "";
	DecimalFormat decimal = new DecimalFormat("0.00");
	decimal.setRoundingMode(RoundingMode.HALF_UP);
	if (actionType != null) {
		if (actionType.indexOf("hit") > -1) {
			aveData = decimal.format((float)data1 / (float) data2 * 100) + "%";
		} else {
			aveData = decimal.format((float)data1 / (float) data2);
		}
	}
	return aveData;
}

%>
</body>
</html>