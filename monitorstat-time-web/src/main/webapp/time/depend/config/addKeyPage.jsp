<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.util.List"%>
<!doctype html>
<html>
	<head>
		<title>Key关系配置</title>
		<%@ include file="/time/common/base.jsp"%>
		<meta http-equiv="content-type" content="text/html;charset=gbk" />		
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
		<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery.autocomplete.css">
					
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>		
		
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/jquery/jquery.jsPlumb-all-min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
		<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.autocomplete.min.js"></script>			
			
<style type="text/css">
body {
	padding-top: 60px;
	padding-bottom: 40px;
}

.sidebar-nav {
	padding: 9px 0;
}
.canvas{
	background-color: white;
	position: relative;
	width: 100%;
	margin: 10px 10px;
	padding: 10px 10px;
	height: 800px;
	overflow: hidden;
	border: 1px solid #DDDDDD;
    border-radius: 4px 4px 4px 4px;
    box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075);
    display: block;
    line-height: 1;
}
</style>
<script type="text/javascript">
var keyNameArray = [];	//存放key名称的数组


$().ready(function() {
	function log(event, data, formatted) {
		$("<li>").html( !data ? "No match!" : "Selected: " + formatted).appendTo("#result");
	}
	
	function formatItem(row) {
		return row[0] + " (<strong>id: " + row[1] + "</strong>)";
	}
	function formatResult(row) {
		return row[0].replace(/(<.+?>)/gi, '');
	}
	
	$("#sourceKeyName").focus().autocomplete(keyNameArray);
	$("#sourceKeyName").setOptions({
		max: 200000
	});	//最多显示的个数
    $("#sourceKeyName").setOptions({
		scrollHeight: 400
	});
	
	$(":text, textarea").result(log).next().click(function() {
		$(this).prev().search();
	});
	$("#scrollChange").click(changeScrollHeight);
	$("#clear").click(function() {
		$(":input").unautocomplete();
	});
});

function changeOptions(){
	var max = parseInt(window.prompt('Please type number of items to display:', jQuery.Autocompleter.defaults.max));
	if (max > 0) {
		$("#sourceKeyName").setOptions({
			max: max
		});
	}
}

function changeScrollHeight() {
    var h = parseInt(window.prompt('Please type new scroll height (number in pixels):', jQuery.Autocompleter.defaults.scrollHeight));
    if(h > 0) {
        $("#sourceKeyName").setOptions({
			scrollHeight: h
		});
    }
}

function changeToMonths(){
	$("#sourceKeyName")
		// clear existing data
		.val("")
		// change the local data to months
		.setOptions({data: months})
		// get the label tag
		.prev()
		// update the label tag
		.text("Month (local):");
}
</script>
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
					<a href="<%=request.getContextPath()%>/index.jsp">实时首页</a> ->${appInfo.appName}的Key关系配置 &nbsp;
					<hr>
					<div class="row-fluid">
						<div class="span12">
						<!-- 查询表单和保存表单分开 -->
							<div class="row-fluid">
								<form id="form2" method="post"
									action="<%=request.getContextPath()%>/config/dependconfig.do">
									<input type="hidden" name="method" value="saveSingleSourceKey"/>
									<input type="hidden" name="sourceAppName" value="${sourceAppName}"/>
									<input type="hidden" name="appId" value="${appInfo.appId}"/>
									<table
										class="table table-striped table-bordered table-condensed"
										width="100%">
										<tbody id="exctbody">
												<tr>
													<td style="text-align: center;" >
														应用名称：
													</td>
													<td style="text-align: center;">
														${sourceAppName}
													</td>
												</tr>
												<tr>
													<td style="text-align: center;" >
														key名称：
													</td>
													<td style="text-align: center;">
													<c:forEach items="${keyList}" var="item">
													<script type="text/javascript">
														keyNameArray.push('${item}');
													</script>
													</c:forEach>
													<input type="text" id="sourceKeyName" name="sourceKeyName" style="width: 900px;"/>
													</td>
												</tr>
												<tr>
													<td></td>
													<td colspan="2"><input type="submit" value="保存">
													<input type="button" value="改变提示数量" onclick="changeOptions();" style="display: none;">
													<input type="button" value="改变下拉框高度" id="scrollChange"></td>
												</tr>
										</tbody>
									</table>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>

</html>
