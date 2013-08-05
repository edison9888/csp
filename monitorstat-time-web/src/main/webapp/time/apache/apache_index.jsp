<%@page import="com.taobao.csp.time.cache.TimeCache"%>
<%@page import="com.taobao.csp.time.util.Arith"%>
<%@page import="com.taobao.csp.time.util.DataUtil"%>
<%@page import="com.taobao.csp.time.web.po.TimeDataInfo"%>
<%@page import="com.taobao.csp.dataserver.Constants"%>
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
<script type="text/javascript"		src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>
<script src="<%=request.getContextPath()%>/statics/js/amcharts/flash/swfobject.js" type="text/javascript"></script>
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript"   src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
<script>
$(function(){
	
});
</script>
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
	<div class="span12" id="page_nav">
		
	</div>
	<script>
			$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {urlPrefix:'<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=',appId: '${appInfo.appId}'});
	</script>	
</div>
	<div class="row-fluid">
        <div class="span2">
			<%@include file="/leftmenu.jsp"%>
		</div>
		<div class="row-fluid">
			<a href="<%=request.getContextPath()%>/index.jsp">实时首页</a> -><a href="<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=${appInfo.appId}" >${appInfo.appName}</a> ->Web流量信息
			<hr>
			<div class="row-fluid"  id="pvinforow">
			<script type="text/javascript">
			$("#pvinforow").load("<%=request.getContextPath()%>/index.do?method=queryIndexTableForPv&appId=${appInfo.appId}");
			</script>
			</div>
			<div class="row-fluid">
			<div class="span12">
			<h5>全网流量信息  &nbsp;&nbsp;&nbsp;<a href="<%=request.getContextPath()%>/app/detail/history.do?method=showHistory&appName=${appInfo.appName}&keyName=PV" target="_blank">总流量历史详情</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="<%=request.getContextPath()%>/app/detail/apache/show.do?method=queryPvHost&appName=${appInfo.appName}">每台主机详情</a>&nbsp;&nbsp;&nbsp;</h5>
				<div class="row-fluid thumbnail">
					<table width="100%">
						<tr>
							<td width="100%"  id="pvchartDivId"  style="height:250px" colspan="2">
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
			                data_file: escape("<%=request.getContextPath()%>/app/detail/apache/show.do?method=showPvInfo&appId=${appInfo.appId}")
			            };
						
						swfobject.embedSWF("<%=request.getContextPath()%>/statics/js/amcharts/flash/amline.swf", 
								"pvchartDivId", "100%", "350", "8.0.0", "<%=request.getContextPath()%>/statics/js/amcharts/flash/expressInstall.swf",
								flashVars, params);
						
						</script>
							</td>							
						</tr>
					</table>
					<table width="100%">
						<tr>
							<td width="50%">
								<h5>全网平均响应时间 </h5>
								<div class="thumbnail" style="height:350px" id="restDivId">
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
			                data_file: escape("<%=request.getContextPath()%>/app/detail/apache/show.do?method=showRestInfo&appId=${appInfo.appId}")
			            };
						
						swfobject.embedSWF("<%=request.getContextPath()%>/statics/js/amcharts/flash/amline.swf", 
								"restDivId", "100%", "350", "8.0.0", "<%=request.getContextPath()%>/statics/js/amcharts/flash/expressInstall.swf",
								flashVars, params);
						
						</script>
								</div>
							</td>
							<td width="50%" valign="top">
								<table width="100%">
									<tr>
										<td width="30%" valign="top">
										<h5>全网访问区域布局(top10)  <a href="<%=request.getContextPath()%>/app/detail/apache/show.do?method=gotoRegionDetail&appId=${appInfo.appId}">详细</a></h5>
										<table class="table table-striped table-bordered table-condensed">
										<thead >
											<tr>
												<td>地区</td><td>次数</td><td>比例</td>
											</tr>
											<tbody id="regionId"  >
											</tbody>
										</table>
										</td>
										<td width="30%"  valign="top">
											<h5>全网网络布局(top10)  <a href="<%=request.getContextPath()%>/app/detail/apache/show.do?method=gotoNetworkDetail&appId=${appInfo.appId}"">详细</a></h5>
											<table class="table table-striped table-bordered table-condensed">
												<thead >
													<tr>
														<td>网络</td><td>次数</td><td>比例</td>
													</tr>
												</thead>
													<tbody id="networkId"  >
													</tbody>
											</table>
										</td>
										<td  width="40%"  valign="top">
										<h5>全网机房布局 </h5>
										
										<%
												List<TimeDataInfo>  datalist =(List<TimeDataInfo>)request.getAttribute("dataList");
												Set<String> cmset = new HashSet<String>();
												for(TimeDataInfo data:datalist){
													Map<String, Object> propMap = data.getOriginalPropertyMap();
													for(Map.Entry<String, Object> cm:propMap.entrySet()){
														if(cm.getKey().startsWith("pv_"))
															cmset.add(cm.getKey());
													}
												}
													%>
											<table class="table table-striped table-bordered table-condensed">
												<tr>
													<td>时间</td>
													<%for(String cm:cmset) {%>
													<td><%=cm %></td>
													<%} %>
												</tr>
												<%
												for(TimeDataInfo data:datalist){
													Map<String, Object> propMap = data.getOriginalPropertyMap();
													long pvc = 0;
													for(String cm:cmset) {
														pvc+=DataUtil.transformLong(propMap.get(cm));
													}
													%>
													<tr>
														<td><%=data.getFtime() %></td>
														<%for(String cm:cmset) {%>
														<td><%=Arith.mul(Arith.div(DataUtil.transformLong(propMap.get(cm)), pvc, 4) ,100)%>%</td>
														<%} %>
													</tr>
													<%
												}
												%>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>								
					</table>					
				</div>
				<hr>
				<h5>URL信息</h5>
				<div class="row-fluid thumbnail">
					<table  width="100%">
						<tr>
							<td width="50%" valign="top">
								<h5>自身URL(top10)   <a href="<%=request.getContextPath()%>/app/detail/apache/show.do?method=gotoSourceDetail&appId=${appInfo.appId}">详细</a></h5>
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
								<h5>来源URL(top10)  <a href="<%=request.getContextPath()%>/app/detail/apache/show.do?method=gotoReferDetail&appId=${appInfo.appId}">详细</a></h5>
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
				<h4>机器流量信息</h4>
				<!-- 如果是PV 应用 展示PV 信息 -->
				<div class="row-fluid">
						<div class="span12 thumbnail ">
								<div>
							         
			           		<!-- 遍历 机器列表  -->
			           		<c:forEach items="${ ipList}"  var="ip" varStatus="vs" >
					              	<div class="span2"  style="margin-left: 2px;width:160px;display:none;" id="ip_div_${vs.count}">
					              		<table class="table table-striped table-bordered table-condensed"  style="margin-bottom:2px;" >
					              			<thead>
						              			<tr >
						              				<td colspan="2"   ><h4 ><a href="" id="ip_${vs.count}"   rel='popover'   data-content='1234'>${vs.count}</a></h4></td>
						              			</tr>
					              			</thead>
					              			<tbody>
						              			<tr>
						              				<td id="pv_${vs.count}">12345</td>
						              				<td id="pv_rate_${vs.count}"><font>↑11</font></td>
						              			</tr>
						              			<tr>
						              				<td  id="rest_${vs.count}">22ms</td>
						              				<td id="rest_rate_${vs.count}"><font>↑11</font></td>
						              			</tr>
					              			</tbody>
					              		</table>
					              	</div>
				           </c:forEach>
								</div>
						</div>
				</div>
				<br>
			</div>
			</div>
		</div>			
    </div>
