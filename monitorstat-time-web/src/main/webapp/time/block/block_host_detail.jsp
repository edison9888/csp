<%@page import="com.taobao.csp.dataserver.Constants"%>
<%@page import="com.taobao.csp.time.web.po.TimeDataInfo"%>
<%@page import="com.taobao.csp.time.web.po.SortEntry"%>
<%@page import="com.taobao.csp.dataserver.PropConstants"%>
<%@page import="com.taobao.csp.time.util.DataUtil"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
<head>
<title>主机列表</title>
<%@ include file="/time/common/base.jsp"%>

<meta http-equiv="content-type" content="text/html;charset=gbk" />
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jsPlumb.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"		src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>
<script src="<%=request.getContextPath()%>/statics/js/amcharts/amcharts.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/statics/js/amcharts/raphael.js" type="text/javascript"></script>

<script type="text/javascript"	src="<%=request.getContextPath()%>/time/app_index.js"></script>

<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript"   src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>


<style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
      .sidebar-nav {
        padding: 9px 0;
      }
</style>
</head>
<body >
<%@ include file="/header.jsp"%>
<div class="container-fluid">
<div class="row-fluid" style="text-align: center">
	<div class="span12" id="page_nav"></div>
<script>
	$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {urlPrefix:'<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=',appId: '${appInfo.appId}'});
</script>	
<%
List<String> timeList  = (List<String>)request.getAttribute("timeList");
List<SortEntry<TimeDataInfo>>  blockList = (List<SortEntry<TimeDataInfo>>)request.getAttribute("blockList");
%>
</div>
	<div class="row-fluid">
        <div class="span2">
			<%@include file="/leftmenu.jsp"%>
		</div>
		<div class="span12">
			${appInfo.opsName}-Block，主机维度信息，括号内数据表示(Tdod Block---SS Block)
			<hr>
			<div class="row-fluid">
				<h4>主机列表</h4>
				<table class="table table-striped table-bordered table-condensed"  width="100%">
					<thead>
						<tr>
							<td style="text-align:center; width: 150px" align="right">IP</td>
							<%
								for(String fTime : timeList) {
									out.println("<td style='text-align:center;'>" + fTime + "</td>");									
								}
							%>
							<td style="text-align:center;">查看历史</td>
						</tr>
					</thead>
					<tbody id="exctbody">
						<%
							for(SortEntry<TimeDataInfo> entry : blockList) {
								out.println("<tr>");
								out.println("<td style=\"text-align:center;\">" + entry.getIp() + "</td>");
								for(String fTime : timeList) {
									
									if(entry.getObjectMap().containsKey(fTime)) {
										TimeDataInfo info = entry.getObjectMap().get(fTime);
										
										Object c = info.getOriginalPropertyMap().get(PropConstants.TDOD);
										Object l= info.getOriginalPropertyMap().get(PropConstants.PV_SS);
										
										out.println("<td style=\"text-align:center;\">" + DataUtil.transformLong(c) + "/" + DataUtil.transformLong(l) + "</td>");
									} else {
										out.println("<td style=\"text-align:center;\">0</td>");
									}
								}
								out.println("<td style=\"text-align:center;\"><a href='" + request.getContextPath() + "/app/detail/history.do?method=showHistoryByip&appName=" + entry.getAppName()+ "&keyName=" + 
										entry.getKeyName() + "&ip="+ entry.getIp() +"' target='_blank'>查看历史</a></td>");
								out.println("</tr>");
							}
						%>
					</tbody>
				</table>
			</div>
		</div>			
    </div>
</div>
</body>
</html>
