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
					�澯Key���� &nbsp;${appInfo.appName }
					<hr>
					<div class="row-fluid">
						<div class="span12">
							<div class="row" style="padding-bottom: 10px">

								<form action="${base}/app/conf/key/show.do" id="form1" method="post">
									<input type="text" name="keyNamePart"
										value="${param.keyNamePart }" style="margin-left: 300px" />
									<input class="btn  primary" type="button" value="��ѯ"
										id="button1" />
									<input type="hidden" name="method" value="alarmKeyList" />
									<input type="hidden" name="pageNo" id="hidden1"
										value="${param.pageNo}" />
									<input type="hidden" name="pageSize" id="hidden2"
										value="${param.pageSize}" />


									<input type="hidden" name="appId" id="hidden4"
										value="${param.appId}" />

									<a class="btn  btn-primary" style="margin-right: 10px; float: right"
										href="<%=request.getContextPath() %>/app/conf/key/show.do?method=showIndex&appId=${appInfo.appId}">����澯</a>
								</form>



							</div>
							<div class="row">

								<div class="span12">


									<table
										class="table table-striped table-condensed table-bordered"
										id="table1" style="table-layout: fixed">
										<thead>
											<tr>
												<!-- class����ָ��Ҫ��ʾ��һЩ�У���������ʾ ����so��show order����д����ǰ׺��Ϊ�˺���ͨ��class���ֿ�������������߼�����ģ�-->
												<th style="text-align: center;">
													Key����
												</th>
												<th style="width: 6%; text-align: center;">
													����
												</th>
												<th style="width: 28%; text-align: center;">
													�澯����
												</th>


												<th style="width: 6%; text-align: center;">
													����
												</th>
												<th style="width: 12%; text-align: center;">
													����
												</th>
											</tr>
										</thead>
										<tbody>

											<c:forEach items="${pagination.list}" var="item">
												<tr>
													<td style="text-align: left;" title="">

														${ item.keyName}

													</td>


													<td style="text-align: center;">
														${ item.propertyName}
													</td>

													<td style="text-align: center;">


														${ item.keyAlias}
													</td>


													<td style="text-align: center;">


														${ item.checkMode}
													</td>
													<td style="text-align: center;">
														<a
															href="<%=request.getContextPath() %>/app/conf/key/show.do?method=alarmKeyEdit&appId=${appInfo.appId}&keyName=${ item.keyName}&propertyName=${ item.propertyName}&id=${item.id }&from=alarmKeyList">�޸�</a>|
														<a
															href="<%=request.getContextPath() %>/app/conf/key/show.do?method=deleteAlarmKey&id=${item.id }&appId=${appInfo.appId}&keyName=${ item.keyName}&from=alarmKeyList">ɾ��</a>|
														<a
															href="<%=request.getContextPath() %>/data/show.do?method=baseline&appName=${appInfo.appName}&keyName=${item.keyName}&propertyName=${item.propertyName}&flag=yes" target="_blank">�鿴</a>
													</td>

												</tr>


											</c:forEach>
									</table>

									<c:if test="${pagination.pageNo > 1}">
										<a href="${pagination.pageNo-1 }" id="link1">��һҳ</a>
									</c:if>
									��ǰҳ��${pagination.pageNo}
									<c:if test="${pagination.pageNo < pagination.totalPage}">
										<a href="${pagination.pageNo+1 }" id="link2">��һҳ</a>
									</c:if>

									��${pagination.totalPage}ҳ ��ת��
									<select name="id" id="select3" style="width: 50px">
										<c:forEach begin="1" end="${pagination.totalPage}"
											var="number">
											<option value="${number}"
												<c:if test="${number==pageNo }">selected="selected"</c:if>>
												${number }
											</option>
										</c:forEach>
									</select>

								</div>
							</div>


						</div>
					</div>
				</div>
			</div>
		</div>
		<script type="text/javascript">
 
	
	
	$(function() {
			// �����ҳ���ӣ��ύ����
			$("#link1").click(function() {

						$("#hidden1").attr("value",
								$("#link1").attr("href"));
						$("#form1").submit();
						return false;

					});
			$("#link2").click(function() {
						$("#hidden1").attr("value",
								$("#link2").attr("href"));
						$("#form1").submit();
						return false;

					});

			$("#select3").change(function() {
						$("#hidden1").attr("value",
								$("#select3").attr("value"));
						$("#form1").submit();
						return false;
					});
			$("#button1").click(function() {
						$("#hidden1").attr("value",1);
						$("#form1").submit();
						return false;

					});

		});
//
	
	</script>
</html>