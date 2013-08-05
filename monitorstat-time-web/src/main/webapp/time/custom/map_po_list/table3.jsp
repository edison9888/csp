<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>实时监控系统</title>
<%@ include file="/time/common/base.jsp"%>
<meta http-equiv="content-type" content="text/html;charset=gbk" />
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/time_commons.js"></script>
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
<script type="text/javascript"
	src="<%=request.getContextPath()%>/time/custom/map_po_list/table.js"></script>

<script>
	$(document).ready(function(){
		var method = "${method}";
		var appName = "${appName}";
		var keyName = "${keyName}";
		var property = "${property}";
		var propertiesRaw = "${properties}";
		var properties = propertiesRaw.split(",");
		mapPoListTable({
			method : method,
			appName : appName,
			keyName : keyName,
			property : property,
			properties : properties
		});
	});	
</script>
</head>
<body>
	<table class="table table-striped table-bordered table-condensed"
		id="mapPoListTable">
		<caption>
			<strong>${appName } ${keyName }</strong>
		</caption>
		<thead>
			<tr>
				<td width="200" style="text-align: center;">KEY</td>
				<td style="text-align: center;" id="time1">10:21</td>
				<td style="text-align: center;" id="time2">10:22</td>
				<td style="text-align: center;" id="time3">10:23</td>
				<td style="text-align: center;" id="time4">10:24</td>
				<td style="text-align: center;" id="time5">10:25</td>
				<td style="text-align: center;" id="time6">10:26</td>
				<td style="text-align: center;" id="time7">10:27</td>
				<td style="text-align: center;" id="time8">10:28</td>
				<td style="text-align: center;" id="time9">10:29</td>
				<td style="text-align: center;" id="time10">10:30</td>
			</tr>
		</thead>
		<tbody id="mapPoListTableBody">
		</tbody>
	</table>
	</body>
</html>