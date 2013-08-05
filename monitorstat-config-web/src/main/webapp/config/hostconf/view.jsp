<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE html>

<html>
	<head>
		<title></title>
		<%@ include file="/common/base.jsp"%>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link href="${base }/common_res/css/bootstrap.css" rel="stylesheet">
		<script src="${base }/common_res/js/jquery.js"></script>
		<script src="${base }/common_res/js/jquery.tablesorter.js"
			charset="utf-8"></script>

		<script src="${base }/config_res/hostconf/view.js"></script>

		<style type="text/css">
body {
	padding-top: 60px;
}
</style>
	</head>

	<body>

		<%@ include file="/header.jsp"%>



		<div class="container-fluid">
			<%@ include file="/leftmenu.jsp"%>
			<div class="content">
				<div class="page-header">
					<h2>
						服务器配置信息
					</h2>
				</div>
				<div class="row">
					<div class="span8">
						<table class="zebra-striped condensed-table bordered-table">
							<tr>
								<td style="text-align: right" class="span2">
									服务器名:
								</td>
								<td style="text-align: left">
									${ po.serverName}
								</td>
							</tr>



							<tr>
								<td style="text-align: right">
									IP:
								</td>
								<td style="text-align: left">
									${ po.serverIp}
								</td>
							</tr>

							<tr>
								<td style="text-align: right">
									描述:
								</td>
								<td style="text-align: left">
									<%
										request.setAttribute("serverDescDefault",
												"<span style=\"color:red\">暂无描述</span>");
									%>
									${ po.serverDesc eq "" || po.serverDesc == null ?
									serverDescDefault :po.serverDesc}
								</td>
							</tr>



						</table>






					</div>
					<!-- <div class="row ">
					<div style="float: right">
						<input class="btn " type="button" value="返回" id="returnBtn" />
					</div>

				</div> -->


				</div>
				<!-- row end -->
				<div class="row ">
					<div class="span2 offset1">
						<input class="btn " type="button" value="返回" id="returnBtn" />
					</div>

				</div>

			</div>




		</div>





	</body>
</html>
