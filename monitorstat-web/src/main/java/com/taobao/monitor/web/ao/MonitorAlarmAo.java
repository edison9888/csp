
package com.taobao.monitor.web.ao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jfree.util.Log;

import com.taobao.csp.dataserver.key.DBMediaKeyCache;
import com.taobao.monitor.alarm.n.key.KeyService;
import com.taobao.monitor.alarm.po.AlarmSendPo;
import com.taobao.monitor.alarm.po.ExtraKeyAlarmDefine;
import com.taobao.monitor.alarm.report.AppReportPO;
import com.taobao.monitor.alarm.report.ThreadTaskGetAppReportPO;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.KeyValuePo;
import com.taobao.monitor.web.cache.KeyCache;
import com.taobao.monitor.web.core.dao.impl.MonitorAlarmDao;
import com.taobao.monitor.web.util.DateFormatUtil;
import com.taobao.monitor.web.vo.AlarmDataForPageViewPo;
import com.taobao.monitor.web.vo.AlarmDataPo;
import com.taobao.monitor.web.vo.AlarmDescPo;
import com.taobao.monitor.web.vo.AlarmRecordPo;
import com.taobao.monitor.web.vo.UserAcceptInfo;
import com.taobao.util.CollectionUtil;

/**
 *
 * @author xiaodu
 * @version 2010-4-19 上午11:36:00
 */
public class MonitorAlarmAo {
	private final int TASK_MINITE_INTERVAL = 20;
	
	private static final Logger logger =  Logger.getLogger(MonitorAlarmAo.class);

	private static MonitorAlarmAo  ao = new MonitorAlarmAo();
	private MonitorAlarmDao dao = new MonitorAlarmDao();

	private MonitorAlarmAo(){
		
	}


	public static  MonitorAlarmAo get(){
		return ao;
	}

	
	

	/**
	 * 获取此key 是否存在一些机器的额外配置
	 * @param appId
	 * @param keyId
	 * @return
	 */
	public List<ExtraKeyAlarmDefine> getExtraKeyAlarmDefine(int appId,int keyId){
		return dao.getExtraKeyAlarmDefine(appId, keyId);
	}


	/**
	 * 记录告警信息
	 * 
	 * @param po
	 */
	public void recordAlarmEvent(int keyId,int appId,int siteId,Date time,String value) {
		dao.recordAlarmEvent(keyId, appId, siteId, time, value);
	}
		


	public boolean addKeyAlarm(AlarmDataPo po){
		boolean result = dao.addKeyAlarm(po);
		if(result){
			AlarmDataPo p = dao.getKeyAlarm(po.getAppId().toString(), po.getKeyId());
			KeyService.get().register(KeyService.createKeyDefine(p));
		}		
		return result;
	}
	/**
	 *查询出所有需要告警的key 信息
	 * @param name
	 * @return
	 */
	public List<AlarmDataPo> findAllAlarmKeyByAimAndLikeName(Integer appId,String keyName){
		return dao.findAllAlarmKeyByAimAndLikeName(appId, keyName,null);
	}
	
	/**
	 * 
	 * @param keyAlarmid
	 */
	public boolean deleteKeyAlarm(String keyAlarmid){
		
		AlarmDataPo po = dao.getKeyAlarmDefine(Integer.parseInt(keyAlarmid));
		
		boolean result = dao.deleteKeyAlarm(keyAlarmid);
		if(result){
			KeyService.get().unregister(KeyService.createKeyDefine(po));
		}
		return result;
	}
	
