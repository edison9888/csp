package com.taobao.monitor.trade.dailyreport;

import static com.taobao.monitor.common.util.Utlitites.getHuanBiMonitorDate;
import static com.taobao.monitor.common.util.Utlitites.getTongBiMonitorDate;

import java.util.HashMap;
import java.util.Map;

import com.taobao.monitor.baseline.db.po.KeyValueBaseLinePo;
import com.taobao.monitor.web.ao.MonitorDayAo;
import com.taobao.monitor.web.vo.MonitorVo;

public class MonitorVoFacade {
	
	public MonitorVo vo  ;
	public MonitorVo tongqiVo ;
	public MonitorVo huanbiVo ;
	public Map<Integer, KeyValueBaseLinePo> appBaseMap ;

	public MonitorVoFacade (String searchDate, Integer selectAppId) throws Exception {

		String tongbiDate = getTongBiMonitorDate(searchDate);
		String huanbiDate = getHuanBiMonitorDate(searchDate);

		Map<Integer, MonitorVo> map = MonitorDayAo.get().findMonitorCountMapByDate(selectAppId, searchDate);
		Map<Integer, MonitorVo> tongqiMap = MonitorDayAo.get().findMonitorCountMapByDate(selectAppId, tongbiDate);
		Map<Integer, MonitorVo> huanqiMap = MonitorDayAo.get().findMonitorCountMapByDate(selectAppId, huanbiDate);
		Map<Integer, Map<Integer, KeyValueBaseLinePo>> baseMap = MonitorDayAo.get().findMonitorBaseLine(selectAppId,
				searchDate);
		appBaseMap = baseMap.get(selectAppId);

		vo = map.get(selectAppId);
		tongqiVo = tongqiMap.get(selectAppId);
		huanbiVo = huanqiMap.get(selectAppId);
		if (vo == null) {
			vo = new MonitorVo();
		}
		if (tongqiVo == null) {
			tongqiVo = new MonitorVo();
		}
		if (huanbiVo == null) {
			huanbiVo = new MonitorVo();
		}
		if(appBaseMap == null) {
			appBaseMap = new HashMap<Integer, KeyValueBaseLinePo>() ;
		}
	}
	public String getBaseValue(Integer keyId) {

		if (appBaseMap == null || keyId == null)
			return null;
		KeyValueBaseLinePo po = appBaseMap.get(keyId);
		if (po == null) {
			return null;
		} else {
			return po.getBaseLineValue() + "";
		}
	}
}
