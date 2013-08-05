package com.taobao.monitor.web.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.taobao.monitor.web.ao.MonitorStatisticsDataAo;
import com.taobao.monitor.web.po.MonitorStatisticsDataPo;

/***
 * 把limit表的数据做统计后插入统计表
 * @author youji.zj
 *
 */
public class UrlStatisticsJob implements Job {

	private static  Logger log = Logger.getLogger(UrlStatisticsJob.class);
			
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		log.info("开始统计数据: " + format.format(new Date(System.currentTimeMillis())));
		
		// 立即购买
		progress(330, 60968);  
		
		// 订单确认
		progress(330, 60873);
		
		// 加入购物车
		progress(341, 65398);
		
		// 我的购物车
		progress(341, 65401);
		
		// 统一订单确认
		progress(330, 62185);
		
		log.info("完成统计数据: " + format.format(new Date(System.currentTimeMillis())));
	}
	
	/***
	 * 操作过程
	 * @param appId
	 * @param keyId
	 */
	public void progress(int appId, int keyId) {
		List<MonitorStatisticsDataPo> statisticsData = MonitorStatisticsDataAo.get().findStatisticsLimitData(appId, keyId);
		
		for (MonitorStatisticsDataPo po : statisticsData) {
			MonitorStatisticsDataAo.get().deleteDuplicateStatisticsData(po.getAppId(), po.getKeyId(), po.getCollectTime());
			MonitorStatisticsDataAo.get().addStatisticsTotalData(po);
		}
	
	}	
	
}
