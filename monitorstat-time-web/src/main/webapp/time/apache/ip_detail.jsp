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
<script type="text/javascript"		src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>
<script src="<%=request.getContextPath()%>/statics/js/amcharts/amcharts.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/statics/js/amcharts/raphael.js" type="text/javascript"></script>

<script type="text/javascript"	src="<%=request.getContextPath()%>/time/app_index.js"></script>
<script src="<%=request.getContextPath()%>/statics/js/swfobject.js" type="text/javascript"></script>

<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript"   src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>

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
<a href="<%=request.getContextPath()%>/index.jsp">实时首页</a> -><a href="<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=${appInfo.appId}" >${appInfo.appName}</a> -><a href="<%=request.getContextPath()%>/app/detail/apache/show.do?method=showIndex&appId=${appInfo.appId}">Web流量信息</a> ->${ip} 主机-详细			<hr>
			<div class="row-fluid">
			<div class="span12">
				<div class="row-fluid  thumbnail">
								<div class=" span7 thumbnail">
									<h5>
										单机流量信息
									</h5>
									<div  id="pvchartDivId" style="height: 280px">
								<script type="text/javascript">
								var so4 = new SWFObject("<%=request.getContextPath()%>/statics/js/amcharts/old/amline/amline.swf", "amline_pv", "100%", "350", "6", "#FFFFFF");
								so4.addVariable("path", "<%=request.getContextPath()%>/statics/js/amcharts/old/amline");
								so4.addVariable("chart_id", "amline4");   
								so4.addVariable("reload_data_interval", "60");  
								so4.addVariable("wmode", "transparent");
								so4.addVariable("settings_file", "<%=request.getContextPath()%>/statics/js/amcharts/old/amline/amline_settings1.xml");
								so4.addVariable("data_file", escape("<%=request.getContextPath()%>/app/detail/apache/show.do?method=showHostPvInfo&appId=${appInfo.appId}&ip=${ip}"));
								so4.write("pvchartDivId");
								</script>

									</div>

								</div>
								<!-- span-->
								<div class="span4 thumbnail">
									<h5>
										单机平均响应时间
									</h5>
									<!--  使用嵌套Div，主要是不让thumbnail边框线和chart的底边黑线交叉-->

									<div id="restDivId" style="height: 260px;">
									<script type="text/javascript">
										var so3 = new SWFObject("<%=request.getContextPath()%>/statics/js/amcharts/old/amline/amline.swf", "amline_rest", "100%", "250", "6", "#FFFFFF");
										so3.addVariable("path", "<%=request.getContextPath()%>/statics/js/amcharts/old/amline");
										so3.addVariable("chart_id", "amline3");
										so3.addVariable("reload_data_interval", "60");   
										so3.addVariable("settings_file", "<%=request.getContextPath()%>/statics/js/amcharts/old/amline/amline_settings1.xml");
										so3.addVariable("data_file", escape("<%=request.getContextPath()%>/app/detail/apache/show.do?method=showHostRestInfo&appId=${appInfo.appId}&ip=${ip}"));
										so3.write("restDivId");
									</script>
									</div>


								</div>
								<!-- span-->
							</div>
							<!-- row -->
				
				<hr>
				<h5>URL信息</h5>
				<div class="row-fluid thumbnail">
					<table  width="100%">
						<tr>
							<td width="50%" valign="top">
								<h5>自身URL</h5>
								<table width="100%" class="table table-striped table-bordered table-condensed">
									<thead>
										<tr>
											<td>URL</td><td>次数</td><td>200</td><td>302</td><td>响应时间</td>
										</tr>
									</thead>
									<tbody id="sourceUrlId"  >
									</tbody>
								</table>
							</td>
							<td width="50%" valign="top">
								<h5>被调用URL</h5>
								<table width="100%" class="table table-striped table-bordered table-condensed">
									<thead>
										<tr>
											<td>应用</td><td>URL</td><td>数量</td>
										</tr>
									</thead>
									<tbody id="referUrlId" >
									</tbody>
									
								</table>
							</td>
						</tr>
					</table>				
				</div>
				<hr>
				
			</div>
			</div>
		</div>			
    </div>
</div>
   
</body>



 <script type="text/javascript">
 
 	$('#tab').tab('show');

	
	
	$("a[rel=popover]")  .popover() .click(function(e) {
      e.preventDefault()
    })
 
            
 		
            
            
         
            
            
          
            
            function showotherInfo(){
            	$.ajax({
					url : "<%=request.getContextPath()%>/app/detail/apache/show.do?method=queryHostDetail&appId=${appInfo.appId}&ip=${ip}",
					success : function(data) {
						var refer = data["refer"];
						
						var tr ="";
						for(var i=0;i<refer.length;i++){
							 tr += "<tr><td>"+refer[i].appName+"</td><td>"+refer[i].keyName+"</td><td>"+refer[i].mainValue+"("+refer[i].mainRate+"%)</td></tr>"
						}
						$("#referUrlId").html(tr);
						
						var source = data["source"];
						tr ="";
						for(var i=0;i<source.length;i++){
							tr += "<tr><td>"+source[i].keyName+"</td><td>"+source[i].mainValue+"("+source[i].mainRate+"%)</td><td>"+source[i].originalPropertyMap['C-200']+"</td><td>"+source[i].originalPropertyMap['C-302']+"</td><td>"+source[i].originalPropertyMap['C-time']+"</td></tr>"
						}
						$("#sourceUrlId").html(tr);
						

						
						
					}
				});
            	
            	window.setTimeout("showotherInfo()",60000);
            }
            showotherInfo();
            
        </script>

</html>

	