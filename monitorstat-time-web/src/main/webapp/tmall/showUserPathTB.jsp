<%@ page language="java" import="java.util.*" pageEncoding="GBK" isELIgnored="false"%>
<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
	<head>
		<title>Tmall依赖大盘</title>
		<%@ include file="/time/common/base.jsp"%>
		<meta http-equiv="content-type" content="text/html;charset=gbk" />
		<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jsPlumb.css">
		<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/index.css">
		<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
		<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
		<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
		<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
		<script type="text/javascript"   src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>
		<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.jsPlumb-1.3.14-all-min.js"></script>
<%
	//大盘基础的基础坐标
	int baseTop = 0;
	int baseLeft = 0;
	int windowWidth = 15;
	int windowHeight = 9;
%>		
<style>
.window { 
border:1px solid #346789;
	-moz-border-radius:0.5em;
	border-radius:0.5em;
	z-index:20; position:absolute;
	background-color:#eeeeef;
	color:black;
	font-family:helvetica;padding:0.5em;
	font-size:0.9em;
    position: absolute;
    height: <%=windowHeight%>em;
    width: <%=windowWidth%>em;
    z-index: 20;	
}
.window:hover {

box-shadow: 2px 2px 19px #444;
   -o-box-shadow: 2px 2px 19px #444;
   -webkit-box-shadow: 2px 2px 19px #444;
   -moz-box-shadow: 2px 2px 19px #444;
    opacity:0.6;
filter:alpha(opacity=60);

}

.active {
	border:1px dotted green;
}
.hover {
	border:1px dotted red;
}
._jsPlumb_connector { z-index:4; }
._jsPlumb_endpoint, .endpointTargetLabel, .endpointSourceLabel{ z-index:-1;cursor:pointer; }
.hl { border:3px solid red; }
#debug { position:absolute; background-color:black; color:red; z-index:5000 }

.aLabel {
 	background-color:white; 
	padding:0.4em; 
	font:12px sans-serif; 
	color:#444;
	z-index:21;
	border:1px dotted gray;
	opacity:0.8;
	filter:alpha(opacity=80);
}
.window td {
	font-size: 12px
}

#opsname_cdn { top:<%=baseTop%>em;left:<%=baseLeft + windowWidth + 5%>em;}
#opsname_promotion { top:<%=baseTop + windowHeight * 3 + 5*3%>em;left:<%=baseLeft + windowWidth*6 + 5*3%>em;}
#alipay_red { top:<%=baseTop-2%>em;left:<%=baseLeft + windowWidth*6 + 5*3%>em;}
#alipay { top:<%=baseTop + windowHeight*2 + 10%>em;left:<%=baseLeft + windowWidth*6 + 5*3%>em;}

#opsname_shopsystem { top:<%=baseTop + windowHeight + 5%>em;left:<%=baseLeft%>em;}
#opsname_shopcenter { top:<%=baseTop + windowHeight * 2 + 5*2%>em;left:<%=baseLeft%>em;}
#opsname_tmallsearch { top:<%=baseTop + windowHeight * 2 + 5*2%>em;left:<%=baseLeft + windowWidth * 2 + 5*2%>em;}

#opsname_memberplatform { top:<%=baseTop + windowHeight + 5%>em;left:<%=baseLeft + windowWidth * 3 + 10%>em;}
#opsname_pointcenter { top:<%=baseTop + windowHeight + 5%>em;left:<%=baseLeft + windowWidth * 4 + 20%>em;}

#tmall_deal { top:<%=baseTop + windowHeight * 3.5 + 5%>em;left:<%=baseLeft + windowWidth * 3.5 + 15%>em;}
#opsname_malldetail { top:<%=baseTop + windowHeight * 3 + 5*3%>em;left:<%=baseLeft + windowWidth + 5%>em;}
#opsname_malldetailskip { top:<%=baseTop + windowHeight * 4 + 5*4%>em;left:<%=baseLeft + windowWidth * 2 + 5*2%>em;}

#opsname_tf_tm { top:<%=baseTop + windowHeight * 4 + 5*4%>em;left:<%=baseLeft%>em;}
#opsname_tradeplatform { top:<%=baseTop + windowHeight * 4 + 5 * 4%>em;left:<%=baseLeft + windowWidth*6 + 5*3%>em;}

