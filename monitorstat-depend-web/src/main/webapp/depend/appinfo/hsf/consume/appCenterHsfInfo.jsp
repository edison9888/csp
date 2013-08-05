<%@page import="com.taobao.csp.depend.util.MethodUtil"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.csp.depend.po.hsf.AppSummary"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.csp.depend.po.hsf.InterfaceSummary"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.csp.depend.po.hsf.ProvideSiteRating"%>
<%@page import="java.util.Map"%>
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

		<script type="text/javascript"
			src="<%=request.getContextPath() %>/statics/js/jquery/jquery.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath() %>/statics/js/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath() %>/statics/userjs/mainpage.js"></script>
		<script language="javascript" type="text/javascript"
			src="<%=request.getContextPath() %>/statics/datePicket/WdatePicker.js"></script>
		<script language="JavaScript"
			src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
		<title>我消费的HSF信息</title>

		<%
String opsName = (String)request.getAttribute("opsName");
String selectDate = (String)request.getAttribute("selectDate");

Map<String,ProvideSiteRating>  provideSiteRatingMap = (Map<String,ProvideSiteRating>)request.getAttribute("provideSiteRatingMap");	//计算机房比例
ArrayList<InterfaceSummary>  interfaceList = (ArrayList<InterfaceSummary>)request.getAttribute("interfaceList");
ArrayList<AppSummary>  appList = (ArrayList<AppSummary>)request.getAttribute("appList");


String interfaceNums = (String)request.getAttribute("interfaceNums");	//调用总接口数
String preInterfaceNums = (String)request.getAttribute("preInterfaceNums");

String allCallNum = (String)request.getAttribute("allCallNum");	//总调用量
String preAllCallNum = (String)request.getAttribute("preAllCallNum");

String appNums = (String)request.getAttribute("appNums");	//调用应用数
String preAppNums = (String)request.getAttribute("preAppNums");


StringBuilder sb = new StringBuilder();
sb.append("<pie>");
for(AppSummary bo:appList){
	String name = bo.getOpsName();
	sb.append("<slice title='"+name+"'>"+bo.getCallAllNum()+"</slice>");			
}
sb.append("</pie>");

