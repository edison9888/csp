<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
<head>
<title>监控详情</title>
<%@ include file="/time/common/base.jsp"%>

<meta http-equiv="content-type" content="text/html;charset=gbk" />
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jsPlumb.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.jsPlumb-all-min.js"></script>

<!-- <script type="text/javascript"	src="<%=request.getContextPath()%>/time/app_index.js"></script> -->

 <link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript"   src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script> 
<script type="text/javascript"		src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>

<style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
      .sidebar-nav {
        padding: 9px 0;
      }
</style>
</head>
<body >
<script>

</script>
<%@ include file="/header.jsp"%>
<div class="container-fluid">
<div class="row-fluid" style="text-align: center">
	
</div><div class="span12" id="page_nav"></div>
<script>
	$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {urlPrefix:'<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=',appId: '${appInfo.appId}'});
</script>	
	<div class="row-fluid">
        <div class="span2">
			<%@include file="../leftmenu.jsp"%>
		</div>
		<div class="span12">
			<a href="<%=request.getContextPath()%>/index.jsp">实时首页</a> ->${appInfo.appName}
			<hr>
			<div class="row-fluid">
			<div class="span12">
				<!-- 绘制依赖应用依赖图 -->
				<div class="row-fluid" style="height:550px; width:100%" id="dependDiv">
					正在加载中...					
				</div>
				<script type="text/javascript">
					$("#dependDiv").load("<%=request.getContextPath()%>/app/depend/query/show.do?appName=${appInfo.appName}&method=queryAppDetailWithTimeData");
				</script>
				<!-- 实时容量图
				<div class="row-fluid" id="capacityDiv" style="height:400px; width:100%">
				正在加载中...						
				</div>
				<script type="text/javascript">
					$("#capacityDiv").load("<%=request.getContextPath()%>/app/capacity.do?method=showRealTimeDepCapacity&appName=${appInfo.appName}");
				</script>-->
			</div>
		</div>			
    </div>
</div>
</body>
<script type="text/javascript">


	
				

function reinitIframe(down){
	var pTar = null;
	if (document.getElementById){
	pTar = document.getElementById(down);
	}
	else{
	eval('pTar = ' + down + ';');
	}
	if (pTar && !window.opera){
	//begin resizing iframe
	pTar.style.display="block"
	if (pTar.contentDocument && pTar.contentDocument.body.offsetHeight){
	//ns6 syntax
	pTar.height = pTar.contentDocument.body.offsetHeight +20;
	pTar.width = pTar.contentDocument.body.scrollWidth+20;
	}
	else if (pTar.Document && pTar.Document.body.scrollHeight){
	//ie5+ syntax
	pTar.height = pTar.Document.body.scrollHeight;
	pTar.width = pTar.Document.body.scrollWidth;
	}
	} 
}
</script>
</html>
