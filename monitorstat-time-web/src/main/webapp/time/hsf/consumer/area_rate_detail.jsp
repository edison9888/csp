<%@page import="com.taobao.csp.dataserver.Constants"%>
<%@page import="com.taobao.csp.dataserver.KeyConstants"%>
<%@page import="java.util.Map.Entry"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!doctype html>
<html>
	<head>
		<title>机房调用比例--详情</title>
		<%@ include file="/time/common/base.jsp"%>

		<meta http-equiv="content-type" content="text/html;charset=gbk" />
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/jsPlumb.css">
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>
		<script
			src="<%=request.getContextPath()%>/statics/js/amcharts/amcharts.js"
			type="text/javascript"></script>
		<script
			src="<%=request.getContextPath()%>/statics/js/amcharts/raphael.js"
			type="text/javascript"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/time_commons.js"></script>

		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>

		<script>

</script>
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
	<body>

		<div class="container-fluid">
			<div class="row-fluid" style="text-align: center">
				<div class="span12">
				</div>
			</div>
			<div class="row-fluid">

				<div class="span12">
					<hr>
					<div class="row-fluid">
						<h4>
							机房调用比例详情
						</h4>
						<%
							String key  = request.getParameter("key");
						 	String[] parts = key.split(Constants.S_SEPERATOR);
						 	
						 %>
						应用${appInfo.appName}在<%=parts[1] %>机房中调用<%=parts[2] %>机房的HSF-provider：
						<table class="table table-striped table-bordered table-condensed"
							width="100%">
							<thead>
								<tr>
									<td style="text-align: center;">
										应用名称
									</td>
									
									<td style="text-align: center;">
										次数
									</td>
								
								</tr>
							</thead>
							<tbody id="exctbody">
							<%
								List<Entry<String,Object>> list= (List<Entry<String,Object>>)request.getAttribute("list");
							 %>
							<%
							 for(Entry<String,Object> e : list){
							 %>
									<tr >
										<td width="250">
											<%=e.getKey() %>
										</td>
										
									
										<td style="text-align: center;" >
											<%=e.getValue() %>
										</td>
									
									</tr>
							<%
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
