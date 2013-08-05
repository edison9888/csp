<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=GBK"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>




<!DOCTYPE html>

<html>
	<head>
		<title></title>

		<%
			//设置base属性，生成绝对路径
			String serverUrl = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort();

			String base = serverUrl + request.getContextPath();

			request.setAttribute("base", base);
		%>

		<script>
 	var base="${base}";
 	var page = {};
 	page.appId = "${appInfo.appId}";
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
		<!-- 弹窗效果 -->
		<script src="${base }/statics/js/tinybox.js"></script>


		<script src="${base}/time/conf/app_url_list.js"></script>
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>


		<style type="text/css">
 body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
      .sidebar-nav {
        padding: 9px 0;
      }

/*弹窗效果 begin*/
#tinybox {
	position: absolute;
	display: none;
	padding: 10px;
	background: #ffffff url(${base}/common_res/images/preload.gif) no-repeat
		50% 50%;
	border: 10px solid #e3e3e3;
	z-index: 2000;
}

#tinymask {
	position: absolute;
	display: none;
	top: 0;
	left: 0;
	height: 100%;
	width: 100%;
	background: #000000;
	z-index: 1500;
}

#tinycontent {
	background: #ffffff;
	font-size: 1.1em;
}
/*弹窗效果 end*/
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
					应用URL管理 &nbsp;${appInfo.appName }
					<hr>
					<div class="row-fluid">
						<div class="span12">
							<div class="row" style="padding-bottom: 10px">
								<div class="span12" style="text-align: right;">
									<a class="btn  primary " href="${base}/app/conf/url/show.do?method=edit&appId=${appInfo.appId}">添加一个AppUrl映射</a>
								</div>

							</div>
							<div class="row">

								<div class="span12">


									<table class="table table-striped table-condensed table-bordered"
										id="table1">
										<thead>
											<tr>
												<!-- class用于指定要显示的一些列，并按序显示 ；（so即show order的缩写，加前缀是为了和普通的class区分开来，这个是有逻辑意义的）-->
												<th class="so_appName blue">
													应用名
												</th>
												<th class="so_appUrl blue">
													应用URL
												</th>
												<th class="so_topUrl blue">
													TopURL
												</th>
												<th class="so_modifyDate blue">
													修改时间
												</th>
												<th class="so_dynamic blue">
													动态
												</th>
												<th class="so_operate blue">
													操作
												</th>
											</tr>
										</thead>
										<tbody>


											<!-- 	 <tr>
							<td>
								1
							</td>
							<td>
								1
							</td>
							<td>
								1
							</td>
							<td>
								1
							</td>
							<td>
								1
							</td>
						</tr>
						<tr>
							<td>
								2
							</td>
							<td>
								2
							</td>
							<td>
								2
							</td>
							<td>
								2
							</td>
							<td>
								2
							</td>
						</tr>  -->
										</tbody>

									</table>
								</div>
							</div>


						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- 作为拷贝源，这里必须要设置z-index，display两个属性 -->
		<div id="confirm1" style="z-index: 100; display: none">
			<div class="alert-message block-message warning">
				<a class="close confirm1_close" href="#">×</a>
				<p>
					"想清楚要删除了没有啊"
				</p>
				<div class="alert-actions">
					<!-- class name 中yes no 有逻辑意义-->
					<a class="btn small confirm1_yes" href="#">是</a>
					<a class="btn small confirm1_no" href="#">否</a>
				</div>
			</div>
		</div>

</html>
