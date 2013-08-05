<%@page import="com.taobao.csp.time.cache.TimeCache"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.cache.AppInfoCache"%>
<%@page import="com.taobao.monitor.common.util.GroupManager"%>
<%@page import="java.util.*"%>
<%@page import="com.taobao.monitor.common.util.CommonUtil"%>
<%@page import="com.taobao.monitor.common.util.CspCacheTBHostInfos"%>
<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%
String appId = request.getParameter("appId");
String showType = request.getParameter("showType");
AppInfoPo app = AppInfoCache.getAppInfoById(Integer.parseInt(appId));
String shownumber = request.getParameter("shownumber");

if(showType != null && showType.equalsIgnoreCase("dependinfo")) {	//��ʾ������Ϣ
	out.print(TimeCache.get().getHtml("http://127.0.0.1:8080/time/index.do?method=queryIndexTableForDepend&appId="+appId));
} else if (showType != null && showType.equalsIgnoreCase("minutetable")){	//��������ʾ����ͼ
	//give it to shownumber
	out.print(TimeCache.get().getHtml("http://127.0.0.1:8080/time/index.do?method=queryIndexTableForDepend&appId="+appId));
} else if(showType != null && showType.equalsIgnoreCase("groupinfo")){
	try {
		AppInfoPo appInfo = AppInfoCache.getAppInfoById(Integer.parseInt(appId));
		String appName = appInfo.getAppName();
		if(appName.equalsIgnoreCase("itemcenter")
				||appName.equalsIgnoreCase("sell")
				||appName.equalsIgnoreCase("tradeplatform")
				||appName.equalsIgnoreCase("ump")) {
			Map<String, String[]> map = CspCacheTBHostInfos.get().getCopyOfAppToGroupMap();
			if(map.containsKey(appName)) {
				for(String appCombine : map.get(appName)) {
					AppInfoPo groupAppInfo = AppInfoCache.getAppInfoByAppName(appCombine);
					if(groupAppInfo == null) {
						out.println("<hr>" + appCombine + "û���ҵ�Ӧ����Ϣ<hr>");
					} else {
						if("pv".equals(app.getAppType())){
							out.print(TimeCache.get().getHtml("http://127.0.0.1:8080/time/index.do?method=queryIndexTableForPv&appId="+groupAppInfo.getAppId()));
						}
						if("center".equals(app.getAppType())){
							out.print(TimeCache.get().getHtml("http://127.0.0.1:8080/time/index.do?method=queryIndexTableForGroup&appId="+groupAppInfo.getAppId()));
						}
						out.println("<hr>");
					}
				}				
			}
		} else {
			out.println("��Ӧ���޷�����Ϣ����鿴Ӧ����Ϣѡ���");
		}
	} catch(Exception e) {
		out.println("������Ϣ��ʾ����:" + e.toString());
	}
} else {	//���ݰ汾�ĵ���
	if("pv".equals(app.getAppType())){
		out.print(TimeCache.get().getHtml("http://127.0.0.1:8080/time/index.do?method=queryIndexTableForPv&appId="+appId));
	}
	if("center".equals(app.getAppType())){
		out.print(TimeCache.get().getHtml("http://127.0.0.1:8080/time/index.do?method=queryIndexTableForHSf&appId="+appId));
	}	
}
%>