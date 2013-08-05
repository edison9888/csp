
package com.taobao.monitor.alarm.n.key;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.alarm.AlarmHelper;
import com.taobao.monitor.alarm.n.AlarmContext;
import com.taobao.monitor.alarm.n.Service;
import com.taobao.monitor.alarm.n.event.AlarmEvent;
import com.taobao.monitor.alarm.n.event.RecordAlarmEvent;
import com.taobao.monitor.alarm.n.key.action.AutoJudge;
import com.taobao.monitor.alarm.n.key.action.Judge;
import com.taobao.monitor.alarm.n.key.action.JudgeFactory;
import com.taobao.monitor.alarm.n.key.action.Result;
import com.taobao.monitor.alarm.po.ExtraKeyAlarmDefine;
import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.web.ao.MonitorAlarmAo;
import com.taobao.monitor.web.cache.AppCache;
import com.taobao.monitor.web.cache.KeyCache;
import com.taobao.monitor.web.vo.AlarmDataPo;

/**
 * 
 * @author xiaodu
 * @version 2011-2-26 下午06:53:40
 */
public class KeyService implements Service<KeyDefine>{
	
	private static final Logger logger = Logger.getLogger(KeyService.class);
	
	private SimpleDateFormat format = new SimpleDateFormat("HH");
	
	
	private static KeyService keyService = new KeyService();
	
	private KeyService(){
		init();
	}
	
	public static KeyService get(){
		return keyService;
	}
	
	/**
	 * 存放 key的定义类，map的key 使用appId_keyId 组合而成
	 */
	private Map<String, KeyDefine> keyDefineMap = new HashMap<String, KeyDefine>();
	
	private KeyAlarmRecord record = new KeyAlarmRecord();
	
	private JudgeFactory judgeFactory = new JudgeFactory();
	
	private List<AlarmEvent> eventList = new ArrayList<AlarmEvent>();
	
	
	public void init(){
		List<AlarmDataPo> alarmKeyList = MonitorAlarmAo.get().findAllAlarmKeyByAimAndLikeName(null, null);
		for(AlarmDataPo po:alarmKeyList){
			register(createKeyDefine(po));
		}
		
		
		eventList.add(new RecordAlarmEvent());
		
	}
	
	public void register(KeyDefine define){
		if(define!=null){
			String key = getKeyString(define.getAppId(),define.getKeyId());
			keyDefineMap.put(key, define);
			
			logger.info(define.getAppName()+"_"+define.getKeyName()+"被修改");
		}
	}
	
	public void unregister(KeyDefine define){
		if(define!=null){
			String key = getKeyString(define.getAppId(),define.getKeyId());
			keyDefineMap.remove(key);
			logger.info(define.getAppName()+"_"+define.getKeyName()+"被删除");
		}
	}
	
	public static KeyDefine createKeyDefine(AlarmDataPo po){
		
		KeyDefine define = new KeyDefine();
		define.setAppId(po.getAppId());
		define.setAppName(po.getAppName());
		define.setKeyAliasName(po.getAlarmFeature());
		define.setKeyId(Integer.parseInt(po.getKeyId()));
		define.setKeyName(po.getKeyName());
		define.setKeyJudgeType(Integer.parseInt(po.getAlarmType()));
		define.setKeyType(po.getKeyType());
		define.parseCommonRangeDefine(po.getAlarmDefine(), po.getAlarmType());
		List<ExtraKeyAlarmDefine> list = MonitorAlarmAo.get().getExtraKeyAlarmDefine(po.getAppId(), Integer.parseInt(po.getKeyId()));
		if(list != null){
			for(ExtraKeyAlarmDefine extra:list){
				define.parseHostRangeDefine(extra.getHostId(), extra.getAlarmDefine());
			}
		}
		return define;
	}
	
	
	/**
	 * 检查是否需要对这个key 做出告警
	 * @param context
	 */
	public boolean lookup(AlarmContext context){
		KeyDefine define = getKeyDefineByContext(context);
		
		if(define == null){
			return false;
		}
		//这个方法修改了context 的内容，位置不能修改
		constructContextByKeyDefine(context, define);
			
		//如果 一个采集数据，和上次
		boolean exist = record.isExist(context.getAppId(), context.getKeyId(),context.getSiteId(), context.getRecentlyDate());
		if(exist){
			logger.info("已经判定过的数据"+context.getAppName()+"_"+context.getKeyName()+"_"+context.getContinuousAlarmTimes()+"");
			return false;//已经判定过的数据
		}
			
		if(!doJudgeAndSetContext(context, define)) return false;
				
		for(AlarmEvent event:eventList){
			event.onAlarm(context);
		}
				
		boolean recently = record.isRecently(context.getAppId(), context.getKeyId(),context.getSiteId(), context.getRecentlyDate());
				
		record.record(context.getAppId(), context.getKeyId(),context.getSiteId(), context.getRecentlyDate());				
		context.setContinuousAlarmTimes(record.getContinuousAlarmTimes(context.getAppId(), context.getKeyId(),context.getSiteId()));				
				
		if(recently){
			logger.info("最近1分钟已经处理过"+context.getAppName()+"_"+context.getKeyName()+"_"+context.getContinuousAlarmTimes()+"");					
			return false;//如果在上1两分钟内已经出现，放弃
			}
				
		if(!checkNight(context)){//如果在凌晨 1点到7点  ，告警必须是需要在5次以上
				return false;
			}
				
		if(!checkImportance(context)){//等级设置
				return false;
			}
				
		if(context.getKeyId() == 176){
				context.setRecentlyValue(Arith.div(Double.parseDouble(context.getRecentlyValue()), 1000,2)+"") ;
			}
				
		//record.getExtraMessage(context.getAppId(), context.getKeyId(), context.getRecentlyDate());
								
		return true;
			
			
	}

