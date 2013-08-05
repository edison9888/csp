var jsReady = false;
var isLoginChartLoad = false;
var isDetailChartLoad = false;
var isTmallDetailChartLoad = false;
var isCartChartLoad = false;
var isTf_buyChartLoad = false;
var isTmallbuyChartLoad = false;
var isTpCreateChartLoad = false;
var isTmallCreateChartLoad = false;
var isTpPayChartLoad = false;
var shareReportInterv = 1000;
var chartInterv = 5000;
var pvIntev = 60000;
var isInit = false;
//document ready
$(document).ready( function(){
	// $(document).bind('scroll', function(){
		// alert('');
		// loadOnScroll();
	// });
	window.onscroll = loadOnScroll;
	//for sharereport
	initRealTimeShareReport();
	//window.setTimeout('getRealTimeShareReport()', 500);
	//for login
	getLoginPV();
	//for detail
	getDetailPV();
	//for malldetail
	getMalldetailPV();
	//for cart
	getCartPV();
	//for tfbuy
	getTfbuyPV();
	//for tmallbuy
	getTmallbuyPV();
	//for tpcreate
	//getTpCreateHSF();
	//for tmallCreate
	//getTmallCreateHSF();
	//for tpPay
	//getTpPayHSF();
	//window.setTimeout('initChart()', 2000);
	//window.setInterval('refrestChart()',chartInterv);
	jsReady = true;
});
//load while scrolling
function loadOnScroll(){
	if( !isLoginChartLoad && $("#loginchart").length > 0){
		if( $(window).scrollTop() + $(window).height() > $("#loginchart").offset().top ){
			getFlashChart("#loginchart");
			isLoginChartLoad = true;
		}
	}
	if( !isDetailChartLoad && $("#detail").length > 0){
		if( $(window).scrollTop() + $(window).height() > $("#detail").offset().top ){
			getFlashChart("#detail");
			isDetailChartLoad = true;
		}
	}
	if( !isTmallDetailChartLoad && $("#tmalldetail").length > 0){
		if( $(window).scrollTop() + $(window).height() > $("#tmalldetail").offset().top ){
			getFlashChart("#tmalldetail");
			isTmallDetailChartLoad = true;
		}
	}
	if( !isCartChartLoad && $("#cart").length > 0){
		if( $(window).scrollTop() + $(window).height() > $("#cart").offset().top ){
			getFlashChart("#cart");
			isCartChartLoad = true;
		}
	}
	if( !isTf_buyChartLoad && $("#tf_buy").length > 0){
		if( $(window).scrollTop() + $(window).height() > $("#tf_buy").offset().top ){
			getFlashChart("#tf_buy");
			isTf_buyChartLoad = true;
		}
	}
	if( !isTmallbuyChartLoad && $("#tmallbuy").length > 0){
		if( $(window).scrollTop() + $(window).height() > $("#tmallbuy").offset().top ){
			getFlashChart("#tmallbuy");
			window.onscroll = null;
			isTmallbuyChartLoad = true;
		}
	}
	// if( !isTpCreateChartLoad && $("#tpcreate").length > 0){
		// if( $(window).scrollTop() + $(window).height() > $("#tpcreate").offset().top ){
			// getFlashChart("#tpcreate");
			// isTpCreateChartLoad = true;
		// }
	// }
	// if( !isTmallCreateChartLoad && $("#tmallcreate").length > 0){
		// if( $(window).scrollTop() + $(window).height() > $("#tmallcreate").offset().top ){
			// getFlashChart("#tmallcreate");
			// isTmallCreateChartLoad = true;
		// }
	// }
	// if( !isTpPayChartLoad && $("#tppay").length > 0){
		// if( $(window).scrollTop() + $(window).height() > $("#tppay").offset().top ){
			// getFlashChart("#tppay");
			// $(document).unbind('scroll');
			// isTpPayChartLoad = true;
		// }
	// }
}
//get chart data
function getFlashChart(obj){
	$(obj).html("<img src='pics/index-loading.gif' title='loading chart graph' alt='loading' />");
	$.get('./get_chart_data.jsp', {
		appName : $(obj).attr("app-name"), 
		keyName : $(obj).attr("key-name")
	},
	function(data, textStatus) {
		drawFlashChart(data, $(obj).attr("id"));
	},
	'text');
}
//init falsh chart
function drawFlashChart(cData, divName){
	var params = {	bgcolor:"#FFFFFF",	wmode:"transparent"	};
	var flashVars = 
	{
		path: "flash/",
		settings_file: "flash/settings.xml",
		chart_data: encodeURIComponent(cData)
	};
	swfobject.embedSWF("flash/amline.swf",divName,"100%", "350", "8.0.0", "flash/expressInstall.swf", flashVars, params);
}

function initRealTimeShareReport(){
	$.get('./get_sharereport.jsp', 
	function(data, textStatus) {
		$('#shareport').html(data);
		getRealTimeShareReport();
	},
	'html');
}
function getRealTimeShareReport(){
	//var t = $('#shareport table tbody tr:first td').last().text();
	$.get('./get_shareport_now.jsp',
	function(data, textStatus) {
		var html = data.replace(/(^\s*)/g, "");
		$(html).insertBefore('#shareport table tbody tr:first');
		if( $('#shareport table tbody tr').length > 19 ){
			$('#shareport table tbody tr:last').remove();
		}
	},
	'html');
	window.setTimeout('getRealTimeShareReport()', shareReportInterv);
}

