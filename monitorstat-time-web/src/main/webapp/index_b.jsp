<%@ page language="java" import="java.util.*" pageEncoding="GBK" isELIgnored="false"%>
<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
	<head>
		<title>ʵʱ���ϵͳ</title>
		<%@ include file="/time/common/base.jsp"%>
		<meta http-equiv="content-type" content="text/html;charset=gbk" />
		<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jsPlumb.css">
		<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/index.css">
		<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
		<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
		<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
		<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
		<script type="text/javascript"   src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
		<script type="text/javascript"		src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>
		<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery//jquery.jsPlumb-all-min.js"></script>
		
		

		<style>

#graph_1 {
	background-color: white;
	background-image: url(<%=request.getContextPath()%>/statics/css/images/dynamicAnchorBg.jpg );
	position: relative;
	left: 0px;
	width: 100%;
	height: 800px;
	overflow: hidden;
}
.window td {
	font-size: 12px
}
</style>

	</head>
	<body onunload="jsPlumb.unload();">


		<%@ include file="/header.jsp"%>

		<div class="index_body">
			<div class="container-fluid">
				<div class="row-fluid"  style="text-align: center">
					<div class="span12" id="page_nav"></div>
<script>
	$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {urlPrefix:'<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=',appId: '${appInfo.appId}'});
</script>	
				</div>
				<!-- row end -->
				<div class="row-fluid" >
					<!-- ����position������Ϊabsolute��relative -->
					<div id="graph_1">
						<c:forEach items="${appList }" var="item">
						<div class="component window" id="opsname_${item.appName}" onclick="popoverAppDetail('${item.appId}')" onmouseover="this.style.backgroundColor='green'" onmouseout="this.style.backgroundColor=''" title="����鿴����">
							<table>
								<tr>
									<td ><a href="<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=${item.appId}">${item.appName} </a>( <font id="${item.appName}_hostSize"></font> ̨)</td>
								</tr>
								<tr>
									<td>[<font id="${item.appName}_collectTime"></font>]&nbsp;pv:<font id="${item.appName}_pv"></font></td>
								</tr>
								<c:if test="${item.appType =='pv' }">
								<tr>
									<td>qps:<font id="${item.appName}_qps"></font>&nbsp;ʧ����:<font id="${item.appName}_failRate"></font></td>
								</tr>
								</c:if>
									<c:if test="${item.appType =='center' }">
										<tr>
											<td >qps:<font id="${item.appName}_qps"></font></td>
										</tr>
									</c:if>
								<tr>
									<td >����:<font id="${item.appName}_capacity"></font>
									<c:if test="${item.appType =='pv' }">
										pagesize:<font id="${item.appName}_pagesize"></font>
									</c:if>
									</td>
								</tr>
								<tr>
									<td  style=" color: red"><a href="<%=request.getContextPath()%>/app/detail/exception/show.do?method=showIndex&appId=${item.appId}">�쳣:<font  id="${item.appName}_exceptionCount"></font></a>&nbsp;�澯: <font id="${item.appName}_alarm"></font></td>
								</tr>
								<tr>
														
								</tr>

							</table>
						</div>
						<!-- window end -->
						
				</c:forEach>
				
					
					</div>
					<!-- span end -->
				</div>
				<!-- row end -->
				<div class="row-fluid" >
					<table width="100%">
						<tr>
							<td width="50%" valign="top">
								<table class="table table-striped table-condensed table-bordered "	id="table1">
									<caption>
										<strong>�쳣���� TOP 10</strong>
									</caption>
									<thead id="exceptionTopThread">
										<tr>
		
										</tr>
									</thead>
									<tbody id="exceptionTopTbody">
		
		
									</tbody>
								</table>
							</td>
							<td width="50%"  valign="top">
									<table class="table table-striped table-condensed table-bordered " id="table2">
									<caption>
										<strong>�澯TOP 10</strong>
									</caption>
									<thead id="alarmTopThread">
										<tr>
		
										</tr>
									</thead>
									<tbody>
		
		
		
									</tbody>
		
								</table>
							</td>
						</tr>
					</table>
				</div>
				<!-- row end -->
			</div>
			<!-- content end -->
		</div>
		<!-- container end -->
		
		<div id="appDentailMessageDiv" ></div>
		<div id="referDiv" ></div>
		<div id="waitDiv"   class="alert alert-block alert-error fade in"  style="position: absolute;left: 0px;top:100px">���ڻ�ȡ����....</div>
		
	</body>
