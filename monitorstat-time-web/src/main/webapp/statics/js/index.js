

// 画JsPlumb图相关 begin
window.jsPlumbDemo = new JsPlumbDemo();

function JsPlumbDemo() {

	this.init = function() {
		jsPlumb.Defaults.Container = $("#graph_1");
		jsPlumb.setMouseEventsEnabled(true);
	}

}
jsPlumb.bind("ready", function() {

			// chrome fix.
			document.onselectstart = function() {
				return false;
			};
			jsPlumbDemo.init();

		});

/** 大盘图 */

/** 更新卡片上的数据 */
$(function() {
			var appDataUpdate = new AppDataUpdate();
		});

function AppDataUpdate() {

	var apps;
	var $this = this;
	var init = function() {
		// 注册一个定时器，每分钟更新下数据
		grepData();
		setInterval(grepData, 60 * 1000);

	};

	var grepData = function() {
		$.ajax({
					url : base + "/index.do?method=getApps",
					success : function(data) {
						apps = data;
						renderApps();
					}
				});

	}
	/** 待关注：可以将这12个卡片、卡片中的hostSize元素都注册到全局（放到一个全局变量中），这样不用每次更新都定位这些元素 */
	var renderApps = function() {
		// alert("renderApps()");
		// 清空连接
		jsPlumb.reset();

		var items = jQuery('div[id^="opsname_"]');
		// alert(items.length);
		for (var i = 0; i < items.length; i++) {
			var item = items[i];
			var opsname = getOpsname(item);

			var cn = "opsname_" + opsname;
			
			var h = apps[opsname].hostSize;
			var e = apps[opsname].exceptionCount;
			var b = apps[opsname].blockCount;
			var c = apps[opsname].collectTime;
			var pv = apps[opsname].pv;
			var fr = apps[opsname].failRate;
			var appPo = apps[opsname].appPo;
			var appType = appPo.appType.toLowerCase();
			var hsfProvider = apps[opsname].hsfProvider;
	

			var selector1 = 'div[id="' + cn + '"] span[class~="hostSize"]';
			jQuery(selector1).html(h);

			var selector2 = 'div[id="' + cn
					+ '"] span[class~="exceptionCount"]';
			jQuery(selector2).html(e);

			var selector3 = 'div[id="' + cn + '"] span[class~="collectTime"]';
			jQuery(selector3).html(c);

			var selector4 = 'div[id="' + cn + '"] span[class~="pv"]';
			jQuery(selector4).html(pv);

			var selector5 = 'div[id="' + cn + '"] span[class~="failRate"]';
			jQuery(selector5).html(fr);
			// 总流量
			var fw = 0;
			// 连接上的label
			if (appType == "pv") {
				fw = pv;
			} else if (appType == "center") {
				fw = hsfProvider;
			}

			jQuery(selector4).html(fw);

			// var selector3 = 'div[class~="'+cn+'"] span[class~="blockCount"]';
			// jQuery(selector3).html(b);

			/** 动态创建、更新连接 */
			var rs;
			if (appType == "pv") {
				rs = apps[opsname].refer;
			} else if (appType == "center") {
				rs = apps[opsname].hsfRefer;
			}
			// alert("123");
			// rs Map<appName, PV-refer>
			for (r in rs) {
				var labelText1 = "";
				if (appType == "pv") {
					labelText1 = "PV-refer: " + rs[r] + "\t 占总流量百分比:"
							+ (rs[r] / fw * 100).toFixed(2) + "%";
				} else if (appType == "center") {
					labelText1 = "HSF-refer: " + rs[r] + "\t 占总流量百分比:"
							+ (rs[r] / fw * 100).toFixed(2) + "%";
				}
				// alert("r: "+ r+"\t opsname: "+ opsname);
				// r是opsname，rs[r]是对应的流量
				jsPlumb.connect({
					source : "opsname_" + r,
					target : "opsname_" + opsname,
					connector : "Bezier",
					anchors : ["AutoDefault", "AutoDefault"],

					paintStyle : {
						lineWidth : 2,
						strokeStyle : "gray"
					},
					endpointStyle : {
						   radius:9, 
            				fillStyle:"gray"

					},

					hoverPaintStyle : {
				  	 strokeStyle : "#ec9f2e"

					},

					overlays : [["Label", {
								cssClass : "l1 component label ",
								label : labelText1,
								location : 0.5
							}],[ "Arrow", { location:0.7 }, { foldback:0.7, fillStyle: "gray", width:14 } ]
]
				}); // connection end
			}
		}

		function getOpsname(item) {
			var c = item.id;
			var opsname = c.substring("opsname_".length);
			return opsname;
		}
	}

	init();
}

