
package com.taobao.monitor.stat.accept;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.stat.config.AppAnalyseInfo;

/**
 * 
 * @author xiaodu
 * @version 2010-5-19 ÏÂÎç06:27:42
 */
public class ScpAcceptPool {
	
	ExecutorService threadPool = Executors.newFixedThreadPool(2);
	
//	private AppAnalyseInfo appAnalyseInfo ;
	private List<String> parseLogPath = new ArrayList<String>();
	private List<HostPo> listHost;
	private String localPath;
	private String opsName;
	
	private  Calendar cal = null;
	
	public ScpAcceptPool(AppAnalyseInfo appAnalyseInfo,List<HostPo> listHost,String localPath,Calendar calendar){
		this.listHost = listHost;
		this.localPath = localPath;
		if(calendar == null){
			this.cal = Calendar.getInstance();
			this.cal.add(Calendar.DAY_OF_MONTH, -1);
		}else{
			this.cal = calendar;
		}
		
		for(String path:appAnalyseInfo.getRemoteLogPath()){
			parseLogPath.add(parsePathTime(path));
		}
		opsName = appAnalyseInfo.getOpsfreeName();
		
		
	}
	
	
	public ScpAcceptPool(String  removePath,String appName,List<HostPo> listHost,String localPath,Calendar calendar){
		if(calendar == null){
			this.cal = Calendar.getInstance();
			this.cal.add(Calendar.DAY_OF_MONTH, -1);
		}else{
			this.cal = calendar;
		}
		this.parseLogPath.add(parsePathTime(removePath));
		this.listHost = listHost;
		this.localPath = localPath;
	}
	
	public void doScp(){
		
		int hostSize = listHost.size();
		CountDownLatch latch = new CountDownLatch(hostSize);
		
		for(HostPo info:listHost){
			ScpAccept scp = new ScpAccept(info.getUserName(),info.getUserPassword(),info.getHostIp(),parseLogPath,localPath+"/"+opsName,latch);
			threadPool.execute(scp);
		}
		try {
			latch.await();
		} catch (InterruptedException e) {
		}
		
		
	}
	
	
	private String parsePathTime(String path){
		
		 Pattern pattern = Pattern.compile("\\$\\{([\\w-]+)\\}");
		 
		 Matcher m = pattern.matcher(path);
		 
		 while(m.find()){
			 String format = m.group(1);
			 
			 SimpleDateFormat sdf = new SimpleDateFormat(format);
			 
			 String date = sdf.format(cal.getTime());			 
			 
			 path = path.replaceAll("\\$\\{"+format+"\\}", date);
			 
		 }
		 
		 return path;
	 }
	

}
