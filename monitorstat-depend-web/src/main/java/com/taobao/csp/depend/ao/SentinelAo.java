package com.taobao.csp.depend.ao;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.csp.depend.dao.SentinelDao;
import com.taobao.monitor.common.util.CspKeyTransfer;

public class SentinelAo {
	private static final Logger logger =  Logger.getLogger(SentinelAo.class);
	
	private SentinelDao dao = new SentinelDao();
	private static SentinelAo ao = new SentinelAo();
	public static SentinelAo get(){
		return ao;
	}
	private SentinelAo() {}
	
	public Set<String> getKeyListFromSentinel(String appName) {
		Set<String> set = dao.getKeyListFromSentinel(appName);
		return changeSentinueKeyToEagleeyeKey(set);
	}
	
	public Set<String> getKeyListFromSentinel() {
		Set<String> set = dao.getKeyListFromSentinel();
		return changeSentinueKeyToEagleeyeKey(set);
	}
	
	private Set<String> changeSentinueKeyToEagleeyeKey(Set<String> set) {
		Set<String> eagleEyeset = new HashSet<String>();;
		for(String hsfKey : set) {
			try {
				eagleEyeset.add(CspKeyTransfer.changeSentinueHsfKeyToEagleeyeKey(hsfKey));
			} catch (Exception e) {
				logger.error("",e);
			}
		}
		return eagleEyeset;
	}
	
	public static void main(String[] args) {
		System.out.println(SentinelAo.get().getKeyListFromSentinel());
	}
}
