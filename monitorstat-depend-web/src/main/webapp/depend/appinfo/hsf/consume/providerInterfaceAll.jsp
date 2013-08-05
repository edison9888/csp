<%@page import="com.taobao.csp.depend.util.MethodUtil"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.csp.depend.po.hsf.AppSummary"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.csp.depend.po.hsf.InterfaceSummary"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.csp.depend.po.hsf.ProvideSiteRating"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=GB18030"  isELIgnored="false" 
    pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0">

<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/userjs/mainpage.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/datePicket/WdatePicker.js"></script>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<title>����ģ��</title>

<%
String opsName = (String)request.getAttribute("opsName");
String selectDate = (String)request.getAttribute("selectDate");


%>

</head>
<body>
<%@ include file="../../../header.jsp"%>
	<form id="mainForm"  action="<%=request.getContextPath() %>/show/hsfconsume.do" method="get">
		<input type="hidden" value="showAppCenterConsumeHsfInfo" name="method">
	<div style="text-align: center">
				<div id="page_nav"></div>
			</div>
		<script>
		$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {appName: '${param.opsName}', selectDate:'<%=selectDate%>'});
		</script>
		
		<div class="container">
			<div class="row">
				<div class="span12"><span style="font-weight:bold">&nbsp;&nbsp;--<%= opsName%>--����HSF�ӿ��б�</span></div>
			</div><!-- row -->
			<div class="row">
				<div class="span12">		<%
																ArrayList<InterfaceSummary>  interfaceList = (ArrayList<InterfaceSummary>)request.getAttribute("interfaceList");
																String allCallNum = (String)request.getAttribute("allCallNum");
																%>
																<table  width="100%"  class="table table-striped table-bordered table-condensed">
																<tr>
																	<td align="center">�ӿ���</td>
																	<td align="center">����Ӧ����</td>
																	<td align="center">�ܵ�����</td>
																	<td align="center">ռ�ܱ�ֵ</td>
																	<td  align="center"></td>
																</tr>
																<%
																for(int i=0;i<interfaceList.size();i++) {
																	InterfaceSummary is = interfaceList.get(i);
																	if(is.getKeyName().indexOf("Exception_") >-1){
																		continue;
																	}
																%>
																<tr>
																	<td ><%=is.getKeyName() %>
																	<%
																		if(is.getOption() == "add") {
																	%>
																		<font color="red">����</font>	
																	<%		
																		} else if(is.getOption() == "sub") {
																	%>
																			<font color="red">��ʧ</font>	
																	<%				
																		}
																	%>																	
																	</td>
																	<td ><%=is.getAppName() %></td>
																	<td ><%=Utlitites.fromatLong(is.getCallAllNum()+"")%><%=Utlitites.scale((is.getCallAllNum()+""), (is.getPreCallAllNum()+""))%></td>
																	<td ><%=Arith.mul(Arith.div(is.getCallAllNum(),Double.parseDouble(allCallNum),4), 100)%>%</td>
																	<td >
																	<a href="javascript:openWin('<%=request.getContextPath()%>/show/hsfconsume.do?method=showProvideMachine&interfaceName=<%=is.getName() %>&opsName=${opsName}&selectDate=${selectDate}',700,1100)">�����ֲ�</a>&nbsp;
																	<a href="javascript:openWin('<%=request.getContextPath()%>/show/hsfconsume.do?method=showInterfaceHistoryGraph&interfaceName=<%=is.getName()%>&sourceAppName=<%=opsName%>&endDate=<%= selectDate%>',700,1100)">�鿴��ʷ����</a>
																	</td>
																</tr>
																<% }%>
																</table></div>
			</div><!-- row -->
			<div class="row">
				<div class="span12"></div>
			</div><!-- row -->
			<div class="row">
				<div class="span12"></div>
			</div><!-- row -->
		</div><!-- container -->
	</form>
<script type="text/javascript">
//��ʼ��search bar
$(document).ready(function(){
	
	$('#mm').accordion('select', "�ҵ���ϸ��Ϣ");
	changeColor('consumeHSF');
}); 
</script>
</body>
</html>