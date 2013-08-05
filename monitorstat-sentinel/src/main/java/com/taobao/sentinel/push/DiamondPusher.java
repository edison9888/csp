package com.taobao.sentinel.push;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.diamond.domain.ContextResult;
import com.taobao.diamond.domain.DiamondConf;
import com.taobao.diamond.domain.DiamondSDKConf;
import com.taobao.diamond.sdkapi.impl.DiamondSDKManagerImpl;
import com.taobao.sentinel.client.SentinelConfig;
import com.taobao.sentinel.constant.Constants;

public class DiamondPusher implements IPusher{
	
	public static Logger logger = Logger.getLogger(DiamondPusher.class);

	@Override
	public boolean pushConfig(String appName, String configInfo) {

		String dataId = Constants.CONFIG_PREFIX + appName;
		
		return pushData(dataId, configInfo);
	}
	
	private boolean pushData(String dataId, String conttent) {
		
		// daily
		List<DiamondConf> diamondConfsD = new ArrayList<DiamondConf>();
		DiamondConf diamondConf_1D = new DiamondConf("10.232.102.182", "8080",
				"admin", "admin");
		DiamondConf diamondConf_2D = new DiamondConf("10.232.102.183", "8080",
				"admin", "admin");
		DiamondConf diamondConf_3D = new DiamondConf("10.232.10.23", "8080",
				"admin", "admin");

		diamondConfsD.add(diamondConf_1D);
		diamondConfsD.add(diamondConf_2D);
		diamondConfsD.add(diamondConf_3D);


		// on line
		List<DiamondConf> diamondConfsO = new ArrayList<DiamondConf>();
		DiamondConf diamondConf_1O = new DiamondConf("172.23.206.72", "8080",
				"admin", "admin");
		DiamondConf diamondConf_2O = new DiamondConf("172.23.172.167", "8080",
				"admin", "admin");
		DiamondConf diamondConf_3O = new DiamondConf("172.24.16.165", "8080",
				"admin", "admin");
		
		
		diamondConfsO.add(diamondConf_1O);
		diamondConfsO.add(diamondConf_2O);
		diamondConfsO.add(diamondConf_3O);
		
		// in advance
		List<DiamondConf> diamondConfsA = new ArrayList<DiamondConf>();
		DiamondConf diamondConf_1A = new DiamondConf("172.23.65.52", "8080",
				"admin", "admin");
		
		diamondConfsA.add(diamondConf_1A);
		
		DiamondSDKConf damondSDKConfD = new DiamondSDKConf(diamondConfsD);
		damondSDKConfD.setServerId("daily");
		
		DiamondSDKConf damondSDKConfO = new DiamondSDKConf(diamondConfsO);
		damondSDKConfO.setServerId("online");
		
		DiamondSDKConf damondSDKConfA = new DiamondSDKConf(diamondConfsA);
		damondSDKConfA.setServerId("advance");
		
		
		Map<String, DiamondSDKConf> diamondSDKConfMaps = new HashMap<String, DiamondSDKConf>(
				3);
		diamondSDKConfMaps.put("daily", damondSDKConfD);
		diamondSDKConfMaps.put("online", damondSDKConfO);
		diamondSDKConfMaps.put("advance", damondSDKConfA);

		DiamondSDKManagerImpl diamondSDKManagerImpl = new DiamondSDKManagerImpl(
				2000, 2000);
		diamondSDKManagerImpl.setDiamondSDKConfMaps(diamondSDKConfMaps);
		try {
			diamondSDKManagerImpl.init();
		} catch (Exception e) {
			logger.error("error", e);
		}
		
		// choose daily for test, and online for online use
		ContextResult response = 
		diamondSDKManagerImpl.pulish(dataId, Constants.GROUP, conttent, SentinelConfig.DIAMOND_ID);
		
		logger.info("diamond : " + SentinelConfig.DIAMOND_ID);
		logger.info("push data: " + dataId + " status code is: " + response.getStatusCode());
		System.out.println(response.getStatusCode());
		
//		ContextResult responses =
//		diamondSDKManagerImpl.queryByDataIdAndGroupName("sentinel_itemcenter", "sentinel", "daily");
//		
//		System.out.println(responses);
		
		if (response.getStatusCode() / 100 == 2) {
			return true;
		}
		
		return false;
	}

}
