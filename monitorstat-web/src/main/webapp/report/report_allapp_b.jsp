<%@page import="com.taobao.monitor.common.ao.capacity.CspLoadRunAo"%>
<%@page import="com.taobao.monitor.common.po.HostPo"%>
<%@page import="com.taobao.monitor.common.util.CspCacheTBHostInfos"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.baseline.db.po.KeyValueBaseLinePo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.text.SimpleDateFormat"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="cache, must-revalidate">
<title>应用监控系统-日报首页</title>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>


</head>
<body>
<%

String t = "关系平台,communitypushclient,华黎#关系平台,communitypushlvs,华黎#关系平台,communitypushngx,华黎#关系平台,matrixac,华黎#关系平台,matrixfa,华黎#关系平台,matrixfc,华黎#关系平台,matrixsubsoil,华黎#关系平台,matrixuc,华黎#关系平台,matrixwwc,华黎#关系平台,matrixwwporter,华黎#关系平台,snsdispatcher,华黎#关系平台,snsgateway,华黎#关系平台,snsusc,华黎#互动应用,communitypsc,华黎#互动应用,grouplus,华黎#互动应用,matrixalbum,华黎#互动应用,matrixblog,华黎#互动应用,matrixtaodan,华黎#互动应用,matrixtiao,华黎#基础平台,communitycount,华黎#基础平台,dianping,华黎#基础平台,dianpingclient,华黎#基础平台,matrixapps,华黎#基础平台,matrixcc,华黎#基础平台,matrixcrust,华黎#基础平台,mytaobao,华黎#基础平台,snsgalaxy,华黎#基础平台,snsmessage,华黎#基础平台,snsmisc,华黎#基础平台,snstaoshare,华黎#基础平台,vmcommon,华黎#卖家服务,communityuis,华黎#卖家服务,ishopbook,华黎#卖家服务,matrixexchange,华黎#卖家服务,matrixgapp,华黎#卖家服务,matrixmission,华黎#卖家服务,matrixpartner,华黎#卖家服务,membersubcenter,华黎#卖家服务,membersubmanager,华黎#卖家服务,snspush,华黎#三方应用,realsteel,华黎#三方应用,starcore,华黎#三方应用,stargate,华黎#三方应用,starsdk,华黎#社区架构,matrixmobile,华黎#淘礼,gift,华黎#淘礼,giftapp,华黎#淘礼,giftpool,华黎#淘礼,mywish,华黎#淘礼,qianliapp,华黎#淘礼,songliewm,华黎#淘礼,taobaogift,华黎#淘礼,taogiftsongli,华黎#终搜,terminator_search,华黎#终搜,terminatordump,华黎#交易核心,arc_ratemgr,范禹#交易核心,buybeta,范禹#交易核心,honey,范禹#交易核心,htcbuyer,范禹#交易核心,htcseller,范禹#交易核心,rateadmin,范禹#交易核心,ratecenter,范禹#交易核心,ratedemo,范禹#交易核心,ratedemosearch,范禹#交易核心,rategateway,范禹#交易核心,ratemanager,范禹#交易核心,ratesearch,范禹#交易核心,sharereport,范禹#交易核心,tccacheadmin,范禹#交易核心,tccheck,范禹#交易核心,tctimetask,范禹#交易核心,tf_buy,范禹#交易核心,tf_tm,范禹#交易核心,timeoutcenter,范禹#交易核心,tradeapi,范禹#交易核心,tradecomb,范禹#交易核心,tradeface,范禹#交易核心,tradelogs,范禹#交易核心,trademanagerbeta,范禹#交易核心,tradeplatform,范禹#交易核心,tradeplatformbeta,范禹#交易核心,traderecord,范禹#交易流程,cart,范禹#交易流程,cartbeta,范禹#交易流程,carttask,范禹#交易流程,eticket,范禹#交易流程,givenchy,范禹#交易流程,ma,范禹#交易流程,maadmin,范禹#交易流程,macenter,范禹#交易流程,refund,范禹#交易流程,seckilllog,范禹#交易流程,softwarecenter,范禹#交易流程,softwaredadmin,范禹#交易流程,softwarestore,范禹#交易支撑,auctionplatform,范禹#交易支撑,betaapache,范禹#交易支撑,call,范禹#交易支撑,cpgw,范禹#交易支撑,dmc,范禹#交易支撑,feeler,范禹#交易支撑,govauction,范禹#交易支撑,htm,范禹#交易支撑,htmbeta,范禹#交易支撑,idle,范禹#交易支撑,idleapi,范禹#交易支撑,idlesell,范禹#交易支撑,idletrade,范禹#交易支撑,misccenter,范禹#交易支撑,tcc,范禹#交易支撑,tradeadmin,范禹#交易支撑,tradegateway,范禹#交易支撑,tradehive,范禹#交易支撑,traderule,范禹#交易支撑,tradesecurity,范禹#分销,boplibraadmin,范遥  #分销,boplibracenter,范遥  #分销,boplibramanager,范遥  #分销,dpc,范遥  #分销,dpcgateway,范遥  #分销,dpeye,范遥  #分销,dpm,范遥  #分销,dpsearch,范遥  #工具后台,hbaseextend,范遥  #工具后台,mmp,范遥  #工具后台,psi,范遥  #工具后台,psi2,范遥  #卖家工具,ecrm,范遥  #卖家工具,jtbas,范遥  #卖家工具,tbac,范遥  #卖家工具,tbas,范遥  #卖家工具,tbas5item,范遥  #卖家工具,tbas5ls,范遥  #卖家工具,tbas5rs,范遥  #卖家管理,scg,范遥  #卖家管理,udc,范遥  #卖家管理,xfile,范遥  #卖家中心,mcdull,范遥  #卖家中心,sellermsgcenter,范遥  #卖家中心,sportal,范遥  #卖家中心,sportalapps,范遥  #商户营销,ecrmgateway,范遥  #商户营销,mealdetail,范遥  #商户营销,pamirspromotion,范遥  #商户营销,promotioncenter,范遥  #商户营销,promotionsearch,范遥  #商户营销,search4ecrm,范遥  #商户营销,umf,范遥  #商户营销,ump,范遥  #商户营销,umpbeta,范遥  #数据平台服务,dataplatform,范遥  #数据平台服务,udp,范遥  #数据平台服务,udpgateway,范遥  #图片中心,cassandra4picture,范遥  #图片中心,mediacenter,范遥  #图片中心,mediagateway,范遥  #图片中心,picturecenter,范遥  #图片中心,picturespider,范遥  #图片中心,tadget,范遥  #物流,commonway,范遥  #物流,consign,范遥  #物流,delivery,范遥  #物流,deliverydump,范遥  #物流,logisticsanalyst,范遥  #物流,logisticscenter,范遥  #物流,logisticsmap,范遥  #物流,logisticsops,范遥  #物流,logisticspartner,范遥  #物流,logisticsspider,范遥  #物流,logisticssyncserver,范遥  #物流,mapsearch,范遥  #物流,wuliuops,范遥  #论坛及后台,bbs,华黎#论坛及后台,dianpingsearch,华黎#论坛及后台,infosearch,华黎#论坛及后台,matrix_ring,华黎#论坛及后台,portaluc,华黎#论坛及后台,realtime_yunti_gateway,华黎#论坛及后台,shopmap,华黎#淘女郎&画报/试用及新品,aicms,华黎#淘女郎&画报/试用及新品,ha2client,华黎#淘女郎&画报/试用及新品,matrixtry,华黎#淘女郎&画报/试用及新品,matrixtryadmin,华黎#淘女郎&画报/试用及新品,newstar,华黎#淘女郎&画报/试用及新品,pix,华黎#淘女郎&画报/试用及新品,poster,华黎#淘女郎&画报/试用及新品,qing,华黎#淘女郎&画报/试用及新品,smh_gift_app,华黎#淘女郎&画报/试用及新品,toyopen,华黎#淘女郎&画报/试用及新品,tstar,华黎#淘女郎&画报/试用及新品,tstarboss,华黎#营销产品,tejiaadmin,华黎#营销产品,tejiaweb,华黎#vsearch,issimporter,范禹#vsearch,isssearch,范禹#vsearch,issstore,范禹#vsearch,kaleidoscope,范禹#vsearch,vsearch,范禹#vsearch,vstore,范禹#vsearch,zookeeper,范禹#本地生活,lifecommon,范禹#本地生活,lifeforesee,范禹#本地生活,lifeios,范禹#本地生活,lifekoubei,范禹#本地生活,lifemarket,范禹#本地生活,lifemy,范禹#本地生活,lifeplace,范禹#本地生活,lifeplaceadmin,范禹#本地生活,lifeplacelistweb,范禹#本地生活,lifeplacereviewdata,范禹#本地生活,lifeplacesearch,范禹#本地生活,lifeplaceweb,范禹#本地生活,lifereview,范禹#本地生活,lifeuser,范禹#本地生活,lifewireless,范禹#本地生活,mapserver,范禹#本地生活,mylifecenter,范禹#本地生活,ticket,范禹#房产市场,fang,范禹#房产市场,fangintra,范禹#公用组件,bp,范禹#公用组件,brandservice,范禹#公用组件,guess,范禹#公用组件,lifemap,范禹#公用组件,paihangbanggateway,范禹#公用组件,qss,范禹#家居市场,jiaju,范禹#家居市场,jiajucenter,范禹#家居市场,riji,范禹#家居市场,rijicenter,范禹#女装市场,ptd_dev_gateway,范禹#女装市场,sizesearch,范禹#女装市场,vmarket,范禹#食品市场,food,范禹#食品市场,foodinfo,范禹#数码市场,shuma,范禹#团购市场,localtuan,范禹#团购市场,tuanbuyer,范禹#团购市场,tuanseller,范禹#团购市场,tuanvsearch,范禹#外卖市场,life,范禹#外卖市场,takeout,范禹#网游市场,vic,范禹#网游市场,vist,范禹#游书园,smh_youshuyuan_aisearch,范禹#游书园,smh_youshuyuan_center,范禹#游书园,smh_youshuyuan_pdisearch,范禹#游书园,smh_youshuyuan_web,范禹#TMS,rms,范禹#TMS,tms,范禹#尺码库 ,mysize,范禹#导购平台,dgdcompass,范禹#导购平台,dgplatform,范禹#导购平台,gmall,范禹#导购平台,switchcenter,范禹#花鸟市场 ,hirdog,范禹#美容市场 ,meirong,范禹#美容市场 ,meirongadmin,范禹#美容市场 ,meirongsearch,范禹#母婴市场 ,babymarket,范禹#商品前台,hesper,范禹#商品前台,hesperbeta,范禹#商品前台,nginxskip,范禹#商品前台,tbskip,范禹#商品详情,cachechecker,范禹#商品详情,cooly,范禹#商品详情,detail,范禹#商品详情,detailbeta,范禹#商品详情,detailskip,范禹#商品详情,outside,范禹#淘宝前端性能监控,speed,范禹#我要买&店铺街,dgdump,范禹#我要买&店铺街,dgsearch,范禹#我要买&店铺街,shopstreet,范禹#我要买&店铺街,shopstreetsearch,范禹#商品发布 ,sell,范禹#商品发布 ,sellbeta,范禹#商品管理 ,cmpcitadel,范禹#商品管理 ,cmpgateway,范禹#商品管理 ,cmploom,范禹#商品管理 ,cmpmatrix,范禹#商品管理 ,cmpsailor,范禹#商品管理 ,cmpscoop,范禹#商品管理 ,commoditydog,范禹#商品管理 ,justice,范禹#商品中心,itemcenter,范禹#商品中心,itemcenterschedule,范禹#商品中心,itemtools,范禹#商品中心,tagcenter,范禹#运营平台 ,catserver,范禹#运营平台 ,cmpenigma,范禹#运营平台 ,forest,范禹#运营平台 ,pamirsmckinley,范禹#运营平台 ,spucenter,范禹#运营平台 ,spueditor,范禹#关联推荐 ,picanalyze,范禹#关联推荐 ,relationadmin,范禹#关联推荐 ,relationrecommend,范禹#关联推荐 ,relationrecommendtest,范禹#试衣间 ,magicroom,范禹#收藏夹 ,mercury,范禹#数据应用 ,footprint,范禹#数据应用 ,hermes,范禹#数据应用 ,pricesystem,范禹#数据应用 ,treasure,范禹#数据应用 ,zeus,范禹#ConfigServer,basestone,华黎#ConfigServer,configserver,华黎#ConfigServer,configserver1,华黎#ConfigServer,configserverops,华黎#ConfigServer,configserverqd,华黎#ConfigServer,diamond,华黎#ConfigServer,diamondops,华黎#ConfigServer,diamondopsqd,华黎#ConfigServer,diamondqd,华黎#ConfigServer,diamondstore,华黎#ConfigServer,pushit,华黎#ConfigServer,pushitqd,华黎#ConfigServer,taokeeper,华黎#ConfigServer,taokeeperops,华黎#ConfigServer,taokeeperopsqd,华黎#ConfigServer,taokeeperqd,华黎#EagleEye,eagleeye,华黎#HSF,bundledownserver,华黎#HSF,dapper,华黎#HSF,hsf,华黎#HSF,hsfops,华黎#HSF,tlogconsole,华黎#HSF,wsproxy,华黎#Lake数据库层,dbaccesslayer,华黎#Meta消息中间件,db_metaqd,华黎#Meta消息中间件,mix_meta,华黎#Meta消息中间件,sync_meta,华黎#Notify讯息件,alipay_notify,华黎#Notify讯息件,backup_notify,华黎#Notify讯息件,delaynotify,华黎#Notify讯息件,file_notify,华黎#Notify讯息件,mix_notify,华黎#Notify讯息件,nconsole,华黎#Notify讯息件,nsearch,华黎#Notify讯息件,trade_notify,华黎#Notify讯息件,trade_notifyqd,华黎#Notify讯息件,trade_sub_notify,华黎#Notify讯息件,trade_sub_notifyqd,华黎#Notify讯息件,trade_sub2_notify,华黎#Notify讯息件,transmitter_notify,华黎#Web旺旺,mpp,华黎#Web旺旺,mpphaproxyin,华黎#Web旺旺,mpphaproxyout,华黎#Web旺旺,mpplvs,华黎#Web旺旺,qin,华黎#Web旺旺,webwangwang,华黎#Web旺旺,webwangwanglvs,华黎#Web旺旺,webwwadmin,华黎#Web旺旺,webwwhaproxy,华黎#独立域名跳转系统,tsu,华黎#分布式数据层,andor,华黎#分布式数据层,dayu,华黎#分布式数据层,jingwei,华黎#分布式数据层,jingwei_ic,华黎#分布式数据层,mid_panalyser,华黎#分布式数据层,tddl,华黎#分布式数据层,tddlsyncserver,华黎#分布式数据层,venusreplicator,华黎#分布式数据层,yugong,华黎#核心系统监控,autodeploy,华黎#核心系统监控,coremonitor,华黎#核心系统监控,coremonitorqd,华黎#核心系统监控,coreperf,华黎#核心系统监控,hotspot,华黎#容灾配置推送,ops_security,华黎#容灾配置推送,ops_securityqd,华黎#容灾配置推送,rtools,华黎#容灾配置推送,rtoolsqd,华黎#异地机房相关服务,tgc,华黎#应用服务器,artoo,华黎#应用服务器,depcenter,华黎#应用服务器,tbcontainer,华黎#终搜,baoming_terminator,华黎#终搜,s4addressfriend,华黎#终搜,s4album,华黎#终搜,s4ark,华黎#终搜,s4arkisv,华黎#终搜,s4baoming,华黎#终搜,s4bbcproduct,华黎#终搜,s4bbcsupplier,华黎#终搜,s4bbs,华黎#终搜,s4blog,华黎#终搜,s4cms,华黎#终搜,s4comment,华黎#终搜,s4company,华黎#终搜,s4diary,华黎#终搜,s4elogistics,华黎#终搜,s4friend,华黎#终搜,s4galaxy,华黎#终搜,s4group,华黎#终搜,s4grouplus,华黎#终搜,s4ipfriend,华黎#终搜,s4jobonline,华黎#终搜,s4juactionlog,华黎#终搜,s4jucheckitem,华黎#终搜,s4judeposititem,华黎#终搜,s4juitemonline,华黎#终搜,s4juke,华黎#终搜,s4jukeseller,华黎#终搜,s4jusellerauth,华黎#终搜,s4jwy,华黎#终搜,s4magicroom,华黎#终搜,s4matrixtry,华黎#终搜,s4newipfriend,华黎#终搜,s4phone,华黎#终搜,s4realwidget,华黎#终搜,s4resumeonline,华黎#终搜,s4tag,华黎#终搜,s4tmshistory,华黎#终搜,s4tmspage,华黎#终搜,s4tstar,华黎#终搜,s4tstaractivity,华黎#终搜,terminatorconsole,华黎#终搜,terminatorzk,华黎#旺铺,clouddrive,范禹#旺铺,clouddrivepush,范禹#旺铺,designaudit,范禹#旺铺,designcenter,范禹#旺铺,designcenterbeta,范禹#旺铺,designmarket,范禹#旺铺,designsearch,范禹#旺铺,designsystem,范禹#旺铺,designsystembeta,范禹#旺铺,imagecapture,范禹#旺铺,pamirsshop,范禹#旺铺,rendersystem,范禹#旺铺,sdk,范禹#旺铺,shopabtest,范禹#旺铺,shopcenter,范禹#旺铺,shophadoop,范禹#旺铺,shopmanager,范禹#旺铺,shopmonitor,范禹#旺铺,shopreport,范禹#旺铺,shopsystem,范禹#旺铺,taeadmin,范禹#旺铺,taeappcenter,范禹#旺铺,taecontainer,范禹#旺铺,taecron,范禹#旺铺,taedevadmin,范禹#旺铺,taegrid,范禹#旺铺,taemarket,范禹#旺铺,taequeue,范禹#旺铺,taesite,范禹#旺铺,taesitecenter,范禹#旺铺,taesiteentrance,范禹#旺铺,taestorm,范禹#二次验证,aq,华黎#二次验证,durex,华黎#二次验证,fandiaoyupindao,华黎#二次验证,global,华黎#二次验证,isv_dianzisuo,华黎#监控,namelist,华黎#监控,pamirslog,华黎#监控,ruleconfig,华黎#网安安全,aliws,华黎#网安安全,archer,华黎#网安安全,ctupf,华黎#网安安全,dsp,华黎#网安安全,gaara,华黎#网安安全,kfc,华黎#网安安全,libra,华黎#网安安全,monitorclient,华黎#网安安全,pamirs_hecla,华黎#网安安全,pfp,华黎#网安安全,police,华黎#网安安全,pomas,华黎#网安安全,punishcenter,华黎#网安安全,rulerun,华黎#网安安全,sdc,华黎#网安安全,tbctu,华黎#网安安全,tbosshive,华黎#网安安全,tee,华黎#网安安全,themis,华黎#网安安全,wwgateway,华黎#网安安全,xu,华黎#TSP,servicemanager,华黎#VOC,beijixing,华黎#VOC,biapp,华黎#VOC,dunhuang,华黎#VOC,panama,华黎#VOC,tqms,华黎#服务中心,logsystem,华黎#服务中心,onlinecs,华黎#服务中心,pamirsminerva,华黎#服务中心,pamirssupport,华黎#服务中心,siderite,华黎#服务中心,support,华黎#核心服务,buyermodule,华黎#核心服务,ctp,华黎#核心服务,pamirsbogda,华黎#核心服务,pamirsshennong,华黎#核心服务,pamirssmartscript,华黎#核心服务,shennong,华黎#核心服务,smartscript,华黎#维权平台,azeroth,华黎#维权平台,capitalcenter,华黎#维权平台,gandalf,华黎#维权平台,serviceone,华黎#线下渠道平台,channelpoint,华黎#线下渠道平台,kalimdor,华黎#";

