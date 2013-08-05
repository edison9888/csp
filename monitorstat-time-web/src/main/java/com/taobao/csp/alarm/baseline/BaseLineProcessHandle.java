
/**
 * monitorstat-alarm
 */
package com.taobao.csp.alarm.baseline;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.csp.alarm.AlarmKeyContainer;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.time.cache.KeyCache;
import com.taobao.csp.time.util.MonitorAppUtil;
import com.taobao.monitor.common.ao.center.KeyAo;
import com.taobao.monitor.common.cache.AppInfoCache;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.CspKeyInfo;
import com.taobao.monitor.common.po.CspKeyMode;
import com.taobao.monitor.common.po.CspNeedBaseline;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CommonUtil;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;
import com.taobao.monitor.common.util.GroupManager;

/**
 * @author xiaodu
 *
 * 下午8:08:10
 */
public class BaseLineProcessHandle implements Runnable{

	private static final Logger logger = Logger.getLogger(BaseLineProcessHandle.class);

	private AlarmKeyContainer container = null;

	private Map<String,Set<String>> keyprefixMap = new HashMap<String, Set<String>>();

	public void addkeyprefix(String keyprefix,String ... propName){
		Set<String> ps=new HashSet<String>();
		for(String p:propName)
			ps.add(p);
		keyprefixMap.put(keyprefix, ps);
	}

	public BaseLineProcessHandle(AlarmKeyContainer c){

		container = c;

		addkeyprefix("PV",PropConstants.E_TIMES,PropConstants.C_TIME);
		addkeyprefix("HSF-provider",PropConstants.E_TIMES,PropConstants.C_TIME);
		//    	addkeyprefix("HSF-Refer",PropConstants.E_TIMES,PropConstants.C_TIME);
		//		addkeyprefix("HSF-Consumer",PropConstants.E_TIMES,PropConstants.C_TIME);
		//addkeyprefix("Tair-Consumer",PropConstants.E_TIMES,PropConstants.C_TIME,PropConstants.P_SIZE);
		addkeyprefix("TOPINFO",PropConstants.CPU,PropConstants.LOAD);
		addkeyprefix("JVMINFO",PropConstants.JVMMEMORY);
		//addkeyprefix("Notify-Consumer",PropConstants.NOTIFY_C_S,PropConstants.NOTIFY_C_S_RT );

		//		addkeyprefix("Notify-provider"+Constants.S_SEPERATOR+"deliveried_S",PropConstants.E_TIMES,PropConstants.C_TIME);
		//		addkeyprefix("Notify-provider"+Constants.S_SEPERATOR+"postedRet_S",PropConstants.E_TIMES,PropConstants.C_TIME);

	}

	private Thread thread = null;

	private boolean start = false;

	public void startup(){
		if(!start){
			thread = new Thread(this);
			thread.setDaemon(true);
			thread.setName("Thread - BaseLineProcessHandle");
			thread.start();
		}
	}


	private void doBaseLine(String appName,String keyName,String propertyName,String keyScope,Map<String,List<HostPo>> mapsite,Calendar cal ){
		if(KeyScope.APP.toString().equals(keyScope)||KeyScope.ALL.toString().equals(keyScope)){

			AppBaseLineProcessor processor = new AppBaseLineProcessor(appName,keyName,propertyName,cal.getTime());
			try {
				processor.process();
			} catch (Exception e) {
				logger.info("基线计算出错"+e);
			}
			logger.info("执行基线计算:  "+appName+":"+keyName+":"+propertyName+":"+keyScope);
		}

		if(KeyScope.HOST.toString().equals(keyScope)||KeyScope.ALL.toString().equals(keyScope)){
			if(mapsite ==null){
				return;
			}
			//按机房平均
			for(Map.Entry<String,List<HostPo>> entry:mapsite.entrySet()){
				HostBaseLineProcessor processor = new HostBaseLineProcessor(entry.getKey(),appName,keyName,propertyName,cal.getTime());
				try {
					processor.process();
				} catch (Exception e) {
					logger.info("基线计算出错(按机房)"+e);
				}
				logger.info("执行基线计算(按机房):  "+appName+":"+keyName+":"+propertyName+":"+keyScope);
			}
			
			//不按机房平均
			HostAllBaseLineProcessor processorAll = new HostAllBaseLineProcessor(appName, keyName, propertyName, cal.getTime(), mapsite.size());
			try {
				processorAll.process();
			} catch (Exception e) {
				logger.info("基线计算出错(不按机房)"+e);
			}
			logger.info("执行基线计算(不按机房):  "+appName+":"+keyName+":"+propertyName+":"+keyScope);
		}
	}