%>

	</head>
	<body>
		<%@ include file="../../../header.jsp"%>
		<form id="mainForm"
			action="<%=request.getContextPath() %>/show/hsfconsume.do"
			method="get">
			<input type="hidden" value="showAppCenterConsumeHsfInfo"
				name="method">


			<div style="text-align: center">
				<div id="page_nav"></div>
			</div>
			<script>
		$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {appName: '${param.opsName}', selectDate:'<%=selectDate%>'});
		</script>
			<div class="container">
				<div class="row">
					<div class="span12">
						<span style="font-weight: bold">&nbsp;&nbsp;--<%= opsName%>--的HSF消费信息</span>
					</div>
				</div>
				
				<div class="row">
					<div class="span12">
												<table width="500"
																			class="table table-striped table-bordered table-condensed">
																			<tr>
																				<td width="120">
																					调用应用数:
																				</td>
																				<td><%=appNums %>&nbsp;&nbsp;<%=MethodUtil.compare(Long.parseLong(appNums), Long.parseLong(preAppNums))%></td>
																			</tr>
																			<tr>
																				<td width="100">
																					调用的总接口数:
																				</td>
																				<td><%=interfaceNums %>&nbsp;&nbsp;<%=MethodUtil.compare(Long.parseLong(interfaceNums), Long.parseLong(preInterfaceNums))%></td>
																			</tr>
																			<tr>
																				<td width="100">
																					总调用量:
																				</td>
																				<td><%=Utlitites.fromatLong(allCallNum) %>&nbsp;&nbsp;<%=Utlitites.scale(allCallNum, preAllCallNum) %></td>
																			</tr>
																			<tr>
																				<td width="100">
																					出现总异常数:
																				</td>
																				<td>
																					&nbsp;
																					<a
																						href="javascript:openWin('<%=request.getContextPath()%>/show/hsfconsume.do?method=showAppHsfException&opsName=${opsName}&selectDate=${selectDate}',700,1100)">查看详细</a>
																				</td>
																			</tr>
																			<tr>
																				<td width="100">
																					机房比例:
																				</td>
																				<td>
																					<%
																				for(Map.Entry<String,ProvideSiteRating> entry:provideSiteRatingMap.entrySet()){
																					out.print(entry.getKey()+":"+Arith.mul(Arith.div(entry.getValue().getCallAllNum(),Double.parseDouble(allCallNum),4), 100)+"% ("+Utlitites.scale(entry.getValue().getCallAllNum()+"", entry.getValue().getPreCallAllNum()+"")+")</br>");
																				}
																				%>
																				</td>
																			</tr>
																			<tr>
																				<td colspan="2">
																					<table width="100%"
																						class="table table-striped table-bordered table-condensed">
																						<tr>
																							<td colspan="4" align="center">
																								调用应用量前10名 &nbsp;&nbsp;
																								<a
																									href="<%=request.getContextPath()%>/show/hsfconsume.do?method=showAppProvideAll&opsName=<%= opsName%>&selectDate=<%= selectDate%>">查看全部</a>
																							</td>
																						</tr>
																						<%
																				for(int i=0;i<appList.size()&&i<10;i++) {
																					AppSummary is = appList.get(i);
																				%>
																						<tr>
																							<td width="20%"><%=is.getOpsName() %>
																								<%
																						if(is.getOption() == "add") {
																					%>
																								<font color="red">新增</font>
																								<%		
																						} else if(is.getOption() == "sub") {
																					%>
																								<font color="red">消失</font>
																								<%				
																						}
																					%>
																							</td>
																							<td width="20%"><%=Utlitites.fromatLong(is.getCallAllNum()+"")%><%=Utlitites.scale((is.getCallAllNum()+""), (is.getPreCallAllNum()+""))%></td>
																							<td width="20%"><%=Arith.mul(Arith.div(is.getCallAllNum(),Double.parseDouble(allCallNum),4), 100)%>%
																							</td>
																							<td width="40%">
																								<a
																									href="javascript:openWin('<%=request.getContextPath()%>/show/hsfconsume.do?method=showAppProviderDetail&providename=<%=is.getOpsName()%>&consumeName=<%=opsName%>&selectDate=<%= selectDate%>',700,1100)">接口分布</a>&nbsp;
																								<a
																									href="javascript:openWin('<%=request.getContextPath()%>/show/hsfconsume.do?method=showAppConsumeProvideMachine&providename=<%=is.getOpsName()%>&consumeName=<%=opsName%>&selectDate=<%= selectDate%>',700,1100)">机器分布</a>&nbsp;
																								<a
																									href="javascript:openWin('<%=request.getContextPath()%>/show/hsfconsume.do?method=showConsumeAppHistoryGraph&targetAppName=<%=is.getOpsName()%>&sourceAppName=<%=opsName%>&endDate=<%= selectDate%>',700,1100)">查看历史走势</a>																									
																							</td>
																						</tr>
																						<% }%>
																					</table>
																				</td>
																			</tr>
																		</table>
					</div>
				</div><!-- row -->
				<div class="row">
				<div class="span12">
				<table width="100%"
																class="table table-striped table-bordered table-condensed">
																<tr>
																	<td colspan="5" align="center">
																		调用接口前10名 &nbsp;&nbsp;
																		<a
																			href="<%=request.getContextPath()%>/show/hsfconsume.do?method=showAppAllCenterInterface&opsName=<%= opsName%>&selectDate=<%= selectDate%>">查看全部</a>
																	</td>
																</tr>
																<tr>
																	<td align="center">
																		接口名
																	</td>
																	<td align="center">
																		所属应用名
																	</td>
																	<td align="center">
																		总调用量
																	</td>
																	<td align="center">
																		占总比值
																	</td>
																	<td align="center"></td>
																</tr>
																<%
																for(int i=0;i<interfaceList.size()&&i<10;i++) {
																	InterfaceSummary is = interfaceList.get(i);
																%>
																<tr>
																	<td><%=is.getKeyName() %>
																		<%
																		if(is.getOption() == "add") {
																	%>
																		<font color="red">新增</font>
																		<%		
																		} else if(is.getOption() == "sub") {
																	%>
																		<font color="red">消失</font>
																		<%				
																		}
																	%>
																	</td>
																	<td><%=is.getAppName() %></td>
																	<td><%=Utlitites.fromatLong(is.getCallAllNum()+"")%><%=Utlitites.scale((is.getCallAllNum()+""), (is.getPreCallAllNum()+""))%></td>
																	<td><%=Arith.mul(Arith.div(is.getCallAllNum(),Double.parseDouble(allCallNum),4), 100)%>%
																	</td>
																	<td>
																		<a
																			href="javascript:openWin('<%=request.getContextPath()%>/show/hsfconsume.do?method=showProvideMachine&interfaceName=<%=is.getName() %>&opsName=${opsName}&selectDate=${selectDate}',700,1100)">机器分布</a>&nbsp;
																		<a href="javascript:openWin('<%=request.getContextPath()%>/show/hsfconsume.do?method=showInterfaceHistoryGraph&interfaceName=<%=is.getName()%>&sourceAppName=<%=opsName%>&endDate=<%= selectDate%>',700,1100)">查看历史走势</a>																				
																	</td>
																</tr>
																<% }%>
															</table>
				</div>
				</div>
			</div><!-- container -->

		</form>
		<script type="text/javascript">
//初始化search bar
$(document).ready(function(){
	$('#mm').accordion('select', "我的详细信息");
	changeColor('consumeHSF');
}); 
</script>
	</body>
</html>