package com.taobao.monitor.other.site;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math.analysis.RombergIntegrator;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.OpsFreeHostCache;
import com.taobao.monitor.common.util.RemoteCommonUtil;
import com.taobao.monitor.common.util.RemoteCommonUtil.CallBack;

/**
 * 
 * @author xiaodu
 * @version 2011-5-30 ÏÂÎç04:31:37
 */
public class SitePageViewsJob implements Job {

	//157369 /home/admin/cai/logs/cronolog/2011/05/2011-05-30-taobao-access_log
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/yyyy-MM-dd");
		String path = "/home/admin/cai/logs/cronolog/" + sdf.format(cal.getTime()) + "-taobao-access_log";
		Set<String> opsNameSet = OpsFreeHostCache.get().getAllOpsApp();
		for (String opsname : opsNameSet) {
			List<HostPo> hostList = OpsFreeHostCache.get().getHostListNoCache(opsname);
			Map<String, String> ipMap = new HashMap<String, String>();
			if (hostList != null && hostList.size() > 0) {
				for (HostPo po : hostList) {
					String ip = ipMap.get(po.getHostSite());
					if (ip == null) {
						ipMap.put(po.getHostSite(), po.getHostIp());
					} else {
						continue;
					}
				}
				final SitePageView v = new SitePageView();
				
				for (Map.Entry<String, String> entry : ipMap.entrySet()) {
					final String siteName = entry.getKey();
					try {
						RemoteCommonUtil.excute(entry.getValue(), "wc -l " + path, new CallBack() {
							public void doLine(String line) {
								String[] r = line.split(" ");
								int pv = Integer.valueOf(r[0]);
								v.getSitePageViewMap().put(siteName, new Long(pv));
							}
						});
					} catch (Exception e) {
					}
				}
				
				
				
				
			}
		}
	}

}