</div>
   
</body>



 <script type="text/javascript">

	function showHost(){
		$.ajax({
			url : "<%=request.getContextPath()%>/app/detail/apache/show.do?method=showIPPv&appId=${appInfo.appId}",
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
						var h = "<table class='table table-striped table-bordered table-condensed'>";
						//遍历属性列表  ips[i].propertyMap.prop
						var propNames = "";
						var propertyMap =obj.propertyMap;
						for(var prop in propertyMap){
							propNames +="<tr><td>"+ prop +"</td>";	
							propNames +="<td>"+  propertyMap[prop] +"</td></tr>"
						}
						h += propNames+"</table>";
						
						$("#"+ip).text(obj.ip+"("+ obj.site+")");
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
		window.setTimeout("showHost()",60000);
		/***/
		function ipclick(ip_size,ip){
			$("#"+ip_size).get(0).onclick = function(){
								var url = "<%=request.getContextPath()%>/app/detail/apache/show.do?method=gotoHostDetail&appId=${appInfo.appId}&ip="+ip;
								window.open(url);
			};
		}
	}
	showHost();
	
	$("a[rel=popover]").popover({placement:'left'}).click(function(e) {
      e.preventDefault()
    })
 
            function showotherInfo(){
            	$.ajax({
					url : "<%=request.getContextPath()%>/app/detail/apache/show.do?method=showotherInfo&appId=${appInfo.appId}",
					success : function(data) {
						var refer = data["refer"];
						var tr ="";
						for(var i=0;i<refer.length;i++){
							
							 tr += "<tr><td>"+refer[i].referAppName+"</td><td>"+refer[i].keyName+"</td><td>"+refer[i].mainValue+"("+refer[i].mainRate+"%)"+refer[i].mainValueRate+"</td></tr>"
						}
						$("#referUrlId").html(tr);
						var source = data["source"];
						tr ="";
						for(var i=0;i<source.length;i++){
							tr += "<tr><td><a title='查看依赖路径' target='_blank' href='<%=request.getContextPath()%>/app/depend/query/show.do?method=queryKeyDetailWithTimeData&appName=${appInfo.appName}&keyName=PV`"+source[i].keyName+"'>"+source[i].keyName+"</a></td><td>"+source[i].mainValue+"("+source[i].mainRate+"%)"+source[i].mainValueRate+"</td><td>"+source[i].originalPropertyMap["C-200"]+"</td><td>"+source[i].originalPropertyMap['C-302']+"</td><td>"+source[i].originalPropertyMap['C-time']+"</td></tr>"
						}
						$("#sourceUrlId").html(tr);
						
						
						var network = data["network"];
						tr ="";
						for(var i=0;i<network.length;i++){
							tr += "<tr><td>"+network[i].keyName+"</td><td>"+network[i].mainValue+"</td><td>"+network[i].mainRate+"%</td></tr>";
						}
						$("#networkId").html(tr);
						
						var region = data["region"];
						tr ="";
						for(var i=0;i<region.length;i++){
							tr += "<tr><td>"+region[i].keyName+"</td><td>"+region[i].mainValue+"</td><td>"+region[i].mainRate+"%</td></tr>";
						}
						$("#regionId").html(tr);
						
					}
				});
            	
            	window.setTimeout("showotherInfo()",60000);
            }
            showotherInfo();
         
        </script>

</html>