Map<String,String[]> mapT = new HashMap<String,String[]>();
String[] tmp = t.split("#");
for(String m:tmp){
	String[] g = m.split(",");
	mapT.put(g[1], g);
}



List<AppInfoPo> appList = AppInfoAo.get().findAllDayApp();

%>

<%//yyyy-MM-dd
String searchDate = request.getParameter("searchDate");
if(searchDate==null){
	searchDate = Utlitites.getMonitorDate();
}
String tongbiDate = Utlitites.getTongBiMonitorDate(searchDate);
String huanbiDate = Utlitites.getHuanBiMonitorDate(searchDate);
Map<Integer, MonitorVo> map = MonitorDayAo.get().findMonitorCountMapByDate(searchDate);

%>
<table style="width:100%" border="1" cellspacing="0" cellpadding="0" bordercolor="#4F81BD">
<thead>
	<tr>
		<td>应用名称</td>
		<td>中文名</td>
		<td>所属团队</td>
		<td>PV</td>
		<td>QPS</td>
		<td>RT</td>
		<td>CPU</td>
		<td>IOWAIT</td>
		<td>Load</td>
		<td>Mem</td>
		<td>Swap</td>
		<td>NetWorkIO</td>
		<td>DiskIO</td>
		<td>系统余量</td>
		<td>机器分布</td>
		<td>是否实体机</td>
	</tr>
