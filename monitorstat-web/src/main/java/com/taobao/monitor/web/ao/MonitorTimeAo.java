
package com.taobao.monitor.web.ao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.KeyPo;
import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.web.cache.AppCache;
import com.taobao.monitor.web.core.dao.impl.MonitorBackupDao;
import com.taobao.monitor.web.core.dao.impl.MonitorTimeDao;
import com.taobao.monitor.common.po.KeyValuePo;
import com.taobao.monitor.web.vo.KeyValueVo;
import com.taobao.monitor.web.vo.OtherKeyValueVo;
import com.taobao.monitor.web.vo.PvUrlPo;
import com.taobao.monitor.web.vo.ReportInfoPo;
import com.taobao.monitor.web.vo.SitePo;

/**
 *
 * @author xiaodu
 * @version 2010-4-19 上午11:36:00
 */
public class MonitorTimeAo {

	private static final Logger logger =  Logger.getLogger(MonitorTimeAo.class);

	private static MonitorTimeAo  ao = new MonitorTimeAo();
	private MonitorTimeDao dao = new MonitorTimeDao();
	private MonitorBackupDao backupDao = new MonitorBackupDao();



	private MonitorTimeAo(){
	}

	public static  MonitorTimeAo get(){
		return ao;
	}

	/**
	 * 获取临时表最近的一个key一台机器的数据
	 * @param appId
	 * @param keyid
	 * @param siteId
	 * @return
	 */
	public Map<Date,Double> findKeyLimitRecentlyData(int appId,int keyid,int siteId){
		return dao.findKeyLimitRecentlyData(appId, keyid, siteId);
	}
	
/**
 * 根据key的前缀名称，like出所有key，并查找出最近的5条记录
 * @param keyName
 * @param appName
 * @return
 */
	public Map<String,List<KeyValuePo>> findLikeKeyAverageByLimit5(String keyName,String appName){
		try {
			Map<String,List<KeyValuePo>> timeVomap = dao.findLikeKeyByLimit(keyName,appName);
			return timeVomap;
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}

    
	/**
	 * 查询时间内的 数据，按照监控点分割
	 * @param keyId
	 * @param appName
	 * @param start
	 * @param end
	 * @return Map<site,List<KeyValuePo>>
	 */
	public Map<String,List<KeyValuePo>> findKeyValueSiteByDate(int appId,int keyId,  java.util.Date start, java.util.Date end){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		
		
		try {
			cal.add(Calendar.DAY_OF_MONTH, -60);	//今天回去2个月的今天
			Date beforeTwoMonth = sdf.parse(sdf.format(cal.getTime()));
			Date tempDate = sdf.parse(sdf.format(start));
			
			//如果选择的日期是今天两个月之前的返回backup的数据，否则显示实时数据而不是备份数据
			if(tempDate.compareTo(beforeTwoMonth) > 0) {
				return dao.findKeyValueSiteByDate(appId, keyId, start, end);
			}
				
		} catch (ParseException e) {
			logger.error("", e);
		}
		return backupDao.findKeyValueSiteByDate(appId, keyId, start, end);
	}

	/**
	 * 获取应用某个key 在某段时间内的平均值
	 * @param keyId
	 * @param appName
	 * @param start
	 * @param end
	 * @return
	 */
	public double findKeyTimeRangeAverageValue(int appId, int keyId, java.util.Date start, java.util.Date end){
		try{
			List<KeyValuePo> poList = dao.findKeyValueByRangeDate(appId,keyId,start,end);
			double all = 0;
			int size = 0;
			for (KeyValuePo po : poList) {
				all = Arith.add(Double.parseDouble(po.getValueStr()), all);
				size++;
			}
			if (size > 0) {
				return Arith.div(all, size, 2);
			}
		}catch (Exception e) {
		}
		return -1;
	}
	
	/**
	 * 获取应用某个key 在某段时间内的最大值
	 * @param keyId
	 * @param appName
	 * @param start
	 * @param end
	 * @return
	 */
	public double findKeyTimeRangeMaxValue(int appId, int keyId, java.util.Date start, java.util.Date end){
		List<KeyValuePo> poList = dao.findKeyValueByRangeDate(appId,keyId,start,end);
		double max = -1;
		for (KeyValuePo po : poList) {
			if(max< Double.parseDouble(po.getValueStr())){
				max = Double.parseDouble(po.getValueStr());
			}
		}
		return max;
	}
	
	
	/**
	 * 获取应用 key 的一天所有的采集数据 map返回
	 * map 中的key 表示时间 HH：mm
	 * @param keyId
	 * @param appId
	 * @param start
	 * @return Map<HH:mm, KeyValuePo>
	 */
	public Map<String, KeyValuePo> findKeyValueByDate(int appId, int keyId, java.util.Date start){
		try {
			return dao.findKeyValueByDate(appId,keyId , start);
		} catch(Exception e) {
			logger.error("", e);
		}
		return null ;
	}
	
	/**
	 * 从临时表中获取应用的某一key的所有数据
	 * 将各主机做平均
	 * @param appId
	 * @param keyId
	 * @return key为时间  HH:mm 
	 */
	public Map<String, KeyValuePo> findKeyValueInLimit(int appId, int keyId) {
		try {
			return dao.findKeyValueInLimit(appId,keyId);
		} catch(Exception e) {
			logger.error("", e);
		}
		return null ;
	}
	
	
	/***
	 * 返回key为时间millis，value为各site这一时间m_data平均值，时间区间在start到end的map
	 * 为重要url的显示加入，如有其它应用需注意是否满足场景
	 * @author youji.zj
	 * 
	 * @param appId
	 * @param keyId
	 * @param start
	 * @param end
	 * @return
	 */
	public Map<String, Double> findKeyValueInTime(int appId, int keyId, Date start, Date end) {
		try {
			return dao.findKeyValueInTime(appId, keyId, start, end);
		} catch(Exception e) {
			logger.error("", e);
		}
		return null ;
	}
	
	/**
	 * 获取应用 key 的一天某段的采集数据 
	 * @param appId
	 * @param keyId
	 * @param start
	 * @param end
	 * @return
	 */
	public List<KeyValuePo> findKeyValueByRangeDate(int appId, int keyId, java.util.Date start,java.util.Date end){
		try {
			return dao.findKeyValueByRangeDate(appId,keyId , start,end);
		} catch(Exception e) {
			logger.error("", e);
		}
		return null ;
	}
	
	
	/**
	 * 获取应用 key 的一天某段的采集数据 
	 * @param appId
	 * @param keyId
	 * @param start
	 * @param end
	 * @return Map<time, value>
	 */
	public Map<String, KeyValuePo> findKeyValueMapByRangeDate(final int appId, final int keyId, java.util.Date start, java.util.Date end) {
		try {
			return dao.findKeyValueMapByRangeDate(appId, keyId , start, end);
		} catch(Exception e) {
			logger.error("", e);
		}
		return null ;
	}
	
	/**
	 * 查询在 limit 表中的最近 几条记录
	 * @param keyName
	 * @param appname
	 * @return
	 */
	public List<KeyValueVo> findLikeKeyByLimit(String keyName,String appname){
		try {
			return dao.findLikeKeyByLimit1(keyName, appname);
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}
	
	/**
	 * 
	 * 将limit 表中的数据全部查询出来
	 * @param keyName
	 * @param appname
	 * @return
	 * @throws Exception
	 */
	public Map<String,Map<String,Map<String,KeyValuePo>>> findLikeKeyByLimit(int appId) {
		return dao.findLikeKeyByLimit(appId);
	}



	/**
	 * 此方法统计在时间范围的数量，如果是多机器 将做 sum 后平均到每台机器
	 * @param key_name
	 * @param app_name
	 * @param collectTime yyyy-MM-dd
	 * @return
	 */
	public long countKeyByDate(int keyId,int appId,Date starTime,Date endTime){

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");

		String tableDate = sdf1.format(starTime);
		try {
			return dao.countKeyByDate(keyId,appId, tableDate, starTime,endTime);
		} catch (Exception e) {
			logger.error("", e);
		}
		return 0;
	}




	/**
	 * 根据时间获取数据
	 * @param keyName
	 * @param appname
	 * @param voList
	 * @return
	 */
	public Map<String,KeyValueVo> findTimeMonitorByTime(String keyName,String appname,List<KeyValueVo>  voList) {

		if(voList==null||voList.size()<1)return null;


		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		cal.setTime(voList.get(0).getDate());
		cal.add(Calendar.DAY_OF_MONTH, -7);
		String tableDate = sdf.format(cal.getTime());
		List<String> collectTimeList = new ArrayList<String>();
		for(KeyValueVo vo:voList){
			cal.setTime(vo.getDate());
			cal.add(Calendar.DAY_OF_MONTH, -7);
			collectTimeList.add(parseLogFormatDate.format(cal.getTime()));
		}
		try {
			return dao.findTimeMonitorByTime(keyName, appname, tableDate, collectTimeList);
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;

	}


	/**
	 * 这个是专门用于pv 按照url 划分的，，将按照监控点分割
	 * @param keyid
	 * @param appName
	 * @param start
	 * @param end
	 * @return
	 */
	public Map<String,List<PvUrlPo>> findPvUrlDetailByDate(String keyId,String appName,Date start,Date end){
		return dao.findPvUrlDetailByDate(keyId, appName, start, end);
	}




	/**
	 * 根据前缀名称 like 出所有这些前缀的key 和value,
	 * 并在ms_monitor_data_limit 表中查询最近的一条记录
	 * 这个是给前奏为OTHER 使用 结构为   OTHER_keytype_keyName_type
	 * @param Prefix
	 * @return
	 */
	public Map<String, OtherKeyValueVo> findKeyValueByLikeOtherFromLimitTable(String appname){
		return dao.findKeyValueByLikeOtherFromLimitTable(appname);
	}


	/**
	 * 删除监控的记录
	 * @param appId
	 */
	public boolean deleteMonitorData(int appId, String time) {
		
		return dao.deleteMonitorData(appId, time);
	}



    public void addMonitorData(String time, long app, long site, long key, long value) {
        dao.addMonitorData(time, app, site, key, value);
    }
    /**
     * 加入在线人数，5秒钟一次
     * @param time
     * @param app
     * @param site
     * @param key
     * @param value
     * @param gmt_create
     */
    public void addMonitorData(String time, long app, long site, long key, long value,String gmt_create) {
        dao.addMonitorData(time, app, site, key, value,gmt_create);
    }
    
	

	
	/**
	 * 获取一个应用时间表中正在使用的监控点
	 * @param appId
	 * @param collectTime
	 * @return
	 */
	public List<KeyPo> findAppAllMonitorKey(int appId, Date collectTime){
		return dao.findAppAllMonitorKey(appId, collectTime);
	}
	
	
	/**
	 * 获取在历史记录中最大的一个
	 * @param appId
	 * @param keyId
	 * @return
	 */
	public double getKeyMaxHistoryValue(int appId,int keyId){
		return dao.getKeyMaxHistoryValue(appId, keyId);
	}
	
	/**
	 * 添加历史记录中最大的一个
	 * @param appId
	 * @param keyId
	 * @param value
	 */
	public void addMaxHistoryValue(int appId,int keyId,float value){
		dao.addMaxHistoryValue(appId, keyId, value);
	}
	
	/**
	 * 修改历史记录中最大的一个
	 * @param appId
	 * @param keyId
	 * @param value
	 */
	public void updateMaxHistoryValue(int appId,int keyId,float value){
		dao.updateMaxHistoryValue(appId, keyId, value);
	}
	
	
	/**
	 * 统计某个应用的某个key 在一天时间内次数
	 * @param appId
	 * @param keyid
	 * @param collectTime  key site  value 统计值
	 * @return
	 */
	public Map<String,Long> sumKeyValueBySite(int appId,int keyid,Date collectTime){
		return dao.sumKeyValueBySite(appId, keyid, collectTime);
	}
	
	
	public Map<Integer, SitePo> findAllSite(){
		return dao.findAllSite();
	}
	/**
	 * 获取所有当前报表
	 * @return
	 */
	public List<ReportInfoPo> findAllReport(){
		return dao.findAllReport();
	}
	
	/**
	 * 获取应用高峰期的 key的 平均值
	 * @param appId
	 * @return
	 */
	public Map<String,Double> getAppSiteRushHourKeyAverageValue(int appId,int keyId){
		AppInfoPo app = AppCache.get().getKey(appId);
		
		String rushHours = app.getAppRushHours();
		int start_hour = 20;
		int end_hour = 22;
		try{
			if(rushHours !=null){
				String[] tmp = rushHours.split("-");
				if(tmp.length ==2){
					int t1 = Integer.parseInt(tmp[0]);
					int t2 = Integer.parseInt(tmp[1]);
					if(t1 <t2){
						start_hour = t1;
						end_hour = t2;
					}
				}
			}
		}catch (Exception e) {
		}
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, start_hour);
		Date start = cal.getTime();
		
		cal.set(Calendar.HOUR_OF_DAY, end_hour);
		Date end = cal.getTime();
		
		Map<String, List<KeyValuePo>> map = dao.findKeyValueSiteByDate(appId, keyId, start, end);
		
		Map<String,Double> mapKeySiteValue = new HashMap<String, Double>(); 
		
		for(Map.Entry<String, List<KeyValuePo>> entry:map.entrySet()){
			String siteName = entry.getKey();
			List<KeyValuePo> list =  entry.getValue();
			
			double sum = 0;
			
			for(KeyValuePo po:list){
				String v = po.getValueStr();
				sum = Arith.add(sum, Double.parseDouble(v));
			}
			
			String[] siteTmp = siteName.split("_");
			String site = siteTmp[0];
			Double d = mapKeySiteValue.get(site);
			if(d == null){
				mapKeySiteValue.put(site, Arith.div(sum, list.size(), 2));
			}else{
				mapKeySiteValue.put(site, Arith.div(Arith.div(sum, list.size(), 2)+d, 2, 2));
			}
		}
		
		return mapKeySiteValue;
	}
	
	
	/**
	 * 创建业务表结构
	 * @param date
	 * @param dbName
	 * @throws SQLException 
	 */
	public void createTimeTable(Date date,String dbName) throws SQLException{
		dao.createTimeTable(date, dbName);
	}
	
	/**
	 * 根据appid获取临时表中此app的记录条数
	 * @param appId
	 * 
	 */
	
	public int findAppCountInLimit(int appId) {
		
		return dao.findAppCountInLimit(appId);
	}
	
	
	/**
	 * 根据appid获取（现在时间-半个小时~~~现在时间)的区间内记录的数量
	 * @param appId
	 * @param date
	 */
	
	public int findAppCountInData(int appId, Date date) {
		return dao.findAppCountInData(appId, date);
	}
	
	/***
	 * 获取指定时间内对应appId,keyId的统计数据,以site_id进行分组
	 * @param appId
	 * @param keyId
	 * @param startTime
	 * @param endTime
	 * @param isLimit
	 * @return
	 */
	public Map<String, BigDecimal> findToalInRangeDate(final int appId, final int keyId, Date startTime, Date endTime, boolean isLimite) {
		return dao.findToalInRangeDate(appId, keyId, startTime, endTime, isLimite);
	}
	
	public Date findLatestDateInLimit(final int appId, final int keyId) {
		return dao.getLatestDataInLimit(appId, keyId);
	}
	
}
