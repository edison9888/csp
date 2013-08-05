<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.csp.time.util.DataUtil"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Collection"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.taobao.csp.time.util.Arith"%>
<%@page import="com.taobao.csp.time.web.po.UrlRtErrorCount"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
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
<title>��Ӧʱ������ͳ��</title>
<script>
	$(function() {
		$( "#time1" ).datepicker();
		$( "#time1" ).datepicker("option", "dateFormat", "yy-mm-dd");
		$( "#time1" ).datepicker( "setDate" , "${time }");
		$( "#time2" ).datepicker();
		$( "#time2" ).datepicker("option", "dateFormat", "yy-mm-dd");
		$( "#time2" ).datepicker( "setDate" , "${compareTime }");
	});
	</script>
</head>
<body>
<%
List<UrlRtErrorCount> urlrtList = (List<UrlRtErrorCount>)request.getAttribute("urlrtList");
Map<String, UrlRtErrorCount> mapCompare = (Map<String, UrlRtErrorCount>)request.getAttribute("mapCompare");
if(mapCompare == null)
	mapCompare = new HashMap<String, UrlRtErrorCount>();

Collections.sort(urlrtList, new Comparator<UrlRtErrorCount>(){
	 public int compare(UrlRtErrorCount o1, UrlRtErrorCount o2){
		  if(o1.getUrlpv()>o2.getUrlpv()){
			  return -1;
		  }else{
			  return 1;
		  }
	  }
});

%>
<div class="row-fluid">
	<div class="span12">
	<form action="<%=request.getContextPath()%>/app/report.do" method="get">
		<input type="hidden" name="method"  value="reportUrlRTDistribution">
		<input type="hidden" name="appName"  value="${appName }">
		����:<input id="time1" type="text" name="time" value="${time}">�Ա�����:<input  id="time2"  type="text" name="compareTime" value="${compareTime}"><input type="submit" value="�ύ">
	</form>
</div>
	<div class="span12">
	<table width="100%" >
		<tr>
			<td align="center">
				<h5>Ӧ��URL��Ӧʱ��ֲ����</h5>
				<h5>��ȡʱ��Ϊ:${time},�Ա�ʱ��Ϊ${compareTime}</h5>
				<h5>���ýӿڱ���������ЩURLû����ͳ���ڣ����Կ���û������</h5>
			</td>
		</tr>
	</table>
