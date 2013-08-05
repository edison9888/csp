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
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/statics/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/statics/css/main.css" type="text/css" />

<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/userjs/mainpage.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/datePicket/WdatePicker.js"></script>
<title>依赖模块</title>

<%
String opsName = (String)request.getAttribute("opsName");
String selectDate = (String)request.getAttribute("selectDate");
if(selectDate == null || "null".equals(selectDate)) {
	selectDate = MethodUtil.getOneDayPre();
}
%>

</head>
<body>
<%@ include file="../../../header.jsp"%>
	<form id="mainForm"  action="<%=request.getContextPath() %>/show/tairconsume.do" method="get">
		<input type="hidden" value="showTairConsumeMain" name="method">
		<div style="text-align: center">
		<div id="page_nav"></div>
			</div>
		<div class="mainwrap">
		<script>
		$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {appName: '${param.opsName}', selectDate:'<%=selectDate%>'});
		</script>
			<table >
				<tr>
					<td valign="top">
						<table class="table table-striped table-bordered table-condensed">
							<tr>
								
								<td width="100%" valign="top">
									<table  width="100%" >
										<tr>
											<td >
													<h4>目前 没有发现<%=opsName %>调用对外Tair服务	！</h4>																					
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
	changeColor('consumeTair');
}); 
</script>
</body>
</html>