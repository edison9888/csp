package com.taobao.monitor.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;

/**
 * 
 * @author xiaodu
 * @version 2011-3-10 下午05:15:32
 */
public class OpsFreeHostCache {

	private Logger log = Logger.getLogger(OpsFreeHostCache.class);

	private Map<String, HostPo> hostIPMap = new ConcurrentHashMap<String, HostPo>();
	
	private Map<String, HostPo> hostNameMap = new ConcurrentHashMap<String, HostPo>();
	
	private Map<String, List<HostPo>> opsHostMap = new ConcurrentHashMap<String,  List<HostPo>>();

	private static OpsFreeHostCache cache = new OpsFreeHostCache();

	private OpsFreeHostCache() {

	}

	public static OpsFreeHostCache get() {
		return cache;
	}

	/**
	 * 重置所有缓存
	 */
	public void resetAll() {
		List<AppInfoPo> list = AppInfoAo.get().findAllEffectiveAppInfo();
		for (AppInfoPo po : list) {
			reset(po.getOpsName());
		}
	}

	/**
	 * 重置某个应用缓存
	 * 
	 * @param opsName
	 */
	public void reset(String opsName) {
		List<HostPo> list = getHostListNoCache(opsName);
		for (HostPo po : list) {
			hostIPMap.put(po.getHostIp(), po);
		}
	}
	
	
	public void clear(){
		hostIPMap.clear();
	}

	public HostPo getHostNoCache(String ip) {
		String result = getUrlResult("http://opsfree2.corp.taobao.com:9999/api/v2.1/node/search?_username=droid/csp&e=1&n=1&q=dns_ip=="
				+ ip + "");
		if(result !=null){
			JSONArray object = JSONArray.fromObject(result);
			if (object.size() > 0) {
				Object obj = object.get(0);
				if (obj instanceof JSONObject) {
					JSONObject json = (JSONObject) obj;
					//String site = json.getString("site");
					//String state = json.getString("state");
					//String device_type = json.getString("device_type");
					String nodegroup = json.getString("nodegroup");
					Map<String, HostPo> map = useGroupNameFindThisGroupHost(nodegroup);
					return map.get(ip);
				}
	
			}
		}
		return null;
	}
	
	
	public HostPo getHostCache(String ip) {
		
		HostPo po = hostIPMap.get(ip);
		if(po == null){
			return getHostNoCache(ip);
		}
		return hostIPMap.get(ip);
	}
	
	
	public HostPo getHostnameCache(String hostname) {
		
		HostPo po = hostNameMap.get(hostname);
		if(po == null){
			return getHostInfoByNodeName(hostname);
		}else{
			return po;
		}
		
	}
	
	
	

	public Map<String, HostPo> useGroupNameFindThisGroupHost(String nodegroup) {
		if(nodegroup != null){
			String result = getUrlResult("http://opsfree2.corp.taobao.com:9999/api/v2.1/products/dumptree?_username=droid/csp&notree=1&nodegroup="
				+ nodegroup);
			return analyseK2Json(result);
		}
		return new HashMap<String, HostPo>();
	}

	public synchronized Map<String, List<HostPo>> getHostMapNoCache(String opsFiled, String opsName) {
		List<HostPo> list = getHostListNoCache(opsFiled, opsName);

		Map<String, List<HostPo>> map = new HashMap<String, List<HostPo>>();
		for (HostPo po : list) {

			List<HostPo> hList = map.get(po.getHostSite());
			if (hList == null) {
				hList = new ArrayList<HostPo>();
				map.put(po.getHostSite(), hList);
			}
			hList.add(po);

		}
		return map;
	}

