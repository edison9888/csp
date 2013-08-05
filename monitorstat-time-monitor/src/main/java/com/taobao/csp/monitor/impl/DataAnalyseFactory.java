
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl;

import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.AnalyseInfo;
import com.taobao.csp.monitor.DataAnalyse;
import com.taobao.csp.monitor.JobInfo;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;
import com.taobao.csp.monitor.impl.analyse.TimeUtil;
import com.taobao.csp.monitor.impl.script.ScriptAnalyse;
import com.taobao.monitor.common.javac.DynamicCompilerCode;

/**
 * @author xiaodu
 *
 * ÉÏÎç10:44:04
 */
public class DataAnalyseFactory {
	
private static Map<String,String> classMap = new HashMap<String, String>();
	
	static {
		classMap.put("com.taobao.monitor.time.analyse.MonitorLogAnalyse", "com.taobao.csp.monitor.impl.analyse.HboLogAnalyse");
		classMap.put("com.taobao.monitor.time.analyse.MonitorLogAnalyse2", "com.taobao.csp.monitor.impl.analyse.HboLogAnalyse");
		classMap.put("com.taobao.monitor.time.analyse.MbeanLogAnalyse", "com.taobao.csp.monitor.impl.analyse.mbean.MbeanLogAnalyse");
		classMap.put("com.taobao.monitor.time.analyse.GCLogAnalyse", "com.taobao.csp.monitor.impl.analyse.jvm.GcLogJob");
		classMap.put("com.taobao.monitor.time.analyse.ApacheLogAnalyse", "com.taobao.csp.monitor.impl.analyse.apache.ApacheLogJob");
		classMap.put("com.taobao.monitor.time.analyse.NginxLogAnalyse1", "com.taobao.csp.monitor.impl.analyse.apache.NginxLogAnalyse1");
		classMap.put("com.taobao.monitor.time.analyse.NginxLogAnalyse", "com.taobao.csp.monitor.impl.analyse.apache.NginxLogAnalyse");
		
		classMap.put("com.taobao.monitor.time.analyse.ExceptionAnalyse", "com.taobao.csp.monitor.impl.analyse.exception.ExceptionLogJob");
		classMap.put("com.taobao.monitor.time.analyse.JbossLogAnalyse", "com.taobao.csp.monitor.impl.analyse.apache.JbossLogAnalyse");
		classMap.put("com.taobao.monitor.time.analyse.TdodRateAnalyse", "com.taobao.csp.monitor.impl.analyse.apache.TdodLogAnalyse");
		classMap.put("com.taobao.monitor.time.analyse.PatternStringAnalyse", "com.taobao.csp.monitor.impl.analyse.other.PatternStringAnalyse");
		
		classMap.put("com.taobao.monitor.time.analyse.BlockCountAnalyse", "com.taobao.csp.monitor.impl.analyse.other.BlockCountAnalyse");
		classMap.put("com.taobao.monitor.time.analyse.ICCacheHitRateAnalyse", "com.taobao.csp.monitor.impl.analyse.other.ICCacheHitRateAnalyse");
		classMap.put("com.taobao.monitor.time.analyse.TpMonitorAnalyse", "com.taobao.csp.monitor.impl.analyse.other.TpMonitorAnalyse");
		classMap.put("com.taobao.monitor.time.analyse.UicDBLogFileAnalyse", "com.taobao.csp.monitor.impl.analyse.other.UicDBLogFileAnalyse");
		classMap.put("com.taobao.monitor.time.analyse.UicFinalClientLogFileAnalyse", "com.taobao.csp.monitor.impl.analyse.other.UicFinalClientLogFileAnalyse");
		classMap.put("com.taobao.monitor.time.analyse.UicServiceLogFileAnalyse", "com.taobao.csp.monitor.impl.analyse.other.UicServiceLogFileAnalyse");
		
		classMap.put("com.taobao.monitor.time.analyse.UicConherenceStatisticLogAnalyse", "com.taobao.csp.monitor.impl.analyse.other.UicConherenceStatisticLogAnalyse");
		classMap.put("com.taobao.monitor.time.analyse.UicTairLogFileAnalyse", "com.taobao.csp.monitor.impl.analyse.other.UicTairLogFileAnalyse");
		classMap.put("com.taobao.monitor.time.analyse.HuijinMymallAnalyse", "com.taobao.csp.monitor.impl.analyse.other.HuijinMymallAnalyse");
		classMap.put("com.taobao.monitor.time.analyse.HuijinRatingAnalyse", "com.taobao.csp.monitor.impl.analyse.other.HuijinRatingAnalyse");
		classMap.put("com.taobao.monitor.time.analyse.CommonLogFileAnalyse", "com.taobao.csp.monitor.impl.analyse.other.CommonLogFileAnalyse");
		classMap.put("com.taobao.monitor.time.analyse.TcMonitorAnalyse", "com.taobao.csp.monitor.impl.analyse.other.TcMonitorAnalyse");
		
		
		
		classMap.put("com.taobao.monitor.time.analyse.WebWangWangLogAnalyse", "com.taobao.csp.monitor.impl.analyse.other.WebWangWangLogAnalyse");
		classMap.put("com.taobao.monitor.time.analyse.ShopMonitorAnalyse", "com.taobao.csp.monitor.impl.analyse.other.ShopMonitorAnalyse");
		
		
		
	}
	
	
	private static final Logger logger =  Logger.getLogger(DataAnalyseFactory.class);
	
	
	public static DataAnalyse createAnalyse(JobInfo info){
		Set<AnalyseInfo> set = info.getAnalyseList();
		DataAnalyseImpl impl = new DataAnalyseImpl();
		
		for(AnalyseInfo a:set){
			DataAnalyse d = createAnalyse(info,a);
			if(d != null){
				impl.addAnalyse(d);
			}
		}
		
		return impl;
	}
	
