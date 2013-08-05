<%@ page language="java" contentType="text/html; charset=GB18030"  isELIgnored="false" 
    pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0">

<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/highcharts.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/exporting.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery-ui.min.js"></script>

<link type="text/css" href="<%=request.getContextPath() %>/statics/css/jquery-ui.css" rel="stylesheet" />

<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/datePicket/WdatePicker.js"></script>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<title>依赖模块</title>
</head>
<body>
<%@ include file="../../top.jsp" %>
<div class="container" style="color:red;font-size:12px;">
					说明：
					1.并不是每天计算成本,每周3周6计算成本　　　
					2.成本的计算方式请参考：
					<a href="<%=request.getContextPath() %>/cost_form.jsp" target="_blank">成本计算公式</a>　　　
					3.有任何问题请联系:毕衫、小赌  
					<br/>
</div>
<div class="container" style="font-size:12px;" id="tab-test2">

	<UL style="background-color:white;">
		 <LI><A href="#tab_all_1"><SPAN>我的成本详情</SPAN></A></LI>
		 <LI><A href="#tab_all_2"><SPAN>我的成本变化趋势</SPAN></A></LI>
		 <LI><A href="#tab_all_3"><SPAN>谁分摊我的成本</SPAN></A></LI>
	</UL>
	
	<DIV class="" style="border-top: 0; padding-bottom: 1em;">
		 <DIV id="tab_all_1">
				

				<div class="row" style="margin-top:15px;">
					<div class="span1" style="padding-top:4px;">日&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;期:</div>
					<div class="span2" style="margin-right:12px;">
					<input type="text" class="myself" id="selectDate" value="${dateStr}" style="width:110px;"  name="selectDate" 
								 onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					</div>

					<div class="span12">
						<iframe id="centerIframe" 
						  src="http://depend.csp.taobao.net:9999/depend/simple_page_nav.jsp?appName=${appName}&redirecturl=http://capacity.taobao.net:9999/cost/cost_mid.jsp&getPar=opsName"
							 width="100%" height="35px" frameborder="0" scrolling="no"></iframe>
					</div>
					
				</div>
				<DIV class="header-footer ui-state-default ui-corner-all" 
						style="padding: 3px 5px 5px; text-align: center; mmargin-bottom: 1ex;margin-top: 20px;">
						
					<table id="mytable">
						<tr><td>应用:${appName}</td><td>总成本:${appInfos.totalAllCost}</td></tr>
					</table>
				</DIV>
				
				<div id="chartPie">
				</div>
				
				<hr/>
				
				<c:if test="${( appInfos.hostInfos != null)}"> 
					<div class="alert-message info">
					机器成本
					  <span class="badge badge-success" style="float:right;">总成本:${appInfos.totalHostCost}&nbsp;&nbsp;&nbsp;&nbsp;<a target="_blank" 
						  href="<%=request.getContextPath() %>/appCost.do?method=showAppHosts&appName=${appName}">查看详情</a></span>
					  
					</div>
				</c:if>
				<c:if test="${( appInfos.hostInfos == null)}"> 
					<h5>机器成本</h5>
				</c:if>
				<table  class="bordered-table">
					 <thead>
						<tr>
							<th>id</th><th>机器型号</th><th>虚拟机</th><th>数量</th><th>累计成本</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${appInfos.hostInfos}" var="host" varStatus="status">
							  <tr>
								<td align="center">${status.index+1}</td>
								<td align="center">${host.macName}</td>
								<td align="center">${host.hostType}</td>
								<td align="center">${host.hostNum}</td>
								<td align="center">${host.totalPrice}</td>
							</tr>
						</c:forEach>	
					</tbody>
				</table>
			<hr/>
				<c:if test="${( appInfos.hsfCost != null)}"> 
					<div class="alert-message info">
					hsf依赖成本
					  <span class="badge badge-success" style="float:right;">总成本:${appInfos.totalHsfCost}</span>
					  
					</div>
				</c:if>
				<div id="tab-test2">
					<table  class="bordered-table">
						 <thead>
							<tr>
								<th>id</th><th>依赖应用</th><th>调用次数</th><th>成本</th><th>机器详情</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${appInfos.hsfCost}" var="hsf" varStatus="status">
								  <tr>
									<td align="center">${status.index+1}</td>
									<td align="center">${hsf.dependName}</td>
									<td align="center">${hsf.callNum}</td>
									<td align="center">${hsf.consumerCost}</td>
									<td align="center"><a href="http://time.csp.taobao.net:9999/time/app/conf/host/show.do?method=showIndex&appId=1"></a></td>
								</tr>
							</c:forEach>	
						</tbody>
					</table>
				</div><!---end tab test-->
				
				<hr/>	
				<c:if test="${( appInfos.baseDependList != null)}"> 
					<div class="alert-message info">
					基础依赖成本
					  <span class="badge badge-success" style="float:right;">总成本:${appInfos.totalBaseDependCost}</span>
					  
					</div>
				</c:if>
				<c:if test="${( appInfos.baseDependList == null)}"> 
					<h5>基础依赖成本</h5>
				</c:if>
				<div id="tab-test1">
					
					<UL>
						<c:forEach items="${appInfos.baseDependList}" var="baseItem" varStatus="status">
							 <LI><A href="#tab_${status.index}"><SPAN>${baseItem.key}</SPAN></A></LI>
						</c:forEach>
					</UL>
				
					<DIV class="" style="border-top: 0; padding-bottom: 1em;">
						
						<c:forEach items="${appInfos.baseDependList}" var="baseItem" varStatus="status">
							
							 <DIV id="tab_${status.index}">
								<table  class="bordered-table">
									 <thead>
										<tr>
											<th>id</th><th>group</th>
											<th>调用次数</th><th>成本</th><th>详情</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${baseItem.value}" var="itemValue" varStatus="status2">
											  <tr>
												<td align="center">${status2.index+1}</td>
												<td align="center">${itemValue.dependName}</td>
												<td align="center">${itemValue.callNum}</td>
												<td align="center">${itemValue.consumerCost}</td>
												<td align="center"></td>
											</tr>
										</c:forEach>	
									</tbody>
								</table>
							</div>
						</c:forEach>
					</div>
				</div><!---end tab test-->
				<input type='hidden' id="hiddenPieData" value='${appInfos.jsonChart}'>
				
				<form name="mainform" action="<%=request.getContextPath() %>/appCost.do?method=showApp" method="post" >
						 <input type='hidden' id="appIputId" value='' name='appName'>
						 <input type='hidden' id="dateIputId" value='' name='dateStr'>
				</form>

		</div>
		<DIV id="tab_all_2">
			<jsp:include page="innerPic.jsp" >
				<jsp:param name="app" value="${appName}" />
				<jsp:param name="vinfo" value="${appInfos.costTrend}" />
				<jsp:param name="dinfo" value="${appInfos.costTrendDay}" />
			</jsp:include>
		</div>
		
		<DIV id="tab_all_3">
			我的基础依赖总成本（机器+DB+TIR）：${appInfos.directAll}
			
			<table  class="bordered-table">
				 <thead>
					<tr>
						<th>id</th><th>应用</th>
						<th>调用次数</th><th>成本</th><th>详情</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${appInfos.costCat}" var="itemValue" varStatus="status3">
						  <tr>
							<td align="center">${status3.index+1}</td>
							<td align="center">${itemValue.appName}</td>
							<td align="center">${itemValue.callNum}</td>
							<td align="center">${itemValue.dependCost}</td>
							<td align="center">
								<a target="_blank" href="http://capacity.taobao.net:9999/cost/appCost.do?method=showApp&appName=${itemValue.appName}">我的成本详情<a></td>
						</tr>
					</c:forEach>	
				</tbody>
			</table>
		</div>
