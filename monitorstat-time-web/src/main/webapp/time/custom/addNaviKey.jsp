<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo" %> 
<%@page import="com.taobao.monitor.common.po.AppInfoPo" %> 
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
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
<link type="text/css" rel="stylesheet" href="/time/statics/css/jquery-ui.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/time/custom/choose.js"></script>
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
			<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/time_commons.js"></script>
<select id="companySelect"></select>
<select id="groupSelect"></select>
<select id="appSelect"></select>


&nbsp;&nbsp;&nbsp;
<input class="btn btn-success" type="button"
	onclick="javscript:location.href='${param.urlPrefix}'+getSelectAppId()"
	value="查看应用">

<input type="text" id="appname_text"
	style="width: 150px; margin-left: 100px;" />
<input class="btn btn-success" type="button"
	onclick="javscript:location.href='${param.urlPrefix}'+getInputAppId()"
	value="应用直达">

<script>

var nw = new NavigateWidget2({appId:'${param.appId}'},"<%=request.getContextPath()%>");
$("#keyName").autocomplete(
		{
			source: base+"/app/detail/custom/show.do?method=getKeyName&appId="+getSelectAppId(),
			minLength: 3,
			select: function( event, ui ) {
			}
		});
</script>
		</div>
		<div class="row-fluid">
			<div class="span2">
				<%@include file="/leftmenu.jsp"%>
			</div>
			<div class="span12">
				<a href="<%=request.getContextPath()%>/index.jsp">实时首页</a> -> 用户导航操作
				<hr>
				<div class="row-fluid">
					<div class="span12">
						<div class="row-fluid">
							<form id="form2" method="post"
								action="<%=request.getContextPath()%>/app/detail/custom/show.do?method=addNaviKey&naviId=${param.naviId}&naviName=${param.naviName}">
								<table>
								<tr><td>关心的Key:</td><td><input name = "keyName" id="keyName"><input type="button" value="下一步" id="next" name="next" /></td></tr>
								<tr><td>查询方式:</td><td><select  id="actionUrl" name="actionUrl"></select></td></tr>
								<tr><td>展现模式:</td><td><select id="viewMod" name="viewMod"></select></td></tr>
								<tr><td>主属性:</td><td><select id="property" name="property"></select></td></tr>
								<tr><td><input type="hidden" name="appId" id="appId"/></td><td><input type="button" value="添加" name="addButton" /> <input type="button" value="预览" id="previewButton" /></td></tr>
								</table>
								<h5 style="color:#F00;" id="prompt"></h5>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>



<script type="text/javascript">
	
	
	$("#keyName").focus(function(){
		var select = $("#viewMod");
		$('option',select).remove();
		select = $("#property");
		$('option',select).remove();
		select = $("#actionUrl");
		$('option',select).remove();
	});
	$(function() {
	
		
	});
	$(function() {
		var addButtons = document.getElementsByName("addButton");
		$(addButtons).click(function() {
			addChoosedItem();
		});
		$("#previewButton").click(function(){
			previewButton();
		});
		$("#next").click(function(){
			generateActionUrlOptions();
			generatePropertyOptions();
			$("#actionUrl").change(function(){
				generateViewModOptions();
			});
		});
	});
	function addChoosedItem() {
		var appId = getSelectAppId();
		$("#appId").val(appId);
		if($("#actionUrl option:selected").val()==null){$("#prompt").html("信息不完整");return;}
		if($("#viewMod option:selected").val()==null){$("#prompt").html("信息不完整");return;}
		if($("#property option:selected").val()==null){$("#prompt").html("信息不完整");return;}
		var a = confirm("确定要添加吗？");
		if (!a)
			return;
		$("#form2").submit();
	}
	function previewButton() {
		var appId = $("#appSelect").val();
		var appName = $("#appSelect").text();
		$("#appId").val(appId);
		if($("#actionUrl option:selected").val()==null){$("#prompt").html("信息不完整");return;}
		if($("#viewMod option:selected").val()==null){$("#prompt").html("信息不完整");return;}
		if($("#property option:selected").val()==null){$("#prompt").html("信息不完整");return;}
		var keyName = $("#keyName")[0].value;
		var actionUrl = $("#actionUrl").val();
		var viewMod = $("#viewMod").val();
		var property = $("#property").val();
		var iframe = actionUrl+"&appName="+appName+"&keyName="+keyName+"&viewMod="+viewMod+"&property="+property;
		window.open(base+iframe,"预览",400,200);
	}
	function generateViewModOptions(){
		var newOptions;
		var param = $("#actionUrl option:selected").val();
		$.ajax({
			url : base+"/app/detail/custom/show.do?method=getViewMod&actionUrl="+param,
			async : false,
			success :  function(data){
				newOptions = data;	
			}
		});
		var select = $("#viewMod");
		if(select.prop){
			var options = select.prop('options');
		}else{
			var options = select.attr('options');
		}
		$('option',select).remove();
		$.each(newOptions,function(val,text){
			options[options.length] = new Option(text,val);	
		});
	}
	
	function generatePropertyOptions(){
		var newOptions;
		if($("#keyName")[0].value=="")return;
		$.ajax({
			url : base+"/app/detail/custom/show.do?method=getProperty&keyName="+$("#keyName")[0].value,
			async : false,
			success :  function(data){
				newOptions = data;	
			}
		});
		var select = $("#property");
		if(select.prop){
			var options = select.prop('options');
		}else{
			var options = select.attr('options');
		}
		$('option',select).remove();
		$.each(newOptions,function(val,text){
			options[options.length] = new Option(text,val);	
		});
	}
	
	function generateActionUrlOptions(){
		var newOptions;
		if($("#keyName")[0].value=="")return;
		$.ajax({
			url : base+"/app/detail/custom/show.do?method=getActionUrl&keyName="+$("#keyName")[0].value,
			async : false,
			success :  function(data){
				newOptions = data;	
			}
		});
		var select = $("#actionUrl");
		if(select.prop){
			var options = select.prop('options');
		}else{
			var options = select.attr('options');
		}
		$('option',select).remove();
		$.each(newOptions,function(val,text){
			options[options.length] = new Option(text,val);	
		});
		generateViewModOptions();
	}
	
</script>
</html>
