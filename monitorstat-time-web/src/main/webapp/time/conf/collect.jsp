<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=GBK"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE html>

<html>
	<head>
		<title>ʵʱ�ɼ�������Ϣ</title>

		<%
			//����base���ԣ����ɾ���·��
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
					<a href="<%=request.getContextPath()%>/index.jsp">ʵʱ��ҳ</a> ->
					ʵʱ�ɼ�������Ϣ &nbsp;${appInfo.appName }
					<hr>
					<a id="btn-add-time" href="<%=request.getContextPath() %>/app_info.do?method=gotoCollectAdd&appId=${appInfo.appId}">���ʵʱ����</a>
					<a style="float:right" id="btn-add-time" href="<%=request.getContextPath() %>/app_info.do?method=changeconfig&appId=${appInfo.appId}">�������</a>
					<div class="row-fluid">
						<div class="span12">
						
							<div class="row">
								<div class="span12">
									<table
										class="table table-striped table-condensed table-bordered"
										id="table1" style="table-layout: fixed">
										<thead>
											<tr>
												<!-- class����ָ��Ҫ��ʾ��һЩ�У���������ʾ ����so��show order����д����ǰ׺��Ϊ�˺���ͨ��class���ֿ�������������߼�����ģ�-->
												<th style="width:15%; text-align: center;">
													ʵʱ�����ļ���
												</th>
												<th style="width:36%; text-align: center;">
													ʵʱ�����ļ�·��
												</th>
												<th style="width:36%;text-align: center;">
													ʵʱ�����ļ�������
												</th>
												<th style="width:13%; text-align: center;">
													����
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
															href="<%=request.getContextPath() %>/app_info.do?method=gotoCollectUpdate&appId=${appInfo.appId}&confId=${item.confId}">�޸�</a>|
														<a
															href="<%=request.getContextPath() %>/app_info.do?method=collectDelete&appId=${appInfo.appId}&confId=${item.confId}" onclick="return confirm('ȷ��Ҫɾ����?')">ɾ��</a>
														
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
