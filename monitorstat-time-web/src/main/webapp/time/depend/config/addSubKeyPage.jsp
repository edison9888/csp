<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.util.List"%>
<!doctype html>
<html>
	<head>
		<title>APP关系配置</title>
		<%@ include file="/time/common/base.jsp"%>
		<meta http-equiv="content-type" content="text/html;charset=gbk" />		
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
		<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery.autocomplete.css">
					
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>		

		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
			
<style type="text/css">
body {
	padding-top: 60px;
	padding-bottom: 40px;
}

.sidebar-nav {
	padding: 9px 0;
}
.canvas{
	background-color: white;
	position: relative;
	width: 100%;
	margin: 10px 10px;
	padding: 10px 10px;
	height: 800px;
	overflow: hidden;
	border: 1px solid #DDDDDD;
    border-radius: 4px 4px 4px 4px;
    box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075);
    display: block;
    line-height: 1;
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
					<a href="<%=request.getContextPath()%>/index.jsp">实时首页</a> 
					-><a href="<%=request.getContextPath()%>/config/dependconfig.do?method=showSourceKeyList&appId=${appInfo.appId}">应用${appInfo.appName}的Key关系配置</a> 			 
					->${keyName}&nbsp;	 
					<hr>
					<div class="row-fluid">
						<div class="span12">
						<!-- 查询表单和保存表单分开 -->
							<div class="row-fluid">
								<form id="form2" method="post"
									action="<%=request.getContextPath()%>/config/dependconfig.do">
									<input type="hidden" name="method" value="saveExtraKey"/>
									<input type="hidden" name="sourceAppName" value="${sourceAppName}"/>
									<input type="hidden" name="sourceKeyName" value="${sourceKeyName}"/>
									<input type="hidden" name="appName" value="${appName}"/>
									<input type="hidden" name="keyName" value="${keyName}"/>
									<input type="hidden" name="appId" value="${appInfo.appId}"/>
									<table class="table table-striped table-bordered table-condensed" width="100%">
										<tbody id="exctbody">
												<tr>
													<td style="text-align: center;" >
														应用名称：
													</td>
													<td style="text-align: center;">
														<input type="text" name="extraAppName">
													</td>
												</tr>
												<tr>
													<td style="text-align: center;" >
														key：
													</td>
													<td style="text-align: center;">
														<input type="text" name="extraKeyName">
													</td>
												</tr>
												<tr>
													<td></td>
													<td colspan="2"><input type="submit" value="保存">
												</tr>
										</tbody>
									</table>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
	<script type="text/javascript">
	</script>
</html>