	public boolean updateKeyAlarm(AlarmDataPo po){
		boolean result = dao.updateKeyAlarm(po);
		if(result){
			KeyService.get().register(KeyService.createKeyDefine(po));
		}
		return result;
	}
	/**
	 * 根据ms_monitor_alarm 表中的数据 将ms_monitor_data_limit 中的数据
	 * @return
	 */
	public List<AlarmDataPo> findAllAlarmWithAlarmTable(int appId){
		
		List<AlarmDataPo> needAlarmKey = new ArrayList<AlarmDataPo>();
		
		try {
			List<AlarmDataPo> alarmDataList = dao.findAllAlarmWithAlarmTable(appId);
			
			if(alarmDataList.size()==0){
				return needAlarmKey;
			}
			
			Map<String,AlarmDataPo> defMap = dao.findAlarmKeyDefByAppId(appId);
			for(AlarmDataPo po:alarmDataList){
				String keyId = po.getKeyId();
				AlarmDataPo def = defMap.get(keyId);
				
				String keyName = KeyCache.get().getKey(Integer.parseInt(keyId)).getKeyName();
				boolean isNotException = true;
				if (keyName != null) {
					isNotException = !keyName.toUpperCase().startsWith("EXCEPTION");
				}
				
				// 没加入告警，或者类型不是exception的去掉
				if(def == null && isNotException){
					continue;
				}
				
				// def存在说明已经加入告警，否则说明是Exception，因为def不存在又不是Exception的在前面已经被continue
				if (def != null) {
					po.setId(def.getId());
					po.setAlarmAim(def.getAlarmAim());
					po.setAlarmDefine(def.getAlarmDefine());
					po.setAlarmFeature(def.getAlarmFeature());
					po.setAlarmType(def.getAlarmType());
					po.setKeyType(def.getKeyType());
				} else {
					po.setId("-1");
					po.setAlarmAim(KeyCache.get().getKey(Integer.parseInt(keyId)).getKeyType());
					po.setAlarmDefine("-1#25$09:00#23:59");
					po.setAlarmFeature(null);
					// 采用阀值策略
					po.setAlarmType("1");
					// 重要
					po.setKeyType(2);
				}
				
				needAlarmKey.add(po);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return needAlarmKey;
	}


	
//	/**
//	 * 取得所有告警人
//	 * @return
//	 */
//	public List<AlarmUserPo> findAllAlarmUser(){		
//		return dao.findAllAlarmUser();		
//	}
//	
//	
//	public boolean addAlarmUser(AlarmUserPo po){
//		boolean result = dao.addAlarmUser(po);
//		if(result){
//			AlarmUserManage.get().updateAlarmUser(po);
//		}
//		return result;
//	}
//	/**
//	 * 修改监控接收者
//	 * @param po
//	 */
//	public boolean updateAlarmUser(AlarmUserPo po){
//		
//		boolean result =dao.updateAlarmUser(po);
//		if(result){
//			AlarmUserManage.get().updateAlarmUser(po);
//		}
//		return result;
//	}
//	/**
//	 * 删除告警接收者
//	 * @param po
//	 */
//	public boolean deleteAlarmUser(AlarmUserPo po) {
//		boolean result =dao.deleteAlarmUser(po);
//		if(result){
//			AlarmUserManage.get().deleteAlarmUser(po);
//		}
//		return result;
//	}
	
	
	/**
     * 查询应用一天内的告警统计
     * @param appid
     * @param start 
     * 
     * @return
     */
    public List<AlarmRecordPo> getAppAlarmCountByDate(int appid,Date start){    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  	
    	String startTime = sdf.format(start)+" 00:00:00";
    	String endTime = sdf.format(start)+" 23:59:59";
    	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    	
    	Map<Integer,AlarmRecordPo>  poMap = new HashMap<Integer, AlarmRecordPo>();  	
    	
		try {
			List<AlarmRecordPo> list = dao.findRecordAlarmByAppIdAndTime(appid, sdf1.parse(startTime), sdf1.parse(endTime));
	    	
	    	for(AlarmRecordPo po:list){
	    		
	    		
	    		AlarmRecordPo r = poMap.get(po.getAlarmkeyId());
	    		if(r==null){
	    			r = new AlarmRecordPo();
	    			r.setAlarmkeyId(po.getAlarmkeyId());
	    			r.setAlarmKeyName(po.getAlarmKeyName());
	    			poMap.put(po.getAlarmkeyId(), r);
	    		}
	    		
	    		Integer siteAlarmNum = r.getSiteMap().get(po.getSiteName());
	    		if(siteAlarmNum==null){
	    			r.getSiteMap().put(po.getSiteName(), 1);
	    		}else{
	    			r.getSiteMap().put(po.getSiteName(), 1+siteAlarmNum);
	    		}    		
	    	}
		} catch (ParseException e) {
			logger.error(e);
		} 
		
		List<AlarmRecordPo> result = new ArrayList<AlarmRecordPo>();
		result.addAll(poMap.values());
    	return result;
    }
    
    
    /**'
     * 
     * @param appid
     * @param start
     * @return
     */
    public List<AlarmRecordPo> getAppAlarmByDate(int appid,Date start){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  	
    	String startTime = sdf.format(start)+" 00:00:00";
    	String endTime = sdf.format(start)+" 23:59:59";
    	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    	
    	try {
			List<AlarmRecordPo> list = dao.findRecordAlarmByAppIdAndTime(appid, sdf1.parse(startTime), sdf1.parse(endTime));
			return list;
		} catch (ParseException e) {
			logger.error(e);
		}
		return null;
    }
    
    /**
     * 
     * @param appid
     * @param keyid
     * @param start
     * @param end
     * @return
     */
    public Map<String,List<KeyValuePo>> getKeyAlarmCountBySite(int appid,int keyid,Date start,Date end){
    	List<AlarmRecordPo> list = dao.findRecordAlarmByAppIdAndTime(appid, start,end);
    	
    	Map<String,Map<String,KeyValuePo>> siteMap = new HashMap<String, Map<String,KeyValuePo>>();
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	for(AlarmRecordPo po:list){    		
    		if(keyid==po.getAlarmkeyId()){    			
    			Map<String,KeyValuePo> map = siteMap.get(po.getSiteName());
    			if(map==null){
    				map = new HashMap<String, KeyValuePo>();
    				siteMap.put(po.getSiteName(), map);
    			}
    			
    			String time = sdf.format(po.getCollectTime());
    			KeyValuePo kv = map.get(time);
    			if(kv==null){
    				kv = new KeyValuePo();
    				map.put(time, kv);
    			}
    			try {
					kv.setCollectTime(sdf.parse(time));
				} catch (ParseException e) {
					logger.error("", e);
				}
				kv.setValueStr(kv.getValueStr()==null?"1":(Integer.parseInt(kv.getValueStr())+1)+"");    			
    		}   		
    	}    	
    	
    	Map<String,List<KeyValuePo>> siteList = new HashMap<String, List<KeyValuePo>>();
    	
    	for(Map.Entry<String,Map<String,KeyValuePo>> entry:siteMap.entrySet()){
    		List<KeyValuePo> nList = new ArrayList<KeyValuePo>();
    		nList.addAll(entry.getValue().values());
    		siteList.put(entry.getKey(),nList);
    	}    	
    	return siteList;
    }
	/**
	 * 根据开始，结束时间和appId来返回所有的AlarmRecordPo
	 * @param appId
	 * @param start
	 * @param end
	 * @return
	 */
	public List<AlarmRecordPo> findAllExceptionMonitorDataDesc(int appId, Date startTime, Date endTime) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");  	
    	String start = sdf.format(startTime)+":00";
    	String end = sdf.format(endTime)+":59";
    	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    	
    	
    	try {
			List<AlarmRecordPo> list = dao.findExceptionMonitorDataDesc(appId, sdf1.parse(start), sdf1.parse(end));
			return list;
		} catch (ParseException e) {
			logger.error(e);
		}
		return null;
	}
    
    /**
     * 
     * @param appId
     * @param start
     * @param end
     * @return
     * @throws Exception 
     */
    public List<AlarmRecordPo> findExceptionMonitorDataDesc(int appId,Date start) throws Exception{
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  	
    	String startTime = sdf.format(start)+" 00:00:00";
    	String endTime = sdf.format(start)+" 23:59:59";
    	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    	try {
			return dao.findExceptionMonitorDataDesc(appId, sdf1.parse(startTime), sdf1.parse(endTime));
		} catch (ParseException e) {
			logger.error(e);
		}
		return null;
    }
    public List<String> findExceptionMonitorDataDesc1(int appId,int keyId,Date start){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  	
    	String startTime = sdf.format(start)+" 00:00:00";
    	String endTime = sdf.format(start)+" 23:59:59";
    	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    	
    	try {
			return dao.findExceptionMonitorDataDesc(appId, keyId, sdf1.parse(startTime), sdf1.parse(endTime));
		} catch (ParseException e) {
			logger.error(e);
		}
		return null;
    }
	/**
	 * 
	 * @param appId
	 * @param keyId
	 * @param collectTime
	 * @return
	 */
	public List<String> findExceptionMonitorDataDesc(int appId, int keyId, Date collectTime) {
		
		return dao.findExceptionMonitorDataDesc(appId, keyId, collectTime);
	}
    
    
    /**
     * 取得应用对应的告警key的数量
     * @param appName
     * @return
     */
    public long countAlarmKeyNum(String appName){
    	return dao.countAlarmKeyNum(appName);
    }
    
    
	
	public  List<AlarmRecordPo> findAlarmRecordByAppAndTime(int appId,Date startTime, Date endTime){
	   
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");  	
    	String start = sdf.format(startTime)+":00";
    	String end = sdf.format(endTime)+":59";
    	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
    	
    	try {
			List<AlarmRecordPo> list = dao.findRecordAlarmByAppIdAndTime(appId, sdf1.parse(start), sdf1.parse(end));
			return list;
		} catch (ParseException e) {
			logger.error(e);
		}
		return null;
    }
	
	/**
	 * 
	 * @return
	 */
	public boolean addRecordAlarmDesc(AlarmDescPo po){
		 return dao.recordAlarmDesc(po);
	}
	

	/**
	 * 
	 * @return
	 */
	public boolean addRecordAlarmDescRelation(AlarmDescPo po){
		return dao.recordAlarmDescRelation(po);
	}
	
	public AlarmDescPo findAlarmDescById(long alarm_id){
		try{
			AlarmDescPo alarmDescPo= dao.findAlarmDesc(alarm_id);
			return alarmDescPo;
		}catch(Exception e){
			logger.error("创建AlarmDescPO  出错", e);
		}
		return null;
	}
	
	public void deleteAlarmDataDesc(int appId,Date date){
		dao.deleteAlarmDataDesc(appId,date);
	}
	
	
	/**
	 * 添加告警接收信息
	 * @param info
	 */
	public void addUserAcceptMsg(int appId,int keyId,int userId,String msg,String alarmType){
		
		UserAcceptInfo info = new UserAcceptInfo();
		info.setAcceptDate(new Date());
		info.setAlarmMsg(msg);
		info.setAlarmType(alarmType);
		info.setAppId(appId);
		info.setKeyId(keyId);
		info.setUserId(userId);
		
		dao.addUserAcceptMsg(info);
	}
	
	
	public void addAlarmSend(int appId,String targetAim,String msg,String alarmType){
		
		AlarmSendPo info = new AlarmSendPo();
		info.setAcceptTime(new Date());
		info.setAlarmMsg(msg);
		info.setAlarmType(alarmType);
		info.setAppId(appId);
		info.setTargetAim(targetAim);
		dao.addAlarmSend(info);
	}
	
	
	/**
	 * 根据日期条件返回对应的告警信息集合
	 */
		public List<AlarmSendPo> findAllAlarmSend(String alarmDateStart,String alarmDateEnd) {
			return dao.findAllAlarmSend(alarmDateStart, alarmDateEnd);
		}
	
	
	/**
	 * 根据日期条件返回对应的告警信息集合
	 */
		public List<UserAcceptInfo> findAllUserAcceptMsg(String alarmDateStart,String alarmDateEnd) {
			
			return dao.findAllUserAcceptMsg(alarmDateStart, alarmDateEnd);
		}
	
	/**
	 * 返回指定日期有告警信息的所有不重复的user
	 */
	public List<String> findAllUser(String alarmDateStart,String alarmDateEnd) {
		
		return dao.findAllUser(alarmDateStart, alarmDateEnd);
	}
		
	/**
	 * 根据alarm_type是wangwang还是phone,返回list中包含该type的总数
	 * @param list
	 * @param alarm_type
	 * @return
	 */
	public double alarmCountByType(List<UserAcceptInfo> list,String alarm_type) {
		
		double all = 0;
		for(UserAcceptInfo u : list) {
			
			if(alarm_type.equals(u.getAlarmType())) {
				all ++;
			}
		}
		
		return all;
	}
	
/**
 * 返回根据exception过滤后的list
 * @param matcher
 * @return
 */
	public List<AlarmRecordPo> findMatcherException(List<AlarmRecordPo> list, String matcherStr) {
		
		List<AlarmRecordPo> filterList = new ArrayList<AlarmRecordPo>();
		  Pattern pattern = Pattern.compile(".*"+ matcherStr+ ".*",Pattern.CASE_INSENSITIVE);
		  Matcher matcher = null;
		for(AlarmRecordPo po : list) {
			matcher = pattern.matcher(po.getAlarmKeyName());
			if(matcher.matches()) {
				filterList.add(po);
			}
		}
		
		return filterList;
		
	}
	
	/**
	 * key添加指定主机以及额外备置
	 * @param appId
	 * @param keyId
	 * @param define
	 * @param hostList
	 * @return
	 */
	public boolean addExtraKeyAlarmDefine(int appId,int keyId, String define,List<String> hostList){
			return dao.addExtraKeyAlarmDefine(appId, keyId, define, hostList);
	}
	
	/**
	 * 删除特定备置的记录
	 * @param appId
	 * @param keyId
	 * @param hostId
	 */
	public boolean deleteExtraKeyAlarmDefine(int appId,int keyId, int hostId){
		
		return dao.deleteExtraKeyAlarmDefine(appId, keyId, hostId);
	}
	
	/**
	 * 关联监控系统对应该告警查询方法
	 * 根据时间段查询所有应用的告警数据
	 * @param start
	 * @param end
	 * @param keyLevelList  需要查询的key的告警级别
	 * @param appNameSet 需要查询的告警应用的名称列表
	 * @return
	 */
	public HashMap<String, List<AlarmDataForPageViewPo>> getHistoryTradeRalateAlarmMapByTime(Date start,Date end,List<Integer> keyLevelList,Set<String> appNameSet){
		HashMap<String, List<AlarmDataForPageViewPo>> historyAlarmMap = new HashMap<String,List<AlarmDataForPageViewPo>>();
		if(CollectionUtil.isEmpty(appNameSet)){
			return historyAlarmMap;
		}
		for(String appName:appNameSet){
			try{
		        ArrayList<Future<List<AlarmDataForPageViewPo>>> results = new ArrayList<Future<List<AlarmDataForPageViewPo>>>();    //Future 相当于是用来存放Executor执行的结果的一种容器  
		        Date concurrent = start;
		        Calendar c = Calendar.getInstance();
		        
		    	//最多20个线程数
		    	ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
		    			5, 20, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(500));
		    	
		        CountDownLatch doneSignal = new CountDownLatch(getIntervalCount(start,end));  //判断总共多少个子任务
		        threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		        //这里的时间点转化为间隔，做成多线程
		        while(concurrent.before(end)){
		        	Date taskStart = concurrent;
		        	c.setTime(concurrent);
		        	c.add(Calendar.MINUTE, TASK_MINITE_INTERVAL);
		        	Date taskEnd = c.getTime();
		        	if( c.after(end) ){//精确查询时间
		        		taskEnd = end;
		        	} 
		        	//执行子任务
			        results.add(threadPool.submit(new TaskGetHistoryTradeRalateAlarmMapByTimeInterval(appName, taskStart, taskEnd, doneSignal)));
		        	  
		        	//设置当前任务的时间点
		        	concurrent = taskEnd;
		        }
	       
		        doneSignal.await();  //所有子任务执行完成后，再获取任务执行的结果
		        List<AlarmDataForPageViewPo> viewPoList = new ArrayList<AlarmDataForPageViewPo>();
		        
		        for (Future<List<AlarmDataForPageViewPo>> fs : results) {  
		            if (fs.isDone()) {  
		            	viewPoList.addAll(fs.get());  
		            } else {  
		            	Log.info("TaskGetHistoryTradeRalateAlarmMapByTimeInterval not ends,fs=" + fs.toString());
		            }  
		        }  
		        //关闭线程池
		        threadPool.shutdown();  
				
				if(CollectionUtil.isEmpty(viewPoList)){
					continue;
				}
				//假设传递的优先级别列表为空，则默认返回所有告警记录
				if(CollectionUtil.isEmpty(keyLevelList)){
					historyAlarmMap.put(appName, viewPoList);
				}else{
					List<AlarmDataForPageViewPo> viewPoListWithKeyLevel = new ArrayList<AlarmDataForPageViewPo>();
					for(AlarmDataForPageViewPo viewPo:viewPoList){
						Integer keyLevel = DBMediaKeyCache.get().getKeyLevelByKeyName(viewPo.getKeyName());
						if(keyLevel == null){
							viewPoListWithKeyLevel.add(viewPo);
							continue;
						}
						if(keyLevelList.contains(keyLevel)){
							viewPoListWithKeyLevel.add(viewPo);
						}
					}
					
					Collections.sort(viewPoListWithKeyLevel,new Comparator<AlarmDataForPageViewPo>() {
						@Override
						public int compare(AlarmDataForPageViewPo p1, AlarmDataForPageViewPo p2) {
							if( p1.getAlarmTime().after(p2.getAlarmTime()) ){
								return -1;
							} else if( p1.getAlarmTime().before(p2.getAlarmTime()) ){
								return 1;
							}
							return 0;
						}
					});	
					
					historyAlarmMap.put(appName, viewPoListWithKeyLevel);
				}
	        }catch(Exception e){ 
	        	Log.info("getHistoryTradeRalateAlarmMapByTime exception,start=" + start + ",end=" + end + ",appName=" + appName,e);
	        }
		} 
		return historyAlarmMap;
	}
	
