<%@page import="com.taobao.monitor.common.util.CspCacheTBHostInfos"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.csp.depend.web.po.AppCheckInfo"%>
<%@ page language="java" contentType="text/html; charset=GB18030"
	isELIgnored="false" pageEncoding="GB18030"%>
	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet"/>
<title>ǿ�����������ҳ</title>
</head>
<%
List<AppCheckInfo> appList = (List<AppCheckInfo>)request.getAttribute("appList");
%>
<body>
<%@ include file="../dailyheader.jsp"%>
	<table width="100%" class="table table-bordered table-striped">
		<thead>
			<tr>
				<td width="10%" style="text-align: center;">Ӧ��</td>
				<td width="10%" style="text-align: center;">����Ӧ�ø���</td>
				<td width="10%" style="text-align: center;">�����ü�����</td>
				<td width="10%" style="text-align: center;">����IP</td>
				<td width="10%" style="text-align: center;">�ϴμ��ʱ��</td>
				<td width="10%" style="text-align: center;">Ӧ����ϸ</td>
			</tr>
		</thead>
		<%
			for(AppCheckInfo info : appList) {
				%>
			<tr>
				<td width="30%" style="text-align: center;"><a href="<%=request.getContextPath() %>/dailycheck.do?method=appIndex&codeversion=<%=info%>&opsName=<%=info.getOpsName()%>"><%=info.getOpsName()%></a></td>
				<td width="10%" style="text-align: center;"><%=info.getDependNum()%></td>
				<td width="10%" style="text-align: center;"><%=info.getCheckDependAppNum()%></td>
				<td width="10%" style="text-align: center;"><%=info.getMachineIp()%></td>
				<td width="10%" style="text-align: center;">
				<%
					if(info.getLastCheckTime() == null)
						out.println("���޼���¼");
					else
						out.println(info.getLastCheckTime());
				%>
				</td>
				<td width="10%" style="text-align: center;"><a href="<%=request.getContextPath() %>/dailycheck.do?method=appIndex&codeversion=<%=selectDate%>&opsName=<%=info.getOpsName()%>">�鿴��ϸ</a></td>
			</tr>				
				<%
			}
		%>
	</table>
<div>
</div>
</body>
</html>