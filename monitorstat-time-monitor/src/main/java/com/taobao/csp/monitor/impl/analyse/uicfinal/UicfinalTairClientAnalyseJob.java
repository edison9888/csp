
/**
 * monitorstat-time-monitor
 */
package com.taobao.csp.monitor.impl.analyse.uicfinal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;
import com.taobao.monitor.common.util.Arith;

/**
 * 
 * 日志样式
 * 2012-05-07 15:41:44,405 WARN  tairClient - area 75 tairShoot 100.00% tairMissedShoot 0.00% tairTimeoutShoot 0.00% tairCnt 16820 tairSucc 16820 tairTimeout 0 tairMiss 0 tairExpt 0 rtFlowCnt 0 passCnt 0 avgTime 1.09 
 * /home/admin/xxx/logs/tairclient.log
 * @author xiaodu
 *
 * 下午3:53:19
 */
public class UicfinalTairClientAnalyseJob extends AbstractDataAnalyse{
	
//	private Map<String, Map<String, Map<String, Double>>> cacheValueMap = new HashMap<String, Map<String,Map<String,Double>>>();
	
	private Map<String, Map<String, UicTair>> timeAreaMap = new HashMap<String, Map<String,UicTair>>();

	/**
	 * @param appName
	 * @param ip
	 */
	public UicfinalTairClientAnalyseJob(String appName, String ip,String feature) {
		super(appName, ip, feature);
	}

