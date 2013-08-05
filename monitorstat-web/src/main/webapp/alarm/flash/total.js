var jsReady = false;
var isAccum = true;
var thridTimer = null;
var secondTimer = null;
var increaseTimer = null;
$(document).ready( function(){
	jsReady = true;
	secondTime = window.setTimeout('refreshChart()', 1000);
	getPastThirdTrade(resetDate(new Date()));
});
/*for flash to test whether it is OK*/
function isReady() {
	return jsReady;
}
/*stop timer*/
function killRefresh(){
	clearTimeout(thridTimer);
	clearTimeout(secondTimer);
	clearTimeout(increaseTimer);
}
/*get movie*/
function thisMovie(movieName) {
	if (navigator.appName.indexOf("Microsoft") != -1) {
		return window[movieName];
	} else {
		return document[movieName];
	}
}

function resetDate(d){
	d.setHours(0);
	d.setMinutes(0);
	d.setSeconds(0);
	d.setMilliseconds(0);
	return d;
}

function formatDate(d){
	var s ="";
	s += d.getFullYear()+"-";
	s += ((d.getMonth()+1)>9) ? ((d.getMonth()+1)+"-") : ("0"+(d.getMonth()+1)+"-");
	s += (d.getDate()>9)?(d.getDate()+" "):("0"+d.getDate()+" ");
	s += (d.getHours()>9)?(d.getHours()+":"):("0"+d.getHours()+":"); 
	s += (d.getMinutes()>9)?(d.getMinutes()+":"):("0"+d.getMinutes()+":");
	s += (d.getSeconds()>9)?(d.getSeconds()+":"):("0"+d.getSeconds()+":");
	return s;
}

function getPastThirdTrade(d){
	var f = formatDate(d);
	$.get('./get_third.jsp',
			{from:f},
			function(data) {
				var arr = new Array();
				arr.push(data.day);
				arr.push(data.bcs);
				arr.push(data.acs);
				arr.push(data.ccs);
				arr.push(data.bps);
				arr.push(data.aps);
				arr.push(data.cps);
				arr.push(data.csum);
				arr.push(data.psum);
				arr.push(f);
				//alert(data);
				if(data.over == "true"){
					isAccum = false;
				}
				thisMovie("flashobj").cylinderUp(arr);
			},
			'json');
	if(isAccum == true){
		increaseTimer = window.setTimeout(function(){
			d.setTime(d.getTime()+20000);
			getPastThirdTrade(d); //每次增加20s
		}, 200);
	} else {
		getPresentThirdTrade();
	}
}

function getPresentThirdTrade(){
	$.get('./get_third.jsp', 
		function(data) {
			var arr = new Array();
			arr.push(data.day);
			arr.push(data.bcs);
			arr.push(data.acs);
			arr.push(data.ccs);
			arr.push(data.bps);
			arr.push(data.aps);
			arr.push(data.cps);
			arr.push(data.csum);
			arr.push(data.psum);
			arr.push(data.date);
			thisMovie("flashobj").cylinderUp(arr);
		},
	'json');
	//刷新当前20ms的数据
	thridTimer = window.setTimeout('getPresentThirdTrade()', 20000);
}

function refreshChart(){
	$.get('./get_total_now.jsp', 
		function(data) {
			var arr = new Array();
			arr.push(data.ac); //taobaocreate
			arr.push(data.bc); //tmallcreate
			arr.push(data.cc); //jhscreate
			arr.push(data.ap); //taobaopaid
			arr.push(data.bp); //tmallpaid
			arr.push(data.date);
			thisMovie("flashobj").refreshChart(arr);
		},
	'json');
	secondTimer = window.setTimeout('refreshChart()', 5000);
}

