<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@ page language="java"
	import=" com.taobao.csp.time.web.po.KeyTreeNode"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
<head>
<title>实时监控系统</title>
<%@ include file="/time/common/base.jsp"%>
<meta http-equiv="content-type" content="text/html;charset=gbk" />
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jsPlumb.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/index.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.jsPlumb-all-min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/graph/raphael-min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/graph/dracula_graffle.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/graph/dracula_graph.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/graph/dracula_algorithms.js"></script>

<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/depend_relation.js"></script>
<style>
/*重写bootstrap*/
.row>[class *="span"] {
	margin-left: 0px;
}

#graph_1 {
	background-color: white;
	background-image: url(<%=       request.getContextPath ()%>/statics/css/images/dynamicAnchorBg.jpg
		);
	position: relative;
	left: 0px;
	width: 100%;
	margin: 10px 10px;
	padding: 10px 10px;
	height: 800px;
	overflow: hidden;
}

/*
官方demo
body{
	padding-top:60px;
	padding-bottom:40px;
}
*/
/*from 1.4 begin*/
table .blue {
	color: #049cdb;
	border-bottom-color: #049cdb;
}

table .headerSortUp.blue,table .headerSortDown.blue {
	background-color: #ade6fe;
}
/*from 1.4 end*/
.index_body {
	margin-left: 50px;
	margin-right: 50px;
}

.window td {
	padding: 0px;
	fixed
}

.window td {
	font-size: 12px
}

#table1 th {
	text-align: center;
}
</style>
<%
	//依赖关系列表
	List<KeyTreeNode> depKeylist = (ArrayList<KeyTreeNode>)request.getAttribute("depKeylist");
	Integer treeMaxWidth = (Integer)request.getAttribute("treeMaxWidth");
%>
</head>
<body onunload="jsPlumb.unload();">
	<div class="navbar navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container">


				<a class="brand" href="#">实时监控系统</a>
				<div class="nav-collapse">
					<ul class="nav">
						<li class="active"><a href="#">日报统计</a></li>
						<li><a href="#about">实时监控</a></li>
						<li><a href="#contact">系统依赖</a></li>
						<li><a href="#contact">容量规划</a></li>
					</ul>
				</div>
				<form action="" class="pull-right  navbar-form">
					<input class="input-small" type="text" placeholder="Username">
					<input class="input-small" type="password" placeholder="Password">
					<button class="btn" type="submit">Sign in</button>
				</form>
			</div>
		</div>
	</div>

	<div class="index_body">
		<div class="row" style="text-align: center">
			<form action="<%=request.getContextPath()%>/findDependency.do"
				style="margin-top: 50px; margin-bottom: 10px;" class="navbar-form">
				<input class="input-xxlarge" type="text" placeholder="keyname"
					id="keyName" name="keyName"
					value="com.taobao.shopservice.core.client.ShopReadService:1.0.0|queryShop">
				<input class="btn" type="submit" value="查询"></input>
			</form>
		</div>
		<div class="content" style="height: auto;">
			<div class="row">
				<div id="graph_1">
					<%
					if(depKeylist != null) {
						for(KeyTreeNode node: depKeylist) {
							%>
					<div class="component keywindow" id="depWin_<%=node.getId()%>">
						<%=node.getKeyName()%></div>
					<%
									if(node.getId()!=null && node.getParentId() != null) {
										%> 
										<script type="text/javascript">
											putEdage('<%=node.getId()%>','<%=node.getParentId()%>');
										</script> 
										<%									
									}
						}							
					}
					%>
				</div>
				<div id="graph_div"></div>
				<script type="text/javascript">
				$(function() {
					jsPlumb.reset();
					drawChat();
				});
				</script>
			</div>
		</div>
	</div>
</body>

</html>
