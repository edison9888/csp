<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=GBK"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE html>

<html>
	<head>
		<title>${param.appName} 告警接收人列表</title>

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
		<link href="${base }/statics/css/bootstrap.css" rel="stylesheet">

		<script src="${base }/statics/js/jquery/jquery.min.js"></script>

		<script src="${base }/statics/js/bootstrap.js"></script>
		<script src="${base}/statics/js/date.js"></script>

<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
	

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
						<div style="text-align: center;margin-bottom:18px"><h3 >${param.appName}  告警接收人列表</h3></div>
					<hr>
					<div class="row-fluid">
						<div class="span12">
							<div class="row" style="padding-bottom: 10px">
								<a href="<%=request.getContextPath()%>/app/conf/alarmReceiverManage.do?method=addReceiverIndex&appId=${param.appId}&appName=${param.appName}">新增告警接收者</a>
							</div>
							<div class="row">

								<div class="span12" >
								

									<table
										class="table table-striped table-condensed table-bordered"
										id="table1" style="width:50%;margin-left:25%">
											<tr>
												<th style="text-align: center;">
													用户名
												</th>
												<th style=" text-align: center;">
													操作
												</th>
											</tr>

											<c:forEach items="${userList}" var="item">
												<tr>
													<td style="" title="">

													 ${ item.wangwang} 	${ item.mail}
 
													</td>


													<td style=" text-align: center;">
														<a href="javascript:void(0)" onclick="deleteReceiver('${item.mail}')">删除</a>
													</td>

							

												</tr>


											</c:forEach>
											
											<script>
												function deleteReceiver(email){
													var url = "<%=request.getContextPath()%>/app/conf/alarmReceiverManage.do?method=deleteReceiver&appId=${param.appId }&appName=${param.appName}&email="+email;
													
													window.location.href=url;
													
												}
											
											</script>
									</table>
					
								</div>
							</div>


						</div>
					</div>
				</div>
			</div>
		</div>
 
	
	
</html>
