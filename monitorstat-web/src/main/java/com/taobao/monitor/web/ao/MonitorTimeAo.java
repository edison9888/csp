
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
 * @version 2010-4-19 ����11:36:00
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
	 * ��ȡ��ʱ�������һ��keyһ̨����������
	 * @param appId
	 * @param keyid
	 * @param siteId
	 * @return
	 */
	public Map<Date,Double> findKeyLimitRecentlyData(int appId,int keyid,int siteId){
		return dao.findKeyLimitRecentlyData(appId, keyid, siteId);
	}
	
/**
 * ����key��ǰ׺���ƣ�like������key�������ҳ������5����¼
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
	 * ��ѯʱ���ڵ� ���ݣ����ռ�ص�ָ�
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
			cal.add(Calendar.DAY_OF_MONTH, -60);	//�����ȥ2���µĽ���
			Date beforeTwoMonth = sdf.parse(sdf.format(cal.getTime()));
			Date tempDate = sdf.parse(sdf.format(start));
			
			//���ѡ��������ǽ���������֮ǰ�ķ���backup�����ݣ�������ʾʵʱ���ݶ����Ǳ�������
			if(tempDate.compareTo(beforeTwoMonth) > 0) {
				return dao.findKeyValueSiteByDate(appId, keyId, start, end);
			}
				
		} catch (ParseException e) {
			logger.error("", e);
		}
		return backupDao.findKeyValueSiteByDate(appId, keyId, start, end);
	}

	/**
	 * ��ȡӦ��ĳ��key ��ĳ��ʱ���ڵ�ƽ��ֵ
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
	 * ��ȡӦ��ĳ��key ��ĳ��ʱ���ڵ����ֵ
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
	 * ��ȡӦ�� key ��һ�����еĲɼ����� map����
	 * map �е�key ��ʾʱ�� HH��mm
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
	 * ����ʱ���л�ȡӦ�õ�ĳһkey����������
	 * ����������ƽ��
	 * @param appId
	 * @param keyId
	 * @return keyΪʱ��  HH:mm 
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
	 * ����keyΪʱ��millis��valueΪ��site��һʱ��m_dataƽ��ֵ��ʱ��������start��end��map
	 * Ϊ��Ҫurl����ʾ���룬��������Ӧ����ע���Ƿ����㳡��
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
	 * ��ȡӦ�� key ��һ��ĳ�εĲɼ����� 
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
	 * ��ȡӦ�� key ��һ��ĳ�εĲɼ����� 
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
	 * ��ѯ�� limit ���е���� ������¼
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
	 * ��limit ���е�����ȫ����ѯ����
	 * @param keyName
	 * @param appname
	 * @return
	 * @throws Exception
	 */
	public Map<String,Map<String,Map<String,KeyValuePo>>> findLikeKeyByLimit(int appId) {
		return dao.findLikeKeyByLimit(appId);
	}



	/**
	 * �˷���ͳ����ʱ�䷶Χ������������Ƕ���� ���� sum ��ƽ����ÿ̨����
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
	 * ����ʱ���ȡ����
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
	 * �����ר������pv ����url ���ֵģ��������ռ�ص�ָ�
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
	 * ����ǰ׺���� like ��������Щǰ׺��key ��value,
	 * ����ms_monitor_data_limit ���в�ѯ�����һ����¼
	 * ����Ǹ�ǰ��ΪOTHER ʹ�� �ṹΪ   OTHER_keytype_keyName_type
	 * @param Prefix
	 * @return
	 */
	public Map<String, OtherKeyValueVo> findKeyValueByLikeOtherFromLimitTable(String appname){
		return dao.findKeyValueByLikeOtherFromLimitTable(appname);
	}


	/**
	 * ɾ����صļ�¼
	 * @param appId
	 */
	public boolean deleteMonitorData(int appId, String time) {
		
		return dao.deleteMonitorData(appId, time);
	}



    public void addMonitorData(String time, long app, long site, long key, long value) {
        dao.addMonitorData(time, app, site, key, value);
    }
    /**
     * ��������������5����һ��
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
	 * ��ȡһ��Ӧ��ʱ���������ʹ�õļ�ص�
	 * @param appId
	 * @param collectTime
	 * @return
	 */
	public List<KeyPo> findAppAllMonitorKey(int appId, Date collectTime){
		return dao.findAppAllMonitorKey(appId, collectTime);
	}
	
	
	/**
	 * ��ȡ����ʷ��¼������һ��
	 * @param appId
	 * @param keyId
	 * @return
	 */
	public double getKeyMaxHistoryValue(int appId,int keyId){
		return dao.getKeyMaxHistoryValue(appId, keyId);
	}
	
	/**
	 * �����ʷ��¼������һ��
	 * @param appId
	 * @param keyId
	 * @param value
	 */
	public void addMaxHistoryValue(int appId,int keyId,float value){
		dao.addMaxHistoryValue(appId, keyId, value);
	}
	
	/**
	 * �޸���ʷ��¼������һ��
	 * @param appId
	 * @param keyId
	 * @param value
	 */
	public void updateMaxHistoryValue(int appId,int keyId,float value){
		dao.updateMaxHistoryValue(appId, keyId, value);
	}
	
	
	/**
	 * ͳ��ĳ��Ӧ�õ�ĳ��key ��һ��ʱ���ڴ���
	 * @param appId
	 * @param keyid
	 * @param collectTime  key site  value ͳ��ֵ
	 * @return
	 */
	public Map<String,Long> sumKeyValueBySite(int appId,int keyid,Date collectTime){
		return dao.sumKeyValueBySite(appId, keyid, collectTime);
	}
	
	
	public Map<Integer, SitePo> findAllSite(){
		return dao.findAllSite();
	}
	/**
	 * ��ȡ���е�ǰ����
	 * @return
	 */
	public List<ReportInfoPo> findAllReport(){
		return dao.findAllReport();
	}
	
	/**
	 * ��ȡӦ�ø߷��ڵ� key�� ƽ��ֵ
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
	 * ����ҵ���ṹ
	 * @param date
	 * @param dbName
	 * @throws SQLException 
	 */
	public void createTimeTable(Date date,String dbName) throws SQLException{
		dao.createTimeTable(date, dbName);
	}
	
	/**
	 * ����appid��ȡ��ʱ���д�app�ļ�¼����
	 * @param appId
	 * 
	 */
	
	public int findAppCountInLimit(int appId) {
		
		return dao.findAppCountInLimit(appId);
	}
	
	
	/**
	 * ����appid��ȡ������ʱ��-���Сʱ~~~����ʱ��)�������ڼ�¼������
	 * @param appId
	 * @param date
	 */
	
	public int findAppCountInData(int appId, Date date) {
		return dao.findAppCountInData(appId, date);
	}
	
	/***
	 * ��ȡָ��ʱ���ڶ�ӦappId,keyId��ͳ������,��site_id���з���
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
