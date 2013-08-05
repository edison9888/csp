
/**
 * monitorstat-common
 */
package com.taobao.monitor.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author xiaodu
 * 
 * http://hsfops.com.taobao.net:9999/hsfops-online/api/services 这个地址存放服务编码映射关系
 *
 * 上午10:28:04
 */
public class HsfServiceCodeMap {
	
	private static Logger logger = Logger.getLogger(HsfServiceCodeMap.class);
	
	/**
	 * map<code,servicename@method> 这个是记录 完整的8位编码对应着服务方法名称
	 */
	private  Map<String,String> serivceMethodCodeMap = new ConcurrentHashMap<String, String>();
	
	/**
	 * map<servicename,code> 这个记录 服务名称与它对应编码
	 */
	private Map<String,String> serivceCodeMap = new ConcurrentHashMap<String, String>();
	
	
	private static HsfServiceCodeMap instance = new HsfServiceCodeMap();
	
	public long lastUpdateTime = 0;//去掉初始时间，否则一次都进不来
	
	
	private HsfServiceCodeMap(){
		init();
	}
	
	
	public static HsfServiceCodeMap get(){
		return instance;
	}
	
	
	
	/**
	 * 根据服务编码和方法编码 获取对应的名称
	 *@author xiaodu
	 * @param serviceCode
	 * @param methodCode
	 * @return String[0] 服务名称String[1] 方法名称
	 *TODO
	 */
	private  String[] getServiceMethodName(String serviceCode,String methodCode){
		String code = "";
		if(serviceCode.length() ==8){
			code = serviceCode.substring(0, 5)+methodCode;
		}else{
			code = serviceCode+methodCode;
		}
		
		String smn = serivceMethodCodeMap.get(code);
		
		
		if(smn == null){
			init();
			smn = serivceMethodCodeMap.get(code);
		}
		
		
		if(smn != null){
			String[] tmp = StringUtils.split(smn, "@");
			if(tmp.length ==2){
				return tmp;
			}
		}
		return new String[]{serviceCode,methodCode};
		
	}
	
	/**
	 * 根据服务名称 获取到方法的名称
	 *@author xiaodu
	 * @param serviceName
	 * @param methodCode
	 * @return
	 *TODO
	 */
	private  String getMethodNameByServiceName(String serviceName,String methodCode){
		String servicecode = serivceCodeMap.get(serviceName);
		
		if(servicecode == null){
			init();
			servicecode = serivceCodeMap.get(serviceName);
		}
		
		if(servicecode != null&&servicecode.length() ==8){
			String[] tmp = getServiceMethodName(servicecode,methodCode);
			return tmp[1];
		}
		return methodCode;
	}
	
	
	
	/**
	 * 
	 *@author xiaodu
	 * @param service
	 * @param method
	 * @return
	 *TODO
	 */
	public  String[] translateServiceMethod(String service,String method){
		boolean sNum = StringUtils.isNumeric(service);
		boolean mNum = StringUtils.isNumeric(method);
		
		if(sNum && mNum){
			return getServiceMethodName(service,method);
		} else if(!sNum&&mNum){
			
			String[] tmp = StringUtils.split(service, ":");
			String s = service;
			if(tmp.length ==2){
				s = tmp[0];
			}
			String m = getMethodNameByServiceName(s,method);
			return new String[]{service,m};			
		}
		return new String[]{service,method};
	}
	
	
	
	private synchronized void init(){
		
		
		if(System.currentTimeMillis() - lastUpdateTime <10*60*1000){
			return ;
		}
		
		try {
			logger.info("请求的URL->http://172.23.179.191:18080/hsfops-online/api/services");
			//URL url = new URL("http://hsfops.com.taobao.net:9999/hsfops-online/api/services");
			URL url = new URL("http://172.23.179.191:18080/hsfops-online/api/services");
			URLConnection urlCon = url.openConnection();
			urlCon.setDoInput(true);
			urlCon.setConnectTimeout(20000);
			urlCon.connect();
			BufferedInputStream input = new BufferedInputStream(urlCon.getInputStream());
			BufferedReader readerf = new BufferedReader(new InputStreamReader(input));

			String str = null;
			while ((str = readerf.readLine()) != null) {
				if(str.startsWith("com")){
					String[] tmp = StringUtils.split(str, "=");
					if(tmp.length == 2){
						String name = tmp[0];
						String code = tmp[1];
						if(code.substring(5, 8).equals("000")){
							serivceCodeMap.put(name, code);
						}else{
							serivceMethodCodeMap.put(code, name);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		lastUpdateTime = System.currentTimeMillis();
	}
	
	
	public static void main(String[] args){
		String[] f = HsfServiceCodeMap.get().translateServiceMethod("12578", "001");
		System.out.println(f[0]+"_"+f[1]);
		
		
	}
	

}
