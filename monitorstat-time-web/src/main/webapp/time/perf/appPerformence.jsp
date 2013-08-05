<%@ page language="java" contentType="text/html; charset=GBK" pageEncoding="GBK" isELIgnored="false"%>
<%@page import="com.taobao.csp.dataserver.KeyConstants"%>
<%@page import="com.taobao.csp.dataserver.PropConstants"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>实时监控系统</title>
<%@ include file="/time/common/base.jsp"%>
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>


<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript"   src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>

<script>

</script>
			
<style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
      .sidebar-nav {
        padding: 9px 0;
      }
.popover-inner {
  padding: 3px;
  width: 350px;
  overflow: hidden;
  background: #000000;
  background: rgba(0, 0, 0, 0.8);
  -webkit-border-radius: 6px;
  -moz-border-radius: 6px;
  border-radius: 6px;
  -webkit-box-shadow: 0 3px 7px rgba(0, 0, 0, 0.3);
  -moz-box-shadow: 0 3px 7px rgba(0, 0, 0, 0.3);
  box-shadow: 0 3px 7px rgba(0, 0, 0, 0.3);
}
</style>
</head>
<body>
<%@include file="/header.jsp" %>
<div class="container-fluid">
<div class="row-fluid" style="text-align: center">
	<div class="span12" id="page_nav"></div>
<script>
	$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {urlPrefix:'<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=',appId: '${appInfo.appId}'});
</script>	
</div>
<div class="row-fluid">
<div class="span2">
<%@include file="/leftmenu.jsp" %>
</div>
<div class="span12">
<a href="<%=request.getContextPath()%>/index.jsp">实时首页</a> -><a href="<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=${appInfo.appId}">${appInfo.appName} </a>->性能数据
<hr>
<div class="row-fluid">
	<div class="span4">
		<table class="table table-striped table-bordered table-condensed" id="cpuTop10">
			<caption>
				<strong>CPU TOP 10<a href="<%=request.getContextPath()%>/app/detail/perf/show.do?method=gotoDetail&appId=${appInfo.appId}&key=<%=KeyConstants.TOPINFO %>&mainProp=<%=PropConstants.CPU %>&title=CPU">详细</a></strong>
			</caption>
			<thead>
			<tr><th>IP</th><th>CPU</th><th>时间</th></tr>
			</thead>
			<tbody id="cpu">
			</tbody>
		</table>
	</div>
	<div class="span4">
		<table class="table table-striped table-bordered table-condensed" id="swapTop10">
			<caption>
				<strong>SWAP TOP 10<a href="<%=request.getContextPath()%>/app/detail/perf/show.do?method=gotoDetail&appId=${appInfo.appId}&key=<%=KeyConstants.TOPINFO %>&mainProp=<%=PropConstants.SWAP %>&title=SWAP">详细</a> </strong>
			</caption>
			<thead>
			<tr><th>IP</th><th>SWAP</th><th>时间</th></tr>
			</thead>
			<tbody id="swap">
			</tbody>
		</table>
	</div>
	<div class="span4">
		<table class="table table-striped table-bordered table-condensed" id="loadTop10">
			<caption>
				<strong>LOAD TOP 10<a href="<%=request.getContextPath()%>/app/detail/perf/show.do?method=gotoDetail&appId=${appInfo.appId}&key=<%=KeyConstants.TOPINFO %>&mainProp=<%=PropConstants.LOAD %>&title=LOAD">详细</a> </strong>
			</caption>
			<thead>
			<tr><th>IP</th><th>LOAD</th><th>时间</th></tr>
			</thead>
			<tbody id="load">
			</tbody>
		</table>
	</div>

