package com.taobao.monitor.web.ao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.web.core.dao.impl.MonitorStatisticsDataDao;
import com.taobao.monitor.web.po.MonitorStatisticsDataPo;

public class MonitorStatisticsDataAo {
	
	private static final Logger logger =  Logger.getLogger(MonitorStatisticsDataAo.class);
	
	private static MonitorStatisticsDataAo ao = new MonitorStatisticsDataAo();
	
	private MonitorStatisticsDataDao dao = new MonitorStatisticsDataDao();
	
	public static MonitorStatisticsDataAo get() {
		return ao;
	}
	
	/***
	 * 从limit表收集统计表的数据对象，这个方法只会在收集数据的时候用到
	 * @param appId
	 * @param keyId
	 * @return
	 */
	public List<MonitorStatisticsDataPo> findStatisticsLimitData(int appId, int keyId) 
	{
		return dao.findStatisticsLimitData(appId, keyId);
	}
	
	/***
	 * 往统计条插入数据
	 * @param po
	 */
	public void addStatisticsTotalData(MonitorStatisticsDataPo po) {
		dao.addStatisticsTotalData(po);
	}
	
	/***
	 * 删除统计表的数据
	 * @param appId
	 * @param keyId
	 * @param collectTime
	 */
	public void deleteDuplicateStatisticsData(int appId, int keyId, Date collectTime) {
		dao.deleteDuplicateStatisticsData(appId, keyId, collectTime);
	}
	
	
	/***
	 * 按时间查找统计表的数据
	 * @param appId
	 * @param keyId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<MonitorStatisticsDataPo> findStatisticsData(int appId, int keyId, Date beginDate, Date endDate){
		return dao.findStatisticsData(appId, keyId, beginDate, endDate);
	}

}
