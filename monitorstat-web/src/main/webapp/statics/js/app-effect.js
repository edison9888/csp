var jsReady = false;
var isAlarmInfoShow = false;
var isQueryEnd = true;
var refInterv = 0;
var refreshInterval = 40000;
var tiptimer = 0;
$(document).ready(function() {
	//全屏查看
	$("#wrapall").click(function() { wrapBanner(); });
	$("#dragscreen").click(function(){dragScreen();});
	
	$("#ascreen").click(function() { 
		if($("#banner").height() > 120){
			$(document).scrollTop(573); 
		} else {
			$(document).scrollTop(110);
		}
	});
	$("#ainfo").click(function() { $(document).scrollTop(110); });
	$("#opselect").change( function(){
		var p = $("#opselect").val();
		if( p == -1){
			$("#selecttip").text("系统查询所有级别的报警信息!");
		} else {
			$("#selecttip").text("系统查询P"+p+"级别的报警信息!");
		}
		$("#selecttip").fadeIn("fast",
				function(){
					window.clearTimeout(tiptimer);
					tiptimer = window.setTimeout(function(){ $("#selecttip").fadeOut("slow");}, 7000);
				});
	});
	$("#view-btn").click(function() { 
		window.open("view_alarm_info.jsp?appId=0&startTime="+$("#fromtimepicker").val()+"&endTime="+$("#totimepicker").val()+"&level="+$("#opselect").find("option:selected").val());
	});
	
	$("#help").fadeIn("slow");
	$("#help").click(function() { $(this).fadeOut('fast'); });
	$("#etimetip").click(function(){ $(this).fadeOut("fast"); });
	$("form").submit(function() { checkTimeFormatAndSubmit(); return false; });
	$("#sear-left").click(function() {	getPreviousAlarm(); });
	$("#sear-right").click(function() {	getNextAlarm();	});
	
	$("#sear-now").click(function() {
		if(refInterv == 0){
			addLoadingImage();
			refreshAlarm();
			refInterv = window.setInterval(refreshAlarm, refreshInterval);
			$("#fromtimepicker").val("");
			$("#totimepicker").val("");
		}
	});
	
	refInterv = window.setInterval(refreshAlarm, refreshInterval);
	window.setTimeout(function(){refreshAlarm();},500);
	//打开界面显示屏幕
	dragScreen();
	jsReady = true;
});

function checkTimeFormatAndSubmit(){
	var start_time = $("#fromtimepicker").val();
	var end_time = $("#totimepicker").val();
	var reg = /[0-1][0-9]\/[0-3][0-9]\/20[0-9][0-9]\s[0-2][0-9]:[0-5][0-9]:[0-5][0-9](?!.+)/;
	if ( (start_time != "" || end_time != "") && (reg.exec(start_time) == null || reg.exec(end_time) == null) ) {
		alert("输入的时间格式不合法!");
	}else{
		var sDate = new Date( Date.parse(start_time) );
		//var sDate = new Date(parseInt(strDate[2]), parseInt(strDate[0]), parseInt(strDate[1]), parseInt(strTime[0]), parseInt(strTime[1]), parseInt(strTime[2]), 0);
		var eDate = new Date( Date.parse(end_time) );
		//eDate = new Date(parseInt(strDate[2]), parseInt(strDate[0]), parseInt(strDate[1]), parseInt(strTime[0]), parseInt(strTime[1]), parseInt(strTime[2]), 0);
		if(eDate.getTime() <= sDate.getTime()){
			alert('查询开始时间不早于查询终止时间,请重新设定时间!');
		} else {
			showEarlyTime();
			searchAlarmByTime(start_time, end_time);
		}
	}
}

