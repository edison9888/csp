
package com.taobao.csp.alarm.transfer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.alarm.ao.MonitorAlarmAo;
import com.taobao.csp.alarm.po.AlarmContext;
import com.taobao.csp.alarm.po.AlarmSendPo;
import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.other.artoo.Artoo;
import com.taobao.csp.other.artoo.ArtooInfo;
import com.taobao.csp.other.beidou.BeiDouAlarmRecordCache;
import com.taobao.csp.time.cache.BaseLineCache;
import com.taobao.csp.time.util.DataUtil;
import com.taobao.csp.time.util.PropNameDescUtil;
import com.taobao.csp.time.web.po.BeiDouAlarmRecordPo;
import com.taobao.monitor.common.ao.center.CspDependInfoAo;
import com.taobao.monitor.common.ao.center.CspTimeKeyAlarmRecordAo;
import com.taobao.monitor.common.messagesend.MessageSend;
import com.taobao.monitor.common.messagesend.MessageSendFactory;
import com.taobao.monitor.common.messagesend.MessageSendType;
import com.taobao.monitor.common.po.CspTimeAppDependInfo;
import com.taobao.monitor.common.po.CspTimeKeyAlarmRecordPo;
import com.taobao.monitor.common.po.CspTimeKeyDependInfo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

/**
 * 
 * @author xiaodu
 * @version 2011-2-12 下午02:27:28
 */
public class WangwangTransfer extends Transfer{

	private static final Logger logger = Logger.getLogger(WangwangTransfer.class);


	private static WangwangTransfer ww = new WangwangTransfer("WangwangTransfer");



	private class WWSend{

		private long recentlyTime=0;

		private int recentlyAlarmNum = 2;

	}


	private Map<String,WWSend> sendRecordMap = new HashMap<String, WWSend>();

	/**
	 * 相同的告警连续发送3次，3次后简单5分钟再次发送
	 *@author xiaodu
	 * @param targetWw
	 * @return
	 *TODO
	 */
	public boolean isNeedSend(String target,String app,String keyName,String property){

		String targetWw = target+","+app+","+keyName+","+property;

		int s = 5;
		if(PropConstants.SWAP.equals(property)){
			s = 60;
		}

		WWSend ww = sendRecordMap.get(targetWw);
		if(ww ==null){
			ww = new WWSend();
			ww.recentlyAlarmNum = 2;
			ww.recentlyTime = System.currentTimeMillis();
			sendRecordMap.put(targetWw, ww);
			return true;
		}

		if(ww.recentlyAlarmNum >0){
			ww.recentlyAlarmNum--;
			return true;
		}

		if(System.currentTimeMillis()-ww.recentlyTime<1000*s*60){
			return false;
		}else{
			ww.recentlyAlarmNum = 2;
			ww.recentlyTime = System.currentTimeMillis();
			return true;
		}



	}

	public static WangwangTransfer getInstance(){
		return ww;
	}


	public void sendExtraMessage(String target,String title,String message){

		messageSend.send(target, title, message);

	}


	private MessageSend messageSend = MessageSendFactory.create(MessageSendType.WangWang);
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	private WangwangTransfer(String name) {
		super(name);
	}

