<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>

<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>


<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Collections"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>²ÊÆ±ÊµÊ±¼à¿Ø</title>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript">
	
	function reinitIframe(down){
		var pTar = null;
		if (document.getElementById){
		pTar = document.getElementById(down);
		}
		else{
		eval('pTar = ' + down + ';');
		}
		if (pTar && !window.opera){
		//begin resizing iframe
		pTar.style.display="block"
		if (pTar.contentDocument && pTar.contentDocument.body.offsetHeight){
		//ns6 syntax
		pTar.height = pTar.contentDocument.body.offsetHeight +20;
		pTar.width = pTar.contentDocument.body.scrollWidth;
		}
		else if (pTar.Document && pTar.Document.body.scrollHeight){
		//ie5+ syntax
		pTar.height = pTar.Document.body.scrollHeight;
		pTar.width = pTar.Document.body.scrollWidth;
		}
		} 
	}
	
</script>

</head>
<body>
<center>
<jsp:include page="../head.jsp"></jsp:include>
</center>
<%
List<String> appNameList = new ArrayList<String>();
appNameList.add("lottery");
appNameList.add("lotteryraffle");
%>

<center>
<font size="5" color="red">²ÊÆ±ÊµÊ±¼à¿ØÊ×Ò³</font>
</center>

<%	
for(String appName:appNameList){
	AppInfoPo appInfo = AppInfoAo.get().getAppInfoByAppName(appName);
%>
<center >
<iframe width="98%" algin="center" id="showmian_<%=appName %>" onload="reinitIframe('showmian_<%=appName %>')" src="../index_time_show.jsp?appName=<%=appName %>&appId=<%=appInfo.getAppId() %>" frameborder="0" height="0" width="100%" marginheight="0" marginwidth="0" scrolling="no"></iframe>
<%} %>

 <br></br>
<table>
   <tr>
      <td><img  src="<%=request.getContextPath () %>/statics/images/line.png"  width="1000" height="1"></td>
   </tr>
</table> 
<jsp:include page="../bottom.jsp"></jsp:include>

</center>
</body>
</html>