
package com.taobao.monitor.alarm.transfer;

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

/**
 * 
 * @author xiaodu
 * @version 2011-2-12 下午02:27:28
 */
public class PhoneTransfer extends Transfer{
	
	
	private Map<String,Date> map = new HashMap<String, Date>();
	
	private static final Logger logger = Logger.getLogger(PhoneTransfer.class);
	private static PhoneTransfer ww = new PhoneTransfer("PhoneTransfer");
	
	public static PhoneTransfer getInstance(){
		return ww;
	}
	
	protected synchronized boolean isRecentlySend(String target,AlarmContext targetData){
		String key = target+"_"+targetData.getAppId();
		Date date = targetData.getRecentlyDate();
		Date d = map.get(key);
		if(d == null){
			map.put(key, date);
			return false;
		}else{
			if(date.getTime() - d.getTime() >30*60*1000){
				map.put(key, date);
				return false;
			}
		}
		
		return true;
	}
	
	
	
	private MessageSend messageSend = MessageSendFactory.create(MessageSendType.Phone);
	
	private PhoneTransfer(String name) {
		super(name);
	}

	@Override
	public String formatTranser(String targetSend, List<AlarmContext> targetDataList) {
		String message = null;
		for(AlarmContext data:targetDataList){
			message = data.getAppName()+"["+data.getKeyName()+"]"+data.getRecentlyValue()+","+data.getRangeMessage();
			if(data.getBaseLineValue() != null)
				message+="基线:"+data.getBaseLineValue();
			
			MonitorAlarmAo.get().addAlarmSend(data.getAppId(), targetSend,data.getRangeMessage(), "phone");
			break;
		}	
		
		if(targetDataList.size() > 1){
			message+="共出现"+targetDataList.size()+"次";
		}
		
		
		
		if(message.length()<=80){	
			message = message.replaceAll("<br/>", "");
			messageSend.send(targetSend, "性能监控告警", message);
		}else{
			int index = 1;
			for(int i=0;i<message.length();){						
				String msg = message.substring(i, i+77>message.length()?message.length():(i+77));
				msg = msg.replaceAll("<br/>", "");
				messageSend.send(targetSend, "性能监控告警", "告警"+index+":"+msg);				
				i+=77;				
				index++;
			}
			
		}
		
		logger.info("wangwang alarm:"+message);
		return null;
	}


}
