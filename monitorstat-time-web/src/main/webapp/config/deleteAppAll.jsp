<%@page import="com.taobao.csp.common.ZKClient"%>
<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>Insert title here</title>
</head>
<body>
<%

//String appName = request.getParameter("appName");

out.println("��ʼɾ��");
String deleteApp = "serviceone tvb vipcenter commonway radar tee ims lifemap hunter lottery etao_activity marketing tmallmeizmalllist matrixuc mine wlbexternal  ocean.gateway.normal matrixfa jiazhuang matrixfc auctionplatform nova govauction onlinecs wlbmywlb jzplatform delta tmallmeizadmin support tmobile picturespider relationrecommend umf ticket vipmanager servicecenter deptadmin wlbconsole softwarestore mbis at ishopbook picturecenter supply treasure lifeplacelistweb sellermanager lifeplaceweb";
String[] appNameArray = deleteApp.split(" ");
out.println("��ɾ����" + appNameArray.length + "��Ӧ��");
for(String appName : appNameArray) {
	out.println("ɾ��->" + appName);
	ZKClient.get().delete("/csp/monitor/app/"+appName.trim());	
}
%>
</body>
</html>