<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=GBK"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE html>

<html>
	<head>
		<title>实时采集配置信息</title>

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


		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/time_commons.js"></script>
		<!-- 		<script src="${base}/time/conf/key_list.js"></script> -->

<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript"   src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>

		<style type="text/css">
body {
	padding-top: 60px;
}

</style>
	</head>

	<body>
		<%@ include file="/header.jsp"%>
		<div class="container-fluid">
			<div class="row-fluid" style="text-align: center">
			<div class="span12" id="page_nav"></div>
				<script>
				$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {urlPrefix:'<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=',appId: '${appInfo.appId}'});
			</script>	
			</div>
			<div class="row-fluid">
				<div class="span2">
					<%@include file="/leftmenu.jsp"%>
				</div>
				<div class="span12">
					<a href="<%=request.getContextPath()%>/index.jsp">实时首页</a> ->
					实时采集配置信息 &nbsp;${appInfo.appName }
					<hr>
					<a id="btn-add-time" href="<%=request.getContextPath() %>/app_info.do?method=gotoCollectAdd&appId=${appInfo.appId}">添加实时配置</a>
					<a style="float:right" id="btn-add-time" href="<%=request.getContextPath() %>/app_info.do?method=changeconfig&appId=${appInfo.appId}">变更配置</a>
					<div class="row-fluid">
						<div class="span12">
						
							<div class="row">
								<div class="span12">
									<table
										class="table table-striped table-condensed table-bordered"
										id="table1" style="table-layout: fixed">
										<thead>
											<tr>
												<!-- class用于指定要显示的一些列，并按序显示 ；（so即show order的缩写，加前缀是为了和普通的class区分开来，这个是有逻辑意义的）-->
												<th style="width:15%; text-align: center;">
													实时配置文件名
												</th>
												<th style="width:36%; text-align: center;">
													实时配置文件路径
												</th>
												<th style="width:36%;text-align: center;">
													实时配置文件分析器
												</th>
												<th style="width:13%; text-align: center;">
													操作
												</th>
												
											</tr>
										</thead>
										<tbody>

											<c:forEach items="${list}" var="item">
												<tr>
													<td style="text-align: left;" title="">
														${ item.aliasLogName}
													</td>

													<td style="text-align: left;">
														${ item.filePath}
													</td>

													<td style="text-align: left;">
														${ item.className}
													</td>
												
													<td style="text-align: left;">
														<a
															href="<%=request.getContextPath() %>/app_info.do?method=gotoCollectUpdate&appId=${appInfo.appId}&confId=${item.confId}">修改</a>|
														<a
															href="<%=request.getContextPath() %>/app_info.do?method=collectDelete&appId=${appInfo.appId}&confId=${item.confId}" onclick="return confirm('确定要删除吗?')">删除</a>
														
													</td>
												</tr>
											</c:forEach>
									</table>

								</div>
							</div>


						</div>
					</div>
				</div>
			</div>
		</div>

	
</html>
