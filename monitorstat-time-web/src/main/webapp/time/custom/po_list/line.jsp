<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>Insert title here</title>
<script src="<%=request.getContextPath()%>/time/custom/po_list/line.js" type="text/javascript"></script>
<script>
	$(document).ready(function(){
		var method = "${method}";
		var appName = "${appName}";
		var keyName = "${keyName}";
		var property = "${property}";
		var propertiesRaw = "${properties}";
		var properties = propertiesRaw.split(",");
		line({
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
	<div id="chartdiv" style="width:100%; height:400px;"></div>
</body>
</html>