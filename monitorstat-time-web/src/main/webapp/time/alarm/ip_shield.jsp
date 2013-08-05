<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
	<head>
		<title>IP</title>
		<%@ include file="/time/common/base.jsp"%>
		<meta http-equiv="content-type" content="text/html;charset=gbk" />		
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>		

<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
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
		<%@include file="/header.jsp"%>
		<div class="container-fluid">
<div class="row-fluid" style="text-align: center">
<div class="span12" id="page_nav"></div>
<script>
	$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {urlPrefix:'<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=',appId: '${appInfo.appId}'});
</script>		
</div>
		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span12"> 
					<div class="row-fluid">
						<div class="span12">
							<div class="row-fluid">
								<form id="form_add" method="post" action="<%=request.getContextPath()%>/app/detail/alarm/show.do?method=addIpShield&appId=${appInfo.appId}">
								<input type="submit"  value="新增" name="addButton"/>
								<input type="text" name="ipName"/>
								</form>
								<form id="form2" method="post" action="<%=request.getContextPath()%>/app/detail/alarm/show.do?method=deleteIpShield&appId=${appInfo.appId}">
									<table
										class="table table-striped table-bordered table-condensed">
										<thead>
											<tr>
												<td>
													<input type="checkbox" id="cb1" />
													全选
												</td>
												<td width="250" style="text-align: center;">
													屏蔽告警的IP
												</td>
												
											</tr>
										</thead>

										<tbody id="exctbody">
											<c:forEach items="${ips }" var="item">
												<tr>
													<td>
														<input type="checkbox" name="ipId" value="${item }" />
													</td>
													<td width="250">
														${item}
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
									<input type="button" value="删除" name="deleteButton" />${deleteMessage }
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</body>



	<script type="text/javascript">
	
	$(function(){
		$("#cb1").click(function(){
				checked = this.checked;
				stateChange();
		});
		
		
		var deleteButtons = document.getElementsByName("deleteButton");
		$(deleteButtons).click(function(){
			deleteChoosedItem();
		});
	});
	

var checked;

function stateChange() {
	if (checked)
		chooseAll();
	else
		unChooseAll();

}

function chooseAll() {
	var c = document.getElementsByName("ipId");

	for (var i = 0; i < c.length; i++) {
		c[i].checked = true;
	}
}

function unChooseAll() {
	var c = document.getElementsByName("ipId");

	for (var i = 0; i < c.length; i++) {
		c[i].checked = false;
	}

}


            
            
    function deleteChoosedItem(){
    	var a = confirm("确定要删除吗？");
    	if(!a)
    		return;
		$("#form2").submit();
    
    }   
            
            
        </script>


</html>
