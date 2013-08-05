package com.taobao.monitor.alarm.network.lb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.taobao.util.CollectionUtil;

public class LoadBalanceConstants {
	private static final Logger logger = Logger.getLogger(LoadBalanceConstants.class);
	
	private static String LB_APP_G1_1_CM3 = "LB-APP-G1-1.cm3";
	private static String LB_APP_G2_1_CM3 = "LB-APP-G2-1.cm3";
	private static String LB_APP_G3_1_CM3 = "LB-APP-G3-1.cm3";
	private static String LB_APP_G4_1_CM3 = "LB-APP-G4-1.cm3";
	
	private static String CSR_1_CM3 = "CSR-1.cm3";
	private static String CSR_2_CM3 = "CSR-2.cm3";
	private static String CSW_1_CM3 = "CSW-1.cm3";
	private static String CSW_2_CM3 = "CSW-2.cm3";
	private static String DSW_APP_1_CM3 = "DSW-APP-1.cm3";
	private static String DSW_APP_2_CM3 = "DSW-APP-2.cm3";
	
	
	private static String LB_APP_G1_1_CM4 = "LB-APP-G1-1.cm4";
	private static String LB_APP_G2_1_CM4 = "LB-APP-G2-1.cm4";
	private static String LB_APP_G3_1_CM4 = "LB-APP-G3-1.cm4";
	private static String LB_APP_G6_1_CM4 = "LB-APP-G6-1.cm4";
	private static String LB_APP_G7_1_CM4 = "LB-APP-G7-1.cm4";
	private static String LB_APP_G9_1_CM4 = "LB-APP-G9-1.cm4";
	private static String LB_APP_G8_1_CM4 = "LB-APP-G8-1.cm4";
	private static String LB_APP_G11_1_CM4 = "LB-APP-G11-1.cm4";
	
	private static String CSR_1_CM4 = "CSR-1.cm4";
	private static String CSR_2_CM4 = "CSR-2.cm4";
	private static String CSW_1_CM4 = "CSW-1.cm4";
	private static String CSW_2_CM4 = "CSW-2.cm4";
	private static String DSW_APP_1_CM4 = "DSW-APP-1.cm4";
	private static String DSW_APP_2_CM4 = "DSW-APP-2.cm4";
	private static String DSW_APP_3_CM4 = "DSW-APP-1.cm4";
	private static String DSW_APP_4_CM4 = "DSW-APP-2.cm4";
	
	
	private static String LB_PREPUB_1_CM3 = "LB-PREPUB-1.cm3";
	
	private static String LB_APP_G1_1_CM5 = "LB-APP-G1-1.cm5";
	
	private static String LB_APP_G1_1_CM6 = "LB-APP-G1-1.cm6";
	private static String LB_APP_G2_1_CM6 = "LB-APP-G2-1.cm6";
	
	private static String CSR_1_CM6 = "CSR-1.cm6";
	private static String CSR_2_CM6 = "CSR-2.cm6";
	private static String CSW_1_CM6 = "CSW-1.cm6";
	private static String CSW_2_CM6 = "CSW-2.cm6";
	private static String DSW_APPDB_1_CM6 = "DSW-APP-1.cm6";
	private static String DSW_APPDB_2_CM6 = "DSW-APP-2.cm6";
	
	private static String URL = "http://110.75.12.100/vipmanage/mapping/getlbinfo.php?";
	
	public static List<String> ALL_IP_SEGMENT = Arrays.asList("*");
	
	private static List<String> networkDeviceList = new ArrayList<String>();
	/**
	 * LB设备与IP段的绑定关系，启动的时候去请求url获取
	 */
	public static Map<String,List<String>> lbIpSegmentList = new HashMap<String,List<String>>();
	
