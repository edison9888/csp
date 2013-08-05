<%@page import="com.taobao.csp.depend.util.MethodUtil"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.csp.depend.po.tair.ActionSummaryPo"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.csp.depend.po.tair.TairConsumeSummaryPo"%>
<%@page import="com.taobao.csp.depend.po.hsf.ProvideSiteRating"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.csp.depend.po.tair.TairConsumeDetailPo"%>

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

<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/userjs/mainpage.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/statics/datePicket/WdatePicker.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/statics/js/swfobject.js"></script>
<title>我消费的Tair信息</title>
<%
String opsName = (String)request.getAttribute("opsName");
String selectDate = (String)request.getAttribute("selectDate");
String allCallNum = (String)request.getAttribute("allCallNum");		

ArrayList<TairConsumeSummaryPo> tairConsumeSummaryList = (ArrayList<TairConsumeSummaryPo>)request.getAttribute("tairConsumeSummaryList");
if(tairConsumeSummaryList == null) {
	tairConsumeSummaryList = new ArrayList<TairConsumeSummaryPo>();
}
%>

</head>
<body>
<%@ include file="../../../header.jsp"%>
	<form id="mainForm"  action="<%=request.getContextPath()%>/show/tairconsume.do" method="get">
		<input type="hidden" value="showTairConsumeMain" name="method">
<div style="text-align: center">
				<div id="page_nav"></div>
			</div>

		<script>
		$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {appName: '${param.opsName}', selectDate:'<%=selectDate%>'});
		</script>
		
		<div class="container">
			<div class="row">
				<div class="span12"><span style="font-weight:bold">&nbsp;&nbsp;--<%=opsName%>--的Tair消费信息</span></div>
			</div><!-- row -->
			<div class="row">
				<div class="span12">	<table width="100%"  class="table table-striped table-bordered table-condensed">
													<tr>
														<td>TairGroupName</td>
														<td style="display: none;">NameSpace</td>
														<td>调用次数</td>
														<td>百分比</td>
														<td></td>
													</tr>																				
												<%
													for(int i=0;i<tairConsumeSummaryList.size();i++) {
														TairConsumeSummaryPo is = tairConsumeSummaryList.get(i);
												%>
													<tr>
												<td ><%=is.getTair_group_name()%></td>
												<td  style="display: none;"><%=is.getNamespace()%></td>
												<td ><%=Utlitites.fromatLong(is.getInvoking_all_num()+"")%><%=Utlitites.scale((is.getInvoking_all_num()+""), (is.getPrecall()+""))%></td>
												<td ><%=Arith.mul(Arith.div(is.getInvoking_all_num(), Double.parseDouble(allCallNum),4), 100)%>%</td>
												<td align="center" width="60px">
												<a href="javascript:openWin('<%=request.getContextPath()%>/show/tairconsume.do?method=showTairByTairGroupName&opsName=${opsName}&selectDate=${selectDate}&tairgroupname=<%=is.getTair_group_name()%>',700,1100)">查看详细</a>
												</td>
												</tr>
												<% }%>																		
												</table>						</div>
			</div><!-- row -->
		
		</div><!-- container -->
		
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