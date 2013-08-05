<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.HostPo"%>
<%@page import="java.util.ArrayList"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>查看监控主机</title>
<link type="text/css"  href="<%=request.getContextPath() %>/common_res/css/bootstrap.css" rel="stylesheet">
<script type="text/javascript" src="<%=request.getContextPath() %>/common_res/js/jquery.js"></script>

<%
AppInfoPo appInfoPo = (AppInfoPo)request.getAttribute("appInfoPo");
%>
<script>
function checkAppHost() {
	var str = "<%=request.getContextPath()%>/show/appconfig.do?method=checkAppHost&appId=<%=appInfoPo.getAppId()%>";
	location.href = str;
}

function singleAddAppHost() {
	var saveData = "";
	if($("#saveData1").attr("checked")==undefined) {
		saveData += 0;
	} else {
		saveData += 1;
	}
	if($("#saveData2").attr("checked")==undefined) {
		saveData += 0;
	} else {
		saveData += 1;
	}
	var hostName = $("#hostName").val();
	var hostSite = $("#hostSite").val();
	var hostIp=$("#hostIp").val();
	
	var str = "<%=request.getContextPath()%>/show/appconfig.do?method=singleAddAppHost&appId=<%=appInfoPo.getAppId()%>&hostName="+hostName+"&hostSite="+hostSite+"&hostIp="+hostIp+"&saveData="+saveData;

	location.href = str;
}
</script>
</head>
<body>
	<jsp:include page="../../../header.jsp"></jsp:include>
	<div class="container" style="padding-top: 60px;">
      <div class="content">
        <div class="page-header">
          <h1>应用：<%=appInfoPo.getOpsName() %> <small>单个添加主机关联</small></h1>
        </div>
        <table class="zebra-striped condensed-table bordered-table">
			<tr>	  	
				<td align="center">主机名</td>
				<td align="center">主机IP</td>
				<td align="center">机房</td>
				<td align="center"><input name='check' type='checkbox' id='checkSaveData1'>&nbsp;&nbsp;临时表</td>
				<td align="center"><input name='check' type='checkbox' id='checkSaveData2'>&nbsp;&nbsp;持久表</td>
		  </tr>
			<tr>
				<td align="center" class="hostName"><input type="text" id="hostName" value=""></td>
				<td align="center" class="hostIp"><input type="text" id="hostIp" value=""></td>
				<td align="center" class="hostSite"><input type="text" id="hostSite" value=""></td>
				<td align="center" class="saveData1"><input type="checkBox" name="saveData1" id="saveData1" class="saveData1" value="1" checked="checked"></td>
				<td align="center" class="saveData2"><input type="checkBox" name="saveData2" id="saveData2" class="saveData2" value="1"></td>
			</tr>	
		</table>
		
		<div class="well" style="padding: 14px 19px;">
			<center>
	        	<input class="btn primary" type="button" value="添加主机关联" onclick="singleAddAppHost()">
				<input class="btn primary" type="button" value="返回" onclick="checkAppHost()">
			</center>
      	</div>
      </div>

      <footer>
        <p>&copy; Taobao 2011</p>
      </footer>

    </div> <!-- /container -->
</body>
</html>