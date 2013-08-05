<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=GBK"%>

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
		<link href="${base}/common_res/css/bootstrap.css" rel="stylesheet">
		<script src="${base}/common_res/js/jquery.js"></script>
		<!-- bootstrap begin -->
		<script src="${base}/common_res/js/bootstrap-alerts.js"></script>
		<!-- bootstrap begin -->

		<script src="${base}/common_res/js/jquery.tablesorter.js"
			charset="utf-8"></script>

		<!-- ����Ч�� -->
		<script src="${base}/common_res/js/tinybox.js"></script>


		<script src="${base}/config_res/hostconf/rel_apps.js"></script>
		<script>
	page.serverId = "${param.id}";
</script>

		<style type="text/css">
body {
	padding-top: 60px;
}

/*����Ч�� begin*/
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
/*����Ч�� end*/
</style>
	</head>

	<body>

		<%@ include file="/header.jsp"%>



		<div class="container-fluid">
			<%@ include file="/leftmenu.jsp"%>
			<div class="content">
				<div class="page-header">
					<h2>
						������������Ϣ


					</h2>

				</div>
				<div class="row">
					<div style="text-align: right">
						<a href="${base }/config/hostconf/list.jsp" class="btn ">����</a>
					</div>
				</div>
				<div class="row ">
					
					<div class="span3"  style="padding-bottom: 15px">
						<span class="label" style="font-size: 15px;">�������Ļ�����Ϣ</span>
					</div>

				</div>

				<div class="row ">
					<div class="span11" style="">
						<table class="zebra-striped condensed-table bordered-table">

							<tr>
								<td style="text-align: right">
									��������:
								</td>
								<td style="text-align: left" id="serverName"></td>
							</tr>
							<tr>
								<td style="text-align: right">
									IP:
								</td>
								<td style="text-align: left" id="serverIp"></td>
							</tr>

							<tr>
								<td style="text-align: right">
									����:
								</td>
								<td style="text-align: left">
									<div class="input">
										<textarea rows="2" cols="100" class="xxlarge" id="serverDesc"></textarea>
									</div>

								</td>
							</tr>

						</table>


					</div>
				</div>

				<div class="row">
					<div class="span3 ">
						<a href="javascript:void(0)" class="btn   primary"
							id="a_relAppAdd">���Ӧ�ù��� </a>
					</div>

				</div>
				<div class="row" style="margin-top: 15px">
					<div class="span18">
						<table class="zebra-striped condensed-table bordered-table"
							id="table1">
							<thead>
								<tr>
									<!-- class����ָ��Ҫ��ʾ��һЩ�У���������ʾ ����so��show order����д����ǰ׺��Ϊ�˺���ͨ��class���ֿ�������������߼�����ģ�-->
									<th class="so_number">
										���
									</th>
									<th class="so_appName">
										Ӧ����
									</th>
									<th class="so_limitData">
										��ʱ��
									</th>
									<th class="so_saveData">
										�־ñ�
									</th>
									<th class="so_operate">
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
