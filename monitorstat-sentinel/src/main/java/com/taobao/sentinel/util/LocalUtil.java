package com.taobao.sentinel.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.taobao.arkclient.ArkDomain;
import com.taobao.sentinel.bo.CommonBo;
import com.taobao.sentinel.po.DataUrlPo;

public class LocalUtil {
	
	public static Object lockObject = new Object();
	
	public static String getCurrentUser(HttpServletRequest request) {
		
		return ArkDomain.getArkUserEmail(request);
		
	}
	
	public static String generateId() {
		synchronized (lockObject) {
			return UUID.randomUUID().toString();
		}
	}
	
	public static String generateVersion() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(Calendar.getInstance().getTime());
	}
	
	public static String generateDataUrl(String appName) {
		return "http://autofillip:7001/" + appName + "/ss/data.stn";
	}
	
	public static String getDataUrl(String appName, String ip) {
		CommonBo commonBo = (CommonBo)SpringUtil.getBean("commonBo");
		DataUrlPo po = commonBo.findDataUrl(appName);
		return po.getDataUrl().replace("autofillip", ip);
	}
	
	public static String generateRefId(String appName, String refApp) {
		return appName + "$$" + refApp;
	}
	
	public static String getRefAppFromRefId(String refId) {
		return refId.split("\\$\\$")[1];
	}
	
	public static String generateRefIdIndex(String appName) {
		return appName + "$$" + "%";
	}
	
	// for testing
	public static void main(String [] args) {
		String aa = "aaaa$$bbbb";
		System.out.println(aa.split("\\$\\$").length);
	}

}
