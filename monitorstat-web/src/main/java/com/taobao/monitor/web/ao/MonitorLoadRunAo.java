package com.taobao.monitor.web.ao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.csp.loadrun.core.LoadrunResult;
import com.taobao.monitor.web.core.dao.impl.MonitorLoadRunDao;
import com.taobao.monitor.web.core.po.LoadRunHost;

/**
 * 
 * @author xiaodu
 * @version 2010-9-2 上午09:40:49
 */
public class MonitorLoadRunAo {

	private static MonitorLoadRunAo ao = new MonitorLoadRunAo();
	private MonitorLoadRunDao dao = new MonitorLoadRunDao();

	private MonitorLoadRunAo() {
	}

	public static MonitorLoadRunAo get() {
		return ao;
	}



	/**
	 * 根据appid取得指定LoadRunHost
	 * 
	 * @return
	 */
	public LoadRunHost findLoadRunHostByAppId(int appId) {

		return dao.findLoadRunHostByAppId(appId);
	}

	/**
	 * 
	 * @return
	 */
	public List<LoadRunHost> findAllLoadRunHost() {
		return dao.findAllLoadRunHost();
	}

	

	/**
	 * 取得最近一个压测时间
	 * 
	 * @param appId
	 * @return
	 */
	public Date findRecentlyLoadDate(int appId) {
		return dao.findRecentlyLoadDate(appId);
	}

	
	/**
	 * 取得最近一次的qps
	 * @param appId
	 * @return
	 */
	public double findRecentlyAppLoadRunQps(int appId) {
		Date date = null;
		try{
			date = findRecentlyLoadDate(appId);
		}catch (Exception e) {
		}
		
		if(date ==null){
			return 0;
		}
		
		return findRecentlyAppLoadRunQps(appId,date);

	}
	
	
	public double findRecentlyAppLoadRunQps(int appId,Date date) {
		
		LoadRunHost host = findLoadRunHostByAppId(appId);
		double maxQps = 0;
		if(date !=null&&host!=null){
			List<LoadrunResult> list = dao.findLoadrunResult(appId,host.getLoadType().getQpsKey(), date);
			for(LoadrunResult r:list){
				double qps = r.getValue();
				if (maxQps < qps) {
					maxQps = qps;
				}
			}
		}
		return maxQps;

	}
	
	
	
	/**
	 * 
	 * @param appId
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Map<String,Double> findLoadrunQpsResult(int appId,Date start,Date end){
		
		LoadRunHost host = findLoadRunHostByAppId(appId);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		List<LoadrunResult> list = dao.findLoadrunResult(appId, host.getLoadType().getQpsKey(), start, end);
		Map<String, Double> map = new HashMap<String, Double>();
		for(LoadrunResult r:list){
			map.put(sdf.format(r.getCollectTime()), r.getValue());
		}
		return map;
	}
	
	

}
