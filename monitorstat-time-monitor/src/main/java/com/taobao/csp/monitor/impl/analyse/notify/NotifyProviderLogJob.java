
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl.analyse.notify;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;
import com.taobao.monitor.common.util.BufferedReader2;

/**
 * notify Provider 端分析
 * @author denghaichuan.pt
 * @version 2012-4-16
 */
public class NotifyProviderLogJob extends AbstractDataAnalyse {

	private static final Logger logger = Logger.getLogger(NotifyProviderLogJob.class);

	private SimpleDateFormat rTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private static Set<String> filterSet = new HashSet<String>();
	static {
		//过滤掉
		//		filterSet.add("postedRet_S");
		//		filterSet.add("postedRet_NH");
		//		filterSet.add("postedRet_EC");
		//		filterSet.add("postedRet_TO");
		//		filterSet.add("postedRet_UK");
		//		filterSet.add("postedRet_F");
		//		filterSet.add("postedRet_E");
		//		filterSet.add("postedRet_TPB");
		//		filterSet.add("postedRet_NL");
		//		filterSet.add("output_OverFlow");

		//添加的
		filterSet.add("pm_oper");
		filterSet.add("pm_oper_failed");
		filterSet.add("pm_oper_timeout");
	}

	public NotifyProviderLogJob(String appName,String ip,String f){
		super(appName, ip, f);
	} 

	private Map<String, Map<String, Map<String, Map<String, Notify>>>> timeCacheValue = new HashMap<String, Map<String,Map<String,Map<String,Notify>>>>();
	@Override
	public void analyseOneLine(String line) {
		try {
			String[] logResult = StringUtils.splitPreserveAllTokens(line, '\01');
			if (logResult.length != 7) {
				return;
			}
			String key1 = logResult[0];
			String key2 = logResult[1];
			String key3 = logResult[2];
			
			if(!filterSet.contains(key1)) {
//				MonitorLog.addStat("NotifyProviderData", new String[]{"provider_lost"}, new Long[]{1l});
				return;
			}
				
				
			long useTime;
			if (logResult[3].equals("")) {
				useTime = 0;
			} else {
				useTime = Long.parseLong(logResult[3]);
			}
			
			long count = Long.parseLong(logResult[4]);
			String collctTime = logResult[5].substring(0, 16);
			if(timeCacheValue.size() > 0 && !timeCacheValue.containsKey(collctTime)) {
				submit();	//来新一分钟的数据，就发送数据，并马上清空map
			}
			Map<String, Map<String, Map<String, Notify>>> allKeyMap = timeCacheValue.get(collctTime);
			if (allKeyMap == null) {
				allKeyMap = new HashMap<String, Map<String,Map<String,Notify>>>();
				timeCacheValue.put(collctTime, allKeyMap);
			}
			
			Map<String, Map<String, Notify>> twoKeyMap = allKeyMap.get(key1);
			if (twoKeyMap == null) {
				twoKeyMap = new HashMap<String, Map<String,Notify>>();
				allKeyMap.put(key1, twoKeyMap);
			}
			
			Map<String, Notify> oneKeyMap = twoKeyMap.get(key2);
			if (oneKeyMap == null) {
				oneKeyMap = new HashMap<String, Notify>();
				twoKeyMap.put(key2, oneKeyMap);
			}
			
			Notify notify = oneKeyMap.get(key3);
			if (notify == null) {
				notify = new Notify();
				oneKeyMap.put(key3, notify);
			}
			
			notify.count += count;
			notify.time += useTime;
		} catch (NumberFormatException e) {
			logger.info("分析notify出错:"+line,e);
		}
	}
	
	@Override
	public void submit() {
		for (Map.Entry<String, Map<String, Map<String, Map<String, Notify>>>> entry : timeCacheValue.entrySet()) {
			try {
				Long time = rTimeFormat.parse(entry.getKey()).getTime();
				Map<String, Map<String, Map<String, Notify>>> allMap = entry.getValue();
				
				for (Map.Entry<String, Map<String, Map<String, Notify>>> entry1 : allMap.entrySet()) {
					String oneKey = entry1.getKey();
					Map<String, Map<String, Notify>> oneMap = entry1.getValue();
					
					for(Map.Entry<String, Map<String, Notify>> entry2 : oneMap.entrySet()) {
						String twoKey = entry2.getKey();
						Map<String, Notify> twoMap = entry2.getValue();
						
						for (Map.Entry<String, Notify> entry3 : twoMap.entrySet()) {
							String threeKey = entry3.getKey();
							Notify notify = entry3.getValue();
							
							Object[] objs = new Object[]{notify.count,notify.time};
							long starTime = System.currentTimeMillis();							
							try {
								CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.NOTIFY_PROVIDER,oneKey,twoKey,threeKey}, 
										new KeyScope[]{KeyScope.NO,KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time"}, objs,
										new ValueOperate[]{ValueOperate.ADD,ValueOperate.ADD});
//								MonitorLog.addStat("NotifyProviderData", new String[]{"provider_send"}, new Long[]{1l,System.currentTimeMillis() - starTime});
							} catch (Exception e) {
								logger.error("发送失败", e);
//								MonitorLog.addStat("NotifyProviderData", new String[]{"provider_error"}, new Long[]{1l,System.currentTimeMillis() - starTime});
							}
						}
					}
				}
			} catch (ParseException e) {
				logger.error("分析notify 出错",e);
			}
		}
		release();
	}

	@Override
	public void release() {
		timeCacheValue.clear();
	}

	//	private void oneSubmit(Map<Long, Map<String, Notify>> oneKeyMap) {
	//		for (Map.Entry<Long, Map<String, Notify>> entry : oneKeyMap.entrySet()) {
	//			Long time = entry.getKey();
	//			for (Map.Entry<String, Notify> entry1 :  entry.getValue().entrySet()) {
	//				String oneKey = entry1.getKey();
	//				Notify notify = entry1.getValue();
	//				
	//				Object[] objs = new Object[]{notify.count,notify.time};
	//				try {
	//					CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.NOTIFY_PROVIDER,oneKey}, 
	//							new KeyScope[]{KeyScope.NO,KeyScope.APP}, new String[]{"E-times","C-time"}, objs,
	//							new ValueOperate[]{ValueOperate.ADD,ValueOperate.ADD});
	//				} catch (Exception e) {
	//					logger.error("发送失败", e);
	//				}
	//			}
	//		}
	//	}

	private class Notify{
		private long time = 0;
		private long count = 0;
	}

	public static void main(String[] args){

		try {

			//			Map<String,HostPo> map = CspCacheTBHostInfos.get().getHostInfoMapByOpsName("tripagent");
			//			for(Map.Entry<String, HostPo> entry:map.entrySet()){

			NotifyProviderLogJob job = new NotifyProviderLogJob("tripagent","","");
			for(int i=0; i<10000; i++) {
				BufferedReader2 reader = new BufferedReader2(new FileReader("D:\\tmp\\monitor-app-sun.misc.Launcher$AppClassLoader.log"), '\02');
				String line = null;
				while((line=reader.readLine())!=null){
					job.analyseOneLine(line);
				}
				job.submit();
				job.release();	
				try {
					TimeUnit.SECONDS.sleep(60);
				} catch (Exception e) {
					// TODO: handle exception
				}
				System.out.println(i + "-----over!");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

}
