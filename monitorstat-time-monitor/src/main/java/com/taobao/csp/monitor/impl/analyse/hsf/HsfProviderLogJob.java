
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl.analyse.hsf;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class HsfProviderLogJob extends AbstractDataAnalyse{
	
	private static final Logger logger = Logger.getLogger(HsfProviderLogJob.class);
	
	private SimpleDateFormat rTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	
	public HsfProviderLogJob(String appName,String ip,String feature){
		super(appName,ip,feature);
	} 
	
	public void analyseOneLine(String line){
		analyseOneLine(line, '\01');
	}
	public void analyseOneLine(String line,char split) {
		
		
		
		String[] logResult = StringUtils.splitPreserveAllTokens(line, split);
		
		if(!"HSF-ProviderDetail".equals(logResult[0])){
			return ;
		}
		
		// HSF-ProviderDetail  com.taobao.item.service.ItemService:1.0.0-L1 sellerUploadPropertyImage 172.24.22.52 1 10 2010-04-10 00:01:13 v015180.cm4.tbsite.net
		
		String className = logResult[1];
		String methodName = logResult[2];
		String referIp = logResult[3];
		int executes =Integer.parseInt(logResult[4]) ;
		int rt = Integer.parseInt(logResult[5]);
		String collectTime = logResult[6].substring(0, 16);
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
				analyseProvide(cTime,className,methodName,s,r);
				analyseRefer(cTime,referIp,className,methodName,s,r);
			}
			
			if(executes-s>0){
				cal.add(Calendar.MINUTE, -1);
				cTime = cal.getTimeInMillis();
				analyseProvide(cTime,className,methodName,executes-s,rt-r);
				analyseRefer(cTime,referIp,className,methodName,executes-s,rt-r);
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
	public void submit(){
		
		
		Map<Long,int[]>allProvideMap = new HashMap<Long, int[]>();
		
		Map<Long,Map<String,int[]>> allclassMap = new HashMap<Long,Map<String,int[]>>();
		
		
		for(Map.Entry<Long,Map<String,Map<String,int[]>>> entry:timeProvideMap.entrySet()){
			long time = entry.getKey();
			Map<String,Map<String,int[]>> classMap = entry.getValue();
			for(Map.Entry<String,Map<String,int[]>> clazz:classMap.entrySet()){
				String className = clazz.getKey();
				Map<String,int[]> methodMap = clazz.getValue();
				for(Map.Entry<String,int[]> method:methodMap.entrySet()){
					String methodName = method.getKey();
					int[] values = method.getValue();
					Object[] objs = new Object[]{values[0],values[1]/values[0]};
					
					
					//记录总量
					int[] all = allProvideMap.get(time);
					if(all == null){
						all = new int[2];
						allProvideMap.put(time, all);
					}
					all[0]+=values[0];
					all[1]+=values[1];
					//记录class 
					Map<String,int[]> allclassPromap = allclassMap.get(time);
					if(allclassPromap == null){
						allclassPromap = new HashMap<String, int[]>();
						allclassMap.put(time, allclassPromap);
					}
					int[] allclass =allclassPromap.get(className);
					if(allclass == null){
						allclass = new int[2];
						allclassPromap.put(className, allclass);
					}
					allclass[0]+=values[0];
					allclass[1]+=values[1];
					
					try {
						CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.HSF_PROVIDER,className,methodName}, new KeyScope[]{KeyScope.NO,KeyScope.NO,KeyScope.ALL}, new String[]{"E-times","C-time"}, objs,new ValueOperate[]{ValueOperate.ADD,ValueOperate.AVERAGE});
					} catch (Exception e) {
						logger.error("发送失败", e);
					}
				}
			}
		}
		//发送总量
		for(Map.Entry<Long,int[]> entry:allProvideMap.entrySet()){
			try {
				long time = entry.getKey();
				int[] values = entry.getValue();
				Object[] objs = new Object[]{values[0],values[1]/values[0]};
				CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.HSF_PROVIDER}, new KeyScope[]{KeyScope.ALL}, new String[]{"E-times","C-time"}, objs,new ValueOperate[]{ValueOperate.ADD,ValueOperate.AVERAGE});
			} catch (Exception e) {
				logger.error("发送失败", e);
			}
		}
		
		//发送class总量
		for(Map.Entry<Long,Map<String,int[]>> entry: allclassMap.entrySet()){
			long time = entry.getKey();
			for(Map.Entry<String,int[]> clazz:entry.getValue().entrySet()){
				try {
					int[] values = clazz.getValue();
					Object[] objs = new Object[]{values[0],values[1]/values[0]};
					CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.HSF_PROVIDER,clazz.getKey()}, new KeyScope[]{KeyScope.NO,KeyScope.ALL}, new String[]{"E-times","C-time"}, objs,new ValueOperate[]{ValueOperate.ADD,ValueOperate.AVERAGE});
				} catch (Exception e) {
					logger.error("发送失败", e);
				}
			}
		}
		
		
		Map<Long,Map<String,int[]>> referAppMap = new HashMap<Long, Map<String,int[]>>();
		
		Map<Long,Map<String,Map<String,int[]>>> referAppclassMap = new HashMap<Long,Map<String,Map<String,int[]>>>();
		
		
		
		for(Map.Entry<Long,Map<String,Map<String,Map<String,int[]>>>> entry:timeReferMap.entrySet()){
			long time = entry.getKey();
			Map<String,Map<String,Map<String,int[]>>> appMap = entry.getValue();
			for(Map.Entry<String,Map<String,Map<String,int[]>>> appentry:appMap.entrySet()){
				String appName = appentry.getKey();
				Map<String,Map<String,int[]>> classmap = appentry.getValue();
				for(Map.Entry<String,Map<String,int[]>> classEntry:classmap.entrySet()){
					String className = classEntry.getKey();
					Map<String,int[]> methodMap = classEntry.getValue();
					for(Map.Entry<String,int[]> methodEntry:methodMap.entrySet()){
						String methodName = methodEntry.getKey();
						int[] values = methodEntry.getValue();
						Object[] objs = new Object[]{values[0],values[1]/values[0]};
						
						
						//refer app 总量
						Map<String,int[]> referApp = referAppMap.get(time);
						if(referApp == null){
							referApp = new HashMap<String, int[]>();
							referAppMap.put(time, referApp);
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
							CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.HSF_REFER,appName,className,methodName}, new KeyScope[]{KeyScope.NO,KeyScope.NO,KeyScope.NO,KeyScope.ALL}, new String[]{"E-times","C-time"}, objs,new ValueOperate[]{ValueOperate.ADD,ValueOperate.AVERAGE});
						} catch (Exception e) {
							logger.error("发送失败", e);
						}
					}
				}
			}
		}
		
		
		
		
		for(Map.Entry<Long,Map<String,int[]>> entry:referAppMap.entrySet()){
			long time = entry.getKey();
			for(Map.Entry<String,int[]> app:entry.getValue().entrySet()){
				String appName = app.getKey();
				int[] values = app.getValue();
				Object[] objs = new Object[]{values[0],values[1]/values[0]};
				try {
					CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.HSF_REFER,appName}, new KeyScope[]{KeyScope.NO,KeyScope.ALL}, new String[]{"E-times","C-time"}, objs,new ValueOperate[]{ValueOperate.ADD,ValueOperate.AVERAGE});
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
						CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.HSF_REFER,_appname,clazz}, new KeyScope[]{KeyScope.NO,KeyScope.NO,KeyScope.ALL}, new String[]{"E-times","C-time"}, objs,new ValueOperate[]{ValueOperate.ADD,ValueOperate.AVERAGE});
					} catch (Exception e) {
						logger.error("发送失败", e);
					}
				}
			}
		}
		
		
		
		for(Map.Entry<Long,Map<String,Map<String,Long>>>  entry:timeAreaMap.entrySet()){
			long time = entry.getKey();
			for(Map.Entry<String,Map<String,Long>> siteEtrny:entry.getValue().entrySet()){
				String selfSite = siteEtrny.getKey();
				List<String> keys = new ArrayList<String>();
				List<Long> values = new ArrayList<Long>();
				List<ValueOperate> ops = new ArrayList<ValueOperate>();
				for(Map.Entry<String,Long> referEntry:siteEtrny.getValue().entrySet()){
					String referSite = referEntry.getKey();
					keys.add(referSite);
					values.add(referEntry.getValue());
					ops.add(ValueOperate.ADD);
				}
				
				try {
					CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.HSF_AREA_RATE,selfSite}, 
							new KeyScope[]{KeyScope.NO,KeyScope.APP},
							keys.toArray(new String[0]), values.toArray(new Long[0]),ops.toArray(new ValueOperate[0]));
				} catch (Exception e) {
					logger.error("发送失败", e);
				}
			}
		}
		
		
		
		
		
		
	}
	
	
	Map<Long,Map<String,Map<String,int[]>>> timeProvideMap = new HashMap<Long, Map<String,Map<String,int[]>>>();
	
	/**
	 * 
	 * @param time
	 * @param className
	 * @param methodName
	 * @param executes
	 * @param rt
	 */
	private void analyseProvide(long time,String className,String methodName,int executes,int rt ){
		Map<String,Map<String,int[]>> classMap = timeProvideMap.get(time);
		if(classMap == null){
			classMap = new HashMap<String, Map<String,int[]>>();
			timeProvideMap.put(time, classMap);
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
	
	
	Map<Long,Map<String,Map<String,Long>>> timeAreaMap = new HashMap<Long, Map<String,Map<String,Long>>>();
	
	Map<Long,Map<String,Map<String,Map<String,int[]>>>> timeReferMap = new HashMap<Long, Map<String,Map<String,Map<String,int[]>>>>();
	/**
	 * 
	 * @param time
	 * @param referip
	 * @param className
	 * @param methodName
	 * @param executes
	 * @param rt
	 */
	private void analyseRefer(long time,String referip,String className,String methodName,int executes,int rt ){
		
		
		Map<String,Map<String,Long>> areaMap= timeAreaMap.get(time);
		if(areaMap ==null){
			areaMap = new HashMap<String, Map<String,Long>>();
			timeAreaMap.put(time, areaMap);
		}
		
		HostPo self = CspCacheTBHostInfos.get().getHostInfoByIp(this.getIp());
		HostPo host = CspCacheTBHostInfos.get().getHostInfoByIp(referip);
		if(self != null){
			Map<String,Long> referEreaMap = areaMap.get(self.getHostSite());
			if(referEreaMap == null){
				referEreaMap = new HashMap<String, Long>();
				areaMap.put(self.getHostSite(), referEreaMap);
			}
			String referSite = "未知";
			if(host != null){
				referSite = host.getHostSite();
			}
			Long data = referEreaMap.get(referSite);
			if(data == null){
				referEreaMap.put(referSite,new  Long(executes));
			}else{
				referEreaMap.put(referSite, executes+data);
			}
		}
		
		
		
		
		
		Map<String,Map<String,Map<String,int[]>>> referMap = timeReferMap.get(time);
		if(referMap == null){
			referMap = new HashMap<String, Map<String,Map<String,int[]>>>();
			timeReferMap.put(time, referMap);
		}
		
		String appName = "未知";
		
		
		if(host != null){
			appName = host.getOpsName();
		}
//		else{
//			appName = referip;
//		}
		
		Map<String,Map<String,int[]>> classMap = referMap.get(appName);
		
		if(classMap == null){
			classMap = new HashMap<String, Map<String,int[]>>();
			referMap.put(appName, classMap);
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
		timeReferMap.clear();
		timeProvideMap.clear();
	}
	
	
	public static void main(String[] args){
		
		try {
			
			
			HsfProviderLogJob job = new HsfProviderLogJob("itemcenter","123.123.13.13","");
			
			
			BufferedWriter writer  = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:\\tmp\\monitor.log_f"),"gbk"));
			
			BufferedReader2 reader = new BufferedReader2(new FileReader("D:\\tmp\\path\\monitor-app-org.eclipse.osgi.internal.baseadaptor.DefaultClassLoader.log"), '\02');
			String line = null;
			while((line=reader.readLine())!=null){
				
				writer.write(line);
				
				job.analyseOneLine(line);
				writer.newLine();
				
			}
			
			job.submit();
			
			writer.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}




	
	
	

}