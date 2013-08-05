
/**
 * monitorstat-time-monitor
 */
package com.taobao.csp.monitor.assist;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import com.taobao.csp.common.ZKClient;

/**
 * @author xiaodu
 *
 * ÏÂÎç9:01:53
 */
public class GroupServiceAssist implements ServiceAssist{
	
	private static final Logger logger =  Logger.getLogger("zookeeper");
	
	public static String ZK_MONITOR_APP_ROOT = "/csp/monitor/app";
	
	public static String ZK_MONITOR_GROUP_ROOT="/csp/monitor/groupmakerlist";
	
	private String groupName = null;
	
	private String collectorIp = null;
	
	
	private CollectorListen collectorCallBack = null;
	
	private AppListen appCallBack = null;
	
	public GroupServiceAssist(String groupName,String ip){
		this.groupName = groupName;
		this.collectorIp = ip;
		
		
		ZKClient.get().addReconnectCallBack(new ZKClient.ReconnectCallBack() {
			@Override
			public void doHandle() {
				 watcherMonitorApps();
				 watcherApp();
				 watcherGroup();
			}
		});
		
	}

	public List<String> findGroupCollectors() {
		return ZKClient.get().list(ZK_MONITOR_GROUP_ROOT+"/"+groupName);
	}

	public List<String> findApps() {
		return ZKClient.get().list(ZK_MONITOR_APP_ROOT);
	}

	public void registerCollector(){
		ZKClient.get().mkdirPersistent(ZK_MONITOR_GROUP_ROOT+"/"+groupName+"/"+this.collectorIp);
		
	}


	@Override
	public void heartbeat(Object obj) {
		ZKClient.get().setData(ZK_MONITOR_GROUP_ROOT+"/"+groupName+"/"+this.collectorIp, obj);
	}
	
	
	private List<String> watcherGroup() {
		return ZKClient.get().list(ZK_MONITOR_GROUP_ROOT+"/"+groupName,new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				
				switch (event.getType()) {
				case None:
					
					break;
				case NodeCreated:
					
					break;
				case NodeDeleted:
					
					break;
				case NodeDataChanged:
					
					break;
				case NodeChildrenChanged:
					
					logger.info(ZK_MONITOR_GROUP_ROOT+"/"+groupName+" watcherGroup NodeChildrenChanged  ...");
					
					List<String> collector = watcherGroup();
					if(collectorCallBack!=null){
						collectorCallBack.collectorChange(collector);
					}
					break;
				default:
					break;
				}
				
			}
		});
	}
	
	
	private List<String> watcherMonitorApps() {
		return ZKClient.get().list(ZK_MONITOR_APP_ROOT,new Watcher() {
			
			@Override
			public void process(WatchedEvent event) {
				switch (event.getType()) {
				case None:
					
					break;
				case NodeCreated:
					
					break;
				case NodeDeleted:
					
					break;
				case NodeDataChanged:
					
					break;
				case NodeChildrenChanged:
					
					logger.info("watcherMonitorApps NodeChildrenChanged  ...");
					
					List<String> apps = watcherMonitorApps();
					if(appCallBack!=null){
						appCallBack.appChange(apps);
					}
					break;
				default:
					break;
				}
			}
		});
	}
	
	private Object watcherApp(){
		return ZKClient.get().getData(ZK_MONITOR_GROUP_ROOT, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				switch (event.getType()) {
				case None:
					
					break;
				case NodeCreated:
					
					break;
				case NodeDeleted:
					
					break;
				case NodeDataChanged:
					
					logger.info("watcherApp NodeDataChanged ....");
					
					Map<String,String> obj =(Map) watcherApp();
					if(appCallBack!=null){
						String action = obj.get("action");
						if("delete".equals(action)){
							appCallBack.appDelete(obj.get("appName"));
							logger.info("watcherApp NodeDataChanged delete "+obj.get("appName"));
						}
						if("add".equals(action)){
							appCallBack.appAdd(obj.get("appName"));
							logger.info("watcherApp NodeDataChanged add "+obj.get("appName"));
						}
					}
					break;
				case NodeChildrenChanged:
				
					break;
				default:
					break;
				}
			}
		});
	}

	@Override
	public synchronized  void addCollectorListen(CollectorListen listen) {
		if(listen !=null){
			collectorCallBack = listen;
			watcherGroup();
		}
	}

	@Override
	public synchronized void addAppListen(AppListen appLisen) {
		if(appLisen != null){
			appCallBack = appLisen;
			 watcherMonitorApps();
			 watcherApp();
		}
	}

	@Override
	public void logoutCollector() {
		ZKClient.get().delete(ZK_MONITOR_GROUP_ROOT+"/"+groupName+"/"+this.collectorIp);
		ZKClient.get().close();
		
		logger.info("GroupServiceAssist logoutCollector....");
	}
}
