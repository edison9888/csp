package com.taobao.monitor.common.db.impl.day;



import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.KeyValueBaseLinePo;
import com.taobao.monitor.common.po.KeyValuePo;
import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.common.util.Constants;
import com.taobao.monitor.common.util.TableNameConverUtil;
import com.taobao.monitor.common.util.Utlitites;
import com.taobao.monitor.common.vo.AppSqlInfo;
import com.taobao.monitor.common.vo.DataSourcePo;
import com.taobao.monitor.common.vo.ForestPo;
import com.taobao.monitor.common.vo.GcPo;
import com.taobao.monitor.common.vo.HsfPo;
import com.taobao.monitor.common.vo.MonitorVo;
import com.taobao.monitor.common.vo.OtherHaBoLogRecord;
import com.taobao.monitor.common.vo.OtherKeyValueVo;
import com.taobao.monitor.common.vo.PageCachePo;
import com.taobao.monitor.common.vo.PingPo;
import com.taobao.monitor.common.vo.SearchEnginePo;
import com.taobao.monitor.common.vo.SqlTop10Po;
import com.taobao.monitor.common.vo.TableRecord;
import com.taobao.monitor.common.vo.TairClientPo;
import com.taobao.monitor.common.vo.ThreadPo;
import com.taobao.monitor.common.vo.ThreadPoolPo;

/**
 * 
 * @author xiaodu
 * @version 2010-4-16 上午11:21:55
 */
/**
 * @author xiaodu
 *
 */