	@Override
	public String formatTranser(String targetSend, List<AlarmContext> targetDataList) {
		logger.info(targetSend+"---接收信息"+targetDataList.size());

		if(targetDataList.size() <1){
			return null;
		}
		AlarmContext data = targetDataList.get(0);

		if(!isNeedSend(targetSend,data.getAppName(),data.getKeyName(),data.getProperty())){

			logger.info(targetSend+"---"+data.getAppName()+"---"+data.getKeyName()+"--"+data.getProperty()+"过滤发送");

			return null;
		}


		int alarmip=0;
		Set<String> tmpip = new HashSet<String>();
		for(AlarmContext alarmContext : targetDataList){
			if(alarmContext.getIp()!=null){
				tmpip.add(alarmContext.getIp());
			}
		}

		List<String> ipList = CspCacheTBHostInfos.get().getIpsListByOpsName(data.getAppName());

		List<CspTimeKeyAlarmRecordPo> list = CspTimeKeyAlarmRecordAo.get().findRecentlyAlarmInfo(data.getAppName(), data.getKeyName(), data.getProperty(),"HOST",3);

		for(CspTimeKeyAlarmRecordPo po:list){
			tmpip.add(po.getIp());
		}
		alarmip=tmpip.size();

		//CspKeyInfo key= KeyCache.getCache().getKeyInfo(data.getKeyName());
		int level = 2;
		if(NumberUtils.isNumber(data.getKeyLevel())){
			try {
				level = Integer.parseInt(data.getKeyLevel());
			} catch (Exception e) {
				logger.error("", e);
				level = 2;
			}
		}
		if(level >1){
			int y = ipList.size()/5>10?10:ipList.size()/5;
			if(ipList.size()<=10){
				y=2;
			}

			if(alarmip <y){
				logger.info("过滤不够告警机器数量"+data.getAppName()+"#"+data.getKeyName()+"#"+data.getProperty()+" 机器数量:"+ipList.size()+"告警机器数"+list.size());
				Iterator<AlarmContext> it = targetDataList.iterator();
				while(it.hasNext()){
					AlarmContext context = it.next();
					if(context.getIp()!=null){
						it.remove();
					}
				}
			}
		}

		if(targetDataList.size() <1){
			return null;
		}

		StringBuilder message = new StringBuilder();
		Integer hostCount = alarmip;
		Integer appCount = 0;
		for(AlarmContext alarmContext : targetDataList){
			if(alarmContext.getKeyScope().equals("APP"))appCount++;
		}
		String propertyView = PropNameDescUtil.getDesc(data.getProperty());
		if(propertyView == null)propertyView = data.getProperty();

		String artmsg = "";
		//机器的发布情况
		Artoo art = ArtooInfo.get().getRecentlySingleArtoo(data.getAppName());
		if(art != null){
			artmsg="小二["+art.getCreator()+"]在"+art.getDeployTime()+art.getPlanType()+art.getState();
		}

		if(hostCount >0){
			message.append("应用:"+data.getAppName()+"全网机器数["+ipList.size()+"]<br>"+artmsg+"<br>告警点:"+(StringUtils.isBlank(data.getKeyAlias())?data.getKeyName():data.getKeyAlias())+"<br>其中告警主机"+hostCount+"台以下只显示1台<br><br>");
		}else{
			message.append("应用:"+data.getAppName()+"全网机器数["+ipList.size()+"]<br>"+artmsg+"<br> 告警点:"+(StringUtils.isBlank(data.getKeyAlias())?data.getKeyName():data.getKeyAlias())+"<br><br>");
		}
		String alarm = "";
		String hyperlink = "<a href='http://time.csp.taobao.net:9999/time/app/detail/alarm/show.do?method=showIndex&appId="+data.getAppId()+"&keyName="+data.getKeyName()+"&property="+data.getProperty()+"&time="+data.getTime()+"'>查看详情</a><br>";

		message.append(hyperlink);

		//最多显示3个
		int a = 0;
		int b = 0;
		for(int i=0;i<targetDataList.size();i++){
			AlarmContext tmp = targetDataList.get(i);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
			String formatTime = simpleDateFormat.format(new Date(tmp.getTime()));
			if(tmp.getKeyScope().equals("APP")){
				if(a >0){
					continue;
				}
				String baseScale = BaseLineCache.get().getScale(data.getAppName(), data.getKeyName(), data.getProperty(), formatTime, DataUtil.transformDouble(tmp.getValue()));
				alarm+="告警范围:";
				alarm+="全网";
				a++;

				alarm+="<br>时间:"+formatTime+"<br>判断模式:"+tmp.getModeName()+"<br>"+propertyView+"值:"+tmp.getValue()+"<br>告警原因:"+tmp.getRangeMessage()+"<br>累积次数:"+tmp.getContinuousAlarmTimes()+"<br>与基线对比:"+baseScale+"<br><br>";
			}
			if(tmp.getKeyScope().equals("HOST")){

				if(b >0){
					continue;
				}

				if(data.getKeyName().startsWith(KeyConstants.EXCEPTION)){
					alarm = "<br><a href='http://time.csp.taobao.net:9999/time/app/detail/exception/show.do?method=desc&appName="+data.getAppName()+"&keyName="+data.getKeyName()+"&ip="+tmp.getIp()+"'>查看异常内容</a><br>";
				}
				String baseScale = BaseLineCache.get().getScale(data.getAppName(), data.getKeyName(), data.getProperty(), formatTime, DataUtil.transformDouble(tmp.getValue()),tmp.getIp());
				alarm+="告警范围:";
				alarm+=tmp.getIp()+"";
				b++;
				alarm+="<br>时间:"+formatTime+"<br>判断模式:"+tmp.getModeName()+"<br>"+propertyView+"值:"+tmp.getValue()+"<br>告警原因:"+tmp.getRangeMessage()+"<br>累积次数:"+tmp.getContinuousAlarmTimes()+"<br>与基线对比:"+baseScale+"<br><br>";
			}
		}
		message.append(alarm);

		if("tradeplatform".equals(data.getAppName())){
			String other = "<br/><a href='http://cm.taobao.net:9999/monitorstat/alarm/trade_alarm_relate.jsp'>交易平台TradePlatform 关联监控系统</a><br/>";
			message.append(other);
		}
		String depappalarmmsg = getDependAppAlarm(data.getAppId(),data.getAppName());

		String depkeyAlarmMsg = getDependKeyAlarm(data.getAppName(),data.getKeyName());

		message.append(depkeyAlarmMsg);//key的依赖信息

		message.append(depappalarmmsg);//应用的依赖信息

		String result = "";
		if(message.length()>1000){
			result = message.substring(0, 1000);
		}else{
			result = message.toString();
		}

		if(level == 0){result="<font color='#484891'>"+result+"</font>";data.setKeyLevel("P0");}
		if(level == 1){result="<font color='#484891'>"+result+"</font>";data.setKeyLevel("P1");}
		if(level == 2){result="<font color='#484891'>"+result+"</font>";data.setKeyLevel("P2");}



		messageSend.send(targetSend, data.getKeyLevel()+"业务数据监控",result);

		logger.info("wangwang alarm:"+targetSend+"----"+message.toString());
		AlarmSendPo alarmSendPo = new AlarmSendPo();
		alarmSendPo.setAcceptTime(new Date());
		alarmSendPo.setAppId(data.getAppId());
		alarmSendPo.setAlarmMsg(data.getRangeMessage());
		alarmSendPo.setAlarmType("wangwang");
		alarmSendPo.setTargetAim(targetSend);
		alarmSendPo.setAlarmMsg(data.getRangeMessage());
		MonitorAlarmAo.get().addAlarmSend(alarmSendPo);
		return null;
	}