	public int getIntervalCount(Date start,Date end){
		Date concurrent = start;
		int  count = 0;
		Calendar c = Calendar.getInstance();
		while(concurrent.before(end)){
	        	c.setTime(concurrent);
	        	c.add(Calendar.MINUTE, TASK_MINITE_INTERVAL);
	        	concurrent = c.getTime();
	        	count ++;
	    }
		return count;
	}
	/**
	 * 查询 告警表中最早时间
	 * @return Date
	 */
	public Date getRecordEarlistDate(){
		return dao.findRecordEarliestTime();
	}
	
	/**
	 * 交易关联系统告警日报的告警数据
	 * @param startTime
	 * @return appReportPoList
	 * @author hongbing.ww
	 */
	public List<AppReportPO> getAppReportPoListForAlarmReport(Date startTime){
		List<AppReportPO> appReportPoList = new ArrayList<AppReportPO>();
		List<String> appNameList = new ArrayList<String>();
		appNameList.add("wass");
		appNameList.add("sirius");
		appNameList.add("wtm");
		appNameList.add("wvs");
		appNameList.add("wmac");
		appNameList.add("wdetail");
		appNameList.add("mtop");
		
		List<Future<AppReportPO>> rtPoList = new ArrayList<Future<AppReportPO>>();
		try {
			ThreadPoolExecutor threadPool = new ThreadPoolExecutor(12, 12, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
			CountDownLatch doneSignal = new CountDownLatch(12);
			threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
			Calendar cal = Calendar.getInstance();
			//可以设定的查询时间
			if(startTime != null){
				cal.setTime(startTime);
			} 
			cal.add(Calendar.DAY_OF_MONTH, -1);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			Date start = cal.getTime();
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
			Date end = cal.getTime();
			for(String appName:appNameList){
				rtPoList.add( threadPool.submit( new ThreadTaskGetAppReportPO(appName, start, end, doneSignal)));
			}
			doneSignal.await();
			
			for (Future<AppReportPO> fr : rtPoList) {  
	            if (fr.isDone()) {  
	            	appReportPoList.add(fr.get());  
	            } else {
	            	logger.warn("ReportTimerTask lost query info!");
	            }
	        }
	        threadPool.shutdown(); 
		} catch (Exception e) {
			logger.warn("getMultiRealTimeTrade error:", e);
		}
		return appReportPoList;
	}
	
	/**
	 * 获得单个应用的告警信息
	 * @param appName
	 * @param start
	 * @param end
	 * @return
	 */
	public List<AlarmDataForPageViewPo> getSingleAppAlarmList(String appName, Date start, Date end, int size, int num){
		try{
			List<AlarmDataForPageViewPo> appAlarmDataList = dao.findRecordAlarmByAppNameAndTime(appName, start, end, size, num);
			return appAlarmDataList;
		} catch(Exception e){
			logger.warn("getSingleAppAlarmList error:", e);
		}
		return null;
	}
	
	/**
	 * 获得单个应用的告警记录数
	 * 用于分页
	 * @param appName
	 * @param start
	 * @param end
	 * @return
	 */
	public int getSingleAppAlarmCount(String appName, Date start, Date end){
		return dao.getAlarmCountByAppNameAndTime(appName, start, end);
	}
}
