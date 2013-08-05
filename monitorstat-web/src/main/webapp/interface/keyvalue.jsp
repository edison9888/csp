
<%@page import="java.net.URLEncoder"%>
<%@page import="java.net.URLDecoder"%>
<%@page import="javax.activation.URLDataSource"%>
<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@page import="com.taobao.monitor.common.analyse.ReportContentInterface"%>
<%@page import="java.util.Map"%>
<%@page import="com.taobao.monitor.common.po.KeyPo"%>
<%@page import="java.util.concurrent.ConcurrentHashMap"%>
<%@page import="com.taobao.monitor.common.po.AppKeyRelation"%>
<%@page import="com.taobao.monitor.common.po.MonitorSite"%>
<%@page import="com.taobao.monitor.common.ao.center.KeyAo"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.common.ao.time.MonitorTimeAo"%>
<%@page import="com.taobao.monitor.common.po.MonitorDetail"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>

<%
try{
	request.setCharacterEncoding("gbk");
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	//http://10.13.33.136:8080/monitorstat/interface/keyvalue.jsp?appId=86&keyName=OTHER_模本渲染_xxxx_AVERAGEUSERTIMES&keyValue=10&collectTime=2011-01-07 12:12
			
	String appId = request.getParameter("appId");
	String key = URLDecoder.decode(request.getParameter("keyName"));
	String value = request.getParameter("keyValue");
	String collectTime = request.getParameter("collectTime");//yyyy-MM-dd HH:mm
	//putReportDataEx(int appId, String hostsite, String keyName, String valueData,String message, String collectTime)
	ReportContent.get().putReportDataEx(Integer.parseInt(appId),"前端性能监控",key,value,null,collectTime);
	response.getWriter().print("ok");
}catch(Exception e){
	response.getWriter().print("fail");
}
%>

<%!

public static class ReportContent implements ReportContentInterface{
	private static ReportContent reportContent = new ReportContent();
	private Map<String,KeyPo> keyCacheMap =new ConcurrentHashMap<String, KeyPo>();	
	private Map<String,AppKeyRelation> appKeyRelation = new ConcurrentHashMap<String, AppKeyRelation>();
	private Map<String,MonitorSite> siteCacheMap =new ConcurrentHashMap<String, MonitorSite>();
	
	private ReportContent(){		
		List<KeyPo> keyPoList = KeyAo.get().findAllKeyInfo();
		for(KeyPo key:keyPoList){
			keyCacheMap.put(key.getKeyName(), key);
		}		
		List<AppKeyRelation> realtionList =AppInfoAo.get().findAllAppKeyRelation();
		for(AppKeyRelation key:realtionList){
			appKeyRelation.put(key.getAppId()+"-"+key.getKeyId(), key);
		}
		
		List<MonitorSite> siteList =MonitorTimeAo.get().findAllMonitorSite();
		for(MonitorSite key:siteList){
			siteCacheMap.put(key.getSiteName(), key);
		}
		
	}
		
	private KeyPo getKeyInfo(String keyName){
		
		KeyPo keyPo = keyCacheMap.get(keyName);
		if(keyPo == null){			
			synchronized (keyCacheMap) {				
				keyPo = keyCacheMap.get(keyName);
				if(keyPo ==null ){
					KeyPo tmp = KeyAo.get().getKeyByName(keyName);
					if(tmp == null){
						keyPo = createNewKey(keyName);
						return keyPo;
					}else{
						keyCacheMap.put(keyName, tmp);
						return tmp;
					}
				}else{
					return keyPo;
				}
			}
		}
		return keyPo;		
	}
	
	
	private AppKeyRelation getAppKeyRelation(int appId,int keyId){
		String key = appId+"-"+keyId;
		AppKeyRelation relation = appKeyRelation.get(key);
		if(relation == null){
			synchronized (appKeyRelation) {
				relation = appKeyRelation.get(key);
				if(relation == null){
					relation = createAppKeyRelation(appId,keyId);
					appKeyRelation.put(key, relation);
					return relation;
				}else{
					return relation;
				}
			}
		}else{
			return relation;
		}		
	}
	
	
	private MonitorSite getMonitorSite(String key){
		MonitorSite monitorSite = siteCacheMap.get(key);
		if(monitorSite == null){
			synchronized (siteCacheMap) {
				monitorSite = siteCacheMap.get(key);
				if(monitorSite == null){
					monitorSite = createMonitorSite(key);
					siteCacheMap.put(key, monitorSite);
					return monitorSite;
				}else{
					return monitorSite;
				}
			}
		}else{
			return monitorSite;
		}
		
		
	}
	
	private MonitorSite createMonitorSite(String key){
		return MonitorTimeAo.get().addMonitorSite(key);
	}
	
	
	private AppKeyRelation createAppKeyRelation(int appId,int keyId){
		
		
		AppKeyRelation app = new AppKeyRelation();
		app.setAppId(appId);
		app.setKeyId(keyId);
		return AppInfoAo.get().addAppKeyRelation(app);
	}
	
	private KeyPo createNewKey(String keyName){
		return KeyAo.get().addMonitorKey(keyName);
	}
		
	public static ReportContent get(){
		return reportContent;
	}	
	
	public void putReportDataEx(int appId, String hostsite, String keyName, String valueData,String message, String collectTime) {
		KeyPo po = getKeyInfo(keyName);
		if(po != null){
			AppKeyRelation relation = getAppKeyRelation(appId,po.getKeyId());
			if(relation != null){
				MonitorSite site = getMonitorSite(hostsite);
				MonitorDetail detail = new MonitorDetail();
				detail.setAppId(appId);
				detail.setKeyId(po.getKeyId());
				detail.setCollectTime(collectTime);
				detail.setKeyName(keyName);
				detail.setValueData(valueData);
				detail.setMonitorDesc(message);
				detail.setSiteId(site.getId());
				//System.out.println("appId:"+appId+"keyName:"+keyName+"collectTime:"+collectTime+"site.getId():"+site.getId()+"valueData:"+valueData);
				MonitorTimeAo.get().addMonitorData(detail);
			}			
		}
		
	}

	public void putReportLimit(int appId, String hostsite, String keyName, String valueData, String collectTime) {
		KeyPo po = getKeyInfo(keyName);
		if(po != null){
			AppKeyRelation relation = getAppKeyRelation(appId,po.getKeyId());
			if(relation != null){
				MonitorSite site = getMonitorSite(hostsite);
				MonitorDetail detail = new MonitorDetail();
				detail.setAppId(appId);
				detail.setKeyId(po.getKeyId());
				detail.setCollectTime(collectTime);
				detail.setKeyName(keyName);
				detail.setValueData(valueData);
				detail.setSiteId(site.getId());
				MonitorTimeAo.get().addMonitorDatalimit(detail);
			}			
		}
	}
	
	
	

}
%>