	private String getDependAppAlarm(int appId,String appName){

		List<CspTimeAppDependInfo> sourceAppList = CspDependInfoAo.get().getCspTimeAppDependInfoBySourceName(appName);

		Set<String> dependSet = new HashSet<String>();

		for(CspTimeAppDependInfo depend:sourceAppList){
			dependSet.add(depend.getAppName());
			dependSet.add(depend.getDepAppName());
		}
		dependSet.remove(appName);

		if(dependSet.size()==0){
			return "<br/><br/>-----------------依赖应用告警信息-----------------<br/>此应用没有配置依赖关系，<a href='http://time.csp.taobao.net:9999/time/config/dependconfig.do?method=searchAppConfigList&appId="+appId+"'>进入配置</a>----------------------------------</font>";

		}


		StringBuffer dependAlarm = new StringBuffer();

		for(String app:dependSet){
			//检查BD 库表
			List<BeiDouAlarmRecordPo> beidouAlarmList = BeiDouAlarmRecordCache.get().get(app);	
			if(beidouAlarmList.size()>0){
				dependAlarm.append(app+"最近5分钟出现"+beidouAlarmList.size()+"告警<br/>");
				continue;
			}
			int alarmcount = CspTimeKeyAlarmRecordAo.get().countRecentlyAlarmNum(app, 5);
			if(alarmcount >0)
				dependAlarm.append(app+"最近5分钟出现"+alarmcount+"告警<br/>");

		}
		if(dependAlarm.length()>0){
			String a = "http://time.csp.taobao.net:9999/time/app/depend/query/show.do?appName="+appName+"&method=queryAppDetailWithTimeData";
			return "<br/><a href='"+a+"'>查看详情</a><br/>-----------------依赖应用告警信息-----------------<br/>"+dependAlarm.toString()+"<br/>----------------------------------</font>";
		}else{
			String a = "http://time.csp.taobao.net:9999/time/app/depend/query/show.do?appName="+appName+"&method=queryAppDetailWithTimeData";
			return "<br/><a href='"+a+"'>查看详情</a><br/>-----------------依赖应用告警信息-----------------<br/>依赖的应用中没有发现告警信息<br/>----------------------------------</font>";
		}

	}