function getLoginPV(){
	$.get('./get_realtime_pv.jsp', 
		{ appName:'login' },
	function(data, textStatus) {
		$('#tb-login .loadingimg').remove();
		$('#tb-login table').remove();
		$('#tb-login').append(data);
	},
	'html');
	window.setTimeout('getLoginPV()', pvIntev);
}

function getDetailPV(){
	$.get('./get_realtime_pv.jsp', 
		{ appName:'detail' },
	function(data, textStatus) {
		$('#tb-detail .loadingimg').remove();
		$('#tb-detail table').remove();
		$('#tb-detail').append(data);
	},
	'html');
	window.setTimeout('getDetailPV()', pvIntev);
}

function getMalldetailPV(){
	$.get('./get_realtime_pv.jsp', 
		{ appName:'malldetail' },
	function(data, textStatus) {
		$('#tb-malldetail .loadingimg').remove();
		$('#tb-malldetail table').remove();
		$('#tb-malldetail').append(data);
	},
	'html');
	window.setTimeout('getMalldetailPV()', pvIntev);
}

function getCartPV(){
	$.get('./get_realtime_pv.jsp', 
		{ appName:'cart' },
	function(data, textStatus) {
		$('#tb-cart .loadingimg').remove();
		$('#tb-cart table').remove();
		$('#tb-cart').append(data);
	},
	'html');
	window.setTimeout('getCartPV()', pvIntev);
}

function getTfbuyPV(){
	$.get('./get_realtime_pv.jsp', 
		{ appName:'tf_buy' },
	function(data, textStatus) {
		$('#tb-tfbuy .loadingimg').remove();
		$('#tb-tfbuy table').remove();
		$('#tb-tfbuy').append(data);
	},
	'html');
	window.setTimeout('getTfbuyPV()', pvIntev);
}

function getTmallbuyPV(){
	$.get('./get_realtime_pv.jsp', 
		{ appName:'tmallbuy' },
	function(data, textStatus) {
		$('#tb-tmallbuy .loadingimg').remove();
		$('#tb-tmallbuy table').remove();
		$('#tb-tmallbuy').append(data);
	},
	'html');
	window.setTimeout('getTmallbuyPV()', pvIntev);
}

function getTpCreateHSF(){
	$.get('./get_realtime_hsf.jsp', 
		{ HSFName:'TpCreate' },
	function(data, textStatus) {
		$('#tb-tradeplatform .loadingimg').remove();
		$('#tb-tradeplatform table').remove();
		$('#tb-tradeplatform').append(data);
	},
	'html');
	window.setTimeout('getTpCreateHSF()', pvIntev);
}

function getTmallCreateHSF(){
	$.get('./get_realtime_hsf.jsp', 
		{ HSFName:'TmallCreate' },
	function(data, textStatus) {
		$('#tb-malltp .loadingimg').remove();
		$('#tb-malltp table').remove();
		$('#tb-malltp').append(data);
	},
	'html');
	window.setTimeout('getTmallCreateHSF()', pvIntev);
}

function getTpPayHSF(){
	$.get('./get_realtime_hsf.jsp', 
		{ HSFName:'TpPay' },
	function(data, textStatus) {
		$('#tb-tppay .loadingimg').remove();
		$('#tb-tppay table').remove();
		$('#tb-tppay').append(data);
	},
	'html');
	window.setTimeout('getTpPayHSF()', pvIntev);
}

/*for flash to test whether it is OK*/
function isReady() {
	return jsReady;
}
/*get movie*/
function thisMovie(movieName) {
	if (navigator.appName.indexOf("Microsoft") != -1) {
		return window[movieName];
	} else {
		return document[movieName];
	}
}

function initChart() {
	//tell flash init chart
/*JSON data like this: [{"c2cc":193,"b2cc":69,"c2cp":142,"b2cp":66,"time":"2012-08-17 16:00:58"},
{"c2cc":199,"b2cc":78,"c2cp":155,"b2cp":79,"time":"2012-08-17 16:00:53"},
{"c2cc":186,"b2cc":82,"c2cp":169,"b2cp":55,"time":"2012-08-17 16:00:48"}]*/
	$.get('./get_shareport_json.jsp', 
		{ init : 'true' },
		function(data) {
			//parse JSON нд╪Ч
			var len = data.length;
			var arr = new Array();
			for(var i=0; i<len; i++){
				var tmpArray = new Array();
				tmpArray.push(data[i].c2cc);
				tmpArray.push(data[i].b2cc);
				tmpArray.push(data[i].c2cp);
				tmpArray.push(data[i].b2cp);
				tmpArray.push(data[i].time);
				arr.push(tmpArray);
			}
			// arr= {1,1,1,1, 2, 2,2 ,2 '20:11'};
			thisMovie("flashobj").initChart(arr);
			isInit = true;
		},
	'json');
}

//get refrest data
//JSON data like this:{"c2cc":194,"b2cc":94,"c2cp":188,"b2cp":66,"c2cbc":194,"b2cbc":94,"c2cbp":188,"b2cbp":66,"time":"2012-08-17 16:11:22"}
function refrestChart(){
	if(isInit == false){
		return false;
	}
	$.get('./get_shareport_json.jsp', 
		function(data) {
			var arr = new Array();
			// arr= {1,1,1,1, 2, 2,2 ,2 '2012-08-14 15:20:11'};
			arr.push(data.c2cc);
			arr.push(data.b2cc);
			arr.push(data.c2cp);
			arr.push(data.b2cp);
			
			arr.push(data.c2cbc);
			arr.push(data.b2cbc);
			arr.push(data.c2cbp);
			arr.push(data.b2cbp);
			arr.push(data.time);
			//alert(arr.toString());
			thisMovie("flashobj").redrawChart(arr);
		},
	'json');
}