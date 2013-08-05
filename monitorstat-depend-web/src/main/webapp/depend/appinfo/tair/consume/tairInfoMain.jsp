<%@page import="com.taobao.csp.depend.util.MethodUtil"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.csp.depend.po.tair.ActionSummaryPo"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.csp.depend.po.tair.TairConsumeSummaryPo"%>
<%@page import="com.taobao.csp.depend.po.hsf.ProvideSiteRating"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.csp.depend.po.tair.TairConsumeDetailPo"%>

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
			src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/userjs/mainpage.js"></script>
		<script language="javascript" type="text/javascript"
			src="<%=request.getContextPath()%>/statics/datePicket/WdatePicker.js"></script>
		<script language="JavaScript"
			src="<%=request.getContextPath()%>/statics/js/swfobject.js"></script>
		<title>我消费的Tair信息</title>
		<%
String opsName = (String)request.getAttribute("opsName");
String selectDate = (String)request.getAttribute("selectDate");

ArrayList<TairConsumeSummaryPo> tairConsumeSummaryList = (ArrayList<TairConsumeSummaryPo>)request.getAttribute("tairConsumeSummaryList");
ArrayList<ActionSummaryPo>  actionList = (ArrayList<ActionSummaryPo>)request.getAttribute("actionList");

String allCallNum = (String)request.getAttribute("allCallNum");		
String preAllCallNum = (String)request.getAttribute("preAllCallNum");
Map<String,ProvideSiteRating> provideSiteRatingMap = (Map<String,ProvideSiteRating>)request.getAttribute("provideSiteRatingMap");

String preAppNums = (String)request.getAttribute("preAppNums");

ArrayList<TairConsumeDetailPo> detailList = (ArrayList<TairConsumeDetailPo>)request.getAttribute("detailList");

