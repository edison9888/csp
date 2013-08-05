<%@page import="com.taobao.monitor.common.po.CspTimeAppDependInfo"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.taobao.csp.time.web.po.IndexEntry"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK" isELIgnored="false"%>
	<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>

<html>
	<head>
		<title>实时监控系统</title>
		<meta http-equiv="content-type" content="text/html;charset=gbk" />
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jsPlumb.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/statics/css/jquery.fancybox.css" media="screen" />
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.jsPlumb-all-source.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/graph/raphael-min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/graph/dracula_graffle.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/graph/dracula_graph.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/graph/dracula_algorithms.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.fancybox.js"></script>

	<script type="text/javascript">
		$(document).ready(function() {
			/*
			 *  Simple image gallery. Uses default settings
			 */

			$('.fancybox').fancybox();

			/*
			 *  Different effects
			 */

			// Change title type, overlay opening speed and opacity
			$(".fancybox-effects-a").fancybox({
				helpers: {
					title : {
						type : 'outside'
					},
					overlay : {
						speedIn : 500,
						opacity : 0.95
					}
				}
			});

			// Disable opening and closing animations, change title type
			$(".fancybox-effects-b").fancybox({
				openEffect  : 'none',
				closeEffect	: 'none',

				helpers : {
					title : {
						type : 'over'
					}
				}
			});

			// Set custom style, close if clicked, change title type and overlay color
			$(".fancybox-effects-c").fancybox({
				wrapCSS    : 'fancybox-custom',
				closeClick : true,

				helpers : {
					title : {
						type : 'inside'
					},
					overlay : {
						css : {
							'background-color' : '#eee'
						}
					}
				}
			});

			// Remove padding, set opening and closing animations, close if clicked and disable overlay
			$(".fancybox-effects-d").fancybox({
				padding: 0,

				openEffect : 'elastic',
				openSpeed  : 150,

				closeEffect : 'elastic',
				closeSpeed  : 150,

				closeClick : true,

				helpers : {
					overlay : null
				}
			});

			/*
			 *  Button helper. Disable animations, hide close button, change title type and content
			 */

			$('.fancybox-buttons').fancybox({
				openEffect  : 'none',
				closeEffect : 'none',

				prevEffect : 'none',
				nextEffect : 'none',

				closeBtn  : false,

				helpers : {
					title : {
						type : 'inside'
					},
					buttons	: {}
				},

				afterLoad : function() {
					this.title = 'Image ' + (this.index + 1) + ' of ' + this.group.length + (this.title ? ' - ' + this.title : '');
				}
			});


			/*
			 *  Thumbnail helper. Disable animations, hide close button, arrows and slide to next gallery item if clicked
			 */

			$('.fancybox-thumbs').fancybox({
				prevEffect : 'none',
				nextEffect : 'none',

				closeBtn  : false,
				arrows    : false,
				nextClick : true,

				helpers : {
					thumbs : {
						width  : 50,
						height : 50
					}
				}
			});

			/*
			 *  Media helper. Group items, disable animations, hide arrows, enable media and button helpers.
			*/
			$('.fancybox-media')
				.attr('rel', 'media-gallery')
				.fancybox({
					openEffect : 'none',
					closeEffect : 'none',
					prevEffect : 'none',
					nextEffect : 'none',

					arrows : false,
					helpers : {
						media : {},
						buttons : {}
					}
				});

			/*
			 *  Open manually
			 */

			$("#fancybox-manual-a").click(function() {
				$.fancybox.open('1_b.jpg');
			});

			$("#fancybox-manual-b").click(function() {
				$.fancybox.open({
					href : 'iframe.html',
					type : 'iframe',
					padding : 5
				});
			});

			$("#fancybox-manual-c").click(function() {
				$.fancybox.open([
					{
						href : '1_b.jpg',
						title : 'My title'
					}, {
						href : '2_b.jpg',
						title : '2nd title'
					}, {
						href : '3_b.jpg'
					}
				], {
					helpers : {
						thumbs : {
							width: 75,
							height: 50
						}
					}
				});
			});


		});
	</script>
	<style type="text/css">
		.fancybox-custom .fancybox-skin {
			box-shadow: 0 0 50px #222;
		}
	</style>
