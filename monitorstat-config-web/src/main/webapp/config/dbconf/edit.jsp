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

		<!--  bootstrap begin -->
		<script src="${base }/common_res/js/bootstrap-twipsy.js"></script>
		<script src="${base }/common_res/js/bootstrap-popover.js"></script>



		<!--  bootstrap end -->

		<script src="${base }/common_res/js/jquery.tablesorter.js"
			charset="utf-8"></script>
		<!-- 弹窗效果 -->
		<script src="${base }/common_res/js/tinybox.js"></script>

		<script src="${base }/config_res/dbconf/edit.js"></script>


		<style type="text/css">
body {
	padding-top: 60px;
}

/*弹窗效果 begin*/
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
/*弹窗效果 end*/
</style>
	</head>

	<body>

		<%@ include file="/header.jsp"%>



		<div class="container-fluid">
			<%@ include file="/leftmenu.jsp"%>
			<div class="content">
				<!-- 	<div class="row ">
					<div class="">
						<input class="btn " type="button" value="返回" id="returnBtn" />
					</div>

				</div> -->
				<div class="page-header">
					<h2>
						数据库配置信息
					</h2>
				</div>
				<!--   style="padding-top:40px"-->
				<div class="row">
					<div class="span10">
						<form action="" method="post" id="form1">
							<input type="hidden" value="${param.id!=null}" name="update"
								id="hidden1" />
							<c:if test="${param.id !=null}">
								<input type="hidden" value="${param.id }" name="dbId" />
							</c:if>
							<div class="clearfix">
								<label for="text1">
									数据库名:
								</label>

								<div class="input">
									<input class="span3" id="text1" type="text" name="dbName"
										value="${ po.dbName}" />
								</div>
							</div>
							<div class="clearfix">
								<label for="select1">
									类型:
								</label>

								<div class="input">
									<select name="dbType" id="select1" class="span3">
										<%
											//int type0 = 0;	String type0Name = "核心库";
											//int type1 = 1;	String type1Name = "业务库";
											//int type2 = 2;	String type2Name = "外部库";
										%>
										<option value="0"
											<c:if test="${ po.dbType==0}">"selected"
								</c:if>>
											核心库
										</option>
										<option value="1"
											<c:if test="${ po.dbType==1}">"selected"
								</c:if>>
											业务库
										</option>
										<option value="2"
											<c:if test="${ po.dbType==2}">"selected"
								</c:if>>
											外部库
										</option>
									</select>
								</div>
							</div>

							<div class="clearfix">
								<label for="text2">
									用户名:
								</label>

								<div class="input">
									<input class="span3" id="text2" type="text" name="dbUser"
										value="${ po.dbUser}" />
								</div>
							</div>
							<div class="clearfix">
								<label for="text3">
									密码:
								</label>

								<div class="input">
									<input class="span3" id="text3" type="text" name="dbPwd"
										value="${ po.dbPwd}" />
								</div>
							</div>
							<div class="clearfix">
								<label for="text4">
									URL:
								</label>

								<div class="input">
									<input class="span8" id="text4" type="text" name="dbUrl"
										value="${po.dbUrl}" />
								</div>
							</div>
							<div class="clearfix">
								<label for="textarea1">
									描述:
								</label>

								<div class="input">
									<textarea class="xxlarge span8" id="textarea1" name="dbDesc">${po.dbDesc}</textarea>
								</div>
							</div>

							<div class="clearfix">


								<div class="input span10">
									<input class="btn  primary span3" type="button" value="更新数据库信息"
										id="submitBtn" />
									&nbsp;&nbsp;
									<input class="btn span2" type="reset" value="返回" id="returnBtn" />

								</div>

							</div>


						</form>
					</div>
				</div>

			</div>





		</div>




	</body>
</html>