#opsname_tmallpromotion { top:<%=baseTop + windowHeight * 4 + 5 * 6%>em;left:<%=baseLeft + windowWidth + 5%>em;}
#opsname_ump { top:<%=baseTop + windowHeight * 5 + 5 * 6%>em;left:<%=baseLeft + windowWidth * 2 + 5*2%>em;}
#opsname_inventoryplatform  { top:<%=baseTop + windowHeight * 5 + 5 * 6%>em;left:<%=baseLeft + windowWidth * 3.5 + 10%>em;}
#opsname_logisticscenter  { top:<%=baseTop + windowHeight * 5 + 5 * 6%>em;left:<%=baseLeft + windowWidth * 4.5 + 15%>em;}
#opsname_top { top:<%=baseTop + windowHeight * 5 + 5 * 6%>em;left:<%=baseLeft + windowWidth*6 + 5*3%>em;}
#opsname_itemcenter { top:<%=baseTop + windowHeight * 6 + 5 * 6%>em;left:<%=baseLeft + windowWidth + 5%>em;}
#opsname_uicfinal { top:<%=baseTop + windowHeight * 6 + 5 * 7%>em;left:<%=baseLeft + windowWidth * 5 + 10%>em;}
#opsname_login { top:<%=baseTop%>em;left:<%=baseLeft + windowWidth * 3.5 + 15%>em;}

</style>
<%
	Map<String, AppInfoPo> appMap = (Map<String, AppInfoPo>) request.getAttribute("appMap");
	Map<String, Integer[]> capacityMap = (Map<String, Integer[]>) request.getAttribute("capacityMap");

	AppInfoPo tmallCart = null;
	AppInfoPo tmallBuy = null;
	if (appMap != null) {
		tmallCart = appMap.get("tmallcart");
		tmallBuy = appMap.get("tmallbuy");
		appMap.remove("tmallcart");
		appMap.remove("tmallbuy");
		if (tmallCart == null) {
			tmallCart = new AppInfoPo();
			tmallCart.setAppId(1);
			tmallCart.setOpsName("tmallcart");
			tmallCart.setAppType("pv");
		}
		if (tmallBuy == null) {
			tmallBuy = new AppInfoPo();
			tmallBuy.setAppId(1);
			tmallBuy.setOpsName("tmallbuy");
			tmallBuy.setAppType("pv");
		}
	} else {
		appMap = new HashMap<String, AppInfoPo>();
	}
	
	if(capacityMap == null)
		capacityMap = new HashMap<String, Integer[]>();
