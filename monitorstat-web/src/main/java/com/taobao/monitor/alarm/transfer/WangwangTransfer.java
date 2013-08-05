
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
 * @version 2011-2-12 ����02:27:28
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
			
			logger.info(targetSend+"---"+data.getAppName()+"---"+data.getKeyName()+"���˷���");
			
			return null;
		}
		
		// �Ƿ���õ��Ƕ�̨����
		boolean multiHosts = false;
		if (data.getSiteId() == -1) {
			multiHosts = true;
		}
		
		StringBuilder message = new StringBuilder();
		if(targetDataList.size() >=3){
			message.append(data.getAppName()+" ["+data.getKeyName()+"]��ǰ��["+targetDataList.size()+"]��������,����ֻ��ʾ3��</br>");
		}else{
			message.append(data.getAppName()+" ["+data.getKeyName()+"]</br>");
		}
		String alarm = "";
		
		//�����ʾ3��
		for(int i=0;i<targetDataList.size()&&i<1;i++){
			AlarmContext tmp = targetDataList.get(i);
			int keyType = tmp.getKeyType();	
			keyType+=tmp.getContinuousAlarmTimes();
			String siteInfo = "";
			if (!multiHosts) {
				siteInfo = "["+tmp.getSiteName()+"]";
			}
			if(keyType==1){
				alarm = "��ͨ-";
				message.append("ʱ��:"+format.format(tmp.getRecentlyDate())+siteInfo+"�澯ֵ:"+tmp.getRecentlyValue()+"."+tmp.getRangeMessage()+" ������"+tmp.getContinuousAlarmTimes()+"��"+"</br>");
			}else if(keyType==2){
				alarm = "��Ҫ-";
				message.append("ʱ��:"+format.format(tmp.getRecentlyDate())+siteInfo+"�澯ֵ:"+tmp.getRecentlyValue()+"."+tmp.getRangeMessage()+" ������"+tmp.getContinuousAlarmTimes()+"��"+"</br>");
			}else if(keyType>=3){
				alarm = "����-";
				message.append("<font color='red'>"+"ʱ��:"+format.format(tmp.getRecentlyDate())+siteInfo+"�澯ֵ:"+tmp.getRecentlyValue()+"."+tmp.getRangeMessage()+"������"+tmp.getContinuousAlarmTimes()+"��"+"</font></br>");
			}else{
				message.append(format.format("ʱ��:"+tmp.getRecentlyDate())+siteInfo+"�澯ֵ:"+tmp.getRecentlyValue()+"."+tmp.getRangeMessage()+" ������"+tmp.getContinuousAlarmTimes()+"��"+"</br>");
			}			
//			if(tmp.getRelationMessage() !=null){
//				message.append("<font color='green'>"+tmp.getRelationMessage()+"</font></br>");
//			}
//			if(tmp.getScriptMessage()!=null){
//				message.append(tmp.getScriptMessage()+"</br>");
//			}
		}
		
		
		
		
		if(data.getBaseLineValue() != null)
			message.append("����:"+data.getBaseLineValue());
		
		if(data.getKeyName().indexOf("EXCEPTION")>-1||data.getKeyName().indexOf("LogLinePattern")>-1){
			message.append("<table><tr><td>");
			message.append("<a target='_blank' href='http://cm.taobao.net:9999/monitorstat/alarm/exception_info.jsp?appId="+data.getAppId()+"&keyId="+data.getKeyId()+"&collectTime="+sdf1.format(new Date())+"'>�鿴�쳣��Ϣ</a>");			
			message.append("</td></tr></table>");
		}else{
			//message.append("<table>");
			//message.append( "<tr><td><a target='_blank' href='http://cm.taobao.net:9999/monitorstat/alarm/desc.jsp?aId="+data.getAppId()+"&o=t&id="+data.getId()+"'>�����д�澯ԭ��</a></td></tr>");
			//message.append("<tr><td>");
			// ����Ƕ�̨ͳ�ƣ�siteid�����ó�-1��)
			if (data.getSiteId() == -1) {
				message.append( "<a target='_blank' href='http://cm.taobao.net:9999/monitorstat/show_total.jsp?appId="+data.getAppId()+"&keyId="+data.getKeyId()+"'>�鿴��������</a>");
			} else {
				message.append( "<a target='_blank' href='http://cm.taobao.net:9999/monitorstat/show.jsp?appId="+data.getAppId()+"&keyId="+data.getKeyId()+"&appName="+data.getAppName()+"'>�鿴��ϸ����</a>");
			}
			//message.append("</td></tr></table>");
		}
		String result = "";
		if(message.length()>1000){
			result = message.substring(0, 1000);
		}else{
			result = message.toString();
		}
		
		messageSend.send(targetSend, alarm+"���ܼ�ظ澯", result);
		
		logger.info("wangwang alarm:"+targetSend+"----"+message.toString());
		
		
		MonitorAlarmAo.get().addAlarmSend(data.getAppId(), targetSend,data.getRangeMessage(), "wangwang");
		
		return null;
	}

	@Override
	protected boolean isRecentlySend(String key,AlarmContext targetData) {
		
		return false;
	}


}
