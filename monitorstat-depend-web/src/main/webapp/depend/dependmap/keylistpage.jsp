<%@page import="com.taobao.csp.depend.util.MethodUtil"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="java.util.*"%>
<%@page import=" com.taobao.csp.depend.util.JspFormatUtil"%>
<%@page import="com.taobao.monitor.common.po.CspMapKeyInfoPo"%>
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
<%%>
<title>������ͼ��ҳ</title>
<%
	String appName = (String)request.getAttribute("appName");
	List<CspMapKeyInfoPo> keyList = (List<CspMapKeyInfoPo>)request.getAttribute("keyList");
%>
</head>
<body>
<table class="table table-bordered table-striped">
<thead>
	<tr>
		<td width="50%">key����</td>
		<td>ռ��������</td>
		<td>key�ļ���</td>
		<td>����״̬</td>
		<td>key��Դ</td>
		<td>����</td>
		<td>������</td>
		<td>����ʱ��</td>
	</tr>
</thead>
<tbody>	
<%
	if(keyList == null) {
		out.println("��ѯ�����ݣ�");
	} else {
		String[] rowClass = new String[]{"error","warning","success"};
		for(CspMapKeyInfoPo po : keyList) {
			int keyLevel = po.getKeyLevel();
			if(keyLevel > 2 || keyLevel < 0)
				keyLevel = 2;
			%>
			<tr class="<%=rowClass[keyLevel]%>">
				<td><%=po.getKeyname()%>&nbsp;
				<a href="<%=request.getContextPath()%>/show/dependmap.do?method=gotoMeDependKeyPage&appName=<%=appName%>&keyName=<%=po.getKeyname()%>" target="_blank;">������</a>&nbsp;
				<a href="<%=request.getContextPath()%>/show/dependmap.do?method=gotoMeDependKeyPage&appName=<%=appName%>&keyName=<%=po.getKeyname()%>" target="_blank;">������</a>
				</td>
				<td><%=po.getRate()%></td>
				<td align="center"><%=JspFormatUtil.getKeyLevel(po.getKeyLevel())%></td>
				<td align="center"><%=JspFormatUtil.getControlString(po.getControlType())%></td>
				<td align="center"><%=JspFormatUtil.getKeyStatusString(po.getKeyStatus())%></td>
				<td><a href="#" onclick="addToBlack(<%=po.getId()%>)">��ӵ�������</a></td>
				<td><%=po.getUpdateBy()%></td>
				<td><%=po.getUpdateTime()%></td>	
			</tr>
			<%	
		}
	}
%>
</tbody>
</table>
</body>
</html>