	@Override
	public void analyseOneLine(String logRecord) {
		
		if (logRecord.indexOf("area") == -1) {
			return;
		}
		
		String time = logRecord.substring(0,16);
		
		Map<String, UicTair> areaMap = timeAreaMap.get(time);
		if (areaMap == null) {
			areaMap = new HashMap<String, UicTair>();
			timeAreaMap.put(time, areaMap);
		}
		
		Pattern pattern = Pattern.compile("tairClient - area\\s+([\\d]+)\\s+tairShoot\\s+([\\d\\.]+)%\\s+tairMissedShoot\\s+([\\d\\.]+)%\\s+tairTimeoutShoot\\s+([\\d\\.]+)%\\s++tairCnt\\s+([\\d]+)\\s+tairSucc\\s+([\\d]+)\\s+tairTimeout\\s+([\\d\\.]+)\\s+tairMiss\\s+([\\d\\.]+)\\s+tairExpt\\s+([\\d\\.]+)\\s+");
		
		Matcher m = pattern.matcher(logRecord);		
		if(m.find()){
			String area = m.group(1);
			UicTair uicTair = areaMap.get(area);
			if (uicTair == null) {
				uicTair = new UicTair();
				areaMap.put(area, uicTair);
			}
			
			String tairShoot = m.group(2);
			double f = uicTair.tairShoot;
			uicTair.tairShoot = Arith.div(Arith.add(Double.parseDouble(tairShoot), f), 2,2);
			
			String tairMissedShoot = m.group(3);
			f = uicTair.tairMissedShoot;
			uicTair.tairMissedShoot = Arith.div(Arith.add(Double.parseDouble(tairMissedShoot), f), 2,2);
			
			String tairTimeoutShoot = m.group(4);
			f = uicTair.tairTimeoutShoot;
			uicTair.tairTimeoutShoot = Arith.div(Arith.add(Double.parseDouble(tairTimeoutShoot), f), 2,2);
			
			String tairCnt = m.group(5);
			f = uicTair.tairCnt;
			uicTair.tairCnt = Arith.add(Double.parseDouble(tairCnt), f);
			
			String tairSucc = m.group(6);
			f = uicTair.tairSucc;
			uicTair.tairSucc = Arith.add(Double.parseDouble(tairSucc), f);
			
			String tairTimeout = m.group(7);
			f = uicTair.tairTimeout;
			uicTair.tairTimeout = Arith.add(Double.parseDouble(tairTimeout), f);
			
			String tairMiss = m.group(8);
			f = uicTair.tairMiss;
			uicTair.tairMiss = Arith.add(Double.parseDouble(tairMiss), f);
			
			String tairExpt = m.group(9);
			f = uicTair.tairExpt;
			uicTair.tairExpt = Arith.add(Double.parseDouble(tairExpt), f);
		}
	}

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	@Override
	public void submit() {
//		Map<String, UicTair> timeMap = new HashMap<String, UicTair>();
		
		for (Map.Entry<String, Map<String, UicTair>> entry : timeAreaMap.entrySet()) {
			
//			UicTair tempUicTair = timeMap.get(entry.getKey());
//			if (tempUicTair == null) {
//				tempUicTair = new UicTair();
//				timeMap.put(entry.getKey(), tempUicTair);
//			}
			
			long time = 0l;
			try {
				time = sdf.parse(entry.getKey()).getTime();
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			
			for (Map.Entry<String, UicTair> entry1 : entry.getValue().entrySet()) {
				String namespace = entry1.getKey();
				UicTair uicTair = entry1.getValue();
				try {
					CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.UIC_TAIR_CLIENT, namespace},
							new KeyScope[]{KeyScope.NO, KeyScope.ALL}, new String[]{"Shoot","Miss-Shoot","Out-Shoot","Tair-Cnt","Tair-Succ","Tair-Timeout","Tair-Miss","Tair-Expt"}, 
							new Object[]{uicTair.tairShoot,uicTair.tairMissedShoot,uicTair.tairTimeout,uicTair.tairCnt,uicTair.tairSucc,uicTair.tairTimeout,uicTair.tairMiss,uicTair.tairExpt},
							new ValueOperate[]{ValueOperate.AVERAGE,ValueOperate.AVERAGE,ValueOperate.AVERAGE,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void release() {
		timeAreaMap.clear();
	}

	public static void main(String[] args) {
		
		
		while(true) {
			
			try {
				CollectDataUtilMulti.collect("detail", "172.24.168.132", new Date().getTime(), new String[]{KeyConstants.UIC_TAIR_CLIENT, "12"},
						new KeyScope[]{KeyScope.NO, KeyScope.ALL}, new String[]{"Shoot","Miss-Shoot","Out-Shoot","Tair-Cnt","Tair-Succ","Tair-Timeout","Tair-Miss","Tair-Expt"}, 
						new Object[]{1,1,1,1,1,1,1,1},
						new ValueOperate[]{ValueOperate.AVERAGE,ValueOperate.AVERAGE,ValueOperate.AVERAGE,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD});
				CollectDataUtilMulti.collect("detail", "172.24.168.132", new Date().getTime(), new String[]{KeyConstants.UIC_TAIR_CLIENT, "13"},
						new KeyScope[]{KeyScope.NO, KeyScope.ALL}, new String[]{"Shoot","Miss-Shoot","Out-Shoot","Tair-Cnt","Tair-Succ","Tair-Timeout","Tair-Miss","Tair-Expt"}, 
						new Object[]{1,1,1,1,1,1,1,1},
						new ValueOperate[]{ValueOperate.AVERAGE,ValueOperate.AVERAGE,ValueOperate.AVERAGE,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD});
				CollectDataUtilMulti.collect("detail", "172.24.168.132", new Date().getTime(), new String[]{KeyConstants.UIC_TAIR_CLIENT, "14"},
						new KeyScope[]{KeyScope.NO, KeyScope.ALL}, new String[]{"Shoot","Miss-Shoot","Out-Shoot","Tair-Cnt","Tair-Succ","Tair-Timeout","Tair-Miss","Tair-Expt"}, 
						new Object[]{1,1,1,1,1,1,1,1},
						new ValueOperate[]{ValueOperate.AVERAGE,ValueOperate.AVERAGE,ValueOperate.AVERAGE,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD});
				CollectDataUtilMulti.collect("detail", "172.24.168.132", new Date().getTime(), new String[]{KeyConstants.UIC_TAIR_CLIENT, "15"},
						new KeyScope[]{KeyScope.NO, KeyScope.ALL}, new String[]{"Shoot","Miss-Shoot","Out-Shoot","Tair-Cnt","Tair-Succ","Tair-Timeout","Tair-Miss","Tair-Expt"}, 
						new Object[]{1,1,1,1,1,1,1,1},
						new ValueOperate[]{ValueOperate.AVERAGE,ValueOperate.AVERAGE,ValueOperate.AVERAGE,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD});
				CollectDataUtilMulti.collect("detail", "172.24.168.132", new Date().getTime(), new String[]{KeyConstants.UIC_TAIR_CLIENT, "16"},
						new KeyScope[]{KeyScope.NO, KeyScope.ALL}, new String[]{"Shoot","Miss-Shoot","Out-Shoot","Tair-Cnt","Tair-Succ","Tair-Timeout","Tair-Miss","Tair-Expt"}, 
						new Object[]{1,1,1,1,1,1,1,1},
						new ValueOperate[]{ValueOperate.AVERAGE,ValueOperate.AVERAGE,ValueOperate.AVERAGE,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD});
				CollectDataUtilMulti.collect("detail", "172.24.168.132", new Date().getTime(), new String[]{KeyConstants.UIC_TAIR_CLIENT, "17"},
						new KeyScope[]{KeyScope.NO, KeyScope.ALL}, new String[]{"Shoot","Miss-Shoot","Out-Shoot","Tair-Cnt","Tair-Succ","Tair-Timeout","Tair-Miss","Tair-Expt"}, 
						new Object[]{1,1,1,1,1,1,1,1},
						new ValueOperate[]{ValueOperate.AVERAGE,ValueOperate.AVERAGE,ValueOperate.AVERAGE,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD});
				CollectDataUtilMulti.collect("detail", "172.24.168.132", new Date().getTime(), new String[]{KeyConstants.UIC_TAIR_CLIENT, "18"},
						new KeyScope[]{KeyScope.NO, KeyScope.ALL}, new String[]{"Shoot","Miss-Shoot","Out-Shoot","Tair-Cnt","Tair-Succ","Tair-Timeout","Tair-Miss","Tair-Expt"}, 
						new Object[]{1,1,1,1,1,1,1,1},
						new ValueOperate[]{ValueOperate.AVERAGE,ValueOperate.AVERAGE,ValueOperate.AVERAGE,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	private class UicTair {
		double tairShoot;
		double tairMissedShoot;
		double tairTimeoutShoot;
		double tairCnt;
		double tairSucc;
		double tairTimeout;
		double tairMiss;
		double tairExpt;

		
	}
}
