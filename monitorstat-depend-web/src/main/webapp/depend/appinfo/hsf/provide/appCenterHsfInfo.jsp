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
		<!-- 		<link rel="stylesheet" type="text/css"
			href="<%=request.getContextPath() %>/statics/js/easyui/themes/gray/easyui.css">
			<link rel="stylesheet"
			href="<%=request.getContextPath() %>/statics/css/main.css"
			type="text/css" />
			
			 -->


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

		<title>依赖模块</title>

		<%
String opsName = (String)request.getAttribute("opsName");
String selectDate = (String)request.getAttribute("selectDate");

Map<String,ProvideSiteRating>  provideSiteRatingMap = (Map<String,ProvideSiteRating>)request.getAttribute("provideSiteRatingMap");
ArrayList<InterfaceSummary>  interfaceList = (ArrayList<InterfaceSummary>)request.getAttribute("interfaceList");
ArrayList<AppSummary>  appList = (ArrayList<AppSummary>)request.getAttribute("appList");


String interfaceNums = (String)request.getAttribute("interfaceNums");
String preInterfaceNums = (String)request.getAttribute("preInterfaceNums");

String allCallNum = (String)request.getAttribute("allCallNum");
String preAllCallNum = (String)request.getAttribute("preAllCallNum");

String appNums = (String)request.getAttribute("appNums");
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
			action="<%=request.getContextPath() %>/show/hsfprovide.do"
			method="get">
			<input type="hidden" value="showAppCenterHsfInfo" name="method">

			<!-- <div class="main_navigation"> 
					<table width="100%" align="center">
					<tr>
						<td width="210px"></td>
						<td align="left">
							<select id="parentGroupSelect" onChange="groupChange(this)">	
							</select>
							<select id="appNameSelect" name="opsName">	
							</select>
							日期: <input type="text" id="selectDate" value="<%-- selectDate---%>" name="selectDate" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})" />
							&nbsp;&nbsp;<input type="submit" value="查询" >
						</td>
						<td id="searchDiv">
									
						</td>
					</tr>
				</table>
				</div>
		 -->
			<div style="text-align:center">
				<div id="page_nav"></div>
				</div>
				<script>
		$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {appName: '${param.opsName}', selectDate:'<%=selectDate%>'});
		</script>
		

			<div class="container">
				<div class="row">
					<div class="span12">


					</div>
				</div>
				<div class="row">
					<div class="span12"
						style="float: left; margin-top: 0px; width: 55%;">
						<span style="font-weight: bold">&nbsp;&nbsp;--<%= opsName%>--的HSF提供信息</span>
					</div>
				</div>


				<div class="row">
					<div class="span6">
						<table class="table table-striped table-bordered table-condensed">
							<tr>
								<td width="120">
									依赖应用数:
								</td>
								<td><%=appNums %>&nbsp;&nbsp;<%=MethodUtil.compare(Long.parseLong(appNums), Long.parseLong(preAppNums))%></td>
							</tr>
							<tr>
								<td width="100">
									提供的总方法数:
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
										href="javascript:openWin('<%=request.getContextPath()%>/show/hsfprovide.do?method=showAppHsfException&opsName=${opsName}&selectDate=${selectDate}',700,1100)">查看详细</a>
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
												应用调用量前10名 &nbsp;&nbsp;
												<a
													href="<%=request.getContextPath()%>/show/hsfprovide.do?method=showAppConsumeAll&opsName=<%= opsName%>&selectDate=<%= selectDate%>">查看全部</a>
											</td>
										</tr>
										<%
																				for(int i=0;i<appList.size()&&i<10;i++) {
																					AppSummary is = appList.get(i);
																				%>
										<tr>
											<td><%=is.getOpsName() %>
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
											<td><%=Utlitites.fromatLong(is.getCallAllNum()+"")%><%=Utlitites.scale((is.getCallAllNum()+""), (is.getPreCallAllNum()+""))%></td>
											<td><%=Arith.mul(Arith.div(is.getCallAllNum(),Double.parseDouble(allCallNum),4), 100)%>%
											</td>
											<td>
												<a href="javascript:openWin('<%=request.getContextPath()%>/show/hsfprovide.do?method=showAppConsumeDetail&providename=<%= opsName%>&consumeName=<%=is.getOpsName() %>&selectDate=<%= selectDate%>',700,1100)">接口分布</a>&nbsp;
												<a href="javascript:openWin('<%=request.getContextPath()%>/show/hsfprovide.do?method=showAppConsumeProvideMachine&providename=<%=opsName%>&consumeName=<%=is.getOpsName() %>&selectDate=<%= selectDate%>',700,1100)">机器分布</a>&nbsp;
												<a href="javascript:openWin('<%=request.getContextPath()%>/show/hsfprovide.do?method=showProvideAppHistoryGraph&targetAppName=<%=is.getOpsName()%>&sourceAppName=<%=opsName%>&endDate=<%= selectDate%>',700,1100)">查看历史走势</a>
												<a href="javascript:openWin('<%=request.getContextPath()%>/show/tairconsume.do?method=showTairConsumeMain&&opsName=<%=is.getOpsName()%>&endDate=<%= selectDate%>',700,1100)">Tair消费</a>
											</td>
										</tr>
										<% }%>
									</table>
								</td>
							</tr>
						</table>
					</div>
					<div id="chartdiv2" align="center" class="span6"></div>
				</div>
				<!-- row -->


				<div class="row">
					<div class="span12">
						<table width="100%"
							class="table table-striped table-bordered table-condensed">
							<tr>
								<td colspan="4" align="center">
									方法调用次数前10名 &nbsp;&nbsp;
									<a
										href="<%=request.getContextPath()%>/show/hsfprovide.do?method=showAppAllCenterInterface&opsName=<%= opsName%>&selectDate=<%= selectDate%>">查看全部</a>
								</td>
							</tr>
							<tr>
								<td align="center">
									接口名
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
																	%> &nbsp;<a href="<%=request.getContextPath()%>/rate/callrate.do?method=gotoTopoPage&sourceKey=<%=is.getKeyName()%>&appName=<%=opsName%>&type=child_key&collectTime=<%=selectDate%>" target="_blank">查看调用路径<img src="<%=request.getContextPath()%>/statics/css/images/newhotcool.gif"/></a>
								</td>
								<td><%=Utlitites.fromatLong(is.getCallAllNum()+"")%><%=Utlitites.scale((is.getCallAllNum()+""), (is.getPreCallAllNum()+""))%></td>
								<td><%=Arith.mul(Arith.div(is.getCallAllNum(),Double.parseDouble(allCallNum),4), 100)%>%
								</td>
								<td>
									<a
										href="javascript:openWin('<%=request.getContextPath()%>/show/hsfprovide.do?method=showAppHsfInterfaceDetail&interfaceName=<%=is.getName() %>&opsName=${opsName}&selectDate=${selectDate}',700,1100)">应用分布</a>&nbsp;&nbsp;
									<a
										href="javascript:openWin('<%=request.getContextPath()%>/show/hsfprovide.do?method=showProvideMachine&interfaceName=<%=is.getName() %>&opsName=${opsName}&selectDate=${selectDate}',700,1100)">机器分布</a>&nbsp;&nbsp;
									<a href="javascript:openWin('<%=request.getContextPath()%>/show/hsfprovide.do?method=showInterfaceHistoryGraph&interfaceName=<%=is.getName()%>&sourceAppName=<%=opsName%>&endDate=<%= selectDate%>',700,1100)">查看历史走势</a>										
								</td>
							</tr>
							<% }%>
						</table>
					</div>

				</div>
			</div>
			<!-- container -->
		</form>


		<script type="text/javascript">
//初始化search bar
$(document).ready(function(){

	$('#mm').accordion('select', "我的详细信息");
	changeColor('provoidHSF');
	var so1 = new SWFObject("<%=request.getContextPath() %>/statics/ampie/ampie.swf", "ampie", "500", "380", "8", "#FFFFFF");
	so1.addVariable("path", "<%=request.getContextPath() %>/statics/amline/");
	so1.addVariable("chart_id", "amline");   
	so1.addVariable("settings_file", "<%=request.getContextPath() %>/statics/ampie/ampie_settings.xml");
	so1.addVariable("chart_data", encodeURIComponent("<%=sb.toString()%>"));
	so1.write("chartdiv2");		
}); 
</script>
	</body>
</html>