	private static DataAnalyse createAnalyse(JobInfo job,AnalyseInfo info){
		
		String type = info.getAnalyseType();
		
		if("2".equals(type)){//javascript
			
//			ScriptAnalyse analyse = ScriptAnalyseFactory.get().getScriptAnalyse(ScriptType.JavaScript, info.getAnalyseDetail());
//			if(analyse != null){
//				return new ScriptProxy(job.getJobName(),job.getAppName(),job.getIp(),analyse);
//			}
		}
		if("1".equals(type)){//java
			return createJava(job.getAppName(),job.getIp(),info);
		}
		if("11".equals(type)){//dynamic-java
			try {
				Class<?> clazz = DynamicCompilerCode.get().getClassByCache(info.getAnalyseDetail(),  "com.taobao.csp.monitor.analyse.tmp");
				try {
					Constructor con = clazz.getConstructor(String.class,String.class);
					Object object = con.newInstance(job.getAppName(),job.getIp());
					if(object instanceof AbstractDataAnalyse){
						return (AbstractDataAnalyse)object;
					}else{
						logger.error(clazz.getName()+" is not implements DataAnalyse");
					}
				} catch (Exception e) {
					logger.error(clazz.getName()+" Constructor error",e);
				} 
				
			} catch (Exception e) {
				logger.error("DynamicCompilerCode "+info.getAnalyseClass()+"£º"+info.getAnalyseDetail(),e);
			}
		}
		return null;
	}
	
	private static DataAnalyse createJava(String appName,String ip,AnalyseInfo info){
		
		String clazz = classMap.get(info.getAnalyseClass());
		if(clazz == null){
			clazz = info.getAnalyseClass();
		}
		try {
			Class _class = Class.forName(clazz);
			if(_class != null){
				Constructor con =null;
				try {
					try{
						con = _class.getConstructor(String.class,String.class,String.class);
					}catch (Exception e) {
					}
					if(con != null){
						Object object = con.newInstance(appName,ip,info.getAnalyseDetail());
						if(object instanceof AbstractDataAnalyse){
							return (AbstractDataAnalyse)object;
						}else{
							logger.error(_class.getName()+" not extends MsAnalyse ");
						}
					}
					try{
						con = _class.getConstructor(String.class,String.class);
					}catch (Exception e) {
					}
					if(con != null){
						Object object = con.newInstance(appName,ip);
						if(object instanceof AbstractDataAnalyse){
							return (AbstractDataAnalyse)object;
						}else{
							logger.error(_class.getName()+" not extends MsAnalyse ");
						}
					}
					if(con==null){
						logger.error(_class.getName()+" not has  Constructor(String.class,String.class)");
					}
				} catch (Exception e) {
					logger.error(_class.getName()+" not has  Constructor(String.class,String.class)",e);
				} 
			}
		} catch (ClassNotFoundException e) {
			logger.error("",e);
		}
		return null;
	}
	
	
	
	private static class ScriptProxy implements DataAnalyse{
		
		private ScriptAnalyse analyseObject = null;
		private String appName;
		
		private String scriptName;
		
