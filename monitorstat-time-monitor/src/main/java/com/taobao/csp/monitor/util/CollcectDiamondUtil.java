package com.taobao.csp.monitor.util;

import java.util.concurrent.Executor;

import org.apache.log4j.Logger;

import com.taobao.diamond.manager.ManagerListener;
import com.taobao.diamond.manager.impl.DefaultDiamondManager;

public class CollcectDiamondUtil {

	private static final Logger logger = Logger.getLogger(CollcectDiamondUtil.class);
	private static volatile boolean  isInit=false;
	
	/**
	 * 控制哪些应用由storm采集，哪些由collect采集
	 */
	public static synchronized void diamondCollectListener(){
		if(isInit){
			return;
		}
		isInit=true;
		try{
			new DefaultDiamondManager("csp", 
	        		"com.taobao.csp.collect", new ManagerListener() {

	            public void receiveConfigInfo(String configInfo) {
	            	try{
	            		logger.warn("receive diamond:"+configInfo);
	            		configInfo=configInfo.replace("\r", "");
	            		String[] values=configInfo.split("\n");
	            		
	            		for(String v:values){
	            			String[] pros=v.split("=");
	            			
	            			if(pros[0].equals("IS_STORM")){
	            				CollectFlag.IS_STORM=Boolean.valueOf(pros[1]);
		            		}
	            			if(pros[0].equals("IS_ALL_STORM")){
	            				CollectFlag.IS_ALL_STORM=Boolean.valueOf(pros[1]);
		            		}
	            			if(pros[0].equals("apps")){
		            			
		            			String[] apps=pros[1].split(",");
		            			CollectFlag.storms.clear();
		            			for(String app:apps){
		            				CollectFlag.storms.put(app, 0);
		            			}

		            			for(String app:CollectFlag.storms.keySet()){
		            				logger.warn("===collect app:"+app);
		            			}
		            		}
	            		}
	            		
	            	}catch(Exception e){
	            		logger.warn("diamondMainListener exception",e);
	            	}
	            }

	            public Executor getExecutor() {
	                 return null;
	            }
	      });
		}catch(Exception e){
			logger.warn("diamondMainListener exception",e);
		}
		logger.warn("===============diamondmain init success=========");
	}
	
	public static void main(String[] args){
		String aa="IS_ALL_STORM=fal\n"+
				"IS_STORM=false\n"+
				"apps=lottery";
		String[] values=aa.split("\n");
		
		for(String v:values){
			String[] pros=v.split("=");
			
			if(pros[0].equals("IS_STORM")){
				CollectFlag.IS_STORM=Boolean.valueOf(pros[1]);
    		}
			if(pros[0].equals("IS_ALL_STORM")){
				CollectFlag.IS_ALL_STORM=Boolean.valueOf(pros[1]);
    		}
			if(pros[0].equals("apps")){
    			
    			String[] apps=pros[1].split(",");
    			CollectFlag.storms.clear();
    			for(String app:apps){
    				CollectFlag.storms.put(app, 0);
    			}
    		}
		}
	}
	
}
