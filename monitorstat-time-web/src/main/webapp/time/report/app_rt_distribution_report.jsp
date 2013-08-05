<%@page import="java.util.Arrays"%>
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
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<link href="<%=request.getContextPath()%>/statics/css/bootstrap.css"	rel="stylesheet">
<link href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css" rel="stylesheet">
<script src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
<script src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>

<title>Ӧ����Ӧʱ��ֲ�</title>
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
List<UrlRtErrorCount> applist = (List<UrlRtErrorCount>)request.getAttribute("listapp");
Map<String, UrlRtErrorCount> mapCompare = (Map<String, UrlRtErrorCount>)request.getAttribute("mapCompare");
if(mapCompare == null)
	mapCompare = new HashMap<String, UrlRtErrorCount>();

Collections.sort(applist, new Comparator<UrlRtErrorCount>(){
	 public int compare(UrlRtErrorCount o1, UrlRtErrorCount o2){
		  if(Arith.div(o1.getAppRt100Pv(),  o1.getAppPv(), 4)>Arith.div(o2.getAppRt100Pv(),  o2.getAppPv(), 4)){
			  return 1;
		  }else if(Arith.div(o1.getAppRt100Pv(),  o1.getAppPv(), 4)<Arith.div(o2.getAppRt100Pv(),  o2.getAppPv(), 4)){
			  return -1;
		  }else{
			  return 0;
		  }
	  }
});
%>

<div class="row-fluid">
<div class="span12">
	<form action="<%=request.getContextPath()%>/app/report.do" method="get">
		<input type="hidden" name="method"  value="reportAppRTDistribution">
		����:<input id="time1" type="text" name="time" value="${time}">�Ա�����:<input  id="time2"  type="text" name="compareTime" value="${compareTime}"><input type="submit" value="�ύ">
	</form>
</div>
<div class="span12">
	<table width="100%" >
		<tr>
			<td align="center">
				<h5>Ӧ����Ӧʱ��ֲ����</h5>
				<h5>��ȡʱ��Ϊ:${time},�Ա�ʱ��Ϊ${compareTime}</h5>
			</td>
			<td align="center">
				<span style="background:#00FF00">
					��Ӧʱ��С��100ms
				</span>
				<span style="background:#6633FF">
					��Ӧʱ��100-500ms
				</span>
				<span style="background:#FFFF66">
					��Ӧʱ��500-1000ms
				</span>
				<span style="background:#FF0000">
					��Ӧʱ�����1000ms
				</span>
			</td>
		</tr>
	</table>
</div>	
	<div class="span12">
		<table  class="table table-striped table-condensed table-bordered">
			<thead>
				<tr>
					<th >Ӧ������</th>
					<th  >�ܷ�����</th>
					<th >��Ӧʱ��ֲ�</th>
					<th >&nbsp;</th>
				</tr>
			</thead>
			<tbody>
				<%
				for(UrlRtErrorCount rt:applist){
					if(rt.getAppPv()<10000){
						continue;
					}
					UrlRtErrorCount rtCompare = mapCompare.get(rt.getAppName());
					if(rtCompare == null)
						rtCompare = new UrlRtErrorCount();
					
				%>
				<tr>
					<td width="100" ><%=rt.getAppName()%></td>
					<td width="100" ><%=Utlitites.fromatLong(rt.getAppPv()+"")%>(<%=Utlitites.scale(rt.getAppPv(),rtCompare.getAppPv()) %>)</td>
					<td align="center"  width="800" >
						<table width="800">
							<tr>
								<td width="<%=(Arith.div(rt.getAppRt100Pv(),  rt.getAppPv(), 4)*800) %>" style="background:#00FF00" title="��Ӧʱ��С��100ms" rel="popover" data-content="<table class='table table-striped table-condensed table-bordered'><tr><td>����</td><td>ռ����</td></tr><tr><td><%=Utlitites.fromatLong(rt.getAppRt100Pv()+"")%></td><td><%=(DataUtil.rate(rt.getAppRt100Pv(), rt.getAppPv())) %>%</td></tr></table>">&nbsp;</td>
								<td width="<%=(Arith.div(rt.getAppRt500pv(),  rt.getAppPv(), 4)*800) %>" style="background:#6633FF" title="��Ӧʱ��100-500ms"  rel="popover" data-content="<table class='table table-striped table-condensed table-bordered'><tr><td>����</td><td>ռ����</td></tr><tr><td><%=Utlitites.fromatLong(rt.getAppRt500pv()+"")%></td><td><%=(DataUtil.rate(rt.getAppRt500pv(), rt.getAppPv())) %>%</td></tr></table>">&nbsp;</td>
								<td width="<%=(Arith.div(rt.getAppRt1000pv(),  rt.getAppPv(), 4)*800) %>" style=" background:#FFFF66" title="��Ӧʱ��500-1000ms"  rel="popover" data-content="<table class='table table-striped table-condensed table-bordered'><tr><td>����</td><td>ռ����</td></tr><tr><td><%=Utlitites.fromatLong(rt.getAppRt1000pv()+"")%></td><td><%=(DataUtil.rate(rt.getAppRt1000pv(), rt.getAppPv())) %>%</td></tr></table>">&nbsp;</td>
								<td width="<%=(Arith.div(rt.getAppErrorPv(),  rt.getAppPv(), 4)*800) %>" style="background:#FF0000" title="��Ӧʱ�����1000ms"  rel="popover" data-content="<table class='table table-striped table-condensed table-bordered'><tr><td>����</td><td>ռ����</td></tr><tr><td><%=Utlitites.fromatLong(rt.getAppErrorPv()+"")%></td><td><%=(DataUtil.rate(rt.getAppErrorPv(), rt.getAppPv())) %>%</td></tr></table>">&nbsp;</td>
							</tr>
						</table>
						<table width="800">
							<tr>
								<td width="200"><%=rt.getAppRt100Pv()%>(<%=Utlitites.scale(rt.getAppRt100Pv(),rtCompare.getAppRt100Pv()) %>)</td>
								<td width="200"><%=rt.getAppRt500pv()%>(<%=Utlitites.scale(rt.getAppRt500pv(),rtCompare.getAppRt500pv()) %>)</td>
								<td width="200"><%=rt.getAppRt1000pv()%>(<%=Utlitites.scale(rt.getAppRt1000pv(),rtCompare.getAppRt1000pv()) %>)</td>
								<td width="200"><%=rt.getAppErrorPv()%>(<%=Utlitites.scale(rt.getAppErrorPv(),rtCompare.getAppErrorPv()) %>)</td>
							</tr>								
						</table>
					</td>
					<td ><a href="http://time.csp.taobao.net:9999/time/app/report.do?method=reportUrlRTDistribution&appName=<%=rt.getAppName() %>&time=${time}&compareTime=${compareTime}" target="_blank">����</a></td>
				</tr>
				<%} %>
			</tbody>
		</table>
	</div>
</div>
</body>
<script type="text/javascript">
  
  $(document).ready(function() {
	  $("td[rel=popover]") .popover({placement:"right"}) .click(function(e) {
	      e.preventDefault()
	    });
    });
</script>
</html>