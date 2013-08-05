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

 	
 	function choose(select, value){
	 	var options = select.get(0).options;
	    for (var i = 0; i < options.length; i++)
	         if (options[i].value == value)
	            options[i].selected = true;
	}
 	
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

		<script src="${base }/time/conf/app_info.js"></script>
		<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>

		<style type="text/css">
 body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
      .sidebar-nav {
        padding: 9px 0;
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
					应用信息&nbsp;${appInfo.appName }
					<hr>
					<div class="row-fluid">
						<div class="span12">

							<div class="row">
								<div class="span12 offset3">

									<form action="" method="post" id="form1">
										<input type="hidden" name="appId" id="hidden1"  value="${po.appId }" />
										<input type="hidden" name="appName" id="hidden2"   value="${ po.appName}" />
										<input type="hidden" name="appStatus" id="hidden3"   value="${ po.appStatus}" />
										<input type="hidden" name="appDayId" id="hidden4"   value="${ po.appDayId}" />
										<input type="hidden" name="appDayFeature" id="hidden5"   value="${ po.appDayFeature}" />	
										<input type="hidden" name="opsField" id="hidden6"   value="${ po.opsField}" />
										<input type="hidden" name="loginName" id="hidden7"   value="${ po.loginName}" />	
										<input type="hidden" name="loginPassword" id="hidden8"   value="${ po.loginPassword}" />
										<input type="hidden" name="appRushHours" id="hidden9"   value="${ po.appRushHours}" />	
										<table>
											
											<tr>
												<td style="text-align: right">
													排序:
												</td>
												<td style="text-align: left; padding-left:10px">
													<input id="text1" type="text" name="sortIndex"
														value="${ po.sortIndex}" />
												</td>
											</tr>

											<tr>
												<td style="text-align: right">
													feature:
												</td>


												<td style="text-align: left; padding-left:10px">
													<input id="text2" type="text" name="feature"
														value="${ po.feature}" />
												</td>


											</tr>
											

											<tr>
												<td style="text-align: right">
													ops_name:	
												</td>


												<td style="text-align: left; padding-left:10px">
												<input id="text3" type="text" name="opsName"
														value="${ po.opsName}" />
												</td>

											</tr>

	<tr>
												<td style="text-align: right">
													组名:	
												</td>


												<td style="text-align: left; padding-left:10px">
												<input id="text4" type="text" name="groupName"
														value="${ po.groupName}" />
												</td>

											</tr>
												<tr>
												<td style="text-align: right">
													day_deploy:	
												</td>


												<td style="text-align: left; padding-left:10px">
													<select name="dayDeploy" class="span3" id="select1">
										<option value="1">
											是
										</option>
										<option value="0">
											否
										</option>
									</select>
									
									<script>
									$(function(){
									   	choose($('#select1'),  "${po.dayDeploy}");
									});
									</script>
													
												</td>

											</tr>
												<tr>
												<td style="text-align: right">
													time_deploy:	
												</td>


												<td style="text-align: left; padding-left:10px">
													<select name="timeDeploy" class="span3" id="select2">
														<option value="1">
															是
														</option>
														<option value="0">
															否
														</option>
													</select>

													<script>
									$(function(){
									   	choose($('#select2'),  "${po.timeDeploy}");
									});
									</script>
													
												</td>

											</tr>
											
											
											<tr>
												<td style="text-align: right">
													应用类型:	
												</td>


												<td style="text-align: left; padding-left:10px">
													<select name="appType" class="span5" id="select3">
														<option value="pv">
															pv
														</option>
														<option value="center">
															center
														</option>
														<option value="tair">
															tair
														</option>
														<option value="notify">
															notify
														</option>
													</select>

													<script>
									$(function(){
									   	choose($('#select3'),  "${po.appType}");
									});
									</script>
													
												</td>

											</tr>
											<tr>
												<td style="text-align: right">
													公司名:
												</td>
												<td style="text-align: left; padding-left:10px">
													<input id="text5" type="text" name="companyName"
														value="${ po.companyName}" />
												</td>
											</tr>
											
											<tr>
												<td colspan="2" style="text-align: center; padding-top:30px">
													<input style="width: 150px"
														class="btn primary" type="button" value="更新应用信息"
														id="submitBtn" />
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
