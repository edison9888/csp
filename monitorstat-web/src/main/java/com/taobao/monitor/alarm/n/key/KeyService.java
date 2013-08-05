
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
 * @version 2011-2-26 ����06:53:40
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
	 * ��� key�Ķ����࣬map��key ʹ��appId_keyId ��϶���
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
			
			logger.info(define.getAppName()+"_"+define.getKeyName()+"���޸�");
		}
	}
	
	public void unregister(KeyDefine define){
		if(define!=null){
			String key = getKeyString(define.getAppId(),define.getKeyId());
			keyDefineMap.remove(key);
			logger.info(define.getAppName()+"_"+define.getKeyName()+"��ɾ��");
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
	 * ����Ƿ���Ҫ�����key �����澯
	 * @param context
	 */
	public boolean lookup(AlarmContext context){
		KeyDefine define = getKeyDefineByContext(context);
		
		if(define == null){
			return false;
		}
		//��������޸���context �����ݣ�λ�ò����޸�
		constructContextByKeyDefine(context, define);
			
		//��� һ���ɼ����ݣ����ϴ�
		boolean exist = record.isExist(context.getAppId(), context.getKeyId(),context.getSiteId(), context.getRecentlyDate());
		if(exist){
			logger.info("�Ѿ��ж���������"+context.getAppName()+"_"+context.getKeyName()+"_"+context.getContinuousAlarmTimes()+"");
			return false;//�Ѿ��ж���������
		}
			
		if(!doJudgeAndSetContext(context, define)) return false;
				
		for(AlarmEvent event:eventList){
			event.onAlarm(context);
		}
				
		boolean recently = record.isRecently(context.getAppId(), context.getKeyId(),context.getSiteId(), context.getRecentlyDate());
				
		record.record(context.getAppId(), context.getKeyId(),context.getSiteId(), context.getRecentlyDate());				
		context.setContinuousAlarmTimes(record.getContinuousAlarmTimes(context.getAppId(), context.getKeyId(),context.getSiteId()));				
				
		if(recently){
			logger.info("���1�����Ѿ������"+context.getAppName()+"_"+context.getKeyName()+"_"+context.getContinuousAlarmTimes()+"");					
			return false;//�������1���������Ѿ����֣�����
			}
				
		if(!checkNight(context)){//������賿 1�㵽7��  ���澯��������Ҫ��5������
				return false;
			}
				
		if(!checkImportance(context)){//�ȼ�����
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
			calendar.add(Calendar.MINUTE, -2); //���������ӣ�ȷ�����ռ�����
			context.setRecentlyDate(calendar.getTime());
		}
	}

	public boolean doJudgeAndSetContext(AlarmContext context, KeyDefine define) {
		long nowTimeMillis =0;
		if(define.getKeyJudgeType() == 3){
			nowTimeMillis = context.getRecentlyDate().getTime(); //�Ѳ��ԣ�������ͬ
		}
		Judge judge = judgeFactory.createJudge(define.getKeyJudgeType());
		if (judge instanceof AutoJudge) {
			((AutoJudge)judge).setNowTimeMillis(nowTimeMillis);
		}
			
		Result e = judge.judge(define,context);
		if(e==null){
			return false;
		}
		
		logger.info("�ж�ͨ��"+context.getAppName()+"_"+context.getKeyName()+"_"+context.getContinuousAlarmTimes()+"");
		context.setKeyJudge(e.getE());
				
		context.setRangeMessage(e.getMessage());
		return true;
	}

	public KeyDefine getKeyDefineByContext(AlarmContext context) {
		String key = getKeyString(context.getAppId(),context.getKeyId());
		KeyDefine define = keyDefineMap.get(key);
		
		// ������쳣����û����澯������Ĭ��keydefine
		String keyName = KeyCache.get().getKey(context.getKeyId()).getKeyName();
		if (define == null && keyName != null && keyName.toUpperCase().startsWith("EXCEPTION")) {
			define = new KeyDefine();
			define.setAppId(context.getAppId());
			define.setAppName(AppCache.get().getKey(context.getAppId()).getAppName());
			define.setKeyId(context.getKeyId());
			define.setKeyName(keyName);
			define.setKeyAliasName(null);
			define.setKeyType(2);
			// ���÷�ֵ����
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
	 * ������賿 1�㵽7��  ���澯��������Ҫ��5������
	 * @param context
	 * @return
	 */
	private boolean checkNight(AlarmContext context){
		
		Date date = context.getRecentlyDate();
		
		int hh = Integer.parseInt(format.format(date));
		
		if(hh >=1 && hh <=7){
			if(context.getContinuousAlarmTimes()<5){
				logger.info("������賿 1�㵽7��  ���澯��������Ҫ��5������ "+context.getAppName()+"_"+context.getKeyName()+"_"+context.getContinuousAlarmTimes()+"");
				return false;
			}else{
				
			}
		}
		
		return true;
		
	}
	
	
	
	/**
	 * ��� key�����ͣ��������������ٴβŸ澯
	 * @param context
	 * @return
	 */
	private boolean checkImportance(AlarmContext context){
		
		
		switch(context.getKeyType()){
			case 1: 
				
				if(context.getContinuousAlarmTimes() >=1 ){
					return true;
				}
				logger.info(context.getAppName()+"_"+context.getKeyName()+"_"+context.getContinuousAlarmTimes()+"������������");
				break;
				 //��ͨ ����4�βŸ澯
			case 2:
				
				if(context.getContinuousAlarmTimes() >=0 ){
					return true;
				}
				logger.info(context.getAppName()+"_"+context.getKeyName()+"_"+context.getContinuousAlarmTimes()+"������������");
				break;  //��Ҫ����2�βŸ澯
			case 3: return true;  //����  �����澯
		}
		
		return false;
	}
	
	
	private String getKeyString(int appId, int keyId){
		return appId+"_"+keyId;
	}

	

}
