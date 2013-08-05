package com.taobao.csp.depend.ao;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.csp.depend.dao.AlarmConfigDao;
import com.taobao.csp.depend.dao.CspAppHsfDependProvideDao;
import com.taobao.csp.depend.po.alarm.AlarmConfigPo;
import com.taobao.csp.depend.util.ConstantParameters;
import com.taobao.csp.depend.util.MethodUtil;

public class AlarmConfigAo {
	private static final Logger logger =  Logger.getLogger(AlarmConfigAo.class);
	
	private AlarmConfigDao dao = new AlarmConfigDao();
	private CspAppHsfDependProvideDao provideDao = new CspAppHsfDependProvideDao();
	private static AlarmConfigAo ao = new AlarmConfigAo();
	public static AlarmConfigAo get(){
		return ao;
	}
	private AlarmConfigAo() {}
	
	/**
	 * 根据报警类型找到所有的alarmPo
	 * @param alarmMode
	 * @return
	 */
	public List<AlarmConfigPo> getAlarmConfig(int alarmMode) {
		return dao.getAlarmConfig(alarmMode);
	}
	
	public Map<String, Set<String>> getHsfAlarmMap(String appName, String collectDay){
		Map<String, Set<String>> map = provideDao.getProvideInterfaceAndConsumeApp(appName, collectDay);
		Map<String, Set<String>> mapReturn = new HashMap<String, Set<String>>();
		/*
		 * chanage IN_HSF-ProviderDetail_com.taobao.udc.client.TargetWarnService:1.0.0_queryCompletedDegree
		 * to 	   com.taobao.udc.client.TargetWarnService:1.0.0_queryCompletedDegree
		 */
		for(String key: map.keySet()) {
			String keyNew = MethodUtil.simplifyHsfInterfaceName(key);
			mapReturn.put(keyNew, map.get(key));
		}
		return mapReturn;
	}
	
	public boolean insertOrUpdate(AlarmConfigPo config){
		try {
			dao.insertOrUpdate(config);
			return true;
		} catch (SQLException e) {
			logger.error("", e);
			return false;
		}
	}
	
	public static void main(String[] args) {
		AlarmConfigPo po = new AlarmConfigPo();
		po.setAlarmMode(ConstantParameters.HSF_PROVIDE_ALARM);
		po.setAppName("inventoryplatform");
		po.setCollectDate(new Date());
		po.setDaysPre(1);
		po.setEmailString("zhongting.zy@taobao.com");
		po.setGMTCreate(new Date());
		po.setLastSendTime(System.currentTimeMillis());
		po.setWangwangString("中亭");
		AlarmConfigAo.get().insertOrUpdate(po);
	}
}
