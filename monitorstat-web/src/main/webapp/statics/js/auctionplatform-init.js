//////////////for js and as
var appNameArray = new Array("tradeplatform","group3_tair","tbsession_tair","itemcenter","auctionplatform","govauction","bid","gov_bid","uicfinal","shopcenter","misccenter","punishcenter");

/*set app url*/
function openTpLink() {
    window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=322");
}
function openGptairLink() {
    window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=552");
}
function openTbstairLink() {
    window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=558");
}
function openIcLink() {
    window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=8");
}
function openAuctionLink() {
    window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=364");
}
function openGovauctionLink() {
    window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=544");
}
function openBidLink() {
    window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=bid&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}
function openGovbidLink() {
    window.open("http://beidou.corp.taobao.com/tianji/alert.php?search_text=cart&from="+$("#starttime").text()+"&to="+$("#endtime").text()+"&alerttype=ALL&leftshow=1&submit=submit");
}
function openUicfinalLink() {
    window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=21");
}
function openShopcenterLink() {
    window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=4");
}
function openMisccenterLink() {
    window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=59");
}
function openPunishcenter() {
    window.open("http://time.csp.taobao.net:9999/time/app/detail/show.do?method=showIndex&appId=50");
}