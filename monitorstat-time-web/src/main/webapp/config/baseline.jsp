<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Arrays"%>
<%@page import="com.taobao.csp.time.util.MonitorAppUtil"%>
<%@page import="com.taobao.csp.alarm.baseline.BaseLineProcessHandle"%>
<%@page import="com.taobao.csp.alarm.baseline.HostBaseLineProcessor"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="com.taobao.monitor.common.util.CspCacheTBHostInfos"%>
<%@page import="com.taobao.csp.alarm.baseline.AppBaseLineProcessor"%>
<%@page import="com.taobao.csp.alarm.baseline.BaseLineProcessor"%>
<%@page import="com.taobao.monitor.common.po.CspKeyInfo"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.taobao.monitor.common.ao.center.KeyAo"%>
<%@page import="com.taobao.monitor.common.po.CspKeyMode"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.csp.alarm.AlarmKeyContainer"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<%

Thread thread  = new Thread(){
	public void run(){
		
		List<String> appList = new ArrayList<String>();
		//appList.addAll(Arrays.asList( 
			//	"login", "tf_tm", "detail", "tf_buy", "uicfinal", "ump","itemcenter","tradeplatform"));
		//appList.addAll(Arrays.asList("tmallpromotion","malllist","wlb","sic","tmallsell","pointcenter","tmallbuy","malldetail","wlbexternal","memberprofile"));
			for(String app:appList){
				for(int i=0;i<7;i++){
							Calendar cal = Calendar.getInstance();
							cal.add(Calendar.DAY_OF_MONTH, -1);
							BaseLineProcessHandle handle = new BaseLineProcessHandle(null);
							try{
								handle.runBaseLine(app,cal.getTime());
							}catch (Exception e) {
							}
					}
			}
	}
};
thread.setName("Thread-Baseline");
thread.setDaemon(false);
thread.start();

%>
</body>
</html>