function searchAlarmByTime(start, end){
	if( isQueryEnd == false){
		return false;
	}
	if(refInterv != 0){
		window.clearInterval(refInterv);
		refInterv = 0;
	}
	addLoadingImage();
	showScreen();
	$('#datachanged').fadeOut('fast');
	//query start
	isQueryEnd = false;
	$.get('./get_app_alarm_info.jsp', 
		  {
			appId:$("#appId").text(),
			startTime:start,
			endTime:end,
			level:$("#opselect").find("option:selected").val()
			},
	function(data, textStatus) {
		$('#tab1').html(data);
		if( data.indexOf("hasalarm")>0 ){
			showAlarmInfo();
		} else {
			ceaseFlashAlarm();
		}
		isQueryEnd = true;
	},
	'html');
}
//设置loading图片
function addLoadingImage() {
	$('#tab1 table').remove();
	$('#tab1 .bigpadding').remove();
	$("#tab1").append("<div class='loading'><img src='pics/loading.gif' /></div>");
}
//获得当前时间报警信息，并触发图形报警
function refreshAlarm() {
	$.get('./get_app_alarm_info.jsp', 
		{ 
			appId:$("#appId").text(),
			level:$("#opselect").find("option:selected").val()
		},
		function(data, textStatus) {
			$('#tab1').html(data);
			if( data.indexOf("hasalarm")>0 ){
				showAlarmInfo();
			} else {
				ceaseFlashAlarm();
			}
		},
		'html');
}
function showAlarmInfo() {
	showScreen();
	informFlashAlarm();
	loadArtooChangeFreeData();
}
//从当前时间往前20分钟的报警
function getPreviousAlarm(){
	//analyze timeformat like this:"2012-06-06 00:00"
	if(document.getElementById("endtime") == null || isQueryEnd == false){
		return false;
	}
	var strTime=$("#starttime").text();
	var d = new Date( Date.parse(strTime.replace(/-/g, "/") ));
	var etime = searchTimeFormat(d);
	d.setTime(d.getTime()-20*60*1000);
	var stime = searchTimeFormat(d);
	
	$("#fromtimepicker").val( stime );
	$("#totimepicker").val( etime );
	
	if(refInterv != 0){
		window.clearInterval(refInterv);
		refInterv = 0;
	}
	//add loading picture
	addLoadingImage();
	$('#datachanged').fadeOut('fast');
	isQueryEnd = false;
	$.get('./get_app_alarm_info.jsp', 
		{
			appId:$("#appId").text(),
			startTime: stime,
			endTime:etime,
			level:$("#opselect").find("option:selected").val()
		},
		function(data, textStatus) {
			$('#tab1').html(data);
			if( data.indexOf("hasalarm")>0 ){
				showAlarmInfo();
			} else {
				ceaseFlashAlarm();
			}
			isQueryEnd = true;
		},
		'html');
}
//从当前时间往后20分钟的报警
function getNextAlarm(){
	//analyze timeformat like this:"2012-06-06 00:00"
	if(document.getElementById("endtime") == null || isQueryEnd == false){
		return false;
	}
	var strTime=$("#endtime").text();
	var d = new Date( Date.parse(strTime.replace(/-/g, "/") ));
	var stime = searchTimeFormat(d);
	d.setTime(d.getTime()+20*60*1000);
	var etime = searchTimeFormat(d);
	
	$("#fromtimepicker").val( stime );
	$("#totimepicker").val( etime );
	
	if(refInterv != 0){
		window.clearInterval(refInterv);
		refInterv = 0;
	}
	//add loading picture
	addLoadingImage();
	$('#datachanged').fadeOut('fast');
	isQueryEnd = false;
	$.get('./get_app_alarm_info.jsp', 
		{
			appId:$("#appId").text(),
			startTime: stime,
			endTime:etime,
			level:$("#opselect").find("option:selected").val()
		},
		function(data, textStatus) {
			$('#tab1').html(data);
			if( data.indexOf("hasalarm")>0 ){
				showAlarmInfo();
			} else {
				ceaseFlashAlarm();
			}
			isQueryEnd = true;
		},
		'html');
}
function searchTimeFormat(d){
	if(d == null){
		d = new Date();
	}
	var s = "";
	m = d.getMonth()+1;
	s += ( (m>9) ? m :("0"+m) );
	s += "/";
	s += ( (d.getDate())>9 ? d.getDate():("0"+d.getDate()));
	s += "/";
	s += d.getFullYear();
	s += " ";
	s += ( (d.getHours())>9 ? d.getHours():("0"+d.getHours()));
	s += ":";
	s += ( (d.getMinutes())>9 ? d.getMinutes():("0"+d.getMinutes()));
	s += ":";
	s += ( (d.getSeconds())>9 ? d.getSeconds():("0"+d.getSeconds()));
	return s;
}
//for body wrapping effect
function wrapBanner() {
	var h = $("#banner").height();
	if (navigator.userAgent.indexOf("MSIE") > 0) {
		if ( h > 100 ) {
			$("#banner").animate({
				height: '1px'
			},
			'slow');
		} else {
			if(isAlarmInfoShow){
				$("#banner").animate({
					height: '574px'
				},
				'slow');
			} else {
				$("#banner").animate({
					height: '111px'
				},
				'slow');
			}
		}
	} else {
		if (h > 100) {
			$("#banner").animate({
				height: '0'
			},
			'slow');
		} else {
			if(isAlarmInfoShow){
				$("#banner").animate({
					height: '573px'
				},
				'slow');
			} else {
				$("#banner").animate({
					height: '110px'
				},
				'slow');
			}
		}

	}
}
function wrapScreen() {
	if (isAlarmInfoShow) {
		$("#banner").animate({
			height: '110px'
		},
		'slow');
		isAlarmInfoShow = false;
	}
}
function showScreen() {
	if (isAlarmInfoShow == false) {
		$("#banner").animate({
			height: '573px'
		},
		'slow');
		isAlarmInfoShow = true;
	}
}
function dragScreen(){
	if( $("#banner").height() > 120 ){
		$("#dragscreen img").attr( { src: "pics/down.gif"} );
		$("#banner").animate({ height: '110px' }, 'slow');
	} else {
		$("#dragscreen img").attr( { src: "pics/up.gif"} );
		$("#banner").animate({ height: '573px' }, 'slow');
	}
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

/**
	this function will return a parameters string!
	A string like "tradeplatform:0:1,diamond:1:0,tcamin:1:1,".
	:0:1-> first bit appalarm, second bit for sourcealarm
	0 not alarm
	1 alarm
*/
function informFlashAlarm() {
	var appParamlist = "";
	if (document.getElementById("hasalarm") != null) {
		for (var i = 0; i < appNameArray.length; i++) {
			var apObj = $('#' + appNameArray[i]);
			if (apObj.attr("alm") == 'true' || apObj.attr("title") == 'true') {
				if (apObj.attr("alm") == 'true') {
					appParamlist += appNameArray[i] + ":1";
				} else {
					appParamlist += appNameArray[i] + ":0";
				}
				if (apObj.attr("title") == 'true') {
					appParamlist += ":1";
				} else {
					appParamlist += ":0";
				}
				appParamlist += ",";
			}
		}
	}
	//tell flash function flashAlarm to alarm
	thisMovie("flashobj").flashAlarm(appParamlist.substring(0, appParamlist.length-1));
}

function ceaseFlashAlarm() {
	thisMovie("flashobj").stopFlashAlarm();
}

/*artoo and changefree*/
function loadArtooChangeFreeData() {
	if (document.getElementById("hasalarm") != null) {
		if ( document.getElementById("changefree") == null || document.getElementById("artoo") == null) {
			$.get('./get_artoo_changefree_info.jsp',
				{	
					appId:$("#appId").text(),
					startTime:( $("#starttime").length>0 ? $("#starttime").text() : ''),
					endTime:( $("#endtime").length>0 ? $("#endtime").text() : '')
				},
			function(data, textStatus) { 
				if (data.indexOf('artoo') > 0 || data.indexOf('changefree') > 0) { 
					$('#datachanged').html(data);
					$('#datachanged').fadeIn('fast');
				}
			},
			'html');
		} else {
			$.get('./get_artoo_changefree_info.jsp',
				{	
					appId:$("#appId").text(),
					startTime:( $("#starttime").length>0 ? $("#starttime").text() : ''),
					endTime:( $("#endtime").length>0 ? $("#endtime").text() : '')
				},
			function(data, textStatus) { 
				$('#datachanged').html(data);
			},
			'html');
		}
	} else {
		if ( document.getElementById("changefree") != null || document.getElementById("artoo") != null){
			$('#datachanged').fadeOut('fast');
		}
	}
}

function hideArtooAndChangefree(obj){
	$(obj).css('display', 'none');
	$('#datachanged').animate({	right:'-612px' }, 700);
	$('#datashow').fadeIn('fast');
}

function showArtooAndChangefree(obj){
	$(obj).fadeOut('fast');
	$('#datachanged').animate({	right:'10px' }, 700);
	$('#datahide').fadeIn('fast');
}

function getTairKeyDesc(app, key, obj){
	$.get('./get_tair_desc.jsp',
	{	
		appName:app,
		keyName:key 
	},
	function(data, textStatus) { 
		$("#tairinfo").html(data);
		var x = $(obj).offset().top-24;
		var y = $(obj).offset().left-100;
		$("#tairinfo").css({"top":x,"left":y});
		$("#tairinfo").fadeIn("fast");
	},
	'html');
}

function closeKeyDesc(){
	$("#tairinfo").fadeOut("fast");
}

function showEarlyTime(){
	$.get('./get_earlytime.jsp',
		function(data) { 
			$("#etimetip").html(data);
			if($("#etimetip").css('display') == 'none'){
				$("#etimetip").fadeIn('fast');
			}
		},
	'html');
}