// 画JsPlumb图相关 begin

// Top N相关 begin
$(function() {
			var topNAppsWidget = new RecentNMinitesTopNAppsWidget({
						tableId : "table1",
						colNumber : 4,
						rowNumber : 11

					});

			var topNAppsWidget2 = new RecentNMinitesTopNAppsWidget({
						tableId : "table2",
						colNumber : 4,
						rowNumber : 11

					});

		});

function RecentNMinitesTopNAppsWidget(config) {
	var config = {
		tableId : config.tableId,
		// 显示指定列数、行数，
		colNumber : config.colNumber,
		// 不包括thead
		rowNumber : config.rowNumber
	};
	var recentM;
	var recentMtopNList;
	// alert(config.lastestMinite);
	var tableRef = document.getElementById(config.tableId);
	var table_th_ref = tableRef.getElementsByTagName("thead")[0];
	var table_th_tr_ref = table_th_ref.getElementsByTagName("tr")[0];
	var table_tb_ref = tableRef.getElementsByTagName("tbody")[0];
	var init = function() {
		createAllGrids();

		var intervalFunName = "grabData_" + config.tableId;
		eval("function " + intervalFunName + "(){grabData()}");

		eval(intervalFunName + "()");

		window.setInterval(eval(intervalFunName), 60 * 1000);

	}

	var grabData = function() {

		$.ajax({
					url : base +"/index.do?method=getRecentMTopN",
					success : function(data) {
						recentM = data.recentM;
						recentMtopNList = data.recentMtopNList;

						recentNMinites();
						topNApps();

					}
				});
	}
	/** 创建表格中所有列，并将行号、列号写进css class中 */
	var createAllGrids = function() {
		for (var i = 0; i < config.rowNumber; i++) {
			// alert("row: "+(i+1));
			var tr;
			// i==0表示thead中的tr
			if (i != 0)
				tr = document.createElement("tr");
			else
				tr = table_th_ref.getElementsByTagName("tr")[0];

			for (var j = 0; j < config.colNumber; j++) {
				var td = document.createElement("td");
				td.innerHTML = "&nbsp;";
				// 用于样式
				if (i == 0)
					appendClassName(td, "blue");
				tr.appendChild(td);
			}
			// alert(123);
			if (i != 0)
				table_tb_ref.appendChild(tr);
		}
	};
	/**
	 * 填充最近n分钟
	 */
	var recentNMinites = function() {
		var rnm = recentM;
		var th_tds = table_th_tr_ref.getElementsByTagName("td");

		for (var i = 0; i < th_tds.length; i++) {
			var td = th_tds[i];
			td.innerHTML = rnm[i];
		}
	}

	/**
	 * 填充tbody,
	 * 结合两种方式来映射某个td和某个app数据：方式1：约定的索引；(正确性取决于getElementsByTagName返回的集合中元素的顺序，如果按元素定义的顺序，就没问题)
	 * 方式2：使用classname
	 */
	var topNApps = function() {
		// /var apps = config.topNApps;
		var trs = table_tb_ref.getElementsByTagName("tr");
		for (var i = 0; i < trs.length; i++) {
			var tr = trs[i];
			var tds = tr.getElementsByTagName("td");
			for (var j = 0; j < tds.length; j++) {
				// var cn = td.className;
				var td = tds[j];
				var rj = recentMtopNList[j];
				if (typeof rj != 'undefined') {
					var rjl = rj["list"];
					if (typeof rjl != 'undefined') {
						var app = rjl[i];
						if (typeof app != 'undefined' && app!=null) {
							var op = app.opsname;
							var ec = app.exceptionCount;
							td.innerHTML = op + "(" + ec + ")";
						}
					}
				}
			}

		}

	}

	/**
	 * 定时更新
	 */

	init();

}

function appendClassName(ele, className) {
	// alert(ele.innerHTML);
	if ($.trim(ele.className) == "")
		ele.className = className;
	else
		ele.className += " " + className;
}

// Top N相关 end

