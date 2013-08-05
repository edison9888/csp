<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>

<html>
	<head>
		<title></title>

		<%
			//����base���ԣ����ɾ���·��
			String serverUrl = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort();

			String base = serverUrl + request.getContextPath();

			request.setAttribute("base", base);
		%>

		<script>
 	var base="${base}";
 	var page = {};
 	page.appId = "${appInfo.appId}";

 	
 	
 	
 </script>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link href="${base }/statics/css/bootstrap.css" rel="stylesheet">

		<script src="${base }/statics/js/jquery/jquery.min.js"></script>

		<script src="${base }/statics/js/bootstrap.js"></script>
		<script src="${base}/statics/js/My97DatePicker/WdatePicker.js"
			charset="utf-8" type="text/javascript"></script>
		<!-- ����Ч�� -->
		<script src="${base }/statics/js/tinybox.js"></script>

<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
		<script src="${base }/time/conf/key_edit.js"></script>

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
					Ӧ��URL���� &nbsp;${appInfo.appName }
					<hr>
					<div class="row-fluid">
						<div class="span12">

							<div class="row">
								<div class="span12 offset3">

									<form action="" method="post" id="form1">




										<input id="text1" type="hidden" name="appId"
											value="${ param.appId}" />
										<table>
											<tr>
												<td style="text-align: right">
													Key����:
												</td>
												<td>
													<input id="text1" type="text" name="keyName"
														value="${ po.keyName}" />
												</td>
											</tr>

											<tr>
												<td style="text-align: right">
													Key����:
												</td>
												<td style="text-align: left">
													<input id="text2" type="text" name="aliasName"
														value="${ po.aliasName}" />
												</td>


											</tr>
											<tr>
												<td style="text-align: right">
													��Key:
												</td>


												<td style="text-align: left">
													<input id="text2" type="text" name="parentKeyName"
														value="${ po.parentKeyName}" />
												</td>


											</tr>
											<!-- <tr>
												<td style="text-align: right">
													�޸�ʱ��:
												</td>


												<td style="text-align: left">
													<input class="Wdate" id="text2" type="text" name="modifyDate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
														value="<fmt:formatDate value="${po.modifyDate }" pattern="yyyy-MM-dd HH:mm:ss"/>" />
												</td>

											</tr> -->

											<tr>
												<td style="text-align: right">
													Key��Χ:
												</td>


												<td style="text-align: left">
													<select name="keyScope" class="span4" id="select1">
														<option value="NO">
															NO
														</option>
														<option value="HOST">
															HOST
														</option>
														<option value="APP">
															APP
														</option>
														<option value="ALL">
															ALL
														</option>
													</select>

													<script>
									$(function(){
										var select1V = "${po.keyScope}";
										var select1 = $('#select1');
									    var options = select1.get(0).options;
									    for (var i = 0; i < options.length; i++)
									         if (options[i].value == select1V){
									            options[i].selected = true;
									         }
									});
									
									</script>

												</td>

											</tr>
											<tr>
												<td style="text-align: right">
													Key����:
												</td>


												<td style="text-align: left">
													<select name="keyLevel" class="span4" id="select1">
														<option value="1">
															P1
														</option>
														<option value="2">
															P2
														</option>
														<option value="3">
															P3
														</option>
														<option value="4">
															P4
														</option>
														<option value="5">
															P5
														</option>
													</select>

													<script>
									$(function(){
										var select2V = "${po.keyLevel}";
										var select2 = $('#select2');
									    var options = select2.get(0).options;
									    for (var i = 0; i < options.length; i++)
									         if (options[i].value == select2V){
									            options[i].selected = true;
									         }
									});
									
									</script>

												</td>

											</tr>

											<tr>
												<td>
													<input style="text-align: right; width: 50px"
														class="btn primary" type="button" value="���"
														id="submitBtn" />
												</td>

												<td style="text-align: left">
													<input class="btn span2" type="reset" value="����"
														id="returnBtn" />
												</td>
											</tr>
										</table>

									</form>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</body>
</html>