<script type="text/javascript">
jsPlumb.bind("ready", function() {
	
	$( "#appDentailMessageDiv" ).dialog({
		autoOpen: false
	});
	$( "#referDiv" ).dialog({
		autoOpen: false
	});
	
	// chrome fix.
	document.onselectstart = function() {
		return false;
	};
	
	jsPlumb.draggable(jsPlumb.getSelector(".window"));
	jsPlumb.Defaults.Container = $("#graph_1");
	jsPlumb.setMouseEventsEnabled(true);
	grap();
});

function grap(){
	
	
	
	$.ajax({
		url : "<%=request.getContextPath()%>/index.do?method=getApps&company=${company}",
		success : function(data) {
			jsPlumb.removeEveryEndpoint();
			for(var i=0;i<data.length;i++){
				var ie = data[i];
				$("#"+ie["appName"]+"_collectTime").text(ie.ftime);
				$("#"+ie["appName"]+"_pv").html(ie.pv+""+ie.pvRate);
				$("#"+ie["appName"]+"_qps").text(ie.qps);
				if($("#"+ie["appName"]+"_failRate"))
					$("#"+ie["appName"]+"_failRate").text(ie.failurerate + "%");
				if($("#"+ie["appName"]+"_pagesize"))
					$("#"+ie["appName"]+"_pagesize").text(ie.pageSize + "K");				
				$("#"+ie["appName"]+"_capacity").text(ie.capcityRate);
				$("#"+ie["appName"]+"_alarm").text(ie.alarms);
				$("#"+ie["appName"]+"_exceptionCount").html(ie.exceptionNum+""+ie.exceptionRate);
				$("#"+ie["appName"]+"_hostSize").text(ie.machines);
				
				
				var refer = data[i].referMap;
				for(referapp in refer){
					var rate = (refer[referapp] / ie.pv * 100).toFixed(2);
					if(rate<1||rate >100){
						continue;
					}
					if(referapp == ie["appName"]){
						continue;
					}
					var labelText1 = "<span  title='����鿴����' onclick=\"referDetail('"+ie["appName"]+"','"+referapp+"')\">����:"	+ rate + "%</span>";
					
					jsPlumb.connect({
						source : "opsname_" + referapp,
						target : "opsname_" + ie["appName"],
						connector : "Bezier",
						anchors : ["AutoDefault", "AutoDefault"],
						paintStyle : {lineWidth : 2,	strokeStyle : "gray"	},
						endpointStyle : {   radius:9, fillStyle:"gray"},
						hoverPaintStyle : {strokeStyle : "#ec9f2e"	},
						overlays : [["Label", {cssClass : "l1 component label ",	label : labelText1,	location : 0.5	}],
						            [ "Arrow", { location:0.2 }, { foldback:0.7, fillStyle: "gray", width:14 } ]]
					}); // connection end
				}
			}
			window.setTimeout("grap()",20000);
			$("#waitDiv").hide();
		}
	});
	
	
}


function queryExceptionTop(){
	
	$.ajax({
		url : "<%=request.getContextPath()%>/app/detail/exception/show.do?method=getRecentlyExceptionTop",
		success : function(data) {
			var recentlyTime = data["recentlyTime"];
			var topExceptionMap = data["topExceptionMap"];
			
			var titleTr = "<tr>";
			for(var i=0;i<recentlyTime.length;i++){
				titleTr+="<td width='200'>"+recentlyTime[i]+"</td>";
			}
			titleTr += "</tr>";
			$("#exceptionTopThread").html(titleTr);
			$("#alarmTopThread").html(titleTr);
			var dataTr = "";
			for(var k=0;k<10;k++){
				dataTr+="<tr>";
				for(var i=0;i<recentlyTime.length;i++){
					if(topExceptionMap[recentlyTime[i]]){
						var e = topExceptionMap[recentlyTime[i]];
						if(k<e.length){
							dataTr+="<td><a href=\"<%=request.getContextPath()%>/app/detail/exception/show.do?method=showIndex&appId="+e[k]['appId']+"\">"+e[k]['appName']+"["+e[k]['mainValue']+"]</a></td>";
						}else{
							dataTr+="<td>&nbsp;</td>";
						}
					}else{
						dataTr+="<td>&nbsp;</td>";
					}
					
				}
				dataTr+="</tr>";
			}
			$("#exceptionTopTbody").html(dataTr);
			
			window.setTimeout("queryExceptionTop()",60000);
		}
	});
}
queryExceptionTop();


function popoverAppDetail(appId){
	$("#appDentailMessageDiv").html("");
	 $("#appDentailMessageDiv").load("<%=request.getContextPath()%>/time/popover_app_table.jsp?appId="+appId,  function(){
		 	$( "#appDentailMessageDiv" ).dialog( "option", "width", 1000 );
			$( "#appDentailMessageDiv" ).dialog( "open" );
		 }); 
}

function referDetail(sapp,dapp){
	
}


</script>
</html>
