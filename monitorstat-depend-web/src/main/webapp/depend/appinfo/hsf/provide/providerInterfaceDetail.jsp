<%@page import="com.taobao.csp.depend.po.hsf.ProvideSiteRating"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.csp.depend.po.hsf.AppSummary"%>
<%@page import="java.util.Set"%>
<%@page import="com.taobao.csp.depend.po.hsf.AppConsumerSummary"%>
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
<!-- JQuery相关的JS都在leftmenu.jsp中定义了 -->
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/userjs/mainpage.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/datePicket/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/statics/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/statics/css/main.css" type="text/css" />
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet"/><title>依赖模块</title>
<%
Map<String,AppSummary> appSumMap = (Map<String,AppSummary>)request.getAttribute("appSumMap");


List<AppSummary> tmpList = new ArrayList<AppSummary>();
for(Map.Entry<String,AppSummary> entry:appSumMap.entrySet()){
	tmpList.add(entry.getValue());
}
Collections.sort(tmpList);

String opsName = (String)request.getAttribute("opsName");
String interfaceName = (String)request.getAttribute("interfaceName");
String selectDate = (String)request.getAttribute("selectDate");
Set<String> siteSet = ( Set<String>)request.getAttribute("siteSet");

Map<String,ProvideSiteRating> siteMap = (Map<String,ProvideSiteRating>)request.getAttribute("siteMap");

String preAllSumCall = (String)request.getAttribute("preAllSumCall");
String allSumCall = (String)request.getAttribute("allSumCall");


%>
</head>
<body>
<table   width="100%">
		<tr>
			<td><h3><%=opsName %>--提供接口:<%=interfaceName %></h3></td>
		</tr>
		<tr >				
			<td >
				<table width="100%" class="table table-striped table-bordered table-condensed">
				    <tr>
				    <td width="100" align="center">调用该方法的应用</td>
				    <td colspan="2" align="center">总流量(<%=Utlitites.fromatLong(allSumCall) %>&nbsp;<%=Utlitites.scale(allSumCall,preAllSumCall) %>)</td>
				    <%for(String siteName:siteSet){ %>
				    <td colspan="2" align="center"><%=siteName %>(<%=Utlitites.fromatLong(siteMap.get(siteName).getCallAllNum()+"")%>&nbsp;<%=Utlitites.scale(siteMap.get(siteName).getCallAllNum(),siteMap.get(siteName).getPreCallAllNum()) %>)</td>
				    <%} %>
				  </tr>   
				  <tr>
				    <td height="20" align="center" ><%=appSumMap.size() %></td>
				    <td width="130" align="center">当前</td>
				    <td width="130" align="center">上周</td>
				 <%for(String siteName:siteSet){ %>
				    <td width="130" align="center">当前</td>
				    <td width="130" align="center">上周</td>
				     <%} %>
				  </tr>
				  <% 
				  for(AppSummary appSum: tmpList){		
				  %>
				   <tr>
				    <td><a href="" style="text-decoration: none;" title="查看应用调用其他接口"><%=appSum.getOpsName() %></a></td>
				    <td><%=Utlitites.fromatLong(appSum.getCallAllNum()+"") %>(<%=Utlitites.scale(appSum.getCallAllNum(),appSum.getPreCallAllNum()) %>)</td>
				    <td><%=Utlitites.fromatLong(appSum.getPreCallAllNum()+"") %></td>
				     <%
				     for(String siteName:siteSet){ 
				    	 AppSummary siteAppSum = appSum.getSiteMap().get(siteName);
				    	 if(siteAppSum!=null){
				     %>
				   	<td><%=Utlitites.fromatLong(siteAppSum.getCallAllNum()+"") %>(<%=Utlitites.scale(siteAppSum.getCallAllNum(),siteAppSum.getPreCallAllNum()) %>)</td>
				    <td><%=Utlitites.fromatLong(siteAppSum.getPreCallAllNum()+"") %></td>
				     <%}else{
				    %>
				    	<td>&nbsp;&nbsp;</td>
				   		 <td>&nbsp;&nbsp;</td>
				    <% 
				     }} %>
				  </tr>  
				  <%} %> 
				</table>				
			</td>
		</tr>
	</table>
</body>
</html>