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
				<h1>${appInfo.appName }导航信息配置</h1>
				<section>
				<form action="<%=request.getContextPath() %>/app/config.do">
					<input type="hidden" value="${appInfo.appId}" name="appId">
					<input type="hidden" value="updateAppNavi" name="method">
					<c:if test="${leftmenuPo.performence==true }">
					<label class="checkbox"><input checked="checked" type="checkbox" value="performence" name="box">性能 </label> 
					</c:if>
					<c:if test="${leftmenuPo.performence==false }">
					<label class="checkbox"><input  type="checkbox" value="performence" name="box"> 性能</label> 
					</c:if>
					<c:if test="${leftmenuPo.pv==true }">
					<label class="checkbox"><input checked="checked" type="checkbox" value="pv" name="box"> PV</label> 
					</c:if>
					<c:if test="${leftmenuPo.pv==false }">
					<label class="checkbox"><input  type="checkbox" value="pv" name="box"> pv</label> 
					</c:if>
					<c:if test="${leftmenuPo.hsf==true }">
					<label class="checkbox"><input checked="checked" type="checkbox" value="hsf" name="box"> HSF</label> 
					</c:if>
					<c:if test="${leftmenuPo.hsf==false }">
					<label class="checkbox"><input  type="checkbox" value="hsf" name="box"> HSF</label> 
					</c:if>
					<c:if test="${leftmenuPo.hsfProvider==true }">
					<label class="checkbox"><input checked="checked" type="checkbox" value="hsfProvider" name="box"> hsf provider</label> 
					</c:if>
					<c:if test="${leftmenuPo.hsfProvider==false }">
					<label class="checkbox"><input  type="checkbox" value="hsfProvider" name="box"> hsf provider</label> 
					</c:if>
					<c:if test="${leftmenuPo.hsfConsumer==true }">
					<label class="checkbox"><input checked="checked" type="checkbox" value="hsfConsumer" name="box"> hsf consumer</label> 
					</c:if>
					<c:if test="${leftmenuPo.hsfConsumer==false }">
					<label class="checkbox"><input  type="checkbox" value="hsfConsumer" name="box"> hsf consumer</label> 
					</c:if>
					<c:if test="${leftmenuPo.hsfRefer==true }">
					<label class="checkbox"><input checked="checked" type="checkbox" value="hsfRefer" name="box"> hsf refer</label> 
					</c:if>
					<c:if test="${leftmenuPo.hsfRefer==false }">
					<label class="checkbox"><input  type="checkbox" value="hsfRefer" name="box"> hsf refer</label> 
					</c:if>
					<c:if test="${leftmenuPo.tairConsumer==true }">
					<label class="checkbox"><input checked="checked" type="checkbox" value="tairConsumer" name="box"> tair consumer</label> 
					</c:if>
					<c:if test="${leftmenuPo.tairConsumer==false }">
					<label class="checkbox"><input  type="checkbox" value="tairConsumer" name="box"> tair consumer</label> 
					</c:if>
					<c:if test="${leftmenuPo.tddl ==true }">
					<label class="checkbox"><input checked="checked" type="checkbox" value="tddl" name="box"> tddl</label> 
					</c:if>
					<c:if test="${leftmenuPo.tddl==false }">
					<label class="checkbox"><input  type="checkbox" value="tddl" name="box"> tddl</label> 
					</c:if>
					<c:if test="${leftmenuPo.notify==true }">
					<label class="checkbox"><input checked="checked" type="checkbox" value="notify" name="box"> notify</label> 
					</c:if>
					<c:if test="${leftmenuPo.notify==false }">
					<label class="checkbox"><input  type="checkbox" value="notify" name="box"> notify</label> 
					</c:if>
					<c:if test="${leftmenuPo.notifyConsumer==true }">
					<label class="checkbox"><input checked="checked" type="checkbox" value="notifyConsumer" name="box"> notify consumer</label> 
					</c:if>
					<c:if test="${leftmenuPo.notifyConsumer==false }">
					<label class="checkbox"><input  type="checkbox" value="notifyConsumer" name="box"> notify consumer</label> 
					</c:if>
					<c:if test="${leftmenuPo.search==true }">
					<label class="checkbox"><input checked="checked" type="checkbox" value="search" name="box"> search</label> 
					</c:if>
					<c:if test="${leftmenuPo.search==false }">
					<label class="checkbox"><input  type="checkbox" value="search" name="box"> search</label> 
					</c:if>
					<c:if test="${leftmenuPo.exception==true }">
					<label class="checkbox"><input checked="checked" type="checkbox" value="exception" name="box"> exception</label> 
					</c:if>
					<c:if test="${leftmenuPo.exception==false }">
					<label class="checkbox"><input  type="checkbox" value="exception" name="box"> exception</label> 
					</c:if>
					<c:if test="${leftmenuPo.mbean==true }">
					<label class="checkbox"><input checked="checked" type="checkbox" value="mbean" name="box"> mbean</label> 
					</c:if>
					<c:if test="${leftmenuPo.mbean==false }">
					<label class="checkbox"><input  type="checkbox" value="mbean" name="box">mbean</label> 
					</c:if>
					<c:if test="${leftmenuPo.uicfinal==true }">
					<label class="checkbox"><input checked="checked" type="checkbox" value="uicfinal" name="box"> uicfinal</label> 
					</c:if>
					<c:if test="${leftmenuPo.uicfinal==false }">
					<label class="checkbox"><input  type="checkbox" value="uicfinal" name="box"> uicfinal</label> 
					</c:if>
					<c:if test="${leftmenuPo.tbsession==true }">
					<label class="checkbox"><input checked="checked" type="checkbox" value="tbsession" name="box"> tbsession</label> 
					</c:if>
					<c:if test="${leftmenuPo.tbsession==false }">
					<label class="checkbox"><input  type="checkbox" value="tbsession" name="box"> tbsession</label> 
					</c:if>
					<c:if test="${leftmenuPo.tairProvider==false }">
					<label class="checkbox"><input  type="checkbox" value="tairProvider" name="box"> tairProvider</label> 
					</c:if>
					<c:if test="${leftmenuPo.tairProvider==true }">
					<label class="checkbox"><input  type="checkbox" value="tairProvider" name="box"> tairProvider</label>
					</c:if>
					<c:if test="${leftmenuPo.b2bOcean==true }">
						<label class="checkbox"><input checked="checked" type="checkbox" value="b2bOcean" name="box">ocean.gateway.normal</label> 
					</c:if>
					<c:if test="${leftmenuPo.b2bOcean==false }">
						<label class="checkbox"><input  type="checkbox" value="b2bOcean" name="box">ocean.gateway.normal</label> 
					</c:if>	
					
					<c:if test="${leftmenuPo.chongzhi==true }">
						<label class="checkbox"><input checked="checked" type="checkbox" value="chongzhi" name="box">chongzhideliver</label> 
					</c:if>
					<c:if test="${leftmenuPo.chongzhi==false }">
						<label class="checkbox"><input  type="checkbox" value="chongzhi" name="box">chongzhideliver</label> 
					</c:if>										
					<input type="submit" value="submit">
					</form>
				</section>
				</div>
			</div>
		</div>

	</body>



	<script type="text/javascript">
	var nw = new NavigateWidget({appId:${appInfo.appId}});   
    </script>

</html>
