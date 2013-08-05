// Top N��� begin
$(function() {

			var topNAppsWidget = new RecentTopNWidget({
						type : "interface",
						tableId : "table1",
						colNumber : 3,
						rowNumber : 10

					});

			var topNAppsWidget2 = new RecentTopNWidget({
						type : "app",
						tableId : "table2",
						colNumber : 3,
						rowNumber : 10

					});

			var hostListGrepData = new HostListGrepData();

			hostListGrepData.grep();

			var chartWidget = new ChartWidget();

		});
page.openTabEffectOnlyInvokeOnce = false;
// ������������
function controlPopover() {
	$(".mypopover").popover('hide');
}

function ChartWidget() {
	var chart1DivId = "chart1Div";
	var rest1DivId = "rest1Div";
	var chart;
	var restChart;

	function init() {
		draw();
	}
	function draw() {

		// ��ʼ��chart�õ����ݣ����루generateChartData()��ִ�к󣬻�����������Щֵ
		var chartData = [{
					"pv" : "7",
					"ftime" : "15:58",
					"rest" : "4",
					"c302" : "6",
					"c200" : "5"
				}];
		AmCharts.ready(function() {

					// ///����chart
					// SERIAL CHART
					chart = new AmCharts.AmSerialChart();
					chart.dataProvider = chartData;
					chart.pathToImages = base + "/statics/js/amcharts/images/";
					chart.categoryField = "ftime";
					chart.marginTop = 0;
					chart.marginRight = 0;

					// value
					var valueAxis = new AmCharts.ValueAxis();
					valueAxis.axisColor = "#DADADA";
					valueAxis.dashLength = 1;
					chart.addValueAxis(valueAxis);

					// GRAPH pv
					var graphPv = new AmCharts.AmGraph();
					graphPv.lineColor = "#00FF00";
					graphPv.negativeLineColor = "#00FF00"; // this line makes
					// the graph
					// to
					// change color when it drops
					// below 0
					graphPv.bullet = "round";
					graphPv.bulletSize = 5;
					graphPv.connect = false; // this makes the graph not to
					// connect
					// data
					// points if data is missing
					graphPv.lineThickness = 2;
					graphPv.type = "smoothedLine";
					graphPv.title = "ȫ������";
					graphPv.valueField = "hsf_provider";
					chart.addGraph(graphPv);

					// GRAPH c200
					var graph200 = new AmCharts.AmGraph();
					graph200.lineColor = "#99CC33";
					graph200.negativeLineColor = "#99CC33"; // this line makes
					// the graph
					// to
					// change color when it drops
					// below 0
					graph200.bullet = "round";
					graph200.bulletSize = 5;
					graph200.connect = false; // this makes the graph not to
					// connect
					// data
					// points if data is missing
					graph200.lineThickness = 2;
					graph200.type = "smoothedLine";
					graph200.title = "����200";
					graph200.valueField = "c200";
					chart.addGraph(graph200);

					// GRAPH c302
					var graph302 = new AmCharts.AmGraph();
					graph302.lineColor = "#FF0000";
					graph302.negativeLineColor = "#FF0000"; // this line makes
					// the graph
					// to
					// change color when it drops
					// below 0
					graph302.bullet = "round";
					graph302.bulletSize = 5;
					graph302.connect = false; // this makes the graph not to
					// connect
					// data
					// points if data is missing
					graph302.lineThickness = 2;
					graph302.type = "smoothedLine";
					graph302.title = "����302";
					graph302.valueField = "c302";
					chart.addGraph(graph302);

					// CURSOR
					var chartCursor = new AmCharts.ChartCursor();
					chartCursor.cursorAlpha = 0;
					chartCursor.cursorPosition = "mouse";
					chart.addChartCursor(chartCursor);

					// SCROLLBAR

					// LEGEND
					var legend = new AmCharts.AmLegend();
					legend.marginLeft = 110;
					chart.addLegend(legend);

					// WRITE
					chart.write(chart1DivId);

					// ��Ӧʱ�� chart

					restChart = new AmCharts.AmSerialChart();
					restChart.dataProvider = chartData;
					restChart.pathToImages = base
							+ "/statics/js/amcharts/images/";
					restChart.categoryField = "ftime";
					restChart.marginTop = 0;
					restChart.marginRight = 0;

					restChart.addValueAxis(valueAxis);

					// GRAPH rest
					var graphrest = new AmCharts.AmGraph();
					graphrest.lineColor = "#00FF00";
					graphrest.negativeLineColor = "#00FF00"; // this line
					// makes the
					// graph
					// to change color when it
					// drops below 0
					graphrest.bullet = "round";
					graphrest.bulletSize = 5;
					graphrest.connect = false; // this makes the graph not to
					// connect
					// data
					// points if data is missing
					graphrest.lineThickness = 2;
					graphrest.type = "smoothedLine";
					graphrest.title = "ȫ��ƽ����Ӧʱ��";
					graphrest.valueField = "rest";
					restChart.addGraph(graphrest);

					// CURSOR
					var restCursor = new AmCharts.ChartCursor();
					restCursor.cursorAlpha = 0;
					restCursor.cursorPosition = "mouse";
					restChart.addChartCursor(restCursor);

					var restlegend = new AmCharts.AmLegend();
					restlegend.marginLeft = 110;
					restChart.addLegend(restlegend);

					restChart.write(rest1DivId);

					generateChartData();
				});

	}

	function generateChartData() {
		$.ajax({
			url : base
					+ "/app/detail/hsf/provider/show.do?method=hsfProviderSingleKeyPerAppDay&appName="
					+ page.appName,
			success : function(data) {
				var data2 = transformList(data);

				chart.dataProvider = data2;
				restChart.dataProvider = data2;
				chart.validateData();
				restChart.validateData();
			}
		});
		window.setTimeout(generateChartData, 60 * 1000);

		function transformList(srcList) {
			var destList = [];
			if (srcList != 'undefined') {
				for (var i = 0; i < srcList.length; i++) {
					var srcItem = srcList[0];
					var destItem = {};
					if(srcItem != 'undefined' && srcItem.data != 'undefined'){
						var d = srcItem.data;
						destItem['ftime'] = srcItem['timeF'];
						destItem['hsf_provider'] = d['E-times'];
						destItem['c200'] = d['C-200'];
						destItem['c302'] = d['C-302'];
						destItem['rest'] = d['C-time'];
						destList[destList.length] = destItem;	
					}

				}
			}
			return destList;

		}
	}
	init();
}// ChartWidget