%>
</head>
	<body data-demo-id="flowchartConnectorsDemo" data-library="jquery">
		<%@ include file="/header.jsp"%>
	<div class="index_body">	
	<div class="container-fluid">
		<div class="row-fluid" style="text-align: center">
			<div class="span12" id="page_nav"></div>
		<script>
			$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {urlPrefix:'<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=',appId: '${appInfo.appId}'});
		</script>
		</div>
		<div>
		<h1 align="center">双十一Tmall主流程依赖大盘</h1>
						<table class="table table-striped table-bordered table-condensed"  width="100%">
			<tr>
				<td>1.QPS=集群QPS/机器数. 容量QPS=CSP压测单台机器QPS的容量值;&nbsp;RT=集群总响应时间/机器数, load=集群的平均load.&nbsp;PV指集群单分钟总pv</td>
			<tr>
			<tr>
				<td>2.失败率=(TDOD阻挡总请求数+SS总限流数)/PV;&nbsp;日志异常=机器日志异常总数.</td>
			</tr>
			<tr>
				<td>3.所有数据均为1分钟内全网数据，对比数据为7天前同时刻数据</td>
			</tr>
			<tr>
				<td>4.点击应用矩形，弹出窗口可以查看应用信息，依赖信息，分组信息</td>
			</tr>
				
		</table>
		</div>
		<div style="position: relative; margin-top: 5px;width: 130em;">
			<div id="demo">
			<%
				List<String> appList = Arrays.asList("promotion", "shopsystem",
						"shopcenter", "tmallsearch","memberplatform","pointcenter", "malldetail",
						"tf_tm", "tradeplatform", "tmallpromotion", "ump",
						"inventoryplatform", "logisticscenter", "itemcenter", "uicfinal","malldetailskip","login");
				for (String appName : appList) {
					AppInfoPo item = appMap.get(appName);
					Integer[] capacity = capacityMap.get(appName);
					if(capacity == null)
						capacity = new Integer[]{0, 0};
					//防null操作在action中做了
			%>
					<div class="component window" id="opsname_<%=item.getOpsName()%>"  onclick="popoverAppDetail('<%=item.getAppId()%>')" >
								<table>
									<tr>
										<td><a href="<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=<%=item.getAppId()%>" target="_blank;"><%=item.getOpsName()%></a>&nbsp;&nbsp;[<font id="<%=item.getOpsName()%>_collectTime">暂无</font>]&nbsp;&nbsp;（<font id="<%=item.getOpsName()%>_hostSize"><%=capacity[0]%></font>台）</td>
									</tr>
									<tr>
										<td>当前QPS:<font color="blue" style="font-weight:bold;" id="<%=item.getOpsName()%>_qps">暂无</font>
										&nbsp;&nbsp; PV:<font color="blue" style="font-weight:bold;" id="<%=item.getOpsName()%>_totalpv">暂无</font></td>
									</tr>
									<tr>
										<td colspan="2">容量QPS:<font color="green" id="<%=item.getOpsName()%>_capacity"><%=capacity[1]%></font>
											&nbsp;&nbsp;失败率:<font id="<%=item.getOpsName()%>_failRate">暂无</font>
										</td>
									</tr>								
									<tr>
										<td >RT（ms）:<font color="blue"  style="font-weight:900;" id="<%=item.getOpsName()%>_rt">暂无</font>
										&nbsp;&nbsp;
										load:<a href="<%=request.getContextPath()%>/app/detail/exception/show.do?method=showIndex&appId=<%=item.getAppId()%>"><font style="color: red;" id="<%=item.getOpsName()%>_load">暂无</font></a>
										</td>
									</tr>							
									<tr>
										<td>日志异常:<a href="<%=request.getContextPath()%>/app/detail/exception/show.do?method=showIndex&appId=<%=item.getAppId()%>"><font style="color: red;" id="<%=item.getOpsName()%>_exceptionCount">暂无</font></a>
										</td>
									</tr>
									<tr>
									</tr>
								</table>
					</div>				
			<%
								}
							%>
				<div class="component window" id="opsname_cdn">
							<table>
								<tr>
									<td><a href="http://110.75.32.52:9999/cdn/index.php" target="_blank;">CDN监控大盘</a></td>
								</tr>
							</table>
				</div>
				<%
				Integer[] capacityTmallCart = capacityMap.get("tmallcart");
				if(capacityTmallCart == null)
					capacityTmallCart = new Integer[]{0, 0};
				Integer[] capacityTmallBuy = capacityMap.get("tmallbuy");
				if(capacityTmallBuy == null)
					capacityTmallBuy = new Integer[]{0, 0};				
				%>
				<div class="component window" id="tmall_deal" style="width: <%=windowWidth%>em; height: <%=windowHeight * 2 + 5%>em;" align="center">
					<p style="font-size:120%"><strong>天猫交易</strong></p>
					<div style="width: 100%">
							<table onclick="popoverAppDetail('<%=tmallCart.getAppId()%>')" >
								<tr>
									<td><a
										href="<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=<%=tmallCart.getAppId()%>"
										target="_blank;"><%=tmallCart.getOpsName()%></a>&nbsp;&nbsp;[<font
										id="<%=tmallCart.getOpsName()%>_collectTime">暂无</font>]&nbsp;&nbsp;（<font
										id="<%=tmallCart.getOpsName()%>_hostSize"><%=capacityTmallCart[0]%></font>台）</td>
								</tr>
								<tr>
									<td>当前QPS:<font color="blue" style="font-weight: bold;"
										id="<%=tmallCart.getOpsName()%>_qps">暂无</font>
									&nbsp;&nbsp; PV:<font color="blue" style="font-weight:bold;" id="<%=tmallCart.getOpsName()%>_totalpv">暂无</font></td>
								</tr>
								<tr>
									<td colspan="2">容量QPS:<font color="green"
										id="<%=tmallCart.getOpsName()%>_capacity"><%=capacityTmallCart[1]%></font> 
									<%
									 	if (tmallCart.getAppType().equals("pv")) {
									 %> &nbsp;&nbsp;失败率:<font id="<%=tmallCart.getOpsName()%>_failRate">暂无</font> 
									 <%
									 	}
									 %>
									</td>
								</tr>
								<tr>
									<td>RT（ms）:<font color="blue" style="font-weight: 900;"
										id="<%=tmallCart.getOpsName()%>_rt">暂无</font>
									&nbsp;&nbsp; load:<a href="<%=request.getContextPath()%>/app/detail/exception/show.do?method=showIndex&appId=<%=tmallCart.getAppId()%>"><font style="color: red;" id="<%=tmallCart.getOpsName()%>_load">2032</font></a>										
										</td>
								</tr>
								<tr>
									<td>日志异常:<a
										href="<%=request.getContextPath()%>/app/detail/exception/show.do?method=showIndex&appId=<%=tmallCart.getAppId()%>"><font
											style="color: red;"
											id="<%=tmallCart.getOpsName()%>_exceptionCount">0</font></a>
									</td>
								</tr>
								<tr>
								</tr>
							</table>
							<hr />
							<table onclick="popoverAppDetail('<%=tmallBuy.getAppId()%>')" >
								<tr>
									<td><a
										href="<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=<%=tmallBuy.getAppId()%>"
										target="_blank;"><%=tmallBuy.getOpsName()%></a>&nbsp;&nbsp;[<font
										id="<%=tmallBuy.getOpsName()%>_collectTime">暂无</font>]&nbsp;&nbsp;（<font
										id="<%=tmallBuy.getOpsName()%>_hostSize"><%=capacityTmallBuy[0]%></font>台）</td>
								</tr>
								<tr>
									<td>当前QPS:<font color="blue" style="font-weight: bold;"
										id="<%=tmallBuy.getOpsName()%>_qps">暂无</font>
									&nbsp;&nbsp; PV:<font color="blue" style="font-weight:bold;" id="<%=tmallBuy.getOpsName()%>_totalpv">暂无</font></td>	
								</tr>
								<tr>
									<td colspan="2">容量QPS:<font color="green"
										id="<%=tmallBuy.getOpsName()%>_capacity"><%=capacityTmallBuy[1]%></font> <%
 										if (tmallBuy.getAppType().equals("pv")) {
									 %> &nbsp;&nbsp;失败率:<font id="<%=tmallBuy.getOpsName()%>_failRate">暂无</font> <%
									 	}
									 %>
									</td>
								</tr>
								<tr>
									<td>RT（ms）:<font color="blue" style="font-weight: 900;"
										id="<%=tmallBuy.getOpsName()%>_rt">暂无</font>
										&nbsp;&nbsp; load:<a href="<%=request.getContextPath()%>/app/detail/exception/show.do?method=showIndex&appId=<%=tmallBuy.getAppId()%>"><font style="color: red;" id="<%=tmallBuy.getOpsName()%>_load">2032</font></a>										
										</td>
								</tr>
								<tr>
									<td>日志异常:<a
										href="<%=request.getContextPath()%>/app/detail/exception/show.do?method=showIndex&appId=<%=tmallBuy.getAppId()%>"><font
											style="color: red;"
											id="<%=tmallBuy.getOpsName()%>_exceptionCount">0</font></a>
									</td>
								</tr>
								<tr>
								</tr>
							</table>
						</div>
			</div>
			<div class="component window" id="alipay_red" style="width: <%=windowWidth* 3 + 5%>em; height: <%=windowHeight*4 + 4%>em;" align="center">
				<div style="width: 100%" id="tradeId">
				<script type="text/javascript">
				$("#tradeId").load("<%=request.getContextPath()%>/index.do?method=tradeTcCreateIndex");
				</script>
				</div>	
			</div>
		</div>
		</div>
			<div class="modal hide fade" id="appDentailMessageDiv" style="width: 960px;left: 400px;">
			  <div class="modal-header">
			    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			     <div id="modelHeader"><h4></h4></div>
			  </div>
			  <div class="modal-body">
				<ul class="nav nav-tabs" id="myTab">
				  <li class="active"><a href="#tableinfo">应用信息</a></li>
				  <li><a href="#dependinfo">依赖信息</a></li>
				  <li><a href="#groupinfo">分组信息</a></li>
				</ul>
				<div class="tab-content">
				  <div class="tab-pane active" id="tableinfo">
				  	<!-- 应用信息 -->
				  </div>				
				  <div class="tab-pane" id="dependinfo">
				  	<!-- 依赖信息 -->
				  </div>
				  <div class="tab-pane" id="groupinfo">
				  	<!-- 分组信息 -->
				  </div>				  
				</div>
			  </div>
			 </div>
			 <input type="hidden" id="currentAppId" value="-1">
		</div>		
		<div id="waitDiv"   class="alert alert-block alert-error fade in"  style="position: absolute;left: 0px;top:100px">正在获取数据....</div>
		</div>
