//////////////for js and as
var appNameArray = new Array("Alipay","group3-tair-cm3", "group3-tair-cm4", "item-tair","itemcenter", "icdb_mysqldb","tradelogs","tcmaindb","tcdb", "delivery","logisticscenter", "misccenter","timeoutcenter","pointcenter", "messagecenter","ump","notify","trade_notify","alipay_notify","trade_sub_notify","trade_notifyqd","notify_ha_tcdb","notify_tradedb","logdb", "tocdb", "tcpointdb", "ecarddb", "yunhaodb", "marketingdb","tradeplatform");
/*set app url*/
function openTpLink(){
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=322");
}
function openAliapyLink() {
	window.open("http://cm.taobao.net:9999/monitorstat/alarm/alarm_record.jsp?currentDay=" + $("#endtime").text() + "&appId=493");
}
function openUictairLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=552");
}
function openGpstairLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=552");
}
function openUicfinalLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=21");
}
function openIctairLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=566");
}
function openIcLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=8");
}
function openIcdbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=icdb&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}
function openTradelogsLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=255");
}
function openTcmainLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=tcmain&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}
function openTcdbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=tc&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}
function openDeliveryLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=381");
}
function openLogisticsLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=80");
}
function openMisccenterLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=59");
}
function openTocLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=366");
}
function openPointcenterLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=390");
}
function openMessagecenterLink() {
	alert("MessageCenter未接入CSP系统");
}
function openUmpLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=338");
}
function openNotifyLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=496");
}
function openNotifymasterLink() {
		window.open("http://beidou.corp.taobao.com/mysql/alert.php?search_text=notify_trade&type=all&match=onlyalert_group&leftshow=1&from=" + $("#starttime").text() + "&to=" + $("#endtime").text());
}
function openNotifyslaveLink() {
			window.open("http://beidou.corp.taobao.com/mysql/alert.php?search_text=notify_ha_tc&type=all&match=onlyalert_group&leftshow=1&from=" + $("#starttime").text() + "&to=" + $("#endtime").text());
}
function openNotifyLink() {
	window.open("http://cm.taobao.net:9999/monitorstat/alarm/alarm_record.jsp?currentDay=" + $("#endtime").text() + "&appId=496");
}
function openLogdbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=lg_core&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}
function openTocdbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=toc&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}
function openTcpointdbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=tcpoint&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}
function openEcarddbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=ecard&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}
function openYunhaodbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=mc_gtrd&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}
function openMarketingdbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=mkt&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}