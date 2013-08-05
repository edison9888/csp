
package com.taobao.monitor.stable;

import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;

/**
 * 
 * @author xiaodu
 * @version 2011-6-16 ÉÏÎç09:28:07
 */
public class RetrievedMonitorThread implements Runnable{
	
	private Thread thread = null;
	
	private Vector<RetrieveEntry> eventSet = new Vector<RetrieveEntry>();
	
	private PackageClassLoader packageClassLoader = null;
	
	
	private static RetrievedMonitorThread retrievedMonitorThread = null;
	
	public static void startup(){
		synchronized (RetrievedMonitorThread.class) {
			if(retrievedMonitorThread ==null){
				retrievedMonitorThread = new RetrievedMonitorThread();
			}
		}
	}
	
	
	
	
	public RetrievedMonitorThread(){
		thread = new Thread(this);
		thread.setName("Thread - RetrievedMonitorListen");
		thread.setDaemon(false);
		thread.start();
	}
	
	public void init(){
		packageClassLoader = new PackageClassLoader("com.taobao.monitor.stable.evnet");
		List<Class<?>> classList = packageClassLoader.findPackageClass();
		if(classList!=null){
			for(Class<?> c:classList){
				if(c.isAnnotationPresent(Feature.class)){
					Feature feature = c.getAnnotation(Feature.class);
					try {
						Object obj = c.newInstance();
						if(obj instanceof RetrieveEvent){
							Class<?>[] classFeatures = feature.features();
							if(classFeatures !=null&&classFeatures.length >0){
								
								RetrieveFeature[] rf = new RetrieveFeature[classFeatures.length];
								int i=0;
								for(Class<?> cFeature:classFeatures){
									Object objFeature = cFeature.newInstance();
									rf[i++] = (RetrieveFeature)objFeature;
								}
								this.putEvent((RetrieveEvent)obj, rf);
							}
						}
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
					
				}
			}
		}
		
		
		
	}
	
	
	
	public void putEvent(RetrieveEvent event,RetrieveFeature ... features){
		RetrieveEntry entry = new RetrieveEntry();
		entry.setEvent(event);
		for(RetrieveFeature feature:features)
			entry.putFeature(feature);
		eventSet.add(entry);
	}
	
	

	@Override
	public void run() {
		init();
		
		while(true){
			
			System.out.println("run");
			
			try{
				List<AppInfoPo> list = AppInfoAo.get().findAllEffectiveAppInfo();
				for(AppInfoPo po:list){
					Iterator<RetrieveEntry> it = eventSet.iterator();
					while(it.hasNext()){
						RetrieveEntry entry = it.next();
						final List<RetrieveFeature> rList = entry.getFeatureList();
						
						entry.getEvent().retrieve(po.getAppId(),new RetrieveCallBack(){
							@Override
							public void call(int appId, String msg) {
								for(RetrieveFeature f:rList){
									f.doFeature(appId, msg);
								}
							}
							
						});
					}
				}
			}catch (Exception e) {
			}
			
			try {
				Thread.sleep(10*60*1000);
			} catch (InterruptedException e) {
			}
			
		}
	}
	
	
	
	
	private class RetrieveEntry{
		private RetrieveEvent event;
		private List<RetrieveFeature> featureList;
		public RetrieveEvent getEvent() {
			return event;
		}
		public void setEvent(RetrieveEvent event) {
			this.event = event;
		}
		public List<RetrieveFeature> getFeatureList() {
			return featureList;
		}
		public void setFeatureList(List<RetrieveFeature> featureList) {
			this.featureList = featureList;
		}
		
		
		public void putFeature(RetrieveFeature feature){
			if(featureList == null){
				featureList = new ArrayList<RetrieveFeature>();
			}
			featureList.add(feature);
		}
	}
	
	public static void main(String[] args){
		RetrievedMonitorThread.startup();
		
	}

}
