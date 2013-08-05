<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
	<head>
		<title>�������</title>
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
					<a href="<%=request.getContextPath()%>/index.jsp">ʵʱ��ҳ</a> ->
					<a
						href="<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=${appInfo.appId}">${appInfo.appName}</a>

					->HSf Provider ������Ϣ
					<hr>
					<div class="row-fluid"  id="pvinforow">
					<script type="text/javascript">
					$("#pvinforow").load("<%=request.getContextPath()%>/index.do?method=queryIndexTableForHSf&appId=${appInfo.appId}");
					</script>
					</div>
					<div class="row-fluid">
						<div class="span12">
							<h5>
								ȫ��HSf Provider������Ϣ &nbsp;&nbsp;&nbsp; <a href="<%=request.getContextPath()%>/app/detail/history.do?method=showHistory&appName=${appInfo.appName}&keyName=HSF-provider" target="_blank">��������ʷ����</a>&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
								<a href="<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=gotohsfProvider&appId=${appInfo.appId}">ÿ̨��������</a>
							</h5>
							<div class="row-fluid thumbnail">
								<table width="100%">
									<tr>
										<td width="100%" id="pvchartDivId" style="height: 350px"
											colspan="2">
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
									                data_file: escape("<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=showHistoryHsProviderPv&appId=${appInfo.appId}")
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
										<td width="30%" valign="top">
											<h5>
												��ԴӦ���������(top10)
												<a
													href="<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=gotohsfRefer&appId=${appInfo.appId}">��ϸ</a>
											</h5>
											<table width="100%"
												class="table table-striped table-bordered table-condensed">
												<thead>
													<tr>
														<td width="100" style="text-align: center;">
															��ԴӦ��
														</td>
														<td width="100" style="text-align: center;">
															������
														</td>
														<td width="100" style="text-align: center;">
															����
														</td>
														<td width="100" style="text-align: center;">
															����
														</td>
													</tr>
												</thead>
												<tbody id="referApp">

												</tbody>
											</table>
										</td>
										<td width="70%" valign="top">
											<h5>
												�ӿ����������(top10)
												<a
													href="<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=gotohsfProvider&appId=${appInfo.appId}">����</a>
											</h5>
											<table width="100%"
												class="table table-striped table-bordered table-condensed">
												<thead>
													<tr>
														<td style="text-align: center;">
															�ṩ�ӿ�
														</td>
														<td width="100" style="text-align: center;">
															������
														</td>
														<td width="100" style="text-align: center;">
															����
														</td>
														<td width="100" style="text-align: center;">
															����
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
							<hr>
							<h4>
								�������ñ���
							</h4>
							<div class="row-fluid">
								<div class="span12 thumbnail " id="area_rate">
								</div>
							</div>
							<hr>
							<h4>
								����������Ϣ
							</h4>
							<!-- �����PV Ӧ�� չʾPV ��Ϣ -->
							<div class="row-fluid">
								<div class="span12 thumbnail ">
									<!-- ���� �����б�  -->
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
															<font>��11</font>
														</td>
													</tr>
													<tr>
														<td id="rest_${vs.count}">
															22ms
														</td>
														<td id="rest_rate_${vs.count}">
															<font>��11</font>
														</td>
													</tr>
												</tbody>
											</table>
										</div>
									</c:forEach>
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
			url : "<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=showhsfProviderHost&appId=${appInfo.appId}",
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
						//���������б�  ips[i].propertyMap.prop
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
						//����ҳ��
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
								var url = "<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=gotoHostDetail&appId=${appInfo.appId}&ip="+ip;
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
					url : "<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=findHsfReferAppAndProviderInterface&appId=${appInfo.appId}",
					success : function(data) {
						var refer = data["referList"];
						var tr ="";
						for(var i=0;i<refer.length;i++){
							 tr += "<tr><td>"+refer[i].keyName+"</td><td>"+refer[i].mainValue+refer[i].mainValueRate+"</td><td>"+refer[i].mainRate+"%</td><td><a href='<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=gotohsfRefer&appId=${appInfo.appId}'>�鿴</a></td></tr>"
						}
						$("#referApp").html(tr);
						
						var source = data["providerList"];
						tr ="";
						for(var i=0;i<source.length;i++){
							tr += "<tr><td>"+source[i].keyName+"</td><td>"+source[i].mainValue+source[i].mainValueRate+"</td><td>"+source[i].mainRate+"%</td><td><a href='<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=gotohsfProvider&appId=${appInfo.appId}'>�鿴</a></td></tr>"
						}
						$("#interfaceApp").html(tr);
						
					}
				});
            	
            	window.setTimeout("showotherInfo()",60000);
            }
            showotherInfo();
            
            
            function areaRate(){
				$.ajax({
					url : "<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=queryAreaRate&appId=${appInfo.appId}",
					success : function(data) {
						var cms = data;
						//����cm��ÿ��cm��һ��float table
						var tables = "";
						for(var i=0;i<cms.length;i++){
							var cm = cms[i];
							tables +="<div style='float:left' class='span5'> <h5>"+cm.keyName+"</h5> ";
							tables +="<table class='table table-striped table-bordered table-condensed ' >";
							tables +="<thead><tr><td>����</td><td>������</td><td>����</td></tr></thead>";
							tables +="<tbody>";
							var propertyMap = cm.propertyMap;
							var total = 0;
							for(var prop in propertyMap)
								total += propertyMap[prop];
							
							for(var prop in propertyMap){
								var propV = propertyMap[prop];
								tables +="<tr><td>"+ prop +"</td><td>"+propV+"</td><td>"+(propV/total*100).toFixed(2)+"%</td></tr>";
							}
							tables +="</tbody></table></div>";
					    }
					    $("#area_rate").html(tables);
					    
					}
				});
				window.setTimeout("areaRate()",60000);
            }
            areaRate();
        </script>

</html>
