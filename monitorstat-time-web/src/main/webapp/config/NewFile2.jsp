<%@page import="net.sf.json.JSONObject"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.Collections"%>
<%@page import="com.taobao.csp.time.util.DataUtil"%>
<%@page import="com.taobao.csp.time.util.TimeUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.csp.dataserver.Constants"%>
<%@page import="com.taobao.csp.dataserver.query.QueryUtil"%>
<%@page import="com.taobao.monitor.common.util.CspCacheTBHostInfos"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.taobao.csp.time.web.po.TimeDataInfo"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>Insert title here</title>
</head>
<body>
<%

String appName = request.getParameter("appName");
String key =  request.getParameter("keyName");
String mainProp =  request.getParameter("mainProp");

Map<String, List<TimeDataInfo>> childMap = new HashMap<String, List<TimeDataInfo>>();

try {
	Map<String, Map<String, Map<String, Object>>> map = QueryUtil.queryChildRealTime(appName, key);
	out.print("map size:"+map.size());
	out.print("<br/>");
	for (Map.Entry<String, Map<String, Map<String, Object>>> entry : map.entrySet()) {

		
		
		
		String fullName = entry.getKey().substring((appName).length() + 1);
		out.print("fullName:"+fullName);
		out.print("<br/>");
		String childName = entry.getKey().substring((appName + Constants.S_SEPERATOR + key).length() + 1);
		out.print("childName:"+childName);
		out.print("<br/>");
		Map<String, Map<String, Object>> timeMap = entry.getValue();

		List<TimeDataInfo> timeList = childMap.get(fullName);
		if (timeList == null) {
			timeList = new ArrayList<TimeDataInfo>();
			childMap.put(fullName, timeList);
		}
		out.print("timeMap:"+timeMap.size());
		out.print("<br/>");
		for (Map.Entry<String, Map<String, Object>> p : timeMap.entrySet()) {

			TimeDataInfo info = new TimeDataInfo();
			info.setAppName(appName);
			info.setKeyName(childName);
			info.setFullKeyName(fullName);
			info.setMainProp(mainProp);

			String time = p.getKey();
			
			out.print("time:"+time);
			out.print("<br/>");
			

			Map<String, Object> m = p.getValue();

			info.setTime(Long.parseLong(time));
			info.setFtime(TimeUtil.formatTime(info.getTime(), "HH:mm"));
			info.getOriginalPropertyMap().putAll(m);

			if (m.get(mainProp) != null){
				info.setMainValue(DataUtil.transformDouble(m.get(mainProp)));
				out.print("mainProp:"+m.get(mainProp));
				out.print("<br/>");
			}
			timeList.add(info);

		}
		
	}

} catch (Exception e) {
	out.print(e.getMessage()+"====<br/>");
}


for(Map.Entry<String, List<TimeDataInfo>> entry:childMap.entrySet() ){
	if(entry.getValue().size()>0){
		out.print(entry.getKey()+"====<br/>");
	
			for(TimeDataInfo td:entry.getValue()){
				out.print(td.getFtime()+"="+td.getMainValue()+"<br/>");
			}
		
		
	}
}

%>
</body>
</html>