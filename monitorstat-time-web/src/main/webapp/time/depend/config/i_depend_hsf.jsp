<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
	<head>
		<title>依赖数据-我依赖-HSF</title>
		<%@ include file="/time/common/base.jsp"%>

		<meta http-equiv="content-type" content="text/html;charset=gbk" />

		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">

		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/jsPlumb.css">
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/i_depend_hsf.css">

		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/log.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/date.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/jquery/jquery.jsPlumb-all-min.js"></script>

		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/i_depend_hsf.js"></script>


		<style>
/*重写bootstrap*/
.row>[class *="span"] {
	margin-left: 0px;
}

#graph_1 {
	background-color: white;
	background-image:
		url('<%=request.getContextPath()%>/statics/css/images/dynamicAnchorBg.jpg' )
		;
	position: relative;
	left: 0px;
	width: 100%;
	margin: 10px 10px;
	padding: 10px 10px;
	height: 600px;
	overflow: hidden;
}


body {
	padding-top: 60px;
	padding-bottom: 40px;
}

/*1.4中特有的东西  begin*/
table .blue {
	color: #049cdb;
	border-bottom-color: #049cdb;
}

table .headerSortUp.blue,table .headerSortDown.blue {
	background-color: #ade6fe;
}

/*1.4中特有的东西 end*/
.index_body {
	margin-left: 50px;
	margin-right: 50px;
}

.window td {
	padding: 0px;
	fixed
}

.window td {
	font-size: 12px
}

#table1 th {
	text-align: center;
}
#table1 td {
	text-align: center;
}
</style>

		<script>
	page.appName = "${param.appName}";
	page.appName = "detail";
</script>
	</head>
	<body onunload="jsPlumb.unload();">


		<%@ include file="/header.jsp"%>

		<div class="index_body">




			<div class="row">
				<h3>
					我依赖的HSF调用
				</h3>
				<!-- 这里position可设置为absolute或relative -->
				<div id="graph_1">


					<%
						//预留的卡片总量
						request.setAttribute("appsCount", 11);
					%>
					<c:forEach begin="1" end="${appsCount}" var="i">




						<div class="component window" id="window_${i}">
							<strong> <span class="windowName"></span>( <span
								class="hostSize"></span> 台)</strong>
							<table class="bordered-table">
								<tr>
									<td style="text-align: right;">
										采集时间:
									</td>
									<td style="text-align: left;">
										<span class="collectTime"></span>
									</td>
									<td style="text-align: right;">
									</td>
									<td style="text-align: left;">
									</td>
								</tr>
								<tr>
									<td style="text-align: right;">
										总流量:
									</td>
									<td style="text-align: left;">
										<span class="pv"></span>
									</td>
									<td style="text-align: right; color: red">
										失败率:
									</td>
									<td style="text-align: left;">
										<span class="failRate"></span>
									</td>
								</tr>
								<tr>
									<td style="text-align: right;">
										容量:
									</td>
									<td style="text-align: left;">
										30%
									</td>
									<td style="text-align: right; color: red">
										告警数:
									</td>
									<td style="text-align: left;">
										0
									</td>
								</tr>
								<tr>

									<td style="text-align: right; color: red">
										异常数:
									</td>
									<td style="text-align: left;">
										<span class="exceptionCount"></span>
									</td>
									<td style="text-align: right; color: red">
									</td>
									<td style="text-align: left;">

									</td>
								</tr>

							</table>
						</div>
						<!-- window_  -->
					</c:forEach>

					<div class="component window" id="window_center">
						<strong> <span class="windowName"></span>( <span
							class="hostSize"></span> 台)</strong>
						<table class="bordered-table">
							<tr>
								<td style="text-align: right;">
									采集时间:
								</td>
								<td style="text-align: left;">
									<span class="collectTime"></span>
								</td>
								<td style="text-align: right;">
								</td>
								<td style="text-align: left;">
								</td>
							</tr>
							<tr>
								<td style="text-align: right;">
									总流量:
								</td>
								<td style="text-align: left;">
									<span class="pv"></span>
								</td>
								<td style="text-align: right; color: red">
									失败率:
								</td>
								<td style="text-align: left;">
									<span class="failRate"></span>
								</td>
							</tr>
							<tr>
								<td style="text-align: right;">
									容量:
								</td>
								<td style="text-align: left;">
									30%
								</td>
								<td style="text-align: right; color: red">
									告警数:
								</td>
								<td style="text-align: left;">
									0
								</td>
							</tr>
							<tr>

								<td style="text-align: right; color: red">
									异常数:
								</td>
								<td style="text-align: left;">
									<span class="exceptionCount"></span>
								</td>
								<td style="text-align: right; color: red">
								</td>
								<td style="text-align: left;">

								</td>
							</tr>

						</table>
					</div>
				</div>
				<!-- graph_1-->
			</div>
			<!-- row end -->
			<div class="row">
				<div style="text-align: center">
					<h3>
						详细列表
					</h3>
				</div>

				<table class="table-striped table-condensed table-bordered " id="table1">
				
					<tr>
						<th class="blue span3">应用</th>
						<th class="blue span5">依赖接口名</th>
						<th class="blue span5">方法名</th>
						<th class="blue span3">调用量</th>
						<th class="blue span3">详细</th>
					</tr>
	<c:forEach begin="1" end="10" var="i">
		<tr>
		<td>login</td>
		<td>ItemQueryService:1.0.0-L0</td>
				<td>queryItemForBuy</td>
		<td>6956</td>
				<td><a href="javascript:void(0)">详细</a></td>
		</tr>
	</c:forEach>

				</table>
			</div>
			<!-- row end -->

		</div>
		<!-- index_body end -->


	</body>

</html>
