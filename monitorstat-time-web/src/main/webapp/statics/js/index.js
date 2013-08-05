

// ��JsPlumbͼ��� begin
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

/** ����ͼ */

/** ���¿�Ƭ�ϵ����� */
$(function() {
			var appDataUpdate = new AppDataUpdate();
		});

function AppDataUpdate() {

	var apps;
	var $this = this;
	var init = function() {
		// ע��һ����ʱ����ÿ���Ӹ���������
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
	/** ����ע�����Խ���12����Ƭ����Ƭ�е�hostSizeԪ�ض�ע�ᵽȫ�֣��ŵ�һ��ȫ�ֱ����У�����������ÿ�θ��¶���λ��ЩԪ�� */
	var renderApps = function() {
		// alert("renderApps()");
		// �������
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
			// ������
			var fw = 0;
			// �����ϵ�label
			if (appType == "pv") {
				fw = pv;
			} else if (appType == "center") {
				fw = hsfProvider;
			}

			jQuery(selector4).html(fw);

			// var selector3 = 'div[class~="'+cn+'"] span[class~="blockCount"]';
			// jQuery(selector3).html(b);

			/** ��̬�������������� */
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
					labelText1 = "PV-refer: " + rs[r] + "\t ռ�������ٷֱ�:"
							+ (rs[r] / fw * 100).toFixed(2) + "%";
				} else if (appType == "center") {
					labelText1 = "HSF-refer: " + rs[r] + "\t ռ�������ٷֱ�:"
							+ (rs[r] / fw * 100).toFixed(2) + "%";
				}
				// alert("r: "+ r+"\t opsname: "+ opsname);
				// r��opsname��rs[r]�Ƕ�Ӧ������
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

// ��JsPlumbͼ��� begin

// Top N��� begin
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
		// ��ʾָ��������������
		colNumber : config.colNumber,
		// ������thead
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
	/** ��������������У������кš��к�д��css class�� */
	var createAllGrids = function() {
		for (var i = 0; i < config.rowNumber; i++) {
			// alert("row: "+(i+1));
			var tr;
			// i==0��ʾthead�е�tr
			if (i != 0)
				tr = document.createElement("tr");
			else
				tr = table_th_ref.getElementsByTagName("tr")[0];

			for (var j = 0; j < config.colNumber; j++) {
				var td = document.createElement("td");
				td.innerHTML = "&nbsp;";
				// ������ʽ
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
	 * ������n����
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
	 * ���tbody,
	 * ������ַ�ʽ��ӳ��ĳ��td��ĳ��app���ݣ���ʽ1��Լ����������(��ȷ��ȡ����getElementsByTagName���صļ�����Ԫ�ص�˳�������Ԫ�ض����˳�򣬾�û����)
	 * ��ʽ2��ʹ��classname
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
	 * ��ʱ����
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

// Top N��� end

