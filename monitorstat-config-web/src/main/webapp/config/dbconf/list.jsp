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

		<!-- ����Ч�� -->
		<script src="${base}/common_res/js/tinybox.js"></script>

		<script src="${base}/config_res/dbconf/list.js"></script>


		<style type="text/css">
body {
	padding-top: 60px;
}

/*����Ч�� begin*/
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
/*����Ч�� end*/
</style>
	</head>

	<body>

		<%@ include file="/header.jsp"%>



		<div class="container-fluid">
			<%@ include file="/leftmenu.jsp"%>
			<div class="content">
				<div class="row" style="padding-bottom: 10px">
					<div class="span18" style="text-align: right;" >
						<a class="btn  primary " href="${base}/dbconf/edit.do">���һ�����ݿ�</a>
					</div>

				</div>
				<div class="row" >

					<div  class="span18" > 


						<table class="zebra-striped condensed-table bordered-table"  id="table1">
							<thead>
								<tr>
									<!-- class����ָ��Ҫ��ʾ��һЩ�У���������ʾ ����so��show order����д����ǰ׺��Ϊ�˺���ͨ��class���ֿ�������������߼�����ģ�-->
									<th class="so_dbName blue">
										���ݿ���
									</th>
									<th class="so_type1num blue">
										����������
									</th>
									<th class="so_dbUrl blue">
										URL
									</th>
									<th class="so_dbType blue">
										����
									</th>
									<th class="so_operate blue">
										����
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
		<!-- ��Ϊ����Դ���������Ҫ����z-index��display�������� -->
		<div id="confirm1" style="z-index: 100; display: none">
			<div class="alert-message block-message warning">
				<a class="close confirm1_close" href="#">��</a>
				<p>
					"�����Ҫɾ����û�а�"
				</p>
				<div class="alert-actions">
					<!-- class name ��yes no ���߼�����-->
					<a class="btn small confirm1_yes" href="#">��</a>
					<a class="btn small confirm1_no" href="#">��</a>
				</div>
			</div>
		</div>

	</body>
</html>
