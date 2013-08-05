package com.taobao.csp.dataserver.util;

import java.util.concurrent.Executor;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.memcache.MemcacheStandard;
import com.taobao.diamond.manager.ManagerListener;
import com.taobao.diamond.manager.impl.DefaultDiamondManager;

public class DiamondUtil {
	
	private static final Logger logger = Logger.getLogger(DiamondUtil.class);

	private static volatile boolean  isInit=false;
	
	
	public static synchronized void diamondMainListener(){
		if(isInit){
			return;
		}
		isInit=true;
		try{
			new DefaultDiamondManager("csp", 
	        		"com.taobao.csp.alarm", new ManagerListener() {

	            public void receiveConfigInfo(String configInfo) {
	            	try{
	            		logger.warn("receive diamond:"+configInfo);
	            		configInfo=configInfo.replace("\r", "");
	            		String[] values=configInfo.split("\n");
	            		
	            		for(String v:values){
	            			String[] pros=v.split("=");
	            			
	            			if(pros[0].equals("IN_BDB")){
		            			Constants.IN_BDB=Boolean.valueOf(pros[1]);
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
	
}
