<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>Insert title here</title>
<link href="<%=request.getContextPath() %>/statics/css/style.css" rel="stylesheet" type="text/css">
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet"/><script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.tabs.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/jquery.ui.widget.js"></script>
</head>
<body>
<form action="<%=request.getContextPath() %>/checkupdepend.do?method=manualCheckup" method="get">
<table width="1000"  class="table table-striped table-condensed table-bordered">
	<tr>
		<td colspan="2">
			应用<font color="greed">${config.opsName }</font>依赖应用<font color="greed">${config.targetOpsName }</font>的强弱依赖检查配置
			<input type="hidden" value="${config.opsName }" name="opsName">
			<input type="hidden" value="${config.targetOpsName }" name="targetOpsName">
		</td>
	</tr>
	<tr>
		<td>${opsName}应用IP:</td>
		<td>${config.selfIp }</td>
	</tr>
	<tr>
		<td>是否需要定时执行:</td>
		<td>
		${config.autoRun }		
		<div  id="autoRunTime"><c:if test="${config.autoRun=='yes' }">时间设置:${config.autoRunTime }</c:if></div>
		</td>
	</tr>
	<tr>
		<td>期望强弱:</td>
		<td>${config.expectDepIntensity }	</td>
	</tr>
	<tr>
		<td>阻隔方式:</td>
		<td>
			${config.preventType}
		</td>
	</tr>
	<tr>
		<td>阻隔配置:</td>
		<td>
		<div>${config.preventFeatrue}</div>
		<div><font id="preventDescId"></font></div>
		</td>
	</tr>
		<tr>
		<td>延迟方案:</td>
		<td>
			${config.delayType}
		</td>
	</tr>
	<tr id="delayDivId">
		<td>延迟配置:</td>
		<td>
		<div>${config.delayFeatrue}</div>
		<div></div>
		</td>
	</tr>
	<tr>
		<td>${opsName}启动脚本地址:</td>
		<td>${config.startupPath }</td>
	</tr>
	<tr>
		<td>可用性检查方式:</td>
		<td>
			<c:if test="${config.checkupType=='url'}">检查单个URL内容</c:if>
			<c:if test="${config.checkupType=='selenium'}">selenium脚本检查</c:if>
		</td>
	</tr>
	<tr id="checkupUrlDivId">
		<td>可用性检查URL:</td>
		<td>${config.checkupUrl }</td>
	</tr>
	<tr id="checkupContextDivId">
		<td>检查URL返回必须要包含内容:</td>
		<td>${config.checkupContext }</td>
	</tr>
	<tr id="seleniumScriptDivId">
		<td>可用性检查selenium配置脚本:</td>
		<td>
		<div>${config.startupPath }</div>
		</td>
	</tr>
	<tr >
		<td colspan="2" align="center"><input type="submit" value="执行强弱检查"></td>
	</tr>
</table>
</form>	
<script type="text/javascript">
	function preventDescChange(){
		var type = '${config.preventType}';
		if(type == 'IpTable') { 			
			$("#preventDescId").text("请输入需要屏蔽的IP名称，多个IP请用短号隔开 比如IP1,IP2");
		} else if(type == 'Btrace'){			
			$("#preventDescId").text("请输入需要屏蔽的接口名称，多个接口请用短号隔开 比如com.taobao.ic.ItemDao,com.taobao.ic.ItemDao");
		} 
	}

	function checkupDescChange(){
		var type =  '${config.checkupType}';
		if(type == 'url') { 			
			$("#checkupUrlDivId").show();
			$("#checkupContextDivId").show();
			$("#seleniumScriptDivId").hide();
		} else if(type == 'selenium'){			
			$("#checkupUrlDivId").hide();
			$("#checkupContextDivId").hide();
			$("#seleniumScriptDivId").show();
		} 
	}
	function delayDescChange(){
		var type = $("#delayType").val();
		if(type == 'nistnet') { 			
			$("#delayDescId").text("请输入需要延迟的IP名称，多个IP请用短号隔开 比如IP1,IP2");
			$("#delayDivId").show();
		} else if(type == 'Btrace'){			
			$("#delayDescId").text("请输入需要延迟的接口名称，多个接口请用短号隔开 比如com.taobao.ic.ItemDao,com.taobao.ic.ItemDao");
			$("#delayDivId").show();
		} else if(type == 'no'){			
			$("#delayDescId").text("不需要");
			$("#delayDivId").hide();
		} 
	}
	delayDescChange();
	preventDescChange();
	checkupDescChange();
</script>

</body>
</html>