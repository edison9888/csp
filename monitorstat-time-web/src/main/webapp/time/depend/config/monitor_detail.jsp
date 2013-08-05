<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
	<head>
		<title>监控详情</title>
		<%@ include file="/time/common/base.jsp"%>

		<meta http-equiv="content-type" content="text/html;charset=gbk" />
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/jsPlumb.css">

		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/monitor_detail.css">
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
			src="<%=request.getContextPath()%>/statics/js/monitor_detail.js"></script>




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
	height: 400px;
	overflow: hidden;
}

#graph_2{
	background-color: white;
	background-image:
		url('<%=request.getContextPath()%>/statics/css/images/dynamicAnchorBg.jpg' )
		;
	position: relative;
	left: 0px;
	width: 100%;
	margin: 10px 10px;
	padding: 10px 10px;
	height: 400px;
	overflow: hidden;
}
#graph_3 {
	background-color: white;
	background-image:
		url('<%=request.getContextPath()%>/statics/css/images/dynamicAnchorBg.jpg' )
		;
	position: relative;
	left: 0px;
	width: 100%;
	margin: 10px 10px;
	padding: 10px 10px;
	height: 400px;
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
					前端应用
				</h3>
				<!-- 这里position可设置为absolute或relative -->
				<div id="graph_1">


					<%
						//预留的卡片总量
						request.setAttribute("pvAppsCount", 11);
						//左侧卡片总量
						request.setAttribute("pvLeftAppsCount", 5);
					%>
					<c:forEach begin="1" end="${pvAppsCount}" var="i">


						<c:choose>
							<c:when test="${i <= pvLeftAppsCount}">
								<c:set var="winIdPart1" value="left" />
								<c:set var="winIdPart2" value="${i}" />
							</c:when>
							<c:when test="${i > pvLeftAppsCount && i< pvAppsCount}">
								<c:set var="winIdPart1" value="right" />
								<c:set var="winIdPart2" value="${i-5}" />
							</c:when>

							<c:otherwise>
								<c:set var="winIdPart1" value="center" />
								<c:set var="winIdPart2" value="" />
							</c:otherwise>
						</c:choose>

						<div class="component window"
							id="window_pv_${winIdPart1}_${winIdPart2}">
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
					</c:forEach>
				</div>
				<!-- graph_1-->
			</div>
			<!-- row end -->
<div class="row">
				<h3>
					HSF
				</h3>
				<!-- 这里position可设置为absolute或relative -->
				<div id="graph_2">


					<%
						//预留的卡片总量
						request.setAttribute("hsfAppsCount", 11);
						//左侧卡片总量
						request.setAttribute("hsfLeftAppsCount", 5);
					%>
					<c:forEach begin="1" end="${pvAppsCount}" var="i">


						<c:choose>
							<c:when test="${i <= hsfLeftAppsCount}">
								<c:set var="winIdPart1" value="left" />
								<c:set var="winIdPart2" value="${i}" />
							</c:when>
							<c:when test="${i > hsfLeftAppsCount && i< hsfAppsCount}">
								<c:set var="winIdPart1" value="right" />
								<c:set var="winIdPart2" value="${i-5}" />
							</c:when>

							<c:otherwise>
								<c:set var="winIdPart1" value="center" />
								<c:set var="winIdPart2" value="" />
							</c:otherwise>
						</c:choose>

						<div class="component window"
							id="window_hsf_${winIdPart1}_${winIdPart2}">
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
					</c:forEach>
				</div>
				<!-- graph_2-->
			</div>
			<!-- row end -->
<div class="row">
				<h3>
					Tair
				</h3>
				<!-- 这里position可设置为absolute或relative -->
				<div id="graph_3">


					<%
						//预留的卡片总量
						request.setAttribute("tairAppsCount", 6);
						//左侧卡片总量
						request.setAttribute("tairLeftAppsCount", 0);
					%>
					<c:forEach begin="1" end="${tairAppsCount}" var="i">


						<c:choose>
							<c:when test="${i <= tairLeftAppsCount}">
								<c:set var="winIdPart1" value="left" />
								<c:set var="winIdPart2" value="${i}" />
							</c:when>
							<c:when test="${i > tairLeftAppsCount && i < tairAppsCount}">
								<c:set var="winIdPart1" value="right" />
								<c:set var="winIdPart2" value="${i}" />
							</c:when>

							<c:otherwise>
								<c:set var="winIdPart1" value="center" />
								<c:set var="winIdPart2" value="" />
							</c:otherwise>
						</c:choose>

						<div class="component window"
							id="window_tair_${winIdPart1}_${winIdPart2}">
							<strong> <span class="windowName"></span></strong>
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
									</td>
									<td style="text-align: left;">
									</td>
									<td style="text-align: right; color: red">
									</td>
									<td style="text-align: left;">
									</td>
								</tr>
								<tr>
									<td style="text-align: right;">
									</td>
									<td style="text-align: left;">
									</td>
									<td style="text-align: right; color: red">
									</td>
									<td style="text-align: left;">
									</td>
								</tr>
								<tr>

									<td style="text-align: right; color: red">
									</td>
									<td style="text-align: left;">
									</td>
									<td style="text-align: right; color: red">
									</td>
									<td style="text-align: left;">

									</td>
								</tr>

							</table>
						</div>
					</c:forEach>
				</div>
				<!-- graph_3-->
			</div>
			<!-- row end -->

		</div>
		<!-- index_body end -->


	</body>

</html>
