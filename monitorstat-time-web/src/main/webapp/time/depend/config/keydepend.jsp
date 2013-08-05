<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@ page language="java" import="com.taobao.csp.time.web.po.TopoPo"%>
<%@ page language="java" import="com.taobao.csp.time.web.po.KeyNode"%>
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
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/graph/raphael-min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/graph/dracula_graffle.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/graph/dracula_graph.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/graph/dracula_algorithms.js"></script>
<%
	List<TopoPo> topoList = (ArrayList<TopoPo>)request.getAttribute("topoList");
%>

<style>
/*重写bootstrap*/
.row>[class *="span"] {
	margin-left: 0px;
}

.canvas_demo {
	background-color: white;
	background-image: url(<%=request.getContextPath()%>/statics/css/images/dynamicAnchorBg.jpg );
	position: relative;
	width: 100%;
	margin: 10px 10px;
	padding: 10px 10px;
	height: 800px;
	overflow: hidden;
}

/*from 1.4 begin*/
 table .blue {
  color: #049cdb;
  border-bottom-color: #049cdb;
}
table .headerSortUp.blue, table .headerSortDown.blue {
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
<script type="text/javascript">
var cavasUnitArray = [];	//按照单元存储画布的内容

function drawKeyChat() {

	for(iIndex=0; iIndex<cavasUnitArray.length; iIndex++) {
		var plumbObj = cavasUnitArray[iIndex].plumbObj;
		plumbObj.Defaults.Container = $("#" + cavasUnitArray[iIndex].cavasId);
		plumbObj.setMouseEventsEnabled(true);
		
		plumbObj.bind("ready", function() {
			document.onselectstart = function() {
				return false;
			};
		});
		
		plumbObj.draggable(plumbObj.getSelector(".itemWindow"));
		
	    var width = $('#' + cavasUnitArray[iIndex].cavasId).width();
	    var height = $('#' + cavasUnitArray[iIndex].cavasId).height();
	    var g = new Graph();
	    g.edgeFactory.template.style.directed = true;

		for(jIndex=0; jIndex<cavasUnitArray[iIndex].nodeSource.length; jIndex++) {
			g.addEdge(cavasUnitArray[iIndex].nodeTarget[jIndex], cavasUnitArray[iIndex].nodeSource[jIndex]);
		}
		
	    var layouter = new Graph.Layout.Ordered(g, topological_sort(g));
	    var renderer = new Graph.Renderer.Raphael(cavasUnitArray[iIndex].cavasId, g, width, height, plumbObj);	
	}
}


</script>
</head>
<body onunload="jsPlumb.unload();">
	<div class="index_body">
			<form action="<%=request.getContextPath()%>/findKeyDependency.do"
				style="margin-top: 50px; margin-bottom: 10px;" class="navbar-form">
				<input class="input-xxlarge" type="text" placeholder="keyname"
					id="keyName" name="keyName"
					value="com.taobao.item.service.ItemQueryService:1.0.0-L0_queryItemForDetail">
				<input class="btn" type="submit" value="查询"></input>
			</form>
			<%
			int count = 0;
			if (topoList != null) {
				for (TopoPo node : topoList) {
					List<KeyNode> keyList = node.getKeyList();
					System.out.println(count++ + ";" + node.getId() + ";" + node.getUrl() + ";" + keyList.size());
			%>
				<script type="text/javascript">
				
				var arrayItem = {};	//每一个画布一个对象
				
				var nodeArray = [];
				arrayItem.plumbObj = jsPlumb.getInstance();
				arrayItem.nodeArray = jsPlumb.nodeArray;
				arrayItem.cavasId = 'canvas_<%=node.getId()%>';
				
				//存储该画布内的所有信息
				var nodeSource = [];	//存储起点
				var nodeTarget = [];	//存储终点
				arrayItem.nodeSource = nodeSource; 
				arrayItem.nodeTarget = nodeTarget; 
				cavasUnitArray.push(arrayItem);	
				
				</script>
				<div class="row">
				<div id="canvas_<%=node.getId()%>" class="canvas_demo" style="width:80%; height:600px;left: 20px; top: 50px">
			<%
				for(KeyNode keyNode: keyList) {
					
					
			%>
					<div class="component itemWindow" id="depWin_<%=keyNode.getId()%>_<%=count%>">	
				<%
						if (keyNode.getParentIds() != null && keyNode.getParentIds().size() != 0) {
							for(String id: keyNode.getParentIds()) {
						%>
								<script type="text/javascript">
								nodeSource.push('depWin_<%=keyNode.getId()%>_<%=count%>');
								nodeTarget.push('depWin_<%=id%>_<%=count%>');
								</script>
						<%							
							}
						}	// end if 
				%>	
				<%=keyNode.getKeyName()%>			
					</div>	
					<%
					}	
				%>
				</div>
				<%
				//break;
				}//end of for
			}
			%>	
			</div><!-- end of div row -->
	</div>
</body>
				<script type="text/javascript">
					$(function() {
						if(cavasUnitArray.length > 0) {
							for(i = 0; i < cavasUnitArray.length; i++) {
								cavasUnitArray[i].plumbObj.reset();
							}							
						}
						drawKeyChat();
					});
				</script>
</html>
