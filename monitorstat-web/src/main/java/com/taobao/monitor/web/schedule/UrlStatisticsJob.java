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
 * ��limit���������ͳ�ƺ����ͳ�Ʊ�
 * @author youji.zj
 *
 */
public class UrlStatisticsJob implements Job {

	private static  Logger log = Logger.getLogger(UrlStatisticsJob.class);
			
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		log.info("��ʼͳ������: " + format.format(new Date(System.currentTimeMillis())));
		
		// ��������
		progress(330, 60968);  
		
		// ����ȷ��
		progress(330, 60873);
		
		// ���빺�ﳵ
		progress(341, 65398);
		
		// �ҵĹ��ﳵ
		progress(341, 65401);
		
		// ͳһ����ȷ��
		progress(330, 62185);
		
		log.info("���ͳ������: " + format.format(new Date(System.currentTimeMillis())));
	}
	
	/***
	 * ��������
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
