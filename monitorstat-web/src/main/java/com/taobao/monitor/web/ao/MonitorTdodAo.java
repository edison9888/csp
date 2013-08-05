package com.taobao.monitor.web.ao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.taobao.monitor.web.core.dao.impl.MonitorTdodDao;
import com.taobao.monitor.web.tdod.TdodPo;
import com.taobao.monitor.web.tdod.TdodVo;

public class MonitorTdodAo {

	private static final MonitorTdodAo ao = new MonitorTdodAo();
	
	private MonitorTdodAo() {
		
	}
	
	public static MonitorTdodAo getInstance() {
		return ao;
	}
	
	private MonitorTdodDao dao = new MonitorTdodDao();
	
	/**
	 * 获取指定日期所有的tdod数据
	 * @author denghaichuan.pt
	 * @version 2012-3-20
	 * @param date
	 * @return
	 */
	public Map<String, TdodVo> findAllTdodByDate(String date) {
		Map<String, TdodVo> appTdodMap = new HashMap<String, TdodVo>();
		List<TdodPo> tdodList = dao.findAllTdodList(date);
		for (TdodPo po : tdodList) {
			String appName = po.getAppName();
			String ip = po.getIp();
			int count = po.getCount();
			
			TdodVo vo = appTdodMap.get(appName);
			if (vo == null) {
				vo = new TdodVo();
				appTdodMap.put(appName, vo);
			}
			vo.setIpKindCount(vo.getIpKindCount() + 1);
			vo.setIpSumCount(vo.getIpSumCount() + count);
			vo.getIpCountMap().put(ip, count);
		}
		return appTdodMap;
	}
	
	/**
	 * 给定应用列表获取tdod数值
	 * @author denghaichuan.pt
	 * @version 2012-3-20
	 * @param appNameSet
	 * @param date
	 * @return
	 */
	public Map<String, TdodVo> findAllTdodByAppList(Set<String> appNameSet, String date) {
		Map<String, TdodVo> appTdodMap = new HashMap<String, TdodVo>();
		List<TdodPo> tdodList = dao.findAllTdodList(appNameSet, date);
		
		for (TdodPo po : tdodList) {
			String appName = po.getAppName();
			String ip = po.getIp();
			int count = po.getCount();
			
			TdodVo vo = appTdodMap.get(appName);
			if (vo == null) {
				vo = new TdodVo();
				appTdodMap.put(appName, vo);
			}
			vo.setIpKindCount(vo.getIpKindCount() + 1);
			vo.setIpSumCount(vo.getIpSumCount() + count);
			vo.getIpCountMap().put(ip, count);
		}
		return appTdodMap;
	}
}
