
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl.analyse.exception;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;
import com.taobao.csp.monitor.impl.analyse.notify.NotifyProviderLogJob;

/**
 * @author xiaodu
 *
 * ÉÏÎç10:39:01
 */
public class ExceptionLogJob extends AbstractDataAnalyse{
	
	private static final Logger logger = Logger.getLogger(ExceptionLogJob.class);
	
	public ExceptionLogJob( String appName,String ip,String feature) {
		super(appName,ip, feature);
	}
	
	private Map<String,ExceptionPo> collectValue = new HashMap<String,ExceptionPo>();
	
	private Pattern pattern = Pattern.compile("\\.(\\w*Exception)");
	
	private Pattern timepattern = Pattern.compile("(\\d\\d\\d\\d-\\d\\d-\\d\\d)");
	
	private String currentException = null;
	
	private String getExceptionName(String line){
		
		Matcher matcher = pattern.matcher(line);
				
		if(matcher.find()){
			return matcher.group(1);
		}		
		return null;		
	}
	
	
	private class ExceptionPo{
		private int num;
		private StringBuffer stackMessage = new StringBuffer();
		
	}


	
	public void analyseOneLine(String line) {
		String exName = getExceptionName(line);
		if(exName != null&&currentException==null){
			ExceptionPo po = collectValue.get(exName);
			if(po == null){
				po = new ExceptionPo();
				collectValue.put(exName, po);
				po.num++;
				po.stackMessage.append(line).append("\n");
			}else{
				po.stackMessage.setLength(0);
				po.stackMessage.append(line).append("\n");
				po.num++;
			}
			
			currentException = exName;
		}else{
			if(currentException != null){
				if(!timepattern.matcher(line).find()){
						ExceptionPo e = collectValue.get(currentException);
						if(e != null){
							e.stackMessage.append(line).append("\n");
						}
				}else{
//					ExceptionPo e = collectValue.get(currentException);
//					System.out.println(e.stackMessage.toString());
//					
					currentException = null;
				}
			}
		}
	}


	
	public void doAnalyse() {
	}


	
	

	@Override
	public void submit() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		for(Map.Entry<String,ExceptionPo> ventry:collectValue.entrySet()){
			String key = ventry.getKey();
			long value = ventry.getValue().num;
			try {
				CollectDataUtilMulti.collect(getAppName(), getIp(), cal.getTimeInMillis(), 
						new String[]{"Exception",key}, new KeyScope[]{KeyScope.APP,KeyScope.APP}, new String[]{"E-times","desc"}, 
						new Object[]{value,ventry.getValue().stackMessage.toString()},new ValueOperate[]{ValueOperate.ADD,ValueOperate.REPLACE});
				
				CollectDataUtilMulti.collect(getAppName(), getIp(), cal.getTimeInMillis(), 
						new String[]{"Exception",key}, new KeyScope[]{KeyScope.HOST,KeyScope.HOST}, new String[]{"E-times"}, 
						new Object[]{value},new ValueOperate[]{ValueOperate.ADD});
				
			} catch (Exception e) {
				logger.error("", e);
			}
		}	
		
	}
	
	
	

	@Override
	public void release() {
		collectValue.clear();
	}
	
	
	
public static void main(String[] args){
		
	ExceptionLogJob job = new ExceptionLogJob("detail","172.17.134.4","");
		
			String line = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("C:\\Users\\xiaodu\\Downloads\\tmp.log"),"gbk"));
			int i = 0;
			while((line=reader.readLine())!=null){
			//	System.out.println(line);
				job.analyseOneLine(line);
				i++;
				if(i>10000){
//					job.submit();
//					job.release();
//					Thread.sleep(60000);
//					i=0;
				}else{
				
				}
			}
			job.submit();
		} catch (Exception e) {
			e.printStackTrace();
		}  
		
	}
	

}