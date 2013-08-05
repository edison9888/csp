
/**
 * monitorstat-common
 */
package com.taobao.monitor.common.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpConnectionParams;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.taobao.monitor.common.ao.center.BeiDouAlertAo;
import com.taobao.monitor.common.ao.center.HostAo;
import com.taobao.monitor.common.po.DbHostGroup;
import com.taobao.monitor.common.po.HostPo;

/**
 * 
 * opsfree-api.corp.taobao.com ����������ϻ�����
opsfree2.corp.taobao.com:9999 ������ճ�����
 * 
 * @author xiaodu
 *
 * ����6:23:01
 */
public class CspSyncOpsHostInfos implements Runnable{
	
	
	private static Logger logger = Logger.getLogger(CspSyncOpsHostInfos.class);
	
	private static CspSyncOpsHostInfos sync = new CspSyncOpsHostInfos();

	private static boolean running = false;
	
	public static boolean isOnLine=true;
	
	private final static String domains[]=new String[]{"http://opsdbtest.ops.aliyun-inc.com:8888","http://a.alibaba-inc.com"};
	private static HttpClient client =null;
	static{
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(
		        new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		ClientConnectionManager cm = new ThreadSafeClientConnManager(schemeRegistry);
		client = new DefaultHttpClient(cm);
		client.getParams().setParameter(  
                HttpConnectionParams.CONNECTION_TIMEOUT, 3000);  
		
		client.getParams().setParameter(ClientPNames.COOKIE_POLICY,
	            CookiePolicy.IGNORE_COOKIES); 
	}
	private CspSyncOpsHostInfos(){
		
	}
	
	public static void startupSyncThread(){
		synchronized (CspSyncOpsHostInfos.class) {
			if(!running){
				Thread thread = new Thread(sync);
				thread.setName("csp-sync-opsfree-thread");
				thread.setDaemon(false);
				thread.start();
				running = true;
			}
		}
	}
	
	public static void immediatelySync(){
		sync.sync();
	}
	
	private void sync(){
	  //ͬ��Beidou��host����ͬ������������Ϊkey
    Map<String,DbHostGroup> dbHostMap = new HashMap<String, DbHostGroup>();
    logger.info("��ʼͬ������Alert����Ϣ��" + new Date());
    try {
      List<DbHostGroup> list = BeiDouAlertAo.getBeiDouAlertAo()
          .findHostGroupList();
      for (DbHostGroup po : list) {
        dbHostMap.put(po.getMemberName(), po);
      }
    } catch (Exception e) {
      logger.error("syncDbHostGroupͬ�������쳣->",e);
    }
    logger.info("����Alert����Ϣͬ����ϣ�����ͬ������:" + dbHostMap.size() + "��");
	  
		int sync = HostAo.get().isSync();
		if(sync == 1){//��ʾ������
			logger.info("===����ͬ��===");
			return;
		}
		HostAo.get().addSync(1);
		int version = HostAo.get().getSyncOpsVersion();
		if(version == -1){
			return ;
		}
		int nextVersion = version+1;
		try{
			Set<String> list = getAllOpsAppName();

			Map<String, Integer> parentMap=new HashMap<String, Integer>(6000);
			if(list.size() >0){
				for(String name:list){
					logger.info("��ʼͬ��Ӧ��:"+name);
					//System.out.println(name);
					Map<String, HostPo> map = findHostListInOpsfreeByOpsName(name);
					
					if(map.size() >0){
						for(Map.Entry<String, HostPo> entry:map.entrySet()){
						  HostPo po = entry.getValue();
						  HostPo hostParent=null;
							//�����������Ļ���Ҫȥ��ȡ��ʵ���,���鵽ops_xen
							if(po.isVirtualHost() && !parentMap.containsKey(po.getVmparent())){
								hostParent=findHostInfoFromOpsByName(po.getVmparent(),"ops_xen");
							}
						  
						  if(OpsFilter.filterDb(po.getOpsName())) { //�����mysql_other֮������ݣ�Ҫ������group_name��ʾ
						    if(po.getHostName() != null && dbHostMap.containsKey(po.getHostName())) {
						      po.setOpsName(po.getOpsName() + "_" + dbHostMap.get(po.getHostName()).getGroupName());						      
						    }
						  }
						  
							if(!HostAo.get().addSyncOpsHostInfo(entry.getValue(),nextVersion)){
								throw new Exception();
							}
							if(hostParent!=null){
								if(!HostAo.get().addSyncOpsHostInfo(hostParent,nextVersion)){
									throw new Exception();
								}
								parentMap.put(po.getVmparent(), 1);
							}
							
						}
					}else{
						List<HostPo> tmp= HostAo.get().findSyncHostInfos(name,version);
						for(HostPo po:tmp){
							
							if(!HostAo.get().addSyncOpsHostInfo(po,nextVersion)){
								throw new Exception();
							}
						}
					}
				}
				HostAo.get().updateSyncVersion(nextVersion);
				logger.info("ɾ���ϰ汾Ӧ��:");

				HostAo.get().deleteSyncOldVersion(version);
			}
		}catch (Exception e) {
			logger.error("����ͬ���쳣", e);
			HostAo.get().deleteSyncOldVersion(nextVersion);
		}finally{
			HostAo.get().addSync(0);
		}

		logger.info("ͬ�����");
	}
	
	
	public static Map<String, HostPo> findHostsByOpsName(String opsName){
		return sync.findHostListInOpsfreeByOpsName(opsName);
	}
	
	public static List<HostPo> findOnlineHostByOpsName(String opsName) {
		Map<String, HostPo> allHost = findHostsByOpsName(opsName);
		List<HostPo> onlineHost = new ArrayList<HostPo>();
		
		if (allHost == null) return onlineHost;
		for (HostPo po : allHost.values()) {
			if (po.getState().equalsIgnoreCase("working_online")) {
				onlineHost.add(po);
			}
		}
		return onlineHost;
	}
	
	
	private  Map<String, HostPo> findHostListInOpsfreeByOpsName(String opsName) {
		try {
			opsName=URLEncoder.encode(opsName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.info("encode exception",e);
		}
		String url = getDomain()+"/page/api/free/product/dumptree.htm?notree=1&appname="+opsName+"&_username=droid/csp"	;
		String result = getUrlResultNew(url);
		return analyseK2Json(result,opsName);
	}
	//���ݻ��������������Ϣ
	private HostPo findHostInfoFromOpsByName(String hostName,String opsName){
		try {
			opsName=URLEncoder.encode(opsName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.info("encode exception",e);
		}
		String url=getDomain()+"/page/api/free/opsfreeInterface/search.htm?q=nodename=="+hostName+"&_username=droid/csp";
		String result = getUrlResultNew(url);
		JSONObject info = JSONObject.fromObject(result);
		
		if(info.optInt("num")!=1){
			logger.error("���صļ�¼������ȷ��" + result);
		}
		return createHostObjFromJson(info.getJSONArray("result").getJSONObject(0),opsName);
	}
	
	protected static String getUrlResultNew(String requestUrl){
	
		try {
			HttpGet httpGet = new HttpGet(requestUrl);
			HttpResponse response1 = client.execute(httpGet);
			String result=org.apache.http.util.EntityUtils.toString(response1.getEntity());
			return result;
		} catch (Exception e) {
			logger.error("http ����url ����:" + requestUrl, e);
		}
		return null;
		
		
	}
	
	private Map<String, HostPo> analyseK2Json(String result,String moduleName) {

		Map<String, HostPo> map = new HashMap<String, HostPo>();
		if(result==null){
			return map;
		}
		try{
			JSONArray info = JSONArray.fromObject(result);
			if(info.size()==0){
				return map;
			}
			JSONObject object = info.getJSONObject(0);
			for(Object obj:object.values())
				if (obj instanceof JSONArray) {
					JSONArray json = (JSONArray) obj;
					for (int ch = 0; ch < json.size(); ch++){
						Object tmp = json.get(ch);
						if(tmp instanceof JSONObject){
							JSONObject child = (JSONObject)tmp;
							JSONArray array = child.getJSONArray("child");
							for(int i=0;i<array.size();i++){
								JSONObject hostObj = array.getJSONObject(i);
								HostPo host=createHostObjFromJson(hostObj,moduleName);
								
								if(host!=null && !host.getHostIp().equals(""))
									map.put(host.getHostIp(), host);
							}
						}
					}
			}
		}catch (Exception e) {
			logger.error(moduleName + "error!");
			logger.error(e);
		}catch(OutOfMemoryError e1){
			logger.error("",e1);
		}
		return map;
	} 
	
	
	private HostPo createHostObjFromJson(JSONObject hostObj,String ops_name ){
		try{
			String dns_ip = hostObj.getString("dns_ip");
			String nodename = hostObj.optString("nodename");
			String site  = hostObj.optString("site");
			
			String manifest = hostObj.optString(".manifest");
			String state = hostObj.optString("state");
			
			String vmparent = hostObj.optString("vmparent");
			String description = hostObj.optString("description");
			String model = hostObj.optString("model");
			String hdrs_chassis = hostObj.optString("hdrs_chassis");
			String rack = hostObj.optString("rack");
			String nodegroup = hostObj.optString("nodegroup");
			
			HostPo host = new HostPo();
			host.setHostSite(site);
			host.setHostType(model);
			host.setOpsName(ops_name);
			host.setHostIp(dns_ip);
			host.setHostName(nodename);
			host.setDescription(description);
			host.setHdrs_chassis(hdrs_chassis);
			host.setNodeGroup(nodegroup);
			host.setRack(rack);
			host.setVmparent(vmparent);
			host.setState(state);
			host.setManifest(manifest);
			
			return host;
		}catch (Exception e) {
			logger.error("parse result as hostpo exception",e);
		}
		return null;
	}
	
	/**
	 * ��pesystem �Ǳ߻�ȡȫ���� ��Ʒ�б�
	 * @return
	 */
	private Set<String> getAllOpsAppName() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String md5 = DigestUtils.md5Hex("taobao_daily"+sdf.format(new Date()));
		String url = "http://proxy.wf.taobao.org/DailyManage/tree-xml.ashx?sign="+md5;
		
		Set<String> opsApp = new HashSet<String>();
		
		SAXReader reader = new SAXReader();
		try {
			Document doc = reader.read(new URL(url));
			List<Node> fristNodeList = doc.selectNodes("/taobao/node/node");
			for(int i=0;i<fristNodeList.size()&&i<1;i++){
				Element fristNode = (Element)fristNodeList.get(i);//��һ���ڵ�  ��Ʒ��					
				String name = fristNode.attributeValue("name");
				String fristid = fristNode.attributeValue("id");
				
				List<Element> secondNodeList = (List<Element>)fristNode.selectNodes("node");
				
				for(Element secondNode:secondNodeList){
					String secondname = secondNode.attributeValue("name");//�ڶ��ڵ� ������
					String secondnid = secondNode.attributeValue("id");
					List<Element> thirdNodeList = (List<Element>)secondNode.selectNodes("node");
					
					for(Element thirdNode:thirdNodeList){
						String thirdname = thirdNode.attributeValue("name");//�����ڵ� ��Ʒ��		
						String thirdid = thirdNode.attributeValue("id");
						List<Element> fourNodeList = (List<Element>)thirdNode.selectNodes("node");
						
						
						for(Element fourNode:fourNodeList){
							String fourname = fourNode.attributeValue("name");//���Ľڵ� Ӧ��
							String fourid = fourNode.attributeValue("id");
							opsApp.add(fourname);
						}
					}
				}
			}
			
			Element fristNode = (Element)fristNodeList.get(1);//��һ���ڵ�  ��Ʒ��	
			if (fristNode.attributeValue("name").equals("��������")) {
				List<Element> secondNodeList = (List<Element>)fristNode.selectNodes("node");
				for(Element secondNode : secondNodeList){
					if (secondNode.attributeValue("name").equals("����ϵͳ") || secondNode.attributeValue("name").equals("DB")) {
						List<Element> thirdNodeList = (List<Element>)secondNode.selectNodes("node");
						for(Element thirdNode:thirdNodeList){
							List<Element> fourNodeList = (List<Element>)thirdNode.selectNodes("node");
							for(Element fourNode:fourNodeList){
								String fourname = fourNode.attributeValue("name");//���Ľڵ� Ӧ��
								String fourid = fourNode.attributeValue("id");
								opsApp.add(fourname);
							}
						}
					}
				}
			}
			
			
		} catch (MalformedURLException e) {
			logger.error("",e);
			e.printStackTrace();
		} catch (DocumentException e) {
			logger.error("",e);
			e.printStackTrace();
		}
		
		return opsApp;

	}
	
	private String getDomain(){
		if(isOnLine){
			return domains[1];
		}
		return domains[0];
	}
	
	public void run() {
		
//		while(true){
//			
//			Calendar cal = Calendar.getInstance();
//			cal.add(Calendar.DAY_OF_MONTH, 1);
//			cal.set(Calendar.HOUR_OF_DAY, 1);
//			long time = cal.getTimeInMillis();
//			
//			long wait = time-System.currentTimeMillis();
//			if(wait <0){
//				wait = 60*24*1000l;
//			}
//			
//			logger.info("ִ�л���ͬ����Ҫ�ȴ�ʱ��Ϊ:"+wait/(60*1000)+"����");
//			try {
//				Thread.sleep(wait);//24Сʱͬ��һ��
//			} catch (InterruptedException e) {
//			}
//			sync();
//			
//		}
		
	}
	
	public static void main(String[] str) throws UnsupportedEncodingException {
		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//		System.out.println(DigestUtils.md5Hex(sdf.format(new Date())+ "taobao_daily"));;
		CspSyncOpsHostInfos.isOnLine=false;
//		
//		CspSyncOpsHostInfos.immediatelySync();
		
//		System.out.println(CspSyncOpsHostInfos.findOnlineHostByOpsName("etao_mtair").size());
	
		CspSyncOpsHostInfos.immediatelySync();
		
		//���ĵ�url
		//String utls="http://opsdbtest.ops.aliyun-inc.com:8888/page/api/free/product/dumptree.htm?notree=1&leafname=������&_username=droid/csp";
		//System.out.println(getUrlResult(utls));
		//System.out.println(getUrlResultNew(utls));

		//�пո�ģ�ͳһ��encodeһ��
//		String url="http://a.alibaba-inc.com/page/api/free/product/dumptree.htm?notree=1&leafname=etao_message_center &_username=droid/csp";
//		System.out.println(getUrlResultNew(url));
//		
//		String p1="etao_message_center ";
//		p1=URLEncoder.encode(p1, "utf-8");
//		String url2="http://a.alibaba-inc.com/page/api/free/product/dumptree.htm?notree=1&leafname="+p1+"&_username=droid/csp";
//		System.out.println(getUrlResultNew(url2));
	}
	

}
