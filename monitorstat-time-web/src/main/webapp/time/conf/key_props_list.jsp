<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=GBK"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<!DOCTYPE html>

<html>
	<head>
		<title>Key����</title>

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



<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>


		<style type="text/css">
body {
	padding-top: 60px;
}


#table1 td {
	word-wrap: break-word
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
					<a
						href="<%=request.getContextPath()%>/app/conf/key/show.do?method=showIndex&appId=${appInfo.appId}">
						Key���� &nbsp;${appInfo.appName }</a> -> ${keyName } �鿴����
					<hr>
					<div class="row-fluid">
						<div class="span12">
							<div class="row" style="padding-bottom: 10px">





							</div>



							<div class="row">

								<div class="span5 offset4">


									<table
										class="table table-striped table-condensed table-bordered"
										id="table1">
										<thead>
											<tr>
												<th style="">
													��������
												</th>

												<th style="" colspan="2">
													����
												</th>
											</tr>
										</thead>
										<tbody>

											<c:forEach items="${addAlarmList}" var="item">

												<tr>
													<td style="text-align: center;">
														${ item.pn}
													</td>

													<td style="text-align: center;">

														<c:choose>
															<c:when test="${ item.isAlarm}">
																<a
																	href="<%=request.getContextPath() %>/app/conf/key/show.do?method=alarmKeyEdit&appId=${appInfo.appId}&keyName=${keyName }&propertyName=${ item.po.propertyName}&id=${item.keyModeId }">�澯�޸�</a>|<a
																	href="<%=request.getContextPath() %>/app/conf/key/show.do?method=deleteAlarmKey&id=${item.keyModeId }&appId=${appInfo.appId}&keyName=${keyName }">�澯ɾ��</a>
															</c:when>
															<c:otherwise>
																<%-- int appId,int keyId,String keyName, String propertyName --%>
																<a
																	href="<%=request.getContextPath() %>/app/conf/key/show.do?method=alarmKeyEdit&appId=${appInfo.appId}&keyName=${keyName }&propertyName=${ item.po.propertyName}">����澯</a>
															</c:otherwise>
														</c:choose>
														<%
														SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
														%>
															|<a href="<%=request.getContextPath() %>/data/show.do?method=reprocess&appName=${appInfo.appName}&keyName=${keyName }&propertyName=${ item.po.propertyName}&time=<%=sdf.format(new Date())%>">���ɻ���</a>
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
		<script type="text/javascript">
	
	
	</script>
</html>