	private String getDependKeyAlarm(String appName,String keyName){
		StringBuffer dependAlarm = new StringBuffer();
		if(keyName.startsWith("PV`http://")){//这个是一个URL类型的
			List<CspTimeKeyDependInfo> list = CspDependInfoAo.get().getCspTimeKeyDependInfo_Key(appName, keyName);


			if(list.size()==0){
				return "<br/><br/>-----------------当前key的依赖告警信息-----------------<br/>此URL没有依赖信息，你可以联系CSP相关人员<br/>----------------------------------</font>";
			}


			for(CspTimeKeyDependInfo depend:list ){
				String dependKey = depend.getDependKeyName();
				String dependApp = depend.getDependAppName();
				int alarmcount = CspTimeKeyAlarmRecordAo.get().countRecentlyAlarmNum(dependApp,dependKey, 3);
				if(alarmcount >0){
					dependAlarm.append(dependApp+"["+dependKey+"]最近3分钟出现"+alarmcount+"告警<br/>");
				}
			}

			if(dependAlarm.length()>0){
				String a = "http://time.csp.taobao.net:9999/time/app/depend/query/show.do?keyName="+keyName+"&appName="+appName+"&method=queryKeyDetailWithTimeData";
				return "<br/><a href='"+a+"'>查看详情</a><br/>-----------------当前key的依赖告警信息-----------------<br/>"+dependAlarm.toString()+"<br/>----------------------------------</font>";
			}else{
				String a = "http://time.csp.taobao.net:9999/time/app/depend/query/show.do?keyName="+keyName+"&appName="+appName+"&method=queryKeyDetailWithTimeData";
				return "<br/><a href='"+a+"'>查看详情</a><br/>-----------------当前key的依赖告警信息-----------------<br/>依赖的key中没有发现告警信息<br/>----------------------------------</font>";
			}


		}

		if(keyName.startsWith("HSF-provider`com")){
			List<CspTimeKeyDependInfo> list = CspDependInfoAo.get().getKeyDependByKeyName(appName,keyName);

			Set<String> set = new HashSet<String>();
			Iterator<CspTimeKeyDependInfo> it = list.iterator();
			while(it.hasNext()){
				CspTimeKeyDependInfo d = it.next();
				if(set.contains(d.getDependAppName()+d.getDependKeyName())){
					it.remove();
				}
			}

			for(CspTimeKeyDependInfo depend:list){
				String dependKey = depend.getDependKeyName();
				String dependApp = depend.getDependAppName();
				int alarmcount = CspTimeKeyAlarmRecordAo.get().countRecentlyAlarmNum(dependApp,dependKey, 3);
				if(alarmcount >0){
					dependAlarm.append(dependApp+"["+dependKey+"]最近3分钟出现"+alarmcount+"告警<br/>");
				}
			}

			if(dependAlarm.length()>0){
				String a = "http://time.csp.taobao.net:9999/time/app/depend/query/show.do?keyName="+keyName+"&appName="+appName+"&method=queryKeyDetailWithTimeData";
				return "<br/><a href='"+a+"'>查看详情</a><br/>-----------------当前key的依赖告警信息-----------------<br/>"+dependAlarm.toString()+"<br/>----------------------------------</font>";
			}else{
				return "";
			}
		}

		return "";

	} 

}
