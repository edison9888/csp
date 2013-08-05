<%@page import="com.taobao.csp.depend.util.MethodUtil"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.csp.depend.po.tair.TairConsumeDetailPo"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="java.util.ArrayList"%>
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
String detailTotal = (String)request.getAttribute("detailTotal");		

ArrayList<TairConsumeDetailPo> tairConsumeSummaryList = (ArrayList<TairConsumeDetailPo>)request.getAttribute("detailList");
if(tairConsumeSummaryList == null) {
	tairConsumeSummaryList = new ArrayList<TairConsumeDetailPo>();
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
				<div class="span12"><table width="100%"  class="table table-striped table-bordered table-condensed">
													<tr>
														<td align="center">Group Name</td>
														<td align="center">NameSpace</td>
														<td align="center">ActionType</td>
														<%
															for(String name: TairConsumeDetailPo.siteName) {
														%>
															<td align="center">机房<%=name%></td>		
														<%
															}
														%>
														<td align="center">调用次数</td>
														<td align="center">百分比</td>
														<td  align="center"></td>
													</tr>
													<%
													for(int i=0;i< tairConsumeSummaryList.size();i++) {
													TairConsumeDetailPo is = tairConsumeSummaryList.get(i);
													%>
													<tr>
														<td align="center"><%=is.getTair_group_name()%></td>
														<td align="center"><%=is.getNamespace()%></td>
														<td ><%=is.getAction_type()%></td>
														<%
															for(String name: TairConsumeDetailPo.siteName) {
														%>
														<td align="center"><%=Utlitites.fromatLong(is.siteMap.get(name)+"")%></td>		
														<%
															}
														%>									
														<td ><%=Utlitites.fromatLong(is.getCallNum()+"")%><%=Utlitites.scale((is.getCallNum()+""), (is.getPreCallNum()+""))%></td>
														<td ><%=Arith.mul(Arith.div(is.getCallNum(),Double.parseDouble(detailTotal),4), 100)%>%</td>
														<td align="center" width="60px">
														<a href="javascript:openWin('<%=request.getContextPath()%>/show/tairconsume.do?method=showMachine&opsName=${opsName}&selectDate=${selectDate}&tairgroupname=<%=is.getTair_group_name()%>&namespace=<%=is.getNamespace()%>&actiontype=<%=is.getAction_type()%>',700,1100)">机器分布</a>
														</td>
													</tr>
													<% }%>												
												</table>							</div>
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