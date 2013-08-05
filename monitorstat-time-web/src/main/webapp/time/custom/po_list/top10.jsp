<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>实时监控系统</title>
<script  type="text/javascript"	src="<%=request.getContextPath()%>/time/custom/po_list/top10.js"></script>

<script>
	$(document).ready(function(){
		var method = "${method}";
		var appName = "${appName}";
		var keyName = "${keyName}";
		var property = "${property}";
		var propertiesRaw = "${properties}";
		var properties = propertiesRaw.split(",");
		poListTop10Table({
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
		<table class="table table-striped table-bordered table-condensed" id="poListTable">
			<caption>
				<strong>${appName } ${keyName }</strong>
			</caption>
			<tbody id="poListTableBody">
			</tbody>
		</table>
</body>
</html>