	static{
		networkDeviceList.add(LB_APP_G1_1_CM3);
		networkDeviceList.add(LB_APP_G2_1_CM3);
		networkDeviceList.add(LB_APP_G3_1_CM3);
		networkDeviceList.add(LB_APP_G4_1_CM3);
		
		networkDeviceList.add(LB_APP_G1_1_CM4);
		networkDeviceList.add(LB_APP_G2_1_CM4);
		networkDeviceList.add(LB_APP_G3_1_CM4);
		networkDeviceList.add(LB_APP_G6_1_CM4);
		networkDeviceList.add(LB_APP_G7_1_CM4);
		networkDeviceList.add(LB_APP_G9_1_CM4);
		networkDeviceList.add(LB_APP_G8_1_CM4);
		networkDeviceList.add(LB_APP_G11_1_CM4);
		
		networkDeviceList.add(LB_PREPUB_1_CM3);
		
		networkDeviceList.add(LB_APP_G1_1_CM5);
		
		networkDeviceList.add(LB_APP_G1_1_CM6);
		networkDeviceList.add(LB_APP_G2_1_CM6);
		
		for(String lbName:networkDeviceList){
			List<String> list = getVlanByLBName(lbName);
			if(CollectionUtil.isNotEmpty(list)){
				lbIpSegmentList.put(lbName, list);
			}
		}
		
		lbIpSegmentList.put(CSR_1_CM4, ALL_IP_SEGMENT );
		lbIpSegmentList.put(CSR_2_CM4, ALL_IP_SEGMENT );
		lbIpSegmentList.put(CSW_1_CM4, ALL_IP_SEGMENT );
		lbIpSegmentList.put(CSW_2_CM4, ALL_IP_SEGMENT );
		lbIpSegmentList.put(DSW_APP_1_CM4, ALL_IP_SEGMENT );
		lbIpSegmentList.put(DSW_APP_2_CM4, ALL_IP_SEGMENT );
		lbIpSegmentList.put(DSW_APP_3_CM4, ALL_IP_SEGMENT );
		lbIpSegmentList.put(DSW_APP_4_CM4, ALL_IP_SEGMENT );
		
		lbIpSegmentList.put(CSR_1_CM3, ALL_IP_SEGMENT );
		lbIpSegmentList.put(CSR_2_CM3, ALL_IP_SEGMENT );
		lbIpSegmentList.put(CSW_1_CM3, ALL_IP_SEGMENT );
		lbIpSegmentList.put(CSW_2_CM3, ALL_IP_SEGMENT );
		lbIpSegmentList.put(DSW_APP_1_CM3, ALL_IP_SEGMENT );
		lbIpSegmentList.put(DSW_APP_2_CM3, ALL_IP_SEGMENT );
		
		lbIpSegmentList.put(CSR_1_CM6, ALL_IP_SEGMENT );
		lbIpSegmentList.put(CSR_2_CM6, ALL_IP_SEGMENT );
		lbIpSegmentList.put(CSW_1_CM6, ALL_IP_SEGMENT );
		lbIpSegmentList.put(CSW_2_CM6, ALL_IP_SEGMENT );
		lbIpSegmentList.put(DSW_APPDB_1_CM6, ALL_IP_SEGMENT );
		lbIpSegmentList.put(DSW_APPDB_2_CM6, ALL_IP_SEGMENT );
		
	}
	
	public  static List<String> getVlanByLBName(String lbName){
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(URL + "device=" + lbName);
		try {
			// 使用系统提供的默认的恢复策略
			getMethod.addRequestHeader("Content-Type","application/x-www-form-urlencoded");
			//设置超时时间
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		    httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);
		    httpClient.getParams().setContentCharset( "GBK"); 
			
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode == HttpStatus.SC_OK) {
				String response = getMethod.getResponseBodyAsString();// 读取内容
				logger.warn("getVlanByLBName response=" + response + ",lbName=" +lbName);
				int beginIndex = response.indexOf("172.");
				int endIndex = response.indexOf("该LB包含的Vip");
				if(beginIndex== -1 || endIndex== -1){
					return null;
				}
				String allSegmentString = 	response.substring(beginIndex,endIndex);
				String segment[] = allSegmentString.split("\r\n");
				if(segment.length == 0){
					return null;
				}
				List<String> list = new ArrayList<String>();
				for(String s:segment){
					//去掉结尾的.0
					list.add(s.substring(0,s.length()-2));
				
				}
				return list;
			} else {
				logger.warn("getVlanByLBName statusCode=" + statusCode + ",lbName=" +lbName);
			}
		} catch (Exception e) {
			logger.warn("getVlanByLBName exception,lbName=" + lbName,e);
		}finally{
			getMethod.releaseConnection();
		}
		return null;
	}
	
}