	public void constructContextByKeyDefine(AlarmContext context,
			KeyDefine define) {
		context.setAppName(define.getAppName());
		context.setKeyName(define.getKeyAliasName() == null || define.getKeyAliasName().length() == 0 ? define.getKeyName() : define.getKeyAliasName());
			
		if (define.getKeyJudgeType() == 3) {
			Date latestInLimit = AlarmHelper.getLatestDateInLimite(context.getAppId(), context.getKeyId());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(latestInLimit);
			calendar.add(Calendar.MINUTE, -2); //后退两分钟，确保是收集满的
			context.setRecentlyDate(calendar.getTime());
		}
	}

	public boolean doJudgeAndSetContext(AlarmContext context, KeyDefine define) {
		long nowTimeMillis =0;
		if(define.getKeyJudgeType() == 3){
			nowTimeMillis = context.getRecentlyDate().getTime(); //已测试，数据相同
		}
		Judge judge = judgeFactory.createJudge(define.getKeyJudgeType());
		if (judge instanceof AutoJudge) {
			((AutoJudge)judge).setNowTimeMillis(nowTimeMillis);
		}
			
		Result e = judge.judge(define,context);
		if(e==null){
			return false;
		}
		
		logger.info("判定通过"+context.getAppName()+"_"+context.getKeyName()+"_"+context.getContinuousAlarmTimes()+"");
		context.setKeyJudge(e.getE());
				
		context.setRangeMessage(e.getMessage());
		return true;
	}

	public KeyDefine getKeyDefineByContext(AlarmContext context) {
		String key = getKeyString(context.getAppId(),context.getKeyId());
		KeyDefine define = keyDefineMap.get(key);
		
		// 如果是异常，又没加入告警，则用默认keydefine
		String keyName = KeyCache.get().getKey(context.getKeyId()).getKeyName();
		if (define == null && keyName != null && keyName.toUpperCase().startsWith("EXCEPTION")) {
			define = new KeyDefine();
			define.setAppId(context.getAppId());
			define.setAppName(AppCache.get().getKey(context.getAppId()).getAppName());
			define.setKeyId(context.getKeyId());
			define.setKeyName(keyName);
			define.setKeyAliasName(null);
			define.setKeyType(2);
			// 采用阀值策略
			define.setKeyJudgeType(1);
			define.parseCommonRangeDefine("-1#25$09:00#23:59", "1");
		}
		return define;
	}
	
	
	/**
	 * 
	 * @param context
	 */
	private void checkcover(){
		
		
		
	}
	
	
	
	/**
	 * 如果在凌晨 1点到7点  ，告警必须是需要在5次以上
	 * @param context
	 * @return
	 */
	private boolean checkNight(AlarmContext context){
		
		Date date = context.getRecentlyDate();
		
		int hh = Integer.parseInt(format.format(date));
		
		if(hh >=1 && hh <=7){
			if(context.getContinuousAlarmTimes()<5){
				logger.info("如果在凌晨 1点到7点  ，告警必须是需要在5次以上 "+context.getAppName()+"_"+context.getKeyName()+"_"+context.getContinuousAlarmTimes()+"");
				return false;
			}else{
				
			}
		}
		
		return true;
		
	}
	
	
	
	/**
	 * 检查 key的类型，根据来决定多少次才告警
	 * @param context
	 * @return
	 */
	private boolean checkImportance(AlarmContext context){
		
		
		switch(context.getKeyType()){
			case 1: 
				
				if(context.getContinuousAlarmTimes() >=1 ){
					return true;
				}
				logger.info(context.getAppName()+"_"+context.getKeyName()+"_"+context.getContinuousAlarmTimes()+"连续次数不够");
				break;
				 //普通 连续4次才告警
			case 2:
				
				if(context.getContinuousAlarmTimes() >=0 ){
					return true;
				}
				logger.info(context.getAppName()+"_"+context.getKeyName()+"_"+context.getContinuousAlarmTimes()+"连续次数不够");
				break;  //重要连续2次才告警
			case 3: return true;  //紧急  立即告警
		}
		
		return false;
	}
	
	
	private String getKeyString(int appId, int keyId){
		return appId+"_"+keyId;
	}

	

}