		private String ip;
		
		public ScriptProxy(String scriptName,String appName,String ip,ScriptAnalyse analyse){
			this.analyseObject = analyse;
			this.appName = appName;
			this.ip = ip;
			this.scriptName = scriptName;
		}

		@Override
		public void analyseOneLine(String line) {
			analyseObject.analyseOneLine(line);
		}

		@Override
		public void submit() {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			
			{
				Map<String,Map<String, Double>> map = analyseObject.getAverageKeyValue();
				for(Map.Entry<String,Map<String, Double>> entry:map.entrySet()){
					String time = entry.getKey();
					try {
						long t = TimeUtil.converMinuteTime(sdf.parse(time));
						for(Map.Entry<String, Double> kvEntry:entry.getValue().entrySet()){
							//send data
								CollectDataUtilMulti.collect(appName, ip,t, 
										new String[]{"js_average",kvEntry.getKey()}, new KeyScope[]{KeyScope.NO,KeyScope.HOST}, new String[]{PropConstants.C_TIME}, 
										new Object[]{kvEntry.getValue().floatValue()},new ValueOperate[]{ValueOperate.AVERAGE});
						}
					} catch (Exception e) {
						logger.error("appName:"+appName+" scriptName:"+scriptName+" ip:"+ip+" time:"+time,e);
					}
				}
			}
			{
				Map<String,Map<String, Long>> map = analyseObject.getCountKeyValue();
				for(Map.Entry<String,Map<String, Long>> entry:map.entrySet()){
					String time = entry.getKey();
					try {
						long t = TimeUtil.converMinuteTime(sdf.parse(time));
						for(Map.Entry<String, Long> kvEntry:entry.getValue().entrySet()){
							CollectDataUtilMulti.collect(appName, ip, t, 
									new String[]{"js_count",kvEntry.getKey()}, new KeyScope[]{KeyScope.NO,KeyScope.HOST}, new String[]{PropConstants.E_TIMES}, 
									new Object[]{kvEntry.getValue().intValue()},new ValueOperate[]{ValueOperate.ADD});
						
						}
					} catch (Exception e) {
						logger.error("appName:"+appName+" scriptName:"+scriptName+" ip:"+ip+" time:"+time,e);
					}
				}
			}
			
			{
				Map<String,Map<String, String>> map = analyseObject.getTextKeyValue();
				for(Map.Entry<String,Map<String, String>> entry:map.entrySet()){
					String time = entry.getKey();
					try {
						long t = TimeUtil.converMinuteTime(sdf.parse(time));
						for(Map.Entry<String, String> kvEntry:entry.getValue().entrySet()){
							//send data
							
								CollectDataUtilMulti.collect(appName, ip, t, 
										new String[]{"js_text",kvEntry.getKey()}, new KeyScope[]{KeyScope.NO,KeyScope.HOST}, new String[]{"desc"}, 
										new Object[]{kvEntry.getValue()},new ValueOperate[]{ValueOperate.REPLACE});
							
						}
					} catch (Exception e) {
						logger.error("appName:"+appName+" scriptName:"+scriptName+" ip:"+ip+" time:"+time,e);
					}
				}
			}
			
		}

		@Override
		public void release() {
			analyseObject.getAverageKeyValue().clear();
			analyseObject.getCountKeyValue().clear();
			analyseObject.getTextKeyValue().clear();
		}

		@Override
		public void doAnalyse() {
			try{
				analyseObject.doAnalyse();
			}catch (Exception e) {
			}
		}
		
	}
	
	
	
	private static class DataAnalyseImpl implements DataAnalyse{
		
		private List<DataAnalyse> list=new ArrayList<DataAnalyse>();
		
		public void addAnalyse(DataAnalyse analyse){
			list.add(analyse);
		}
		@Override
		public void analyseOneLine(String line) {
			
			for(DataAnalyse a:list){
				try{
					a.analyseOneLine(line);
				}catch (Exception e) {
				}
			}
			
		}

		@Override
		public void submit() {
			doAnalyse();
			for(DataAnalyse a:list){
				try{
					a.submit();
				}catch (Exception e) {
				}
			}
		}

		@Override
		public void release() {
			
			for(DataAnalyse a:list){
				try{
					a.release();
				}catch (Exception e) {
				}
			}
		}
		@Override
		public void doAnalyse() {
			for(DataAnalyse a:list){
				try{
					a.doAnalyse();
				}catch (Exception e) {
				}
			}
			
		}
	}

}