function RecentTopNWidget(config) {
	var config = {
		type : config.type,
		tableId : config.tableId,
		// ��ʾָ��������������
		colNumber : config.colNumber,
		// ������thead
		rowNumber : config.rowNumber
	};

	var url, key1;
	if (config.type == 'interface') {
		url = base
				+ "/app/detail/hsf/provider/show.do?method=provideInterfaceTopN";
		key1 = 'interfaceName';
	} else if (config.type == 'app') {
		url = base
				+ "/app/detail/hsf/provider/show.do?method=otherAppsInvokeCurrAppTopN";
		key1 = 'appName';
	}
	var topNList;
	// alert(config.lastestMinite);
	var tableRef = document.getElementById(config.tableId);
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
					url : url + "&appName=" + page.appName,
					success : function(data) {
						topNList = data;
						topN();

					}
				});
	}
	var createAllGrids = function() {
		for (var i = 0; i < config.rowNumber; i++) {
			// alert("row: "+(i+1));
			var tr;
			// i==0��ʾthead�е�tr
			tr = document.createElement("tr");

			for (var j = 0; j < config.colNumber; j++) {
				var td = document.createElement("td");
				// Ĭ��td�����¿�ȣ���Ȼ̫С��
				// appendClassName(td, "span1");
				// Ĭ��td��ֵ����Ȼtd̫С��
				td.innerHTML = "&nbsp;";
				tr.appendChild(td);
			}
			table_tb_ref.appendChild(tr);
		}
	};

	/**
	 * ���tbody,
	 * ������ַ�ʽ��ӳ��ĳ��td��ĳ��app���ݣ���ʽ1��Լ����������(��ȷ��ȡ����getElementsByTagName���صļ�����Ԫ�ص�˳�������Ԫ�ض����˳�򣬾�û����)
	 * ��ʽ2��ʹ��classname��jsp����table
	 */
	var topN = function() {
		// /var apps = config.topNApps;
		var trs = table_tb_ref.getElementsByTagName("tr");
		if (topNList.length > 0) {
			for (var i = 0; i < trs.length; i++) {
				var tr = trs[i];
				var tds = tr.getElementsByTagName("td");

				var app = topNList[i];
				if (typeof app != 'undefined') {
					for (var j = 0; j < tds.length; j++) {
						var td = tds[j];
						var v;
						if (j == 0) {
							v = app['collectTime'];
						} else if (j == 1) {
							// log("if 2");
							v = app[key1];
						} else if (j == 2) {
							v = app['eTimes'] + "(" + app['rate'] + ")";
						}
						td.innerHTML = v;
					}

				}

			}

		} else {
			// alert("û������");
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

/** ��ʾcm3��cm4�����б� */
function HostListGrepData() {
	var hstcId = "hostSiteTabContent";
	var hstId = "hostSiteTab";
	var hstcEle = $("#" + hstcId);
	var hstEle = $("#" + hstId);
	var $data;
	this.grep = function() {

		var funName = "hostListGrepDataInterval";
		eval(funName + "=function(){doGrep()}");

		eval(funName + "()");
		setInterval(eval(funName), 30 * 1000);

	}

	function doGrep() {
		var url = base
				+ "/app/detail/hsf/provider/show.do?method=hostList&appName="
				+ page.appName;
		$.ajax({
					url : url,
					success : function(data) {
						$data = data;
						createHostListWidgetIfNotExist();
						updateData();
					}
				});

	}
	function updateData() {
		for (var item in $data) {
			var temp_a;
			eval("temp_a=window.hostListWidget_" + item);
			temp_a.update($data);
		}
	}

	function createHostListWidgetIfNotExist() {
		createHostSiteTab();
		createHostSiteTabContent();

		// �ж��Ƿ���class*="active"��Ԫ�أ����û�У�����һ��Ԫ�����active
		if ($("#" + hstId + " li[class*=active]").length == 0) {
			appendClassName($("#" + hstId + " li")[0], "active");
		}

		// �ж��Ƿ���class*="active"��Ԫ�أ����û�У�����һ��Ԫ�����active
		if ($("#" + hstcId + " div[class*=active]").length == 0) {
			appendClassName($("#" + hstcId + " div")[0], "active");
		}

		if (page.openTabEffectOnlyInvokeOnce == false) {
			// ����ѡ�Ч��

			// ��Ȼactive class��li�ϣ����ǵ����¼��Ƿ���a�ϵ�
			$("#" + hstId + " a").click(function(e) {
						// ��ֹ���ӵ�ê����Ϊ
						e.preventDefault();
						$(this).tab('show');
					});
		}

		for (var item in $data) {
			// ��������ڴ���ʵ��
			if (typeof eval("window.hostListWidget_" + item) == 'undefined') {
				eval("window.hostListWidget_"
						+ item
						+ " = new HostListWidget({id_prefix : item,containerId : item + '_container'});");

			}

		}

	}
	function createHostSiteTab() {
		for (var item in $data) {
			// ʾ��:cm3_tab
			var id = item + "_tab";
			// �ж�ҳ���Ƿ���ڴ�Ԫ��,��������ڴ�tab������
			var t = $("#" + id);
			// log("------ id: "+id+"\t typeof t[0]: "+(typeof t[0]));
			if (typeof t[0] == 'undefined') {

				var li = document.createElement("li");
				li.id = id;
				// li.className = "active";
				var a = document.createElement("a");
				a["href"] = "#" + item + "_container";
				a["data-toggle"] = "tab";
				a.innerHTML = item;
				li.appendChild(a);
				hstEle[0].appendChild(li);
			}
		}

	};
	function createHostSiteTabContent() {
		for (var item in $data) {
			// ʾ��:cm3_container
			var id = item + "_container";
			// �ж�ҳ���Ƿ���ڴ�Ԫ��,��������ڴ�tab������
			var t = $("#" + id);
			if (typeof t[0] == 'undefined') {
				var div = document.createElement("div");
				div.className = "thumbnails tab-pane";
				div.style.marginLeft = "0px";
				div.id = id;
				hstcEle[0].appendChild(div);
			}
		}

	};
}
function HostListWidget(config) {
	var list;
	var config = {
		id_prefix : config.id_prefix,
		// ����div��Id
		containerId : config.containerId,
		hostSite : config.id_prefix

	};
	var hostSite = config.hostSite;
	var containerId = config.containerId;
	var init = function() {
		// update();
	}

	function adjustDivCounts() {
		// log("adjustDivCounts()");
		var l = (typeof list != 'undefined') ? list.length : 0;
		// ��������host�ĸ���
		var co = $("#" + hostSite + "_container div[class*='host']")
		// log("l: " + l + "\t co.length: " + co.length);
		if (typeof co != 'undefined') {
			var l2 = co.length;
			if (l2 != 'undefined') {
				if (l > l2) {

					addDivs();

				} else {
					// ɾ������div
					deleteDivs();

				}
			}

		} // ����div
		/**
		 * ����begin�� l2+1�� ����end��l
		 */
		function addDivs() {
			// log("addDivs()");
			var beginIndex = l2 + 1;
			var endIndex = l;
			// log("beginIndex: " + beginIndex + "\t endIndex: " + endIndex);
			for (var i = beginIndex; i <= endIndex; i++) {
				var nd = document.createElement("div");
				nd.id = genDivId(i);
				nd.className = "host thumbnail";
				nd.innerHTML = $("#copy1").html();
				// add div
				// log("add div: " + nd.id);
				document.getElementById(containerId).appendChild(nd);
				// log("" + containerId + ": "+ $("#" + containerId + "
				// div").length);
			}
		}
		function deleteDivs() {

			var beginIndex = l + 1;
			var endIndex = l2;
			for (var i = beginIndex; i <= endIndex; i++) {
				var id = genDivId(i);
				var nd = document.getElementById(id);
				nd.parentNode.removeChild(nd);
			}

		}

		function genDivId(i) {
			return hostSite + "_" + i;
		}
	}
	this.update = function(data) {

		list = data[hostSite];
		doUpdate();

	}
	function doUpdate() {
		// ��մ�����div��Ȼ�����jsPlumbN.reset
		// reset();
		render();
	}
	var render = function() {
		adjustDivCounts();
		controlPopover();
		renderElements();
	}

	var renderElements = function() {
		for (var i = 0; i < list.length; i++) {
			var host = list[i];
			if (typeof host != 'undefined') {
				var host_data = host['data'];
				var host_ip = host['ip'];

				var hid = config.id_prefix + "_" + (i + 1);
				renderElement(hid, host_data, host_ip);
			}

		}

		function renderElement(hid, host_data, host_ip) {

			if (typeof host_data != 'undefined'
					&& typeof host_ip != 'undefined') {

				handlePopover(hid, host_data);
				// ���1����
				var hdt = host_data[0];
				if (typeof hdt != 'undefined') {
					var hdtd = hdt['data'];
					if (typeof hdtd != 'undefined') {
						var $w = $("#" + hid);
						$w.css("display", "block");

						var ipd = $("#" + hid + " span[class*='ip']");
						ipd.html('IP: ' + host_ip);

						var etd = $("#" + hid + " span[class*='eTimes']");
						etd.html(hdtd['E-times']);

						var ct = $("#" + hid + " span[class*='collectTime']");

						var pq = hdt['time'];
						ct.html(longToDateStr(pq));

					}
				}
			}

			function handlePopover(hid, hd) {
				var c = content(hd);
				var e = $("#" + hid + " span[class*='mypopover']");
				e.attr("data-content", c);

			}

			function content(hd) {
				if (typeof hd != 'undefined') {
					var c = "<table class='table table-striped table-bordered table-condensed'>";
					c += "<thead><tr><td>time</td><td>E-times</td><td>C-time</td><td>P-size</td></tr></thead><tbody>";
					for (var i = 0; i < hd.length; i++) {
						// ĳһ��
						var hdi = hd[i];

						if (typeof hdi != 'undefined') {
							var time = hdi['time'];
							time = longToDateStr(time);
							var hdid = hdi['data'];
							if (typeof hdid != 'undefined') {
								c += "<tr>";
								c += "<td>" + time + "</td>";
								c += "<td>" + hdid['E-times'] + "</td>";
								c += "<td>" + hdid['C-time'] + "</td>";
								c += "<td>" + hdid['P-size'] + "</td>";
								c += "</tr>";
							}
						}

					}
					c += "</tbody></table>";
					return c;

				}
				return "no data";
			}
		}
	}
	init();
}

function longToDateStr(l) {
	if (typeof l != 'undefined') {
		var d = new Date();
		d.setTime(l);
		return d.toString("HH:mm");
	}
	return "";
}
