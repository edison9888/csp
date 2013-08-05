//////////////for js and as
var appNameArray= new Array("configserver","diamond","group3-tair-cm3","group3-tair-cm4","tradeplatform","tcmaindb", "tcdb","tradeapi","cartdb","shopcenter","timeoutcenter","item-tair","itemcenter","icdb_mysqldb", "supudb", "tocdb", "tradeapi");
/*set app url*/
function openDiamondLink() {
	window.open("http://cm.taobao.net:9999/monitorstat/alarm/alarm_record.jsp?currentDay="+$("#endtime").text()+"&appId=468");
}
function openConfigserverLink() {
	window.open("http://cm.taobao.net:9999/monitorstat/alarm/alarm_record.jsp?currentDay="+$("#endtime").text()+"&appId=469");
}
function openGptairLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=552");
}
function openGpstairLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=552");
}
function openTpLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=322");
}
function openTradeapiLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=341");
}
function openTocLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=366");
}
function openShopcenterLink() {
	window.open("http://cm.taobao.net:9999/monitorstat/alarm/alarm_record.jsp?currentDay="+$("#endtime").text()+"&appId=4");
}
function openUmpLink() {
	window.open("http://cm.taobao.net:9999/monitorstat/alarm/alarm_record.jsp?currentDay="+$("#endtime").text()+"&appId=338");
}
function openItemtairLink(){
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=566");
}
function openIclink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=8");
}
function openIcdbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=icdb&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}
function openTcmaindbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=tcmain&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}
function openTcdbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=tc&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}
function openSupudbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=tbshop&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}
function openTocdbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=toc&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}
function openUicLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=21");
}