
package com.taobao.monitor.stat;

import java.util.List;
import java.util.Map;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.OpsFreeHostCache;
import com.taobao.monitor.stat.analyse.ApacheLogAnalyse;
import com.taobao.monitor.stat.analyse.MonitorLogAnalyse2;
import com.taobao.monitor.stat.analyse.PvAnalyse;
import com.taobao.monitor.stat.content.ReportContent;
import com.taobao.monitor.stat.util.DepIpInfo;
import com.taobao.monitor.stat.util.DepIpInfoContain;

/**
 * 
 * @author xiaodu
 * @version 2011-6-10 ÏÂÎç05:26:14
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String HOSTNAME = System.getenv("HOSTNAME");
		List<AppInfoPo> appList = AppInfoAo.get().findAllAppByServerNameAndAppType(HOSTNAME,"day");
		try {
			ApacheLogAnalyse a = new ApacheLogAnalyse("wlbconsole");
			a.analyseLogFile(new ReportContent());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
