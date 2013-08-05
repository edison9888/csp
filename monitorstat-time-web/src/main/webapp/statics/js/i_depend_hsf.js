/*
 * this is the JS for the main jsPlumb demo. it is shared between the YUI,
 * jQuery and MooTools demo pages.
 */
page = {};


window.jsPlumbDemo = new JsPlumbDemo();

$(function() {
	jsPlumbReady();
	new HSFWidget();

});





/** 鼠标移动到连接上，显示每个url的信息 */
function HSFWidget() {
	// 进入、流出
	var $in, $out, $center = {}, currJsPlumb = jsPlumb;

	$center.appName = page.appName;
	$center.wid = "window_center";

	// 对应当前函数（类）名称
	var funId = "HSFWidget";
	// 对应jsPlumb容器div
	var graphId = "graph_1";

	var init = function() {
		update();
	}
	function doUpdate() {
		// 清空创建的div、然后调用jsPlumbN.reset
		reset();
		grepData("$in");
	}

	function reset() {
		$("#" + graphId + " div").css("display", "none");

		currJsPlumb.reset();
	}

	var update = function() {

		var funName = funId + "_doUpdate";
		eval(funName + "=function(){doUpdate()}");

		eval(funName + "()");
		setInterval(eval(funName), 60 * 1000);

	}

	function wid_generate(type, i) {
		return "window_" + (i + 1);
	}
	var urls = {
		$in : base + "/i_depend_hsf.do?method=getPvInApps"
	};

	/** 此type要和变量$in $out一致，因为下面使用eval赋值 */
	var grepData = function(type) {
		$.ajax( {
			url : urls[type],
			success : function(data) {
				eval(type + "=data");
				render(type);
			}
		});
	}
	var render = function(type) {
		renderCenter();
		renderElements(type);

		renderConnections(type);
	}
	function renderCenter() {
		var wid = $center.wid;
		var c = $("#" + wid);
		c.css("display", "block");
		var wnr = $("#" + wid + " [class*='windowName']");
		wnr.html(page.appName + new Date().toString("HH:mm"));
	}
	function renderConnections(type) {
		var data;
		eval("data=" + type);
		for ( var i = 0; i < data.length; i++) {
			var app = data[i];
			var wid = wid_generate(type, i);
			renderConnection(wid, app,type);
		}
		function renderConnection(wid, app,type) {
			//log("source: "+ wid+"\t target: "+$center.wid);
			var labelText1 = "connection";

			// alert("r: "+ r+"\t opsname: "+ opsname);
			// r是opsname，rs[r]是对应的流量
			
			var $source, $target;
			if(type=="$in"){
				$source = wid;
				$target = $center.wid;
			}
			else{
				$source = $center.wid;
				$target = wid;
			}
			currJsPlumb.connect( {
				source : $source,
				target : $target,
				connector : "Bezier",
				anchors : [ "AutoDefault", "AutoDefault" ],

				paintStyle : {
					lineWidth : 13,
					strokeStyle : "#a7b04b",
					outlineWidth : 1,
					outlineColor : "#666"
				},
				endpointStyle : {
					fillStyle : "#a7b04b"
				},

				hoverPaintStyle : {
					lineWidth : 13,
					strokeStyle : "#7ec3d9"
				},

				overlays : [ [ "Label", {
					cssClass : "l1 component label ",
					label : labelText1,
					location : 0.5,
					events : {
						"click" : function(label, evt) {
							alert("clicked on label for connection 1");
						}
					}
				} ], [ "Arrow", {
					location : 0.2,
					width : 50,
					events : {
						"click" : function(arrow, evt) {
							alert("clicked on arrow for connection 1");
						}
					}
				} ] ]
			}); // connection end
		}
	}

	var renderElements = function(type) {
		var data;
		eval("data=" + type);

		for ( var i = 0; i < data.length; i++) {
			var app = data[i];
			var wid = wid_generate(type, i);
			//log("wid: "+ wid);
			renderElement(wid, app);
		}

		function renderElement(wid, app) {

			var $w = $("#" + wid);
			var appName = app.appName;
			$w.css("display", "block");
			var wnr = $("#" + wid + " [class*='windowName']");
			wnr.html(appName + new Date().toString("HH:mm")+"--hsf");
		}
	}
	// var createElement = function(wid, html2) {
	// //alert("wid: "+ wid+"\t html2: "+html2);
	// var nd = document.createElement("div");
	// nd.className = "component window";
	// nd.id = wid;
	// nd.innerHTML = html2;
	// $("#"+graphId).get(0).appendChild(nd);
	//
	// }
	init();
}


function jsPlumbReady() {
	// chrome fix.
	document.onselectstart = function() {
		return false;
	};

	jsPlumbDemo.init();
}
function JsPlumbDemo() {
	this.init = function() {
		jsPlumb.Defaults.Container = $("#graph_1");
		jsPlumb.setMouseEventsEnabled(true);
	}
}

