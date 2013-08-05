<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=GBK"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE html>

<html>
	<head>
		<title>${param.appName} 新增告警接收人</title>

		<%
			//设置base属性，生成绝对路径
			String serverUrl = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort();

			String base = serverUrl + request.getContextPath();

			request.setAttribute("base", base);
		%>

		<script>
 	var base="${base}";

 </script>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<Meta http-equiv="Content-Type" Content="text/html; Charset=gb2312">
		<link href="${base }/statics/css/bootstrap.css" rel="stylesheet">

		<script src="${base }/statics/js/jquery/jquery.min.js"></script>

		<script src="${base }/statics/js/bootstrap.js"></script>
		<script src="${base}/statics/js/date.js"></script>

<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery-ui-1.9.0/jquery-ui.js"></script>
	

		<style type="text/css">
body {
	padding-top: 60px;
}

#table1 td {
	word-wrap: break-word
}
</style>
	</head>

	<body>
		<%@ include file="/header.jsp"%>
		<div class="container-fluid">
			<div class="row-fluid" style="text-align: center">

			</div>
			<div class="row-fluid">
				<div class="span2">
				</div>
				<div class="span12">
						<div style="text-align: center;margin-bottom:18px"><h3 >${param.appName}  新增告警接收人</h3></div>
					<hr>
					<div class="row-fluid">
						<div class="span12">
							<div class="row" style="padding-bottom: 10px">
							</div>
							<div class="row">
								<div class="span12" >
								<form action="<%=request.getContextPath()%>/app/conf/alarmReceiverManage.do?method=addReceiver">
									<input type="text" name="email" id="email"/>
									
									<input type="hidden" name="appId" value="${param.appId}" />
									<input type="hidden" name="method" value="addReceiver" />
									<input type="submit" value="提交"/><red>${message}</red><a href="<%=request.getContextPath()%>/app/conf/alarmReceiverManage.do?method=receiverList&appId=${param.appId }&appName=${param.appName }">返回</a>
								</form>
								</div>
							</div>


						</div>
					</div>
				</div>
			</div>
		</div>
<script>
$(function(){
	$.ajax({
		  url: '<%=request.getContextPath()%>/app/conf/alarmReceiverManage.do?method=userAutoComplate',
		  dataType: 'json',
		  success: function(data) {
			  //alert(data.length);
			  $("#email").autocomplete({source:data});
		  },
		  async: false
		});
	
	
});


</script>
	
	
</html>
