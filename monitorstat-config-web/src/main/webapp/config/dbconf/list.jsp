<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=GBK"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>




<!DOCTYPE html>

<html>
	<head>
	<%@ include file="/common/base.jsp" %>
		<title></title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link href="${base}/common_res/css/bootstrap.css" rel="stylesheet">
		<script src="${base}/common_res/js/jquery.js"></script>
		<!-- bootstrap begin -->
		<script src="${base}/common_res/js/bootstrap-alerts.js"></script>
		<!-- bootstrap begin -->

		<script src="${base}/common_res/js/jquery.tablesorter.js"
			charset="utf-8"></script>

		<!-- 弹窗效果 -->
		<script src="${base}/common_res/js/tinybox.js"></script>

		<script src="${base}/config_res/dbconf/list.js"></script>


		<style type="text/css">
body {
	padding-top: 60px;
}

/*弹窗效果 begin*/
#tinybox {
	position: absolute;
	display: none;
	padding: 10px;
	background: #ffffff url(${base}/common_res/images/preload.gif) no-repeat 50% 50%;
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
			<%@ include file="/leftmenu.jsp"%>
			<div class="content">
				<div class="row" style="padding-bottom: 10px">
					<div class="span18" style="text-align: right;" >
						<a class="btn  primary " href="${base}/dbconf/edit.do">添加一个数据库</a>
					</div>

				</div>
				<div class="row" >

					<div  class="span18" > 


						<table class="zebra-striped condensed-table bordered-table"  id="table1">
							<thead>
								<tr>
									<!-- class用于指定要显示的一些列，并按序显示 ；（so即show order的缩写，加前缀是为了和普通的class区分开来，这个是有逻辑意义的）-->
									<th class="so_dbName blue">
										数据库名
									</th>
									<th class="so_type1num blue">
										昨天数据量
									</th>
									<th class="so_dbUrl blue">
										URL
									</th>
									<th class="so_dbType blue">
										类型
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

	</body>
</html>
