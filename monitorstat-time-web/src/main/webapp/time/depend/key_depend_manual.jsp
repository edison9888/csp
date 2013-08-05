<%@page import="com.taobao.monitor.common.po.CspTimeKeyDependInfo"%>
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
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/statics/css/jquery.fancybox.css" media="screen" />
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
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
	Map<String,Integer> alarmCountMap = (Map<String,Integer>)request.getAttribute("alarmCountMap");
	List<CspTimeKeyDependInfo> keyList = (List<CspTimeKeyDependInfo>)request.getAttribute("keyList");
	
	Map<String,Integer> indexMap = new HashMap<String,Integer>();
	Map<String,CspTimeKeyDependInfo> keydependMap = new HashMap<String,CspTimeKeyDependInfo>();
	
	
	int i=0;
	for(CspTimeKeyDependInfo key:keyList){
		String k1 = key.getAppName()+"_"+key.getKeyName();
		String k2 =  key.getDependAppName()+"_"+key.getDependKeyName();
		
		keydependMap.put(k1, key);
		
		if(!keydependMap.containsKey(k2)) {
		  CspTimeKeyDependInfo po = new CspTimeKeyDependInfo();
		  po.setAppName(key.getDependAppName());
		  po.setKeyName(key.getDependAppName());
		  keydependMap.put(k2, key);
		}
		
		Integer index1 = indexMap.get(k1);
		if(index1 == null){
			indexMap.put(k1, i++);
		}
		Integer index2 = indexMap.get(k2);
		if(index2 == null){
			indexMap.put(k2, i++);
		}
	}
	%>
		<div id="graph_1" style="position: relative;	left: 0px;	width: 100%;	height: 100%;	overflow: hidden;background-color: white;background-image: url(<%=request.getContextPath()%>/statics/css/images/dynamicAnchorBg.jpg );">
			<%
			for(Map.Entry<String,Integer> enrty: indexMap.entrySet()){
				Integer alarmNum = alarmCountMap.get(enrty.getKey() );
				CspTimeKeyDependInfo keyPo = keydependMap.get(enrty.getKey());	//展示key所属应用名称
				if(alarmNum != null&&alarmNum >0){
					%>
					<script type="text/javascript">
					flickerArray[flickerArray.length] = 'opsname_<%=enrty.getValue() %>';
					</script>
				  	<div class="component " id="opsname_<%=enrty.getValue() %>" style="border: 2px solid #346789;  font-size: 0.8em; height: auto; position: absolute; width: 200px; z-index: 20;;word-break:break-all">
						<table width="100%">
								<thead>
									<tr >
										<td colspan="2"  width="100%">
										<%=keyPo.getKeyName()%>
										&nbsp;(
										<a href="<%=request.getContextPath()%>/app/alarm/show.do?method=recentlyAlarmList&appName=<%=keyPo.getAppName()%>">
										<%=keyPo.getAppName()%></a>)
										</td>
									</tr>
								</thead>
								<tr>
									<td style="color: red" colspan="2">告警:<%=alarmNum%></td>
								</tr>
							</table>
							<a class="fancybox fancybox.iframe" href="<%=request.getContextPath()%>/app/alarm/show.do?method=recentlyKeyAlarmList&appName=<%=keyPo.getAppName()%>&keyName=<%=keyPo.getKeyName()%>">查看告警详情</a>
					</div>					
					<%
				} else {
				  %>
					<div class="component " id="opsname_<%=enrty.getValue() %>" style="border: 2px solid #346789;  font-size: 0.8em; height: auto; position: absolute; width: 200px; z-index: 20;;word-break:break-all">
						<strong><%=enrty.getKey()%>&nbsp;(<%=keyPo.getKeyName()%>)</strong>
					</div>
				  <%
				}
			} %>
		</div>
		
		<script type="text/javascript">
		var arraySource = [];	//存储起点
		var arrayTarget = [];	//存储重点Id
		
		<%
		for(CspTimeKeyDependInfo key:keyList){
			String k1 = key.getAppName()+"_"+key.getKeyName();
			String k2 =  key.getDependAppName()+"_"+key.getDependKeyName();
			Integer index1 = indexMap.get(k1);
			Integer index2 = indexMap.get(k2);
			%>
			putEdage("opsname_<%=index1 %>","opsname_<%=index2 %>");
			<%
		}
		%>
		function putEdage(sourceId, targetId) {
			arraySource.push(sourceId);
			arrayTarget.push(targetId);
		}
		function drawChat() {
		    var width = $('#graph_1').width();
		    var height = $('#graph_1').height();
		    var g = new Graph();
		    g.edgeFactory.template.style.directed = true;

			for(i=0; i<arraySource.length; i++) {
				g.addEdge(arraySource[i], arrayTarget[i]);
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
	</body>
</html>
