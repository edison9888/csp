
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl.analyse.hsf;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.BufferedReader2;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

/**
 * @author xiaodu
 *
 * 下午4:04:03
 */
public class HsfConsumerLogJob extends AbstractDataAnalyse{
	
	private static final Logger logger = Logger.getLogger(HsfConsumerLogJob.class);
	
	private SimpleDateFormat rTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	public HsfConsumerLogJob(String appName,String ip,String feature){
		super(appName,ip, feature);
	} 
	
	//这个记录的是 接口和IP ,provider 可能会是多个IP，这里主要是为了获取接口是什么应用提供的，所以主要是一个Ip不影响结果
	private Map<String,String> interfaceMap = new ConcurrentHashMap<String, String>();
	
	//Map< 机房,Map<应用名称,Integer>>
	private Map<String,Map<String,Integer>> jifangMap = new HashMap<String, Map<String,Integer>>();
	
	public void analyseOneLine(String line){
		analyseOneLine(line, '\01');
	}
	
	public void analyseOneLine(String line, char split){
		
		String[] logResult = StringUtils.splitPreserveAllTokens(line, split);
		// HSF-ConsumerDetail^Acom.taobao.delivery.common.service.DeliveryService:1.0.0^AgetTemplateSnapshotByArea^A172.23.204.110^A2^A2^A2012-03-04 00:01:31^Aitem183064.cm3
		if("HSF-ConsumerDetail".equals(logResult[0])){
			
			String className = logResult[1];
			String methodName = logResult[2];
			String referIp = logResult[3];
			String count = logResult[4];
			
			HostPo host = CspCacheTBHostInfos.get().getHostInfoByIp(referIp);
			if(host != null){
				interfaceMap.put(className+"_"+methodName, host.getOpsName());
				
				Map<String,Integer> tmp = jifangMap.get(host.getHostSite());
				if(tmp == null){
					tmp = new HashMap<String, Integer>();
					jifangMap.put(host.getHostSite(), tmp);
				}
				
				Integer c = tmp.get(host.getOpsName());
				if(c == null){
					tmp.put(host.getOpsName(), Integer.parseInt(count));
				}else{
					tmp.put(host.getOpsName(), Integer.parseInt(count)+c);
				}
			}
			return ;
		}
		if("HSF-Consumer".equals(logResult[0])){
			
			//HSF-Consumer^Acom.taobao.uic.common.service.userdata.UicDataService:1.0.0^AgetData^Acom.taobao.detail.web.module.control.SellerRatedInfo@execute@46^A1^A2^A2012-03-04 00:01:31^Aitem183064.cm3
			String className = logResult[1];
			String methodName = logResult[2];
			int executes =Integer.parseInt(logResult[4]) ;
			int rt = Integer.parseInt(logResult[5]);
			String collectTime = logResult[6].substring(0, 16);;
			try{
				Date date = rTimeFormat.parse(collectTime);
				Calendar cal = Calendar.getInstance();
				cal.setTime(date);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				long cTime = cal.getTimeInMillis();
				
				int s = executes/2;
				int r = rt/2;
				if(s>0){
					analyseConsumer(cTime,className,methodName,s,r);
				}
				if(executes-s>0){
					cal.add(Calendar.MINUTE, -1);
					cTime = cal.getTimeInMillis();
					analyseConsumer(cTime,className,methodName,executes-s,rt-r);
				}
			}catch (Exception e) {
			}
			return ;
		}
		
		
		
		
	}
	
	
	public void submit(){
		
		Map<Long,Map<String,int[]>> referTimeAppMap = new HashMap<Long, Map<String,int[]>>();
		
		Map<Long,Map<String,Map<String,int[]>>> referAppclassMap = new HashMap<Long,Map<String,Map<String,int[]>>>();
		
		
		
		for(Map.Entry<Long,Map<String,Map<String,int[]>>> entry:timeConsumerMap.entrySet()){
			long time = entry.getKey();
			Map<String,Map<String,int[]>> classMap = entry.getValue();
			for(Map.Entry<String,Map<String,int[]>> clazz:classMap.entrySet()){
				String className = clazz.getKey();
				Map<String,int[]> methodMap = clazz.getValue();
				for(Map.Entry<String,int[]> method:methodMap.entrySet()){
					String methodName = method.getKey();
					int[] values = method.getValue();
					
					
					Object[] objs = new Object[]{values[0],values[1]/values[0]};
					
					String appName = interfaceMap.get(className+"_"+methodName);
					if(appName == null){
						appName ="未知";
					}
					
					//refer app 总量
					Map<String,int[]> referApp = referTimeAppMap.get(time);
					if(referApp == null){
						referApp = new HashMap<String, int[]>();
						referTimeAppMap.put(time, referApp);
					}
					int[] app = referApp.get(appName);
					if(app == null){
						app = new int[2];
						referApp.put(appName, app);
					}
					app[0]+=values[0];
					app[1]+=values[1];
					
					//refer app class 总量
					Map<String,Map<String,int[]>> referappclass = referAppclassMap.get(time);
					if(referappclass == null){
						referappclass = new HashMap<String, Map<String,int[]>>();
						referAppclassMap.put(time, referappclass);
					}
					Map<String,int[]> ra = referappclass.get(appName);
					if(ra == null){
						ra = new HashMap<String, int[]>();
						referappclass.put(appName, ra);
					}
					int[] cl = ra.get(className);
					if(cl == null){
						cl = new int[2];
						ra.put(className, cl);
					}
					cl[0]+=values[0];
					cl[1]+=values[1];
					
					
					
					try {
						CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.HSF_CONSUMER,appName,className,methodName}, new KeyScope[]{KeyScope.NO,KeyScope.NO,KeyScope.NO,KeyScope.ALL}, new String[]{"E-times","C-time"}, objs,new ValueOperate[]{ValueOperate.ADD,ValueOperate.AVERAGE});
					} catch (Exception e) {
						logger.error("发送失败", e);
					}
					
				}
			}
		}
		
		
		for(Map.Entry<Long,Map<String,int[]>> entry:referTimeAppMap.entrySet()){
			long time = entry.getKey();
			for(Map.Entry<String,int[]> app:entry.getValue().entrySet()){
				String appName = app.getKey();
				int[] values = app.getValue();
				Object[] objs = new Object[]{values[0],values[1]/values[0]};
				try {
					CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.HSF_CONSUMER,appName}, new KeyScope[]{KeyScope.NO,KeyScope.ALL}, new String[]{"E-times","C-time"}, objs,new ValueOperate[]{ValueOperate.ADD,ValueOperate.AVERAGE});
				} catch (Exception e) {
					logger.error("发送失败", e);
				}
			}
		}
		
		
		
		for(Map.Entry<Long,Map<String,Map<String,int[]>>> entry:referAppclassMap.entrySet()){
			long time = entry.getKey();
			Map<String,Map<String,int[]>> appMap = entry.getValue();
			for(Map.Entry<String,Map<String,int[]>> appEntry:appMap.entrySet()){
				String _appname = appEntry.getKey();
				Map<String,int[]> map = appEntry.getValue();
				for(Map.Entry<String,int[]> classEntry:map.entrySet()){
					String clazz = classEntry.getKey();
					int[] values = classEntry.getValue();
					Object[] objs = new Object[]{values[0],values[1]/values[0]};
					try {
						CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.HSF_CONSUMER,_appname,clazz}, new KeyScope[]{KeyScope.NO,KeyScope.NO,KeyScope.ALL}, new String[]{"E-times","C-time"}, objs,new ValueOperate[]{ValueOperate.ADD,ValueOperate.AVERAGE});
					} catch (Exception e) {
						logger.error("发送失败", e);
					}
				}
			}
		}
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		
		
		HostPo cHost = CspCacheTBHostInfos.get().getHostInfoByIp(getIp());
		if(cHost !=null){
			
			
			for(Map.Entry<String,Map<String,Integer>> entry: jifangMap.entrySet()){
				String hostsite = entry.getKey();
				Map<String,Integer> pMap = entry.getValue();
				List<String> keys = new ArrayList<String>();
				List<Integer> values = new ArrayList<Integer>();
				List<ValueOperate> ops = new ArrayList<ValueOperate>();
				for(Map.Entry<String,Integer> h:pMap.entrySet()){
					keys.add(h.getKey());
					values.add(h.getValue());
					ops.add(ValueOperate.ADD);
				}
				
				try {
					CollectDataUtilMulti.collect(getAppName(), getIp(), cal.getTimeInMillis(), new String[]{KeyConstants.HSF_CONSUMER_RATE,cHost.getHostSite(),hostsite},
							new KeyScope[]{KeyScope.NO,KeyScope.APP,KeyScope.APP},keys.toArray(new String[0]), values.toArray(new Integer[0]),ops.toArray(new ValueOperate[0]));
				} catch (Exception e) {
					logger.error("发送失败", e);
				}
			}
		}
		
		
		
	}
	
	
	Map<Long,Map<String,Map<String,int[]>>> timeConsumerMap = new HashMap<Long, Map<String,Map<String,int[]>>>();
	
	
	
	/**
	 * 
	 * @param time
	 * @param className
	 * @param methodName
	 * @param executes
	 * @param rt
	 */
	private void analyseConsumer(long time,String className,String methodName,int executes,int rt ){
		Map<String,Map<String,int[]>> classMap = timeConsumerMap.get(time);
		if(classMap == null){
			classMap = new HashMap<String, Map<String,int[]>>();
			timeConsumerMap.put(time, classMap);
		}
		
		Map<String,int[]> methodMap = classMap.get(className);
		if(methodMap == null){
			methodMap = new HashMap<String, int[]>();
			classMap.put(className, methodMap);
		}
		
		int[] values = methodMap.get(methodName);
		if(values == null){
			values = new int[2];
			methodMap.put(methodName, values);
		}
		values[0]+=executes;
		values[1]+=rt;
		
	}
	
	

	@Override
	public void release() {
		timeConsumerMap.clear();
		interfaceMap.clear();
	}
	
	
	
	
	
	public static void main(String[] args){
		
		try {
			
			
			HsfConsumerLogJob job = new HsfConsumerLogJob("mytaobao","172.24.18.51","");
			
			
			
			BufferedReader2 reader = new BufferedReader2(new FileReader("D:\\tmp\\monitor_c.log"), '\02');
			String line = null;
			while((line=reader.readLine())!=null){
				
				
				job.analyseOneLine(line);
				
			}
			
			job.submit();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}


	
	

}