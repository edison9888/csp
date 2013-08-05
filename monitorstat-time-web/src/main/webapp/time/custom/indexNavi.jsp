<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<!doctype html>
<html>
<head>
<title>导航操作</title>
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
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/time_commons.js"></script>
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
				<a href="<%=request.getContextPath()%>/index.jsp">实时首页</a> -> 用户导航操作
				&nbsp;${userName}
				<hr>
				<div class="row-fluid">
					<div class="span12">
						<div class="row-fluid">
							<form id="form_add" method="post"
								action="<%=request.getContextPath()%>/app/detail/custom/show.do?method=addNavi">
								<input type="submit" value="新增" name="addButton" /> <input
									type="text" name="naviName" /> <input type="hidden"
									value="${appInfo.appId}" name="appId">
							</form>
							<form id="form2" method="post"
								action="<%=request.getContextPath()%>/app/detail/custom/show.do?method=deleteNavi&appId=${appInfo.appId}">
								<table
									class="table table-striped table-bordered table-condensed">
									<thead>
										<tr>
											<td><input type="checkbox" id="cb1" /> 全选</td>
											<td width="250" style="text-align: center;">导航</td>
											<td style="text-align: center;" id="time1">编辑</td>
										</tr>
									</thead>

									<tbody id="exctbody">
										<c:forEach items="${navis }" var="item">
											<tr>
												<td><input type="checkbox" name="naviId"
													value="${item.naviId }" /></td>
												<td width="250" class="edittd" id="${item.naviId }">${item.naviName }</td>
												<td style="text-align: center;"><a
													href="<%=request.getContextPath()%>/app/detail/custom/show.do?method=indexNaviKey&naviId=${item.naviId }&naviName=${item.naviName}&userName=${userName}&appId=${appInfo.appId}">编辑导航</a>
												</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
								<input type="button" value="删除" name="deleteButton" />${deleteMessage
								}
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
	var c = document.getElementsByName("naviId");

	for (var i = 0; i < c.length; i++) {
		c[i].checked = true;
	}
}

function unChooseAll() {
	var c = document.getElementsByName("naviId");

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
        <script type="text/javascript">
$(function(){
	$(".edittd").dblclick(function(){
		var tdhtml = $(this).html();
		var tdid = $(this).attr("id");
		$(this).html("<input type='text' id='input"+tdid+"' value='"+tdhtml+"''>");
		$("#input"+tdid).focus();
		$("#input"+tdid).blur(function(){
			var editval = $(this).val();
			$.post(base+"/app/detail/custom/show.do?method=updateNaviName&naviId="+tdid+"&naviName="+editval);	
			$(this).parent().html(editval);
		});
	});
});
</script>

</html>
