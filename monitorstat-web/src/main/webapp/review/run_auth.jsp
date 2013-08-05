<%@page import="ch.ethz.ssh2.Connection"%>
<%@page import="com.taobao.monitor.common.util.RemoteCommonUtil"%>
<%@page import="java.io.IOException"%>
<%@page import="com.taobao.monitor.common.util.CspCacheTBHostInfos"%>
<%@page import="com.taobao.monitor.common.po.HostPo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%
List<AppInfoPo> appList = AppInfoAo.get().findAllEffectiveAppInfo();
try {
	out.print("Î´Í¨¹ýÑéÖ¤Ö÷»úÁÐ±í£º<br/>");
	for (int i = 0; i < appList.size(); i++) {
		try {
			AppInfoPo aip = appList.get(i);
			if (aip.getDayDeploy() == 0 && aip.getTimeDeploy() == 0) {
				continue;
			}
			String opsName = aip.getOpsName();
			List<HostPo> hostList = CspCacheTBHostInfos.get().getHostInfoListByOpsName(opsName);
			for (HostPo h : hostList) {
				String hostIp = h.getHostIp();
				String hostUser = h.getUserName();// Ö÷»úµÄÓÃ»§Ãû
				String hostPSW = h.getUserPassword();// Ö÷»úµÄÃÜÂë
				/* ²âÊÔÖ÷»úÊÇ·ñÔÚÏß */
				
				Connection con = null;
		try{
			con = new Connection(hostIp);
			con.connect(null,1000,1000);
			boolean b = con.authenticateWithPassword(aip.getLoginName(),aip.getLoginPassword());
			if(!b){
				out.print(h.getHostName()+"<br/>");
			}
		}catch (Exception e) {
		}finally{
			if(con!= null){
				con.close();
			}
		}
				
				
			}

		} catch (Exception e) {
		}
	}
} catch (IOException e1) {
	e1.printStackTrace();
} finally {
}


%>
</body>
</html>