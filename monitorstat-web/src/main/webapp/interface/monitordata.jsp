
<%@page import="java.io.IOException"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.taobao.monitor.web.ao.MonitorTimeAo"%>
<%@page import="com.taobao.monitor.common.po.KeyValuePo"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>


<%
SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyyMMdd");
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