</body>
<script type="text/javascript">
window.jsPlumbDemo = {
		init : function() {
			jsPlumb.importDefaults({
				// default drag options
				DragOptions : { cursor: 'pointer', zIndex:2000 },
				// default to blue at one end and green at the other
				EndpointStyles : [{ fillStyle:'#225588' }, { fillStyle:'#558822' }],
				// blue endpoints 7 px; green endpoints 11.
				Endpoints : [ [ "Dot", {radius:3} ], [ "Dot", { radius:3 } ]],
				// the overlays to decorate each connection with.  note that the label overlay uses a function to generate the label text; in this
				// case it returns the 'labelText' member that we set on each connection in the 'init' method below.
				ConnectionOverlays : [
					[ "Arrow", { location:0.9 } ]
					/*,
					[ "Label", { 
						location:0.1,
						id:"label",
						cssClass:"aLabel"
					}]*/
				],
				ConnectorZIndex:5
			});			

			// this is the paint style for the connecting lines..
			var connectorPaintStyle = {
				lineWidth:5,
				strokeStyle:"gray",
				joinstyle:"round",
				outlineColor:"white",
				outlineWidth:7
			},
			// .. and this is the hover style. 
			connectorHoverStyle = {
				lineWidth:5,
				strokeStyle:"#ec9f2e"
			},
			// the definition of source endpoints (the small blue ones)
			sourceEndpoint = {
				endpoint:"Dot",
				paintStyle:{ fillStyle:"#225588",radius:7 },
				isSource:true,
				connector:[ "Flowchart", { stub:[40, 60], gap:10 } ],								
				connectorStyle:connectorPaintStyle,
				hoverPaintStyle:connectorHoverStyle,
				connectorHoverStyle:connectorHoverStyle,
                dragOptions:{}
				/*,
                overlays:[
                	[ "Label", { 
	                	location:[0.5, 1.5], 
	                	label:"Drag",
	                	cssClass:"endpointSourceLabel" 
	                } ]
                ]*/
			},
			// a source endpoint that sits at BottomCenter
		//	bottomSource = jsPlumb.extend( { anchor:"BottomCenter" }, sourceEndpoint),
			// the definition of target endpoints (will appear when the user drags a connection) 
			targetEndpoint = {
				endpoint:"Dot",					
				paintStyle:{ fillStyle:"#558822",radius:7 },
				hoverPaintStyle:connectorHoverStyle,
				maxConnections:-1,
				dropOptions:{ hoverClass:"hover", activeClass:"active" },
				isTarget:true
				/*,			
                overlays:[
                	[ "Label", { location:[0.5, -0.5], label:"Drop", cssClass:"endpointTargetLabel" } ]
                ]*/
			},			
			init = function(connection) {
				//connection.getOverlay("label").setLabel(connection.sourceId.substring(6) + "-" + connection.targetId.substring(6));
			};			

			var allSourceEndpoints = [], allTargetEndpoints = [];
				_addEndpoints = function(toId, sourceAnchors, targetAnchors) {
					for (var i = 0; i < sourceAnchors.length; i++) {
						var sourceUUID = toId + sourceAnchors[i];
						allSourceEndpoints.push(jsPlumb.addEndpoint(toId, sourceEndpoint, { anchor:sourceAnchors[i], uuid:sourceUUID }));						
					}
					for (var j = 0; j < targetAnchors.length; j++) {
						var targetUUID = toId + targetAnchors[j];
						allTargetEndpoints.push(jsPlumb.addEndpoint(toId, targetEndpoint, { anchor:targetAnchors[j], uuid:targetUUID }));						
					}
				};

			_addEndpoints("opsname_cdn", ["LeftMiddle", "RightMiddle","BottomCenter"], []);			
			_addEndpoints("opsname_shopsystem", ["BottomCenter"], ["TopCenter"]);
			_addEndpoints("opsname_shopcenter", ["BottomCenter"], ["TopCenter"]);
			
			_addEndpoints("opsname_tmallsearch", ["LeftMiddle"], ["TopCenter"]);
			_addEndpoints("opsname_memberplatform", ["RightMiddle"], []);
			_addEndpoints("opsname_pointcenter", ["LeftMiddle"], []);
			
			_addEndpoints("opsname_malldetail", ["RightMiddle"], ["LeftMiddle", "BottomCenter","TopCenter"]);			
			
			_addEndpoints("tmall_deal", ["RightMiddle"], ["LeftMiddle", "BottomCenter","TopCenter"]);
			_addEndpoints("opsname_tf_tm", ["TopCenter"], []);
				
			_addEndpoints("opsname_promotion", ["BottomCenter"], []);
			//_addEndpoints("alipay_red", ["BottomCenter"], ["TopCenter"]);
			
			_addEndpoints("opsname_tradeplatform", ["BottomCenter"], ["TopCenter", "LeftMiddle"]);
			//_addEndpoints("opsname_top", [], ["TopCenter"]);
			
			_addEndpoints("opsname_tmallpromotion", ["TopCenter", "RightMiddle"], ["LeftMiddle", "BottomCenter"]);			
			_addEndpoints("opsname_ump", ["TopCenter"], ["BottomCenter"]);
			_addEndpoints("opsname_inventoryplatform", ["TopCenter"], []);
			_addEndpoints("opsname_logisticscenter", ["TopCenter"], []);
			_addEndpoints("opsname_itemcenter", ["LeftMiddle","RightMiddle"], []);
			_addEndpoints("opsname_uicfinal", [], ["LeftMiddle"]);
			
			// listen for new connections; initialise them the same way we initialise the connections at startup.
			jsPlumb.bind("jsPlumbConnection", function(connInfo, originalEvent) { 
				init(connInfo.connection);
			});			
						
			// make all the window divs draggable						
			//jsPlumb.draggable(jsPlumb.getSelector(".window"), { grid: [20, 20] });
			// THIS DEMO ONLY USES getSelector FOR CONVENIENCE. Use your library's appropriate selector method!
			jsPlumb.draggable(jsPlumb.getSelector(".window"));

			/*cdn*/
			jsPlumb.connect({uuids:["opsname_cdnLeftMiddle", "opsname_shopsystemTopCenter"]});
			jsPlumb.connect({uuids:["opsname_cdnRightMiddle", "opsname_tmallsearchTopCenter"]});
			jsPlumb.connect({uuids:["opsname_cdnBottomCenter", "opsname_malldetailTopCenter"]});

			/*shopsystem*/			
			jsPlumb.connect({uuids:["opsname_shopsystemBottomCenter", "opsname_shopcenterTopCenter"]});
			
			/*shopcenter*/			
			jsPlumb.connect({uuids:["opsname_shopcenterBottomCenter", "opsname_malldetailLeftMiddle"]});
			
			/*malltmallsearch*/
			jsPlumb.connect({uuids:["opsname_tmallsearchLeftMiddle", "opsname_malldetailTopCenter"]});
			
			/*malldetail*/
			jsPlumb.connect({uuids:["opsname_malldetailRightMiddle", "tmall_dealLeftMiddle"]});
			
			/*tf_tm*/
			jsPlumb.connect({uuids:["opsname_tf_tmTopCenter", "opsname_malldetailLeftMiddle"]});
			
			/*tmallpromotion*/
			jsPlumb.connect({uuids:["opsname_tmallpromotionTopCenter", "opsname_malldetailBottomCenter"]});
			
			/*ump*/
			jsPlumb.connect({uuids:["opsname_umpTopCenter", "tmall_dealBottomCenter"]});
			
			/*mp*/
			jsPlumb.connect({uuids:["opsname_memberplatformRightMiddle", "tmall_dealTopCenter"]});
			
			/*pc*/
			jsPlumb.connect({uuids:["opsname_pointcenterLeftMiddle", "tmall_dealTopCenter"]});
			
			/*inventoryplatform*/
			jsPlumb.connect({uuids:["opsname_inventoryplatformTopCenter", "tmall_dealBottomCenter"]});			
			
			/*tradeplatform,不连接top*/
			//jsPlumb.connect({uuids:["opsname_tradeplatformBottomCenter", "opsname_topTopCenter"]});
			
			/*itemcenter*/
			jsPlumb.connect({uuids:["opsname_itemcenterLeftMiddle", "opsname_tmallpromotionLeftMiddle"]});
			jsPlumb.connect({uuids:["opsname_itemcenterRightMiddle", "opsname_uicfinalLeftMiddle"]});
			
			/*promotion*/
			jsPlumb.connect({uuids:["opsname_promotionBottomCenter", "opsname_tradeplatformTopCenter"]});
			
			/*malldeal*/
			jsPlumb.connect({uuids:["tmall_dealRightMiddle", "opsname_tradeplatformLeftMiddle"]});
			
			//
			// listen for clicks on connections, and offer to delete connections on click.
			//
			
			/*
			jsPlumb.bind("click", function(conn, originalEvent) {
				if (confirm("Delete connection from " + conn.sourceId + " to " + conn.targetId + "?"))
					jsPlumb.detach(conn); 
			});	
			*/
			
			jsPlumb.bind("connectionDrag", function(connection) {
				console.log("connection " + connection.id + " is being dragged");
			});		
			
			jsPlumb.bind("connectionDragStop", function(connection) {
				console.log("connection " + connection.id + " was dragged");
			});
		}
	};

	if(jsPlumbDemo.reset) 
		jsPlumbDemo.reset();
	jsPlumb.setRenderMode(jsPlumb.SVG);
	jsPlumbDemo.init();
	
	grap();
	
	function grap(){
		$.ajax({
			url : "<%=request.getContextPath()%>/index.do?method=getAppInfo2012&company=${company}",
			success : function(data) {
				for(var i=0;i<data.length;i++){
					var ie = data[i];
					$("#"+ie["appName"]+"_collectTime").text(ie.ftime);
					$("#"+ie["appName"]+"_totalpv").html(ie.pv);
					$("#"+ie["appName"]+"_qps").html(ie.qps+""+ie.pvRate); //QPS和PV的增长率，按相同算
					$("#"+ie["appName"]+"_rt").text(ie.rt);
					if($("#"+ie["appName"]+"_failRate"))
						$("#"+ie["appName"]+"_failRate").html(ie.failurerate);
					//$("#"+ie["appName"]+"_capacity").text(ie.capcityRate);
					$("#"+ie["appName"]+"_alarm").text(ie.alarms);
					$("#"+ie["appName"]+"_exceptionCount").html(ie.exceptionNum+""+ie.exceptionRate);
					$("#"+ie["appName"]+"_load").html(ie.load);
					//$("#"+ie["appName"]+"_hostSize").text(ie.machines);
				}
				//刷新交易数据
				$("#tradeId").html("");	
				$("#tradeId").load("<%=request.getContextPath()%>/index.do?method=tradeTcCreateIndex");
				window.setTimeout("grap()",20000);	//20s
				$("#waitDiv").hide();
			}
		});
	}
	//弹出
	function popoverAppDetail(appId){
		$("#tableinfo").html("");	//清空第一个选项卡(统计信息部分)
		 $("#tableinfo").load("<%=request.getContextPath()%>/time/popover_app_table.jsp?appId="+appId,  function(){
		 	$('#myTab a[href="#tableinfo"]').tab('show');
			$('#appDentailMessageDiv').modal();
			$('#currentAppId').attr("value", appId);	//弹出时记录当前的AppId
		 }); 
	}
	//点击弹出窗口的Tab的时候，按照appid加载相应的页面
	$(function() {
		$('#myTab a').click(function (e) {
			  e.preventDefault();
			  var nodeValue = $(this)[0].attributes[0].nodeValue;
			  if(nodeValue == "#tableinfo") {
				$(nodeValue).html("");	
				$(nodeValue).load("<%=request.getContextPath()%>/time/popover_app_table.jsp?appId=" + $('#currentAppId').val()); 
			  } else if(nodeValue == "#dependinfo") {
				$(nodeValue).html("");	
				$(nodeValue).load("<%=request.getContextPath()%>/time/popover_app_table.jsp?appId=" + $('#currentAppId').val() + "&showType=dependinfo");				  
			  } else if(nodeValue == "#groupinfo") {
					$(nodeValue).html("");//分组信息
					$(nodeValue).load("<%=request.getContextPath()%>/time/popover_app_table.jsp?appId=" + $('#currentAppId').val() + "&showType=groupinfo");  
			  }
			  $(this).tab('show');
		})
	});
	
</script>
</html>
