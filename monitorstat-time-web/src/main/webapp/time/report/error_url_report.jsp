<%@page import="java.util.Iterator"%>
<%@page import="com.taobao.csp.time.util.Arith"%>
<%@page import="com.taobao.csp.time.web.po.UrlRtErrorCount"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link href="<%=request.getContextPath()%>/statics/css/bootstrap.css"	rel="stylesheet">
<link href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css" rel="stylesheet">
<script src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>

<title>���󳬹�1s��ͳ��</title>
</head>
<body>
<%
Map<String ,List<UrlRtErrorCount>> appMap = (Map<String ,List<UrlRtErrorCount>>)request.getAttribute("appMap");
%>

<table class="table table-striped table-condensed table-bordered">
	<tbody>
		<tr>
			<td>Ӧ������</td>
			<td>URL</td>
			<td>�ܷ�����</td>
			<td>��Ӧʱ�䳬1s������</td>
			<td>ռȫ������</td>
			<td>ռ���URL����</td>
			<td>һ���ӻ�������һ��</td>
			<td>ƽ��ÿ������</td>
		</tr>
	</tbody>
	<%
		for(Map.Entry<String ,List<UrlRtErrorCount>> entry:appMap.entrySet()){
			String appName = entry.getKey();
			List<UrlRtErrorCount> list = entry.getValue();
			
			Iterator<UrlRtErrorCount> it = list.iterator();
			while(it.hasNext()){
				UrlRtErrorCount c = it.next();
				if(c.getErrorUrlPv()<1000){
					it.remove();
				}
			}
			
			boolean first = true;
			for(UrlRtErrorCount url:list){
				double pvRate = Arith.mul(Arith.div(url.getUrlpv(), url.getAppPv(), 4), 100);
				double errorRate = Arith.mul(Arith.div(url.getErrorUrlPv(), url.getAppPv(), 4), 100);
				double errorUrlRate = Arith.mul(Arith.div(url.getErrorUrlPv(), url.getUrlpv(), 4), 100);
		%>
		<tr>
			<%if(first){ %>
			<td rowspan="<%=list.size()%>"><%=appName %></td>
			<%first = false;} %>
			<td><%=url.getUrl() %></td>
			<td><%=url.getUrlpv() %>(<%=pvRate %>%)</td>
			<td><%=url.getErrorUrlPv()%>(<%=errorRate %>%)</td>
			<td><%=errorRate %>%</td>
			<td><%=errorUrlRate %>%</td>
			<td><%=url.getMaxErrorUrlPv()%></td>
			<td><%=url.getAverageErrorUrlPv()%></td>
		</tr>
		<%	
		}}
	%>
</table>
</body>
</html>