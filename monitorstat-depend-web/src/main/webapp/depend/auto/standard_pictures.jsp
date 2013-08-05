<%@ page language="java" contentType="text/html; charset=GB18030"
	pageEncoding="GB18030" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="com.taobao.csp.depend.util.StartUpParamWraper"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>检测过程描述页面</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link href="<%=request.getContextPath()%>/statics/css/bootstrap.css"
	rel="stylesheet">
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet"/><link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">

<script
	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"
	type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/statics/js/bootstrap.js"
	type="text/javascript"></script>
<script language="javascript" type="text/javascript"
	src="<%=request.getContextPath()%>/statics/datePicket/WdatePicker.js"></script>

<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
	
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/jquery.galleryview-3.0-dev.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.easing.1.3.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.galleryview-3.0-dev.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.timers-1.2.js"></script>
<%
  Map<String, List<String[]>> map = (Map<String, List<String[]>>) request
      .getAttribute("map");
  if (map == null) {
    map = new HashMap<String, List<String[]>>();
  }
  String[] appArray = (String[]) request.getAttribute("appArray");
  if (appArray == null)
    appArray = new String[0];
  
  //初始化的数组************
%>
<style type="text/css">
	.fancybox-custom .fancybox-skin {
		box-shadow: 0 0 50px #222;
	}
</style>
</head>
<body>

	<div class="container-fluid">
		<div style="text-align: center;">
			<h3>
				<span>12个核心应用检测标准检测过程</span>
			</h3>
		</div>
		<%
		  int i=0;
		  for (String opsName : map.keySet()) {
		    List<String[]> gallerylist = map.get(opsName);
			%>
			<div align="center">
			<h3>
				<span>应用：<%=opsName%></span>
			</h3>
			<ul id="gallery_<%=i++%>">
					<%
					  for (String[] rowStr : gallerylist) {
					      if (rowStr.length < 2)
					        continue;
					      out.println("<li><img src='ftp://" + StartUpParamWraper.getFtpIp()
					          + "/" + StartUpParamWraper.getFtpPath() + "/standard/" + opsName
					          + "/" + rowStr[1] + "' alt='" + rowStr[0] + "'/></li>");
					    }
					%>
				</ul>
			</div>
			&nbsp;<br/>
			<%
		  }
		%>
	</div>
</body>
</html>
<%
i=0;
for(String appName: appArray) {
  %>
  <script type="text/javascript">
    $(document).ready(function () {
    	alert($('#gallery' + '<%=i++%>'));
		$('#gallery' + '<%=i++%>').galleryView({
		    frame_width: 300,
		    frame_height: 150,
		    show_captions: true,
		    show_filmstrip_nav: false,
		    panel_scale: 'fit'
		});	   
    });
</script>
  <%
}
%>
