<%@page import="com.taobao.csp.depend.util.MethodUtil"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="java.util.*"%>
<%@page import=" com.taobao.csp.depend.util.JspFormatUtil"%>
<%@page import="com.taobao.monitor.common.po.CspMapKeyInfoPo"%>
<%@page import="com.taobao.monitor.common.po.ReleaseInfo"%>
<%@ page language="java" contentType="text/html; charset=GB18030"
	isELIgnored="false" pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0">
<link href="<%=request.getContextPath()%>/statics/css/bootstrap.css"
	rel="stylesheet" />
<link
	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css"
	rel="stylesheet" />
<script language="JavaScript"
	src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>

<style>
<!--
body {
	padding-bottom: 40px;
	padding-top: 60px;
}
-->
</style>
<%%>
<title>������ͼ-������Ϣҳ��</title>
<%
String appName = (String)request.getAttribute("appName");
List<ReleaseInfo> list = (List<ReleaseInfo>)request.getAttribute("list");
%>
</head>
<body>
<h2>
${beginDate}��${endDate},${opsName}�ķ�����Ϣ��
</h2>
<table class="table table-bordered table-striped">
<thead>
	<tr>
		<td>������id</td>
		<td>Ӧ����</td>
		<td>�����ƻ�ʱ��</td>
		<td>��������</td>
		<td>��������</td>
		<td>��������</td>
		<td>����ϵͳ</td>
		<td>֪ͨ����</td>
		<td>����ʱ��</td>
		<td>�������</td>
	</tr>
</thead>
<tbody>	
<%
	if(list == null) {
		out.println("��ѯ�����ݣ�");
	} else {
		for(ReleaseInfo po : list) {
			%>
			<tr>
			<td><%=po.getPlanId()%></td>
			<td><%=po.getAppName()%></td>
			<td><%=po.getPlanTime()%></td>
			<td><%=po.getPubType()%></td>
<%
			if(po.getPlanKind().equals("normal"))
				out.println("<td>����</td>");
			else
				out.println("<td>�ع�</td>");

			if(po.getPubLevel().equals("prepub"))
				out.println("<td>Ԥ��</td>");
			else if(po.getPubLevel().equals("beta"))
				out.println("<td>beta����</td>");
			else
				out.println("<td>��ʽ����</td>");
%>			
			<td><%=po.getCallSystem()%></td>
<%
			if(po.getNotifyType().equals("start"))
				out.println("<td>������ʼ</td>");
			else
				out.println("<td>��������</td>");

			if(po.getFinishTime() == null)
				out.println("<td>-</td>");
			else
				out.println("<td>" + po.getFinishTime() + "</td>");
			
			if(po.getResult() != null) {
				if(po.getResult().equals("SUC")) 
					out.println("<td>�����ɹ�</td>");
				else
					out.println("<td><strong style='color: red;'>����ʧ��</strong></td>");						
			} else {
				out.println("<td>������...</td>");
			}
%>				
			</tr>
			<%	
		}
	}
%>
</tbody>
</table>
</body>
</html>