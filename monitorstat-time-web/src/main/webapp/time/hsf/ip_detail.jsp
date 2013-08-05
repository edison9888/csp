<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
	<head>
		<title>监控详情</title>
		<%@ include file="/time/common/base.jsp"%>

		<meta http-equiv="content-type" content="text/html;charset=gbk" />
		<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
		<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
		<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>
		<script src="<%=request.getContextPath()%>/statics/js/amcharts/flash/swfobject.js" type="text/javascript"></script>
		<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
		<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
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
					<a
						href="<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=${appInfo.appId}">${appInfo.appName}</a>
					->
					<a
						href="<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=gotohsfProviderIndex&appId=${appInfo.appId}">HSf
						Provider 流量信息 </a> ->${ip} 主机-详细
					<hr>
					<div class="row-fluid">
						<div class="span12">
							<div class="row-fluid  thumbnail">
								<div class=" span7 thumbnail">
									<h5>
										全网HSF-provider流量信息
									</h5>
									<div id="pvchartDivId" style="height: 280px">
									<script type="text/javascript">
										 var params = 
								            {
								                bgcolor:"#FFFFFF",
								                wmode:"transparent"
								            };
										
										var flashVars = 
							            {
							                path: "<%=request.getContextPath()%>/statics/js/amcharts/flash/",
							                settings_file: "<%=request.getContextPath()%>/statics/js/amcharts/flash/setting/amline_settings1.xml",
							                data_file: escape("<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=showHostPvInfo&appId=${appInfo.appId}&ip=${ip}")
							            };
										
										swfobject.embedSWF("<%=request.getContextPath()%>/statics/js/amcharts/flash/amline.swf", 
												"pvchartDivId", "100%", "350", "8.0.0", "<%=request.getContextPath()%>/statics/js/amcharts/flash/expressInstall.swf",
												flashVars, params);
										
										</script>
									</div>

								</div>
								<!-- span-->
								<div class="span4 thumbnail">
									<h5>
										全网HSF-provider平均响应时间
									</h5>
									<!--  使用嵌套Div，主要是不让thumbnail边框线和chart的底边黑线交叉-->

									<div id="restDivId" style="height: 260px;">
									<script type="text/javascript">
										 var params = 
								            {
								                bgcolor:"#FFFFFF",
								                wmode:"transparent"
								            };
										
										var flashVars = 
							            {
							                path: "<%=request.getContextPath()%>/statics/js/amcharts/flash/",
							                settings_file: "<%=request.getContextPath()%>/statics/js/amcharts/flash/setting/amline_settings1.xml",
							                data_file: escape("<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=showHostRestInfo&appId=${appInfo.appId}&ip=${ip}")
							            };
										
										swfobject.embedSWF("<%=request.getContextPath()%>/statics/js/amcharts/flash/amline.swf", 
												"restDivId", "100%", "250", "8.0.0", "<%=request.getContextPath()%>/statics/js/amcharts/flash/expressInstall.swf",
												flashVars, params);
										
										</script>
									</div>
								</div>
								<!-- span-->
							</div>
							<!-- row -->
<br/>
<br/>
<br/>
<br/>
							<div class="row-fluid thumbnail" >

								<table width="100%" >
									<tr>
										<td width="30%" valign="top">
											<h5>
												来源应用最近调用(top10)
											</h5>
											<table width="100%"
												class="table table-striped table-bordered table-condensed">
												<thead>
													<tr>
														<td width="100" style="text-align: center;">
															来源应用
														</td>
														<td width="100" style="text-align: center;">
															调用量
														</td>
														<td width="100" style="text-align: center;">
															比例
														</td>
													</tr>
												</thead>
												<tbody id="referApp">

												</tbody>
											</table>
										</td>
										<td width="70%" valign="top">
											<h5>
												接口最近被调用(top10)
											</h5>
											<table width="100%"
												class="table table-striped table-bordered table-condensed">
												<thead>
													<tr>
														<td style="text-align: center;">
															提供接口
														</td>
														<td width="100" style="text-align: center;">
															调用量
														</td>
														<td width="100" style="text-align: center;">
															比例
														</td>

													</tr>
												</thead>
												<tbody id="interfaceApp">

												</tbody>
											</table>
										</td>
									</tr>
								</table>
							</div>
							<!-- row-fluid -->
						</div>
						<!-- span12 -->
					</div>
					<!-- row-fluid -->
				</div>
				<!-- span10 -->
			</div>
			<!-- row-fluid -->
		</div>
		<!-- container-fluid -->
	</body>



	<script type="text/javascript">
 
 	$('#tab').tab('show');

	
	$("a[rel=popover]")  .popover() .click(function(e) {
      e.preventDefault()
    })
 
     function showotherInfo(){
            	$.ajax({
					url : "<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=queryHostDetail&appId=${appInfo.appId}&ip=${ip}",
					success : function(data) {
						var refer = data["refer"];
						var tr ="";
						for(var i=0;i<refer.length;i++){
							  tr += "<tr><td>"+refer[i].keyName+"</td><td>"+refer[i].mainValue+"</td><td>"+refer[i].mainRate+"</td></tr>"
						}
						$("#referApp").html(tr);
						
						var source = data["source"];
						tr ="";
						for(var i=0;i<source.length;i++){
							tr += "<tr><td>"+source[i].keyName+"</td><td>"+source[i].mainValue+"</td><td>"+source[i].mainRate+"</td></tr>"
						}
						$("#interfaceApp").html(tr);
						
					}
				});
            	
            	window.setTimeout("showotherInfo()",60000);
            }
            showotherInfo();
            
        </script>

</html>

