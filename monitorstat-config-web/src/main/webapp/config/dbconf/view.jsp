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

		<script src="${base }/config_res/dbconf/view.js"></script>

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
				<!-- -->
				<div class="page-header">
					<h2>
						���ݿ�������Ϣ
					</h2>
				</div>
				<div class="row">
					<div class="span13">
						<table class="zebra-striped condensed-table bordered-table">
							<tr>
								<td style="text-align: right" class="span2"  >
									���ݿ���:
								</td>
								<td style="text-align: left">
									${ po.dbName}
								</td>
							</tr>
							<tr>
								<td style="text-align: right">
									����:
								</td>
								<td style="text-align: left">
									${ po.dbType==0?"���Ŀ�":(po.dbType==1?"ҵ���":"�ⲿ��")}
								</td>
							</tr>

							<tr>
								<td style="text-align: right">
									�û���:
								</td>
								<td style="text-align: left">
									${ po.dbUser}
								</td>
							</tr>

							<tr>
								<td style="text-align: right">
									����:
								</td>
								<td style="text-align: left">
									${ po.dbPwd}
								</td>
							</tr>

							<tr>
								<td style="text-align: right">
									URL:
								</td>
								<td style="text-align: left">
									${ po.dbUrl}
								</td>
							</tr>

							<tr>
								<td style="text-align: right">
									����:
								</td>
								<%
									request.setAttribute("dbDescDefault",
											"<span style=\"color:red\">��������</span>");
								%>
								<td style="text-align: left">

									${ po.dbDesc eq "" || po.dbDesc == null ? dbDescDefault
									:po.dbDesc}
								</td>
							</tr>



						</table>






					</div>
				</div>
				<!-- row end -->
				<div class="row ">
					<div class="span2 offset1">
						<input class="btn " type="button" value="����" id="returnBtn" />
					</div>

				</div>
			</div>




		</div>





	</body>
</html>
