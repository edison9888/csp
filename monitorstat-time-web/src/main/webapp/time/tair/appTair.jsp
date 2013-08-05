<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK" isELIgnored="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
		<title>实时监控系统</title>
		<%@ include file="/time/common/base.jsp"%>
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/time_commons.js"></script>
			
		<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript"   src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>	

		<style type="text/css">
body {
	padding-top: 60px;
	padding-bottom: 40px;
}


</style>
<script type="text/javascript">
</script>
	</head>
	<body>
		<%@include file="/header.jsp"%>
		<div class="container-fluid">
			<div class="row-fluid" style="text-align: center">
<div class="span12" id="page_nav"></div>
<script>
	$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {urlPrefix:'<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=',appId: '${appInfo.appId}'});
</script>				</div>
			<div class="row-fluid">
				<div class="span2">
					<%@include file="/leftmenu.jsp"%>
				</div>
				<div class="span12">
					<a href="<%=request.getContextPath()%>/index.jsp">实时首页</a> 
					-><a href="<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=${appInfo.appId}">${appInfo.appName}</a>
					->Tair调用信息
					<div class="row-fluid">
						<div class="span6">
							<table class="table table-striped table-bordered table-condensed"
								id="tairIpTopTable">
								<caption>
									<strong>TOP 10 By IP <a
										href="<%=request.getContextPath()%>/app/detail/tair/show.do?method=gotoHost&appId=${appInfo.appId}">详情</a>
									</strong>
								</caption>
								<thead>
									<tr>
										<td>
											IP
										</td>
										<td>
											调用次数
										</td>
										<td>
											时间
										</td>
									</tr>
								</thead>
								<tbody id="ipList">
								</tbody>
							</table>
						</div>
						<div class="span6">
							<table class="table table-striped table-bordered table-condensed"
								id="tairGroupTable">
								<caption>
									<strong>TOP 10 By TairGroup<a
										href="<%=request.getContextPath()%>/app/detail/tair/show.do?method=gotoGroup&appId=${appInfo.appId}">详情</a>
									</strong>
								</caption>
								<thead>
									<tr>
										<td>
											TairGroupName
										</td>
										<td>
											调用次数
										</td>
										<td>
											时间
										</td>
									</tr>
								</thead>
								<tbody id="groupList">
								</tbody>
							</table>
						</div>
					</div>
					<div class="row-fluid">
						<div class="span12">
							<h4>
								机器流量信息
							</h4>
							<!-- 如果是PV 应用 展示PV 信息 -->
							<div class="row-fluid">
								<div class="span12 thumbnail ">
									<!-- 遍历 机器列表  -->
									<c:forEach items="${ ipList}" var="ip" varStatus="vs">
										<div class="span2"
											style="margin-left: 2px; width: 160px; display: none;"
											id="ip_div_${vs.count}">
											<table
												class="table table-striped table-bordered table-condensed"
												style="margin-bottom: 2px;">
												<thead>
													<tr>
														<td colspan="2">
															<h4>
																<a href="" id="ip_${vs.count}" rel='popover'
																	data-content='1234'>${vs.count}</a>
															</h4>
														</td>
													</tr>
												</thead>
												<tbody>
													<tr>
														<td id="pv_${vs.count}">
															12345
														</td>
														<td id="pv_rate_${vs.count}">
															<font>↑11</font>
														</td>
													</tr>
													<tr>
														<td id="rest_${vs.count}">
															22ms
														</td>
														<td id="rest_rate_${vs.count}">
															<font>↑11</font>
														</td>
													</tr>
												</tbody>
											</table>
										</div>
									</c:forEach>
									
									
								</div>
							</div>
							
							
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
<script type="text/javascript">
	function showHost(){
		$.ajax({
			url : "<%=request.getContextPath()%>/app/detail/tair/show.do?method=queryRecentlyHost&appId=${appInfo.appId}",
			success : function(data) {
					var ips = data;
					
					for(var i=0;i<ips.length;i++){
						if(ips[i].pv ==-1){
							continue;
						}
						var size = i+1;
						var obj = ips[i];
						var ipDiv = "ip_div_"+size;
						var ip = "ip_"+size;
						var pv = "pv_"+size;
						var pvRate = "pv_rate_"+size;
						var rest = "rest_"+size;
						var restRate = "rest_rate_"+size;
						$("#"+ipDiv).show();
						
						var h = "<table class='table table-striped table-bordered table-condensed'><tr>";
						//遍历属性列表  ips[i].propertyMap.prop
						var propNames = "", propValues="";
						var propertyMap =obj.propertyMap;
						for(var prop in propertyMap){
							propNames +="<td>"+ prop +"</td>";	
							propValues +="<td>"+  propertyMap[prop] +"</td>"
						}
						h += propNames+"</tr><tr>"+propValues+"</tr></table>";
						
						$("#"+ip).text(""+obj.ip+"("+obj.site+")");
						$("#"+ip).attr("data-content",h);
						$("#"+ip).attr("data-original-title",obj.ip);
						//详情页面
						ipclick(ip, obj.ip);
						
						$("#"+pv).html(obj.mainValue+obj.mainValueRate);
						$("#"+pvRate).text("");
						$("#"+rest).text(obj.originalPropertyMap['C-time']);
						$("#"+restRate).text("");
						
				}
			}
		});
		function ipclick(ip_size,ip){
			$("#"+ip_size).get(0).onclick = function(){
								var url = "<%=request.getContextPath()%>/app/detail/tair/show.do?method=gotoHostGroup&appId=${appInfo.appId}&ip="+ip;
								window.open(url);
							};
		}
		window.setTimeout("showHost()",60000)
	}
	showHost();
	
	$("a[rel=popover]")  .popover({placement:'left'}) .click(function(e) {
      e.preventDefault()
    })
    
            
function showotherInfo(){
	$.ajax({
		url : "<%=request.getContextPath()%>/app/detail/tair/show.do?method=queryOtherInfo&appId=${appInfo.appId}",
		success : function(data) {
			var ipList = data["ipList"];
			var tr ="";
			for(var i=0;i<ipList.length;i++){
				 tr += "<tr><td>"+ipList[i].ip+"</td><td>"+ipList[i].mainValue+ipList[i].mainValueRate+"</td><td>"+ipList[i].originalPropertyMap['C-time']+"</td></tr>"
			}
			$("#ipList").html(tr);
			
			var groupList = data["groupList"];
			tr ="";
			for(var i=0;i<groupList.length;i++){
				tr += "<tr><td>"+groupList[i].keyName+"</td><td>"+groupList[i].mainValue+groupList[i].mainValueRate+"</td><td>"+groupList[i].originalPropertyMap['C-time']+"</td></tr>"
			}
			$("#groupList").html(tr);
				
		}
	});
	        	
	window.setTimeout("showotherInfo()",60000);
}
showotherInfo();

</script>
</html>