public class MonitorDayDao extends MysqlRouteBase {
	
	
	public MonitorDayDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("Main_DAY"));
	}
	
	private static final Logger logger =  Logger.getLogger(MonitorDayDao.class);
	
	
	
	
	/**
	 * 查询统计表中时间段内的数据
	 * @param appId
	 * @param keyId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<KeyValuePo> findMonitorCountByDate(int appId,int keyId,Date startDate,Date endDate){
		
		logger.info("sql MonitorDayDao:findMonitorCountByDate");
		
		final List<KeyValuePo> keyvalueList = new ArrayList<KeyValuePo>();
		
		String sql = "select * from ms_monitor_count where app_id = ? and key_id = ? and collect_time >= ? and collect_time <=?";
		try {
			this.query(sql, new Object[]{appId,keyId,startDate,endDate}, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					KeyValuePo po = new KeyValuePo();
					po.setAppId(rs.getInt("app_id"));
					po.setKeyId(rs.getInt("key_id"));
					Timestamp time = rs.getTimestamp("collect_time");
					po.setCollectTime(new Date(time.getTime()));
					po.setValueStr(rs.getString("m_data"));
					keyvalueList.add(po);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		//
		return keyvalueList;
	}
	
	/**
	 *  * 这是查询C应用在在某一天的所有关联key的列表，这个key列表给统计pv的时候用，这里查出c应用所有关联的key为匹配IN_HSF-ProviderDetail\\_%\\_COUNTTIMES的值
	 * @param appId
	 * @param keyId
	 * @param date
	 * @return
	 * @throws Exception 
	 */
	public List<KeyValuePo> findCappPvDetailByTime(Integer appId,String date) throws Exception{
		
		logger.info("sql MonitorDayDao:findCappPvDetailByTime");
		
		final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		final List<KeyValuePo> keyValuePoList = new ArrayList<KeyValuePo>();

		String tableName = TableNameConverUtil.formatDayTableName(date+" 00:00");
		
		//String sql = "select d.app_id,d.key_id,k.key_value,d.m_data,d.collect_time from "+tableName+" d,ms_monitor_key k where k.key_id=d.key_id and  d.app_id=? and d.collect_time between ? and ?";
		String sql = "SELECT * FROM "+ tableName + " d WHERE d.app_id=? AND d.collect_time BETWEEN ? AND ? AND d.key_id IN (SELECT key_id FROM ms_monitor_key WHERE key_value LIKE 'IN_HSF-ProviderDetail\\_%\\_COUNTTIMES')";
		
		this.query(sql,new Object[]{appId,date+" 00:00:00",date+" 23:59:59"}, new SqlCallBack(){
			
			public void readerRows(ResultSet rs) throws Exception {
				
				KeyValuePo po = new KeyValuePo();		
				String value =rs.getString("m_data");
				Integer app_id = rs.getInt("app_id");
				Integer key_id = rs.getInt("key_id");
				
				value = Utlitites.formatDotTwo(value);
				
				String collectTime = rs.getString("collect_time");
				po.setAppId(app_id);
				po.setKeyId(key_id);
				po.setCollectTime(parseLogFormatDate.parse(collectTime));
				po.setValueStr(value);			
				keyValuePoList.add(po);
			}});
		return keyValuePoList;
	}
	/**
	 *  * 这是查询C应用在在某一天的所有关联key的列表，这个key列表给统计rt的时候用，这里查出c应用所有关联的key为匹配IN_HSF-ProviderDetail\\_%\\_COUNTTIMES的值
	 * @param appId
	 * @param keyId
	 * @param date
	 * @return
	 * @throws Exception 
	 */
	public List<KeyValuePo> findCappRtDetailByTime(Integer appId,String date) throws Exception{
		
		logger.info("sql MonitorDayDao:findCappRtDetailByTime");
		
		final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		final List<KeyValuePo> keyValuePoList = new ArrayList<KeyValuePo>();
		
		
		String tableName = TableNameConverUtil.formatDayTableName(date+" 00:00");
		
		//String sql = "select d.app_id,d.key_id,k.key_value,d.m_data,d.collect_time from "+tableName+" d,ms_monitor_key k where k.key_id=d.key_id and  d.app_id=? and d.collect_time between ? and ?";
		String sql = "SELECT * FROM "+ tableName + " d WHERE d.app_id=? AND d.collect_time BETWEEN ? AND ? AND d.key_id IN (SELECT key_id FROM ms_monitor_key WHERE key_value LIKE 'IN_HSF-ProviderDetail\\_%\\_AVERAGEUSERTIMES')";
		
		this.query(sql,new Object[]{appId,date+" 00:00:00",date+" 23:59:59"}, new SqlCallBack(){
			
			public void readerRows(ResultSet rs) throws Exception {
				
				KeyValuePo po = new KeyValuePo();		
				String value =rs.getString("m_data");
				Integer app_id = rs.getInt("app_id");
				Integer key_id = rs.getInt("key_id");
				
			
				value = Utlitites.formatDotTwo(value);
				
				String collectTime = rs.getString("collect_time");
				po.setAppId(app_id);
				po.setKeyId(key_id);
				po.setCollectTime(parseLogFormatDate.parse(collectTime));
				po.setValueStr(value);			
				keyValuePoList.add(po);
			}});
		return keyValuePoList;
	}

	/**
	 * 
	 * @param appId
	 * @param keyId
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public List<KeyValuePo> findKeyDetailByTime(Integer appId,Integer keyId,String date) throws Exception{
		
		logger.info("sql MonitorDayDao:findKeyDetailByTime");
		
		final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		final List<KeyValuePo> keyValuePoList = new ArrayList<KeyValuePo>();

		String tableName = TableNameConverUtil.formatDayTableName(date+" 00:00");
		
		String sql = "select d.app_id,d.key_id,k.key_value,d.m_data,d.collect_time from "+tableName+" d,ms_monitor_key k where k.key_id=d.key_id and  d.app_id=? and d.key_id=? and d.collect_time between ? and ?";
		
		
		this.query(sql,new Object[]{appId,keyId,date+" 00:00:00",date+" 23:59:59"}, new SqlCallBack(){
			
			public void readerRows(ResultSet rs) throws Exception {
				
				KeyValuePo po = new KeyValuePo();					
				Integer app_id = rs.getInt("app_id");
				Integer key_id = rs.getInt("key_id");
				String keyName = rs.getString("key_value");
				String value =rs.getString("m_data");
				if(keyName.indexOf("_GC_")>-1){
					if(keyName.indexOf(Constants.AVERAGE_USERTIMES_FLAG)>-1){
						value = (rs.getDouble("m_data")/1000000)+"";
					}
				}
				value = Utlitites.formatDotTwo(value);
				
				String collectTime = rs.getString("collect_time");
				po.setAppId(app_id);
				po.setKeyId(key_id);
				po.setCollectTime(parseLogFormatDate.parse(collectTime));
				po.setValueStr(value);					
				keyValuePoList.add(po);
			}});

		return keyValuePoList;
	}
	
	
	public List<KeyValuePo> findKeyDetailByDate(Integer appId,Integer keyId,String startCollectDate,String endCollectDate) throws Exception{
		
		logger.info("sql MonitorDayDao:findKeyDetailByDate");
		
		String sql = "select d.app_id,d.key_id,k.key_value,d.m_data,d.collect_time from ms_monitor_count d,ms_monitor_key k where k.key_id=d.key_id and  d.app_id=? and d.key_id=? and d.collect_time between ? and ?";
		
		final List<KeyValuePo> keyValuePoList = new ArrayList<KeyValuePo>();
		
		final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		this.query(sql,new Object[]{appId,keyId,startCollectDate+" 00:00:00",endCollectDate+" 00:00:00"}, new SqlCallBack(){
			
			public void readerRows(ResultSet rs) throws Exception {
				
				KeyValuePo po = new KeyValuePo();					
				Integer app_id = rs.getInt("app_id");
				Integer key_id = rs.getInt("key_id");
				String value =rs.getString("m_data");
				String keyName = rs.getString("key_value");
				if(keyName.indexOf("_GC_")>-1){
					if(keyName.indexOf(Constants.AVERAGE_USERTIMES_FLAG)>-1){
						value = (rs.getDouble("m_data")/1000000)+"";
					}
				}
				value = Utlitites.formatDotTwo(value);
				String collectTime = rs.getString("collect_time");
				
				po.setAppId(app_id);
				po.setKeyId(key_id);
				po.setCollectTime(parseLogFormatDate.parse(collectTime));
				po.setValueStr(value);					
				keyValuePoList.add(po);
			}});
		
		return keyValuePoList;
	}
	
	

	/**
	 * 
	 * @param date yyyy-MM-dd
	 * @return
	 * @throws Exception
	 */
	public Map<Integer, MonitorVo> findMonitorCountMapByDate(String date) throws Exception {		
		return findMonitorCountMapByDate(null, null, date);
	}
	
	
	
	
	/**
	 * 
	 * @param appid
	 * @param date
	 * @return
	 */
	public Map<Integer,Map<Integer,KeyValueBaseLinePo>> findMonitorBaseLine(Integer appid,String date){
		
		logger.info("sql MonitorDayDao:findMonitorBaseLine");
		
		String sql ="select * from ms_monitor_count_baseline where collect_time =?";
		Object[] obj = new Object[]{date+" 00:00:00"};
		if(appid!=null){
			sql = "select * from ms_monitor_count_baseline where collect_time =? and app_id=?";
			obj = new Object[]{date+" 00:00:00",appid};
		}
		
		final Map<Integer,Map<Integer,KeyValueBaseLinePo>> appMap = new HashMap<Integer, Map<Integer,KeyValueBaseLinePo>>();
		
		try {
			this.query(sql, obj,new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					Integer appId = rs.getInt("app_id");
					Integer keyid = rs.getInt("key_id");
					
					double baseValue = rs.getDouble("baseline_data");
					double sameValue = rs.getDouble("sameday_data");
					double yesterValue = rs.getDouble("yesterday_data");
										
					Map<Integer,KeyValueBaseLinePo> keyMap = appMap.get(appId);
					if(keyMap==null){
						keyMap = new HashMap<Integer, KeyValueBaseLinePo>();
						appMap.put(appId, keyMap);
					}
					
					KeyValueBaseLinePo po = keyMap.get(keyid);
					if(po==null){
						po = new KeyValueBaseLinePo();
						keyMap.put(keyid, po);
					}
					
					po.setAppId(appId);
					po.setKeyId(keyid);
					
					po.setBaseLineValue(baseValue);
					po.setSameDayValue(sameValue);
					po.setYesterdayValue(yesterValue);
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return appMap;
	}
	
	
	public KeyValuePo findKeyValueFromCountMax(int keyId,int appId){
		String sql = "select max(m_data) as d ms_monitor_count where app_id=? and key_id=?";
		final KeyValuePo po = new KeyValuePo();
		try {
			this.query(sql, new Object[]{appId,keyId}, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					po.setValueStr(rs.getString("d"));
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return po;
	}
	
	public KeyValuePo findKeyValueFromCountByDate(int keyId,int appId,String date){
		
		logger.info("sql MonitorDayDao:findKeyValueFromCountByDate");
		
		String sql = "select c.app_id,c.m_data from ms_monitor_count c where c.collect_time = ?  and c.app_id = ? and c.key_id=?";
		
		Object[] objs = new Object[]{date+" 00:00:00",appId,keyId};
		
		final KeyValuePo po = new KeyValuePo();
		
		try {
			this.query(sql, objs, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					//int appId = rs.getInt("app_id");
					String mData = rs.getString("m_data");	
					if(po.getValueStr()!=null){
						if(Double.parseDouble(po.getValueStr()) <Double.parseDouble(mData)){
							po.setValueStr(mData);
						}
					}else{
						po.setValueStr(mData);
					}
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return po;
	}

	
	/**
	 * 返回keyId和keyValue的map
	 * @return
	 */
	public Map<Integer,String> getKeyMap(){
	
		String sql = "select * from ms_monitor_key";
		final Map<Integer,String> keyMap = new HashMap<Integer,String>();
		try{
			this.query(sql, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					
					int keyId = rs.getInt("key_id");
					String keyValue = rs.getString("key_value");
					keyMap.put(keyId, keyValue);
				}});
			}catch (Exception e) {
				logger.error("", e);
			}
			return keyMap;
			
	}
	
	
	
	
	/**
	 * 查询C应用的qps，这里是通过统计累加指定时间（20:30~22:30之间的）的qps总值，返回的list是每一分钟的qps总值
	 * 这里c的qps是调用接口（key为IN_HSF-ProviderDetail开头且结尾是COUNTTIMES的）的数量
	 */
	public List<KeyValuePo> findCappQpsCountByTime(Integer appId,Date startDate,Date endDate) {
		
		logger.info("sql MonitorDayDao:findCappQpsCountByTime");
		
		final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
		final List<KeyValuePo> keyValuePoList = new ArrayList<KeyValuePo>();
		String tableName = TableNameConverUtil.formatDayTableName(startDate);
		
		String sql = "SELECT d.collect_time,sum(m_data) FROM "+ tableName + " d " +
				"WHERE d.app_id=? AND d.collect_time BETWEEN ? AND ? AND d.key_id IN " +
				"(SELECT key_id FROM ms_monitor_key WHERE key_value LIKE 'IN_HSF-ProviderDetail\\_%\\_COUNTTIMES') group by d.collect_time";
		//Monitor
		try{
		this.query(sql,new Object[]{appId,startDate,endDate}, new SqlCallBack(){
			
			public void readerRows(ResultSet rs) throws Exception {
				
				KeyValuePo po = new KeyValuePo();					
				
				String value =rs.getString("sum(m_data)");
				value = Utlitites.formatDotTwo(value);
				String collectTime = rs.getString("collect_time");				
				po.setCollectTime(parseLogFormatDate.parse(collectTime));
				po.setValueStr(value);					
				keyValuePoList.add(po);
			}});
		}catch (Exception e) {
			logger.error("", e);
		}
		return keyValuePoList;
	}	
	
	/**
	 * 这是C应用的pv累加m_data的总值，应用包含所有key为IN_HSF-ProviderDetail开头且结尾是COUNTTIMES的
	 * @param appId
	 * @param date
	 * @return
	 */
public long findCappPvCountByDate(int appId,String date){
		
		logger.info("sql MonitorDayDao:findKeyValueFromCountByDate");
		
		String sql = "SELECT sum(m_data) FROM ms_monitor_count c WHERE c.app_id=? AND c.collect_time = ? AND c.key_id IN " +
					"(SELECT key_id FROM ms_monitor_key WHERE key_value LIKE 'IN_HSF-ProviderDetail\\_%\\_COUNTTIMES')";
		long sum = 0;
		try {
			sum = this.getLongValue(sql, new Object[]{appId, date});
			
			
		} catch (Exception e) {
			logger.error("", e);
		}
		return sum;
	}

/**
 * 查询C应用的rt，这里是通过统计累加指定时间（20:30~22:30之间的）的rt总值，返回的list是每一分钟的rt总值
 * 这里c的rt是调用接口（key为IN_HSF-ProviderDetail开头且结尾是_AVERAGEUSERTIMES的）的数量
 */
public List<KeyValuePo> findCappRtCountByTime(Integer appId,Date colectDate) {
	
	logger.info("sql MonitorDayDao:findCappRtCountByTime");
	
	final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");		
	final List<KeyValuePo> keyValuePoList = new ArrayList<KeyValuePo>();
	
	String sql = "SELECT * FROM ms_monitor_count d " +
			"WHERE d.app_id=? AND collect_time = ? AND d.key_id IN " +
			"(SELECT key_id FROM ms_monitor_key WHERE key_value LIKE 'IN_HSF-ProviderDetail\\_%\\_AVERAGEUSERTIMES')";

	//Monitor
	try{
	this.query(sql,new Object[]{appId,colectDate}, new SqlCallBack(){
		
		public void readerRows(ResultSet rs) throws Exception {
			
			KeyValuePo po = new KeyValuePo();					
			int keyId = rs.getInt("key_id");
			String value =rs.getString("m_data");
			value = Utlitites.formatDotTwo(value);
			String collectTime = rs.getString("collect_time");				
			po.setCollectTime(parseLogFormatDate.parse(collectTime));
			po.setValueStr(value);		
			po.setKeyId(keyId);
			keyValuePoList.add(po);
		}});
	}catch (Exception e) {
		logger.error("", e);
	}
	return keyValuePoList;
}	

///**
// * 这是C应用的pv累加m_data的总值，应用包含所有key为IN_HSF-ProviderDetail开头且结尾是_AVERAGEUSERTIMES的
// * @param appId
// * @param date
// * @return
// */
//public KeyValuePo findCappRtCountByDate(int appId,String date){
//	
//	logger.info("sql MonitorDayDao:findKeyValueFromCountByDate");
//	
////		String sql = "select c.app_id,k.key_id,k.key_value,c.m_data " + 
////        			"from ms_monitor_count c , ms_monitor_key k " + 
////        			"where c.key_id = k.key_id and c.collect_time = ? and c.app_id = ?";
//	
//	String sql = "SELECT * FROM ms_monitor_count c WHERE c.app_id=? AND c.collect_time = ? AND c.key_id IN " +
//	"(SELECT key_id FROM ms_monitor_key WHERE key_value LIKE 'IN_HSF-ProviderDetail\\_%\\_AVERAGEUSERTIMES')";
//	
//	Object[] objs = new Object[]{appId,date+" 00:00:00"};
//	final Map<Integer, String> keyMap = this.getKeyMap();
//
//	final KeyValuePo result = new KeyValuePo();
//	final List<KeyValuePo> poList = new ArrayList<KeyValuePo>();
//	try {
//		this.query(sql, objs, new SqlCallBack(){
//			
//			public void readerRows(ResultSet rs) throws Exception {
//				//int appId = rs.getInt("app_id");
//				KeyValuePo po = new KeyValuePo();
//				Integer key_id = rs.getInt("key_id");
//				String keyValue = keyMap.get(key_id);
//				String mData = rs.getString("m_data");		
//				po.setKeyName(keyValue);
//				po.setValueStr(mData);
//				poList.add(po);
//			}});
//	} catch (Exception e) {
//		logger.error("", e);
//	}
//	
//	double mData = 0.0;
//	int size = 0;
//	for(KeyValuePo p : poList) {
//		
//		mData += Double.parseDouble(p.getValueStr());
//		size++;
//	}
//	result.setValueStr(mData / size + "");
//	return result;
//}

	/**
	 * 
	 * @param date yyyy-MM-dd
	 * @return
	 * @throws Exception
	 */
	public Map<String, KeyValuePo> findMonitorCountKeyMapByDate(int appId,String date) throws Exception {
		
		
		logger.info("sql MonitorDayDao:findMonitorCountKeyMapByDate");
		
		Object[] obj = new Object[]{date+" 00:00:00",appId};
		String sql = "select c.app_id,k.key_id,k.key_value,c.m_data " + 
        "from ms_monitor_count c,ms_monitor_key k " +
        "where k.key_id = c.key_id and c.collect_time = ?  and c.app_id = ?";
		
		final Map<String, KeyValuePo> map = new HashMap<String, KeyValuePo>();
		
		
		this.query(sql, obj, new SqlCallBack(){
			
			public void readerRows(ResultSet rs) throws Exception {
				KeyValuePo po = new KeyValuePo();
				Integer keyId = rs.getInt("key_id");
				String m_data = rs.getString("m_data");
				if(keyId == 27733||keyId == 27734||keyId == 27735){//IN_HSF_AllInterfacePV_COUNTTIMES  
					long v = (long)Double.parseDouble(m_data);
					if(v <1)
					return ;
				}				
				String keyName = rs.getString("key_value");
				po.setValueStr(m_data);
				po.setKeyName(keyName);
				map.put(keyName, po);
				
			}});
		
		
		return map;
	}
	
	

    public Map<Long, Long> findMonitorCountMapAsValueByDate(long appId, String date, long[] keyId) throws Exception {
    	
    	logger.info("sql MonitorDayDao:findMonitorCountMapAsValueByDate");
    	
        final Map<Long, Long> value = new HashMap<Long, Long>(16);
        StringBuilder sql = new StringBuilder().append("select key_id,m_data from  ms_monitor_count where app_id = ? and collect_time = ? and (");
        Object[] parameters = new Object[keyId.length + 2];
        parameters[0] = appId;
        parameters[1] = new StringBuilder(20).append(date).append(" 00:00:00").toString();
        for (int i = 2; i < parameters.length; i++) {
            parameters[i] = keyId[i - 2];
            sql.append("key_id=? or ");
        }
        sql.delete(sql.length() - 3, sql.length());
        sql.append(")");
        this.query(sql.toString(), parameters, new SqlCallBack() {
            public void readerRows(ResultSet rs) throws Exception {
                value.put(rs.getLong("key_id"), rs.getLong("m_data"));
            }
        });
        return value;
    }
    
    public Map<Long, String> findMonitorCountStrMapAsValueByDate(long appId, String date, long[] keyId) throws Exception {
    	
    	logger.info("sql MonitorDayDao:findMonitorCountStrMapAsValueByDate");
    	
        final Map<Long, String> value = new HashMap<Long, String>(16);
        StringBuilder sql = new StringBuilder().append("select key_id,m_data from  ms_monitor_count where app_id = ? and collect_time = ? and (");
        Object[] parameters = new Object[keyId.length + 2];
        parameters[0] = appId;
        parameters[1] = new StringBuilder(20).append(date).append(" 00:00:00").toString();
        for (int i = 2; i < parameters.length; i++) {
            parameters[i] = keyId[i - 2];
            sql.append("key_id=? or ");
        }
        sql.delete(sql.length() - 3, sql.length());
        sql.append(")");
        this.query(sql.toString(), parameters, new SqlCallBack() {
            public void readerRows(ResultSet rs) throws Exception {
                value.put(rs.getLong("key_id"), rs.getString("m_data"));
            }
        });
        return value;
    }
    
	/**
	 * 查询某天的总PV
	 * @param keyId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<KeyValuePo> findMonitorCountAllByDate(int keyId,Date startDate,Date endDate){
		
		logger.info("sql findMonitorCountAllByDate");
		
		final List<KeyValuePo> keyvalueList = new ArrayList<KeyValuePo>();
		
		String sql = "select * from ms_monitor_count where key_id = ? and collect_time >= ? and collect_time <=?";
		try {
			this.query(sql, new Object[]{keyId,startDate,endDate}, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					KeyValuePo po = new KeyValuePo();
					po.setAppId(rs.getInt("app_id"));
					po.setKeyId(rs.getInt("key_id"));
					Timestamp time = rs.getTimestamp("collect_time");
					po.setCollectTime(new Date(time.getTime()));
					po.setValueStr(rs.getString("m_data"));
					keyvalueList.add(po);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		//
		return keyvalueList;
	}	    
	/**
	 * 
	 * @param date yyyy-MM-dd
	 * @return
	 * @throws Exception
	 */
	public Map<Integer, MonitorVo> findMonitorCountMapByDate(final Integer appId, final String appNameGive, final String date) {
		
		logger.info("sql MonitorDayDao:findMonitorCountMapByDate");
		
		String sql = "select c.app_id,k.key_id,k.key_value,c.m_data " + 
        "from ms_monitor_count c,ms_monitor_key k " +
        "where k.key_id = c.key_id and c.collect_time = ? ";
		
		Object[] obj = new Object[]{date+" 00:00:00"};
		if(appId!=null){
			sql = "select c.app_id,k.key_id,k.key_value,c.m_data " + 
	        "from ms_monitor_count c,ms_monitor_key k " +
	        "where k.key_id = c.key_id and c.collect_time = ?  and c.app_id = ?";
			
			obj = new Object[]{date+" 00:00:00",appId};
		}
		
		
		final Map<Integer, MonitorVo>  appMap = new HashMap<Integer, MonitorVo>();
		try {
			this.query(sql,obj, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					
						try{
							int app_id= rs.getInt("app_id");
							String appName = appNameGive;
							if(app_id==10){
								appName = "tbdb1";
							}
							if(app_id==11){
								appName = "tbdb2";
							}
							if(app_id==12){
								appName = "heart";
							}
							if(app_id==13){
								appName = "comm";
							}
							if(app_id==14){
								appName = "bmw";
							}
							if(app_id==15){
								appName = "misc";
							}
							
							String keyName = rs.getString("key_value"); 
							String dataValue = rs.getString("m_data");
							
							Integer keyId = rs.getInt("key_id");
							
							MonitorVo vo = appMap.get(app_id);
							if(vo==null){
								vo = new MonitorVo();
								appMap.put(app_id, vo);
							}
							
							vo.setAppId(app_id);
							
							if(keyId == 27733){//IN_HSF_AllInterfacePV_COUNTTIMES  
								vo.setAllHsfInterfacePvId(27733);
								long v = (long)Double.parseDouble(dataValue);
								if(v >0)
								vo.setAllHsfInterfacePv(v);
								return ;
							}
							if(keyId == 27734){//IN_HSF_AllInterfaceQps_AVERAGEUSERTIMES
								vo.setAllHsfInterfaceQpsId(27734);
								long v = (long)Double.parseDouble(dataValue);
								if(v >0)
								vo.setAllHsfInterfaceQps(v);
								return ;
							}
							if(keyId == 27735){//IN_HSF_AllInterfaceRest_AVERAGEUSERTIMES
								vo.setAllHsfInterfaceRestId(27735);
								long v = (long)Double.parseDouble(dataValue);
								if(v >0)
								vo.setAllHsfInterfaceRest(v);
								return ;
							}
							
							if(keyName.startsWith("OTHER")){
								String[] keyStructs = keyName.split("_");				
								if(keyStructs.length!=4){
									return;
								}//
								
								//String perfix = keyStructs[0];
								String type = keyStructs[1];
								String name = keyStructs[2];
								//String valuetype = keyStructs[3];
								
								OtherKeyValueVo other = vo.getOtherKeyValueMap().get(type);
								if(other==null){
									other = new OtherKeyValueVo();
									vo.getOtherKeyValueMap().put(type, other);
									other.setTypeName(type);
								}
								OtherHaBoLogRecord record = other.getKeyMap().get(name);
								
								if(record==null){
									record = new OtherHaBoLogRecord();
									other.getKeyMap().put(name, record);
								}
								
								record.setTypeName(type);
								record.setKeyName(name);
								KeyValuePo po = new KeyValuePo();
								
								if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>-1){
									KeyValuePo countpo = record.getExeCount();
									if(countpo==null){
										po.setKeyId(keyId);
										po.setValueStr(fromatLong(dataValue));
										record.setExeCount(po);
									}
									
								}else if(keyName.indexOf(Constants.AVERAGE_USERTIMES_FLAG)>-1){
									KeyValuePo averagePo = record.getAverageExeTime();
									if(averagePo==null){
										po.setKeyId(keyId);
										po.setValueStr(Utlitites.formatDotTwo(dataValue));
										record.setAverageExeTime(po);
									}				
								}	
								
								if(keyName.indexOf("添加购物车临时添加-OK")>-1){
									if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>0){
										vo.setShoppingCartTmp(fromatLong(dataValue));
									}
								}
								if(keyName.indexOf("添加购物车会员添加-OK")>-1){
									if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>0){
										vo.setShoppingCartMember(fromatLong(dataValue));
									}
								}
								
								
							}else if(keyName.startsWith("IN")){
								
								if(keyName.indexOf("QPS")>-1){
									vo.setQpsNum(Utlitites.formatDotTwo(dataValue));
									vo.setQpsKeyId(keyId);
								}
								if(keyName.indexOf("ResT")>-1){
									vo.setRtNum(Utlitites.formatDotTwo(dataValue));
									vo.setRtKeyId(keyId);
								}								
								//IN_HSF-ProviderDetail_com.taobao.uic.common.service.userinfo.UserReadService:1.0.0_getUserAndUserExtraByUID32_executes
								//IN_HSF-ProviderDetail_
								if(keyName.indexOf("HSF-ProviderDetail")>-1){
																	
									
									String[] keys = keyName.split("_");									
									String hsfClassName = keys[2]+keys[3];
									
									
									HsfPo hsf = vo.getInHsfMap().get(hsfClassName);
									if(hsf==null){
										hsf = new HsfPo();
										vo.getInHsfMap().put(hsfClassName, hsf);
										vo.getInHsfList().add(hsf);
									}
									String _cn = keys[2];
									_cn = parseClassNameWithVersionNum(_cn); //获得类名和版本号
									hsf.setClassName(_cn);
									hsf.setName(keys[1]);
									hsf.setMethodName(keys[3]);									
									
									if(keys[1].equals("HSF-ProviderDetail-BizException")){
										if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>-1){											
											hsf.getBizExecptionNumMap().put(keys[2], Utlitites.getLong(dataValue));
											hsf.setBizExecptionNum(fromatLong(dataValue));
											hsf.setBizCountkeyId(keyId);
										}
										
									}else if(keys[1].equals("HSF-ProviderDetail-Exception")){
										if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>-1){
											
											hsf.getExecptionNumMap().put(keys[2], Utlitites.getLong(dataValue));
											
											hsf.setExecptionNum(fromatLong(dataValue));
											hsf.setExcCountkeyId(keyId);
										}
										
									}else if(keys[1].equals("HSF-ProviderDetail")){										
										if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>-1){
											
											hsf.getExeCountNumMap().put(keys[2], Utlitites.getLong(dataValue));
											
											hsf.setExeCount(fromatLong(dataValue));											
											hsf.setExeCountNum(Utlitites.getLong(dataValue));
											hsf.setCountkeyId(keyId);
										}
										if(keyName.indexOf(Constants.AVERAGE_USERTIMES_FLAG)>-1){
											hsf.getAverageExeMap().put(keys[2],Utlitites.getDouble(dataValue));											
											hsf.setAverageExe(Utlitites.formatDotTwo(dataValue));
											hsf.setAverageKeyId(keyId);
										}										
										
									}
								}
							}else if(keyName.startsWith("SELF")){
								///SELF_GC_GC
								/**
								 * |    309 | SELF_Swap(2G)      | NULL          | NULL    |
									|    310 | SELF_Memory(4G)    | NULL          | NULL    |
									|    311 | SELF_IOWAIT        | NULL          | NULL    |
									|    312 | SELF_CPU           | NULL          | NULL    |
									|    313 | SELF_Load          | NULL          | NULL    |
									|    348 | SELF_GC_PSYoungGen | NULL          | NULL    |
									|    349 | SELF_GC_GC         | NULL          | NULL    |
									|    350 | SELF_GC_GC_TIMES   | NULL          | NULL    |
									|    351 | SELF_GC_PSPermGen  | NULL          | NULL    |
									|    352 | SELF_GC_Full       | NULL          | NULL    |
									|    353 | SELF_GC_PSOldGen
								 */
								if(keyName.indexOf("_GC_")>-1){												
									String[] keys = keyName.split("_");		
									String gcKey = keys[2];
									GcPo gcpo = null;
									
									if(gcKey.equals("GC")){	
										gcpo =vo.getGcpo();
										if(gcpo==null){
											gcpo = new GcPo();
											vo.setGcpo(gcpo);
										}
									}
									if(gcKey.equals("Full")){
										gcpo = vo.getFullGcpo();
										if(gcpo==null){
											gcpo = new GcPo();
											vo.setFullGcpo(gcpo);
										}
									}
									
									if(keyName.indexOf(Constants.AVERAGE_MACHINE_FLAG)>-1){
										GcPo t = new GcPo();
										t.setKeyId(keyId);
										t.setKeyValue(dataValue);
										
										vo.getGcMap().put(keyName, t);
										
										if(gcpo!=null){
											gcpo.setGcCount(fromatLong(dataValue));
											gcpo.setAveMachinekeyId(keyId);
										}
										
									}
									if(keyName.indexOf(Constants.AVERAGE_USERTIMES_FLAG)>-1){
										GcPo t = new GcPo();
										t.setKeyId(keyId);
										t.setKeyValue(Utlitites.formatDotTwo((Double.parseDouble(dataValue)/1000000)+""));
										vo.getGcMap().put(keyName, t);
										
										if(gcpo!=null){
											gcpo.setGcAverage(Utlitites.formatDotTwo((Double.parseDouble(dataValue)/1000000)+""));
											gcpo.setAveuserTimeKeyId(keyId);
										}
										
									}
									
								}
								if(keyName.indexOf("Swap")>-1){
									vo.setSwap(Utlitites.formatDotTwo(dataValue));
									vo.setSwapKeyId(keyId);
								}
								if(keyName.indexOf("Memory")>-1){
									vo.setMen(Utlitites.formatDotTwo(dataValue));
									vo.setMenKeyId(keyId);								}
								if(keyName.indexOf("IOWAIT")>-1){
									vo.setIowait(Utlitites.formatDotTwo(dataValue));
									vo.setIowaitKeyId(keyId);
								}
								if(keyName.indexOf("CPU")>-1){
									vo.setCpu(Utlitites.formatDotTwo(dataValue));
									vo.setCpuKeyId(keyId);
								}
								if(keyName.indexOf("Load")>-1){
									vo.setLoad(Utlitites.formatDotTwo(dataValue));
									vo.setLoadKeyId(keyId);
								}
								
								
								if(keyName.indexOf("_Thread_")>-1){
									if(keyName.indexOf(Constants.AVERAGE_USERTIMES_FLAG)<0)return;
									//SELF_Thread_name_NEW
									String[] keys = keyName.split("_");									
									String threadName = keys[2];
									String ms = keys[3];
									ThreadPo po = vo.getThreadPo().get(threadName);
									if(po==null){
										po = new ThreadPo();
										vo.getThreadPo().put(threadName, po);
									}
									if("NEW".equals(ms)){
										po.setNewThread(fromatLong(dataValue));
									}
									if("BLOCKED".equals(ms)){
										po.setBlocked(fromatLong(dataValue));
									}
									if("RUNNABLE".equals(ms)){
										po.setRunnable(fromatLong(dataValue));
									}
									if("WAITING".equals(ms)){
										po.setWaiting(fromatLong(dataValue));
									}
									if("TERMINATED".equals(ms)){
										po.setTerminated(fromatLong(dataValue));
									}
									if("TIMEDWAITING".equals(ms)){
										po.setTimedWaiting(fromatLong(dataValue));
									}
								}
								
								if(keyName.indexOf("_ThreadPool_")>-1){
									if(keyName.indexOf(Constants.AVERAGE_USERTIMES_FLAG)<0)return ;
									String[] keys = keyName.split("_");									
									String threadName = keys[2];
									String ms = keys[3];
									ThreadPoolPo po = vo.getThreadPoolMap().get(threadName);
									if(po==null){
										po = new ThreadPoolPo();
										vo.getThreadPoolMap().put(threadName, po);
									}
									if("maxThreads".equals(ms)){
										po.setMaxThreads(fromatLong(dataValue));
									}
									if("currentThreadCount".equals(ms)){
										po.setCurrentThreadCount(fromatLong(dataValue));
									}
								}
								if(keyName.indexOf("_DataSource_")>-1){
									if(keyName.indexOf(Constants.AVERAGE_USERTIMES_FLAG)<0)return ;
									//MaxConnectionsInUseCount,InUseConnectionCount,AvailableConnectionCount,ConnectionCount,MaxSize,MinSize
									String[] keys = keyName.split("_");									
									String dsName = keys[2];
									String ms = keys[3];									
									DataSourcePo po = vo.getDataSourceMap().get(dsName);
									if(po==null){
										po = new DataSourcePo();
										vo.getDataSourceMap().put(dsName, po);										
									}
									
									if("MaxConnectionsInUseCount".equals(ms)){
										po.setMaxConnectionsInUseCount(fromatLong(dataValue));
									}
									if("InUseConnectionCount".equals(ms)){
										po.setInUseConnectionCount(fromatLong(dataValue));
									}

									if("AvailableConnectionCount".equals(ms)){
										po.setAvailableConnectionCount(fromatLong(dataValue));
									}
									if("ConnectionCount".equals(ms)){
										po.setConnectionCount(fromatLong(dataValue));
									}
									if("MaxSize".equals(ms)){
										po.setMaxSize(fromatLong(dataValue));
									}
									if("MinSize".equals(ms)){
										po.setMinSize(fromatLong(dataValue));
									}
									
								}
								
							}else if(keyName.startsWith("OUT")){								
								
								if(keyName.indexOf("PageCache")>-1){									
									String[] keys = keyName.split("_");
									String name = keys[1];
									PageCachePo po = vo.getPageCacheMap().get(name);
									if(po==null){
										po = new PageCachePo();
										vo.getPageCacheMap().put(name, po);
										vo.getPageCacheList().add(po);
									}
									po.setPageCacheName(name);
									if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>-1){
										po.setExeCount(fromatLong(dataValue));
										po.setExeCountNum(Utlitites.getLong(dataValue));
										po.setCountkeyId(keyId);
									}
									if(keyName.indexOf(Constants.AVERAGE_USERTIMES_FLAG)>-1){
										po.setExeAverage(Utlitites.formatDotTwo(dataValue));
										po.setAverageKeyId(keyId);
									}
								}
								if(keyName.indexOf("TairClient")>-1){									
									//OUT_TairClient_get_AVERAGEUSERTIMES
									String[] keys = keyName.split("_");
									String actionType =keys[2];
									
									TairClientPo po = vo.getTairClientMap().get(actionType);
									if(po==null){
										po = new TairClientPo();
										vo.getTairClientMap().put(actionType, po);
//										vo.getTairClientList().add(po);
									}
									po.setMethodName(keys[2]);
									if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>-1){
										po.setExeCount(fromatLong(dataValue));
										po.setExeCountNum(Utlitites.getLong(dataValue));
										po.setCountkeyId(keyId);
									}
									if(keyName.indexOf(Constants.AVERAGE_USERTIMES_FLAG)>-1){
										po.setAverageExe(dataValue);
										po.setAverageKeyId(keyId);
									}									
								}
								if(keyName.indexOf("SearchEngine")>-1){
									//OUT_SearchEngine_http://b2c-promotion-search.config-vip.taobao.com:2088/bin/search?_auction_times
									String[] keys = keyName.split("_");
									
									String name = keys[2]+"_"+keys[3];
									
									SearchEnginePo po = vo.getOutSearchMap().get(name);
									if(po==null){
										po = new SearchEnginePo();
										vo.getOutSearchMap().put(name, po);
										vo.getOutSearchList().add(po);
									}
									po.setUrl(keys[2]);
									po.setType(keys[3]);
									if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>-1){
										po.setExeCount(fromatLong(dataValue));
										po.setExeCountNum(Utlitites.getLong(dataValue));
										po.setCountkeyId(keyId);
									}
									if(keyName.indexOf(Constants.AVERAGE_USERTIMES_FLAG)>-1){
										po.setExeAverage(Utlitites.formatDotTwo(dataValue));
										po.setAverageKeyId(keyId);
									}	
									
								}
								if(keyName.indexOf("forest")>-1){
									//OUT_forest_client_getAllRelations_times  
									
									String[] keys = keyName.split("_");
									
									ForestPo po = vo.getForestMap().get(keys[2]+"_"+keys[3]);
									if(po==null){
										po = new ForestPo();
										vo.getForestMap().put(keys[2]+"_"+keys[3], po);
										vo.getForestList().add(po);
									}
									String _cn = keys[2];
//									if(_cn.indexOf(":")>-1){
//										_cn = _cn.substring(0,_cn.lastIndexOf(":"));
//										_cn = _cn.substring(_cn.lastIndexOf(".")+1,_cn.length());
//									}
									_cn = parseClassNameWithVersionNum(_cn); //获得类名和版本号
									po.setMethodName(keys[3]);
									po.setType(_cn);
									
									if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>-1){
										po.setExeCount(fromatLong(dataValue));
										po.setExeCountNum(Utlitites.getLong(dataValue));
										po.setCountkeyId(keyId);
									}
									if(keyName.indexOf(Constants.AVERAGE_USERTIMES_FLAG)>-1){
										po.setExeAverage(Utlitites.formatDotTwo(dataValue));
										po.setAverageKeyId(keyId);
									}
								}
								
								if(keyName.indexOf("HSF-Consumer")>-1){
									
									//OUT_HSF-Consumer-BizException_com.taobao.tc.service.TcTradeService:1.0.0_sellerConfirmSendGoods_executes  
									
									
									String[] keys = keyName.split("_");
									if("JVM".equals(keys[2])){return ;}
									
									String name = keys[2]+"_"+keys[3];
																		
									HsfPo po = vo.getOutHsfMap().get(name);
									if(po==null){
										po = new HsfPo();										
										vo.getOutHsfMap().put(name, po);
										vo.getOutHsfList().add(po);
									}
									if(keys[2].indexOf("com.taobao.item")>-1){
										po.setAim("ic");
									}else if(keys[2].indexOf("com.taobao.tc")>-1){
										po.setAim("tc");
									}else
									if(keys[2].indexOf("com.taobao.uic")>-1){
										po.setAim("uic");
									}else
									if(keys[2].indexOf("com.taobao.ic")>-1){
										po.setAim("ic");
									}else
									if(keys[2].indexOf("com.taobao.sc")>-1){
										po.setAim("sc");
									}else if(keys[2].indexOf("com.taobao.designcenter")>-1){
										po.setAim("designcenter");
									}else if(keys[2].indexOf("com.taobao.db")>-1){
										po.setAim("db");
									}else if(keys[2].indexOf("com.taobao.logistics")>-1){
										po.setAim("logistics");
									}else if(keys[2].indexOf("com.taobao.upp")>-1){
										po.setAim("upp");
									}else if(keys[2].indexOf("com.taobao.clientSide")>-1){
										po.setAim("clientSide");
									}else if(keys[2].indexOf("com.taobao.messenger")>-1){
										po.setAim("messenger");
									}else if(keys[2].indexOf("com.taobao.shopservice")>-1){
										po.setAim("shopservice");
									}else if(keys[2].indexOf("com.taobao.misccenter")>-1){
										po.setAim("misccenter");
									}else if(keys[2].indexOf("com.taobao.mmp")>-1){
										po.setAim("mmp");
									}else if(keys[2].indexOf("com.taobao.promotioncenter")>-1){
										po.setAim("promotioncenter");
									}else if(keys[2].indexOf("com.taobao.vic")>-1){
										po.setAim("vic");
									}else if(keys[2].indexOf("com.taobao.forest")>-1){
										po.setAim("forest");
									}else if(keys[2].indexOf("com.taobao.top")>-1){
										po.setAim("top");
									}else if(keys[2].indexOf("com.taobao.kfc")>-1){
										po.setAim("kfc");
									}else if(keys[2].indexOf("com.taobao.serviceone")>-1){
										po.setAim("serviceone");
									}else{										
										po.setAim(" - ");
									}
									
									String _cn = keys[2];

									_cn = parseClassNameWithVersionNum(_cn); //获得类名和版本号
									po.setClassName(_cn);
									po.setMethodName(keys[3]);
																		
									
									if(keys[1].equals("HSF-Consumer-BizException")){
										if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>-1){
											po.getBizExecptionNumMap().put(keys[2], Utlitites.getLong(dataValue));
											po.setBizExecptionNum(fromatLong(dataValue));
											po.setBizCountkeyId(keyId);
										}
										
									}else if(keys[1].equals("HSF-Consumer-Exception")){
										if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>-1){
											po.getExecptionNumMap().put(keys[2], Utlitites.getLong(dataValue));
											po.setExecptionNum(fromatLong(dataValue));
											po.setExcCountkeyId(keyId);
										}
										
									}else if(keys[1].equals("HSF-Consumer")){										
										if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>-1){
											po.getExeCountNumMap().put(keys[2], Utlitites.getLong(dataValue));
											po.setExeCount(fromatLong(dataValue));											
											po.setExeCountNum(Utlitites.getLong(dataValue));
											po.setCountkeyId(keyId);
										}
										if(keyName.indexOf(Constants.AVERAGE_USERTIMES_FLAG)>-1){
											po.getAverageExeMap().put(keys[2], Utlitites.getDouble(dataValue));
											po.setAverageExe(Utlitites.formatDotTwo(dataValue));
											po.setAverageKeyId(keyId);
										}										
										
									}
									
								}
								
								
							}else if(keyName.startsWith("SQL_TOP_10")){
								
								//SQL_TOP_10_EXEC_执行次数TOP10
								//SQL_TOP_10_DISKGETS_磁盘物理读TOP10
								//SQL_TOP_10_ELAPSED_总耗时TOP10
								//SQL_TOP_10_BUFFERGETS_逻辑读TOP10
								SqlTop10Po po = new SqlTop10Po();
								po.setDbName(appName);
								String sqlFullText = "";
								if(keyName.indexOf("SQL_TOP_10_EXEC_")>-1){
									sqlFullText = keyName.substring("SQL_TOP_10_EXEC_".length(), keyName.length());									
									po.setSqlFullText(sqlFullText);
									po.setValueData(fromatLong(dataValue));
									po.setValueDataNum(Utlitites.getLong(dataValue));
									
									if(hasSQLTOP(vo.getSqlTop10Exe(),po)){
										vo.getSqlTop10Exe().add(po);
									}
									
								}
								if(keyName.indexOf("SQL_TOP_10_DISKGETS_")>-1){
									sqlFullText = keyName.substring("SQL_TOP_10_DISKGETS_".length(), keyName.length());									
									po.setSqlFullText(sqlFullText);
									po.setValueData(fromatLong(dataValue));
									po.setValueDataNum(Utlitites.getLong(dataValue));
									
									if(hasSQLTOP(vo.getSqlTop10Disk(),po)){
										vo.getSqlTop10Disk().add(po);
									}
									
								}
								if(keyName.indexOf("SQL_TOP_10_ELAPSED_")>-1){
									sqlFullText = keyName.substring("SQL_TOP_10_ELAPSED_".length(), keyName.length());									
									po.setSqlFullText(sqlFullText);
									po.setValueData(fromatLong(dataValue));
									po.setValueDataNum(Utlitites.getLong(dataValue));
									
									if(hasSQLTOP(vo.getSqlTop10Elap(),po)){
										vo.getSqlTop10Elap().add(po);
									}
									
								}
								if(keyName.indexOf("SQL_TOP_10_BUFFERGETS_")>-1){
									sqlFullText = keyName.substring("SQL_TOP_10_BUFFERGETS_".length(), keyName.length());									
									po.setSqlFullText(sqlFullText);
									po.setValueData(fromatLong(dataValue));
									po.setValueDataNum(Utlitites.getLong(dataValue));
									
									if(hasSQLTOP(vo.getSqlTop10Buff(),po)){
										vo.getSqlTop10Buff().add(po);
									}
								}
								
								
							}else if(keyName.startsWith("SQL_INFO_")){
								//SQL_INFO_db_ms
								
								String[] keys = keyName.split("_");
								String dbName = keys[2];
								String ms = keys[3];								
								AppSqlInfo sqlInfo = vo.getAppSqlInfoMap().get(dbName);
								
								if(sqlInfo==null){
									sqlInfo = new AppSqlInfo();
									vo.getAppSqlInfoMap().put(dbName, sqlInfo);
								}
								sqlInfo.setDb(dbName);
								if("SQLTOTAL".equals(ms)){
									sqlInfo.setSqlTotal(fromatLong(dataValue));
									sqlInfo.setSqlTotalKeyId(keyId);
								}								
								if("EXECTOTAL".equals(ms)){
									sqlInfo.setExecTotal(fromatLong(dataValue));
									sqlInfo.setExecTotalKeyId(keyId);
								}
								if("EXECAVG".equals(ms)){
									sqlInfo.setExecAvg(fromatLong(dataValue));
									sqlInfo.setExecAvgKeyId(keyId);
								}
								if("CONNSUM".equals(ms)){
									sqlInfo.setConnSum(fromatLong(dataValue));
								}
								if("CONNMIN".equals(ms)){
									sqlInfo.setConnMin(fromatLong(dataValue));
								}
								if("CONNMAX".equals(ms)){
									sqlInfo.setConnMax(fromatLong(dataValue));
								}
							}else if(keyName.startsWith("SQL_TABLESIZE_")){								
								if(keyName.indexOf("_INCREMENT")>-1){
									String tableName = keyName.substring(14, keyName.length()-10);
									TableRecord table = vo.getTableSizeMap().get(tableName);
									if(table==null){
										table = new TableRecord();
										vo.getTableSizeMap().put(tableName, table);
									}
									table.setTableName(tableName);
									table.setRecordInc(fromatLong(dataValue));									
								}
								
								if(keyName.indexOf("_RECORDNUM")>-1){
									String tableName = keyName.substring(14, keyName.length()-10);
									TableRecord table = vo.getTableSizeMap().get(tableName);
									if(table==null){
										table = new TableRecord();
										vo.getTableSizeMap().put(tableName, table);
									}
									table.setTableName(tableName);
									table.setRecordSize(fromatLong(dataValue));	
								}
							}else if(keyName.startsWith("PING_")){
								//String key1 = "PING_"+hostSize+"_responetime"+Constants.AVERAGE_MACHINE_FLAG;
								//String key2 = "PING_"+hostSize+"_packetLoss"+Constants.AVERAGE_MACHINE_FLAG;
								//String key3 = "PING_"+hostSize+"_packetReceive"+Constants.AVERAGE_MACHINE_FLAG;
								
								String[] keys = keyName.split("_");
								String hostSize = keys[1];
								PingPo po = vo.getPingInfoMap().get(hostSize);
								
								if(po==null){
									po = new PingPo();
									vo.getPingInfoMap().put(hostSize, po);
								}		
								po.setHostSize(hostSize);
								if(keyName.indexOf("_responetime")>-1){
									
									Float f =Float.parseFloat(dataValue)/1000;
									
									po.setAverageTime(Utlitites.formatDotTwo(f.toString()));
								}
								if(keyName.indexOf("_packetReceive")>-1){									
									po.setLossPacket(Utlitites.formatDotTwo(dataValue));
								}
								if(keyName.indexOf("_packetLoss")>-1){
									po.getLossHostList().add(dataValue);
								}																
							}else{
								
								if(keyName.equals("PV")){
									vo.setPv(fromatLong(dataValue));
									vo.setPvKeyId(keyId);
									vo.setPvNum(dataValue);
								}
								if(keyName.equals("PV_QPS_AVERAGEUSERTIMES")){
									vo.setApacheQps(dataValue);
									vo.setApacheQpsKeyId(keyId);
								}
								if(keyName.equals("PV_REST_AVERAGEUSERTIMES")){
									vo.setApacheRest(Arith.div(Double.parseDouble(dataValue), 1000, 2)+"");
									vo.setApacheRestKeyId(keyId);
								}
								if(keyName.equals("PV_VISIT_COUNTTIMES")){
									vo.setApachePv(dataValue);
									vo.setApachePvKeyId(keyId);
								}
								
								if(keyName.equals("PV_User_REQUEST_COUNTTIMES")){
									vo.setUserRequestUrlNum(dataValue);
									vo.setUserRequestUrlKeyId(keyId);
								}

								if(keyName.startsWith("PV_VISIT_STATE_")) {	
									String name = keyName.substring(15, 18);
									Integer count = vo.getApacheStateMap().get(name);
									
									if(count == null) {
										count = Integer.valueOf(dataValue);										
										vo.getApacheStateMap().put(name, count);										
									} else {										
										count = Integer.valueOf(dataValue);
									}																		
								}								

//z								
								if(keyName.startsWith("PV_URL_SOURCE_")){
									String name = keyName.substring(14,keyName.length()-11);
									Long count = vo.getSrcUrlPvMap().get(name);
									if(count == null){
										count = Long.valueOf(dataValue);
										vo.getSrcUrlPvMap().put(name, count);
									}
								}
								
								if(keyName.startsWith("PV_URL_REQUEST_")){
									String name = keyName.substring(15,keyName.length()-11);
									Long count = vo.getReqUrlPvMap().get(name);
									if(count == null){
										count = Long.valueOf(dataValue);
										vo.getReqUrlPvMap().put(name, count);
									}
								}
//z
								if(keyName.startsWith("HOSTS")){
									
									String[] r = keyName.split("_");
									
									String m = vo.getMachines();
									if(m==null){
										vo.setMachines(r[1]+":"+fromatLong(dataValue));
									}else{
										String h = r[1]+":"+fromatLong(dataValue);
										if(m.indexOf(h)<0){
											vo.setMachines(r[1]+":"+fromatLong(dataValue)+"/"+m);
										}
									}
								}
								if(keyName.startsWith("ALARM_")){
									if(Utlitites.getLong(dataValue)>0){
										vo.getAlarmMap().put(keyName, fromatLong(dataValue));
										vo.getAlarmKeyIdMap().put(keyName, keyId);
									}
									
								}
								
								if(keyName.startsWith("SUBMITBUY_")){
									if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>0){
										if(keyName.startsWith("SUBMITBUY_Exception")){
											vo.setSubmitBuyExc(fromatLong(dataValue));
										}else{
											vo.setSubmitBuy(fromatLong(dataValue));
										}
									}
								}
								if(keyName.startsWith("SHOPPINGCART_")){
									if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>0){
										vo.setShoppingCart(fromatLong(dataValue));
									}
								}
								
								
								if(keyName.startsWith("CREATE_ORDER_COUNT")){
									vo.setCreateOrderCount(fromatLong(dataValue));
									vo.setCreateOrderCountNum(Utlitites.getLong(dataValue));
								}
								
								if(keyName.startsWith("PAY_ORDER_COUNT")){
									vo.setPayOrderCount(fromatLong(dataValue));
									vo.setPayOrderCountNum(Utlitites.getLong(dataValue));
								}
								if(keyName.startsWith("AMOUNT_ORDER_COUNT")){
									vo.setAmountAll(fromatLong(dataValue));
									vo.setAmountAllNum(Utlitites.getLong(dataValue));
								}
							}}catch(Exception e){
								e.printStackTrace();
							}											
						}						
					

				});
		}catch (Exception e) {
			logger.error("", e);
		}
		return appMap;
		
	}
	
	
	private String parseClassNameWithVersionNum(String _cn) {
//		if(_cn.indexOf(":")>-1){
//			String versionNum = _cn.substring(_cn.lastIndexOf(":"),_cn.length()); //获得类的版本号
//			_cn = _cn.substring(0,_cn.lastIndexOf(":"));										
//			_cn = _cn.substring(_cn.lastIndexOf(".")+1,_cn.length());
//			_cn = _cn + versionNum ; //获得类名+版本号
//		}else{
//			_cn = _cn.substring(_cn.lastIndexOf(".")+1,_cn.length());
//		}

		return _cn;
	}

	private static boolean hasSQLTOP(List<SqlTop10Po> tops,SqlTop10Po po){
		
		for(SqlTop10Po top:tops){
			if(top.getDbName().equals(po.getDbName())&&top.getSqlFullText().equals(po.getSqlFullText())){
				return false;
			}
		}
		return true;
	}
	
	
	public static String fromatLong(String num){
		int index = num.lastIndexOf(".");
		if(index>-1){
			num =num.substring(0,num.lastIndexOf("."));
		}
		
		return num;	
	}
	
	

	
	
	/**
	 * 获取全部app 
	 * @return
	 */
	public List<AppInfoPo> findAllApp(){		
		String sql ="select * from ms_monitor_app where app_type='app'";
		
		final List<AppInfoPo> appList = new ArrayList<AppInfoPo>();
		
		try {
			this.query(sql, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					
					AppInfoPo po = new AppInfoPo();
					po.setAppId(rs.getInt("app_id"));
					po.setAppName(rs.getString("app_name"));
					po.setSortIndex(rs.getInt("sort_index"));
					po.setFeature(rs.getString("feature"));
					appList.add(po);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}		
		Collections.sort(appList);
		return appList;
	}
	
	
	
	public void addDayMonitorCount(int appId,int keyId,String m_data,String collectTime){
		
		String sql = "insert into ms_monitor_count(app_id,key_id,m_data,collect_time,gmt_create)values(?,?,?,?,NOW())";
		try {
			this.execute(sql, new Object[]{appId,keyId,m_data,collectTime});
		} catch (SQLException e) {
			logger.error("", e);
		}
		
	}
	
	public List<Integer> getAppIds(String collectTime){
		String sql = "select distinct app_id from ms_monitor_count where collect_time=?";
		
		final List<Integer> list = new ArrayList<Integer>();
		
		try {
			this.query(sql, new Object[]{collectTime}, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					list.add(rs.getInt("app_id"));
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
	
	/**
	 * 获取应用在一天内产生的记录数量
	 * @param appId
	 * @param date
	 * @return
	 */
	
	public int getDataById(int appId, String collectTime) {
		int dateCount = 0;
		String sql = "select count(*) from ms_monitor_count where app_id=? and collect_time=?";
		try {
			dateCount = this.getIntValue(sql, new Object[]{appId, collectTime});
		} catch (SQLException e) {
			logger.error("", e);
		}
		return dateCount;
	} 
	

}
