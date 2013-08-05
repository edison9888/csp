
package com.taobao.monitor.web.ao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.monitor.baseline.db.po.KeyValueBaseLinePo;
import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.common.util.Constants;
import com.taobao.monitor.web.cache.AppCache;
import com.taobao.monitor.web.core.dao.impl.MonitorDayDao;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.KeyValuePo;
import com.taobao.monitor.web.vo.MonitorVo;

/**
 * 
 * @author xiaodu
 * @version 2010-4-19 ����11:36:00
 */
public class MonitorDayAo {
	
	private static final Logger logger =  Logger.getLogger(MonitorDayAo.class);
	
	private static MonitorDayAo  ao = new MonitorDayAo();
	private MonitorDayDao dao = new MonitorDayDao();
	
	private MonitorDayAo(){
	
	}
	
	
	public static  MonitorDayAo get(){
		return ao;
	}
	
	public Map<String,Double> findAppMachineNumber(int appDayId,  Date start,Date end){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Map<Integer,String> map = MonitorDayAo.get().getKeyMap();
		Set<Integer> keyList = new HashSet<Integer>();
		for(Map.Entry<Integer,String> entry:map.entrySet()){
			if(entry.getValue().startsWith("HOSTS_")){
				keyList.add(entry.getKey());
			}
		}
		Map<String,Double> mapMachine = new HashMap<String, Double>();
		
		for(Integer keyId:keyList){
			List<KeyValuePo> listQps = MonitorDayAo.get().findMonitorCountByDate(appDayId, keyId, start,end);
			for(KeyValuePo po:listQps){
				String key = sdf.format(po.getCollectTime());
				Double v = mapMachine.get(key);
				if(v == null){
					mapMachine.put(key, Double.parseDouble(po.getValueStr()));
				}else{
					mapMachine.put(key, Double.parseDouble(po.getValueStr())+v);
				}
				
			}
		}
		
		return mapMachine;
		
	}
	
	
	/**
	 * ����keyId��keyValue��map
	 * @return
	 */
	public Map<Integer,String> getKeyMap(){
		return dao.getKeyMap();
	}
	/**
	 * ��ѯͳ�Ʊ���ʱ����ڵ�����
	 * @param appId
	 * @param keyId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<KeyValuePo> findMonitorCountByDate(int appId,int keyId,Date startDate,Date endDate){
		return dao.findMonitorCountByDate(appId, keyId, startDate, endDate);
	}
	
	
	/**
	 * 
	 * @param date yyyy-MM-dd
	 * @return
	 * @throws Exception
	 */
	public Map<Integer, MonitorVo> findMonitorCountMapByDate(String date) throws Exception{
		if(dao!=null){
			
			Map<Integer, MonitorVo> result = dao.findMonitorCountMapByDate(date);			
			return result;
		}else{
			return null;
		}
	}
	
	
	public Map<Date,Integer> findAppHostCount(int appId,Integer[] keyIds,Date start,Date end ){
		
		Map<Date,Integer> map = new HashMap<Date, Integer>();
		
		for(Integer keyId:keyIds){
			List<KeyValuePo> list = dao.findMonitorCountByDate(appId,keyId,start,end);
			for(KeyValuePo po:list){
				Integer n = map.get(po.getCollectTime());
				if(n == null){
					map.put(po.getCollectTime(), Integer.parseInt(po.getValueStr()));
				}else{
					map.put(po.getCollectTime(), n+Integer.parseInt(po.getValueStr()));
				}
			}
			
		}
		
		return map;
	}
	

	
	public Map<String, KeyValuePo> findMonitorCountKeyMapByDate(int appId,String date){
		try {
			return dao.findMonitorCountKeyMapByDate(appId, date);
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}
	
	/**
	 * 
	 * @param date yyyy-MM-dd
	 * @return
	 * @throws Exception
	 */
	public Map<Integer, MonitorVo> findMonitorCountMapByDate(Integer appid,String date) throws Exception{
		if(dao!=null){
			
			Map<Integer, MonitorVo> result = dao.findMonitorCountMapByDate(appid,date);
			return result;
		}else{
			return null;
		}
	}
    /**
	 *
	 * @param date yyyy-MM-dd
	 * @return
	 * @throws Exception
	 */
	public Map<Long, Long> findMonitorCountMapAsValueByDate(long appId,String date,long[] keyId) throws Exception{
        if (dao != null) {
            try {
                Map<Long, Long> result = dao.findMonitorCountMapAsValueByDate(appId, date, keyId);
                return result;
            } catch (Exception e) {
                logger.error("��ѯ��������WEB�����ʼ�", e);
            }
        }
        return null;
	}
	
	public Map<Long, String> findMonitorCountStrMapAsValueByDate(long appId,String date,long[] keyId) throws Exception{
        if (dao != null) {
            try {
                Map<Long, String> result = dao.findMonitorCountStrMapAsValueByDate(appId, date, keyId);
                return result;
            } catch (Exception e) {
                logger.error("��ѯ����", e);
            }
        }
        return null;
	}
	public KeyValuePo findKeyValueFromCountByDate(int keyId,int appid,String date){
		return dao.findKeyValueFromCountByDate(keyId, appid, date);
	}
	/**
	 * ȡ������һ��
	 * @param keyId
	 * @param appid
	 * @return
	 */
	public KeyValuePo findKeyValueFromCountMax(int keyId,int appid){
		return dao.findKeyValueFromCountMax(keyId, appid);
	}
	
	/**
	 * ����CӦ�õ�pv�ۼ�m_data����ֵ��Ӧ�ð�������keyΪIN_HSF-ProviderDetail��ͷ�ҽ�β��COUNTTIMES��
	 * @param appId
	 * @param date
	 * @return
	 */
	public long findCappPvCountByDate(int appId,String date){
	
		return dao.findCappPvCountByDate(appId, date);
	}
	
	/**
	 *
	 * @param appId
	 * @param date
	 * @return
	 */
	public String findCappRtCountByDate(int appId,String date)  throws Exception {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
		Date c = sdf.parse(date);
		Calendar cal = Calendar.getInstance();
		cal.setTime(c);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date colectDate = cal.getTime();
		
		List<KeyValuePo> listPo = dao.findCappRtCountByTime(appId, colectDate);//���ص���20:30~22:30ÿ��key��ƽ��ֵ��key���б�
		//�����ǶԷ��ص�20:30~22:30�ļ�¼����ƽ����
		Double aveValue = 0d;
		int size = 0;
		for(KeyValuePo po:listPo){
			Double value = Double.parseDouble(po.getValueStr());
			if(value < 1000) {		//����100���޳���
				aveValue= Arith.add(aveValue, value);
				size++;
			}
		}			
		
		if(size>0){
			aveValue = Arith.div(aveValue, size,2);				//�ܵ�����/�ӿ���			
			return 	aveValue.toString();				
		}
		return "0";	
		}
	

	/**
	 * �����ṩ��CӦ�ò�ѯqps�ģ�����CӦ���������key��mData��һ��ƽ��ֵ
	 * @param appId
	 * @param collectTime		//yyyy-MM-dd
	 * @return
	 * @throws Exception
	 */
	public String findCappQpsCountByDate(Integer appId,String collectTime) throws Exception{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");	
		Date c = sdf.parse(collectTime);
		Calendar cal = Calendar.getInstance();
		cal.setTime(c);
		cal.set(Calendar.HOUR_OF_DAY, 20);
		cal.set(Calendar.MINUTE, 30);
		cal.set(Calendar.SECOND, 0);
		Date startDate = cal.getTime();
		cal.set(Calendar.HOUR_OF_DAY, 22);
		cal.set(Calendar.MINUTE, 30);
		cal.set(Calendar.SECOND, 0);
		Date endDate = cal.getTime();
		
		
		List<KeyValuePo> listPo = dao.findCappQpsCountByTime(appId, startDate, endDate);//���ص���20:30~22:30ÿһ���ӵļ�¼
		//�����ǶԷ��ص�20:30~22:30�ļ�¼����ƽ����
		Double aveValue = 0d;
		int size = 0;
		for(KeyValuePo po:listPo){
			Double value = Double.parseDouble(po.getValueStr());
			aveValue= Arith.add(aveValue, value);
			size++;
		}			
		
		if(size>0){
			aveValue = Arith.div(aveValue, size,2);						
			return 	aveValue.toString();				
		}
		return "0";
	}

	/**
	 *  * ���ǲ�ѯCӦ����ĳһ������й���key���б����key�б��ͳ��pv��ʱ���ã�������cӦ�����й�����keyΪƥ��IN_HSF-ProviderDetail\\_%\\_COUNTTIMES��ֵ
	 * @param appId
	 * @param keyId
	 * @param date
	 * @return
	 * @throws Exception 
	 */
	public List<KeyValuePo> findCappPvDetailByTime(Integer appId,String date) throws Exception{
			
		return	dao.findCappPvDetailByTime(appId, date);
	
	}
		

	/**
	 *  * ���ǲ�ѯCӦ������ĳһ������й���key���б����key�б��ͳ��rt��ʱ���ã�������cӦ�����й�����keyΪƥ��IN_HSF-ProviderDetail\\_%\\_COUNTTIMES��ֵ
	 * @param appId
	 * @param keyId
	 * @param date
	 * @return
	 * @throws Exception 
	 */
	public List<KeyValuePo> findCappRtDetailByTime(Integer appId,String date) throws Exception{
		
		return dao.findCappRtDetailByTime(appId, date);
	}
	
	public String parseCenterAppQps(Integer appId,Integer keyId,String collectTime){
		SimpleDateFormat sdf1 = new SimpleDateFormat("HHmm");	
		List<KeyValuePo> listPo = findMonitorDataListByTime(appId,keyId,collectTime);
		Double aveValue = 0d;
		int size = 0;
		for(KeyValuePo po:listPo){
			Date date = po.getCollectTime();			
			Double value = Double.parseDouble(po.getValueStr());
			int time = Integer.parseInt(sdf1.format(date))	;
			if(time>=2030&&time<=2230){
				aveValue= Arith.add(aveValue, value);
				size++;
			}			
		}
		if(size>0){
			aveValue = Arith.div(aveValue, size,2);						
			return 	aveValue.toString();				
		}
		return "0";
	}
	
	/**
	 * ��ѯĳ��key һ���ڵ�����
	 * @param appId
	 * @param keyId
	 * @param collectDate yyyy-MM-dd
	 * @return
	 */
	public List<KeyValuePo> findMonitorDataListByTime(Integer appId,Integer keyId,String collectTime){
		
		if(collectTime==null)return null;
		try {
			return dao.findKeyDetailByTime(appId, keyId, collectTime);
		} catch (Exception e) {
			logger.error("��ѯĳ��key һ���ڵ����� ����", e);
		}
		
		return new ArrayList<KeyValuePo>();
	}

	
	
	/**
	 * ��ѯĳ��key һ���ڵ�����
	 * @param appId
	 * @param keyId
	 * @param startCollectDate yyyy-MM-dd
	 * @param endCollectDate yyyy-MM-dd
	 * @return
	 */
	public List<KeyValuePo> findMonitorDataListByDate(Integer appId,Integer keyId,String startCollectDate,String endCollectDate){
		
		try {
			return dao.findKeyDetailByDate(appId, keyId, startCollectDate,endCollectDate);
		} catch (Exception e) {
			logger.error("��ѯĳ��key һ���ڵ����� ����", e);
		}
		
		return null;
	}
	
	
	/**
	 * ��ȡȫ��app 
	 * @return
	 */
	public List<AppInfoPo> findAllApp(){
		return dao.findAllApp();
	}
	
	
	/**
	 * 
	 * @param appid
	 * @param date
	 * @return
	 */
	public Map<Integer,Map<Integer,KeyValueBaseLinePo>> findMonitorBaseLine(Integer appid,String date){
		return dao.findMonitorBaseLine(appid, date);
	}
	
	
	
	public void addDayMonitorCount(int appId,int keyId,String m_data,String collectTime){
		dao.addDayMonitorCount(appId, keyId, m_data, collectTime);
	}
	
	public List<Integer> getAppIds(String collectTime){
		return dao.getAppIds(collectTime);
	}
	
	/**
	 * ��ȡӦ����һ���ڲ����ļ�¼����
	 * @param appId
	 * @param date
	 * @return
	 */
	public int getDataById(int appId, String collectTime) {
		
		
		return dao.getDataById(appId, collectTime);
	}
	

}
