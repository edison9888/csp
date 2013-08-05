//////////////for js and as
var appNameArray = new Array("Alipay","tcmaindb","tcdb", "tcmiscdb","tradeplatform", "configserver", "diamond","uicfinal", "cart","tf_buy", "tf_tm","cartdb","session-tair","group3-tair-cm3", "group3-tair-cm4","trade_notify","alipay_notify","trade_sub_notify","trade_notifyqd");
/*set app url*/
function openTpLink(){
	window.open("tradeplatform_relate.jsp");
}
function openAliapyLink() {
	window.open("http://cm.taobao.net:9999/monitorstat/alarm/alarm_record.jsp?currentDay=" + $("#endtime").text() + "&appId=493");
}
function openNotifyLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=496");
}
function openTcmainLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=tcmain&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}
function openTcdbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=tc&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}
function openTcmiscLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=tcmisc&db_role=master&orderby=hostname&leftshow=1");
}
function openConfigserverLink() {
	window.open("http://cm.taobao.net:9999/monitorstat/alarm/alarm_record.jsp?currentDay=" + $("#endtime").text() + "&appId=469");
}
function openDiamondLink() {
	window.open("http://cm.taobao.net:9999/monitorstat/alarm/alarm_record.jsp?currentDay=" + $("#endtime").text() + "&appId=468");
}
function openUicLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=21");
}

function openCartLink() {
	window.open("cart_relate.jsp");
}
function openTfbuyLink() {
	window.open("tf_buy_relate.jsp");
}
function openTftmLink() {
	window.open("tf_tm_relate.jsp");
}
function openCartdbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=cart&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}
function openSessiontairLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=558");
}
function openGptairLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=552");
}
function openGpstairLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=552");
}