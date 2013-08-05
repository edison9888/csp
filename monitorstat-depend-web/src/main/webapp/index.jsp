<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030" isELIgnored="false"%>
<%@page import="java.util.Map"%>    
<%@page import="java.util.HashMap"%>    
<%@page import="java.util.List"%>    
<%@page import="java.util.TreeSet"%>    
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0">
<title>依赖系统首页</title>
<link href="<%=request.getContextPath()%>/statics/css/bootstrap.css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/userjs/mainpage.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/statics/datePicket/WdatePicker.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/statics/js/swfobject.js"></script>
<style type="text/css">
td {
	word-break:break-all;
}
</style>
</head>
<body>
<% 
Map allAppInfoMap = (Map)request.getAttribute("allAppInfoMap");
Map<String, List<String>> appListMap = null;
if(allAppInfoMap != null) {
  appListMap = (Map<String, List<String>>)allAppInfoMap.get("appListMap");
  if(appListMap == null) {
    appListMap = new HashMap<String, List<String>>();
  }
}
TreeSet<String> set = new TreeSet<String>(appListMap.keySet());
%>
<%@ include file="depend/header.jsp"%>
<div class="container" style="width: 90%">
		<h1 align="center">欢迎来到依赖系统（新版本系统首页）<img src="<%=request.getContextPath()%>/statics/css/images/newhotcool.gif" /></h1>
			<form action="<%=request.getContextPath()%>/main/appmain.do" method="get">
						<input type="hidden" value="showAppIndexMain" name="method">
							<div id="page_nav"></div>
				<script>
					$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {appName: '${opsName}', selectDate:'${selectDate}'});
				</script>
			</form>		
			<a href="#">点击下面应用超链接进入应用主页。</a>没有找到应用请加入群“<strong>CSP答疑群</strong>”（911658553），申请添加应用
<table class="table table-striped table-bordered table-condensed" > 
<%
	for(String company: set) {
	  List<String> appNameList = appListMap.get(company);
	  company = company.replace('`', ' ');
	  out.print("<tr><td><strong>" + company + "</strong>&nbsp;&nbsp;&nbsp;");
	  for(String appName: appNameList) {
	    out.print("<a href='" + request.getContextPath() + "/main/appmain.do?method=showAppIndexMain&opsName=" + appName + "'>" + appName + "</a>" + "&nbsp;&nbsp;&nbsp;");
	  }
	  out.print("</td></tr>");
	}
%>
</table>
</div>
</body>
</html>