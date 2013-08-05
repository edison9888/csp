
package com.taobao.monitor.stat.analyse;

import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.stat.content.ReportContent;
import com.taobao.monitor.stat.content.ReportContentInterface;
import com.taobao.monitor.stat.db.impl.HaBoDataDao;

/**
 * 
 * @author xiaodu
 * @version 2010-4-8 上午10:41:15
 */
public class PvAnalyse extends Analyse {
	
	private static final Logger logger =  Logger.getLogger(PvAnalyse.class);
	
	private HaBoDataDao dao = new HaBoDataDao();
	
	private AppInfoPo app;
	
	private ReportContent ceontent = new ReportContent();
	
	
	public PvAnalyse(String opsName){
		super(opsName);			
		this.app = AppInfoAo.get().getAppInfoByOpsName(opsName);
	}
	
	public void analyseLogFile(ReportContentInterface content) {
		
		logger.info("开始PvAnalyse:"+this.getAppName());	
		if(this.app==null){
			return ;
		}
		String feature = app.getAppDayFeature();
		
		if(feature==null){
			return ;
		}
		
		String[] _features =  feature.split(";");
		String nagiosType = null;
		
		for(String _featrue:_features){
			if(_featrue.indexOf("nagiosType")>-1){
				String[] _nagiosType = _featrue.split(":");
				if(_nagiosType.length==2){
					nagiosType = _nagiosType[1];
				}
				
			}
		}
		
		if(nagiosType==null){
			return ;
		}
		
		logger.info("开始PvJob");
		if(!"-1".equals(nagiosType)){
			
			
			long pv = dao.findAppDatePv(nagiosType,this.getCollectDate());	
			logger.info(app.getOpsName()+"pv:"+pv);
			ceontent.putReportDataByCount(app.getOpsName(), "PV", pv,this.getCollectDate());
			
			long useTime = System.currentTimeMillis();
			Map<String, Long> result = dao.findPvByTime(nagiosType, this.getCollectDate());
			
			logger.info("取得pv 平均点时间:"+(System.currentTimeMillis()-useTime));
			
			for(Map.Entry<String, Long> entry:result.entrySet()){
				ceontent.putReportData(app.getOpsName(), "PV", entry.getValue().toString(), entry.getKey());			
			}
		}else{
			logger.info(app.getOpsName()+"不需要pv");
		}	
	}

	@Override
	protected void insertToDb(ReportContentInterface content) {
		
	}

}
