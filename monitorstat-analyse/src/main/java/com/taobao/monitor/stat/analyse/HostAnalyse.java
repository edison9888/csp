
package com.taobao.monitor.stat.analyse;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.OpsFreeHostCache;
import com.taobao.monitor.stat.content.ReportContentInterface;

/**
 * 解析host xml 文件 将机器数量入库
 * @author xiaodu
 * @version 2010-4-8 下午05:53:33
 */
public class HostAnalyse extends Analyse {	
	
	public HostAnalyse(String appName) {
		super(appName);
	}
	private static final Logger logger =  Logger.getLogger(HostAnalyse.class);
	public void analyseLogFile(ReportContentInterface content) {
		logger.info("开始HostAnalyse");
		AppInfoPo app = AppInfoAo.get().getAppInfoByOpsName(this.getAppName());
		Map<String,List<HostPo>> map = OpsFreeHostCache.get().getHostMapNoCache(app.getOpsField(), app.getOpsName());
		
		String appName = this.getAppName();
		
		Iterator<Map.Entry<String,List<HostPo>>> ithh = map.entrySet().iterator();
		while(ithh.hasNext()){
			Map.Entry<String,List<HostPo>> entryhh = ithh.next();
			String key = entryhh.getKey().toLowerCase();
			content.putReportDataByCount(appName, "HOSTS_"+key, entryhh.getValue().size(), this.getCollectDate());
			
		}

	}
	
	@Override
	protected void insertToDb(ReportContentInterface content) {
		
	}

}