</thead>
<tbody>
<%
List<AppInfoPo> daylist = AppInfoAo.get().findAllDayApp();
for(AppInfoPo po:daylist){
	try{
		String[] msg = mapT.get(po.getAppName());
		if(msg == null){
			continue;
		}
	double  maxqps = CspLoadRunAo.get().findRecentlyAppLoadRunQps(po.getAppId());
	
	
	
	
	MonitorVo vo = map.get(po.getAppDayId());
	if(vo == null){
		continue;
	}
	
	String qps ="0";
	String rt_v = "";
	String pv = "";
	
	if("PV".equals(po.getAppType().toUpperCase())){
		qps =vo.getApacheQps();
		 rt_v = vo.getApacheRest();
		 pv = vo.getApachePv();
	}else{
		 qps =vo.getAllHsfInterfaceQps()+"";
		 rt_v = vo.getAllHsfInterfaceRest()+"";
		 pv = vo.getAllHsfInterfacePv()+"";
	}
	
	if("0".equals(pv)){
		continue;
	}
	
	double groupCapacityRate = Arith.mul(Arith.div(Arith.sub(maxqps, Double.parseDouble(qps)), maxqps, 4), 100);
	
	
	int[]  v = CspCacheTBHostInfos.get().getHostType(po.getAppName());
	Map<String,List<HostPo>> roommap = CspCacheTBHostInfos.get().getHostMapByRoom(po.getAppName());
	String r = "";
	for(Map.Entry<String,List<HostPo>> entry: roommap.entrySet()){
		r += entry.getKey()+":"+entry.getValue().size()+"/";
	}
%>
	<tr>
		<td><%=po.getAppName() %></td>
		<td><%=msg[0] %></td>
		<td><%=msg[2] %></td>
		
		<td><%=Utlitites.fromatLong(pv) %></td>
		<td><%=qps %></td>
		<td><%=rt_v %></td>
		
		<td><%=vo.getCpu() %></td>
		<td><%=vo.getIowait()%></td>
		<td><%=vo.getLoad()%></td>
		<td><%=vo.getMen()  %></td>
		<td><%=vo.getSwap() %></td>
		<td></td>
		<td></td>
		<td><%=groupCapacityRate<=0?"未压测":groupCapacityRate %></td>
		<td><%=r%></td>
		<td><%= ("虚:"+v[0])+("实:"+v[1] )%></td>
	</tr>
<%}catch(Exception e){
	e.printStackTrace();
}} %>
</tbody>
</table>
</body>
</html>