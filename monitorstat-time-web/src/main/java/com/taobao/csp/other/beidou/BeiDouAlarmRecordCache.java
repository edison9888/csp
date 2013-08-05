package com.taobao.csp.other.beidou;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.taobao.csp.alarm.ao.BeiDouAlarmAo;
import com.taobao.csp.time.web.po.BeiDouAlarmRecordPo;


public class BeiDouAlarmRecordCache implements Runnable{
	private static Logger logger = Logger.getLogger(BeiDouAlarmRecordCache.class);
	private Map<String,List<BeiDouAlarmRecordPo>> cacheMap = new ConcurrentHashMap<String,List<BeiDouAlarmRecordPo>>();
	private static BeiDouAlarmRecordCache cache = new BeiDouAlarmRecordCache();
	private Thread thread;
	private BeiDouAlarmRecordCache(){
		update();
		thread = new Thread(this);
		thread.start();
	}
	public static BeiDouAlarmRecordCache get(){
		return cache;
	}
	public List<BeiDouAlarmRecordPo> get(String opsName){
		List<BeiDouAlarmRecordPo> list =  null;
		list = cacheMap.get(opsName);
		if(list == null){
			list = new ArrayList<BeiDouAlarmRecordPo>();
		}
		return list;
	}
	@Override
	public void run() {
		
		while(true){
			update();
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				logger.info(e);
			}
		}
	}
	private void update(){
		cacheMap.clear();
		List<BeiDouAlarmRecordPo> list = BeiDouAlarmAo.get().getDbAlarmInfoRecently(5);
		for(BeiDouAlarmRecordPo po : list){
			if(po.getGroupName()==null)continue;
			
			if(po.getAlarmMsg()!=null){
				if(po.getAlarmMsg().startsWith("swap")){
					continue;
				}
			}
			
			List<BeiDouAlarmRecordPo> valueList = cacheMap.get(po.getGroupName());
			if(valueList == null){
				valueList = new ArrayList<BeiDouAlarmRecordPo>();
				cacheMap.put(po.getGroupName(), valueList);
			}
			valueList.add(po);
		}
	}
	
	public static void main(String[] args){
		BeiDouAlarmRecordCache.get().get("mysql_other");
	}
	
}
