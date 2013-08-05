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
			src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/userjs/mainpage.js"></script>
		<script language="javascript" type="text/javascript"
			src="<%=request.getContextPath()%>/statics/datePicket/WdatePicker.js"></script>
		<script src="<%=request.getContextPath()%>/statics/js/amcharts/flash/swfobject.js" type="text/javascript"></script>
		<%
		  String type = (String) request.getAttribute("type");
		  String startDate = (String) request.getAttribute("startDate");
		  String endDate = (String) request.getAttribute("endDate");
		  String opsName = (String) request.getAttribute("opsName");
		  String url = (String) request.getAttribute("url");

		  if (type == null)
		    type = "consume";
		%>

		<title>
	<%
	  String dataUrl = request.getContextPath();
	  String dataUrl2 = request.getContextPath();
	  String formSubUrl = request.getContextPath();
	  if ("origin".equals(type)) {
	    out.println("来源URL历史统计");
	    dataUrl += "/show/urlorigin.do?method=getUrlOriginTotalHistoryGraphData&startDate="
	        + startDate
	        + "&endDate="
	        + endDate
	        + "&opsName="
	        + opsName;
	    
	    dataUrl2 += "/show/urlorigin.do?method=getUrlOriginHistoryGraphData&startDate="
	        + startDate
	        + "&endDate="
	        + endDate
	        + "&opsName="
	        + opsName + "&url=" + url;
	    
	    formSubUrl += "/show/urlorigin.do?method=showUrlHistoryGraph";
	  } else {
	    dataUrl += "/show/urlorigin.do?method=getUrlRequestTotalHistoryData&startDate="
	        + startDate
	        + "&endDate="
	        + endDate
	        + "&opsName="
	        + opsName;
	    
	    dataUrl2 += "/show/urlorigin.do?method=getUrlRequestHistoryData&startDate="
	        + startDate
	        + "&endDate="
	        + endDate
	        + "&opsName="
	        + opsName + "&url=" + url;
	    out.println("实际跳转URL历史统计");
	    
	    formSubUrl += "/show/urlorigin.do?method=showUrlHistoryGraph";
	  }
	  
    if(url != null) {
      url = url.replaceAll("!", ":");
    }
	%>
</title>
	</head>
	<body>
	<div>
		<div align="center">
			<h1>
				<%
				  if ("origin".equals(type)) {
				    out.println("来源URl信息历史统计");
				  } else {
				    out.println("时间跳转URl历史统计");
				  }
				%>
			</h1>
		</div>
		<form action="<%=formSubUrl%>" method="post">
	<input type="hidden" value="${opsName}" name="opsName" id="opsName">
	<input type="hidden" value="<%=url%>" name="url" id="url">
	<input type="hidden" value="<%=type%>" name="type" id="type">
	<table>
		<tr>
			<td>开始日期:<input type="text" id="startDate" value="${startDate}" name="startDate" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})" class="span2"/><td>
			<td>结束日期:<input type="text" id="endDate" value="${endDate}" name="endDate" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})" class="span2"/><td>
		</tr>
		<tr>
			<td colspan="2"><input type="submit" value="查询" class="btn btn-success"></td>
		</tr>
	</table>
	</form>
	</div>
	<div align="center">
		<%
		  if ("origin".equals(type)) {
		    %>
		    <h5>应用${opsName}的URl总请求量</h5>
		    <%
		  } else {
		    %>
		    <h5>应用${opsName}的URL总跳转量</h5>
		    <%
		  }
		%>
		<div class="thumbnail" style="height: 250px" id="appTotalDivId">
			<script type="text/javascript">
						 var params = 
				            {
				                bgcolor:"#FFFFFF",
				                wmode:"transparent"
				            };
						
						var flashVars = 
			            {
			                path: "<%=request.getContextPath()%>/statics/js/amcharts/flash/",
			                settings_file: "<%=request.getContextPath()%>/statics/js/amcharts/flash/setting/amline_settings1.xml",
			                data_file: escape("<%=dataUrl%>")
			            };
						
						swfobject.embedSWF("<%=request.getContextPath()%>/statics/js/amcharts/flash/amline.swf", 
								"appTotalDivId", "100%", "350", "8.0.0", "<%=request.getContextPath()%>/statics/js/amcharts/flash/expressInstall.swf",flashVars, params);
		</script>
		</div>
	</div>
	<div align="center">
		<%
		  if ("consume".equals(type)) {
		    %>
		    <h5>应用${opsName}被URL"<%=url%>"请求的历史情况</h5>
		    <%
		  } else {
		    %>
		      <h5>应用${opsName}跳转到URL"<%=url%>"的历史情况</h5>
		    <%
		  }
		%>
		<div class="thumbnail" style="height: 250px" id="AppSingleId">
			<script type="text/javascript">
						 var params = 
				            {
				                bgcolor:"#FFFFFF",
				                wmode:"transparent"
				            };
						var flashVars = 
			            {
			                path: "<%=request.getContextPath()%>/statics/js/amcharts/flash/",
			                settings_file: "<%=request.getContextPath()%>/statics/js/amcharts/flash/setting/amline_settings1.xml",
			                data_file: escape("<%=dataUrl2%>")
			            };
						swfobject.embedSWF("<%=request.getContextPath()%>/statics/js/amcharts/flash/amline.swf", 
								"AppSingleId", "100%", "350", "8.0.0", "<%=request.getContextPath()%>/statics/js/amcharts/flash/expressInstall.swf",
							flashVars, params);
		</script>
		</div>
	</div>

</body>
</html>