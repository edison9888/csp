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

Map<String, List<TimeDataInfo>> ipMap = new HashMap<String, List<TimeDataInfo>>();

List<String> ipList = CspCacheTBHostInfos.get().getIpsListByOpsName(appName);

try {
	Map<String, Map<String, Map<String, Object>>> map = QueryUtil.queryHostRealTime(appName, key, ipList);
	for (Map.Entry<String, Map<String, Map<String, Object>>> entry : map.entrySet()) {

		String ip = entry.getKey().substring((appName + Constants.S_SEPERATOR + key).length() + 1);

		Map<String, Map<String, Object>> timeMap = entry.getValue();

		List<TimeDataInfo> timeList = ipMap.get(ip);
		if (timeList == null) {
			timeList = new ArrayList<TimeDataInfo>();
			ipMap.put(ip, timeList);
		}

		for (Map.Entry<String, Map<String, Object>> p : timeMap.entrySet()) {

			TimeDataInfo info = new TimeDataInfo();
			info.setAppName(appName);
			info.setKeyName(key);
			info.setFullKeyName(entry.getKey().substring((appName).length() + 1));
			info.setMainProp(mainProp);
			info.setIp(ip);

			String time = p.getKey();

			Map<String, Object> m = p.getValue();

			info.setTime(Long.parseLong(time));
			info.setFtime(TimeUtil.formatTime(info.getTime(), "HH:mm"));
			info.getOriginalPropertyMap().putAll(m);

			if (m.get(mainProp) != null)
				info.setMainValue(DataUtil.transformDouble(m.get(mainProp)));
			
			out.print(ip+"="+time+"="+m.get(mainProp));
			
			timeList.add(info);
		}
		
		
		Collections.sort(timeList,new Comparator<TimeDataInfo>() {
			public int compare(TimeDataInfo o1, TimeDataInfo o2) {
				if(o1.getTime()>o2.getTime()){
					return -1;
				}else if(o1.getTime()<o2.getTime()){
					return 1;
				}
				return 0;
			}
		});
		
	}

} catch (Exception e) {
}







%>
</body>
</html>