</div>
				
	
		
</div>

<script type="text/javascript">
	//初始化search bar
	function onResponse(value){
	  document.getElementById("appIputId").value=value;
	  document.getElementById("dateIputId").value= document.getElementById("selectDate").value;
	  
	  document.mainform.submit();
	}  

	$(document).ready( function() {
		$("#tab-test1").tabs();
		$("#tab-test2").tabs();
		
		$(".bordered-table tr td").mouseover(function(){
	         $(this).parent().children("td").addClass("report_on");
	      });
		 $(".bordered-table tr td").mouseout(function(){
       		 $(this).parent().children("td").removeClass("report_on");
    		 });
    		 

		 var colors = Highcharts.getOptions().colors,
		 dataStr = document.getElementById("hiddenPieData").value;
		 
		 // Build the data arrays
	        var browserData = [];
	        var versionsData = [];
	        
	        var data=eval(dataStr);
	        for (var i = 0; i < data.length; i++) {
	    
	            // add browser data
	            browserData.push({
	                name: data[i].name,
	                y: data[i].y,
	                color: colors[i]
	            });
	    
	            // add version data
	            for (var j = 0; j < data[i].drilldown.data.length; j++) {
	                var brightness = 0.2 - (j / data[i].drilldown.data.length) / 5 ;
	                versionsData.push({
	                    name: data[i].drilldown.categories[j],
	                    y: data[i].drilldown.data[j],
	                    color: Highcharts.Color(colors[i]).brighten(brightness).get()
	                });
	            }
	        }
	    
	        // Create the chart
	        chart = new Highcharts.Chart({
	            chart: {
	                renderTo: 'chartPie',
	                type: 'pie'
	            },
	            title: {
	                text: '成本组成'
	            },
	            yAxis: {
	                title: {
	                    text: ''
	                }
	            },
	            plotOptions: {
	                pie: {
	                    shadow: false
	                }
	            },
	            tooltip: {
	                valueSuffix: '%'
	            },
	            series: [{
	                name: '组成',
	                data: browserData,
	                size: '60%',
	                dataLabels: {
	                    formatter: function() {
	                        return this.y > 5 ? this.point.name : null;
	                    },
	                    color: 'white',
	                    distance: -30
	                }
	            }, {
	                name: '明细',
	                data: versionsData,
	                innerSize: '60%',
	                dataLabels: {
	                    formatter: function() {
	                        // display only if larger than 1
	                        return this.y > 1 ? '<b>'+ this.point.name +':</b> '+ this.y +'%'  : null;
	                    }
	                }
	            }]
	        });
	});
	
</script>

<style type="text/css">

	#mytable {
		width: 100%; 
		padding: 0;
		margin: 0;
	}
	
	#mytable td {
		border-right: 1px solid #C1DAD7;
		border-bottom: 1px solid #C1DAD7;
		background: #fff;
		font-size:14px;
		font-weight:bolder;
		padding: 6px 6px 6px 12px;
		color: #4f6b72;
	}
	
	#mytable td.alt {
		background: #F5FAFA;
		color: #797268;
	}
	h5 {
  margin-top:20px;

}

</style>
    
</body>
</html>