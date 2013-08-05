<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
	<head>
		<title>应用机器列表管理</title>
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
					应用机器列表管理 &nbsp;${appInfo.appName }
					<hr>
					<div class="row-fluid">
						<div class="span12">

							<div class="row-fluid ">
							<table>
							<tr>
							<td>
									<form
									action="<%=request.getContextPath()%>/app/conf/host/show.do?method=upload&appId=${appInfo.appId}"
									method="post" enctype="multipart/form-data" style="float: left; ">
									<table>
										<tr>
											<td>
												文件导入：
											</td>
											<td>
												<input id="file1" type="file" name="file" />
											</td>
											<td>
												<input type="submit" value="导入"></input>
											</td>
											<td>
												示例：
												<a href="<%=request.getContextPath()%>/a.xls">a.xls</a>
											</td>
											
										</tr>
										<tr>
											<td colspan="4">
												${message }
											</td>
										</tr>
									</table>

								</form> 
							</td>
							<td>
									<form  
									action="<%=request.getContextPath()%>/app/conf/host/show.do?method=ipImport&appId=${appInfo.appId}"
									method="post" enctype="multipart/form-data" style="float: left; margin-left:30px;">
									<table style="5px solid blue">
										<tr>
											<td>
												IP导入：
											</td>
											<td>
												<input type="file" name="file" />
											</td>
											<td>
												<input type="submit" value="导入"></input>
											</td>
											<td>
												示例：
												<a href="<%=request.getContextPath()%>/b.xls">b.xls</a>
											</td>
								
										</tr>
<tr>
											<td colspan="4">
													${messageForIpImport}
											</td>
										</tr>
									</table>

								</form>
							</td>
							</tr>
							</table>
						
						

							</div>
							<div class="row-fluid">
								<form id="form2" method="post"
									action="<%=request.getContextPath()%>/app/conf/host/show.do?method=delete&appId=${appInfo.appId}">
									<input type="button" value="删除" name="deleteButton" />${deleteMessage }
									<table
										class="table table-striped table-bordered table-condensed"
										width="100%">
										<thead>




											<tr>
												<td>
													<input type="checkbox" id="cb1" />
													全选
												</td>
												<td width="250" style="text-align: center;">
													应用名
												</td>
												<td style="text-align: center;" id="time1">
													主机名
												</td>
												<td style="text-align: center;" id="time2">
													IP
												</td>
												<td style="text-align: center;" id="time3">
													机房
												</td>
												<td style="text-align: center;" id="time3">
													主机类型
												</td>
											</tr>
										</thead>

										<tbody id="exctbody">
											<c:forEach items="${list }" var="item">
												<tr>
													<td>
														<input type="checkbox" name="ip" value="${item.hostIp }" />
													</td>
													<td width="250">
														${item.opsName }
													</td>
													<td style="text-align: center;">
														${item.hostName }
													</td>
													<td style="text-align: center;">
														${item.hostIp }
													</td>
													<td style="text-align: center;">
														${item.hostSite }
													</td>
													<td style="text-align: center;">
														${item.hostType}
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
	var c = document.getElementsByName("ip");

	for (var i = 0; i < c.length; i++) {
		c[i].checked = true;
	}
}

function unChooseAll() {
	var c = document.getElementsByName("ip");

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