<div class = "row-fluid">
	<div class="span4">
		<table class="table table-striped table-bordered table-condensed" id="memoryTop10">
			<caption>
				<strong>Memory TOP 10 <a href="<%=request.getContextPath()%>/app/detail/perf/show.do?method=gotoDetail&appId=${appInfo.appId}&key=<%=KeyConstants.JVMINFO %>&mainProp=<%=PropConstants.JVMMEMORY %>&title=JVMMEMORY">详细</a></strong>
			</caption>
			<thead>
			<tr><th>IP</th><th>MEMORY</th><th>时间</th></tr>
			</thead>
			<tbody id="memory">
			</tbody>
		</table>
	</div>
	<div class="span4">
		<table class="table table-striped table-bordered table-condensed" id="fullgcTop10">
			<caption>
				<strong>FULL GC TOP 10 <a href="<%=request.getContextPath()%>/app/detail/perf/show.do?method=gotoDetail&appId=${appInfo.appId}&key=<%=KeyConstants.JVMINFO %>&mainProp=<%=PropConstants.JVMFULLGC %>&title=JVMFULLGC">详细</a></strong>
			</caption>
			<thead>
			<tr><th>IP</th><th>FULLGC</th><th>时间</th></tr>
			</thead>
			<tbody id="fullgc">
			</tbody>
		</table>
	</div>
	<div class="span4">
		<table class="table table-striped table-bordered table-condensed" id="gcTop10">
			<caption>
				<strong>GC TOP 10 <a href="<%=request.getContextPath()%>/app/detail/perf/show.do?method=gotoDetail&appId=${appInfo.appId}&key=<%=KeyConstants.JVMINFO %>&mainProp=<%=PropConstants.JVMGC%>&title=JVMGC">详细</a></strong>
			</caption>
			<thead>
			<tr><th>IP</th><th>GC</th><th>时间</th></tr>
			</thead>
			<tbody id="gc">
			</tbody>
		</table>
	</div>
</div>
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
						              				<td >CPU</td>
						              				<td id="cpu_${vs.count}"><font>11</font></td>
						              			</tr>
						              			<tr>
						              				<td>LOAD</td>
						              				<td id="load_${vs.count}"><font>11</font></td>
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
			url : "<%=request.getContextPath()%>/app/detail/perf/show.do?method=ipDetail&appId=${appInfo.appId}",
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
						var cpu = "cpu_"+size;
						var load = "load_"+size;
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
						
						$("#"+ip).text(obj.ip+"("+ obj.site+")");
						$("#"+ip).attr("data-content",h);
						$("#"+ip).attr("data-original-title",obj.ip);
						//详情页面
						//ipclick(ip, obj.ip);						
							
						$("#"+cpu).html(obj.originalPropertyMap['CPU']);
						$("#"+load).text(obj.originalPropertyMap['LOAD']);
			
					}
			}
		});
		window.setTimeout("showHost()",60000);


	}
	showHost();
	$("a[rel=popover]").popover({placement:'left'}).click(function(e) {
      e.preventDefault()
    })
    
      function showotherInfo(){
            	$.ajax({
					url : "<%=request.getContextPath()%>/app/detail/perf/show.do?method=top10&appId=${appInfo.appId}",
					success : function(data) {
						var cpu = data["cpu"];
						var tr ="";
						for(var i=0;i<cpu.length;i++){
							var item = cpu[i];
							 tr += "<tr><td>"+item.ip+"</td><td>"+item.mainValue+item.mainValueRate+"</td><td>"+item.ftime+"</td></tr>"
						}
						$("#cpu").html(tr);
						
						var swap = data["swap"];
						var tr ="";
						for(var i=0;i<swap.length;i++){
							var item = swap[i];
							 tr += "<tr><td>"+item.ip+"</td><td>"+item.mainValue+item.mainValueRate+"</td><td>"+item.ftime+"</td></tr>"
						}
						$("#swap").html(tr);
						
						var load = data["load"];
						var tr ="";
						for(var i=0;i<load.length;i++){
							var item = load[i];
							 tr += "<tr><td>"+item.ip+"</td><td>"+item.mainValue+item.mainValueRate+"</td><td>"+item.ftime+"</td></tr>"
						}
						$("#load").html(tr);
						
						var memory = data["memory"];
						var tr ="";
						for(var i=0;i<memory.length;i++){
							var item = memory[i];
							 tr += "<tr><td>"+item.ip+"</td><td>"+item.mainValue+item.mainValueRate+"</td><td>"+item.ftime+"</td></tr>"
						}
						$("#memory").html(tr);
						
						var fullgc = data["fullgc"];
						var tr ="";
						for(var i=0;i<fullgc.length;i++){
							var item = fullgc[i];
							 tr += "<tr><td>"+item.ip+"</td><td>"+item.mainValue+item.mainValueRate+"</td><td>"+item.ftime+"</td></tr>"
						}
						$("#fullgc").html(tr);		
						
						var gc = data["gc"];
						var tr ="";
						for(var i=0;i<gc.length;i++){
							var item = gc[i];
							 tr += "<tr><td>"+item.ip+"</td><td>"+item.mainValue+item.mainValueRate+"</td><td>"+item.ftime+"</td></tr>"
						}
						$("#gc").html(tr);			
						
					}
				});
            	
            	window.setTimeout("showotherInfo()",60000);
            }
            showotherInfo();
         
 
	
</script>
</html>