<%@page import="com.taobao.csp.common.ZKClient"%>
<%@page import="java.util.*"%>
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
/*
String path = request.getParameter("path");
List<String> list = ZKClient.get().list("/csp/monitor/app");
for(String app : list) {
	if(app.indexOf("tair")>0) {
		out.println(app + "<br/>");
		//ZKClient.get().delete(path);
		path = "/csp/monitor/app/" + app;
		out.println("删除路径->" + path);
	}
}
*/
String path = request.getParameter("path");
		if(path != null) {
			out.println("删除路径->" + path);
			ZKClient.get().delete(path);
		}
%>
</body>
</html>