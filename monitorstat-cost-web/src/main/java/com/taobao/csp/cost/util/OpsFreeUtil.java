package com.taobao.csp.cost.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.po.HostPo;

public class OpsFreeUtil {
	
	private static Logger logger = Logger.getLogger(OpsFreeUtil.class);
	
	public static String getHostNameFromIp(String ip) {
		String hostName="";
		
		try {
			String [] ipStr = ip.split("\\.");//以"."拆分字符串
			byte [] ipBuf = new byte[4];
			for(int i = 0 ; i < 4; i++){
				ipBuf[i] = (byte)(Integer.parseInt(ipStr[i])&0xFF);//调整整数大小。
			} 
			
			InetAddress a= InetAddress.getByAddress(ipBuf);
			hostName=a.getHostName();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.warn("get ip: "+ip+" hostname: "+hostName);
		return  hostName;
	}
	
	/**
	 * 获取机器的硬件信息
	 * 字段说明：http://opsdbtest.ops.aliyun-inc.com:8888/page/api/free/setting/describe.htm
	 * 
	 * hw_cpu:CPU核数
	 * hw_harddisk:磁盘大小
	 * hw_mem:内存大小
	 * 
	 * http://a.alibaba-inc.com/page/api/free/opsfreeInterface/search.htm?q=nodename==my52.cm4&_username=droid/csp&select=*
	 * @param hostName
	 * @return
	 */
	public static int[] getHardInfoFromArmory(String hostName) {
		String url = "http://a.alibaba-inc.com/page/api/free/opsfreeInterface/search.htm?q=nodename=="
				+ hostName+"&_username=droid/csp&select=*";
		String result = getUrlResult(url);
		
		if(result==null){
			return null;
		}
		try{
			JSONObject json = JSONObject.fromObject(result);
			
			JSONArray ja=json.getJSONArray("result");
			JSONObject hardInfo=null;
			if(ja!=null && ja.size()>0){
				hardInfo=ja.getJSONObject(0);
				
				return new int[]{hardInfo.getInt("hw_mem"),
						hardInfo.getInt("hw_cpu"),hardInfo.getInt("hw_harddisk")};
			}
		}catch (Exception e) {
			logger.error("",e);
		}catch(OutOfMemoryError e1){
			logger.error("",e1);
		}
		return null;
	}
	
	
	public static Map<String, HostPo> findHostListInOpsfreeByOpsName(String opsName) {
		String url = "http://opsfree2.corp.taobao.com:9999/api/v2.1/products/dumptree?_username=droid/csp&notree=1&leafname="
				+ opsName;
		String result = getUrlResult(url);
		return analyseK2Json(result);
	}
	
	private static String getUrlResult(String path) {
		try {
			URL url = new URL(path);
			URLConnection urlCon = url.openConnection();
			urlCon.setDoInput(true);
			urlCon.setConnectTimeout(200000);
			urlCon.connect();
			BufferedInputStream input = new BufferedInputStream(urlCon.getInputStream());
			BufferedReader readerf = new BufferedReader(new InputStreamReader(input, "utf-8"));

			String str = null;
			StringBuffer sb = new StringBuffer();
			while ((str = readerf.readLine()) != null) {
				sb.append(str);
				
				if(sb.length() >2000000){
					throw new Exception("too long:"+path);
				}
				
				
			}
			
			return sb.toString();
		} catch (Exception e) {
			logger.error("http get ops free info error:" + path, e);
		}
		return null;
	}
//	
//	/**
//	 * 根据ip获得hostname
//	 * @param ip
//	 * @return
//	 */
//	public static String getHostNameFromIp(String ip){
//		  
//		 String[] tms=ip.split("\\.");
//		 
//		 int ip1=Integer.parseInt(tms[0]);
//		 int ip2=Integer.parseInt(tms[1]);
//		 int ip3=Integer.parseInt(tms[2]);
//		 int ip4=Integer.parseInt(tms[3]);
//		 byte[] ipb=new byte[]{(byte) ip1,(byte) ip2,(byte) ip3,(byte) ip4};
//		 InetAddress address = null;
//		 try {
//			 //address=InetAddress.getByAddress(ipb);
//			 InetAddress inetAddress = InetAddress.getByName(ip);  
//			 String ipAddress = inetAddress.getHostName();  
//			 System.out.println(ipAddress);  
//			 
//			 return address.getHostName();
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		 return null;
//	}
//	
//	public static void main(String[] args){
//		System.out.println(getHostNameFromIp("10.232.101.64"));
//	}
	
	/***
	 * from csp
	 * @param result
	 * @return
	 */
	private static Map<String, HostPo> analyseK2Json(String result) {

		Map<String, HostPo> map = new HashMap<String, HostPo>();
		if(result==null){
			return map;
		}
		
		try{
			JSONArray object = JSONArray.fromObject(result);
			if (object.size() > 0) {
				Object obj = object.get(0);
				if (obj instanceof JSONObject) {
					JSONObject json = (JSONObject) obj;
					Set set = json.keySet();
					for (Object j : set) {
						String key = j.toString();
	
						String[] keySplit = key.split("\\.");
						String moduleName = keySplit[keySplit.length - 1];
	
						Object detailObj = json.get(j);
						if (detailObj instanceof JSONArray) {
							JSONArray array = (JSONArray) detailObj;
							for (int i = 0; i < array.size(); i++) {
								Object o = array.get(i);
								if (o instanceof JSONObject) {
									JSONObject p = (JSONObject) o;
									JSONObject nodegroup_info = (JSONObject) p.get("nodegroup_info");
									JSONObject detail = (JSONObject) nodegroup_info.get("detail");
									if (detail == null) continue;
									//String nodegroup_name = detail.getString("nodegroup_name");
									JSONArray child = (JSONArray) p.get("child");
									if (child == null) continue;
	
									for (int c = 0; c < child.size(); c++) {
										try {
											JSONObject childNode = (JSONObject) child.get(c);
											String dns_ip = childNode.getString("dns_ip");
											
											try{
												String nodename = childNode.optString("nodename");
												String site  = childNode.optString("site");
												
												String device_type = childNode.optString("device_type");
												String state = childNode.optString("state");
												if (!state.equals("working_online")) continue;
												
												String vmparent = childNode.optString("vmparent");
												String description = childNode.optString("description");
												String model = childNode.optString("model");
												String hdrs_chassis = childNode.optString("hdrs_chassis");
												String rack = childNode.optString("rack");
												String nodegroup = childNode.optString("nodegroup");
												
												HostPo host = new HostPo();
												host.setHostSite(site);
												host.setHostType(model);
												host.setOpsName(moduleName);
												host.setHostIp(dns_ip);
												host.setHostName(nodename);
												host.setDescription(description);
												host.setHdrs_chassis(hdrs_chassis);
												host.setNodeGroup(nodegroup);
												host.setRack(rack);
												host.setVmparent(vmparent);
												host.setState(state);
												map.put(dns_ip, host);
											}catch (Exception e) {
												logger.error("unknown "+dns_ip+" site", e);
											}
										} catch (Exception e) {
											logger.error("", e);
										}
									}
								}
							}
						}
					}
				}
			}
		}catch (Exception e) {
			logger.error("",e);
		}catch(OutOfMemoryError e1){
			logger.error("",e1);
		}
		return map;
	} 
	
//	public static void main(String [] args) {
////		String [] a = new String [] { "topats", "tvcrm", "thboss", "threport", "mapserver"};
//		String [] a = new String [] { "tmallrefund", "malldetailskip" };
//		
//		for (String app : a) {
//			Map<String, HostPo> hosts = findHostListInOpsfreeByOpsName(app);
//			Iterator<HostPo> ite = hosts.values().iterator();
//			
//			while(ite.hasNext()) {
//				HostPo po = ite.next();
//				System.out.println(po.getHostName());
//			}
//		}
//	}
}