	public void runBaseLine(String appName,Date runDate,List<CspNeedBaseline> needList){

		if(needList == null){
			needList = new ArrayList<CspNeedBaseline>();
		}

		logger.info("执行"+appName+"基线计算。。。");

		AppInfoPo appInfo = AppInfoCache.getAppInfoByAppName(appName);

		if(appInfo ==null){
			logger.info("无法找到应用"+appName);
			return ;
		}

		Map<String,List<HostPo>> mapsite = CspCacheTBHostInfos.get().getHostMapByRoom(appName);


		Set<String> baselineSet = new HashSet<String>();


		Calendar cal = Calendar.getInstance();
		cal.setTime(runDate);

		for(CspNeedBaseline need: needList){
			CspKeyInfo mode =  KeyCache.getCache().getKeyInfo(need.getKeyName());

			if(mode == null){
				continue;
			}

			Set<String> pSet = null;
			for(Map.Entry<String,Set<String>> entry:keyprefixMap.entrySet()){
				String k = entry.getKey();
				if(mode.getKeyName().startsWith(k)){
					pSet = entry.getValue();
					break;
				}
			}
			if(pSet == null){
				continue;
			}



			String keyScope = mode.getKeyScope();

			for(String propertyName:pSet){
				baselineSet.add(appName+""+mode.getKeyName()+""+propertyName);
				doBaseLine(appName,mode.getKeyName(),propertyName,keyScope,mapsite,cal);
			}
		}
		List<CspKeyMode> kmList = KeyAo.get().findKeyModes(appName);
		for(CspKeyMode ckm:kmList){
			String k = appName+""+ckm.getKeyName()+""+ckm.getPropertyName();
			if(!baselineSet.contains(k)){
				doBaseLine(appName,ckm.getKeyName(),ckm.getPropertyName(),ckm.getKeyScope(),mapsite,cal);
			}
		}



	}


	@Override
	public void run() {

		while(true){

			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, 1);
			cal.set(Calendar.HOUR_OF_DAY, 1);
			long time = cal.getTimeInMillis();

			long wait = time-System.currentTimeMillis();
			if(wait <0){
				wait = 60*24*1000l;
			}

			logger.info("执行基线计算需要等待时间为:"+wait/(60*1000)+"分钟");

			try {
				Thread.sleep(wait);
			} catch (InterruptedException e) {
			}
			List<String> apps = MonitorAppUtil.getMonitorApps();

			//把分组信息组合成应用，和zk监控的采集节点合并
			Map<String, Map<String, List<String>>> mainGroup = GroupManager.get().getGroupInfo();
			for(Entry<String, Map<String, List<String>>> entryMain : mainGroup.entrySet()) {
				String appName = entryMain.getKey();
				Map<String, List<String>> groupMap = entryMain.getValue();
				for(Entry<String, List<String>> entry : groupMap.entrySet()) {
					String groupName = entry.getKey();
					apps.add(CommonUtil.combinAppNameAndGroupName(appName, groupName));
				}
			}
			Map<String,List<CspNeedBaseline>> mapBase =  KeyAo.get().findAllNeedBaseline();

			for(String app:apps){
				if(container.isFit(app)){
					try{
						runBaseLine(app,new Date(),mapBase.get(app));
					}catch (Exception e) {
						logger.error("执行"+app+"基线计算出错",e);
					}
				}
			}

		}

	}

}