</div>	
	<div class="span12">
	<table  class="table table-striped table-condensed table-bordered">
		<thead>
			<tr>
				<td width="100" >URL</td>
				<td width="100" >URL�ܷ�����</td>
				<td width="100" >ռӦ�õı���</td>
				<td width="820">URL��Ӧʱ��ֲ�</td>
				<td width="300">���ýӿڱ���</td>
			</tr>
		</thead>
		<tbody>
			<%
			for(UrlRtErrorCount rt:urlrtList){
				UrlRtErrorCount rtCompare = mapCompare.get(rt.getUrl());
				if(rtCompare == null)
					rtCompare = new UrlRtErrorCount();
			%>
			<tr>
				<td><%=rt.getUrl() %></td>
				<td><%=Utlitites.fromatLong(rt.getUrlpv()+"")%>(<%=Utlitites.scale(rt.getUrlpv(),rtCompare.getUrlpv()) %>)</td>
				<td><%=DataUtil.rate(rt.getUrlpv(), rt.getAppPv())%>%</td>
				<td align="center"  width="800" >
						<table width="800"  >
						<tr>
							<td width="<%=(Arith.div(rt.getUrlRt100Pv(),  rt.getUrlpv(), 4)*800) %>" style="background:#00FF00" title="��Ӧʱ��С��100ms" rel="popover" data-content="<table class='table table-striped table-condensed table-bordered'><tr><td>����</td><td>ռURL����</td><td>ռȫ������</td></tr><tr><td><%=Utlitites.fromatLong(rt.getUrlRt100Pv()+"")%></td><td><%=(DataUtil.rate(rt.getUrlRt100Pv(), rt.getUrlpv())) %>%</td><td><%=(DataUtil.rate(rt.getUrlRt100Pv(), rt.getAppPv())) %>%</td></tr></table>">&nbsp;</td>
							<td width="<%=(Arith.div(rt.getUrlRt500pv(),  rt.getUrlpv(), 4)*800) %>" style="background:#6633FF" title="��Ӧʱ��100-500ms"  rel="popover" data-content="<table class='table table-striped table-condensed table-bordered'><tr><td>����</td><td>ռURL����</td><td>ռȫ������</td></tr><tr><td><%=Utlitites.fromatLong(rt.getUrlRt500pv()+"")%></td><td><%=(DataUtil.rate(rt.getUrlRt500pv(), rt.getUrlpv())) %>%</td><td><%=(DataUtil.rate(rt.getUrlRt500pv(), rt.getAppPv())) %>%</td></tr></table>">&nbsp;</td>
							<td width="<%=(Arith.div(rt.getUrlRt1000pv(),  rt.getUrlpv(), 4)*800) %>" style=" background:#FFFF66" title="��Ӧʱ��500-1000ms"  rel="popover" data-content="<table class='table table-striped table-condensed table-bordered'><tr><td>����</td><td>ռURL����</td><td>ռȫ������</td></tr><tr><td><%=Utlitites.fromatLong(rt.getUrlRt1000pv()+"")%></td><td><%=(DataUtil.rate(rt.getUrlRt1000pv(), rt.getUrlpv())) %>%</td><td><%=(DataUtil.rate(rt.getUrlRt1000pv(), rt.getAppPv())) %>%</td></tr></table>">&nbsp;</td>
							<td width="<%=(Arith.div(rt.getErrorUrlPv(),  rt.getUrlpv(), 4)*800) %>" style="background:#FF0000" title="��Ӧʱ�����1000ms"  rel="popover" data-content="<table class='table table-striped table-condensed table-bordered'><tr><td>����</td><td>ռURL����</td><td>ռȫ������</td></tr><tr><td><%=Utlitites.fromatLong(rt.getErrorUrlPv()+"")%></td><td><%=(DataUtil.rate(rt.getErrorUrlPv(), rt.getUrlpv())) %>%</td><td><%=(DataUtil.rate(rt.getErrorUrlPv(), rt.getAppPv())) %>%</td></tr></table>">&nbsp;</td>
						</tr>
					</table>
						<table width="800">
							<tr>
								<td width="200"><%=Utlitites.fromatLong(rt.getUrlRt100Pv()+"")%>(<%=Utlitites.scale(rt.getUrlRt100Pv(),rtCompare.getUrlRt100Pv()) %>)</td>
								<td width="200"><%=Utlitites.fromatLong(rt.getUrlRt500pv()+"")%>(<%=Utlitites.scale(rt.getUrlRt500pv(),rtCompare.getUrlRt500pv()) %>)</td>
								<td width="200"><%=Utlitites.fromatLong(rt.getUrlRt1000pv()+"")%>(<%=Utlitites.scale(rt.getUrlRt1000pv(),rtCompare.getUrlRt1000pv()) %>)</td>
								<td width="200"><%=Utlitites.fromatLong(rt.getErrorUrlPv()+"")%>(<%=Utlitites.scale(rt.getErrorUrlPv(),rtCompare.getErrorUrlPv()) %>)</td>
							</tr>								
						</table>					
				</td>
				<td ><a href="http://depend.csp.taobao.net:9999/depend/rate/callrate.do?method=gotoTopoPage&sourceKey=<%=rt.getUrl() %>" target="_blank">����</a></td>
			</tr>
			<%} %>
		</tbody>
	</table>
</div>
</div>
</body>
<script type="text/javascript">
  
  $(document).ready(function() {
	  $("td[rel=popover]") .popover({placement:"left"}) .click(function(e) {
	      e.preventDefault()
	    });
    });
</script>
</html>