<script type="text/javascript">
var flickerArray = [];	//存储要闪烁的应用的名字
</script>
</head>
	
	<body onunload="jsPlumb.unload();">
		<%
		List<CspTimeAppDependInfo> sourceAppList = (List<CspTimeAppDependInfo>)request.getAttribute("sourceAppList");
		
		if(sourceAppList.size() >0){
		
		
		Map<String,IndexEntry> indexMap =(Map<String,IndexEntry>)request.getAttribute("indexMap");
		Set<String> appNames = (Set<String>)request.getAttribute("appNames");
		
		Map<String,Integer> idMap = new HashMap<String,Integer>();
		int h=0;
		for(String app:appNames){
			idMap.put(app, h++);
		}
		
		
		%>
					<table>
					<tr>
						<td align="center"><a href="<%=request.getContextPath()%>/config/dependconfig.do?method=searchAppConfigList&appId=${appinfo.appId}">修改应用依赖关系</a></td>
					</tr>
				</table>
					<div id="graph_1" style="position: relative;	left: 0px;	width: 100%;	height: 100%;	overflow: hidden;background-color: white;background-image: url(<%=request.getContextPath()%>/statics/css/images/dynamicAnchorBg.jpg );">
						<%
						for(String appName:appNames){
							IndexEntry ie = indexMap.get(appName);
							if(ie ==null){
							%>
							<div class=" component" id="opsname_<%=idMap.get(appName)%>"  style="border: 2px solid #346789;  font-size: 0.8em; height: 50px; position: absolute; width: 80px; z-index: 20;">
							<strong><%=appName %></strong>
							</div>
							<%	
							}else{
							  if(ie.getAlarms() > 0 || ie.getExceptionDBNum()>0) {
							    %>
							    <script type="text/javascript">
							    flickerArray.push('opsname_<%=idMap.get(appName)%>');
							    </script>
							    <%
							  }
							  
						%>
						<div class="component " id="opsname_<%=idMap.get(appName)%>" style="border: 2px solid #346789;  font-size: 0.8em; height: 60px; position: absolute; width: 120px; z-index: 20;">
							<%if(ie.getExceptionDBNum()==0) {%>
							<table width="100%">
								<thead>
									<tr >
										<td colspan="2"  width="100%"><a href="<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=<%=ie.getAppId() %>"><%=appName %> </a>(<%=ie.getMachines() %>台)</td>
									</tr>
								</thead>
								<tr>
									<td>qps:<%=ie.getQps() %></td>
									<td >容量:<%=ie.getCapcityRate()%></td>
								</tr>
								<tr>
									<td  style="color: red"><a target="_blank" title="查看异常详情" href="<%=request.getContextPath()%>/app/detail/exception/show.do?method=showIndex&appId=<%=ie.getAppId()%>">异常:<%=ie.getExceptionNum()%></a></td>
									<td style="color: red">
									<a class="fancybox fancybox.iframe" href="<%=request.getContextPath()%>/app/alarm/show.do?method=recentlyAlarmList&appName=<%=ie.getAppName()%>">告警:<%=ie.getAlarms()%></a></td>
								</tr>								
							</table>
							<%}else{ %>
							<table width="100%">
								<thead>
									<tr>
										<td colspan="2"  width="100%"><%=appName %> </td>
									</tr>
								</thead>
								<tr>
									<td  style="color: red"><a class="fancybox fancybox.iframe" target="_blank" title="查看异常详情" href="<%=request.getContextPath()%>/app/alarm/show.do?method=recentlyDBAlarmList&appName=<%=ie.getAppName()%>">DB异常:<%=ie.getExceptionDBNum()%></a></td>
								</tr>								
							</table>
							<%} %>
						</div>
						<!-- window end -->
						<%}
						%>	
							
						<%
						}
						%>
					</div>
					<script type="text/javascript">
appTarget =[];
function putTarget(app,target,config){
	appTarget[appTarget.length] = {"s":app,"t":target,"c":config};
}
<%
List<IndexEntry> indexList = (List<IndexEntry>)request.getAttribute("indexList");
%>
<%
int i=0;
for(CspTimeAppDependInfo entry:sourceAppList){
	
	String sapp = entry.getAppName();
	String tapp = entry.getDepAppName();
	
	i++;
	String appname = entry.getAppName();
	IndexEntry ie = indexMap.get(tapp);
	String rateString = "";
	if(ie != null){
		
		Map<String, Long> referMap = ie.getReferMap();
		
		Long pv = referMap.get(sapp);
		if(pv != null){
			BigDecimal d1 = new BigDecimal(pv);
			BigDecimal d2 = new BigDecimal(ie.getPv());
			BigDecimal d3 = new BigDecimal(100);
			float rate = d1.divide(d2,3,BigDecimal.ROUND_HALF_UP).multiply(d3).floatValue();
			if(rate>=1||rate<=100){
				rateString = "<span >比例:"+rate+"%</span>";
			}
		}
	}else{
	}
	%>
	var labelText<%=i%> = "<%=rateString%>";
	var ff_<%=idMap.get(sapp)%>_<%=idMap.get(tapp)%> ={
			source : "opsname_<%=idMap.get(sapp)%>",
			target : "opsname_<%=idMap.get(tapp)%>",
			connector : "Bezier",
			anchors : ["AutoDefault", "AutoDefault"],
			paintStyle : {lineWidth : 2,	strokeStyle : "gray"	},
			endpointStyle : {   radius:9, fillStyle:"gray"},
			hoverPaintStyle : {strokeStyle : "#ec9f2e"	},
			overlays : [["Label", {cssClass : "l1 component label ",	label : labelText<%=i%>,	location : 0.5	}],
			            [ "Arrow", { location:0.2 }, { foldback:0.7, fillStyle: "gray", width:14 } ]]
		};
	putTarget('opsname_<%=idMap.get(sapp)%>','opsname_<%=idMap.get(tapp)%>',ff_<%=idMap.get(sapp)%>_<%=idMap.get(tapp)%>);
	<%
}
%>


function drawChat() {
	
    var width = $('#graph_1').width();
    var height = $('#graph_1').height();
    var g = new Graph();
    g.edgeFactory.template.style.directed = true;
	for(var i=0;i<appTarget.length;i++) {
		g.addEdge(appTarget[i].s, appTarget[i].t,appTarget[i].c);
	}

    var layouter = new Graph.Layout.Ordered(g, topological_sort(g));
    
    var renderer = new Graph.Renderer.Raphael("graph_1", g, width, height, jsPlumb);	
}


$(document).ready(function() {
	jsPlumb.Defaults.Container = $("#graph_1");
	jsPlumb.setMouseEventsEnabled(true);
	
	jsPlumb.bind("ready", function() {
		document.onselectstart = function() {
			return false;
		};
	});
	jsPlumb.draggable(jsPlumb.getSelector(".component"));
	drawChat();
	
	if(flickerArray.length > 0) {
		var timer=null;
		var i=0;
		timer=setInterval(function(){
			for(icount=0;icount<flickerArray.length;icount++) {
				var oDiv=$('#' + flickerArray[icount]);
				if(i%2)
				{
					oDiv.css("background","#FF0000");
				}
				else
				{
					oDiv.css("background","");
				}
			}
			i++;
			},600);
	}
})





</script>
<%}else{
%>
<table style="left: 0px;	width: 80%;	height: 400px;	overflow: hidden;background-color: white;background-image: url(<%=request.getContextPath()%>/statics/css/images/dynamicAnchorBg.jpg );">
	<tr>
		<td align="center"><a style=" font-size:36px" href="<%=request.getContextPath()%>/config/dependconfig.do?method=searchAppConfigList&appId=${appinfo.appId}">您还没有设置应用依赖关系，点击编辑</a></td>
	</tr>
</table>
<%	
} 
%>
	</body>

</html>