%>

	</head>
	<body>
		<%@ include file="../../../header.jsp"%>
		<form id="mainForm"
			action="<%=request.getContextPath()%>/show/tairconsume.do"
			method="get">
			<input type="hidden" value="showTairConsumeMain" name="method">
			<div style="text-align: center">
				<div id="page_nav"></div>
			</div>
			<script>
		$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {appName: '${param.opsName}', selectDate:'<%=selectDate%>'});
		</script>


			<div class="container">
				<div class="row">
					<div class="span12">
						<span style="font-weight: bold">&nbsp;&nbsp;--<%=opsName%>--的Tair消费信息</span>
					</div>

				</div>
				<div class="row">
					<div class="span5">
						<table width="100%"
							class="table table-striped table-bordered table-condensed">
							<tr>
								<td width="100">
									总调用量:
								</td>
								<td><%=Utlitites.fromatLong(allCallNum)%>&nbsp;&nbsp;<%=Utlitites.scale(allCallNum, preAllCallNum)%></td>
							</tr>
							<tr>
								<td width="100">
									出现总异常数:
								</td>
								<td>
									&nbsp;
									<a
										href="javascript:openWin('<%=request.getContextPath()%>/show/tairconsume.do?method=showExceptionDetail&opsName=${opsName}&selectDate=${selectDate}',700,1100)"
										style="color: red;">查看详细</a>
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
												Tair数据（By ActionType 总计）
											</td>
										</tr>
										<tr>
											<td>
												ActionType
											</td>
											<td>
												调用次数
											</td>
											<td>
												百分比
											</td>
										</tr>
										<%
																						for(int i=0;i < actionList.size()&& i< 10;i++) {
																							ActionSummaryPo is = actionList.get(i);
																					%>
										<tr>
											<td><%=is.getActionName()%></td>
											<td><%=Utlitites.fromatLong(is.getCallAllNum()+"")%><%=Utlitites.scale((is.getCallAllNum()+""), (is.getPreCallAllNum()+""))%></td>
											<td><%=Arith.mul(Arith.div(is.getCallAllNum(), Double.parseDouble(allCallNum),4), 100)%>%
											</td>
										</tr>
										<% }%>
									</table>
								</td>
							</tr>
						</table>
					</div>
					<div class="span7">
						<table class="table table-striped table-bordered table-condensed">
							<tr>
								<td colspan="5" align="center">
									Tair数据（By TairGroup）前10名&nbsp;&nbsp;
									<a
										href="<%=request.getContextPath()%>/show/tairconsume.do?method=showSummaryAll&opsName=<%=opsName%>&selectDate=<%=selectDate%>">查看全部</a>
								</td>
							</tr>
							<tr>
								<td>
									TairGroupName
								</td>
								<td>
									调用次数
								</td>
								<td>
									百分比
								</td>
								<td></td>
							</tr>
							<%
																				for(int i=0;i<tairConsumeSummaryList.size()&&i<10;i++) {
																					TairConsumeSummaryPo is = tairConsumeSummaryList.get(i);
																			%>
							<tr>
								<td><%=is.getTair_group_name()%></td>
								<td><%=Utlitites.fromatLong(is.getInvoking_all_num()+"")%><%=Utlitites.scale((is.getInvoking_all_num()+""), (is.getPrecall()+""))%></td>
								<td><%=Arith.mul(Arith.div(is.getInvoking_all_num(), Double.parseDouble(allCallNum),4), 100)%>%
								</td>
								<td align="center" width="60px">
									<a
										href="javascript:openWin('<%=request.getContextPath()%>/show/tairconsume.do?method=showTairByTairGroupName&opsName=${opsName}&selectDate=${selectDate}&tairgroupname=<%=is.getTair_group_name()%>',700,1100)">查看详细</a>
								</td>
							</tr>
							<% }%>
						</table>
					</div>
				</div>
				<!-- row -->
				<div class="row">
					<div class="span12">
						<table width="100%"
							class="table table-striped table-bordered table-condensed">
							<tr>
								<td colspan="<%=TairConsumeDetailPo.siteName.size() + 6%>"
									align="center">
									Tair数据（By ActionType 详细）前10名 &nbsp;&nbsp;
									<a
										href="<%=request.getContextPath()%>/show/tairconsume.do?method=showDetailAll&opsName=<%= opsName%>&selectDate=<%= selectDate%>">查看全部</a>
								</td>
							</tr>
							<tr>
								<td align="center">
									Group Name
								</td>
								<td align="center">
									NameSpace
								</td>
								<td align="center">
									ActionType
								</td>
								<%
																		for(String name: TairConsumeDetailPo.siteName) {
																	%>
								<td align="center">
									机房<%=name%></td>
								<%
																		}
																	%>
								<td align="center">
									调用总计
								</td>
								<td align="left">
									百分比
								</td>
								<td align="center"></td>
							</tr>
							<%
																for(int i=0;i<detailList.size()&&i<10;i++) {
																	TairConsumeDetailPo is = detailList.get(i);
																%>
							<tr>
								<td align="center"><%=is.getTair_group_name()%></td>
								<td align="center"><%=is.getNamespace()%></td>
								<td><%=is.getAction_type()%></td>
								<%
																		for(String name: TairConsumeDetailPo.siteName) {
																	%>
								<td align="center"><%=Utlitites.fromatLong(is.siteMap.get(name)+"")%></td>
								<%
																		}
																	%>
								<td><%=Utlitites.fromatLong(is.getCallNum()+"")%><%=Utlitites.scale((is.getCallNum()+""), (is.getPreCallNum()+""))%></td>
								<td><%=Arith.mul(Arith.div(is.getCallNum(),Double.parseDouble(allCallNum),4), 100)%>%
								</td>
								<td align="center" width="60px">
									<a
										href="javascript:openWin('<%=request.getContextPath()%>/show/tairconsume.do?method=showMachine&opsName=${opsName}&selectDate=${selectDate}&tairgroupname=<%=is.getTair_group_name()%>&namespace=<%=is.getNamespace()%>&actiontype=<%=is.getAction_type()%>',700,1100)">机器分布</a>
								</td>
							</tr>
							<% }%>
						</table>
					</div>
				</div>
				<!-- -->
			</div>
			<!-- container -->

		</form>
		<script type="text/javascript">
//初始化search bar
$(document).ready(function(){
	
	$('#mm').accordion('select', "我的详细信息");
	
	changeColor('consumeTair');
}); 
</script>
	</body>
</html>