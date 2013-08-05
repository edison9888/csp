
package com.taobao.monitor.alarm.transfer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.alarm.n.AlarmContext;
import com.taobao.monitor.common.messagesend.MessageSend;
import com.taobao.monitor.common.messagesend.MessageSendFactory;
import com.taobao.monitor.common.messagesend.MessageSendType;
import com.taobao.monitor.web.ao.MonitorAlarmAo;
import com.taobao.monitor.web.cache.SiteCache;

/**
 * 
 * @author xiaodu
 * @version 2011-2-12 下午02:27:28
 */
public class WangwangTransfer extends Transfer{
	
	private static final Logger logger = Logger.getLogger(WangwangTransfer.class);
	
	
	private static WangwangTransfer ww = new WangwangTransfer("WangwangTransfer");
	
	private SimpleDateFormat format = new SimpleDateFormat("HH:mm");
	
	
	private class WWSend{
		
		private long recentlyTime=0;
		
		private int recentlyAlarmNum = 2;
		
	}
	
	
	private Map<String,WWSend> sendRecordMap = new HashMap<String, WWSend>();
	
	
	public boolean isNeedSend(String targetWw){
		
		
		WWSend ww = sendRecordMap.get(targetWw);
		if(ww ==null){
			ww = new WWSend();
			ww.recentlyAlarmNum = 3;
			ww.recentlyTime = System.currentTimeMillis();
			sendRecordMap.put(targetWw, ww);
			return true;
		}
		
		if(ww.recentlyAlarmNum >0){
			ww.recentlyAlarmNum--;
			return true;
		}
		
		if(System.currentTimeMillis()-ww.recentlyTime<1000*40*60){
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
		
		if(targetDataList.size() <1){
			return null;
		}
		
		AlarmContext data = targetDataList.get(0);
		
		if(!isNeedSend(targetSend+data.getAppId()+data.getKeyId())){
			
			logger.info(targetSend+"---"+data.getAppName()+"---"+data.getKeyName()+"过滤发送");
			
			return null;
		}
		
		// 是否采用的是多台策略
		boolean multiHosts = false;
		if (data.getSiteId() == -1) {
			multiHosts = true;
		}
		
		StringBuilder message = new StringBuilder();
		if(targetDataList.size() >=3){
			message.append(data.getAppName()+" ["+data.getKeyName()+"]当前在["+targetDataList.size()+"]机器出现,以下只显示3个</br>");
		}else{
			message.append(data.getAppName()+" ["+data.getKeyName()+"]</br>");
		}
		String alarm = "";
		
		//最多显示3个
		for(int i=0;i<targetDataList.size()&&i<1;i++){
			AlarmContext tmp = targetDataList.get(i);
			int keyType = tmp.getKeyType();	
			keyType+=tmp.getContinuousAlarmTimes();
			String siteInfo = "";
			if (!multiHosts) {
				siteInfo = "["+tmp.getSiteName()+"]";
			}
			if(keyType==1){
				alarm = "普通-";
				message.append("时间:"+format.format(tmp.getRecentlyDate())+siteInfo+"告警值:"+tmp.getRecentlyValue()+"."+tmp.getRangeMessage()+" 已连续"+tmp.getContinuousAlarmTimes()+"次"+"</br>");
			}else if(keyType==2){
				alarm = "重要-";
				message.append("时间:"+format.format(tmp.getRecentlyDate())+siteInfo+"告警值:"+tmp.getRecentlyValue()+"."+tmp.getRangeMessage()+" 已连续"+tmp.getContinuousAlarmTimes()+"次"+"</br>");
			}else if(keyType>=3){
				alarm = "紧急-";
				message.append("<font color='red'>"+"时间:"+format.format(tmp.getRecentlyDate())+siteInfo+"告警值:"+tmp.getRecentlyValue()+"."+tmp.getRangeMessage()+"已连续"+tmp.getContinuousAlarmTimes()+"次"+"</font></br>");
			}else{
				message.append(format.format("时间:"+tmp.getRecentlyDate())+siteInfo+"告警值:"+tmp.getRecentlyValue()+"."+tmp.getRangeMessage()+" 已连续"+tmp.getContinuousAlarmTimes()+"次"+"</br>");
			}			
//			if(tmp.getRelationMessage() !=null){
//				message.append("<font color='green'>"+tmp.getRelationMessage()+"</font></br>");
//			}
//			if(tmp.getScriptMessage()!=null){
//				message.append(tmp.getScriptMessage()+"</br>");
//			}
		}
		
		
		
		
		if(data.getBaseLineValue() != null)
			message.append("基线:"+data.getBaseLineValue());
		
		if(data.getKeyName().indexOf("EXCEPTION")>-1||data.getKeyName().indexOf("LogLinePattern")>-1){
			message.append("<table><tr><td>");
			message.append("<a target='_blank' href='http://cm.taobao.net:9999/monitorstat/alarm/exception_info.jsp?appId="+data.getAppId()+"&keyId="+data.getKeyId()+"&collectTime="+sdf1.format(new Date())+"'>查看异常信息</a>");			
			message.append("</td></tr></table>");
		}else{
			//message.append("<table>");
			//message.append( "<tr><td><a target='_blank' href='http://cm.taobao.net:9999/monitorstat/alarm/desc.jsp?aId="+data.getAppId()+"&o=t&id="+data.getId()+"'>点击填写告警原因</a></td></tr>");
			//message.append("<tr><td>");
			// 如果是多台统计（siteid被设置成-1了)
			if (data.getSiteId() == -1) {
				message.append( "<a target='_blank' href='http://cm.taobao.net:9999/monitorstat/show_total.jsp?appId="+data.getAppId()+"&keyId="+data.getKeyId()+"'>查看报警快照</a>");
			} else {
				message.append( "<a target='_blank' href='http://cm.taobao.net:9999/monitorstat/show.jsp?appId="+data.getAppId()+"&keyId="+data.getKeyId()+"&appName="+data.getAppName()+"'>查看详细数据</a>");
			}
			//message.append("</td></tr></table>");
		}
		String result = "";
		if(message.length()>1000){
			result = message.substring(0, 1000);
		}else{
			result = message.toString();
		}
		
		messageSend.send(targetSend, alarm+"性能监控告警", result);
		
		logger.info("wangwang alarm:"+targetSend+"----"+message.toString());
		
		
		MonitorAlarmAo.get().addAlarmSend(data.getAppId(), targetSend,data.getRangeMessage(), "wangwang");
		
		return null;
	}

	@Override
	protected boolean isRecentlySend(String key,AlarmContext targetData) {
		
		return false;
	}


}
