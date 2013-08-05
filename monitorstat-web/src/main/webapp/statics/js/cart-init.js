//////////////for js and as
var appNameArray= new Array("configserver","diamond","group3-tair-cm3","group3-tair-cm4","tradeplatform","tcmaindb", "tcdb","cart","cartdb","shopcenter","ump","item-tair","itemcenter","icdb_mysqldb", "supudb", "marketingdb");
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
function openCartLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=341");
}
function openCartdbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=cart&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
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
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=tc&db_role=master&orderby=hostname&leftshow=1");
}
function openSupudbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=tbshop&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}
function openMarketingdbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=mkt&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}