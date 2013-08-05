<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorTairAo"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.web.tair.TairInfoPo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.math.RoundingMode"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>Tair������ϸ</title>
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
</head>
<body>
<%
request.setCharacterEncoding("gbk");

String appId = request.getParameter("appId");
String groupName = request.getParameter("groupName");
String namespace = request.getParameter("namespace");
String collectTime = request.getParameter("collectTime");

String strPage = request.getParameter("strPage");

String appName = request.getParameter("appName");

AppInfoPo appInfopo = null;
if (appId != null) {
	appInfopo = AppCache.get().getDayAppId(Integer.parseInt(appId));
} else {
	appInfopo = AppCache.get().getOpsName(appName);
}

if (appInfopo == null) {
	out.print("�����Ӧ�ò���ȷ��");
}

Map<String, List<TairInfoPo>> ipTairMap = MonitorTairAo.getInstance().findTairInfoByNamespace(appInfopo.getOpsName(),groupName,namespace,collectTime);



%>

<div id="main">
	<jsp:include page="../head.jsp"></jsp:include>
	<div id="content">
		<table width="100%" align="center">
			<tr>
			   <td align="center"><font style="color:red" size="5">��<%=appInfopo.getOpsName()%>��</font></td>
			</tr>
		</table>
		
		<table width="100%" align="center"  border="1"  class="ui-widget ui-widget-content">
			<tr align="center" class="ui-widget-header ">
				<td>Tair����IP</td>
				<td>����</td>
				<td>����</td>
				<td>��ֵ</td>
				<td>����</td>
				<td>�����ռ�</td>
				<td>������</td>

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