	private Map<String, HostPo> analyseK2Json(String result) {

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
									//String nodegroup_name = detail.getString("nodegroup_name");
									JSONArray child = (JSONArray) p.get("child");
	
									for (int c = 0; c < child.size(); c++) {
										try {
											JSONObject childNode = (JSONObject) child.get(c);
											String dns_ip = childNode.getString("dns_ip");
											
											try{
												String nodename = childNode.optString("nodename");
												String site  = childNode.optString("site");
												
												String device_type = childNode.optString("device_type");
												String state = childNode.optString("state");
												
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
												log.error("unknown "+dns_ip+" site", e);
											}
										} catch (Exception e) {
											log.error("", e);
										}
									}
								}
							}
						}
					}
				}
			}
		}catch (Exception e) {
			log.error("",e);
		}catch(OutOfMemoryError e1){
			log.error("",e1);
		}
		return map;
	}

	/**
	 * 取得机器列表，不通过缓存
	 * 
	 * @param opsName
	 * @return
	 */
	public synchronized List<HostPo> getHostListNoCache(String opsName) {
		String url = "http://opsfree2.corp.taobao.com:9999/api/v2.1/products/dumptree?_username=droid/csp&notree=1&leafname="
				+ opsName;
		String result = getUrlResult(url);
		List<HostPo> list = new ArrayList<HostPo>();
		if(result !=null){
			Map<String, HostPo> map = analyseK2Json(result);
			list.addAll(map.values());
		}
		return list;
	}

	/**
	 * 取得机器列表，不通过缓存
	 * 
	 * @param opsName
	 * @return
	 */
	public synchronized List<HostPo> getHostListNoCache(String opsFiled, String opsName) {
		
		if("module_name".equals(opsFiled)){
			return getHostListNoCache(opsName);
		}
		List<HostPo> list = new ArrayList<HostPo>();
				
		String result = getUrlResult("http://opsfree2.corp.taobao.com:9999/api/v2.1/node/search?_username=droid/csp&e=1&n=0&q="
				+ opsFiled + "==" + opsName + "");
		if(result !=null){
			Map<String, HostPo> map = analyseK2Json(result);
			list.addAll(map.values());
		}
		
		return list;
	}
	
	
	
	
	
	
	public HostPo getHostInfoByNodeName(String nodename){
		String result = getUrlResult("http://opsfree2.corp.taobao.com:9999/api/v2.1/node/search?_username=droid/csp&e=1&n=0&q=nodename==" + nodename + "");
		if(result !=null){
			JSONArray object = JSONArray.fromObject(result);
			if (object.size() > 0) {
				Object obj = object.get(0);
				if (obj instanceof JSONObject) {
					JSONObject json = (JSONObject) obj;
					String nodegroup = json.getString("nodegroup");
					String dns_ip = json.getString("dns_ip");
					useGroupNameFindThisGroupHost(nodegroup);
					
					return hostIPMap.get(dns_ip);
				}
			}
		}
		return null;
	}
	
	

	public synchronized Set<String> getIpSetNoCache(String opsFiled, String opsName) {
		
		if("module_name".equals(opsFiled)){
			List<HostPo> list = getHostListNoCache(opsName);
			Set<String> set = new HashSet<String>();
			for(HostPo po:list){
				set.add(po.getHostIp());
			}
			return set;
		}
		
		
		String result = getUrlResult("http://opsfree2.corp.taobao.com:9999/api/v2.1/node/search?_username=droid/csp&e=1&n=0&q="
				+ opsFiled + "==" + opsName + "");
		Map<String, HostPo> map = analyseK2Json(result);
		return map.keySet();
	}

	/**
	 * 获取应用在虚拟机和实体机的数量
	 * 
	 * @param opsName
	 * @return int[0] 实体机数量 int[1] 虚拟机数量
	 */
	public synchronized int[] getHostType(String opsFiled, String opsName) {
		List<HostPo> list = this.getHostListNoCache(opsFiled, opsName);
		int v = 0;
		int s = 0;
		for (HostPo po : list) {
			if ("vm".equals(po.getHostType())) {
				s++;
			} else {
				v++;
			}
		}
		return new int[] { s, v };
	}

	/**
	 * 获取应用在虚拟机和实体机的数量
	 * 
	 * @param opsName
	 * @return int[0] 实体机数量 int[1] 虚拟机数量
	 */
	public synchronized int[] getHostType(String opsName) {
		List<HostPo> list = getHostListNoCache(opsName);
		int v = 0;
		int s = 0;
		for (HostPo po : list) {
			if ("vm".equals(po.getHostType())) {
				s++;
			} else {
				v++;
			}
		}
		return new int[] { s, v };
	}

	public Set<String> getAllOpsApp() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String md5 = DigestUtils.md5Hex("taobao_daily"+sdf.format(new Date()));
		String url = "http://proxy.wf.taobao.org/DailyManage/tree-xml.ashx?sign="+md5;
		
		Set<String> opsApp = new HashSet<String>();
		
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read(new URL(url));
			List<Node> fristNodeList = doc.selectNodes("/taobao/node/node");
			for(int i=0;i<fristNodeList.size()&&i<1;i++){
				Element fristNode = (Element)fristNodeList.get(i);//第一个节点  产品线					
				String name = fristNode.attributeValue("name");
				String fristid = fristNode.attributeValue("id");
				
				//rootNode.getChildren().add(fristOpsTreeNode);
				List<Element> secondNodeList = (List<Element>)fristNode.selectNodes("node");
				
				for(Element secondNode:secondNodeList){
					String secondname = secondNode.attributeValue("name");//第二节点 开发组
					String secondnid = secondNode.attributeValue("id");
					List<Element> thirdNodeList = (List<Element>)secondNode.selectNodes("node");
					
					for(Element thirdNode:thirdNodeList){
						String thirdname = thirdNode.attributeValue("name");//第三节点 产品线		
						String thirdid = thirdNode.attributeValue("id");
						List<Element> fourNodeList = (List<Element>)thirdNode.selectNodes("node");
						
						
						for(Element fourNode:fourNodeList){
							String fourname = fourNode.attributeValue("name");//第四节点 应用
							String fourid = fourNode.attributeValue("id");
							if(fourname.indexOf("it专用") >-1){
								continue;
							}
							opsApp.add(name+"->"+secondname+"->"+thirdname+","+fourname);
						}
						
					}
					
				}
			
		}
			
		} catch (MalformedURLException e) {
			log.error("",e);
		} catch (DocumentException e) {
			log.error("",e);
		}
		
		return opsApp;

	}

	private String getUrlResult(String path) {
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
				
				if(sb.length() >1000000){
					throw new Exception("接收到的数据太大:"+path);
				}
				
				
			}
			
			return sb.toString();
		} catch (Exception e) {
			log.error("http 请求url 出错:" + path, e);
		}
		return null;
	}

	public static void main(String[] args) {
		
		OpsFreeHostCache cache = OpsFreeHostCache.get();
		List<HostPo> list = cache.getHostListNoCache("detail");
		System.out.println(list.size());
		
	}

}
