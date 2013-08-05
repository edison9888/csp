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

		<script src="${base }/config_res/keyconf/edit.js"></script>


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
					<div class="offset1">
						<input class="btn " type="button" value="返回" id="returnBtn" />
					</div>

				</div> -->
				<div class="page-header">
					<h2>
						key配置信息
					</h2>
				</div>
				<div class="row" style="margin-top: 30px">
					<div class="span10">
						<form action="" method="post" id="form1">

							<input type="hidden" value="${param.id!=null}" name="update"
								id="hidden1" />

							<c:if test="${param.id !=null}">
								<input type="hidden" value="${param.id }" name="keyId" />
							</c:if>

							<div class="clearfix">
								<label for="text1">
									Key名称:
								</label>

								<div class="input">
									<input class="span8" id="text1" type="text" name="keyName"
										value="${ po.keyName}"  />
								</div>
							</div>
							<div class="clearfix">
								<label for="select1">
									别名:
								</label>

								<div class="input">
									<input class="span8" id="text1" type="text" name="aliasName"
										value="${ po.aliasName}" />
								</div>
							</div>




								<div class="clearfix">
								<label for="select1">
									类型:
								</label>

								<div class="input">
									<input class="span4" id="text1" type="text" name="keyType"
										value="${ po.keyType }" />
								</div>
							</div>
		<div class="clearfix">
								<label for="textarea1">
									描述:
								</label>

								<div class="input">
									<textarea class="xxlarge span8" id="textarea1" name="feature">${po.feature}</textarea>
								</div>
							</div>
							<div class="clearfix">

								<div class="input span10">
									<input class="btn  primary span3" type="button" value="更新Key"
										id="submitBtn" />
									&nbsp;&nbsp;
									<input class="btn span2" type="reset" value="返回" id="returnBtn" />

								</div>

							</div>


						</form>
					</div>

				</div>
				<!-- row end -->

			</div>





		</div>




	</body>
</html>
