<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
<head>
<title>应用机器列表管理</title>

<meta http-equiv="content-type" content="text/html;charset=gbk" />
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery-ui.min.js"></script>

<link type="text/css" href="<%=request.getContextPath() %>/statics/css/jquery-ui.css" rel="stylesheet" />

<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/EditTable.js"></script>
	
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
	<%@ include file="../../top.jsp"%>

	<div class="container">
		<div class="row-fluid" style="margin:20px 0px 10px 0px;">
			
			<iframe id="centerIframe"
				src="http://depend.csp.taobao.net:9999/depend/simple_page_nav.jsp?appName=${param.appName}&redirecturl=http://capacity.taobao.net:9999/cost/cost_mid.jsp&getPar=opsName"
				width="100%" height="35px" frameborder="0" scrolling="no"></iframe>
		
				 <span class="label label-success">单价列可双击编辑</span>
				 <span id="errorMsg" class="label label-error" style="font-size:14px;"></span>
		</div>
		
		<div class="row-fluid bordered-table" class="span12">

			<table id="hostCostListTable"
				class="table table-striped table-bordered table-condensed"	width="100%">
				<thead>
					<tr>
						<td style="text-align: center;" id="time1">主机名</td>
						<td style="text-align: center;" id="time3">机房</td>
						<td style="text-align: center;" id="time4">主机类型</td>
						<td style="text-align: center;">虚拟机</td>
						<td style="text-align: center;">硬件信息</td>
						<td style="text-align: center;">成本</td>
					</tr>
				</thead>

				<tbody id="exctbody">
					<c:forEach items="${list}" var="item">
						<tr>
							<td style="text-align: center;">${item.hostName}</td>
							<td style="text-align: center;">${item.detailHostInfo.hostSite }</td>
							<td style="text-align: center;">${item.hostType}</td>
							<td style="text-align: center;">						
								<c:choose>
								   <c:when test="${item.detailHostInfo.isVirtualHost()}">
								   		虚拟机&nbsp;&nbsp;&nbsp;一虚${item.parentSplitSize}
								   </c:when>
								   <c:otherwise>
								   	实体机
								   </c:otherwise>
								</c:choose>
							</td>
							<td style="text-align: center;">${item.hardInfo}</td>
							<td style="text-align: center;" class="editThis" myProp="${item.hostName}">${item.hostPrice}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
				
		</div>
	</div>
	<form name="mainform" action="<%=request.getContextPath() %>/appCost.do?method=showAppHosts"
		 method="post" >
			 <input type='hidden' id="appIputId" value='' name='appName'>
	</form>
</body>

	<script type="text/javascript">
 
	function onResponse(value){
	  document.getElementById("appIputId").value=value;
	  
	  document.mainform.submit();
	}  
	function onEditDone(td,oldV,hostn){
		var n= new Number(td.text().trim());
		if(!isNaN(n)){
			if(n<100){
				alert("数值必须大于100");
				td.html(oldV); 
				return;
			}
			
			n=n.toFixed(1);

			$.post("<%=request.getContextPath()%>/appCost.do",{method:"editHostCost",hostName:hostn,cost:n},function(result){
				$("#errorMsg").show()
				result=replaceRn(result);
				result=removeAllSpace(result);
				
				if(result!=""){
					td.html(oldV); 
					$("#errorMsg").html(result);
				}else{
					td.html(n); 
					$("#errorMsg").html("应用:"+hostn+"修改成本成功");
				}
			});
		}else{
			alert("只能输入数值类型的值");
			td.html(oldV); 
		}
			
	}  

	function removeAllSpace(str) {
	  return str.replace(/\s+/g, "");
	}

	function replaceRn(str){
  	  return str.replace(/\r\n|\n/g,"");
	}

	$(function(){
		$("#errorMsg").hide()
		tableEdit.readyCall("#hostCostListTable .editThis","myProp",onEditDone);
		
		$(".bordered-table tr td").mouseover(function(){
	         $(this).parent().children("td").addClass("report_on");
	      })
		 $(".bordered-table tr td").mouseout(function(){
   		 $(this).parent().children("td").removeClass("report_on");
		 })
	});
    </script>

<style type="text/css">

	.label-success,
	.badge-success {
	  background-color: #468847;
	  font-size:12x;
	}
	.label-error{
		background-color:red;
		font-size:14x;
	}

</style>
</html>
