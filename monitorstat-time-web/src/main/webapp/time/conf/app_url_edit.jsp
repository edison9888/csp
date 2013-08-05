<%@ page contentType="text/html; charset=GBK"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>  

<!DOCTYPE html>

<html>
	<head>
		<title></title>

		<%
			//设置base属性，生成绝对路径
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
        <script src="${base}/statics/js/My97DatePicker/WdatePicker.js" charset="utf-8"
      
         type="text/javascript"></script>
		<!-- 弹窗效果 -->
		<script src="${base }/statics/js/tinybox.js"></script>
				<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/time_commons.js"></script>
		<script src="${base }/time/conf/app_url_edit.js"></script>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
		<style type="text/css">
body {
	padding-top: 60px;
}

/*弹窗效果 begin*/
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
/*弹窗效果 end*/
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
					<a href="<%=request.getContextPath()%>/index.jsp">实时首页</a> ->
					应用URL管理 &nbsp;${appInfo.appName }
					<hr>
					<div class="row-fluid">
						<div class="span12">

							<div class="row">
								<div class="span12 offset3">

									<form action="" method="post" id="form1">
					
										<input type="hidden" value="${param.id!=null}" name="update"
											id="hidden1" />

										<c:if test="${param.id !=null}">
											<input type="hidden" value="${param.id }" name="id" />
										</c:if>
										<input id="text1" type="hidden" name="appName" value="${ po.appName}" />
										<table>
											
											<tr>
												<td style="text-align: right">
													应用URL:
												</td>
												<td>
													<input id="text1" type="text" name="appUrl"
														value="${ po.appUrl}" />
												</td>
											</tr>

											<tr>
												<td style="text-align: right">
													TopURL:
												</td>


												<td style="text-align: left">
													<input  id="text2" type="text" name="topUrl"
														value="${ po.topUrl}" />
												</td>


											</tr>
											<!-- <tr>
												<td style="text-align: right">
													修改时间:
												</td>


												<td style="text-align: left">
													<input class="Wdate" id="text2" type="text" name="modifyDate" onClick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
														value="<fmt:formatDate value="${po.modifyDate }" pattern="yyyy-MM-dd HH:mm:ss"/>" />
												</td>

											</tr> -->

											<tr>
												<td style="text-align: right">
													动态:	
												</td>


												<td style="text-align: left">
													<select name="dynamic" class="span3" id="select1">
										<option value="true">
											是
										</option>
										<option value="false">
											否
										</option>
									</select>
									
									<script>
									$(function(){
										var select1V = "${po.dynamic}";
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
												<td>
													<input style="text-align: right; width: 50px"
														class="btn primary" type="button" value="更新"
														id="submitBtn" />
												</td>

												<td style="text-align: left">
													<input class="btn span2" type="reset" value="返回"
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
