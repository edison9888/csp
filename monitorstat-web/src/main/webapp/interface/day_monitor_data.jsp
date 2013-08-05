<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>Day Monitor</title>

<%
SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd");

String appId = request.getParameter("appId");

String keyId = request.getParameter("keyId");

String collectTime = request.getParameter("collectTime");

String returnType = request.getParameter("returnType");

if(returnType == null){
	returnType = "json";
}


Map<String, KeyValuePo> map = MonitorTimeAo.get().findKeyValueByDate(Integer.parseInt(appId), Integer.parseInt(keyId), parseLogFormatDate.parse(collectTime)) ;
Map<String,String> valueTimeMap = new HashMap<String,String>();
for(Map.Entry<String, KeyValuePo> entry:map.entrySet()){
	valueTimeMap.put(entry.getKey(),entry.getValue().getValueStr());
}
if("json".equals(returnType)){
	JSONObject json = JSONObject.fromObject(valueTimeMap);
	response.setContentType("text/html;charset=gbk"); 
	try {
		response.getWriter().write(json.toString());
		response.flushBuffer();
	} catch (IOException e) {
	}	
	return ;	
}
%>
</head>
<body>

</body>
</html>