<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>ʵʱ���ϵͳ</title>
<script  type="text/javascript"	src="<%=request.getContextPath()%>/time/custom/map_po/top10.js"></script>
<script>
	$(document).ready(function(){
		var method = "${method}";
		var appName = "${appName}";
		var keyName = "${keyName}";
		var poType = "${poType}";
		var property = "${property}";
		var propertiesRaw = "${properties}";
		var properties = propertiesRaw.split(",");
		mapPoTop10Table({
			method : method,
			appName : appName,
			keyName : keyName,
			poType : poType,
			property : property,
			properties : properties
		});
	});	
</script>
</head>
<body>
		<table class="table table-striped table-bordered table-condensed" id="mapPoTable">
			<caption>
				<strong>${appName } ${keyName }</strong>
			</caption>
			<tbody id="mapPoTableBody">
			</tbody>
		</table>
</body>
</html>