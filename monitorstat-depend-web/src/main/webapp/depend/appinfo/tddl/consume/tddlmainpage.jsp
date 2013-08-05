<%@page import="com.taobao.csp.depend.util.MethodUtil"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.csp.depend.po.tddl.ConsumeDbDetail"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.csp.depend.po.hsf.ProvideSiteRating"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
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
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/statics/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/main.css" type="text/css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery-1.6.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/userjs/mainpage.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/statics/datePicket/WdatePicker.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/statics/js/swfobject.js"></script>
<title>Tddl信息统计</title>
<%
String opsName = (String)request.getAttribute("opsName");
String selectDate = (String)request.getAttribute("selectDate");

String totalNum = (String)request.getAttribute("totalNum");		
String sqlSuccessNum = (String)request.getAttribute("sqlSuccessNum");
String timeOutNum = (String)request.getAttribute("timeOutNum");
List<ConsumeDbDetail> detailList = (ArrayList<ConsumeDbDetail>)request.getAttribute("detailList");
Map<String, ProvideSiteRating> machineMap = (Map<String, ProvideSiteRating>)request.getAttribute("machineMap");
%>

</head>
<body>
<%@ include file="../../../header.jsp"%>
	<form id="mainForm"  action="<%=request.getContextPath()%>/show/tddlconsume.do" method="get">
		<input type="hidden" value="showTddlConsumeMain" name="method">
		<div class="mainwrap">
		 <div id="page_nav"></div>
		<script>
		$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {appName: '${param.opsName}', selectDate:'<%=selectDate%>'});
		</script>
			<table >
				<tr>
					<td valign="top">
						<table >
							<tr>
								
								<td width="100%" valign="top">
									<table  width="100%" >
										<tr>
											<td valign="top">
											<div style="float:left;margin-top:0px;width:55%;"><span style="font-weight:bold">&nbsp;&nbsp;应用[<%=opsName%>]调用Tddl的信息</span></div>
											<table width="100%" class="table table-striped table-bordered table-condensed">			
												<tr>
													<td width="200">总调用量:
													<%=Utlitites.fromatLong(totalNum)%>&nbsp;&nbsp;<%=Utlitites.scale(totalNum, totalNum)%></br>
													成功率：<%=Arith.mul(Arith.div(Double.parseDouble(sqlSuccessNum),Double.parseDouble(totalNum),4), 100)+"%" %>
													</td>
													<td width="">机房比例:
													<%
													for(Map.Entry<String,ProvideSiteRating> entry:machineMap.entrySet()){
														out.print(entry.getKey()+":"+Arith.mul(Arith.div(entry.getValue().getCallAllNum(),Double.parseDouble(totalNum),4), 100)+"% ("+Utlitites.scale(entry.getValue().getCallAllNum()+"", entry.getValue().getPreCallAllNum()+"")+")</br>");
													}
													%>
													</td>
												</tr>	
											</table>
											</td>
										</tr>
										<tr>
											<td>
												<table  width="100%"  class="table table-striped table-bordered table-condensed">
												<tr>
													<td colspan="<%=machineMap.size() + 6%>" align="center">调用Tddl的SQL操作前10名 &nbsp;&nbsp; <a href="<%=request.getContextPath()%>/show/tddlconsume.do?method=showTddlAll&opsName=<%=opsName%>&selectDate=<%= selectDate%>">查看全部</a></td>
												</tr>
												<tr>
													<td align="left">SQL操作</td>
													<%
														for(String name: machineMap.keySet()) {
													%>
														<td align="center">机房<%=name%></td>		
													<%
														}
													%>
													<td align="center">调用总计</td>
													<td align="center">数据库</td>
													<td align="center">操作状态</td>
													<td align="center"></td>
												</tr>
												<%
												for(int i=0;i< detailList.size()&&i<10;i++) {
													ConsumeDbDetail is = detailList.get(i);
												%>
												<tr>
													<td><%=is.getSqlText() %></td>
													<%
														for(String name: machineMap.keySet()) {
													%>
														<td align="center"><%=Utlitites.fromatLong(is.siteMap.get(name)+"")%></td>		
													<%
														}
													%>																	
													<td ><%=Utlitites.fromatLong(is.getExecuteSum()+"")%>&nbsp;<%=Utlitites.scale((is.getExecuteSum() + ""), (is.getExecuteSum() + ""))%></td>
													<td align="left"><%=is.getDbName()%></td>
													<td><%=is.getExecuteType()%></td>
													<td align="center" width="120px">
													<a href="javascript:openWin('<%=request.getContextPath()%>/show/tddlconsume.do?method=showTddlByMachine&opsName=${opsName}&selectDate=${selectDate}&executeType=<%=is.getExecuteType()%>&sqlText=<%=is.getSqlText()%>',700,1100)">机器分布</a>
													</td>
												</tr>
												<% }%>
												</table>
											</td>
										</tr>
									</table>												
								</td>
							</tr>								
						</table>
						</td>
						</tr>
					</table>
		</div>
	</form>
<script type="text/javascript">
//初始化search bar
$(document).ready(function(){
	
	$('#mm').accordion('select', "我的详细信息");
	changeColor('consumeTddl');
}); 
</script>
</body>
</html>