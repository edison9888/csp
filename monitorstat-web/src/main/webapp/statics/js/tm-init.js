//////////////for js and as
var appNameArray = new Array("tradeplatform", "tcmaindb","tcdb","group3-tair-cm3", "group3-tair-cm4", "item-tair", "itemcenter", "icdb_mysqldb","uiclogin", "uic-logindb", "communityuis","wwposthouse", "misccenter","htm","messagecenter", "shopcenter", "snsdb", "ecarddb", "yunhaodb","mcdb","supudb","tf_tm");

/*set app url*/
function openTftmLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=330");
}
function openTpLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=322");
}
function openTcmaindbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=tcmain&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}
function openTcdbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=tc&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}
function openGptairLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=552");
}
function openGpstairLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=552");
}

function openItemtairLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=566");
}
function openIcLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=8");
}
function openIcdbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=icdb&db_role=all&orderby=hostname&leftshow=1");
}
function openUicloginLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=33");
}
function openCommunityuisLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=229");
}
function openPosthouseLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=389");
}
function openMisccenterLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=59");
}
function openHtmLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=374");
}
function openMessagecenterLink() {
	alert("MessageCenter未接入CSP系统");
}
function openShopcenterLink() {
	window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=4");
}

function openUiclogindbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=cart&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}
function openSnsdbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=snscoin&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}
function openEcarddbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=ecard&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}
function openYunhaodbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=mc_gtrd&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}function openMcdbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=misc&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}
function openSupudbLink() {
	window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=tbshop&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}