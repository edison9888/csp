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
<title>Ӧ�ü��ϵͳ-�ձ���ҳ</title>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>


</head>
<body>
<%

String t = "��ϵƽ̨,communitypushclient,����#��ϵƽ̨,communitypushlvs,����#��ϵƽ̨,communitypushngx,����#��ϵƽ̨,matrixac,����#��ϵƽ̨,matrixfa,����#��ϵƽ̨,matrixfc,����#��ϵƽ̨,matrixsubsoil,����#��ϵƽ̨,matrixuc,����#��ϵƽ̨,matrixwwc,����#��ϵƽ̨,matrixwwporter,����#��ϵƽ̨,snsdispatcher,����#��ϵƽ̨,snsgateway,����#��ϵƽ̨,snsusc,����#����Ӧ��,communitypsc,����#����Ӧ��,grouplus,����#����Ӧ��,matrixalbum,����#����Ӧ��,matrixblog,����#����Ӧ��,matrixtaodan,����#����Ӧ��,matrixtiao,����#����ƽ̨,communitycount,����#����ƽ̨,dianping,����#����ƽ̨,dianpingclient,����#����ƽ̨,matrixapps,����#����ƽ̨,matrixcc,����#����ƽ̨,matrixcrust,����#����ƽ̨,mytaobao,����#����ƽ̨,snsgalaxy,����#����ƽ̨,snsmessage,����#����ƽ̨,snsmisc,����#����ƽ̨,snstaoshare,����#����ƽ̨,vmcommon,����#���ҷ���,communityuis,����#���ҷ���,ishopbook,����#���ҷ���,matrixexchange,����#���ҷ���,matrixgapp,����#���ҷ���,matrixmission,����#���ҷ���,matrixpartner,����#���ҷ���,membersubcenter,����#���ҷ���,membersubmanager,����#���ҷ���,snspush,����#����Ӧ��,realsteel,����#����Ӧ��,starcore,����#����Ӧ��,stargate,����#����Ӧ��,starsdk,����#�����ܹ�,matrixmobile,����#����,gift,����#����,giftapp,����#����,giftpool,����#����,mywish,����#����,qianliapp,����#����,songliewm,����#����,taobaogift,����#����,taogiftsongli,����#����,terminator_search,����#����,terminatordump,����#���׺���,arc_ratemgr,����#���׺���,buybeta,����#���׺���,honey,����#���׺���,htcbuyer,����#���׺���,htcseller,����#���׺���,rateadmin,����#���׺���,ratecenter,����#���׺���,ratedemo,����#���׺���,ratedemosearch,����#���׺���,rategateway,����#���׺���,ratemanager,����#���׺���,ratesearch,����#���׺���,sharereport,����#���׺���,tccacheadmin,����#���׺���,tccheck,����#���׺���,tctimetask,����#���׺���,tf_buy,����#���׺���,tf_tm,����#���׺���,timeoutcenter,����#���׺���,tradeapi,����#���׺���,tradecomb,����#���׺���,tradeface,����#���׺���,tradelogs,����#���׺���,trademanagerbeta,����#���׺���,tradeplatform,����#���׺���,tradeplatformbeta,����#���׺���,traderecord,����#��������,cart,����#��������,cartbeta,����#��������,carttask,����#��������,eticket,����#��������,givenchy,����#��������,ma,����#��������,maadmin,����#��������,macenter,����#��������,refund,����#��������,seckilllog,����#��������,softwarecenter,����#��������,softwaredadmin,����#��������,softwarestore,����#����֧��,auctionplatform,����#����֧��,betaapache,����#����֧��,call,����#����֧��,cpgw,����#����֧��,dmc,����#����֧��,feeler,����#����֧��,govauction,����#����֧��,htm,����#����֧��,htmbeta,����#����֧��,idle,����#����֧��,idleapi,����#����֧��,idlesell,����#����֧��,idletrade,����#����֧��,misccenter,����#����֧��,tcc,����#����֧��,tradeadmin,����#����֧��,tradegateway,����#����֧��,tradehive,����#����֧��,traderule,����#����֧��,tradesecurity,����#����,boplibraadmin,��ң  #����,boplibracenter,��ң  #����,boplibramanager,��ң  #����,dpc,��ң  #����,dpcgateway,��ң  #����,dpeye,��ң  #����,dpm,��ң  #����,dpsearch,��ң  #���ߺ�̨,hbaseextend,��ң  #���ߺ�̨,mmp,��ң  #���ߺ�̨,psi,��ң  #���ߺ�̨,psi2,��ң  #���ҹ���,ecrm,��ң  #���ҹ���,jtbas,��ң  #���ҹ���,tbac,��ң  #���ҹ���,tbas,��ң  #���ҹ���,tbas5item,��ң  #���ҹ���,tbas5ls,��ң  #���ҹ���,tbas5rs,��ң  #���ҹ���,scg,��ң  #���ҹ���,udc,��ң  #���ҹ���,xfile,��ң  #��������,mcdull,��ң  #��������,sellermsgcenter,��ң  #��������,sportal,��ң  #��������,sportalapps,��ң  #�̻�Ӫ��,ecrmgateway,��ң  #�̻�Ӫ��,mealdetail,��ң  #�̻�Ӫ��,pamirspromotion,��ң  #�̻�Ӫ��,promotioncenter,��ң  #�̻�Ӫ��,promotionsearch,��ң  #�̻�Ӫ��,search4ecrm,��ң  #�̻�Ӫ��,umf,��ң  #�̻�Ӫ��,ump,��ң  #�̻�Ӫ��,umpbeta,��ң  #����ƽ̨����,dataplatform,��ң  #����ƽ̨����,udp,��ң  #����ƽ̨����,udpgateway,��ң  #ͼƬ����,cassandra4picture,��ң  #ͼƬ����,mediacenter,��ң  #ͼƬ����,mediagateway,��ң  #ͼƬ����,picturecenter,��ң  #ͼƬ����,picturespider,��ң  #ͼƬ����,tadget,��ң  #����,commonway,��ң  #����,consign,��ң  #����,delivery,��ң  #����,deliverydump,��ң  #����,logisticsanalyst,��ң  #����,logisticscenter,��ң  #����,logisticsmap,��ң  #����,logisticsops,��ң  #����,logisticspartner,��ң  #����,logisticsspider,��ң  #����,logisticssyncserver,��ң  #����,mapsearch,��ң  #����,wuliuops,��ң  #��̳����̨,bbs,����#��̳����̨,dianpingsearch,����#��̳����̨,infosearch,����#��̳����̨,matrix_ring,����#��̳����̨,portaluc,����#��̳����̨,realtime_yunti_gateway,����#��̳����̨,shopmap,����#��Ů��&����/���ü���Ʒ,aicms,����#��Ů��&����/���ü���Ʒ,ha2client,����#��Ů��&����/���ü���Ʒ,matrixtry,����#��Ů��&����/���ü���Ʒ,matrixtryadmin,����#��Ů��&����/���ü���Ʒ,newstar,����#��Ů��&����/���ü���Ʒ,pix,����#��Ů��&����/���ü���Ʒ,poster,����#��Ů��&����/���ü���Ʒ,qing,����#��Ů��&����/���ü���Ʒ,smh_gift_app,����#��Ů��&����/���ü���Ʒ,toyopen,����#��Ů��&����/���ü���Ʒ,tstar,����#��Ů��&����/���ü���Ʒ,tstarboss,����#Ӫ����Ʒ,tejiaadmin,����#Ӫ����Ʒ,tejiaweb,����#vsearch,issimporter,����#vsearch,isssearch,����#vsearch,issstore,����#vsearch,kaleidoscope,����#vsearch,vsearch,����#vsearch,vstore,����#vsearch,zookeeper,����#��������,lifecommon,����#��������,lifeforesee,����#��������,lifeios,����#��������,lifekoubei,����#��������,lifemarket,����#��������,lifemy,����#��������,lifeplace,����#��������,lifeplaceadmin,����#��������,lifeplacelistweb,����#��������,lifeplacereviewdata,����#��������,lifeplacesearch,����#��������,lifeplaceweb,����#��������,lifereview,����#��������,lifeuser,����#��������,lifewireless,����#��������,mapserver,����#��������,mylifecenter,����#��������,ticket,����#�����г�,fang,����#�����г�,fangintra,����#�������,bp,����#�������,brandservice,����#�������,guess,����#�������,lifemap,����#�������,paihangbanggateway,����#�������,qss,����#�Ҿ��г�,jiaju,����#�Ҿ��г�,jiajucenter,����#�Ҿ��г�,riji,����#�Ҿ��г�,rijicenter,����#Ůװ�г�,ptd_dev_gateway,����#Ůװ�г�,sizesearch,����#Ůװ�г�,vmarket,����#ʳƷ�г�,food,����#ʳƷ�г�,foodinfo,����#�����г�,shuma,����#�Ź��г�,localtuan,����#�Ź��г�,tuanbuyer,����#�Ź��г�,tuanseller,����#�Ź��г�,tuanvsearch,����#�����г�,life,����#�����г�,takeout,����#�����г�,vic,����#�����г�,vist,����#����԰,smh_youshuyuan_aisearch,����#����԰,smh_youshuyuan_center,����#����԰,smh_youshuyuan_pdisearch,����#����԰,smh_youshuyuan_web,����#TMS,rms,����#TMS,tms,����#����� ,mysize,����#����ƽ̨,dgdcompass,����#����ƽ̨,dgplatform,����#����ƽ̨,gmall,����#����ƽ̨,switchcenter,����#�����г� ,hirdog,����#�����г� ,meirong,����#�����г� ,meirongadmin,����#�����г� ,meirongsearch,����#ĸӤ�г� ,babymarket,����#��Ʒǰ̨,hesper,����#��Ʒǰ̨,hesperbeta,����#��Ʒǰ̨,nginxskip,����#��Ʒǰ̨,tbskip,����#��Ʒ����,cachechecker,����#��Ʒ����,cooly,����#��Ʒ����,detail,����#��Ʒ����,detailbeta,����#��Ʒ����,detailskip,����#��Ʒ����,outside,����#�Ա�ǰ�����ܼ��,speed,����#��Ҫ��&���̽�,dgdump,����#��Ҫ��&���̽�,dgsearch,����#��Ҫ��&���̽�,shopstreet,����#��Ҫ��&���̽�,shopstreetsearch,����#��Ʒ���� ,sell,����#��Ʒ���� ,sellbeta,����#��Ʒ���� ,cmpcitadel,����#��Ʒ���� ,cmpgateway,����#��Ʒ���� ,cmploom,����#��Ʒ���� ,cmpmatrix,����#��Ʒ���� ,cmpsailor,����#��Ʒ���� ,cmpscoop,����#��Ʒ���� ,commoditydog,����#��Ʒ���� ,justice,����#��Ʒ����,itemcenter,����#��Ʒ����,itemcenterschedule,����#��Ʒ����,itemtools,����#��Ʒ����,tagcenter,����#��Ӫƽ̨ ,catserver,����#��Ӫƽ̨ ,cmpenigma,����#��Ӫƽ̨ ,forest,����#��Ӫƽ̨ ,pamirsmckinley,����#��Ӫƽ̨ ,spucenter,����#��Ӫƽ̨ ,spueditor,����#�����Ƽ� ,picanalyze,����#�����Ƽ� ,relationadmin,����#�����Ƽ� ,relationrecommend,����#�����Ƽ� ,relationrecommendtest,����#���¼� ,magicroom,����#�ղؼ� ,mercury,����#����Ӧ�� ,footprint,����#����Ӧ�� ,hermes,����#����Ӧ�� ,pricesystem,����#����Ӧ�� ,treasure,����#����Ӧ�� ,zeus,����#ConfigServer,basestone,����#ConfigServer,configserver,����#ConfigServer,configserver1,����#ConfigServer,configserverops,����#ConfigServer,configserverqd,����#ConfigServer,diamond,����#ConfigServer,diamondops,����#ConfigServer,diamondopsqd,����#ConfigServer,diamondqd,����#ConfigServer,diamondstore,����#ConfigServer,pushit,����#ConfigServer,pushitqd,����#ConfigServer,taokeeper,����#ConfigServer,taokeeperops,����#ConfigServer,taokeeperopsqd,����#ConfigServer,taokeeperqd,����#EagleEye,eagleeye,����#HSF,bundledownserver,����#HSF,dapper,����#HSF,hsf,����#HSF,hsfops,����#HSF,tlogconsole,����#HSF,wsproxy,����#Lake���ݿ��,dbaccesslayer,����#Meta��Ϣ�м��,db_metaqd,����#Meta��Ϣ�м��,mix_meta,����#Meta��Ϣ�м��,sync_meta,����#NotifyѶϢ��,alipay_notify,����#NotifyѶϢ��,backup_notify,����#NotifyѶϢ��,delaynotify,����#NotifyѶϢ��,file_notify,����#NotifyѶϢ��,mix_notify,����#NotifyѶϢ��,nconsole,����#NotifyѶϢ��,nsearch,����#NotifyѶϢ��,trade_notify,����#NotifyѶϢ��,trade_notifyqd,����#NotifyѶϢ��,trade_sub_notify,����#NotifyѶϢ��,trade_sub_notifyqd,����#NotifyѶϢ��,trade_sub2_notify,����#NotifyѶϢ��,transmitter_notify,����#Web����,mpp,����#Web����,mpphaproxyin,����#Web����,mpphaproxyout,����#Web����,mpplvs,����#Web����,qin,����#Web����,webwangwang,����#Web����,webwangwanglvs,����#Web����,webwwadmin,����#Web����,webwwhaproxy,����#����������תϵͳ,tsu,����#�ֲ�ʽ���ݲ�,andor,����#�ֲ�ʽ���ݲ�,dayu,����#�ֲ�ʽ���ݲ�,jingwei,����#�ֲ�ʽ���ݲ�,jingwei_ic,����#�ֲ�ʽ���ݲ�,mid_panalyser,����#�ֲ�ʽ���ݲ�,tddl,����#�ֲ�ʽ���ݲ�,tddlsyncserver,����#�ֲ�ʽ���ݲ�,venusreplicator,����#�ֲ�ʽ���ݲ�,yugong,����#����ϵͳ���,autodeploy,����#����ϵͳ���,coremonitor,����#����ϵͳ���,coremonitorqd,����#����ϵͳ���,coreperf,����#����ϵͳ���,hotspot,����#������������,ops_security,����#������������,ops_securityqd,����#������������,rtools,����#������������,rtoolsqd,����#��ػ�����ط���,tgc,����#Ӧ�÷�����,artoo,����#Ӧ�÷�����,depcenter,����#Ӧ�÷�����,tbcontainer,����#����,baoming_terminator,����#����,s4addressfriend,����#����,s4album,����#����,s4ark,����#����,s4arkisv,����#����,s4baoming,����#����,s4bbcproduct,����#����,s4bbcsupplier,����#����,s4bbs,����#����,s4blog,����#����,s4cms,����#����,s4comment,����#����,s4company,����#����,s4diary,����#����,s4elogistics,����#����,s4friend,����#����,s4galaxy,����#����,s4group,����#����,s4grouplus,����#����,s4ipfriend,����#����,s4jobonline,����#����,s4juactionlog,����#����,s4jucheckitem,����#����,s4judeposititem,����#����,s4juitemonline,����#����,s4juke,����#����,s4jukeseller,����#����,s4jusellerauth,����#����,s4jwy,����#����,s4magicroom,����#����,s4matrixtry,����#����,s4newipfriend,����#����,s4phone,����#����,s4realwidget,����#����,s4resumeonline,����#����,s4tag,����#����,s4tmshistory,����#����,s4tmspage,����#����,s4tstar,����#����,s4tstaractivity,����#����,terminatorconsole,����#����,terminatorzk,����#����,clouddrive,����#����,clouddrivepush,����#����,designaudit,����#����,designcenter,����#����,designcenterbeta,����#����,designmarket,����#����,designsearch,����#����,designsystem,����#����,designsystembeta,����#����,imagecapture,����#����,pamirsshop,����#����,rendersystem,����#����,sdk,����#����,shopabtest,����#����,shopcenter,����#����,shophadoop,����#����,shopmanager,����#����,shopmonitor,����#����,shopreport,����#����,shopsystem,����#����,taeadmin,����#����,taeappcenter,����#����,taecontainer,����#����,taecron,����#����,taedevadmin,����#����,taegrid,����#����,taemarket,����#����,taequeue,����#����,taesite,����#����,taesitecenter,����#����,taesiteentrance,����#����,taestorm,����#������֤,aq,����#������֤,durex,����#������֤,fandiaoyupindao,����#������֤,global,����#������֤,isv_dianzisuo,����#���,namelist,����#���,pamirslog,����#���,ruleconfig,����#������ȫ,aliws,����#������ȫ,archer,����#������ȫ,ctupf,����#������ȫ,dsp,����#������ȫ,gaara,����#������ȫ,kfc,����#������ȫ,libra,����#������ȫ,monitorclient,����#������ȫ,pamirs_hecla,����#������ȫ,pfp,����#������ȫ,police,����#������ȫ,pomas,����#������ȫ,punishcenter,����#������ȫ,rulerun,����#������ȫ,sdc,����#������ȫ,tbctu,����#������ȫ,tbosshive,����#������ȫ,tee,����#������ȫ,themis,����#������ȫ,wwgateway,����#������ȫ,xu,����#TSP,servicemanager,����#VOC,beijixing,����#VOC,biapp,����#VOC,dunhuang,����#VOC,panama,����#VOC,tqms,����#��������,logsystem,����#��������,onlinecs,����#��������,pamirsminerva,����#��������,pamirssupport,����#��������,siderite,����#��������,support,����#���ķ���,buyermodule,����#���ķ���,ctp,����#���ķ���,pamirsbogda,����#���ķ���,pamirsshennong,����#���ķ���,pamirssmartscript,����#���ķ���,shennong,����#���ķ���,smartscript,����#άȨƽ̨,azeroth,����#άȨƽ̨,capitalcenter,����#άȨƽ̨,gandalf,����#άȨƽ̨,serviceone,����#��������ƽ̨,channelpoint,����#��������ƽ̨,kalimdor,����#";

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
		<td>Ӧ������</td>
		<td>������</td>
		<td>�����Ŷ�</td>
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
		<td>ϵͳ����</td>
		<td>�����ֲ�</td>
		<td>�Ƿ�ʵ���</td>
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
		<td><%=groupCapacityRate<=0?"δѹ��":groupCapacityRate %></td>
		<td><%=r%></td>
		<td><%= ("��:"+v[0])+("ʵ:"+v[1] )%></td>
	</tr>
<%}catch(Exception e){
	e.printStackTrace();
}} %>
</tbody>
</table>
</body>
</html>