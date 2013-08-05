
package com.taobao.monitor.alarm.n.key;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * ��¼ һ��key �澯���
 * @author xiaodu
 * @version 2011-2-28 ����11:20:43
 */
public class KeyAlarmRecord {
	
	private static int continue_interval = 240*1000;
	
	private Map<String,Record> recordMap = new HashMap<String,Record>();
	
//	private Map<String,Map<String,Record>> extraRecord = new HashMap<String, Map<String,Record>>();
//	
//	
//	public String getExtraMessage(int appId,int keyId,String siteName,java.util.Date date){
//		String key = appId+"_"+keyId;
//		Map<String,Record> r = extraRecord.get(key);
//		
//		if(r == null){
//			r = new HashMap<String, Record>();
//			extraRecord.put(key, r);
//		}
//		
//		Record c = r.get(siteName);
//		
//		if(c == null){
//			c = new Record();
//			r.put(siteName, c);
//		}
//		
//		
//		if(date.getTime()-c.getPreviousAlarmTime()<3*60*1000){
//			
//			
//			
//		}	
//		
//	}
	
	/**
	 * ��¼����
	 * @param appId
	 * @param keyId
	 * @param siteId
	 * @param date
	 */
	public synchronized void record(int appId,int keyId,int siteId,java.util.Date date){
		
		String key = createKey(appId,keyId,siteId);
		
		Record r = recordMap.get(key);
		if(r == null){
			r = new Record();
			recordMap.put(key, r);
		}
		
		if(date.getTime()-r.getPreviousAlarmTime()>continue_interval){
			r.setContinuousAlarmTimes(0);
		}else{
			r.setContinuousAlarmTimes(r.getContinuousAlarmTimes()+1);
		}
		
		r.setPreviousAlarmTime(date.getTime());
		
	}
	
	/**
	 * true ��ʾ���ʱ���Ѿ����ڹ�
	 * @param appId
	 * @param keyId
	 * @param siteId
	 * @param date
	 * @return
	 */
	public boolean isExist(int appId,int keyId,int siteId,java.util.Date date){
		
		long l = getPreviousAlarmTime(appId,keyId,siteId);
		
		if(l == date.getTime()){
			return true;
		}else{
			return false;
		}
		
	}
	
	
	
	public boolean isRecently(int appId,int keyId,int siteId,java.util.Date date){
		long l = getPreviousAlarmTime(appId,keyId,siteId);
		
		if(date.getTime()-l <60000 ){
			return true;
		}else{
			return false;
		}
	}
	
	
	/**
	 * ��ȡ��ǰkey�ϴθ澯ʱ��
	 * @param appId
	 * @param keyId
	 * @return
	 */
	public long getPreviousAlarmTime(int appId,int keyId,int siteId){
		
		String key = createKey(appId,keyId,siteId);
		
		Record r = recordMap.get(key);
		if(r != null){
			return r.getPreviousAlarmTime();
		}
		return 0;
	}
	
	/**
	 * ��ȡ��ǰkey����������
	 * @param appId
	 * @param keyId
	 * @return
	 */
	public int getContinuousAlarmTimes(int appId,int keyId,int siteId){
		
		String key = createKey(appId,keyId,siteId);
		
		Record r = recordMap.get(key);
		if(r != null){
			return r.getContinuousAlarmTimes();
		}
		return 0;
	}
	
	
	private String createKey(int appId,int keyId,int siteId){
		return appId+"_"+keyId+"_"+siteId;
	}
	
	
	private static class Record{
		
		private long previousAlarmTime;//�ϴθ澯��ʱ��
		
		private int continuousAlarmTimes;//�����澯����

		public long getPreviousAlarmTime() {
			return previousAlarmTime;
		}

		public void setPreviousAlarmTime(long previousAlarmTime) {
			this.previousAlarmTime = previousAlarmTime;
		}

		public int getContinuousAlarmTimes() {
			return continuousAlarmTimes;
		}

		public void setContinuousAlarmTimes(int continuousAlarmTimes) {
			this.continuousAlarmTimes = continuousAlarmTimes;
		}
	}
	
	
	

}
